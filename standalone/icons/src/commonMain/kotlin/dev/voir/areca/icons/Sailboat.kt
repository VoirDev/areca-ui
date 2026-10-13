package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Sailboat icon.
 *
 * Converted from Lucide `sailboat` (ISC).
 */
public val ArecaIcons.Sailboat: ImageVector
    get() = cachedSailboat ?: ImageVector.Builder(
        name = "Sailboat",
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
            moveTo(10f, 2f)
            verticalLineTo(17f)
            moveTo(7f, 22f)
            arcTo(4f, 4f, 0f, false, true, 3f, 18f)
            arcTo(1f, 1f, 0f, false, true, 4f, 17f)
            horizontalLineTo(20f)
            arcTo(1f, 1f, 0f, false, true, 21f, 18f)
            arcTo(4f, 4f, 0f, false, true, 17f, 22f)
            close()
            moveTo(9.159f, 2.46f)
            arcTo(1f, 1f, 0f, false, true, 10.68f, 2.267f)
            lineTo(20.657f, 11.247f)
            arcTo(1f, 1f, 0f, false, true, 20f, 13f)
            horizontalLineTo(4f)
            arcTo(1f, 1f, 0f, false, true, 3.176f, 11.433f)
            close()
        }
    }.build().also { cachedSailboat = it }

/** `Sailboat` vector, built on first access and reused afterwards. */
private var cachedSailboat: ImageVector? = null
