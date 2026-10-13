package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Dice 6 icon.
 *
 * Converted from Lucide `dice-6` (ISC).
 */
public val ArecaIcons.Dice6: ImageVector
    get() = cachedDice6 ?: ImageVector.Builder(
        name = "Dice6",
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
            moveTo(16f, 12f)
            horizontalLineTo(16.01f)
            moveTo(16f, 16f)
            horizontalLineTo(16.01f)
            moveTo(8f, 8f)
            horizontalLineTo(8.01f)
            moveTo(8f, 12f)
            horizontalLineTo(8.01f)
            moveTo(8f, 16f)
            horizontalLineTo(8.01f)
        }
    }.build().also { cachedDice6 = it }

/** `Dice6` vector, built on first access and reused afterwards. */
private var cachedDice6: ImageVector? = null
