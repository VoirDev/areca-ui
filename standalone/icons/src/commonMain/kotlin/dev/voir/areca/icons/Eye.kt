package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Eye icon.
 *
 * Converted from Lucide `eye` (ISC).
 */
public val ArecaIcons.Eye: ImageVector
    get() = cachedEye ?: ImageVector.Builder(
        name = "Eye",
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
            moveTo(2.062f, 12.348f)
            arcTo(1f, 1f, 0f, false, true, 2.062f, 11.652f)
            arcTo(10.75f, 10.75f, 0f, false, true, 21.938f, 11.652f)
            arcTo(1f, 1f, 0f, false, true, 21.938f, 12.348f)
            arcTo(10.75f, 10.75f, 0f, false, true, 2.062f, 12.348f)
            moveTo(9f, 12f)
            arcTo(3f, 3f, 0f, true, true, 15f, 12f)
            arcTo(3f, 3f, 0f, true, true, 9f, 12f)
            close()
        }
    }.build().also { cachedEye = it }

/** `Eye` vector, built on first access and reused afterwards. */
private var cachedEye: ImageVector? = null
