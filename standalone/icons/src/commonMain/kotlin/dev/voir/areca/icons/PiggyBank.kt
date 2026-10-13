package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Piggy bank icon.
 *
 * Converted from Lucide `piggy-bank` (ISC).
 */
public val ArecaIcons.PiggyBank: ImageVector
    get() = cachedPiggyBank ?: ImageVector.Builder(
        name = "PiggyBank",
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
            moveTo(11f, 17f)
            horizontalLineTo(14f)
            verticalLineTo(19f)
            arcTo(1f, 1f, 0f, false, false, 15f, 20f)
            horizontalLineTo(17f)
            arcTo(1f, 1f, 0f, false, false, 18f, 19f)
            verticalLineTo(16f)
            arcTo(3.16f, 3.16f, 0f, false, false, 20f, 14f)
            horizontalLineTo(21f)
            arcTo(1f, 1f, 0f, false, false, 22f, 13f)
            verticalLineTo(11f)
            arcTo(1f, 1f, 0f, false, false, 21f, 10f)
            horizontalLineTo(20f)
            arcTo(5f, 5f, 0f, false, false, 18f, 6f)
            verticalLineTo(3f)
            arcTo(4f, 4f, 0f, false, false, 14.8f, 4.6f)
            lineTo(14.5f, 5f)
            horizontalLineTo(11f)
            arcTo(6f, 6f, 0f, false, false, 5f, 11f)
            verticalLineTo(12f)
            arcTo(5f, 5f, 0f, false, false, 7f, 16f)
            verticalLineTo(19f)
            arcTo(1f, 1f, 0f, false, false, 8f, 20f)
            horizontalLineTo(10f)
            arcTo(1f, 1f, 0f, false, false, 11f, 19f)
            close()
            moveTo(16f, 10f)
            horizontalLineTo(16.01f)
            moveTo(2f, 8f)
            verticalLineTo(9f)
            arcTo(2f, 2f, 0f, false, false, 4f, 11f)
            horizontalLineTo(5f)
        }
    }.build().also { cachedPiggyBank = it }

/** `PiggyBank` vector, built on first access and reused afterwards. */
private var cachedPiggyBank: ImageVector? = null
