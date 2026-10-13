package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Rings icon.
 *
 * Converted from Tabler Icons `rings` (MIT).
 */
public val ArecaIcons.Rings: ImageVector
    get() = cachedRings ?: ImageVector.Builder(
        name = "Rings",
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
            moveTo(4f, 17f)
            arcTo(3f, 3f, 0f, true, false, 10f, 17f)
            arcTo(3f, 3f, 0f, true, false, 4f, 17f)
            moveTo(14f, 17f)
            arcTo(3f, 3f, 0f, true, false, 20f, 17f)
            arcTo(3f, 3f, 0f, true, false, 14f, 17f)
            moveTo(7f, 15f)
            verticalLineTo(4f)
            moveTo(17f, 15f)
            verticalLineTo(4f)
            moveTo(3f, 4f)
            horizontalLineTo(21f)
        }
    }.build().also { cachedRings = it }

/** `Rings` vector, built on first access and reused afterwards. */
private var cachedRings: ImageVector? = null
