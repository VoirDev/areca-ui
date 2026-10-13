package dev.voir.areca.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/** Nothing to do: a desktop window has no system bars. */
@Composable
public actual fun SystemBars(color: Color, lightIcons: Boolean): Unit = Unit
