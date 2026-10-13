package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Message 2 heart icon.
 *
 * Converted from Tabler Icons `message-2-heart` (MIT).
 */
public val ArecaIcons.AnnotationHeart: ImageVector
    get() = cachedAnnotationHeart ?: ImageVector.Builder(
        name = "AnnotationHeart",
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
            horizontalLineTo(11.5f)
            moveTo(10.5f, 19.5f)
            lineTo(9f, 18f)
            horizontalLineTo(6f)
            arcTo(3f, 3f, 0f, false, true, 3f, 15f)
            verticalLineTo(7f)
            arcTo(3f, 3f, 0f, false, true, 6f, 4f)
            horizontalLineTo(18f)
            arcTo(3f, 3f, 0f, false, true, 21f, 7f)
            verticalLineTo(11f)
            moveTo(18f, 22f)
            lineTo(21.35f, 18.716f)
            arcTo(2.143f, 2.143f, 0f, false, false, 21.355f, 15.645f)
            arcTo(2.242f, 2.242f, 0f, false, false, 18.226f, 15.639f)
            lineTo(18.002f, 15.859f)
            lineTo(17.779f, 15.639f)
            arcTo(2.242f, 2.242f, 0f, false, false, 14.651f, 15.633f)
            arcTo(2.143f, 2.143f, 0f, false, false, 14.645f, 18.704f)
            lineTo(18f, 22f)
        }
    }.build().also { cachedAnnotationHeart = it }

/** `AnnotationHeart` vector, built on first access and reused afterwards. */
private var cachedAnnotationHeart: ImageVector? = null
