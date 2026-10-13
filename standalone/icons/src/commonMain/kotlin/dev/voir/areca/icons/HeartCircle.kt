package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Circle heart icon.
 *
 * Converted from Tabler Icons `circle-heart` (MIT).
 */
public val ArecaIcons.HeartCircle: ImageVector
    get() = cachedHeartCircle ?: ImageVector.Builder(
        name = "HeartCircle",
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
            moveTo(3f, 12f)
            arcTo(9f, 9f, 0f, true, false, 21f, 12f)
            arcTo(9f, 9f, 0f, true, false, 3f, 12f)
            moveTo(12f, 16f)
            lineTo(15.35f, 12.716f)
            arcTo(2.143f, 2.143f, 0f, false, false, 15.355f, 9.645f)
            arcTo(2.24f, 2.24f, 0f, false, false, 12.226f, 9.639f)
            lineTo(12.002f, 9.859f)
            lineTo(11.779f, 9.639f)
            arcTo(2.24f, 2.24f, 0f, false, false, 8.651f, 9.633f)
            arcTo(2.143f, 2.143f, 0f, false, false, 8.645f, 12.704f)
            lineTo(12f, 16f)
        }
    }.build().also { cachedHeartCircle = it }

/** `HeartCircle` vector, built on first access and reused afterwards. */
private var cachedHeartCircle: ImageVector? = null
