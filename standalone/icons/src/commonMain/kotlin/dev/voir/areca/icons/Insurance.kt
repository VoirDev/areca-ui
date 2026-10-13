package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Shield dollar icon.
 *
 * Same drawing as `ArecaIcons.ShieldDollar`; both names are kept.
 *
 * Converted from Tabler Icons `shield-dollar` (MIT).
 */
public val ArecaIcons.Insurance: ImageVector
    get() = cachedInsurance ?: ImageVector.Builder(
        name = "Insurance",
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
            moveTo(13.018f, 20.687f)
            curveTo(12.685f, 20.806f, 12.345f, 20.91f, 12f, 21f)
            arcTo(12f, 12f, 0f, false, true, 3.5f, 6f)
            arcTo(12f, 12f, 0f, false, false, 12f, 3f)
            arcTo(12f, 12f, 0f, false, false, 20.5f, 6f)
            curveTo(20.933f, 7.472f, 21.075f, 8.998f, 20.936f, 10.495f)
            moveTo(21f, 15f)
            horizontalLineTo(18.5f)
            arcTo(1.5f, 1.5f, 0f, false, false, 18.5f, 18f)
            horizontalLineTo(19.5f)
            arcTo(1.5f, 1.5f, 0f, false, true, 19.5f, 21f)
            horizontalLineTo(17f)
            moveTo(19f, 21f)
            verticalLineTo(22f)
            moveTo(19f, 14f)
            verticalLineTo(15f)
        }
    }.build().also { cachedInsurance = it }

/** `Insurance` vector, built on first access and reused afterwards. */
private var cachedInsurance: ImageVector? = null
