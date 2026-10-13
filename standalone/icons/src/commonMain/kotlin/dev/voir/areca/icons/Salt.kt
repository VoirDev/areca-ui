package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Salt icon.
 *
 * Converted from Tabler Icons `salt` (MIT).
 */
public val ArecaIcons.Salt: ImageVector
    get() = cachedSalt ?: ImageVector.Builder(
        name = "Salt",
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
            moveTo(12f, 13f)
            verticalLineTo(13.01f)
            moveTo(10f, 16f)
            verticalLineTo(16.01f)
            moveTo(14f, 16f)
            verticalLineTo(16.01f)
            moveTo(7.5f, 8f)
            horizontalLineTo(16.5f)
            lineTo(16.219f, 5.752f)
            arcTo(2f, 2f, 0f, false, false, 14.234f, 4f)
            horizontalLineTo(9.766f)
            arcTo(2f, 2f, 0f, false, false, 7.78f, 5.752f)
            lineTo(7.5f, 8f)
            moveTo(7.5f, 8f)
            lineTo(5.888f, 17.671f)
            arcTo(2f, 2f, 0f, false, false, 7.861f, 20f)
            horizontalLineTo(16.139f)
            arcTo(2f, 2f, 0f, false, false, 18.112f, 17.671f)
            lineTo(16.5f, 8f)
        }
    }.build().also { cachedSalt = it }

/** `Salt` vector, built on first access and reused afterwards. */
private var cachedSalt: ImageVector? = null
