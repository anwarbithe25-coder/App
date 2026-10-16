const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = process.env.GCP_PROJECT || "demo-no-project";
const ALICE_UID = "alice_123";
const BOB_UID = "bob_456";

const [emulatorHost, emulatorPortStr] = (process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085").split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

test("Public user can read apps list", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertSucceeds(unauthDb.collection("apps").get());
});

test("Unauthenticated user cannot publish an app", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(
    unauthDb.collection("apps").doc("test_app").set({
      id: "test_app",
      name: "Pixel Rogue",
      tagline: "Libre roguelike",
      packageName: "org.libre.rogue",
      category: "Games",
      license: "GPL-3.0-or-later",
      gitRepoUrl: "https://github.com/test/rogue",
      userId: ALICE_UID,
      createdAt: new Date(),
    })
  );
});

test("Authenticated user can publish their own app", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertSucceeds(
    aliceDb.collection("apps").doc("test_app").set({
      id: "test_app",
      name: "Pixel Rogue",
      tagline: "Libre roguelike",
      packageName: "org.libre.rogue",
      category: "Games",
      license: "GPL-3.0-or-later",
      gitRepoUrl: "https://github.com/test/rogue",
      userId: ALICE_UID,
      createdAt: new Date(),
    })
  );
});

test("Authenticated user cannot spoof another user's app publish", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertFails(
    aliceDb.collection("apps").doc("test_app").set({
      id: "test_app",
      name: "Pixel Rogue",
      tagline: "Libre roguelike",
      packageName: "org.libre.rogue",
      category: "Games",
      license: "GPL-3.0-or-later",
      gitRepoUrl: "https://github.com/test/rogue",
      userId: BOB_UID, // Spoofed UID
      createdAt: new Date(),
    })
  );
});

test("Authenticated user can post a community review", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertSucceeds(
    aliceDb.collection("reviews").doc("rev_1").set({
      id: "rev_1",
      appId: "test_app",
      userId: ALICE_UID,
      author: "Alice",
      rating: 5,
      title: "Clean FOSS Code",
      comment: "Audited reproducible build. Zero trackers.",
      createdAt: new Date(),
    })
  );
});
