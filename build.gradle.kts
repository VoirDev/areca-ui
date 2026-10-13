@file:OptIn(ExperimentalAbiValidation::class)

import com.vanniktech.maven.publish.MavenPublishBaseExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.abi.AbiValidationMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.abi.ExperimentalAbiValidation
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    // Gives the root project `check` and `build`, which run the repository-wide checks below.
    base
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.serialization) apply false
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidMultiplatformLibrary) apply false
    alias(libs.plugins.composeHotReload) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.mavenPublish) apply false
}

/**
 * Version every module builds and publishes as.
 *
 * `VERSION` is the canonical source, and only the release workflow changes it. The `releaseVersion`
 * Gradle property overrides it for a one-off build; the release workflow passes it with the same
 * value, so the staged artifacts state their version explicitly.
 */
val projectVersion = providers.gradleProperty("releaseVersion")
    .orElse(providers.fileContents(layout.projectDirectory.file("VERSION")).asText.map(String::trim))
    .get()

/**
 * Maven repository the release workflow stages publications in, under `build/release-staging`.
 *
 * The release pull request's Verify run keeps its files, and publication uploads them unchanged
 * after merge, so `publishAllPublicationsToReleaseStagingRepository` is the task that produces the
 * released bytes.
 */
val releaseStagingRepository = "ReleaseStaging"

/**
 * Whether this invocation publishes only to an unsigned repository: `~/.m2`, or the release staging
 * repository whose files the release workflow signs at publication.
 */
val isUnsignedPublish = gradle.startParameter.taskNames.any {
    it.contains("publishToMavenLocal", ignoreCase = true) ||
        it.contains(releaseStagingRepository, ignoreCase = true)
}

/**
 * Lowest Java version the published JVM and Android bytecode runs on.
 *
 * The build itself runs on the JDK 21 toolchain each module declares; only the emitted class files
 * are held to this floor, so a desktop consumer on Java 17 can load them.
 */
val publishedJvmTarget = JvmTarget.JVM_17

/**
 * Fails the build when the Claude Code plugin manifest states a version other than `VERSION`.
 *
 * The repository root is the `areca` plugin, whose skills describe the library at this version, so
 * `.claude-plugin/plugin.json` repeats `VERSION` exactly. `ci/release/prepare-version` writes both,
 * and this check, part of `check`, keeps any other change from moving one without the other.
 */
val verifyPluginVersion = tasks.register("verifyPluginVersion") {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Checks that .claude-plugin/plugin.json states the version in VERSION."

    val versionFile = layout.projectDirectory.file("VERSION").asFile
    val manifestFile = layout.projectDirectory.file(".claude-plugin/plugin.json").asFile
    inputs.files(versionFile, manifestFile).withPathSensitivity(PathSensitivity.RELATIVE)

    // Nothing is produced; the marker lets Gradle skip the task when neither file changed.
    val marker = layout.buildDirectory.file("plugin-version/checked.txt")
    outputs.file(marker)

    doLast {
        val version = versionFile.readText().trim()
        val manifest = groovy.json.JsonSlurper().parse(manifestFile) as Map<*, *>
        val manifestVersion = manifest["version"]
        if (manifestVersion != version) {
            throw GradleException(
                ".claude-plugin/plugin.json states version '$manifestVersion', but VERSION holds " +
                    "'$version'. The plugin version follows VERSION exactly; set it to '$version'.",
            )
        }
        marker.get().asFile.apply {
            parentFile.mkdirs()
            writeText("$version\n")
        }
    }
}

tasks.named("check") {
    dependsOn(verifyPluginVersion)
}

allprojects {
    group = "dev.voir.areca"
    version = projectVersion
}

subprojects {
    plugins.withId("com.vanniktech.maven.publish") {
        // Covers the `jvm()` and `android` targets alike: both compile through `KotlinJvmCompile`.
        tasks.withType<KotlinJvmCompile>().configureEach {
            compilerOptions.jvmTarget.set(publishedJvmTarget)
        }
        tasks.withType<JavaCompile>().configureEach {
            options.release.set(publishedJvmTarget.target.toInt())
        }

        // Every published module keeps its public API in a committed dump under `api/`: one for the
        // `jvm()` target and one klib dump for the iOS targets. The Kotlin plugin does not dump the
        // `android` target, whose public declarations are the same `expect`/`actual` API. `check`
        // compares the compiled API with the dump, so an API change fails the build until
        // `updateLegacyAbi` rewrites the dump and the dump's diff is reviewed with the change.
        plugins.withId("org.jetbrains.kotlin.multiplatform") {
            // `abiValidation` is an extension of `kotlin`, which has no type-safe accessor here.
            (extensions.getByType<KotlinMultiplatformExtension>() as ExtensionAware).extensions
                .configure<AbiValidationMultiplatformExtension> {
                    enabled.set(true)
                    klib {
                        enabled.set(true)
                    }
                }
            // The Kotlin plugin registers the check but leaves it out of `check`.
            tasks.named("check") {
                dependsOn("checkLegacyAbi")
            }
        }

        extensions.configure<PublishingExtension> {
            repositories {
                maven {
                    name = releaseStagingRepository
                    url = uri(rootProject.layout.buildDirectory.dir("release-staging"))
                }
            }
        }

        // The POM fields every published module shares, so that no module can publish without a
        // licence, a source location or a developer. A module adds only its own `name` and
        // `description` in its `mavenPublishing { pom { … } }` block; Maven Central rejects a POM
        // missing either.
        extensions.configure<MavenPublishBaseExtension> {
            publishToMavenCentral()
            if (!isUnsignedPublish) {
                signAllPublications()
            }

            pom {
                url.set("https://github.com/VoirDev/areca-ui/")

                licenses {
                    license {
                        name.set("Apache License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }

                developers {
                    developer {
                        id.set("checksanity")
                        name.set("Gary Bezruchko")
                        email.set("hello@voir.dev")
                        organization.set("VOIR")
                        organizationUrl.set("https://voir.dev")
                    }
                }

                scm {
                    url.set("https://github.com/VoirDev/areca-ui/")
                    connection.set("scm:git:git://github.com/VoirDev/areca-ui.git")
                    developerConnection.set("scm:git:ssh://git@github.com/VoirDev/areca-ui.git")
                }
            }
        }
    }
}
