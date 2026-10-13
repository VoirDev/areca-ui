package dev.voir.areca.foundation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalView

/**
 * Announces through the view hosting the composition.
 *
 * `View.announceForAccessibility` is deprecated from API 36 in favour of live regions, but a Compose
 * live region is not announced when its node first appears, which is the case these announcements
 * exist for. TalkBack still speaks the announcement.
 *
 * @return Announcer posting through the current view.
 */
@Composable
internal actual fun rememberAccessibilityAnnouncer(): AccessibilityAnnouncer {
    val view = LocalView.current
    return remember(view) {
        AccessibilityAnnouncer { text ->
            @Suppress("DEPRECATION")
            view.announceForAccessibility(text)
        }
    }
}
