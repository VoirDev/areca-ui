package dev.voir.areca.window

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

/** How a window's size and input become classes and a profile. */
class WindowClassTest {
    @Test
    fun `width classes change exactly at their breakpoints`() {
        assertEquals(WidthClass.Compact, WidthClass.of(599.9.dp))
        assertEquals(WidthClass.Medium, WidthClass.of(600.dp))
        assertEquals(WidthClass.Medium, WidthClass.of(839.dp))
        assertEquals(WidthClass.Expanded, WidthClass.of(840.dp))
        assertEquals(WidthClass.Expanded, WidthClass.of(1199.dp))
        assertEquals(WidthClass.Large, WidthClass.of(1200.dp))
        assertEquals(WidthClass.Large, WidthClass.of(1599.dp))
        assertEquals(WidthClass.ExtraLarge, WidthClass.of(1600.dp))
    }

    @Test
    fun `height classes change exactly at their breakpoints`() {
        assertEquals(HeightClass.Compact, HeightClass.of(479.dp))
        assertEquals(HeightClass.Medium, HeightClass.of(480.dp))
        assertEquals(HeightClass.Medium, HeightClass.of(899.dp))
        assertEquals(HeightClass.Expanded, HeightClass.of(900.dp))
    }

    @Test
    fun `an unmeasured window reads as a phone held upright`() {
        val unmeasured = WindowClass(DpSize.Unspecified, InputMode.Touch)

        assertEquals(WidthClass.Compact, unmeasured.width)
        assertEquals(HeightClass.Medium, unmeasured.height)
        assertEquals(WindowProfile.PhonePortrait, unmeasured.profile)
        assertFalse(unmeasured.isLandscape)
        assertEquals(WidthClass.Compact, WidthClass.of(Dp.Unspecified))
        assertEquals(WidthClass.Compact, WidthClass.of((-1).dp))
    }

    @Test
    fun `one unmeasured side keeps the other`() {
        val window = WindowClass(DpSize(900.dp, Dp.Unspecified), InputMode.Pointer)

        assertEquals(WidthClass.Expanded, window.width)
        assertEquals(HeightClass.Medium, window.height)
        assertFalse(window.isLandscape)
    }

    @Test
    fun `profiles follow the window and not the device`() {
        fun profile(w: Int, h: Int, input: InputMode) = WindowClass(DpSize(w.dp, h.dp), input).profile

        assertEquals(WindowProfile.PhonePortrait, profile(393, 852, InputMode.Touch))
        assertEquals(WindowProfile.PhoneLandscape, profile(852, 393, InputMode.Touch))
        assertEquals(WindowProfile.PhonePortrait, profile(344, 882, InputMode.Touch))
        assertEquals(WindowProfile.TabletPortrait, profile(690, 830, InputMode.Touch))
        assertEquals(WindowProfile.TabletPortrait, profile(820, 1180, InputMode.Touch))
        assertEquals(WindowProfile.TabletLandscape, profile(1180, 820, InputMode.Touch))
        assertEquals(WindowProfile.Desktop, profile(1100, 760, InputMode.Pointer))
        assertEquals(WindowProfile.TabletPortrait, profile(700, 900, InputMode.Pointer))
        // A desktop window dragged narrow is laid out like a phone.
        assertEquals(WindowProfile.PhonePortrait, profile(420, 900, InputMode.Pointer))
    }

    @Test
    fun `a tablet's profile under touch follows its shape and not its width`() {
        fun profile(w: Int, h: Int) = WindowClass(DpSize(w.dp, h.dp), InputMode.Touch).profile

        // A 13-inch tablet: as wide upright as a small one on its side, still held upright.
        assertEquals(WindowProfile.TabletPortrait, profile(1032, 1376))
        assertEquals(WindowProfile.TabletLandscape, profile(1376, 1032))
        // An unfolded foldable on its side is nearly square, and held like a tablet upright.
        assertEquals(WindowProfile.TabletPortrait, profile(841, 701))
        assertEquals(WindowProfile.TabletPortrait, profile(830, 690))
    }

    @Test
    fun `actions sit beside the title on a wide touch window or under a pointer and else at the foot`() {
        fun reach(w: Int, h: Int, input: InputMode = InputMode.Touch) =
            WindowClass(DpSize(w.dp, h.dp), input).actionReach

        // Held from below.
        assertEquals(ActionReach.Thumb, reach(393, 852))
        assertEquals(ActionReach.Thumb, reach(344, 882))
        assertEquals(ActionReach.Thumb, reach(834, 1194))
        assertEquals(ActionReach.Thumb, reach(1032, 1376))
        assertEquals(ActionReach.Thumb, reach(690, 830))
        assertEquals(ActionReach.Thumb, reach(841, 701))
        assertEquals(ActionReach.Thumb, reach(800, 800))
        // On its side, on a stand or by its short edges.
        assertEquals(ActionReach.Header, reach(1194, 834))
        assertEquals(ActionReach.Header, reach(1376, 1032))
        assertEquals(ActionReach.Header, reach(1280, 800))
        // A phone on its side has its rail and its top bar.
        assertEquals(ActionReach.Header, reach(852, 393))
        // A pointer reaches everywhere, at any shape.
        assertEquals(ActionReach.Header, reach(1440, 900, InputMode.Pointer))
        assertEquals(ActionReach.Header, reach(700, 1100, InputMode.Pointer))
        assertEquals(ActionReach.Header, reach(420, 900, InputMode.Pointer))
    }

    @Test
    fun `the wide line is inclusive and an unmeasured window is held from below`() {
        val ratio = WindowClass.WideAspectRatio

        assertEquals(ActionReach.Header, WindowClass(DpSize((800 * ratio).dp, 800.dp), InputMode.Touch).actionReach)
        assertEquals(ActionReach.Thumb, WindowClass(DpSize((800 * ratio - 1).dp, 800.dp), InputMode.Touch).actionReach)
        assertEquals(ActionReach.Thumb, WindowClass(DpSize.Unspecified, InputMode.Touch).actionReach)
    }

    @Test
    fun `landscape means wider than tall`() {
        assertTrue(WindowClass(DpSize(852.dp, 393.dp), InputMode.Touch).isLandscape)
        assertFalse(WindowClass(DpSize(600.dp, 600.dp), InputMode.Touch).isLandscape)
    }

    @Test
    fun `select falls back to the next smaller step`() {
        fun gutter(width: WidthClass) = width.select(compact = 16, medium = 24, large = 40)

        assertEquals(16, gutter(WidthClass.Compact))
        assertEquals(24, gutter(WidthClass.Medium))
        assertEquals(24, gutter(WidthClass.Expanded))
        assertEquals(40, gutter(WidthClass.Large))
        assertEquals(40, gutter(WidthClass.ExtraLarge))
        assertEquals("tall", HeightClass.Expanded.select(compact = "short", medium = "regular", expanded = "tall"))
        assertEquals("regular", HeightClass.Expanded.select(compact = "short", medium = "regular"))
    }

    @Test
    fun `classes are ordered from smallest to largest`() {
        assertTrue(WidthClass.Expanded >= WidthClass.Medium)
        assertTrue(WidthClass.Compact < WidthClass.Medium)
        assertTrue(HeightClass.Compact < HeightClass.Expanded)
    }

    @Test
    fun `equal sizes and inputs are equal classes`() {
        val size = DpSize(820.dp, 1180.dp)

        assertEquals(WindowClass(size, InputMode.Touch), WindowClass(size, InputMode.Touch))
        assertEquals(WindowClass(size, InputMode.Touch).hashCode(), WindowClass(size, InputMode.Touch).hashCode())
        assertNotEquals(WindowClass(size, InputMode.Touch), WindowClass(size, InputMode.Pointer))
    }
}
