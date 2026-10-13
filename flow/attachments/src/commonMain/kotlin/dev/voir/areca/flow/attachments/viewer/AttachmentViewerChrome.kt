package dev.voir.areca.flow.attachments.viewer

import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.lerp
import dev.voir.areca.flow.attachments.AttachmentItem
import dev.voir.areca.flow.attachments.part.AttachmentThumbnail
import dev.voir.areca.flow.attachments.part.AttachmentTileStyle
import dev.voir.areca.foundation.topbar.CloseAction
import dev.voir.areca.foundation.topbar.OverflowAction
import dev.voir.areca.foundation.topbar.ShareAction
import dev.voir.areca.foundation.topbar.TopBar
import dev.voir.areca.foundation.topbar.TopBarTitleAlignment
import dev.voir.areca.indication.FadeIndication
import dev.voir.areca.modifier.MinimumTouchTarget
import dev.voir.areca.modifier.clickCursor
import dev.voir.areca.theme.ArecaTheme
import kotlin.math.abs

/**
 * The viewer's heading: the way out, what the file is and where it sits among the others, and what
 * can be done with it.
 *
 * A foundation [TopBar] in the viewer's plain style, with the title centred on the screen. The
 * share control keeps its room while a file cannot be shared yet, so the options control beside it
 * does not jump sideways as the pages are swiped; while it cannot be pressed it is neither drawn
 * nor in the accessibility tree.
 *
 * @param title What the file is.
 * @param position Where it sits among the files ("2 of 3"), or `null` for none.
 * @param style Colours of the viewer.
 * @param closeLabel What the close button does, read out.
 * @param onClose Called when the viewer is closed.
 * @param shareLabel What the share button does, read out.
 * @param onShare Called when sharing is asked for, or `null` to draw no share button.
 * @param moreLabel What the options button does, read out.
 * @param onMore Called when the options are asked for, or `null` to draw no options button.
 * @param modifier Modifier applied to the bar.
 */
@Composable
internal fun AttachmentViewerTopBar(
    title: String,
    position: String?,
    style: AttachmentViewerStyle,
    closeLabel: String,
    onClose: () -> Unit,
    shareLabel: String,
    onShare: (() -> Unit)?,
    moreLabel: String,
    onMore: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    TopBar(
        title = title,
        subtitle = position,
        modifier = modifier,
        style = style.topBar(),
        titleAlignment = TopBarTitleAlignment.Center,
        leading = { CloseAction(onClick = onClose, contentDescription = closeLabel) },
        trailing = {
            val share = onShare
            if (share != null) {
                ShareAction(onClick = share, contentDescription = shareLabel)
            } else {
                ShareAction(
                    onClick = {},
                    modifier = Modifier
                        .alpha(0f)
                        .clearAndSetSemantics {},
                    contentDescription = null,
                    enabled = false,
                )
            }

            if (onMore != null) {
                OverflowAction(onClick = onMore, contentDescription = moreLabel)
            }
        },
    )
}

/**
 * Every file in a row under the viewer, to jump between them.
 *
 * **The current one is larger and the rest are faded**, rather than ringed. Both follow the pager's
 * scroll rather than its page: a swipe grows the next file and shrinks the last one as it goes, and
 * a jump across several files passes through each on the way. The row follows the pager, so swiping
 * keeps the current file in view here as well.
 *
 * Every file's pressable slot is the platform's minimum target, the size of the current square,
 * however small its square is drawn; the press and focus answer follow the square. To assistive
 * technology the row is one group of tabs, the file on screen selected.
 *
 * @param items The files.
 * @param current Index of the file on screen.
 * @param position Where the pager is, in pages: the index of the page in view plus how far it has
 *   been dragged towards the next, so `1.5` is halfway from the second file to the third. Read only
 *   while drawing, so a swipe redraws the squares without composing the row again.
 * @param onSelect Called with the index of a pressed file.
 * @param label Words a file for assistive technology, given its index and the count.
 * @param style Colours of the viewer.
 * @param modifier Modifier applied to the row.
 */
@Composable
internal fun AttachmentFilmstrip(
    items: List<AttachmentItem>,
    current: Int,
    position: () -> Float,
    onSelect: (Int) -> Unit,
    label: (item: AttachmentItem, index: Int, count: Int) -> String,
    style: AttachmentViewerStyle,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val shape = RoundedCornerShape(ArecaTheme.radius.md)
    val tileStyle = AttachmentTileStyle.default(bg = style.controlBg, glyph = style.controlContent, shape = shape)
    val large = MinimumTouchTarget
    val small = ArecaTheme.spacing.xl5

    LaunchedEffect(current) {
        if (current in items.indices) listState.animateScrollToItem(current)
    }

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .selectableGroup(),
        state = listState,
        contentPadding = PaddingValues(horizontal = ArecaTheme.spacing.horizontalContentInsets),
        // Centred while they fit, which a handful of files always does. The slots are all the large
        // size, so the gap between two small squares is this plus the room they leave.
        horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xs, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        itemsIndexed(items = items, key = { _, item -> item.key.toString() }) { index, item ->
            val chosen = index == current
            val description = label(item, index, items.size)
            val interactionSource = remember { MutableInteractionSource() }

            // 1 on the page in view, falling to 0 a page away either side.
            fun nearness(): Float = (1f - abs(index - position())).coerceIn(0f, 1f)

            Box(
                modifier = Modifier
                    .size(large)
                    .semantics { contentDescription = description }
                    .clickCursor()
                    .selectable(
                        selected = chosen,
                        interactionSource = interactionSource,
                        indication = null,
                        role = Role.Tab,
                        onClick = { onSelect(index) },
                    ),
                contentAlignment = Alignment.Center,
            ) {
                // Drawn at the large size and scaled down, so the picture is loaded once rather than
                // at every size a swipe passes through, and the scale is read only while drawing.
                Box(
                    modifier = Modifier
                        .requiredSize(large)
                        .graphicsLayer {
                            val scale = lerp(small, large, nearness()) / large
                            scaleX = scale
                            scaleY = scale
                        }
                        .indication(interactionSource, FadeIndication.tint(shape)),
                ) {
                    AttachmentThumbnail(
                        item = item,
                        modifier = Modifier.graphicsLayer {
                            alpha = UNCHOSEN_ALPHA + (1f - UNCHOSEN_ALPHA) * nearness()
                        },
                        style = tileStyle,
                        size = large,
                        marks = false,
                    )
                }
            }
        }
    }
}

/** How far the files not on screen fade back in the filmstrip. */
private const val UNCHOSEN_ALPHA = 0.5f
