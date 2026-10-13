package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Baggage claim icon.
 *
 * Converted from Lucide `baggage-claim` (ISC).
 */
public val ArecaIcons.BaggageClaim: ImageVector
    get() = cachedBaggageClaim ?: ImageVector.Builder(
        name = "BaggageClaim",
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
            moveTo(22f, 18f)
            horizontalLineTo(6f)
            arcTo(2f, 2f, 0f, false, true, 4f, 16f)
            verticalLineTo(7f)
            arcTo(2f, 2f, 0f, false, false, 2f, 5f)
            moveTo(17f, 14f)
            verticalLineTo(4f)
            arcTo(2f, 2f, 0f, false, false, 15f, 2f)
            horizontalLineTo(14f)
            arcTo(2f, 2f, 0f, false, false, 12f, 4f)
            verticalLineTo(14f)
            moveTo(9f, 6f)
            horizontalLineTo(20f)
            arcTo(1f, 1f, 0f, false, true, 21f, 7f)
            verticalLineTo(13f)
            arcTo(1f, 1f, 0f, false, true, 20f, 14f)
            horizontalLineTo(9f)
            arcTo(1f, 1f, 0f, false, true, 8f, 13f)
            verticalLineTo(7f)
            arcTo(1f, 1f, 0f, false, true, 9f, 6f)
            close()
            moveTo(16f, 20f)
            arcTo(2f, 2f, 0f, true, true, 20f, 20f)
            arcTo(2f, 2f, 0f, true, true, 16f, 20f)
            close()
            moveTo(7f, 20f)
            arcTo(2f, 2f, 0f, true, true, 11f, 20f)
            arcTo(2f, 2f, 0f, true, true, 7f, 20f)
            close()
        }
    }.build().also { cachedBaggageClaim = it }

/** `BaggageClaim` vector, built on first access and reused afterwards. */
private var cachedBaggageClaim: ImageVector? = null
