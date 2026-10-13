package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Swimming icon.
 *
 * Converted from Tabler Icons `swimming` (MIT).
 */
public val ArecaIcons.Swimming: ImageVector
    get() = cachedSwimming ?: ImageVector.Builder(
        name = "Swimming",
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
            moveTo(15f, 9f)
            arcTo(1f, 1f, 0f, true, false, 17f, 9f)
            arcTo(1f, 1f, 0f, true, false, 15f, 9f)
            moveTo(6f, 11f)
            lineTo(10f, 9f)
            lineTo(13.5f, 12f)
            lineTo(12f, 14f)
            moveTo(3f, 16.75f)
            arcTo(2.4f, 2.4f, 0f, false, false, 4f, 17f)
            arcTo(2.4f, 2.4f, 0f, false, false, 6f, 16f)
            arcTo(2.4f, 2.4f, 0f, false, true, 8f, 15f)
            arcTo(2.4f, 2.4f, 0f, false, true, 10f, 16f)
            arcTo(2.4f, 2.4f, 0f, false, false, 12f, 17f)
            arcTo(2.4f, 2.4f, 0f, false, false, 14f, 16f)
            arcTo(2.4f, 2.4f, 0f, false, true, 16f, 15f)
            arcTo(2.4f, 2.4f, 0f, false, true, 18f, 16f)
            arcTo(2.4f, 2.4f, 0f, false, false, 20f, 17f)
            arcTo(2.4f, 2.4f, 0f, false, false, 21f, 16.75f)
        }
    }.build().also { cachedSwimming = it }

/** `Swimming` vector, built on first access and reused afterwards. */
private var cachedSwimming: ImageVector? = null
