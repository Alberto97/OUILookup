@file:Suppress("UnstableApiUsage")

import java.io.FileInputStream
import java.util.Properties
import org.alberto97.ouilookup.buildsrc.DependencyUpdates
import org.alberto97.ouilookup.buildsrc.ReleaseType

plugins {
    id("com.github.ben-manes.versions") version "0.46.0"
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("com.google.dagger.hilt.android")
    id("org.gradle.android.cache-fix")
}

val secureProperties = Properties().apply {
    try {
        load(FileInputStream("privateConfig/secure.properties"))
    } catch (ex: Exception) {
        put("KEYSTORE_FILE", "")
    }
}

android {
    compileSdk = 33

    defaultConfig {
        applicationId = "org.alberto97.ouilookup"
        namespace = "org.alberto97.ouilookup"
        minSdk = 21
        targetSdk = 33
        versionCode = 17
        versionName = "1.5.3"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    packagingOptions {
        resources.excludes.add("META-INF/*")
    }

    signingConfigs {
        register("release") {
            if (secureProperties.getProperty("KEYSTORE_FILE").isNotEmpty()) {
                storeFile = file("$rootDir/privateConfig/${secureProperties["KEYSTORE_FILE"]}")
                storePassword = "${secureProperties["KEYSTORE_PASSWORD"]}"
                keyAlias = "${secureProperties["KEY_ALIAS"]}"
                keyPassword = "${secureProperties["KEY_PASSWORD"]}"
            }
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            if (secureProperties.getProperty("KEYSTORE_FILE").isNotEmpty()) {
                signingConfig = signingConfigs["release"]
            }
        }
    }

    flavorDimensions += "version"
    productFlavors {
        create("gms") {

        }
        create("foss") {

        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlin {
        jvmToolchain(11)
    }

    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
}

dependencies {
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.2")

    implementation("androidx.core:core-splashscreen:1.0.0")
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("com.github.doyaaaaaken:kotlin-csv-jvm:1.9.0")

    // Compose
    implementation(platform("androidx.compose:compose-bom:2023.04.01"))
    implementation("androidx.compose.material:material")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.activity:activity-compose:1.7.0")

    // Hilt
    val hiltVersion = "2.45"
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")
    kapt("androidx.hilt:hilt-compiler:1.0.0")

    // In-app review - Play Services
    "gmsImplementation"("com.google.android.play:core:1.10.3")
    "gmsImplementation"("com.google.android.play:core-ktx:1.8.1")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.5.3")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    // Retrofit
    val retrofitVersion = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-scalars:$retrofitVersion")

    // Room
    val roomVersion = "2.5.1"
    kapt("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")

    // Work
    implementation("androidx.work:work-runtime-ktx:2.8.1")
    implementation("androidx.hilt:hilt-work:1.0.0")

    // Test
    val mockkVersion = "1.13.3"
    testImplementation("junit:junit:4.13.2")
    testImplementation("io.mockk:mockk-android:$mockkVersion")
    androidTestImplementation("androidx.test.ext:junit-ktx:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("io.mockk:mockk-android:$mockkVersion")
}

tasks.withType<com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask> {
    rejectVersionIf {
        val current = DependencyUpdates.versionToRelease(currentVersion)
        // If we're using a SNAPSHOT, ignore since we must be doing so for a reason.
        if (current == ReleaseType.SNAPSHOT) return@rejectVersionIf true

        // Otherwise we reject if the candidate is more 'unstable' than our version
        val candidate = DependencyUpdates.versionToRelease(candidate.version)
        return@rejectVersionIf candidate.isLessStableThan(current)
    }
}
