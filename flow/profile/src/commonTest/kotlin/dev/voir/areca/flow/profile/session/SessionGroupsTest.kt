package dev.voir.areca.flow.profile.session

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** How the devices sections split a list into this device and the rest. */
class SessionGroupsTest {

    private val phone = SessionItem(id = "1", device = SessionDevice.Phone, name = "Pixel", isCurrent = true)
    private val tablet = SessionItem(id = "2", device = SessionDevice.Tablet, name = "iPad")
    private val secondTablet = SessionItem(id = "3", device = SessionDevice.Tablet, name = "Galaxy Tab")

    @Test
    fun `the current session is split from the others and they keep their order`() {
        val groups = SessionGroups.of(listOf(tablet, phone, secondTablet))

        assertEquals(phone, groups.current)
        assertEquals(listOf(tablet, secondTablet), groups.others)
    }

    @Test
    fun `others can be signed out with a current session and another one`() {
        assertTrue(SessionGroups.of(listOf(phone, tablet)).canSignOutOthers)
    }

    @Test
    fun `others cannot be signed out when this device is alone`() {
        assertFalse(SessionGroups.of(listOf(phone)).canSignOutOthers)
    }

    @Test
    fun `others cannot be signed out with no current session`() {
        val groups = SessionGroups.of(listOf(tablet, secondTablet))

        assertNull(groups.current)
        assertFalse(groups.canSignOutOthers)
    }
}
