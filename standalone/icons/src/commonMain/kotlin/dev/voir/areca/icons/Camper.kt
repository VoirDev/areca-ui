package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Camper icon.
 *
 * Converted from Tabler Icons `camper` (MIT).
 */
public val ArecaIcons.Camper: ImageVector
    get() = cachedCamper ?: ImageVector.Builder(
        name = "Camper",
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
            moveTo(5f, 18f)
            arcTo(2f, 2f, 0f, true, false, 9f, 18f)
            arcTo(2f, 2f, 0f, false, false, 5f, 18f)
            moveTo(15f, 18f)
            arcTo(2f, 2f, 0f, true, false, 19f, 18f)
            arcTo(2f, 2f, 0f, false, false, 15f, 18f)
            moveTo(5f, 18f)
            horizontalLineTo(4f)
            arcTo(1f, 1f, 0f, false, true, 3f, 17f)
            verticalLineTo(6f)
            arcTo(2f, 2f, 0f, false, true, 5f, 4f)
            horizontalLineTo(17f)
            arcTo(4f, 4f, 0f, false, true, 21f, 8f)
            horizontalLineTo(3f)
            moveTo(9f, 18f)
            horizontalLineTo(15f)
            moveTo(19f, 18f)
            horizontalLineTo(20f)
            arcTo(1f, 1f, 0f, false, false, 21f, 17f)
            verticalLineTo(13f)
            lineTo(18f, 8f)
            moveTo(21f, 13f)
            horizontalLineTo(14f)
            moveTo(14f, 8f)
            verticalLineTo(18f)
        }
    }.build().also { cachedCamper = it }

/** `Camper` vector, built on first access and reused afterwards. */
private var cachedCamper: ImageVector? = null
