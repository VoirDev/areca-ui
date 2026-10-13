package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Credit card reader icon.
 *
 * Converted from Lucide `credit-card-reader` (ISC).
 */
public val ArecaIcons.CreditCardReader: ImageVector
    get() = cachedCreditCardReader ?: ImageVector.Builder(
        name = "CreditCardReader",
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
            moveTo(15f, 16f)
            verticalLineTo(17f)
            moveTo(16.963f, 7.734f)
            arcTo(1f, 1f, 0f, false, false, 15.999f, 7f)
            horizontalLineTo(8.003f)
            arcTo(1f, 1f, 0f, false, false, 7.039f, 7.734f)
            lineTo(4.073f, 18.467f)
            arcTo(2f, 2f, 0f, false, false, 6f, 21f)
            horizontalLineTo(18f)
            arcTo(2f, 2f, 0f, false, false, 19.927f, 18.468f)
            close()
            moveTo(2.678f, 8.5f)
            arcTo(2f, 2f, 0f, false, true, 2f, 7f)
            verticalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 4f, 3f)
            horizontalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 22f, 5f)
            verticalLineTo(7f)
            arcTo(2f, 2f, 0f, false, true, 21.324f, 8.499f)
            moveTo(9f, 21f)
            lineTo(11f, 7f)
        }
    }.build().also { cachedCreditCardReader = it }

/** `CreditCardReader` vector, built on first access and reused afterwards. */
private var cachedCreditCardReader: ImageVector? = null
