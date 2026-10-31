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
        namespace = "dev.voir.areca.foundation"
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
        }

        // The fold a foldable reports, read in `platformWindowFold`.
        androidMain.dependencies {
            implementation(libs.androidx.window)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
        }

        // Composition tests — which window class a subtree reads — need Skia's native library for
        // this host.
        jvmTest.dependencies {
            implementation(libs.compose.uiTest)
            implementation(compose.desktop.currentOs)
        }
    }
}

mavenPublishing {
    pom {
        name.set("Areca Foundation")
        description.set(
            "The Areca design system for Compose Multiplatform: theme tokens, components, fields, lists, layouts, overlays and navigation for Android, iOS and the JVM.",
        )
    }
}

extra["bundledIconsPackage"] = "dev.voir.areca.icon"
extra["bundledIcons"] = listOf(
    "Bell",
    "Check",
    "ChevronLeft",
    "Cross",
    "DotsVertical",
    "Download",
    "Edit",
    "EyeHide",
    "EyeShow",
    "FilterLines",
    "Heart",
    "HeartFill",
    "Menu01",
    "Plus",
    "Search",
    "Settings",
    "Share",
    "Trash",
    "XClose",
)
apply(from = rootProject.file("gradle/bundled-icons.gradle.kts"))
