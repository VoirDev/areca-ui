package dev.voir.areca.foundation.notice

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import dev.voir.areca.foundation.icon.FeaturedIconStyle
import dev.voir.areca.theme.ArecaTheme

/**
 * Colors and shape of a [Notice].
 *
 * Built from a tone and an emphasis with [of] rather than assembled by hand, so a caller that needs
 * a different ground or corner changes that one thing and keeps every ink the tone decided.
 *
 * @property bg Ground of the block.
 * @property shape Shape of the block.
 * @property title Color of the title.
 * @property body Color of an ordinary line of copy.
 * @property quiet Color of the quietest line — advice rather than the fact.
 * @property chip Fill and glyph colors of the mark of a titled notice.
 * @property chipShape Shape of the mark of a titled notice.
 * @property glyph Color of the bare mark of an untitled notice, and of the spinner of a busy one.
 */
@Immutable
public data class NoticeStyle(
    public val bg: Color,
    public val shape: Shape,
    public val title: Color,
    public val body: Color,
    public val quiet: Color,
    public val chip: FeaturedIconStyle,
    public val chipShape: Shape,
    public val glyph: Color,
) {
    /** The theme default, and [of], which builds a [NoticeStyle] from a tone and an emphasis. */
    public companion object {
        /** Theme default: a subtle neutral notice, as [Notice] draws without a tone. */
        public val Default: NoticeStyle
            @Composable
            get() = of(NoticeTone.Neutral)

        /**
         * Builds a style from the current theme.
         *
         * [bg] is only the ground of a [NoticeEmphasis.Subtle] notice: a filled one is on its
         * tone's own muted tint whatever it is drawn over. Pick it for what the notice stands on —
         * a grey tile inside a white panel, the white surface on a grey page.
         *
         * @param tone What the notice is saying.
         * @param emphasis How much of the notice carries [tone].
         * @param bg Ground of a subtle notice.
         * @param shape Shape of the block.
         * @param chipShape Shape of the mark of a titled notice.
         * @return Style for one notice.
         */
        @Composable
        public fun of(
            tone: NoticeTone,
            emphasis: NoticeEmphasis = NoticeEmphasis.Subtle,
            bg: Color = ArecaTheme.colors.bgSubtle,
            shape: Shape = ArecaTheme.shapes.lg,
            chipShape: Shape = ArecaTheme.shapes.md,
        ): NoticeStyle {
            val colors = ArecaTheme.colors
            val ink = toneInk(tone)

            return when (emphasis) {
                NoticeEmphasis.Subtle -> NoticeStyle(
                    bg = bg,
                    shape = shape,
                    title = colors.fgBase,
                    body = colors.fgSubtle,
                    quiet = colors.fgMuted,
                    chip = FeaturedIconStyle(bg = ink.muted, content = ink.mark),
                    chipShape = chipShape,
                    glyph = ink.mark,
                )

                NoticeEmphasis.Filled -> NoticeStyle(
                    bg = ink.muted,
                    shape = shape,
                    title = ink.title,
                    body = ink.body,
                    quiet = ink.quiet,
                    chip = FeaturedIconStyle(bg = ink.base, content = ink.onBase),
                    chipShape = chipShape,
                    glyph = ink.mark,
                )
            }
        }

        /**
         * The theme tokens that stand for one [NoticeTone].
         *
         * @param tone Tone to resolve.
         * @return Its colors.
         */
        @Composable
        private fun toneInk(tone: NoticeTone): ToneInk {
            val colors = ArecaTheme.colors
            return when (tone) {
                // Neutral has no text step of its own: a filled neutral notice keeps the page's
                // own ramp of inks on a neutral tint.
                NoticeTone.Neutral -> ToneInk(
                    muted = colors.bgNeutralMuted,
                    base = colors.bgNeutralBase,
                    onBase = colors.fgNeutral,
                    mark = colors.fgMuted,
                    title = colors.fgBase,
                    body = colors.fgSubtle,
                    quiet = colors.fgMuted,
                )

                NoticeTone.Info -> ToneInk.of(
                    muted = colors.bgInfoMuted,
                    base = colors.bgInfoBase,
                    onBase = colors.fgOnFill,
                    mark = colors.fgInfo,
                    text = colors.fgInfoText,
                )

                NoticeTone.Success -> ToneInk.of(
                    muted = colors.bgSuccessMuted,
                    base = colors.bgSuccessBase,
                    onBase = colors.fgOnFill,
                    mark = colors.fgSuccess,
                    text = colors.fgSuccessText,
                )

                // The warning fill is light in both themes, so what sits on it is dark.
                NoticeTone.Warning -> ToneInk.of(
                    muted = colors.bgWarnMuted,
                    base = colors.bgWarnBase,
                    onBase = colors.fgOnWarn,
                    mark = colors.fgWarn,
                    text = colors.fgWarnText,
                )

                NoticeTone.Danger -> ToneInk.of(
                    muted = colors.bgDangerMuted,
                    base = colors.bgDangerBase,
                    onBase = colors.fgOnFill,
                    mark = colors.fgDanger,
                    text = colors.fgDangerText,
                )
            }
        }
    }
}

/**
 * [NoticeStyle] every [Notice] in scope draws with, in place of the one its tone and emphasis
 * derive.
 *
 * Unlike the other component style locals it holds no default: a notice's look follows from what it
 * says, so [dev.voir.areca.theme.ArecaThemeProvider] seeds it with `null` and each notice builds
 * [NoticeStyle.of] from its own `tone` and `emphasis`. Providing a style overrides that for the
 * whole subtree — every tone then draws the same — so provide one only for a region that needs a
 * single look, such as a panel whose notices all sit on its own ground. An explicit `style`
 * argument still wins over the local.
 */
public val LocalNoticeStyle: ProvidableCompositionLocal<NoticeStyle?> = compositionLocalOf { null }

/**
 * The colors of one tone, as a notice uses them.
 *
 * @property muted Tint of the tone, the ground of a filled notice and of a subtle chip.
 * @property base Solid fill of the tone, the chip of a filled notice.
 * @property onBase Glyph on [base].
 * @property mark Glyph on [muted] or on a neutral ground.
 * @property title Title of a filled notice.
 * @property body Ordinary line of a filled notice.
 * @property quiet Quietest line of a filled notice.
 */
private class ToneInk(
    val muted: Color,
    val base: Color,
    val onBase: Color,
    val mark: Color,
    val title: Color,
    val body: Color,
    val quiet: Color,
) {
    /** Builder for the tones whose copy is one text step throughout. */
    companion object {
        /**
         * A tone whose title, lines and quietest line are all its text step.
         *
         * A filled notice is one message in one ink; ranking its lines by lightness would put a
         * third of it below the contrast the tone's text step was chosen to clear.
         *
         * @param muted Tint of the tone.
         * @param base Solid fill of the tone.
         * @param onBase Glyph on [base].
         * @param mark Glyph on [muted].
         * @param text Text step of the tone.
         * @return The tone's colors.
         */
        fun of(muted: Color, base: Color, onBase: Color, mark: Color, text: Color): ToneInk =
            ToneInk(
                muted = muted,
                base = base,
                onBase = onBase,
                mark = mark,
                title = text,
                body = text,
                quiet = text,
            )
    }
}
