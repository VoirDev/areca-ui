package dev.voir.areca.datetime.calendar.range

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/** What a range adds to a day's spoken description. */
class DateRangeLabelsTest {

    private val position = DateRangeLabels.English.position

    @Test
    fun `a lone chosen day adds nothing since it is already announced as selected`() {
        assertNull(position(DateRangeDayPosition.Single))
    }

    @Test
    fun `a day outside the range adds nothing`() {
        assertNull(position(DateRangeDayPosition.None))
    }

    @Test
    fun `the ends and the days between say where they sit`() {
        assertEquals("start of range", position(DateRangeDayPosition.Start))
        assertEquals("in range", position(DateRangeDayPosition.InRange))
        assertEquals("end of range", position(DateRangeDayPosition.End))
    }
}
