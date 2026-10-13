package dev.voir.areca.flow.auth

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * Tests for the share of the viewport each [AuthLayout] gives its artwork.
 *
 * The fraction can arrive from a slider or a stored preference, so it is clamped rather than
 * rejected, and only the split layouts have one.
 */
class AuthLayoutTest {
    @Test
    fun `a fraction inside the range is kept`() {
        assertEquals(0.5f, AuthLayout.HeroTop(fraction = 0.5f).heroFraction)
    }

    @Test
    fun `a split layout without a fraction uses the default`() {
        assertEquals(AuthLayout.DefaultHeroFraction, AuthLayout.HeroBottom().heroFraction)
    }

    @Test
    fun `a fraction below the range is raised to the minimum`() {
        assertEquals(AuthLayout.MinHeroFraction, AuthLayout.HeroTop(fraction = 0.01f).heroFraction)
    }

    @Test
    fun `a fraction above the range is lowered to the maximum`() {
        assertEquals(AuthLayout.MaxHeroFraction, AuthLayout.HeroBottom(fraction = 0.95f).heroFraction)
    }

    @Test
    fun `a negative fraction is raised to the minimum`() {
        assertEquals(AuthLayout.MinHeroFraction, AuthLayout.HeroBottom(fraction = -1f).heroFraction)
    }

    @Test
    fun `layouts that do not split the screen have no fraction`() {
        assertNull(AuthLayout.Plain.heroFraction)
        assertNull(AuthLayout.HeroInline.heroFraction)
        assertNull(AuthLayout.FullBleed.heroFraction)
    }
}
