package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Swiss franc icon.
 *
 * Converted from Lucide `swiss-franc` (ISC).
 */
public val ArecaIcons.SwissFranc: ImageVector
    get() = cachedSwissFranc ?: ImageVector.Builder(
        name = "SwissFranc",
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
            moveTo(10f, 21f)
            verticalLineTo(3f)
            horizontalLineTo(18f)
            moveTo(6f, 16f)
            horizontalLineTo(15f)
            moveTo(10f, 9.5f)
            horizontalLineTo(17f)
        }
    }.build().also { cachedSwissFranc = it }

/** `SwissFranc` vector, built on first access and reused afterwards. */
private var cachedSwissFranc: ImageVector? = null
