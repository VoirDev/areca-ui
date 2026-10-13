package dev.voir.areca.foundation.topbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.badge.Badge
import dev.voir.areca.foundation.badge.BadgeSize
import dev.voir.areca.foundation.badge.BadgeStyle
import dev.voir.areca.foundation.badge.IndicatorDot
import dev.voir.areca.foundation.badge.IndicatorDotSize
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.icon.Chevron
import dev.voir.areca.foundation.icon.ChevronDirection
import dev.voir.areca.foundation.icon.ChevronSize
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.icon.IconSize
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.icon.BundledIcons
import dev.voir.areca.icon.ChevronLeft
import dev.voir.areca.theme.ArecaTheme

/**
 * Ready-made blocks for a [TopBar]'s title slot.
 *
 * A screen's heading is rarely only its name: it is a name and how many of something there are, or
 * a name and where it sits, or a name that opens a picker. Written by hand each time, those come
 * out with a different gap, a different text style and a different truncation rule on every screen.
 * These do not: each reads the metrics of the bar it is composed in, holds itself to the height of
 * a plain title, and truncates rather than wrapping.
 *
 * Every block here takes the bar's own [TopBarSize] and [TopBarStyle] by default, because a bar
 * re-provides what it resolved to — so a block inside a [TopBarSize.Compact] bar is compact without
 * being told.
 */
public object TopBarTitle {
    /**
     * A screen's name, and optionally a line of detail under it.
     *
     * What the `title`/`subtitle` overload of [TopBar] composes.
     *
     * @param title The screen's name.
     * @param modifier Modifier applied to the block.
     * @param subtitle Line of detail under the title. `null` shows the title alone.
     * @param alignment Where the block's lines are aligned. Match the bar's own, or the second line
     *   will sit off the first.
     * @param style Colors of the bar. Defaults to the bar this is composed in.
     * @param size Metrics of the bar. Defaults to the bar this is composed in.
     */
    @Composable
    public fun Default(
        title: String,
        modifier: Modifier = Modifier,
        subtitle: String? = null,
        alignment: TopBarTitleAlignment = TopBarTitleAlignment.Start,
        style: TopBarStyle = LocalTopBarStyle.current ?: TopBarStyle.Default,
        size: TopBarSize = LocalTopBarSize.current ?: TopBarSize.Default,
    ) {
        TitleColumn(modifier = modifier, alignment = alignment, size = size) {
            TitleText(text = title, size = size)

            subtitle?.let { SubtitleText(text = it, size = size, style = style) }
        }
    }

    /**
     * A screen's name under the name of where it came from.
     *
     * The parent goes above rather than beside the title. A `parent › child` pair written across a
     * bar leaves both halves fighting for a width neither has, and truncates the half the user
     * actually needs.
     *
     * @param parent Where the screen sits — a folder, a project, a conversation.
     * @param title The screen's name.
     * @param modifier Modifier applied to the block.
     * @param onParentClick Called when the parent is pressed. `null` leaves it as plain text, which
     *   is right when the bar already has a back control that goes to the same place.
     * @param alignment Where the block's lines are aligned.
     * @param style Colors of the bar. Defaults to the bar this is composed in.
     * @param size Metrics of the bar. Defaults to the bar this is composed in.
     */
    @Composable
    public fun Breadcrumb(
        parent: String,
        title: String,
        modifier: Modifier = Modifier,
        onParentClick: (() -> Unit)? = null,
        alignment: TopBarTitleAlignment = TopBarTitleAlignment.Start,
        style: TopBarStyle = LocalTopBarStyle.current ?: TopBarStyle.Default,
        size: TopBarSize = LocalTopBarSize.current ?: TopBarSize.Default,
    ) {
        TitleColumn(modifier = modifier, alignment = alignment, size = size) {
            if (onParentClick == null) {
                SubtitleText(text = parent, size = size, style = style)
            } else {
                Button(
                    onClick = onParentClick,
                    style = ButtonStyle.plain(ActionColor.Neutral, ArecaTheme.shapes.md)
                        .copy(content = style.subtitle, extra = style.subtitle),
                    // No padding and no minimum height: a breadcrumb sits on the title's line, and
                    // a control-sized tap target here would make the bar taller than a plain title.
                    // It is a shortcut to somewhere the back control already goes, never the only
                    // way there.
                    size = size.buttonSize.copy(
                        minHeight = 0.dp,
                        padding = PaddingValues(0.dp),
                        textStyle = size.subtitleTextStyle,
                    ),
                    textStyle = size.subtitleTextStyle,
                    leading = {
                        // The bundled chevron mirrors itself, so in a right-to-left layout it
                        // still points back toward the parent rather than away from it.
                        Icon(
                            imageVector = BundledIcons.ChevronLeft,
                            contentDescription = null,
                            size = IconSize.ExtraSmall,
                        )
                    },
                ) {
                    Text(text = parent, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }

            TitleText(text = title, size = size)
        }
    }

    /**
     * A screen's name over what it is currently doing.
     *
     * The dot carries no meaning assistive technology can read, so the words beside it have to say
     * the status outright — "Connected", not a green dot next to the room's name.
     *
     * @param title The screen's name.
     * @param status What is happening — "Online", "Syncing", "Offline".
     * @param modifier Modifier applied to the block.
     * @param statusColor Color of the dot. Defaults to the subtitle's color, which is the honest
     *   choice for a status with no urgency.
     * @param alignment Where the block's lines are aligned.
     * @param style Colors of the bar. Defaults to the bar this is composed in.
     * @param size Metrics of the bar. Defaults to the bar this is composed in.
     */
    @Composable
    public fun Status(
        title: String,
        status: String,
        modifier: Modifier = Modifier,
        statusColor: Color = Color.Unspecified,
        alignment: TopBarTitleAlignment = TopBarTitleAlignment.Start,
        style: TopBarStyle = LocalTopBarStyle.current ?: TopBarStyle.Default,
        size: TopBarSize = LocalTopBarSize.current ?: TopBarSize.Default,
    ) {
        TitleColumn(modifier = modifier, alignment = alignment, size = size) {
            TitleText(text = title, size = size)

            Row(
                horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.sm),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IndicatorDot(
                    color = statusColor.takeIf { it != Color.Unspecified } ?: style.subtitle,
                    size = IndicatorDotSize.Small,
                )

                SubtitleText(text = status, size = size, style = style)
            }
        }
    }

    /**
     * A screen's name with how many of something it holds.
     *
     * @param title The screen's name.
     * @param count The total, already formatted — "12", "99+", "3 of 8".
     * @param modifier Modifier applied to the block.
     * @param badgeStyle Semantic family of the count. Neutral by default, because a total is a fact
     *   rather than a warning.
     * @param size Metrics of the bar. Defaults to the bar this is composed in.
     */
    @Composable
    public fun Counter(
        title: String,
        count: String,
        modifier: Modifier = Modifier,
        badgeStyle: BadgeStyle = BadgeStyle.Neutral,
        size: TopBarSize = LocalTopBarSize.current ?: TopBarSize.Default,
    ) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // The title gives way first: a count that truncated would be a wrong number, and a
            // count is only two or three characters wide in the first place.
            TitleText(text = title, size = size, modifier = Modifier.weight(1f, fill = false))

            Badge(style = badgeStyle, size = BadgeSize.Small, soft = true) {
                Text(text = count, maxLines = 1)
            }
        }
    }

    /**
     * A screen's name that opens a picker: a workspace switcher, an account, a filter.
     *
     * The chevron is drawn at text size rather than at the bar's glyph size, because it is a mark
     * on the title rather than a control beside it — and it is the animated
     * [dev.voir.areca.foundation.icon.Chevron] rather than a vector, because unlike a back arrow this one
     * genuinely turns when the picker opens.
     *
     * Announce the picker's state on the popup this opens; the title itself only says it is a
     * button.
     *
     * @param title The current selection.
     * @param expanded Whether the picker this opens is showing.
     * @param onClick Called when the title is pressed.
     * @param modifier Modifier applied to the block.
     * @param style Colors of the bar. Defaults to the bar this is composed in.
     * @param size Metrics of the bar. Defaults to the bar this is composed in.
     */
    @Composable
    public fun Menu(
        title: String,
        expanded: Boolean,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        style: TopBarStyle = LocalTopBarStyle.current ?: TopBarStyle.Default,
        size: TopBarSize = LocalTopBarSize.current ?: TopBarSize.Default,
    ) {
        Button(
            onClick = onClick,
            style = ButtonStyle.plain(ActionColor.Neutral, ArecaTheme.shapes.md)
                .copy(content = style.content, extra = style.subtitle),
            size = size.buttonSize.copy(textStyle = size.textStyle),
            textStyle = size.textStyle,
            modifier = modifier,
            trailing = {
                Chevron(
                    direction = if (expanded) ChevronDirection.Up else ChevronDirection.Down,
                    size = ChevronSize.Small,
                )
            },
        ) {
            Text(text = title, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }

    /**
     * A screen's name beside a mark that says whose product it is.
     *
     * @param title The screen's name.
     * @param modifier Modifier applied to the block.
     * @param subtitle Line of detail under the title.
     * @param alignment Where the block's lines are aligned.
     * @param style Colors of the bar. Defaults to the bar this is composed in.
     * @param size Metrics of the bar. Defaults to the bar this is composed in.
     * @param logo The mark, drawn in a box the size of the bar's avatar controls so a logo and an
     *   account photo in the same bar come out the same size.
     */
    @Composable
    public fun Branded(
        title: String,
        modifier: Modifier = Modifier,
        subtitle: String? = null,
        alignment: TopBarTitleAlignment = TopBarTitleAlignment.Start,
        style: TopBarStyle = LocalTopBarStyle.current ?: TopBarStyle.Default,
        size: TopBarSize = LocalTopBarSize.current ?: TopBarSize.Default,
        logo: @Composable BoxScope.() -> Unit,
    ) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(size.gap),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier.size(size.avatar),
                contentAlignment = Alignment.Center,
                content = logo,
            )

            Default(
                title = title,
                subtitle = subtitle,
                alignment = alignment,
                style = style,
                size = size,
                modifier = Modifier.weight(1f, fill = false),
            )
        }
    }
}

/**
 * The two-line stack every multi-line title block is built on.
 *
 * @param modifier Modifier applied to the column.
 * @param alignment Where the lines are aligned.
 * @param size Metrics the gap between the lines comes from.
 * @param content The lines.
 */
@Composable
private fun TitleColumn(
    modifier: Modifier,
    alignment: TopBarTitleAlignment,
    size: TopBarSize,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = when (alignment) {
            TopBarTitleAlignment.Start -> Alignment.Start
            TopBarTitleAlignment.Center -> Alignment.CenterHorizontally
        },
        verticalArrangement = Arrangement.spacedBy(size.titleGap),
    ) {
        content()
    }
}

/**
 * A title line: the bar's own text style and color, held to one line.
 *
 * @param text The line.
 * @param size Metrics the text style comes from.
 * @param modifier Modifier applied to the text.
 */
@Composable
private fun TitleText(text: String, size: TopBarSize, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = size.textStyle,
        modifier = modifier,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

/**
 * A line of detail under a title, a step back from it and held to one line.
 *
 * @param text The line.
 * @param style Colors the text color comes from.
 * @param size Metrics the text style comes from.
 * @param modifier Modifier applied to the text.
 */
@Composable
private fun SubtitleText(
    text: String,
    style: TopBarStyle,
    size: TopBarSize,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = size.subtitleTextStyle,
        color = style.subtitle,
        modifier = modifier,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}
