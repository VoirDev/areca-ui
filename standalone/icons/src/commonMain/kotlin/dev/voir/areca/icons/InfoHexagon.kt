package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Info hexagon icon.
 *
 * Converted from Tabler Icons `info-hexagon` (MIT).
 */
public val ArecaIcons.InfoHexagon: ImageVector
    get() = cachedInfoHexagon ?: ImageVector.Builder(
        name = "InfoHexagon",
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
            moveTo(19.875f, 6.27f)
            curveTo(20.575f, 6.668f, 21.005f, 7.413f, 21f, 8.218f)
            verticalLineTo(15.502f)
            curveTo(21f, 16.311f, 20.557f, 17.057f, 19.842f, 17.45f)
            lineTo(13.092f, 21.72f)
            arcTo(2.269f, 2.269f, 0f, false, true, 10.908f, 21.72f)
            lineTo(4.158f, 17.45f)
            arcTo(2.225f, 2.225f, 0f, false, true, 3f, 15.502f)
            verticalLineTo(8.217f)
            curveTo(3f, 7.408f, 3.443f, 6.663f, 4.158f, 6.27f)
            lineTo(10.908f, 2.29f)
            arcTo(2.33f, 2.33f, 0f, false, true, 13.158f, 2.29f)
            lineTo(19.908f, 6.27f)
            horizontalLineTo(19.875f)
            moveTo(12f, 9f)
            horizontalLineTo(12.01f)
            moveTo(11f, 12f)
            horizontalLineTo(12f)
            verticalLineTo(16f)
            horizontalLineTo(13f)
        }
    }.build().also { cachedInfoHexagon = it }

/** `InfoHexagon` vector, built on first access and reused afterwards. */
private var cachedInfoHexagon: ImageVector? = null
