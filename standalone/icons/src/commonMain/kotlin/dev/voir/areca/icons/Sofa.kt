package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Sofa icon.
 *
 * Converted from Lucide `sofa` (ISC).
 */
public val ArecaIcons.Sofa: ImageVector
    get() = cachedSofa ?: ImageVector.Builder(
        name = "Sofa",
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
            moveTo(20f, 9f)
            verticalLineTo(6f)
            arcTo(2f, 2f, 0f, false, false, 18f, 4f)
            horizontalLineTo(6f)
            arcTo(2f, 2f, 0f, false, false, 4f, 6f)
            verticalLineTo(9f)
            moveTo(2f, 16f)
            arcTo(2f, 2f, 0f, false, false, 4f, 18f)
            horizontalLineTo(20f)
            arcTo(2f, 2f, 0f, false, false, 22f, 16f)
            verticalLineTo(11f)
            arcTo(2f, 2f, 0f, false, false, 18f, 11f)
            verticalLineTo(12.5f)
            arcTo(0.5f, 0.5f, 0f, false, true, 17.5f, 13f)
            horizontalLineTo(6.5f)
            arcTo(0.5f, 0.5f, 0f, false, true, 6f, 12.5f)
            verticalLineTo(11f)
            arcTo(2f, 2f, 0f, false, false, 2f, 11f)
            close()
            moveTo(4f, 18f)
            verticalLineTo(20f)
            moveTo(20f, 18f)
            verticalLineTo(20f)
            moveTo(12f, 4f)
            verticalLineTo(13f)
        }
    }.build().also { cachedSofa = it }

/** `Sofa` vector, built on first access and reused afterwards. */
private var cachedSofa: ImageVector? = null
