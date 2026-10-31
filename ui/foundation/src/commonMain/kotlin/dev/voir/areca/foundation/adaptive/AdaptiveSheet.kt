package dev.voir.areca.foundation.adaptive

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.isSpecified
import dev.voir.areca.foundation.overlay.Dialog
import dev.voir.areca.foundation.overlay.FlowModal
import dev.voir.areca.foundation.overlay.FlowModalSize
import dev.voir.areca.foundation.overlay.LocalFlowModalSize
import dev.voir.areca.foundation.overlay.rememberFlowModal
import dev.voir.areca.theme.ArecaTheme

/**
 * A sheet on a phone, a dialog on a tablet in landscape or a desktop.
 *
 * Below an expanded window, or on any window too short for a dialog (a phone on its side), it is a
 * [FlowModal] rising from the bottom edge, capped at [AdaptiveDefaults.ReadableWidth] unless the
 * theme's flow modal size caps it already. From an expanded window up it is a centred [Dialog] in
 * the theme's modal style. The form follows the window, not the pane that opened it.
 *
 * Shown while [visible]; the person dismisses it with the scrim, back or a drag, which calls
 * [onDismissRequest] — set [visible] to `false` there.
 *
 * ```kotlin
 * AdaptiveSheet(
 *     visible = editing,
 *     onDismissRequest = { editing = false },
 *     header = { TopBar("New transaction", windowInsets = FlowModalHeaderInsets) },
 *     footer = { FlowModalActionBar(confirmText = "Save", onConfirm = save) },
 * ) {
 *     TransactionForm(…)
 * }
 * ```
 *
 * @param visible Whether the sheet is shown.
 * @param onDismissRequest Called when the person asks to dismiss it.
 * @param modifier Modifier applied to the sheet or the dialog.
 * @param header Content pinned to the top, such as a [dev.voir.areca.foundation.topbar.TopBar].
 * @param footer Content pinned to the bottom, such as a
 *   [dev.voir.areca.foundation.overlay.FlowModalActionBar].
 * @param content The sheet's body.
 */
@Composable
public fun AdaptiveSheet(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    header: (@Composable () -> Unit)? = null,
    footer: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    if (AdaptiveDefaults.usesDialog(ArecaTheme.hostWindow)) {
        if (visible) {
            Dialog(
                onDismissRequest = onDismissRequest,
                modifier = modifier,
                header = header,
                footer = footer,
                content = content,
            )
        }
    } else {
        // A form is as tall as it is, not a fixed share of the window.
        val controller = rememberFlowModal(allowPartial = false, autoHeight = true)
        LaunchedEffect(visible) {
            if (visible) controller.show(partial = false) else controller.hide()
        }
        val size = LocalFlowModalSize.current ?: FlowModalSize.Default
        FlowModal(
            controller = controller,
            modifier = modifier,
            size = if (size.maxWidth.isSpecified) size else size.copy(maxWidth = AdaptiveDefaults.ReadableWidth),
            onDismissRequest = onDismissRequest,
            header = header,
            footer = footer,
            content = content,
        )
    }
}
