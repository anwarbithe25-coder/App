package com.example.data.remote

import com.example.data.model.FossApp
import com.example.data.model.Review
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@IgnoreExtraProperties
data class FirestoreApp(
    @DocumentId val id: String = "",
    val name: String = "",
    val tagline: String = "",
    val description: String = "",
    val packageName: String = "",
    val category: String = "",
    val subcategory: String = "",
    val license: String = "",
    val gitRepoUrl: String = "",
    val gitBranch: String = "main",
    val latestGitTag: String = "v1.0.0",
    val commitSha: String = "",
    val versionName: String = "1.0.0",
    val versionCode: Long = 1L,
    val apkSizeMb: Double = 0.0,
    val apkSha256: String = "",
    val reproducibleVerified: Boolean = true,
    val trackerCount: Long = 0L,
    val developerName: String = "",
    val userId: String = "",
    val rating: Double = 5.0,
    val reviewCount: Long = 0L,
    val downloadCount: Long = 0L,
    val buildSystem: String = "Gradle (Android)",
    val iconKey: String = "gamepad",
    val donationLiberapay: String = "",
    val donationGithub: String = "",
    val donationCrypto: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
) {
    fun toFossApp(): FossApp {
        return FossApp(
            id = id,
            packageName = packageName,
            name = name,
            tagline = tagline,
            description = description,
            category = category,
            subcategory = subcategory,
            license = license,
            gitRepoUrl = gitRepoUrl,
            gitBranch = gitBranch,
            latestGitTag = latestGitTag,
            commitSha = commitSha,
            versionName = versionName,
            versionCode = versionCode.toInt(),
            apkSizeMb = apkSizeMb,
            apkSha256 = apkSha256,
            reproducibleVerified = reproducibleVerified,
            trackerCount = trackerCount.toInt(),
            developerName = developerName,
            donationLiberapay = donationLiberapay,
            donationGithub = donationGithub,
            donationCrypto = donationCrypto,
            rating = rating.toFloat(),
            reviewCount = reviewCount.toInt(),
            downloadCount = downloadCount.toInt(),
            buildSystem = buildSystem,
            iconKey = iconKey,
            isUserUploaded = true,
            lastUpdated = updatedAt?.toDate()?.time ?: (createdAt?.toDate()?.time ?: System.currentTimeMillis())
        )
    }

    companion object {
        fun fromFossApp(app: FossApp, userId: String): Map<String, Any?> {
            return mapOf(
                "id" to app.id,
                "name" to app.name,
                "tagline" to app.tagline,
                "description" to app.description,
                "packageName" to app.packageName,
                "category" to app.category,
                "subcategory" to app.subcategory,
                "license" to app.license,
                "gitRepoUrl" to app.gitRepoUrl,
                "gitBranch" to app.gitBranch,
                "latestGitTag" to app.latestGitTag,
                "commitSha" to app.commitSha,
                "versionName" to app.versionName,
                "versionCode" to app.versionCode.toLong(),
                "apkSizeMb" to app.apkSizeMb,
                "apkSha256" to app.apkSha256,
                "reproducibleVerified" to app.reproducibleVerified,
                "trackerCount" to app.trackerCount.toLong(),
                "developerName" to app.developerName,
                "userId" to userId,
                "rating" to app.rating.toDouble(),
                "reviewCount" to app.reviewCount.toLong(),
                "downloadCount" to app.downloadCount.toLong(),
                "buildSystem" to app.buildSystem,
                "iconKey" to app.iconKey,
                "donationLiberapay" to app.donationLiberapay,
                "donationGithub" to app.donationGithub,
                "donationCrypto" to app.donationCrypto
            )
        }
    }
}

@IgnoreExtraProperties
data class FirestoreReview(
    @DocumentId val id: String = "",
    val appId: String = "",
    val userId: String = "",
    val author: String = "",
    val rating: Long = 5L,
    val title: String = "",
    val comment: String = "",
    val isAuditor: Boolean = false,
    val auditorRole: String = "",
    val auditedCommit: String = "",
    val helpfulVotes: Long = 0L,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
) {
    fun toReview(): Review {
        return Review(
            id = id,
            appId = appId,
            author = author,
            rating = rating.toInt(),
            title = title,
            comment = comment,
            timestamp = createdAt?.toDate()?.time ?: System.currentTimeMillis(),
            isAuditor = isAuditor,
            auditorRole = auditorRole,
            auditedCommit = auditedCommit,
            helpfulVotes = helpfulVotes.toInt()
        )
    }

    companion object {
        fun fromReview(review: Review, userId: String): Map<String, Any?> {
            return mapOf(
                "id" to review.id,
                "appId" to review.appId,
                "userId" to userId,
                "author" to review.author,
                "rating" to review.rating.toLong(),
                "title" to review.title,
                "comment" to review.comment,
                "isAuditor" to review.isAuditor,
                "auditorRole" to review.auditorRole,
                "auditedCommit" to review.auditedCommit,
                "helpfulVotes" to review.helpfulVotes.toLong()
            )
        }
    }
}
