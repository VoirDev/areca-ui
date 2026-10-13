package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Cash register icon.
 *
 * Converted from Tabler Icons `cash-register` (MIT).
 */
public val ArecaIcons.CashRegister: ImageVector
    get() = cachedCashRegister ?: ImageVector.Builder(
        name = "CashRegister",
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
            moveTo(21f, 15f)
            horizontalLineTo(18.5f)
            curveTo(18.102f, 15f, 17.721f, 15.158f, 17.439f, 15.439f)
            curveTo(17.158f, 15.72f, 17f, 16.102f, 17f, 16.5f)
            curveTo(17f, 16.898f, 17.158f, 17.279f, 17.439f, 17.561f)
            curveTo(17.72f, 17.842f, 18.102f, 18f, 18.5f, 18f)
            horizontalLineTo(19.5f)
            curveTo(19.898f, 18f, 20.279f, 18.158f, 20.561f, 18.439f)
            curveTo(20.842f, 18.72f, 21f, 19.102f, 21f, 19.5f)
            curveTo(21f, 19.898f, 20.842f, 20.279f, 20.561f, 20.561f)
            curveTo(20.28f, 20.842f, 19.898f, 21f, 19.5f, 21f)
            horizontalLineTo(17f)
            moveTo(19f, 21f)
            verticalLineTo(22f)
            moveTo(19f, 14f)
            verticalLineTo(15f)
            moveTo(13f, 21f)
            horizontalLineTo(6f)
            curveTo(5.47f, 21f, 4.961f, 20.789f, 4.586f, 20.414f)
            curveTo(4.211f, 20.039f, 4f, 19.53f, 4f, 19f)
            verticalLineTo(9f)
            curveTo(4f, 8.47f, 4.211f, 7.961f, 4.586f, 7.586f)
            curveTo(4.961f, 7.211f, 5.47f, 7f, 6f, 7f)
            horizontalLineTo(8f)
            moveTo(20f, 10.12f)
            verticalLineTo(9f)
            curveTo(20f, 8.47f, 19.789f, 7.961f, 19.414f, 7.586f)
            curveTo(19.039f, 7.211f, 18.53f, 7f, 18f, 7f)
            horizontalLineTo(16f)
            moveTo(16f, 10f)
            verticalLineTo(4f)
            curveTo(16f, 3.47f, 15.789f, 2.961f, 15.414f, 2.586f)
            curveTo(15.039f, 2.211f, 14.53f, 2f, 14f, 2f)
            horizontalLineTo(10f)
            curveTo(9.47f, 2f, 8.961f, 2.211f, 8.586f, 2.586f)
            curveTo(8.211f, 2.961f, 8f, 3.47f, 8f, 4f)
            verticalLineTo(10f)
            moveTo(16f, 10f)
            horizontalLineTo(8f)
            moveTo(16f, 10f)
            horizontalLineTo(17f)
            moveTo(8f, 10f)
            horizontalLineTo(7f)
            moveTo(8f, 14f)
            verticalLineTo(14.01f)
            moveTo(8f, 17f)
            verticalLineTo(17.01f)
            moveTo(12f, 13.99f)
            verticalLineTo(14f)
            moveTo(12f, 17f)
            verticalLineTo(17.01f)
        }
    }.build().also { cachedCashRegister = it }

/** `CashRegister` vector, built on first access and reused afterwards. */
private var cachedCashRegister: ImageVector? = null
