@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://repo.eclipse.org/content/repositories/paho-releases/")
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://repo.eclipse.org/content/repositories/paho-releases/")
    }
}

rootProject.name = "NSPanelSense"
include(":app")
include(":core")
include(":domain")
include(":data")
