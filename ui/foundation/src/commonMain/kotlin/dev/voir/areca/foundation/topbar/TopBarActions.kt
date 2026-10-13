package dev.voir.areca.foundation.topbar

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.avatar.Avatar
import dev.voir.areca.foundation.avatar.AvatarSize
import dev.voir.areca.foundation.avatar.AvatarStyle
import dev.voir.areca.foundation.badge.NotificationBadge
import dev.voir.areca.foundation.badge.NotificationBadgeSize
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.button.IconButton
import dev.voir.areca.foundation.button.IconButtonStyle
import dev.voir.areca.foundation.feedback.ProgressBar
import dev.voir.areca.foundation.feedback.ProgressBarSize
import dev.voir.areca.foundation.feedback.ProgressBarStyle
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.icon.BundledIcons
import dev.voir.areca.icon.ChevronLeft
import dev.voir.areca.icon.DotsVertical
import dev.voir.areca.icon.Menu01
import dev.voir.areca.icon.XClose
import dev.voir.areca.indication.FadeIndication
import dev.voir.areca.indication.FocusOutline
import dev.voir.areca.theme.ArecaTheme

/*
 * The ready-made controls of a TopBar.
 *
 * Every one is an extension on TopBarActionScope rather than a free composable or a member of a
 * defaults object, so it can only be written inside a bar slot and can only be measured by the bar
 * it is in. That is what makes them consistent with each other: a back chevron, a notification bell
 * and a "Done" label in the same bar share one glyph size, one hit target, one press shape, one
 * content color and one distance from the screen gutter — none of which the call site can set, or
 * get wrong.
 *
 * How they are drawn is the bar's decision too, made once on TopBarStyle.actions: bare glyphs, or
 * glyphs in discs and words in capsules (see TopBarActionStyle). A control given
 * ActionColor.Neutral takes that appearance as it is. Any other family is drawn one step up the
 * emphasis ladder the appearance names, which is how a destructive or affirmative control stands
 * out without also changing size or turning into a different kind of object.
 *
 * The named verbs — SearchAction, ShareAction, DeleteAction and the rest — live in
 * TopBarCommonActions.kt.
 */

/**
 * A control holding whatever is passed to it, sized and aligned like every other in the bar.
 *
 * The escape hatch behind the named controls below. Reach for it when the bar needs a mark none of
 * them draws — a brand glyph, a stacked avatar group — and let it, rather than a hand-rolled box,
 * supply the metrics and the bar's appearance: in a bar of discs, the mark gets a disc too.
 *
 * @param onClick Called when the control is pressed. `null` makes it non-interactive.
 * @param modifier Modifier applied to the control.
 * @param color Semantic family of the control. [ActionColor.Neutral] takes the bar's control color.
 * @param enabled Whether the control is available.
 * @param loading Whether the control's work is under way. Swaps the mark for a spinner of the same
 *   size, so the bar cannot change width while it runs.
 * @param badge Count drawn over the control's top corner, such as an unread total. `null` draws
 *   none. The badge is not read by assistive technology; say the count in the mark's description.
 * @param interactionSource Source of the control's press, hover and focus interactions; pass
 *   one to observe them from outside, or `null` to let the control create its own.
 * @param content The mark.
 */
@Composable
public fun TopBarActionScope.Action(
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    color: ActionColor = ActionColor.Neutral,
    enabled: Boolean = true,
    loading: Boolean = false,
    badge: String? = null,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable BoxScope.() -> Unit,
) {
    ActionControl(
        onClick = onClick,
        modifier = modifier,
        color = color,
        enabled = enabled,
        interactionSource = interactionSource,
        loading = loading,
        badge = badge,
        content = content,
    )
}

/**
 * [Action], with a hook onto the pressable button itself.
 *
 * [modifier] lands on the wrapper that also holds the badge, which is a separate semantics node from
 * the button. State that assistive technology must read as part of the control — a toggle's on/off,
 * say — has to sit on the button, so it goes in [controlModifier].
 *
 * @param onClick Called when the control is pressed. `null` makes it non-interactive.
 * @param modifier Modifier applied to the wrapper holding the control and its badge.
 * @param controlModifier Modifier applied to the pressable button, merged into its semantics node.
 * @param color Semantic family of the control. [ActionColor.Neutral] takes the bar's control color.
 * @param enabled Whether the control is available.
 * @param loading Whether the control's work is under way.
 * @param badge Count drawn over the control's top corner. `null` draws none. Hidden from
 *   assistive technology.
 * @param interactionSource Source of the control's press, hover and focus interactions; pass
 *   one to observe them from outside, or `null` to let the control create its own.
 * @param content The mark.
 */
@Composable
internal fun TopBarActionScope.ActionControl(
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    controlModifier: Modifier = Modifier,
    color: ActionColor = ActionColor.Neutral,
    enabled: Boolean = true,
    loading: Boolean = false,
    badge: String? = null,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable BoxScope.() -> Unit,
) {
    val buttonSize = if (isContained) size.containedIconButtonSize else size.iconButtonSize
    val buttonStyle = actionStyle(color)
    val elevation = Modifier.actionElevation(style.actions)

    // The badge sits outside the button rather than in its content, because the button clips its
    // subtree to its own shape and would cut the corner off.
    Box(modifier = modifier) {
        IconButton(
            onClick = onClick,
            modifier = elevation.then(controlModifier),
            style = buttonStyle,
            size = buttonSize,
            enabled = enabled,
            interactionSource = interactionSource,
            loading = loading,
            content = content,
        )

        badge?.let {
            NotificationBadge(
                text = it,
                size = NotificationBadgeSize.Small,
                enabled = enabled,
                // Hidden from assistive technology: a bare "3" read as a node of its own says
                // nothing about what there are three of. The control's description says the count.
                modifier = Modifier.align(Alignment.TopEnd).clearAndSetSemantics {},
            )
        }
    }
}

/**
 * A control shown as a single icon: the bar's ordinary action.
 *
 * @param icon Mark to draw.
 * @param contentDescription What the control does, read by assistive technology.
 * @param onClick Called when the control is pressed.
 * @param modifier Modifier applied to the control.
 * @param color Semantic family of the control. [ActionColor.Neutral] takes the bar's control color.
 * @param enabled Whether the control is available.
 * @param loading Whether the control's work is under way.
 * @param badge Count drawn over the control's top corner. `null` draws none. The badge is not read
 *   by assistive technology; say the count in [contentDescription].
 * @param interactionSource Source of the control's press, hover and focus interactions; pass
 *   one to observe them from outside, or `null` to let the control create its own.
 */
@Composable
public fun TopBarActionScope.IconAction(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: ActionColor = ActionColor.Neutral,
    enabled: Boolean = true,
    loading: Boolean = false,
    badge: String? = null,
    interactionSource: MutableInteractionSource? = null,
) {
    Action(
        onClick = onClick,
        modifier = modifier,
        color = color,
        enabled = enabled,
        interactionSource = interactionSource,
        loading = loading,
        badge = badge,
    ) {
        Icon(imageVector = icon, contentDescription = contentDescription)
    }
}

/**
 * The conventional back control: a leading chevron pointing against the reading direction.
 *
 * Drawn from the icon set rather than the animated [dev.voir.areca.foundation.icon.Chevron] canvas,
 * which exists for a mark whose direction changes — a disclosure. A back arrow's never does, and a
 * 16dp hairline canvas beside 24dp vector icons would make the leading control look like it
 * belonged to a different component. The bundled chevron mirrors itself, so it points toward the
 * right edge in a right-to-left layout.
 *
 * @param onClick Called when the control is pressed.
 * @param modifier Modifier applied to the control.
 * @param label Word beside the chevron, such as the screen being returned to. `null` shows the
 *   chevron alone, which is what a bar with a centred title wants.
 * @param contentDescription What the control does, read by assistive technology. Ignored when
 *   [label] is set, since the label already says it.
 * @param color Semantic family of the control. [ActionColor.Neutral] takes the bar's control color.
 * @param enabled Whether the control is available.
 * @param interactionSource Source of the control's press, hover and focus interactions; pass
 *   one to observe them from outside, or `null` to let the control create its own.
 */
@Composable
public fun TopBarActionScope.BackAction(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    contentDescription: String? = "Back",
    color: ActionColor = ActionColor.Neutral,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
) {
    if (label == null) {
        IconAction(
            icon = BundledIcons.ChevronLeft,
            contentDescription = contentDescription,
            onClick = onClick,
            modifier = modifier,
            color = color,
            enabled = enabled,
            interactionSource = interactionSource,
        )
    } else {
        TextAction(
            text = label,
            onClick = onClick,
            modifier = modifier,
            color = color,
            enabled = enabled,
            interactionSource = interactionSource,
            leading = { Icon(imageVector = BundledIcons.ChevronLeft, contentDescription = null) },
        )
    }
}

/**
 * The conventional dismiss control, for a bar heading a sheet or a full-screen task.
 *
 * @param onClick Called when the control is pressed.
 * @param modifier Modifier applied to the control.
 * @param contentDescription What the control does, read by assistive technology.
 * @param color Semantic family of the control. [ActionColor.Neutral] takes the bar's control color.
 * @param enabled Whether the control is available.
 * @param interactionSource Source of the control's press, hover and focus interactions; pass
 *   one to observe them from outside, or `null` to let the control create its own.
 */
@Composable
public fun TopBarActionScope.CloseAction(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = "Close",
    color: ActionColor = ActionColor.Neutral,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
) {
    IconAction(
        icon = BundledIcons.XClose,
        contentDescription = contentDescription,
        onClick = onClick,
        modifier = modifier,
        color = color,
        enabled = enabled,
        interactionSource = interactionSource,
    )
}

/**
 * The control that opens a navigation drawer.
 *
 * @param onClick Called when the control is pressed.
 * @param modifier Modifier applied to the control.
 * @param contentDescription What the control does, read by assistive technology.
 * @param color Semantic family of the control. [ActionColor.Neutral] takes the bar's control color.
 * @param enabled Whether the control is available.
 * @param badge Count drawn over the control's top corner, for a drawer holding unread items. Not
 *   read by assistive technology; say the count in [contentDescription].
 * @param interactionSource Source of the control's press, hover and focus interactions; pass
 *   one to observe them from outside, or `null` to let the control create its own.
 */
@Composable
public fun TopBarActionScope.MenuAction(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = "Menu",
    color: ActionColor = ActionColor.Neutral,
    enabled: Boolean = true,
    badge: String? = null,
    interactionSource: MutableInteractionSource? = null,
) {
    IconAction(
        icon = BundledIcons.Menu01,
        contentDescription = contentDescription,
        onClick = onClick,
        modifier = modifier,
        color = color,
        enabled = enabled,
        interactionSource = interactionSource,
        badge = badge,
    )
}

/**
 * The control that opens everything the bar had no room for.
 *
 * A bar holds two controls a side comfortably and three at a push; past that they stop reading as
 * distinct marks. Put the rest behind this and a [dev.voir.areca.foundation.overlay.DropdownPopup].
 *
 * @param onClick Called when the control is pressed.
 * @param modifier Modifier applied to the control.
 * @param contentDescription What the control does, read by assistive technology.
 * @param color Semantic family of the control. [ActionColor.Neutral] takes the bar's control color.
 * @param enabled Whether the control is available.
 * @param interactionSource Source of the control's press, hover and focus interactions; pass
 *   one to observe them from outside, or `null` to let the control create its own.
 */
@Composable
public fun TopBarActionScope.OverflowAction(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = "More",
    color: ActionColor = ActionColor.Neutral,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
) {
    IconAction(
        icon = BundledIcons.DotsVertical,
        contentDescription = contentDescription,
        onClick = onClick,
        modifier = modifier,
        color = color,
        enabled = enabled,
        interactionSource = interactionSource,
    )
}

/**
 * A control written out as a word: "Cancel", "Done", "Edit".
 *
 * The label stays on one line — a bar control that wrapped would be a bar that changed height. In
 * a bar of discs it becomes a capsule as tall as a disc.
 *
 * Brand by default, as a bar's affirmative control; pass [ActionColor.Neutral] for the cancelling
 * half of a pair so the two do not carry equal weight — or use [DoneAction] and [CancelAction],
 * which carry that convention for you.
 *
 * @param text The label.
 * @param onClick Called when the control is pressed.
 * @param modifier Modifier applied to the control.
 * @param color Semantic family of the label. [ActionColor.Neutral] takes the bar's control color.
 * @param enabled Whether the control is available.
 * @param loading Whether the control's work is under way.
 * @param interactionSource Source of the control's press, hover and focus interactions; pass
 *   one to observe them from outside, or `null` to let the control create its own.
 * @param leading Mark before the label, drawn at the bar's icon size.
 * @param trailing Mark after the label, drawn at the bar's icon size.
 */
@Composable
public fun TopBarActionScope.TextAction(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: ActionColor = ActionColor.Brand,
    enabled: Boolean = true,
    loading: Boolean = false,
    interactionSource: MutableInteractionSource? = null,
    leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
) {
    Button(
        onClick = onClick,
        style = textActionStyle(color),
        size = if (isContained) size.containedButtonSize else size.buttonSize,
        modifier = modifier.actionElevation(style.actions),
        enabled = enabled,
        interactionSource = interactionSource,
        loading = loading,
        leading = leading,
        trailing = trailing,
    ) {
        Text(text = text, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

/**
 * A control showing who is signed in, normally opening an account menu.
 *
 * Given a wider hit target than the other controls so the face inside it stays recognisable while
 * still sitting the same distance inside its own edge — which is what keeps it aligned with the
 * icons beside it. In a bar of discs the face fills a disc instead.
 *
 * @param onClick Called when the control is pressed.
 * @param modifier Modifier applied to the control.
 * @param letters Initials shown when [content] draws nothing, such as before a photo loads.
 * @param contentDescription Whose account this is, read by assistive technology.
 * @param enabled Whether the control is available.
 * @param loading Whether the account is still loading.
 * @param interactionSource Source of the control's press, hover and focus interactions; pass
 *   one to observe them from outside, or `null` to let the control create its own.
 * @param content The photo. `null` falls back to [letters].
 */
@Composable
public fun TopBarActionScope.AvatarAction(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    letters: String? = null,
    contentDescription: String? = "Account",
    enabled: Boolean = true,
    loading: Boolean = false,
    interactionSource: MutableInteractionSource? = null,
    content: (@Composable BoxScope.() -> Unit)? = null,
) {
    val avatarSize = avatarSize()
    val buttonStyle = actionStyle(ActionColor.Neutral)

    IconButton(
        onClick = onClick,
        style = buttonStyle,
        size = if (isContained) size.containedAvatarButtonSize else size.avatarButtonSize,
        modifier = modifier.actionElevation(style.actions),
        enabled = enabled,
        interactionSource = interactionSource,
    ) {
        Avatar(
            size = avatarSize,
            style = AvatarStyle.Default,
            letters = letters,
            contentDescription = contentDescription,
            loading = loading,
            content = content,
        )
    }
}

/**
 * A hairline of progress along a [TopBar]'s bottom edge, for the bar's bottom band.
 *
 * A screen that is loading says so at its own top edge rather than in a box somewhere in the
 * middle, and putting the rule inside the bar is what keeps it there when the bar has window insets
 * or a tab strip under it.
 *
 * Pair it with a bar whose bottom band has no padding —
 * `size = TopBarSize.Default.copy(bottomPadding = PaddingValues())` — for a rule flush to the bar's
 * edge, which is the convention. Left padded, it reads as a measure of something on the screen
 * rather than of the screen itself.
 *
 * @param progress How far along the work is, `0f` through `1f`. `null` runs indeterminately, for
 *   work whose length is not known.
 * @param modifier Modifier applied to the rule.
 * @param contentDescription What is progressing, read by assistive technology.
 * @param style Track and fill colors. The track is clear by default, so the rule reads as part of
 *   the bar rather than as a component sitting in it.
 */
@Composable
public fun TopBarProgress(
    progress: Float?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    style: ProgressBarStyle = ProgressBarStyle.Default.copy(track = Color.Transparent),
) {
    ProgressBar(
        progress = progress,
        modifier = modifier.fillMaxWidth(),
        contentDescription = contentDescription,
        style = style,
        size = ProgressBarSize.Small.copy(thickness = 2.dp, shape = RectangleShape),
    )
}

/**
 * Colors for a control drawn as a mark alone.
 *
 * @param color Semantic family of the control.
 * @return A neutral control in the bar's appearance, or a colored one at the appearance's
 *   emphasis step, both in the appearance's shape.
 */
@Composable
private fun TopBarActionScope.actionStyle(color: ActionColor): IconButtonStyle {
    val actions = style.actions
    val shape = actions.shape

    if (color != ActionColor.Neutral) {
        return when (actions.emphasis) {
            TopBarActionEmphasis.Plain -> IconButtonStyle.plain(color = color, shape = shape)
            TopBarActionEmphasis.Soft -> IconButtonStyle.soft(color = color, shape = shape)
            TopBarActionEmphasis.Solid -> IconButtonStyle.solid(color = color, shape = shape)
        }
    }

    val base = IconButtonStyle.plain(color = ActionColor.Neutral, shape = shape)

    return if (actions.isContained) {
        base.copy(
            bg = actions.bg,
            bgDisabled = actions.bg,
            border = actions.border,
            borderDisabled = actions.border,
            content = actions.content,
            indication = containedIndication(actions),
        )
    } else {
        base.copy(content = actions.content)
    }
}

/**
 * Colors for a control drawn as a word.
 *
 * @param color Semantic family of the control.
 * @return A neutral control in the bar's appearance, or a colored one at the appearance's
 *   emphasis step, both in the appearance's shape.
 */
@Composable
private fun TopBarActionScope.textActionStyle(color: ActionColor): ButtonStyle {
    val actions = style.actions
    val shape = actions.shape

    if (color != ActionColor.Neutral) {
        return when (actions.emphasis) {
            TopBarActionEmphasis.Plain -> ButtonStyle.plain(color = color, shape = shape)
            TopBarActionEmphasis.Soft -> ButtonStyle.soft(color = color, shape = shape)
            TopBarActionEmphasis.Solid -> ButtonStyle.solid(color = color, shape = shape)
        }
    }

    val base = ButtonStyle.plain(color = ActionColor.Neutral, shape = shape)
        .copy(content = actions.content, extra = actions.content)

    return if (actions.isContained) {
        base.copy(
            bg = actions.bg,
            bgDisabled = actions.bg,
            border = actions.border,
            borderDisabled = actions.border,
            indication = containedIndication(actions),
        )
    } else {
        base
    }
}

/**
 * Press, hover and focus feedback for a neutral contained control.
 *
 * Inked with the control's own content color rather than a fixed dark, so a light glyph on a scrim
 * disc lightens its disc on press instead of muddying it.
 *
 * @param actions Appearance of the bar's controls.
 * @return Feedback in the appearance's shape.
 */
private fun containedIndication(actions: TopBarActionStyle): FadeIndication = FadeIndication(
    shape = actions.shape,
    hoverColor = actions.content.copy(alpha = CONTAINED_HOVER_ALPHA),
    pressedColor = actions.content.copy(alpha = CONTAINED_PRESSED_ALPHA),
    focusOutline = FocusOutline(),
)

/**
 * Lifts a contained control off the content behind the bar by the appearance's elevation.
 *
 * @param actions Appearance of the bar's controls.
 * @return Modifier drawing the shadow, or this modifier unchanged when there is none.
 */
private fun Modifier.actionElevation(actions: TopBarActionStyle): Modifier =
    if (actions.elevation > 0.dp) {
        shadow(elevation = actions.elevation, shape = actions.shape, clip = false)
    } else {
        this
    }

/** Opacity of a contained control's ink while hovered. */
private const val CONTAINED_HOVER_ALPHA = 0.06f

/** Opacity of a contained control's ink while pressed, heavier than hover. */
private const val CONTAINED_PRESSED_ALPHA = 0.12f

/**
 * Avatar metrics for a control in this bar.
 *
 * Built from the bar's tokens rather than a named [AvatarSize] step, for the same reason
 * [iconButtonSize] is: a named step would re-provide its own icon and text sizes and leave the
 * account control drawn to a scale the rest of the bar knows nothing about.
 *
 * @return Avatar metrics matching the bar's own.
 */
@Composable
private fun TopBarActionScope.avatarSize(): AvatarSize = AvatarSize(
    container = if (isContained) size.action else size.avatar,
    icon = size.icon,
    text = ArecaTheme.typography.labelSmall,
    spinner = size.spinner,
)
