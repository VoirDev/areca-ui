package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Circle percent icon.
 *
 * Converted from Lucide `circle-percent` (ISC).
 */
public val ArecaIcons.PercentCircle: ImageVector
    get() = cachedPercentCircle ?: ImageVector.Builder(
        name = "PercentCircle",
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
            moveTo(15f, 9f)
            lineTo(9f, 15f)
            moveTo(9f, 9f)
            horizontalLineTo(9.01f)
            moveTo(15f, 15f)
            horizontalLineTo(15.01f)
        }
    }.build().also { cachedPercentCircle = it }

/** `PercentCircle` vector, built on first access and reused afterwards. */
private var cachedPercentCircle: ImageVector? = null
