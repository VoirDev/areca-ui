package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Nfc icon.
 *
 * Converted from Lucide `nfc` (ISC).
 */
public val ArecaIcons.Nfc: ImageVector
    get() = cachedNfc ?: ImageVector.Builder(
        name = "Nfc",
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
            moveTo(6f, 8.32f)
            arcTo(7.43f, 7.43f, 0f, false, true, 6f, 15.68f)
            moveTo(9.46f, 6.21f)
            arcTo(11.76f, 11.76f, 0f, false, true, 9.46f, 17.79f)
            moveTo(12.91f, 4.1f)
            arcTo(15.91f, 15.91f, 0f, false, true, 12.92f, 19.9f)
            moveTo(16.37f, 2f)
            arcTo(20.16f, 20.16f, 0f, false, true, 16.37f, 22f)
        }
    }.build().also { cachedNfc = it }

/** `Nfc` vector, built on first access and reused afterwards. */
private var cachedNfc: ImageVector? = null
