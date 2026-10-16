package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AppListItem
import com.example.ui.components.GameFeaturedCard
import com.example.ui.theme.FossBorderMedium
import com.example.ui.theme.FossBorderSubtle
import com.example.ui.theme.FossCyanLight
import com.example.ui.theme.FossCyanPrimary
import com.example.ui.theme.FossDarkBg
import com.example.ui.theme.FossEmeraldGreen
import com.example.ui.theme.FossEmeraldLight
import com.example.ui.theme.FossPurpleAccent
import com.example.ui.theme.FossRoseAccent
import com.example.ui.theme.FossSurfaceDark
import com.example.ui.theme.FossSurfaceElevated
import com.example.ui.theme.FossTextPrimary
import com.example.ui.theme.FossTextSecondary
import com.example.ui.theme.FossTextTertiary
import com.example.ui.viewmodel.OpenPlayViewModel

@Composable
fun GamesScreen(
    viewModel: OpenPlayViewModel,
    onAppClick: (String) -> Unit,
    onUploadGameClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val games by viewModel.openSourceGames.collectAsState()
    val installingAppId by viewModel.installingAppId.collectAsState()
    val installProgress by viewModel.installProgress.collectAsState()

    var selectedGenre by remember { mutableStateOf("All Genres") }
    val genres = listOf(
        "All Genres",
        "Roguelike RPG",
        "Strategy & Logistics",
        "Voxel RPG",
        "Turn-based Strategy",
        "3D Racing Arcade"
    )

    val filteredGames = games.filter { game ->
        selectedGenre == "All Genres" || game.subcategory.contains(selectedGenre, ignoreCase = true)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(FossDarkBg)
            .testTag("games_screen"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Header
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
                                imageVector = Icons.Default.SportsEsports,
                                contentDescription = null,
                                tint = FossPurpleAccent,
                                modifier = Modifier.size(26.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Libre Games",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = FossTextPrimary
                            )
                        }
                        Text(
                            text = "No microtransactions • Zero ads • Full source code",
                            style = MaterialTheme.typography.bodySmall,
                            color = FossTextSecondary
                        )
                    }

                    Button(
                        onClick = onUploadGameClick,
                        colors = ButtonDefaults.buttonColors(containerColor = FossPurpleAccent),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("upload_game_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Upload Game", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Philosophy Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = FossSurfaceDark),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.horizontalGradient(
                            listOf(FossPurpleAccent.copy(alpha = 0.5f), FossCyanPrimary.copy(alpha = 0.5f))
                        )
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(FossPurpleAccent.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Gamepad,
                                contentDescription = null,
                                tint = FossPurpleAccent,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "100% Community-Owned Gaming",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 13.sp
                            )
                            Text(
                                text = "Every listed game is built from public Git repos with reproducible releases.",
                                color = FossTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }

        // Genre Filter Chips
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                genres.forEach { genre ->
                    val isSelected = selectedGenre == genre
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedGenre = genre },
                        label = {
                            Text(
                                text = genre,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = FossPurpleAccent,
                            selectedLabelColor = Color.White,
                            containerColor = FossSurfaceDark,
                            labelColor = FossTextSecondary
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = FossBorderMedium,
                            selectedBorderColor = FossPurpleAccent
                        )
                    )
                }
            }
        }

        // Games List Count
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Open Source Games (${filteredGames.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = FossTextPrimary
                )
                Text(
                    text = "Malware-Free Verified",
                    fontSize = 11.sp,
                    color = FossEmeraldLight,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Games list
        items(filteredGames, key = { it.id }) { game ->
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                AppListItem(
                    app = game,
                    isInstalling = installingAppId == game.id,
                    installProgress = installProgress,
                    onClick = { onAppClick(game.id) },
                    onInstallClick = { viewModel.installApp(game) },
                    onBookmarkClick = { viewModel.toggleBookmark(game) }
                )
            }
        }
    }
}
