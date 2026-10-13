package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Wallet icon.
 *
 * Converted from Tabler Icons `wallet` (MIT).
 */
public val ArecaIcons.WalletAlt: ImageVector
    get() = cachedWalletAlt ?: ImageVector.Builder(
        name = "WalletAlt",
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
            moveTo(17f, 8f)
            verticalLineTo(5f)
            arcTo(1f, 1f, 0f, false, false, 16f, 4f)
            horizontalLineTo(6f)
            arcTo(2f, 2f, 0f, false, false, 6f, 8f)
            horizontalLineTo(18f)
            arcTo(1f, 1f, 0f, false, true, 19f, 9f)
            verticalLineTo(12f)
            moveTo(19f, 16f)
            verticalLineTo(19f)
            arcTo(1f, 1f, 0f, false, true, 18f, 20f)
            horizontalLineTo(6f)
            arcTo(2f, 2f, 0f, false, true, 4f, 18f)
            verticalLineTo(6f)
            moveTo(20f, 12f)
            verticalLineTo(16f)
            horizontalLineTo(16f)
            arcTo(2f, 2f, 0f, false, true, 16f, 12f)
            horizontalLineTo(20f)
        }
    }.build().also { cachedWalletAlt = it }

/** `WalletAlt` vector, built on first access and reused afterwards. */
private var cachedWalletAlt: ImageVector? = null
