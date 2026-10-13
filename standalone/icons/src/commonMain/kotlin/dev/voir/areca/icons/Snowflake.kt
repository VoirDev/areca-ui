package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Snowflake icon.
 *
 * Converted from Lucide `snowflake` (ISC).
 */
public val ArecaIcons.Snowflake: ImageVector
    get() = cachedSnowflake ?: ImageVector.Builder(
        name = "Snowflake",
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
            moveTo(10f, 20f)
            lineTo(8.75f, 17.5f)
            lineTo(6f, 18f)
            moveTo(10f, 4f)
            lineTo(8.75f, 6.5f)
            lineTo(6f, 6f)
            moveTo(14f, 20f)
            lineTo(15.25f, 17.5f)
            lineTo(18f, 18f)
            moveTo(14f, 4f)
            lineTo(15.25f, 6.5f)
            lineTo(18f, 6f)
            moveTo(17f, 21f)
            lineTo(14f, 15f)
            horizontalLineTo(10f)
            moveTo(17f, 3f)
            lineTo(14f, 9f)
            lineTo(15.5f, 12f)
            moveTo(2f, 12f)
            horizontalLineTo(8.5f)
            lineTo(10f, 9f)
            moveTo(20f, 10f)
            lineTo(18.5f, 12f)
            lineTo(20f, 14f)
            moveTo(22f, 12f)
            horizontalLineTo(15.5f)
            lineTo(14f, 15f)
            moveTo(4f, 10f)
            lineTo(5.5f, 12f)
            lineTo(4f, 14f)
            moveTo(7f, 21f)
            lineTo(10f, 15f)
            lineTo(8.5f, 12f)
            moveTo(7f, 3f)
            lineTo(10f, 9f)
            horizontalLineTo(14f)
        }
    }.build().also { cachedSnowflake = it }

/** `Snowflake` vector, built on first access and reused afterwards. */
private var cachedSnowflake: ImageVector? = null
