package dev.voir.areca.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Raw color palette the theme is built from.
 *
 * Each ramp runs from the lightest step (`10`) to the darkest (`100`). Steps are not a uniform
 * lightness interval — the middle of each ramp is where usable fills and foregrounds live, so it is
 * sampled more finely than the ends.
 *
 * The ramps carry no theme direction of their own: [ArecaColors.light] reads them from the light end
 * and [ArecaColors.dark] from the dark end, so replacing this palette re-themes both directions at
 * once. That is the intended extension point — supply an [ArecaBaseColors] with brand ramps and both
 * themes follow, rather than overriding semantic tokens one by one.
 *
 * Read a step directly only when no semantic token in [ArecaColors] expresses the intent, such as a
 * chart series or an illustration. Component code should read [ArecaColors].
 */
@Immutable
public data class ArecaBaseColors(
    // BASE — absolute endpoints, unaffected by theme direction.
    /** Pure white, whatever the theme direction. */
    public val baseWhite: Color = Color(0xFFFFFFFF),

    /** Pure black, whatever the theme direction. */
    public val baseBlack: Color = Color(0xFF000000),

    // BRAND — product identity. Drives accents, primary actions, and selection.
    /** Brand ramp, step 10, the lightest. */
    public val brand10: Color = Color(0xFFF6F5FF),

    /** Brand ramp, step 20. */
    public val brand20: Color = Color(0xFFF0EDFF),

    /** Brand ramp, step 30. */
    public val brand30: Color = Color(0xFFDBD4FF),

    /** Brand ramp, step 40. */
    public val brand40: Color = Color(0xFFB4A6FF),

    /** Brand ramp, step 50. */
    public val brand50: Color = Color(0xFF907AFF),

    /** Brand ramp, step 60. */
    public val brand60: Color = Color(0xFF7257FF),

    /** Brand ramp, step 70. */
    public val brand70: Color = Color(0xFF5336E2),

    /** Brand ramp, step 80. */
    public val brand80: Color = Color(0xFF34228F),

    /** Brand ramp, step 90. */
    public val brand90: Color = Color(0xFF291F61),

    /** Brand ramp, step 100, the darkest. */
    public val brand100: Color = Color(0xFF130D33),

    // NEUTRAL — surfaces, text, and borders. The ramp the theme direction inverts.
    /** Neutral ramp, step 10, the lightest. */
    public val neutral10: Color = Color(0xFFF7F9FA),

    /**
     * Neutral ramp, step 15: a half step between [neutral10] and [neutral20].
     *
     * The light theme's grouped-region fill. [neutral10] sits too close to white for a settings card
     * to read as a surface on the canvas, and [neutral20] is already the resting border and the
     * inset well, so the region takes a step of its own.
     */
    public val neutral15: Color = Color(0xFFF2F4F5),

    /** Neutral ramp, step 20. */
    public val neutral20: Color = Color(0xFFF0F2F3),

    /** Neutral ramp, step 30. */
    public val neutral30: Color = Color(0xFFE5E8E9),

    /** Neutral ramp, step 40. */
    public val neutral40: Color = Color(0xFFCED1D3),

    /** Neutral ramp, step 50. */
    public val neutral50: Color = Color(0xFF9EA2A4),

    /** Neutral ramp, step 60. */
    public val neutral60: Color = Color(0xFF727679),

    /** Neutral ramp, step 70. */
    public val neutral70: Color = Color(0xFF505457),

    /** Neutral ramp, step 80. */
    public val neutral80: Color = Color(0xFF343738),

    /** Neutral ramp, step 90. */
    public val neutral90: Color = Color(0xFF1F2224),

    /** Neutral ramp, step 100, the darkest. */
    public val neutral100: Color = Color(0xFF131214),

    // GREEN — success and positive change.
    /** Green ramp, step 10, the lightest. */
    public val green10: Color = Color(0xFFE8FAF0),

    /** Green ramp, step 20. */
    public val green20: Color = Color(0xFFD7F5EF),

    /** Green ramp, step 30. */
    public val green30: Color = Color(0xFF9BEBBF),

    /** Green ramp, step 40. */
    public val green40: Color = Color(0xFF51C285),

    /** Green ramp, step 50. */
    public val green50: Color = Color(0xFF23A15D),

    /** Green ramp, step 60. */
    public val green60: Color = Color(0xFF008557),

    /** Green ramp, step 70. */
    public val green70: Color = Color(0xFF006341),

    /** Green ramp, step 80. */
    public val green80: Color = Color(0xFF0D4F2B),

    /** Green ramp, step 90. */
    public val green90: Color = Color(0xFF05381D),

    /** Green ramp, step 100, the darkest. */
    public val green100: Color = Color(0xFF021F10),

    // BLUE — information and neutral emphasis.
    /** Blue ramp, step 10, the lightest. */
    public val blue10: Color = Color(0xFFF2F7FF),

    /** Blue ramp, step 20. */
    public val blue20: Color = Color(0xFFE5F0FF),

    /** Blue ramp, step 30. */
    public val blue30: Color = Color(0xFFC2DCFF),

    /** Blue ramp, step 40. */
    public val blue40: Color = Color(0xFF75B1FF),

    /** Blue ramp, step 50. */
    public val blue50: Color = Color(0xFF308AFF),

    /** Blue ramp, step 60. */
    public val blue60: Color = Color(0xFF0A69FA),

    /** Blue ramp, step 70. */
    public val blue70: Color = Color(0xFF0050C7),

    /** Blue ramp, step 80. */
    public val blue80: Color = Color(0xFF003C94),

    /** Blue ramp, step 90. */
    public val blue90: Color = Color(0xFF042961),

    /** Blue ramp, step 100, the darkest. */
    public val blue100: Color = Color(0xFF021026),

    // YELLOW — warnings. Lighter than the other ramps at equal steps, so its fills take a dark
    // foreground where the others take white.
    /** Yellow ramp, step 10, the lightest. */
    public val yellow10: Color = Color(0xFFFFF9E6),

    /** Yellow ramp, step 20. */
    public val yellow20: Color = Color(0xFFFFEFB3),

    /** Yellow ramp, step 30. */
    public val yellow30: Color = Color(0xFFFFD84D),

    /** Yellow ramp, step 40. */
    public val yellow40: Color = Color(0xFFED9B16),

    /** Yellow ramp, step 50. */
    public val yellow50: Color = Color(0xFFD67507),

    /** Yellow ramp, step 60. */
    public val yellow60: Color = Color(0xFFB26205),

    /** Yellow ramp, step 70. */
    public val yellow70: Color = Color(0xFF824B0D),

    /** Yellow ramp, step 80. */
    public val yellow80: Color = Color(0xFF663C0C),

    /** Yellow ramp, step 90. */
    public val yellow90: Color = Color(0xFF4D2B05),

    /** Yellow ramp, step 100, the darkest. */
    public val yellow100: Color = Color(0xFF331C03),

    // RED — errors and destructive actions.
    /** Red ramp, step 10, the lightest. */
    public val red10: Color = Color(0xFFFFF3F0),

    /** Red ramp, step 20. */
    public val red20: Color = Color(0xFFFFE9E3),

    /** Red ramp, step 30. */
    public val red30: Color = Color(0xFFFFCEC2),

    /** Red ramp, step 40. */
    public val red40: Color = Color(0xFFFF9175),

    /** Red ramp, step 50. */
    public val red50: Color = Color(0xFFFF5226),

    /** Red ramp, step 60. */
    public val red60: Color = Color(0xFFDB340B),

    /** Red ramp, step 70. */
    public val red70: Color = Color(0xFFAD1D00),

    /** Red ramp, step 80. */
    public val red80: Color = Color(0xFF8A1700),

    /** Red ramp, step 90. */
    public val red90: Color = Color(0xFF611000),

    /** Red ramp, step 100, the darkest. */
    public val red100: Color = Color(0xFF290800),
)

/** Raw palette in scope. Read it through [ArecaTheme.baseColors]. */
internal val LocalBaseColors = staticCompositionLocalOf { ArecaBaseColors() }
