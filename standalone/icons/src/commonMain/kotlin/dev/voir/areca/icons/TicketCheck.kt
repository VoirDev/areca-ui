package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Ticket check icon.
 *
 * Converted from Lucide `ticket-check` (ISC).
 */
public val ArecaIcons.TicketCheck: ImageVector
    get() = cachedTicketCheck ?: ImageVector.Builder(
        name = "TicketCheck",
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
            moveTo(9f, 12f)
            lineTo(11f, 14f)
            lineTo(15f, 10f)
        }
    }.build().also { cachedTicketCheck = it }

/** `TicketCheck` vector, built on first access and reused afterwards. */
private var cachedTicketCheck: ImageVector? = null
