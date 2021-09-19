package org.alberto97.ouilookup.buildsrc

object Libs {
    const val androidGradlePlugin = "com.android.tools.build:gradle:7.0.2"

    object Kotlin {
        private const val version = "1.5.21"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
    }

    object AndroidX {
        object Activity {
            const val activityCompose = "androidx.activity:activity-compose:1.3.1"
        }

        const val appcompat = "androidx.appcompat:appcompat:1.3.1"

        object Compose {
            const val version = "1.0.2"

            const val material = "androidx.compose.material:material:$version"
            const val materialIconsExtended = "androidx.compose.material:material-icons-extended:$version"
            const val ui = "androidx.compose.ui:ui:$version"
            const val tooling = "androidx.compose.ui:ui-tooling:$version"
        }

        object Datastore {
            const val preferences = "androidx.datastore:datastore-preferences:1.0.0"
        }

        object Hilt {
            const val compiler = "androidx.hilt:hilt-compiler:1.0.0"
            const val navigationCompose = "androidx.hilt:hilt-navigation-compose:1.0.0-alpha03"
            const val work = "androidx.hilt:hilt-work:1.0.0"
        }

        object Navigation {
            const val compose = "androidx.navigation:navigation-compose:2.4.0-alpha06"
        }

        object Room {
            private const val version = "2.3.0"

            const val compiler = "androidx.room:room-compiler:$version"
            const val ktx = "androidx.room:room-ktx:$version"
        }

        object Test {
            object Ext {
                private const val version = "1.1.3"
                const val junit = "androidx.test.ext:junit-ktx:$version"
            }
            const val espressoCore = "androidx.test.espresso:espresso-core:3.4.0"
        }

        object Work {
            private const val version = "2.7.0-beta01"

            const val runtime = "androidx.work:work-runtime-ktx:$version"
        }
    }

    object Hilt {
        private const val version = "2.38.1"

        const val gradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:$version"
        const val android = "com.google.dagger:hilt-android:$version"
        const val androidCompiler = "com.google.dagger:hilt-android-compiler:$version"
    }

    object JUnit {
        private const val version = "4.13.2"
        const val junit = "junit:junit:$version"
    }

    object Retrofit {
        private const val version = "2.9.0"

        const val converterScalars = "com.squareup.retrofit2:converter-scalars:$version"
        const val retrofit = "com.squareup.retrofit2:retrofit:$version"
    }

    const val kotlinCsv = "com.github.doyaaaaaken:kotlin-csv-jvm:0.15.2"
}