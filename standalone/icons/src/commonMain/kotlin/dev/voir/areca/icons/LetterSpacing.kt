package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Letter spacing icon.
 *
 * Converted from Tabler Icons `letter-spacing` (MIT).
 */
public val ArecaIcons.LetterSpacing: ImageVector
    get() = cachedLetterSpacing ?: ImageVector.Builder(
        name = "LetterSpacing",
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
            moveTo(5f, 12f)
            verticalLineTo(6.5f)
            arcTo(2.5f, 2.5f, 0f, false, true, 10f, 6.5f)
            verticalLineTo(12f)
            moveTo(10f, 8f)
            horizontalLineTo(5f)
            moveTo(13f, 4f)
            lineTo(16f, 12f)
            lineTo(19f, 4f)
            moveTo(5f, 18f)
            horizontalLineTo(19f)
            moveTo(17f, 20f)
            lineTo(19f, 18f)
            lineTo(17f, 16f)
            moveTo(7f, 16f)
            lineTo(5f, 18f)
            lineTo(7f, 20f)
        }
    }.build().also { cachedLetterSpacing = it }

/** `LetterSpacing` vector, built on first access and reused afterwards. */
private var cachedLetterSpacing: ImageVector? = null
