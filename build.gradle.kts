// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.application) apply false
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.dev.detekt)
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.hiltLibrary) apply false
}

subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")

    tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
        buildUponDefaultConfig = false
        parallel = true
        basePath = projectDir.path
        reports {
            html.required.set(true)
            html.outputLocation.set(file("build/reports/detekt.html"))
            sarif.required.set(false)
            txt.required.set(false)
            xml.required.set(false)
        }
    }

    detekt {
        buildUponDefaultConfig = false
        config.setFrom("$rootDir/detekt.yml")
        parallel = true
        toolVersion = "1.23.1"
        basePath = projectDir.path
    }
}
