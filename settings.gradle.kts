@file:Suppress("UnstableApiUsage")

include(":core")


pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()

    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "NSPanelSense"
include(":app")
include(":domain")
include(":data")

//buildscript {
//    repositories {
//        google()
//        mavenCentral()
//    }
//
//    dependencies {
//        classpath("com.google.dagger:hilt-android-gradle-plugin:2.47")
//    }
//}
