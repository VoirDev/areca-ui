package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Tag icon.
 *
 * Converted from Tabler Icons `tag` (MIT).
 */
public val ArecaIcons.TagAlt: ImageVector
    get() = cachedTagAlt ?: ImageVector.Builder(
        name = "TagAlt",
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
            moveTo(6.5f, 7.5f)
            arcTo(1f, 1f, 0f, true, false, 8.5f, 7.5f)
            arcTo(1f, 1f, 0f, true, false, 6.5f, 7.5f)
            moveTo(3f, 6f)
            verticalLineTo(11.172f)
            arcTo(2f, 2f, 0f, false, false, 3.586f, 12.586f)
            lineTo(11.296f, 20.296f)
            arcTo(2.41f, 2.41f, 0f, false, false, 14.704f, 20.296f)
            lineTo(20.296f, 14.704f)
            arcTo(2.41f, 2.41f, 0f, false, false, 20.296f, 11.296f)
            lineTo(12.586f, 3.586f)
            arcTo(2f, 2f, 0f, false, false, 11.172f, 3f)
            horizontalLineTo(6f)
            arcTo(3f, 3f, 0f, false, false, 3f, 6f)
        }
    }.build().also { cachedTagAlt = it }

/** `TagAlt` vector, built on first access and reused afterwards. */
private var cachedTagAlt: ImageVector? = null
