package dev.voir.areca.datetime.calendar

import kotlin.test.Test
import kotlin.test.assertEquals

/** Which of its nine looks a day cell takes. */
class CalendarStyleTest {

    private fun appearance(
        selected: Boolean = false,
        isToday: Boolean = false,
        enabled: Boolean = true,
        adjacent: Boolean = false,
    ) = dayAppearanceOf(
        selected = selected,
        isToday = isToday,
        enabled = enabled,
        adjacent = adjacent,
    )

    @Test
    fun `an ordinary day is plain`() {
        assertEquals(DayAppearance.Plain, appearance())
    }

    @Test
    fun `a chosen today reads as chosen`() {
        assertEquals(DayAppearance.Selected, appearance(selected = true, isToday = true))
    }

    @Test
    fun `an unchosen today is ringed as today`() {
        assertEquals(DayAppearance.Today, appearance(isToday = true))
    }

    @Test
    fun `an unavailable chosen day stays visibly chosen`() {
        assertEquals(
            DayAppearance.SelectedDisabled,
            appearance(selected = true, isToday = true, enabled = false),
        )
    }

    @Test
    fun `an unavailable today keeps its own look`() {
        assertEquals(DayAppearance.TodayDisabled, appearance(isToday = true, enabled = false))
    }

    @Test
    fun `an unavailable day is greyed`() {
        assertEquals(DayAppearance.Disabled, appearance(enabled = false))
    }

    @Test
    fun `adjacency wins over every other fact`() {
        assertEquals(
            DayAppearance.AdjacentSelected,
            appearance(selected = true, isToday = true, enabled = false, adjacent = true),
        )
        assertEquals(
            DayAppearance.AdjacentToday,
            appearance(isToday = true, enabled = false, adjacent = true),
        )
        assertEquals(DayAppearance.Adjacent, appearance(enabled = false, adjacent = true))
    }

    @Test
    fun `every combination of the four facts resolves to one of the nine looks`() {
        val flags = listOf(false, true)
        val seen = buildSet {
            flags.forEach { selected ->
                flags.forEach { isToday ->
                    flags.forEach { enabled ->
                        flags.forEach { adjacent ->
                            add(appearance(selected, isToday, enabled, adjacent))
                        }
                    }
                }
            }
        }
        assertEquals(DayAppearance.entries.toSet(), seen)
    }
}
