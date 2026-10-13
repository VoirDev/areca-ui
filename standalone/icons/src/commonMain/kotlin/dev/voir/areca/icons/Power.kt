package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Power icon.
 *
 * Converted from Lucide `power` (ISC).
 */
public val ArecaIcons.Power: ImageVector
    get() = cachedPower ?: ImageVector.Builder(
        name = "Power",
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
            moveTo(12f, 2f)
            verticalLineTo(12f)
            moveTo(18.4f, 6.6f)
            arcTo(9f, 9f, 0f, true, true, 5.63f, 6.64f)
        }
    }.build().also { cachedPower = it }

/** `Power` vector, built on first access and reused afterwards. */
private var cachedPower: ImageVector? = null
