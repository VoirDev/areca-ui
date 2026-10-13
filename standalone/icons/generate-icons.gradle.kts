/**
 * Regenerates every icon of `:standalone:icons` and its `NOTICE.md` from Lucide and Tabler Icons SVG sources.
 *
 * `icons.tsv` names the upstream drawing behind each `ArecaIcons` property. The task reads those SVGs,
 * converts their elements into `ImageVector` builder calls, and rewrites one Kotlin file per icon, so the whole
 * set shares one shape and one dialect. Icons marked `manual` in the mapping are hand-maintained and left as
 * they are; any other file in the package that the mapping does not name is deleted.
 *
 * The task is a development tool. Nothing in `check` or CI runs it, and it needs the upstream packages on disk:
 *
 * ```
 * ./gradlew :standalone:icons:generateIcons \
 *     -Plucide=<unpacked lucide-static package> -Ptabler=<unpacked @tabler/icons package>
 * ```
 *
 * The packages are the npm tarballs of `lucide-static` and `@tabler/icons` at the versions in
 * [GenerateIcons.LUCIDE_VERSION] and [GenerateIcons.TABLER_VERSION]; moving to other versions
 * means changing those constants, regenerating and reviewing the diff.
 */

import java.io.File
import java.util.Locale
import javax.xml.XMLConstants
import javax.xml.parsers.DocumentBuilderFactory
import org.w3c.dom.Element

/**
 * Converts the SVG drawings listed in the mapping into `ImageVector` sources.
 *
 * The task has no declared outputs on purpose: it rewrites committed sources and should run every time it is
 * asked to.
 */
abstract class GenerateIcons : DefaultTask() {

    /** Mapping from `ArecaIcons` property names to upstream drawings. */
    @get:InputFile
    abstract val mapping: RegularFileProperty

    /** Unpacked `lucide-static` package, or its `icons` directory. */
    @get:Input
    @get:Optional
    abstract val lucidePath: Property<String>

    /** Unpacked `@tabler/icons` package, or its `icons/outline` directory. */
    @get:Input
    @get:Optional
    abstract val tablerPath: Property<String>

    /** Package directory the icon sources are written to. */
    @get:Internal
    abstract val iconDirectory: DirectoryProperty

    /** Notice file regenerated from the upstream licences. */
    @get:Internal
    abstract val noticeFile: RegularFileProperty

    /**
     * Regenerates the icon sources and the notice.
     *
     * @throws GradleException When a source package is missing or has an unexpected version, the mapping is
     *   malformed, or an SVG uses something the converter does not understand.
     */
    @TaskAction
    fun generate() {
        val lucide = sourcePackage(lucidePath.orNull, "lucide", listOf("icons"), LUCIDE_VERSION)
        val tabler = sourcePackage(tablerPath.orNull, "tabler", listOf("icons/outline", "outline"), TABLER_VERSION)
        val rows = readMapping(mapping.get().asFile)
        val directory = iconDirectory.get().asFile

        rows.filter { it.set == IconSet.Manual }.forEach {
            if (!directory.resolve("${it.name}.kt").isFile) {
                throw GradleException("`${it.name}` is marked manual but ${it.name}.kt does not exist.")
            }
        }

        val keep = rows.map { "${it.name}.kt" }.toSet() + "ArecaIcons.kt"
        directory.listFiles()?.filter { it.isFile && it.name !in keep }?.forEach { it.delete() }

        rows.filter { it.set != IconSet.Manual }.forEach { row ->
            val svg = when (row.set) {
                IconSet.Lucide -> lucide.icons.resolve("${row.id}.svg")
                else -> tabler.icons.resolve("${row.id}.svg")
            }
            if (!svg.isFile) {
                throw GradleException("`${row.name}`: ${row.set.label} has no icon `${row.id}` ($svg).")
            }
            val paths = try {
                readSvg(svg, isFilled = "filled" in row.flags)
            } catch (e: IllegalArgumentException) {
                throw GradleException("`${row.name}`: cannot convert $svg: ${e.message}", e)
            }
            directory.resolve("${row.name}.kt").writeText(renderIcon(row, paths))
        }

        noticeFile.get().asFile.writeText(renderNotice(lucide, tabler, rows))
        val manualCount = rows.count { it.set == IconSet.Manual }
        logger.lifecycle("Generated ${rows.size - manualCount} icons; kept $manualCount manual ones.")
    }

    /**
     * Upstream package on disk.
     *
     * @property root Package root holding `package.json` and `LICENSE`.
     * @property icons Directory holding one SVG per icon id.
     * @property version Version read from `package.json`.
     */
    private class SourcePackage(val root: File, val icons: File, val version: String)

    /**
     * Upstream set an icon is converted from.
     *
     * @property label Name used in KDoc and messages.
     * @property licence Short licence name used in KDoc.
     */
    private enum class IconSet(val label: String, val licence: String) {
        /** Lucide (`lucide-static`), ISC. */
        Lucide("Lucide", "ISC"),

        /** Tabler Icons outline style (`@tabler/icons`), MIT. */
        Tabler("Tabler Icons", "MIT"),

        /** Hand-maintained file that is not regenerated. */
        Manual("manual", ""),
    }

    /**
     * One row of the mapping.
     *
     * @property name `ArecaIcons` property and file name.
     * @property set Upstream set.
     * @property id Upstream icon id, `-` for manual icons.
     * @property flags `mirror`, `filled` and `same-as=<Name>` flags.
     */
    private class MappingRow(val name: String, val set: IconSet, val id: String, val flags: Set<String>) {
        /** Name whose drawing this row deliberately repeats, or `null` when the drawing is its own. */
        val sameAs: String? = flags.firstOrNull { it.startsWith("same-as=") }?.removePrefix("same-as=")
    }

    /**
     * Paint and geometry of one generated `path { … }` block.
     *
     * @property isFilled Whether the block is filled in black.
     * @property isStroked Whether the block is stroked in black at the set's 2-unit width.
     * @property commands Builder calls, one per line, without indentation.
     */
    private class VectorPath(val isFilled: Boolean, val isStroked: Boolean, val commands: List<String>)

    /**
     * Resolves and checks an upstream package.
     *
     * @param path Value of the Gradle property, a package root or its SVG directory.
     * @param property Name of the Gradle property, for messages.
     * @param iconDirectories SVG directories relative to the package root, in lookup order.
     * @param expectedVersion Version the mapping and the notice are written against.
     * @return The resolved package.
     */
    private fun sourcePackage(
        path: String?,
        property: String,
        iconDirectories: List<String>,
        expectedVersion: String,
    ): SourcePackage {
        if (path == null) {
            throw GradleException("Pass the unpacked $property package with -P$property=<directory>.")
        }
        val given = File(path).absoluteFile
        val root = generateSequence(given) { it.parentFile }.take(3).firstOrNull { it.resolve("package.json").isFile }
            ?: throw GradleException("No package.json at or above $given.")
        val icons = iconDirectories.map(root::resolve).firstOrNull { it.isDirectory }
            ?: throw GradleException("$root has none of ${iconDirectories.joinToString()}.")
        val version = Regex("\"version\"\\s*:\\s*\"([^\"]+)\"").find(root.resolve("package.json").readText())
            ?.groupValues?.get(1)
            ?: throw GradleException("${root.resolve("package.json")} declares no version.")
        if (version != expectedVersion) {
            throw GradleException(
                "$property is $version but the mapping and NOTICE.md are written against $expectedVersion. " +
                    "Update the expected version in generate-icons.gradle.kts, regenerate and review the diff.",
            )
        }
        return SourcePackage(root, icons, version)
    }

    /**
     * Reads the mapping.
     *
     * @param file Tab-separated mapping; `#` starts a comment line.
     * @return Rows sorted by name.
     */
    private fun readMapping(file: File): List<MappingRow> {
        val rows = file.readLines().filter { it.isNotBlank() && !it.startsWith("#") }.map { line ->
            val columns = line.split('\t')
            if (columns.size !in 3..4) {
                throw GradleException("Malformed mapping line: `$line`.")
            }
            val set = IconSet.entries.firstOrNull { it.name.lowercase() == columns[1] }
                ?: throw GradleException("Unknown set `${columns[1]}` in `$line`.")
            val flags = columns.getOrNull(3).orEmpty().split(',').filter { it.isNotEmpty() }.toSet()
            flags.filterNot { it == "mirror" || it == "filled" || it.startsWith("same-as=") }.forEach {
                throw GradleException("Unknown flag `$it` in `$line`.")
            }
            MappingRow(columns[0], set, columns[2], flags)
        }
        val names = rows.map { it.name }
        names.groupingBy { it }.eachCount().filter { it.value > 1 }.keys.forEach {
            throw GradleException("`$it` is mapped more than once.")
        }
        rows.forEach { row ->
            val sameAs = row.sameAs
            if (sameAs != null && rows.none { it.name == sameAs && it.set == row.set && it.id == row.id }) {
                throw GradleException("`${row.name}` claims the drawing of `$sameAs`, which maps elsewhere.")
            }
        }
        rows.filter { it.set != IconSet.Manual && it.sameAs == null }
            .groupBy { Triple(it.set, it.id, "filled" in it.flags) }
            .values.filter { it.size > 1 }
            .forEach { group ->
                throw GradleException(
                    "${group.joinToString { "`${it.name}`" }} share one drawing; mark all but one `same-as=<Name>`.",
                )
            }
        return rows.sortedBy { it.name }
    }

    /**
     * Converts one SVG into path blocks.
     *
     * Consecutive stroke-only elements share one block; filled elements keep their own so their fill rules do
     * not interact.
     *
     * @param file SVG on the 24-unit grid with a 2-unit round stroke, as Lucide and Tabler outline icons are.
     * @param isFilled Whether every element is also filled.
     * @return The path blocks in drawing order.
     * @throws IllegalArgumentException When the SVG leaves that grid or uses unsupported markup.
     */
    private fun readSvg(file: File, isFilled: Boolean): List<VectorPath> {
        val factory = DocumentBuilderFactory.newInstance().apply {
            setFeature("http://apache.org/xml/features/disallow-doctype-decl", true)
            setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "")
            setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "")
        }
        val svg = factory.newDocumentBuilder().parse(file).documentElement
        require(svg.tagName == "svg") { "root element is <${svg.tagName}>" }
        mapOf(
            "viewBox" to "0 0 24 24",
            "fill" to "none",
            "stroke" to "currentColor",
            "stroke-width" to "2",
            "stroke-linecap" to "round",
            "stroke-linejoin" to "round",
        ).forEach { (attribute, expected) ->
            require(svg.getAttribute(attribute) == expected) {
                "<svg $attribute=\"${svg.getAttribute(attribute)}\">, expected \"$expected\""
            }
        }

        val blocks = mutableListOf<VectorPath>()
        val children = svg.childNodes
        for (index in 0 until children.length) {
            val element = children.item(index) as? Element ?: continue
            if (isTablerBoundingBox(element)) continue
            val fill = paint(element, "fill", default = false) || isFilled
            val stroke = paint(element, "stroke", default = true)
            require(fill || stroke) { "<${element.tagName}> is neither filled nor stroked" }
            val commands = shapeCommands(element)
            val previous = blocks.lastOrNull()
            if (previous != null && !fill && !previous.isFilled && previous.isStroked == stroke) {
                blocks[blocks.lastIndex] =
                    VectorPath(isFilled = false, isStroked = stroke, commands = previous.commands + commands)
            } else {
                blocks += VectorPath(fill, stroke, commands)
            }
        }
        require(blocks.isNotEmpty()) { "no drawable elements" }
        return blocks
    }

    /**
     * Whether [element] is the invisible 24 × 24 box Tabler puts first in every icon.
     *
     * @param element Child of the root `<svg>`.
     * @return `true` for `<path stroke="none" d="M0 0h24v24H0z" fill="none"/>`.
     */
    private fun isTablerBoundingBox(element: Element): Boolean =
        element.tagName == "path" &&
            element.getAttribute("stroke") == "none" &&
            element.getAttribute("fill") == "none" &&
            element.getAttribute("d").replace(" ", "") == "M00h24v24H0z"

    /**
     * Reads a `fill` or `stroke` attribute.
     *
     * @param element Element carrying the attribute.
     * @param attribute `fill` or `stroke`.
     * @param default Value inherited from the root `<svg>` when the element does not set it.
     * @return Whether the element is painted with `currentColor` through [attribute].
     */
    private fun paint(element: Element, attribute: String, default: Boolean): Boolean =
        when (val value = element.getAttribute(attribute)) {
            "" -> default
            "currentColor" -> true
            "none" -> false
            else -> throw IllegalArgumentException("<${element.tagName} $attribute=\"$value\">")
        }

    /**
     * Converts one SVG shape element into absolute `PathBuilder` calls.
     *
     * @param element `path`, `circle`, `ellipse`, `rect`, `line`, `polyline` or `polygon`.
     * @return Builder calls for the shape.
     */
    private fun shapeCommands(element: Element): List<String> {
        val allowed = setOf(
            "d", "cx", "cy", "r", "rx", "ry", "x", "y", "width", "height", "x1", "y1", "x2", "y2", "points",
            "fill", "stroke",
        )
        val attributes = element.attributes
        for (index in 0 until attributes.length) {
            val attribute = attributes.item(index).nodeName
            require(attribute in allowed) { "<${element.tagName}> has unsupported attribute `$attribute`" }
        }

        fun number(name: String, default: Double? = null): Double {
            val value = element.getAttribute(name)
            if (value.isEmpty()) {
                return requireNotNull(default) { "<${element.tagName}> lacks `$name`" }
            }
            return value.toDouble()
        }

        return when (element.tagName) {
            "path" -> pathCommands(element.getAttribute("d"))
            "circle" -> ellipseCommands(number("cx"), number("cy"), number("r"), number("r"))
            "ellipse" -> ellipseCommands(number("cx"), number("cy"), number("rx"), number("ry"))
            "line" -> listOf(
                "moveTo(${f(number("x1"))}, ${f(number("y1"))})",
                "lineTo(${f(number("x2"))}, ${f(number("y2"))})",
            )
            "polyline", "polygon" -> {
                val values = element.getAttribute("points").trim().split(Regex("[\\s,]+")).map(String::toDouble)
                require(values.size >= 4 && values.size % 2 == 0) { "odd point list" }
                val points = values.chunked(2)
                listOf("moveTo(${f(points[0][0])}, ${f(points[0][1])})") +
                    points.drop(1).map { "lineTo(${f(it[0])}, ${f(it[1])})" } +
                    if (element.tagName == "polygon") listOf("close()") else emptyList()
            }
            "rect" -> {
                val rx = element.getAttribute("rx").takeIf { it.isNotEmpty() }?.toDouble()
                val ry = element.getAttribute("ry").takeIf { it.isNotEmpty() }?.toDouble()
                rectCommands(
                    x = number("x", 0.0),
                    y = number("y", 0.0),
                    width = number("width"),
                    height = number("height"),
                    rx = rx ?: ry ?: 0.0,
                    ry = ry ?: rx ?: 0.0,
                )
            }
            else -> throw IllegalArgumentException("unsupported element <${element.tagName}>")
        }
    }

    /**
     * Draws an ellipse as two half arcs, starting and ending at its leftmost point.
     *
     * @param cx Centre x.
     * @param cy Centre y.
     * @param rx Horizontal radius.
     * @param ry Vertical radius.
     * @return Builder calls for the closed ellipse.
     */
    private fun ellipseCommands(cx: Double, cy: Double, rx: Double, ry: Double): List<String> = listOf(
        "moveTo(${f(cx - rx)}, ${f(cy)})",
        "arcTo(${f(rx)}, ${f(ry)}, 0f, true, true, ${f(cx + rx)}, ${f(cy)})",
        "arcTo(${f(rx)}, ${f(ry)}, 0f, true, true, ${f(cx - rx)}, ${f(cy)})",
        "close()",
    )

    /**
     * Draws a rectangle, rounding its corners when a radius is given.
     *
     * @param x Left edge.
     * @param y Top edge.
     * @param width Width.
     * @param height Height.
     * @param rx Horizontal corner radius, clamped to half the width.
     * @param ry Vertical corner radius, clamped to half the height.
     * @return Builder calls for the closed rectangle.
     */
    private fun rectCommands(
        x: Double,
        y: Double,
        width: Double,
        height: Double,
        rx: Double,
        ry: Double,
    ): List<String> {
        val h = rx.coerceAtMost(width / 2)
        val v = ry.coerceAtMost(height / 2)
        val right = x + width
        val bottom = y + height
        if (h <= 0.0 || v <= 0.0) {
            return listOf(
                "moveTo(${f(x)}, ${f(y)})",
                "horizontalLineTo(${f(right)})",
                "verticalLineTo(${f(bottom)})",
                "horizontalLineTo(${f(x)})",
                "close()",
            )
        }
        val corner = "${f(h)}, ${f(v)}, 0f, false, true"
        return listOf(
            "moveTo(${f(x + h)}, ${f(y)})",
            "horizontalLineTo(${f(right - h)})",
            "arcTo($corner, ${f(right)}, ${f(y + v)})",
            "verticalLineTo(${f(bottom - v)})",
            "arcTo($corner, ${f(right - h)}, ${f(bottom)})",
            "horizontalLineTo(${f(x + h)})",
            "arcTo($corner, ${f(x)}, ${f(bottom - v)})",
            "verticalLineTo(${f(y + v)})",
            "arcTo($corner, ${f(x + h)}, ${f(y)})",
            "close()",
        )
    }

    /**
     * Converts SVG path data into absolute `PathBuilder` calls.
     *
     * Relative commands are resolved against the current point, so every generated call is absolute and the
     * coordinates can be read against the 24-unit grid directly.
     *
     * @param data Value of a `d` attribute.
     * @return Builder calls in order.
     */
    private fun pathCommands(data: String): List<String> {
        val number = Regex("[+-]?(?:\\d+\\.?\\d*|\\.\\d+)(?:[eE][+-]?\\d+)?")
        var index = 0
        fun skipSeparators() {
            while (index < data.length && (data[index].isWhitespace() || data[index] == ',')) index++
        }
        fun hasNumber(): Boolean {
            skipSeparators()
            return index < data.length && (data[index].isDigit() || data[index] in "+-.")
        }
        fun next(): Double {
            skipSeparators()
            val match = requireNotNull(number.matchAt(data, index)) { "expected a number at $index in `$data`" }
            index = match.range.last + 1
            return match.value.toDouble()
        }
        fun flag(): Boolean {
            skipSeparators()
            require(index < data.length && data[index] in "01") { "expected an arc flag at $index in `$data`" }
            return data[index++] == '1'
        }

        val out = mutableListOf<String>()
        var x = 0.0
        var y = 0.0
        var startX = 0.0
        var startY = 0.0
        while (true) {
            skipSeparators()
            if (index >= data.length) break
            val command = data[index++]
            require(command in "MmLlHhVvCcSsQqTtAaZz") { "unexpected `$command` in `$data`" }
            val relative = command.isLowerCase()
            if (command == 'Z' || command == 'z') {
                out += "close()"
                x = startX
                y = startY
                continue
            }
            var first = true
            do {
                val dx = if (relative) x else 0.0
                val dy = if (relative) y else 0.0
                when (command.uppercaseChar()) {
                    'M' -> {
                        x = next() + dx
                        y = next() + dy
                        if (first) {
                            out += "moveTo(${f(x)}, ${f(y)})"
                            startX = x
                            startY = y
                        } else {
                            out += "lineTo(${f(x)}, ${f(y)})"
                        }
                    }
                    'L' -> {
                        x = next() + dx
                        y = next() + dy
                        out += "lineTo(${f(x)}, ${f(y)})"
                    }
                    'H' -> {
                        x = next() + dx
                        out += "horizontalLineTo(${f(x)})"
                    }
                    'V' -> {
                        y = next() + dy
                        out += "verticalLineTo(${f(y)})"
                    }
                    'C' -> {
                        val values = List(6) { next() + if (it % 2 == 0) dx else dy }
                        x = values[4]
                        y = values[5]
                        out += "curveTo(${values.joinToString { f(it) }})"
                    }
                    'S' -> {
                        val values = List(4) { next() + if (it % 2 == 0) dx else dy }
                        x = values[2]
                        y = values[3]
                        out += "reflectiveCurveTo(${values.joinToString { f(it) }})"
                    }
                    'Q' -> {
                        val values = List(4) { next() + if (it % 2 == 0) dx else dy }
                        x = values[2]
                        y = values[3]
                        out += "quadTo(${values.joinToString { f(it) }})"
                    }
                    'T' -> {
                        x = next() + dx
                        y = next() + dy
                        out += "reflectiveQuadTo(${f(x)}, ${f(y)})"
                    }
                    'A' -> {
                        val rx = next()
                        val ry = next()
                        val rotation = next()
                        val isLarge = flag()
                        val isSweep = flag()
                        x = next() + dx
                        y = next() + dy
                        out += "arcTo(${f(rx)}, ${f(ry)}, ${f(rotation)}, $isLarge, $isSweep, ${f(x)}, ${f(y)})"
                    }
                }
                first = false
            } while (hasNumber())
        }
        return out
    }

    /**
     * Formats a coordinate as a Kotlin float literal, rounded to three decimals.
     *
     * @param value Coordinate.
     * @return Literal such as `12f`, `0.5f` or `-7.25f`.
     */
    private fun f(value: Double): String {
        val text = String.format(Locale.ROOT, "%.3f", value).trimEnd('0').trimEnd('.')
        return (if (text == "-0") "0" else text) + "f"
    }

    /**
     * Renders the source file of one icon.
     *
     * @param row Mapping row of the icon.
     * @param paths Converted path blocks.
     * @return Full file text.
     */
    private fun renderIcon(row: MappingRow, paths: List<VectorPath>): String = buildString {
        val isMirrored = "mirror" in row.flags
        val isFilled = "filled" in row.flags
        val hasStroke = paths.any { it.isStroked }
        val cache = "cached${row.name}"

        appendLine("package dev.voir.areca.icons")
        appendLine()
        appendLine("import androidx.compose.ui.graphics.Color")
        appendLine("import androidx.compose.ui.graphics.SolidColor")
        if (hasStroke) {
            appendLine("import androidx.compose.ui.graphics.StrokeCap")
            appendLine("import androidx.compose.ui.graphics.StrokeJoin")
        }
        appendLine("import androidx.compose.ui.graphics.vector.ImageVector")
        appendLine("import androidx.compose.ui.graphics.vector.path")
        appendLine("import androidx.compose.ui.unit.dp")
        appendLine()
        appendLine("/**")
        val summary = describe(row.id)
        appendLine(" * ${if (isFilled) "Filled ${summary.replaceFirstChar { it.lowercase() }}" else summary}.")
        val notes = listOfNotNull(
            row.sameAs?.let { "Same drawing as `ArecaIcons.$it`; both names are kept." },
            "Mirrored horizontally in right-to-left layouts.".takeIf { isMirrored },
        )
        if (notes.isNotEmpty()) {
            appendLine(" *")
            notes.forEach { appendLine(" * $it") }
        }
        appendLine(" *")
        val variant = if (isFilled) ", filled" else ""
        appendLine(" * Converted from ${row.set.label} `${row.id}` (${row.set.licence})$variant.")
        appendLine(" */")
        appendLine("public val ArecaIcons.${row.name}: ImageVector")
        appendLine("    get() = $cache ?: ImageVector.Builder(")
        appendLine("        name = \"${row.name}\",")
        appendLine("        defaultWidth = 24.dp,")
        appendLine("        defaultHeight = 24.dp,")
        appendLine("        viewportWidth = 24f,")
        appendLine("        viewportHeight = 24f,")
        if (isMirrored) {
            appendLine("        autoMirror = true,")
        }
        appendLine("    ).apply {")
        paths.forEach { path ->
            val parameters = buildList {
                if (path.isFilled) add("fill = SolidColor(Color.Black)")
                if (path.isStroked) {
                    add("stroke = SolidColor(Color.Black)")
                    add("strokeLineWidth = 2f")
                    add("strokeLineCap = StrokeCap.Round")
                    add("strokeLineJoin = StrokeJoin.Round")
                }
            }
            if (parameters.size == 1) {
                appendLine("        path(${parameters.single()}) {")
            } else {
                appendLine("        path(")
                parameters.forEach { appendLine("            $it,") }
                appendLine("        ) {")
            }
            path.commands.forEach { appendLine("            $it") }
            appendLine("        }")
        }
        appendLine("    }.build().also { $cache = it }")
        appendLine()
        appendLine("/** `${row.name}` vector, built on first access and reused afterwards. */")
        appendLine("private var $cache: ImageVector? = null")
    }

    /**
     * Describes an upstream id in words.
     *
     * @param id Upstream icon id, such as `circle-arrow-down` or `brand-figma`.
     * @return Sentence-case noun phrase, such as "Circle arrow down icon" or "Figma logo".
     */
    private fun describe(id: String): String {
        val brand = id.removePrefix("brand-")
        val words = brand.replace('-', ' ')
        return (if (brand != id) "$words logo" else "$words icon").replaceFirstChar { it.uppercase() }
    }

    /**
     * Renders `NOTICE.md` from the upstream licence files.
     *
     * @param lucide Lucide package.
     * @param tabler Tabler Icons package.
     * @param rows Mapping rows, used to list the Feather-derived Lucide icons in use.
     * @return Full notice text.
     */
    private fun renderNotice(lucide: SourcePackage, tabler: SourcePackage, rows: List<MappingRow>): String {
        val lucideLicence = lucide.root.resolve("LICENSE").readText().trim()
        val tablerLicence = tabler.root.resolve("LICENSE").readText().trim()
        val iscText = lucideLicence.substringBefore("\n---").trim()
        val featherNames = Regex("derived from the Feather project:\\s*\\n\\s*\\n?([^\\n]+)").find(lucideLicence)
            ?.groupValues?.get(1)?.split(',')?.map { it.trim() }
            ?: throw GradleException(
                "The Lucide LICENSE no longer lists the Feather-derived icons; update the converter.",
            )
        val featherText = lucideLicence.substringAfter("The MIT License (MIT) (for the icons listed above)", "")
            .trim()
            .takeIf { it.isNotEmpty() }
            ?: throw GradleException("The Lucide LICENSE no longer carries the Feather MIT text; update the converter.")
        val featherBodies = featherNames.mapNotNull { name ->
            lucide.icons.resolve("$name.svg").takeIf { it.isFile }?.let(::svgBody)
        }.toSet()
        val featherIcons = rows.filter { it.set == IconSet.Lucide }
            .map { it.id }
            .distinct()
            .filter { svgBody(lucide.icons.resolve("$it.svg")) in featherBodies }
            .sorted()

        return buildString {
            appendLine("# Third-party notices")
            appendLine()
            appendLine("Generated by `./gradlew :standalone:icons:generateIcons` from the upstream licence files; edit")
            appendLine("`generate-icons.gradle.kts`, not this file.")
            appendLine()
            appendLine("Every icon in `dev.voir.areca:icons`, except the three sign-in marks listed under")
            appendLine("Trademarks, is converted from Lucide or Tabler Icons into an `ImageVector` builder. Its KDoc")
            appendLine("names the upstream icon and licence (\"Converted from Lucide `x` (ISC)\" or \"Converted from")
            appendLine("Tabler Icons `x` (MIT)\"). Those licences require the notices below in all copies, including")
            appendLine("the copies other Areca modules bundle. It ships in each such artifact under `META-INF/`.")
            appendLine()
            appendLine("## Lucide")
            appendLine()
            appendLine("Converted from `lucide-static` ${lucide.version} (https://lucide.dev).")
            appendLine()
            appendLine("```text")
            appendLine(iscText)
            appendLine("```")
            appendLine()
            appendLine("### Feather")
            appendLine()
            appendLine("These converted Lucide icons derive from the Feather project (https://feathericons.com):")
            appendLine(wrap(featherIcons.joinToString(", ") { "`$it`" } + "."))
            appendLine()
            appendLine("```text")
            appendLine("The MIT License (MIT)")
            appendLine()
            appendLine(featherText)
            appendLine("```")
            appendLine()
            appendLine("## Tabler Icons")
            appendLine()
            appendLine("Converted from `@tabler/icons` ${tabler.version}, outline style (https://tabler.io/icons).")
            appendLine()
            appendLine("```text")
            appendLine(tablerLicence)
            appendLine("```")
            appendLine()
            appendLine("## Trademarks")
            appendLine()
            appendLine("Brand icons (Tabler `brand-*` drawings: `BrandApple`, `BrandAndroid`, `Codepen`, `Figma`,")
            appendLine("`Framer`, `GoogleChrome` and `Youtube`) depict trademarks of their owners. The icon licences")
            appendLine("above cover the drawings, not the marks; their presence implies no endorsement.")
            appendLine()
            appendLine("`SocialApple`, `SocialFacebook` and `SocialGoogle` are the providers' own sign-in marks: Apple")
            appendLine("is a trademark of Apple Inc., Facebook of Meta Platforms, Inc., and Google of Google LLC. They")
            appendLine("are not covered by the icon licences above. They are included for sign-in buttons and must")
            appendLine("be used as each provider's sign-in branding guidelines require.")
        }
    }

    /**
     * Wraps prose at word boundaries.
     *
     * @param text Single-line text.
     * @param width Maximum line length.
     * @return The text broken into lines no longer than [width], unless a single word is longer.
     */
    private fun wrap(text: String, width: Int = 110): String = text.split(' ').fold(mutableListOf("")) { lines, word ->
        val last = lines.last()
        if (last.isEmpty()) {
            lines[lines.lastIndex] = word
        } else if (last.length + 1 + word.length <= width) {
            lines[lines.lastIndex] = "$last $word"
        } else {
            lines += word
        }
        lines
    }.joinToString("\n")

    /**
     * Normalises an SVG for comparison by dropping comments and class attributes.
     *
     * @param file SVG file.
     * @return The remaining markup.
     */
    private fun svgBody(file: File): String =
        file.readText().replace(Regex("<!--.*?-->|class=\"[^\"]*\"", RegexOption.DOT_MATCHES_ALL), "").trim()

    /** Versions the mapping and the notice are written against. */
    companion object {
        /** `lucide-static` version the mapping is reviewed against. */
        const val LUCIDE_VERSION: String = "1.55.0"

        /** `@tabler/icons` version the mapping is reviewed against. */
        const val TABLER_VERSION: String = "3.49.0"
    }
}

tasks.register<GenerateIcons>("generateIcons") {
    group = "build setup"
    description = "Regenerates the icon sources and NOTICE.md from Lucide and Tabler Icons (development only)."
    mapping.set(layout.projectDirectory.file("icons.tsv"))
    lucidePath.set(providers.gradleProperty("lucide"))
    tablerPath.set(providers.gradleProperty("tabler"))
    iconDirectory.set(layout.projectDirectory.dir("src/commonMain/kotlin/dev/voir/areca/icons"))
    noticeFile.set(layout.projectDirectory.file("NOTICE.md"))
}
