package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Beef icon.
 *
 * Converted from Lucide `beef` (ISC).
 */
public val ArecaIcons.Beef: ImageVector
    get() = cachedBeef ?: ImageVector.Builder(
        name = "Beef",
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
            moveTo(16.4f, 13.7f)
            arcTo(6.5f, 6.5f, 0f, true, false, 6.28f, 6.6f)
            curveTo(5.18f, 9.73f, 5.5f, 10.5f, 3.1f, 12.68f)
            arcTo(3f, 3f, 0f, false, false, 5f, 18f)
            curveTo(9f, 18f, 13.4f, 16.2f, 16.4f, 13.7f)
            moveTo(18.5f, 6f)
            lineTo(20.254f, 9.5f)
            arcTo(6.48f, 6.48f, 0f, false, true, 18.4f, 17.7f)
            curveTo(15.4f, 20.2f, 11f, 22f, 7f, 22f)
            arcTo(3f, 3f, 0f, false, true, 4.32f, 20.34f)
            lineTo(2.4f, 16.5f)
            moveTo(10f, 8.5f)
            arcTo(2.5f, 2.5f, 0f, true, true, 15f, 8.5f)
            arcTo(2.5f, 2.5f, 0f, true, true, 10f, 8.5f)
            close()
        }
    }.build().also { cachedBeef = it }

/** `Beef` vector, built on first access and reused afterwards. */
private var cachedBeef: ImageVector? = null
