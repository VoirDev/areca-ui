package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Old icon.
 *
 * Converted from Tabler Icons `old` (MIT).
 */
public val ArecaIcons.Old: ImageVector
    get() = cachedOld ?: ImageVector.Builder(
        name = "Old",
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
            moveTo(11f, 21f)
            lineTo(10f, 17f)
            lineTo(8f, 14f)
            verticalLineTo(8f)
            moveTo(5f, 14f)
            lineTo(4f, 11f)
            lineTo(8f, 8f)
            lineTo(11f, 10f)
            lineTo(14f, 10.5f)
            moveTo(7f, 4f)
            arcTo(1f, 1f, 0f, true, false, 9f, 4f)
            arcTo(1f, 1f, 0f, true, false, 7f, 4f)
            moveTo(7f, 17f)
            lineTo(5f, 21f)
            moveTo(16f, 21f)
            verticalLineTo(12.5f)
            arcTo(1.5f, 1.5f, 0f, false, true, 19f, 12.5f)
            verticalLineTo(13f)
        }
    }.build().also { cachedOld = it }

/** `Old` vector, built on first access and reused afterwards. */
private var cachedOld: ImageVector? = null
