package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Credit card refund icon.
 *
 * Converted from Tabler Icons `credit-card-refund` (MIT).
 */
public val ArecaIcons.CreditCardRefresh: ImageVector
    get() = cachedCreditCardRefresh ?: ImageVector.Builder(
        name = "CreditCardRefresh",
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
            moveTo(12f, 19f)
            horizontalLineTo(6f)
            arcTo(3f, 3f, 0f, false, true, 3f, 16f)
            verticalLineTo(8f)
            arcTo(3f, 3f, 0f, false, true, 6f, 5f)
            horizontalLineTo(18f)
            arcTo(3f, 3f, 0f, false, true, 21f, 8f)
            verticalLineTo(12.5f)
            moveTo(3f, 10f)
            horizontalLineTo(21f)
            moveTo(7f, 15f)
            horizontalLineTo(7.01f)
            moveTo(11f, 15f)
            horizontalLineTo(13f)
            moveTo(16f, 19f)
            horizontalLineTo(22f)
            moveTo(19f, 16f)
            lineTo(16f, 19f)
            lineTo(19f, 22f)
        }
    }.build().also { cachedCreditCardRefresh = it }

/** `CreditCardRefresh` vector, built on first access and reused afterwards. */
private var cachedCreditCardRefresh: ImageVector? = null
