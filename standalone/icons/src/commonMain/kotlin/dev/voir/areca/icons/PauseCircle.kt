package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Circle pause icon.
 *
 * Converted from Lucide `circle-pause` (ISC).
 */
public val ArecaIcons.PauseCircle: ImageVector
    get() = cachedPauseCircle ?: ImageVector.Builder(
        name = "PauseCircle",
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
            moveTo(10f, 15f)
            lineTo(10f, 9f)
            moveTo(14f, 15f)
            lineTo(14f, 9f)
        }
    }.build().also { cachedPauseCircle = it }

/** `PauseCircle` vector, built on first access and reused afterwards. */
private var cachedPauseCircle: ImageVector? = null
