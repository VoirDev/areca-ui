/**
 * Conventions from `SHOWCASES.md` that are cheap to check and expensive to lose, run as part of
 * `check`.
 */

private val playgroundSources = layout.projectDirectory
    .dir("src/commonMain/kotlin/dev/voir/areca/playground")

/** Chrome may reach for tokens and modifier utilities, but never for a design-system component. */
private val chromePackages = listOf("showcase", "shell")

/**
 * Design-system packages a showcase page may import but the chrome may not. See SHOWCASES.md §5.
 *
 * Every published component module is listed, so a component the chrome draws with can never be the
 * one under test.
 */
private val designSystemImports = listOf(
    "dev.voir.areca.foundation.",
    "dev.voir.areca.charts.",
    "dev.voir.areca.crop.",
    "dev.voir.areca.datetime.",
    "dev.voir.areca.keyboard.",
    "dev.voir.areca.media.",
    "dev.voir.areca.flow.",
    "dev.voir.areca.icons.",
)

/**
 * Fails the build when the playground drifts from the authoring contract.
 *
 * Reads sources directly rather than compiling, so it stays fast and runs before the Kotlin tasks
 * that would fail more confusingly.
 */
val verifyShowcaseConventions = tasks.register("verifyShowcaseConventions") {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Checks the playground against the conventions in SHOWCASES.md."

    val sourceRoot = playgroundSources.asFile
    val chrome = chromePackages
    val designSystem = designSystemImports

    inputs.dir(sourceRoot).withPathSensitivity(PathSensitivity.RELATIVE)

    // Nothing is produced; the marker lets Gradle skip the task when no source changed.
    val marker = layout.buildDirectory.file("showcase-conventions/checked.txt")
    outputs.file(marker)

    doLast {
        val kotlinFiles = sourceRoot.walkTopDown().filter { it.isFile && it.extension == "kt" }.toList()
        val failures = mutableListOf<String>()

        kotlinFiles.forEach { file ->
            val relative = file.relativeTo(sourceRoot).invariantSeparatorsPath
            val lines = file.readLines()

            // 1. Material3 was the playground's second visual language. It is not coming back.
            lines.forEachIndexed { index, line ->
                if (line.trimStart().startsWith("import androidx.compose.material3")) {
                    failures += "$relative:${index + 1} imports Material3. " +
                        "The playground renders in Areca only — see SHOWCASES.md §5."
                }
            }

            // 2. The shell must survive a broken component, because it is how you look at one.
            val topLevelPackage = relative.substringBefore('/')
            if (topLevelPackage in chrome) {
                lines.forEachIndexed { index, line ->
                    val trimmed = line.trimStart()
                    if (!trimmed.startsWith("import ")) return@forEachIndexed
                    designSystem.firstOrNull { trimmed.startsWith("import $it") }?.let { offending ->
                        failures += "$relative:${index + 1} imports $offending from the chrome. " +
                            "Chrome is built from theme tokens and raw Compose — see SHOWCASES.md §5."
                    }
                }
            }

            // 3. A page named one thing and declaring another is how entries go missing.
            if (file.name.endsWith("Showcase.kt") && topLevelPackage !in chrome) {
                val expected = file.name.removeSuffix(".kt")
                val declared = lines.any { it.startsWith("fun $expected(") }
                if (!declared) {
                    failures += "$relative does not declare `fun $expected()`. " +
                        "File name and entry point must match — see SHOWCASES.md §2."
                }
            }
        }

        // 4. A page that is not registered is a page nobody can reach.
        // The registry is split into one `*Showcases.kt` file per group; count across all of them.
        val registryFiles = sourceRoot.resolve("registry").listFiles { file -> file.extension == "kt" }.orEmpty()
        if (registryFiles.none { it.name == "ShowcaseRegistry.kt" }) {
            failures += "registry/ShowcaseRegistry.kt is missing."
        } else {
            val registryText = registryFiles.joinToString("\n") { it.readText() }
            val entryPattern = Regex("""^fun ([A-Za-z]+Showcase)\(""", RegexOption.MULTILINE)

            kotlinFiles
                .filter { it.relativeTo(sourceRoot).invariantSeparatorsPath.substringBefore('/') !in chrome }
                .forEach { file ->
                    entryPattern.findAll(file.readText()).forEach { match ->
                        val entryPoint = match.groupValues[1]
                        val references = Regex("""\b$entryPoint\(\)""").findAll(registryText).count()
                        if (references != 1) {
                            val relative = file.relativeTo(sourceRoot).invariantSeparatorsPath
                            failures += "$relative declares `$entryPoint()`, referenced " +
                                "$references times in the registry — it must be exactly once. " +
                                "See SHOWCASES.md §8."
                        }
                    }
                }
        }

        marker.get().asFile.apply {
            parentFile.mkdirs()
            writeText("${kotlinFiles.size} files checked\n")
        }

        if (failures.isNotEmpty()) {
            throw GradleException(
                buildString {
                    appendLine("Playground conventions violated (${failures.size}):")
                    failures.forEach { appendLine("  - $it") }
                },
            )
        }

        logger.lifecycle("Showcase conventions: ${kotlinFiles.size} files checked, no violations.")
    }
}

tasks.named("check") {
    dependsOn(verifyShowcaseConventions)
}
