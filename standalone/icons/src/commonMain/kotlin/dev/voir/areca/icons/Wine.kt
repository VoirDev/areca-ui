package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Wine icon.
 *
 * Converted from Lucide `wine` (ISC).
 */
public val ArecaIcons.Wine: ImageVector
    get() = cachedWine ?: ImageVector.Builder(
        name = "Wine",
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
            moveTo(8f, 22f)
            horizontalLineTo(16f)
            moveTo(7f, 10f)
            horizontalLineTo(17f)
            moveTo(12f, 15f)
            verticalLineTo(22f)
            moveTo(12f, 15f)
            arcTo(5f, 5f, 0f, false, false, 17f, 10f)
            curveTo(17f, 8f, 16.5f, 6f, 15f, 2f)
            horizontalLineTo(9f)
            curveTo(7.5f, 6f, 7f, 8f, 7f, 10f)
            arcTo(5f, 5f, 0f, false, false, 12f, 15f)
            close()
        }
    }.build().also { cachedWine = it }

/** `Wine` vector, built on first access and reused afterwards. */
private var cachedWine: ImageVector? = null
