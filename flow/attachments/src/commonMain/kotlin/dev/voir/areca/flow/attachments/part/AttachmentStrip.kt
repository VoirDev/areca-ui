package dev.voir.areca.flow.attachments.part

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.offset
import dev.voir.areca.flow.attachments.AttachmentItem
import dev.voir.areca.flow.attachments.AttachmentState
import dev.voir.areca.flow.attachments.icon.AlertCircle
import dev.voir.areca.flow.attachments.icon.BundledIcons
import dev.voir.areca.foundation.AnnounceOnChange
import dev.voir.areca.foundation.notice.Notice
import dev.voir.areca.foundation.notice.NoticeText
import dev.voir.areca.foundation.notice.NoticeTone
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.theme.ArecaTheme

/**
 * A record's files as a row of squares that scrolls sideways — on the form that writes the record,
 * or on the details that show it.
 *
 * **One strip, from nothing to many.** Empty, it is the add square with a line beside it saying
 * what can go there; with files, it is the add square followed by every file. Without [onAdd] the
 * add square is not drawn — at a limit, or where the files are only shown — and a strip with nothing
 * to show, nothing to add and nothing to report draws nothing at all.
 *
 * **Edge to edge.** The squares scroll under both screen edges while the first one starts at
 * [gutter], like every other row of a screen. Place the strip full width; inside a container already
 * padded by [gutter], pass [bleed] so it reaches past that padding.
 *
 * Which files there are, how many are allowed and whether one may be taken off are the
 * application's: the strip draws [items], offers [onAdd] and [onRemove] when given, and states
 * [counter] and [failure] as worded.
 *
 * @param items The files, in the order they are shown.
 * @param onOpen Called with a file whose square was pressed, or `null` for squares that only show.
 * @param modifier Modifier applied to the block.
 * @param onAdd Called when the add square is pressed, or `null` to draw none.
 * @param onRemove Called with a file whose remove mark was pressed, or `null` to draw no marks.
 * @param title Quiet label over the strip ("Attachments"), or `null` for none.
 * @param counter Words at the other end of that line — how many there are against a limit
 *   ("3 of 20") — or `null` for none.
 * @param emptyTitle Line beside the add square while there are no files ("Add a receipt or
 *   file"), or `null` for none.
 * @param emptyDescription Quieter line under it ("Photos, PDFs and documents, up to 25 MB each"), or
 *   `null` for none.
 * @param failure Why the last change to the files could not be made, stated under the strip and
 *   announced, or `null` for none.
 * @param caption Line under a file's square, or `null` for none; see [AttachmentTile].
 * @param detail Second line under a file's square, or `null` for none.
 * @param addCaption Word under the add square, or `null` for none.
 * @param addLabel What the add square does, read out in place of its plus.
 * @param removeLabel What a file's remove mark does, read out in place of its cross.
 * @param description What a file's square is called for assistive technology; see
 *   [AttachmentTile].
 * @param stateDescription Where a file's bytes are, worded for assistive technology, or `null` to say
 *   nothing; see [AttachmentTile].
 * @param gutter Distance from the screen edge to the first square and to the lines of text.
 * @param bleed Whether the strip sits in a container already padded by [gutter], and reaches past
 *   that padding to both edges.
 * @param style Colours and shape of every square. Defaults to the [LocalAttachmentTileStyle] in
 *   scope, or [AttachmentTileStyle.Default].
 * @param tileSize Side of every square. A step larger where the files are what was opened to be
 *   looked at, as on a record's details.
 */
@Composable
public fun AttachmentStrip(
    items: List<AttachmentItem>,
    onOpen: ((AttachmentItem) -> Unit)?,
    modifier: Modifier = Modifier,
    onAdd: (() -> Unit)? = null,
    onRemove: ((AttachmentItem) -> Unit)? = null,
    title: String? = null,
    counter: String? = null,
    emptyTitle: String? = null,
    emptyDescription: String? = null,
    failure: String? = null,
    caption: (AttachmentItem) -> String? = { item -> item.label },
    detail: (AttachmentItem) -> String? = { null },
    addCaption: String? = null,
    addLabel: String = "Add attachment",
    removeLabel: (AttachmentItem) -> String = { item -> "Remove ${item.label}" },
    description: (AttachmentItem) -> String = { item -> defaultDescription(item) },
    stateDescription: (AttachmentState) -> String? = { state -> defaultStateDescription(state) },
    gutter: Dp = ArecaTheme.spacing.horizontalContentInsets,
    bleed: Boolean = false,
    style: AttachmentTileStyle = LocalAttachmentTileStyle.current ?: AttachmentTileStyle.Default,
    tileSize: Dp = ArecaTheme.spacing.xl8,
) {
    // Before the early return, so a failure that clears is forgotten and one given again is
    // announced again.
    AnnounceOnChange(failure)

    if (items.isEmpty() && onAdd == null && failure == null) return

    val inset = Modifier.padding(horizontal = gutter)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .then(if (bleed) Modifier.bleed(gutter) else Modifier),
        verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
    ) {
        if (title != null || counter != null) {
            Row(
                modifier = Modifier.fillMaxWidth().then(inset),
                horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
            ) {
                Text(
                    text = title.orEmpty(),
                    modifier = Modifier.weight(1f),
                    style = ArecaTheme.typography.bodySmall,
                    color = ArecaTheme.colors.fgMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                if (counter != null) {
                    Text(
                        text = counter,
                        style = ArecaTheme.typography.bodySmall,
                        color = ArecaTheme.colors.fgMuted,
                        maxLines = 1,
                    )
                }
            }
        }

        if (items.isEmpty()) {
            if (onAdd != null) {
                EmptyStrip(
                    onAdd = onAdd,
                    title = emptyTitle,
                    description = emptyDescription,
                    tileSize = tileSize,
                    addLabel = addLabel,
                    style = style,
                    modifier = inset,
                )
            }
        } else {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = gutter),
                horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
            ) {
                if (onAdd != null) {
                    item(key = AddKey) {
                        AttachmentAddTile(
                            onClick = onAdd,
                            size = tileSize,
                            caption = addCaption,
                            label = addLabel,
                            style = style,
                        )
                    }
                }

                items(items = items, key = { item -> item.key.toString() }) { item ->
                    AttachmentTile(
                        item = item,
                        onClick = onOpen?.let { open -> { open(item) } },
                        size = tileSize,
                        caption = caption(item),
                        detail = detail(item),
                        onRemove = onRemove?.let { remove -> { remove(item) } },
                        removeLabel = removeLabel(item),
                        style = style,
                        description = description(item),
                        stateDescription = stateDescription(item.state),
                    )
                }
            }
        }

        if (failure != null) {
            Notice(
                title = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .then(inset),
                icon = BundledIcons.AlertCircle,
                tone = NoticeTone.Danger,
            ) {
                NoticeText(failure)
            }
        }
    }
}

/**
 * The strip with nothing in it: the add square and, beside it, what can be added.
 *
 * @param onAdd Called when the add square is pressed.
 * @param title Line beside the square, or `null` for none.
 * @param description Quieter line under it, or `null` for none.
 * @param tileSize Side of the square.
 * @param addLabel What the square does, read out.
 * @param style Colours and shape.
 * @param modifier Modifier applied to the row.
 */
@Composable
private fun EmptyStrip(
    onAdd: () -> Unit,
    title: String?,
    description: String?,
    tileSize: Dp,
    addLabel: String,
    style: AttachmentTileStyle,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AttachmentAddTile(onClick = onAdd, size = tileSize, label = addLabel, style = style)

        if (title != null || description != null) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xs2),
            ) {
                if (title != null) {
                    Text(
                        text = title,
                        style = ArecaTheme.typography.labelMedium,
                        color = ArecaTheme.colors.fgBase,
                    )
                }

                if (description != null) {
                    Text(
                        text = description,
                        style = ArecaTheme.typography.bodySmall,
                        color = ArecaTheme.colors.fgMuted,
                    )
                }
            }
        }
    }
}

/**
 * Widens the element past its parent's padding by [horizontal] on each side.
 *
 * @param horizontal How far to reach past each edge.
 * @return The modifier.
 */
private fun Modifier.bleed(horizontal: Dp): Modifier = layout { measurable, constraints ->
    val extra = horizontal.roundToPx() * 2
    val placeable = measurable.measure(constraints.offset(horizontal = extra))

    layout(placeable.width - extra, placeable.height) {
        placeable.place(-extra / 2, 0)
    }
}

/** Key of the add square, which is not a file and has no key of its own. */
private const val AddKey = "dev.voir.areca.flow.attachments.add"
