package dev.voir.areca.foundation.overlay

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.foundation.LocalTextStyle
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.theme.ArecaTheme

/**
 * Metrics and type of a [ConfirmationDialog].
 *
 * @property contentPadding Inset between the dialog's edges and its content.
 * @property contentSpacing Space between the leading mark, the title, the text, any extra content,
 *   the buttons and the footer.
 * @property actionsSpacing Space between the confirming and the declining button.
 * @property title Type of the title.
 * @property text Type of the explanatory text under the title.
 * @property button Size of both buttons.
 */
@Immutable
public data class ConfirmationDialogSize(
    public val contentPadding: PaddingValues,
    public val contentSpacing: Dp,
    public val actionsSpacing: Dp,
    public val title: TextStyle,
    public val text: TextStyle,
    public val button: ButtonSize,
) {
    /**
     * The theme's default [ConfirmationDialogSize], and [default] for building a variation on it.
     */
    public companion object {
        /** Theme default. */
        public val Default: ConfirmationDialogSize
            @Composable
            get() = default()

        /**
         * Builds a size from the current theme.
         *
         * @param contentPadding Inset between the dialog's edges and its content.
         * @param contentSpacing Space between the dialog's stacked parts.
         * @param actionsSpacing Space between the two buttons.
         * @param title Type of the title.
         * @param text Type of the explanatory text.
         * @param button Size of both buttons.
         * @return Size built from the supplied metrics.
         */
        @Composable
        public fun default(
            contentPadding: PaddingValues = PaddingValues(ArecaTheme.spacing.xl),
            contentSpacing: Dp = ArecaTheme.spacing.lg,
            actionsSpacing: Dp = ArecaTheme.spacing.sm,
            title: TextStyle = ArecaTheme.typography.headingExtraSmall,
            text: TextStyle = ArecaTheme.typography.bodyMedium,
            button: ButtonSize = ButtonSize.Medium,
        ): ConfirmationDialogSize = ConfirmationDialogSize(
            contentPadding = contentPadding,
            contentSpacing = contentSpacing,
            actionsSpacing = actionsSpacing,
            title = title,
            text = text,
            button = button,
        )
    }
}

/**
 * Colors of a [ConfirmationDialog] and the look of its two buttons.
 *
 * The dialog's surface and scrim are the [ModalStyle] in scope; this style covers only what a
 * confirmation adds to a plain [Dialog].
 *
 * @property title Color of the title.
 * @property text Color of the explanatory text.
 * @property confirm Look of the confirming button — the action the dialog asks about.
 * @property decline Look of the declining button, a step quieter so the two are never mistaken.
 */
@Immutable
public data class ConfirmationDialogStyle(
    public val title: Color,
    public val text: Color,
    public val confirm: ButtonStyle,
    public val decline: ButtonStyle,
) {
    /**
     * The theme's default [ConfirmationDialogStyle], and [default] for building a variation on it.
     */
    public companion object {
        /** Theme default: a brand confirming button over a plain declining one. */
        public val Default: ConfirmationDialogStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme.
         *
         * @param title Color of the title.
         * @param text Color of the explanatory text.
         * @param confirm Look of the confirming button.
         * @param decline Look of the declining button.
         * @return Style built from the supplied colors.
         */
        @Composable
        public fun default(
            title: Color = ArecaTheme.colors.fgBase,
            text: Color = ArecaTheme.colors.fgMuted,
            confirm: ButtonStyle = ButtonStyle.solid(ActionColor.Brand),
            decline: ButtonStyle = ButtonStyle.plain(ActionColor.Neutral),
        ): ConfirmationDialogStyle = ConfirmationDialogStyle(
            title = title,
            text = text,
            confirm = confirm,
            decline = decline,
        )
    }
}

/**
 * A centred [Dialog] that asks one question and waits for the answer.
 *
 * Nothing but its own buttons closes it: the scrim, the back gesture and a press outside are all
 * off, so the person cannot dismiss the question without answering it. Both handlers are `suspend`
 * and run with the pressed button showing a spinner while the other one is disabled (see
 * [ModalDefaults.Actions]); [onDismissRequest] is called once the handler returns. A handler that
 * throws leaves the dialog open so the person can try again, and the exception reaches [onFailure].
 *
 * For a confirmation answered with a thumb on a phone, prefer [ConfirmSheet].
 *
 * ```kotlin
 * if (confirmingDelete) {
 *     ConfirmationDialog(
 *         confirmText = { Text("Delete") },
 *         onConfirm = { repository.delete(id) },
 *         onDismissRequest = { confirmingDelete = false },
 *         declineText = { Text("Cancel") },
 *         onDecline = {},
 *         title = { Text("Delete workspace?") },
 *         text = { Text("This removes every project and cannot be undone.") },
 *     )
 * }
 * ```
 *
 * @param confirmText Label of the confirming button.
 * @param onConfirm Work the confirming button does. [onDismissRequest] follows when it returns.
 * @param modifier Modifier applied to the dialog surface.
 * @param onDecline Work the declining button does, or `null` for a dialog with one button.
 *   [onDismissRequest] follows when it returns.
 * @param onDismissRequest Called after either handler returns; hide the dialog here.
 * @param onFailure Called with what [onConfirm] or [onDecline] threw. The dialog stays open, and
 *   the buttons accept a press again.
 * @param style Colors of the text and the look of the buttons. Defaults to the theme's.
 * @param size Spacing, type and button size. Defaults to the theme's.
 * @param leading Content above the title, such as a [dev.voir.areca.foundation.icon.FeaturedIcon].
 * @param title The question, drawn in [ConfirmationDialogSize.title] type.
 * @param text What answering yes means, drawn in [ConfirmationDialogSize.text] type.
 * @param extraContent Content between the text and the buttons, such as a box to type into.
 * @param declineText Label of the declining button. Drawn only together with [onDecline].
 * @param footer Content under the buttons, such as a link to more information.
 */
@Composable
public fun ConfirmationDialog(
    confirmText: @Composable () -> Unit,
    onConfirm: suspend () -> Unit,
    modifier: Modifier = Modifier,
    onDecline: (suspend () -> Unit)? = null,
    onDismissRequest: () -> Unit = {},
    onFailure: (Exception) -> Unit = {},
    style: ConfirmationDialogStyle = LocalConfirmationDialogStyle.current
        ?: ConfirmationDialogStyle.Default,
    size: ConfirmationDialogSize = LocalConfirmationDialogSize.current
        ?: ConfirmationDialogSize.Default,
    leading: (@Composable ColumnScope.() -> Unit)? = null,
    title: (@Composable () -> Unit)? = null,
    text: (@Composable () -> Unit)? = null,
    extraContent: (@Composable ColumnScope.() -> Unit)? = null,
    declineText: (@Composable () -> Unit)? = null,
    footer: (@Composable ColumnScope.() -> Unit)? = null,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        dismissOnClickOutside = false,
        dismissOnBackPress = false,
        dismissOnOverlayClick = false,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(size.contentPadding),
            verticalArrangement = Arrangement.spacedBy(size.contentSpacing),
        ) {
            leading?.invoke(this)

            CompositionLocalProvider(
                LocalContentColor provides style.title,
                LocalTextStyle provides size.title
            ) {
                title?.invoke()
            }

            CompositionLocalProvider(
                LocalContentColor provides style.text,
                LocalTextStyle provides size.text
            ) {
                text?.invoke()
            }

            extraContent?.invoke(this)

            ModalDefaults.Actions(
                confirmText = confirmText,
                onConfirm = {
                    onConfirm()
                    onDismissRequest()
                },
                declineText = declineText,
                onDecline = if (onDecline != null) {
                    {
                        onDecline()
                        onDismissRequest()
                    }
                } else null,
                onFailure = onFailure,
                gap = size.actionsSpacing,
                size = size.button,
                declineStyle = style.decline,
                confirmStyle = style.confirm,
            )

            footer?.invoke(this)
        }
    }
}

/** Default [ConfirmationDialogStyle] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalConfirmationDialogStyle: ProvidableCompositionLocal<ConfirmationDialogStyle?> =
    compositionLocalOf { null }

/** Default [ConfirmationDialogSize] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalConfirmationDialogSize: ProvidableCompositionLocal<ConfirmationDialogSize?> =
    compositionLocalOf { null }
