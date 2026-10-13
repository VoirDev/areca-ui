package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Underline icon.
 *
 * Converted from Tabler Icons `underline` (MIT).
 */
public val ArecaIcons.UnderlineAlt: ImageVector
    get() = cachedUnderlineAlt ?: ImageVector.Builder(
        name = "UnderlineAlt",
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
            moveTo(7f, 5f)
            verticalLineTo(10f)
            arcTo(5f, 5f, 0f, false, false, 17f, 10f)
            verticalLineTo(5f)
            moveTo(5f, 19f)
            horizontalLineTo(19f)
        }
    }.build().also { cachedUnderlineAlt = it }

/** `UnderlineAlt` vector, built on first access and reused afterwards. */
private var cachedUnderlineAlt: ImageVector? = null
