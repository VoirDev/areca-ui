package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Arrow down right icon.
 *
 * Mirrored horizontally in right-to-left layouts.
 *
 * Converted from Lucide `arrow-down-right` (ISC).
 */
public val ArecaIcons.ArrowDownRight: ImageVector
    get() = cachedArrowDownRight ?: ImageVector.Builder(
        name = "ArrowDownRight",
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
            moveTo(7f, 7f)
            lineTo(17f, 17f)
            moveTo(17f, 7f)
            verticalLineTo(17f)
            horizontalLineTo(7f)
        }
    }.build().also { cachedArrowDownRight = it }

/** `ArrowDownRight` vector, built on first access and reused afterwards. */
private var cachedArrowDownRight: ImageVector? = null
