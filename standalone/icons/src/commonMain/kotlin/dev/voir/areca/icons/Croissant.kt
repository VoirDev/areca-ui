package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Croissant icon.
 *
 * Converted from Lucide `croissant` (ISC).
 */
public val ArecaIcons.Croissant: ImageVector
    get() = cachedCroissant ?: ImageVector.Builder(
        name = "Croissant",
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
            moveTo(10.2f, 18f)
            horizontalLineTo(4.774f)
            arcTo(1.5f, 1.5f, 0f, false, true, 3.422f, 17.03f)
            arcTo(11f, 11f, 0f, false, true, 3.554f, 10.543f)
            moveTo(18f, 10.2f)
            verticalLineTo(4.774f)
            arcTo(1.5f, 1.5f, 0f, false, false, 17.03f, 3.422f)
            arcTo(11f, 11f, 0f, false, false, 10.544f, 3.554f)
            moveTo(18f, 5f)
            arcTo(4f, 3f, 0f, false, true, 22f, 8f)
            arcTo(2f, 2f, 0f, false, true, 20f, 10f)
            arcTo(10f, 10f, 0f, false, false, 14.861f, 11.42f)
            moveTo(5f, 18f)
            arcTo(3f, 4f, 0f, false, false, 8f, 22f)
            arcTo(2f, 2f, 0f, false, false, 10f, 20f)
            arcTo(10f, 10f, 0f, false, true, 11.42f, 14.86f)
            moveTo(8.709f, 2.554f)
            arcTo(10f, 10f, 0f, false, false, 2.554f, 8.709f)
            arcTo(1.5f, 1.5f, 0f, false, false, 3.23f, 10.335f)
            lineTo(13.037f, 15.755f)
            arcTo(2f, 2f, 0f, false, false, 15.755f, 13.037f)
            lineTo(10.335f, 3.23f)
            arcTo(1.5f, 1.5f, 0f, false, false, 8.709f, 2.554f)
        }
    }.build().also { cachedCroissant = it }

/** `Croissant` vector, built on first access and reused afterwards. */
private var cachedCroissant: ImageVector? = null
