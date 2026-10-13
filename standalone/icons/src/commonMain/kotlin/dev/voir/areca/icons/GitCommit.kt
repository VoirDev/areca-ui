package dev.voir.areca.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Git commit horizontal icon.
 *
 * Converted from Lucide `git-commit-horizontal` (ISC).
 */
public val ArecaIcons.GitCommit: ImageVector
    get() = cachedGitCommit ?: ImageVector.Builder(
        name = "GitCommit",
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
            moveTo(9f, 12f)
            arcTo(3f, 3f, 0f, true, true, 15f, 12f)
            arcTo(3f, 3f, 0f, true, true, 9f, 12f)
            close()
            moveTo(3f, 12f)
            lineTo(9f, 12f)
            moveTo(15f, 12f)
            lineTo(21f, 12f)
        }
    }.build().also { cachedGitCommit = it }

/** `GitCommit` vector, built on first access and reused afterwards. */
private var cachedGitCommit: ImageVector? = null
