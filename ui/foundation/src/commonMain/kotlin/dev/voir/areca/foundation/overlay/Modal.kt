package dev.voir.areca.foundation.overlay

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.slot.SlotRow
import dev.voir.areca.foundation.slot.SlotRowLayout
import dev.voir.areca.theme.ArecaTheme
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

/**
 * Metrics of a [Dialog] and of a modal surface generally.
 *
 * Both limits are needed, and for different reasons. [contentMaxWidth] keeps a dialog from stretching
 * across a wide window into an unreadable line length; [contentMaxHeightFraction] is a *fraction* so
 * a tall dialog still leaves the scrim visible around it, which is what tells the user there is
 * something behind it.
 *
 * @property outerPadding Space between the dialog and the window's edges.
 * @property contentMaxWidth Widest the dialog will grow.
 * @property contentMaxHeightFraction Share of the window's height the dialog may take.
 * @property borderWidth Width of the dialog's border.
 */
@Immutable
public data class ModalSize(
    public val outerPadding: PaddingValues,
    public val contentMaxWidth: Dp,
    public val contentMaxHeightFraction: Float,
    public val borderWidth: Dp,
) {
    /** The theme's default [ModalSize], and [default] for building a variation on it. */
    public companion object {
        /** Theme default. */
        public val Default: ModalSize
            @Composable
            get() = default()

        /**
         * Builds a size from the current theme.
         *
         * @param contentMaxWidth Widest the dialog will grow.
         * @param contentMaxHeightFraction Share of the window's height the dialog may take.
         * @param outerPadding Space between the dialog and the window's edges.
         * @param borderWidth Width of the dialog's border.
         * @return Size built from the supplied metrics.
         */
        @Composable
        public fun default(
            contentMaxWidth: Dp = 480.dp,
            contentMaxHeightFraction: Float = 0.8f,
            outerPadding: PaddingValues = PaddingValues(
                vertical = ArecaTheme.spacing.verticalContentInsets,
                horizontal = ArecaTheme.spacing.horizontalContentInsets
            ),
            borderWidth: Dp = 1.dp,
        ): ModalSize = ModalSize(
            contentMaxWidth = contentMaxWidth,
            contentMaxHeightFraction = contentMaxHeightFraction,
            outerPadding = outerPadding,
            borderWidth = borderWidth,
        )
    }
}

/**
 * Colors of a modal surface and the scrim behind it.
 *
 * @property overlay Scrim covering whatever the modal interrupts.
 * @property bg Fill of the modal surface.
 * @property border Border of the modal surface.
 * @property shape Shape of the surface's fill and border.
 */
@Immutable
public data class ModalStyle(
    public val overlay: Color,
    public val bg: Color,
    public val border: Color,
    public val shape: Shape,
) {
    /** The theme's default [ModalStyle], and [default] for building a variation on it. */
    public companion object {
        /** Theme default. */
        public val Default: ModalStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme.
         *
         * @param overlay Scrim covering whatever the modal interrupts.
         * @param bg Fill of the modal surface.
         * @param border Border of the modal surface.
         * @param shape Shape of the surface's fill and border.
         * @return Style built from the supplied colors and shape.
         */
        @Composable
        public fun default(
            overlay: Color = ArecaTheme.colors.bgOverlayDark,
            bg: Color = ArecaTheme.colors.bgCanvas,
            border: Color = ArecaTheme.colors.borderBase,
            shape: Shape = ArecaTheme.shapes.md,
        ): ModalStyle = ModalStyle(
            overlay = overlay,
            bg = bg,
            border = border,
            shape = shape,
        )
    }
}

/**
 * Ready-made parts of a modal: its action buttons.
 *
 * A modal's *header* is a [dev.voir.areca.foundation.topbar.TopBar] with
 * [dev.voir.areca.foundation.topbar.TopBarSize.Modal] and
 * [dev.voir.areca.foundation.topbar.TopBarStyle.Modal]:
 *
 * ```kotlin
 * Dialog(
 *     onDismissRequest = ::dismiss,
 *     header = {
 *         TopBar(
 *             title = "New expense",
 *             size = TopBarSize.Modal,
 *             style = TopBarStyle.Modal,
 *             titleAlignment = TopBarTitleAlignment.Center,
 *             trailing = { CloseAction(onClick = ::dismiss) },
 *         )
 *     },
 *     footer = {
 *         ModalDefaults.Actions(
 *             confirmText = { Text("Save") },
 *             onConfirm = { save(); dismiss() },
 *         )
 *     },
 * ) {
 *     …
 * }
 * ```
 *
 * There is deliberately no modal header here. A sheet's heading and a screen's heading are the same
 * object — a title between controls — and a second implementation would make a close cross in a
 * modal and a close cross in a bar different sizes, different distances from the edge, and
 * reachable through different names.
 */
public object ModalDefaults {
    /**
     * A modal's action buttons, stacked with the confirming one on top.
     *
     * Stacked rather than side by side: a modal is normally narrow, and two full-width buttons give
     * both a target the user cannot miss. The confirming action comes first because it is the one
     * the modal is asking about.
     *
     * Both handlers are `suspend`, and the button that is running shows a spinner while the other
     * one disables — so a slow confirmation cannot be double-submitted or cancelled halfway. A
     * handler that throws ends its spinner and hands the exception to [onFailure] instead of
     * crashing the application; cancellation still propagates.
     *
     * @param confirmText Label of the confirming action.
     * @param onConfirm Work the confirming action does. Runs on a scope tied to this composition.
     * @param modifier Modifier applied to the column holding both buttons.
     * @param confirmEnabled Whether the confirming action is available.
     * @param declineText Label of the declining action. Both it and [onDecline] are needed to draw
     *   one.
     * @param onDecline Work the declining action does.
     * @param declineEnabled Whether the declining action is available.
     * @param onFailure Called with what [onConfirm] or [onDecline] threw, after its button
     *   accepts a press again.
     * @param gap Space between the two buttons.
     * @param declineStyle Colors of the declining action.
     * @param confirmStyle Colors of the confirming action.
     * @param size Size shared by both buttons.
     */
    @Composable
    public fun Actions(
        confirmText: @Composable () -> Unit,
        onConfirm: suspend () -> Unit,
        modifier: Modifier = Modifier,
        confirmEnabled: Boolean = true,
        declineText: (@Composable () -> Unit)? = null,
        onDecline: (suspend () -> Unit)? = null,
        declineEnabled: Boolean = true,
        onFailure: (Exception) -> Unit = {},
        gap: Dp = ArecaTheme.spacing.md,
        declineStyle: ButtonStyle = ButtonStyle.plain(ActionColor.Neutral),
        confirmStyle: ButtonStyle = ButtonStyle.solid(ActionColor.Brand),
        size: ButtonSize = ButtonSize.Medium,
    ) {
        val scope = rememberCoroutineScope()

        var confirmLoading by remember { mutableStateOf(false) }
        var declineLoading by remember { mutableStateOf(false) }

        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(gap),
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    scope.launch {
                        if (confirmLoading) return@launch
                        confirmLoading = true
                        try {
                            runModalAction(onConfirm, onFailure)
                        } finally {
                            confirmLoading = false
                        }
                    }
                },
                enabled = confirmEnabled && !declineLoading,
                loading = confirmLoading,
                size = size,
                style = confirmStyle,
            ) {
                confirmText()
            }

            if (declineText != null && onDecline != null) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        scope.launch {
                            if (declineLoading) return@launch

                            declineLoading = true
                            try {
                                runModalAction(onDecline, onFailure)
                            } finally {
                                declineLoading = false
                            }
                        }
                    },
                    enabled = declineEnabled && !confirmLoading,
                    loading = declineLoading,
                    size = size,
                    style = declineStyle,
                ) {
                    declineText()
                }
            }
        }
    }
}

/**
 * Runs a modal button's [action], handing anything it throws to [onFailure].
 *
 * The action runs on a scope tied to the composition, which has no exception handler: an exception
 * left to escape would crash the application. Cancellation is not a failure — the composition went
 * away — so it is rethrown for the scope to finish quietly.
 *
 * @param action Work the button does.
 * @param onFailure Called with what [action] threw.
 * @throws CancellationException When the coroutine running [action] is cancelled.
 */
internal suspend fun runModalAction(action: suspend () -> Unit, onFailure: (Exception) -> Unit) {
    try {
        action()
    } catch (cancellation: CancellationException) {
        throw cancellation
    } catch (failure: Exception) {
        onFailure(failure)
    }
}

/** Default [ModalSize] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalModalSize: ProvidableCompositionLocal<ModalSize?> = compositionLocalOf { null }

/** Default [ModalStyle] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalModalStyle: ProvidableCompositionLocal<ModalStyle?> = compositionLocalOf { null }
