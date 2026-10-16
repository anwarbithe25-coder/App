package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FossApp
import com.example.ui.components.AppIconBadge
import com.example.ui.components.AppListItem
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

@Composable
fun DeveloperHubScreen(
    viewModel: OpenPlayViewModel,
    onAppClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val userUploadedApps by viewModel.userUploadedApps.collectAsState()
    val isUploading by viewModel.isUploading.collectAsState()
    val uploadProgressStep by viewModel.uploadProgressStep.collectAsState()

    var showUploadForm by remember { mutableStateOf(false) }

    // Form fields
    var name by remember { mutableStateOf("") }
    var tagline by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Games") }
    var subcategory by remember { mutableStateOf("Indie Roguelike") }
    var license by remember { mutableStateOf("GPL-3.0-or-later") }
    var gitRepoUrl by remember { mutableStateOf("https://github.com/myteam/open-adventure.git") }
    var gitBranch by remember { mutableStateOf("main") }
    var latestGitTag by remember { mutableStateOf("v1.0.0") }
    var buildSystem by remember { mutableStateOf("Gradle (Android)") }
    var developerName by remember { mutableStateOf("") }
    var donationLiberapay by remember { mutableStateOf("") }
    var donationGithub by remember { mutableStateOf("") }
    var donationCrypto by remember { mutableStateOf("") }
    var iconKey by remember { mutableStateOf("gamepad") }

    val categories = listOf("Games", "Privacy & Security", "Utilities", "Media", "Dev Tools")
    val licenses = listOf("GPL-3.0-or-later", "MIT", "Apache-2.0", "MPL-2.0", "AGPL-3.0", "BSD-3-Clause")
    val buildSystems = listOf("Gradle (Android)", "Godot 4.x Headless", "Rust / Cargo-NDK", "CMake / NDK")
    val iconChoices = listOf(
        "gamepad" to "Gamepad",
        "dungeon" to "Dungeon / RPG",
        "factory" to "Logistics / Tech",
        "rocket" to "Arcade / Space",
        "shield" to "Security",
        "terminal" to "Dev Tool"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(FossDarkBg)
            .testTag("developer_hub_screen"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Hub Banner Header
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
                                imageVector = Icons.Default.Code,
                                contentDescription = null,
                                tint = FossCyanPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Developer Hub",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = FossTextPrimary
                            )
                        }
                        Text(
                            text = "Git version control • Automated verification • 0% platform fee",
                            style = MaterialTheme.typography.bodySmall,
                            color = FossTextSecondary
                        )
                    }

                    Button(
                        onClick = { showUploadForm = !showUploadForm },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (showUploadForm) FossSurfaceElevated else FossCyanPrimary
                        ),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("toggle_upload_form_button")
                    ) {
                        Icon(
                            imageVector = if (showUploadForm) Icons.Default.Sync else Icons.Default.CloudUpload,
                            contentDescription = null,
                            tint = if (showUploadForm) FossTextPrimary else Color.Black,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (showUploadForm) "Close Form" else "Publish Project",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (showUploadForm) FossTextPrimary else Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Feature Highlights Strip
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = FossSurfaceDark),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.horizontalGradient(
                            listOf(FossCyanPrimary.copy(alpha = 0.4f), FossEmeraldGreen.copy(alpha = 0.4f))
                        )
                    )
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "The OpenPlay Publishing Pipeline",
                            fontWeight = FontWeight.Bold,
                            color = FossCyanLight,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "1. Connect your public Git repo (GitHub, GitLab, Codeberg)\n2. OpenPlay automated container builds your game hermetically\n3. ClamAV & Exodus confirm 0 malware and 0 trackers\n4. Your project goes live with direct creator donation links!",
                            color = FossTextSecondary,
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        // Live Uploading Progress Overlay
        if (isUploading) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF030712)),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.linearGradient(listOf(FossEmeraldGreen, FossCyanPrimary))
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = FossCyanPrimary, modifier = Modifier.size(36.dp))
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Running Automated Build Verification Pipeline...",
                            fontWeight = FontWeight.Bold,
                            color = FossTextPrimary,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = uploadProgressStep,
                            color = FossEmeraldLight,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // Upload Form (Expandable)
        if (showUploadForm) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = FossSurfaceDark),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.linearGradient(listOf(FossCyanPrimary, FossEmeraldGreen))
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Submit Open-Source Game / Software",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = FossTextPrimary
                        )

                        // Title & Tagline
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("App / Game Name *", color = FossTextSecondary) },
                            placeholder = { Text("e.g., Pixel Quest FOSS", color = FossTextTertiary) },
                            modifier = Modifier.fillMaxWidth().testTag("upload_name_input"),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = FossTextPrimary,
                                unfocusedTextColor = FossTextPrimary,
                                focusedBorderColor = FossCyanPrimary,
                                unfocusedBorderColor = FossBorderMedium
                            )
                        )

                        OutlinedTextField(
                            value = tagline,
                            onValueChange = { tagline = it },
                            label = { Text("Short Tagline *", color = FossTextSecondary) },
                            placeholder = { Text("e.g., Retro 2D dungeon crawler built with Godot", color = FossTextTertiary) },
                            modifier = Modifier.fillMaxWidth().testTag("upload_tagline_input"),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = FossTextPrimary,
                                unfocusedTextColor = FossTextPrimary,
                                focusedBorderColor = FossCyanPrimary,
                                unfocusedBorderColor = FossBorderMedium
                            )
                        )

                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Detailed Description *", color = FossTextSecondary) },
                            placeholder = { Text("Explain mechanics, open-source tech stack...", color = FossTextTertiary) },
                            modifier = Modifier.fillMaxWidth().height(100.dp).testTag("upload_description_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = FossTextPrimary,
                                unfocusedTextColor = FossTextPrimary,
                                focusedBorderColor = FossCyanPrimary,
                                unfocusedBorderColor = FossBorderMedium
                            )
                        )

                        // Category Chips
                        Text("Category:", color = FossTextSecondary, fontSize = 11.sp)
                        Row(
                            modifier = Modifier.horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            categories.forEach { cat ->
                                val isSelected = category == cat
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { category = cat },
                                    label = { Text(cat, fontSize = 11.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = FossCyanPrimary,
                                        selectedLabelColor = Color.Black,
                                        containerColor = FossSurfaceElevated,
                                        labelColor = FossTextSecondary
                                    )
                                )
                            }
                        }

                        // Open Source License Chips
                        Text("Open Source License:", color = FossTextSecondary, fontSize = 11.sp)
                        Row(
                            modifier = Modifier.horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            licenses.forEach { lic ->
                                val isSelected = license == lic
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { license = lic },
                                    label = { Text(lic, fontSize = 10.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = FossEmeraldGreen,
                                        selectedLabelColor = Color.Black,
                                        containerColor = FossSurfaceElevated,
                                        labelColor = FossTextSecondary
                                    )
                                )
                            }
                        }

                        // Build System
                        Text("Hermetic Build System:", color = FossTextSecondary, fontSize = 11.sp)
                        Row(
                            modifier = Modifier.horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            buildSystems.forEach { bs ->
                                val isSelected = buildSystem == bs
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { buildSystem = bs },
                                    label = { Text(bs, fontSize = 10.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = FossPurpleAccent,
                                        selectedLabelColor = Color.White,
                                        containerColor = FossSurfaceElevated,
                                        labelColor = FossTextSecondary
                                    )
                                )
                            }
                        }

                        // Git Integration fields
                        OutlinedTextField(
                            value = gitRepoUrl,
                            onValueChange = { gitRepoUrl = it },
                            label = { Text("Git Repository URL *", color = FossTextSecondary) },
                            placeholder = { Text("https://github.com/...", color = FossTextTertiary) },
                            modifier = Modifier.fillMaxWidth().testTag("upload_git_url_input"),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = FossTextPrimary,
                                unfocusedTextColor = FossTextPrimary,
                                focusedBorderColor = FossCyanPrimary,
                                unfocusedBorderColor = FossBorderMedium
                            )
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = gitBranch,
                                onValueChange = { gitBranch = it },
                                label = { Text("Branch", color = FossTextSecondary) },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = FossTextPrimary,
                                    unfocusedTextColor = FossTextPrimary,
                                    focusedBorderColor = FossCyanPrimary,
                                    unfocusedBorderColor = FossBorderMedium
                                )
                            )

                            OutlinedTextField(
                                value = latestGitTag,
                                onValueChange = { latestGitTag = it },
                                label = { Text("Release Tag", color = FossTextSecondary) },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = FossTextPrimary,
                                    unfocusedTextColor = FossTextPrimary,
                                    focusedBorderColor = FossCyanPrimary,
                                    unfocusedBorderColor = FossBorderMedium
                                )
                            )
                        }

                        // Developer Name & Donation Links
                        OutlinedTextField(
                            value = developerName,
                            onValueChange = { developerName = it },
                            label = { Text("Developer / Team Name *", color = FossTextSecondary) },
                            placeholder = { Text("e.g. FreeGames Collective", color = FossTextTertiary) },
                            modifier = Modifier.fillMaxWidth().testTag("upload_dev_name_input"),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = FossTextPrimary,
                                unfocusedTextColor = FossTextPrimary,
                                focusedBorderColor = FossCyanPrimary,
                                unfocusedBorderColor = FossBorderMedium
                            )
                        )

                        OutlinedTextField(
                            value = donationLiberapay,
                            onValueChange = { donationLiberapay = it },
                            label = { Text("Liberapay URL (Optional)", color = FossTextSecondary) },
                            placeholder = { Text("https://liberapay.com/myteam", color = FossTextTertiary) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = FossTextPrimary,
                                unfocusedTextColor = FossTextPrimary,
                                focusedBorderColor = FossEmeraldGreen,
                                unfocusedBorderColor = FossBorderMedium
                            )
                        )

                        // Icon choice
                        Text("App Icon Symbol:", color = FossTextSecondary, fontSize = 11.sp)
                        Row(
                            modifier = Modifier.horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            iconChoices.forEach { (key, label) ->
                                val isSelected = iconKey == key
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) FossCyanPrimary.copy(alpha = 0.2f) else FossSurfaceElevated)
                                        .border(
                                            1.dp,
                                            if (isSelected) FossCyanPrimary else FossBorderSubtle,
                                            RoundedCornerShape(8.dp)
                                        )
                                        .clickable { iconKey = key }
                                        .padding(horizontal = 8.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = label,
                                        fontSize = 11.sp,
                                        color = if (isSelected) FossCyanLight else FossTextSecondary,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Submit Button
                        Button(
                            onClick = {
                                if (name.isNotBlank() && tagline.isNotBlank() && description.isNotBlank()) {
                                    viewModel.uploadApp(
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
                                    ) {
                                        showUploadForm = false
                                        name = ""
                                        tagline = ""
                                        description = ""
                                    }
                                }
                            },
                            enabled = name.isNotBlank() && tagline.isNotBlank() && description.isNotBlank() && !isUploading,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("submit_upload_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = FossCyanPrimary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Start Automated Build & Publish",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }

        // Community Uploaded Projects Section
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Your Published Projects (${userUploadedApps.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = FossTextPrimary
                )
                Text(
                    text = "Automated CI Active",
                    fontSize = 11.sp,
                    color = FossEmeraldLight,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        if (userUploadedApps.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = FossSurfaceDark),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(FossBorderMedium, FossBorderSubtle)))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudUpload,
                            contentDescription = null,
                            tint = FossTextTertiary,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No projects uploaded yet",
                            fontWeight = FontWeight.Bold,
                            color = FossTextSecondary,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "Tap 'Publish Project' above to upload your first open-source game or app with built-in Git sync and reproducible build verification.",
                            color = FossTextTertiary,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        } else {
            items(userUploadedApps, key = { it.id }) { app ->
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
}
