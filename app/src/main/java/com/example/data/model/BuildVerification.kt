package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "build_verifications")
data class BuildVerification(
    @PrimaryKey
    val id: String,
    val appId: String,
    val buildNumber: Int,
    val gitTag: String,
    val commitSha: String,
    val status: String, // "VERIFIED", "IN_PROGRESS", "QUEUED", "FAILED"
    val reproducibilityPercentage: Int = 100, // 100% byte-for-byte reproducibility
    val sourceArchiveHash: String,
    val binaryArtifactHash: String,
    val scannersRun: Int = 18, // ClamAV, YARA rules, Exodus privacy scanner, SAST
    val threatsDetected: Int = 0,
    val securityScore: Int = 100,
    val verifiedBy: String = "OpenPlay Hermetic Verifier (Node #04)",
    val logOutput: String,
    val timestamp: Long
)
