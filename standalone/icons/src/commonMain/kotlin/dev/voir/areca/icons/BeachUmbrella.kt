package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Beach icon.
 *
 * Converted from Tabler Icons `beach` (MIT).
 */
public val ArecaIcons.BeachUmbrella: ImageVector
    get() = cachedBeachUmbrella ?: ImageVector.Builder(
        name = "BeachUmbrella",
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
            moveTo(17.553f, 16.75f)
            arcTo(7.5f, 7.5f, 0f, false, false, 6.947f, 16.75f)
            moveTo(18f, 3.804f)
            arcTo(6f, 6f, 0f, false, false, 9.804f, 6f)
            lineTo(20.196f, 12f)
            arcTo(6f, 6f, 0f, false, false, 18f, 3.804f)
            moveTo(16.732f, 10f)
            curveTo(18.39f, 7.13f, 18.957f, 4.356f, 18f, 3.804f)
            curveTo(17.043f, 3.252f, 14.925f, 5.13f, 13.268f, 8f)
            moveTo(15f, 9f)
            lineTo(12f, 14.196f)
            moveTo(3f, 19.25f)
            arcTo(2.4f, 2.4f, 0f, false, true, 4f, 19f)
            arcTo(2.4f, 2.4f, 0f, false, true, 6f, 20f)
            arcTo(2.4f, 2.4f, 0f, false, false, 8f, 21f)
            arcTo(2.4f, 2.4f, 0f, false, false, 10f, 20f)
            arcTo(2.4f, 2.4f, 0f, false, true, 12f, 19f)
            arcTo(2.4f, 2.4f, 0f, false, true, 14f, 20f)
            arcTo(2.4f, 2.4f, 0f, false, false, 16f, 21f)
            arcTo(2.4f, 2.4f, 0f, false, false, 18f, 20f)
            arcTo(2.4f, 2.4f, 0f, false, true, 20f, 19f)
            arcTo(2.4f, 2.4f, 0f, false, true, 21f, 19.25f)
        }
    }.build().also { cachedBeachUmbrella = it }

/** `BeachUmbrella` vector, built on first access and reused afterwards. */
private var cachedBeachUmbrella: ImageVector? = null
