package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Fish icon.
 *
 * Converted from Lucide `fish` (ISC).
 */
public val ArecaIcons.Fish: ImageVector
    get() = cachedFish ?: ImageVector.Builder(
        name = "Fish",
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
            moveTo(6.5f, 12f)
            curveTo(7.44f, 8.54f, 11.44f, 6f, 15f, 6f)
            curveTo(18.56f, 6f, 21.06f, 8.54f, 22f, 12f)
            curveTo(21.06f, 15.47f, 18.56f, 18f, 15f, 18f)
            reflectiveCurveTo(7.44f, 15.47f, 6.5f, 12f)
            close()
            moveTo(18f, 12f)
            verticalLineTo(12.5f)
            moveTo(16f, 17.93f)
            arcTo(9.77f, 9.77f, 0f, false, true, 16f, 6.07f)
            moveTo(7f, 10.67f)
            curveTo(7f, 8f, 5.58f, 5.97f, 2.73f, 5.5f)
            curveTo(1.73f, 7f, 1.73f, 10.5f, 2.96f, 12f)
            curveTo(1.72f, 13.5f, 1.72f, 17f, 2.73f, 18.5f)
            curveTo(5.58f, 18.03f, 7f, 16f, 7f, 13.33f)
            moveTo(10.46f, 7.26f)
            curveTo(10.2f, 5.88f, 9.17f, 4.24f, 8f, 3f)
            horizontalLineTo(13.8f)
            arcTo(2f, 2f, 0f, false, true, 15.78f, 4.67f)
            lineTo(16.01f, 6.07f)
            moveTo(16.01f, 17.93f)
            lineTo(15.78f, 19.33f)
            arcTo(2f, 2f, 0f, false, true, 13.8f, 21f)
            horizontalLineTo(9.5f)
            arcTo(5.96f, 5.96f, 0f, false, false, 10.99f, 17.02f)
        }
    }.build().also { cachedFish = it }

/** `Fish` vector, built on first access and reused afterwards. */
private var cachedFish: ImageVector? = null
