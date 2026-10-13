package dev.voir.areca.playground.foundation.wheel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.foundation.wheel.WheelPicker
import dev.voir.areca.foundation.wheel.WheelPickerColumn
import dev.voir.areca.foundation.wheel.WheelPickerFalloff
import dev.voir.areca.foundation.wheel.WheelPickerAcceleration
import dev.voir.areca.foundation.wheel.WheelPickerItem
import dev.voir.areca.foundation.wheel.WheelPickerScroll
import dev.voir.areca.foundation.wheel.WheelPickerScrollPropagation
import dev.voir.areca.foundation.wheel.WheelPickerSize
import dev.voir.areca.foundation.wheel.WheelPickerStyle
import dev.voir.areca.foundation.wheel.rememberWheelPickerColumnState
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.Anatomy
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme
import kotlin.time.Duration.Companion.milliseconds

/** Values used by every wheel on this page that has nothing more interesting to show. */
private val Numbers: List<Int> = (0..59).toList()

/** Short list, for showing what a wheel does when it runs out of rows. */
private val Sizes: List<String> = listOf("XS", "S", "M", "L", "XL")

/** A wheel long enough to show how a gesture is paced. A short one runs out of rows and hides it. */
private val Years: List<Int> = (1900..2100).toList()

/** Showcase for [WheelPicker]. */
@Composable
fun WheelPickerShowcase() = ShowcaseScaffold(
    title = "Wheel picker",
    summary = "Columns of snapping wheels, driven by drag, throw, scroll wheel, keyboard or a tap " +
        "on the row you want.",
    module = "foundation",
) {
    Playground {
        val visibleRows by numberControl(
            key = "visibleItemsCount",
            default = 5f,
            range = 3f..9f,
            step = 2f,
            format = { "${it.toInt()}" },
            description = "Rows on screen. Always odd, so one sits in the centre.",
        )
        val itemHeight by numberControl(
            key = "itemHeight",
            default = 36f,
            range = 24f..56f,
            step = 4f,
            format = { "${it.toInt()}dp" },
            description = "Height of one row.",
        )
        val infinite by booleanControl(
            key = "infinite",
            default = true,
            description = "Whether the wheel runs past its last value back to its first.",
        )
        val curved by booleanControl(
            key = "curved falloff",
            default = false,
            description = "WheelPickerFalloff.Curved tips the rows away, as a physical drum does.",
        )
        val maxRowsAhead by numberControl(
            key = "maxRowsAhead",
            default = 2f,
            range = 0.5f..8f,
            step = 0.5f,
            description = "How far a gesture may queue travel ahead of the wheel. The setting " +
                "that decides how far one flick carries.",
        )
        val maxRowsPerSecond by numberControl(
            key = "maxRowsPerSecond",
            default = 12f,
            range = 3f..60f,
            step = 3f,
            format = { "${it.toInt()}/s" },
            description = "Top speed while scrolling. Lower it until the rows stay readable.",
        )
        val maxGain by numberControl(
            key = "acceleration.maxGain",
            default = 3f,
            range = 1f..8f,
            step = 0.5f,
            format = { "${it}x" },
            description = "How much faster a sustained scroll turns the wheel than a short one. " +
                "1x turns acceleration off.",
        )
        val windUpMillis by numberControl(
            key = "acceleration.windUp",
            default = 600f,
            range = 200f..1500f,
            step = 100f,
            format = { "${it.toInt()}ms" },
            description = "How long a gesture must run to reach the full gain.",
        )
        val columnCount by numberControl(
            key = "columns",
            default = 2f,
            range = 1f..3f,
            step = 1f,
            format = { "${it.toInt()}" },
            description = "Scroll one of them and check that the others stay put.",
        )

        preview {
            val size = WheelPickerSize(
                itemHeight = itemHeight.dp,
                visibleItemsCount = visibleRows.toInt(),
            )
            val scroll = WheelPickerScroll(
                maxRowsAhead = maxRowsAhead,
                maxRowsPerSecond = maxRowsPerSecond,
                acceleration = WheelPickerAcceleration(
                    maxGain = maxGain,
                    windUp = windUpMillis.toInt().milliseconds,
                ),
            )
            val style = WheelPickerStyle.default(
                falloff = if (curved) WheelPickerFalloff.Curved else WheelPickerFalloff.Default,
            )
            // Three states are always created and the first `columnCount` of them used: a
            // composable cannot be called a variable number of times without changing the shape of
            // the composition on every step of the control.
            val allStates = listOf(
                rememberWheelPickerColumnState(initialIndex = 0, infinite = infinite),
                rememberWheelPickerColumnState(initialIndex = 5, infinite = infinite),
                rememberWheelPickerColumnState(initialIndex = 10, infinite = infinite),
            )
            val states = allStates.take(columnCount.toInt())

            Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
                WheelPicker(
                    modifier = Modifier.width(220.dp),
                    size = size,
                    style = style,
                    scroll = scroll,
                    horizontalArrangement = Arrangement.spacedBy(
                        space = ArecaTheme.spacing.sm,
                        alignment = Alignment.CenterHorizontally,
                    ),
                ) {
                    states.forEachIndexed { column, state ->
                        WheelPickerColumn(
                            items = Numbers,
                            state = state,
                            modifier = Modifier.weight(1f),
                            contentDescription = "Wheel ${column + 1}",
                        ) { value ->
                            WheelPickerItem(value.toString().padStart(2, '0'))
                        }
                    }
                }
                Text(
                    text = states.joinToString(" · ") { "${it.settledIndex}" },
                    variant = TextVariant.BodySmall,
                    color = TextColor.Muted,
                )
            }
        }
    }

    Anatomy {
        slot(
            name = "backgroundDecoration",
            description = "Drawn behind the wheels. Defaults to the highlight marking the centre " +
                "slot — behind, because an opaque fill over the wheels hides the row it marks.",
        ) {
            SingleWheel(style = WheelPickerStyle.default(surface = ArecaTheme.colors.bgSurface))
        }
        slot(
            name = "foregroundDecoration",
            description = "Drawn over the wheels. Defaults to the fade that turns the top and " +
                "bottom rows into the far side of a drum.",
        ) {
            SingleWheel(style = WheelPickerStyle.default(highlight = Color.Unspecified))
        }
        slot(
            name = "itemContent",
            description = "One row, handed where it sits on the wheel. Read distanceFromCenter " +
                "inside a graphics layer and the row re-draws as the wheel turns without " +
                "recomposing.",
        ) {
            SingleWheel()
        }
    }

    Variants(
        title = "Falloff",
        description = "How a row's appearance falls away from the centre.",
        layout = VariantLayout.Horizontal,
    ) {
        variant("Default", detail = "shrink and fade") {
            SingleWheel()
        }
        variant("Curved", detail = "WheelPickerFalloff.Curved") {
            SingleWheel(style = WheelPickerStyle.default(falloff = WheelPickerFalloff.Curved))
        }
        variant("None", detail = "WheelPickerFalloff(0f, 0f)") {
            SingleWheel(
                style = WheelPickerStyle.default(
                    falloff = WheelPickerFalloff(scalePerRow = 0f, alphaPerRow = 0f),
                ),
            )
        }
    }

    Variants(
        title = "Pacing",
        description = "How hard a wheel answers the hand. Each pace is a base speed that winds up " +
            "the longer a gesture is sustained, so nudge one of these a row at a time and then " +
            "keep scrolling. Shown on two hundred years, because a short wheel reaches its ends " +
            "before a gesture is over and hides the difference.",
        layout = VariantLayout.Horizontal,
    ) {
        variant("Short", detail = "WheelPickerScroll.Short") {
            YearWheel(scroll = WheelPickerScroll.Short)
        }
        variant("Medium", detail = "WheelPickerScroll.Medium") {
            YearWheel(scroll = WheelPickerScroll.Medium)
        }
        variant("Long", detail = "chosen automatically for 200 rows") {
            YearWheel(scroll = null)
        }
        variant("Brisk", detail = "WheelPickerScroll.Brisk") {
            YearWheel(scroll = WheelPickerScroll.Brisk)
        }
        variant("No acceleration", detail = "WheelPickerAcceleration.None") {
            YearWheel(
                scroll = WheelPickerScroll(acceleration = WheelPickerAcceleration.None),
            )
        }
    }

    EdgeCases {
        case(
            name = "Two wheels side by side",
            expectation = "Point at one and scroll: only that wheel turns. Neither its neighbour " +
                "nor this page moves.",
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
            ) {
                SingleWheel(modifier = Modifier.weight(1f))
                SingleWheel(modifier = Modifier.weight(1f), initialIndex = 30)
            }
        }
        case(
            name = "One notch, one row",
            expectation = "One click of a stepped mouse wheel advances exactly one row, on every " +
                "platform, whatever height the picker has. Acceleration does not change this: a " +
                "gesture starts from rest, so a single notch is never sped up.",
        ) {
            SingleWheel()
        }
        case(
            name = "A wheel with ends",
            expectation = "Scrolling past XL stops at XL. The page behind does not take over.",
        ) {
            val state = rememberWheelPickerColumnState(initialIndex = 2)
            WheelPicker(modifier = Modifier.width(120.dp)) {
                WheelPickerColumn(
                    items = Sizes,
                    state = state,
                    modifier = Modifier.weight(1f),
                    contentDescription = "Size",
                )
            }
        }
        case(
            name = "A wheel that lets the page through",
            expectation = "Same wheel with WheelPickerScrollPropagation.Propagate: overscrolling " +
                "it scrolls this page instead of stopping.",
        ) {
            val state = rememberWheelPickerColumnState(initialIndex = 2)
            WheelPicker(modifier = Modifier.width(120.dp)) {
                WheelPickerColumn(
                    items = Sizes,
                    state = state,
                    modifier = Modifier.weight(1f),
                    scrollPropagation = WheelPickerScrollPropagation.Propagate,
                    contentDescription = "Size",
                )
            }
        }
        case(
            name = "A wheel two hundred rows long",
            expectation = "A flick advances a handful of rows, not a century, and the wheel stops " +
                "shortly after the gesture does rather than coasting on.",
        ) {
            YearWheel()
        }
        case(
            name = "No items",
            expectation = "Keeps its height and draws nothing, rather than collapsing the layout " +
                "around it.",
        ) {
            val state = rememberWheelPickerColumnState()
            WheelPicker(modifier = Modifier.width(120.dp)) {
                WheelPickerColumn(
                    items = emptyList<String>(),
                    state = state,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

/**
 * One wheel of two hundred years, for the sections about pacing.
 *
 * Length is the point: a wheel of twelve runs out of rows before a gesture is over, so it absorbs
 * any amount of over-eagerness and shows none of it.
 *
 * @param scroll How hard the wheel answers scrolling, or `null` to let it pace itself.
 */
@Composable
private fun YearWheel(scroll: WheelPickerScroll? = null) {
    val state = rememberWheelPickerColumnState(initialIndex = Years.indexOf(2026))
    WheelPicker(modifier = Modifier.width(120.dp), scroll = scroll) {
        WheelPickerColumn(
            items = Years,
            state = state,
            modifier = Modifier.weight(1f),
            contentDescription = "Year",
        )
    }
}

/**
 * One wheel of numbers, used by the sections that are showing something other than the wheel itself.
 *
 * @param modifier Modifier applied to the picker.
 * @param style Colors and falloff.
 * @param initialIndex Value the wheel starts on.
 */
@Composable
private fun SingleWheel(
    modifier: Modifier = Modifier.width(120.dp),
    style: WheelPickerStyle = WheelPickerStyle.Default,
    initialIndex: Int = 0,
) {
    val state = rememberWheelPickerColumnState(initialIndex = initialIndex, infinite = true)
    val items = remember { Numbers }
    WheelPicker(modifier = modifier, style = style) {
        WheelPickerColumn(
            items = items,
            state = state,
            modifier = Modifier.weight(1f),
            contentDescription = "Number",
        ) { value ->
            WheelPickerItem(value.toString().padStart(2, '0'))
        }
    }
}
