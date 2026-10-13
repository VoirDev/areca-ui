package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Buildings icon.
 *
 * Converted from Tabler Icons `buildings` (MIT).
 */
public val ArecaIcons.Buildings: ImageVector
    get() = cachedBuildings ?: ImageVector.Builder(
        name = "Buildings",
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
            moveTo(4f, 21f)
            verticalLineTo(6f)
            curveTo(4f, 5f, 5f, 4f, 6f, 4f)
            horizontalLineTo(11f)
            curveTo(12f, 4f, 13f, 5f, 13f, 6f)
            verticalLineTo(21f)
            moveTo(16f, 8f)
            horizontalLineTo(18f)
            curveTo(19f, 8f, 20f, 9f, 20f, 10f)
            verticalLineTo(21f)
            moveTo(3f, 21f)
            horizontalLineTo(21f)
            moveTo(10f, 12f)
            verticalLineTo(12.01f)
            moveTo(10f, 16f)
            verticalLineTo(16.01f)
            moveTo(10f, 8f)
            verticalLineTo(8.01f)
            moveTo(7f, 12f)
            verticalLineTo(12.01f)
            moveTo(7f, 16f)
            verticalLineTo(16.01f)
            moveTo(7f, 8f)
            verticalLineTo(8.01f)
            moveTo(17f, 12f)
            verticalLineTo(12.01f)
            moveTo(17f, 16f)
            verticalLineTo(16.01f)
        }
    }.build().also { cachedBuildings = it }

/** `Buildings` vector, built on first access and reused afterwards. */
private var cachedBuildings: ImageVector? = null
