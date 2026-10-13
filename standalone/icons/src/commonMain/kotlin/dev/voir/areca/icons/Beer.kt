package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Beer icon.
 *
 * Converted from Lucide `beer` (ISC).
 */
public val ArecaIcons.Beer: ImageVector
    get() = cachedBeer ?: ImageVector.Builder(
        name = "Beer",
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
            moveTo(17f, 11f)
            horizontalLineTo(18f)
            arcTo(3f, 3f, 0f, false, true, 18f, 17f)
            horizontalLineTo(17f)
            moveTo(9f, 12f)
            verticalLineTo(18f)
            moveTo(13f, 12f)
            verticalLineTo(18f)
            moveTo(14f, 7.5f)
            curveTo(13f, 7.5f, 12.56f, 8f, 11f, 8f)
            reflectiveCurveTo(9f, 7.5f, 8f, 7.5f)
            reflectiveCurveTo(6.28f, 8f, 5.5f, 8f)
            arcTo(2.5f, 2.5f, 0f, false, true, 5.5f, 3f)
            curveTo(6.28f, 3f, 7.07f, 3.5f, 8f, 3.5f)
            reflectiveCurveTo(9.44f, 2f, 11f, 2f)
            reflectiveCurveTo(13f, 3.5f, 14f, 3.5f)
            reflectiveCurveTo(15.72f, 3f, 16.5f, 3f)
            arcTo(2.5f, 2.5f, 0f, false, true, 16.5f, 8f)
            curveTo(15.72f, 8f, 15f, 7.5f, 14f, 7.5f)
            close()
            moveTo(5f, 8f)
            verticalLineTo(20f)
            arcTo(2f, 2f, 0f, false, false, 7f, 22f)
            horizontalLineTo(15f)
            arcTo(2f, 2f, 0f, false, false, 17f, 20f)
            verticalLineTo(8f)
        }
    }.build().also { cachedBeer = it }

/** `Beer` vector, built on first access and reused afterwards. */
private var cachedBeer: ImageVector? = null
