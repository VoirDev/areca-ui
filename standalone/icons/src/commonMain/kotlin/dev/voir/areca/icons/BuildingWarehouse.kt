package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Building warehouse icon.
 *
 * Converted from Tabler Icons `building-warehouse` (MIT).
 */
public val ArecaIcons.BuildingWarehouse: ImageVector
    get() = cachedBuildingWarehouse ?: ImageVector.Builder(
        name = "BuildingWarehouse",
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
            verticalLineTo(8f)
            lineTo(12f, 4f)
            lineTo(21f, 8f)
            verticalLineTo(21f)
            moveTo(13f, 13f)
            horizontalLineTo(17f)
            verticalLineTo(21f)
            horizontalLineTo(7f)
            verticalLineTo(15f)
            horizontalLineTo(13f)
            moveTo(13f, 21f)
            verticalLineTo(12f)
            arcTo(1f, 1f, 0f, false, false, 12f, 11f)
            horizontalLineTo(10f)
            arcTo(1f, 1f, 0f, false, false, 9f, 12f)
            verticalLineTo(15f)
        }
    }.build().also { cachedBuildingWarehouse = it }

/** `BuildingWarehouse` vector, built on first access and reused afterwards. */
private var cachedBuildingWarehouse: ImageVector? = null
