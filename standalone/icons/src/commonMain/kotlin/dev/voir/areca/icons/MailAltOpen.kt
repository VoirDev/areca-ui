package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Mail opened icon.
 *
 * Converted from Tabler Icons `mail-opened` (MIT).
 */
public val ArecaIcons.MailAltOpen: ImageVector
    get() = cachedMailAltOpen ?: ImageVector.Builder(
        name = "MailAltOpen",
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
            moveTo(3f, 9f)
            lineTo(12f, 15f)
            lineTo(21f, 9f)
            lineTo(12f, 3f)
            lineTo(3f, 9f)
            moveTo(21f, 9f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 19f, 21f)
            horizontalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 3f, 19f)
            verticalLineTo(9f)
            moveTo(3f, 19f)
            lineTo(9f, 13f)
            moveTo(15f, 13f)
            lineTo(21f, 19f)
        }
    }.build().also { cachedMailAltOpen = it }

/** `MailAltOpen` vector, built on first access and reused afterwards. */
private var cachedMailAltOpen: ImageVector? = null
