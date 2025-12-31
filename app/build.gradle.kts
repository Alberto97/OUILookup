import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    id("org.gradle.android.cache-fix")
    id("androidx.room") version "2.8.4"
}

val secureProperties = Properties().apply {
    try {
        load(FileInputStream("privateConfig/secure.properties"))
    } catch (ex: Exception) {
        put("KEYSTORE_FILE", "")
    }
}

android {
    compileSdk = 36

    defaultConfig {
        applicationId = "org.alberto97.ouilookup"
        namespace = "org.alberto97.ouilookup"
        minSdk = 26
        targetSdk = 36
        versionCode = 23
        versionName = "1.6.5"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    packaging {
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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    room {
        schemaDirectory("$projectDir/schemas/")
    }
}

dependencies {
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("androidx.datastore:datastore-preferences:1.2.0")
    implementation("com.github.doyaaaaaken:kotlin-csv-jvm:1.10.0")

    // Compose
    implementation(platform("androidx.compose:compose-bom:2025.10.00"))
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.activity:activity-compose:1.12.2")

    // Hilt
    val hiltVersion = "2.57.2"
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    ksp("com.google.dagger:hilt-android-compiler:$hiltVersion")
    ksp("androidx.hilt:hilt-compiler:1.3.0")

    // In-app review - Play Services
    "gmsImplementation"("com.google.android.play:review-ktx:2.0.1")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.10.0")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.9.6")
    implementation("androidx.hilt:hilt-navigation-compose:1.3.0")

    // Retrofit
    val retrofitVersion = "3.0.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-scalars:$retrofitVersion")

    // Room
    val roomVersion = "2.8.4"
    ksp("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")

    // Work
    implementation("androidx.work:work-runtime-ktx:2.10.5")
    implementation("androidx.hilt:hilt-work:1.3.0")

    // Test
    val mockkVersion = "1.14.7"
    testImplementation(kotlin("test"))
    testImplementation("io.mockk:mockk-android:$mockkVersion")
    androidTestImplementation("androidx.test.ext:junit-ktx:1.3.0")
    androidTestImplementation("io.mockk:mockk-android:$mockkVersion")
}
