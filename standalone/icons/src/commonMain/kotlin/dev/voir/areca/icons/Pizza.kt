package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Pizza icon.
 *
 * Converted from Lucide `pizza` (ISC).
 */
public val ArecaIcons.Pizza: ImageVector
    get() = cachedPizza ?: ImageVector.Builder(
        name = "Pizza",
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
            moveTo(12f, 14f)
            lineTo(11f, 15f)
            moveTo(13.75f, 18.25f)
            lineTo(12.5f, 19.67f)
            moveTo(17.775f, 5.654f)
            arcTo(15.68f, 15.68f, 0f, false, false, 5.654f, 17.774f)
            moveTo(18.8f, 9.3f)
            arcTo(1f, 1f, 0f, false, false, 20.9f, 17f)
            moveTo(21.964f, 20.732f)
            arcTo(1f, 1f, 0f, false, true, 20.732f, 21.964f)
            lineTo(2.732f, 16.964f)
            arcTo(1f, 1f, 0f, false, true, 2.037f, 15.732f)
            arcTo(19.68f, 19.68f, 0f, false, true, 15.732f, 2.037f)
            arcTo(1f, 1f, 0f, false, true, 16.964f, 2.732f)
            close()
        }
    }.build().also { cachedPizza = it }

/** `Pizza` vector, built on first access and reused afterwards. */
private var cachedPizza: ImageVector? = null
