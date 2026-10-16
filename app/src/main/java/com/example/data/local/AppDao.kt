package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.BuildVerification
import com.example.data.model.DeveloperDonation
import com.example.data.model.FossApp
import com.example.data.model.GitCommit
import com.example.data.model.Review
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Query("SELECT * FROM apps ORDER BY downloadCount DESC")
    fun getAllApps(): Flow<List<FossApp>>

    @Query("SELECT * FROM apps WHERE category = :category ORDER BY rating DESC")
    fun getAppsByCategory(category: String): Flow<List<FossApp>>

    @Query("SELECT * FROM apps WHERE id = :appId")
    fun getAppById(appId: String): Flow<FossApp?>

    @Query("SELECT * FROM apps WHERE id = :appId LIMIT 1")
    suspend fun getAppByIdDirect(appId: String): FossApp?

    @Query("SELECT * FROM apps WHERE isInstalled = 1")
    fun getInstalledApps(): Flow<List<FossApp>>

    @Query("SELECT * FROM apps WHERE isBookmarked = 1")
    fun getBookmarkedApps(): Flow<List<FossApp>>

    @Query("SELECT * FROM apps WHERE isUserUploaded = 1")
    fun getUserUploadedApps(): Flow<List<FossApp>>

    @Query("SELECT * FROM apps WHERE name LIKE '%' || :query || '%' OR tagline LIKE '%' || :query || '%' OR category LIKE '%' || :query || '%'")
    fun searchApps(query: String): Flow<List<FossApp>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApps(apps: List<FossApp>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApp(app: FossApp)

    @Update
    suspend fun updateApp(app: FossApp)

    // Reviews
    @Query("SELECT * FROM reviews WHERE appId = :appId ORDER BY timestamp DESC")
    fun getReviewsForApp(appId: String): Flow<List<Review>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: Review)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReviews(reviews: List<Review>)

    @Query("UPDATE reviews SET helpfulVotes = helpfulVotes + 1 WHERE id = :reviewId")
    suspend fun upvoteReview(reviewId: String)

    // Git Commits
    @Query("SELECT * FROM git_commits WHERE appId = :appId ORDER BY timestamp DESC")
    fun getCommitsForApp(appId: String): Flow<List<GitCommit>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCommits(commits: List<GitCommit>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCommit(commit: GitCommit)

    // Build Verifications
    @Query("SELECT * FROM build_verifications WHERE appId = :appId ORDER BY timestamp DESC")
    fun getBuildVerificationsForApp(appId: String): Flow<List<BuildVerification>>

    @Query("SELECT * FROM build_verifications WHERE appId = :appId ORDER BY timestamp DESC LIMIT 1")
    fun getLatestBuildVerification(appId: String): Flow<BuildVerification?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBuildVerification(verification: BuildVerification)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBuildVerifications(verifications: List<BuildVerification>)

    // Donations
    @Query("SELECT * FROM donations ORDER BY timestamp DESC")
    fun getAllDonations(): Flow<List<DeveloperDonation>>

    @Query("SELECT * FROM donations WHERE appId = :appId ORDER BY timestamp DESC")
    fun getDonationsForApp(appId: String): Flow<List<DeveloperDonation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDonation(donation: DeveloperDonation)
}
