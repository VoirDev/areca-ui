package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Massage icon.
 *
 * Converted from Tabler Icons `massage` (MIT).
 */
public val ArecaIcons.Massage: ImageVector
    get() = cachedMassage ?: ImageVector.Builder(
        name = "Massage",
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
            moveTo(3f, 17f)
            arcTo(1f, 1f, 0f, true, false, 5f, 17f)
            arcTo(1f, 1f, 0f, true, false, 3f, 17f)
            moveTo(8f, 5f)
            arcTo(1f, 1f, 0f, true, false, 10f, 5f)
            arcTo(1f, 1f, 0f, true, false, 8f, 5f)
            moveTo(4f, 22f)
            lineTo(8f, 20f)
            verticalLineTo(17f)
            horizontalLineTo(20f)
            moveTo(11f, 20f)
            horizontalLineTo(20f)
            moveTo(8f, 14f)
            lineTo(11f, 12f)
            lineTo(12f, 8f)
            curveTo(15f, 9f, 15f, 12f, 15f, 14f)
        }
    }.build().also { cachedMassage = it }

/** `Massage` vector, built on first access and reused afterwards. */
private var cachedMassage: ImageVector? = null
