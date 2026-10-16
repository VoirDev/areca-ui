package dev.voir.areca.flow.profile.part

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.style.TextOverflow
import dev.voir.areca.flow.profile.ProfileSummary
import dev.voir.areca.foundation.avatar.AvatarSize
import dev.voir.areca.foundation.feedback.Skeleton
import dev.voir.areca.foundation.feedback.SkeletonSize
import dev.voir.areca.foundation.icon.Chevron
import dev.voir.areca.foundation.icon.ChevronDirection
import dev.voir.areca.foundation.icon.ChevronSize
import dev.voir.areca.foundation.slot.SlotRow
import dev.voir.areca.foundation.slot.SlotRowLayout
import dev.voir.areca.foundation.slot.firstLineCenter
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.indication.FadeIndication
import dev.voir.areca.modifier.clickCursor
import dev.voir.areca.theme.ArecaTheme

/**
 * Who is signed in, as a compact card that leads into the account — the first block of an account
 * menu or a settings screen.
 *
 * A way into somewhere, so it is pressable across its width; put a [ProfileSummaryCardAction] —
 * "Edit" and a chevron — in [trailing] to name the action at its end. The card is a settings
 * section's card, with the same fill, corner and press tint, so it reads as part of the list under
 * it. [ProfileGreeting] is the same row for a surface
 * that is not about the account; [ProfileHeaderCard] is the larger card a profile screen opens with.
 *
 * @param summary Who is signed in.
 * @param onClick Called when the card is pressed, to open the account.
 * @param modifier Modifier applied to the card.
 * @param onClickLabel What pressing does, for assistive technology, or `null` for none.
 * @param caption The quiet line under the name. Defaults to the summary's caption, else its handle,
 *   else its address. Pass [ProfileSummaryCaption] with text of your own — synchronisation state,
 *   a plan name — to replace it.
 * @param trailing Drawn at the card's end — a [ProfileSummaryCardAction], usually — or `null` for
 *   nothing.
 */
@Composable
public fun ProfileSummaryCard(
    summary: ProfileSummary,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onClickLabel: String? = null,
    caption: @Composable () -> Unit = { DefaultCaption(summary) },
    trailing: (@Composable BoxScope.() -> Unit)? = null,
) {
    ProfileCardSurface(modifier = modifier, onClick = onClick, onClickLabel = onClickLabel) {
        ProfileSummaryRow(
            modifier = Modifier.padding(cardPadding()),
            summary = summary,
            caption = caption,
            captionFirst = false,
            end = trailing,
        )
    }
}

/**
 * The end of a [ProfileSummaryCard] that names what pressing it does: a word and a chevron.
 *
 * @param modifier Modifier applied to the action.
 * @param label Word before the chevron — "Edit" — or `null` for the chevron alone.
 */
@Composable
public fun ProfileSummaryCardAction(
    modifier: Modifier = Modifier,
    label: String? = null,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.sm),
    ) {
        if (label != null) {
            Text(
                text = label,
                style = ArecaTheme.typography.bodySmall,
                color = ArecaTheme.colors.fgMuted,
            )
        }
        Chevron(
            direction = ChevronDirection.Right,
            color = ArecaTheme.colors.fgMuted,
            size = ChevronSize.ExtraSmall,
        )
    }
}

/**
 * Stand-in for [ProfileSummaryCard] while the account is loading, at the card's final height.
 *
 * Read by assistive technology as one item, [contentDescription], rather than as nothing at all.
 *
 * @param modifier Modifier applied to the card.
 * @param contentDescription What is loading, for assistive technology.
 */
@Composable
public fun ProfileSummaryCardPlaceholder(
    modifier: Modifier = Modifier,
    contentDescription: String = "Loading account",
) {
    ProfileCardSurface(modifier = modifier) {
        ProfileSummaryRowPlaceholder(
            contentDescription = contentDescription,
            modifier = Modifier.padding(cardPadding()),
        )
    }
}

/**
 * Who is signed in, across the top of a screen that is not about the account.
 *
 * The same picture beside the same name as [ProfileSummaryCard], and nothing else: no card, no
 * padding of its own and no press or hover tint. A home screen is not a way into the account, and a
 * block of text that flinches whenever the screen beside it is touched is noise on something people
 * scroll. It still opens the account when tapped — an avatar in a corner is where people look for
 * their own settings — and it is still a button: announced as one, given the hand cursor on desktop,
 * and ringed when keyboard focus reaches it, since a control focus lands on without showing it is
 * one a keyboard user cannot find.
 *
 * @param summary Who is signed in.
 * @param onClick Called when the greeting is tapped, to open the account.
 * @param modifier Modifier applied to the greeting.
 * @param greeting Line above the name — "Welcome back" — or `null` for the name alone.
 * @param onClickLabel What tapping does, for assistive technology, or `null` for none.
 */
@Composable
public fun ProfileGreeting(
    summary: ProfileSummary,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    greeting: String? = null,
    onClickLabel: String? = null,
) {
    ProfileSummaryRow(
        modifier = modifier
            .clickCursor()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                // The focus ring alone: no fill at rest, on hover or on press.
                indication = FadeIndication(
                    shape = ArecaTheme.shapes.lg,
                    hoverColor = Color.Transparent,
                    pressedColor = Color.Transparent,
                ),
                onClickLabel = onClickLabel,
                role = Role.Button,
                onClick = onClick,
            ),
        summary = summary,
        caption = greeting?.let { text -> { ProfileSummaryCaption(text = text) } },
        captionFirst = true,
    )
}

/**
 * Stand-in for [ProfileGreeting] while the account is loading, at the greeting's final height.
 *
 * Read by assistive technology as one item, [contentDescription], rather than as nothing at all.
 *
 * @param modifier Modifier applied to the row.
 * @param contentDescription What is loading, for assistive technology.
 */
@Composable
public fun ProfileGreetingPlaceholder(
    modifier: Modifier = Modifier,
    contentDescription: String = "Loading account",
) {
    ProfileSummaryRowPlaceholder(contentDescription = contentDescription, modifier = modifier)
}

/**
 * The quiet line of a summary row, in the one style both summary surfaces draw it in.
 *
 * @param text What it says.
 * @param modifier Modifier applied to the line.
 * @param color What to draw it in. Muted unless the line reports something somebody has to act on.
 */
@Composable
public fun ProfileSummaryCaption(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = ArecaTheme.colors.fgMuted,
) {
    Text(
        text = text,
        modifier = modifier,
        style = ArecaTheme.typography.bodySmall,
        color = color,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

/**
 * The caption a summary card draws when it is given none: the caption, else the handle, else the
 * address.
 *
 * @param summary Who is signed in.
 */
@Composable
private fun DefaultCaption(summary: ProfileSummary) {
    val text = summary.caption ?: summary.identityLine
    if (text != null) ProfileSummaryCaption(text = text)
}

/**
 * The row both summary surfaces are: a picture, two lines, and whatever the surface puts at the end.
 *
 * @param summary Who is signed in.
 * @param caption The quiet line, or `null` for the name alone.
 * @param captionFirst Whether [caption] sits above the name. A card leads with the name because it
 *   is a row among other things; a greeting leads with the greeting because it addresses somebody.
 * @param modifier Modifier applied to the row.
 * @param end Drawn at the trailing edge, or `null` for nothing.
 */
@Composable
private fun ProfileSummaryRow(
    summary: ProfileSummary,
    caption: (@Composable () -> Unit)?,
    captionFirst: Boolean,
    modifier: Modifier = Modifier,
    end: (@Composable BoxScope.() -> Unit)? = null,
) {
    // Aligned as a list row is: two lines beside an avatar centre as one block, so the chevron at
    // the end sits level with the picture rather than with the name.
    SlotRow(
        modifier = modifier,
        layout = SlotRowLayout.Auto.copy(gap = ArecaTheme.spacing.lg),
        start = {
            ProfileAvatar(
                name = summary.name,
                image = summary.avatar,
                size = AvatarSize.Medium,
            )
        },
        center = {
            Column {
                val name: @Composable () -> Unit = {
                    Text(
                        text = summary.name,
                        modifier = Modifier.firstLineCenter(ArecaTheme.typography.labelLarge),
                        style = ArecaTheme.typography.labelLarge,
                        color = ArecaTheme.colors.fgBase,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                if (caption == null) {
                    name()
                } else {
                    if (captionFirst) caption() else name()
                    Spacer(Modifier.height(ArecaTheme.spacing.xs))
                    if (captionFirst) name() else caption()
                }
            }
        },
        end = end,
    )
}

/**
 * Placeholder shaped like [ProfileSummaryRow], built from the same avatar size and text styles.
 *
 * @param contentDescription What is loading, announced for the whole row.
 * @param modifier Modifier applied to the row.
 */
@Composable
private fun ProfileSummaryRowPlaceholder(
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
    SlotRow(
        modifier = modifier.clearAndSetSemantics { this.contentDescription = contentDescription },
        layout = SlotRowLayout.Auto.copy(gap = ArecaTheme.spacing.lg),
        start = {
            Skeleton(
                width = AvatarSize.Medium.container,
                height = AvatarSize.Medium.container,
                size = SkeletonSize.Default.copy(shape = ArecaTheme.shapes.full),
            )
        },
        center = {
            Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xs)) {
                Skeleton(
                    textStyle = ArecaTheme.typography.labelLarge,
                    modifier = Modifier.fillMaxWidth(NameWidthFraction),
                )
                Skeleton(
                    textStyle = ArecaTheme.typography.bodySmall,
                    // A short phrase, not a line that fills the column.
                    modifier = Modifier.width(ArecaTheme.spacing.xl9),
                )
            }
        },
    )
}

/** Share of the text column the placeholder's name bar takes. */
private const val NameWidthFraction = 0.55f

/**
 * Inset of the summary row from the card's edges: a settings row's gutter at the side, and a
 * roomier step above and below, since the card holds one row rather than a list of them.
 *
 * @return The inset.
 */
@Composable
private fun cardPadding(): PaddingValues =
    PaddingValues(horizontal = ArecaTheme.spacing.xl, vertical = ArecaTheme.spacing.lg)
