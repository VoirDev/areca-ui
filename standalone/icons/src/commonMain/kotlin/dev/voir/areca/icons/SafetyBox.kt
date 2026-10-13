package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Vault icon.
 *
 * Same drawing as `ArecaIcons.Vault`; both names are kept.
 *
 * Converted from Lucide `vault` (ISC).
 */
public val ArecaIcons.SafetyBox: ImageVector
    get() = cachedSafetyBox ?: ImageVector.Builder(
        name = "SafetyBox",
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
            moveTo(5f, 3f)
            horizontalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 21f, 5f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, false, true, 19f, 21f)
            horizontalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 3f, 19f)
            verticalLineTo(5f)
            arcTo(2f, 2f, 0f, false, true, 5f, 3f)
            close()
        }
        path(
            fill = SolidColor(Color.Black),
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
            moveTo(7f, 7.5f)
            arcTo(0.5f, 0.5f, 0f, true, true, 8f, 7.5f)
            arcTo(0.5f, 0.5f, 0f, true, true, 7f, 7.5f)
            close()
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
            moveTo(7.9f, 7.9f)
            lineTo(10.6f, 10.6f)
        }
        path(
            fill = SolidColor(Color.Black),
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
            moveTo(16f, 7.5f)
            arcTo(0.5f, 0.5f, 0f, true, true, 17f, 7.5f)
            arcTo(0.5f, 0.5f, 0f, true, true, 16f, 7.5f)
            close()
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
            moveTo(13.4f, 10.6f)
            lineTo(16.1f, 7.9f)
        }
        path(
            fill = SolidColor(Color.Black),
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
            moveTo(7f, 16.5f)
            arcTo(0.5f, 0.5f, 0f, true, true, 8f, 16.5f)
            arcTo(0.5f, 0.5f, 0f, true, true, 7f, 16.5f)
            close()
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
            moveTo(7.9f, 16.1f)
            lineTo(10.6f, 13.4f)
        }
        path(
            fill = SolidColor(Color.Black),
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
            moveTo(16f, 16.5f)
            arcTo(0.5f, 0.5f, 0f, true, true, 17f, 16.5f)
            arcTo(0.5f, 0.5f, 0f, true, true, 16f, 16.5f)
            close()
        }
        path(
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
            moveTo(13.4f, 13.4f)
            lineTo(16.1f, 16.1f)
            moveTo(10f, 12f)
            arcTo(2f, 2f, 0f, true, true, 14f, 12f)
            arcTo(2f, 2f, 0f, true, true, 10f, 12f)
            close()
        }
    }.build().also { cachedSafetyBox = it }

/** `SafetyBox` vector, built on first access and reused afterwards. */
private var cachedSafetyBox: ImageVector? = null
