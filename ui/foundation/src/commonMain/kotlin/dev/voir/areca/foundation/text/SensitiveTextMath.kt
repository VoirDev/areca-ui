package dev.voir.areca.foundation.text

import kotlin.math.abs

/**
 * The arithmetic behind [SensitiveText]'s veil, kept apart from the drawing that uses it.
 *
 * Separated for the reason [dev.voir.areca.foundation.topbar.TopBarScrollMath] is: how a reveal is
 * paced is a rule, and a rule reads and tests better as a plain function than as an expression
 * buried in a draw scope. Everything here works in fractions rather than pixels, so none of it
 * depends on the density or the type size the text happens to be drawn at.
 */
internal object SensitiveTextMath {
    /**
     * How far either side of its own phase a spark is visible, in units of reveal progress.
     *
     * Wide enough that neighbouring sparks overlap into a continuous wave rather than firing as
     * separate blinks, and narrow enough that the wave still has a front to follow.
     */
    const val SPARK_WINDOW = 0.36f

    /**
     * Progress at which the text starts fading in where the platform cannot blur.
     *
     * The veil is opaque on that path, so the glyphs it hides are only safe while it is still
     * covering them. Holding the text back until the veil is mostly gone keeps the crossfade from
     * showing a legible value through a half-faded veil.
     */
    const val CONTENT_FADE_START = 0.55f

    /** Odd 32-bit fraction of the golden ratio, `0x9E3779B9`, which scatters sequential seeds. */
    private const val GOLDEN_RATIO_ODD = -1640531527

    /** First mixing constant of the MurmurHash3 finalizer, `0x85EBCA6B`. */
    private const val MURMUR_MIX_1 = -2048144789

    /** Second mixing constant of the MurmurHash3 finalizer, `0xC2B2AE35`. */
    private const val MURMUR_MIX_2 = -1028477387

    /** Number of distinct values the top 24 bits of a hash produce, which the result is scaled by. */
    private const val MANTISSA_SCALE = 16_777_216f

    /**
     * A stable pseudo-random fraction for [seed].
     *
     * Sparks are placed with this rather than with [kotlin.random.Random] so that a given text draws
     * the same scatter on every frame, on every platform, and in every screenshot — a fresh random
     * number per frame would make them jitter instead of twinkle. Integer arithmetic wraps
     * identically on every Kotlin target, so the same seed gives the same fraction everywhere.
     *
     * @param seed Any integer. Sequential seeds are scattered, not adjacent.
     * @return A fraction in `[0, 1)`.
     */
    fun noise(seed: Int): Float {
        var hash = seed * GOLDEN_RATIO_ODD
        hash = hash xor (hash ushr 15)
        hash *= MURMUR_MIX_1
        hash = hash xor (hash ushr 13)
        hash *= MURMUR_MIX_2
        hash = hash xor (hash ushr 16)
        return (hash ushr 8) / MANTISSA_SCALE
    }

    /**
     * Opacity of one spark at the current point of a reveal.
     *
     * A spark's [phase] is its position along the text, so the sparks brighten and fade in the order
     * they are written in and the burst reads as a wave crossing the line rather than as every spark
     * flashing at once. Concealing runs [progress] the other way, which sends the wave back.
     *
     * @param progress Reveal progress, `0` fully concealed and `1` fully revealed.
     * @param phase Progress at which this spark peaks, normally its fraction along the line.
     * @param window Half-width of the spark's visible span, in progress.
     * @return Opacity in `[0, 1]`. Zero at rest, so a settled state draws no sparks at all.
     */
    fun sparkAlpha(progress: Float, phase: Float, window: Float = SPARK_WINDOW): Float {
        if (progress <= 0f || progress >= 1f || window <= 0f) return 0f
        return (1f - abs(progress - phase) / window).coerceIn(0f, 1f)
    }

    /**
     * Opacity the text is drawn at where the platform cannot blur it.
     *
     * Where it can, the text stays fully drawn throughout and the blur does the hiding. Where it
     * cannot, this is what stands in for that blur: the value is held invisible until
     * [CONTENT_FADE_START] and only then fades up, so it never sits legible under a half-gone veil.
     *
     * @param progress Reveal progress, `0` fully concealed and `1` fully revealed.
     * @return Opacity in `[0, 1]`.
     */
    fun contentAlpha(progress: Float): Float =
        ((progress - CONTENT_FADE_START) / (1f - CONTENT_FADE_START)).coerceIn(0f, 1f)

    /**
     * Horizontal centre of the shimmer band travelling across a concealed text.
     *
     * The band starts and finishes a full band-width clear of the text, so each cycle begins and
     * ends with no highlight on screen instead of snapping back through one.
     *
     * @param progress Shimmer progress within one cycle, in `[0, 1]`.
     * @param width Width of the text, in pixels.
     * @param bandWidth Half-width of the band, in pixels.
     * @return Centre of the band, in pixels from the text's left edge. Negative before it arrives.
     */
    fun shimmerCenter(progress: Float, width: Float, bandWidth: Float): Float =
        -bandWidth + (width + bandWidth * 2f) * progress
}
