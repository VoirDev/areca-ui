package dev.voir.areca.playground.registry

import dev.voir.areca.playground.keyboard.KeyboardShowcase

/**
 * Component pages for `:ui:keyboard`, in the [ShowcaseGroup.Keyboard] group.
 *
 * Source order is for reading only: the sidebar sorts each section by title.
 */
internal val KeyboardShowcases: List<ShowcaseEntry> = listOf(
    ShowcaseEntry(
        id = "keyboard/keyboard",
        group = ShowcaseGroup.Keyboard,
        title = "Keyboard",
        summary = "Custom keyboard docked to the bottom of a screen: host, styled surface with " +
            "header and footer slots, and grids of weighted keys.",
        keywords = listOf("keypad", "numpad", "numeric", "pin", "calculator", "input", "keys"),
        content = { KeyboardShowcase() },
    ),
)
