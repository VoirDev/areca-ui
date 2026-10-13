package dev.voir.areca.keyboard.key

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** The grid transforms that make a preset a starting point rather than something to fork. */
class KeyboardGridTest {
    @Test
    fun `key finds a key anywhere in the grid`() {
        val grid = grid()

        assertEquals("6", grid.key("6")?.id)
    }

    @Test
    fun `key returns null for an id the grid does not hold`() {
        assertNull(grid().key("absent"))
    }

    @Test
    fun `mapKeys rewrites every key in every row`() {
        val grid = grid()

        val disabled = grid.mapKeys { it.copy(enabled = false) }

        assertTrue(disabled.rows.flatMap { it.keys }.none { it.enabled })
    }

    @Test
    fun `mapKeys preserves the grid's shape`() {
        val grid = grid()

        val mapped = grid.mapKeys { it.copy(enabled = false) }

        assertEquals(grid.rows.map { row -> row.keys.map { it.id } }, mapped.rows.map { row -> row.keys.map { it.id } })
    }

    @Test
    fun `replaceKey rewrites only the matching key`() {
        val grid = grid()

        val replaced = grid.replaceKey("6") { it.copy(enabled = false) }

        assertFalse(replaced.key("6")!!.enabled)
        assertTrue(replaced.key("5")!!.enabled)
    }

    @Test
    fun `replaceKey leaves a grid without that key untouched`() {
        val grid = grid()

        assertEquals(grid, grid.replaceKey("absent") { it.copy(enabled = false) })
    }

    @Test
    fun `removeKey drops the key and lets its row reflow`() {
        val grid = grid()

        val removed = grid.removeKey("6")

        assertNull(removed.key("6"))
        assertEquals(listOf("4", "5"), removed.rows[1].keys.map { it.id })
    }

    @Test
    fun `removeKey drops a row it empties`() {
        val grid = KeyboardGrid(KeyRow(key("only")), KeyRow(key("kept")))

        val removed = grid.removeKey("only")

        assertEquals(listOf(listOf("kept")), removed.rows.map { row -> row.keys.map { it.id } })
    }

    @Test
    fun `replacing a key with a blank holds its slot open`() {
        val grid = grid()

        val blanked = grid.replaceKey("6") { Key.blank(it.id, it.weight) }

        assertEquals(3, blanked.rows[1].keys.size)
        assertEquals(KeyContent.Blank, blanked.key("6")?.content)
    }

    @Test
    fun `appendRow adds below the last row and prependRow above the first`() {
        val grid = grid()
        val extra = KeyRow(key("extra"))

        assertEquals("extra", grid.appendRow(extra).rows.last().keys.single().id)
        assertEquals("extra", grid.prependRow(extra).rows.first().keys.single().id)
    }

    @Test
    fun `rows are independent so a grid may mix row lengths`() {
        val grid = KeyboardGrid(
            KeyRow(key("a"), key("b"), key("c"), key("d"), key("e")),
            KeyRow(key("f"), key("g"), key("h"), key("i")),
        )

        assertEquals(listOf(5, 4), grid.rows.map { it.keys.size })
    }

    @Test
    fun `a blank key is not pressable`() {
        val blank = Key.blank("gap")

        assertFalse(blank.enabled)
        assertEquals(KeyContent.Blank, blank.content)
    }

    @Test
    fun `a key without a positive finite weight is rejected`() {
        listOf(0f, -1f, Float.NaN, Float.POSITIVE_INFINITY).forEach { weight ->
            assertFailsWith<IllegalArgumentException> { Key.text("1", weight = weight) {} }
        }
    }

    @Test
    fun `a grid holding two keys with one id is rejected`() {
        assertFailsWith<IllegalArgumentException> {
            KeyboardGrid(KeyRow(Key.blank("gap"), key("1")), KeyRow(Key.blank("gap")))
        }
    }

    @Test
    fun `a transform that repeats an id is rejected`() {
        assertFailsWith<IllegalArgumentException> { grid().replaceKey("6") { it.copy(id = "5") } }
    }

    @Test
    fun `inserted text must not be empty`() {
        assertFailsWith<IllegalArgumentException> { KeyAction.Insert("") }
    }

    @Test
    fun `text defaults its id to its label`() {
        assertEquals("7", Key.text("7") {}.id)
    }

    /**
     * A grid of three rows of three, for exercising transforms.
     *
     * @return A grid whose key ids are `"1"` through `"9"`.
     */
    private fun grid() = KeyboardGrid(
        KeyRow(key("1"), key("2"), key("3")),
        KeyRow(key("4"), key("5"), key("6")),
        KeyRow(key("7"), key("8"), key("9")),
    )

    /**
     * A label key that does nothing.
     *
     * @param id Identity and label of the key.
     * @return A key for use in a test grid.
     */
    private fun key(id: String) = Key.text(id) {}
}
