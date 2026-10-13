package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Signature icon.
 *
 * Converted from Lucide `signature` (ISC).
 */
public val ArecaIcons.Signature: ImageVector
    get() = cachedSignature ?: ImageVector.Builder(
        name = "Signature",
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
            moveTo(21f, 17f)
            lineTo(18.844f, 15.132f)
            arcTo(0.5f, 0.5f, 0f, false, false, 18f, 15.5f)
            verticalLineTo(16f)
            arcTo(1f, 1f, 0f, false, true, 17f, 17f)
            horizontalLineTo(15f)
            arcTo(1f, 1f, 0f, false, true, 14f, 16f)
            curveTo(14f, 13.455f, 10.009f, 12.03f, 5.5f, 12f)
            arcTo(1f, 1f, 0f, false, false, 5.5f, 17f)
            curveTo(9.653f, 17f, 10.245f, 5.705f, 11.208f, 3.5f)
            arcTo(2.5f, 2.5f, 0f, true, true, 14.518f, 6.784f)
            moveTo(3f, 21f)
            horizontalLineTo(21f)
        }
    }.build().also { cachedSignature = it }

/** `Signature` vector, built on first access and reused afterwards. */
private var cachedSignature: ImageVector? = null
