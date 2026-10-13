package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Cat icon.
 *
 * Converted from Lucide `cat` (ISC).
 */
public val ArecaIcons.Cat: ImageVector
    get() = cachedCat ?: ImageVector.Builder(
        name = "Cat",
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
            moveTo(12f, 5f)
            curveTo(12.67f, 5f, 13.35f, 5.09f, 14f, 5.26f)
            curveTo(15.78f, 3.26f, 19.03f, 2.42f, 20.42f, 3f)
            curveTo(21.82f, 3.58f, 20f, 10f, 20f, 10f)
            curveTo(20.57f, 11.07f, 21f, 12.24f, 21f, 13.44f)
            curveTo(21f, 17.9f, 16.97f, 21f, 12f, 21f)
            reflectiveCurveTo(3f, 18f, 3f, 13.44f)
            curveTo(3f, 12.19f, 3.5f, 11.04f, 4f, 10f)
            curveTo(4f, 10f, 2.11f, 3.58f, 3.5f, 3f)
            curveTo(4.89f, 2.42f, 8.22f, 3.23f, 10f, 5.23f)
            arcTo(9.04f, 9.04f, 0f, false, true, 12f, 5f)
            close()
            moveTo(8f, 14f)
            verticalLineTo(14.5f)
            moveTo(16f, 14f)
            verticalLineTo(14.5f)
            moveTo(11.25f, 16.25f)
            horizontalLineTo(12.75f)
            lineTo(12f, 17f)
            lineTo(11.25f, 16.25f)
            close()
        }
    }.build().also { cachedCat = it }

/** `Cat` vector, built on first access and reused afterwards. */
private var cachedCat: ImageVector? = null
