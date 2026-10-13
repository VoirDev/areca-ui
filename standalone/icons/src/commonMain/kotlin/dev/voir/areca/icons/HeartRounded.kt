package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Heart icon.
 *
 * Converted from Tabler Icons `heart` (MIT).
 */
public val ArecaIcons.HeartRounded: ImageVector
    get() = cachedHeartRounded ?: ImageVector.Builder(
        name = "HeartRounded",
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
            lineTo(12f, 20f)
            lineTo(4.5f, 12.572f)
            arcTo(5f, 5f, 0f, true, true, 12f, 6.006f)
            arcTo(5f, 5f, 0f, true, true, 19.5f, 12.578f)
        }
    }.build().also { cachedHeartRounded = it }

/** `HeartRounded` vector, built on first access and reused afterwards. */
private var cachedHeartRounded: ImageVector? = null
