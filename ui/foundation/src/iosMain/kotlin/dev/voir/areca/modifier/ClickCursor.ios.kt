package dev.voir.areca.modifier

import androidx.compose.ui.Modifier

/** No-op on iOS, which has no pointer cursor to change. */
public actual fun Modifier.clickCursor(enabled: Boolean): Modifier = this
