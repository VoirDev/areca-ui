package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Credit card icon.
 *
 * Converted from Lucide `credit-card` (ISC).
 */
public val ArecaIcons.CreditCard: ImageVector
    get() = cachedCreditCard ?: ImageVector.Builder(
        name = "CreditCard",
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
            moveTo(4f, 5f)
            horizontalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 22f, 7f)
            verticalLineTo(17f)
            arcTo(2f, 2f, 0f, false, true, 20f, 19f)
            horizontalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 2f, 17f)
            verticalLineTo(7f)
            arcTo(2f, 2f, 0f, false, true, 4f, 5f)
            close()
            moveTo(2f, 10f)
            lineTo(22f, 10f)
            moveTo(6f, 14f)
            horizontalLineTo(8f)
        }
    }.build().also { cachedCreditCard = it }

/** `CreditCard` vector, built on first access and reused afterwards. */
private var cachedCreditCard: ImageVector? = null
