package dev.voir.areca.foundation.status

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import dev.voir.areca.theme.ArecaTheme

/**
 * Metrics and typography of a [StatusScene].
 *
 * The gaps grow down the scene on purpose: the lines of copy are one block to read and sit close,
 * the actions are a different kind of thing and get a section's worth of air, and the note under
 * them is an afterthought set a little apart.
 *
 * @property markGap Space between the mark and the title.
 * @property textInset Extra horizontal inset of the copy, past whatever the scene is placed in. A
 * centred paragraph that reaches the edges has no shape to read; the measure is what makes the
 * block look placed rather than poured in.
 * @property lineGap Space between the title, the message and the footnote.
 * @property footnoteGap Extra space above the footnote, on top of [lineGap].
 * @property actionsGap Space above the actions.
 * @property noteGap Space above the note.
 * @property titleTextStyle Style of the title.
 * @property messageTextStyle Style of the message.
 * @property footnoteTextStyle Style of the footnote.
 * @property noteTextStyle Style of the note.
 */
@Immutable
public data class StatusSceneSize(
    public val markGap: Dp,
    public val textInset: Dp,
    public val lineGap: Dp,
    public val footnoteGap: Dp,
    public val actionsGap: Dp,
    public val noteGap: Dp,
    public val titleTextStyle: TextStyle,
    public val messageTextStyle: TextStyle,
    public val footnoteTextStyle: TextStyle,
    public val noteTextStyle: TextStyle,
) {
    /** The theme's default [StatusSceneSize], and [default] for building a variation on it. */
    public companion object {
        /** Theme default. */
        public val Default: StatusSceneSize
            @Composable
            get() = default()

        /**
         * Builds a size from the current theme.
         *
         * @param markGap Space between the mark and the title.
         * @param textInset Extra horizontal inset of the copy.
         * @param lineGap Space between the title, the message and the footnote.
         * @param footnoteGap Extra space above the footnote.
         * @param actionsGap Space above the actions.
         * @param noteGap Space above the note.
         * @param titleTextStyle Style of the title.
         * @param messageTextStyle Style of the message.
         * @param footnoteTextStyle Style of the footnote.
         * @param noteTextStyle Style of the note.
         * @return Size for a scene.
         */
        @Composable
        public fun default(
            markGap: Dp = ArecaTheme.spacing.xl3,
            textInset: Dp = ArecaTheme.spacing.xl3,
            lineGap: Dp = ArecaTheme.spacing.xs,
            footnoteGap: Dp = ArecaTheme.spacing.xs2,
            actionsGap: Dp = ArecaTheme.spacing.xl4,
            noteGap: Dp = ArecaTheme.spacing.xl2,
            titleTextStyle: TextStyle = ArecaTheme.typography.headingSmall,
            messageTextStyle: TextStyle = ArecaTheme.typography.bodyMedium,
            footnoteTextStyle: TextStyle = ArecaTheme.typography.bodySmall,
            noteTextStyle: TextStyle = ArecaTheme.typography.bodySmall,
        ): StatusSceneSize = StatusSceneSize(
            markGap = markGap,
            textInset = textInset,
            lineGap = lineGap,
            footnoteGap = footnoteGap,
            actionsGap = actionsGap,
            noteGap = noteGap,
            titleTextStyle = titleTextStyle,
            messageTextStyle = messageTextStyle,
            footnoteTextStyle = footnoteTextStyle,
            noteTextStyle = noteTextStyle,
        )
    }
}

/** Default [StatusSceneSize] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalStatusSceneSize: ProvidableCompositionLocal<StatusSceneSize?> =
    compositionLocalOf { null }
