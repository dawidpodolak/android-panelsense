@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlin)
    kotlin("kapt")
}

android {
    namespace = "com.panelsense.core"
    compileSdk = libs.versions.compileSdk.get().toInt()

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.datastore)
    implementation(libs.datastore.preferences)
    implementation(libs.datastore.preferences.core)
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
