package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Cupcake icon.
 *
 * Converted from Lucide `cupcake` (ISC).
 */
public val ArecaIcons.Cupcake: ImageVector
    get() = cachedCupcake ?: ImageVector.Builder(
        name = "Cupcake",
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
            moveTo(12f, 22f)
            verticalLineTo(13f)
            moveTo(14f, 4f)
            horizontalLineTo(15f)
            arcTo(3f, 3f, 0f, false, true, 18f, 7f)
            lineTo(17.996f, 7.125f)
            arcTo(4f, 4f, 0f, false, true, 21f, 11f)
            verticalLineTo(13f)
            moveTo(15.5f, 22f)
            lineTo(17f, 13f)
            moveTo(21f, 13f)
            arcTo(1f, 1f, 0f, false, true, 21.919f, 14.394f)
            lineTo(19.179f, 20.788f)
            arcTo(2f, 2f, 0f, false, true, 17.34f, 22f)
            horizontalLineTo(6.659f)
            arcTo(2f, 2f, 0f, false, true, 4.821f, 20.788f)
            lineTo(2.081f, 14.394f)
            arcTo(1f, 1f, 0f, false, true, 3f, 13f)
            close()
            moveTo(3f, 13f)
            verticalLineTo(11f)
            arcTo(4f, 4f, 0f, false, true, 6.003f, 7.125f)
            lineTo(6f, 7f)
            arcTo(3f, 3f, 0f, false, true, 9f, 4f)
            horizontalLineTo(10f)
            moveTo(8.5f, 22f)
            lineTo(7f, 13f)
            moveTo(10f, 4f)
            arcTo(2f, 2f, 0f, true, true, 14f, 4f)
            arcTo(2f, 2f, 0f, true, true, 10f, 4f)
            close()
        }
    }.build().also { cachedCupcake = it }

/** `Cupcake` vector, built on first access and reused afterwards. */
private var cachedCupcake: ImageVector? = null
