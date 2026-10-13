package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Martini icon.
 *
 * Converted from Lucide `martini` (ISC).
 */
public val ArecaIcons.Martini: ImageVector
    get() = cachedMartini ?: ImageVector.Builder(
        name = "Martini",
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
            moveTo(12f, 12f)
            lineTo(4.207f, 4.207f)
            arcTo(0.707f, 0.707f, 0f, false, true, 4.707f, 3f)
            horizontalLineTo(19.293f)
            arcTo(0.707f, 0.707f, 0f, false, true, 19.793f, 4.207f)
            close()
            moveTo(12f, 12f)
            verticalLineTo(22f)
            moveTo(7f, 22f)
            horizontalLineTo(17f)
        }
    }.build().also { cachedMartini = it }

/** `Martini` vector, built on first access and reused afterwards. */
private var cachedMartini: ImageVector? = null
