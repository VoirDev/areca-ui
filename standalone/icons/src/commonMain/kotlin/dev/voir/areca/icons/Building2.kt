package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Building complex icon.
 *
 * Converted from Lucide `building-complex` (ISC).
 */
public val ArecaIcons.Building2: ImageVector
    get() = cachedBuilding2 ?: ImageVector.Builder(
        name = "Building2",
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
            moveTo(10f, 12f)
            horizontalLineTo(14f)
            moveTo(10f, 8f)
            horizontalLineTo(14f)
            moveTo(14f, 21f)
            verticalLineTo(18f)
            arcTo(2f, 2f, 0f, false, false, 10f, 18f)
            verticalLineTo(21f)
            moveTo(6f, 10f)
            horizontalLineTo(4f)
            arcTo(2f, 2f, 0f, false, false, 2f, 12f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, false, 4f, 21f)
            horizontalLineTo(20f)
            arcTo(2f, 2f, 0f, false, false, 22f, 19f)
            verticalLineTo(9f)
            arcTo(2f, 2f, 0f, false, false, 20f, 7f)
            horizontalLineTo(18f)
            moveTo(6f, 21f)
            verticalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 8f, 3f)
            horizontalLineTo(16f)
            arcTo(2f, 2f, 0f, false, true, 18f, 5f)
            verticalLineTo(21f)
        }
    }.build().also { cachedBuilding2 = it }

/** `Building2` vector, built on first access and reused afterwards. */
private var cachedBuilding2: ImageVector? = null
