package dev.voir.areca.foundation.overlay

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.window.DialogProperties
import dev.voir.areca.modifier.thenIf
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.tween
import androidx.compose.ui.window.Dialog as ComposeDialog

/**
 * A surface that interrupts the screen to ask something, over a scrim.
 *
 * Opened in its own platform window, so it sits above everything including system chrome, and the
 * back gesture closes it. Content that is *not* a question — a long form, a picker — belongs in a
 * [FlowModal] instead, which the user can drag away.
 *
 * Three ways out, each independently switchable: pressing the scrim, the back gesture, and a click
 * outside the window. Turn all three off for a dialog the user must answer, and give them an action
 * that closes it.
 *
 * The dialog grows and shrinks with its content rather than jumping, and is bounded by [size] so it
 * never fills a wide window edge to edge.
 *
 * There is one scrim, in [ModalStyle.overlay]: on iOS and the desktop the platform's dialog window
 * draws it, and on Android, whose window dim cannot take a colour, the dialog clears that dim and
 * draws the scrim itself.
 *
 * Use [dev.voir.areca.foundation.topbar.TopBar] for the header and [ModalDefaults.Actions] for the
 * buttons — the parts a dialog usually has.
 *
 * @param onDismissRequest Called when the user asks to close the dialog by any of the three routes.
 * @param modifier Modifier applied to the dialog surface, not to the scrim.
 * @param style Scrim, surface and border colors. Defaults to the theme's.
 * @param size Width and height limits, and outer padding. Defaults to the theme's.
 * @param dismissOnOverlayClick Whether pressing the scrim closes the dialog.
 * @param dismissOnBackPress Whether the back gesture closes the dialog.
 * @param dismissOnClickOutside Whether a click outside the dialog's window closes it.
 * @param overlayContentDescription What pressing the scrim does, read by assistive technology. Only
 *   used when [dismissOnOverlayClick] is on.
 * @param header Content above [content], normally a [dev.voir.areca.foundation.topbar.TopBar]
 *   with a title and a close control.
 * @param footer Content below [content], normally the actions.
 * @param overlay Content drawn over the whole window, above the dialog itself. For a toast host or
 *   a loading veil that must cover the dialog too. Its semantics are kept, after the dialog's own,
 *   so a toast shown there is announced and its action reachable.
 * @param content The dialog's body.
 */
@Composable
public fun Dialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    style: ModalStyle = LocalModalStyle.current ?: ModalStyle.Default,
    size: ModalSize = LocalModalSize.current ?: ModalSize.Default,
    dismissOnOverlayClick: Boolean = true,
    dismissOnBackPress: Boolean = true,
    dismissOnClickOutside: Boolean = true,
    overlayContentDescription: String = "Dismiss",
    header: (@Composable () -> Unit)? = null,
    footer: (@Composable () -> Unit)? = null,
    overlay: @Composable BoxScope.() -> Unit = {},
    content: @Composable () -> Unit,
) {
    ComposeDialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = dismissOnBackPress,
            dismissOnClickOutside = dismissOnClickOutside,
            usePlatformDefaultWidth = false,
        ).withScrim(style.overlay),
    ) {
        ClearPlatformDim()

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val contentMaxHeight = maxHeight * size.contentMaxHeightFraction

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    // One scrim: the window's where it can take the style's colour, else this one.
                    .thenIf(!platformDrawsModalScrim) { background(style.overlay) }
                    .then(
                        if (dismissOnOverlayClick) {
                            // A semantics action rather than `clickable`: the scrim must be
                            // dismissable by touch without becoming an unlabelled full-screen button
                            // that a screen reader reads out and the keyboard focuses before the
                            // dialog's own content.
                            Modifier
                                .pointerInput(onDismissRequest) {
                                    detectDismissTap { onDismissRequest() }
                                }
                                .semantics {
                                    contentDescription = overlayContentDescription
                                    onClick(label = overlayContentDescription) {
                                        onDismissRequest()
                                        true
                                    }
                                }
                        } else {
                            Modifier
                        }
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = modifier
                        .padding(size.outerPadding)
                        .widthIn(max = size.contentMaxWidth)
                        .heightIn(max = contentMaxHeight)
                        .animateContentSize(
                            animationSpec = ArecaTheme.motion.medium.tween(
                                ArecaTheme.motion.standard,
                            ),
                        )
                        .background(style.bg, style.shape)
                        .border(width = size.borderWidth, color = style.border, shape = style.shape)
                        .fillMaxWidth()
                        .clip(style.shape)
                        // Swallows presses so they never reach the scrim behind. A `clickable` here
                        // would do the same but would also announce the whole dialog body as a
                        // button and put it in the focus order.
                        .pointerInput(Unit) { detectDismissTap { } },
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        header?.invoke()
                        content()
                        footer?.invoke()
                    }
                }
            }

            // Drawn last so it covers the dialog as well as the scrim, and read after the dialog's
            // own content: a toast shown here is announced, and its action can be reached.
            Box(modifier = Modifier.fillMaxSize()) {
                overlay()
            }
        }
    }
}

/**
 * Consumes taps on this area, invoking [onTap] for each one.
 *
 * Separate from `clickable` on purpose: it adds no semantics, so a scrim or a modal body can absorb
 * presses without announcing itself as a control.
 *
 * @param onTap Called when a tap completes within this area.
 */
private suspend fun PointerInputScope.detectDismissTap(onTap: () -> Unit) {
    detectTapGestures { onTap() }
}
