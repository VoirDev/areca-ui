rootProject.name = "areca"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

// The Areca design system: theme-bound, and useless without `ArecaThemeProvider`.
include(":ui:charts")
include(":ui:crop")
include(":ui:datetime")
include(":ui:foundation")
include(":ui:keyboard")

// Whole screens and screen sequences, assembled from `ui`. Theme-bound and opinionated about
// composition, but free of business logic. See `flow/FLOWS.md`.
include(":flow:attachments")
include(":flow:auth")
include(":flow:profile")

// Independently usable: no theme, no dependency on anything else in this repository.
include(":standalone:icons")
include(":standalone:viewmodel")

// The showcase application. Not published.
include(":playground")
