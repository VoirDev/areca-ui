package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Stamp icon.
 *
 * Converted from Lucide `stamp` (ISC).
 */
public val ArecaIcons.Stamp: ImageVector
    get() = cachedStamp ?: ImageVector.Builder(
        name = "Stamp",
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
            moveTo(14f, 13f)
            verticalLineTo(8.5f)
            curveTo(14f, 7f, 15f, 7f, 15f, 5f)
            arcTo(3f, 3f, 0f, false, false, 9f, 5f)
            curveTo(9f, 7f, 10f, 7f, 10f, 8.5f)
            verticalLineTo(13f)
            moveTo(20f, 15.5f)
            arcTo(2.5f, 2.5f, 0f, false, false, 17.5f, 13f)
            horizontalLineTo(6.5f)
            arcTo(2.5f, 2.5f, 0f, false, false, 4f, 15.5f)
            verticalLineTo(17f)
            arcTo(1f, 1f, 0f, false, false, 5f, 18f)
            horizontalLineTo(19f)
            arcTo(1f, 1f, 0f, false, false, 20f, 17f)
            close()
            moveTo(5f, 22f)
            horizontalLineTo(19f)
        }
    }.build().also { cachedStamp = it }

/** `Stamp` vector, built on first access and reused afterwards. */
private var cachedStamp: ImageVector? = null
