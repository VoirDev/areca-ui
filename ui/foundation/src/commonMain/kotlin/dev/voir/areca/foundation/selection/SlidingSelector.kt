package dev.voir.areca.foundation.selection

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified

/**
 * Whether a [SlidingSelector] may be left with nothing chosen.
 */
public enum class SelectorMode {
    /**
     * Exactly one option is chosen, like tabs and segments. Choosing the chosen option again does
     * nothing. Each option is announced as a radio button.
     */
    Required,

    /**
     * None or one option is chosen, like a three-state "has / avoid / either" toggle. Choosing the
     * chosen option again clears the selection. Each option is announced as a switch.
     */
    Optional,
}

/** How a [SlidingSelector] shares the length it is given among its options. */
public enum class SelectorLayout {
    /**
     * The options share the selector's length equally, and the selector fills the length it is
     * given. Every option must fit.
     */
    Fill,

    /** Each option is as long as its content, and the selector as long as its options. */
    Hug,

    /**
     * Each option is as long as its content, and the selector scrolls when the options overflow,
     * keeping the chosen one in view.
     */
    Scroll,
}

/** Which way a [SlidingSelector] runs. */
public enum class SelectorOrientation {
    /** A row of options. The thumb slides sideways, mirrored in a right-to-left layout. */
    Horizontal,

    /** A column of options. The thumb slides up and down. */
    Vertical,
}

/** What a [SlidingSelector] draws to mark the chosen option. */
public enum class SelectorThumbKind {
    /**
     * A shape behind the chosen option: a segment's raised capsule, a pill under the chosen words,
     * a button's fill.
     */
    Fill,

    /** A bar along one [edge][SlidingSelectorStyle.thumbEdge] of the chosen option, as tabs draw. */
    Underline,

    /** No mark; only the chosen option's content colour changes. */
    None,
}

/**
 * Which long edge of a [SlidingSelector] carries an underline thumb and the divider.
 */
public enum class SelectorEdge {
    /** The top of a horizontal selector, or its start edge in reading order when vertical. */
    Start,

    /** The bottom of a horizontal selector, or its end edge in reading order when vertical. */
    End,
}

/**
 * One option of a [SlidingSelector].
 *
 * @property value What this option stands for. Must be unique within its selector, and is what
 *   `onSelect` reports.
 * @property enabled Whether the option can be chosen. A disabled option is skipped by touch,
 *   keyboard and screen reader focus.
 * @property thumb Colour of the thumb while this option is chosen, or [Color.Unspecified] for the
 *   style's [thumb][SlidingSelectorStyle.thumb]. The thumb cross-fades between options' colours
 *   while it moves.
 * @property contentSelected Colour of this option's content while it is chosen, or
 *   [Color.Unspecified] for the style's
 *   [contentSelected][SlidingSelectorStyle.contentSelected].
 * @property contentDescription What a screen reader announces for the option, for content that does
 *   not name it alone: an icon, or a word that needs the thing it applies to ("Has iris"). `null`
 *   announces the content.
 * @property content The option's content, given its current state. Plain [dev.voir.areca.foundation.text.Text]
 *   and [dev.voir.areca.foundation.icon.Icon] take the selector's content colour, type and icon
 *   size.
 */
@Immutable
public data class SelectorItem<T>(
    public val value: T,
    public val enabled: Boolean = true,
    public val thumb: Color = Color.Unspecified,
    public val contentSelected: Color = Color.Unspecified,
    public val contentDescription: String? = null,
    public val content: @Composable SelectorItemScope.() -> Unit,
)

/**
 * State of a [SelectorItem] while its content is being composed.
 *
 * @property selected Whether this option is the chosen one.
 * @property enabled Whether this option can be chosen.
 */
@Immutable
public data class SelectorItemScope(
    public val selected: Boolean,
    public val enabled: Boolean,
)

/**
 * A row of options with one thumb that slides to the chosen option.
 *
 * Covers segmented controls, words with a pill moving under them, underlined choices and three-state
 * toggles; [dev.voir.areca.foundation.tabs.Tabs] and
 * [dev.voir.areca.foundation.tabbar.FloatingTabBar] are built on it. The look is the [style]:
 * [SlidingSelectorStyle.Segmented], [SlidingSelectorStyle.Pill],
 * [SlidingSelectorStyle.Underline] or [SlidingSelectorStyle.Button].
 *
 * The thumb takes each option's *measured* bounds, so options may differ in length. It moves on the
 * style's [motion][SlidingSelectorStyle.motion], its colour cross-fading from the old option's
 * [thumb][SelectorItem.thumb] colour to the new one's. In [SelectorMode.Optional] it grows in at
 * the first chosen option and shrinks away where it was when the selection is cleared, never
 * sliding in from a stale place. A selector opens with its thumb in place, and follows a layout
 * change (a rotation, a label changing length) without animating. With
 * [ArecaMotion.reduceMotion][dev.voir.areca.theme.ArecaMotion.reduceMotion] the thumb fades out
 * and back in at its new place instead of travelling.
 *
 * Announced as a group: in [SelectorMode.Required] each option is a radio button, in
 * [SelectorMode.Optional] a switch. Each enabled option is a focus stop; the arrow keys move focus
 * within the group and Enter or Space chooses.
 *
 * @param items Options to show, in order.
 * @param selected Value of the chosen option, or `null` for none (meaningful in
 *   [SelectorMode.Optional]).
 * @param onSelect Called once for each tap that changes the selection: with the tapped option's
 *   value, or with `null` when an [SelectorMode.Optional] selection is cleared. Never called for a
 *   disabled option, nor for the chosen option in [SelectorMode.Required].
 * @param modifier Modifier applied to the selector.
 * @param mode Whether the selection may be cleared.
 * @param style Colours, shapes, thumb and motion. Defaults to the theme's segmented look.
 * @param size Metrics of the options. Defaults to the theme's.
 * @param layout How the options share the selector's length.
 * @param orientation Which way the selector runs.
 */
@Composable
public fun <T> SlidingSelector(
    items: List<SelectorItem<T>>,
    selected: T?,
    onSelect: (T?) -> Unit,
    modifier: Modifier = Modifier,
    mode: SelectorMode = SelectorMode.Required,
    style: SlidingSelectorStyle = LocalSlidingSelectorStyle.current ?: SlidingSelectorStyle.Segmented,
    size: SlidingSelectorSize = LocalSlidingSelectorSize.current ?: SlidingSelectorSize.Default,
    layout: SelectorLayout = SelectorLayout.Fill,
    orientation: SelectorOrientation = SelectorOrientation.Horizontal,
) {
    val selectedIndex = if (selected == null) -1 else items.indexOfFirst { it.value == selected }

    SelectorCore(
        count = items.size,
        selectedIndex = selectedIndex,
        isEnabled = { items[it].enabled },
        onTap = { index ->
            when (val tap = resolveSelectorTap(items[index], selected, mode)) {
                is SelectorTap.Select -> onSelect(tap.value)
                SelectorTap.Ignore -> Unit
            }
        },
        role = if (mode == SelectorMode.Optional) SelectorRole.Switch else SelectorRole.RadioButton,
        style = style,
        size = size,
        layout = layout,
        orientation = orientation,
        modifier = modifier,
        thumbColor = { index -> items[index].thumb.takeIf { it.isSpecified } ?: style.thumb },
        contentSelectedColor = { index ->
            items[index].contentSelected.takeIf { it.isSpecified } ?: style.contentSelected
        },
        contentDescription = { items[it].contentDescription },
    ) { index, isSelected ->
        val item = items[index]
        item.content(SelectorItemScope(selected = isSelected, enabled = item.enabled))
    }
}

/** What a tap on one option of a [SlidingSelector] does to its selection. */
internal sealed interface SelectorTap<out T> {
    /**
     * The selection changes to [value].
     *
     * @property value Value now chosen, or `null` when the selection is cleared.
     */
    data class Select<T>(val value: T?) : SelectorTap<T>

    /** The selection stays as it is, and `onSelect` is not called. */
    data object Ignore : SelectorTap<Nothing>
}

/**
 * Resolves a tap on [item], given the current [selected] value and the [mode].
 *
 * @param item Option tapped.
 * @param selected Value chosen before the tap, or `null` for none.
 * @param mode Whether the selection may be cleared.
 * @return The change to report, or [SelectorTap.Ignore] for a disabled option or a chosen option in
 *   [SelectorMode.Required].
 */
internal fun <T> resolveSelectorTap(
    item: SelectorItem<T>,
    selected: T?,
    mode: SelectorMode,
): SelectorTap<T> = when {
    !item.enabled -> SelectorTap.Ignore
    item.value != selected -> SelectorTap.Select(item.value)
    mode == SelectorMode.Optional -> SelectorTap.Select(null)
    else -> SelectorTap.Ignore
}
