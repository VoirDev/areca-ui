// A stand-alone build, separate from the repository's own, that consumes the staged release the way
// a desktop application does. `ci/release/build-artifacts` runs it; see `build.gradle.kts`.
rootProject.name = "areca-release-consumer"

dependencyResolutionManagement {
    repositories {
        exclusiveContent {
            forRepository {
                maven {
                    name = "ReleaseStaging"
                    url = uri(providers.gradleProperty("stagingRepository").get())
                }
            }
            filter { includeGroup("dev.voir.areca") }
        }
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
