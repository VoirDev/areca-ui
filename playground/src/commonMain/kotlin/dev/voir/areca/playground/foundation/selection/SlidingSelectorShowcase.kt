package dev.voir.areca.playground.foundation.selection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.selection.SelectorItem
import dev.voir.areca.foundation.selection.SelectorLayout
import dev.voir.areca.foundation.selection.SelectorMode
import dev.voir.areca.foundation.selection.SelectorOrientation
import dev.voir.areca.foundation.selection.SlidingSelector
import dev.voir.areca.foundation.selection.SlidingSelectorSize
import dev.voir.areca.foundation.selection.SlidingSelectorStyle
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.Monitor
import dev.voir.areca.icons.MoonAlt
import dev.voir.areca.icons.Sun
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.Sizes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.ArecaThemeProvider

/** The looks a selector is drawn with, as the playground names them. */
private enum class SelectorPreset {
    Segmented,
    Pill,
    Underline,
    Button,
}

/** The [SlidingSelectorStyle] preset of this name. */
private val SelectorPreset.style: SlidingSelectorStyle
    @Composable
    get() = when (this) {
        SelectorPreset.Segmented -> SlidingSelectorStyle.Segmented
        SelectorPreset.Pill -> SlidingSelectorStyle.Pill
        SelectorPreset.Underline -> SlidingSelectorStyle.Underline
        SelectorPreset.Button -> SlidingSelectorStyle.Button
    }

/** Ladder of [SlidingSelectorSize] presets. */
private enum class SelectorSizeOption {
    Small,
    Default,
    Large,
}

/** The [SlidingSelectorSize] preset of this name. */
private val SelectorSizeOption.size: SlidingSelectorSize
    @Composable
    get() = when (this) {
        SelectorSizeOption.Small -> SlidingSelectorSize.Small
        SelectorSizeOption.Default -> SlidingSelectorSize.Default
        SelectorSizeOption.Large -> SlidingSelectorSize.Large
    }

/** Fragrance layers, the options of the examples. */
private val Layers = listOf("Any level", "Top", "Heart", "Base")

/** Showcase for [SlidingSelector]. */
@Composable
fun SlidingSelectorShowcase() = ShowcaseScaffold(
    title = "Sliding selector",
    summary = "A row of options with one thumb that slides to the chosen one.",
    module = "foundation",
) {
    Playground {
        val preset by enumControl(
            key = "style",
            default = SelectorPreset.Segmented,
            options = SelectorPreset.entries,
            optionLabel = { it.name },
        )
        val size by enumControl(
            key = "size",
            default = SelectorSizeOption.Default,
            options = SelectorSizeOption.entries,
            optionLabel = { it.name },
        )
        val mode by enumControl(
            key = "mode",
            default = SelectorMode.Required,
            options = SelectorMode.entries,
            optionLabel = { it.name },
            description = "Optional clears the selection when the chosen option is tapped again.",
        )
        val layout by enumControl(
            key = "layout",
            default = SelectorLayout.Hug,
            options = SelectorLayout.entries,
            optionLabel = { it.name },
        )
        val orientation by enumControl(
            key = "orientation",
            default = SelectorOrientation.Horizontal,
            options = SelectorOrientation.entries,
            optionLabel = { it.name },
        )
        val disabled by booleanControl(
            key = "disabled",
            default = false,
            description = "Disable the last option.",
        )

        preview {
            var selected by remember { mutableStateOf<String?>("Top") }
            SlidingSelector(
                items = Layers.map { SelectorItem(it, enabled = !(disabled && it == Layers.last())) { Text(it) } },
                selected = selected,
                onSelect = { selected = it },
                modifier = if (orientation == SelectorOrientation.Vertical) Modifier.height(200.dp) else Modifier,
                mode = mode,
                style = preset.style,
                size = size.size,
                layout = layout,
                orientation = orientation,
            )
        }
    }

    Variants(
        title = "Style × mode",
        description = "Each preset in both modes. In Optional, tap the chosen option to clear it.",
        layout = VariantLayout.Grid,
    ) {
        for (preset in SelectorPreset.entries) {
            for (mode in SelectorMode.entries) {
                variant(name = "${preset.name} / ${mode.name}") {
                    var selected by remember { mutableStateOf<String?>(if (mode == SelectorMode.Required) "Top" else null) }
                    SlidingSelector(
                        items = Layers.map { SelectorItem(it) { Text(it) } },
                        selected = selected,
                        onSelect = { selected = it },
                        mode = mode,
                        style = preset.style,
                        layout = SelectorLayout.Hug,
                    )
                }
            }
        }
    }

    Variants(
        title = "Layout",
        description = "Fill shares the width equally, Hug sizes each option to its label, Scroll overflows.",
        layout = VariantLayout.Vertical,
    ) {
        for (layout in SelectorLayout.entries) {
            variant(name = layout.name) {
                var selected by remember { mutableStateOf<String?>("Heart") }
                SlidingSelector(
                    items = (Layers + listOf("Accord", "Drydown")).map { SelectorItem(it) { Text(it) } },
                    selected = selected,
                    onSelect = { selected = it },
                    modifier = if (layout == SelectorLayout.Scroll) Modifier.width(260.dp) else Modifier,
                    layout = layout,
                )
            }
        }
    }

    Sizes(layout = VariantLayout.Vertical) {
        for (option in SelectorSizeOption.entries) {
            variant(
                name = option.name,
                detail = when (option) {
                    SelectorSizeOption.Small -> "28dp min height, labelSmall"
                    SelectorSizeOption.Default -> "32dp min height, labelMedium"
                    SelectorSizeOption.Large -> "40dp min height, labelLarge"
                },
            ) {
                var selected by remember { mutableStateOf<String?>("Top") }
                SlidingSelector(
                    items = Layers.map { SelectorItem(it) { Text(it) } },
                    selected = selected,
                    onSelect = { selected = it },
                    size = option.size,
                    layout = SelectorLayout.Hug,
                )
            }
        }
    }

    States {
        variant(name = "Nothing selected", detail = "Optional mode; the first choice grows the thumb in.") {
            var selected by remember { mutableStateOf<String?>(null) }
            SlidingSelector(
                items = Layers.map { SelectorItem(it) { Text(it) } },
                selected = selected,
                onSelect = { selected = it },
                mode = SelectorMode.Optional,
                style = SlidingSelectorStyle.Pill,
                layout = SelectorLayout.Hug,
            )
        }
        variant(name = "Disabled option", detail = "Skipped by touch, keyboard and screen readers.") {
            var selected by remember { mutableStateOf<String?>("Top") }
            SlidingSelector(
                items = Layers.map { SelectorItem(it, enabled = it != "Heart") { Text(it) } },
                selected = selected,
                onSelect = { selected = it },
                layout = SelectorLayout.Hug,
            )
        }
        variant(name = "Reduced motion", detail = "The thumb fades out and back in instead of travelling.") {
            var selected by remember { mutableStateOf<String?>("Top") }
            WithReducedMotion {
                SlidingSelector(
                    items = Layers.map { SelectorItem(it) { Text(it) } },
                    selected = selected,
                    onSelect = { selected = it },
                    layout = SelectorLayout.Hug,
                )
            }
        }
    }

    Recipes {
        recipe(
            name = "Has / Avoid",
            description = "A three-state filter: Optional mode, no ground, a thumb colour per option.",
        ) {
            var selected by remember { mutableStateOf<String?>(null) }
            SlidingSelector(
                items = listOf(
                    SelectorItem(
                        value = "Has",
                        thumb = ArecaTheme.colors.fgBase,
                        contentDescription = "Has iris",
                    ) { Text("Has") },
                    SelectorItem(
                        value = "Avoid",
                        thumb = ArecaTheme.colors.bgDangerBase,
                        contentDescription = "Avoid iris",
                    ) { Text("Avoid") },
                ),
                selected = selected,
                onSelect = { selected = it },
                mode = SelectorMode.Optional,
                style = SlidingSelectorStyle.Pill,
                size = SlidingSelectorSize.Small,
                layout = SelectorLayout.Hug,
            )
        }
        recipe(
            name = "Theme switch",
            description = "Icon options, each named for a screen reader.",
        ) {
            var selected by remember { mutableStateOf<String?>("System") }
            SlidingSelector(
                items = listOf(
                    SelectorItem("Light", contentDescription = "Light theme") {
                        Icon(ArecaIcons.Sun, contentDescription = null)
                    },
                    SelectorItem("Dark", contentDescription = "Dark theme") {
                        Icon(ArecaIcons.MoonAlt, contentDescription = null)
                    },
                    SelectorItem("System", contentDescription = "System theme") {
                        Icon(ArecaIcons.Monitor, contentDescription = null)
                    },
                ),
                selected = selected,
                onSelect = { selected = it },
                layout = SelectorLayout.Hug,
            )
        }
        recipe(
            name = "Icon and label",
            description = "Content is any composable; the selector colours and sizes it.",
        ) {
            var selected by remember { mutableStateOf<String?>("Day") }
            SlidingSelector(
                items = listOf("Day" to ArecaIcons.Sun, "Night" to ArecaIcons.MoonAlt).map { (label, icon) ->
                    SelectorItem(label) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xs),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(icon, contentDescription = null)
                            Text(label)
                        }
                    }
                },
                selected = selected,
                onSelect = { selected = it },
                style = SlidingSelectorStyle.Button,
                layout = SelectorLayout.Hug,
            )
        }
    }

    EdgeCases {
        case(
            name = "Right-to-left",
            expectation = "The order and the thumb's motion mirror.",
        ) {
            var selected by remember { mutableStateOf<String?>("Top") }
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                SlidingSelector(
                    items = Layers.map { SelectorItem(it) { Text(it) } },
                    selected = selected,
                    onSelect = { selected = it },
                    style = SlidingSelectorStyle.Underline,
                    layout = SelectorLayout.Hug,
                )
            }
        }
        case(
            name = "Labels of very different lengths",
            expectation = "The thumb takes each option's measured width.",
        ) {
            var selected by remember { mutableStateOf<String?>("All of them") }
            SlidingSelector(
                items = listOf("All of them", "Any one", "No").map { SelectorItem(it) { Text(it) } },
                selected = selected,
                onSelect = { selected = it },
                style = SlidingSelectorStyle.Pill,
                layout = SelectorLayout.Hug,
            )
        }
        case(
            name = "Many options in a narrow container",
            expectation = "Scroll layout keeps the chosen option in view.",
        ) {
            var selected by remember { mutableStateOf<String?>("Drydown") }
            SlidingSelector(
                items = (Layers + listOf("Accord", "Drydown")).map { SelectorItem(it) { Text(it) } },
                selected = selected,
                onSelect = { selected = it },
                modifier = Modifier.width(200.dp),
                layout = SelectorLayout.Scroll,
            )
        }
        case(
            name = "Vertical",
            expectation = "The thumb slides down the column, as wide as the column.",
        ) {
            var selected by remember { mutableStateOf<String?>("Heart") }
            SlidingSelector(
                items = Layers.map { SelectorItem(it) { Text(it) } },
                selected = selected,
                onSelect = { selected = it },
                style = SlidingSelectorStyle.Button,
                layout = SelectorLayout.Hug,
                orientation = SelectorOrientation.Vertical,
            )
        }
    }
}

/**
 * Provides the current theme again with [reduceMotion][dev.voir.areca.theme.ArecaMotion.reduceMotion]
 * on, the way an app passes the platform's setting through.
 *
 * @param content Content that should move less.
 */
@Composable
private fun WithReducedMotion(content: @Composable () -> Unit) {
    ArecaThemeProvider(
        darkTheme = ArecaTheme.isDark,
        baseColors = ArecaTheme.baseColors,
        colors = ArecaTheme.colors,
        radius = ArecaTheme.radius,
        shapes = ArecaTheme.shapes,
        spacing = ArecaTheme.spacing,
        elevation = ArecaTheme.elevation,
        motion = ArecaTheme.motion.copy(reduceMotion = true),
        fonts = ArecaTheme.fonts,
        baseTypography = ArecaTheme.baseTypography,
        typography = ArecaTheme.typography,
        content = content,
    )
}
