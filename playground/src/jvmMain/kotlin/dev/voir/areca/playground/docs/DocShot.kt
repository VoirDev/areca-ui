package dev.voir.areca.playground.docs

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * One column of a documentation image, rendered in a scene of its own.
 *
 * A shot is usually one panel. It is two when each half holds a window — a dialog, a sheet — since a
 * window covers the whole scene it opens in; the halves are rendered apart and placed side by side.
 *
 * @property width Logical width of the panel.
 * @property padded Whether the panel keeps a margin around its content. A panel that is a whole
 *   screen — a sign-in, a crop editor — draws to its edges and passes `false`.
 * @property content What the panel shows, built from the published components as an app builds it.
 */
internal class DocPanel(
    val width: Dp,
    val padded: Boolean = true,
    val content: @Composable () -> Unit,
)

/**
 * One documentation image, written as `<name>-light.png` and `<name>-dark.png`.
 *
 * @property name File name stem, as `docs/plans/documentation-redesign.md` lists it.
 * @property panels Columns of the image, left to right, all as tall as the image.
 * @property height Logical height of the image, or `null` to fit the tallest panel's content. A panel
 *   that fills its height — a screen with a bar at the bottom, a window — needs a fixed one.
 * @property gap Canvas between two panels, so two screens read as two.
 */
internal class DocShot(
    val name: String,
    val panels: List<DocPanel>,
    val height: Dp? = null,
    val gap: Dp = 16.dp,
) {
    /**
     * A shot of a single panel.
     *
     * @param name File name stem.
     * @param width Logical width of the image.
     * @param height Logical height, or `null` to fit the content.
     * @param padded Whether the content keeps a margin.
     * @param content What the image shows.
     */
    constructor(
        name: String,
        width: Dp,
        height: Dp? = null,
        padded: Boolean = true,
        content: @Composable () -> Unit,
    ) : this(name, listOf(DocPanel(width, padded, content)), height)
}
