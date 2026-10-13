package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Gift card icon.
 *
 * Converted from Tabler Icons `gift-card` (MIT).
 */
public val ArecaIcons.GiftCard: ImageVector
    get() = cachedGiftCard ?: ImageVector.Builder(
        name = "GiftCard",
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
            moveTo(3f, 8f)
            arcTo(3f, 3f, 0f, false, true, 6f, 5f)
            horizontalLineTo(18f)
            arcTo(3f, 3f, 0f, false, true, 21f, 8f)
            verticalLineTo(16f)
            arcTo(3f, 3f, 0f, false, true, 18f, 19f)
            horizontalLineTo(6f)
            arcTo(3f, 3f, 0f, false, true, 3f, 16f)
            lineTo(3f, 8f)
            moveTo(7f, 16f)
            lineTo(10f, 13f)
            lineTo(13f, 16f)
            moveTo(8f, 13f)
            curveTo(7.211f, 13f, 6f, 12.328f, 6f, 11.5f)
            reflectiveCurveTo(6.711f, 10f, 7.5f, 10f)
            curveTo(8.628f, 9.98f, 9.577f, 11.17f, 10f, 13f)
            curveTo(10.423f, 11.17f, 11.372f, 9.98f, 12.5f, 10f)
            curveTo(13.289f, 10f, 14f, 10.672f, 14f, 11.5f)
            reflectiveCurveTo(12.789f, 13f, 12f, 13f)
            horizontalLineTo(8f)
        }
    }.build().also { cachedGiftCard = it }

/** `GiftCard` vector, built on first access and reused afterwards. */
private var cachedGiftCard: ImageVector? = null
