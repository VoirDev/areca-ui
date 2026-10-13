package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Share 2 icon.
 *
 * Converted from Lucide `share-2` (ISC).
 */
public val ArecaIcons.ShareNodes: ImageVector
    get() = cachedShareNodes ?: ImageVector.Builder(
        name = "ShareNodes",
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
            moveTo(15f, 5f)
            arcTo(3f, 3f, 0f, true, true, 21f, 5f)
            arcTo(3f, 3f, 0f, true, true, 15f, 5f)
            close()
            moveTo(3f, 12f)
            arcTo(3f, 3f, 0f, true, true, 9f, 12f)
            arcTo(3f, 3f, 0f, true, true, 3f, 12f)
            close()
            moveTo(15f, 19f)
            arcTo(3f, 3f, 0f, true, true, 21f, 19f)
            arcTo(3f, 3f, 0f, true, true, 15f, 19f)
            close()
            moveTo(8.59f, 13.51f)
            lineTo(15.42f, 17.49f)
            moveTo(15.41f, 6.51f)
            lineTo(8.59f, 10.49f)
        }
    }.build().also { cachedShareNodes = it }

/** `ShareNodes` vector, built on first access and reused afterwards. */
private var cachedShareNodes: ImageVector? = null
