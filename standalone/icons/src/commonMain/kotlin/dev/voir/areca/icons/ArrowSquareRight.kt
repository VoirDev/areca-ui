package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Square arrow right icon.
 *
 * Mirrored horizontally in right-to-left layouts.
 *
 * Converted from Lucide `square-arrow-right` (ISC).
 */
public val ArecaIcons.ArrowSquareRight: ImageVector
    get() = cachedArrowSquareRight ?: ImageVector.Builder(
        name = "ArrowSquareRight",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
        autoMirror = true,
    ).apply {
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
            moveTo(5f, 3f)
            horizontalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 21f, 5f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 19f, 21f)
            horizontalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 3f, 19f)
            verticalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 5f, 3f)
            close()
            moveTo(8f, 12f)
            horizontalLineTo(16f)
            moveTo(12f, 16f)
            lineTo(16f, 12f)
            lineTo(12f, 8f)
        }
    }.build().also { cachedArrowSquareRight = it }

/** `ArrowSquareRight` vector, built on first access and reused afterwards. */
private var cachedArrowSquareRight: ImageVector? = null
