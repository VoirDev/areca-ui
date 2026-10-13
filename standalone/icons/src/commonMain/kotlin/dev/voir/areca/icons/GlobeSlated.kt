package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * World icon.
 *
 * Same drawing as `ArecaIcons.World`; both names are kept.
 *
 * Converted from Tabler Icons `world` (MIT).
 */
public val ArecaIcons.GlobeSlated: ImageVector
    get() = cachedGlobeSlated ?: ImageVector.Builder(
        name = "GlobeSlated",
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
            moveTo(3f, 12f)
            arcTo(9f, 9f, 0f, true, false, 21f, 12f)
            arcTo(9f, 9f, 0f, false, false, 3f, 12f)
            moveTo(3.6f, 9f)
            horizontalLineTo(20.4f)
            moveTo(3.6f, 15f)
            horizontalLineTo(20.4f)
            moveTo(11.5f, 3f)
            arcTo(17f, 17f, 0f, false, false, 11.5f, 21f)
            moveTo(12.5f, 3f)
            arcTo(17f, 17f, 0f, false, true, 12.5f, 21f)
        }
    }.build().also { cachedGlobeSlated = it }

/** `GlobeSlated` vector, built on first access and reused afterwards. */
private var cachedGlobeSlated: ImageVector? = null
