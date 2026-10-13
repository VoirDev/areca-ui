package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Bath icon.
 *
 * Converted from Lucide `bath` (ISC).
 */
public val ArecaIcons.Bath: ImageVector
    get() = cachedBath ?: ImageVector.Builder(
        name = "Bath",
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
            moveTo(10f, 4f)
            lineTo(8f, 6f)
            moveTo(17f, 19f)
            verticalLineTo(21f)
            moveTo(2f, 12f)
            horizontalLineTo(22f)
            moveTo(7f, 19f)
            verticalLineTo(21f)
            moveTo(9f, 5f)
            lineTo(7.621f, 3.621f)
            arcTo(2.121f, 2.121f, 0f, false, false, 4f, 5f)
            verticalLineTo(17f)
            arcTo(2f, 2f, 0f, false, false, 6f, 19f)
            horizontalLineTo(18f)
            arcTo(2f, 2f, 0f, false, false, 20f, 17f)
            verticalLineTo(12f)
        }
    }.build().also { cachedBath = it }

/** `Bath` vector, built on first access and reused afterwards. */
private var cachedBath: ImageVector? = null
