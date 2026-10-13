package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Confetti icon.
 *
 * Converted from Tabler Icons `confetti` (MIT).
 */
public val ArecaIcons.Confetti: ImageVector
    get() = cachedConfetti ?: ImageVector.Builder(
        name = "Confetti",
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
            moveTo(4f, 5f)
            horizontalLineTo(6f)
            moveTo(5f, 4f)
            verticalLineTo(6f)
            moveTo(11.5f, 4f)
            lineTo(11f, 6f)
            moveTo(18f, 5f)
            horizontalLineTo(20f)
            moveTo(19f, 4f)
            verticalLineTo(6f)
            moveTo(15f, 9f)
            lineTo(14f, 10f)
            moveTo(18f, 13f)
            lineTo(20f, 12.5f)
            moveTo(18f, 19f)
            horizontalLineTo(20f)
            moveTo(19f, 18f)
            verticalLineTo(20f)
            moveTo(14f, 16.518f)
            lineTo(7.482f, 10f)
            lineTo(3.092f, 19.58f)
            arcTo(1f, 1f, 0f, false, false, 4.421f, 20.909f)
            lineTo(14f, 16.519f)
        }
    }.build().also { cachedConfetti = it }

/** `Confetti` vector, built on first access and reused afterwards. */
private var cachedConfetti: ImageVector? = null
