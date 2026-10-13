package dev.voir.areca.playground.keyboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.keyboard.KeyboardHost
import dev.voir.areca.keyboard.KeyboardScrim
import dev.voir.areca.keyboard.key.Key
import dev.voir.areca.keyboard.key.KeyAction
import dev.voir.areca.keyboard.key.KeyRole
import dev.voir.areca.keyboard.key.KeyStyle
import dev.voir.areca.keyboard.key.KeyOperation
import dev.voir.areca.keyboard.key.KeyRow
import dev.voir.areca.keyboard.key.KeyboardGrid
import dev.voir.areca.keyboard.key.KeyboardGrids
import dev.voir.areca.keyboard.key.KeyboardKeys
import dev.voir.areca.keyboard.key.mapKeys
import dev.voir.areca.keyboard.keyboardTarget
import dev.voir.areca.keyboard.layout.KeyboardLayout
import dev.voir.areca.keyboard.layout.KeyboardSize
import dev.voir.areca.keyboard.layout.KeyboardStyle
import dev.voir.areca.keyboard.rememberKeyboardController
import dev.voir.areca.keyboard.rememberKeyboardTarget
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.Anatomy
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

/** Which stock grid the playground is showing. */
private enum class GridPreset(val label: String) {
    Numeric("numeric"),
    Decimal("decimal"),
    Phone("phone"),
    Calculator("calculator"),
}

/** Height of the fake screen the host is shown inside. A host fills whatever it is given. */
private val ScreenHeight = 460.dp

/** Width of the fake screen, about that of a phone. */
private val ScreenWidth = 360.dp

/** Showcase for the keyboard module. */
@Composable
fun KeyboardShowcase() = ShowcaseScaffold(
    title = "Keyboard",
    summary = "A custom keyboard docked to the bottom of a screen: a host that coordinates focus " +
        "and scrolling, a styled surface with header and footer slots, and grids of weighted keys.",
    module = "keyboard",
) {
    Playground {
        val preset by enumControl(
            key = "grid",
            default = GridPreset.Calculator,
            options = GridPreset.entries.toList(),
            optionLabel = { it.label },
            description = "Stock layout from KeyboardGrids. Each returns a value you can transform.",
        )
        val compressContent by booleanControl(
            key = "compressContent",
            default = true,
            description = "Whether the screen shrinks by the keyboard's height, or the keyboard " +
                "is drawn over it.",
        )
        val showScrim by booleanControl(
            key = "scrim",
            default = false,
            description = "Tints the content and closes the keyboard when tapped. Off by " +
                "default — a keyboard is not modal.",
        )
        val showHeader by booleanControl(
            key = "header",
            default = true,
            description = "The slot above the keys. Normally the value being edited.",
        )
        val showFooter by booleanControl(
            key = "footer",
            default = false,
            description = "The slot below the keys. A toolbar, a hint, a suggestion strip.",
        )
        val flat by booleanControl(
            key = "flat surface",
            default = false,
            description = "KeyboardStyle.flat(): no shadow, square corners, a hairline on top.",
        )
        val keyHeight by numberControl(
            key = "keyHeight",
            default = 52f,
            range = 36f..72f,
            step = 4f,
            format = { "${it.toInt()}dp" },
            description = "Height of one key, and so of a row that does not set its own.",
        )

        preview {
            var entered by remember { mutableStateOf("0") }

            FakeScreen {
                val controller = rememberKeyboardController()
                val target = rememberKeyboardTarget()

                KeyboardHost(
                    controller = controller,
                    compressContent = compressContent,
                    scrim = if (showScrim) KeyboardScrim.Default else null,
                    keyboardContent = {
                        KeyboardLayout(
                            grid = grid(preset) { action ->
                                entered = entered.apply(action)
                                if (action is KeyAction.Done || action is KeyAction.Hide) {
                                    controller.close()
                                }
                            },
                            style = if (flat) KeyboardStyle.flat() else KeyboardStyle.Default,
                            size = KeyboardSize.default(keyHeight = keyHeight.dp),
                            // The fake screen is not the real one, so its safe area is not the
                            // keyboard's to clear.
                            windowInsets = WindowInsets(0, 0, 0, 0),
                            header = if (showHeader) {
                                {
                                    Text(
                                        text = entered,
                                        modifier = Modifier.fillMaxWidth(),
                                        variant = TextVariant.HeadingSmall,
                                        textAlign = TextAlign.End,
                                    )
                                }
                            } else {
                                null
                            },
                            footer = if (showFooter) {
                                {
                                    Text(
                                        text = "Press a key, or type on your keyboard",
                                        modifier = Modifier.fillMaxWidth(),
                                        variant = TextVariant.BodySmall,
                                        color = TextColor.Muted,
                                        textAlign = TextAlign.Center,
                                    )
                                }
                            } else {
                                null
                            },
                        )
                    },
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(ArecaTheme.spacing.xl),
                        verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
                    ) {
                        Text("Screen content", variant = TextVariant.BodySmall, color = TextColor.Muted)
                        Text(
                            text = entered,
                            modifier = Modifier
                                .fillMaxWidth()
                                .keyboardTarget(controller, target)
                                .padding(vertical = ArecaTheme.spacing.lg),
                            variant = TextVariant.HeadingLarge,
                            textAlign = TextAlign.Center,
                        )
                        Text(
                            text = "Tap the value to open the keyboard.",
                            variant = TextVariant.BodySmall,
                            color = TextColor.Muted,
                        )
                    }
                }
            }
        }
    }

    Anatomy {
        slot(
            name = "header",
            description = "Above the keys. The value being edited — a running total, a formula, " +
                "the digits so far.",
        ) {
            StaticKeyboard(header = { Text("1 234,56", variant = TextVariant.HeadingSmall) })
        }
        slot(
            name = "content",
            description = "The keys. A KeyboardGrid through the grid overload, or anything at all " +
                "through the slot one.",
        ) {
            StaticKeyboard()
        }
        slot(
            name = "footer",
            description = "Below the keys. A toolbar, a currency switch, a hint.",
        ) {
            StaticKeyboard(
                footer = {
                    Text("EUR", variant = TextVariant.BodySmall, color = TextColor.Muted)
                },
            )
        }
    }

    Variants(
        title = "Key styles",
        description = "What a key means, not how loud it is. A grid marks its numbers Digit, its " +
            "operators Action, and the key that concludes entry Confirm; KeyStyle turns each " +
            "role into a look.",
        layout = VariantLayout.Horizontal,
    ) {
        variant("Digit", detail = "numbers") { SingleKey("7", KeyRole.Digit) }
        variant("Action", detail = "operators") { SingleKey("+", KeyRole.Action) }
        variant("Confirm", detail = "the way out") { SingleKey("=", KeyRole.Confirm) }
        variant("Destructive", detail = "backspace") { SingleKey("⌫", KeyRole.Destructive) }
        variant("Caution", detail = "clear") { SingleKey("AC", KeyRole.Caution) }
        variant("Info", detail = "paste") { SingleKey("Paste", KeyRole.Info) }
        variant("Ghost", detail = "hide") { SingleKey("Hide", KeyRole.Ghost) }
    }

    Variants(
        title = "Grids",
        description = "Each preset is a value, not a component: transform it rather than fork it.",
        layout = VariantLayout.Vertical,
    ) {
        variant("numeric", detail = "digits and a backspace") {
            StaticKeys(GridPreset.Numeric)
        }
        variant("decimal", detail = "and a separator") {
            StaticKeys(GridPreset.Decimal)
        }
        variant("phone", detail = "with * and #") {
            StaticKeys(GridPreset.Phone)
        }
        variant("calculator", detail = "five operators over rows of four") {
            StaticKeys(GridPreset.Calculator)
        }
    }

    EdgeCases(
        title = "Edge cases",
        description = "What the grid does when a row is not four even keys.",
        layout = VariantLayout.Vertical,
    ) {
        case(
            name = "Mixed row lengths",
            expectation = "A row of five and a row of two each divide the full width on their " +
                "own. Nothing lines up between them, because nothing should.",
        ) {
            StaticGrid(
                KeyboardGrid(
                    KeyRow(key("1"), key("2"), key("3"), key("4"), key("5")),
                    KeyRow(key("6"), key("7")),
                ),
            )
        }
        case(
            name = "Weighted keys",
            expectation = "The 2f key is twice as wide as each 1f key beside it.",
        ) {
            StaticGrid(
                KeyboardGrid(
                    KeyRow(
                        key("1"),
                        Key.text("wide", weight = 2f) {},
                        key("3"),
                    ),
                ),
            )
        }
        case(
            name = "Blank key",
            expectation = "The gap holds its slot open, so the keys below stay aligned with the " +
                "keys above. Nothing is announced there.",
        ) {
            StaticGrid(
                KeyboardGrid(
                    KeyRow(key("1"), key("2"), key("3")),
                    KeyRow(Key.blank("gap"), key("0"), key("<")),
                ),
            )
        }
        case(
            name = "Disabled grid",
            expectation = "Every key greys out and stops responding, from one mapKeys call.",
        ) {
            StaticGrid(
                KeyboardGrid(
                    KeyRow(key("1"), key("2"), key("3")),
                ).mapKeys { it.copy(enabled = false) },
            )
        }
        case(
            name = "Label longer than its key",
            expectation = "The label stays on one line and ends in an ellipsis rather than " +
                "wrapping or widening the row past the screen.",
        ) {
            StaticGrid(
                KeyboardGrid(
                    KeyRow(key("1"), Key.text("a very long key label") {}, key("3")),
                ),
            )
        }
    }
}

/**
 * A phone-sized frame for a component that fills whatever it is given.
 *
 * @param content The host under inspection.
 */
@Composable
private fun FakeScreen(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .width(ScreenWidth)
            .height(ScreenHeight)
            .clip(ArecaTheme.shapes.lg)
            .background(ArecaTheme.colors.bgCanvas)
            .border(1.dp, ArecaTheme.colors.borderSubtle, ArecaTheme.shapes.lg),
        content = { content() },
    )
}

/**
 * The grid for a preset, wired to [onAction].
 *
 * @param preset Which stock layout to build.
 * @param onAction Called with what each key means.
 * @return The preset's grid.
 */
private fun grid(preset: GridPreset, onAction: (KeyAction) -> Unit): KeyboardGrid = when (preset) {
    GridPreset.Numeric -> KeyboardGrids.numeric(onAction)
    GridPreset.Decimal -> KeyboardGrids.decimal(onAction, decimalSeparator = ',')
    GridPreset.Phone -> KeyboardGrids.phone(onAction)
    GridPreset.Calculator -> KeyboardGrids.calculator(
        onAction = onAction,
        decimalSeparator = ',',
        // The module ships no clipboard, so the slot beside the 9 is the caller's to fill.
        auxiliaryKey = Key.text("Paste", id = "paste", role = KeyRole.Info) {
            onAction(KeyAction.Custom("paste"))
        },
    )
}

/**
 * A keyboard surface that does nothing, for showing a slot or a preset.
 *
 * @param preset Layout to draw.
 * @param header Content above the keys.
 * @param footer Content below the keys.
 */
@Composable
private fun StaticKeyboard(
    preset: GridPreset = GridPreset.Calculator,
    header: (@Composable ColumnScope.() -> Unit)? = null,
    footer: (@Composable ColumnScope.() -> Unit)? = null,
) {
    Box(Modifier.width(ScreenWidth)) {
        KeyboardLayout(
            grid = grid(preset) {},
            windowInsets = WindowInsets(0, 0, 0, 0),
            header = header,
            footer = footer,
        )
    }
}

/**
 * A preset's keys without the surface around them.
 *
 * @param preset Layout to draw.
 */
@Composable
private fun StaticKeys(preset: GridPreset) {
    Box(Modifier.width(ScreenWidth)) {
        KeyboardKeys(grid = grid(preset) {})
    }
}

/**
 * An arbitrary grid without the surface around it.
 *
 * @param grid Keys to draw.
 */
@Composable
private fun StaticGrid(grid: KeyboardGrid) {
    Box(Modifier.width(ScreenWidth)) {
        KeyboardKeys(grid = grid)
    }
}

/**
 * One key on its own, for showing a role's look.
 *
 * @param label Text on the key.
 * @param role Role whose look the key takes from [KeyStyle.Default].
 */
@Composable
private fun SingleKey(label: String, role: KeyRole) {
    Box(Modifier.width(96.dp)) {
        KeyboardKeys(
            grid = KeyboardGrid(KeyRow(Key.text(label, role = role) {})),
            style = KeyStyle.Default,
        )
    }
}

/**
 * A label key that does nothing.
 *
 * @param label Text and identity of the key.
 * @return A key for use in a demonstration grid.
 */
private fun key(label: String) = Key.text(label) {}

/**
 * Applies a key press to the displayed value.
 *
 * Deliberately naive: the playground shows what the keyboard *reports*, and leaves what that means
 * to an application. A real calculator would parse and evaluate here.
 *
 * @param action What the pressed key meant.
 * @return The value after the press.
 */
private fun String.apply(action: KeyAction): String = when (action) {
    is KeyAction.Digit -> if (this == "0") action.value.toString() else this + action.value
    KeyAction.DecimalSeparator -> if (contains(',')) this else "$this,"
    is KeyAction.Operation -> this + action.operation.symbol
    is KeyAction.Insert -> if (this == "0") action.text else this + action.text
    KeyAction.Backspace -> dropLast(1).ifEmpty { "0" }
    KeyAction.Clear -> "0"
    KeyAction.Evaluate, KeyAction.Done, KeyAction.Hide -> this
    is KeyAction.Custom -> this
}

/** The character this operator is written with. */
private val KeyOperation.symbol: Char
    get() = when (this) {
        KeyOperation.Add -> '+'
        KeyOperation.Subtract -> '−'
        KeyOperation.Multiply -> '×'
        KeyOperation.Divide -> '÷'
    }
