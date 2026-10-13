package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Home icon.
 *
 * Converted from Tabler Icons `home` (MIT).
 */
public val ArecaIcons.Home01: ImageVector
    get() = cachedHome01 ?: ImageVector.Builder(
        name = "Home01",
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
            lineTo(3f, 12f)
            lineTo(12f, 3f)
            lineTo(21f, 12f)
            lineTo(19f, 12f)
            moveTo(5f, 12f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, false, 7f, 21f)
            horizontalLineTo(17f)
            arcTo(2f, 2f, 0f, false, false, 19f, 19f)
            verticalLineTo(12f)
            moveTo(9f, 21f)
            verticalLineTo(15f)
            arcTo(2f, 2f, 0f, false, true, 11f, 13f)
            horizontalLineTo(13f)
            arcTo(2f, 2f, 0f, false, true, 15f, 15f)
            verticalLineTo(21f)
        }
    }.build().also { cachedHome01 = it }

/** `Home01` vector, built on first access and reused afterwards. */
private var cachedHome01: ImageVector? = null
