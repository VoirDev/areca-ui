package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Dog icon.
 *
 * Converted from Lucide `dog` (ISC).
 */
public val ArecaIcons.Dog: ImageVector
    get() = cachedDog ?: ImageVector.Builder(
        name = "Dog",
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
            moveTo(11.25f, 16.25f)
            horizontalLineTo(12.75f)
            lineTo(12f, 17f)
            close()
            moveTo(16f, 14f)
            verticalLineTo(14.5f)
            moveTo(4.42f, 11.247f)
            arcTo(13.152f, 13.152f, 0f, false, false, 4f, 14.556f)
            curveTo(4f, 18.728f, 7.582f, 21f, 12f, 21f)
            reflectiveCurveTo(20f, 18.728f, 20f, 14.556f)
            arcTo(11.702f, 11.702f, 0f, false, false, 19.507f, 11.247f)
            moveTo(8f, 14f)
            verticalLineTo(14.5f)
            moveTo(8.5f, 8.5f)
            curveTo(8.116f, 9.55f, 7.417f, 10.528f, 6.156f, 11f)
            curveTo(4.225f, 11.722f, 2.58f, 10.703f, 2.5f, 10f)
            curveTo(2.387f, 9.006f, 3.677f, 3.47f, 6.5f, 3f)
            curveTo(8.423f, 2.679f, 10.151f, 3.845f, 10.151f, 5.235f)
            arcTo(7.497f, 7.497f, 0f, false, true, 14f, 5.277f)
            curveTo(14f, 3.887f, 15.844f, 2.679f, 17.767f, 3f)
            curveTo(20.59f, 3.47f, 21.88f, 9.006f, 21.767f, 10f)
            curveTo(21.687f, 10.703f, 20.042f, 11.722f, 18.111f, 11f)
            curveTo(16.85f, 10.528f, 16.256f, 9.55f, 15.872f, 8.5f)
        }
    }.build().also { cachedDog = it }

/** `Dog` vector, built on first access and reused afterwards. */
private var cachedDog: ImageVector? = null
