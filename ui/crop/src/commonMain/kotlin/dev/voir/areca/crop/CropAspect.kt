package dev.voir.areca.crop

import androidx.compose.runtime.Immutable

/**
 * The proportions a crop frame keeps.
 *
 * An aspect says only what shape of rectangle is cut out. What the cut-out is masked to — a circle,
 * a rounded square — is a separate choice, [CropSpec.mask], so a circle over [Square] is an avatar and
 * the same circle over a 16:9 ratio is an ellipse.
 */
@Immutable
public sealed interface CropAspect {

    /** Any proportions: the frame's corners and edges drag freely. */
    public data object Free : CropAspect

    /**
     * The photo's own proportions, following it through quarter turns: a landscape photo turned
     * once gives a portrait frame.
     */
    public data object Original : CropAspect

    /**
     * Fixed proportions of [width] to [height].
     *
     * Only the ratio matters — `Ratio(4, 3)` and `Ratio(8, 6)` cut the same frame — but the numbers
     * are kept as given because a toolbar shows them as the chip's label.
     *
     * @property width Horizontal part of the ratio. Positive.
     * @property height Vertical part of the ratio. Positive.
     */
    public data class Ratio(
        public val width: Int,
        public val height: Int,
    ) : CropAspect {
        init {
            require(width > 0 && height > 0) { "A crop ratio needs positive sides, was $width:$height." }
        }

        /** Width over height. */
        val value: Float get() = width.toFloat() / height

        /** Whether both sides are equal, so swapping orientation would change nothing. */
        val isSquare: Boolean get() = width == height
    }

    /** Common aspects. */
    public companion object {
        /** 1:1, the proportion of an avatar or a grid tile. */
        public val Square: Ratio = Ratio(1, 1)

        /**
         * The set a general photo editor offers: free, the photo's own, and the common print and
         * screen ratios. [CropSpec.Photo] offers it; pass it as [CropSpec.aspects] in a spec of your
         * own.
         */
        public val PhotoPresets: List<CropAspect> = listOf(
            Free,
            Original,
            Square,
            Ratio(4, 5),
            Ratio(4, 3),
            Ratio(3, 2),
            Ratio(16, 9),
        )
    }
}
