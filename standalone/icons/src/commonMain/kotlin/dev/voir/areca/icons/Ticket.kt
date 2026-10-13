package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Ticket icon.
 *
 * Converted from Lucide `ticket` (ISC).
 */
public val ArecaIcons.Ticket: ImageVector
    get() = cachedTicket ?: ImageVector.Builder(
        name = "Ticket",
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
            moveTo(2f, 9f)
            arcTo(3f, 3f, 0f, false, true, 2f, 15f)
            verticalLineTo(17f)
            arcTo(2f, 2f, 0f, false, false, 4f, 19f)
            horizontalLineTo(20f)
            arcTo(2f, 2f, 0f, false, false, 22f, 17f)
            verticalLineTo(15f)
            arcTo(3f, 3f, 0f, false, true, 22f, 9f)
            verticalLineTo(7f)
            arcTo(2f, 2f, 0f, false, false, 20f, 5f)
            horizontalLineTo(4f)
            arcTo(2f, 2f, 0f, false, false, 2f, 7f)
            close()
            moveTo(13f, 5f)
            verticalLineTo(7f)
            moveTo(13f, 17f)
            verticalLineTo(19f)
            moveTo(13f, 11f)
            verticalLineTo(13f)
        }
    }.build().also { cachedTicket = it }

/** `Ticket` vector, built on first access and reused afterwards. */
private var cachedTicket: ImageVector? = null
