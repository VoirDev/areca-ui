package dev.voir.areca.foundation.topbar

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.foundation.LocalTextStyle
import dev.voir.areca.foundation.icon.LocalIconSize
import dev.voir.areca.foundation.slot.SlotRow
import dev.voir.areca.foundation.slot.SlotRowLayout
import dev.voir.areca.modifier.thenIf
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.tween

/**
 * A screen's heading bar: a title between the controls on either side of it, over an optional band.
 *
 * Built on [SlotRow], so it shares its arrangement rules with every other three-slot component.
 * [titleAlignment] chooses between the two arrangements that make sense for a bar, and neither of
 * them ever shrinks a control to make room for the title: a title that will not fit truncates.
 *
 * **The controls agree with each other by construction.** [leading] and [trailing] are composed
 * inside a [TopBarActionScope], which has already decided the space between two controls, their
 * vertical alignment, their content color, their glyph size and their distance from the screen
 * gutter. Use the controls the scope brings with it — [BackAction], [IconAction], [TextAction] and
 * the rest: a back chevron, a notification bell and a "Done" label written that way cannot come
 * out different sizes or different distances from the edge,
 * which is the failure this bar is arranged to make impossible. Anything else placed in a slot is
 * pulled outward like a control is; see [TopBarActionScope].
 *
 * **The band below the title** is part of the bar rather than something stacked under it, so a tab
 * strip, a search field or a segmented picker in [bottom] sits inside the bar's fill, inside its
 * window insets, and above its rule instead of below them.
 *
 * **A bar handed a [scrollState]** shows its own separator: the rule fades in once content is under
 * the bar and out again when it is not, so no screen has to decide by hand whether its bar is
 * bordered today. Under [TopBarScrollBehavior.EnterAlways] the bar also travels with the gesture.
 *
 * The title row is as tall as [TopBarSize.minHeight] whatever it holds, so a title-only screen and
 * a screen with buttons do not differ in height.
 *
 * The title slot is announced as a heading, which is how a screen reader user jumps to the top of a
 * screen. That is a claim about the content: put the screen's name there, and give a bar whose
 * middle is a control instead — a segmented picker, a search field — its own semantics.
 *
 * @param modifier Modifier applied to the bar.
 * @param style Background, content, control and rule colors. Defaults to the theme's.
 * @param size Padding, gaps, glyph size, height and text styles. Defaults to the theme's.
 * @param titleAlignment Where the title sits between the controls.
 * @param layout How the three slots share the bar's width. Defaults to what [titleAlignment] asks
 *   for; pass one only to override that.
 * @param windowInsets Platform insets the bar pads itself by, so its fill runs under the status bar
 *   while its content clears it. None by default.
 * @param scrollState State of the content scrolling beneath the bar, from
 *   [rememberTopBarScrollState]. `null` leaves the bar unaware of scrolling, and its rule fixed at
 *   [TopBarStyle.border].
 * @param leading Controls before the title, normally a back control.
 * @param trailing Controls after the title.
 * @param bottom Band under the title row, inside the bar: a tab strip, a search field, a progress
 *   rule.
 * @param content The title.
 */
@Composable
public fun TopBar(
    modifier: Modifier = Modifier,
    style: TopBarStyle = LocalTopBarStyle.current ?: TopBarStyle.Default,
    size: TopBarSize = LocalTopBarSize.current ?: TopBarSize.Default,
    titleAlignment: TopBarTitleAlignment = TopBarTitleAlignment.Start,
    layout: SlotRowLayout = titleAlignment.slotRowLayout(size.gap),
    windowInsets: WindowInsets = WindowInsets(0, 0, 0, 0),
    scrollState: TopBarScrollState? = null,
    leading: (@Composable TopBarActionScope.() -> Unit)? = null,
    trailing: (@Composable TopBarActionScope.() -> Unit)? = null,
    bottom: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    TopBarSurface(
        modifier = modifier,
        style = style,
        size = size,
        windowInsets = windowInsets,
        scrollState = scrollState,
        bottom = bottom,
    ) {
        TopBarTitleRow(
            style = style,
            size = size,
            layout = layout,
            leading = leading,
            trailing = trailing,
            content = content,
        )
    }
}

/**
 * A screen's heading bar over a plain title, and optionally a line of detail under it.
 *
 * The common case of the slot overload above. Both lines are held to one line and truncate, which
 * is what stops a long name from turning the bar into a paragraph; reach for the slot overload and
 * [TopBarTitle] for a title that is a breadcrumb, a status, or a control.
 *
 * @param title The screen's name.
 * @param modifier Modifier applied to the bar.
 * @param subtitle Line of detail under [title] — a count, a status, a breadcrumb.
 * @param style Background, content, control and rule colors. Defaults to the theme's.
 * @param size Padding, gaps, glyph size, height and text styles. Defaults to the theme's.
 * @param titleAlignment Where the title block sits between the controls.
 * @param layout How the three slots share the bar's width. Defaults to what [titleAlignment] asks
 *   for; pass one only to override that.
 * @param windowInsets Platform insets the bar pads itself by. None by default.
 * @param scrollState State of the content scrolling beneath the bar. `null` leaves the bar unaware
 *   of scrolling.
 * @param leading Controls before the title, normally a back control.
 * @param trailing Controls after the title.
 * @param bottom Band under the title row, inside the bar.
 */
@Composable
public fun TopBar(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    style: TopBarStyle = LocalTopBarStyle.current ?: TopBarStyle.Default,
    size: TopBarSize = LocalTopBarSize.current ?: TopBarSize.Default,
    titleAlignment: TopBarTitleAlignment = TopBarTitleAlignment.Start,
    layout: SlotRowLayout = titleAlignment.slotRowLayout(size.gap),
    windowInsets: WindowInsets = WindowInsets(0, 0, 0, 0),
    scrollState: TopBarScrollState? = null,
    leading: (@Composable TopBarActionScope.() -> Unit)? = null,
    trailing: (@Composable TopBarActionScope.() -> Unit)? = null,
    bottom: (@Composable () -> Unit)? = null,
) {
    TopBar(
        modifier = modifier,
        style = style,
        size = size,
        titleAlignment = titleAlignment,
        layout = layout,
        windowInsets = windowInsets,
        scrollState = scrollState,
        leading = leading,
        trailing = trailing,
        bottom = bottom,
    ) {
        TopBarTitle.Default(title = title, subtitle = subtitle, alignment = titleAlignment)
    }
}

/**
 * The fill, insets, rule and travel every bar shares, around whatever its title row turns out to be.
 *
 * Shared by [TopBar] and [LargeTopBar] so the two cannot drift apart in the things a caller is
 * least likely to notice and most likely to be annoyed by — which side of the rule the window
 * insets fall on, whether the fill runs under the status bar, how fast the separator fades.
 *
 * @param modifier Modifier applied to the bar.
 * @param style Colors of the bar.
 * @param size Metrics of the bar.
 * @param windowInsets Platform insets the bar pads itself by.
 * @param scrollState State of the content scrolling beneath the bar, or `null`.
 * @param bottom Band under the title row, or `null`.
 * @param content The title row.
 */
@Composable
internal fun TopBarSurface(
    modifier: Modifier,
    style: TopBarStyle,
    size: TopBarSize,
    windowInsets: WindowInsets,
    scrollState: TopBarScrollState?,
    bottom: (@Composable () -> Unit)?,
    content: @Composable () -> Unit,
) {
    val rule by animateColorAsState(
        targetValue = if (scrollState?.isScrolled == true) style.scrolledBorder else style.border,
        animationSpec = ArecaTheme.motion.fast.tween(),
        label = "topBarRule",
    )

    CompositionLocalProvider(
        // Re-provided from what this bar actually resolved to, not from the theme, so anything
        // composed inside it — a title block, a control, a band — reads this bar's metrics even
        // when the bar was given a size or style of its own.
        LocalTopBarSize provides size,
        LocalTopBarStyle provides style,
        LocalTextStyle provides size.textStyle,
        LocalContentColor provides style.content,
        LocalIconSize provides size.icon,
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .thenIf(scrollState?.behavior == TopBarScrollBehavior.EnterAlways) {
                    // Only this behavior moves the whole bar, and only it measures its travel from
                    // the whole bar: a collapsing bar's travel is its headline's height, which the
                    // headline reports for itself. A bar whose height has not been measured yet has
                    // nowhere to travel and simply does not move.
                    onSizeChanged { scrollState?.updateCollapseRange(it.height.toFloat()) }
                        .graphicsLayer { translationY = scrollState?.offsetPx ?: 0f }
                }
                .background(style.bg)
                .thenIf(rule != Color.Transparent && size.borderWidth > 0.dp) {
                    bottomRule(color = rule, width = size.borderWidth)
                }
                // Insets before the minimum height, so the bar is the status bar plus a full bar
                // rather than a bar with its content pushed out of it.
                .windowInsetsPadding(windowInsets),
        ) {
            content()

            bottom?.let { band ->
                Box(modifier = Modifier.fillMaxWidth().padding(size.bottomPadding)) { band() }
            }
        }
    }
}

/**
 * The title row of a [TopBar]: controls, title, controls.
 *
 * @param style Colors of the bar.
 * @param size Metrics of the bar.
 * @param layout How the three slots share the row's width.
 * @param leading Controls before the title, or `null`.
 * @param trailing Controls after the title, or `null`.
 * @param content The title.
 */
@Composable
private fun TopBarTitleRow(
    style: TopBarStyle,
    size: TopBarSize,
    layout: SlotRowLayout,
    leading: (@Composable TopBarActionScope.() -> Unit)?,
    trailing: (@Composable TopBarActionScope.() -> Unit)?,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = size.minHeight)
            .padding(size.padding),
        contentAlignment = Alignment.Center,
    ) {
        SlotRow(
            modifier = Modifier.fillMaxWidth(),
            layout = layout,
            start = leading?.let { slot ->
                { TopBarActionRow(size = size, style = style, toStart = true, content = slot) }
            },
            end = trailing?.let { slot ->
                { TopBarActionRow(size = size, style = style, toStart = false, content = slot) }
            },
            center = {
                Box(modifier = Modifier.semantics(mergeDescendants = true) { heading() }) {
                    content()
                }
            },
        )
    }
}

/**
 * Draws a rule along the bottom edge of the node, inside its bounds.
 *
 * Drawn rather than composed, so the rule cannot take part in the bar's layout and change its
 * height by a hairline as it fades in and out under a scroll.
 *
 * @param color Color of the rule.
 * @param width Thickness of the rule.
 * @return Modifier that draws the rule behind the content.
 */
internal fun Modifier.bottomRule(color: Color, width: Dp): Modifier = drawBehind {
    val thickness = width.toPx()
    val y = size.height - thickness / 2f

    drawLine(
        color = color,
        start = Offset(0f, y),
        end = Offset(size.width, y),
        strokeWidth = thickness,
    )
}
