package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Microscope icon.
 *
 * Converted from Lucide `microscope` (ISC).
 */
public val ArecaIcons.Microscope: ImageVector
    get() = cachedMicroscope ?: ImageVector.Builder(
        name = "Microscope",
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
            moveTo(6f, 18f)
            horizontalLineTo(14f)
            moveTo(3f, 22f)
            horizontalLineTo(21f)
            moveTo(14f, 22f)
            arcTo(7f, 7f, 0f, true, false, 14f, 8f)
            horizontalLineTo(13f)
            moveTo(9f, 14f)
            horizontalLineTo(11f)
            moveTo(9f, 12f)
            arcTo(2f, 2f, 0f, false, true, 7f, 10f)
            verticalLineTo(6f)
            horizontalLineTo(13f)
            verticalLineTo(10f)
            arcTo(2f, 2f, 0f, false, true, 11f, 12f)
            close()
            moveTo(12f, 6f)
            verticalLineTo(3f)
            arcTo(1f, 1f, 0f, false, false, 11f, 2f)
            horizontalLineTo(9f)
            arcTo(1f, 1f, 0f, false, false, 8f, 3f)
            verticalLineTo(6f)
        }
    }.build().also { cachedMicroscope = it }

/** `Microscope` vector, built on first access and reused afterwards. */
private var cachedMicroscope: ImageVector? = null
