package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Certificate icon.
 *
 * Converted from Tabler Icons `certificate` (MIT).
 */
public val ArecaIcons.Certificate: ImageVector
    get() = cachedCertificate ?: ImageVector.Builder(
        name = "Certificate",
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
            moveTo(12f, 15f)
            arcTo(3f, 3f, 0f, true, false, 18f, 15f)
            arcTo(3f, 3f, 0f, true, false, 12f, 15f)
            moveTo(13f, 17.5f)
            verticalLineTo(22f)
            lineTo(15f, 20.5f)
            lineTo(17f, 22f)
            verticalLineTo(17.5f)
            moveTo(10f, 19f)
            horizontalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 3f, 17f)
            verticalLineTo(7f)
            curveTo(3f, 5.9f, 3.9f, 5f, 5f, 5f)
            horizontalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 21f, 7f)
            verticalLineTo(17f)
            arcTo(2f, 2f, 0f, false, true, 20f, 18.73f)
            moveTo(6f, 9f)
            lineTo(18f, 9f)
            moveTo(6f, 12f)
            lineTo(9f, 12f)
            moveTo(6f, 15f)
            lineTo(8f, 15f)
        }
    }.build().also { cachedCertificate = it }

/** `Certificate` vector, built on first access and reused afterwards. */
private var cachedCertificate: ImageVector? = null
