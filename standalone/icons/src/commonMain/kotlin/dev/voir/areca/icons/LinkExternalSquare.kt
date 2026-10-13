package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Square arrow out up right icon.
 *
 * Converted from Lucide `square-arrow-out-up-right` (ISC).
 */
public val ArecaIcons.LinkExternalSquare: ImageVector
    get() = cachedLinkExternalSquare ?: ImageVector.Builder(
        name = "LinkExternalSquare",
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
            moveTo(21f, 13f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 19f, 21f)
            horizontalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 3f, 19f)
            verticalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 5f, 3f)
            horizontalLineTo(11f)
            moveTo(21f, 3f)
            lineTo(12f, 12f)
            moveTo(15f, 3f)
            horizontalLineTo(21f)
            verticalLineTo(9f)
        }
    }.build().also { cachedLinkExternalSquare = it }

/** `LinkExternalSquare` vector, built on first access and reused afterwards. */
private var cachedLinkExternalSquare: ImageVector? = null
