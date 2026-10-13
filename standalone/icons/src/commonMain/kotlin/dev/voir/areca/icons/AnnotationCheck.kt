package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Message 2 check icon.
 *
 * Converted from Tabler Icons `message-2-check` (MIT).
 */
public val ArecaIcons.AnnotationCheck: ImageVector
    get() = cachedAnnotationCheck ?: ImageVector.Builder(
        name = "AnnotationCheck",
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
            moveTo(8f, 9f)
            horizontalLineTo(16f)
            moveTo(8f, 13f)
            horizontalLineTo(14f)
            moveTo(12f, 21f)
            lineTo(11f, 20f)
            lineTo(9f, 18f)
            horizontalLineTo(6f)
            arcTo(3f, 3f, 0f, false, true, 3f, 15f)
            verticalLineTo(7f)
            arcTo(3f, 3f, 0f, false, true, 6f, 4f)
            horizontalLineTo(18f)
            arcTo(3f, 3f, 0f, false, true, 21f, 7f)
            verticalLineTo(13f)
            moveTo(15f, 19f)
            lineTo(17f, 21f)
            lineTo(21f, 17f)
        }
    }.build().also { cachedAnnotationCheck = it }

/** `AnnotationCheck` vector, built on first access and reused afterwards. */
private var cachedAnnotationCheck: ImageVector? = null
