package dev.voir.areca.foundation

import androidx.compose.runtime.Composable

/**
 * Speaks nothing: Compose for desktop has no announcement API. A desktop screen reader reads the
 * message when it reaches it.
 *
 * @return Announcer that ignores every sentence.
 */
@Composable
internal actual fun rememberAccessibilityAnnouncer(): AccessibilityAnnouncer = SilentAnnouncer

/** The announcer every desktop screen shares. */
private val SilentAnnouncer = AccessibilityAnnouncer { }
