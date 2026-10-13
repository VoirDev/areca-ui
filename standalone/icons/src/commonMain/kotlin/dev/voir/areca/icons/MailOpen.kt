package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Mail open icon.
 *
 * Converted from Lucide `mail-open` (ISC).
 */
public val ArecaIcons.MailOpen: ImageVector
    get() = cachedMailOpen ?: ImageVector.Builder(
        name = "MailOpen",
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
            moveTo(21.2f, 8.4f)
            curveTo(21.7f, 8.78f, 22f, 9.37f, 22f, 10f)
            verticalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 20f, 22f)
            horizontalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 2f, 20f)
            verticalLineTo(10f)
            arcTo(2f, 2f, 0f, false, true, 2.8f, 8.4f)
            lineTo(10.8f, 2.4f)
            arcTo(2f, 2f, 0f, false, true, 13.2f, 2.4f)
            lineTo(21.2f, 8.4f)
            close()
            moveTo(22f, 10f)
            lineTo(13.03f, 15.7f)
            arcTo(1.94f, 1.94f, 0f, false, true, 10.97f, 15.7f)
            lineTo(2f, 10f)
        }
    }.build().also { cachedMailOpen = it }

/** `MailOpen` vector, built on first access and reused afterwards. */
private var cachedMailOpen: ImageVector? = null
