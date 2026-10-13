package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Bottle icon.
 *
 * Converted from Tabler Icons `bottle` (MIT).
 */
public val ArecaIcons.Bottle: ImageVector
    get() = cachedBottle ?: ImageVector.Builder(
        name = "Bottle",
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
            moveTo(10f, 5f)
            horizontalLineTo(14f)
            verticalLineTo(3f)
            arcTo(1f, 1f, 0f, false, false, 13f, 2f)
            horizontalLineTo(11f)
            arcTo(1f, 1f, 0f, false, false, 10f, 3f)
            verticalLineTo(5f)
            moveTo(14f, 3.5f)
            curveTo(14f, 5.126f, 14.507f, 6.712f, 15.45f, 8.037f)
            lineTo(15.5f, 8.107f)
            arcTo(8.093f, 8.093f, 0f, false, true, 17f, 12.801f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 15f, 21f)
            horizontalLineTo(9f)
            arcTo(2f, 2f, 0f, false, true, 7f, 19f)
            verticalLineTo(12.8f)
            curveTo(7f, 11.118f, 7.524f, 9.478f, 8.5f, 8.107f)
            lineTo(8.55f, 8.037f)
            arcTo(7.823f, 7.823f, 0f, false, false, 10f, 3.5f)
            moveTo(7f, 14.803f)
            arcTo(2.4f, 2.4f, 0f, false, false, 8f, 14f)
            arcTo(2.4f, 2.4f, 0f, false, true, 10f, 13f)
            arcTo(2.4f, 2.4f, 0f, false, true, 12f, 14f)
            arcTo(2.4f, 2.4f, 0f, false, false, 14f, 15f)
            arcTo(2.4f, 2.4f, 0f, false, false, 16f, 14f)
            arcTo(2.4f, 2.4f, 0f, false, true, 17f, 13.195f)
        }
    }.build().also { cachedBottle = it }

/** `Bottle` vector, built on first access and reused afterwards. */
private var cachedBottle: ImageVector? = null
