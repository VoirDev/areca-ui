package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Photo check icon.
 *
 * Converted from Tabler Icons `photo-check` (MIT).
 */
public val ArecaIcons.ImageCheck: ImageVector
    get() = cachedImageCheck ?: ImageVector.Builder(
        name = "ImageCheck",
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
            moveTo(15f, 8f)
            horizontalLineTo(15.01f)
            moveTo(11.5f, 21f)
            horizontalLineTo(6f)
            arcTo(3f, 3f, 0f, false, true, 3f, 18f)
            verticalLineTo(6f)
            arcTo(3f, 3f, 0f, false, true, 6f, 3f)
            horizontalLineTo(18f)
            arcTo(3f, 3f, 0f, false, true, 21f, 6f)
            verticalLineTo(13f)
            moveTo(3f, 16f)
            lineTo(8f, 11f)
            curveTo(8.928f, 10.107f, 10.072f, 10.107f, 11f, 11f)
            lineTo(15f, 15f)
            moveTo(14f, 14f)
            lineTo(15f, 13f)
            curveTo(15.928f, 12.107f, 17.072f, 12.107f, 18f, 13f)
            lineTo(18.5f, 13.5f)
            moveTo(15f, 19f)
            lineTo(17f, 21f)
            lineTo(21f, 17f)
        }
    }.build().also { cachedImageCheck = it }

/** `ImageCheck` vector, built on first access and reused afterwards. */
private var cachedImageCheck: ImageVector? = null
