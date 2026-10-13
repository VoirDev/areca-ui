package dev.voir.areca.modifier

import androidx.compose.ui.Modifier

/**
 * Shows the pointer's "clickable" cursor over this element while [enabled].
 *
 * Apply it next to `Modifier.clickable` on anything the user can act on, so a desktop pointer says
 * so before the click. It only reports intent — it does not make the element clickable.
 *
 * Platforms without a pointer return the receiver unchanged, so the call is free on touch targets
 * and there is no need to guard it at the call site.
 *
 * @param enabled Whether the element currently accepts input. Pass the same value as the
 *   `clickable`'s `enabled`, so a disabled control does not advertise a click.
 * @return The receiver, with the cursor applied where the platform has one.
 */
public expect fun Modifier.clickCursor(enabled: Boolean = true): Modifier
