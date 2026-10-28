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
        namespace = "dev.voir.areca.flow.attachments"
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

            // `api` for the same reason as the other flow modules: the parts take and return
            // foundation types (`AttachmentViewerStyle.topBar()` is a `TopBarStyle`, its
            // `secondaryButton()` a `ButtonStyle`) and are composed beside foundation components in
            // a consumer's screen; the recipes use foundation's `ConfirmSheet`, `Settings` and
            // `Button` directly.
            api(projects.ui.foundation)
            // The viewer is a `MediaViewer`, its pictures zoom with media's zoom, and they load through
            // `LocalMediaImageLoader`, the one loader an application provides for every picture.
            api(projects.ui.media)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

// Stated explicitly, like every flow module: `dev.voir.areca:attachments` would read as a file
// library rather than a set of screen parts. See `flow/FLOWS.md` §8.
mavenPublishing {
    coordinates(
        groupId = project.group.toString(),
        artifactId = "flow-attachments",
        version = project.version.toString(),
    )

    pom {
        name.set("Areca Attachments Flow")
        description.set(
            "Parts and recipes for attaching, listing and viewing files, built on the Areca design system.",
        )
    }
}

extra["bundledIconsPackage"] = "dev.voir.areca.flow.attachments.icon"
extra["bundledIcons"] = listOf(
    "AlertCircle",
    "Camera",
    "CloudOff",
    "Download",
    "DownloadCloud",
    "FileText",
    "Image",
    "LinkExternal",
    "Paperclip",
    "Pencil",
    "Plus",
    "Share",
    "Trash",
    "XClose",
)
apply(from = rootProject.file("gradle/bundled-icons.gradle.kts"))
