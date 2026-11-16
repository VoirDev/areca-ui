package dev.voir.areca.flow.profile.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import dev.voir.areca.flow.profile.icon.BundledIcons
import dev.voir.areca.flow.profile.icon.Camera
import dev.voir.areca.flow.profile.icon.Image
import dev.voir.areca.flow.profile.icon.Trash
import dev.voir.areca.foundation.adaptive.AdaptiveDefaults
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.overlay.Dialog
import dev.voir.areca.foundation.overlay.FlowModal
import dev.voir.areca.foundation.overlay.FlowModalHeaderInsets
import dev.voir.areca.foundation.overlay.rememberFlowModal
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.topbar.CloseAction
import dev.voir.areca.foundation.topbar.TopBar
import dev.voir.areca.foundation.topbar.TopBarSize
import dev.voir.areca.foundation.topbar.TopBarStyle
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.foundation.list.ListItem
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.theme.horizontalContentInsets

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
    closeLabel: String = ArecaTheme.labels.close,
) {
    val header: @Composable () -> Unit = {
        TopBar(
            title = title,
            modifier = Modifier.fillMaxWidth(),
            size = TopBarSize.Modal,
            style = TopBarStyle.Modal,
            windowInsets = FlowModalHeaderInsets,
            trailing = { CloseAction(onClick = onDismiss, contentDescription = closeLabel) },
        )
    }

    val body: @Composable () -> Unit = {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                // The choices run edge to edge on the sheet; the subtitle keeps to its gutter.
                .padding(top = ArecaTheme.spacing.sm, bottom = ArecaTheme.spacing.xl),
        ) {
            // Under the bar rather than in it, where it may run to more than one line.
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    modifier = Modifier.padding(horizontal = ArecaTheme.spacing.horizontalContentInsets),
                    style = ArecaTheme.typography.bodySmall,
                    color = ArecaTheme.colors.fgSubtle,
                )

                if (options.isNotEmpty()) Spacer(Modifier.height(ArecaTheme.spacing.md))
            }

            // The choices as rows straight on the sheet, as every action sheet lists them: a sheet
            // is already a segment of its own and needs no block inside it.
            options.forEach { option ->
                // The danger row's own ink, the others keep the row's.
                val ink = if (option.isDestructive) ArecaTheme.colors.fgDanger else LocalContentColor.current
                ListItem(
                    title = { Text(text = option.label, color = ink) },
                    onClick = option.onClick,
                    leading = { Icon(imageVector = option.icon, contentDescription = null, tint = ink) },
                )
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
    val controller = rememberFlowModal()

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
