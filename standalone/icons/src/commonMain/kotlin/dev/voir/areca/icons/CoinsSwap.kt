package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Exchange icon.
 *
 * Converted from Tabler Icons `exchange` (MIT).
 */
public val ArecaIcons.CoinsSwap: ImageVector
    get() = cachedCoinsSwap ?: ImageVector.Builder(
        name = "CoinsSwap",
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
            moveTo(3f, 18f)
            arcTo(2f, 2f, 0f, true, false, 7f, 18f)
            arcTo(2f, 2f, 0f, true, false, 3f, 18f)
            moveTo(17f, 6f)
            arcTo(2f, 2f, 0f, true, false, 21f, 6f)
            arcTo(2f, 2f, 0f, true, false, 17f, 6f)
            moveTo(19f, 8f)
            verticalLineTo(13f)
            arcTo(5f, 5f, 0f, false, true, 14f, 18f)
            horizontalLineTo(11f)
            lineTo(14f, 15f)
            moveTo(14f, 21f)
            lineTo(11f, 18f)
            moveTo(5f, 16f)
            verticalLineTo(11f)
            arcTo(5f, 5f, 0f, false, true, 10f, 6f)
            horizontalLineTo(13f)
            lineTo(10f, 3f)
            moveTo(10f, 9f)
            lineTo(13f, 6f)
        }
    }.build().also { cachedCoinsSwap = it }

/** `CoinsSwap` vector, built on first access and reused afterwards. */
private var cachedCoinsSwap: ImageVector? = null
