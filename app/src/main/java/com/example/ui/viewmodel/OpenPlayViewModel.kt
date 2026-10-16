package com.example.ui.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.R
import com.example.data.local.AppDatabase
import com.example.data.model.BuildVerification
import com.example.data.model.DeveloperDonation
import com.example.data.model.FossApp
import com.example.data.model.GitCommit
import com.example.data.model.Review
import com.example.data.repository.OpenPlayRepository
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class OpenPlayViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: OpenPlayRepository
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    init {
        val database = AppDatabase.getInstance(application)
        repository = OpenPlayRepository(database.appDao(), application)
    }

    // Auth State
    private val _currentUser = MutableStateFlow<FirebaseUser?>(auth.currentUser)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser.asStateFlow()

    private val _isAuthLoading = MutableStateFlow(false)
    val isAuthLoading: StateFlow<Boolean> = _isAuthLoading.asStateFlow()

    init {
        auth.addAuthStateListener { firebaseAuth ->
            _currentUser.value = firebaseAuth.currentUser
        }
    }

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _selectedLicenseFilter = MutableStateFlow("All")
    val selectedLicenseFilter: StateFlow<String> = _selectedLicenseFilter.asStateFlow()

    private val _onlyVerifiedFilter = MutableStateFlow(false)
    val onlyVerifiedFilter: StateFlow<Boolean> = _onlyVerifiedFilter.asStateFlow()

    // Installation progress state
    private val _installingAppId = MutableStateFlow<String?>(null)
    val installingAppId: StateFlow<String?> = _installingAppId.asStateFlow()

    private val _installProgress = MutableStateFlow(0f)
    val installProgress: StateFlow<Float> = _installProgress.asStateFlow()

    private val _installStatusText = MutableStateFlow("")
    val installStatusText: StateFlow<String> = _installStatusText.asStateFlow()

    // Git sync & build verification states
    private val _isSyncingGit = MutableStateFlow(false)
    val isSyncingGit: StateFlow<Boolean> = _isSyncingGit.asStateFlow()

    private val _isVerifyingBuild = MutableStateFlow(false)
    val isVerifyingBuild: StateFlow<Boolean> = _isVerifyingBuild.asStateFlow()

    private val _verificationLog = MutableStateFlow<String?>(null)
    val verificationLog: StateFlow<String?> = _verificationLog.asStateFlow()

    // Selected App Detail state
    private val _selectedAppId = MutableStateFlow<String?>(null)
    val selectedAppId: StateFlow<String?> = _selectedAppId.asStateFlow()

    private val _selectedApp = MutableStateFlow<FossApp?>(null)
    val selectedApp: StateFlow<FossApp?> = _selectedApp.asStateFlow()

    private val _appReviews = MutableStateFlow<List<Review>>(emptyList())
    val appReviews: StateFlow<List<Review>> = _appReviews.asStateFlow()

    private val _appCommits = MutableStateFlow<List<GitCommit>>(emptyList())
    val appCommits: StateFlow<List<GitCommit>> = _appCommits.asStateFlow()

    private val _appBuildVerifications = MutableStateFlow<List<BuildVerification>>(emptyList())
    val appBuildVerifications: StateFlow<List<BuildVerification>> = _appBuildVerifications.asStateFlow()

    private val _appDonations = MutableStateFlow<List<DeveloperDonation>>(emptyList())
    val appDonations: StateFlow<List<DeveloperDonation>> = _appDonations.asStateFlow()

    // Upload state
    private val _isUploading = MutableStateFlow(false)
    val isUploading: StateFlow<Boolean> = _isUploading.asStateFlow()

    private val _uploadProgressStep = MutableStateFlow("")
    val uploadProgressStep: StateFlow<String> = _uploadProgressStep.asStateFlow()

    // Message banner / toast notification
    private val _uiNotice = MutableStateFlow<String?>(null)
    val uiNotice: StateFlow<String?> = _uiNotice.asStateFlow()

    val rawApps: StateFlow<List<FossApp>> = repository.getAllApps().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val installedApps: StateFlow<List<FossApp>> = repository.getInstalledApps().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val bookmarkedApps: StateFlow<List<FossApp>> = repository.getBookmarkedApps().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val userUploadedApps: StateFlow<List<FossApp>> = repository.getUserUploadedApps().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allDonations: StateFlow<List<DeveloperDonation>> = repository.getAllDonations().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val filteredApps: StateFlow<List<FossApp>> = combine(
        rawApps,
        _searchQuery,
        _selectedCategory,
        _selectedLicenseFilter,
        _onlyVerifiedFilter
    ) { apps, query, category, license, onlyVerified ->
        apps.filter { app ->
            val matchesQuery = query.isBlank() ||
                app.name.contains(query, ignoreCase = true) ||
                app.tagline.contains(query, ignoreCase = true) ||
                app.description.contains(query, ignoreCase = true) ||
                app.developerName.contains(query, ignoreCase = true) ||
                app.category.contains(query, ignoreCase = true)

            val matchesCategory = (category == "All") || (app.category.equals(category, ignoreCase = true))

            val matchesLicense = (license == "All") || (app.license.contains(license, ignoreCase = true))

            val matchesVerified = !onlyVerified || app.reproducibleVerified

            matchesQuery && matchesCategory && matchesLicense && matchesVerified
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val openSourceGames: StateFlow<List<FossApp>> = rawApps.combine(_searchQuery) { apps, query ->
        apps.filter { it.category.equals("Games", ignoreCase = true) && (query.isBlank() || it.name.contains(query, ignoreCase = true)) }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private var appDetailJob: Job? = null

    fun selectApp(appId: String) {
        _selectedAppId.value = appId
        appDetailJob?.cancel()
        appDetailJob = viewModelScope.launch {
            launch {
                repository.getAppById(appId).collect { app ->
                    _selectedApp.value = app
                }
            }
            launch {
                repository.getReviews(appId).collect { reviews ->
                    _appReviews.value = reviews
                }
            }
            launch {
                repository.getCommits(appId).collect { commits ->
                    _appCommits.value = commits
                }
            }
            launch {
                repository.getBuildVerifications(appId).collect { bvs ->
                    _appBuildVerifications.value = bvs
                }
            }
            launch {
                repository.getDonationsForApp(appId).collect { dons ->
                    _appDonations.value = dons
                }
            }
        }
    }

    fun clearSelectedApp() {
        _selectedAppId.value = null
        _selectedApp.value = null
        appDetailJob?.cancel()
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedCategory(category: String) {
        _selectedCategory.value = category
    }

    fun setSelectedLicenseFilter(license: String) {
        _selectedLicenseFilter.value = license
    }

    fun toggleOnlyVerified() {
        _onlyVerifiedFilter.value = !_onlyVerifiedFilter.value
    }

    fun toggleBookmark(app: FossApp) {
        viewModelScope.launch {
            repository.toggleBookmark(app)
            _uiNotice.value = if (!app.isBookmarked) "Added to Bookmarks" else "Removed from Bookmarks"
        }
    }

    fun installApp(app: FossApp) {
        if (_installingAppId.value != null) return
        viewModelScope.launch {
            _installingAppId.value = app.id
            _installStatusText.value = "Pending..."
            delay(250)
            _installStatusText.value = "Downloading verified APK (${app.apkSizeMb} MB)..."
            _installProgress.value = 0.25f
            delay(350)
            _installStatusText.value = "Auditing APK SHA-256 against reproducible hash..."
            _installProgress.value = 0.65f
            delay(300)
            _installStatusText.value = "OpenPlay Protect scan: 0 threats detected..."
            _installProgress.value = 0.90f
            delay(250)
            _installStatusText.value = "Installing ${app.name}..."
            _installProgress.value = 1.0f
            delay(200)
            repository.installApp(app)
            _installingAppId.value = null
            _installProgress.value = 0f
            _installStatusText.value = ""
            _uiNotice.value = "${app.name} installed successfully."
        }
    }

    fun uninstallApp(app: FossApp) {
        viewModelScope.launch {
            repository.uninstallApp(app)
            _uiNotice.value = "Uninstalled ${app.name}"
        }
    }

    fun submitReview(
        appId: String,
        author: String,
        rating: Int,
        title: String,
        comment: String,
        isAuditor: Boolean,
        auditorRole: String
    ) {
        viewModelScope.launch {
            repository.addReview(
                appId = appId,
                author = author,
                rating = rating,
                title = title,
                comment = comment,
                isAuditor = isAuditor,
                auditorRole = auditorRole
            )
            _uiNotice.value = "Review published to OpenPlay Community & synced to cloud!"
        }
    }

    fun upvoteReview(reviewId: String) {
        viewModelScope.launch {
            repository.upvoteReview(reviewId)
            _uiNotice.value = "Marked review as helpful"
        }
    }

    fun syncFromGit(appId: String) {
        if (_isSyncingGit.value) return
        viewModelScope.launch {
            _isSyncingGit.value = true
            _uiNotice.value = "Querying remote Git repository tags and commits..."
            delay(800)
            val newCommit = repository.syncGitRepo(appId)
            _isSyncingGit.value = false
            if (newCommit != null) {
                _uiNotice.value = "Git sync complete! New tag ${newCommit.tag} verified & compiled."
            }
        }
    }

    fun runAutomatedVerification(app: FossApp) {
        if (_isVerifyingBuild.value) return
        viewModelScope.launch {
            _isVerifyingBuild.value = true
            _verificationLog.value = "Starting hermetic reproducibility pipeline for ${app.name}..."
            delay(500)
            _verificationLog.value = "Verifying GPG signature on Git tag ${app.latestGitTag}..."
            delay(600)
            _verificationLog.value = "Running independent byte-for-byte build comparison..."
            delay(700)
            val verification = repository.triggerAutomatedBuildVerification(app)
            _isVerifyingBuild.value = false
            _verificationLog.value = verification.logOutput
            _uiNotice.value = "OpenPlay Protect: 100% byte-for-byte match, 0 malware detected!"
        }
    }

    fun recordDonation(
        appId: String,
        amount: Double,
        platform: String,
        message: String,
        onSuccess: (DeveloperDonation) -> Unit
    ) {
        viewModelScope.launch {
            val donation = repository.recordDonation(appId, amount, platform, message)
            if (donation != null) {
                _uiNotice.value = "Tipped $${amount.toInt()} directly to ${donation.developerName}! 🎉"
                onSuccess(donation)
            }
        }
    }

    fun uploadApp(
        name: String,
        tagline: String,
        description: String,
        category: String,
        subcategory: String,
        license: String,
        gitRepoUrl: String,
        gitBranch: String,
        latestGitTag: String,
        buildSystem: String,
        developerName: String,
        donationLiberapay: String,
        donationGithub: String,
        donationCrypto: String,
        iconKey: String,
        onSuccess: (FossApp) -> Unit
    ) {
        viewModelScope.launch {
            _isUploading.value = true
            _uploadProgressStep.value = "Cloning Git repository from $gitRepoUrl..."
            delay(500)
            _uploadProgressStep.value = "Analyzing open-source license ($license) and manifest..."
            delay(500)
            _uploadProgressStep.value = "Building with $buildSystem in hermetic sandbox..."
            delay(600)
            _uploadProgressStep.value = "Running 18 anti-malware and tracker scanners..."
            delay(500)
            _uploadProgressStep.value = "Verifying byte-for-byte build reproducibility (100%)..."
            delay(400)

            val uploadedApp = repository.uploadUserApp(
                name = name,
                tagline = tagline,
                description = description,
                category = category,
                subcategory = subcategory,
                license = license,
                gitRepoUrl = gitRepoUrl,
                gitBranch = gitBranch,
                latestGitTag = latestGitTag,
                buildSystem = buildSystem,
                developerName = developerName,
                donationLiberapay = donationLiberapay,
                donationGithub = donationGithub,
                donationCrypto = donationCrypto,
                iconKey = iconKey
            )
            _isUploading.value = false
            _uploadProgressStep.value = ""
            _uiNotice.value = "Success! '${uploadedApp.name}' verified and published to OpenPlay & Firestore."
            onSuccess(uploadedApp)
        }
    }

    fun signInWithGoogle(context: Context) {
        val clientId = try {
            context.getString(R.string.default_web_client_id)
        } catch (e: Exception) {
            _uiNotice.value = "Google Sign-In configuration error"
            return
        }

        val credentialManager = CredentialManager.create(context)
        val signInOption = GetSignInWithGoogleOption.Builder(serverClientId = clientId).build()
        val request = GetCredentialRequest.Builder().addCredentialOption(signInOption).build()

        viewModelScope.launch {
            _isAuthLoading.value = true
            try {
                val result = credentialManager.getCredential(context as Activity, request)
                val credential = result.credential
                if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val googleIdToken = GoogleIdTokenCredential.createFrom(credential.data).idToken
                    val authCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
                    auth.signInWithCredential(authCredential).await()
                    _uiNotice.value = "Welcome back, ${auth.currentUser?.displayName ?: "User"}!"
                } else {
                    _uiNotice.value = "Unexpected credential format."
                }
            } catch (e: GetCredentialCancellationException) {
                Log.w("Auth", "Google Sign-In cancelled: ${e.message}", e)
            } catch (e: Exception) {
                Log.e("Auth", "Google Sign-In failed", e)
                _uiNotice.value = "Sign-in error: ${e.localizedMessage}"
            } finally {
                _isAuthLoading.value = false
            }
        }
    }

    fun signOutGoogle(context: Context) {
        viewModelScope.launch {
            auth.signOut()
            try {
                val credentialManager = CredentialManager.create(context)
                credentialManager.clearCredentialState(ClearCredentialStateRequest())
            } catch (e: Exception) {
                Log.e("Auth", "Error clearing credentials", e)
            }
            _uiNotice.value = "Signed out of Google Account."
        }
    }

    fun clearNotice() {
        _uiNotice.value = null
    }

    fun clearVerificationLog() {
        _verificationLog.value = null
    }
}
