package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Package open icon.
 *
 * Converted from Lucide `package-open` (ISC).
 */
public val ArecaIcons.PackageOpen: ImageVector
    get() = cachedPackageOpen ?: ImageVector.Builder(
        name = "PackageOpen",
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
            moveTo(12f, 22f)
            verticalLineTo(13f)
            moveTo(15.17f, 2.21f)
            arcTo(1.67f, 1.67f, 0f, false, true, 16.8f, 2.21f)
            lineTo(21f, 4.57f)
            arcTo(1.93f, 1.93f, 0f, false, true, 21f, 7.93f)
            lineTo(8.82f, 14.79f)
            arcTo(1.655f, 1.655f, 0f, false, true, 7.18f, 14.79f)
            lineTo(3f, 12.43f)
            arcTo(1.93f, 1.93f, 0f, false, true, 3f, 9.07f)
            close()
            moveTo(20f, 13f)
            verticalLineTo(16.87f)
            arcTo(2.06f, 2.06f, 0f, false, true, 18.89f, 18.7f)
            lineTo(12.89f, 21.78f)
            arcTo(1.93f, 1.93f, 0f, false, true, 11.11f, 21.78f)
            lineTo(5.11f, 18.7f)
            arcTo(2.06f, 2.06f, 0f, false, true, 4f, 16.87f)
            verticalLineTo(13f)
            moveTo(21f, 12.43f)
            arcTo(1.93f, 1.93f, 0f, false, false, 21f, 9.07f)
            lineTo(8.83f, 2.2f)
            arcTo(1.64f, 1.64f, 0f, false, false, 7.2f, 2.2f)
            lineTo(3f, 4.57f)
            arcTo(1.93f, 1.93f, 0f, false, false, 3f, 7.93f)
            lineTo(15.18f, 14.79f)
            arcTo(1.636f, 1.636f, 0f, false, false, 16.81f, 14.79f)
            close()
        }
    }.build().also { cachedPackageOpen = it }

/** `PackageOpen` vector, built on first access and reused afterwards. */
private var cachedPackageOpen: ImageVector? = null
