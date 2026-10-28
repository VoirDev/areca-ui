package dev.voir.areca.media.thumbnail

import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import dev.voir.areca.indication.TouchIndication
import dev.voir.areca.modifier.clickCursor
import dev.voir.areca.theme.ArecaTheme

/**
 * A row — or a column — of small pictures, one per item, the selected one ringed: under a product's
 * pictures, along a viewer's foot, beside a desktop product page's picture.
 *
 * Pressing one calls [onSelect]; moving the carousel or viewer is the caller's, and the strip
 * follows [selectedIndex], scrolling it to the middle as it changes. Each thumbnail is a tab to
 * assistive technology, named by [label] — "Laces, close up, 3 of 7".
 *
 * Draw each picture in [thumbnail], cropped to fill: a
 * [MediaFrame][dev.voir.areca.media.image.MediaFrame] with `fit = MediaFit.Fill`, or anything that
 * fills the square. The strip clips it to its shape.
 *
 * @param count Number of items.
 * @param selectedIndex Index of the item shown.
 * @param onSelect Called with the index of a thumbnail pressed.
 * @param modifier Modifier applied to the strip.
 * @param orientation [Orientation.Horizontal] for a strip, [Orientation.Vertical] for a rail.
 * @param contentPadding Space before the first thumbnail and after the last, inside the scroll.
 * @param label Names a thumbnail for assistive technology, given its index and the count.
 * @param style Ring, shape and the others' opacity.
 * @param size Metrics.
 * @param thumbnail Draws the picture for an index.
 */
@Composable
public fun ThumbnailStrip(
    count: Int,
    selectedIndex: Int,
    onSelect: (index: Int) -> Unit,
    modifier: Modifier = Modifier,
    orientation: Orientation = Orientation.Horizontal,
    contentPadding: PaddingValues = PaddingValues(ArecaTheme.spacing.none),
    label: (index: Int, count: Int) -> String = { index, total -> "Picture ${index + 1} of $total" },
    style: ThumbnailStripStyle = LocalThumbnailStripStyle.current ?: ThumbnailStripStyle.Default,
    size: ThumbnailStripSize = LocalThumbnailStripSize.current ?: ThumbnailStripSize.Default,
    thumbnail: @Composable (index: Int) -> Unit,
) {
    val listState = rememberLazyListState()
    val side = with(LocalDensity.current) { size.side.roundToPx() }
    LaunchedEffect(selectedIndex, count) {
        if (selectedIndex in 0 until count) listState.centreOn(selectedIndex, side, orientation)
    }

    val items: LazyListScope.() -> Unit = {
        items(count) { index ->
            Thumbnail(
                selected = index == selectedIndex,
                label = label(index, count),
                onClick = { onSelect(index) },
                style = style,
                size = size,
            ) { thumbnail(index) }
        }
    }
    val group = modifier.selectableGroup()
    if (orientation == Orientation.Horizontal) {
        LazyRow(
            modifier = group,
            state = listState,
            contentPadding = contentPadding,
            horizontalArrangement = Arrangement.spacedBy(size.spacing),
            content = items,
        )
    } else {
        LazyColumn(
            modifier = group,
            state = listState,
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(size.spacing),
            content = items,
        )
    }
}

/**
 * One thumbnail: its square, pressable, ringed when selected, faint otherwise.
 *
 * @param selected Whether it is the item shown.
 * @param label Its name for assistive technology.
 * @param onClick Selects it.
 * @param style Look.
 * @param size Metrics.
 * @param content The picture.
 */
@Composable
private fun Thumbnail(
    selected: Boolean,
    label: String,
    onClick: () -> Unit,
    style: ThumbnailStripStyle,
    size: ThumbnailStripSize,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(size.side)
            .semantics { contentDescription = label }
            .clickCursor()
            .selectable(
                selected = selected,
                interactionSource = null,
                indication = TouchIndication(shape = style.shape),
                role = Role.Tab,
                onClick = onClick,
            )
            .then(if (selected) Modifier.border(size.ringWidth, style.ring, style.shape) else Modifier),
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(if (selected) size.ringWidth + size.ringGap else ArecaTheme.spacing.none)
                .clip(style.shape)
                .alpha(if (selected) 1f else style.otherAlpha),
        ) { content() }
    }
}

/**
 * Scrolls so the item at [index] sits in the middle of the strip, or as near as its ends allow.
 *
 * @param index The item.
 * @param side Its side, in pixels.
 * @param orientation Which way the strip runs.
 */
private suspend fun LazyListState.centreOn(index: Int, side: Int, orientation: Orientation) {
    val viewport = layoutInfo.viewportSize.let { if (orientation == Orientation.Horizontal) it.width else it.height }
    animateScrollToItem(index, scrollOffset = -(viewport - side) / 2)
}
