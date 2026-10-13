package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Pen icon.
 *
 * Converted from Lucide `pen` (ISC).
 */
public val ArecaIcons.Edit: ImageVector
    get() = cachedEdit ?: ImageVector.Builder(
        name = "Edit",
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
            moveTo(21.174f, 6.812f)
            arcTo(1f, 1f, 0f, false, false, 17.188f, 2.825f)
            lineTo(3.842f, 16.174f)
            arcTo(2f, 2f, 0f, false, false, 3.342f, 17.004f)
            lineTo(2.021f, 21.356f)
            arcTo(0.5f, 0.5f, 0f, false, false, 2.644f, 21.978f)
            lineTo(6.997f, 20.658f)
            arcTo(2f, 2f, 0f, false, false, 7.827f, 20.161f)
            close()
        }
    }.build().also { cachedEdit = it }

/** `Edit` vector, built on first access and reused afterwards. */
private var cachedEdit: ImageVector? = null
