package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Archive icon.
 *
 * Same drawing as `ArecaIcons.Archive`; both names are kept.
 *
 * Converted from Lucide `archive` (ISC).
 */
public val ArecaIcons.Box: ImageVector
    get() = cachedBox ?: ImageVector.Builder(
        name = "Box",
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
            moveTo(3f, 3f)
            horizontalLineTo(21f)
            arcTo(1f, 1f, 0f, false, true, 22f, 4f)
            verticalLineTo(7f)
            arcTo(1f, 1f, 0f, false, true, 21f, 8f)
            horizontalLineTo(3f)
            arcTo(1f, 1f, 0f, false, true, 2f, 7f)
            verticalLineTo(4f)
            arcTo(1f, 1f, 0f, false, true, 3f, 3f)
            close()
            moveTo(4f, 8f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, false, 6f, 21f)
            horizontalLineTo(18f)
            arcTo(2f, 2f, 0f, false, false, 20f, 19f)
            verticalLineTo(8f)
            moveTo(10f, 12f)
            horizontalLineTo(14f)
        }
    }.build().also { cachedBox = it }

/** `Box` vector, built on first access and reused afterwards. */
private var cachedBox: ImageVector? = null
