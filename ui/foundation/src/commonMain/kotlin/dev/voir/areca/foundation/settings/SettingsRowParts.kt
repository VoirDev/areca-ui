package dev.voir.areca.foundation.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.voir.areca.foundation.badge.LocalBadgeSize
import dev.voir.areca.foundation.badge.LocalBadgeStyle
import dev.voir.areca.foundation.feedback.Spinner
import dev.voir.areca.foundation.feedback.SpinnerStyle
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.theme.ArecaTheme

/*
 * The pieces every settings row is assembled from.
 *
 * Each public row is a ListItem, a toggle row or a check row with the same few additions: a title
 * that reports where its text starts and can carry a badge, a value slot that turns into a spinner
 * while a change is in flight, and a bottom slot that grows an error message when it fails. Keeping
 * them here is what makes a toggle row and a select row fail, load and separate identically.
 */

/**
 * Title of a settings row, followed by an optional badge, reporting its placement to [anchor].
 *
 * The title keeps the width it needs and wraps before the badge is pushed out: a badge that
 * vanishes on a long title is the one piece of the row that was trying to be noticed. The badge is
 * given the list's badge size and colors — extra small and neutral by default — so it sits on the
 * title's line without making the row taller or louder.
 *
 * @param anchor Anchor of the row the title belongs to.
 * @param title Title content.
 * @param badge Content after the title, such as a "New" [dev.voir.areca.foundation.badge.Badge].
 */
@Composable
internal fun SettingsRowTitle(
    anchor: SeparatorAnchor,
    title: @Composable () -> Unit,
    badge: (@Composable () -> Unit)?,
) {
    if (badge == null) {
        Box(modifier = Modifier.separatorText(anchor)) { title() }
        return
    }

    Row(
        modifier = Modifier.separatorText(anchor),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.sm),
    ) {
        Box(modifier = Modifier.weight(1f, fill = false)) { title() }
        CompositionLocalProvider(
            LocalBadgeSize provides SettingsTheme.sizes.badge,
            LocalBadgeStyle provides SettingsTheme.styles.badge,
            content = badge,
        )
    }
}

/**
 * Value slot of a row in [status]: the spinner while a change is in flight, the value otherwise.
 *
 * The spinner takes the value's place rather than the control's, because the value is what is
 * changing; a switch stays where the hand left it.
 *
 * @param status Status of the row.
 * @param value Value content, or `null` for a row without one.
 * @return Value slot content, or `null` when there is nothing to show.
 */
@Composable
internal fun statusValue(
    status: SettingsRowStatus,
    value: (@Composable () -> Unit)?,
): (@Composable () -> Unit)? {
    if (status != SettingsRowStatus.Loading) return value

    val spinnerSize = SettingsTheme.sizes.spinner
    val spinnerColor = SettingsTheme.styles.spinner
    return { Spinner(style = SpinnerStyle(color = spinnerColor), size = spinnerSize) }
}

/**
 * Bottom slot of a row in [status]: the caller's content, then the error message when it failed.
 *
 * @param status Status of the row.
 * @param bottom Caller's bottom content, or `null` for none.
 * @return Bottom slot content, or `null` when there is nothing to show.
 */
@Composable
internal fun statusBottom(
    status: SettingsRowStatus,
    bottom: (@Composable ColumnScope.() -> Unit)?,
): (@Composable ColumnScope.() -> Unit)? {
    if (status !is SettingsRowStatus.Error) return bottom

    val color = SettingsTheme.styles.error
    val style = SettingsTheme.sizes.statusTextStyle
    return {
        bottom?.invoke(this)
        Text(text = status.message, color = color, style = style)
    }
}

/**
 * Wraps a click handler so it does nothing while the row is loading.
 *
 * The row stays pressable rather than turning inert, so it neither flickers to its disabled
 * colors nor loses its place in the focus order while a change settles.
 *
 * @receiver Click handler to guard, or `null` for a row without one.
 * @param status Status of the row.
 * @return The guarded handler, or `null` when there is no handler.
 */
internal fun (() -> Unit)?.unlessLoading(status: SettingsRowStatus): (() -> Unit)? {
    val onClick = this ?: return null
    return { if (status != SettingsRowStatus.Loading) onClick() }
}
