package dev.voir.areca.keyboard.key

import androidx.compose.runtime.Immutable

/**
 * What assistive technology reads for the utility keys of the [KeyboardGrids] presets, with English
 * defaults. Pass the product's own.
 *
 * The keys themselves show glyphs — an arrow, a chevron, a tick, `"AC"` — so these words are only
 * ever spoken.
 *
 * @property backspace The key that deletes the last character.
 * @property clear The key that clears the entry, shown as `"AC"`.
 * @property hide The key that closes the keyboard without committing.
 * @property done The key that finishes entry.
 */
@Immutable
public data class KeyboardGridLabels(
    public val backspace: String = "Backspace",
    public val clear: String = "Clear",
    public val hide: String = "Hide keyboard",
    public val done: String = "Done",
)
