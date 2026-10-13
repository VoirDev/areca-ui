package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Package icon.
 *
 * Converted from Lucide `package` (ISC).
 */
public val ArecaIcons.Package: ImageVector
    get() = cachedPackage ?: ImageVector.Builder(
        name = "Package",
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
            moveTo(11f, 21.73f)
            arcTo(2f, 2f, 0f, false, false, 13f, 21.73f)
            lineTo(20f, 17.73f)
            arcTo(2f, 2f, 0f, false, false, 21f, 16f)
            verticalLineTo(8f)
            arcTo(2f, 2f, 0f, false, false, 20f, 6.27f)
            lineTo(13f, 2.27f)
            arcTo(2f, 2f, 0f, false, false, 11f, 2.27f)
            lineTo(4f, 6.27f)
            arcTo(2f, 2f, 0f, false, false, 3f, 8f)
            verticalLineTo(16f)
            arcTo(2f, 2f, 0f, false, false, 4f, 17.73f)
            close()
            moveTo(12f, 22f)
            verticalLineTo(12f)
            moveTo(3.29f, 7f)
            lineTo(12f, 12f)
            lineTo(20.71f, 7f)
            moveTo(7.5f, 4.27f)
            lineTo(16.5f, 9.42f)
        }
    }.build().also { cachedPackage = it }

/** `Package` vector, built on first access and reused afterwards. */
private var cachedPackage: ImageVector? = null
