package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Heart check icon.
 *
 * Converted from Tabler Icons `heart-check` (MIT).
 */
public val ArecaIcons.CheckHeart: ImageVector
    get() = cachedCheckHeart ?: ImageVector.Builder(
        name = "CheckHeart",
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
            moveTo(19.5f, 12.572f)
            lineTo(16.5f, 15.5f)
            moveTo(11f, 19f)
            arcTo(8916.99f, 8916.99f, 0f, false, false, 4.5f, 12.572f)
            arcTo(5f, 5f, 0f, true, true, 12f, 6.006f)
            arcTo(5f, 5f, 0f, true, true, 19.5f, 12.578f)
            moveTo(15f, 19f)
            lineTo(17f, 21f)
            lineTo(21f, 17f)
        }
    }.build().also { cachedCheckHeart = it }

/** `CheckHeart` vector, built on first access and reused afterwards. */
private var cachedCheckHeart: ImageVector? = null
