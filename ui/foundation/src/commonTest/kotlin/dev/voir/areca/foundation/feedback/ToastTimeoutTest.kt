package dev.voir.areca.foundation.feedback

import androidx.compose.ui.platform.AccessibilityManager
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class ToastTimeoutTest {
    /** Accessibility settings that record what they were asked and answer with [answer]. */
    private class RecordingAccessibilityManager(private val answer: (Long) -> Long) : AccessibilityManager {
        var askedControls: Boolean? = null
        var askedIcons: Boolean? = null

        override fun calculateRecommendedTimeoutMillis(
            originalTimeoutMillis: Long,
            containsIcons: Boolean,
            containsText: Boolean,
            containsControls: Boolean,
        ): Long {
            askedControls = containsControls
            askedIcons = containsIcons
            return answer(originalTimeoutMillis)
        }
    }

    @Test
    fun `without accessibility settings a toast keeps its own duration`() {
        val toast = ToastData(label = "Saved", duration = 3.seconds)

        assertEquals(3.seconds, toast.dismissTimeout(accessibilityManager = null))
    }

    @Test
    fun `a toast with no duration stays whatever the settings say`() {
        val manager = RecordingAccessibilityManager { 10_000L }
        val toast = ToastData(label = "Saved", duration = null)

        assertNull(toast.dismissTimeout(manager))
    }

    @Test
    fun `the platform recommendation lengthens the timeout`() {
        val manager = RecordingAccessibilityManager { original -> original * 5 }
        val toast = ToastData(label = "Saved", duration = 2.seconds)

        assertEquals(10_000.milliseconds, toast.dismissTimeout(manager))
    }

    @Test
    fun `a toast with an action is reported as holding controls`() {
        val manager = RecordingAccessibilityManager { it }
        val toast = ToastData(label = "Deleted", onClick = {})

        toast.dismissTimeout(manager)

        assertEquals(true, manager.askedControls)
        assertEquals(false, manager.askedIcons)
    }

    @Test
    fun `a plain toast is reported as holding no controls`() {
        val manager = RecordingAccessibilityManager { it }
        val toast = ToastData(label = "Saved")

        toast.dismissTimeout(manager)

        assertEquals(false, manager.askedControls)
    }

    @Test
    fun `an unbounded recommendation keeps the toast until dismissed`() {
        val manager = RecordingAccessibilityManager { Long.MAX_VALUE }
        val toast = ToastData(label = "Saved", duration = 2.seconds)

        assertNull(toast.dismissTimeout(manager))
    }
}
