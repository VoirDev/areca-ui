package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Perspective icon.
 *
 * Converted from Tabler Icons `perspective` (MIT).
 */
public val ArecaIcons.Perspective: ImageVector
    get() = cachedPerspective ?: ImageVector.Builder(
        name = "Perspective",
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
            moveTo(6.141f, 4.163f)
            lineTo(18.141f, 5.877f)
            arcTo(1f, 1f, 0f, false, true, 19f, 6.867f)
            verticalLineTo(17.133f)
            arcTo(1f, 1f, 0f, false, true, 18.141f, 18.123f)
            lineTo(6.141f, 19.837f)
            arcTo(1f, 1f, 0f, false, true, 5f, 18.847f)
            verticalLineTo(5.153f)
            arcTo(1f, 1f, 0f, false, true, 6.141f, 4.163f)
        }
    }.build().also { cachedPerspective = it }

/** `Perspective` vector, built on first access and reused afterwards. */
private var cachedPerspective: ImageVector? = null
