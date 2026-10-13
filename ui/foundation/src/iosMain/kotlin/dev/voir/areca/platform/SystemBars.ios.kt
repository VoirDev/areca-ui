package dev.voir.areca.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/** Nothing to do: the status bar's text style belongs to the application's view controller. */
@Composable
public actual fun SystemBars(color: Color, lightIcons: Boolean): Unit = Unit
