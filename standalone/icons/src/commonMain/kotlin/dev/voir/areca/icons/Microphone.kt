package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Mic icon.
 *
 * Converted from Lucide `mic` (ISC).
 */
public val ArecaIcons.Microphone: ImageVector
    get() = cachedMicrophone ?: ImageVector.Builder(
        name = "Microphone",
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
            moveTo(12f, 19f)
            verticalLineTo(22f)
            moveTo(19f, 10f)
            verticalLineTo(12f)
            arcTo(7f, 7f, 0f, false, true, 5f, 12f)
            verticalLineTo(10f)
            moveTo(12f, 2f)
            horizontalLineTo(12f)
            arcTo(3f, 3f, 0f, false, true, 15f, 5f)
            verticalLineTo(12f)
            arcTo(3f, 3f, 0f, false, true, 12f, 15f)
            horizontalLineTo(12f)
            arcTo(3f, 3f, 0f, false, true, 9f, 12f)
            verticalLineTo(5f)
            arcTo(3f, 3f, 0f, false, true, 12f, 2f)
            close()
        }
    }.build().also { cachedMicrophone = it }

/** `Microphone` vector, built on first access and reused afterwards. */
private var cachedMicrophone: ImageVector? = null
