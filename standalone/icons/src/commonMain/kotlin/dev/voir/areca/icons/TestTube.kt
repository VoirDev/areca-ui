package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Test tube icon.
 *
 * Converted from Lucide `test-tube` (ISC).
 */
public val ArecaIcons.TestTube: ImageVector
    get() = cachedTestTube ?: ImageVector.Builder(
        name = "TestTube",
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
            moveTo(14.5f, 2f)
            verticalLineTo(19.5f)
            curveTo(14.5f, 20.9f, 13.4f, 22f, 12f, 22f)
            curveTo(10.6f, 22f, 9.5f, 20.9f, 9.5f, 19.5f)
            verticalLineTo(2f)
            moveTo(8.5f, 2f)
            horizontalLineTo(15.5f)
            moveTo(14.5f, 16f)
            horizontalLineTo(9.5f)
        }
    }.build().also { cachedTestTube = it }

/** `TestTube` vector, built on first access and reused afterwards. */
private var cachedTestTube: ImageVector? = null
