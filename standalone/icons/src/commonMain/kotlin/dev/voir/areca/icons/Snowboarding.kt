package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Snowboarding icon.
 *
 * Converted from Tabler Icons `snowboarding` (MIT).
 */
public val ArecaIcons.Snowboarding: ImageVector
    get() = cachedSnowboarding ?: ImageVector.Builder(
        name = "Snowboarding",
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
            moveTo(15f, 3f)
            arcTo(1f, 1f, 0f, true, false, 17f, 3f)
            arcTo(1f, 1f, 0f, false, false, 15f, 3f)
            moveTo(7f, 19f)
            lineTo(11f, 16.5f)
            lineTo(10.5f, 15f)
            moveTo(16f, 21f)
            lineTo(15f, 15f)
            lineTo(10.5f, 12f)
            lineTo(14f, 6f)
            moveTo(7f, 9f)
            lineTo(8.5f, 6f)
            horizontalLineTo(14f)
            lineTo(16f, 10f)
            lineTo(19f, 11f)
            moveTo(3f, 17f)
            curveTo(3.399f, 18.154f, 3.899f, 18.805f, 4.5f, 18.951f)
            curveTo(10.5f, 20.415f, 15.272f, 21.213f, 18f, 21.878f)
            curveTo(19.333f, 22.203f, 20.333f, 21.878f, 21f, 20.902f)
        }
    }.build().also { cachedSnowboarding = it }

/** `Snowboarding` vector, built on first access and reused afterwards. */
private var cachedSnowboarding: ImageVector? = null
