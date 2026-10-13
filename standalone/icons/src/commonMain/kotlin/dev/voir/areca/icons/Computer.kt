package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Computer icon.
 *
 * Converted from Lucide `computer` (ISC).
 */
public val ArecaIcons.Computer: ImageVector
    get() = cachedComputer ?: ImageVector.Builder(
        name = "Computer",
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
            moveTo(12f, 18f)
            horizontalLineTo(18f)
            moveTo(6f, 18f)
            horizontalLineTo(6.01f)
            moveTo(8f, 6f)
            horizontalLineTo(9f)
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
            moveTo(6f, 2f)
            horizontalLineTo(18f)
            arcTo(2f, 2f, 0f, false, true, 20f, 4f)
            verticalLineTo(12f)
            arcTo(2f, 2f, 0f, false, true, 18f, 14f)
            horizontalLineTo(6f)
            arcTo(2f, 2f, 0f, false, true, 4f, 12f)
            verticalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 6f, 2f)
            close()
        }
    }.build().also { cachedComputer = it }

/** `Computer` vector, built on first access and reused afterwards. */
private var cachedComputer: ImageVector? = null
