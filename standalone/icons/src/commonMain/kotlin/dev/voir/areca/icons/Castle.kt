package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Castle icon.
 *
 * Converted from Lucide `castle` (ISC).
 */
public val ArecaIcons.Castle: ImageVector
    get() = cachedCastle ?: ImageVector.Builder(
        name = "Castle",
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
            moveTo(10f, 5f)
            verticalLineTo(3f)
            moveTo(14f, 5f)
            verticalLineTo(3f)
            moveTo(15f, 21f)
            verticalLineTo(18f)
            arcTo(3f, 3f, 0f, false, false, 9f, 18f)
            verticalLineTo(21f)
            moveTo(18f, 3f)
            verticalLineTo(11f)
            moveTo(18f, 5f)
            horizontalLineTo(6f)
            moveTo(22f, 11f)
            horizontalLineTo(2f)
            moveTo(22f, 9f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 20f, 21f)
            horizontalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 2f, 19f)
            verticalLineTo(9f)
            moveTo(6f, 3f)
            verticalLineTo(11f)
        }
    }.build().also { cachedCastle = it }

/** `Castle` vector, built on first access and reused afterwards. */
private var cachedCastle: ImageVector? = null
