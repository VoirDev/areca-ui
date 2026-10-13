package dev.voir.areca.flow.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.painter.Painter

/**
 * What an avatar's picture is doing, as far as drawing it is concerned.
 *
 * Three states because an avatar has three appearances and they must be told apart on sight:
 * initials under a spinner, the picture, and initials alone.
 */
@Immutable
public sealed interface AvatarImageState {
    /** Requested and not yet resolved. The avatar draws its initials under its spinner. */
    public data object Loading : AvatarImageState

    /**
     * Ready to draw.
     *
     * @property painter Picture, drawn cropped to the avatar's shape.
     */
    public data class Ready(public val painter: Painter) : AvatarImageState

    /**
     * Given up on. The avatar draws its initials.
     *
     * Never an empty circle: an avatar with nothing in it reads as a picture still arriving, for as
     * long as it is on screen.
     */
    public data object Failed : AvatarImageState
}

/**
 * Turns an application's picture model into something an avatar can draw.
 *
 * The module has no image-loading library and takes none on: an application that uses Coil should
 * not be handed a second loader, and one that does not should not be handed the first. The
 * application implements this once — usually in a few lines over its own loader — and provides it
 * through [LocalAvatarImageLoader]. See the module README for a Coil implementation.
 */
@Stable
public interface AvatarImageLoader {
    /**
     * Starts or resumes loading [model] and reports where it has got to.
     *
     * Called during composition, once per avatar per recomposition, so an implementation should
     * remember its work keyed on [model] rather than restart it.
     *
     * @param model Picture model from [ProfileSummary.avatar], never `null`.
     * @return What the avatar should draw right now.
     */
    @Composable
    public fun load(model: Any): AvatarImageState

    /**
     * The loader in scope when the application provides none.
     *
     * Answers [AvatarImageState.Failed] for every model, so an application with no pictures yet
     * draws initials rather than a spinner that never stops.
     */
    public data object None : AvatarImageLoader {
        /**
         * Reports failure without looking at [model].
         *
         * @param model Picture model, ignored.
         * @return [AvatarImageState.Failed], whatever [model] is.
         */
        @Composable
        override fun load(model: Any): AvatarImageState = AvatarImageState.Failed
    }
}

/**
 * Image loader in scope for every part that draws an avatar.
 *
 * Static because an application provides it once, above everything, and never changes it.
 * Unprovided, it resolves to [AvatarImageLoader.None].
 */
public val LocalAvatarImageLoader: ProvidableCompositionLocal<AvatarImageLoader> =
    staticCompositionLocalOf { AvatarImageLoader.None }
