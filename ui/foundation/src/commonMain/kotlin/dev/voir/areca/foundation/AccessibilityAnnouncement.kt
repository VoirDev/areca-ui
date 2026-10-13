package dev.voir.areca.foundation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

/**
 * Speaks a sentence through the platform's screen reader without moving focus to anything.
 *
 * Compose announces a live region only when a node that already existed changes: on Android a node
 * that is created with its text is skipped, and on iOS a live region is only a trait VoiceOver does
 * not speak on appearance. A message that appears by being composed — a validation error, a failed
 * attempt — would stay silent exactly when it matters, so it is announced through this instead.
 */
internal fun interface AccessibilityAnnouncer {
    /**
     * Queues [text] to be spoken. Does nothing when no screen reader is running.
     *
     * @param text Sentence to speak, already worded.
     */
    fun announce(text: String)
}

/**
 * The current platform's [AccessibilityAnnouncer].
 *
 * Android posts through the composing view, iOS posts a VoiceOver announcement notification, and
 * the JVM has no announcement API, so it speaks nothing there.
 *
 * @return Announcer bound to the screen being composed.
 */
@Composable
internal expect fun rememberAccessibilityAnnouncer(): AccessibilityAnnouncer

/**
 * Announces [text] to assistive technology when it is first composed and every time it changes to
 * a new value.
 *
 * Use it in place of a live region for a message that appears by being composed: it is keyed on
 * the text, not on a node, so the first message is spoken as well as later ones. `null` or blank
 * text says nothing, and a message that is cleared and then given again is spoken again.
 *
 * Speaks through `View.announceForAccessibility` on Android and a
 * `UIAccessibilityAnnouncementNotification` on iOS. Compose for desktop has no announcement API,
 * so nothing is spoken there; a desktop screen reader reads the message when it reaches it.
 *
 * ```kotlin
 * AnnounceOnChange(error?.toString())
 * ```
 *
 * @param text Sentence to announce, already worded, or `null` when there is nothing to say.
 */
@Composable
public fun AnnounceOnChange(text: String?) {
    val announcer = rememberAccessibilityAnnouncer()
    LaunchedEffect(text, announcer) {
        if (!text.isNullOrBlank()) announcer.announce(text)
    }
}
