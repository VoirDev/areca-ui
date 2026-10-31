package dev.voir.areca.modifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import java.awt.Cursor

/** The system's horizontal resize cursor. */
private val ResizeIcon = PointerIcon(Cursor(Cursor.E_RESIZE_CURSOR))

internal actual fun Modifier.resizeCursor(): Modifier = pointerHoverIcon(ResizeIcon)
