package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Credit card x icon.
 *
 * Converted from Lucide `credit-card-x` (ISC).
 */
public val ArecaIcons.CreditCardX: ImageVector
    get() = cachedCreditCardX ?: ImageVector.Builder(
        name = "CreditCardX",
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
            moveTo(12.5f, 19f)
            horizontalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 2f, 17f)
            verticalLineTo(7f)
            arcTo(2f, 2f, 0f, false, true, 4f, 5f)
            horizontalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 22f, 7f)
            verticalLineTo(10.5f)
            moveTo(2f, 10f)
            horizontalLineTo(22f)
            moveTo(6f, 14f)
            horizontalLineTo(8f)
            moveTo(16.5f, 14.5f)
            lineTo(21.5f, 19.5f)
            moveTo(21.5f, 14.5f)
            lineTo(16.5f, 19.5f)
        }
    }.build().also { cachedCreditCardX = it }

/** `CreditCardX` vector, built on first access and reused afterwards. */
private var cachedCreditCardX: ImageVector? = null
