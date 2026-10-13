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
 * Converted from Tabler Icons `mail` (MIT).
 */
public val ArecaIcons.MailAlt: ImageVector
    get() = cachedMailAlt ?: ImageVector.Builder(
        name = "MailAlt",
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
            moveTo(3f, 7f)
            arcTo(2f, 2f, 0f, false, true, 5f, 5f)
            horizontalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 21f, 7f)
            verticalLineTo(17f)
            arcTo(2f, 2f, 0f, false, true, 19f, 19f)
            horizontalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 3f, 17f)
            verticalLineTo(7f)
            moveTo(3f, 7f)
            lineTo(12f, 13f)
            lineTo(21f, 7f)
        }
    }.build().also { cachedMailAlt = it }

/** `MailAlt` vector, built on first access and reused afterwards. */
private var cachedMailAlt: ImageVector? = null
