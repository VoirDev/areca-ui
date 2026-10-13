package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Mailbox icon.
 *
 * Converted from Lucide `mailbox` (ISC).
 */
public val ArecaIcons.Mailbox: ImageVector
    get() = cachedMailbox ?: ImageVector.Builder(
        name = "Mailbox",
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
            moveTo(22f, 17f)
            arcTo(2f, 2f, 0f, false, true, 20f, 19f)
            horizontalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 2f, 17f)
            verticalLineTo(9.5f)
            curveTo(2f, 7f, 4f, 5f, 6.5f, 5f)
            horizontalLineTo(18f)
            curveTo(20.2f, 5f, 22f, 6.8f, 22f, 9f)
            verticalLineTo(17f)
            close()
            moveTo(15f, 9f)
            lineTo(18f, 9f)
            lineTo(18f, 11f)
            moveTo(6.5f, 5f)
            curveTo(9f, 5f, 11f, 7f, 11f, 9.5f)
            verticalLineTo(17f)
            arcTo(2f, 2f, 0f, false, true, 9f, 19f)
            moveTo(6f, 10f)
            lineTo(7f, 10f)
        }
    }.build().also { cachedMailbox = it }

/** `Mailbox` vector, built on first access and reused afterwards. */
private var cachedMailbox: ImageVector? = null
