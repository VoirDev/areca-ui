package dev.voir.areca.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.window.InputMode
import dev.voir.areca.window.PlatformInputMode

/**
 * How large controls are, for the way they are pressed.
 *
 * A finger needs a 48dp target and a mouse does not, so the same "medium" button is 48dp tall on a
 * phone and 36dp on a laptop. Every component size preset reads its height from here — a
 * `ButtonSize.Medium` is [controlMd] tall — which keeps a button, a field and an icon button beside
 * each other the same height whatever the input.
 *
 * [ArecaThemeProvider] picks [touch] or [pointer] from the input mode in scope; pass one explicitly to
 * force a density, such as a dense data screen in a touch app. `copy` retunes one step.
 *
 * @property controlXs Height of the smallest controls: inside a dense row or a table cell.
 * @property controlSm Height of small controls: beside a field, in a toolbar.
 * @property controlMd Height of the default control.
 * @property controlLg Height of a screen's primary action.
 * @property controlXl Height of a landing screen's single call to action.
 * @property rowCompact Smallest height of a dense list row.
 * @property rowMedium Smallest height of a list row with one line.
 * @property rowComfortable Smallest height of a list row with a supporting line.
 * @property touchTarget Smallest width and height an interactive control reserves in layout, or zero
 *   to reserve nothing beyond what it draws.
 * @property dense Whether controls are drawn tight, for a pointer. Presets read it through [select]
 *   to pick the paddings that fit the heights above.
 */
@Immutable
public data class ArecaSizing(
    public val controlXs: Dp = 32.dp,
    public val controlSm: Dp = 40.dp,
    public val controlMd: Dp = 48.dp,
    public val controlLg: Dp = 56.dp,
    public val controlXl: Dp = 64.dp,
    public val rowCompact: Dp = 32.dp,
    public val rowMedium: Dp = 44.dp,
    public val rowComfortable: Dp = 56.dp,
    public val touchTarget: Dp = 48.dp,
    public val dense: Boolean = false,
) {
    /**
     * Picks the value for this density: [pointer] when [dense], otherwise [touch].
     *
     * For a dimension no height above covers — a button's padding, an icon's size — so that a
     * component's touch and pointer geometry sit side by side where it is defined.
     *
     * @param touch Value for touch.
     * @param pointer Value for a pointer.
     * @return The value for this density.
     */
    public fun <T> select(touch: T, pointer: T): T = if (dense) pointer else touch

    /** The built-in densities. */
    public companion object {
        /**
         * Sizes for a finger: 32 to 64dp controls and a 48dp touch target. Areca's sizes before
         * pointer density existed, unchanged.
         *
         * @return The touch sizing.
         */
        public fun touch(): ArecaSizing = ArecaSizing()

        /**
         * Sizes for a mouse or trackpad: 28 to 52dp controls, a 36dp default, and no touch target.
         *
         * @return The pointer sizing.
         */
        public fun pointer(): ArecaSizing = ArecaSizing(
            controlXs = 28.dp,
            controlSm = 32.dp,
            controlMd = 36.dp,
            controlLg = 44.dp,
            controlXl = 52.dp,
            rowCompact = 28.dp,
            rowMedium = 36.dp,
            rowComfortable = 44.dp,
            touchTarget = 0.dp,
            dense = true,
        )

        /**
         * The sizing for [input]: [pointer] for a pointer, [touch] for a finger.
         *
         * @param input What presses the controls.
         * @return The sizing for [input].
         */
        public fun forInput(input: InputMode): ArecaSizing = when (input) {
            InputMode.Touch -> touch()
            InputMode.Pointer -> pointer()
        }
    }
}

/**
 * Control sizing in scope. Read it through [ArecaTheme.sizing]. Without a provider it is the
 * platform's usual one, so a desktop preview draws pointer sizes.
 */
internal val LocalSizing = staticCompositionLocalOf { ArecaSizing.forInput(PlatformInputMode) }
