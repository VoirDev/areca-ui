plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.mavenPublish)
}

kotlin {
    explicitApi()
    jvmToolchain(21)

    jvm()
    android {
        namespace = "dev.voir.areca.viewmodel"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        // Runs `commonTest` on the JVM against the Android variant as well.
        withHostTest {}
    }
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            api(libs.kotlinx.coroutines.core)
            api(libs.lifecycle.viewmodel)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}

mavenPublishing {
    pom {
        name.set("Areca ViewModel")
        description.set(
            "Multiplatform ViewModel, UI state and paged-list helpers for Kotlin Multiplatform, independent of the Areca design system.",
        )
    }
}
