package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Calculator icon.
 *
 * Converted from Lucide `calculator` (ISC).
 */
public val ArecaIcons.Calculator: ImageVector
    get() = cachedCalculator ?: ImageVector.Builder(
        name = "Calculator",
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
            moveTo(6f, 2f)
            horizontalLineTo(18f)
            arcTo(2f, 2f, 0f, false, true, 20f, 4f)
            verticalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 18f, 22f)
            horizontalLineTo(6f)
            arcTo(2f, 2f, 0f, false, true, 4f, 20f)
            verticalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 6f, 2f)
            close()
            moveTo(8f, 6f)
            lineTo(16f, 6f)
            moveTo(16f, 14f)
            lineTo(16f, 18f)
            moveTo(16f, 10f)
            horizontalLineTo(16.01f)
            moveTo(12f, 10f)
            horizontalLineTo(12.01f)
            moveTo(8f, 10f)
            horizontalLineTo(8.01f)
            moveTo(12f, 14f)
            horizontalLineTo(12.01f)
            moveTo(8f, 14f)
            horizontalLineTo(8.01f)
            moveTo(12f, 18f)
            horizontalLineTo(12.01f)
            moveTo(8f, 18f)
            horizontalLineTo(8.01f)
        }
    }.build().also { cachedCalculator = it }

/** `Calculator` vector, built on first access and reused afterwards. */
private var cachedCalculator: ImageVector? = null
