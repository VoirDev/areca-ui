package dev.voir.areca.keyboard.key

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp

/**
 * One row of keys.
 *
 * Keys share the row's width by [Key.weight], so rows are independent of each other: an operator row
 * of five and a digit row of four sit in the same grid without either knowing about the other. There
 * is no column count.
 *
 * @property keys Keys in the row, left to right in every layout direction.
 * @property height Height of the row. `null` takes the keyboard's default key height, which is what
 *   all but a deliberately taller action row wants.
 */
@Immutable
public data class KeyRow(
    public val keys: List<Key>,
    public val height: Dp? = null,
) {
    /**
     * Builds a row from keys given inline.
     *
     * @param keys Keys in the row, left to right in every layout direction.
     */
    public constructor(vararg keys: Key) : this(keys.toList())
}

/**
 * A keyboard's keys, as rows.
 *
 * This is a value, not a component: the presets in [KeyboardGrids] return one, and the transforms
 * below rewrite one. That is what lets a preset be a starting point rather than something to fork —
 * take the stock calculator grid, restyle one key, drop another, and render the result.
 *
 * ```kotlin
 * KeyboardGrids.calculator(onAction = ::onKeyAction)
 *     .replaceKey("clear") { it.copy(role = KeyRole.Destructive) }
 *     .removeKey("paste")
 * ```
 *
 * @property rows Rows of keys, top to bottom. Every key's [Key.id] is unique across them.
 * @throws IllegalArgumentException When two keys share an id. The transforms address keys by id,
 *   and the keyboard keys its composition by it, so a repeated id would make both ambiguous.
 */
@Immutable
public data class KeyboardGrid(
    public val rows: List<KeyRow>,
) {
    init {
        val duplicates = duplicateKeyIds(rows)
        require(duplicates.isEmpty()) {
            "Key ids must be unique within a grid; repeated: ${duplicates.joinToString()}."
        }
    }

    /**
     * Builds a grid from rows given inline.
     *
     * @param rows Rows of keys, top to bottom.
     */
    public constructor(vararg rows: KeyRow) : this(rows.toList())
}

/**
 * Ids held by more than one key.
 *
 * @param rows Rows to inspect.
 * @return Each repeated id once, in the order it first repeats. Empty when every id is unique.
 */
private fun duplicateKeyIds(rows: List<KeyRow>): List<String> {
    val seen = HashSet<String>()
    val repeated = LinkedHashSet<String>()
    rows.forEach { row -> row.keys.forEach { key -> if (!seen.add(key.id)) repeated += key.id } }
    return repeated.toList()
}

/**
 * Finds a key by its [Key.id].
 *
 * @param id Identity to look for.
 * @return The key, or `null` when the grid holds none with that id.
 */
public fun KeyboardGrid.key(id: String): Key? = rows.firstNotNullOfOrNull { row ->
    row.keys.firstOrNull { it.id == id }
}

/**
 * Rewrites every key in the grid.
 *
 * For a change that applies across the board — disabling the whole keyboard, restyling every key for
 * a dark surface.
 *
 * @param transform Called for each key; returns what replaces it.
 * @return A grid with every key transformed.
 */
public fun KeyboardGrid.mapKeys(transform: (Key) -> Key): KeyboardGrid = KeyboardGrid(
    rows = rows.map { row -> row.copy(keys = row.keys.map(transform)) },
)

/**
 * Rewrites the key with the given [id], leaving the rest alone.
 *
 * Does nothing when no key has that id, so a transform against a preset that changed shape fails
 * quietly rather than crashing. Check with [key] when the key's presence actually matters.
 *
 * @param id Identity of the key to rewrite.
 * @param transform Called with the matching key; returns what replaces it.
 * @return A grid with that key transformed.
 */
public fun KeyboardGrid.replaceKey(id: String, transform: (Key) -> Key): KeyboardGrid =
    mapKeys { key -> if (key.id == id) transform(key) else key }

/**
 * Drops the key with the given [id].
 *
 * The rest of its row spreads out to fill the gap, because keys are laid out by weight. To take a
 * key away *without* reflowing the row, replace it with a blank instead:
 *
 * ```kotlin
 * grid.replaceKey("paste") { Key.blank(it.id, it.weight) }
 * ```
 *
 * @param id Identity of the key to drop.
 * @return A grid without that key. A row emptied by the drop is removed with it.
 */
public fun KeyboardGrid.removeKey(id: String): KeyboardGrid = KeyboardGrid(
    rows = rows
        .map { row -> row.copy(keys = row.keys.filterNot { it.id == id }) }
        .filter { it.keys.isNotEmpty() },
)

/**
 * Adds a row below the last one.
 *
 * @param row Row to add.
 * @return A grid with the row appended.
 */
public fun KeyboardGrid.appendRow(row: KeyRow): KeyboardGrid = KeyboardGrid(rows = rows + row)

/**
 * Adds a row above the first one.
 *
 * @param row Row to add.
 * @return A grid with the row prepended.
 */
public fun KeyboardGrid.prependRow(row: KeyRow): KeyboardGrid =
    KeyboardGrid(rows = listOf(row) + rows)
