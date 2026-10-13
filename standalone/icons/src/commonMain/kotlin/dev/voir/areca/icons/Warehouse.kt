package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Warehouse icon.
 *
 * Converted from Lucide `warehouse` (ISC).
 */
public val ArecaIcons.Warehouse: ImageVector
    get() = cachedWarehouse ?: ImageVector.Builder(
        name = "Warehouse",
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
            moveTo(18f, 21f)
            verticalLineTo(10f)
            arcTo(1f, 1f, 0f, false, false, 17f, 9f)
            horizontalLineTo(7f)
            arcTo(1f, 1f, 0f, false, false, 6f, 10f)
            verticalLineTo(21f)
            moveTo(22f, 19f)
            arcTo(2f, 2f, 0f, false, true, 20f, 21f)
            horizontalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 2f, 19f)
            verticalLineTo(8f)
            arcTo(2f, 2f, 0f, false, true, 3.132f, 6.197f)
            lineTo(11.082f, 2.223f)
            arcTo(2f, 2f, 0f, false, true, 12.919f, 2.223f)
            lineTo(20.867f, 6.197f)
            arcTo(2f, 2f, 0f, false, true, 22f, 8f)
            close()
            moveTo(6f, 13f)
            horizontalLineTo(18f)
            moveTo(6f, 17f)
            horizontalLineTo(18f)
        }
    }.build().also { cachedWarehouse = it }

/** `Warehouse` vector, built on first access and reused afterwards. */
private var cachedWarehouse: ImageVector? = null
