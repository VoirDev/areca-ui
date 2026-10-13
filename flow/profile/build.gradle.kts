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
        namespace = "dev.voir.areca.flow.profile"
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

            // `api` for the same reason as `flow-auth`: the parts take foundation types (AvatarSize)
            // and are composed beside foundation components in a consumer's screen.
            api(projects.ui.foundation)

            // `api` because `BirthdayRow`'s signature takes `kotlinx.datetime.LocalDate` and
            // `DayOfWeek` and the datetime module's `CalendarLabels`: a consumer has to name those
            // types to call it, so that module and the `kotlinx-datetime` it exposes belong on the
            // consumer's compile classpath. A date of birth is a calendar date, and an own date
            // type would only be converted back at the calendar. See the README's note on
            // `kotlinx-datetime`.
            api(projects.ui.datetime)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

// Stated explicitly, like every flow module: `dev.voir.areca:profile` would read as a profile
// library rather than a set of screen parts. See `flow/FLOWS.md` §8.
mavenPublishing {
    coordinates(
        groupId = project.group.toString(),
        artifactId = "flow-profile",
        version = project.version.toString(),
    )

    pom {
        name.set("Areca Profile Flow")
        description.set(
            "Parts and recipes for account and profile screens built on the Areca design system, free of business logic.",
        )
    }
}

extra["bundledIconsPackage"] = "dev.voir.areca.flow.profile.icon"
extra["bundledIcons"] = listOf(
    "AlertTriangle",
    "Browser",
    "Camera",
    "Check",
    "CloudOff",
    "Download",
    "Gift",
    "Hand",
    "HelpCircle",
    "Image",
    "LogOut",
    "Mail",
    "Monitor",
    "Tablet",
    "Trash",
    "UploadCloud",
    "UserPlus",
)
apply(from = rootProject.file("gradle/bundled-icons.gradle.kts"))
