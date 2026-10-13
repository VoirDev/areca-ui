package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Face slightly frowning icon.
 *
 * Converted from Lucide `face-slightly-frowning` (ISC).
 */
public val ArecaIcons.FaceFrown: ImageVector
    get() = cachedFaceFrown ?: ImageVector.Builder(
        name = "FaceFrown",
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
            moveTo(15f, 10f)
            verticalLineTo(9f)
            moveTo(9f, 10f)
            verticalLineTo(9f)
            moveTo(9f, 16f)
            arcTo(5f, 5f, 0f, false, true, 15f, 16f)
            moveTo(2f, 12f)
            arcTo(10f, 10f, 0f, true, true, 22f, 12f)
            arcTo(10f, 10f, 0f, true, true, 2f, 12f)
            close()
        }
    }.build().also { cachedFaceFrown = it }

/** `FaceFrown` vector, built on first access and reused afterwards. */
private var cachedFaceFrown: ImageVector? = null
