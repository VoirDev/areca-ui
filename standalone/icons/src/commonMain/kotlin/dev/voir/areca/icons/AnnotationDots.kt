package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Message dots icon.
 *
 * Converted from Tabler Icons `message-dots` (MIT).
 */
public val ArecaIcons.AnnotationDots: ImageVector
    get() = cachedAnnotationDots ?: ImageVector.Builder(
        name = "AnnotationDots",
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
            moveTo(12f, 11f)
            verticalLineTo(11.01f)
            moveTo(8f, 11f)
            verticalLineTo(11.01f)
            moveTo(16f, 11f)
            verticalLineTo(11.01f)
            moveTo(18f, 4f)
            arcTo(3f, 3f, 0f, false, true, 21f, 7f)
            verticalLineTo(15f)
            arcTo(3f, 3f, 0f, false, true, 18f, 18f)
            horizontalLineTo(13f)
            lineTo(8f, 21f)
            verticalLineTo(18f)
            horizontalLineTo(6f)
            arcTo(3f, 3f, 0f, false, true, 3f, 15f)
            verticalLineTo(7f)
            arcTo(3f, 3f, 0f, false, true, 6f, 4f)
            lineTo(18f, 4f)
        }
    }.build().also { cachedAnnotationDots = it }

/** `AnnotationDots` vector, built on first access and reused afterwards. */
private var cachedAnnotationDots: ImageVector? = null
