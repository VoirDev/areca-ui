package dev.voir.areca.keyboard.layout

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.foundation.feedback.SpinnerSize
import dev.voir.areca.foundation.icon.IconSize
import dev.voir.areca.theme.ArecaTheme

/**
 * Spacing and key metrics of a keyboard.
 *
 * [keyHeight] is the one height a key has: keys are laid out by weight across the width, so their
 * height is the only dimension the keyboard decides, and [key] deliberately carries no minimum
 * height of its own to compete with it. A row may still override it for a taller action row.
 *
 * @property padding Space between the surface's edge and its contents.
 * @property rowGap Space between key rows.
 * @property keyGap Space between keys within a row.
 * @property keyHeight Height of a key, and so the height of a row that does not set its own.
 * @property headerGap Space between the header slot and the keys.
 * @property footerGap Space between the keys and the footer slot.
 * @property key Padding, type, and icon size of a single key. Its minimum width is zero, unlike
 *   every [ButtonSize] step: a button sized for a row of text would refuse to shrink below 96dp,
 *   which in a four-key row on a narrow phone forces the row wider than the screen. Its minimum
 *   height is ignored in favour of [keyHeight].
 * @property keyLabelMaxLines Most lines a label key's text takes before it is cut short with an
 *   ellipsis. At least `1`.
 * @throws IllegalArgumentException When [keyLabelMaxLines] is below `1`.
 */
@Immutable
public data class KeyboardSize(
    public val padding: PaddingValues,
    public val rowGap: Dp,
    public val keyGap: Dp,
    public val keyHeight: Dp,
    public val headerGap: Dp,
    public val footerGap: Dp,
    public val key: ButtonSize,
    public val keyLabelMaxLines: Int = 1,
) {
    init {
        require(keyLabelMaxLines >= 1) { "Key labels need at least one line, was $keyLabelMaxLines." }
    }

    /** Theme-backed [KeyboardSize] presets and their builder. */
    public companion object {
        /** Theme default: a comfortable touch target on a phone. */
        public val Default: KeyboardSize
            @Composable
            get() = default()

        /**
         * Tighter metrics for a keyboard that has to share a short screen: a landscape phone, or a
         * keyboard under a form that must stay partly visible.
         */
        public val Compact: KeyboardSize
            @Composable
            get() = default(
                padding = PaddingValues(
                    horizontal = ArecaTheme.spacing.lg,
                    vertical = ArecaTheme.spacing.md,
                ),
                rowGap = ArecaTheme.spacing.sm,
                keyGap = ArecaTheme.spacing.sm,
                keyHeight = 44.dp,
            )

        /**
         * Builds keyboard metrics from the current theme.
         *
         * Every parameter defaults to the value [Default] uses, so overriding one stays a one-line
         * change.
         *
         * @param padding Space between the surface's edge and its contents.
         * @param rowGap Space between key rows.
         * @param keyGap Space between keys within a row.
         * @param keyHeight Height of a key.
         * @param headerGap Space between the header slot and the keys.
         * @param footerGap Space between the keys and the footer slot.
         * @param key Padding, type, and icon size of a single key.
         * @param keyLabelMaxLines Most lines a label key's text takes.
         * @return A keyboard size containing the supplied metrics.
         * @throws IllegalArgumentException When [keyLabelMaxLines] is below `1`.
         */
        @Composable
        public fun default(
            padding: PaddingValues = PaddingValues(
                horizontal = ArecaTheme.spacing.xl,
                vertical = ArecaTheme.spacing.lg,
            ),
            rowGap: Dp = ArecaTheme.spacing.md,
            keyGap: Dp = ArecaTheme.spacing.md,
            keyHeight: Dp = 52.dp,
            headerGap: Dp = ArecaTheme.spacing.md,
            footerGap: Dp = ArecaTheme.spacing.md,
            key: ButtonSize = ButtonSize(
                borderWidth = 1.dp,
                padding = PaddingValues(ArecaTheme.spacing.xs),
                gap = ArecaTheme.spacing.sm,
                icon = IconSize.Large,
                textStyle = ArecaTheme.typography.bodyLarge.merge(fontWeight = FontWeight.SemiBold),
                minWidth = 0.dp,
                minHeight = 0.dp,
                spinner = SpinnerSize.Small,
            ),
            keyLabelMaxLines: Int = 1,
        ): KeyboardSize = KeyboardSize(
            padding = padding,
            rowGap = rowGap,
            keyGap = keyGap,
            keyHeight = keyHeight,
            headerGap = headerGap,
            footerGap = footerGap,
            key = key,
            keyLabelMaxLines = keyLabelMaxLines,
        )
    }
}

/**
 * Default [KeyboardSize] in scope, or `null` to use [KeyboardSize.Default].
 *
 * Not seeded by [dev.voir.areca.theme.ArecaThemeProvider] — see [LocalKeyboardStyle] for why.
 */
public val LocalKeyboardSize: ProvidableCompositionLocal<KeyboardSize?> =
    compositionLocalOf { null }
