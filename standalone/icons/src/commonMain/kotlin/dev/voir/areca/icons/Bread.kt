package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Bread icon.
 *
 * Converted from Tabler Icons `bread` (MIT).
 */
public val ArecaIcons.Bread: ImageVector
    get() = cachedBread ?: ImageVector.Builder(
        name = "Bread",
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
            moveTo(18f, 4f)
            arcTo(3f, 3f, 0f, false, true, 20f, 9.235f)
            verticalLineTo(18f)
            arcTo(2f, 2f, 0f, false, true, 18f, 20f)
            horizontalLineTo(6f)
            arcTo(2f, 2f, 0f, false, true, 4f, 18f)
            verticalLineTo(9.236f)
            arcTo(3f, 3f, 0f, false, true, 5.824f, 4.005f)
            horizontalLineTo(18f)
            verticalLineTo(4f)
        }
    }.build().also { cachedBread = it }

/** `Bread` vector, built on first access and reused afterwards. */
private var cachedBread: ImageVector? = null
