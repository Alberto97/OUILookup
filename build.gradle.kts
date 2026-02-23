// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    val agpVersion = "9.0.1"
    val kotlinVersion = "2.3.10"
    id("com.android.application") version agpVersion apply false
    id("com.android.library") version agpVersion apply false
    id("org.jetbrains.kotlin.plugin.compose") version kotlinVersion apply false
    id("com.google.dagger.hilt.android") version "2.59.1" apply false
    id("com.google.devtools.ksp") version "2.3.6" apply false
    id("org.gradle.android.cache-fix") version "3.0.3" apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register("clean", Delete::class.java) {
    delete(layout.buildDirectory)
}
