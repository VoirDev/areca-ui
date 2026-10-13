package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Headset icon.
 *
 * Converted from Lucide `headset` (ISC).
 */
public val ArecaIcons.Headset: ImageVector
    get() = cachedHeadset ?: ImageVector.Builder(
        name = "Headset",
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
            moveTo(3f, 11f)
            horizontalLineTo(6f)
            arcTo(2f, 2f, 0f, false, true, 8f, 13f)
            verticalLineTo(16f)
            arcTo(2f, 2f, 0f, false, true, 6f, 18f)
            horizontalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 3f, 16f)
            verticalLineTo(11f)
            close()
            moveTo(3f, 11f)
            arcTo(9f, 9f, 0f, true, true, 21f, 11f)
            moveTo(21f, 11f)
            verticalLineTo(16f)
            arcTo(2f, 2f, 0f, false, true, 19f, 18f)
            horizontalLineTo(18f)
            arcTo(2f, 2f, 0f, false, true, 16f, 16f)
            verticalLineTo(13f)
            arcTo(2f, 2f, 0f, false, true, 18f, 11f)
            horizontalLineTo(21f)
            close()
            moveTo(21f, 16f)
            verticalLineTo(18f)
            arcTo(4f, 4f, 0f, false, true, 17f, 22f)
            horizontalLineTo(12f)
        }
    }.build().also { cachedHeadset = it }

/** `Headset` vector, built on first access and reused afterwards. */
private var cachedHeadset: ImageVector? = null
