import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    id("org.gradle.android.cache-fix")
    id("androidx.room") version "2.6.0"
}

val secureProperties = Properties().apply {
    try {
        load(FileInputStream("privateConfig/secure.properties"))
    } catch (ex: Exception) {
        put("KEYSTORE_FILE", "")
    }
}

android {
    compileSdk = 34

    defaultConfig {
        applicationId = "org.alberto97.ouilookup"
        namespace = "org.alberto97.ouilookup"
        minSdk = 21
        targetSdk = 34
        versionCode = 18
        versionName = "1.6.0"

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
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }

    room {
        schemaDirectory("$projectDir/schemas/")
    }
}

dependencies {
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.3")

    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("com.github.doyaaaaaken:kotlin-csv-jvm:1.9.2")

    // Compose
    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.activity:activity-compose:1.8.0")

    // Hilt
    val hiltVersion = "2.48.1"
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    ksp("com.google.dagger:hilt-android-compiler:$hiltVersion")
    ksp("androidx.hilt:hilt-compiler:1.1.0")

    // In-app review - Play Services
    "gmsImplementation"("com.google.android.play:core:1.10.3")
    "gmsImplementation"("com.google.android.play:core-ktx:1.8.1")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.4")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    // Retrofit
    val retrofitVersion = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-scalars:$retrofitVersion")

    // Room
    val roomVersion = "2.6.0"
    ksp("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")

    // Work
    implementation("androidx.work:work-runtime-ktx:2.8.1")
    implementation("androidx.hilt:hilt-work:1.0.0")

    // Test
    val mockkVersion = "1.13.8"
    testImplementation("junit:junit:4.13.2")
    testImplementation("io.mockk:mockk-android:$mockkVersion")
    androidTestImplementation("androidx.test.ext:junit-ktx:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("io.mockk:mockk-android:$mockkVersion")
}
