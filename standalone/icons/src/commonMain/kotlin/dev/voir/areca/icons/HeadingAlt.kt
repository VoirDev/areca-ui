package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Heading icon.
 *
 * Converted from Tabler Icons `heading` (MIT).
 */
public val ArecaIcons.HeadingAlt: ImageVector
    get() = cachedHeadingAlt ?: ImageVector.Builder(
        name = "HeadingAlt",
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
            moveTo(7f, 12f)
            horizontalLineTo(17f)
            moveTo(7f, 5f)
            verticalLineTo(19f)
            moveTo(17f, 5f)
            verticalLineTo(19f)
            moveTo(15f, 19f)
            horizontalLineTo(19f)
            moveTo(15f, 5f)
            horizontalLineTo(19f)
            moveTo(5f, 19f)
            horizontalLineTo(9f)
            moveTo(5f, 5f)
            horizontalLineTo(9f)
        }
    }.build().also { cachedHeadingAlt = it }

/** `HeadingAlt` vector, built on first access and reused afterwards. */
private var cachedHeadingAlt: ImageVector? = null
