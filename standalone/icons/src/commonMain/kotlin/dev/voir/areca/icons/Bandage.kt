package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Bandage icon.
 *
 * Converted from Lucide `bandage` (ISC).
 */
public val ArecaIcons.Bandage: ImageVector
    get() = cachedBandage ?: ImageVector.Builder(
        name = "Bandage",
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
            moveTo(10f, 10.01f)
            horizontalLineTo(10.01f)
            moveTo(10f, 14.01f)
            horizontalLineTo(10.01f)
            moveTo(14f, 10.01f)
            horizontalLineTo(14.01f)
            moveTo(14f, 14.01f)
            horizontalLineTo(14.01f)
            moveTo(18f, 6f)
            verticalLineTo(18f)
            moveTo(6f, 6f)
            verticalLineTo(18f)
            moveTo(4f, 6f)
            horizontalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 22f, 8f)
            verticalLineTo(16f)
            arcTo(2f, 2f, 0f, false, true, 20f, 18f)
            horizontalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 2f, 16f)
            verticalLineTo(8f)
            arcTo(2f, 2f, 0f, false, true, 4f, 6f)
            close()
        }
    }.build().also { cachedBandage = it }

/** `Bandage` vector, built on first access and reused afterwards. */
private var cachedBandage: ImageVector? = null
