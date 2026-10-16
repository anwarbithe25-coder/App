package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.FossApp
import com.example.ui.components.AppIconBadge
import com.example.ui.components.AppListItem
import com.example.ui.components.GameFeaturedCard
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
import com.example.ui.theme.FossSkyBlue
import com.example.ui.theme.FossSurfaceDark
import com.example.ui.theme.FossSurfaceElevated
import com.example.ui.theme.FossTextPrimary
import com.example.ui.theme.FossTextSecondary
import com.example.ui.theme.FossTextTertiary
import com.example.ui.viewmodel.OpenPlayViewModel

@Composable
fun HomeScreen(
    viewModel: OpenPlayViewModel,
    onAppClick: (String) -> Unit,
    onNavigateToGames: () -> Unit,
    onNavigateToLibrary: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val selectedLicense by viewModel.selectedLicenseFilter.collectAsState()
    val filteredApps by viewModel.filteredApps.collectAsState()
    val installingAppId by viewModel.installingAppId.collectAsState()
    val installProgress by viewModel.installProgress.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val isAuthLoading by viewModel.isAuthLoading.collectAsState()

    var showAccountDialog by remember { mutableStateOf(false) }
    var selectedTopTab by remember { mutableIntStateOf(0) }
    val playTabs = listOf("For you", "Top charts", "Categories", "Play Protect")

    val categories = listOf("All", "Games", "Privacy & Security", "Utilities", "Media", "Dev Tools")
    val licenses = listOf("All", "GPL-3.0", "MIT", "Apache-2.0", "MPL-2.0")

    val featuredGame = filteredApps.firstOrNull { it.category == "Games" } ?: filteredApps.firstOrNull()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(FossDarkBg)
            .testTag("home_screen"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Play Store Search Bar with Avatar
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 4.dp)
            ) {
                // Play Store Search Pill
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(28.dp))
                        .background(FossSurfaceElevated)
                        .border(1.dp, FossBorderMedium, RoundedCornerShape(28.dp))
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = FossCyanPrimary,
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.setSearchQuery(it) },
                        placeholder = {
                            Text(
                                "Search apps & games",
                                color = FossTextTertiary,
                                fontSize = 14.sp
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear",
                                        tint = FossTextSecondary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedTextColor = FossTextPrimary,
                            unfocusedTextColor = FossTextPrimary
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("play_search_input")
                    )

                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Voice search",
                        tint = FossTextSecondary,
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    // Play Store User Profile Circle
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(listOf(FossCyanPrimary, FossEmeraldGreen))
                            )
                            .clickable { showAccountDialog = true }
                            .testTag("user_avatar_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (currentUser?.displayName?.firstOrNull() ?: currentUser?.email?.firstOrNull() ?: 'U').uppercase(),
                            color = Color.Black,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Play Store Sub-tabs: "For you", "Top charts", "Categories", "Play Protect"
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    playTabs.forEachIndexed { index, tabTitle ->
                        val isSelected = selectedTopTab == index
                        Column(
                            modifier = Modifier
                                .clickable { selectedTopTab = index }
                                .padding(vertical = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = tabTitle,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) FossCyanPrimary else FossTextSecondary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .width(28.dp)
                                        .height(3.dp)
                                        .clip(RoundedCornerShape(1.5.dp))
                                        .background(FossCyanPrimary)
                                )
                            }
                        }
                    }
                }
            }
        }

        // TAB 0: "For you"
        if (selectedTopTab == 0) {
            // Hero Banner
            if (searchQuery.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = FossSurfaceDark),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = Brush.horizontalGradient(listOf(FossCyanPrimary.copy(alpha = 0.4f), FossPurpleAccent.copy(alpha = 0.4f)))
                        )
                    ) {
                        Box(modifier = Modifier.fillMaxWidth().height(165.dp)) {
                            Image(
                                painter = painterResource(id = R.drawable.img_store_banner),
                                contentDescription = "OpenPlay Hero Art",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(
                                                Color.Transparent,
                                                FossSurfaceDark.copy(alpha = 0.85f),
                                                FossSurfaceDark
                                            )
                                        )
                                    )
                            )
                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(16.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "OPENPLAY • PLAY STORE LIBRE EDITION",
                                        color = FossCyanLight,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        letterSpacing = 1.sp
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    TrackerZeroPill()
                                }
                                Text(
                                    text = "Clean, Privacy-First Play Store",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "Powered by Room & Firestore hybrid sync with Google Sign-In.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = FossTextSecondary,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }

            // Category Chips Row
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { cat ->
                        val isSelected = selectedCategory == cat
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.setSelectedCategory(cat) },
                            label = { Text(cat, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = FossCyanPrimary,
                                selectedLabelColor = Color.Black,
                                containerColor = FossSurfaceDark,
                                labelColor = FossTextSecondary
                            )
                        )
                    }
                }
            }

            // Spotlight Game Card
            if (featuredGame != null && searchQuery.isEmpty() && selectedCategory in listOf("All", "Games")) {
                item {
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Featured open-source games",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = FossTextPrimary
                            )
                            Text(
                                text = "See more →",
                                color = FossCyanLight,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.clickable(onClick = onNavigateToGames)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        GameFeaturedCard(
                            app = featuredGame,
                            onClick = { onAppClick(featuredGame.id) }
                        )
                    }
                }
            }

            // Horizontal Recommended Apps Carousel
            item {
                Column(modifier = Modifier.fillMaxWidth().padding(top = 10.dp)) {
                    Text(
                        text = "Suggested for you",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = FossTextPrimary,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(filteredApps.take(6), key = { "rec_${it.id}" }) { app ->
                            PlayStoreMiniCard(
                                app = app,
                                onClick = { onAppClick(app.id) }
                            )
                        }
                    }
                }
            }

            // Section: All Verified Listings
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (searchQuery.isNotEmpty()) "Search Results (${filteredApps.size})" else "All Open Source Releases",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = FossTextPrimary
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CloudDone, contentDescription = null, tint = FossEmeraldLight, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Synced", color = FossEmeraldLight, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }

            items(filteredApps, key = { it.id }) { app ->
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp)) {
                    AppListItem(
                        app = app,
                        isInstalling = installingAppId == app.id,
                        installProgress = installProgress,
                        onClick = { onAppClick(app.id) },
                        onInstallClick = { viewModel.installApp(app) },
                        onBookmarkClick = { viewModel.toggleBookmark(app) }
                    )
                }
            }
        }

        // TAB 1: "Top charts"
        if (selectedTopTab == 1) {
            item {
                Text(
                    text = "Top Free Open-Source Charts",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = FossTextPrimary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            val topSorted = filteredApps.sortedByDescending { it.downloadCount }
            itemsIndexed(topSorted, key = { _, app -> "top_${app.id}" }) { index, app ->
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                    PlayStoreTopChartItem(
                        rank = index + 1,
                        app = app,
                        onClick = { onAppClick(app.id) }
                    )
                }
            }
        }

        // TAB 2: "Categories"
        if (selectedTopTab == 2) {
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Explore Categories",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = FossTextPrimary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    categories.filter { it != "All" }.forEach { cat ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    viewModel.setSelectedCategory(cat)
                                    selectedTopTab = 0
                                },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = FossSurfaceDark),
                            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(FossBorderMedium, FossBorderSubtle)))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AppIconBadge(iconKey = if (cat == "Games") "gamepad" else "shield", category = cat, size = 40)
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(cat, fontWeight = FontWeight.Bold, color = FossTextPrimary, fontSize = 14.sp)
                                    Text("Open source & 100% reproducible", color = FossTextSecondary, fontSize = 11.sp)
                                }
                                Text("Explore →", color = FossCyanLight, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }
        }

        // TAB 3: "Play Protect"
        if (selectedTopTab == 3) {
            item {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = FossSurfaceDark),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = Brush.linearGradient(listOf(FossEmeraldGreen, FossCyanPrimary))
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(FossEmeraldGreen.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = FossEmeraldLight, modifier = Modifier.size(26.dp))
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text("OpenPlay Protect", fontWeight = FontWeight.Bold, color = FossTextPrimary, fontSize = 16.sp)
                                    Text("No harmful apps found • Scanned just now", color = FossEmeraldLight, fontSize = 11.sp)
                                }
                            }
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                "OpenPlay Protect checks your apps and device for malware and harmful code before downloading. All apps undergo 18 static and heuristic security analysis checks.",
                                color = FossTextSecondary,
                                fontSize = 12.sp,
                                lineHeight = 17.sp
                            )
                        }
                    }
                }
            }
        }
    }

    if (showAccountDialog) {
        PlayStoreAccountDialog(
            user = currentUser,
            isLoading = isAuthLoading,
            viewModel = viewModel,
            onDismiss = { showAccountDialog = false },
            onNavigateToLibrary = onNavigateToLibrary
        )
    }
}

@Composable
fun PlayStoreMiniCard(
    app: FossApp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(135.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = FossSurfaceDark),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(FossBorderMedium, FossBorderSubtle)))
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            AppIconBadge(iconKey = app.iconKey, category = app.category, size = 64, modifier = Modifier.align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = app.name,
                fontWeight = FontWeight.Bold,
                color = FossTextPrimary,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${app.apkSizeMb} MB",
                color = FossTextTertiary,
                fontSize = 10.sp
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = String.format("%.1f", app.rating),
                    color = FossTextSecondary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(2.dp))
                Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFBBF24), modifier = Modifier.size(10.dp))
            }
        }
    }
}

@Composable
fun PlayStoreTopChartItem(
    rank: Int,
    app: FossApp,
    onClick: () -> Unit
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
            Text(
                text = "$rank",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = if (rank <= 3) FossCyanPrimary else FossTextTertiary,
                modifier = Modifier.width(24.dp)
            )

            AppIconBadge(iconKey = app.iconKey, category = app.category, size = 48)

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = app.name,
                    fontWeight = FontWeight.Bold,
                    color = FossTextPrimary,
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${app.category} • ${app.apkSizeMb} MB",
                    color = FossTextSecondary,
                    fontSize = 11.sp
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = String.format("%.1f ★", app.rating),
                        color = Color(0xFFFBBF24),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${app.downloadCount / 1000}K downloads",
                        color = FossTextTertiary,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}
