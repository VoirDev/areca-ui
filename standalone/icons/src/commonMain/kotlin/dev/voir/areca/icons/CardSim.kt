package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Card sim icon.
 *
 * Converted from Lucide `card-sim` (ISC).
 */
public val ArecaIcons.CardSim: ImageVector
    get() = cachedCardSim ?: ImageVector.Builder(
        name = "CardSim",
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
            moveTo(12f, 14f)
            verticalLineTo(18f)
            moveTo(14.172f, 2f)
            arcTo(2f, 2f, 0f, false, true, 15.586f, 2.586f)
            lineTo(19.414f, 6.414f)
            arcTo(2f, 2f, 0f, false, true, 20f, 7.828f)
            verticalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 18f, 22f)
            horizontalLineTo(6f)
            arcTo(2f, 2f, 0f, false, true, 4f, 20f)
            verticalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 6f, 2f)
            close()
            moveTo(8f, 14f)
            horizontalLineTo(16f)
            moveTo(10f, 10f)
            horizontalLineTo(14f)
            arcTo(2f, 2f, 0f, false, true, 16f, 12f)
            verticalLineTo(16f)
            arcTo(2f, 2f, 0f, false, true, 14f, 18f)
            horizontalLineTo(10f)
            arcTo(2f, 2f, 0f, false, true, 8f, 16f)
            verticalLineTo(12f)
            arcTo(2f, 2f, 0f, false, true, 10f, 10f)
            close()
        }
    }.build().also { cachedCardSim = it }

/** `CardSim` vector, built on first access and reused afterwards. */
private var cachedCardSim: ImageVector? = null
