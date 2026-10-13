package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Milk icon.
 *
 * Converted from Lucide `milk` (ISC).
 */
public val ArecaIcons.Milk: ImageVector
    get() = cachedMilk ?: ImageVector.Builder(
        name = "Milk",
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
            moveTo(8f, 2f)
            horizontalLineTo(16f)
            moveTo(9f, 2f)
            verticalLineTo(4.789f)
            arcTo(4f, 4f, 0f, false, true, 8.328f, 7.008f)
            lineTo(7.672f, 7.992f)
            arcTo(4f, 4f, 0f, false, false, 7f, 10.212f)
            verticalLineTo(20f)
            arcTo(2f, 2f, 0f, false, false, 9f, 22f)
            horizontalLineTo(15f)
            arcTo(2f, 2f, 0f, false, false, 17f, 20f)
            verticalLineTo(10.211f)
            arcTo(4f, 4f, 0f, false, false, 16.328f, 7.992f)
            lineTo(15.672f, 7.008f)
            arcTo(4f, 4f, 0f, false, true, 15f, 4.788f)
            verticalLineTo(2f)
            moveTo(7f, 15f)
            arcTo(6.472f, 6.472f, 0f, false, true, 12f, 15f)
            arcTo(6.47f, 6.47f, 0f, false, false, 17f, 15f)
        }
    }.build().also { cachedMilk = it }

/** `Milk` vector, built on first access and reused afterwards. */
private var cachedMilk: ImageVector? = null
