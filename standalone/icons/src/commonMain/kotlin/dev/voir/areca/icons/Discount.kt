package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Discount icon.
 *
 * Converted from Tabler Icons `discount` (MIT).
 */
public val ArecaIcons.Discount: ImageVector
    get() = cachedDiscount ?: ImageVector.Builder(
        name = "Discount",
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
            moveTo(9f, 15f)
            lineTo(15f, 9f)
        }
        path(
            fill = SolidColor(Color.Black),
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
            moveTo(9f, 9.5f)
            arcTo(0.5f, 0.5f, 0f, true, false, 10f, 9.5f)
            arcTo(0.5f, 0.5f, 0f, true, false, 9f, 9.5f)
        }
        path(
            fill = SolidColor(Color.Black),
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
            moveTo(14f, 14.5f)
            arcTo(0.5f, 0.5f, 0f, true, false, 15f, 14.5f)
            arcTo(0.5f, 0.5f, 0f, true, false, 14f, 14.5f)
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
            moveTo(3f, 12f)
            arcTo(9f, 9f, 0f, true, false, 21f, 12f)
            arcTo(9f, 9f, 0f, true, false, 3f, 12f)
        }
    }.build().also { cachedDiscount = it }

/** `Discount` vector, built on first access and reused afterwards. */
private var cachedDiscount: ImageVector? = null
