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
        namespace = "dev.voir.areca.keyboard"
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

            // `api`: keys and layouts take foundation's ButtonStyle and ButtonSize in their public API.
            api(projects.ui.foundation)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}

mavenPublishing {
    pom {
        name.set("Areca Keyboard")
        description.set(
            "An on-screen keyboard for Compose Multiplatform: host, layouts and key grids, drawn with the Areca theme.",
        )
    }
}

extra["bundledIconsPackage"] = "dev.voir.areca.keyboard.icon"
extra["bundledIcons"] = listOf("Check", "ChevronDown", "Delete")
apply(from = rootProject.file("gradle/bundled-icons.gradle.kts"))
