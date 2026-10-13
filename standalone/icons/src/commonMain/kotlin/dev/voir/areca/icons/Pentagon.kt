package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Pentagon icon.
 *
 * Converted from Lucide `pentagon` (ISC).
 */
public val ArecaIcons.Pentagon: ImageVector
    get() = cachedPentagon ?: ImageVector.Builder(
        name = "Pentagon",
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
            moveTo(10.83f, 2.38f)
            arcTo(2f, 2f, 0f, false, true, 13.17f, 2.38f)
            lineTo(21.17f, 8.12f)
            arcTo(2f, 2f, 0f, false, true, 21.9f, 10.37f)
            lineTo(18.86f, 19.63f)
            arcTo(2f, 2f, 0f, false, true, 16.96f, 21f)
            horizontalLineTo(7.04f)
            arcTo(2f, 2f, 0f, false, true, 5.14f, 19.63f)
            lineTo(2.1f, 10.37f)
            arcTo(2f, 2f, 0f, false, true, 2.83f, 8.12f)
            close()
        }
    }.build().also { cachedPentagon = it }

/** `Pentagon` vector, built on first access and reused afterwards. */
private var cachedPentagon: ImageVector? = null
