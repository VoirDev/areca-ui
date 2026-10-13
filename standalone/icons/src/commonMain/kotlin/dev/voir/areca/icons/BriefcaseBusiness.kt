package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Briefcase business icon.
 *
 * Converted from Lucide `briefcase-business` (ISC).
 */
public val ArecaIcons.BriefcaseBusiness: ImageVector
    get() = cachedBriefcaseBusiness ?: ImageVector.Builder(
        name = "BriefcaseBusiness",
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
            moveTo(12f, 12f)
            horizontalLineTo(12.01f)
            moveTo(16f, 6f)
            verticalLineTo(4f)
            arcTo(2f, 2f, 0f, false, false, 14f, 2f)
            horizontalLineTo(10f)
            arcTo(2f, 2f, 0f, false, false, 8f, 4f)
            verticalLineTo(6f)
            moveTo(22f, 13f)
            arcTo(18.15f, 18.15f, 0f, false, true, 2f, 13f)
            moveTo(4f, 6f)
            horizontalLineTo(20f)
            arcTo(2f, 2f, 0f, false, true, 22f, 8f)
            verticalLineTo(18f)
            arcTo(2f, 2f, 0f, false, true, 20f, 20f)
            horizontalLineTo(4f)
            arcTo(2f, 2f, 0f, false, true, 2f, 18f)
            verticalLineTo(8f)
            arcTo(2f, 2f, 0f, false, true, 4f, 6f)
            close()
        }
    }.build().also { cachedBriefcaseBusiness = it }

/** `BriefcaseBusiness` vector, built on first access and reused afterwards. */
private var cachedBriefcaseBusiness: ImageVector? = null
