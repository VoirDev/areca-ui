package dev.voir.areca.foundation.notice

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import dev.voir.areca.foundation.icon.FeaturedIconSize
import dev.voir.areca.foundation.icon.IconSize
import dev.voir.areca.theme.ArecaTheme

/**
 * Metrics and typography of a [Notice].
 *
 * Three gaps rather than one: [iconGap] parts the mark from the copy, [textGap] hangs the first line
 * under the title, and [lineGap] holds the lines together. They are different distances because they
 * separate different things — the mark is beside the message, the title is its first line.
 *
 * @property padding Space between the block's edges and its content.
 * @property iconGap Space between the mark and the copy, below it or after it.
 * @property textGap Space between the title and the first line.
 * @property lineGap Space between lines.
 * @property chip Size of the mark of a titled notice, drawn as a chip.
 * @property glyph Size of the mark of an untitled notice, drawn bare.
 * @property iconPlacement Where the mark sits; `null` puts it on top of a titled notice and at the
 * start of an untitled one.
 * @property titleTextStyle Style of the title.
 * @property bodyTextStyle Style of an ordinary line.
 * @property quietTextStyle Style of the quietest line.
 */
@Immutable
public data class NoticeSize(
    public val padding: PaddingValues,
    public val iconGap: Dp,
    public val textGap: Dp,
    public val lineGap: Dp,
    public val chip: FeaturedIconSize,
    public val glyph: Dp,
    public val iconPlacement: NoticeIconPlacement?,
    public val titleTextStyle: TextStyle,
    public val bodyTextStyle: TextStyle,
    public val quietTextStyle: TextStyle,
) {
    /** The theme's default [NoticeSize], and [default] for building a variation on it. */
    public companion object {
        /** Theme default. */
        public val Default: NoticeSize
            @Composable
            get() = default()

        /**
         * Builds a size from the current theme.
         *
         * @param padding Space between the block's edges and its content.
         * @param iconGap Space between the mark and the copy.
         * @param textGap Space between the title and the first line.
         * @param lineGap Space between lines.
         * @param chip Size of the mark of a titled notice.
         * @param glyph Size of the mark of an untitled notice.
         * @param iconPlacement Where the mark sits; `null` decides by whether there is a title.
         * @param titleTextStyle Style of the title.
         * @param bodyTextStyle Style of an ordinary line.
         * @param quietTextStyle Style of the quietest line.
         * @return Size for a notice.
         */
        @Composable
        public fun default(
            padding: PaddingValues = PaddingValues(ArecaTheme.spacing.xl),
            iconGap: Dp = ArecaTheme.spacing.lg,
            textGap: Dp = ArecaTheme.spacing.xs,
            lineGap: Dp = ArecaTheme.spacing.xs,
            chip: FeaturedIconSize = FeaturedIconSize.Small,
            glyph: Dp = IconSize.Medium,
            iconPlacement: NoticeIconPlacement? = null,
            titleTextStyle: TextStyle = ArecaTheme.typography.labelMedium,
            bodyTextStyle: TextStyle = ArecaTheme.typography.bodySmall,
            quietTextStyle: TextStyle = ArecaTheme.typography.bodyExtraSmall,
        ): NoticeSize = NoticeSize(
            padding = padding,
            iconGap = iconGap,
            textGap = textGap,
            lineGap = lineGap,
            chip = chip,
            glyph = glyph,
            iconPlacement = iconPlacement,
            titleTextStyle = titleTextStyle,
            bodyTextStyle = bodyTextStyle,
            quietTextStyle = quietTextStyle,
        )
    }
}

/** Default [NoticeSize] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalNoticeSize: ProvidableCompositionLocal<NoticeSize?> = compositionLocalOf { null }
