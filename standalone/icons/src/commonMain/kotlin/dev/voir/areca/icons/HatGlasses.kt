package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Hat glasses icon.
 *
 * Converted from Lucide `hat-glasses` (ISC).
 */
public val ArecaIcons.HatGlasses: ImageVector
    get() = cachedHatGlasses ?: ImageVector.Builder(
        name = "HatGlasses",
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
            moveTo(14f, 18f)
            arcTo(2f, 2f, 0f, false, false, 10f, 18f)
            moveTo(19f, 11f)
            lineTo(16.89f, 4.343f)
            arcTo(2f, 2f, 0f, false, false, 14.138f, 3.195f)
            lineTo(12.862f, 3.805f)
            arcTo(2f, 2f, 0f, false, true, 12f, 4f)
            horizontalLineTo(8.5f)
            arcTo(2f, 2f, 0f, false, false, 6.575f, 5.456f)
            lineTo(5f, 11f)
            moveTo(2f, 11f)
            horizontalLineTo(22f)
            moveTo(14f, 18f)
            arcTo(3f, 3f, 0f, true, true, 20f, 18f)
            arcTo(3f, 3f, 0f, true, true, 14f, 18f)
            close()
            moveTo(4f, 18f)
            arcTo(3f, 3f, 0f, true, true, 10f, 18f)
            arcTo(3f, 3f, 0f, true, true, 4f, 18f)
            close()
        }
    }.build().also { cachedHatGlasses = it }

/** `HatGlasses` vector, built on first access and reused afterwards. */
private var cachedHatGlasses: ImageVector? = null
