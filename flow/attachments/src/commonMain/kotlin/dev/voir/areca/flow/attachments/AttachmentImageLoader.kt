package dev.voir.areca.flow.attachments

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.painter.Painter
import dev.voir.areca.media.LocalMediaImageLoader
import dev.voir.areca.media.MediaImageLoader
import dev.voir.areca.media.MediaImageState

/**
 * What an attachment's picture is doing, as far as drawing it is concerned.
 *
 * A square draws its kind of file in every state but [Ready], so a picture that is slow, or one that
 * cannot be decoded, still says what the file is rather than leaving an empty square.
 */
@Deprecated(
    "Attachments load through media's loader. Use MediaImageState.",
    ReplaceWith("MediaImageState", "dev.voir.areca.media.MediaImageState"),
)
@Immutable
public sealed interface AttachmentImageState {
    /** Requested and not yet decoded. */
    public data object Loading : AttachmentImageState

    /**
     * Ready to draw.
     *
     * @property painter The picture, drawn cropped in a square and fitted in the viewer.
     */
    public data class Ready(public val painter: Painter) : AttachmentImageState

    /** Given up on: the bytes are not a picture this platform can decode, or cannot be read. */
    public data object Failed : AttachmentImageState
}

/**
 * Turns an application's file model into a picture a square or the viewer can draw.
 *
 * The module has no image-loading library and takes none on: an application that uses Coil should
 * not be handed a second loader, and one that does not should not be handed the first. The
 * application implements this once over its own loader and provides it through
 * [LocalAttachmentImageLoader]. See the module README for a Coil implementation.
 */
@Deprecated(
    "Attachments load through media's loader, shared with every picture. Implement MediaImageLoader and provide LocalMediaImageLoader.",
    ReplaceWith("MediaImageLoader", "dev.voir.areca.media.MediaImageLoader"),
)
@Suppress("DEPRECATION")
@Stable
public interface AttachmentImageLoader {
    /**
     * Starts or resumes loading [model] and reports where it has got to.
     *
     * Called during composition, once per picture per recomposition, so an implementation should
     * remember its work keyed on [model] and [maxSize] rather than restart it.
     *
     * Asked for a picture drawn whole, fitted inside its frame — the viewer's — so the longest
     * edge is the one that has to fit. [loadSquare] asks for a picture cropped into a square.
     *
     * @param model File model from [AttachmentItem.model], never `null`.
     * @param maxSize Longest edge, in pixels, the picture is drawn at. Decode no larger: a picture
     *   from a file picker can be thousands of pixels on its long edge.
     * @return What to draw right now.
     */
    @Composable
    public fun load(model: Any, maxSize: Int): AttachmentImageState

    /**
     * Starts or resumes loading [model] to fill a square of [side] pixels, cropped.
     *
     * A cropped picture has to **cover** the square: its shorter edge must reach [side], so a 4:3
     * photograph is decoded to `side × 4/3` on its long edge rather than to [side]. Decode no larger
     * than that covering size: a strip of squares holds many photographs. With Coil, that is
     * `.size(side).scale(Scale.FILL)`.
     *
     * Called during composition like [load], with the same obligation to remember the work keyed on
     * [model] and [side]. The default asks [load] for twice [side] on the long edge, which covers
     * every picture up to twice as long as it is wide; override it to ask for the exact covering
     * size.
     *
     * @param model File model from [AttachmentItem.model], never `null`.
     * @param side Side, in pixels, of the square the picture is cropped into.
     * @return What to draw right now.
     */
    @Composable
    public fun loadSquare(model: Any, side: Int): AttachmentImageState =
        load(model = model, maxSize = side * SQUARE_FALLBACK_RATIO)

    /**
     * The loader in scope when the application provides none.
     *
     * Answers [AttachmentImageState.Failed] for every model, so an application with no loader yet
     * draws each file as its kind of file rather than a spinner that never stops.
     */
    public data object None : AttachmentImageLoader {
        /**
         * Reports failure without looking at [model].
         *
         * @param model File model, ignored.
         * @param maxSize Longest edge asked for, ignored.
         * @return [AttachmentImageState.Failed], whatever [model] is.
         */
        @Composable
        override fun load(model: Any, maxSize: Int): AttachmentImageState = AttachmentImageState.Failed
    }
}

/**
 * Image loader in scope for every part that draws an attachment's picture.
 *
 * Static because an application provides it once, above everything, and never changes it.
 * Unprovided, it resolves to [AttachmentImageLoader.None].
 */
@Deprecated(
    "Provide LocalMediaImageLoader. Attachments read it first and fall back to this local while it is unprovided.",
    ReplaceWith("LocalMediaImageLoader", "dev.voir.areca.media.LocalMediaImageLoader"),
)
@Suppress("DEPRECATION")
public val LocalAttachmentImageLoader: ProvidableCompositionLocal<AttachmentImageLoader> =
    staticCompositionLocalOf { AttachmentImageLoader.None }

/**
 * Longest edge [AttachmentImageLoader.loadSquare] asks [AttachmentImageLoader.load] for, by default,
 * as a multiple of the square's side: enough to cover the square with any picture up to 2:1.
 */
private const val SQUARE_FALLBACK_RATIO = 2

/**
 * The loader attachments draw pictures with: [LocalMediaImageLoader] when the application provides
 * it, else the deprecated [LocalAttachmentImageLoader] behind media's interface.
 *
 * @return The loader.
 */
@Composable
internal fun attachmentPictureLoader(): MediaImageLoader {
    val media = LocalMediaImageLoader.current
    return if (media != MediaImageLoader.None) media else DeprecatedLoader
}

/** The deprecated [LocalAttachmentImageLoader], read through media's interface. */
@Suppress("DEPRECATION")
private object DeprecatedLoader : MediaImageLoader {
    @Composable
    override fun load(model: Any, maxSize: Int): MediaImageState =
        LocalAttachmentImageLoader.current.load(model = model, maxSize = maxSize).toMedia()

    @Composable
    override fun loadSquare(model: Any, side: Int): MediaImageState =
        LocalAttachmentImageLoader.current.loadSquare(model = model, side = side).toMedia()
}

/** @return The same state as media's. */
@Suppress("DEPRECATION")
private fun AttachmentImageState.toMedia(): MediaImageState = when (this) {
    AttachmentImageState.Loading -> MediaImageState.Loading
    is AttachmentImageState.Ready -> MediaImageState.Ready(painter)
    AttachmentImageState.Failed -> MediaImageState.Failed
}
