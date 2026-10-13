package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Hand heart icon.
 *
 * Converted from Lucide `hand-heart` (ISC).
 */
public val ArecaIcons.HandHeart: ImageVector
    get() = cachedHandHeart ?: ImageVector.Builder(
        name = "HandHeart",
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
            moveTo(11f, 14f)
            horizontalLineTo(13f)
            arcTo(2f, 2f, 0f, false, false, 13f, 10f)
            horizontalLineTo(10f)
            curveTo(9.4f, 10f, 8.9f, 10.2f, 8.6f, 10.6f)
            lineTo(3f, 16f)
            moveTo(14.45f, 13.39f)
            lineTo(19.5f, 8.696f)
            curveTo(20.196f, 8f, 21f, 6.85f, 21f, 5.75f)
            arcTo(2.75f, 2.75f, 0f, false, false, 16.203f, 3.913f)
            arcTo(0.276f, 0.276f, 0f, false, true, 15.797f, 3.913f)
            arcTo(2.75f, 2.75f, 0f, false, false, 11f, 5.75f)
            curveTo(11f, 6.95f, 11.802f, 7.998f, 12.5f, 8.696f)
            lineTo(16f, 11.95f)
            moveTo(2f, 15f)
            lineTo(8f, 21f)
            moveTo(7f, 20f)
            lineTo(8.6f, 18.6f)
            curveTo(8.9f, 18.2f, 9.4f, 18f, 10f, 18f)
            horizontalLineTo(14f)
            curveTo(15.1f, 18f, 16.1f, 17.6f, 16.8f, 16.8f)
            lineTo(21.4f, 12.4f)
            arcTo(1f, 1f, 0f, false, false, 18.65f, 9.49f)
        }
    }.build().also { cachedHandHeart = it }

/** `HandHeart` vector, built on first access and reused afterwards. */
private var cachedHandHeart: ImageVector? = null
