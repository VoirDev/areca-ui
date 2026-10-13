/**
 * Vendors the handful of icons a module draws, so no module has to depend on `:standalone:icons`.
 *
 * Depending on `:standalone:icons` would put every icon of the set into each consumer's graph, so each module
 * keeps its own copies instead: ordinary committed sources, `internal`, invisible in the published API, and
 * identical to the originals apart from the package, the receiver, the visibility and one KDoc line.
 *
 * Copies are committed rather than generated into the build directory so a module stays readable and
 * buildable if `:standalone:icons` ever moves to a repository of its own. `syncBundledIcons` rewrites them;
 * `verifyBundledIcons` runs as part of `check` and fails when a copy has drifted from the original or a file
 * the task does not own sits in the package.
 *
 * The target package belongs to this script: `syncBundledIcons` deletes any file in it that is not a declared
 * icon or the receiver, so hand-written code must live elsewhere.
 *
 * The originals are converted from Lucide (ISC) and Tabler Icons (MIT), whose licences require their notices
 * in every copy. Each copy keeps its "Converted from" attribution, and the module also gets
 * `src/commonMain/resources/META-INF/<package>/ICONS-NOTICE.md`, a copy of `standalone/icons/NOTICE.md` that
 * ships in its jar, its AAR and its sources jars.
 *
 * Applying modules declare what they need before applying this script:
 *
 * ```kotlin
 * extra["bundledIconsPackage"] = "dev.voir.areca.icon"
 * extra["bundledIcons"] = listOf("Check", "ChevronLeft")
 * apply(from = rootProject.file("gradle/bundled-icons.gradle.kts"))
 * ```
 */

/** Package the copies are written to. Mirrors the applying module's own namespace. */
private val targetPackage: String = extra["bundledIconsPackage"] as? String
    ?: throw GradleException("Set extra[\"bundledIconsPackage\"] to the package the icon copies live in.")

/** Simple names of the icons this module draws, as they are declared in `:standalone:icons`. */
private val iconNames: List<String> = run {
    val declared = extra["bundledIcons"]
    if (declared !is List<*> || declared.any { it !is String }) {
        throw GradleException("Set extra[\"bundledIcons\"] to a list of icon names.")
    }
    val names = declared.map { it as String }
    val repeated = names.groupingBy { it }.eachCount().filterValues { it > 1 }.keys
    if (repeated.isNotEmpty()) {
        throw GradleException("`bundledIcons` lists ${repeated.joinToString()} more than once.")
    }
    names.sorted()
}

/** Receiver the copied extension properties hang off, standing in for `ArecaIcons`. */
private val receiver = "BundledIcons"

/** Source of truth every copy is derived from. */
private val originDirectory = rootProject.layout.projectDirectory
    .dir("standalone/icons/src/commonMain/kotlin/dev/voir/areca/icons")

/** Notice of the converted originals. */
private val originNotice = rootProject.layout.projectDirectory.file("standalone/icons/NOTICE.md")

/** Where the copies live in the applying module. */
private val targetDirectory = layout.projectDirectory
    .dir("src/commonMain/kotlin/${targetPackage.replace('.', '/')}")

/** Resource path of the notice, unique per module so merged application resources never collide. */
private val noticePath = "META-INF/$targetPackage/ICONS-NOTICE.md"

/** Where the notice lives in the applying module. */
private val noticeFile = layout.projectDirectory.file("src/commonMain/resources/$noticePath")

/** Warning that the file is derived, aimed at whoever opens it expecting to edit it. */
private val generatedHeader = """
    // Copied from `:standalone:icons` by the `syncBundledIcons` task. Do not edit by hand.
    // Add or remove entries through `bundledIcons` in this module's build script.
""".trimIndent()

/**
 * Renders the copy of one icon.
 *
 * The original's KDoc, including its attribution, stays the copy's only KDoc; the transform adds one line
 * naming the original and the notice, and turns `public val ArecaIcons.<Name>` into
 * `internal val BundledIcons.<Name>`.
 *
 * @param name Simple name of the icon, as declared in `:standalone:icons`.
 * @return Full text of the copied source file.
 * @throws GradleException When `:standalone:icons` has no such icon, or its shape is not the one this
 *   transform understands.
 */
private fun renderIcon(name: String): String {
    val origin = originDirectory.file("$name.kt").asFile
    if (!origin.isFile) {
        throw GradleException("`:standalone:icons` declares no icon named `$name` (looked in $origin).")
    }

    val text = origin.readText()
    val declaration = "\n */\npublic val ArecaIcons.$name: ImageVector\n"
    val packageLine = "package dev.voir.areca.icons\n"
    if (!text.startsWith(packageLine) || text.split(declaration).size != 2 || text.split("\npublic ").size != 2) {
        throw GradleException(
            "$origin is not one KDoc'd `public val ArecaIcons.$name: ImageVector` — " +
                "the copy transform needs updating.",
        )
    }

    val body = text
        .replaceFirst(packageLine, "package $targetPackage\n")
        .replaceFirst(
            declaration,
            "\n *\n * Copy of `ArecaIcons.$name` from `dev.voir.areca:icons`; its licence notice ships as" +
                "\n * `$noticePath`." +
                "\n */\ninternal val $receiver.$name: ImageVector\n",
        )

    val attribution = Regex("Converted from [^\n]*").find(text)?.value
    if ("public " in body || body.split("internal val $receiver.$name:").size != 2 ||
        (attribution != null && attribution !in body)
    ) {
        throw GradleException(
            "Copying $origin produced an unexpected declaration — the copy transform needs updating.",
        )
    }

    return "$generatedHeader\n\n$body"
}

/**
 * Renders the receiver object the copied properties extend.
 *
 * @return Full text of the receiver's source file.
 */
private fun renderReceiver(): String = buildString {
    appendLine(generatedHeader)
    appendLine()
    appendLine("package $targetPackage")
    appendLine()
    appendLine("/**")
    appendLine(" * Namespace for the icons this module draws.")
    appendLine(" *")
    appendLine(" * Stands in for `ArecaIcons` so the module does not depend on `:standalone:icons`. Callers outside the")
    appendLine(" * module reach for `ArecaIcons` from `dev.voir.areca:icons` instead.")
    appendLine(" */")
    appendLine("internal object $receiver")
}

/**
 * Renders the notice resource shipped with the copies.
 *
 * @return Full text of the notice: the `:standalone:icons` notice behind a one-line provenance comment.
 */
private fun renderNotice(): String =
    "<!-- Copied from `standalone/icons/NOTICE.md` by the `syncBundledIcons` task. Do not edit by hand. -->\n\n" +
        originNotice.asFile.readText()

/**
 * Every file this module owns under [targetDirectory], keyed by file name.
 *
 * Rendered while the build is configured rather than inside the task actions: the tasks then
 * capture plain strings, which is what the configuration cache can carry across builds. Reading
 * the origin here also registers it with the cache, so editing an icon invalidates the entry.
 */
private val rendered: Map<String, String> =
    iconNames.associate { "$it.kt" to renderIcon(it) } + ("$receiver.kt" to renderReceiver())

/** Text of [noticeFile], rendered for the same reason as [rendered]. */
private val renderedNotice: String = renderNotice()

/** Rewrites the copies and the notice from `:standalone:icons`. */
val syncBundledIcons = tasks.register("syncBundledIcons") {
    group = "build setup"
    description = "Rewrites this module's icon copies and their notice from `:standalone:icons`."

    val expected = rendered
    val notice = renderedNotice
    val target = targetDirectory.asFile
    val noticeTarget = noticeFile.asFile
    val iconCount = iconNames.size
    val packageName = targetPackage
    inputs.property("rendered", expected)
    inputs.property("notice", notice)
    outputs.dir(target)
    outputs.file(noticeTarget)

    doLast {
        target.mkdirs()
        // Removing first keeps a dropped icon from lingering as an unreferenced file.
        target.listFiles()?.filter { it.isFile && it.name !in expected }?.forEach { it.delete() }
        expected.forEach { (fileName, text) -> target.resolve(fileName).writeText(text) }
        noticeTarget.parentFile.mkdirs()
        noticeTarget.writeText(notice)
        logger.lifecycle("Bundled icons: $iconCount copied into $packageName.")
    }
}

/** Fails when a copy or the notice has drifted, or the package holds a file this script does not own. */
val verifyBundledIcons = tasks.register("verifyBundledIcons") {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Fails when this module's icon copies or their notice have drifted from `:standalone:icons`."

    val expected = rendered
    val notice = renderedNotice
    val target = targetDirectory.asFile
    val noticeTarget = noticeFile.asFile
    val noticeName = noticePath
    val projectPath = project.path
    inputs.property("rendered", expected)
    inputs.property("notice", notice)
    inputs.dir(target).withPathSensitivity(PathSensitivity.RELATIVE)
    inputs.files(noticeTarget).withPathSensitivity(PathSensitivity.RELATIVE)

    // Nothing is produced; the marker lets Gradle skip the task when neither side changed.
    val marker = layout.buildDirectory.file("bundled-icons/checked.txt")
    outputs.file(marker)

    doLast {
        val failures = mutableListOf<String>()

        expected.forEach { (fileName, text) ->
            val copy = target.resolve(fileName)
            when {
                !copy.isFile -> failures += "$fileName is missing."
                copy.readText() != text -> failures += "$fileName has drifted from `:standalone:icons`."
            }
        }
        target.listFiles()?.filter { it.isFile && it.name !in expected }?.forEach {
            failures += "${it.name} is not declared in `bundledIcons` — delete it or declare it."
        }
        when {
            !noticeTarget.isFile -> failures += "$noticeName is missing."
            noticeTarget.readText() != notice ->
                failures += "$noticeName has drifted from `standalone/icons/NOTICE.md`."
        }

        marker.get().asFile.apply {
            parentFile.mkdirs()
            writeText("${expected.size + 1} files checked\n")
        }

        if (failures.isNotEmpty()) {
            throw GradleException(
                buildString {
                    appendLine("Bundled icons are out of date (${failures.size}):")
                    failures.forEach { appendLine("  - $it") }
                    appendLine("Run `./gradlew $projectPath:syncBundledIcons` to rewrite them.")
                },
            )
        }

        logger.lifecycle("Bundled icons: ${expected.size + 1} files checked, no drift.")
    }
}

// The sources jars carry the copies as well, so they carry the notice too.
tasks.withType(org.gradle.jvm.tasks.Jar::class.java)
    .matching { it.name == "sourcesJar" || it.name.endsWith("SourcesJar") }
    .configureEach {
        from(noticeFile) {
            into("META-INF/$targetPackage")
        }
    }

tasks.named("check") {
    dependsOn(verifyBundledIcons)
}
