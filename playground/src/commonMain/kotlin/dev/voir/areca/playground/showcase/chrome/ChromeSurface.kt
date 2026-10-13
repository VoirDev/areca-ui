package dev.voir.areca.playground.showcase.chrome

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import dev.voir.areca.theme.ArecaTheme

/**
 * Elevation roles the shell distinguishes. The showcase is a flat, bordered surface stack rather
 * than a shadowed one, so a "level" here is a background step, not a z-offset.
 */
enum class ChromeLevel {
    /** The page itself. */
    Canvas,

    /** A section container sitting on the page. */
    Raised,

    /** A panel inside a section, such as the control rail. */
    Inset,
}

/**
 * A bordered container in the showcase shell.
 *
 * @param modifier Modifier applied to the container.
 * @param level Background step this surface sits at.
 * @param shape Corner shape; defaults to the theme's large radius.
 * @param bordered Whether to draw the resting border.
 * @param content Content placed inside the surface.
 */
@Composable
fun ChromeSurface(
    modifier: Modifier = Modifier,
    level: ChromeLevel = ChromeLevel.Raised,
    shape: Shape = RoundedCornerShape(ArecaTheme.radius.lg),
    bordered: Boolean = true,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(level.color, shape)
            .then(
                if (bordered) {
                    Modifier.border(1.dp, ArecaTheme.colors.borderBase, shape)
                } else {
                    Modifier
                }
            ),
    ) {
        content()
    }
}

internal val ChromeLevel.color: Color
    @Composable
    @ReadOnlyComposable
    get() = when (this) {
        ChromeLevel.Canvas -> ArecaTheme.colors.bgCanvas
        ChromeLevel.Raised -> ArecaTheme.colors.bgSurface
        ChromeLevel.Inset -> ArecaTheme.colors.bgSubtle
    }
