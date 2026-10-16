package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reviews")
data class Review(
    @PrimaryKey
    val id: String,
    val appId: String,
    val author: String,
    val rating: Int, // 1 to 5
    val title: String,
    val comment: String,
    val timestamp: Long,
    val isAuditor: Boolean = false,
    val auditorRole: String = "", // "Reproducible Build Verifier", "Security Auditor", "FOSS Core Contributor"
    val auditedCommit: String = "",
    val helpfulVotes: Int = 0
)
