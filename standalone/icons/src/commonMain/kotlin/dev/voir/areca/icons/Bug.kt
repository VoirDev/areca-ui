package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Bug icon.
 *
 * Converted from Lucide `bug` (ISC).
 */
public val ArecaIcons.Bug: ImageVector
    get() = cachedBug ?: ImageVector.Builder(
        name = "Bug",
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
            moveTo(12f, 20f)
            verticalLineTo(11f)
            moveTo(14f, 7f)
            arcTo(4f, 4f, 0f, false, true, 18f, 11f)
            verticalLineTo(14f)
            arcTo(6f, 6f, 0f, false, true, 6f, 14f)
            verticalLineTo(11f)
            arcTo(4f, 4f, 0f, false, true, 10f, 7f)
            close()
            moveTo(14.12f, 3.88f)
            lineTo(16f, 2f)
            moveTo(21f, 21f)
            arcTo(4f, 4f, 0f, false, false, 17.19f, 17f)
            moveTo(21f, 5f)
            arcTo(4f, 4f, 0f, false, true, 17.45f, 8.97f)
            moveTo(22f, 13f)
            horizontalLineTo(18f)
            moveTo(3f, 21f)
            arcTo(4f, 4f, 0f, false, true, 6.81f, 17f)
            moveTo(3f, 5f)
            arcTo(4f, 4f, 0f, false, false, 6.55f, 8.97f)
            moveTo(6f, 13f)
            horizontalLineTo(2f)
            moveTo(8f, 2f)
            lineTo(9.88f, 3.88f)
            moveTo(9f, 7.13f)
            verticalLineTo(6f)
            arcTo(3f, 3f, 0f, true, true, 15f, 6f)
            verticalLineTo(7.13f)
        }
    }.build().also { cachedBug = it }

/** `Bug` vector, built on first access and reused afterwards. */
private var cachedBug: ImageVector? = null
