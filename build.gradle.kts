// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    val agpVersion = "8.2.0"
    val kotlinVersion = "1.9.20"
    id("com.android.application") version agpVersion apply false
    id("com.android.library") version agpVersion apply false
    id("org.jetbrains.kotlin.android") version kotlinVersion apply false
    id("com.google.dagger.hilt.android") version "2.49" apply false
    id("com.google.devtools.ksp") version "$kotlinVersion-1.0.13" apply false
    id("org.gradle.android.cache-fix") version "3.0" apply false
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
