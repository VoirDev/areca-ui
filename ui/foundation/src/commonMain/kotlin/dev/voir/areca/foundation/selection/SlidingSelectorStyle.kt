package dev.voir.areca.foundation.selection

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.foundation.Indication
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.indication.FadeIndication
import dev.voir.areca.indication.TouchIndication
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.slide

/**
 * Look of a [SlidingSelector]: its colours and shapes, the track the options sit on, the thumb that
 * marks the chosen one, and how that thumb moves.
 *
 * The geometry that makes a look — the track's padding and border, the thumb's elevation and
 * thickness — belongs to the style rather than to [SlidingSelectorSize], so a preset alone gives the
 * whole look and any size can be worn with it.
 *
 * Content colours reach an option's content through
 * [LocalContentColor][dev.voir.areca.foundation.LocalContentColor], so a plain `Text` or `Icon` is
 * coloured correctly without being told.
 *
 * @property content Content colour of an option that is not chosen.
 * @property contentSelected Content colour of the chosen option, unless the option gives its own.
 * @property contentDisabled Content colour of an option that cannot be chosen.
 * @property track Ground the options sit on. [Color.Transparent] for none.
 * @property trackBorder Outline of the track. [Color.Transparent] for none.
 * @property trackShape Shape of the track and its outline.
 * @property trackBorderWidth Width of the track's outline.
 * @property trackPadding Space between the track's edge and the options.
 * @property thumb Colour of the thumb, unless the chosen option gives its own.
 * @property thumbKind What the thumb is: a fill behind the option, a bar along one edge, or none.
 * @property thumbShape Shape of the thumb. An underline's ends follow it too.
 * @property thumbElevation Shadow under a [fill][SelectorThumbKind.Fill] thumb. `0.dp` for flat.
 * @property thumbThickness Thickness of an [underline][SelectorThumbKind.Underline] thumb.
 * @property thumbLength Length of an underline thumb, centred on the chosen option, or
 *   [Dp.Unspecified] for the option's full length.
 * @property thumbPadding Space between the chosen option's bounds and the thumb. For an underline,
 *   the padding on its [edge][thumbEdge] lifts it off that edge.
 * @property thumbEdge Edge an underline thumb and the [divider] run along.
 * @property divider Line running the selector's length along [thumbEdge]. [Color.Transparent] for
 *   none.
 * @property dividerThickness Thickness of the [divider].
 * @property itemShape Shape of an option's press, hover and focus feedback.
 * @property indication Feedback an option draws for press, hover and focus.
 * @property motion How the thumb moves between options, grows in and shrinks away. Defaults to the
 *   theme's [slide][dev.voir.areca.theme.slide] spring; pass a spring with a lower damping ratio for a
 *   slight settle, or a tween.
 */
@Immutable
public data class SlidingSelectorStyle(
    public val content: Color,
    public val contentSelected: Color,
    public val contentDisabled: Color,
    public val track: Color,
    public val trackBorder: Color,
    public val trackShape: Shape,
    public val trackBorderWidth: Dp,
    public val trackPadding: Dp,
    public val thumb: Color,
    public val thumbKind: SelectorThumbKind,
    public val thumbShape: Shape,
    public val thumbElevation: Dp,
    public val thumbThickness: Dp,
    public val thumbLength: Dp,
    public val thumbPadding: PaddingValues,
    public val thumbEdge: SelectorEdge,
    public val divider: Color,
    public val dividerThickness: Dp,
    public val itemShape: Shape,
    public val indication: Indication,
    public val motion: FiniteAnimationSpec<Float>,
) {
    /** Theme-backed presets and builders. */
    public companion object {
        /** Theme default: [Segmented]. */
        public val Default: SlidingSelectorStyle
            @Composable
            get() = Segmented

        /**
         * Options in a capsule track, with a raised capsule thumb sliding to the chosen one. For a
         * setting with a few values.
         */
        public val Segmented: SlidingSelectorStyle
            @Composable
            get() = default()

        /**
         * No track and no border: the words alone, with a capsule sliding under the chosen one. For
         * a light choice inside other content, such as a filter.
         */
        public val Pill: SlidingSelectorStyle
            @Composable
            get() = default(
                contentSelected = ArecaTheme.colors.fgOnFill,
                track = Color.Transparent,
                trackPadding = 0.dp,
                thumb = ArecaTheme.colors.fgBase,
                thumbElevation = 0.dp,
                // The theme's foreground, not the light selected colour, which would not show over
                // the bare ground under an option that is not chosen.
                indication = TouchIndication(shape = ArecaTheme.shapes.full),
            )

        /**
         * A divider along the options, with a bar sliding along it under the chosen one. For
         * switching views within a screen.
         */
        public val Underline: SlidingSelectorStyle
            @Composable
            get() = default(
                track = Color.Transparent,
                trackShape = ArecaTheme.shapes.none,
                trackPadding = 0.dp,
                thumb = ArecaTheme.colors.fgBase,
                thumbKind = SelectorThumbKind.Underline,
                thumbElevation = 0.dp,
                divider = ArecaTheme.colors.borderBase,
                itemShape = ArecaTheme.shapes.md,
                indication = FadeIndication(shape = ArecaTheme.shapes.md),
            )

        /**
         * No track: the chosen option wears a flat fill, which slides between options. Reads as a
         * group of toggle buttons.
         */
        public val Button: SlidingSelectorStyle
            @Composable
            get() = default(
                track = Color.Transparent,
                trackShape = ArecaTheme.shapes.md,
                trackPadding = 0.dp,
                thumb = ArecaTheme.colors.bgMuted,
                thumbShape = ArecaTheme.shapes.md,
                thumbElevation = 0.dp,
                itemShape = ArecaTheme.shapes.md,
            )

        /**
         * Builds a style from the current theme. Every parameter defaults to the [Segmented] look.
         *
         * @param content Content colour of an option that is not chosen.
         * @param contentSelected Content colour of the chosen option.
         * @param contentDisabled Content colour of a disabled option.
         * @param track Ground the options sit on.
         * @param trackBorder Outline of the track.
         * @param trackShape Shape of the track.
         * @param trackBorderWidth Width of the track's outline.
         * @param trackPadding Space between the track's edge and the options.
         * @param thumb Colour of the thumb.
         * @param thumbKind What the thumb is.
         * @param thumbShape Shape of the thumb.
         * @param thumbElevation Shadow under a fill thumb.
         * @param thumbThickness Thickness of an underline thumb.
         * @param thumbLength Length of an underline thumb, or [Dp.Unspecified] for the option's.
         * @param thumbPadding Space between the chosen option's bounds and the thumb.
         * @param thumbEdge Edge an underline thumb and the divider run along.
         * @param divider Line running the selector's length.
         * @param dividerThickness Thickness of the divider.
         * @param itemShape Shape of an option's interaction feedback.
         * @param indication Feedback an option draws for press, hover and focus.
         * @param motion How the thumb moves.
         * @return Style built from the supplied values.
         */
        @Composable
        public fun default(
            content: Color = ArecaTheme.colors.fgMuted,
            contentSelected: Color = ArecaTheme.colors.fgBase,
            contentDisabled: Color = ArecaTheme.colors.fgDisabled,
            track: Color = ArecaTheme.colors.bgNeutralMuted,
            trackBorder: Color = Color.Transparent,
            trackShape: Shape = ArecaTheme.shapes.full,
            trackBorderWidth: Dp = 1.dp,
            trackPadding: Dp = ArecaTheme.spacing.xs2,
            // Raised means lighter in both themes: white on the light track, and a step lighter than
            // the track in dark, where the surface colour sits darker than it.
            thumb: Color = if (ArecaTheme.isDark) ArecaTheme.colors.bgNeutralBase else ArecaTheme.colors.bgSurface,
            thumbKind: SelectorThumbKind = SelectorThumbKind.Fill,
            thumbShape: Shape = ArecaTheme.shapes.full,
            thumbElevation: Dp = ArecaTheme.elevation.xs,
            thumbThickness: Dp = 2.dp,
            thumbLength: Dp = Dp.Unspecified,
            thumbPadding: PaddingValues = PaddingValues(0.dp),
            thumbEdge: SelectorEdge = SelectorEdge.End,
            divider: Color = Color.Transparent,
            dividerThickness: Dp = 1.dp,
            itemShape: Shape = ArecaTheme.shapes.full,
            indication: Indication = TouchIndication(shape = itemShape, color = contentSelected),
            motion: FiniteAnimationSpec<Float> = ArecaTheme.motion.slide(),
        ): SlidingSelectorStyle = SlidingSelectorStyle(
            content = content,
            contentSelected = contentSelected,
            contentDisabled = contentDisabled,
            track = track,
            trackBorder = trackBorder,
            trackShape = trackShape,
            trackBorderWidth = trackBorderWidth,
            trackPadding = trackPadding,
            thumb = thumb,
            thumbKind = thumbKind,
            thumbShape = thumbShape,
            thumbElevation = thumbElevation,
            thumbThickness = thumbThickness,
            thumbLength = thumbLength,
            thumbPadding = thumbPadding,
            thumbEdge = thumbEdge,
            divider = divider,
            dividerThickness = dividerThickness,
            itemShape = itemShape,
            indication = indication,
            motion = motion,
        )
    }
}

/** Default [SlidingSelectorStyle] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalSlidingSelectorStyle: ProvidableCompositionLocal<SlidingSelectorStyle?> =
    compositionLocalOf { null }
