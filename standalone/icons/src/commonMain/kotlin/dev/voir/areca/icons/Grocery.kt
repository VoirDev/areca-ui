package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Store icon.
 *
 * Same drawing as `ArecaIcons.Store`; both names are kept.
 *
 * Converted from Lucide `store` (ISC).
 */
public val ArecaIcons.Grocery: ImageVector
    get() = cachedGrocery ?: ImageVector.Builder(
        name = "Grocery",
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
            moveTo(15f, 21f)
            verticalLineTo(16f)
            arcTo(1f, 1f, 0f, false, false, 14f, 15f)
            horizontalLineTo(10f)
            arcTo(1f, 1f, 0f, false, false, 9f, 16f)
            verticalLineTo(21f)
            moveTo(17.774f, 10.31f)
            arcTo(1.12f, 1.12f, 0f, false, false, 16.225f, 10.31f)
            arcTo(2.5f, 2.5f, 0f, false, true, 12.774f, 10.31f)
            arcTo(1.12f, 1.12f, 0f, false, false, 11.226f, 10.31f)
            arcTo(2.5f, 2.5f, 0f, false, true, 7.774f, 10.31f)
            arcTo(1.12f, 1.12f, 0f, false, false, 6.225f, 10.31f)
            arcTo(2.5f, 2.5f, 0f, false, true, 2.455f, 7.062f)
            lineTo(5.344f, 2.878f)
            arcTo(2f, 2f, 0f, false, true, 7f, 2f)
            horizontalLineTo(17f)
            arcTo(2f, 2f, 0f, false, true, 18.653f, 2.873f)
            lineTo(21.548f, 7.065f)
            arcTo(2.5f, 2.5f, 0f, false, true, 17.774f, 10.309f)
            moveTo(4f, 10.95f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, false, 6f, 21f)
            horizontalLineTo(18f)
            arcTo(2f, 2f, 0f, false, false, 20f, 19f)
            verticalLineTo(10.95f)
        }
    }.build().also { cachedGrocery = it }

/** `Grocery` vector, built on first access and reused afterwards. */
private var cachedGrocery: ImageVector? = null
