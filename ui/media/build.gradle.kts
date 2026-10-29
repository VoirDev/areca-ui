plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.mavenPublish)
}

kotlin {
    jvmToolchain(21)
    explicitApi()

    jvm()
    android {
        namespace = "dev.voir.areca.media"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            api(libs.compose.runtime)
            api(libs.compose.foundation)
            api(libs.compose.ui)

            // `api`: the parts are composed beside foundation components and take foundation types
            // — a carousel's arrows take an `IconButtonStyle`, and a viewer's bar is foundation's.
            api(projects.ui.foundation)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }

        // Gesture tests drive the parts with real pointer input, which needs Skia's native library
        // for this host.
        jvmTest.dependencies {
            implementation(libs.compose.uiTest)
            implementation(compose.desktop.currentOs)
        }
    }
}

mavenPublishing {
    pom {
        name.set("Areca Media")
        description.set(
            "Pictures for Compose Multiplatform: frames with loading and failed faces, zoom, carousels, thumbnails and a full-screen viewer, drawn with the Areca theme.",
        )
    }
}

extra["bundledIconsPackage"] = "dev.voir.areca.media.icon"
extra["bundledIcons"] = listOf("ChevronLeft", "ChevronRight", "ImageX", "SwitchHorizontal")
apply(from = rootProject.file("gradle/bundled-icons.gradle.kts"))
