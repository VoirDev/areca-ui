package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Badge percent icon.
 *
 * Same drawing as `ArecaIcons.BadgePercent`; both names are kept.
 *
 * Converted from Lucide `badge-percent` (ISC).
 */
public val ArecaIcons.Sale: ImageVector
    get() = cachedSale ?: ImageVector.Builder(
        name = "Sale",
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
            moveTo(3.85f, 8.62f)
            arcTo(4f, 4f, 0f, false, true, 8.63f, 3.85f)
            arcTo(4f, 4f, 0f, false, true, 15.37f, 3.85f)
            arcTo(4f, 4f, 0f, false, true, 20.15f, 8.63f)
            arcTo(4f, 4f, 0f, false, true, 20.15f, 15.37f)
            arcTo(4f, 4f, 0f, false, true, 15.38f, 20.15f)
            arcTo(4f, 4f, 0f, false, true, 8.63f, 20.15f)
            arcTo(4f, 4f, 0f, false, true, 3.85f, 15.38f)
            arcTo(4f, 4f, 0f, false, true, 3.85f, 8.62f)
            close()
            moveTo(15f, 9f)
            lineTo(9f, 15f)
            moveTo(9f, 9f)
            horizontalLineTo(9.01f)
            moveTo(15f, 15f)
            horizontalLineTo(15.01f)
        }
    }.build().also { cachedSale = it }

/** `Sale` vector, built on first access and reused afterwards. */
private var cachedSale: ImageVector? = null
