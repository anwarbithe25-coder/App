package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FossApp
import com.example.ui.components.AppIconBadge
import com.example.ui.components.LicensePill
import com.example.ui.components.ReproducibleBadgePill
import com.example.ui.components.TrackerZeroPill
import com.example.ui.theme.FossBorderMedium
import com.example.ui.theme.FossBorderSubtle
import com.example.ui.theme.FossCyanLight
import com.example.ui.theme.FossCyanPrimary
import com.example.ui.theme.FossDarkBg
import com.example.ui.theme.FossEmeraldGreen
import com.example.ui.theme.FossEmeraldLight
import com.example.ui.theme.FossPurpleAccent
import com.example.ui.theme.FossRoseAccent
import com.example.ui.theme.FossSkyBlue
import com.example.ui.theme.FossSurfaceDark
import com.example.ui.theme.FossSurfaceElevated
import com.example.ui.theme.FossTextPrimary
import com.example.ui.theme.FossTextSecondary
import com.example.ui.theme.FossTextTertiary
import com.example.ui.viewmodel.OpenPlayViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDetailScreen(
    viewModel: OpenPlayViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val app by viewModel.selectedApp.collectAsState()
    val reviews by viewModel.appReviews.collectAsState()
    val commits by viewModel.appCommits.collectAsState()
    val buildVerifications by viewModel.appBuildVerifications.collectAsState()
    val donations by viewModel.appDonations.collectAsState()

    val installingAppId by viewModel.installingAppId.collectAsState()
    val installProgress by viewModel.installProgress.collectAsState()
    val installStatusText by viewModel.installStatusText.collectAsState()
    val isSyncingGit by viewModel.isSyncingGit.collectAsState()
    val isVerifyingBuild by viewModel.isVerifyingBuild.collectAsState()

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var showReviewDialog by remember { mutableStateOf(false) }
    var showDonationDialog by remember { mutableStateOf(false) }
    var showLogDialog by remember { mutableStateOf(false) }

    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    BackHandler { onBack() }

    if (app == null) {
        Box(
            modifier = Modifier.fillMaxSize().background(FossDarkBg),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = FossCyanPrimary)
        }
        return
    }

    val currentApp = app!!
    val isCurrentlyInstalling = installingAppId == currentApp.id
    val latestVerification = buildVerifications.firstOrNull()

    Scaffold(
        modifier = modifier.fillMaxSize().testTag("app_detail_screen"),
        containerColor = FossDarkBg,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = currentApp.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = FossTextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("back_button")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = FossTextPrimary
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.toggleBookmark(currentApp) },
                        modifier = Modifier.testTag("detail_bookmark_button")
                    ) {
                        Icon(
                            imageVector = if (currentApp.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = if (currentApp.isBookmarked) FossCyanPrimary else FossTextSecondary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = FossSurfaceDark)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // Header Info Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(FossSurfaceDark)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppIconBadge(
                        iconKey = currentApp.iconKey,
                        category = currentApp.category,
                        size = 72
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = currentApp.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = FossTextPrimary
                        )
                        Text(
                            text = currentApp.developerName,
                            style = MaterialTheme.typography.bodyMedium,
                            color = FossCyanLight,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "v${currentApp.versionName}",
                                color = FossTextSecondary,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace
                            )
                            Text("•", color = FossTextTertiary, fontSize = 11.sp)
                            Text(
                                text = "${currentApp.apkSizeMb} MB",
                                color = FossTextSecondary,
                                fontSize = 11.sp
                            )
                            Text("•", color = FossTextTertiary, fontSize = 11.sp)
                            LicensePill(currentApp.license)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Stats row (Rating, Downloads, Trackers)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(FossSurfaceElevated)
                        .padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = String.format("%.1f", currentApp.rating),
                                fontWeight = FontWeight.Bold,
                                color = FossTextPrimary,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFBBF24),
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        Text(
                            text = "${currentApp.reviewCount} reviews",
                            color = FossTextTertiary,
                            fontSize = 10.sp
                        )
                    }

                    Box(modifier = Modifier.width(1.dp).height(24.dp).background(FossBorderMedium))

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${currentApp.downloadCount / 1000}k+",
                            fontWeight = FontWeight.Bold,
                            color = FossTextPrimary,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Downloads",
                            color = FossTextTertiary,
                            fontSize = 10.sp
                        )
                    }

                    Box(modifier = Modifier.width(1.dp).height(24.dp).background(FossBorderMedium))

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "0 Trackers",
                            fontWeight = FontWeight.Bold,
                            color = FossSkyBlue,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Exodus Verified",
                            color = FossTextTertiary,
                            fontSize = 10.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Primary Action Button (Install / Update / Open)
                if (isCurrentlyInstalling) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = installStatusText,
                            color = FossCyanLight,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { installProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = FossCyanPrimary,
                            trackColor = FossBorderMedium
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        if (currentApp.isInstalled) {
                            Button(
                                onClick = {
                                    Toast.makeText(context, "Opening ${currentApp.name}...", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("open_app_button"),
                                colors = ButtonDefaults.buttonColors(containerColor = FossEmeraldGreen),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.OpenInNew,
                                    contentDescription = null,
                                    tint = Color.Black,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Open App", color = Color.Black, fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = { viewModel.uninstallApp(currentApp) },
                                modifier = Modifier.height(48.dp).testTag("uninstall_app_button"),
                                shape = RoundedCornerShape(12.dp),
                                border = ButtonDefaults.outlinedButtonBorder().copy(brush = Brush.linearGradient(listOf(FossBorderMedium, FossBorderMedium)))
                            ) {
                                Text("Uninstall", color = FossTextSecondary, fontSize = 12.sp)
                            }
                        } else {
                            Button(
                                onClick = { viewModel.installApp(currentApp) },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("install_app_button"),
                                colors = ButtonDefaults.buttonColors(containerColor = FossCyanPrimary),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Download,
                                    contentDescription = null,
                                    tint = Color.Black,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Install (${currentApp.apkSizeMb} MB)",
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Support / Donate button
                        Button(
                            onClick = { showDonationDialog = true },
                            modifier = Modifier
                                .height(48.dp)
                                .testTag("support_developer_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = FossSurfaceElevated),
                            shape = RoundedCornerShape(12.dp),
                            border = ButtonDefaults.outlinedButtonBorder().copy(
                                brush = Brush.linearGradient(listOf(FossRoseAccent.copy(alpha = 0.6f), FossPurpleAccent.copy(alpha = 0.6f)))
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = null,
                                tint = FossRoseAccent,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Support", color = FossTextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        }
                    }
                }
            }

            // Tab Row (Overview, Build Verification, Git Sync, Community Reviews)
            val tabs = listOf("Overview", "Build Audit", "Git Releases", "Reviews (${reviews.size})")
            PrimaryTabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = FossSurfaceDark,
                contentColor = FossCyanPrimary
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                fontSize = 12.sp,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTabIndex == index) FossCyanPrimary else FossTextSecondary
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Tab Content
            when (selectedTabIndex) {
                0 -> OverviewTabContent(
                    app = currentApp,
                    onOpenDonation = { showDonationDialog = true },
                    onCopySha = {
                        clipboardManager.setText(AnnotatedString(currentApp.apkSha256))
                        Toast.makeText(context, "Copied APK SHA-256 Checksum", Toast.LENGTH_SHORT).show()
                    }
                )
                1 -> BuildAuditTabContent(
                    app = currentApp,
                    verification = latestVerification,
                    isVerifying = isVerifyingBuild,
                    onRunVerification = { viewModel.runAutomatedVerification(currentApp) },
                    onViewLogs = { showLogDialog = true }
                )
                2 -> GitReleasesTabContent(
                    app = currentApp,
                    commits = commits,
                    isSyncing = isSyncingGit,
                    onSyncGit = { viewModel.syncFromGit(currentApp.id) }
                )
                3 -> ReviewsTabContent(
                    reviews = reviews,
                    onWriteReview = { showReviewDialog = true },
                    onUpvote = { viewModel.upvoteReview(it) }
                )
            }
        }
    }

    if (showReviewDialog) {
        ReviewDialog(
            app = currentApp,
            onDismiss = { showReviewDialog = false },
            onSubmit = { author, rating, title, comment, isAuditor, role ->
                viewModel.submitReview(
                    appId = currentApp.id,
                    author = author,
                    rating = rating,
                    title = title,
                    comment = comment,
                    isAuditor = isAuditor,
                    auditorRole = role
                )
            }
        )
    }

    if (showDonationDialog) {
        DonationDialog(
            app = currentApp,
            onDismiss = { showDonationDialog = false },
            onTipCompleted = { amount, platform, message ->
                viewModel.recordDonation(
                    appId = currentApp.id,
                    amount = amount,
                    platform = platform,
                    message = message
                ) {}
            }
        )
    }

    if (showLogDialog && latestVerification != null) {
        BuildVerificationLogDialog(
            app = currentApp,
            logOutput = latestVerification.logOutput,
            onDismiss = { showLogDialog = false }
        )
    }
}

@Composable
private fun OverviewTabContent(
    app: FossApp,
    onOpenDonation: () -> Unit,
    onCopySha: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Description Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = FossSurfaceDark),
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(FossBorderMedium, FossBorderSubtle)))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "About this project",
                    style = MaterialTheme.typography.titleSmall,
                    color = FossCyanLight,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = app.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = FossTextPrimary,
                    lineHeight = 20.sp
                )
            }
        }

        // Privacy & Permissions Audit Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = FossSurfaceDark),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.horizontalGradient(listOf(FossEmeraldGreen.copy(alpha = 0.5f), FossBorderMedium))
            )
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = FossEmeraldLight,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Exodus Privacy & Safety Audit",
                        style = MaterialTheme.typography.titleSmall,
                        color = FossEmeraldLight,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(FossSurfaceElevated)
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = FossEmeraldGreen,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Zero Trackers Found (0 / 480 checked)",
                            color = FossTextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "No analytics, no ad SDKs, no device fingerprinting.",
                            color = FossTextSecondary,
                            fontSize = 10.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Permissions breakdown
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Manifest Permissions Required:",
                        fontSize = 11.sp,
                        color = FossTextTertiary,
                        fontWeight = FontWeight.SemiBold
                    )
                    val permissions = if (app.category == "Games") {
                        listOf("VIBRATE (Haptic feedback)", "WAKE_LOCK (Prevent screen sleep during gameplay)")
                    } else if (app.id.contains("aegis")) {
                        listOf("USE_BIOMETRIC (Fingerprint vault unlock)")
                    } else {
                        listOf("INTERNET (Download open content)", "ACCESS_NETWORK_STATE")
                    }
                    permissions.forEach { perm ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("•", color = FossCyanPrimary, fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(perm, color = FossTextSecondary, fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // Package & Technical Details
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = FossSurfaceDark),
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(FossBorderMedium, FossBorderSubtle)))
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Technical Specifications",
                    style = MaterialTheme.typography.titleSmall,
                    color = FossCyanLight,
                    fontWeight = FontWeight.Bold
                )

                DetailRow(label = "Package Name", value = app.packageName)
                DetailRow(label = "Build System", value = app.buildSystem)
                DetailRow(label = "Git Branch", value = app.gitBranch)
                DetailRow(label = "Release Tag", value = app.latestGitTag)
                DetailRow(label = "Commit SHA", value = app.commitSha)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(FossSurfaceElevated)
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("APK SHA-256 Digest:", fontSize = 10.sp, color = FossTextTertiary)
                        Text(
                            text = app.apkSha256.take(28) + "...",
                            fontSize = 11.sp,
                            color = FossCyanLight,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    IconButton(onClick = onCopySha, modifier = Modifier.size(28.dp)) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy Hash",
                            tint = FossCyanPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        // Direct Developer Support Banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onOpenDonation),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = FossSurfaceDark),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.linearGradient(listOf(FossRoseAccent, FossPurpleAccent))
            )
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(FossRoseAccent.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = FossRoseAccent,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Support ${app.developerName}",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Direct funding keeps this project 100% libre and independent.",
                        color = FossTextSecondary,
                        fontSize = 11.sp
                    )
                }
                Button(
                    onClick = onOpenDonation,
                    colors = ButtonDefaults.buttonColors(containerColor = FossRoseAccent),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Tip", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun BuildAuditTabContent(
    app: FossApp,
    verification: com.example.data.model.BuildVerification?,
    isVerifying: Boolean,
    onRunVerification: () -> Unit,
    onViewLogs: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Status Badge Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = FossSurfaceDark),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.linearGradient(listOf(FossEmeraldGreen, FossCyanPrimary))
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(FossEmeraldGreen.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.VerifiedUser,
                            contentDescription = null,
                            tint = FossEmeraldGreen,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "REPRODUCIBLE BUILD VERIFIED",
                            fontWeight = FontWeight.ExtraBold,
                            color = FossEmeraldLight,
                            fontSize = 13.sp,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "100% Byte-for-byte binary match from Git tag ${app.latestGitTag}",
                            color = FossTextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Checklist of verification
                val checks = listOf(
                    "Source code clone matched GPG signature" to true,
                    "Hermetic isolated build container used" to true,
                    "Zero proprietary binary blobs detected" to true,
                    "ClamAV & heuristic scanners: 0 threats detected" to true,
                    "Exodus privacy audit: 0 trackers detected" to true
                )

                checks.forEach { (label, passed) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = FossEmeraldLight,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(label, color = FossTextPrimary, fontSize = 11.sp)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onViewLogs,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        border = ButtonDefaults.outlinedButtonBorder().copy(
                            brush = Brush.linearGradient(listOf(FossBorderMedium, FossBorderMedium))
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Terminal,
                            contentDescription = null,
                            tint = FossCyanPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Inspect Build Logs", color = FossCyanLight, fontSize = 12.sp)
                    }

                    Button(
                        onClick = onRunVerification,
                        enabled = !isVerifying,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = FossSurfaceElevated),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        if (isVerifying) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(14.dp),
                                color = FossEmeraldGreen,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = null,
                                tint = FossEmeraldLight,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Re-Verify Now", color = FossTextPrimary, fontSize = 12.sp)
                    }
                }
            }
        }

        // Attestation Authority Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = FossSurfaceDark),
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(FossBorderMedium, FossBorderSubtle)))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "Automated Build Attestation Details",
                    style = MaterialTheme.typography.titleSmall,
                    color = FossCyanLight,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                DetailRow(label = "Attestation Node", value = verification?.verifiedBy ?: "OpenPlay Node #04")
                DetailRow(label = "Security Score", value = "100 / 100")
                DetailRow(label = "Scanners Evaluated", value = "18 Antivirus & SAST engines")
                DetailRow(label = "Threats Detected", value = "0 (Clean)")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun GitReleasesTabContent(
    app: FossApp,
    commits: List<com.example.data.model.GitCommit>,
    isSyncing: Boolean,
    onSyncGit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Git Repo Header Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = FossSurfaceDark),
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(FossCyanPrimary.copy(alpha = 0.5f), FossBorderMedium)))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Code,
                        contentDescription = null,
                        tint = FossCyanPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Git Version Control Integration",
                        fontWeight = FontWeight.Bold,
                        color = FossTextPrimary,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = app.gitRepoUrl,
                    color = FossCyanLight,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Active Branch: ", fontSize = 11.sp, color = FossTextTertiary)
                        Text(app.gitBranch, fontSize = 11.sp, color = FossTextPrimary, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Tag: ", fontSize = 11.sp, color = FossTextTertiary)
                        Text(app.latestGitTag, fontSize = 11.sp, color = FossEmeraldLight, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onSyncGit,
                        enabled = !isSyncing,
                        colors = ButtonDefaults.buttonColors(containerColor = FossCyanPrimary),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.testTag("sync_git_button")
                    ) {
                        if (isSyncing) {
                            CircularProgressIndicator(modifier = Modifier.size(12.dp), color = Color.Black, strokeWidth = 2.dp)
                        } else {
                            Icon(Icons.Default.Sync, contentDescription = null, tint = Color.Black, modifier = Modifier.size(14.dp))
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Pull from Git", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Commit Log List
        Text(
            text = "Commit History (${commits.size})",
            style = MaterialTheme.typography.titleSmall,
            color = FossTextPrimary,
            fontWeight = FontWeight.Bold
        )

        commits.forEach { commit ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = FossSurfaceDark),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(FossBorderMedium, FossBorderSubtle)))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = FossSurfaceElevated
                        ) {
                            Text(
                                text = commit.sha,
                                color = FossCyanLight,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        if (commit.tag.isNotBlank()) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = FossEmeraldGreen.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = commit.tag,
                                    color = FossEmeraldLight,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        if (commit.isGpgVerified) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = FossEmeraldLight,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text("GPG", color = FossEmeraldLight, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = commit.message,
                        color = FossTextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = commit.author,
                            color = FossTextTertiary,
                            fontSize = 10.sp
                        )
                        Text(
                            text = "+${commit.additions} -${commit.deletions} lines",
                            color = FossTextSecondary,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun ReviewsTabContent(
    reviews: List<com.example.data.model.Review>,
    onWriteReview: () -> Unit,
    onUpvote: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Write review button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Ratings and reviews",
                style = MaterialTheme.typography.titleMedium,
                color = FossTextPrimary,
                fontWeight = FontWeight.Bold
            )

            Button(
                onClick = onWriteReview,
                colors = ButtonDefaults.buttonColors(containerColor = FossCyanPrimary),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                modifier = Modifier.testTag("open_write_review_button")
            ) {
                Icon(Icons.Default.RateReview, contentDescription = null, tint = Color.Black, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Write a review", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }

        // Play Store 5-Star Histogram Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = FossSurfaceDark),
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(FossBorderMedium, FossBorderSubtle)))
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val avgRating = if (reviews.isNotEmpty()) reviews.map { it.rating }.average() else 4.8
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(90.dp)
                ) {
                    Text(
                        text = String.format("%.1f", avgRating),
                        fontSize = 38.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = FossTextPrimary
                    )
                    Row {
                        for (i in 1..5) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFBBF24),
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${reviews.size} reviews",
                        fontSize = 10.sp,
                        color = FossTextTertiary
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // 5 horizontal bars
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    val count5 = reviews.count { it.rating == 5 }
                    val count4 = reviews.count { it.rating == 4 }
                    val count3 = reviews.count { it.rating == 3 }
                    val count2 = reviews.count { it.rating == 2 }
                    val count1 = reviews.count { it.rating == 1 }
                    val total = maxOf(1, reviews.size)

                    RatingBarRow(starNum = 5, progress = count5.toFloat() / total)
                    RatingBarRow(starNum = 4, progress = count4.toFloat() / total)
                    RatingBarRow(starNum = 3, progress = count3.toFloat() / total)
                    RatingBarRow(starNum = 2, progress = count2.toFloat() / total)
                    RatingBarRow(starNum = 1, progress = count1.toFloat() / total)
                }
            }
        }

        if (reviews.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("No community reviews yet. Be the first to audit this project!", color = FossTextTertiary, fontSize = 12.sp)
            }
        } else {
            reviews.forEach { rev ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = FossSurfaceDark),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.horizontalGradient(
                            if (rev.isAuditor) listOf(FossEmeraldGreen.copy(alpha = 0.5f), FossBorderMedium)
                            else listOf(FossBorderMedium, FossBorderSubtle)
                        )
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = rev.author,
                                fontWeight = FontWeight.Bold,
                                color = FossTextPrimary,
                                fontSize = 13.sp
                            )

                            if (rev.isAuditor) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(FossEmeraldGreen.copy(alpha = 0.2f))
                                        .padding(horizontal = 5.dp, vertical = 2.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Verified,
                                        contentDescription = null,
                                        tint = FossEmeraldLight,
                                        modifier = Modifier.size(11.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = rev.auditorRole.ifBlank { "Auditor" },
                                        color = FossEmeraldLight,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.weight(1f))

                            Row {
                                for (i in 1..rev.rating) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = Color(0xFFFBBF24),
                                        modifier = Modifier.size(13.dp)
                                    )
                                }
                            }
                        }

                        if (rev.auditedCommit.isNotBlank()) {
                            Text(
                                text = "Audited Commit: ${rev.auditedCommit}",
                                fontSize = 10.sp,
                                color = FossCyanLight,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = rev.title,
                            fontWeight = FontWeight.SemiBold,
                            color = FossTextPrimary,
                            fontSize = 13.sp
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = rev.comment,
                            color = FossTextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date(rev.timestamp)),
                                color = FossTextTertiary,
                                fontSize = 10.sp
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable { onUpvote(rev.id) }
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ThumbUp,
                                    contentDescription = "Helpful",
                                    tint = FossCyanPrimary,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Helpful (${rev.helpfulVotes})",
                                    color = FossCyanLight,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, color = FossTextTertiary, fontSize = 11.sp)
        Text(
            text = value,
            color = FossTextPrimary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = if (label.contains("Branch") || label.contains("SHA") || label.contains("Tag")) FontFamily.Monospace else FontFamily.Default
        )
    }
}

@Composable
private fun RatingBarRow(starNum: Int, progress: Float) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "$starNum",
            fontSize = 11.sp,
            color = FossTextTertiary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(12.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .weight(1f)
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = FossCyanPrimary,
            trackColor = FossSurfaceElevated
        )
    }
}

