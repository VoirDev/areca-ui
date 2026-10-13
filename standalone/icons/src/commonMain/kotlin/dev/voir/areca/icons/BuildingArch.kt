package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Building arch icon.
 *
 * Converted from Tabler Icons `building-arch` (MIT).
 */
public val ArecaIcons.BuildingArch: ImageVector
    get() = cachedBuildingArch ?: ImageVector.Builder(
        name = "BuildingArch",
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
            moveTo(4f, 21f)
            verticalLineTo(6f)
            arcTo(2f, 2f, 0f, false, true, 6f, 4f)
            horizontalLineTo(18f)
            arcTo(2f, 2f, 0f, false, true, 20f, 6f)
            verticalLineTo(21f)
            moveTo(9f, 21f)
            verticalLineTo(13f)
            arcTo(3f, 3f, 0f, false, true, 15f, 13f)
            verticalLineTo(21f)
        }
    }.build().also { cachedBuildingArch = it }

/** `BuildingArch` vector, built on first access and reused afterwards. */
private var cachedBuildingArch: ImageVector? = null
