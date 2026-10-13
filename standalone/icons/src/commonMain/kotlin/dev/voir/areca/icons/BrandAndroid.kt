package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Android logo.
 *
 * Converted from Tabler Icons `brand-android` (MIT).
 */
public val ArecaIcons.BrandAndroid: ImageVector
    get() = cachedBrandAndroid ?: ImageVector.Builder(
        name = "BrandAndroid",
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
            moveTo(4f, 10f)
            lineTo(4f, 16f)
            moveTo(20f, 10f)
            lineTo(20f, 16f)
            moveTo(7f, 9f)
            horizontalLineTo(17f)
            verticalLineTo(17f)
            arcTo(1f, 1f, 0f, false, true, 16f, 18f)
            horizontalLineTo(8f)
            arcTo(1f, 1f, 0f, false, true, 7f, 17f)
            verticalLineTo(9f)
            arcTo(5f, 5f, 0f, false, true, 17f, 9f)
            moveTo(8f, 3f)
            lineTo(9f, 5f)
            moveTo(16f, 3f)
            lineTo(15f, 5f)
            moveTo(9f, 18f)
            lineTo(9f, 21f)
            moveTo(15f, 18f)
            lineTo(15f, 21f)
        }
    }.build().also { cachedBrandAndroid = it }

/** `BrandAndroid` vector, built on first access and reused afterwards. */
private var cachedBrandAndroid: ImageVector? = null
