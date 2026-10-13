package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Shower head icon.
 *
 * Converted from Lucide `shower-head` (ISC).
 */
public val ArecaIcons.ShowerHead: ImageVector
    get() = cachedShowerHead ?: ImageVector.Builder(
        name = "ShowerHead",
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
            moveTo(4f, 4f)
            lineTo(6.5f, 6.5f)
            moveTo(13.5f, 6.5f)
            arcTo(4.95f, 4.95f, 0f, false, false, 6.5f, 13.5f)
            moveTo(15f, 5f)
            lineTo(5f, 15f)
            moveTo(14f, 17f)
            verticalLineTo(17.01f)
            moveTo(10f, 16f)
            verticalLineTo(16.01f)
            moveTo(13f, 13f)
            verticalLineTo(13.01f)
            moveTo(16f, 10f)
            verticalLineTo(10.01f)
            moveTo(11f, 20f)
            verticalLineTo(20.01f)
            moveTo(17f, 14f)
            verticalLineTo(14.01f)
            moveTo(20f, 11f)
            verticalLineTo(11.01f)
        }
    }.build().also { cachedShowerHead = it }

/** `ShowerHead` vector, built on first access and reused afterwards. */
private var cachedShowerHead: ImageVector? = null
