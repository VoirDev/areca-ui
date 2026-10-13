package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Sparkles icon.
 *
 * Same drawing as `ArecaIcons.Sparkles`; both names are kept.
 *
 * Converted from Lucide `sparkles` (ISC).
 */
public val ArecaIcons.Stars: ImageVector
    get() = cachedStars ?: ImageVector.Builder(
        name = "Stars",
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
            moveTo(11.017f, 2.814f)
            arcTo(1f, 1f, 0f, false, true, 12.983f, 2.814f)
            lineTo(14.034f, 8.372f)
            arcTo(2f, 2f, 0f, false, false, 15.628f, 9.966f)
            lineTo(21.186f, 11.017f)
            arcTo(1f, 1f, 0f, false, true, 21.186f, 12.983f)
            lineTo(15.628f, 14.034f)
            arcTo(2f, 2f, 0f, false, false, 14.034f, 15.628f)
            lineTo(12.983f, 21.186f)
            arcTo(1f, 1f, 0f, false, true, 11.017f, 21.186f)
            lineTo(9.966f, 15.628f)
            arcTo(2f, 2f, 0f, false, false, 8.372f, 14.034f)
            lineTo(2.814f, 12.983f)
            arcTo(1f, 1f, 0f, false, true, 2.814f, 11.017f)
            lineTo(8.372f, 9.966f)
            arcTo(2f, 2f, 0f, false, false, 9.966f, 8.372f)
            close()
            moveTo(20f, 2f)
            verticalLineTo(6f)
            moveTo(22f, 4f)
            horizontalLineTo(18f)
            moveTo(2f, 20f)
            arcTo(2f, 2f, 0f, true, true, 6f, 20f)
            arcTo(2f, 2f, 0f, true, true, 2f, 20f)
            close()
        }
    }.build().also { cachedStars = it }

/** `Stars` vector, built on first access and reused afterwards. */
private var cachedStars: ImageVector? = null
