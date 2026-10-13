package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Inbox icon.
 *
 * Converted from Lucide `inbox` (ISC).
 */
public val ArecaIcons.Inbox: ImageVector
    get() = cachedInbox ?: ImageVector.Builder(
        name = "Inbox",
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
            moveTo(22f, 12f)
            lineTo(16f, 12f)
            lineTo(14f, 15f)
            lineTo(10f, 15f)
            lineTo(8f, 12f)
            lineTo(2f, 12f)
            moveTo(5.45f, 5.11f)
            lineTo(2f, 12f)
            verticalLineTo(18f)
            arcTo(2f, 2f, 0f, false, false, 4f, 20f)
            horizontalLineTo(20f)
            arcTo(2f, 2f, 0f, false, false, 22f, 18f)
            verticalLineTo(12f)
            lineTo(18.55f, 5.11f)
            arcTo(2f, 2f, 0f, false, false, 16.76f, 4f)
            horizontalLineTo(7.24f)
            arcTo(2f, 2f, 0f, false, false, 5.45f, 5.11f)
            close()
        }
    }.build().also { cachedInbox = it }

/** `Inbox` vector, built on first access and reused afterwards. */
private var cachedInbox: ImageVector? = null
