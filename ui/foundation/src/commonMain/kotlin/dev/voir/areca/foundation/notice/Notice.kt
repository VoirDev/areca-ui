package dev.voir.areca.foundation.notice

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.foundation.LocalTextStyle
import dev.voir.areca.foundation.feedback.Spinner
import dev.voir.areca.foundation.feedback.SpinnerSize
import dev.voir.areca.foundation.feedback.SpinnerStyle
import dev.voir.areca.foundation.icon.FeaturedIcon
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.modifier.thenIf

/**
 * A block set off from the page to say one thing: what happened, or what is true, and what follows
 * from it.
 *
 * It takes two shapes, chosen by whether there is a [title]:
 *
 * - **A banner** — a title and the lines that explain it, with the mark as a chip **on top**. On
 *   top rather than beside, because beside it claims a column the whole height of the block and
 *   pushes every line in by its width; above, the copy runs the full width and the mark reads as the
 *   heading.
 * - **A note** — a line or two with no title, and the mark as a bare glyph **at the start**, level
 *   with the first line. A mark above one sentence would be a heading with nothing under it.
 *
 * [NoticeSize.iconPlacement] overrides either placement.
 *
 * **A danger notice is filled by default** — its ground, a solid mark and every line of copy take
 * the tone — because it is the one block on the page saying something went wrong, and a red mark
 * over grey copy reads as a warning glyph dropped onto an ordinary paragraph. Every other tone is
 * subtle by default: the mark carries the tone and the copy stays the page's greys. See
 * [NoticeEmphasis].
 *
 * The copy is a slot. Write its lines with [NoticeText], which takes the notice's inks; plain text
 * inside also takes the ordinary line's ink and style. Anything else can go in it too — a button, a
 * progress bar, a divider and a footer row.
 *
 * A notice is not a live region. One that appears in response to something the reader did — a
 * failed sign-in, say — should say so through [modifier]'s semantics, and one that comes and goes
 * should be wrapped in [AnimatedNotice].
 *
 * @param title The headline, or `null` for a note.
 * @param modifier Modifier applied to the block.
 * @param icon The mark, or `null` for none.
 * @param tone What the notice is saying.
 * @param emphasis How much of the notice carries [tone].
 * @param busy Whether something the notice describes is running, which a spinner in the mark's place
 * says.
 * @param style Colors and shape. Defaults to [LocalNoticeStyle] when a subtree provides one, and
 *   otherwise to [NoticeStyle.of] for [tone] and [emphasis]. Build one with [NoticeStyle.of] to
 *   change the ground or the corner.
 * @param size Metrics and typography. Defaults to the theme's.
 * @param content The lines under the title — or, in a note, the whole of what it says.
 */
@Composable
public fun Notice(
    title: String?,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    tone: NoticeTone = NoticeTone.Neutral,
    emphasis: NoticeEmphasis = if (tone == NoticeTone.Danger) {
        NoticeEmphasis.Filled
    } else {
        NoticeEmphasis.Subtle
    },
    busy: Boolean = false,
    style: NoticeStyle = LocalNoticeStyle.current ?: NoticeStyle.of(tone, emphasis),
    size: NoticeSize = LocalNoticeSize.current ?: NoticeSize.Default,
    content: @Composable ColumnScope.() -> Unit = {},
) {
    val titled = title != null
    val placement = size.iconPlacement ?: if (titled) NoticeIconPlacement.Top else NoticeIconPlacement.Start
    val block = modifier
        .fillMaxWidth()
        .clip(style.shape)
        .background(style.bg)
        .padding(size.padding)

    val copy: @Composable ColumnScope.() -> Unit = {
        if (title != null) {
            Text(text = title, style = size.titleTextStyle, color = style.title)
            Spacer(Modifier.height(size.textGap))
        }

        CompositionLocalProvider(
            LocalNoticeInk provides NoticeInk(style.body, style.quiet, size.quietTextStyle),
            LocalContentColor provides style.body,
            LocalTextStyle provides size.bodyTextStyle,
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(size.lineGap), content = content)
        }
    }

    when {
        icon == null && !busy -> Column(modifier = block, content = copy)

        placement == NoticeIconPlacement.Top -> Column(modifier = block) {
            NoticeMark(icon = icon, busy = busy, chip = titled, style = style, size = size)
            Spacer(Modifier.height(size.iconGap))
            copy()
        }

        else -> Row(modifier = block) {
            val mark = if (titled) size.chip.container else size.glyph
            val firstLine = if (titled) size.titleTextStyle else size.bodyTextStyle

            NoticeMark(
                icon = icon,
                busy = busy,
                chip = titled,
                style = style,
                size = size,
                // Level with the first line: a mark shorter than the line it sits beside is moved
                // down onto that line's centre rather than left hanging from its top edge.
                modifier = Modifier.padding(top = centreOffset(firstLine, mark)),
            )
            Spacer(Modifier.width(size.iconGap))
            Column(modifier = Modifier.weight(1f), content = copy)
        }
    }
}

/**
 * One line of a [Notice]'s copy, in the notice's own ink.
 *
 * @param text The line.
 * @param modifier Modifier applied to the line.
 * @param quiet Whether it is the notice's last, quietest line — advice rather than the fact.
 */
@Composable
public fun NoticeText(
    text: String,
    modifier: Modifier = Modifier,
    quiet: Boolean = false,
) {
    val ink = LocalNoticeInk.current
    if (quiet) {
        Text(text = text, modifier = modifier, style = ink.quietTextStyle, color = ink.quiet)
    } else {
        Text(text = text, modifier = modifier, color = ink.body)
    }
}

/**
 * A notice that is there only while there is something for it to say.
 *
 * It fades and unfolds in when [value] arrives and folds away when it is cleared, and it holds on to
 * the last value while it leaves — so a notice does not empty itself halfway through its own exit.
 *
 * @param value What the notice is about, or `null` for no notice.
 * @param modifier Modifier applied to the animated container.
 * @param content The notice, given the value it is about.
 */
@Composable
public fun <T : Any> AnimatedNotice(
    value: T?,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit,
) {
    var last by remember { mutableStateOf(value) }
    if (value != null) last = value

    AnimatedVisibility(
        visible = value != null,
        modifier = modifier,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically(),
    ) {
        last?.let { content(it) }
    }
}

/**
 * The mark of a [Notice]: a chip on a titled one, a bare glyph on a note, a spinner on a busy one.
 *
 * @param icon Glyph to draw; unused while [busy].
 * @param busy Whether to draw a spinner instead.
 * @param chip Whether the mark is a chip rather than a bare glyph.
 * @param style Colors of the notice.
 * @param size Metrics of the notice.
 * @param modifier Modifier applied to the mark.
 */
@Composable
private fun NoticeMark(
    icon: ImageVector?,
    busy: Boolean,
    chip: Boolean,
    style: NoticeStyle,
    size: NoticeSize,
    modifier: Modifier = Modifier,
) {
    val box = if (chip) size.chip.container else size.glyph

    when {
        // In the mark's own footprint — on the chip's own tile, for a titled notice — so nothing
        // moves or changes colour but the glyph when the work ends and the mark returns.
        busy -> Box(
            modifier = modifier
                .size(box)
                .thenIf(chip) { background(style.chip.bg ?: Color.Transparent, style.chipShape) },
            contentAlignment = Alignment.Center,
        ) {
            Spinner(
                style = SpinnerStyle(color = style.glyph),
                size = SpinnerSize(if (chip) size.chip.icon else size.glyph),
            )
        }

        icon == null -> Unit

        chip -> FeaturedIcon(
            imageVector = icon,
            contentDescription = null,
            modifier = modifier,
            size = size.chip,
            style = style.chip,
            shape = style.chipShape,
        )

        else -> Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = modifier,
            tint = style.glyph,
            size = size.glyph,
        )
    }
}

/**
 * How far to move a mark down so its centre is on the centre of a line of text.
 *
 * @param line Style of the line the mark sits beside.
 * @param mark Height of the mark.
 * @return Offset from the line's top edge; zero for a mark as tall as the line or taller.
 */
@Composable
private fun centreOffset(line: TextStyle, mark: Dp): Dp {
    val lineHeight = with(LocalDensity.current) {
        if (line.lineHeight.isSpecified) line.lineHeight.toDp() else line.fontSize.toDp()
    }
    return ((lineHeight - mark) / 2).coerceAtLeast(0.dp)
}

/**
 * The inks of the notice a line is inside.
 *
 * @property body Color of an ordinary line.
 * @property quiet Color of the quietest line.
 * @property quietTextStyle Style of the quietest line.
 */
private class NoticeInk(val body: Color, val quiet: Color, val quietTextStyle: TextStyle)

/** The inks of the enclosing [Notice]; unspecified outside one. */
private val LocalNoticeInk = staticCompositionLocalOf {
    NoticeInk(body = Color.Unspecified, quiet = Color.Unspecified, quietTextStyle = TextStyle.Default)
}
