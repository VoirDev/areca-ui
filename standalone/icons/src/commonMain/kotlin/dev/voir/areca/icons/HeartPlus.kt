package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Heart plus icon.
 *
 * Converted from Tabler Icons `heart-plus` (MIT).
 */
public val ArecaIcons.HeartPlus: ImageVector
    get() = cachedHeartPlus ?: ImageVector.Builder(
        name = "HeartPlus",
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
            moveTo(12f, 20f)
            lineTo(4.5f, 12.572f)
            arcTo(5f, 5f, 0f, true, true, 12f, 6.006f)
            arcTo(5f, 5f, 0f, true, true, 19.96f, 12.059f)
            moveTo(16f, 19f)
            horizontalLineTo(22f)
            moveTo(19f, 16f)
            verticalLineTo(22f)
        }
    }.build().also { cachedHeartPlus = it }

/** `HeartPlus` vector, built on first access and reused afterwards. */
private var cachedHeartPlus: ImageVector? = null
