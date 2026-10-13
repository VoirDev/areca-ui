package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Fingerprint pattern icon.
 *
 * Converted from Lucide `fingerprint-pattern` (ISC).
 */
public val ArecaIcons.Fingerprint: ImageVector
    get() = cachedFingerprint ?: ImageVector.Builder(
        name = "Fingerprint",
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
            moveTo(12f, 10f)
            arcTo(2f, 2f, 0f, false, false, 10f, 12f)
            curveTo(10f, 13.02f, 9.9f, 14.51f, 9.74f, 16f)
            moveTo(14f, 13.12f)
            curveTo(14f, 15.5f, 14f, 19.5f, 13f, 22f)
            moveTo(17.29f, 21.02f)
            curveTo(17.41f, 20.42f, 17.72f, 18.72f, 17.79f, 18f)
            moveTo(2f, 12f)
            arcTo(10f, 10f, 0f, false, true, 20f, 6f)
            moveTo(2f, 16f)
            horizontalLineTo(2.01f)
            moveTo(21.8f, 16f)
            curveTo(22f, 14f, 21.931f, 10.646f, 21.8f, 10f)
            moveTo(5f, 19.5f)
            curveTo(5.5f, 18f, 6f, 15f, 6f, 12f)
            arcTo(6f, 6f, 0f, false, true, 6.34f, 10f)
            moveTo(8.65f, 22f)
            curveTo(8.86f, 21.34f, 9.1f, 20.68f, 9.22f, 20f)
            moveTo(9f, 6.8f)
            arcTo(6f, 6f, 0f, false, true, 18f, 12f)
            verticalLineTo(14f)
        }
    }.build().also { cachedFingerprint = it }

/** `Fingerprint` vector, built on first access and reused afterwards. */
private var cachedFingerprint: ImageVector? = null
