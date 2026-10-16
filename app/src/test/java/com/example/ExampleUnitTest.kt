package com.example

import com.example.data.local.DefaultData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun sampleApps_containOpenSourceGamesAndPrivacyApps() {
        val apps = DefaultData.sampleApps
        assertTrue("Apps catalogue should not be empty", apps.isNotEmpty())

        val games = apps.filter { it.category == "Games" }
        assertTrue("Should contain open source games", games.size >= 4)

        val privacyApps = apps.filter { it.category == "Privacy & Security" }
        assertTrue("Should contain privacy apps", privacyApps.isNotEmpty())
    }

    @Test
    fun sampleApps_verifyZeroTrackersAndReproducibility() {
        for (app in DefaultData.sampleApps) {
            assertEquals("Apps must have 0 trackers", 0, app.trackerCount)
            assertTrue("Apps should have reproducible verified status", app.reproducibleVerified)
            assertTrue("Git repo URL must be specified", app.gitRepoUrl.isNotBlank())
            assertTrue("Latest Git tag must be specified", app.latestGitTag.isNotBlank())
            assertTrue("APK SHA-256 must be valid length", app.apkSha256.length >= 32)
        }
    }

    @Test
    fun buildVerifications_haveZeroMalwareThreats() {
        val verifications = DefaultData.sampleBuildVerifications
        assertTrue("Should have build verifications", verifications.isNotEmpty())
        for (bv in verifications) {
            assertEquals("Threats detected must be 0", 0, bv.threatsDetected)
            assertEquals("Reproducibility percentage must be 100", 100, bv.reproducibilityPercentage)
            assertTrue("Build log must be present", bv.logOutput.isNotBlank())
        }
    }

    @Test
    fun gitCommits_haveGpgSignatures() {
        val commits = DefaultData.sampleCommits
        assertTrue("Should have sample Git commits", commits.isNotEmpty())
        for (commit in commits) {
            assertTrue("Git commits should have valid sha", commit.sha.length >= 7)
            assertTrue("Git commit should be GPG verified", commit.isGpgVerified)
        }
    }
}
