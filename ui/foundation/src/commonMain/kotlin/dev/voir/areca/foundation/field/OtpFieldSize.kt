package dev.voir.areca.foundation.field

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.theme.ArecaTheme

/**
 * Size every [OtpField] below this point uses unless it is given one.
 *
 * Null means "no app-wide choice has been made", and the field falls back to
 * [OtpFieldSize.Default]. Nullable rather than erroring for the same reason [LocalFieldSize] is.
 */
public val LocalOtpFieldSize: ProvidableCompositionLocal<OtpFieldSize?> =
    compositionLocalOf { null }

/**
 * Metrics for an [OtpField] at one size on the ramp.
 *
 * The cells are field containers, so their border and focus ring take the same thicknesses a
 * [FieldSize] of the same height does, and a code field sits in a form without looking like a
 * different family of control.
 *
 * @property cellWidth Width of each cell when the field is not stretched. A stretched field
 *   divides its width between the cells instead, and ignores this.
 * @property cellHeight Height of each cell, stretched or not.
 * @property gap Space between two cells of the same group.
 * @property groupGap Space between two groups, when the field is grouped. Wider than [gap], so
 *   the grouping reads without a separator.
 * @property borderWidth Border thickness of each cell.
 * @property focusRing Thickness of the halo around the cell that takes the next character.
 * @property caretWidth Width of the bar drawn in the cell that takes the next character.
 * @property caretHeight Height of that bar.
 * @property textStyle Type for the characters. Tabular figures, so a column of codes does not
 *   jitter from one digit to the next.
 */
@Stable
public data class OtpFieldSize(
    public val cellWidth: Dp,
    public val cellHeight: Dp,
    public val gap: Dp,
    public val groupGap: Dp,
    public val borderWidth: Dp,
    public val focusRing: Dp,
    public val caretWidth: Dp,
    public val caretHeight: Dp,
    public val textStyle: TextStyle,
) {
    /** The steps of the ramp. */
    public companion object {
        /** Theme default: the [Medium] cell, the code a verification screen exists for. */
        public val Default: OtpFieldSize
            @Composable
            get() = Medium

        /**
         * A compact cell, 40 by 48dp under touch, 32 by 36dp under a pointer — a code inside a
         * dialog or a settings row.
         */
        public val Small: OtpFieldSize
            @Composable
            get() {
                val sizing = ArecaTheme.sizing
                val spacing = ArecaTheme.spacing
                return OtpFieldSize(
                    cellWidth = sizing.select(touch = 40.dp, pointer = 32.dp),
                    cellHeight = sizing.controlMd,
                    gap = sizing.select(touch = spacing.sm, pointer = spacing.xs),
                    groupGap = sizing.select(touch = spacing.xl, pointer = spacing.lg),
                    borderWidth = 1.dp,
                    focusRing = 3.dp,
                    caretWidth = 2.dp,
                    caretHeight = 20.dp,
                    textStyle = codeTextStyle(ArecaTheme.baseTypography.lg),
                )
            }

        /**
         * The default cell, 48 by 56dp under touch, 40 by 44dp under a pointer — the code a
         * verification screen exists for.
         */
        public val Medium: OtpFieldSize
            @Composable
            get() {
                val sizing = ArecaTheme.sizing
                val spacing = ArecaTheme.spacing
                return OtpFieldSize(
                    cellWidth = sizing.select(touch = 48.dp, pointer = 40.dp),
                    cellHeight = sizing.controlLg,
                    gap = sizing.select(touch = spacing.md, pointer = spacing.sm),
                    groupGap = sizing.select(touch = spacing.xl2, pointer = spacing.xl),
                    borderWidth = 1.dp,
                    focusRing = 3.dp,
                    caretWidth = 2.dp,
                    caretHeight = 24.dp,
                    textStyle = codeTextStyle(ArecaTheme.baseTypography.xl),
                )
            }

        /**
         * A roomy cell, 56 by 64dp under touch, 48 by 52dp under a pointer — a short code, four
         * digits, that is the whole screen.
         */
        public val Large: OtpFieldSize
            @Composable
            get() {
                val sizing = ArecaTheme.sizing
                val spacing = ArecaTheme.spacing
                return OtpFieldSize(
                    cellWidth = sizing.select(touch = 56.dp, pointer = 48.dp),
                    cellHeight = sizing.controlXl,
                    gap = sizing.select(touch = spacing.lg, pointer = spacing.md),
                    groupGap = sizing.select(touch = spacing.xl3, pointer = spacing.xl2),
                    borderWidth = 1.dp,
                    focusRing = 4.dp,
                    caretWidth = 2.dp,
                    caretHeight = 28.dp,
                    textStyle = codeTextStyle(ArecaTheme.baseTypography.xl2),
                )
            }

        /**
         * Turns a step of the base type ramp into the type a code is drawn in.
         *
         * @param base Step of the ramp that sets the size.
         * @return The step, semibold, with tabular figures.
         */
        private fun codeTextStyle(base: TextStyle): TextStyle = base.merge(
            fontWeight = FontWeight.SemiBold,
            fontFeatureSettings = "tnum",
        )
    }
}
