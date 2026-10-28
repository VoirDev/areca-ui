package dev.voir.areca.media

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.painter.Painter

/**
 * What a picture is doing, as far as drawing it is concerned.
 *
 * A frame draws a face for each: a skeleton or a blurred stand-in while [Loading], the picture when
 * [Ready], a quiet mark and a retry when [Failed].
 */
@Immutable
public sealed interface MediaImageState {
    /** Requested and not yet decoded. */
    public data object Loading : MediaImageState

    /**
     * Ready to draw.
     *
     * @property painter The picture. Its intrinsic size is what zoom fits and clamps against.
     */
    public data class Ready(public val painter: Painter) : MediaImageState

    /** Given up on: not a picture this platform can decode, unreachable, or refused. */
    public data object Failed : MediaImageState
}

/**
 * Turns an application's picture model into something a frame can draw.
 *
 * The module has no image-loading library and takes none on: an application that uses Coil should
 * not be handed a second loader, and one that does not should not be handed the first. Implement
 * this once over your own loader and provide it through [LocalMediaImageLoader]. The module README
 * has a Coil implementation to copy.
 */
@Stable
public interface MediaImageLoader {
    /**
     * Starts or resumes loading [model], drawn whole inside a frame, and reports where it has got to.
     *
     * Called during composition, once per picture per recomposition, so an implementation should
     * remember its work keyed on [model] and [maxSize] rather than restart it. A frame retries a
     * failed picture by calling this from a fresh composition, so work remembered in composition is
     * started again.
     *
     * @param model The application's picture model, never `null`.
     * @param maxSize Longest edge, in pixels, the picture is drawn at. Decode no larger.
     * @return What to draw right now.
     */
    @Composable
    public fun load(model: Any, maxSize: Int): MediaImageState

    /**
     * Starts or resumes loading [model] to cover a square of [side] pixels, cropped — a thumbnail.
     *
     * A cropped picture has to cover the square: its shorter edge must reach [side]. With Coil, that
     * is `.size(side).scale(Scale.FILL)`. The default asks [load] for twice [side] on the long edge,
     * which covers any picture up to twice as long as it is wide.
     *
     * @param model The application's picture model, never `null`.
     * @param side Side, in pixels, of the square the picture is cropped into.
     * @return What to draw right now.
     */
    @Composable
    public fun loadSquare(model: Any, side: Int): MediaImageState =
        load(model = model, maxSize = side * SQUARE_FALLBACK_RATIO)

    /**
     * The loader in scope when the application provides none: every picture fails, so a screen
     * with no loader yet shows its failed faces rather than a skeleton that never resolves.
     */
    public data object None : MediaImageLoader {
        /**
         * Reports failure without looking at [model].
         *
         * @param model Picture model, ignored.
         * @param maxSize Longest edge asked for, ignored.
         * @return [MediaImageState.Failed].
         */
        @Composable
        override fun load(model: Any, maxSize: Int): MediaImageState = MediaImageState.Failed
    }
}

/**
 * Image loader in scope for every part that draws a picture from a model.
 *
 * Static because an application provides it once, above everything, and never changes it.
 * Unprovided, it resolves to [MediaImageLoader.None].
 */
public val LocalMediaImageLoader: ProvidableCompositionLocal<MediaImageLoader> =
    staticCompositionLocalOf { MediaImageLoader.None }

/** Longest edge [MediaImageLoader.loadSquare] asks [MediaImageLoader.load] for, as a multiple of the side. */
private const val SQUARE_FALLBACK_RATIO = 2
