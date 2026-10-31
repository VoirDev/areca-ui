package dev.voir.areca.flow.profile.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import dev.voir.areca.flow.profile.icon.BundledIcons
import dev.voir.areca.flow.profile.icon.Camera
import dev.voir.areca.flow.profile.icon.Image
import dev.voir.areca.flow.profile.icon.Trash
import dev.voir.areca.foundation.adaptive.AdaptiveDefaults
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.layout.DividerStyle
import dev.voir.areca.foundation.layout.HorizontalDivider
import dev.voir.areca.foundation.overlay.Dialog
import dev.voir.areca.foundation.overlay.FlowModal
import dev.voir.areca.foundation.overlay.FlowModalHeaderInsets
import dev.voir.areca.foundation.overlay.rememberFlowModal
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.topbar.CloseAction
import dev.voir.areca.foundation.topbar.TopBar
import dev.voir.areca.foundation.topbar.TopBarSize
import dev.voir.areca.foundation.topbar.TopBarStyle
import dev.voir.areca.indication.FadeIndication
import dev.voir.areca.modifier.MinimumTouchTarget
import dev.voir.areca.modifier.clickCursor
import dev.voir.areca.theme.ArecaTheme

/**
 * One choice on the picture sheet.
 *
 * Any action the product has — take a photo, pick from the library, choose from an avatar set,
 * import from a linked account, remove the current picture — is one of these. [AvatarSourceOptions]
 * builds the common ones with the module's wording and glyphs.
 *
 * @property label What the choice does.
 * @property icon Glyph beside the label.
 * @property onClick Called when the choice is picked. The sheet does not close itself.
 * @property isDestructive Whether the choice loses something — removing the picture — which draws it
 *   in the danger colour.
 */
@Stable
public class AvatarSourceOption(
    public val label: String,
    public val icon: ImageVector,
    public val onClick: () -> Unit,
    public val isDestructive: Boolean = false,
)

/** The picture choices most products offer, with the module's glyphs. */
public object AvatarSourceOptions {
    /**
     * Take a new photograph.
     *
     * @param label What the choice says.
     * @param onClick Called when it is picked.
     * @return The choice.
     */
    public fun camera(label: String = "Take a photo", onClick: () -> Unit): AvatarSourceOption =
        AvatarSourceOption(label, BundledIcons.Camera, onClick)

    /**
     * Pick an existing picture from the device's library.
     *
     * @param label What the choice says.
     * @param onClick Called when it is picked.
     * @return The choice.
     */
    public fun library(label: String = "Choose from library", onClick: () -> Unit): AvatarSourceOption =
        AvatarSourceOption(label, BundledIcons.Image, onClick)

    /**
     * Remove the current picture, leaving the initials. Destructive.
     *
     * @param label What the choice says.
     * @param onClick Called when it is picked.
     * @return The choice.
     */
    public fun remove(label: String = "Remove current photo", onClick: () -> Unit): AvatarSourceOption =
        AvatarSourceOption(label = label, icon = BundledIcons.Trash, onClick = onClick, isDestructive = true)
}

/**
 * Where a new profile picture comes from, and how to get rid of the old one.
 *
 * It offers [options] and nothing else, in the order given. Which choices exist is the product's:
 * a camera option that opens nothing is worse than none, so list only what the current platform can
 * complete, and offer removal only when there is a picture to remove.
 *
 * Capturing, picking, cropping and uploading are the application's. The sheet reports the choice
 * through its `onClick` and does not close itself: the caller's state decides when it goes. Its way
 * out is the close control in its heading, a foundation [TopBar] in the modal style, which keeps
 * the sheet's gutter so the title lines up with the choices under it.
 *
 * On a phone, a folded foldable or a tablet held upright it is a sheet rising from the bottom edge.
 * From an expanded window up — a tablet on its side, a desktop — it is a centred [Dialog] with the
 * same heading and choices, decided by [AdaptiveDefaults.usesDialog] on the whole window
 * ([ArecaTheme.hostWindow]), so a sheet opened from a narrow pane is a dialog too.
 *
 * @param visible Whether the sheet is shown.
 * @param options Choices to offer, in order.
 * @param onDismiss Called when the sheet is dismissed without a choice.
 * @param title Heading of the sheet.
 * @param subtitle Line under the heading, or `null` for none.
 * @param closeLabel What the close control does, read out.
 */
@Composable
public fun AvatarSourceSheet(
    visible: Boolean,
    options: List<AvatarSourceOption>,
    onDismiss: () -> Unit,
    title: String = "Change photo",
    subtitle: String? = null,
    closeLabel: String = "Close",
) {
    val header: @Composable () -> Unit = {
        TopBar(
            title = title,
            modifier = Modifier.fillMaxWidth(),
            size = TopBarSize.Modal.copy(
                padding = PaddingValues(
                    horizontal = ArecaTheme.spacing.xl3,
                    vertical = ArecaTheme.spacing.md,
                ),
            ),
            style = TopBarStyle.Modal,
            windowInsets = FlowModalHeaderInsets,
            trailing = { CloseAction(onClick = onDismiss, contentDescription = closeLabel) },
        )
    }

    val body: @Composable () -> Unit = {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(
                    start = ArecaTheme.spacing.xl3,
                    top = ArecaTheme.spacing.sm,
                    end = ArecaTheme.spacing.xl3,
                    bottom = ArecaTheme.spacing.xl3,
                ),
        ) {
            // Under the bar rather than in it, where it may run to more than one line.
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = ArecaTheme.typography.bodySmall,
                    color = ArecaTheme.colors.fgSubtle,
                )

                if (options.isNotEmpty()) Spacer(Modifier.height(ArecaTheme.spacing.lg))
            }

            // No block at all for no options, rather than an empty one.
            if (options.isNotEmpty()) {
                val radius = ArecaTheme.radius.lg
                // The block is the subtle fill, which the resting border sits level with.
                val separator = DividerStyle.default(line = ArecaTheme.colors.borderOnSubtle)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        // A block of choices set off from the sheet it sits on. Drawn in its shape
                        // rather than clipped to it, so a choice's focus ring is not cut off.
                        .background(ArecaTheme.colors.bgSubtle, RoundedCornerShape(radius)),
                ) {
                    options.forEachIndexed { index, option ->
                        if (index > 0) HorizontalDivider(style = separator)

                        SourceChoice(
                            icon = option.icon,
                            label = option.label,
                            // The danger row's own ink: the icon step, which reads as red rather
                            // than brown at a row's size.
                            content = if (option.isDestructive) {
                                ArecaTheme.colors.fgDanger
                            } else {
                                ArecaTheme.colors.fgBase
                            },
                            shape = choiceCorners(index = index, count = options.size).shape(radius),
                            onClick = option.onClick,
                        )
                    }
                }
            }
        }
    }

    if (AdaptiveDefaults.usesDialog(ArecaTheme.hostWindow)) {
        // The same heading and choices, centred: on a wide window the bottom edge is far from where
        // the person is looking. The window decides, not the pane that opened the sheet.
        if (visible) {
            Dialog(onDismissRequest = onDismiss, header = header, content = body)
        }
    } else {
        AvatarSourceFlowModal(visible = visible, onDismiss = onDismiss, header = header, body = body)
    }
}

/**
 * The sheet form of [AvatarSourceSheet]: a [FlowModal] rising from the bottom edge, as on a phone.
 *
 * @param visible Whether the sheet is shown.
 * @param onDismiss Called when the sheet is dismissed without a choice.
 * @param header The heading with its close control.
 * @param body The subtitle and the choices.
 */
@Composable
private fun AvatarSourceFlowModal(
    visible: Boolean,
    onDismiss: () -> Unit,
    header: @Composable () -> Unit,
    body: @Composable () -> Unit,
) {
    val controller = rememberFlowModal(autoHeight = true)

    LaunchedEffect(visible) {
        if (visible) controller.show() else controller.hide()
    }

    FlowModal(
        controller = controller,
        onDismissRequest = onDismiss,
        header = header,
        content = body,
    )
}

/**
 * One choice inside the sheet.
 *
 * Answers hover, press and focus with the same tint as every other pressable surface in the module,
 * inked with [content] so the destructive choice reddens rather than greys, and shows the hand cursor
 * on desktop. The tint follows [shape], so the first and last choices round the block's corners.
 *
 * @param icon Glyph on the left.
 * @param label What the choice does.
 * @param content Colour of both the glyph and the label.
 * @param shape Shape the press tint and focus ring follow.
 * @param onClick Called when it is chosen.
 */
@Composable
private fun SourceChoice(
    icon: ImageVector,
    label: String,
    content: Color,
    shape: Shape,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = MinimumTouchTarget)
            .clickCursor()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = FadeIndication.tint(shape = shape, ink = content),
                role = Role.Button,
                onClick = onClick,
            )
            .padding(
                horizontal = ArecaTheme.spacing.xl,
                vertical = ArecaTheme.spacing.lg,
            ),
        horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = content,
            size = ArecaTheme.spacing.xl2,
        )

        Text(
            text = label,
            style = ArecaTheme.typography.labelMedium,
            color = content,
        )
    }
}

/** Which corners of the block of choices one choice carries. */
internal enum class ChoiceCorners {
    /** The only choice: all four. */
    All,

    /** The first of several: the top two. */
    Top,

    /** The last of several: the bottom two. */
    Bottom,

    /** A choice between two others: none. */
    None,
}

/**
 * The corners of the block the choice at [index] carries, so its press tint stays inside the block.
 *
 * @param index Position of the choice, from 0.
 * @param count Number of choices in the block.
 * @return The corners it rounds.
 */
internal fun choiceCorners(index: Int, count: Int): ChoiceCorners = when {
    count <= 1 -> ChoiceCorners.All
    index == 0 -> ChoiceCorners.Top
    index == count - 1 -> ChoiceCorners.Bottom
    else -> ChoiceCorners.None
}

/**
 * The shape a choice with these corners is drawn in.
 *
 * @param radius Corner radius of the block of choices.
 * @return A shape rounding these corners by [radius] and leaving the others square.
 */
private fun ChoiceCorners.shape(radius: Dp): Shape = when (this) {
    ChoiceCorners.All -> RoundedCornerShape(radius)
    ChoiceCorners.Top -> RoundedCornerShape(topStart = radius, topEnd = radius)
    ChoiceCorners.Bottom -> RoundedCornerShape(bottomStart = radius, bottomEnd = radius)
    ChoiceCorners.None -> RectangleShape
}
