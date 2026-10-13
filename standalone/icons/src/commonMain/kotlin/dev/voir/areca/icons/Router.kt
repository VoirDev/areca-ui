package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Router icon.
 *
 * Converted from Lucide `router` (ISC).
 */
public val ArecaIcons.Router: ImageVector
    get() = cachedRouter ?: ImageVector.Builder(
        name = "Router",
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
            moveTo(4f, 14f)
            horizontalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 22f, 16f)
            verticalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 20f, 22f)
            horizontalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 2f, 20f)
            verticalLineTo(16f)
            arcTo(2f, 2f, 0f, false, true, 4f, 14f)
            close()
            moveTo(6.01f, 18f)
            horizontalLineTo(6f)
            moveTo(10.01f, 18f)
            horizontalLineTo(10f)
            moveTo(15f, 10f)
            verticalLineTo(14f)
            moveTo(17.84f, 7.17f)
            arcTo(4f, 4f, 0f, false, false, 12.18f, 7.17f)
            moveTo(20.66f, 4.34f)
            arcTo(8f, 8f, 0f, false, false, 9.35f, 4.34f)
        }
    }.build().also { cachedRouter = it }

/** `Router` vector, built on first access and reused afterwards. */
private var cachedRouter: ImageVector? = null
