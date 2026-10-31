package dev.voir.areca.flow.auth

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import dev.voir.areca.window.InputMode
import dev.voir.areca.window.WindowClass
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * Tests for the share of the viewport each [AuthLayout] gives its artwork, and for the layout
 * [AuthLayout.Split] resolves to in each window.
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

    @Test
    fun `a split clamps its fraction like the other split layouts`() {
        assertEquals(AuthLayout.MaxHeroFraction, AuthLayout.Split(fraction = 0.95f).heroFraction)
        assertEquals(AuthLayout.DefaultHeroFraction, AuthLayout.Split().heroFraction)
    }

    @Test
    fun `a split is drawn from an expanded width up`() {
        val split = AuthLayout.Split()
        assertEquals(split, split.resolve(window(840, 820)))
        assertEquals(split, split.resolve(window(1440, 900)))
    }

    @Test
    fun `a split falls back on a window narrower than expanded`() {
        assertEquals(AuthLayout.HeroTop(), AuthLayout.Split().resolve(window(393, 852)))
        assertEquals(AuthLayout.HeroTop(), AuthLayout.Split().resolve(window(690, 830)))
        assertEquals(AuthLayout.HeroTop(), AuthLayout.Split().resolve(window(820, 1180)))
    }

    @Test
    fun `a split falls back on a window too short for it`() {
        val split = AuthLayout.Split(fallback = AuthLayout.Plain)
        assertEquals(AuthLayout.Plain, split.resolve(window(852, 393)))
    }

    @Test
    fun `a split falls back to the layout it names`() {
        val split = AuthLayout.Split(fallback = AuthLayout.FullBleed)
        assertEquals(AuthLayout.FullBleed, split.resolve(window(393, 852)))
    }

    @Test
    fun `a split nested as a fallback resolves to its own fallback`() {
        val split = AuthLayout.Split(fallback = AuthLayout.Split(fallback = AuthLayout.HeroInline))
        assertEquals(AuthLayout.HeroInline, split.resolve(window(393, 852)))
    }

    @Test
    fun `a split resolves to its fallback before the window is measured`() {
        val unmeasured = WindowClass(DpSize.Unspecified, InputMode.Touch)
        assertEquals(AuthLayout.HeroTop(), AuthLayout.Split().resolve(unmeasured))
    }

    @Test
    fun `layouts other than split resolve to themselves on any window`() {
        val layouts = listOf(
            AuthLayout.Plain,
            AuthLayout.HeroTop(),
            AuthLayout.HeroBottom(),
            AuthLayout.HeroInline,
            AuthLayout.FullBleed,
        )
        for (layout in layouts) {
            assertEquals(layout, layout.resolve(window(393, 852)))
            assertEquals(layout, layout.resolve(window(1440, 900)))
        }
    }

    /**
     * A touch window of the given size.
     *
     * @param width Width in dp.
     * @param height Height in dp.
     * @return The window's class.
     */
    private fun window(width: Int, height: Int): WindowClass =
        WindowClass(DpSize(width.dp, height.dp), InputMode.Touch)
}
