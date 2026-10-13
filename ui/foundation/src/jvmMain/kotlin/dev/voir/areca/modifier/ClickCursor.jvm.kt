package dev.voir.areca.modifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon

/** Shows the hand cursor on desktop, which is where a pointer actually exists. */
public actual fun Modifier.clickCursor(enabled: Boolean): Modifier =
    if (enabled) pointerHoverIcon(PointerIcon.Hand) else this
