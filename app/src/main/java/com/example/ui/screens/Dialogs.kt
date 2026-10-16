package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.FossApp
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

@Composable
fun ReviewDialog(
    app: FossApp,
    onDismiss: () -> Unit,
    onSubmit: (author: String, rating: Int, title: String, comment: String, isAuditor: Boolean, role: String) -> Unit
) {
    var rating by remember { mutableIntStateOf(5) }
    var author by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var comment by remember { mutableStateOf("") }
    var isAuditor by remember { mutableStateOf(false) }
    var auditorRole by remember { mutableStateOf("Reproducible Build Verifier") }

    val auditorRoles = listOf(
        "Reproducible Build Verifier",
        "Security Auditor",
        "FOSS Core Contributor"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = FossSurfaceDark,
        title = {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = FossCyanPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Community Review & Audit",
                        color = FossTextPrimary,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Text(
                    text = "Reviewing ${app.name} (${app.latestGitTag})",
                    color = FossTextSecondary,
                    fontSize = 12.sp
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Star rating row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in 1..5) {
                        IconButton(
                            onClick = { rating = i },
                            modifier = Modifier.testTag("star_rate_$i")
                        ) {
                            Icon(
                                imageVector = if (i <= rating) Icons.Default.Star else Icons.Outlined.Star,
                                contentDescription = "$i stars",
                                tint = if (i <= rating) Color(0xFFFBBF24) else FossBorderMedium,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = author,
                    onValueChange = { author = it },
                    label = { Text("Your Name or Handle", color = FossTextSecondary) },
                    placeholder = { Text("e.g., LibreCoder", color = FossTextTertiary) },
                    modifier = Modifier.fillMaxWidth().testTag("review_author_input"),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = FossTextPrimary,
                        unfocusedTextColor = FossTextPrimary,
                        focusedBorderColor = FossCyanPrimary,
                        unfocusedBorderColor = FossBorderMedium
                    )
                )

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Review / Audit Summary", color = FossTextSecondary) },
                    placeholder = { Text("e.g. 100% reproducible and clean permissions", color = FossTextTertiary) },
                    modifier = Modifier.fillMaxWidth().testTag("review_title_input"),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = FossTextPrimary,
                        unfocusedTextColor = FossTextPrimary,
                        focusedBorderColor = FossCyanPrimary,
                        unfocusedBorderColor = FossBorderMedium
                    )
                )

                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    label = { Text("Detailed Community Feedback", color = FossTextSecondary) },
                    placeholder = { Text("Describe app performance, privacy audit notes, gameplay...", color = FossTextTertiary) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .testTag("review_comment_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = FossTextPrimary,
                        unfocusedTextColor = FossTextPrimary,
                        focusedBorderColor = FossCyanPrimary,
                        unfocusedBorderColor = FossBorderMedium
                    )
                )

                // Auditor toggle
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(FossSurfaceElevated)
                        .clickable { isAuditor = !isAuditor }
                        .padding(8.dp)
                ) {
                    Checkbox(
                        checked = isAuditor,
                        onCheckedChange = { isAuditor = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = FossEmeraldGreen,
                            uncheckedColor = FossBorderMedium
                        ),
                        modifier = Modifier.testTag("review_auditor_checkbox")
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = "I am a Code Contributor / Auditor",
                            color = FossTextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Adds verified auditor badge & commit stamp",
                            color = FossEmeraldLight,
                            fontSize = 10.sp
                        )
                    }
                }

                if (isAuditor) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Select Auditor Role:", color = FossTextSecondary, fontSize = 11.sp)
                        auditorRoles.forEach { role ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (auditorRole == role) FossCyanPrimary.copy(alpha = 0.2f) else Color.Transparent)
                                    .clickable { auditorRole = role }
                                    .padding(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                if (auditorRole == role) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = FossCyanPrimary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                }
                                Text(
                                    text = role,
                                    color = if (auditorRole == role) FossCyanLight else FossTextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank() && comment.isNotBlank()) {
                        onSubmit(author, rating, title, comment, isAuditor, auditorRole)
                        onDismiss()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = FossCyanPrimary),
                enabled = title.isNotBlank() && comment.isNotBlank(),
                modifier = Modifier.testTag("submit_review_button")
            ) {
                Text("Publish Review", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = FossTextSecondary)
            }
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DonationDialog(
    app: FossApp,
    onDismiss: () -> Unit,
    onTipCompleted: (amount: Double, platform: String, message: String) -> Unit
) {
    var tipAmount by remember { mutableDoubleStateOf(10.0) }
    var customAmountText by remember { mutableStateOf("") }
    var selectedPlatform by remember { mutableStateOf("Direct Supporter Tip") }
    var message by remember { mutableStateOf("Thank you for developing open-source software!") }
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    val presetAmounts = listOf(2.0, 5.0, 10.0, 25.0, 50.0)

    val currentBadge = when {
        tipAmount >= 50 -> "Freedom Champion"
        tipAmount >= 20 -> "Gold Patron"
        tipAmount >= 10 -> "Silver Sponsor"
        else -> "Bronze Backer"
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = FossSurfaceDark),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.linearGradient(listOf(FossEmeraldGreen, FossCyanPrimary))
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
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
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Support ${app.developerName}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = FossTextPrimary
                        )
                        Text(
                            text = "Direct funding for ${app.name}",
                            style = MaterialTheme.typography.bodySmall,
                            color = FossTextSecondary
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = FossTextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tier Badge Banner
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(FossSurfaceElevated)
                        .border(1.dp, FossBorderMedium, RoundedCornerShape(10.dp))
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = when (currentBadge) {
                            "Freedom Champion" -> Color(0xFFF43F5E)
                            "Gold Patron" -> Color(0xFFFBBF24)
                            "Silver Sponsor" -> Color(0xFF94A3B8)
                            else -> Color(0xFFD97706)
                        },
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Supporter Tier: $currentBadge",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = FossTextPrimary
                        )
                        Text(
                            text = "100% of proceeds go directly to developer with 0 platform fees.",
                            fontSize = 10.sp,
                            color = FossEmeraldLight
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Preset tip amounts
                Text("Select Tip Amount (USD):", color = FossTextSecondary, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    presetAmounts.forEach { amt ->
                        val isSelected = tipAmount == amt
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) FossEmeraldGreen else FossSurfaceElevated)
                                .border(
                                    1.dp,
                                    if (isSelected) FossEmeraldLight else FossBorderMedium,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable {
                                    tipAmount = amt
                                    customAmountText = ""
                                }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$${amt.toInt()}",
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.Black else FossTextPrimary,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Platform selection
                Text("Donation Channel:", color = FossTextSecondary, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val platforms = listOf(
                        "Direct Supporter Tip",
                        "Liberapay",
                        "GitHub Sponsors",
                        "Crypto (XMR/BTC)"
                    )
                    platforms.forEach { plat ->
                        val isSelected = selectedPlatform == plat
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) FossCyanPrimary.copy(alpha = 0.2f) else FossSurfaceElevated)
                                .border(
                                    1.dp,
                                    if (isSelected) FossCyanPrimary else FossBorderMedium,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { selectedPlatform = plat }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = plat,
                                color = if (isSelected) FossCyanLight else FossTextSecondary,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Developer's external public links
                if (app.donationLiberapay.isNotBlank() || app.donationGithub.isNotBlank() || app.donationCrypto.isNotBlank()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(FossDarkBg)
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Developer's Verified Addresses:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = FossTextSecondary
                        )
                        if (app.donationLiberapay.isNotBlank()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Liberapay: ${app.donationLiberapay}", color = FossCyanLight, fontSize = 10.sp, modifier = Modifier.weight(1f))
                                IconButton(
                                    onClick = {
                                        clipboardManager.setText(AnnotatedString(app.donationLiberapay))
                                        Toast.makeText(context, "Copied Liberapay link!", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = FossCyanPrimary, modifier = Modifier.size(14.dp))
                                }
                            }
                        }
                        if (app.donationCrypto.isNotBlank()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Crypto: ${app.donationCrypto.take(18)}...", color = FossEmeraldLight, fontSize = 10.sp, modifier = Modifier.weight(1f))
                                IconButton(
                                    onClick = {
                                        clipboardManager.setText(AnnotatedString(app.donationCrypto))
                                        Toast.makeText(context, "Copied Crypto Address!", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = FossEmeraldLight, modifier = Modifier.size(14.dp))
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Supporter Message
                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    label = { Text("Note to Developer", color = FossTextSecondary) },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = FossTextPrimary,
                        unfocusedTextColor = FossTextPrimary,
                        focusedBorderColor = FossEmeraldGreen,
                        unfocusedBorderColor = FossBorderMedium
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Submit Button
                Button(
                    onClick = {
                        onTipCompleted(tipAmount, selectedPlatform, message)
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("confirm_donation_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = FossEmeraldGreen),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Send $${tipAmount.toInt()} Tip to Developer",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun BuildVerificationLogDialog(
    app: FossApp,
    logOutput: String,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 560.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF030712)),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.linearGradient(listOf(FossEmeraldGreen, FossCyanPrimary))
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Terminal,
                        contentDescription = null,
                        tint = FossEmeraldGreen,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Reproducible Build Logs",
                            color = FossTextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "${app.name} (${app.latestGitTag}) • Commit ${app.commitSha}",
                            color = FossTextTertiary,
                            fontSize = 11.sp
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = FossTextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF0B1120))
                        .border(1.dp, FossBorderMedium, RoundedCornerShape(8.dp))
                        .padding(12.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = logOutput,
                        color = FossEmeraldLight,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = FossSkyBlue,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "18 Security Heuristics Passed",
                            color = FossSkyBlue,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = FossSurfaceElevated)
                    ) {
                        Text("Close Inspector", color = FossTextPrimary, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
