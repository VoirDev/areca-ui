package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Credit card check icon.
 *
 * Converted from Lucide `credit-card-check` (ISC).
 */
public val ArecaIcons.CreditCardCheck: ImageVector
    get() = cachedCreditCardCheck ?: ImageVector.Builder(
        name = "CreditCardCheck",
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
            verticalLineTo(11f)
            moveTo(2f, 10f)
            horizontalLineTo(22f)
            moveTo(6f, 14f)
            horizontalLineTo(8f)
            moveTo(16f, 17f)
            lineTo(18f, 19f)
            lineTo(22f, 15f)
        }
    }.build().also { cachedCreditCardCheck = it }

/** `CreditCardCheck` vector, built on first access and reused afterwards. */
private var cachedCreditCardCheck: ImageVector? = null
