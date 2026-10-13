package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Rubber stamp icon.
 *
 * Converted from Tabler Icons `rubber-stamp` (MIT).
 */
public val ArecaIcons.RubberStamp: ImageVector
    get() = cachedRubberStamp ?: ImageVector.Builder(
        name = "RubberStamp",
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
            moveTo(21f, 17.85f)
            horizontalLineTo(3f)
            curveTo(3f, 13.8f, 4.421f, 13.8f, 6.79f, 13.8f)
            curveTo(12f, 13.8f, 8f, 9.21f, 8f, 7f)
            arcTo(4f, 4f, 0f, true, true, 16f, 7f)
            curveTo(16f, 9.21f, 12f, 13.8f, 17.21f, 13.8f)
            curveTo(19.579f, 13.8f, 21f, 13.8f, 21f, 17.85f)
            moveTo(5f, 21f)
            horizontalLineTo(19f)
        }
    }.build().also { cachedRubberStamp = it }

/** `RubberStamp` vector, built on first access and reused afterwards. */
private var cachedRubberStamp: ImageVector? = null
