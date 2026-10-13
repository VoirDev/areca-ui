package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Siren icon.
 *
 * Converted from Lucide `siren` (ISC).
 */
public val ArecaIcons.Siren: ImageVector
    get() = cachedSiren ?: ImageVector.Builder(
        name = "Siren",
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
            moveTo(7f, 18f)
            verticalLineTo(12f)
            arcTo(5f, 5f, 0f, true, true, 17f, 12f)
            verticalLineTo(18f)
            moveTo(5f, 21f)
            arcTo(1f, 1f, 0f, false, false, 6f, 22f)
            horizontalLineTo(18f)
            arcTo(1f, 1f, 0f, false, false, 19f, 21f)
            verticalLineTo(20f)
            arcTo(2f, 2f, 0f, false, false, 17f, 18f)
            horizontalLineTo(7f)
            arcTo(2f, 2f, 0f, false, false, 5f, 20f)
            close()
            moveTo(21f, 12f)
            horizontalLineTo(22f)
            moveTo(18.5f, 4.5f)
            lineTo(18f, 5f)
            moveTo(2f, 12f)
            horizontalLineTo(3f)
            moveTo(12f, 2f)
            verticalLineTo(3f)
            moveTo(4.929f, 4.929f)
            lineTo(5.636f, 5.636f)
            moveTo(12f, 12f)
            verticalLineTo(18f)
        }
    }.build().also { cachedSiren = it }

/** `Siren` vector, built on first access and reused afterwards. */
private var cachedSiren: ImageVector? = null
