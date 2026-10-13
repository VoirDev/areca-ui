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
        namespace = "dev.voir.areca.flow.auth"
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

            // `api`: foundation types are part of this module's public signatures — `ButtonSize`
            // and `LocalButtonSize` (SsoButton), `IconButtonSize` and `LocalIconButtonSize`
            // (SsoIconButton), `NoticeTone` (AuthBanner), `Requirement`, `RequirementState` and
            // `defaultRequirementStateDescription` (AuthPasswordRequirements) and `ArecaTheme`
            // (AuthScaffold's gutter default). The parts are also meant to be composed beside
            // foundation components in a consumer's own screen.
            api(projects.ui.foundation)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}

// Every other module takes its artifact id from its directory name, which would publish this one
// as `dev.voir.areca:auth`. That name says nothing next to `foundation`, `charts` and `icons`, so
// this module states its coordinates instead. See `flow/FLOWS.md`.
mavenPublishing {
    coordinates(
        groupId = project.group.toString(),
        artifactId = "flow-auth",
        version = project.version.toString(),
    )

    pom {
        name.set("Areca Auth Flow")
        description.set(
            "Parts and recipes for authentication screens built on the Areca design system, free of business logic.",
        )
    }
}

extra["bundledIconsPackage"] = "dev.voir.areca.flow.auth.icon"
extra["bundledIcons"] = listOf(
    "AlertCircle",
    "Check",
    "Mail",
    "SocialApple",
    "SocialFacebook",
    "SocialGoogle",
)
apply(from = rootProject.file("gradle/bundled-icons.gradle.kts"))
