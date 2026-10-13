package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Cast icon.
 *
 * Converted from Lucide `cast` (ISC).
 */
public val ArecaIcons.Cast: ImageVector
    get() = cachedCast ?: ImageVector.Builder(
        name = "Cast",
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
            moveTo(2f, 8f)
            verticalLineTo(6f)
            arcTo(2f, 2f, 0f, false, true, 4f, 4f)
            horizontalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 22f, 6f)
            verticalLineTo(18f)
            arcTo(2f, 2f, 0f, false, true, 20f, 20f)
            horizontalLineTo(14f)
            moveTo(2f, 12f)
            arcTo(9f, 9f, 0f, false, true, 10f, 20f)
            moveTo(2f, 16f)
            arcTo(5f, 5f, 0f, false, true, 6f, 20f)
            moveTo(2f, 20f)
            lineTo(2.01f, 20f)
        }
    }.build().also { cachedCast = it }

/** `Cast` vector, built on first access and reused afterwards. */
private var cachedCast: ImageVector? = null
