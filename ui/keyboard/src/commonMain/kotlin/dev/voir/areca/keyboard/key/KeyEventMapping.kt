package dev.voir.areca.keyboard.key

import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.key.utf16CodePoint
import androidx.compose.ui.input.key.Key as PhysicalKey

/**
 * Maps a physical key press onto the same [KeyAction] the on-screen keyboard would report.
 *
 * A custom keyboard replaces the platform's, which on a desktop — or a tablet with a keyboard
 * attached — would otherwise mean the hardware keys do nothing. Feed presses through this and one
 * handler serves both:
 *
 * ```kotlin
 * Modifier
 *     .keyboardTarget(keyboard, target)
 *     .onPreviewKeyEvent { event ->
 *         val action = event.toKeyAction(decimalSeparator = ',') ?: return@onPreviewKeyEvent false
 *         onKeyAction(action)
 *         true
 *     }
 * ```
 *
 * Returning `null` for an unrecognised press is what lets the caller leave it to whatever is behind:
 * Tab still moves focus, and a shortcut still reaches the window.
 *
 * @param decimalSeparator Separator this keyboard uses. Both `.` and `,` are accepted whatever it
 *   is, since a physical keyboard's layout is not the application's to choose.
 * @return The action the press means, or `null` when it means nothing to a keyboard.
 */
public fun KeyEvent.toKeyAction(decimalSeparator: Char = '.'): KeyAction? {
    if (type != KeyEventType.KeyDown) return null
    return keyActionFor(
        key = key,
        character = utf16CodePoint.toChar(),
        decimalSeparator = decimalSeparator,
    )
}

/**
 * The mapping itself, without a [KeyEvent] around it.
 *
 * Split out because a `KeyEvent` wraps a platform event and cannot be built in a common test, while
 * the decision it drives is pure and worth testing.
 *
 * Named keys are matched first, then everything else by the character produced — so a numeric
 * keypad, a shifted `+`, and a layout that puts `*` somewhere unusual all behave the same.
 *
 * @param key Which key was pressed.
 * @param character Character the press produced.
 * @param decimalSeparator Separator the keyboard uses.
 * @return The action the press means, or `null` when it means nothing to a keyboard.
 */
internal fun keyActionFor(
    key: PhysicalKey,
    character: Char,
    decimalSeparator: Char,
): KeyAction? {
    when (key) {
        PhysicalKey.Backspace, PhysicalKey.Delete -> return KeyAction.Backspace
        PhysicalKey.Enter, PhysicalKey.NumPadEnter -> return KeyAction.Done
        PhysicalKey.Escape -> return KeyAction.Hide
        PhysicalKey.Equals, PhysicalKey.NumPadEquals -> return KeyAction.Evaluate
    }

    return when (character) {
        in '0'..'9' -> KeyAction.Digit(character)
        '.', ',', decimalSeparator -> KeyAction.DecimalSeparator
        '+' -> KeyAction.Operation(KeyOperation.Add)
        '-', '−' -> KeyAction.Operation(KeyOperation.Subtract)
        '*', '×' -> KeyAction.Operation(KeyOperation.Multiply)
        '/', '÷' -> KeyAction.Operation(KeyOperation.Divide)
        '=' -> KeyAction.Evaluate
        else -> null
    }
}
