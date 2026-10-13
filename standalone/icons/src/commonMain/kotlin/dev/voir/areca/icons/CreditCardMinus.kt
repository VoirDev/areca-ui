package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Credit card minus icon.
 *
 * Converted from Lucide `credit-card-minus` (ISC).
 */
public val ArecaIcons.CreditCardMinus: ImageVector
    get() = cachedCreditCardMinus ?: ImageVector.Builder(
        name = "CreditCardMinus",
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
            moveTo(22f, 13f)
            verticalLineTo(7f)
            arcTo(2f, 2f, 0f, false, false, 20f, 5f)
            horizontalLineTo(4f)
            arcTo(2f, 2f, 0f, false, false, 2f, 7f)
            verticalLineTo(17f)
            arcTo(2f, 2f, 0f, false, false, 4f, 19f)
            horizontalLineTo(12.536f)
            moveTo(22f, 10f)
            horizontalLineTo(2f)
            moveTo(6f, 14f)
            horizontalLineTo(8f)
            moveTo(16f, 17f)
            horizontalLineTo(22f)
        }
    }.build().also { cachedCreditCardMinus = it }

/** `CreditCardMinus` vector, built on first access and reused afterwards. */
private var cachedCreditCardMinus: ImageVector? = null
