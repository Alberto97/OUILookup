import org.alberto97.ouilookup.buildsrc.Libs

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
        kotlinCompilerExtensionVersion = Libs.AndroidX.Compose.version
    }
}

dependencies {

    implementation(Libs.AndroidX.appcompat)
    implementation(Libs.AndroidX.Datastore.preferences)
    implementation(Libs.kotlinCsv)

    // Compose
    implementation(Libs.AndroidX.Compose.ui)
    implementation(Libs.AndroidX.Compose.material)
    implementation(Libs.AndroidX.Compose.materialIconsExtended)
    implementation(Libs.AndroidX.Compose.tooling)
    implementation(Libs.AndroidX.Activity.activityCompose)

    // Hilt
    implementation(Libs.Hilt.android)
    kapt(Libs.Hilt.androidCompiler)
    kapt(Libs.AndroidX.Hilt.compiler)

    // Navigation
    implementation(Libs.AndroidX.Navigation.compose)
    implementation(Libs.AndroidX.Hilt.navigationCompose)

    // Retrofit
    implementation(Libs.Retrofit.converterScalars)
    implementation(Libs.Retrofit.retrofit)

    // Room
    kapt(Libs.AndroidX.Room.compiler)
    implementation(Libs.AndroidX.Room.ktx)

    // Work
    implementation(Libs.AndroidX.Work.runtime)
    implementation(Libs.AndroidX.Hilt.work)

    // Test
    testImplementation(Libs.JUnit.junit)
    androidTestImplementation(Libs.AndroidX.Test.Ext.junit)
    androidTestImplementation(Libs.AndroidX.Test.espressoCore)
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
