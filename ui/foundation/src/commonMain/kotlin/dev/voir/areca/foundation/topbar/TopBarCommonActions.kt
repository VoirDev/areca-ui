package dev.voir.areca.foundation.topbar

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.state.ToggleableState
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.icon.Bell
import dev.voir.areca.icon.BundledIcons
import dev.voir.areca.icon.Download
import dev.voir.areca.icon.Edit
import dev.voir.areca.icon.FilterLines
import dev.voir.areca.icon.Heart
import dev.voir.areca.icon.HeartFill
import dev.voir.areca.icon.Plus
import dev.voir.areca.icon.Search
import dev.voir.areca.icon.Settings
import dev.voir.areca.icon.Share
import dev.voir.areca.icon.Trash

/*
 * The verbs a bar is asked for again and again, each with its glyph, its words and its color
 * settled once.
 *
 * Without them every screen picks its own: one draws "share" as an arrow out of a box and the next
 * as three connected dots, one colors "delete" red and the next leaves it grey. A named control
 * removes the choice — it is still an IconAction or a TextAction underneath, sized and drawn by the
 * bar it is in, and IconAction and Action remain for any verb not listed here.
 */

/**
 * The control that opens search.
 *
 * @param onClick Called when the control is pressed.
 * @param modifier Modifier applied to the control.
 * @param contentDescription What the control does, read by assistive technology.
 * @param enabled Whether the control is available.
 * @param interactionSource Source of the control's press, hover and focus interactions; pass
 *   one to observe them from outside, or `null` to let the control create its own.
 */
@Composable
public fun TopBarActionScope.SearchAction(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = "Search",
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
) {
    IconAction(
        icon = BundledIcons.Search,
        contentDescription = contentDescription,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
    )
}

/**
 * The control that hands what is on screen to another application.
 *
 * @param onClick Called when the control is pressed.
 * @param modifier Modifier applied to the control.
 * @param contentDescription What the control does, read by assistive technology.
 * @param enabled Whether the control is available.
 * @param loading Whether the share is being prepared, such as a file still downloading.
 * @param interactionSource Source of the control's press, hover and focus interactions; pass
 *   one to observe them from outside, or `null` to let the control create its own.
 */
@Composable
public fun TopBarActionScope.ShareAction(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = "Share",
    enabled: Boolean = true,
    loading: Boolean = false,
    interactionSource: MutableInteractionSource? = null,
) {
    IconAction(
        icon = BundledIcons.Share,
        contentDescription = contentDescription,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        loading = loading,
    )
}

/**
 * The control that opens the notification list, with the unread count over it.
 *
 * @param onClick Called when the control is pressed.
 * @param modifier Modifier applied to the control.
 * @param badge Unread count drawn over the control's corner, already formatted ("3", "99+").
 *   `null` draws none.
 * @param contentDescription What the control does, read by assistive technology. Say the count in
 *   it when there is one; the badge itself is not read.
 * @param enabled Whether the control is available.
 * @param interactionSource Source of the control's press, hover and focus interactions; pass
 *   one to observe them from outside, or `null` to let the control create its own.
 */
@Composable
public fun TopBarActionScope.NotificationsAction(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    badge: String? = null,
    contentDescription: String? = "Notifications",
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
) {
    IconAction(
        icon = BundledIcons.Bell,
        contentDescription = contentDescription,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        badge = badge,
    )
}

/**
 * The control that opens what is on screen for editing.
 *
 * Drawn as a glyph. A screen whose one action is editing reads better with the word — pass
 * `TextAction("Edit", …)` there instead.
 *
 * @param onClick Called when the control is pressed.
 * @param modifier Modifier applied to the control.
 * @param contentDescription What the control does, read by assistive technology.
 * @param enabled Whether the control is available.
 * @param interactionSource Source of the control's press, hover and focus interactions; pass
 *   one to observe them from outside, or `null` to let the control create its own.
 */
@Composable
public fun TopBarActionScope.EditAction(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = "Edit",
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
) {
    IconAction(
        icon = BundledIcons.Edit,
        contentDescription = contentDescription,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
    )
}

/**
 * The control that deletes what is on screen, or what is selected.
 *
 * Danger-colored by default, so it is never mistaken for the neutral controls beside it. It only
 * asks: confirming is the application's sheet or dialog, not the bar's.
 *
 * @param onClick Called when the control is pressed.
 * @param modifier Modifier applied to the control.
 * @param contentDescription What the control does, read by assistive technology.
 * @param color Semantic family of the control.
 * @param enabled Whether the control is available.
 * @param loading Whether the deletion is under way.
 * @param interactionSource Source of the control's press, hover and focus interactions; pass
 *   one to observe them from outside, or `null` to let the control create its own.
 */
@Composable
public fun TopBarActionScope.DeleteAction(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = "Delete",
    color: ActionColor = ActionColor.Danger,
    enabled: Boolean = true,
    loading: Boolean = false,
    interactionSource: MutableInteractionSource? = null,
) {
    IconAction(
        icon = BundledIcons.Trash,
        contentDescription = contentDescription,
        onClick = onClick,
        modifier = modifier,
        color = color,
        enabled = enabled,
        interactionSource = interactionSource,
        loading = loading,
    )
}

/**
 * The control that creates a new item of whatever the screen lists.
 *
 * @param onClick Called when the control is pressed.
 * @param modifier Modifier applied to the control.
 * @param contentDescription What the control creates, read by assistive technology — "New
 *   expense" says more than "Add".
 * @param color Semantic family of the control. Neutral by default; pass
 *   [ActionColor.Brand] when creating is the screen's main action.
 * @param enabled Whether the control is available.
 * @param interactionSource Source of the control's press, hover and focus interactions; pass
 *   one to observe them from outside, or `null` to let the control create its own.
 */
@Composable
public fun TopBarActionScope.AddAction(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = "Add",
    color: ActionColor = ActionColor.Neutral,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
) {
    IconAction(
        icon = BundledIcons.Plus,
        contentDescription = contentDescription,
        onClick = onClick,
        modifier = modifier,
        color = color,
        enabled = enabled,
        interactionSource = interactionSource,
    )
}

/**
 * The control that opens a list's filters, with the number of filters in force over it.
 *
 * @param onClick Called when the control is pressed.
 * @param modifier Modifier applied to the control.
 * @param badge Number of filters applied, already formatted. `null` draws none, which is what a
 *   list showing everything wants.
 * @param contentDescription What the control does, read by assistive technology. Say the number
 *   of filters in it when there are any; the badge itself is not read.
 * @param enabled Whether the control is available.
 * @param interactionSource Source of the control's press, hover and focus interactions; pass
 *   one to observe them from outside, or `null` to let the control create its own.
 */
@Composable
public fun TopBarActionScope.FilterAction(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    badge: String? = null,
    contentDescription: String? = "Filters",
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
) {
    IconAction(
        icon = BundledIcons.FilterLines,
        contentDescription = contentDescription,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        badge = badge,
    )
}

/**
 * The control that opens settings.
 *
 * @param onClick Called when the control is pressed.
 * @param modifier Modifier applied to the control.
 * @param contentDescription What the control does, read by assistive technology.
 * @param enabled Whether the control is available.
 * @param interactionSource Source of the control's press, hover and focus interactions; pass
 *   one to observe them from outside, or `null` to let the control create its own.
 */
@Composable
public fun TopBarActionScope.SettingsAction(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = "Settings",
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
) {
    IconAction(
        icon = BundledIcons.Settings,
        contentDescription = contentDescription,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
    )
}

/**
 * The control that saves what is on screen to the device.
 *
 * @param onClick Called when the control is pressed.
 * @param modifier Modifier applied to the control.
 * @param contentDescription What the control does, read by assistive technology.
 * @param enabled Whether the control is available.
 * @param loading Whether the download is under way.
 * @param interactionSource Source of the control's press, hover and focus interactions; pass
 *   one to observe them from outside, or `null` to let the control create its own.
 */
@Composable
public fun TopBarActionScope.DownloadAction(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = "Download",
    enabled: Boolean = true,
    loading: Boolean = false,
    interactionSource: MutableInteractionSource? = null,
) {
    IconAction(
        icon = BundledIcons.Download,
        contentDescription = contentDescription,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        loading = loading,
    )
}

/**
 * The control that marks what is on screen as a favorite, and unmarks it.
 *
 * An outline heart while off and a filled one in [selectedColor] while on, so the state reads from
 * the glyph and the color together rather than from color alone.
 *
 * @param selected Whether it is currently a favorite.
 * @param onClick Called when the control is pressed. Flip [selected] in response.
 * @param modifier Modifier applied to the control.
 * @param contentDescription What the control does, read by assistive technology. Whether it
 *   is on is announced with it, as the control's toggle state.
 * @param selectedColor Semantic family of the control while [selected].
 * @param enabled Whether the control is available.
 * @param interactionSource Source of the control's press, hover and focus interactions; pass
 *   one to observe them from outside, or `null` to let the control create its own.
 */
@Composable
public fun TopBarActionScope.FavoriteAction(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = "Favorite",
    selectedColor: ActionColor = ActionColor.Danger,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
) {
    ActionControl(
        onClick = onClick,
        modifier = modifier,
        controlModifier = Modifier.semantics { toggleableState = ToggleableState(selected) },
        color = if (selected) selectedColor else ActionColor.Neutral,
        enabled = enabled,
        interactionSource = interactionSource,
    ) {
        Icon(
            imageVector = if (selected) BundledIcons.HeartFill else BundledIcons.Heart,
            contentDescription = contentDescription,
        )
    }
}

/**
 * The worded control that finishes a task: "Done", "Save", "Send".
 *
 * Brand-colored, as the affirmative half of a [CancelAction] pair. In a bar of discs it is the one
 * filled capsule, which is the whole reason to prefer it over a check-mark glyph: the word says
 * what finishing does.
 *
 * @param onClick Called when the control is pressed.
 * @param modifier Modifier applied to the control.
 * @param text The label.
 * @param enabled Whether the control is available — false while the task cannot yet be finished.
 * @param loading Whether finishing is under way. Keeps the control's width while it runs.
 * @param interactionSource Source of the control's press, hover and focus interactions; pass
 *   one to observe them from outside, or `null` to let the control create its own.
 */
@Composable
public fun TopBarActionScope.DoneAction(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String = "Done",
    enabled: Boolean = true,
    loading: Boolean = false,
    interactionSource: MutableInteractionSource? = null,
) {
    TextAction(
        text = text,
        onClick = onClick,
        modifier = modifier,
        color = ActionColor.Brand,
        enabled = enabled,
        interactionSource = interactionSource,
        loading = loading,
    )
}

/**
 * The worded control that abandons a task: "Cancel", "Discard".
 *
 * Neutral, so it never carries the same weight as the [DoneAction] opposite it. A task that can be
 * left without losing anything wants [CloseAction] instead.
 *
 * @param onClick Called when the control is pressed.
 * @param modifier Modifier applied to the control.
 * @param text The label.
 * @param enabled Whether the control is available.
 * @param interactionSource Source of the control's press, hover and focus interactions; pass
 *   one to observe them from outside, or `null` to let the control create its own.
 */
@Composable
public fun TopBarActionScope.CancelAction(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String = "Cancel",
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
) {
    TextAction(
        text = text,
        onClick = onClick,
        modifier = modifier,
        color = ActionColor.Neutral,
        enabled = enabled,
        interactionSource = interactionSource,
    )
}
