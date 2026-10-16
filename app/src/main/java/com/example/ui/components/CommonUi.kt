package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FossApp
import com.example.ui.theme.FossBorderMedium
import com.example.ui.theme.FossBorderSubtle
import com.example.ui.theme.FossCyanLight
import com.example.ui.theme.FossCyanPrimary
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

@Composable
fun AppIconBadge(
    iconKey: String,
    category: String,
    size: Int = 48,
    modifier: Modifier = Modifier
) {
    val (vector, bgBrush) = when (iconKey) {
        "dungeon", "sword" -> Icons.Default.Shield to Brush.linearGradient(listOf(Color(0xFF831843), Color(0xFFBE185D)))
        "factory" -> Icons.Default.Build to Brush.linearGradient(listOf(Color(0xFF78350F), Color(0xFFD97706)))
        "castle" -> Icons.Default.Security to Brush.linearGradient(listOf(Color(0xFF1E3A8A), Color(0xFF2563EB)))
        "rocket" -> Icons.Default.RocketLaunch to Brush.linearGradient(listOf(Color(0xFF4C1D95), Color(0xFF7C3AED)))
        "shield" -> Icons.Default.VerifiedUser to Brush.linearGradient(listOf(Color(0xFF064E3B), Color(0xFF059669)))
        "video" -> Icons.Default.Videocam to Brush.linearGradient(listOf(Color(0xFF7F1D1D), Color(0xFFDC2626)))
        "map" -> Icons.Default.Map to Brush.linearGradient(listOf(Color(0xFF065F46), Color(0xFF10B981)))
        "terminal" -> Icons.Default.Terminal to Brush.linearGradient(listOf(Color(0xFF111827), Color(0xFF374151)))
        else -> if (category == "Games") {
            Icons.Default.Gamepad to Brush.linearGradient(listOf(Color(0xFF581C87), Color(0xFF9333EA)))
        } else {
            Icons.Default.Code to Brush.linearGradient(listOf(Color(0xFF0E7490), Color(0xFF06B6D4)))
        }
    }

    Box(
        modifier = modifier
            .size(size.dp)
            .clip(RoundedCornerShape((size * 0.28).dp))
            .background(bgBrush)
            .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape((size * 0.28).dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = vector,
            contentDescription = "App Icon",
            tint = Color.White,
            modifier = Modifier.size((size * 0.55).dp)
        )
    }
}

@Composable
fun TrackerZeroPill(modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFF0369A1).copy(alpha = 0.25f))
            .border(1.dp, Color(0xFF38BDF8).copy(alpha = 0.4f), RoundedCornerShape(6.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Shield,
            contentDescription = null,
            tint = FossSkyBlue,
            modifier = Modifier.size(11.dp)
        )
        Spacer(modifier = Modifier.width(3.dp))
        Text(
            text = "0 Trackers",
            color = FossSkyBlue,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun ReproducibleBadgePill(modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFF064E3B).copy(alpha = 0.3f))
            .border(1.dp, FossEmeraldGreen.copy(alpha = 0.45f), RoundedCornerShape(6.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = FossEmeraldLight,
            modifier = Modifier.size(11.dp)
        )
        Spacer(modifier = Modifier.width(3.dp))
        Text(
            text = "Reproducible",
            color = FossEmeraldLight,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun LicensePill(license: String, modifier: Modifier = Modifier) {
    Text(
        text = license,
        color = FossCyanLight,
        fontSize = 10.sp,
        fontWeight = FontWeight.Medium,
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(FossCyanPrimary.copy(alpha = 0.15f))
            .border(1.dp, FossCyanPrimary.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    )
}

@Composable
fun AppListItem(
    app: FossApp,
    isInstalling: Boolean = false,
    installProgress: Float = 0f,
    onClick: () -> Unit,
    onInstallClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("app_item_${app.id}"),
        colors = CardDefaults.cardColors(containerColor = FossSurfaceDark),
        shape = RoundedCornerShape(14.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(
                listOf(FossBorderMedium, FossBorderSubtle)
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                AppIconBadge(iconKey = app.iconKey, category = app.category, size = 52)
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = app.name,
                            style = MaterialTheme.typography.titleMedium,
                            color = FossTextPrimary,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        if (app.isUserUploaded) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "COMMUNITY",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = FossPurpleAccent,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(FossPurpleAccent.copy(alpha = 0.15f))
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }
                    Text(
                        text = app.developerName,
                        style = MaterialTheme.typography.bodySmall,
                        color = FossTextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = app.tagline,
                        style = MaterialTheme.typography.bodySmall,
                        color = FossTextTertiary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                IconButton(
                    onClick = onBookmarkClick,
                    modifier = Modifier.size(36.dp).testTag("bookmark_${app.id}")
                ) {
                    Icon(
                        imageVector = if (app.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = if (app.isBookmarked) FossCyanPrimary else FossTextTertiary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Metadata pills & rating
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                TrackerZeroPill()
                if (app.reproducibleVerified) {
                    ReproducibleBadgePill()
                }
                LicensePill(app.license)

                Spacer(modifier = Modifier.weight(1f))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFBBF24),
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = String.format("%.1f", app.rating),
                        color = FossTextPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (isInstalling) {
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { installProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = FossCyanPrimary,
                    trackColor = FossBorderMedium
                )
            }
        }
    }
}

@Composable
fun GameFeaturedCard(
    app: FossApp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("game_featured_${app.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = FossSurfaceDark),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.linearGradient(
                listOf(FossCyanPrimary.copy(alpha = 0.5f), FossEmeraldGreen.copy(alpha = 0.5f))
            )
        )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header banner background
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xFF1E1B4B),
                                Color(0xFF0F172A),
                                Color(0xFF064E3B)
                            )
                        )
                    )
                    .padding(14.dp)
            ) {
                Column(
                    modifier = Modifier.align(Alignment.BottomStart)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "FEATURED OPEN SOURCE GAME",
                            color = FossCyanLight,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.Gamepad,
                            contentDescription = null,
                            tint = FossCyanLight,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                    Text(
                        text = app.name,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.Black.copy(alpha = 0.6f),
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFBBF24),
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = String.format("%.1f", app.rating),
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = app.tagline,
                    style = MaterialTheme.typography.bodyMedium,
                    color = FossTextSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    TrackerZeroPill()
                    ReproducibleBadgePill()
                    LicensePill(app.license)

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = app.buildSystem,
                        fontSize = 10.sp,
                        color = FossTextTertiary,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }
}
