package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Refresh ccw icon.
 *
 * Converted from Lucide `refresh-ccw` (ISC).
 */
public val ArecaIcons.RefreshCcw: ImageVector
    get() = cachedRefreshCcw ?: ImageVector.Builder(
        name = "RefreshCcw",
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
            moveTo(21f, 12f)
            arcTo(9f, 9f, 0f, false, false, 12f, 3f)
            arcTo(9.75f, 9.75f, 0f, false, false, 5.26f, 5.74f)
            lineTo(3f, 8f)
            moveTo(3f, 3f)
            verticalLineTo(8f)
            horizontalLineTo(8f)
            moveTo(3f, 12f)
            arcTo(9f, 9f, 0f, false, false, 12f, 21f)
            arcTo(9.75f, 9.75f, 0f, false, false, 18.74f, 18.26f)
            lineTo(21f, 16f)
            moveTo(16f, 16f)
            horizontalLineTo(21f)
            verticalLineTo(21f)
        }
    }.build().also { cachedRefreshCcw = it }

/** `RefreshCcw` vector, built on first access and reused afterwards. */
private var cachedRefreshCcw: ImageVector? = null
