# Settings screen

A settings screen: a top bar with a back control, a scrolling list of sections on cards, a toggle
that reports whether its change reached the server, a choice from a short list, and a destructive
action in a section of its own.

Needs `dev.voir.areca:foundation`, and `dev.voir.areca:icons` for the bell.

## Components

| Component | Role here |
| --- | --- |
| `BaseLayout` | The screen's frame; hands the content the bar's height as padding |
| `TopBar` with `BackAction` and `rememberTopBarScrollState` | The title bar; it draws its rule once content scrolls under it |
| `Settings` | The list: provides the settings look to its sections and rows. It does not scroll — the screen does |
| `Section` | One card of rows, with a header above and a footer below |
| `SettingsToggleRow` | A setting that applies the moment it is flipped, with a `SettingsRowStatus` |
| `SettingsSelectRow` | One of a few options, picked from a menu hanging off the row's value |
| `SettingsRow` with `SettingsRowTone.Danger` | An action row in the danger colour |
| `SettingsRowIcon` | A glyph on a tile, the leading visual every row in a section shares |

Every component is in the [catalogue](../components.md).

## Code

```kotlin
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.layout.BaseLayout
import dev.voir.areca.foundation.settings.Section
import dev.voir.areca.foundation.settings.Settings
import dev.voir.areca.foundation.settings.SettingsRow
import dev.voir.areca.foundation.settings.SettingsRowIcon
import dev.voir.areca.foundation.settings.SettingsRowStatus
import dev.voir.areca.foundation.settings.SettingsRowTone
import dev.voir.areca.foundation.settings.SettingsSelectRow
import dev.voir.areca.foundation.settings.SettingsToggleRow
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.topbar.BackAction
import dev.voir.areca.foundation.topbar.TopBar
import dev.voir.areca.foundation.topbar.rememberTopBarScrollState
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.Bell
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.horizontalContentInsets
```

```kotlin
enum class AlertSound { Chime, Bell, Silent }

@Composable
fun NotificationSettingsScreen(
    pushEnabled: Boolean,
    pushStatus: SettingsRowStatus,
    onPushEnabledChange: (Boolean) -> Unit,
    sound: AlertSound,
    onSoundChange: (AlertSound) -> Unit,
    soundLabel: (AlertSound) -> String,
    onSignOut: () -> Unit,
    onBack: () -> Unit,
) {
    val scrollState = rememberTopBarScrollState()

    BaseLayout(
        bg = ArecaTheme.colors.bgCanvas,
        contentWindowInsets = WindowInsets.safeDrawing.only(
            WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal,
        ),
        topBar = {
            TopBar(
                title = "Notifications",
                windowInsets = WindowInsets.safeDrawing.only(
                    WindowInsetsSides.Top + WindowInsetsSides.Horizontal,
                ),
                scrollState = scrollState,
                leading = { BackAction(onClick = onBack, contentDescription = "Back") },
            )
        },
    ) { padding ->
        Settings(
            modifier = Modifier
                .nestedScroll(scrollState.nestedScrollConnection)
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .horizontalContentInsets()
                .padding(vertical = ArecaTheme.spacing.xl3),
        ) {
            Section(
                header = { Text("Alerts") },
                footer = { Text("Applies to this device only.") },
            ) {
                SettingsToggleRow(
                    checked = pushEnabled,
                    onCheckedChange = onPushEnabledChange,
                    title = { Text("Push notifications") },
                    leading = { SettingsRowIcon(ArecaIcons.Bell, color = ActionColor.Brand) },
                    status = pushStatus,
                )
                SettingsSelectRow(
                    title = { Text("Sound") },
                    options = AlertSound.entries,
                    selected = sound,
                    onSelect = onSoundChange,
                    optionLabel = soundLabel,
                )
            }
            Section {
                SettingsRow(
                    title = { Text("Sign out") },
                    tone = SettingsRowTone.Danger,
                    onClick = onSignOut,
                )
            }
        }
    }
}
```

- **Status.** `SettingsRowStatus.Loading` shows a spinner on the row while a change is saved, and
  `SettingsRowStatus.Error(message)` shows the message under it. Hold the status in the screen's
  state and set it from the result of the save.
- **Scrolling.** The `nestedScroll` connection tells the top bar when content is under it. Apply the
  layout's `padding` before the gutters, so the first section clears the bar.
- **Labels.** `soundLabel` turns an option into its words; pass your localised names.

## Variations

**A danger action on the card's band.** A section's closing action goes on its `band`, a strip at the
bottom of the card in a colour of its own, rather than behind a hairline:

```kotlin
Section(
    band = {
        SettingsRow(
            title = { Text("Delete account") },
            tone = SettingsRowTone.Danger,
            onClick = onDelete,
        )
    },
    bandColor = ArecaTheme.colors.bgDangerMuted,
) {
    SettingsRow(title = { Text("Email") }, value = { Text("ada@example.com") })
}
```

**Text typed in place.** `SettingsTextFieldRow` is a field inside the card, with a counter when it has
a `maxLength`:

```kotlin
Section(header = { Text("Bio") }) {
    SettingsTextFieldRow(
        value = bio,
        onValueChange = onBioChange,
        placeholder = "A line about you",
        maxLength = 160,
        singleLine = false,
        status = bioError?.let { SettingsRowStatus.Error(it) } ?: SettingsRowStatus.Idle,
    )
}
```

**One of a few options, all visible.** `SettingsOptionRow` marks the chosen row with a tick; put the
header outside the card with `SettingsHeaderPlacement.Outside`:

```kotlin
Settings(headerPlacement = SettingsHeaderPlacement.Outside) {
    Section(header = { Text("Appearance") }) {
        listOf("System", "Light", "Dark").forEach { option ->
            SettingsOptionRow(
                selected = theme == option,
                onClick = { theme = option },
                title = { Text(option) },
            )
        }
    }
}
```

**A second action on a row.** `SettingsRowLink` in the `trailing` slot — "Verify" beside an
unconfirmed address — while the row itself stays the main action:

```kotlin
SettingsRow(
    title = { Text("Email") },
    value = { Text("ada@example.com") },
    onClick = onEmail,
    trailing = { SettingsRowLink("Verify", onClick = onVerify) },
)
```

**Not for long lists.** A section composes every row at once and has no empty or error face. A
collection that can grow without limit — every device ever signed in, every invoice — belongs in a
[list screen](list.md).

**Account screens.** `flow-profile` ships rows for a `Section` — email, linked accounts, sign out,
delete account, devices — built on these. See [flow/profile](../../flow/profile/README.md).
