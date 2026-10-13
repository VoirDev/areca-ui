package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Microwave icon.
 *
 * Converted from Lucide `microwave` (ISC).
 */
public val ArecaIcons.Microwave: ImageVector
    get() = cachedMicrowave ?: ImageVector.Builder(
        name = "Microwave",
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
            moveTo(4f, 4f)
            horizontalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 22f, 6f)
            verticalLineTo(17f)
            arcTo(2f, 2f, 0f, false, true, 20f, 19f)
            horizontalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 2f, 17f)
            verticalLineTo(6f)
            arcTo(2f, 2f, 0f, false, true, 4f, 4f)
            close()
            moveTo(7f, 8f)
            horizontalLineTo(13f)
            arcTo(1f, 1f, 0f, false, true, 14f, 9f)
            verticalLineTo(14f)
            arcTo(1f, 1f, 0f, false, true, 13f, 15f)
            horizontalLineTo(7f)
            arcTo(1f, 1f, 0f, false, true, 6f, 14f)
            verticalLineTo(9f)
            arcTo(1f, 1f, 0f, false, true, 7f, 8f)
            close()
            moveTo(18f, 8f)
            verticalLineTo(15f)
            moveTo(6f, 19f)
            verticalLineTo(21f)
            moveTo(18f, 19f)
            verticalLineTo(21f)
        }
    }.build().also { cachedMicrowave = it }

/** `Microwave` vector, built on first access and reused afterwards. */
private var cachedMicrowave: ImageVector? = null
