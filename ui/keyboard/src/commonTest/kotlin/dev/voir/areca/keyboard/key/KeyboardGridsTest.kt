package dev.voir.areca.keyboard.key

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Shapes, ids, roles, and actions of the stock presets in [KeyboardGrids], built without a
 * composition.
 */
class KeyboardGridsTest {
    @Test
    fun `numeric lays out three digits a row above leading zero backspace`() {
        val grid = KeyboardGrids.numeric({}, Key.blank("leading"), KeyboardGridLabels())

        assertEquals(
            listOf(
                listOf("1", "2", "3"),
                listOf("4", "5", "6"),
                listOf("7", "8", "9"),
                listOf("leading", "0", "backspace"),
            ),
            grid.ids(),
        )
    }

    @Test
    fun `decimal puts the separator in the leading slot showing the given character`() {
        val grid = KeyboardGrids.decimal({}, decimalSeparator = ',', KeyboardGridLabels())

        assertEquals(listOf("separator", "0", "backspace"), grid.ids().last())
        assertEquals(KeyContent.Label(","), grid.requireKey("separator").content)
    }

    @Test
    fun `decimal separator reports the separator action whatever character it shows`() {
        val actions = mutableListOf<KeyAction>()
        val grid = KeyboardGrids.decimal(actions::add, decimalSeparator = ',', KeyboardGridLabels())

        grid.requireKey("separator").onClick()

        assertEquals(listOf<KeyAction>(KeyAction.DecimalSeparator), actions)
    }

    @Test
    fun `phone is the four-by-three keypad with backspace beneath the hash`() {
        val grid = KeyboardGrids.phone({}, KeyboardGridLabels())

        assertEquals(
            listOf(
                listOf("1", "2", "3"),
                listOf("4", "5", "6"),
                listOf("7", "8", "9"),
                listOf("asterisk", "0", "hash"),
                listOf("phoneLeading", "phoneMiddle", "backspace"),
            ),
            grid.ids(),
        )
    }

    @Test
    fun `phone star and hash insert their own characters`() {
        val actions = mutableListOf<KeyAction>()
        val grid = KeyboardGrids.phone(actions::add, KeyboardGridLabels())

        grid.requireKey("asterisk").onClick()
        grid.requireKey("hash").onClick()

        assertEquals(listOf<KeyAction>(KeyAction.Insert("*"), KeyAction.Insert("#")), actions)
    }

    @Test
    fun `calculator puts five operators over four rows of four`() {
        val grid = KeyboardGrids.calculator({}, '.', Key.blank("auxiliary"), KeyboardGridLabels())

        assertEquals(
            listOf(
                listOf("divide", "multiply", "add", "subtract", "evaluate"),
                listOf("7", "8", "9", "auxiliary"),
                listOf("4", "5", "6", "clear"),
                listOf("1", "2", "3", "backspace"),
                listOf("hide", "0", "separator", "done"),
            ),
            grid.ids(),
        )
    }

    @Test
    fun `calculator keys report what they mean`() {
        val actions = mutableListOf<KeyAction>()
        val grid = KeyboardGrids.calculator(actions::add, '.', Key.blank("auxiliary"), KeyboardGridLabels())
        val expected = mapOf(
            "divide" to KeyAction.Operation(KeyOperation.Divide),
            "multiply" to KeyAction.Operation(KeyOperation.Multiply),
            "add" to KeyAction.Operation(KeyOperation.Add),
            "subtract" to KeyAction.Operation(KeyOperation.Subtract),
            "evaluate" to KeyAction.Evaluate,
            "clear" to KeyAction.Clear,
            "backspace" to KeyAction.Backspace,
            "hide" to KeyAction.Hide,
            "separator" to KeyAction.DecimalSeparator,
            "done" to KeyAction.Done,
            "7" to KeyAction.Digit('7'),
            "0" to KeyAction.Digit('0'),
        )

        expected.keys.forEach { id -> grid.requireKey(id).onClick() }

        assertEquals(expected.values.toList(), actions)
    }

    @Test
    fun `every digit key reports its own digit`() {
        val actions = mutableListOf<KeyAction>()
        val grid = KeyboardGrids.numeric(actions::add, Key.blank("leading"), KeyboardGridLabels())
        val digits = "0123456789".toList()

        digits.forEach { digit -> grid.requireKey(digit.toString()).onClick() }

        assertEquals<List<KeyAction>>(digits.map(KeyAction::Digit), actions)
    }

    @Test
    fun `every preset holds unique ids`() {
        val presets = listOf(
            KeyboardGrids.numeric({}, Key.blank("leading"), KeyboardGridLabels()),
            KeyboardGrids.decimal({}, '.', KeyboardGridLabels()),
            KeyboardGrids.phone({}, KeyboardGridLabels()),
            KeyboardGrids.calculator({}, '.', Key.blank("auxiliary"), KeyboardGridLabels()),
        )

        presets.forEach { grid ->
            val ids = grid.ids().flatten()
            assertEquals(ids.distinct(), ids)
        }
    }

    @Test
    fun `utility keys are described with the given labels`() {
        val labels = KeyboardGridLabels(backspace = "Löschen", clear = "Leeren", hide = "Ausblenden", done = "Fertig")
        val grid = KeyboardGrids.calculator({}, '.', Key.blank("auxiliary"), labels)

        assertEquals(
            listOf("Löschen", "Leeren", "Ausblenden", "Fertig"),
            listOf("backspace", "clear", "hide", "done").map { grid.requireKey(it).contentDescription },
        )
    }

    @Test
    fun `calculator utility keys carry the roles that pick their looks`() {
        val grid = KeyboardGrids.calculator({})
        val expected = mapOf(
            "7" to KeyRole.Digit,
            "separator" to KeyRole.Digit,
            "add" to KeyRole.Action,
            "evaluate" to KeyRole.Confirm,
            "done" to KeyRole.Confirm,
            "backspace" to KeyRole.Destructive,
            "clear" to KeyRole.Caution,
            "hide" to KeyRole.Ghost,
        )

        assertEquals(expected, expected.keys.associateWith { grid.requireKey(it).role })
    }

    /**
     * Ids of every key, row by row.
     *
     * @return One list of ids per row, top to bottom.
     */
    private fun KeyboardGrid.ids(): List<List<String>> = rows.map { row -> row.keys.map(Key::id) }

    /**
     * The key with [id], failing the test when the grid holds none.
     *
     * @param id Identity to look for.
     * @return The key.
     */
    private fun KeyboardGrid.requireKey(id: String): Key = assertNotNull(key(id), "No key \"$id\"")
}
