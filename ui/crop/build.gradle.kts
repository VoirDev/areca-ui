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
        namespace = "dev.voir.areca.crop"
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

            implementation(projects.ui.foundation)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
        }

        // The render tests draw into real bitmaps, which needs Skia's native library for this host.
        jvmTest.dependencies {
            implementation(compose.desktop.currentOs)
        }
    }
}

mavenPublishing {
    pom {
        name.set("Areca Crop")
        description.set(
            "An image cropper for Compose Multiplatform: stage, toolbar, dial, masks, aspect ratios, straightening and rendering, drawn with the Areca theme.",
        )
    }
}

extra["bundledIconsPackage"] = "dev.voir.areca.crop.icon"
extra["bundledIcons"] = listOf("Angle", "Check", "Crop", "FlipBackward", "Reflect", "RefreshCcw", "RotateCcwSquare", "XClose", "ZoomIn")
apply(from = rootProject.file("gradle/bundled-icons.gradle.kts"))
