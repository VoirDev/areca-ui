package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Slash icon.
 *
 * Converted from Lucide `slash` (ISC).
 */
public val ArecaIcons.SlashDivider: ImageVector
    get() = cachedSlashDivider ?: ImageVector.Builder(
        name = "SlashDivider",
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
            moveTo(22f, 2f)
            lineTo(2f, 22f)
        }
    }.build().also { cachedSlashDivider = it }

/** `SlashDivider` vector, built on first access and reused afterwards. */
private var cachedSlashDivider: ImageVector? = null
