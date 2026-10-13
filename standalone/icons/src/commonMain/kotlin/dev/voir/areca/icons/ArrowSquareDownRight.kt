package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Square arrow down right icon.
 *
 * Mirrored horizontally in right-to-left layouts.
 *
 * Converted from Lucide `square-arrow-down-right` (ISC).
 */
public val ArecaIcons.ArrowSquareDownRight: ImageVector
    get() = cachedArrowSquareDownRight ?: ImageVector.Builder(
        name = "ArrowSquareDownRight",
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
            moveTo(15f, 15f)
            lineTo(9f, 9f)
            moveTo(9f, 15f)
            horizontalLineTo(15f)
            verticalLineTo(9f)
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
        }
    }.build().also { cachedArrowSquareDownRight = it }

/** `ArrowSquareDownRight` vector, built on first access and reused afterwards. */
private var cachedArrowSquareDownRight: ImageVector? = null
