package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Anchor icon.
 *
 * Converted from Lucide `anchor` (ISC).
 */
public val ArecaIcons.Anchor: ImageVector
    get() = cachedAnchor ?: ImageVector.Builder(
        name = "Anchor",
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
            moveTo(12f, 6f)
            verticalLineTo(22f)
            moveTo(19f, 13f)
            lineTo(21f, 12f)
            arcTo(9f, 9f, 0f, false, true, 3f, 12f)
            lineTo(5f, 13f)
            moveTo(9f, 11f)
            horizontalLineTo(15f)
            moveTo(10f, 4f)
            arcTo(2f, 2f, 0f, true, true, 14f, 4f)
            arcTo(2f, 2f, 0f, true, true, 10f, 4f)
            close()
        }
    }.build().also { cachedAnchor = it }

/** `Anchor` vector, built on first access and reused afterwards. */
private var cachedAnchor: ImageVector? = null
