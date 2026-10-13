package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Settings 2 icon.
 *
 * Converted from Lucide `settings-2` (ISC).
 */
public val ArecaIcons.SettingsControl: ImageVector
    get() = cachedSettingsControl ?: ImageVector.Builder(
        name = "SettingsControl",
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
            moveTo(14f, 17f)
            horizontalLineTo(5f)
            moveTo(19f, 7f)
            horizontalLineTo(10f)
            moveTo(14f, 17f)
            arcTo(3f, 3f, 0f, true, true, 20f, 17f)
            arcTo(3f, 3f, 0f, true, true, 14f, 17f)
            close()
            moveTo(4f, 7f)
            arcTo(3f, 3f, 0f, true, true, 10f, 7f)
            arcTo(3f, 3f, 0f, true, true, 4f, 7f)
            close()
        }
    }.build().also { cachedSettingsControl = it }

/** `SettingsControl` vector, built on first access and reused afterwards. */
private var cachedSettingsControl: ImageVector? = null
