package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Building monument icon.
 *
 * Converted from Tabler Icons `building-monument` (MIT).
 */
public val ArecaIcons.BuildingMonument: ImageVector
    get() = cachedBuildingMonument ?: ImageVector.Builder(
        name = "BuildingMonument",
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
            moveTo(8f, 18f)
            lineTo(10f, 5f)
            lineTo(12f, 3f)
            lineTo(14f, 5f)
            lineTo(16f, 18f)
            moveTo(5f, 21f)
            verticalLineTo(18f)
            horizontalLineTo(19f)
            verticalLineTo(21f)
            moveTo(3f, 21f)
            lineTo(21f, 21f)
        }
    }.build().also { cachedBuildingMonument = it }

/** `BuildingMonument` vector, built on first access and reused afterwards. */
private var cachedBuildingMonument: ImageVector? = null
