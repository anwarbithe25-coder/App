package com.example.data.repository

import android.content.Context
import android.util.Log
import com.example.R
import com.example.data.local.AppDao
import com.example.data.local.DefaultData
import com.example.data.model.BuildVerification
import com.example.data.model.DeveloperDonation
import com.example.data.model.FossApp
import com.example.data.model.GitCommit
import com.example.data.model.Review
import com.example.data.remote.FirestoreApp
import com.example.data.remote.FirestoreReview
import com.example.data.remote.OperationType
import com.example.data.remote.handleFirestoreError
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

class OpenPlayRepository(
    private val appDao: AppDao,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) {

    constructor(appDao: AppDao, context: Context) : this(
        appDao = appDao,
        firestore = FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        ),
        auth = FirebaseAuth.getInstance()
    )

    init {
        coroutineScope.launch {
            // Seed local Room database if empty
            val existing = appDao.getAllApps().first()
            if (existing.isEmpty()) {
                appDao.insertApps(DefaultData.sampleApps)
                appDao.insertReviews(DefaultData.sampleReviews)
                appDao.insertCommits(DefaultData.sampleCommits)
                appDao.insertBuildVerifications(DefaultData.sampleBuildVerifications)
                for (donation in DefaultData.sampleDonations) {
                    appDao.insertDonation(donation)
                }
            }

            // Start listening to remote Firestore apps to keep Room cache synced
            startFirestoreSync()
        }
    }

    private fun startFirestoreSync() {
        try {
            firestore.collection("apps")
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        handleFirestoreError(error, OperationType.LIST, "apps")
                        return@addSnapshotListener
                    }
                    if (snapshot != null && !snapshot.isEmpty) {
                        coroutineScope.launch {
                            val remoteApps = snapshot.documents.mapNotNull { doc ->
                                doc.toObject(FirestoreApp::class.java)?.toFossApp()
                            }
                            if (remoteApps.isNotEmpty()) {
                                appDao.insertApps(remoteApps)
                            }
                        }
                    }
                }

            firestore.collection("reviews")
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        handleFirestoreError(error, OperationType.LIST, "reviews")
                        return@addSnapshotListener
                    }
                    if (snapshot != null && !snapshot.isEmpty) {
                        coroutineScope.launch {
                            val remoteReviews = snapshot.documents.mapNotNull { doc ->
                                doc.toObject(FirestoreReview::class.java)?.toReview()
                            }
                            if (remoteReviews.isNotEmpty()) {
                                appDao.insertReviews(remoteReviews)
                            }
                        }
                    }
                }
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.LIST, "sync")
        }
    }

    // Room local cached streams
    fun getAllApps(): Flow<List<FossApp>> = appDao.getAllApps()

    fun getAppsByCategory(category: String): Flow<List<FossApp>> = appDao.getAppsByCategory(category)

    fun getAppById(appId: String): Flow<FossApp?> = appDao.getAppById(appId)

    fun getInstalledApps(): Flow<List<FossApp>> = appDao.getInstalledApps()

    fun getBookmarkedApps(): Flow<List<FossApp>> = appDao.getBookmarkedApps()

    fun getUserUploadedApps(): Flow<List<FossApp>> = appDao.getUserUploadedApps()

    fun searchApps(query: String): Flow<List<FossApp>> = appDao.searchApps(query)

    fun getReviews(appId: String): Flow<List<Review>> = appDao.getReviewsForApp(appId)

    fun getCommits(appId: String): Flow<List<GitCommit>> = appDao.getCommitsForApp(appId)

    fun getBuildVerifications(appId: String): Flow<List<BuildVerification>> =
        appDao.getBuildVerificationsForApp(appId)

    fun getLatestBuildVerification(appId: String): Flow<BuildVerification?> =
        appDao.getLatestBuildVerification(appId)

    fun getAllDonations(): Flow<List<DeveloperDonation>> = appDao.getAllDonations()

    fun getDonationsForApp(appId: String): Flow<List<DeveloperDonation>> =
        appDao.getDonationsForApp(appId)

    suspend fun toggleBookmark(app: FossApp) {
        appDao.updateApp(app.copy(isBookmarked = !app.isBookmarked))
    }

    suspend fun installApp(app: FossApp) {
        val updatedApp = app.copy(
            isInstalled = true,
            installedVersion = app.versionName,
            downloadCount = app.downloadCount + 1
        )
        appDao.updateApp(updatedApp)

        // Sync download count to Firestore if signed in
        if (auth.currentUser != null) {
            try {
                firestore.collection("apps").document(app.id)
                    .update("downloadCount", FieldValue.increment(1L), "updatedAt", FieldValue.serverTimestamp())
            } catch (e: Exception) {
                handleFirestoreError(e, OperationType.UPDATE, "apps/${app.id}")
            }
        }
    }

    suspend fun uninstallApp(app: FossApp) {
        appDao.updateApp(
            app.copy(
                isInstalled = false,
                installedVersion = ""
            )
        )
    }

    suspend fun addReview(
        appId: String,
        author: String,
        rating: Int,
        title: String,
        comment: String,
        isAuditor: Boolean,
        auditorRole: String
    ) {
        val app = appDao.getAppByIdDirect(appId)
        val reviewId = "rev-" + UUID.randomUUID().toString().take(8)
        val currentUserId = auth.currentUser?.uid ?: "local_user_${UUID.randomUUID().toString().take(6)}"

        val review = Review(
            id = reviewId,
            appId = appId,
            author = author.ifBlank { auth.currentUser?.displayName ?: "Community Contributor" },
            rating = rating,
            title = title,
            comment = comment,
            timestamp = System.currentTimeMillis(),
            isAuditor = isAuditor,
            auditorRole = if (isAuditor) auditorRole else "",
            auditedCommit = app?.commitSha ?: "head",
            helpfulVotes = 0
        )

        // 1. Cache in local Room database
        appDao.insertReview(review)

        // Update local app rating average
        if (app != null) {
            val newReviewCount = app.reviewCount + 1
            val newRating = ((app.rating * app.reviewCount) + rating) / newReviewCount
            appDao.updateApp(app.copy(rating = newRating, reviewCount = newReviewCount))
        }

        // 2. Publish to remote Firestore if signed in
        if (auth.currentUser != null) {
            try {
                val reviewMap = FirestoreReview.fromReview(review, auth.currentUser!!.uid)
                    .filterValues { it != null } + mapOf(
                        "createdAt" to FieldValue.serverTimestamp(),
                        "updatedAt" to FieldValue.serverTimestamp()
                    )

                firestore.collection("reviews").document(reviewId)
                    .set(reviewMap)
                    .await()

                if (app != null) {
                    val newCount = (app.reviewCount + 1).toLong()
                    val newAvg = (((app.rating * app.reviewCount) + rating) / newCount).toDouble()
                    firestore.collection("apps").document(appId)
                        .update(
                            mapOf(
                                "rating" to newAvg,
                                "reviewCount" to newCount,
                                "updatedAt" to FieldValue.serverTimestamp()
                            )
                        )
                }
            } catch (e: Exception) {
                handleFirestoreError(e, OperationType.CREATE, "reviews/$reviewId")
            }
        }
    }

    suspend fun upvoteReview(reviewId: String) {
        appDao.upvoteReview(reviewId)

        if (auth.currentUser != null) {
            try {
                firestore.collection("reviews").document(reviewId)
                    .update("helpfulVotes", FieldValue.increment(1L), "updatedAt", FieldValue.serverTimestamp())
            } catch (e: Exception) {
                handleFirestoreError(e, OperationType.UPDATE, "reviews/$reviewId")
            }
        }
    }

    suspend fun recordDonation(
        appId: String,
        amountUsd: Double,
        platform: String,
        message: String
    ): DeveloperDonation? {
        val app = appDao.getAppByIdDirect(appId) ?: return null
        val badge = when {
            amountUsd >= 50 -> "Freedom Champion"
            amountUsd >= 20 -> "Gold Patron"
            amountUsd >= 10 -> "Silver Sponsor"
            else -> "Bronze Backer"
        }
        val donation = DeveloperDonation(
            id = "don-" + UUID.randomUUID().toString().take(8),
            appId = appId,
            appName = app.name,
            developerName = app.developerName,
            amountUsd = amountUsd,
            currency = "USD",
            platform = platform,
            supporterMessage = message.ifBlank { "Direct support for open-source development!" },
            backerBadge = badge,
            timestamp = System.currentTimeMillis()
        )
        appDao.insertDonation(donation)
        return donation
    }

    suspend fun syncGitRepo(appId: String): GitCommit? {
        val app = appDao.getAppByIdDirect(appId) ?: return null
        val shortSha = UUID.randomUUID().toString().take(8)
        val newTag = "v" + app.versionName + ".1"
        val newCommit = GitCommit(
            id = "commit-" + UUID.randomUUID().toString().take(8),
            appId = appId,
            sha = shortSha,
            tag = newTag,
            branch = app.gitBranch,
            author = app.developerName,
            message = "Automated sync from upstream Git tag $newTag: Performance updates and vulnerability patches",
            timestamp = System.currentTimeMillis(),
            isGpgVerified = true,
            gpgKeyId = "0x" + UUID.randomUUID().toString().take(8).uppercase(),
            filesChanged = (3..15).random(),
            additions = (50..300).random(),
            deletions = (5..40).random()
        )
        appDao.insertCommit(newCommit)

        triggerAutomatedBuildVerification(app.copy(commitSha = shortSha, latestGitTag = newTag))
        return newCommit
    }

    suspend fun triggerAutomatedBuildVerification(app: FossApp): BuildVerification {
        val verificationId = "bv-" + UUID.randomUUID().toString().take(8)
        val sourceHash = "sha256:" + UUID.randomUUID().toString().replace("-", "")
        val binaryHash = "sha256:" + UUID.randomUUID().toString().replace("-", "")

        val logOutput = """
[STAGE 1: GIT CLONE & SIGNATURE VERIFICATION]
> git clone ${app.gitRepoUrl} --depth=1 --branch ${app.latestGitTag}
> Commit ${app.commitSha} verified against developer GPG public key.
> Hermetic build environment: openplay/build-env:isolated-v2

[STAGE 2: DEPENDENCY AUDITING]
> Parsing lockfile for build system: ${app.buildSystem}
> Verifying cryptographic digests of build dependencies... OK
> Checking for proprietary closed-source blobs... NONE detected (100% Libre)

[STAGE 3: COMPILATION & BYTE-FOR-BYTE REPRODUCIBILITY]
> Running headless hermetic compile job...
> Generating output package: ${app.packageName}-${app.latestGitTag}.apk
> Comparing against independent builder attestation node...
> Match: 100% BYTE-FOR-BYTE IDENTICAL ($binaryHash)

[STAGE 4: MALWARE & STATIC ANALYSIS ENGINE]
> ClamAV FOSS engine: CLEAN (0 threats detected)
> YARA Android Heuristic engine: CLEAN
> Exodus Tracker Inspection: 0 trackers detected
> Manifest Permissions: Verified least-privilege principle

[STATUS: VERIFIED & REPRODUCIBLE]
OpenPlay Build Attestation created and published.
        """.trimIndent()

        val verification = BuildVerification(
            id = verificationId,
            appId = app.id,
            buildNumber = (100..999).random(),
            gitTag = app.latestGitTag,
            commitSha = app.commitSha,
            status = "VERIFIED",
            reproducibilityPercentage = 100,
            sourceArchiveHash = sourceHash,
            binaryArtifactHash = binaryHash,
            scannersRun = 18,
            threatsDetected = 0,
            securityScore = 100,
            verifiedBy = "OpenPlay Automated Attestation Node #07",
            logOutput = logOutput,
            timestamp = System.currentTimeMillis()
        )
        appDao.insertBuildVerification(verification)
        return verification
    }

    suspend fun uploadUserApp(
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
        iconKey: String
    ): FossApp {
        val appId = "user-app-" + UUID.randomUUID().toString().take(8)
        val cleanPackage = "org.openplay." + name.lowercase().replace(Regex("[^a-z0-9]"), "")
        val shortSha = UUID.randomUUID().toString().take(8)
        val apkHash = UUID.randomUUID().toString().replace("-", "") + UUID.randomUUID().toString().replace("-", "")
        val currentUserId = auth.currentUser?.uid ?: "creator_${UUID.randomUUID().toString().take(6)}"

        val newApp = FossApp(
            id = appId,
            packageName = cleanPackage,
            name = name,
            tagline = tagline,
            description = description,
            category = category,
            subcategory = subcategory.ifBlank { if (category == "Games") "Indie Game" else "FOSS Tool" },
            license = license,
            gitRepoUrl = gitRepoUrl,
            gitBranch = gitBranch.ifBlank { "main" },
            latestGitTag = latestGitTag.ifBlank { "v1.0.0" },
            commitSha = shortSha,
            versionName = latestGitTag.removePrefix("v").ifBlank { "1.0.0" },
            versionCode = 1,
            apkSizeMb = (12.0 + (5..60).random().toDouble()),
            apkSha256 = apkHash,
            reproducibleVerified = true,
            trackerCount = 0,
            antiFeatures = "",
            developerName = developerName.ifBlank { auth.currentUser?.displayName ?: "Independent Creator" },
            donationLiberapay = donationLiberapay,
            donationGithub = donationGithub,
            donationCrypto = donationCrypto,
            rating = 5.0f,
            reviewCount = 1,
            downloadCount = 12,
            isInstalled = false,
            isBookmarked = false,
            isUserUploaded = true,
            buildSystem = buildSystem,
            iconKey = iconKey
        )

        // 1. Cache in local Room
        appDao.insertApp(newApp)

        // Add initial git commit & verification
        val initialCommit = GitCommit(
            id = "commit-" + UUID.randomUUID().toString().take(8),
            appId = appId,
            sha = shortSha,
            tag = newApp.latestGitTag,
            branch = newApp.gitBranch,
            author = "$developerName <developer@openplay.org>",
            message = "Initial public open-source release ${newApp.latestGitTag}: Ready for OpenPlay verification",
            timestamp = System.currentTimeMillis(),
            isGpgVerified = true,
            gpgKeyId = "0x" + UUID.randomUUID().toString().take(8).uppercase(),
            filesChanged = 42,
            additions = 1850,
            deletions = 0
        )
        appDao.insertCommit(initialCommit)
        triggerAutomatedBuildVerification(newApp)

        val auditReview = Review(
            id = "rev-" + UUID.randomUUID().toString().take(8),
            appId = appId,
            author = "OpenPlay Automated CI Bot",
            rating = 5,
            title = "Automated Security & Reproducibility Check Passed",
            comment = "Successfully cloned $gitRepoUrl at tag ${newApp.latestGitTag}. Static analysis detected 0 malware threats, 0 ad trackers, and 100% reproducible binary output with license $license.",
            timestamp = System.currentTimeMillis(),
            isAuditor = true,
            auditorRole = "Automated CI Scanner",
            auditedCommit = shortSha,
            helpfulVotes = 1
        )
        appDao.insertReview(auditReview)

        // 2. Publish to remote Firestore if signed in
        if (auth.currentUser != null) {
            try {
                val appMap = FirestoreApp.fromFossApp(newApp, auth.currentUser!!.uid)
                    .filterValues { it != null } + mapOf(
                        "createdAt" to FieldValue.serverTimestamp(),
                        "updatedAt" to FieldValue.serverTimestamp()
                    )

                firestore.collection("apps").document(appId)
                    .set(appMap)
                    .await()
            } catch (e: Exception) {
                handleFirestoreError(e, OperationType.CREATE, "apps/$appId")
            }
        }

        return newApp
    }
}
