package com.example.data.local

import com.example.data.model.BuildVerification
import com.example.data.model.DeveloperDonation
import com.example.data.model.FossApp
import com.example.data.model.GitCommit
import com.example.data.model.Review

object DefaultData {

    val sampleApps = listOf(
        FossApp(
            id = "game-shattered-pd",
            packageName = "com.shatteredpixel.shatteredpixeldungeon",
            name = "Shattered Pixel Dungeon",
            tagline = "Traditional rogue-like dungeon crawler with high replayability",
            description = "Shattered Pixel Dungeon is an open-source traditional roguelike RPG with pixel art graphics and lots of variety. Each run is a unique challenge, with four different playable heroes, randomized levels and enemies, and hundreds of items to collect and use. Fully libre, no ads or trackers.",
            category = "Games",
            subcategory = "Roguelike RPG",
            license = "GPL-3.0-or-later",
            gitRepoUrl = "https://github.com/00-Evan/shattered-pixel-dungeon",
            gitBranch = "master",
            latestGitTag = "v2.4.2",
            commitSha = "3e9b7a4f",
            versionName = "2.4.2",
            versionCode = 624,
            apkSizeMb = 16.4,
            apkSha256 = "9b642e038d8ec4f51bc72e01b356f91cbca12e340a97cbfb40a33a1e2fbc7891",
            reproducibleVerified = true,
            trackerCount = 0,
            antiFeatures = "",
            developerName = "Evan Debenham",
            developerAvatar = "https://avatars.githubusercontent.com/u/00-Evan",
            donationLiberapay = "https://liberapay.com/Evan",
            donationGithub = "https://github.com/sponsors/00-Evan",
            donationKofi = "https://ko-fi.com/shatteredpixel",
            donationCrypto = "888tNkZrPN6JsEgekjMnABU4TBmb2...",
            rating = 4.9f,
            reviewCount = 842,
            downloadCount = 142000,
            isInstalled = true,
            installedVersion = "2.4.2",
            isBookmarked = true,
            isUserUploaded = false,
            buildSystem = "Gradle (Android)",
            iconKey = "dungeon"
        ),
        FossApp(
            id = "game-mindustry",
            packageName = "io.anuke.mindustry",
            name = "Mindustry",
            tagline = "Open-source sandbox tower defense & industrial logistics game",
            description = "Build elaborate supply chains of conveyor belts to feed ammo into your turrets, produce materials to use for building, and defend your structures from waves of enemies. Includes multiplayer, custom map editor, and automated tech trees.",
            category = "Games",
            subcategory = "Strategy & Logistics",
            license = "GPL-3.0-only",
            gitRepoUrl = "https://github.com/Anuken/Mindustry",
            gitBranch = "master",
            latestGitTag = "v146.0",
            commitSha = "8c21e5fa",
            versionName = "146.0",
            versionCode = 146,
            apkSizeMb = 64.2,
            apkSha256 = "3f82cd98aa4512e9b0485cd0102e3b8a1c900e42d640fb8725164bc98e10034a",
            reproducibleVerified = true,
            trackerCount = 0,
            antiFeatures = "",
            developerName = "Anuke",
            developerAvatar = "https://avatars.githubusercontent.com/u/Anuken",
            donationGithub = "https://github.com/sponsors/Anuken",
            donationKofi = "https://ko-fi.com/anuke",
            rating = 4.8f,
            reviewCount = 520,
            downloadCount = 98500,
            isInstalled = false,
            isBookmarked = true,
            isUserUploaded = false,
            buildSystem = "Gradle (Android)",
            iconKey = "factory"
        ),
        FossApp(
            id = "game-veloren",
            packageName = "net.veloren.mobile",
            name = "Veloren Mobile Client",
            tagline = "Multiplayer voxel RPG built with Rust and Bevy Engine",
            description = "Veloren is an open-world, open-source multiplayer voxel RPG inspired by Cube World, Legend of Zelda: Breath of the Wild, Dwarf Fortress, and Minecraft. Fully community-driven, written in Rust for exceptional performance and memory safety.",
            category = "Games",
            subcategory = "Voxel RPG",
            license = "GPL-3.0-or-later",
            gitRepoUrl = "https://gitlab.com/veloren/veloren",
            gitBranch = "master",
            latestGitTag = "v0.16.0",
            commitSha = "d5f178bc",
            versionName = "0.16.0",
            versionCode = 160,
            apkSizeMb = 88.5,
            apkSha256 = "c081297e5102ff94a4c52089ebcd81a28a901047683921b7c1258ef9c52b0a1d",
            reproducibleVerified = true,
            trackerCount = 0,
            antiFeatures = "",
            developerName = "Veloren Core Team",
            donationLiberapay = "https://liberapay.com/Veloren",
            donationGithub = "https://opencollective.com/veloren",
            rating = 4.7f,
            reviewCount = 310,
            downloadCount = 45000,
            isInstalled = false,
            isBookmarked = false,
            isUserUploaded = false,
            buildSystem = "Rust / Cargo-NDK",
            iconKey = "sword"
        ),
        FossApp(
            id = "game-unciv",
            packageName = "com.unciv.app",
            name = "Unciv",
            tagline = "Fast, lightweight open-source 4X civilization building game",
            description = "Unciv is a remake of the classic 4X turn-based strategy game for Android and Desktop. Manage your cities, research technologies, build armies, trade with other civilizations, and conquer the world or achieve a diplomatic victory.",
            category = "Games",
            subcategory = "Turn-based Strategy",
            license = "MPL-2.0",
            gitRepoUrl = "https://github.com/yairm210/Unciv",
            gitBranch = "master",
            latestGitTag = "v4.11.14",
            commitSha = "4a9108bc",
            versionName = "4.11.14",
            versionCode = 914,
            apkSizeMb = 21.0,
            apkSha256 = "e87c0a941bf280598cd7216a943bc091249bce781b490a61250cf411893de790",
            reproducibleVerified = true,
            trackerCount = 0,
            antiFeatures = "",
            developerName = "Yair Morgenstern",
            donationGithub = "https://github.com/sponsors/yairm210",
            donationKofi = "https://ko-fi.com/yairm",
            rating = 4.8f,
            reviewCount = 412,
            downloadCount = 89000,
            isInstalled = false,
            isBookmarked = false,
            isUserUploaded = false,
            buildSystem = "Gradle (Android)",
            iconKey = "castle"
        ),
        FossApp(
            id = "game-supertuxkart",
            packageName = "org.supertuxkart.stk",
            name = "SuperTuxKart Mobile",
            tagline = "Action-packed 3D open-source arcade kart racing",
            description = "Race with Tux the penguin and fellow open source mascots! Featuring over 20 detailed tracks, battle arenas, Grand Prix tournaments, split-screen, and online cross-platform multiplayer. Pure open source fun without microtransactions.",
            category = "Games",
            subcategory = "3D Racing Arcade",
            license = "GPL-3.0-or-later",
            gitRepoUrl = "https://github.com/supertuxkart/stk-code",
            gitBranch = "master",
            latestGitTag = "v1.4.0",
            commitSha = "79cb2091",
            versionName = "1.4.0",
            versionCode = 140,
            apkSizeMb = 158.0,
            apkSha256 = "120894cae0921bf38650a297cba4005b82194857b290cd456912389e0cb7814a",
            reproducibleVerified = true,
            trackerCount = 0,
            antiFeatures = "",
            developerName = "SuperTuxKart Development Team",
            donationLiberapay = "https://liberapay.com/SuperTuxKart",
            rating = 4.6f,
            reviewCount = 280,
            downloadCount = 67000,
            isInstalled = false,
            isBookmarked = false,
            isUserUploaded = false,
            buildSystem = "CMake / NDK",
            iconKey = "rocket"
        ),
        FossApp(
            id = "app-aegis",
            packageName = "com.beemdevelopment.aegis",
            name = "Aegis Authenticator",
            tagline = "Secure, privacy-focused open source 2FA app for Android",
            description = "Aegis Authenticator is a free, secure and open source app for managing your 2-step verification tokens. Supports HOTP and TOTP, encrypted backups, biometric unlock, and custom icon packs. Completely tracker-free.",
            category = "Privacy & Security",
            subcategory = "Authentication",
            license = "GPL-3.0-or-later",
            gitRepoUrl = "https://github.com/beemdevelopment/Aegis",
            gitBranch = "master",
            latestGitTag = "v3.2.1",
            commitSha = "5f28a9b1",
            versionName = "3.2.1",
            versionCode = 83,
            apkSizeMb = 14.2,
            apkSha256 = "48390bca8210984ef298104859a0981e4b859012895cd4502891ba5f98a28091",
            reproducibleVerified = true,
            trackerCount = 0,
            antiFeatures = "",
            developerName = "Beem Development",
            donationLiberapay = "https://liberapay.com/Aegis-Authenticator",
            donationCrypto = "bc1qar0srrr7xfkvy5l643lydnw9re59gtzzwf5mdq",
            rating = 4.9f,
            reviewCount = 950,
            downloadCount = 210000,
            isInstalled = true,
            installedVersion = "3.2.1",
            isBookmarked = true,
            isUserUploaded = false,
            buildSystem = "Gradle (Android)",
            iconKey = "shield"
        ),
        FossApp(
            id = "app-newpipe",
            packageName = "org.schabi.newpipe",
            name = "NewPipe",
            tagline = "Lightweight, privacy-first streaming media client",
            description = "NewPipe was created to get the original streaming experience on your smartphone without annoying ads and questionable permissions. Does not use proprietary Google Play Services APIs, and only sends data required to fetch video/audio.",
            category = "Media",
            subcategory = "Audio & Video",
            license = "GPL-3.0-or-later",
            gitRepoUrl = "https://github.com/TeamNewPipe/NewPipe",
            gitBranch = "dev",
            latestGitTag = "v0.27.2",
            commitSha = "98ecb321",
            versionName = "0.27.2",
            versionCode = 997,
            apkSizeMb = 12.8,
            apkSha256 = "b71948501c9a823058b764210985c4912058bca79124098501258ac719485012",
            reproducibleVerified = true,
            trackerCount = 0,
            antiFeatures = "",
            developerName = "Team NewPipe",
            donationLiberapay = "https://liberapay.com/TeamNewPipe",
            donationGithub = "https://github.com/sponsors/TeamNewPipe",
            rating = 4.8f,
            reviewCount = 1450,
            downloadCount = 420000,
            isInstalled = false,
            isBookmarked = false,
            isUserUploaded = false,
            buildSystem = "Gradle (Android)",
            iconKey = "video"
        ),
        FossApp(
            id = "app-organicmaps",
            packageName = "app.organicmaps",
            name = "Organic Maps",
            tagline = "Offline privacy-first OpenStreetMap navigation for travelers",
            description = "Organic Maps is an open-source, privacy-focused offline maps and turn-by-turn navigation app powered by OpenStreetMap. No tracking, no data collection, no ads. Built for drivers, hikers, and cyclists with battery optimization.",
            category = "Utilities",
            subcategory = "Navigation & Maps",
            license = "Apache-2.0",
            gitRepoUrl = "https://github.com/organicmaps/organicmaps",
            gitBranch = "master",
            latestGitTag = "v2026.04.10",
            commitSha = "21bc4589",
            versionName = "2026.04.10",
            versionCode = 260410,
            apkSizeMb = 55.0,
            apkSha256 = "90124859a80bca218501249bca781940128945671239084128509124859a80bc",
            reproducibleVerified = true,
            trackerCount = 0,
            antiFeatures = "",
            developerName = "Organic Maps Community",
            donationGithub = "https://github.com/sponsors/organicmaps",
            donationLiberapay = "https://liberapay.com/OrganicMaps",
            rating = 4.8f,
            reviewCount = 680,
            downloadCount = 180000,
            isInstalled = false,
            isBookmarked = false,
            isUserUploaded = false,
            buildSystem = "CMake / NDK",
            iconKey = "map"
        ),
        FossApp(
            id = "app-termux",
            packageName = "com.termux",
            name = "Termux",
            tagline = "Advanced Android terminal emulator and Linux environment",
            description = "Termux combines powerful terminal emulation with an extensive Linux package collection. Enjoy the bash and zsh shells, edit files with nano and vim, access servers over ssh, and develop with clang, make, and python directly on your phone.",
            category = "Dev Tools",
            subcategory = "Terminal & CLI",
            license = "GPL-3.0-only",
            gitRepoUrl = "https://github.com/termux/termux-app",
            gitBranch = "master",
            latestGitTag = "v0.118.0",
            commitSha = "67da3490",
            versionName = "0.118.0",
            versionCode = 118,
            apkSizeMb = 48.0,
            apkSha256 = "781290485a0bc981245012894120958bca791248901258ac7194850129012485",
            reproducibleVerified = true,
            trackerCount = 0,
            antiFeatures = "",
            developerName = "Fredrik Fornwall & Team",
            donationGithub = "https://github.com/sponsors/termux",
            donationLiberapay = "https://liberapay.com/termux",
            rating = 4.9f,
            reviewCount = 1120,
            downloadCount = 350000,
            isInstalled = true,
            installedVersion = "0.118.0",
            isBookmarked = false,
            isUserUploaded = false,
            buildSystem = "Gradle (Android)",
            iconKey = "terminal"
        )
    )

    val sampleReviews = listOf(
        Review(
            id = "rev-1",
            appId = "game-shattered-pd",
            author = "SecurityAuditor_Dave",
            rating = 5,
            title = "100% Hermetic & Reproducible Build Verified",
            comment = "Audited the Git tag v2.4.2 against the official signed APK. Byte-for-byte reproducibility confirmed with SHA-256 match. Zero embedded trackers, zero non-free SDKs, pristine Gradle dependency lockfile.",
            timestamp = System.currentTimeMillis() - 86400000L * 2,
            isAuditor = true,
            auditorRole = "Reproducible Build Verifier",
            auditedCommit = "3e9b7a4f",
            helpfulVotes = 87
        ),
        Review(
            id = "rev-2",
            appId = "game-shattered-pd",
            author = "PixelKnight99",
            rating = 5,
            title = "The benchmark for mobile open-source gaming",
            comment = "I've logged over 200 hours on this roguelike. Incredible tactical depth, fantastic developer responsiveness on GitHub, and absolutely zero predatory pay-to-win mechanics. Tipped $10 to Evan!",
            timestamp = System.currentTimeMillis() - 86400000L * 5,
            isAuditor = false,
            helpfulVotes = 42
        ),
        Review(
            id = "rev-3",
            appId = "app-aegis",
            author = "CryptoFoss_Lead",
            rating = 5,
            title = "Zero network access permission, gold standard",
            comment = "Checked AndroidManifest.xml: doesn't even declare INTERNET permission! Tokens never touch the cloud unless explicitly exported by user via encrypted backup. Fully transparent code.",
            timestamp = System.currentTimeMillis() - 86400000L * 3,
            isAuditor = true,
            auditorRole = "Security Auditor",
            auditedCommit = "5f28a9b1",
            helpfulVotes = 115
        ),
        Review(
            id = "rev-4",
            appId = "game-mindustry",
            author = "ConveyorWiz",
            rating = 5,
            title = "Limitless engineering and multiplayer",
            comment = "Running dedicated servers with friends is effortless. Love that the Git repo receives daily commits and the whole game logic is readable in open source.",
            timestamp = System.currentTimeMillis() - 86400000L * 8,
            isAuditor = false,
            helpfulVotes = 29
        )
    )

    val sampleCommits = listOf(
        GitCommit(
            id = "com-1",
            appId = "game-shattered-pd",
            sha = "3e9b7a4f",
            tag = "v2.4.2",
            branch = "master",
            author = "00-Evan <evan@shatteredpixel.com>",
            message = "Release v2.4.2: Balance adjustments for Duelist abilities & fix crash on Android 15",
            timestamp = System.currentTimeMillis() - 86400000L * 4,
            isGpgVerified = true,
            gpgKeyId = "0x89EF012A",
            filesChanged = 12,
            additions = 340,
            deletions = 45
        ),
        GitCommit(
            id = "com-2",
            appId = "game-shattered-pd",
            sha = "7c1209da",
            tag = "",
            branch = "master",
            author = "00-Evan <evan@shatteredpixel.com>",
            message = "Refactor inventory UI for foldable screens & update Gradle wrapper to 8.6",
            timestamp = System.currentTimeMillis() - 86400000L * 6,
            isGpgVerified = true,
            gpgKeyId = "0x89EF012A",
            filesChanged = 5,
            additions = 98,
            deletions = 22
        ),
        GitCommit(
            id = "com-3",
            appId = "game-mindustry",
            sha = "8c21e5fa",
            tag = "v146.0",
            branch = "master",
            author = "Anuken <anuken@mindustry.dev>",
            message = "v146: Add new naval units, optimize network packet compression",
            timestamp = System.currentTimeMillis() - 86400000L * 7,
            isGpgVerified = true,
            gpgKeyId = "0x34AB710C",
            filesChanged = 28,
            additions = 1240,
            deletions = 310
        ),
        GitCommit(
            id = "com-4",
            appId = "app-aegis",
            sha = "5f28a9b1",
            tag = "v3.2.1",
            branch = "master",
            author = "Alexander <alex@beemdevelopment.com>",
            message = "v3.2.1: Update translations, enhance Argon2id memory parameter for vault encryption",
            timestamp = System.currentTimeMillis() - 86400000L * 9,
            isGpgVerified = true,
            gpgKeyId = "0xF729B801",
            filesChanged = 8,
            additions = 150,
            deletions = 14
        )
    )

    val sampleBuildVerifications = listOf(
        BuildVerification(
            id = "bv-1",
            appId = "game-shattered-pd",
            buildNumber = 624,
            gitTag = "v2.4.2",
            commitSha = "3e9b7a4f",
            status = "VERIFIED",
            reproducibilityPercentage = 100,
            sourceArchiveHash = "sha256:7a4f9012384a92185cba79012845019284ba0912850192847192845019284501",
            binaryArtifactHash = "sha256:9b642e038d8ec4f51bc72e01b356f91cbca12e340a97cbfb40a33a1e2fbc7891",
            scannersRun = 18,
            threatsDetected = 0,
            securityScore = 100,
            verifiedBy = "OpenPlay Hermetic Verifier (Node #04)",
            logOutput = """
[STAGE 1: ISOLATED CLONE]
> git clone https://github.com/00-Evan/shattered-pixel-dungeon.git --depth=1 --branch v2.4.2
> commit 3e9b7a4f signed by 0x89EF012A (Valid GPG signature)
> Clean environment container: docker.openplay.org/foss-builder:android-jdk17-v3

[STAGE 2: DEPENDENCY AUDIT]
> Verifying sha256 checksums of 42 Gradle dependencies against locked hash list... OK
> Checking for remote executable downloads... ZERO found
> Proprietary SDK check... ZERO closed-source blobs detected

[STAGE 3: COMPILATION & PACKAGING]
> ./gradlew assembleRelease --no-daemon -Dorg.gradle.project.reproducible=true
> Output: app/build/outputs/apk/release/shattered-pixel-dungeon-v2.4.2.apk
> Byte-for-byte SHA256 matches independent build node #01 & #02! Match: 100.0%

[STAGE 4: MALWARE & STATIC ANALYSIS]
> ClamAV scanner: OK (0 threats)
> YARA FOSS Rule Engine: Clean
> Exodus Privacy Static Scanner: 0 trackers detected
> Dangerous Permissions: 0 detected (Vibrate only)

[RESULT: VERIFIED MALWARE-FREE]
All 18 security checkpoints passed. APK approved for direct installation.
            """.trimIndent(),
            timestamp = System.currentTimeMillis() - 86400000L * 4
        ),
        BuildVerification(
            id = "bv-2",
            appId = "app-aegis",
            buildNumber = 83,
            gitTag = "v3.2.1",
            commitSha = "5f28a9b1",
            status = "VERIFIED",
            reproducibilityPercentage = 100,
            sourceArchiveHash = "sha256:889412058bca79124098501258ac71948501290124859a80bc218501249bca78",
            binaryArtifactHash = "sha256:48390bca8210984ef298104859a0981e4b859012895cd4502891ba5f98a28091",
            scannersRun = 18,
            threatsDetected = 0,
            securityScore = 100,
            verifiedBy = "OpenPlay Hermetic Verifier (Node #02)",
            logOutput = """
[STAGE 1: ISOLATED CLONE]
> git clone https://github.com/beemdevelopment/Aegis.git --depth=1 --branch v3.2.1
> commit 5f28a9b1 signed with key 0xF729B801 (Beem GPG Release Key)
> Source archive verified against upstream release tarball.

[STAGE 2: REPRODUCIBLE BUILD CHECK]
> Hermetic build in airgapped container with pinned AGP 8.4 and OpenJDK 17.
> Computed APK Hash: sha256:48390bca8210984ef298104859a0981e4b859012895cd4502891ba5f98a28091
> Upstream Developer Hash: EXACT MATCH (100% Reproducible)

[STAGE 3: PRIVACY & PERMISSION AUDIT]
> Network sockets: NONE (No android.permission.INTERNET in manifest)
> Background telemetry: 0 detected
> Cryptographic primitives: Hardware Keystore & BiometricPrompt verified.

[RESULT: VERIFIED MALWARE-FREE & REPRODUCIBLE]
Approved and signed with OpenPlay attestation key.
            """.trimIndent(),
            timestamp = System.currentTimeMillis() - 86400000L * 9
        )
    )

    val sampleDonations = listOf(
        DeveloperDonation(
            id = "don-1",
            appId = "game-shattered-pd",
            appName = "Shattered Pixel Dungeon",
            developerName = "Evan Debenham",
            amountUsd = 10.0,
            currency = "USD",
            platform = "Direct Supporter Tip",
            supporterMessage = "Thanks for keeping this game 100% open source and ad-free for all these years!",
            backerBadge = "Gold Patron",
            timestamp = System.currentTimeMillis() - 86400000L * 1
        ),
        DeveloperDonation(
            id = "don-2",
            appId = "app-aegis",
            appName = "Aegis Authenticator",
            developerName = "Beem Development",
            amountUsd = 15.0,
            currency = "USD",
            platform = "GitHub Sponsors",
            supporterMessage = "Best 2FA app on Android. Zero telemetry, pure freedom.",
            backerBadge = "Freedom Champion",
            timestamp = System.currentTimeMillis() - 86400000L * 3
        ),
        DeveloperDonation(
            id = "don-3",
            appId = "game-mindustry",
            appName = "Mindustry",
            developerName = "Anuke",
            amountUsd = 5.0,
            currency = "USD",
            platform = "Liberapay",
            supporterMessage = "Enjoying the multiplayer servers with friends! Coffee on me.",
            backerBadge = "Silver Sponsor",
            timestamp = System.currentTimeMillis() - 86400000L * 6
        )
    )
}
