package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * At sign icon.
 *
 * Converted from Lucide `at-sign` (ISC).
 */
public val ArecaIcons.AtSign: ImageVector
    get() = cachedAtSign ?: ImageVector.Builder(
        name = "AtSign",
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
            moveTo(8f, 12f)
            arcTo(4f, 4f, 0f, true, true, 16f, 12f)
            arcTo(4f, 4f, 0f, true, true, 8f, 12f)
            close()
            moveTo(16f, 8f)
            verticalLineTo(13f)
            arcTo(3f, 3f, 0f, false, false, 22f, 13f)
            verticalLineTo(12f)
            arcTo(10f, 10f, 0f, true, false, 18f, 20f)
        }
    }.build().also { cachedAtSign = it }

/** `AtSign` vector, built on first access and reused afterwards. */
private var cachedAtSign: ImageVector? = null
