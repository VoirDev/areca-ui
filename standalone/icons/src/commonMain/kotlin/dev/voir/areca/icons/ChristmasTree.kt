package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Christmas tree icon.
 *
 * Converted from Tabler Icons `christmas-tree` (MIT).
 */
public val ArecaIcons.ChristmasTree: ImageVector
    get() = cachedChristmasTree ?: ImageVector.Builder(
        name = "ChristmasTree",
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
            moveTo(12f, 3f)
            lineTo(16f, 7f)
            lineTo(14f, 8f)
            lineTo(18f, 12f)
            lineTo(15f, 13f)
            lineTo(19f, 17f)
            horizontalLineTo(5f)
            lineTo(9f, 13f)
            lineTo(6f, 12f)
            lineTo(10f, 8f)
            lineTo(8f, 7f)
            lineTo(12f, 3f)
            moveTo(14f, 17f)
            verticalLineTo(20f)
            arcTo(1f, 1f, 0f, false, true, 13f, 21f)
            horizontalLineTo(11f)
            arcTo(1f, 1f, 0f, false, true, 10f, 20f)
            verticalLineTo(17f)
        }
    }.build().also { cachedChristmasTree = it }

/** `ChristmasTree` vector, built on first access and reused afterwards. */
private var cachedChristmasTree: ImageVector? = null
