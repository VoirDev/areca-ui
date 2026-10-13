package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Octagon alert icon.
 *
 * Converted from Lucide `octagon-alert` (ISC).
 */
public val ArecaIcons.AlertOctagon: ImageVector
    get() = cachedAlertOctagon ?: ImageVector.Builder(
        name = "AlertOctagon",
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
            moveTo(12f, 16f)
            horizontalLineTo(12.01f)
            moveTo(12f, 8f)
            verticalLineTo(12f)
            moveTo(15.312f, 2f)
            arcTo(2f, 2f, 0f, false, true, 16.726f, 2.586f)
            lineTo(21.414f, 7.274f)
            arcTo(2f, 2f, 0f, false, true, 22f, 8.688f)
            verticalLineTo(15.312f)
            arcTo(2f, 2f, 0f, false, true, 21.414f, 16.726f)
            lineTo(16.726f, 21.414f)
            arcTo(2f, 2f, 0f, false, true, 15.312f, 22f)
            horizontalLineTo(8.688f)
            arcTo(2f, 2f, 0f, false, true, 7.274f, 21.414f)
            lineTo(2.586f, 16.726f)
            arcTo(2f, 2f, 0f, false, true, 2f, 15.312f)
            verticalLineTo(8.688f)
            arcTo(2f, 2f, 0f, false, true, 2.586f, 7.274f)
            lineTo(7.274f, 2.586f)
            arcTo(2f, 2f, 0f, false, true, 8.688f, 2f)
            close()
        }
    }.build().also { cachedAlertOctagon = it }

/** `AlertOctagon` vector, built on first access and reused afterwards. */
private var cachedAlertOctagon: ImageVector? = null
