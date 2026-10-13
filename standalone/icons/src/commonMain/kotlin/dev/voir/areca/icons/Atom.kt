package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Atom icon.
 *
 * Converted from Lucide `atom` (ISC).
 */
public val ArecaIcons.Atom: ImageVector
    get() = cachedAtom ?: ImageVector.Builder(
        name = "Atom",
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
            moveTo(11f, 12f)
            arcTo(1f, 1f, 0f, true, true, 13f, 12f)
            arcTo(1f, 1f, 0f, true, true, 11f, 12f)
            close()
            moveTo(20.2f, 20.2f)
            curveTo(22.24f, 18.17f, 20.22f, 12.84f, 15.7f, 8.3f)
            curveTo(11.16f, 3.78f, 5.83f, 1.76f, 3.8f, 3.8f)
            curveTo(1.76f, 5.83f, 3.78f, 11.16f, 8.3f, 15.7f)
            curveTo(12.84f, 20.22f, 18.17f, 22.24f, 20.2f, 20.2f)
            close()
            moveTo(15.7f, 15.7f)
            curveTo(20.22f, 11.16f, 22.24f, 5.83f, 20.2f, 3.8f)
            curveTo(18.17f, 1.76f, 12.84f, 3.78f, 8.3f, 8.3f)
            curveTo(3.78f, 12.84f, 1.76f, 18.17f, 3.8f, 20.2f)
            curveTo(5.83f, 22.24f, 11.16f, 20.22f, 15.7f, 15.7f)
            close()
        }
    }.build().also { cachedAtom = it }

/** `Atom` vector, built on first access and reused afterwards. */
private var cachedAtom: ImageVector? = null
