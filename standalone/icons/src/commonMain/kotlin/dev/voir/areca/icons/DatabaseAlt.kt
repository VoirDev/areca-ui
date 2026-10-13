package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Database icon.
 *
 * Converted from Tabler Icons `database` (MIT).
 */
public val ArecaIcons.DatabaseAlt: ImageVector
    get() = cachedDatabaseAlt ?: ImageVector.Builder(
        name = "DatabaseAlt",
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
            moveTo(4f, 6f)
            arcTo(8f, 3f, 0f, true, false, 20f, 6f)
            arcTo(8f, 3f, 0f, true, false, 4f, 6f)
            moveTo(4f, 6f)
            verticalLineTo(12f)
            arcTo(8f, 3f, 0f, false, false, 20f, 12f)
            verticalLineTo(6f)
            moveTo(4f, 12f)
            verticalLineTo(18f)
            arcTo(8f, 3f, 0f, false, false, 20f, 18f)
            verticalLineTo(12f)
        }
    }.build().also { cachedDatabaseAlt = it }

/** `DatabaseAlt` vector, built on first access and reused afterwards. */
private var cachedDatabaseAlt: ImageVector? = null
