package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Flask conical icon.
 *
 * Converted from Lucide `flask-conical` (ISC).
 */
public val ArecaIcons.Beaker: ImageVector
    get() = cachedBeaker ?: ImageVector.Builder(
        name = "Beaker",
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
            moveTo(14f, 2f)
            verticalLineTo(8f)
            arcTo(2f, 2f, 0f, false, false, 14.245f, 8.96f)
            lineTo(19.755f, 19.04f)
            arcTo(2f, 2f, 0f, false, true, 18f, 22f)
            horizontalLineTo(6f)
            arcTo(2f, 2f, 0f, false, true, 4.245f, 19.04f)
            lineTo(9.755f, 8.96f)
            arcTo(2f, 2f, 0f, false, false, 10f, 8f)
            verticalLineTo(2f)
            moveTo(6.453f, 15f)
            horizontalLineTo(17.547f)
            moveTo(8.5f, 2f)
            horizontalLineTo(15.5f)
        }
    }.build().also { cachedBeaker = it }

/** `Beaker` vector, built on first access and reused afterwards. */
private var cachedBeaker: ImageVector? = null
