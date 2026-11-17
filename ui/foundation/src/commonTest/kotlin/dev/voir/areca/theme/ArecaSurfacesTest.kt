package dev.voir.areca.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import kotlin.math.max
import kotlin.math.min
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * The surface ladders hold in every layout and both themes: each surface is told apart from what is
 * laid on it, set into it and drawn across it, and the theme's text reads on it.
 */
class ArecaSurfacesTest {
    private val base = ArecaBaseColors()

    private fun cases(): List<Triple<String, ArecaSurfaces, ArecaColors>> =
        SurfaceLayout.entries.flatMap { layout ->
            listOf(
                Triple("$layout light", ArecaSurfaces.from(base, layout, darkTheme = false), ArecaColors.light(base)),
                Triple("$layout dark", ArecaSurfaces.from(base, layout, darkTheme = true), ArecaColors.dark(base)),
            )
        }

    private fun ladders(surfaces: ArecaSurfaces) = listOf("screen" to surfaces.screen, "sheet" to surfaces.sheet)

    @Test
    fun `every role stands apart from the surface it is drawn on`() {
        for ((name, surfaces, _) in cases()) {
            for ((root, ladder) in ladders(surfaces)) {
                for (level in SurfaceLevel.entries) {
                    val surface = ladder.at(level)
                    val where = "$name $root $level"
                    assertApart(surface.ground, surface.raised, "$where raised")
                    assertApart(surface.ground, surface.well, "$where well", minimum = MinimumWellStep)
                    assertApart(surface.well, surface.onWell, "$where onWell")
                    assertApart(surface.ground, surface.separator, "$where separator")
                    assertApart(surface.ground, surface.band, "$where band", minimum = MinimumWellStep)
                }
            }
        }
    }

    @Test
    fun `body text and quiet text read on every ground and every raised fill`() {
        for ((name, surfaces, colors) in cases()) {
            for ((root, ladder) in ladders(surfaces)) {
                for (level in SurfaceLevel.entries) {
                    val surface = ladder.at(level)
                    for ((role, fill) in listOf("ground" to surface.ground, "raised" to surface.raised)) {
                        val where = "$name $root $level $role"
                        assertContrast(colors.fgBase, fill, minimum = 7f, where = "$where fgBase")
                        assertContrast(colors.fgMuted, fill, minimum = 4.5f, where = "$where fgMuted")
                    }
                }
            }
        }
    }

    @Test
    fun `a card is drawn in the fill its page raises and an inset in the fill its card raises`() {
        for ((name, surfaces, _) in cases()) {
            for ((root, ladder) in ladders(surfaces)) {
                assertEquals(ladder.page.raised, ladder.card.ground, "$name $root card")
                assertEquals(ladder.card.raised, ladder.inset.ground, "$name $root inset")
            }
        }
    }

    @Test
    fun `a card's band stands apart from the page around the card`() {
        for ((name, surfaces, _) in cases()) {
            for ((root, ladder) in ladders(surfaces)) {
                assertApart(ladder.page.ground, ladder.card.band, "$name $root card band on page", minimum = MinimumWellStep)
            }
        }
    }

    @Test
    fun `a grouped page is grey with white cards and a plain one the reverse while sheets stay white`() {
        val grouped = ArecaSurfaces.from(base, SurfaceLayout.Grouped, darkTheme = false)
        val plain = ArecaSurfaces.from(base, SurfaceLayout.Plain, darkTheme = false)

        assertEquals(base.neutral15, grouped.screen.page.ground)
        assertEquals(base.baseWhite, grouped.screen.card.ground)
        assertEquals(base.baseWhite, plain.screen.page.ground)
        assertEquals(base.neutral15, plain.screen.card.ground)
        assertEquals(base.baseWhite, grouped.sheet.page.ground)
        assertEquals(base.baseWhite, plain.sheet.page.ground)
    }

    @Test
    fun `the dark theme keeps one ladder for both layouts`() {
        val grouped = ArecaSurfaces.from(base, SurfaceLayout.Grouped, darkTheme = true)
        val plain = ArecaSurfaces.from(base, SurfaceLayout.Plain, darkTheme = true)

        assertEquals(grouped.screen, plain.screen)
        assertEquals(grouped.sheet, plain.sheet)
    }

    private fun contrast(a: Color, b: Color): Float {
        val la = a.luminance()
        val lb = b.luminance()
        return (max(la, lb) + 0.05f) / (min(la, lb) + 0.05f)
    }

    private fun assertApart(a: Color, b: Color, where: String, minimum: Float = MinimumSurfaceStep) {
        val ratio = contrast(a, b)
        assertTrue(ratio >= minimum, "$where: $ratio is under $minimum")
    }

    private fun assertContrast(text: Color, fill: Color, minimum: Float, where: String) {
        val ratio = contrast(text, fill)
        assertTrue(ratio >= minimum, "$where: $ratio is under $minimum")
    }

    private companion object {
        /** The least contrast two neighbouring surfaces may have and still read as two. */
        const val MinimumSurfaceStep = 1.05f

        /** The least contrast a well may have with its ground: a field or a track must show. */
        const val MinimumWellStep = 1.1f
    }
}
