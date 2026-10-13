package dev.voir.areca.foundation.status

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import dev.voir.areca.theme.ArecaTheme

/**
 * Colors of a [StatusScene]'s copy.
 *
 * The message and footnote are the secondary step rather than the muted one: they carry the facts —
 * counts, what was skipped — and the muted step is documented as never being the only thing saying
 * something. Hierarchy between them is made with size instead. Only the note, which is advice, is
 * muted.
 *
 * @property title Color of the title.
 * @property message Color of the message and the footnote.
 * @property note Color of the note.
 */
@Immutable
public data class StatusSceneStyle(
    public val title: Color,
    public val message: Color,
    public val note: Color,
) {
    /** The theme's default [StatusSceneStyle], and [default] for building a variation on it. */
    public companion object {
        /** Theme default. */
        public val Default: StatusSceneStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme.
         *
         * @param title Color of the title.
         * @param message Color of the message and the footnote.
         * @param note Color of the note.
         * @return Style for a scene.
         */
        @Composable
        public fun default(
            title: Color = ArecaTheme.colors.fgBase,
            message: Color = ArecaTheme.colors.fgSubtle,
            note: Color = ArecaTheme.colors.fgMuted,
        ): StatusSceneStyle = StatusSceneStyle(
            title = title,
            message = message,
            note = note,
        )
    }
}

/** Default [StatusSceneStyle] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalStatusSceneStyle: ProvidableCompositionLocal<StatusSceneStyle?> =
    compositionLocalOf { null }
