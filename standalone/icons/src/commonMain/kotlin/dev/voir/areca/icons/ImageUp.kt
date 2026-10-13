package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Image up icon.
 *
 * Converted from Lucide `image-up` (ISC).
 */
public val ArecaIcons.ImageUp: ImageVector
    get() = cachedImageUp ?: ImageVector.Builder(
        name = "ImageUp",
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
            moveTo(10.3f, 21f)
            horizontalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 3f, 19f)
            verticalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 5f, 3f)
            horizontalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 21f, 5f)
            verticalLineTo(15f)
            lineTo(17.9f, 11.9f)
            arcTo(2f, 2f, 0f, false, false, 15.086f, 11.914f)
            lineTo(6f, 21f)
            moveTo(14f, 19.5f)
            lineTo(17f, 16.5f)
            lineTo(20f, 19.5f)
            moveTo(17f, 22f)
            verticalLineTo(16.5f)
            moveTo(7f, 9f)
            arcTo(2f, 2f, 0f, true, true, 11f, 9f)
            arcTo(2f, 2f, 0f, true, true, 7f, 9f)
            close()
        }
    }.build().also { cachedImageUp = it }

/** `ImageUp` vector, built on first access and reused afterwards. */
private var cachedImageUp: ImageVector? = null
