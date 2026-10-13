package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Receipt icon.
 *
 * Converted from Lucide `receipt` (ISC).
 */
public val ArecaIcons.Receipt: ImageVector
    get() = cachedReceipt ?: ImageVector.Builder(
        name = "Receipt",
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
            moveTo(12f, 17f)
            verticalLineTo(7f)
            moveTo(16f, 8f)
            horizontalLineTo(10f)
            arcTo(2f, 2f, 0f, false, false, 10f, 12f)
            horizontalLineTo(14f)
            arcTo(2f, 2f, 0f, false, true, 14f, 16f)
            horizontalLineTo(8f)
            moveTo(4f, 3f)
            arcTo(1f, 1f, 0f, false, true, 5f, 2f)
            arcTo(1.3f, 1.3f, 0f, false, true, 5.7f, 2.2f)
            lineTo(6.633f, 2.8f)
            arcTo(1.3f, 1.3f, 0f, false, false, 8.033f, 2.8f)
            lineTo(8.967f, 2.2f)
            arcTo(1.3f, 1.3f, 0f, false, true, 10.367f, 2.2f)
            lineTo(11.3f, 2.8f)
            arcTo(1.3f, 1.3f, 0f, false, false, 12.7f, 2.8f)
            lineTo(13.633f, 2.2f)
            arcTo(1.3f, 1.3f, 0f, false, true, 15.033f, 2.2f)
            lineTo(15.967f, 2.8f)
            arcTo(1.3f, 1.3f, 0f, false, false, 17.367f, 2.8f)
            lineTo(18.3f, 2.2f)
            arcTo(1.3f, 1.3f, 0f, false, true, 19f, 2f)
            arcTo(1f, 1f, 0f, false, true, 20f, 3f)
            verticalLineTo(21f)
            arcTo(1f, 1f, 0f, false, true, 19f, 22f)
            arcTo(1.3f, 1.3f, 0f, false, true, 18.3f, 21.8f)
            lineTo(17.367f, 21.2f)
            arcTo(1.3f, 1.3f, 0f, false, false, 15.967f, 21.2f)
            lineTo(15.033f, 21.8f)
            arcTo(1.3f, 1.3f, 0f, false, true, 13.633f, 21.8f)
            lineTo(12.7f, 21.2f)
            arcTo(1.3f, 1.3f, 0f, false, false, 11.3f, 21.2f)
            lineTo(10.367f, 21.8f)
            arcTo(1.3f, 1.3f, 0f, false, true, 8.967f, 21.8f)
            lineTo(8.033f, 21.2f)
            arcTo(1.3f, 1.3f, 0f, false, false, 6.633f, 21.2f)
            lineTo(5.7f, 21.8f)
            arcTo(1.3f, 1.3f, 0f, false, true, 5f, 22f)
            arcTo(1f, 1f, 0f, false, true, 4f, 21f)
            close()
        }
    }.build().also { cachedReceipt = it }

/** `Receipt` vector, built on first access and reused afterwards. */
private var cachedReceipt: ImageVector? = null
