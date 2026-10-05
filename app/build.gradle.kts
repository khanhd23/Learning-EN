import groovy.json.JsonSlurper
import java.util.Properties

plugins { alias(libs.plugins.android.application) }

// App identity + tunables live in config/app_config.json (SKILL.md section 2).
val appConfig = JsonSlurper().parse(file("../config/app_config.json")) as Map<*, *>
val flags = appConfig["flags"] as Map<*, *>

// Real AdMob IDs + signing live in git-ignored secrets.properties. Missing keys fall back to Google's
// public test IDs so debug builds always work; release bundles refuse to build with test IDs.
val secrets = Properties().apply {
    rootProject.file("secrets.properties").takeIf { it.exists() }?.inputStream()?.use { load(it) }
}
val testAppId = "ca-app-pub-3940256099942544~3347511713"
val testAds = mapOf(
    "banner" to "ca-app-pub-3940256099942544/9214589741",
    "interstitial" to "ca-app-pub-3940256099942544/1033173712",
    "rewarded" to "ca-app-pub-3940256099942544/5224354917",
)
fun quoted(v: Any?) = "\"${v?.toString()?.replace("\"", "\\\"") ?: ""}\""

android {
    namespace = "com.yourbrand.englishlearn"
    compileSdk = 37

    defaultConfig {
        applicationId = appConfig["applicationId"].toString()
        minSdk = 24
        targetSdk = 37
        versionCode = (appConfig["versionCode"] as Number).toInt()
        versionName = appConfig["versionName"].toString()
        resValue("string", "app_name", appConfig["displayName"].toString().replace("'", "\\'"))
        resValue("string", "app_short_name", appConfig["shortName"].toString().replace("'", "\\'"))
        manifestPlaceholders["admobAppId"] = secrets.getProperty("admob.appId") ?: testAppId
        buildConfigField("String", "SUPPORT_EMAIL", quoted(appConfig["supportEmail"]))
        buildConfigField("String", "PRIVACY_URL", quoted(appConfig["privacyPolicyUrl"]))
        buildConfigField("boolean", "ADS_ENABLED", (flags["ads"] == true).toString())
        testAds.forEach { (kind, testId) ->
            buildConfigField("String", "AD_${kind.uppercase()}", quoted(secrets.getProperty("admob.$kind") ?: testId))
        }
    }

    buildFeatures { viewBinding = true; buildConfig = true; resValues = true }

    signingConfigs {
        create("release") {
            secrets.getProperty("signing.storeFile")?.let { storeFile = rootProject.file(it) }
            storePassword = secrets.getProperty("signing.storePassword")
            keyAlias = secrets.getProperty("signing.keyAlias")
            keyPassword = secrets.getProperty("signing.keyPassword")
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            buildConfigField("boolean", "FORCE_TEST_ADS", "true")
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            buildConfigField("boolean", "FORCE_TEST_ADS", "false")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            if (secrets.getProperty("signing.storeFile") != null) signingConfig = signingConfigs.getByName("release")
        }
    }

    // Vietnamese UI (default values/) + English (values-en/). Everything else is filtered out.
    androidResources { localeFilters += listOf("vi", "en") }

    sourceSets["main"].assets.srcDir("build/generated/contentAssets")

    testOptions { unitTests.isReturnDefaultValues = true }
}

dependencies {
    implementation(libs.androidx.core)
    implementation(libs.androidx.appcompat)
    // AdMob + UMP consent: the only network SDKs allowed (SKILL.md section 3).
    implementation(libs.play.services.ads)
    implementation(libs.ump)
    testImplementation(libs.junit)
    testImplementation(libs.org.json)
}

// ---------------------------------------------------------------------------------------------
// Content: content/en/*.json + shippable content/i18n packs + config/exam_formats.json -> assets/
// ---------------------------------------------------------------------------------------------
val contentOut = layout.buildDirectory.dir("generated/contentAssets").get().asFile
val copyContent = tasks.register("generateContentAssets") {
    group = "build"
    description = "Bundles the validated content packs into assets/content."
    inputs.dir(rootProject.file("content"))
    inputs.file(rootProject.file("config/exam_formats.json"))
    inputs.file(rootProject.file("config/app_config.json"))
    outputs.dir(contentOut)
    doLast {
        contentOut.deleteRecursively()
        val dst = File(contentOut, "content").apply { mkdirs() }
        listOf("en/words.json", "en/grammar.json", "en/questions.json", "en/relations.json").forEach {
            rootProject.file("content/$it").copyTo(File(dst, it), overwrite = true)
        }
        // Locale packs remain outside the APK until their folder status is complete.
        val localeDir = rootProject.file("content/i18n")
        localeDir.listFiles { f -> f.isDirectory && File(f, "status.json").isFile }?.forEach { source ->
            val meta = runCatching { JsonSlurper().parse(File(source, "status.json")) as Map<*, *> }.getOrNull()
            val complete = meta?.get("status") == "complete" && meta["todo"] != true
            if (complete || source.name == "vi") {
                source.copyRecursively(File(dst, "i18n/${source.name}"), overwrite = true)
            }
        }
        rootProject.file("config/exam_formats.json").copyTo(File(dst, "exam_formats.json"), overwrite = true)
        rootProject.file("content/i18n/market_profiles.json").copyTo(File(dst, "market_profiles.json"), overwrite = true)
        rootProject.file("config/app_config.json").copyTo(File(dst, "app_config.json"), overwrite = true)
        rootProject.file("content/LICENSES.md").copyTo(File(dst, "LICENSES.md"), overwrite = true)
        rootProject.file("content/open-vocabulary-manifest.json").copyTo(File(dst, "open-vocabulary-manifest.json"), overwrite = true)
    }
}
tasks.matching {
    it.name.startsWith("merge") && it.name.endsWith("Assets") ||
        it.name.startsWith("lint") && it.name.contains("Analyze") ||
        it.name.startsWith("generate") && (it.name.endsWith("LintModel") || it.name.endsWith("LintReportModel"))
}
    .configureEach { if (name != "generateContentAssets") dependsOn(copyContent) }

val validateContent = tasks.register<Exec>("validateContent") {
    group = "verification"
    description = "Validates content (ids, answers, blanks, NFC, balance, explanations)."
    workingDir = rootProject.projectDir
    commandLine("python", "tools/validate_content.py")
}
val auditContent = tasks.register<Exec>("auditContent") {
    group = "verification"
    description = "Reports content quality without failing the build."
    workingDir = rootProject.projectDir
    commandLine("python", "tools/audit_content.py")
}
tasks.named("check") { dependsOn(validateContent, auditContent) }

val allowTestAds = providers.gradleProperty("allowTestAds").isPresent
val verifyReleaseAds = tasks.register("verifyReleaseAds") {
    group = "verification"
    description = "Fails when the release would ship Google test ad IDs."
    doLast {
        if (allowTestAds) { logger.warn("verifyReleaseAds skipped (-PallowTestAds). Do NOT upload this build."); return@doLast }
        val missing = (listOf("appId") + testAds.keys).filter {
            val v = secrets.getProperty("admob.$it")
            v.isNullOrBlank() || v.startsWith("ca-app-pub-3940256099942544")
        }
        if (missing.isNotEmpty()) throw GradleException("Release has no real AdMob IDs for: ${missing.joinToString()} (secrets.properties).")
    }
}
tasks.matching { it.name == "bundleRelease" || it.name == "assembleRelease" }.configureEach { dependsOn(verifyReleaseAds, validateContent) }
