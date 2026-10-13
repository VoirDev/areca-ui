package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Wallet minimal icon.
 *
 * Same drawing as `ArecaIcons.WalletMinimal`; both names are kept.
 *
 * Converted from Lucide `wallet-minimal` (ISC).
 */
public val ArecaIcons.WalletClassic: ImageVector
    get() = cachedWalletClassic ?: ImageVector.Builder(
        name = "WalletClassic",
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
            moveTo(17f, 14f)
            horizontalLineTo(17.01f)
            moveTo(7f, 7f)
            horizontalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 21f, 9f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 19f, 21f)
            horizontalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 3f, 19f)
            verticalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 5f, 3f)
            horizontalLineTo(19f)
        }
    }.build().also { cachedWalletClassic = it }

/** `WalletClassic` vector, built on first access and reused afterwards. */
private var cachedWalletClassic: ImageVector? = null
