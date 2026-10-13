package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Code xml icon.
 *
 * Converted from Lucide `code-xml` (ISC).
 */
public val ArecaIcons.CodeAlt: ImageVector
    get() = cachedCodeAlt ?: ImageVector.Builder(
        name = "CodeAlt",
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
            moveTo(18f, 16f)
            lineTo(22f, 12f)
            lineTo(18f, 8f)
            moveTo(6f, 8f)
            lineTo(2f, 12f)
            lineTo(6f, 16f)
            moveTo(14.5f, 4f)
            lineTo(9.5f, 20f)
        }
    }.build().also { cachedCodeAlt = it }

/** `CodeAlt` vector, built on first access and reused afterwards. */
private var cachedCodeAlt: ImageVector? = null
