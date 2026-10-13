package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Building pavilion icon.
 *
 * Converted from Tabler Icons `building-pavilion` (MIT).
 */
public val ArecaIcons.BuildingPavilion: ImageVector
    get() = cachedBuildingPavilion ?: ImageVector.Builder(
        name = "BuildingPavilion",
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
            horizontalLineTo(10f)
            verticalLineTo(18f)
            arcTo(2f, 2f, 0f, false, true, 14f, 18f)
            verticalLineTo(21f)
            horizontalLineTo(21f)
            moveTo(6f, 21f)
            lineTo(6f, 12f)
            moveTo(18f, 21f)
            lineTo(18f, 12f)
            moveTo(6f, 12f)
            horizontalLineTo(18f)
            arcTo(3f, 3f, 0f, false, false, 21f, 9f)
            arcTo(9f, 8f, 0f, false, true, 12f, 3f)
            arcTo(9f, 8f, 0f, false, true, 3f, 9f)
            arcTo(3f, 3f, 0f, false, false, 6f, 12f)
        }
    }.build().also { cachedBuildingPavilion = it }

/** `BuildingPavilion` vector, built on first access and reused afterwards. */
private var cachedBuildingPavilion: ImageVector? = null
