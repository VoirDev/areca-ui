package dev.voir.areca.modifier

import androidx.compose.ui.Modifier

/**
 * Shows the pointer's left-right resize cursor over this element, for a handle that drags a pane's
 * edge. Platforms without a pointer return the receiver unchanged.
 */
internal expect fun Modifier.resizeCursor(): Modifier
