package com.example.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.base.FirestoreEmulatorTestBase
import com.example.data.local.AppDatabase
import com.example.data.model.FossApp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import java.util.UUID

class OpenPlayRepositoryRuleTest : FirestoreEmulatorTestBase() {

  private lateinit var inMemoryDb: AppDatabase
  private lateinit var repository: OpenPlayRepository

  @Before
  override fun setUpFirebase() {
    super.setUpFirebase()
    val context = ApplicationProvider.getApplicationContext<Context>()
    inMemoryDb = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
      .allowMainThreadQueries()
      .build()
    repository = OpenPlayRepository(inMemoryDb.appDao(), firestore, auth)
  }

  @After
  override fun tearDownFirebase() {
    super.tearDownFirebase()
    inMemoryDb.close()
  }

  @Test
  fun uploadUserApp_authenticatedUser_publishesToRoomAndFirestore() = runBlocking {
    val uid = signInTestUser(ALICE_EMAIL)

    val createdApp = withTimeout<FossApp>(DEFAULT_TIMEOUT_MS) {
      repository.uploadUserApp(
        name = "Space Rogue FOSS",
        tagline = "Procedural Libre roguelike",
        description = "Fully open source game with zero trackers",
        category = "Games",
        subcategory = "Roguelike",
        license = "GPL-3.0-or-later",
        gitRepoUrl = "https://github.com/alice/space-rogue",
        gitBranch = "main",
        latestGitTag = "v1.0.0",
        buildSystem = "Gradle (Android)",
        developerName = "Alice Dev",
        donationLiberapay = "https://liberapay.com/alice",
        donationGithub = "https://github.com/sponsors/alice",
        donationCrypto = "bc1qtest123",
        iconKey = "dungeon"
      )
    }

    assertEquals("Space Rogue FOSS", createdApp.name)

    // Verify presence in local Room
    val roomApp = withTimeout(DEFAULT_TIMEOUT_MS) {
      repository.getAppById(createdApp.id).first()
    }
    assertNotNull(roomApp)
    assertEquals("Space Rogue FOSS", roomApp?.name)

    // Verify document published in Firestore
    val firestoreDoc = withTimeout(DEFAULT_TIMEOUT_MS) {
      firestore.collection("apps").document(createdApp.id).get().await()
    }
    assertTrue(firestoreDoc.exists())
    assertEquals("Space Rogue FOSS", firestoreDoc.getString("name"))
    assertEquals(uid, firestoreDoc.getString("userId"))
  }

  @Test
  fun addReview_authenticatedUser_persistsReviewAndUpdatesApp() = runBlocking {
    val uid = signInTestUser(ALICE_EMAIL)

    val app = withTimeout<FossApp>(DEFAULT_TIMEOUT_MS) {
      repository.uploadUserApp(
        name = "Libre Calc",
        tagline = "Open source scientific calculator",
        description = "Clean calculator with privacy guarantee",
        category = "Tools",
        subcategory = "Productivity",
        license = "Apache-2.0",
        gitRepoUrl = "https://github.com/alice/libre-calc",
        gitBranch = "main",
        latestGitTag = "v1.0.0",
        buildSystem = "Gradle (Android)",
        developerName = "Alice Dev",
        donationLiberapay = "",
        donationGithub = "",
        donationCrypto = "",
        iconKey = "calculator"
      )
    }

    // Add review
    withTimeout(DEFAULT_TIMEOUT_MS) {
      repository.addReview(
        appId = app.id,
        author = "Security Auditor Bob",
        rating = 5,
        title = "Code Audit Passed",
        comment = "Verified zero proprietary binaries. Reproducible build byte-identical.",
        isAuditor = true,
        auditorRole = "Lead Security Researcher"
      )
    }

    val reviews = withTimeout(DEFAULT_TIMEOUT_MS) {
      repository.getReviews(app.id).first()
    }
    assertTrue(reviews.any { it.title == "Code Audit Passed" })
  }

  @Test
  fun unauthenticatedUser_cannotWriteToFirestoreDirectly() = runBlocking {
    auth.signOut()

    val fakeAppId = "unauth-" + UUID.randomUUID().toString().take(8)
    try {
      withTimeout(DEFAULT_TIMEOUT_MS) {
        firestore.collection("apps").document(fakeAppId).set(
          mapOf(
            "id" to fakeAppId,
            "name" to "Unauth App",
            "tagline" to "Hacked",
            "packageName" to "org.openplay.hacked",
            "category" to "Tools",
            "license" to "MIT",
            "gitRepoUrl" to "https://github.com/hacker/bad",
            "userId" to "spoofed_uid",
            "createdAt" to FieldValue.serverTimestamp()
          )
        ).await()
      }
      fail("Expected PERMISSION_DENIED exception for unauthenticated write")
    } catch (e: FirebaseFirestoreException) {
      assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, e.code)
    }
  }

  @Test
  fun crossUser_cannotDeleteOrHijackAnotherUsersApp() = runBlocking {
    val aliceUid = signInTestUser(ALICE_EMAIL)
    val appId = "alice-app-" + UUID.randomUUID().toString().take(8)

    // Alice creates app
    withTimeout(DEFAULT_TIMEOUT_MS) {
      firestore.collection("apps").document(appId).set(
        mapOf(
          "id" to appId,
          "name" to "Alice Secure App",
          "tagline" to "Encrypted Notes",
          "packageName" to "org.openplay.secnotes",
          "category" to "Security",
          "license" to "GPL-3.0-or-later",
          "gitRepoUrl" to "https://github.com/alice/secnotes",
          "userId" to aliceUid,
          "createdAt" to FieldValue.serverTimestamp()
        )
      ).await()
    }

    // Bob signs in and attempts to delete Alice's app
    signInTestUser(BOB_EMAIL)
    try {
      withTimeout(DEFAULT_TIMEOUT_MS) {
        firestore.collection("apps").document(appId).delete().await()
      }
      fail("Expected PERMISSION_DENIED exception when deleting another user's app")
    } catch (e: FirebaseFirestoreException) {
      assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, e.code)
    }
  }

  private companion object {
    const val ALICE_EMAIL = "alice@openplay.org"
    const val BOB_EMAIL = "bob@openplay.org"
    const val DEFAULT_TIMEOUT_MS = 6000L
  }
}
