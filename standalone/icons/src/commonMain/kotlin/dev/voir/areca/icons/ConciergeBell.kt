package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Concierge bell icon.
 *
 * Converted from Lucide `concierge-bell` (ISC).
 */
public val ArecaIcons.ConciergeBell: ImageVector
    get() = cachedConciergeBell ?: ImageVector.Builder(
        name = "ConciergeBell",
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
            moveTo(3f, 20f)
            arcTo(1f, 1f, 0f, false, true, 2f, 19f)
            verticalLineTo(18f)
            arcTo(2f, 2f, 0f, false, true, 4f, 16f)
            horizontalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 22f, 18f)
            verticalLineTo(19f)
            arcTo(1f, 1f, 0f, false, true, 21f, 20f)
            close()
            moveTo(20f, 16f)
            arcTo(8f, 8f, 0f, true, false, 4f, 16f)
            moveTo(12f, 4f)
            verticalLineTo(8f)
            moveTo(10f, 4f)
            horizontalLineTo(14f)
        }
    }.build().also { cachedConciergeBell = it }

/** `ConciergeBell` vector, built on first access and reused afterwards. */
private var cachedConciergeBell: ImageVector? = null
