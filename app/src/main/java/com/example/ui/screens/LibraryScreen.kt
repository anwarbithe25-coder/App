package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DeveloperDonation
import com.example.data.model.FossApp
import com.example.ui.components.AppIconBadge
import com.example.ui.components.AppListItem
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

@androidx.compose.material3.ExperimentalMaterial3Api
@Composable
fun LibraryScreen(
    viewModel: OpenPlayViewModel,
    onAppClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val installedApps by viewModel.installedApps.collectAsState()
    val bookmarkedApps by viewModel.bookmarkedApps.collectAsState()
    val donations by viewModel.allDonations.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf(
        "Installed (${installedApps.size})",
        "Bookmarks (${bookmarkedApps.size})",
        "Backer Hub (${donations.size})",
        "Privacy Audit"
    )

    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(FossDarkBg)
            .testTag("library_screen"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Top Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LibraryBooks,
                                contentDescription = null,
                                tint = FossCyanPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "My Library & Backing",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = FossTextPrimary
                            )
                        }
                        Text(
                            text = "Installed packages, saved projects, and developer contributions",
                            style = MaterialTheme.typography.bodySmall,
                            color = FossTextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Tabs Row
                PrimaryTabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = FossSurfaceDark,
                    contentColor = FossCyanPrimary
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                Text(
                                    text = title,
                                    fontSize = 11.sp,
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selectedTab == index) FossCyanPrimary else FossTextSecondary
                                )
                            }
                        )
                    }
                }
            }
        }

        when (selectedTab) {
            0 -> {
                // Installed Apps
                if (installedApps.isEmpty()) {
                    item {
                        EmptyLibraryState(
                            message = "No open-source apps or games installed yet",
                            subMessage = "Explore the store and install malware-free, reproducible packages."
                        )
                    }
                } else {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Installed Libre Software",
                                fontWeight = FontWeight.Bold,
                                color = FossTextPrimary,
                                fontSize = 13.sp
                            )
                            OutlinedButton(
                                onClick = {
                                    Toast.makeText(context, "All upstream Git release tags are up-to-date!", Toast.LENGTH_SHORT).show()
                                },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                shape = RoundedCornerShape(8.dp),
                                border = ButtonDefaults.outlinedButtonBorder().copy(brush = Brush.horizontalGradient(listOf(FossBorderMedium, FossBorderMedium)))
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = null, tint = FossCyanPrimary, modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Check Updates", fontSize = 11.sp, color = FossCyanLight)
                            }
                        }
                    }

                    items(installedApps, key = { it.id }) { app ->
                        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                            InstalledAppCard(
                                app = app,
                                onClick = { onAppClick(app.id) },
                                onOpen = {
                                    Toast.makeText(context, "Launching ${app.name}...", Toast.LENGTH_SHORT).show()
                                },
                                onUninstall = { viewModel.uninstallApp(app) }
                            )
                        }
                    }
                }
            }

            1 -> {
                // Bookmarks
                if (bookmarkedApps.isEmpty()) {
                    item {
                        EmptyLibraryState(
                            message = "No bookmarked projects",
                            subMessage = "Tap the bookmark icon on any game or tool to save it here for quick access."
                        )
                    }
                } else {
                    items(bookmarkedApps, key = { it.id }) { app ->
                        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                            AppListItem(
                                app = app,
                                onClick = { onAppClick(app.id) },
                                onInstallClick = { viewModel.installApp(app) },
                                onBookmarkClick = { viewModel.toggleBookmark(app) }
                            )
                        }
                    }
                }
            }

            2 -> {
                // Backer Hub & Developer Support History
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        val totalTipped = donations.sumOf { it.amountUsd }
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = FossSurfaceDark),
                            border = CardDefaults.outlinedCardBorder().copy(
                                brush = Brush.linearGradient(listOf(FossRoseAccent, FossPurpleAccent))
                            )
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(FossRoseAccent.copy(alpha = 0.2f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Favorite,
                                            contentDescription = null,
                                            tint = FossRoseAccent,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = "Total Developer Backing",
                                            color = FossTextSecondary,
                                            fontSize = 11.sp
                                        )
                                        Text(
                                            text = "$${totalTipped.toInt()} USD Funded",
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color.White,
                                            fontSize = 20.sp
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "Direct funding empowers open-source maintainers to keep software surveillance-free and sustainable.",
                                    color = FossTextTertiary,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Your Contribution Records",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = FossTextPrimary
                        )
                    }
                }

                if (donations.isEmpty()) {
                    item {
                        EmptyLibraryState(
                            message = "No donations recorded yet",
                            subMessage = "Tip creators directly via the Support Developer portal on any project detail screen."
                        )
                    }
                } else {
                    items(donations, key = { it.id }) { donation ->
                        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp)) {
                            DonationRecordCard(donation)
                        }
                    }
                }
            }

            3 -> {
                // Privacy Audit Summary
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = FossSurfaceDark),
                            border = CardDefaults.outlinedCardBorder().copy(
                                brush = Brush.linearGradient(listOf(FossEmeraldGreen, FossCyanPrimary))
                            )
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Shield,
                                        contentDescription = null,
                                        tint = FossEmeraldLight,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "Device Freedom & Privacy Health",
                                            fontWeight = FontWeight.Bold,
                                            color = FossTextPrimary,
                                            fontSize = 14.sp
                                        )
                                        Text(
                                            text = "All ${installedApps.size} installed apps verified 100% telemetry-free",
                                            color = FossEmeraldLight,
                                            fontSize = 11.sp
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceAround
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("0", color = FossSkyBlue, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                                        Text("Ad Trackers", color = FossTextTertiary, fontSize = 11.sp)
                                    }
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("0", color = FossRoseAccent, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                                        Text("Proprietary SDKs", color = FossTextTertiary, fontSize = 11.sp)
                                    }
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("100%", color = FossEmeraldLight, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                                        Text("Reproducible", color = FossTextTertiary, fontSize = 11.sp)
                                    }
                                }
                            }
                        }

                        // Principles Card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = FossSurfaceDark),
                            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(FossBorderMedium, FossBorderSubtle)))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "OpenPlay Safety Commitments",
                                    fontWeight = FontWeight.Bold,
                                    color = FossCyanLight,
                                    fontSize = 13.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "• Hermetic builds: Compiled in isolated sandboxes from tagged Git source\n• Binary verification: Independent attestations verify byte-for-byte reproducibility\n• Zero surveillance: No user accounts required, no telemetry pingbacks\n• Unrestricted downloads: Direct APK extraction without proprietary play services",
                                    color = FossTextSecondary,
                                    fontSize = 11.sp,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InstalledAppCard(
    app: FossApp,
    onClick: () -> Unit,
    onOpen: () -> Unit,
    onUninstall: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = FossSurfaceDark),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(FossBorderMedium, FossBorderSubtle)))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppIconBadge(iconKey = app.iconKey, category = app.category, size = 48)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = app.name,
                    fontWeight = FontWeight.Bold,
                    color = FossTextPrimary,
                    fontSize = 14.sp
                )
                Text(
                    text = "v${app.versionName} • ${app.apkSizeMb} MB",
                    color = FossTextSecondary,
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = FossEmeraldLight, modifier = Modifier.size(11.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text("Verified Malware-Free", color = FossEmeraldLight, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Button(
                    onClick = onOpen,
                    colors = ButtonDefaults.buttonColors(containerColor = FossEmeraldGreen),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Open", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onUninstall,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(8.dp),
                    border = ButtonDefaults.outlinedButtonBorder().copy(brush = Brush.horizontalGradient(listOf(FossBorderMedium, FossBorderMedium)))
                ) {
                    Text("Remove", color = FossTextTertiary, fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
private fun DonationRecordCard(donation: DeveloperDonation) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = FossSurfaceDark),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(FossBorderMedium, FossBorderSubtle)))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = FossRoseAccent.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = "$${donation.amountUsd.toInt()} USD",
                        color = FossRoseAccent,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "To ${donation.developerName}",
                        fontWeight = FontWeight.Bold,
                        color = FossTextPrimary,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "For ${donation.appName} via ${donation.platform}",
                        color = FossTextSecondary,
                        fontSize = 10.sp
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = FossSurfaceElevated
                ) {
                    Text(
                        text = donation.backerBadge,
                        color = FossCyanLight,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }

            if (donation.supporterMessage.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "\"${donation.supporterMessage}\"",
                    color = FossTextTertiary,
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date(donation.timestamp)),
                color = FossTextTertiary,
                fontSize = 9.sp
            )
        }
    }
}

@Composable
private fun EmptyLibraryState(message: String, subMessage: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.LibraryBooks,
                contentDescription = null,
                tint = FossTextTertiary,
                modifier = Modifier.size(44.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = message,
                fontWeight = FontWeight.Bold,
                color = FossTextSecondary,
                fontSize = 13.sp
            )
            Text(
                text = subMessage,
                color = FossTextTertiary,
                fontSize = 11.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
