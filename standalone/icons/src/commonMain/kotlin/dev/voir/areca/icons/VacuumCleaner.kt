package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Vacuum cleaner icon.
 *
 * Converted from Tabler Icons `vacuum-cleaner` (MIT).
 */
public val ArecaIcons.VacuumCleaner: ImageVector
    get() = cachedVacuumCleaner ?: ImageVector.Builder(
        name = "VacuumCleaner",
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
            moveTo(21f, 12f)
            arcTo(9f, 9f, 0f, true, true, 3f, 12f)
            arcTo(9f, 9f, 0f, false, true, 21f, 12f)
            moveTo(14f, 9f)
            arcTo(2f, 2f, 0f, true, true, 10f, 9f)
            arcTo(2f, 2f, 0f, false, true, 14f, 9f)
            moveTo(12f, 16f)
            horizontalLineTo(12.01f)
        }
    }.build().also { cachedVacuumCleaner = it }

/** `VacuumCleaner` vector, built on first access and reused afterwards. */
private var cachedVacuumCleaner: ImageVector? = null
