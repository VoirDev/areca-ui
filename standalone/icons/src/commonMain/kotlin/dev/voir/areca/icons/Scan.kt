package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Scan icon.
 *
 * Converted from Lucide `scan` (ISC).
 */
public val ArecaIcons.Scan: ImageVector
    get() = cachedScan ?: ImageVector.Builder(
        name = "Scan",
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
            moveTo(3f, 7f)
            verticalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 5f, 3f)
            horizontalLineTo(7f)
            moveTo(17f, 3f)
            horizontalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 21f, 5f)
            verticalLineTo(7f)
            moveTo(21f, 17f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 19f, 21f)
            horizontalLineTo(17f)
            moveTo(7f, 21f)
            horizontalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 3f, 19f)
            verticalLineTo(17f)
        }
    }.build().also { cachedScan = it }

/** `Scan` vector, built on first access and reused afterwards. */
private var cachedScan: ImageVector? = null
