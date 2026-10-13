package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Mosque icon.
 *
 * Converted from Lucide `mosque` (ISC).
 */
public val ArecaIcons.Mosque: ImageVector
    get() = cachedMosque ?: ImageVector.Builder(
        name = "Mosque",
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
            moveTo(12.268f, 2f)
            arcTo(2f, 2f, 0f, false, false, 15.733f, 4f)
            moveTo(14f, 5f)
            lineTo(14f, 8f)
            moveTo(16f, 22f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, false, 12f, 19f)
            verticalLineTo(22f)
            moveTo(21f, 13f)
            curveTo(20.338f, 11.503f, 19.334f, 10.247f, 18.1f, 9.37f)
            curveTo(16.825f, 8.47f, 15.422f, 8f, 14f, 8f)
            reflectiveCurveTo(11.174f, 8.47f, 9.9f, 9.37f)
            curveTo(8.668f, 10.248f, 7.663f, 11.504f, 7f, 13f)
            close()
            moveTo(3f, 9f)
            horizontalLineTo(7f)
            moveTo(7f, 22f)
            verticalLineTo(6f)
            arcTo(5f, 5f, 0f, false, false, 5f, 2f)
            arcTo(5f, 5f, 0f, false, false, 3f, 6f)
            verticalLineTo(20f)
            arcTo(2f, 2f, 0f, false, false, 5f, 22f)
            horizontalLineTo(19f)
            arcTo(2f, 2f, 0f, false, false, 21f, 20f)
            verticalLineTo(13f)
        }
    }.build().also { cachedMosque = it }

/** `Mosque` vector, built on first access and reused afterwards. */
private var cachedMosque: ImageVector? = null
