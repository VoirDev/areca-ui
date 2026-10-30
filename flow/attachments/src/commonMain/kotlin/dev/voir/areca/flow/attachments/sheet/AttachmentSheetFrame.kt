package dev.voir.areca.flow.attachments.sheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import dev.voir.areca.foundation.adaptive.AdaptiveDefaults
import dev.voir.areca.foundation.overlay.Dialog
import dev.voir.areca.foundation.overlay.FlowModal
import dev.voir.areca.foundation.overlay.FlowModalHeaderInsets
import dev.voir.areca.foundation.overlay.rememberFlowModal
import dev.voir.areca.foundation.topbar.TopBar
import dev.voir.areca.foundation.topbar.TopBarActionScope
import dev.voir.areca.foundation.topbar.TopBarSize
import dev.voir.areca.foundation.topbar.TopBarStyle
import dev.voir.areca.foundation.topbar.TopBarTitleAlignment
import dev.voir.areca.theme.ArecaTheme

/**
 * The frame every attachment sheet is drawn in: a modal sized to its content, a heading bar,
 * whatever the sheet asks, and an optional foot.
 *
 * One frame so the module's sheets — where a file comes from, what it is, what to do with it, what
 * to call it — read as one family. The heading is a foundation [TopBar] in its modal style, so a
 * sheet's way out sits where every other sheet puts it: a list of choices closes from [trailing].
 * A form commits from [footer] — a [dev.voir.areca.foundation.overlay.FlowModalActionBar], as on
 * every other sheet that commits a draft — and keeps its heading free of controls. The bar keeps the
 * sheet's own gutter, so its title lines up with the content under it.
 *
 * **A sheet on a phone, a dialog on a wide window.** Where [AdaptiveDefaults.usesDialog] says so
 * for the whole window — a tablet in landscape, a desktop — the same heading, content and foot open
 * as a centred foundation [Dialog] rather than rising from a bottom edge that is far from where the
 * person is looking. The heading and the foot stay put there and the content scrolls between them,
 * so a foot's commit is never pushed off a short window. Everywhere else, phones included, it is
 * the sheet it has always been.
 *
 * Shown and hidden from [visible], so the caller's state decides when it goes; dragging it away,
 * tapping the scrim and the back gesture all arrive as [onDismiss], and in the dialog the scrim,
 * Escape and the back gesture do.
 *
 * @param visible Whether the sheet is up.
 * @param title Heading of the sheet.
 * @param onDismiss Called when the sheet is dragged, tapped or navigated away.
 * @param subtitle Line under the heading, or `null` for none.
 * @param titleAlignment Where the heading sits: at the start beside a list's close control, centred
 *   over a form, whose controls are in its [footer].
 * @param leading Controls before the heading.
 * @param trailing Controls after the heading, such as the close.
 * @param footer The sheet's foot, such as a form's action bar, or `null` for none.
 * @param content What the sheet asks, under the heading.
 */
@Composable
internal fun AttachmentSheetFrame(
    visible: Boolean,
    title: String,
    onDismiss: () -> Unit,
    subtitle: String? = null,
    titleAlignment: TopBarTitleAlignment = TopBarTitleAlignment.Start,
    leading: (@Composable TopBarActionScope.() -> Unit)? = null,
    trailing: (@Composable TopBarActionScope.() -> Unit)? = null,
    footer: (@Composable () -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val header: @Composable () -> Unit = {
        TopBar(
            title = title,
            subtitle = subtitle,
            modifier = Modifier.fillMaxWidth(),
            size = TopBarSize.Modal.copy(
                padding = PaddingValues(
                    horizontal = ArecaTheme.spacing.xl3,
                    vertical = ArecaTheme.spacing.md,
                ),
            ),
            style = TopBarStyle.Modal,
            windowInsets = FlowModalHeaderInsets,
            titleAlignment = titleAlignment,
            leading = leading,
            trailing = trailing,
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
            content()
        }
    }

    // Chosen by the whole window, not the pane the sheet was opened from: a sheet raised from a
    // narrow pane of a desktop window is still a dialog.
    if (AdaptiveDefaults.usesDialog(ArecaTheme.hostWindow)) {
        if (visible) {
            Dialog(onDismissRequest = onDismiss) {
                // The dialog's own header and footer slots would let a tall body push the foot out
                // of a short window; here the body takes what is left between them and scrolls.
                Column(modifier = Modifier.fillMaxWidth()) {
                    header()

                    Column(
                        modifier = Modifier
                            .weight(1f, fill = false)
                            .verticalScroll(rememberScrollState()),
                    ) {
                        body()
                    }

                    footer?.invoke()
                }
            }
        }
        return
    }

    val controller = rememberFlowModal(autoHeight = true)

    LaunchedEffect(visible) {
        if (visible) controller.show() else controller.hide()
    }

    FlowModal(
        controller = controller,
        onDismissRequest = onDismiss,
        header = header,
        footer = footer,
        content = body,
    )
}

/**
 * The last value [value] held that was not `null`.
 *
 * A sheet driven by a nullable value — up while it is set — still has to draw that value while it
 * slides away, after the caller has already cleared it.
 *
 * The value is recorded once a composition is applied, never during one, so a composition that is
 * abandoned leaves nothing behind. A plain holder rather than a state object: it is read only in a
 * later composition, one the caller's change of [value] already caused, so there is nothing for it
 * to invalidate.
 *
 * @param value The current value.
 * @return [value], or the last non-null one before it.
 */
@Composable
internal fun <T : Any> rememberLastNonNull(value: T?): T? {
    val holder = remember { LastValue<T>() }
    SideEffect {
        if (value != null) holder.value = value
    }
    return value ?: holder.value
}

/**
 * Box the last value is kept in.
 *
 * @param T Type of the value.
 */
private class LastValue<T : Any> {
    /** The last non-null value applied, or `null` before the first. */
    var value: T? = null
}
