package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Broccoli icon.
 *
 * Converted from Lucide `broccoli` (ISC).
 */
public val ArecaIcons.Broccoli: ImageVector
    get() = cachedBroccoli ?: ImageVector.Builder(
        name = "Broccoli",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
            moveTo(10f, 13f)
            arcTo(3f, 3f, 0f, false, true, 7.879f, 7.879f)
            moveTo(15.606f, 14.204f)
            curveTo(12.106f, 15.704f, 9.707f, 18.707f, 6.707f, 21.707f)
            arcTo(1f, 1f, 0f, false, true, 6f, 22f)
            curveTo(4f, 22f, 2f, 20f, 2f, 18f)
            arcTo(1f, 1f, 0f, false, true, 2.293f, 17.293f)
            curveTo(4.204f, 15.382f, 6.116f, 13.715f, 7.64f, 11.852f)
            moveTo(16.573f, 14.737f)
            arcTo(4f, 4f, 0f, false, true, 14f, 11f)
            moveTo(7.14f, 10.907f)
            arcTo(4f, 4f, 0f, true, true, 9.896f, 3.477f)
            arcTo(4f, 4f, 0f, false, true, 16.7f, 4.48f)
            arcTo(2f, 2f, 0f, false, true, 19.52f, 7.3f)
            arcTo(4f, 4f, 0f, false, true, 20.522f, 14.105f)
            arcTo(4f, 4f, 0f, true, true, 13.012f, 15.695f)
        }
    }.build().also { cachedBroccoli = it }

/** `Broccoli` vector, built on first access and reused afterwards. */
private var cachedBroccoli: ImageVector? = null
