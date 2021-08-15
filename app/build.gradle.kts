import org.alberto97.ouilookup.buildsrc.Libs

plugins {
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
        versionCode = 1
        versionName = "1.0"

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