plugins {
    id("com.github.ben-manes.versions") version "0.39.0"
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "org.alberto97.ouilookup"
        minSdk = 21
        targetSdk = 31
        versionCode = 2
        versionName = "1.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.get()
    }
}

dependencies {

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.doyaaaaaken.kotlinCsv)

    // Compose
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.materialIconsExt)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.ui)
    implementation(libs.androidx.activity.activityCompose)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.androidCompiler)
    kapt(libs.androidx.hilt.compiler)

    // Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigationCompose)

    // Retrofit
    implementation(libs.retrofit.converterScalars)
    implementation(libs.retrofit.retrofit)

    // Room
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // Work
    implementation(libs.androidx.work.runtime)
    implementation(libs.androidx.hilt.work)

    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espressoCore)
}

tasks.withType<com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask> {
    rejectVersionIf {
        isAndroidX(candidate) && worseAndroidXChannel(candidate, currentVersion)
    }
}

fun isAndroidX(candidate: ModuleComponentIdentifier): Boolean {
    return candidate.group.startsWith("androidx")
}

fun worseAndroidXChannel(candidate: ModuleComponentIdentifier, currentVersion: String): Boolean {
    val channel = mapOf("alpha" to 0, "beta" to 1, "rc" to 3, "" to 4)
    val candidateExtra = candidate.version.filter { char -> char.isLetter() }
    val currentExtra = currentVersion.filter { char -> char.isLetter() }
    val candidateChannel = channel.getValue(candidateExtra)
    val currentChannel = channel.getValue(currentExtra)

    return candidateChannel < currentChannel
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
}
