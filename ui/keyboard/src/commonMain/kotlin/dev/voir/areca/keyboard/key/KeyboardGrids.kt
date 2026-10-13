package dev.voir.areca.keyboard.key

import dev.voir.areca.keyboard.icon.BundledIcons
import dev.voir.areca.keyboard.icon.Check
import dev.voir.areca.keyboard.icon.ChevronDown
import dev.voir.areca.keyboard.icon.Delete

/**
 * Stock [KeyboardGrid] layouts.
 *
 * Each returns a value, not a component, and none needs a composition: keys carry a [KeyRole], and
 * the keyboard's [KeyStyle] turns roles into looks only when the grid is drawn. So a preset is where
 * a keyboard *starts* rather than something to copy and diverge from — build it once, restyle a key,
 * drop one, add a row; see the transforms in [KeyboardGrid]:
 *
 * ```kotlin
 * KeyboardGrids.decimal(onAction = ::onKeyAction)
 *     .replaceKey("backspace") { it.copy(role = KeyRole.Ghost) }
 *     .appendRow(KeyRow(Key.text("Done", role = KeyRole.Confirm) { commit() }))
 * ```
 *
 * Every preset reports intent through [KeyAction] and holds no opinion about what that intent does.
 * None of them knows what a calculator is.
 *
 * **Keys show glyphs, and speak words.** The utility keys show symbols that need no translation — a
 * backspace arrow, a chevron, a tick — and `"AC"`, which is what a calculator's clear key reads as
 * in most locales. What assistive technology reads for them is a word, taken from the `labels`
 * every preset accepts; its defaults are English.
 */
public object KeyboardGrids {
    /**
     * A plain number pad: digits and a backspace, with the bottom-left slot held open.
     *
     * For entering a whole number — a quantity, a code, a PIN.
     *
     * @param onAction Called with what each key means.
     * @param leadingKey The bottom-left slot, beside the zero. Blank by default; a good place for a
     *   key that closes the keyboard, or one that switches to letters.
     * @param labels What assistive technology reads for the utility keys.
     * @return A four-row, three-column grid: `1 2 3`, `4 5 6`, `7 8 9`, then [leadingKey], `0`,
     *   backspace.
     */
    public fun numeric(
        onAction: (KeyAction) -> Unit,
        leadingKey: Key = Key.blank("leading"),
        labels: KeyboardGridLabels = KeyboardGridLabels(),
    ): KeyboardGrid = KeyboardGrid(
        KeyRow(digit('1', onAction), digit('2', onAction), digit('3', onAction)),
        KeyRow(digit('4', onAction), digit('5', onAction), digit('6', onAction)),
        KeyRow(digit('7', onAction), digit('8', onAction), digit('9', onAction)),
        KeyRow(leadingKey, digit('0', onAction), backspace(onAction, labels)),
    )

    /**
     * A number pad with a decimal separator.
     *
     * For entering a measurement or an amount that is not being calculated.
     *
     * @param onAction Called with what each key means.
     * @param decimalSeparator Character the separator key shows. The action it reports is
     *   [KeyAction.DecimalSeparator] regardless, so a locale change here needs no change in the
     *   application.
     * @param labels What assistive technology reads for the utility keys.
     * @return A four-row, three-column grid: [numeric] with the separator in the bottom-left slot.
     */
    public fun decimal(
        onAction: (KeyAction) -> Unit,
        decimalSeparator: Char = '.',
        labels: KeyboardGridLabels = KeyboardGridLabels(),
    ): KeyboardGrid = numeric(
        onAction = onAction,
        leadingKey = separator(decimalSeparator, onAction),
        labels = labels,
    )

    /**
     * A telephone keypad: digits, `*`, and `#` in the standard four-by-three arrangement, and a
     * backspace.
     *
     * @param onAction Called with what each key means. `*` and `#` report [KeyAction.Insert] with
     *   their own character, from keys with the ids `"asterisk"` and `"hash"`.
     * @param labels What assistive technology reads for the utility keys.
     * @return A five-row, three-column grid: `1 2 3`, `4 5 6`, `7 8 9`, `* 0 #`, then a row holding
     *   only backspace, beneath `#`.
     */
    public fun phone(
        onAction: (KeyAction) -> Unit,
        labels: KeyboardGridLabels = KeyboardGridLabels(),
    ): KeyboardGrid = KeyboardGrid(
        KeyRow(digit('1', onAction), digit('2', onAction), digit('3', onAction)),
        KeyRow(digit('4', onAction), digit('5', onAction), digit('6', onAction)),
        KeyRow(digit('7', onAction), digit('8', onAction), digit('9', onAction)),
        KeyRow(
            Key.text("*", id = "asterisk") { onAction(KeyAction.Insert("*")) },
            digit('0', onAction),
            Key.text("#", id = "hash") { onAction(KeyAction.Insert("#")) },
        ),
        KeyRow(
            Key.blank("phoneLeading"),
            Key.blank("phoneMiddle"),
            backspace(onAction, labels),
        ),
    )

    /**
     * An arithmetic pad: operators across the top, digits below, utilities down the right edge.
     *
     * The operator row holds five keys over rows of four. Nothing reconciles them — keys divide
     * their own row by weight — which is what lets the row carry an extra key without the digits
     * shifting.
     *
     * @param onAction Called with what each key means.
     * @param decimalSeparator Character the separator key shows. See [decimal].
     * @param auxiliaryKey The slot beside the `9`, for something the keyboard pulls in from outside:
     *   a paste key, a scan key, a recent-values key. Blank by default, since the module has no
     *   clipboard of its own.
     * @param labels What assistive technology reads for the utility keys.
     * @return A five-row grid: five operators over four rows of four.
     */
    public fun calculator(
        onAction: (KeyAction) -> Unit,
        decimalSeparator: Char = '.',
        auxiliaryKey: Key = Key.blank("auxiliary"),
        labels: KeyboardGridLabels = KeyboardGridLabels(),
    ): KeyboardGrid = KeyboardGrid(
        KeyRow(
            operation("÷", "divide", KeyOperation.Divide, onAction),
            operation("×", "multiply", KeyOperation.Multiply, onAction),
            operation("+", "add", KeyOperation.Add, onAction),
            operation("−", "subtract", KeyOperation.Subtract, onAction),
            Key.text("=", id = "evaluate", role = KeyRole.Confirm) { onAction(KeyAction.Evaluate) },
        ),
        KeyRow(digit('7', onAction), digit('8', onAction), digit('9', onAction), auxiliaryKey),
        KeyRow(
            digit('4', onAction),
            digit('5', onAction),
            digit('6', onAction),
            Key.text(
                label = "AC",
                id = "clear",
                role = KeyRole.Caution,
                contentDescription = labels.clear,
            ) {
                onAction(KeyAction.Clear)
            },
        ),
        KeyRow(
            digit('1', onAction),
            digit('2', onAction),
            digit('3', onAction),
            backspace(onAction, labels),
        ),
        KeyRow(
            Key.icon(
                image = BundledIcons.ChevronDown,
                id = "hide",
                contentDescription = labels.hide,
                role = KeyRole.Ghost,
            ) {
                onAction(KeyAction.Hide)
            },
            digit('0', onAction),
            separator(decimalSeparator, onAction),
            Key.icon(
                image = BundledIcons.Check,
                id = "done",
                contentDescription = labels.done,
                role = KeyRole.Confirm,
            ) {
                onAction(KeyAction.Done)
            },
        ),
    )

    /**
     * A digit key.
     *
     * @param value The digit, which is also the key's label and id.
     * @param onAction Called with [KeyAction.Digit].
     * @return A [KeyRole.Digit] key.
     */
    private fun digit(value: Char, onAction: (KeyAction) -> Unit): Key =
        Key.text(value.toString()) { onAction(KeyAction.Digit(value)) }

    /**
     * The decimal separator key.
     *
     * @param separator Character to show.
     * @param onAction Called with [KeyAction.DecimalSeparator].
     * @return A [KeyRole.Digit] key with the id `"separator"`.
     */
    private fun separator(separator: Char, onAction: (KeyAction) -> Unit): Key =
        Key.text(separator.toString(), id = "separator") {
            onAction(KeyAction.DecimalSeparator)
        }

    /**
     * An arithmetic operator key.
     *
     * @param label Symbol to show.
     * @param id Identity within the grid.
     * @param operation Operator the key reports.
     * @param onAction Called with [KeyAction.Operation].
     * @return A [KeyRole.Action] key.
     */
    private fun operation(
        label: String,
        id: String,
        operation: KeyOperation,
        onAction: (KeyAction) -> Unit,
    ): Key = Key.text(label, id = id, role = KeyRole.Action) {
        onAction(KeyAction.Operation(operation))
    }

    /**
     * The backspace key, shown as an arrow rather than a word.
     *
     * @param onAction Called with [KeyAction.Backspace].
     * @param labels Source of the key's spoken description.
     * @return A [KeyRole.Destructive] icon key with the id `"backspace"`.
     */
    private fun backspace(onAction: (KeyAction) -> Unit, labels: KeyboardGridLabels): Key = Key.icon(
        image = BundledIcons.Delete,
        id = "backspace",
        contentDescription = labels.backspace,
        role = KeyRole.Destructive,
    ) {
        onAction(KeyAction.Backspace)
    }
}
