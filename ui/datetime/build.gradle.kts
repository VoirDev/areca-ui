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
        namespace = "dev.voir.areca.datetime"
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

            api(libs.kotlinx.datetime)

            // `api`: public signatures carry foundation types (FieldStyle, WheelPickerSize).
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
        name.set("Areca Date and Time")
        description.set(
            "Calendar, date range, time picker and date-time field for Compose Multiplatform, drawn with the Areca theme.",
        )
    }
}

extra["bundledIconsPackage"] = "dev.voir.areca.datetime.icon"
extra["bundledIcons"] = listOf("Calendar", "Clock")
apply(from = rootProject.file("gradle/bundled-icons.gradle.kts"))
