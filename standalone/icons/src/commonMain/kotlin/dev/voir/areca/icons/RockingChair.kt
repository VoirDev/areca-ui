package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Rocking chair icon.
 *
 * Converted from Lucide `rocking-chair` (ISC).
 */
public val ArecaIcons.RockingChair: ImageVector
    get() = cachedRockingChair ?: ImageVector.Builder(
        name = "RockingChair",
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
            moveTo(15f, 13f)
            lineTo(18.708f, 20.416f)
            moveTo(3f, 19f)
            arcTo(15f, 15f, 0f, false, false, 21f, 19f)
            moveTo(3f, 2f)
            lineTo(6.21f, 11.633f)
            arcTo(2f, 2f, 0f, false, false, 8.109f, 13f)
            horizontalLineTo(18f)
            moveTo(9f, 13f)
            lineTo(5.292f, 20.416f)
        }
    }.build().also { cachedRockingChair = it }

/** `RockingChair` vector, built on first access and reused afterwards. */
private var cachedRockingChair: ImageVector? = null
