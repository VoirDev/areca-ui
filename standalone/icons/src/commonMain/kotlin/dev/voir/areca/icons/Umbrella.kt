package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Umbrella icon.
 *
 * Converted from Lucide `umbrella` (ISC).
 */
public val ArecaIcons.Umbrella: ImageVector
    get() = cachedUmbrella ?: ImageVector.Builder(
        name = "Umbrella",
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
            moveTo(12f, 13f)
            verticalLineTo(20f)
            arcTo(2f, 2f, 0f, false, false, 16f, 20f)
            moveTo(12f, 2f)
            verticalLineTo(4f)
            moveTo(20.992f, 13f)
            arcTo(1f, 1f, 0f, false, false, 21.962f, 11.726f)
            arcTo(10.284f, 10.284f, 0f, false, false, 2.039f, 11.726f)
            arcTo(1f, 1f, 0f, false, false, 3f, 13f)
            close()
        }
    }.build().also { cachedUmbrella = it }

/** `Umbrella` vector, built on first access and reused afterwards. */
private var cachedUmbrella: ImageVector? = null
