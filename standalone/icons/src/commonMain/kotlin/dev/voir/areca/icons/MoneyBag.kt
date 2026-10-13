package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Moneybag icon.
 *
 * Converted from Tabler Icons `moneybag` (MIT).
 */
public val ArecaIcons.MoneyBag: ImageVector
    get() = cachedMoneyBag ?: ImageVector.Builder(
        name = "MoneyBag",
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
            moveTo(9.5f, 3f)
            horizontalLineTo(14.5f)
            arcTo(1.5f, 1.5f, 0f, false, true, 16f, 4.5f)
            arcTo(3.5f, 3.5f, 0f, false, true, 12.5f, 8f)
            horizontalLineTo(11.5f)
            arcTo(3.5f, 3.5f, 0f, false, true, 8f, 4.5f)
            arcTo(1.5f, 1.5f, 0f, false, true, 9.5f, 3f)
            moveTo(4f, 17f)
            verticalLineTo(16f)
            arcTo(8f, 8f, 0f, true, true, 20f, 16f)
            verticalLineTo(17f)
            arcTo(4f, 4f, 0f, false, true, 16f, 21f)
            horizontalLineTo(8f)
            arcTo(4f, 4f, 0f, false, true, 4f, 17f)
        }
    }.build().also { cachedMoneyBag = it }

/** `MoneyBag` vector, built on first access and reused afterwards. */
private var cachedMoneyBag: ImageVector? = null
