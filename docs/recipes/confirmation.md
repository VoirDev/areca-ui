# Confirmation flow

A destructive action confirmed in a bottom sheet: the button opens the sheet, the sheet shows
progress while the work runs, a failure keeps the sheet open with the reason, and success closes it
and confirms with a toast.

Needs `dev.voir.areca:foundation`, and `dev.voir.areca:icons` for the sign-out glyph.

## Components

| Component | Role here |
| --- | --- |
| `BaseLayout` with an `overlay` | The screen's frame; the toast host sits in its overlay, above the content |
| `TopBar` with `BackAction` | The title bar |
| `Button` with `ButtonStyle.soft(ActionColor.Danger)` | The destructive action, as a secondary button |
| `ConfirmSheet` | The yes-or-no question, answered with a thumb |
| `rememberToastState` and `ToastHost` | The confirmation after the work succeeds |

Every component is in the [catalogue](../components.md).

## Code

```kotlin
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.feedback.ToastHost
import dev.voir.areca.foundation.feedback.rememberToastState
import dev.voir.areca.foundation.layout.BaseLayout
import dev.voir.areca.foundation.overlay.ConfirmSheet
import dev.voir.areca.foundation.topbar.BackAction
import dev.voir.areca.foundation.topbar.TopBar
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.LogOut
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.contentInsets
import kotlinx.coroutines.launch
```

```kotlin
@Composable
fun SecurityScreen(
    signOutOtherDevices: suspend () -> Boolean,
    onBack: () -> Unit,
) {
    var asking by rememberSaveable { mutableStateOf(false) }
    var working by remember { mutableStateOf(false) }
    var failed by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val toasts = rememberToastState()

    BaseLayout(
        bg = ArecaTheme.colors.bgCanvas,
        contentWindowInsets = WindowInsets.safeDrawing.only(
            WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal,
        ),
        topBar = {
            TopBar(
                title = "Security",
                windowInsets = WindowInsets.safeDrawing.only(
                    WindowInsetsSides.Top + WindowInsetsSides.Horizontal,
                ),
                leading = { BackAction(onClick = onBack, contentDescription = "Back") },
            )
        },
        overlay = { ToastHost(state = toasts) },
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).contentInsets(),
            verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl),
        ) {
            Button(
                text = "Sign out other devices",
                onClick = {
                    failed = false
                    asking = true
                },
                modifier = Modifier.fillMaxWidth(),
                style = ButtonStyle.soft(ActionColor.Danger),
            )
        }
    }

    ConfirmSheet(
        visible = asking,
        icon = ArecaIcons.LogOut,
        title = "Sign out of every other device?",
        description = "Phones, tablets and browsers on this account must sign in again.",
        confirmText = "Sign out others",
        onConfirm = {
            working = true
            scope.launch {
                val done = signOutOtherDevices()
                working = false
                if (done) {
                    asking = false
                    toasts.show(label = "Other devices signed out")
                } else {
                    failed = true
                }
            }
        },
        onDismiss = { asking = false },
        note = if (failed) "Couldn’t reach the server. Try again." else "You stay signed in here.",
        noteColor = if (failed) ArecaTheme.colors.fgDangerText else ArecaTheme.colors.fgSubtle,
        confirming = working,
        cancelText = "Cancel",
    )
}
```

- **Visibility is your state.** Call `ConfirmSheet` always and drive it with `visible`, so it
  animates in and out. Never wrap it in `if`.
- **While working.** Answer `onConfirm` by setting `confirming`. The sheet shows progress and takes
  no further press until you clear `visible` (success) or `confirming` (failure).
- **The note** under the question says what happens next, or why the last attempt failed — in
  `fgDangerText` when it is a failure.
- **The toast** says the work is done once the sheet has gone. `show` returns an id for
  `dismiss(id)`; a toast with an action needs `duration = null`.

## Variations

**A centred dialog.** `ConfirmationDialog` asks the same question in the middle of the screen, which
suits the desktop. Its handlers are `suspend`: the pressed button shows a spinner while the handler
runs, a handler that throws leaves the dialog open and passes the exception to `onFailure`, and
`onDismissRequest` follows when it returns. Only its buttons close it:

```kotlin
if (visible) {
    ConfirmationDialog(
        confirmText = { Text("Delete") },
        onConfirm = { deleteWorkspace() },
        onDismissRequest = onDone,
        onFailure = onFailure,
        declineText = { Text("Cancel") },
        onDecline = {},
        title = { Text("Delete workspace?") },
        text = { Text("This removes every project and cannot be undone.") },
    )
}
```

**Something to type or tick first.** `ConfirmSheet`'s `extra` slot holds content under the
description, and `confirmEnabled = false` keeps the confirm button off until it is satisfied.

**A whole task in a sheet.** A form, a picker or a filter is a `FlowModal` with a
`FlowModalActionBar` as its footer; see the [analytics overview](analytics.md#variations) for a
sheet with two actions.

**After the work, a whole screen.** When the outcome is the screen — an import finished, a payment
sent — use `StatusScene` with a `StatusMark` instead of a toast.

**Account actions.** `flow-profile` ships the sign-out, sign-out-others, revoke-device and
delete-account sheets, built on `ConfirmSheet`; see [flow/profile](../../flow/profile/README.md).
