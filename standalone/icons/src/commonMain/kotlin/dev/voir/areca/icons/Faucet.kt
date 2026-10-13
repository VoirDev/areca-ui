package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Faucet icon.
 *
 * Converted from Lucide `faucet` (ISC).
 */
public val ArecaIcons.Faucet: ImageVector
    get() = cachedFaucet ?: ImageVector.Builder(
        name = "Faucet",
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
            moveTo(10.083f, 5.428f)
            lineTo(5.57f, 4.083f)
            arcTo(2f, 2f, 0f, true, false, 5.571f, 7.917f)
            lineTo(10.083f, 6.572f)
            moveTo(12f, 8f)
            verticalLineTo(11f)
            moveTo(13.917f, 5.428f)
            lineTo(18.428f, 4.083f)
            arcTo(2f, 2f, 0f, true, true, 18.428f, 7.917f)
            lineTo(13.918f, 6.572f)
            moveTo(18f, 17f)
            verticalLineTo(12.994f)
            moveTo(22f, 11f)
            verticalLineTo(19f)
            moveTo(22f, 12f)
            horizontalLineTo(19f)
            arcTo(1f, 1f, 0f, false, false, 18f, 12.994f)
            horizontalLineTo(15.461f)
            arcTo(4f, 4f, 0f, false, false, 8.546f, 12.982f)
            lineTo(7f, 13f)
            arcTo(5f, 5f, 0f, false, false, 2f, 18f)
            verticalLineTo(19f)
            arcTo(1f, 1f, 0f, false, false, 3f, 20f)
            horizontalLineTo(5f)
            arcTo(1f, 1f, 0f, false, false, 6f, 19f)
            verticalLineTo(18f)
            arcTo(1f, 1f, 0f, false, true, 6.995f, 17f)
            lineTo(8.547f, 17.018f)
            arcTo(4f, 4f, 0f, false, false, 15.454f, 17.018f)
            lineTo(18f, 17f)
            arcTo(1f, 1f, 0f, false, false, 19f, 18f)
            horizontalLineTo(22f)
            moveTo(10f, 6f)
            arcTo(2f, 2f, 0f, true, true, 14f, 6f)
            arcTo(2f, 2f, 0f, true, true, 10f, 6f)
            close()
        }
    }.build().also { cachedFaucet = it }

/** `Faucet` vector, built on first access and reused afterwards. */
private var cachedFaucet: ImageVector? = null
