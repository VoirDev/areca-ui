package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Tools icon.
 *
 * Converted from Tabler Icons `tools` (MIT).
 */
public val ArecaIcons.Tools: ImageVector
    get() = cachedTools ?: ImageVector.Builder(
        name = "Tools",
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
            moveTo(3f, 21f)
            horizontalLineTo(7f)
            lineTo(20f, 8f)
            arcTo(1.5f, 1.5f, 0f, false, false, 16f, 4f)
            lineTo(3f, 17f)
            verticalLineTo(21f)
            moveTo(14.5f, 5.5f)
            lineTo(18.5f, 9.5f)
            moveTo(12f, 8f)
            lineTo(7f, 3f)
            lineTo(3f, 7f)
            lineTo(8f, 12f)
            moveTo(7f, 8f)
            lineTo(5.5f, 9.5f)
            moveTo(16f, 12f)
            lineTo(21f, 17f)
            lineTo(17f, 21f)
            lineTo(12f, 16f)
            moveTo(16f, 17f)
            lineTo(14.5f, 18.5f)
        }
    }.build().also { cachedTools = it }

/** `Tools` vector, built on first access and reused afterwards. */
private var cachedTools: ImageVector? = null
