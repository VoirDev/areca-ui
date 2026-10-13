package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Qr code icon.
 *
 * Converted from Lucide `qr-code` (ISC).
 */
public val ArecaIcons.QrCode: ImageVector
    get() = cachedQrCode ?: ImageVector.Builder(
        name = "QrCode",
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
            moveTo(4f, 3f)
            horizontalLineTo(7f)
            arcTo(1f, 1f, 0f, false, true, 8f, 4f)
            verticalLineTo(7f)
            arcTo(1f, 1f, 0f, false, true, 7f, 8f)
            horizontalLineTo(4f)
            arcTo(1f, 1f, 0f, false, true, 3f, 7f)
            verticalLineTo(4f)
            arcTo(1f, 1f, 0f, false, true, 4f, 3f)
            close()
            moveTo(17f, 3f)
            horizontalLineTo(20f)
            arcTo(1f, 1f, 0f, false, true, 21f, 4f)
            verticalLineTo(7f)
            arcTo(1f, 1f, 0f, false, true, 20f, 8f)
            horizontalLineTo(17f)
            arcTo(1f, 1f, 0f, false, true, 16f, 7f)
            verticalLineTo(4f)
            arcTo(1f, 1f, 0f, false, true, 17f, 3f)
            close()
            moveTo(4f, 16f)
            horizontalLineTo(7f)
            arcTo(1f, 1f, 0f, false, true, 8f, 17f)
            verticalLineTo(20f)
            arcTo(1f, 1f, 0f, false, true, 7f, 21f)
            horizontalLineTo(4f)
            arcTo(1f, 1f, 0f, false, true, 3f, 20f)
            verticalLineTo(17f)
            arcTo(1f, 1f, 0f, false, true, 4f, 16f)
            close()
            moveTo(21f, 16f)
            horizontalLineTo(18f)
            arcTo(2f, 2f, 0f, false, false, 16f, 18f)
            verticalLineTo(21f)
            moveTo(21f, 21f)
            verticalLineTo(21.01f)
            moveTo(12f, 7f)
            verticalLineTo(10f)
            arcTo(2f, 2f, 0f, false, true, 10f, 12f)
            horizontalLineTo(7f)
            moveTo(3f, 12f)
            horizontalLineTo(3.01f)
            moveTo(12f, 3f)
            horizontalLineTo(12.01f)
            moveTo(12f, 16f)
            verticalLineTo(16.01f)
            moveTo(16f, 12f)
            horizontalLineTo(17f)
            moveTo(21f, 12f)
            verticalLineTo(12.01f)
            moveTo(12f, 21f)
            verticalLineTo(20f)
        }
    }.build().also { cachedQrCode = it }

/** `QrCode` vector, built on first access and reused afterwards. */
private var cachedQrCode: ImageVector? = null
