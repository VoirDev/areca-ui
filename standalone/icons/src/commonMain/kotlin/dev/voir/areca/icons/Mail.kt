package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Mail icon.
 *
 * Converted from Lucide `mail` (ISC).
 */
public val ArecaIcons.Mail: ImageVector
    get() = cachedMail ?: ImageVector.Builder(
        name = "Mail",
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
            moveTo(22f, 7f)
            lineTo(13.009f, 12.727f)
            arcTo(2f, 2f, 0f, false, true, 11f, 12.727f)
            lineTo(2f, 7f)
            moveTo(4f, 4f)
            horizontalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 22f, 6f)
            verticalLineTo(18f)
            arcTo(2f, 2f, 0f, false, true, 20f, 20f)
            horizontalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 2f, 18f)
            verticalLineTo(6f)
            arcTo(2f, 2f, 0f, false, true, 4f, 4f)
            close()
        }
    }.build().also { cachedMail = it }

/** `Mail` vector, built on first access and reused afterwards. */
private var cachedMail: ImageVector? = null
