package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Dices icon.
 *
 * Converted from Lucide `dices` (ISC).
 */
public val ArecaIcons.Dices: ImageVector
    get() = cachedDices ?: ImageVector.Builder(
        name = "Dices",
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
            moveTo(4f, 10f)
            horizontalLineTo(12f)
            arcTo(2f, 2f, 0f, false, true, 14f, 12f)
            verticalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 12f, 22f)
            horizontalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 2f, 20f)
            verticalLineTo(12f)
            arcTo(2f, 2f, 0f, false, true, 4f, 10f)
            close()
            moveTo(17.92f, 14f)
            lineTo(21.42f, 10.5f)
            arcTo(2.24f, 2.24f, 0f, false, false, 21.42f, 7.5f)
            lineTo(16.42f, 2.58f)
            arcTo(2.24f, 2.24f, 0f, false, false, 13.42f, 2.58f)
            lineTo(10f, 6f)
            moveTo(6f, 18f)
            horizontalLineTo(6.01f)
            moveTo(10f, 14f)
            horizontalLineTo(10.01f)
            moveTo(15f, 6f)
            horizontalLineTo(15.01f)
            moveTo(18f, 9f)
            horizontalLineTo(18.01f)
        }
    }.build().also { cachedDices = it }

/** `Dices` vector, built on first access and reused afterwards. */
private var cachedDices: ImageVector? = null
