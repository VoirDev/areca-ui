package dev.voir.areca.keyboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import dev.voir.areca.theme.ArecaTheme

/**
 * The tinted layer drawn between a [KeyboardHost]'s content and its keyboard.
 *
 * A keyboard is not modal by default — a screen normally stays readable and scrollable while it is
 * up — so a host draws no scrim unless it is given one. Supply one when the keyboard genuinely
 * interrupts, such as a numeric pad that owns the screen until an amount is confirmed.
 *
 * Whether or not it closes the keyboard, the tint takes every press that lands on it, so the content
 * beneath cannot be pressed or scrolled while it shows.
 *
 * @property color Tint laid over the content. Translucent, or the content disappears.
 * @property dismissOnTap Whether tapping the tint closes the keyboard. `false` makes the tint absorb
 *   taps without closing anything, for a keyboard that must be dismissed through one of its own
 *   keys.
 * @property dismissLabel What assistive technology reads for the tint, and for the action that
 *   closes the keyboard through it. Only used when [dismissOnTap] is set. English by default; pass
 *   the product's own.
 */
@Immutable
public data class KeyboardScrim(
    public val color: Color,
    public val dismissOnTap: Boolean = true,
    public val dismissLabel: String = "Close keyboard",
) {
    /** Theme-backed [KeyboardScrim] preset and its builder. */
    public companion object {
        /** Theme-backed scrim, using the palette's dark overlay. */
        public val Default: KeyboardScrim
            @Composable
            get() = default()

        /**
         * Creates the default scrim treatment.
         *
         * @param color Tint laid over the content.
         * @param dismissOnTap Whether tapping the tint closes the keyboard.
         * @param dismissLabel What assistive technology reads for the tint and its close action.
         * @return A scrim with the supplied tint and behaviour.
         */
        @Composable
        public fun default(
            color: Color = ArecaTheme.colors.bgOverlayDark,
            dismissOnTap: Boolean = true,
            dismissLabel: String = "Close keyboard",
        ): KeyboardScrim = KeyboardScrim(
            color = color,
            dismissOnTap = dismissOnTap,
            dismissLabel = dismissLabel,
        )
    }
}
