package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Tickets icon.
 *
 * Converted from Lucide `tickets` (ISC).
 */
public val ArecaIcons.Tickets: ImageVector
    get() = cachedTickets ?: ImageVector.Builder(
        name = "Tickets",
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
            moveTo(3.173f, 8.18f)
            lineTo(14.173f, 3.18f)
            arcTo(2f, 2f, 0f, false, true, 16.82f, 4.173f)
            lineTo(18.56f, 8f)
            moveTo(6f, 10f)
            verticalLineTo(8f)
            moveTo(6f, 14f)
            verticalLineTo(15f)
            moveTo(6f, 19f)
            verticalLineTo(21f)
            moveTo(4f, 8f)
            horizontalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 22f, 10f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 20f, 21f)
            horizontalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 2f, 19f)
            verticalLineTo(10f)
            arcTo(2f, 2f, 0f, false, true, 4f, 8f)
            close()
        }
    }.build().also { cachedTickets = it }

/** `Tickets` vector, built on first access and reused afterwards. */
private var cachedTickets: ImageVector? = null
