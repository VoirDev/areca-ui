package dev.voir.areca.foundation

import androidx.compose.runtime.Composable
import platform.UIKit.UIAccessibilityAnnouncementNotification
import platform.UIKit.UIAccessibilityPostNotification

/**
 * Announces by posting `UIAccessibilityAnnouncementNotification`, which VoiceOver speaks without
 * moving its cursor.
 *
 * @return Announcer posting VoiceOver announcements.
 */
@Composable
internal actual fun rememberAccessibilityAnnouncer(): AccessibilityAnnouncer = VoiceOverAnnouncer

/** The one announcer every screen shares; it holds no state of its own. */
private val VoiceOverAnnouncer = AccessibilityAnnouncer { text ->
    UIAccessibilityPostNotification(UIAccessibilityAnnouncementNotification, text)
}
