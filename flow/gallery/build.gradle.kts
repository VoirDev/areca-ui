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
        namespace = "dev.voir.areca.flow.gallery"
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

            // `api` for the same reason as the other flow modules: the parts are composed beside
            // foundation and media components in a consumer's screen — inside a `MediaViewer`'s bars,
            // over a `Carousel` — and take their types.
            api(projects.ui.foundation)
            api(projects.ui.media)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }

        // UI tests press the parts with real pointer input, which needs Skia's native library for
        // this host.
        jvmTest.dependencies {
            implementation(libs.compose.uiTest)
            implementation(compose.desktop.currentOs)
        }
    }
}

// Stated explicitly, like every flow module: `dev.voir.areca:gallery` would read as a picture
// library rather than a set of screen parts. See `flow/FLOWS.md` §8.
mavenPublishing {
    coordinates(
        groupId = project.group.toString(),
        artifactId = "flow-gallery",
        version = project.version.toString(),
    )

    pom {
        name.set("Areca Gallery Flow")
        description.set(
            "Parts and recipes for product galleries — captions, author lines, the primary action and grouped grids — built on Areca media.",
        )
    }
}
