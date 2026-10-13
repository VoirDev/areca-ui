package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Dumbbell icon.
 *
 * Converted from Lucide `dumbbell` (ISC).
 */
public val ArecaIcons.Dumbbell: ImageVector
    get() = cachedDumbbell ?: ImageVector.Builder(
        name = "Dumbbell",
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
            moveTo(17.596f, 12.768f)
            arcTo(2f, 2f, 0f, true, false, 20.425f, 9.939f)
            lineTo(18.657f, 8.172f)
            arcTo(2f, 2f, 0f, false, false, 21.485f, 5.343f)
            lineTo(18.657f, 2.515f)
            arcTo(2f, 2f, 0f, false, false, 15.828f, 5.343f)
            lineTo(14.061f, 3.575f)
            arcTo(2f, 2f, 0f, true, false, 11.232f, 6.404f)
            close()
            moveTo(2.5f, 21.5f)
            lineTo(3.9f, 20.1f)
            moveTo(20.1f, 3.9f)
            lineTo(21.5f, 2.5f)
            moveTo(5.343f, 21.485f)
            arcTo(2f, 2f, 0f, true, false, 8.172f, 18.657f)
            lineTo(9.939f, 20.425f)
            arcTo(2f, 2f, 0f, true, false, 12.768f, 17.596f)
            lineTo(6.404f, 11.232f)
            arcTo(2f, 2f, 0f, true, false, 3.575f, 14.061f)
            lineTo(5.343f, 15.828f)
            arcTo(2f, 2f, 0f, false, false, 2.515f, 18.657f)
            close()
            moveTo(9.6f, 14.4f)
            lineTo(14.4f, 9.6f)
        }
    }.build().also { cachedDumbbell = it }

/** `Dumbbell` vector, built on first access and reused afterwards. */
private var cachedDumbbell: ImageVector? = null
