import java.util.zip.ZipFile

/**
 * Resolves every staged Areca component as a Java 17 desktop consumer would, and checks what arrives.
 *
 * Resolution exercises the POMs, the Gradle module metadata and the coordinates between modules; the
 * bytecode check holds every Areca class to the Java 17 floor the root build compiles to. Takes the
 * staged repository (`stagingRepository`) and the root coordinates to resolve (`coordinatesFile`,
 * one `group:artifact:version` per line, as `ci/release/check-staging` writes them).
 */

plugins {
    `java-base`
}

/** Highest class-file major version a published class may have: 61 is Java 17. */
val maximumClassFileMajor = 61

/** Root coordinates of every staged component, read from `coordinatesFile`. */
val coordinates: List<String> = providers.fileContents(
    layout.projectDirectory.file(providers.gradleProperty("coordinatesFile")),
).asText.get().lines().filter(String::isNotBlank)

/** The runtime classpath of a desktop application on Java 17 that depends on every component. */
val releaseClasspath: Configuration = configurations.create("releaseClasspath") {
    isCanBeConsumed = false
    isCanBeResolved = true
    attributes {
        attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_RUNTIME))
        attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.LIBRARY))
        attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, objects.named(LibraryElements.JAR))
        attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
        attribute(TargetJvmEnvironment.TARGET_JVM_ENVIRONMENT_ATTRIBUTE, objects.named(TargetJvmEnvironment.STANDARD_JVM))
        attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, 17)
    }
}

dependencies {
    coordinates.forEach { releaseClasspath(it) }
}

/** Fails unless every staged component resolves for Java 17 and every Areca class runs on it. */
val verifyReleaseConsumer = tasks.register("verifyReleaseConsumer") {
    val artifacts = releaseClasspath.incoming.artifacts.resolvedArtifacts
    val expected = coordinates.map { it.substringBeforeLast(':') }.toSet()
    val version = coordinates.first().substringAfterLast(':')
    val maximumMajor = maximumClassFileMajor

    doLast {
        val failures = mutableListOf<String>()
        val resolvedRoots = mutableSetOf<String>()

        artifacts.get()
            .filter { (it.id.componentIdentifier as? ModuleComponentIdentifier)?.group == "dev.voir.areca" }
            .forEach { artifact ->
                val id = artifact.id.componentIdentifier as ModuleComponentIdentifier
                if (id.version != version) {
                    failures += "${id.displayName} resolved instead of version $version."
                }
                resolvedRoots += "${id.group}:${id.module.removeSuffix("-jvm")}"

                ZipFile(artifact.file).use { jar ->
                    jar.entries().asSequence()
                        .filter { it.name.endsWith(".class") && !it.name.startsWith("META-INF/") }
                        .forEach { entry ->
                            val header = jar.getInputStream(entry).use { it.readNBytes(8) }
                            val major = ((header[6].toInt() and 0xFF) shl 8) or (header[7].toInt() and 0xFF)
                            if (major > maximumMajor) {
                                failures += "${artifact.file.name}: ${entry.name} has class-file major $major, " +
                                    "above the Java 17 floor ($maximumMajor)."
                            }
                        }
                }
            }

        (expected - resolvedRoots).forEach { failures += "$it did not resolve to a JVM artifact." }

        if (failures.isNotEmpty()) {
            throw GradleException(
                buildString {
                    appendLine("The staged release fails as a Java 17 dependency (${failures.size}):")
                    failures.take(50).forEach { appendLine("  - $it") }
                },
            )
        }
        logger.lifecycle("Resolved ${expected.size} components for Java 17; every Areca class is Java 17 bytecode.")
    }
}
