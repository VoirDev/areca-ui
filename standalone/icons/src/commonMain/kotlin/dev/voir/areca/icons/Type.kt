package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Type icon.
 *
 * Converted from Lucide `type` (ISC).
 */
public val ArecaIcons.Type: ImageVector
    get() = cachedType ?: ImageVector.Builder(
        name = "Type",
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
            moveTo(12f, 4f)
            verticalLineTo(20f)
            moveTo(4f, 7f)
            verticalLineTo(5f)
            arcTo(1f, 1f, 0f, false, true, 5f, 4f)
            horizontalLineTo(19f)
            arcTo(1f, 1f, 0f, false, true, 20f, 5f)
            verticalLineTo(7f)
            moveTo(9f, 20f)
            horizontalLineTo(15f)
        }
    }.build().also { cachedType = it }

/** `Type` vector, built on first access and reused afterwards. */
private var cachedType: ImageVector? = null
