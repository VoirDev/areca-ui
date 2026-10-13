package dev.voir.areca.flow.auth.part

import androidx.compose.ui.text.input.PlatformImeOptions

/**
 * Nothing to add: the desktop has no platform autofill for Compose text fields to reach.
 *
 * @return `null`.
 */
internal actual fun AuthTextKind.platformImeOptions(): PlatformImeOptions? = null
