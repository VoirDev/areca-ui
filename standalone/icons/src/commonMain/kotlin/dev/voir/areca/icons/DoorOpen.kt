package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Door open icon.
 *
 * Converted from Lucide `door-open` (ISC).
 */
public val ArecaIcons.DoorOpen: ImageVector
    get() = cachedDoorOpen ?: ImageVector.Builder(
        name = "DoorOpen",
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
            moveTo(10f, 21f)
            horizontalLineTo(2f)
            moveTo(10f, 3f)
            horizontalLineTo(7f)
            arcTo(2f, 2f, 0f, false, false, 5f, 5f)
            verticalLineTo(21f)
            moveTo(14f, 12f)
            horizontalLineTo(14.01f)
            moveTo(19f, 21f)
            verticalLineTo(5f)
            arcTo(2f, 2f, 0f, false, false, 17.325f, 3.026f)
            lineTo(11.162f, 2.013f)
            arcTo(1f, 1f, 0f, false, false, 10f, 3f)
            verticalLineTo(21f)
            arcTo(1f, 1f, 0f, false, false, 11.124f, 21.992f)
            close()
            moveTo(22f, 21f)
            horizontalLineTo(19f)
        }
    }.build().also { cachedDoorOpen = it }

/** `DoorOpen` vector, built on first access and reused afterwards. */
private var cachedDoorOpen: ImageVector? = null
