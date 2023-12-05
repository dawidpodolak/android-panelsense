import java.io.FileInputStream
import java.util.Properties

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.application)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.dev.detekt)
    alias(libs.plugins.sentry)
    kotlin("kapt")
    alias(libs.plugins.hiltLibrary)
}

android {
    namespace = "com.panelsense.app"
    compileSdk = libs.versions.compileSdk.get().toInt()
    val keysProperties = Properties()
    keysProperties.load(FileInputStream(rootProject.file("keys.properties")))

    defaultConfig {
        applicationId = "com.panel.sense"
        minSdk = 26
        //noinspection EditedTargetSdkVersion
        targetSdk = 34
        versionCode = getVersionCode(properties = properties)
        versionName = "1.0"

        buildConfigField("String", "SENTRY_DSN", keysProperties.getProperty("sentryDSN"))
        manifestPlaceholders["SENTRY_DSN"] = keysProperties.getProperty("sentryDSN")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("release") {
            val keystorePath = file("../keystore-properties.txt")
            val prop = Properties()
            if (keystorePath.canRead()) {
                prop.load(FileInputStream(keystorePath))
                keyAlias = prop.getProperty("keyAlias")
                keyPassword = prop.getProperty("keyPassword")
                storeFile = file("../keystore.jks")
                storePassword = prop.getProperty("storePassword")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isDebuggable = false
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(project(":data"))
    implementation(project(":core"))
    implementation(project(":domain"))
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.datastore)
    implementation(libs.datastore.preferences)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.timber)
    implementation(libs.compose.grid)
    implementation(libs.kt.coil)
    implementation(libs.compose.image.drawable)
    implementation(libs.compose.constraintLayout)
    implementation(libs.hilt.android)
    implementation(libs.gson)
    implementation(libs.threetenabp)

    implementation(libs.kotlin.coroutines.android)
    implementation(libs.kotlin.coroutines.core)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    kapt(libs.hilt.compiler)
}

fun getVersionCode(properties: MutableMap<String, *>): Int =
    if (properties.containsKey("android.injected.invoked.from.ide")) 100 else (System.currentTimeMillis() / 1000 / 60).toInt()
