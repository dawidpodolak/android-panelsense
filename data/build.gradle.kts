
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.dev.detekt)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.panelsense.data"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = 26
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":domain"))
    implementation(files("libs/svg-android-1.1.jar"))
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson.converter)
    implementation(libs.retrofit.scalars.converter)
    implementation(libs.gson)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.hilt.android)
    implementation(libs.okhttp)
    implementation(libs.timber)
    implementation(libs.datastore)
    implementation(libs.datastore.preferences)
    implementation(libs.jsoup)
    implementation(libs.yml)
    implementation(libs.mqtt)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlin.coroutines.android)
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.threetenabp)
    implementation(libs.threetenadapter) {
        isTransitive = false
    }

    kapt(libs.hilt.compiler)
}
