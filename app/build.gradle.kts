import java.io.FileInputStream
import java.util.Properties
import org.alberto97.ouilookup.buildsrc.DependencyUpdates
import org.alberto97.ouilookup.buildsrc.ReleaseType

plugins {
    id("com.github.ben-manes.versions") version "0.42.0"
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

val secureProperties = Properties().apply {
    try {
        load(FileInputStream("privateConfig/secure.properties"))
    } catch (ex: Exception) {
        put("KEYSTORE_FILE", "")
    }
}

val composeVersion = "1.0.5"

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "org.alberto97.ouilookup"
        minSdk = 21
        targetSdk = 31
        versionCode = 4
        versionName = "1.1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        kotlinCompilerExtensionVersion = composeVersion
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("com.github.doyaaaaaken:kotlin-csv-jvm:1.2.0")

    // Compose
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.material:material-icons-extended:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling:$composeVersion")
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.activity:activity-compose:1.4.0")

    // Hilt
    val hiltVersion = "2.40.5"
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")
    kapt("androidx.hilt:hilt-compiler:1.0.0")

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.10")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.4.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    // Retrofit
    val retrofitVersion = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-scalars:$retrofitVersion")

    // Room
    val roomVersion = "2.4.1"
    kapt("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")

    // Work
    implementation("androidx.work:work-runtime-ktx:2.7.1")
    implementation("androidx.hilt:hilt-work:1.0.0")

    // Test
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit-ktx:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("com.linkedin.dexmaker:dexmaker-mockito-inline:2.28.1")
    androidTestImplementation("org.mockito:mockito-core:4.3.1")
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

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
}
