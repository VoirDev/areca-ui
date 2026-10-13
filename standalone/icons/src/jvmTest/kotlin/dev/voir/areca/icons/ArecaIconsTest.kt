package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.VectorGroup
import androidx.compose.ui.graphics.vector.VectorNode
import androidx.compose.ui.graphics.vector.VectorPath
import androidx.compose.ui.unit.dp
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

/** Builds every compiled icon and checks it against `icons.tsv`, the mapping it was generated from. */
class ArecaIconsTest {

    @Test
    fun `every mapped icon is compiled and every compiled icon is mapped`() {
        assertEquals(mapping.keys, icons.keys)
    }

    @Test
    fun `every icon is 24 dp on a 24-unit viewport and named after its property`() {
        icons.forEach { (name, icon) ->
            assertEquals(name, icon.name, "name of $name")
            assertEquals(24.dp, icon.defaultWidth, "width of $name")
            assertEquals(24.dp, icon.defaultHeight, "height of $name")
            assertEquals(24f, icon.viewportWidth, "viewport width of $name")
            assertEquals(24f, icon.viewportHeight, "viewport height of $name")
        }
    }

    @Test
    fun `exactly the icons flagged mirror flip in right-to-left layouts`() {
        icons.forEach { (name, icon) ->
            assertEquals("mirror" in mapping.getValue(name).flags, icon.autoMirror, "autoMirror of $name")
        }
    }

    @Test
    fun `converted icons draw in black with a round 2-unit stroke`() {
        icons.filterKeys { mapping.getValue(it).set != "manual" }.forEach { (name, icon) ->
            paths(icon.root).forEach { path ->
                assertTrue(path.fill == null || path.fill == SolidColor(Color.Black), "fill of $name")
                if (path.stroke != null) {
                    assertEquals(SolidColor(Color.Black), path.stroke, "stroke of $name")
                    assertEquals(2f, path.strokeLineWidth, "stroke width of $name")
                    assertEquals(StrokeCap.Round, path.strokeLineCap, "stroke cap of $name")
                    assertEquals(StrokeJoin.Round, path.strokeLineJoin, "stroke join of $name")
                }
            }
        }
    }

    @Test
    fun `repeated drawings are the aliases the mapping declares`() {
        icons.entries
            .groupBy({ (_, icon) -> paths(icon.root).map { Triple(it.pathData, it.fill, it.stroke) } }, { it.key })
            .values
            .filter { it.size > 1 }
            .forEach { names ->
                val primaries = names.map { mapping.getValue(it).sameAs ?: it }.toSet()
                assertEquals(1, primaries.size, "$names share a drawing but are not marked same-as one name")
            }
    }

    /**
     * One mapping row.
     *
     * @property set `lucide`, `tabler` or `manual`.
     * @property flags Flags such as `mirror`, `filled` and `same-as=<Name>`.
     */
    private class Row(val set: String, val flags: Set<String>) {
        /** Name whose drawing this icon repeats, or `null` when the drawing is its own. */
        val sameAs: String? = flags.firstOrNull { it.startsWith("same-as=") }?.removePrefix("same-as=")
    }

    private companion object {
        /** `icons.tsv` rows keyed by icon name; the build passes the file's path. */
        val mapping: Map<String, Row> = run {
            val path = System.getProperty("areca.icons.mapping") ?: fail("The build sets areca.icons.mapping.")
            File(path).readLines()
                .filter { it.isNotBlank() && !it.startsWith("#") }
                .associate { line ->
                    val columns = line.split('\t')
                    val flags = columns.getOrNull(3).orEmpty().split(',').filter { it.isNotEmpty() }.toSet()
                    columns[0] to Row(columns[1], flags)
                }
        }

        /** Every compiled icon, built through its generated getter and keyed by property name. */
        val icons: Map<String, ImageVector> = run {
            val classes = File(ArecaIcons::class.java.protectionDomain.codeSource.location.toURI())
                .resolve("dev/voir/areca/icons")
            val files = classes.listFiles() ?: fail("$classes is not a class directory.")
            files.filter { it.name.endsWith("Kt.class") }.associate { file ->
                val name = file.name.removeSuffix("Kt.class")
                val getter = Class.forName("dev.voir.areca.icons.${name}Kt")
                    .getMethod("get$name", ArecaIcons::class.java)
                name to getter.invoke(null, ArecaIcons) as ImageVector
            }
        }

        /**
         * Flattens a vector's node tree.
         *
         * @param node Group to walk.
         * @return Every path under [node], in drawing order.
         */
        fun paths(node: VectorNode): List<VectorPath> = when (node) {
            is VectorPath -> listOf(node)
            is VectorGroup -> node.flatMap(::paths)
        }
    }
}
