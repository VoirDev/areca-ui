package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Sport shoe icon.
 *
 * Converted from Lucide `sport-shoe` (ISC).
 */
public val ArecaIcons.SportShoe: ImageVector
    get() = cachedSportShoe ?: ImageVector.Builder(
        name = "SportShoe",
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
            moveTo(15f, 10.42f)
            lineTo(19.8f, 5.35f)
            moveTo(19f, 18f)
            horizontalLineTo(22f)
            moveTo(9.5f, 22f)
            lineTo(21.414f, 9.415f)
            arcTo(2f, 2f, 0f, false, false, 21.2f, 6.4f)
            lineTo(15.59f, 2.192f)
            arcTo(1f, 1f, 0f, false, false, 14f, 3f)
            verticalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 12.606f, 6.906f)
            lineTo(8.677f, 8.053f)
            arcTo(1f, 1f, 0f, false, false, 8f, 9f)
            curveTo(7.845f, 15.393f, 5.918f, 18f, 4f, 18f)
            arcTo(2f, 2f, 0f, false, false, 4f, 22f)
            horizontalLineTo(18f)
        }
    }.build().also { cachedSportShoe = it }

/** `SportShoe` vector, built on first access and reused afterwards. */
private var cachedSportShoe: ImageVector? = null
