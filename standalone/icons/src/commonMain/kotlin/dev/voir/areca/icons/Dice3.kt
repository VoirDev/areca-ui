package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Dice 3 icon.
 *
 * Converted from Lucide `dice-3` (ISC).
 */
public val ArecaIcons.Dice3: ImageVector
    get() = cachedDice3 ?: ImageVector.Builder(
        name = "Dice3",
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
            moveTo(5f, 3f)
            horizontalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 21f, 5f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 19f, 21f)
            horizontalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 3f, 19f)
            verticalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 5f, 3f)
            close()
            moveTo(16f, 8f)
            horizontalLineTo(16.01f)
            moveTo(12f, 12f)
            horizontalLineTo(12.01f)
            moveTo(8f, 16f)
            horizontalLineTo(8.01f)
        }
    }.build().also { cachedDice3 = it }

/** `Dice3` vector, built on first access and reused afterwards. */
private var cachedDice3: ImageVector? = null
