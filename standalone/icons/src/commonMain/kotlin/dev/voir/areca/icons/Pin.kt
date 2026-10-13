package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Pin icon.
 *
 * Converted from Lucide `pin` (ISC).
 */
public val ArecaIcons.Pin: ImageVector
    get() = cachedPin ?: ImageVector.Builder(
        name = "Pin",
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
            moveTo(12f, 17f)
            verticalLineTo(22f)
            moveTo(9f, 10.76f)
            arcTo(2f, 2f, 0f, false, true, 7.89f, 12.55f)
            lineTo(6.11f, 13.45f)
            arcTo(2f, 2f, 0f, false, false, 5f, 15.24f)
            verticalLineTo(16f)
            arcTo(1f, 1f, 0f, false, false, 6f, 17f)
            horizontalLineTo(18f)
            arcTo(1f, 1f, 0f, false, false, 19f, 16f)
            verticalLineTo(15.24f)
            arcTo(2f, 2f, 0f, false, false, 17.89f, 13.45f)
            lineTo(16.11f, 12.55f)
            arcTo(2f, 2f, 0f, false, true, 15f, 10.76f)
            verticalLineTo(7f)
            arcTo(1f, 1f, 0f, false, true, 16f, 6f)
            arcTo(2f, 2f, 0f, false, false, 16f, 2f)
            horizontalLineTo(8f)
            arcTo(2f, 2f, 0f, false, false, 8f, 6f)
            arcTo(1f, 1f, 0f, false, true, 9f, 7f)
            close()
        }
    }.build().also { cachedPin = it }

/** `Pin` vector, built on first access and reused afterwards. */
private var cachedPin: ImageVector? = null
