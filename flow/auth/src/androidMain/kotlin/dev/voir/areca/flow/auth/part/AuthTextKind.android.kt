package dev.voir.areca.flow.auth.part

import androidx.compose.ui.text.input.PlatformImeOptions

/**
 * Nothing to add: Android autofill reads the `contentType` semantics the field already carries.
 *
 * @return `null`.
 */
internal actual fun AuthTextKind.platformImeOptions(): PlatformImeOptions? = null
