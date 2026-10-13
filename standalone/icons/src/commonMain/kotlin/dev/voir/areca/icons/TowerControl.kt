package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Tower control icon.
 *
 * Converted from Lucide `tower-control` (ISC).
 */
public val ArecaIcons.TowerControl: ImageVector
    get() = cachedTowerControl ?: ImageVector.Builder(
        name = "TowerControl",
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
            moveTo(18.2f, 12.27f)
            lineTo(20f, 6f)
            horizontalLineTo(4f)
            lineTo(5.8f, 12.27f)
            arcTo(1f, 1f, 0f, false, false, 6.75f, 13f)
            horizontalLineTo(17.25f)
            arcTo(1f, 1f, 0f, false, false, 18.21f, 12.27f)
            close()
            moveTo(8f, 13f)
            verticalLineTo(22f)
            moveTo(16f, 22f)
            verticalLineTo(13f)
            moveTo(9f, 6f)
            lineTo(10f, 13f)
            moveTo(15f, 6f)
            lineTo(14f, 13f)
            moveTo(12f, 6f)
            verticalLineTo(2f)
            moveTo(13f, 2f)
            horizontalLineTo(11f)
        }
    }.build().also { cachedTowerControl = it }

/** `TowerControl` vector, built on first access and reused afterwards. */
private var cachedTowerControl: ImageVector? = null
