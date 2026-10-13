package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Trash icon.
 *
 * Converted from Tabler Icons `trash` (MIT).
 */
public val ArecaIcons.TrashAlt: ImageVector
    get() = cachedTrashAlt ?: ImageVector.Builder(
        name = "TrashAlt",
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
            moveTo(4f, 7f)
            lineTo(20f, 7f)
            moveTo(10f, 11f)
            lineTo(10f, 17f)
            moveTo(14f, 11f)
            lineTo(14f, 17f)
            moveTo(5f, 7f)
            lineTo(6f, 19f)
            arcTo(2f, 2f, 0f, false, false, 8f, 21f)
            horizontalLineTo(16f)
            arcTo(2f, 2f, 0f, false, false, 18f, 19f)
            lineTo(19f, 7f)
            moveTo(9f, 7f)
            verticalLineTo(4f)
            arcTo(1f, 1f, 0f, false, true, 10f, 3f)
            horizontalLineTo(14f)
            arcTo(1f, 1f, 0f, false, true, 15f, 4f)
            verticalLineTo(7f)
        }
    }.build().also { cachedTrashAlt = it }

/** `TrashAlt` vector, built on first access and reused afterwards. */
private var cachedTrashAlt: ImageVector? = null
