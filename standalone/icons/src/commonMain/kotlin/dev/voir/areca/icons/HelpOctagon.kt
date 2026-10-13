package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Help octagon icon.
 *
 * Converted from Tabler Icons `help-octagon` (MIT).
 */
public val ArecaIcons.HelpOctagon: ImageVector
    get() = cachedHelpOctagon ?: ImageVector.Builder(
        name = "HelpOctagon",
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
            moveTo(12.802f, 2.165f)
            lineTo(18.377f, 4.554f)
            curveTo(18.857f, 4.76f, 19.24f, 5.143f, 19.447f, 5.624f)
            lineTo(21.835f, 11.198f)
            curveTo(22.055f, 11.71f, 22.055f, 12.29f, 21.835f, 12.802f)
            lineTo(19.446f, 18.377f)
            curveTo(19.24f, 18.857f, 18.857f, 19.24f, 18.376f, 19.447f)
            lineTo(12.802f, 21.835f)
            curveTo(12.29f, 22.055f, 11.71f, 22.055f, 11.198f, 21.835f)
            lineTo(5.623f, 19.446f)
            arcTo(2.036f, 2.036f, 0f, false, true, 4.553f, 18.376f)
            lineTo(2.165f, 12.802f)
            arcTo(2.036f, 2.036f, 0f, false, true, 2.165f, 11.198f)
            lineTo(4.554f, 5.623f)
            curveTo(4.76f, 5.143f, 5.143f, 4.76f, 5.624f, 4.553f)
            lineTo(11.198f, 2.165f)
            arcTo(2.036f, 2.036f, 0f, false, true, 12.802f, 2.165f)
            moveTo(12f, 16f)
            verticalLineTo(16.01f)
            moveTo(12f, 13f)
            arcTo(2f, 2f, 0f, false, false, 12.914f, 9.218f)
            arcTo(1.98f, 1.98f, 0f, false, false, 10.5f, 9.701f)
        }
    }.build().also { cachedHelpOctagon = it }

/** `HelpOctagon` vector, built on first access and reused afterwards. */
private var cachedHelpOctagon: ImageVector? = null
