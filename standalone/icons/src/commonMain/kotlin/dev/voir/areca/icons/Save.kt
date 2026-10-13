package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Save icon.
 *
 * Converted from Lucide `save` (ISC).
 */
public val ArecaIcons.Save: ImageVector
    get() = cachedSave ?: ImageVector.Builder(
        name = "Save",
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
            moveTo(15.2f, 3f)
            arcTo(2f, 2f, 0f, false, true, 16.6f, 3.6f)
            lineTo(20.4f, 7.4f)
            arcTo(2f, 2f, 0f, false, true, 21f, 8.8f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 19f, 21f)
            horizontalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 3f, 19f)
            verticalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 5f, 3f)
            close()
            moveTo(17f, 21f)
            verticalLineTo(14f)
            arcTo(1f, 1f, 0f, false, false, 16f, 13f)
            horizontalLineTo(8f)
            arcTo(1f, 1f, 0f, false, false, 7f, 14f)
            verticalLineTo(21f)
            moveTo(7f, 3f)
            verticalLineTo(7f)
            arcTo(1f, 1f, 0f, false, false, 8f, 8f)
            horizontalLineTo(15f)
        }
    }.build().also { cachedSave = it }

/** `Save` vector, built on first access and reused afterwards. */
private var cachedSave: ImageVector? = null
