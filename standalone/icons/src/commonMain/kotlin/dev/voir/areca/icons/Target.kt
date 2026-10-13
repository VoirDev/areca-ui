package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Target icon.
 *
 * Converted from Lucide `target` (ISC).
 */
public val ArecaIcons.Target: ImageVector
    get() = cachedTarget ?: ImageVector.Builder(
        name = "Target",
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
            moveTo(2f, 12f)
            arcTo(10f, 10f, 0f, true, true, 22f, 12f)
            arcTo(10f, 10f, 0f, true, true, 2f, 12f)
            close()
            moveTo(6f, 12f)
            arcTo(6f, 6f, 0f, true, true, 18f, 12f)
            arcTo(6f, 6f, 0f, true, true, 6f, 12f)
            close()
            moveTo(10f, 12f)
            arcTo(2f, 2f, 0f, true, true, 14f, 12f)
            arcTo(2f, 2f, 0f, true, true, 10f, 12f)
            close()
        }
    }.build().also { cachedTarget = it }

/** `Target` vector, built on first access and reused afterwards. */
private var cachedTarget: ImageVector? = null
