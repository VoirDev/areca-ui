package dev.voir.areca.foundation.status

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * What a [StatusMark] is showing: work in progress, or how it ended.
 *
 * The three are one object in three states rather than three illustrations — the same disc, in the
 * same place, at the same size — because the only thing that changes is whether the work is still
 * going and how it went.
 */
@Immutable
public sealed interface StatusMarkState {
    /**
     * The work is running.
     *
     * @property icon Glyph inside the spinning arc, naming the work — a file, a cloud — or `null`
     * for the arc alone.
     */
    public data class Working(public val icon: ImageVector? = null) : StatusMarkState

    /** The work finished as asked. */
    public data object Success : StatusMarkState

    /**
     * The work stopped before it finished — failed, cancelled, or cut short.
     *
     * @property icon Glyph saying so, normally a warning.
     */
    public data class Stopped(public val icon: ImageVector) : StatusMarkState
}
