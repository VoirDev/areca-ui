package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Bot icon.
 *
 * Converted from Lucide `bot` (ISC).
 */
public val ArecaIcons.Bot: ImageVector
    get() = cachedBot ?: ImageVector.Builder(
        name = "Bot",
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
            moveTo(12f, 8f)
            verticalLineTo(4f)
            horizontalLineTo(8f)
            moveTo(6f, 8f)
            horizontalLineTo(18f)
            arcTo(2f, 2f, 0f, false, true, 20f, 10f)
            verticalLineTo(18f)
            arcTo(2f, 2f, 0f, false, true, 18f, 20f)
            horizontalLineTo(6f)
            arcTo(2f, 2f, 0f, false, true, 4f, 18f)
            verticalLineTo(10f)
            arcTo(2f, 2f, 0f, false, true, 6f, 8f)
            close()
            moveTo(2f, 14f)
            horizontalLineTo(4f)
            moveTo(20f, 14f)
            horizontalLineTo(22f)
            moveTo(15f, 13f)
            verticalLineTo(15f)
            moveTo(9f, 13f)
            verticalLineTo(15f)
        }
    }.build().also { cachedBot = it }

/** `Bot` vector, built on first access and reused afterwards. */
private var cachedBot: ImageVector? = null
