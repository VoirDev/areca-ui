package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Circle stop icon.
 *
 * Converted from Lucide `circle-stop` (ISC).
 */
public val ArecaIcons.StopCircle: ImageVector
    get() = cachedStopCircle ?: ImageVector.Builder(
        name = "StopCircle",
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
            moveTo(2f, 12f)
            arcTo(10f, 10f, 0f, true, true, 22f, 12f)
            arcTo(10f, 10f, 0f, true, true, 2f, 12f)
            close()
            moveTo(10f, 9f)
            horizontalLineTo(14f)
            arcTo(1f, 1f, 0f, false, true, 15f, 10f)
            verticalLineTo(14f)
            arcTo(1f, 1f, 0f, false, true, 14f, 15f)
            horizontalLineTo(10f)
            arcTo(1f, 1f, 0f, false, true, 9f, 14f)
            verticalLineTo(10f)
            arcTo(1f, 1f, 0f, false, true, 10f, 9f)
            close()
        }
    }.build().also { cachedStopCircle = it }

/** `StopCircle` vector, built on first access and reused afterwards. */
private var cachedStopCircle: ImageVector? = null
