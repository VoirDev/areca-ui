package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Turkish lira icon.
 *
 * Converted from Lucide `turkish-lira` (ISC).
 */
public val ArecaIcons.TurkishLira: ImageVector
    get() = cachedTurkishLira ?: ImageVector.Builder(
        name = "TurkishLira",
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
            moveTo(15f, 4f)
            lineTo(5f, 9f)
            moveTo(15f, 8.5f)
            lineTo(5f, 13.5f)
            moveTo(18f, 12f)
            arcTo(9f, 9f, 0f, false, true, 9f, 21f)
            verticalLineTo(3f)
        }
    }.build().also { cachedTurkishLira = it }

/** `TurkishLira` vector, built on first access and reused afterwards. */
private var cachedTurkishLira: ImageVector? = null
