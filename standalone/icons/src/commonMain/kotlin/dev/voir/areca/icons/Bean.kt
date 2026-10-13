package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Bean icon.
 *
 * Converted from Lucide `bean` (ISC).
 */
public val ArecaIcons.Bean: ImageVector
    get() = cachedBean ?: ImageVector.Builder(
        name = "Bean",
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
            moveTo(10.165f, 6.598f)
            curveTo(9.954f, 7.478f, 9.64f, 8.36f, 9f, 9f)
            curveTo(8.36f, 9.64f, 7.479f, 9.954f, 6.598f, 10.165f)
            arcTo(6f, 6f, 0f, false, false, 8f, 22f)
            curveTo(15.732f, 22f, 22f, 15.732f, 22f, 8f)
            arcTo(6f, 6f, 0f, false, false, 10.165f, 6.598f)
            close()
            moveTo(5.341f, 10.62f)
            arcTo(4f, 4f, 0f, true, false, 10.62f, 5.34f)
        }
    }.build().also { cachedBean = it }

/** `Bean` vector, built on first access and reused afterwards. */
private var cachedBean: ImageVector? = null
