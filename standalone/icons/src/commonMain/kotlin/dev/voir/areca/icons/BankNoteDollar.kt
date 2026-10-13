package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Cash banknote icon.
 *
 * Same drawing as `ArecaIcons.CashBanknote`; both names are kept.
 *
 * Converted from Tabler Icons `cash-banknote` (MIT).
 */
public val ArecaIcons.BankNoteDollar: ImageVector
    get() = cachedBankNoteDollar ?: ImageVector.Builder(
        name = "BankNoteDollar",
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
            moveTo(9f, 12f)
            arcTo(3f, 3f, 0f, true, false, 15f, 12f)
            arcTo(3f, 3f, 0f, false, false, 9f, 12f)
            moveTo(3f, 8f)
            arcTo(2f, 2f, 0f, false, true, 5f, 6f)
            horizontalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 21f, 8f)
            verticalLineTo(16f)
            arcTo(2f, 2f, 0f, false, true, 19f, 18f)
            horizontalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 3f, 16f)
            lineTo(3f, 8f)
            moveTo(18f, 12f)
            horizontalLineTo(18.01f)
            moveTo(6f, 12f)
            horizontalLineTo(6.01f)
        }
    }.build().also { cachedBankNoteDollar = it }

/** `BankNoteDollar` vector, built on first access and reused afterwards. */
private var cachedBankNoteDollar: ImageVector? = null
