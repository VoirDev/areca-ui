package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Paw print icon.
 *
 * Converted from Lucide `paw-print` (ISC).
 */
public val ArecaIcons.PawPrint: ImageVector
    get() = cachedPawPrint ?: ImageVector.Builder(
        name = "PawPrint",
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
            moveTo(9f, 4f)
            arcTo(2f, 2f, 0f, true, true, 13f, 4f)
            arcTo(2f, 2f, 0f, true, true, 9f, 4f)
            close()
            moveTo(16f, 8f)
            arcTo(2f, 2f, 0f, true, true, 20f, 8f)
            arcTo(2f, 2f, 0f, true, true, 16f, 8f)
            close()
            moveTo(18f, 16f)
            arcTo(2f, 2f, 0f, true, true, 22f, 16f)
            arcTo(2f, 2f, 0f, true, true, 18f, 16f)
            close()
            moveTo(9f, 10f)
            arcTo(5f, 5f, 0f, false, true, 14f, 15f)
            verticalLineTo(18.5f)
            arcTo(3.5f, 3.5f, 0f, false, true, 7.16f, 19.545f)
            quadTo(6.52f, 17.48f, 4.46f, 16.84f)
            arcTo(3.5f, 3.5f, 0f, false, true, 5.5f, 10f)
            close()
        }
    }.build().also { cachedPawPrint = it }

/** `PawPrint` vector, built on first access and reused afterwards. */
private var cachedPawPrint: ImageVector? = null
