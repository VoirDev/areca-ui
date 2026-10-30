package dev.voir.areca.modifier

import androidx.compose.ui.Modifier

/** No-op: the platform draws its own pointer, when it has one. */
internal actual fun Modifier.resizeCursor(): Modifier = this
