package dev.voir.areca.foundation.overlay

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import dev.voir.areca.foundation.topbar.CloseAction
import dev.voir.areca.foundation.topbar.TopBar
import dev.voir.areca.foundation.topbar.TopBarActionScope
import dev.voir.areca.foundation.topbar.TopBarSize
import dev.voir.areca.foundation.topbar.TopBarStyle
import dev.voir.areca.foundation.topbar.TopBarTitleAlignment
import kotlinx.coroutines.flow.first
import dev.voir.areca.theme.ArecaTheme

/**
 * A bottom sheet shown while [visible] is `true`, for a sheet whose visibility is screen or view
 * model state rather than a command.
 *
 * The same sheet as the [FlowModalController] overload: setting [visible] to `false` slides it out
 * rather than removing it, so keep it composed and drive it from state. [onDismissRequest] is called
 * when the person asks to dismiss it — back, the scrim, a drag down — and should clear the state.
 *
 * @param visible Whether the sheet is shown.
 * @param onDismissRequest Called when the person asks to dismiss the sheet; clear [visible] in it.
 * @param modifier Modifier applied to the sheet.
 * @param style Colors and shape of the scrim, sheet and drag handle.
 * @param size Geometry of the drag handle.
 * @param controller The sheet's heights and drag behaviour, from [rememberFlowModal].
 * @param partial Whether the sheet opens at its partial height, when it has one.
 * @param overlayContentDescription What pressing the scrim does, read by assistive technology.
 * @param header Content above the body, starting at the sheet's top edge; see [FlowModalTopBar].
 * @param footer Content below the body.
 * @param content Body of the sheet.
 */
@Composable
public fun FlowModal(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    style: FlowModalStyle = LocalFlowModalStyle.current ?: FlowModalStyle.Default,
    size: FlowModalSize = LocalFlowModalSize.current ?: FlowModalSize.Default,
    controller: FlowModalController = rememberFlowModal(),
    partial: Boolean = true,
    overlayContentDescription: String = ArecaTheme.labels.dismiss,
    header: (@Composable () -> Unit)? = null,
    footer: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    LaunchedEffect(controller, visible) {
        if (visible) controller.show(partial) else controller.hide()
    }
    FlowModal(
        controller = controller,
        modifier = modifier,
        style = style,
        size = size,
        onDismissRequest = onDismissRequest,
        overlayContentDescription = overlayContentDescription,
        header = header,
        footer = footer,
        content = content,
    )
}

/**
 * A bottom sheet about one thing, shown while [item] is not `null`: the transaction being edited, the
 * file being renamed.
 *
 * The sheet keeps drawing the last item it was given while it slides out, so it leaves the way it
 * came even when the item is gone at once — deleted from the list it came from, say. Never wrap the
 * sheet in `if (item != null)`, which would remove it in a frame.
 *
 * @param T Type of the item.
 * @param item What the sheet is about, or `null` for no sheet.
 * @param onDismissRequest Called when the person asks to dismiss the sheet; clear [item] in it.
 * @param modifier Modifier applied to the sheet.
 * @param style Colors and shape of the scrim, sheet and drag handle.
 * @param size Geometry of the drag handle.
 * @param controller The sheet's heights and drag behaviour, from [rememberFlowModal].
 * @param partial Whether the sheet opens at its partial height, when it has one.
 * @param overlayContentDescription What pressing the scrim does, read by assistive technology.
 * @param header Content above the body, for the item shown.
 * @param footer Content below the body, for the item shown.
 * @param content Body of the sheet, for the item shown.
 */
@Composable
public fun <T : Any> FlowModal(
    item: T?,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    style: FlowModalStyle = LocalFlowModalStyle.current ?: FlowModalStyle.Default,
    size: FlowModalSize = LocalFlowModalSize.current ?: FlowModalSize.Default,
    controller: FlowModalController = rememberFlowModal(),
    partial: Boolean = true,
    overlayContentDescription: String = ArecaTheme.labels.dismiss,
    header: (@Composable (T) -> Unit)? = null,
    footer: (@Composable (T) -> Unit)? = null,
    content: @Composable (T) -> Unit,
) {
    val shown = rememberLastNonNull(item) ?: return
    FlowModal(
        visible = item != null,
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        style = style,
        size = size,
        controller = controller,
        partial = partial,
        overlayContentDescription = overlayContentDescription,
        header = header?.let { slot -> { slot(shown) } },
        footer = footer?.let { slot -> { slot(shown) } },
        content = { content(shown) },
    )
}

/**
 * Remembers the last non-null [value], so an overlay about something can stay composed — and
 * animate out — after that thing is gone.
 *
 * @param T Type of the value.
 * @param value The current value.
 * @return [value], or the last non-null value before it; `null` only until there has been one.
 */
@Composable
public fun <T : Any> rememberLastNonNull(value: T?): T? {
    var last by remember { mutableStateOf(value) }
    if (value != null && value != last) last = value
    return value ?: last
}

/**
 * Hides the sheet and returns once it is gone: navigate after a sheet has left rather than while it
 * is still on screen. Returns at once for a sheet already hidden.
 */
public suspend fun FlowModalController.hideAndWait() {
    hide()
    snapshotFlow { state }.first { it == FlowModalState.Hidden }
}

/**
 * The header of a bottom sheet: its title centred, a close cross at the end, the sheet's top-bar
 * size and style, and the insets that run its fill under the drag handle.
 *
 * Every sheet takes its header from here, so sheets share one header without restating four
 * arguments that must agree. A sheet that answers with a Cancel button at its foot passes no
 * [onClose]: a cross and a Cancel are the same way out twice.
 *
 * @param title What the sheet is for.
 * @param modifier Modifier applied to the bar.
 * @param onClose Called when the cross is pressed; `null` draws no cross.
 * @param closeContentDescription What the cross does, read by assistive technology.
 * @param leading A control before the title — a way back in a sheet that is a step of a flow.
 * @param trailing Controls at the end, drawn instead of the cross.
 * @param bottom Content under the title inside the header — a search field pinned above a list.
 * @param style Colors of the header. Defaults to the sheet header's.
 * @param size Metrics and type of the header. Defaults to the sheet header's; pass another
 *   [TopBarSize] — `TopBarSize.Default` — for a larger title.
 * @param titleAlignment Where the title sits: centred by default, at the start for a sheet whose
 *   title reads as a heading.
 */
@Composable
public fun FlowModalTopBar(
    title: String,
    modifier: Modifier = Modifier,
    onClose: (() -> Unit)? = null,
    closeContentDescription: String = ArecaTheme.labels.close,
    leading: (@Composable TopBarActionScope.() -> Unit)? = null,
    trailing: (@Composable TopBarActionScope.() -> Unit)? = null,
    bottom: (@Composable () -> Unit)? = null,
    style: TopBarStyle = TopBarStyle.Modal,
    size: TopBarSize = TopBarSize.Modal,
    titleAlignment: TopBarTitleAlignment = TopBarTitleAlignment.Center,
) {
    TopBar(
        title = title,
        modifier = modifier,
        style = style,
        size = size,
        titleAlignment = titleAlignment,
        windowInsets = FlowModalHeaderInsets,
        leading = leading,
        trailing = trailing ?: onClose?.let { close ->
            { CloseAction(onClick = close, contentDescription = closeContentDescription) }
        },
        bottom = bottom,
    )
}
