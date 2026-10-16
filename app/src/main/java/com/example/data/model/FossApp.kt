package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "apps")
data class FossApp(
    @PrimaryKey
    val id: String,
    val packageName: String,
    val name: String,
    val tagline: String,
    val description: String,
    val category: String, // "Games", "Privacy & Security", "Utilities", "Media", "Dev Tools"
    val subcategory: String = "",
    val license: String, // "GPL-3.0-or-later", "MIT", "Apache-2.0", "AGPL-3.0"
    val gitRepoUrl: String,
    val gitBranch: String = "main",
    val latestGitTag: String = "v1.0.0",
    val commitSha: String = "a1b2c3d",
    val versionName: String = "1.0.0",
    val versionCode: Int = 10,
    val apkSizeMb: Double = 24.5,
    val apkSha256: String = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
    val reproducibleVerified: Boolean = true,
    val trackerCount: Int = 0, // Exodus tracker analysis (0 is FOSS gold standard)
    val antiFeatures: String = "", // Comma-separated or empty
    val developerName: String,
    val developerAvatar: String = "",
    val donationLiberapay: String = "",
    val donationGithub: String = "",
    val donationKofi: String = "",
    val donationCrypto: String = "",
    val rating: Float = 4.8f,
    val reviewCount: Int = 128,
    val downloadCount: Int = 15400,
    val isInstalled: Boolean = false,
    val installedVersion: String = "",
    val isBookmarked: Boolean = false,
    val isUserUploaded: Boolean = false,
    val buildSystem: String = "Gradle (Android)", // "Gradle (Android)", "Godot 4.x Headless", "Rust / Cargo-NDK", "CMake / NDK"
    val iconKey: String = "gamepad", // icon identifier or drawable name
    val bannerUrl: String = "",
    val lastUpdated: Long = System.currentTimeMillis()
)
