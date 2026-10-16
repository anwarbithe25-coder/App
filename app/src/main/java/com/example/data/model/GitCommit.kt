package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "git_commits")
data class GitCommit(
    @PrimaryKey
    val id: String,
    val appId: String,
    val sha: String,
    val tag: String = "",
    val branch: String = "main",
    val author: String,
    val message: String,
    val timestamp: Long,
    val isGpgVerified: Boolean = true,
    val gpgKeyId: String = "0x9E72BD1A",
    val filesChanged: Int = 4,
    val additions: Int = 142,
    val deletions: Int = 18
)
