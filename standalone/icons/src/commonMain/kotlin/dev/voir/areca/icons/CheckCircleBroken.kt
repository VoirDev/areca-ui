package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Circle check big icon.
 *
 * Converted from Lucide `circle-check-big` (ISC).
 */
public val ArecaIcons.CheckCircleBroken: ImageVector
    get() = cachedCheckCircleBroken ?: ImageVector.Builder(
        name = "CheckCircleBroken",
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
            moveTo(21.801f, 10f)
            arcTo(10f, 10f, 0f, true, true, 17f, 3.335f)
            moveTo(9f, 11f)
            lineTo(12f, 14f)
            lineTo(22f, 4f)
        }
    }.build().also { cachedCheckCircleBroken = it }

/** `CheckCircleBroken` vector, built on first access and reused afterwards. */
private var cachedCheckCircleBroken: ImageVector? = null
