package dev.voir.areca.keyboard.key

import androidx.compose.runtime.Immutable

/**
 * An arithmetic operator a keyboard can emit.
 *
 * Named rather than carried as a character, because the character a keyboard *shows* is a
 * presentation choice — `×` on a key, `*` on a physical keyboard, `·` in some locales — and an
 * application should not have to recognise all of them to know what was pressed.
 */
public enum class KeyOperation {
    /** Addition. */
    Add,

    /** Subtraction. */
    Subtract,

    /** Multiplication. */
    Multiply,

    /** Division. */
    Divide,
}

/**
 * What pressing a key means, independently of what the key looked like.
 *
 * This is the whole reason [KeyboardGrids]' presets are reusable: the keyboard module knows how to
 * lay out a numeric pad but nothing about what the numbers are *for*, so a preset reports intent and
 * the application decides what it does to its own state. A currency field, a calculator, and a PIN
 * entry consume the same [Digit] differently.
 *
 * [Insert] carries text whose meaning is the text itself, and [Custom] is the open end: a key an
 * application added for something the module has no name for reports its own [Key.id] and is
 * handled alongside the rest.
 */
@Immutable
public sealed interface KeyAction {
    /**
     * A digit was pressed.
     *
     * @property value The digit, `'0'` through `'9'`.
     */
    public data class Digit(public val value: Char) : KeyAction

    /**
     * The decimal separator was pressed.
     *
     * Which character it showed is the keyboard's business; that a separator was asked for is the
     * application's.
     */
    public data object DecimalSeparator : KeyAction

    /**
     * An arithmetic operator was pressed.
     *
     * @property operation Which operator.
     */
    public data class Operation(public val operation: KeyOperation) : KeyAction

    /**
     * Text should be inserted as it is.
     *
     * For a key whose meaning *is* its text and needs no name of its own: a `00` or `000` key on an
     * amount pad, a currency symbol, the `*` and `#` of a phone keypad. An application inserts it
     * where it would insert a typed character.
     *
     * @property text What to insert. Not empty.
     * @throws IllegalArgumentException When [text] is empty.
     */
    public data class Insert(public val text: String) : KeyAction {
        init {
            require(text.isNotEmpty()) { "Inserted text must not be empty." }
        }
    }

    /** The last character should be removed. */
    public data object Backspace : KeyAction

    /** Everything entered so far should be discarded. */
    public data object Clear : KeyAction

    /** The expression entered so far should be evaluated, leaving the keyboard open. */
    public data object Evaluate : KeyAction

    /** Entry is finished. An application normally commits the value and closes the keyboard. */
    public data object Done : KeyAction

    /** The keyboard should close without committing anything. */
    public data object Hide : KeyAction

    /**
     * A key with no meaning the module names.
     *
     * @property id The pressed key's [Key.id].
     */
    public data class Custom(public val id: String) : KeyAction
}
