package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Feather icon.
 *
 * Converted from Lucide `feather` (ISC).
 */
public val ArecaIcons.Feather: ImageVector
    get() = cachedFeather ?: ImageVector.Builder(
        name = "Feather",
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
            moveTo(14.086f, 18.412f)
            arcTo(2f, 2f, 0f, false, true, 12.67f, 19f)
            horizontalLineTo(5f)
            verticalLineTo(11.328f)
            arcTo(2f, 2f, 0f, false, true, 5.586f, 9.914f)
            lineTo(11.75f, 3.75f)
            arcTo(6f, 6f, 0f, true, true, 20.24f, 12.24f)
            close()
            moveTo(16f, 8f)
            lineTo(2f, 22f)
            moveTo(17.488f, 15f)
            horizontalLineTo(9f)
        }
    }.build().also { cachedFeather = it }

/** `Feather` vector, built on first access and reused afterwards. */
private var cachedFeather: ImageVector? = null
