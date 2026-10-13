package dev.voir.areca.playground.registry

import dev.voir.areca.playground.foundation.icon.IconsShowcase
import dev.voir.areca.playground.foundation.indication.IndicationShowcase
import dev.voir.areca.playground.foundation.theme.ColorsShowcase

/**
 * Token and asset pages in the [ShowcaseGroup.Foundations] group.
 *
 * Source order is for reading only: the sidebar sorts each section by title.
 */
internal val FoundationsShowcases: List<ShowcaseEntry> = listOf(
    ShowcaseEntry(
        id = "foundation/colors",
        group = ShowcaseGroup.Foundations,
        title = "Colors",
        summary = "Every token in ArecaColors, and the raw ramps underneath it.",
        keywords = listOf(
            "color", "colour", "token", "palette", "theme", "ramp", "swatch",
            "contrast", "accessibility", "dark mode", "foreground", "background",
        ),
        content = { ColorsShowcase() },
    ),
    ShowcaseEntry(
        id = "icons/catalog",
        group = ShowcaseGroup.Foundations,
        title = "Icons",
        summary = "The Areca icon set, grouped by purpose and filterable by name.",
        keywords = listOf("icon", "glyph", "symbol", "pictogram"),
        content = { IconsShowcase() },
    ),
    ShowcaseEntry(
        id = "foundation/indication",
        group = ShowcaseGroup.Foundations,
        title = "Indication",
        summary = "Press, hover and focus feedback shared by every interactive component.",
        keywords = listOf("ripple", "press", "hover", "focus", "interaction"),
        content = { IndicationShowcase() },
    ),
)
