package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Plug icon.
 *
 * Converted from Lucide `plug` (ISC).
 */
public val ArecaIcons.Plug: ImageVector
    get() = cachedPlug ?: ImageVector.Builder(
        name = "Plug",
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
            moveTo(12f, 22f)
            verticalLineTo(17f)
            moveTo(15f, 8f)
            verticalLineTo(2f)
            moveTo(17f, 8f)
            arcTo(1f, 1f, 0f, false, true, 18f, 9f)
            verticalLineTo(13f)
            arcTo(4f, 4f, 0f, false, true, 14f, 17f)
            horizontalLineTo(10f)
            arcTo(4f, 4f, 0f, false, true, 6f, 13f)
            verticalLineTo(9f)
            arcTo(1f, 1f, 0f, false, true, 7f, 8f)
            close()
            moveTo(9f, 8f)
            verticalLineTo(2f)
        }
    }.build().also { cachedPlug = it }

/** `Plug` vector, built on first access and reused afterwards. */
private var cachedPlug: ImageVector? = null
