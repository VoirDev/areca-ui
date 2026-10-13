package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Tornado icon.
 *
 * Converted from Lucide `tornado` (ISC).
 */
public val ArecaIcons.Hurricane: ImageVector
    get() = cachedHurricane ?: ImageVector.Builder(
        name = "Hurricane",
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
            moveTo(21f, 4f)
            horizontalLineTo(3f)
            moveTo(18f, 8f)
            horizontalLineTo(6f)
            moveTo(19f, 12f)
            horizontalLineTo(9f)
            moveTo(16f, 16f)
            horizontalLineTo(10f)
            moveTo(11f, 20f)
            horizontalLineTo(9f)
        }
    }.build().also { cachedHurricane = it }

/** `Hurricane` vector, built on first access and reused afterwards. */
private var cachedHurricane: ImageVector? = null
