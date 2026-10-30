package dev.voir.areca.foundation.overlay

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.icon.FeaturedIcon
import dev.voir.areca.foundation.icon.FeaturedIconSize
import dev.voir.areca.foundation.icon.FeaturedIconStyle
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.foundation.adaptive.AdaptiveDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.isSpecified

/**
 * Colors and shapes of a [ConfirmSheet].
 *
 * The note's colour is not here: it is what the question is about, so each sheet passes it as
 * `noteColor`.
 *
 * @property sheet Scrim, surface and drag handle of the sheet the question rises in.
 * @property icon Tile behind the glyph at the top — neutral, so the note stays the one coloured
 *   thing.
 * @property title Color of the question.
 * @property description Color of what happens if the answer is yes.
 * @property noteShape Shape of the tinted box behind the note.
 * @property confirm Look of the confirming button: the dark one every primary action uses.
 * @property cancel Look of the cancelling button.
 */
@Immutable
public data class ConfirmSheetStyle(
    public val sheet: FlowModalStyle,
    public val icon: FeaturedIconStyle,
    public val title: Color,
    public val description: Color,
    public val noteShape: Shape,
    public val confirm: ButtonStyle,
    public val cancel: ButtonStyle,
) {
    /** The theme's default [ConfirmSheetStyle], and [default] for building a variation on it. */
    public companion object {
        /** Theme default. */
        public val Default: ConfirmSheetStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme.
         *
         * @param sheet Scrim, surface and drag handle of the sheet.
         * @param icon Tile behind the glyph at the top.
         * @param title Color of the question.
         * @param description Color of the description.
         * @param noteShape Shape of the tinted box behind the note.
         * @param confirm Look of the confirming button.
         * @param cancel Look of the cancelling button.
         * @return Style built from the supplied colors and shapes.
         */
        @Composable
        public fun default(
            sheet: FlowModalStyle = LocalFlowModalStyle.current ?: FlowModalStyle.Default,
            icon: FeaturedIconStyle = FeaturedIconStyle.soft(ActionColor.Neutral),
            title: Color = ArecaTheme.colors.fgBase,
            description: Color = ArecaTheme.colors.fgSubtle,
            noteShape: Shape = RoundedCornerShape(ArecaTheme.radius.lg),
            // The dark button whatever the sheet is about. What the action costs is the note's to
            // say; the button's job is to be the obvious way forward.
            confirm: ButtonStyle = ButtonStyle.solid(ActionColor.NeutralDark, shape = CircleShape),
            cancel: ButtonStyle = ButtonStyle.soft(ActionColor.Neutral, shape = CircleShape),
        ): ConfirmSheetStyle = ConfirmSheetStyle(
            sheet = sheet,
            icon = icon,
            title = title,
            description = description,
            noteShape = noteShape,
            confirm = confirm,
            cancel = cancel,
        )
    }
}

/**
 * Metrics and type of a [ConfirmSheet].
 *
 * @property sheet Geometry of the sheet's drag handle.
 * @property contentPadding Inset between the sheet's edges and its content, for a sheet without an
 *   icon.
 * @property iconTopPadding Inset above the icon, replacing the top of [contentPadding] when there
 *   is one. A step more than the sides: the drag handle sits just above the padding, and at the
 *   same inset the icon reads as pressed up against it rather than as the head of the sheet.
 * @property icon Size of the icon tile.
 * @property iconGap Space between the icon and the title.
 * @property title Type of the question.
 * @property description Type of the description.
 * @property descriptionGap Space between the title and the description.
 * @property note Type of the note.
 * @property notePadding Inset between the note's tinted box and its text.
 * @property noteGap Space above the note.
 * @property extraGap Space above the caller's extra content.
 * @property actionsGap Space above the confirming button.
 * @property buttonGap Space between the two buttons.
 * @property button Size of both buttons.
 */
@Immutable
public data class ConfirmSheetSize(
    public val sheet: FlowModalSize,
    public val contentPadding: PaddingValues,
    public val iconTopPadding: Dp,
    public val icon: FeaturedIconSize,
    public val iconGap: Dp,
    public val title: TextStyle,
    public val description: TextStyle,
    public val descriptionGap: Dp,
    public val note: TextStyle,
    public val notePadding: PaddingValues,
    public val noteGap: Dp,
    public val extraGap: Dp,
    public val actionsGap: Dp,
    public val buttonGap: Dp,
    public val button: ButtonSize,
) {
    /** The theme's default [ConfirmSheetSize], and [default] for building a variation on it. */
    public companion object {
        /** Theme default. */
        public val Default: ConfirmSheetSize
            @Composable
            get() = default()

        /**
         * Builds a size from the current theme.
         *
         * @param sheet Geometry of the sheet's drag handle.
         * @param contentPadding Inset between the sheet's edges and its content.
         * @param iconTopPadding Inset above the icon, when there is one.
         * @param icon Size of the icon tile.
         * @param iconGap Space between the icon and the title.
         * @param title Type of the question.
         * @param description Type of the description.
         * @param descriptionGap Space between the title and the description.
         * @param note Type of the note.
         * @param notePadding Inset between the note's box and its text.
         * @param noteGap Space above the note.
         * @param extraGap Space above the extra content.
         * @param actionsGap Space above the confirming button.
         * @param buttonGap Space between the two buttons.
         * @param button Size of both buttons.
         * @return Size built from the supplied metrics.
         */
        @Composable
        public fun default(
            sheet: FlowModalSize = LocalFlowModalSize.current ?: FlowModalSize.Default,
            contentPadding: PaddingValues = PaddingValues(ArecaTheme.spacing.xl3),
            iconTopPadding: Dp = ArecaTheme.spacing.xl4,
            icon: FeaturedIconSize = FeaturedIconSize.Medium,
            iconGap: Dp = ArecaTheme.spacing.lg,
            title: TextStyle = ArecaTheme.typography.headingExtraSmall,
            description: TextStyle = ArecaTheme.typography.bodySmall,
            descriptionGap: Dp = ArecaTheme.spacing.md,
            note: TextStyle = ArecaTheme.typography.bodySmall,
            notePadding: PaddingValues = PaddingValues(ArecaTheme.spacing.lg),
            noteGap: Dp = ArecaTheme.spacing.xl,
            extraGap: Dp = ArecaTheme.spacing.xl,
            actionsGap: Dp = ArecaTheme.spacing.xl2,
            buttonGap: Dp = ArecaTheme.spacing.lg,
            button: ButtonSize = ButtonSize.Medium,
        ): ConfirmSheetSize = ConfirmSheetSize(
            sheet = sheet,
            contentPadding = contentPadding,
            iconTopPadding = iconTopPadding,
            icon = icon,
            iconGap = iconGap,
            title = title,
            description = description,
            descriptionGap = descriptionGap,
            note = note,
            notePadding = notePadding,
            noteGap = noteGap,
            extraGap = extraGap,
            actionsGap = actionsGap,
            buttonGap = buttonGap,
            button = button,
        )
    }
}

/**
 * A sheet — a `FlowModal`, rising from the bottom edge — that asks once before something hard to
 * take back. On a window from expanded up the same question is a centred [Dialog] instead
 * ([AdaptiveDefaults.usesDialog]), answered with the same buttons.
 *
 * Every confirmation is this shape, so the difference between signing out, deleting an account and
 * throwing away a half-filled form is an icon, some words and the colour of one box. A sheet rather
 * than a centred [ConfirmationDialog] because it is answered with a thumb, and it arrives from the
 * same edge as the keyboard the person was just using.
 *
 * Whether the sheet is up is the caller's state, not the sheet's: a confirmation survives the screen
 * being rebuilt and cannot get out of step with the work it confirms. The sheet's animation is
 * driven from [visible].
 *
 * **Answered once.** The buttons and every dismissal answer only while the sheet rests open, and
 * only the first answer counts: a press while the sheet is still rising or already sliding away is
 * ignored, so a double tap on Cancel cannot land on the confirming button as the sheet drops under
 * the finger. The sheet takes answers again when [visible] or [confirming] changes — the next time
 * it is shown, or once failed work is cleared and can be retried. So answer [onConfirm] by clearing
 * [visible] or by setting [confirming]: a sheet left up with neither takes no further press.
 *
 * **One coloured thing, and it is the note.** An icon, a note and a button all tinted from one
 * accent make a screen-wide surface shout one warning three times. The icon is a neutral tile, the
 * confirming button is the dark one every primary action uses, and what is genuinely at stake is
 * said once, in words, in the box that carries the colour.
 *
 * @param visible Whether the sheet is shown.
 * @param icon Glyph at the top of the sheet, or `null` for a question that needs no mark — leaving
 *   a form, say, which is neither good nor bad news.
 * @param title The question.
 * @param description What happens if the answer is yes, or `null` for the question alone.
 * @param confirmText Caption of the confirming button.
 * @param onConfirm Called when the person confirms, at most once per answer.
 * @param onDismiss Called when the sheet is dismissed any other way — Cancel, the scrim, a drag
 *   down, back — at most once per answer.
 * @param modifier Modifier applied to the sheet.
 * @param note The one thing worth a tinted box, or `null` for none.
 * @param noteColor Colour of the note and its box — the sheet's only status colour.
 * @param confirming Whether the confirmed work is in flight. Shows the spinner and refuses a second
 *   press of either button — and the scrim, a drag and Cancel stop dismissing, so the caller must
 *   clear it when the work ends, however it ends.
 * @param confirmEnabled Whether the confirming button accepts a press before anything is in flight.
 * @param cancelText Caption of the cancelling button — the answer that keeps things as they are.
 * @param style Colors of the sheet, its text and its buttons. Defaults to the theme's.
 * @param size Spacing, type and button size. Defaults to the theme's.
 * @param extra Drawn between the note and the buttons — a box to type into, say.
 */
@Composable
public fun ConfirmSheet(
    visible: Boolean,
    icon: ImageVector?,
    title: String,
    description: String?,
    confirmText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    note: String? = null,
    noteColor: Color = ArecaTheme.colors.fgSubtle,
    confirming: Boolean = false,
    confirmEnabled: Boolean = true,
    cancelText: String = "Cancel",
    style: ConfirmSheetStyle = LocalConfirmSheetStyle.current ?: ConfirmSheetStyle.Default,
    size: ConfirmSheetSize = LocalConfirmSheetSize.current ?: ConfirmSheetSize.Default,
    extra: (@Composable ColumnScope.() -> Unit)? = null,
) {
    val layoutDirection = LocalLayoutDirection.current
    val padding = size.contentPadding

    // The same question in either form: a sheet a thumb answers on a phone, a centred dialog on a
    // window wide enough that the bottom edge is far from where the person is looking.
    val body: @Composable (confirm: () -> Unit, dismiss: () -> Unit) -> Unit = { confirm, dismiss ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(
                    start = padding.calculateStartPadding(layoutDirection),
                    end = padding.calculateEndPadding(layoutDirection),
                    bottom = padding.calculateBottomPadding(),
                    top = if (icon != null) size.iconTopPadding else padding.calculateTopPadding(),
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (icon != null) {
                FeaturedIcon(
                    imageVector = icon,
                    contentDescription = null,
                    size = size.icon,
                    style = style.icon,
                )

                Spacer(Modifier.height(size.iconGap))
            }

            Text(
                text = title,
                modifier = Modifier.semantics { heading() },
                style = size.title,
                color = style.title,
                textAlign = TextAlign.Center,
            )

            if (description != null) {
                Spacer(Modifier.height(size.descriptionGap))

                Text(
                    text = description,
                    style = size.description,
                    color = style.description,
                    textAlign = TextAlign.Center,
                )
            }

            if (note != null) {
                Spacer(Modifier.height(size.noteGap))

                Text(
                    text = note,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(noteColor.copy(alpha = NoteTintAlpha), style.noteShape)
                        .padding(size.notePadding),
                    style = size.note,
                    color = noteColor,
                    textAlign = TextAlign.Center,
                )
            }

            if (extra != null) {
                Spacer(Modifier.height(size.extraGap))
                extra()
            }

            Spacer(Modifier.height(size.actionsGap))

            Button(
                modifier = Modifier.fillMaxWidth(),
                style = style.confirm,
                size = size.button,
                loading = confirming,
                enabled = confirmEnabled && !confirming,
                onClick = confirm,
            ) {
                Text(confirmText)
            }

            Spacer(Modifier.height(size.buttonGap))

            Button(
                modifier = Modifier.fillMaxWidth(),
                style = style.cancel,
                size = size.button,
                enabled = !confirming,
                onClick = dismiss,
            ) {
                Text(cancelText)
            }
        }
    }

    if (AdaptiveDefaults.usesDialog(ArecaTheme.hostWindow)) {
        // A dialog has no slide to answer during, so the gate is simply the first answer per showing.
        val answered = remember(visible, confirming) { mutableStateOf(false) }
        val answerOnce = { answer: () -> Unit ->
            if (!answered.value) {
                answered.value = true
                answer()
            }
        }
        if (visible) {
            Dialog(
                onDismissRequest = { if (!confirming) answerOnce(onDismiss) },
                modifier = modifier,
            ) {
                body({ answerOnce(onConfirm) }, { answerOnce(onDismiss) })
            }
        }
        return
    }

    val controller = rememberFlowModal(autoHeight = true)

    LaunchedEffect(visible) {
        if (visible) controller.show() else controller.hide()
    }

    // Re-armed whenever either input changes, so a sheet shown again, or one whose failed work was
    // cleared, can be answered again.
    val gate = remember(visible, confirming) { ConfirmSheetAnswerGate(isArmed = visible) }
    val confirm = { if (gate.tryAnswer(controller.state)) onConfirm() }
    val dismiss = { if (gate.tryAnswer(controller.state)) onDismiss() }

    FlowModal(
        controller = controller,
        style = style.sheet,
        // On a tablet held upright the sheet stops at a readable width rather than spanning it; a
        // phone is narrower than the cap.
        size = if (size.sheet.maxWidth.isSpecified) size.sheet else size.sheet.copy(maxWidth = AdaptiveDefaults.ReadableWidth),
        onDismissRequest = { if (!confirming) dismiss() },
        modifier = modifier,
    ) {
        body(confirm, dismiss)
    }
}

/**
 * How much of the note's colour is left when it is the background behind itself.
 *
 * Derived from the one colour so a caller cannot pass a text colour and a tint that disagree.
 */
private const val NoteTintAlpha = 0.12f

/**
 * [ConfirmSheetStyle] in scope, or null for the theme default.
 *
 * Not seeded by [dev.voir.areca.theme.ArecaThemeProvider]: the default is built on
 * [LocalFlowModalStyle], so it is resolved where each sheet is drawn and follows the flow modal
 * style in scope — an application's own, or a nested dark theme's.
 */
public val LocalConfirmSheetStyle: ProvidableCompositionLocal<ConfirmSheetStyle?> =
    compositionLocalOf { null }

/**
 * [ConfirmSheetSize] in scope, or null for the theme default.
 *
 * Not seeded by [dev.voir.areca.theme.ArecaThemeProvider]: the default is built on
 * [LocalFlowModalSize], so it is resolved where each sheet is drawn and follows the flow modal
 * size in scope — an application's own, or a nested dark theme's.
 */
public val LocalConfirmSheetSize: ProvidableCompositionLocal<ConfirmSheetSize?> =
    compositionLocalOf { null }
