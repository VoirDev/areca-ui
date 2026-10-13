package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Apple logo.
 *
 * Converted from Tabler Icons `brand-apple` (MIT).
 */
public val ArecaIcons.BrandApple: ImageVector
    get() = cachedBrandApple ?: ImageVector.Builder(
        name = "BrandApple",
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
            moveTo(8.286f, 7.008f)
            curveTo(5.07f, 7.008f, 4f, 10.238f, 4f, 12.928f)
            curveTo(4f, 16.157f, 6.143f, 21f, 8.286f, 21f)
            curveTo(9.451f, 20.95f, 10.085f, 20.462f, 11.5f, 20.462f)
            curveTo(12.906f, 20.462f, 13.107f, 21f, 14.714f, 21f)
            reflectiveCurveTo(19f, 17.771f, 19f, 15.619f)
            curveTo(18.97f, 15.608f, 16.351f, 15.185f, 16.321f, 12.389f)
            curveTo(16.301f, 10.054f, 18.91f, 9.21f, 19f, 9.161f)
            curveTo(17.904f, 7.555f, 15.838f, 7.048f, 15.25f, 7.008f)
            curveTo(13.715f, 6.888f, 12.218f, 8.085f, 11.5f, 8.085f)
            curveTo(10.771f, 8.085f, 9.464f, 7.008f, 8.286f, 7.008f)
            moveTo(12f, 4f)
            arcTo(2f, 2f, 0f, false, false, 14f, 2f)
            arcTo(2f, 2f, 0f, false, false, 12f, 4f)
        }
    }.build().also { cachedBrandApple = it }

/** `BrandApple` vector, built on first access and reused afterwards. */
private var cachedBrandApple: ImageVector? = null
