package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Test pipe icon.
 *
 * Converted from Tabler Icons `test-pipe` (MIT).
 */
public val ArecaIcons.TestPipe: ImageVector
    get() = cachedTestPipe ?: ImageVector.Builder(
        name = "TestPipe",
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
            moveTo(20f, 8.04f)
            lineTo(7.878f, 20.164f)
            arcTo(2.857f, 2.857f, 0f, true, true, 3.837f, 16.124f)
            lineTo(15.959f, 4f)
            moveTo(7f, 13f)
            horizontalLineTo(15f)
            moveTo(19f, 15f)
            lineTo(20.5f, 16.6f)
            arcTo(2f, 2f, 0f, true, true, 17.5f, 16.6f)
            lineTo(19f, 15f)
            moveTo(15f, 3f)
            lineTo(21f, 9f)
        }
    }.build().also { cachedTestPipe = it }

/** `TestPipe` vector, built on first access and reused afterwards. */
private var cachedTestPipe: ImageVector? = null
