package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Building store icon.
 *
 * Converted from Tabler Icons `building-store` (MIT).
 */
public val ArecaIcons.BuildingStore: ImageVector
    get() = cachedBuildingStore ?: ImageVector.Builder(
        name = "BuildingStore",
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
            lineTo(21f, 21f)
            moveTo(3f, 7f)
            verticalLineTo(8f)
            arcTo(3f, 3f, 0f, false, false, 9f, 8f)
            verticalLineTo(7f)
            moveTo(9f, 8f)
            arcTo(3f, 3f, 0f, false, false, 15f, 8f)
            verticalLineTo(7f)
            moveTo(15f, 8f)
            arcTo(3f, 3f, 0f, false, false, 21f, 8f)
            verticalLineTo(7f)
            horizontalLineTo(3f)
            lineTo(5f, 3f)
            horizontalLineTo(19f)
            lineTo(21f, 7f)
            moveTo(5f, 21f)
            lineTo(5f, 10.85f)
            moveTo(19f, 21f)
            lineTo(19f, 10.85f)
            moveTo(9f, 21f)
            verticalLineTo(17f)
            arcTo(2f, 2f, 0f, false, true, 11f, 15f)
            horizontalLineTo(13f)
            arcTo(2f, 2f, 0f, false, true, 15f, 17f)
            verticalLineTo(21f)
        }
    }.build().also { cachedBuildingStore = it }

/** `BuildingStore` vector, built on first access and reused afterwards. */
private var cachedBuildingStore: ImageVector? = null
