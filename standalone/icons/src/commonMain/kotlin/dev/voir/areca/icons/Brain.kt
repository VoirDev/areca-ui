package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Brain icon.
 *
 * Converted from Lucide `brain` (ISC).
 */
public val ArecaIcons.Brain: ImageVector
    get() = cachedBrain ?: ImageVector.Builder(
        name = "Brain",
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
            moveTo(12f, 18f)
            verticalLineTo(5f)
            moveTo(15f, 13f)
            arcTo(4.17f, 4.17f, 0f, false, true, 12f, 9f)
            arcTo(4.17f, 4.17f, 0f, false, true, 9f, 13f)
            moveTo(17.598f, 6.5f)
            arcTo(3f, 3f, 0f, true, false, 12f, 5f)
            arcTo(3f, 3f, 0f, true, false, 6.402f, 6.5f)
            moveTo(17.997f, 5.125f)
            arcTo(4f, 4f, 0f, false, true, 20.523f, 10.895f)
            moveTo(18f, 18f)
            arcTo(4f, 4f, 0f, false, false, 20f, 10.536f)
            moveTo(19.967f, 17.483f)
            arcTo(4f, 4f, 0f, true, true, 12f, 18f)
            arcTo(4f, 4f, 0f, true, true, 4.033f, 17.483f)
            moveTo(6f, 18f)
            arcTo(4f, 4f, 0f, false, true, 4f, 10.536f)
            moveTo(6.003f, 5.125f)
            arcTo(4f, 4f, 0f, false, false, 3.477f, 10.895f)
        }
    }.build().also { cachedBrain = it }

/** `Brain` vector, built on first access and reused afterwards. */
private var cachedBrain: ImageVector? = null
