// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    val agpVersion = "7.4.1"
    id("com.android.application") version agpVersion apply false
    id("com.android.library") version agpVersion apply false
    id("org.jetbrains.kotlin.android") version "1.8.0" apply false
    id("com.google.dagger.hilt.android") version "2.44.2" apply false
    id("org.gradle.android.cache-fix") version "2.6.3" apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register("clean", Delete::class.java) {
    delete(rootProject.buildDir)
}
