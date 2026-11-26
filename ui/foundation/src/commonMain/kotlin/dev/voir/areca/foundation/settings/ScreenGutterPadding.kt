package dev.voir.areca.foundation.settings

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import dev.voir.areca.theme.horizontalGutter

/**
 * The default [SettingsSectionSize.outerPadding]: the screen gutter on either side, taken for the
 * width class where the section is drawn. Sizes are often built once — at the theme's root, in an
 * app's component defaults — where the width class is the window's; a section in a narrower pane
 * would then keep the window's gutter while the content beside it takes the pane's. [Section]
 * resolves this padding where it draws, so the two always agree.
 *
 * Read anywhere else, it is the compact gutter on either side and nothing above or below.
 */
@Immutable
internal object ScreenGutterPadding : PaddingValues {
    /** The compact gutter, for a reader that does not resolve it. */
    private val Compact: Dp = 16.dp

    override fun calculateLeftPadding(layoutDirection: LayoutDirection): Dp = Compact

    override fun calculateTopPadding(): Dp = 0.dp

    override fun calculateRightPadding(layoutDirection: LayoutDirection): Dp = Compact

    override fun calculateBottomPadding(): Dp = 0.dp

    override fun toString(): String = "ScreenGutterPadding"
}

/**
 * @return The receiver, with [ScreenGutterPadding] replaced by the screen gutter for the width class
 *   in scope.
 */
@Composable
internal fun PaddingValues.resolveScreenGutter(): PaddingValues =
    if (this === ScreenGutterPadding) PaddingValues(horizontal = horizontalGutter) else this
