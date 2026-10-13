package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Scissors icon.
 *
 * Converted from Lucide `scissors` (ISC).
 */
public val ArecaIcons.Scissors: ImageVector
    get() = cachedScissors ?: ImageVector.Builder(
        name = "Scissors",
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
            moveTo(3f, 6f)
            arcTo(3f, 3f, 0f, true, true, 9f, 6f)
            arcTo(3f, 3f, 0f, true, true, 3f, 6f)
            close()
            moveTo(8.12f, 8.12f)
            lineTo(12f, 12f)
            moveTo(20f, 4f)
            lineTo(8.12f, 15.88f)
            moveTo(3f, 18f)
            arcTo(3f, 3f, 0f, true, true, 9f, 18f)
            arcTo(3f, 3f, 0f, true, true, 3f, 18f)
            close()
            moveTo(14.8f, 14.8f)
            lineTo(20f, 20f)
        }
    }.build().also { cachedScissors = it }

/** `Scissors` vector, built on first access and reused afterwards. */
private var cachedScissors: ImageVector? = null
