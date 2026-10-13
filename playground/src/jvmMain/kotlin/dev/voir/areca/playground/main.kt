package dev.voir.areca.playground

import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import java.awt.Dimension

/** Starts the desktop playground and opens the component gallery in a centered window. */
fun main() = application {
    val state = rememberWindowState(
        size = DpSize(1200.dp, 760.dp),
        placement = WindowPlacement.Floating,
        position = WindowPosition.Aligned(Alignment.Center),
        isMinimized = false,
    )

    Window(
        onCloseRequest = ::exitApplication,
        title = "Areca Playground",
        state = state,
        resizable = true,
        transparent = false,
    ) {
        window.minimumSize = Dimension(1200, 760)

        App()
    }
}
