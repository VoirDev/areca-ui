package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Receipt tax icon.
 *
 * Converted from Tabler Icons `receipt-tax` (MIT).
 */
public val ArecaIcons.ReceiptTax: ImageVector
    get() = cachedReceiptTax ?: ImageVector.Builder(
        name = "ReceiptTax",
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
            moveTo(9f, 14f)
            lineTo(15f, 8f)
        }
        path(
            fill = SolidColor(Color.Black),
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
            moveTo(9f, 8.5f)
            arcTo(0.5f, 0.5f, 0f, true, false, 10f, 8.5f)
            arcTo(0.5f, 0.5f, 0f, true, false, 9f, 8.5f)
        }
        path(
            fill = SolidColor(Color.Black),
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
            moveTo(14f, 13.5f)
            arcTo(0.5f, 0.5f, 0f, true, false, 15f, 13.5f)
            arcTo(0.5f, 0.5f, 0f, true, false, 14f, 13.5f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
            moveTo(5f, 21f)
            verticalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 7f, 3f)
            horizontalLineTo(17f)
            arcTo(2f, 2f, 0f, false, true, 19f, 5f)
            verticalLineTo(21f)
            lineTo(16f, 19f)
            lineTo(14f, 21f)
            lineTo(12f, 19f)
            lineTo(10f, 21f)
            lineTo(8f, 19f)
            lineTo(5f, 21f)
        }
    }.build().also { cachedReceiptTax = it }

/** `ReceiptTax` vector, built on first access and reused afterwards. */
private var cachedReceiptTax: ImageVector? = null
