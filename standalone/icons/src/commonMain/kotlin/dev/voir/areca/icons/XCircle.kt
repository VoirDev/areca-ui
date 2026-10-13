package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Circle x icon.
 *
 * Same drawing as `ArecaIcons.CrossCircle`; both names are kept.
 *
 * Converted from Lucide `circle-x` (ISC).
 */
public val ArecaIcons.XCircle: ImageVector
    get() = cachedXCircle ?: ImageVector.Builder(
        name = "XCircle",
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
            moveTo(2f, 12f)
            arcTo(10f, 10f, 0f, true, true, 22f, 12f)
            arcTo(10f, 10f, 0f, true, true, 2f, 12f)
            close()
            moveTo(15f, 9f)
            lineTo(9f, 15f)
            moveTo(9f, 9f)
            lineTo(15f, 15f)
        }
    }.build().also { cachedXCircle = it }

/** `XCircle` vector, built on first access and reused afterwards. */
private var cachedXCircle: ImageVector? = null
