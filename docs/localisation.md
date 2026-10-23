# Localisation

Areca ships no string resources. Every word a component draws or speaks is a parameter, and each one
the component cannot do without has a plain English default, so a screen works before it is
translated. A localised application passes its own strings — from its Compose resources, in its
language — for every one of them.

This page lists those defaults, module by module, and how to replace each.

## The rules

- **Two kinds of parameter.** A single word is a `String` parameter of the component that shows it:
  `BackAction(contentDescription = "Back")`. Where a component speaks several, they are gathered in a
  labels object with an `English` value: `ContentListLabels`, `CalendarLabels`, `CropToolbarLabels`.
- **Words that format a value are functions**, so the translation decides word order and the locale
  decides how a number or a date reads: `CalendarLabels.dayDescription`,
  `BarChart(valueFormatter = …)`, `UsernameCheckFooter(availableText = …)`.
- **Optional words default to `null`** and draw nothing: placeholders, subtitles, hints. A component
  never invents copy.
- **Keep labels objects as top-level values**, or `remember` them. A labels object built with new
  lambdas on every recomposition compares unequal and rebuilds what depends on it.
- **Content you pass is already yours.** Titles, row text, error messages and device names arrive
  worded; only the defaults below are Areca's.

The snippets use literal strings to stay short. In an application, each is a `stringResource(…)`
call or a value built from your resources:

```kotlin
BackAction(onClick = onBack, contentDescription = stringResource(Res.string.back))
```

## foundation

### Single strings

| Component | Parameter | Default |
| --- | --- | --- |
| `Button`, `IconButton` | `loadingStateDescription` | `"Busy"` |
| `PasswordField` | `revealContentDescription`, `hideContentDescription` | `"Show password"`, `"Hide password"` |
| `SearchField` | `searchContentDescription`, `clearContentDescription` | `"Search"`, `"Clear search"` |
| `FieldGroup` | `requiredText`, `requiredDescription` | `"*"`, `"required"` |
| `SelectFlowModal` | `searchPlaceholder` | `"Search"` |
| `SensitiveText` | `concealedDescription` | `"Hidden"` |
| `ConfirmSheet` | `cancelText` | `"Cancel"` |
| `Dialog`, `FlowModal` | `overlayContentDescription` (the scrim) | `"Dismiss"` |
| `WheelPickerColumn` | `valueDescription` | `toString()` of the item |
| Top bar controls | `contentDescription` | `BackAction` `"Back"`, `CloseAction` `"Close"`, `MenuAction` `"Menu"`, `OverflowAction` `"More"`, `AvatarAction` `"Account"`, `SearchAction` `"Search"`, `ShareAction` `"Share"`, `NotificationsAction` `"Notifications"`, `EditAction` `"Edit"`, `DeleteAction` `"Delete"`, `AddAction` `"Add"`, `FilterAction` `"Filters"`, `SettingsAction` `"Settings"`, `DownloadAction` `"Download"`, `FavoriteAction` `"Favorite"` |
| Top bar text controls | `text` | `DoneAction` `"Done"`, `CancelAction` `"Cancel"` |

```kotlin
TopBar(
    "Einstellungen",
    leading = { BackAction(onClick = onBack, contentDescription = "Zurück") },
    trailing = { DoneAction(onClick = onDone, text = "Fertig") },
)

PasswordField(
    password,
    onValueChange = { password = it },
    revealContentDescription = "Passwort anzeigen",
    hideContentDescription = "Passwort verbergen",
)
```

### Labels objects

| Labels | Package | Used by | Holds (English) |
| --- | --- | --- | --- |
| `ContentListLabels` | `foundation.collection` | `ContentList`, `PagedContentList` and their grouped forms (`labels`) | `emptyTitle` "Nothing here yet", `errorTitle` "Something went wrong", `retry` "Try again", `loadMoreErrorTitle` "Couldn’t load more items", `endReached` "You reached the end" |
| `SelectFieldLabels` | `foundation.field.select` | `SelectField` (`labels`) | `expanded` "Expanded", `collapsed` "Collapsed" |
| `RequirementListLabels` | `foundation.feedback` | `RequirementList` (`labels`) | `met` "met", `notMet` "not met" |
| `NumberStepperLabels` | `foundation.selection` | `NumberStepper` (`labels`) | `decrease` "Decrease", `increase` "Increase" |

```kotlin
val GermanContentListLabels = ContentListLabels(
    emptyTitle = "Noch nichts hier",
    errorTitle = "Etwas ist schiefgelaufen",
    retry = "Erneut versuchen",
    loadMoreErrorTitle = "Weitere Einträge konnten nicht geladen werden",
    endReached = "Das war alles",
)

RequirementList(
    requirements,
    labels = RequirementListLabels(met = "erfüllt", notMet = "nicht erfüllt"),
    contentDescription = "Passwortanforderungen",
)

NumberStepper(
    guests,
    onValueChange = { guests = it },
    minValue = 1,
    maxValue = 12,
    labels = NumberStepperLabels(decrease = "Weniger Gäste", increase = "Mehr Gäste"),
)
```

The `ContentListDefaults` faces (`EmptyState`, `ErrorState`, …) take the same words as `title` and
`actionText` when you compose them yourself.

## charts

Charts draw your labels and no words of their own. What they generate is the description assistive
technology reads, and the numbers in it:

| Chart | Parameter | Default |
| --- | --- | --- |
| `BarChart` | `valueFormatter` | `BarChartBar.formatValue`: `42`, `42.5` — no grouping, `.` as the decimal mark |
| `StackedBarChart` | `fractionFormatter` | A whole percentage, `52%` |
| Every chart | `contentDescription` | Generated from the data, joined with English punctuation |

```kotlin
BarChart(
    groups = revenue,
    valueFormatter = { value -> "${value.roundToInt()} Tausend Euro" },
)
```

Pass `contentDescription` when the generated sentence does not suit the language, and on every
`ColumnChartColumn`. See [ui/charts](../ui/charts/README.md).

## datetime

Two kinds of thing vary by locale, and the module takes them separately.

**Conventions — `DateTimeLocale`.** The first day of the week and whether clocks count to twelve or
twenty-four. `DateTimeLocale.Current` reads them from the platform; components take them from it
unless given `startDayOfWeek`, `format` or `timeFormat`. Provide `LocalDateTimeLocale` to pin a
subtree — a screenshot test, an application with its own language setting. `DateTimeLocale.Iso` is
Monday weeks and a 24-hour clock:

```kotlin
CompositionLocalProvider(LocalDateTimeLocale provides DateTimeLocale.Iso) {
    BookingScreen()
}
```

**Words — the labels objects**, each with an `English` value:

| Labels | Package | Used by | Holds |
| --- | --- | --- | --- |
| `CalendarLabels` | `datetime.calendar` | `Calendar`, `DateRangeCalendar`, `MonthYearPicker`, `MonthGrid`, `DateTile` (`labels`); `DateTimeField` (`calendarLabels`) | Weekday headings and spoken names, the month heading, month names, the spoken day ("Tuesday 15 September 2026"), the control names ("Previous month", "Today", …), the digits of days and years. `EnglishNarrow` draws one-letter weekdays |
| `DateRangeLabels` | `datetime.calendar.range` | `DateRangeCalendar` (`rangeLabels`) | What a day's place in the range adds ("start of range", "in range", "end of range") and a range's length ("3 nights") |
| `TimePickerLabels` | `datetime` | `TimePicker` (`labels`), `DateTimeField` (`timeLabels`) | The wheels' names ("Hour", "Minute", "AM or PM"), the `am` and `pm` rows, how a twelve-hour hour is read with its period |
| `DateTimeFieldLabels` | `datetime` | `DateTimeField` (`labels`) | The halves' names ("Date", "Time"), their expanded and collapsed states, the placeholders ("Pick a date", "--:--") |

```kotlin
val GermanMonths = listOf(
    "Januar", "Februar", "März", "April", "Mai", "Juni",
    "Juli", "August", "September", "Oktober", "November", "Dezember",
)
val GermanWeekdays = listOf(
    "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag",
)

val GermanCalendarLabels = CalendarLabels(
    weekday = { day -> GermanWeekdays[day.ordinal].take(2) },
    monthHeader = { month -> "${GermanMonths[month.month.ordinal]} ${month.year}" },
    pickerMonth = { month -> GermanMonths[month.ordinal] },
    shortMonth = { month -> GermanMonths[month.ordinal].take(3) },
    dayDescription = { date ->
        val weekday = GermanWeekdays[date.dayOfWeek.ordinal]
        "$weekday, ${date.day}. ${GermanMonths[date.month.ordinal]} ${date.year}"
    },
    monthPickerAction = "Monat und Jahr wählen",
    monthPickerMonth = "Monat",
    monthPickerYear = "Jahr",
    previousMonthAction = "Vorheriger Monat",
    nextMonthAction = "Nächster Monat",
    todayAction = "Heute",
    monthPickerExpanded = "Ausgeklappt",
    monthPickerCollapsed = "Eingeklappt",
    weekdayName = { day -> GermanWeekdays[day.ordinal] },
)

val GermanTimePickerLabels = TimePickerLabels(hour = "Stunde", minute = "Minute", period = "Tageszeit")
```

Some spoken text is still joined in English order: the month control, the field's halves and a range
day join their parts with `", "`, and the hour and minute wheels draw and read Western digits. See
[ui/datetime](../ui/datetime/README.md).

## crop

Every word the crop toolbar draws or reads aloud is a property of `CropToolbarLabels`, each with an
English default. Pass the same value as `labels` to both `CropToolbar` and `ImageCropper`; the
screen's title is their `title` parameter.

| Group | Properties (English) |
| --- | --- |
| Options | `reset` "Reset", `angle` "Angle", `zoom` "Zoom", `format` "Format", `mirror` "Mirror", `rotate` "Rotate" |
| The bar | `cancel` "Cancel", `done` "Done", `undo` "Undo", `ok` "OK" |
| Tool actions | `level` "Level", `fit` "Fit", `swapOrientation` "Swap" |
| The discard question | `discardTitle` "Discard changes?", `discardDescription`, `discard` "Discard", `keepEditing` "Keep editing" |
| Ratio chips | `free` "Free", `original` "Original" |
| Assistive-technology actions | `moveLeft`, `moveRight`, `moveUp`, `moveDown` ("Move photo left", …), `zoomIn`, `zoomOut`, `widenFrame` "Make frame wider", `heightenFrame` "Make frame taller" |
| Values | `angleValue` (`+3.2°`), `zoomValue` (`2.5×`), `rotationValue` (`90°`) |

```kotlin
val GermanCropLabels = CropToolbarLabels(
    angle = "Winkel",
    zoomValue = { zoom -> "${(zoom * 10).roundToInt() / 10f}-fach".replace('.', ',') },
)
```

See [ui/crop](../ui/crop/README.md).

## keyboard

Keys show glyphs that need no translation. What assistive technology reads is a word:

| Where | Parameter | Default |
| --- | --- | --- |
| `KeyboardGridLabels`, the `labels` of every `KeyboardGrids` preset | `backspace`, `clear`, `hide`, `done` | `"Backspace"`, `"Clear"`, `"Hide keyboard"`, `"Done"` |
| `KeyboardHost` | `paneTitle`, announced when the keyboard appears; `null` announces nothing | `"Keyboard"` |
| `KeyboardScrim.default(…)` | `dismissLabel`, for the tint that closes the keyboard | `"Close keyboard"` |

```kotlin
KeyboardGrids.calculator(
    onAction = ::onKeyAction,
    labels = KeyboardGridLabels(
        backspace = "Rücktaste",
        clear = "Löschen",
        hide = "Tastatur ausblenden",
        done = "Fertig",
    ),
)
```

See [ui/keyboard](../ui/keyboard/README.md).

## flow-auth

Every word a part draws is a parameter of that part.

| Part | Parameter | Default |
| --- | --- | --- |
| `AuthNameField` | `label` | `"Full name"` |
| `AuthEmailField` | `label` | `"Email"` |
| `AuthPasswordField` | `label`, `revealDescription`, `hideDescription` | `"Password"`, `"Show password"`, `"Hide password"` |
| `AuthCodeField` | `label` | `"Verification code"` |
| `AuthIdentityChip` | `changeLabel`, `verifiedDescription` | `"Change"`, `"Verified"` |
| `AuthResendCode` | `actionText` | `"Send a new code"` |
| `LegalConsent` | `sentence`, `termsLabel`, `privacyLabel` | `"By continuing, you agree to our %1$s and %2$s."`, `"Terms of Service"`, `"Privacy Policy"` |
| `SsoGroup` | `providerLabel` | `"Continue with"` and the provider's name |
| `SsoButton`, `SsoIconButton` | `label`, `contentDescription` | The same |
| `AuthBanner` | `contentDescription`, a function of the title and the message | `"<title>. <message>"` |

Placeholders, the rule above the providers (`dividerText`) and the chip's action label default to
`null`. Errors under the fields are always yours: a field takes `error: String?`, worded.

The legal line is one sentence with `%1$s` for the terms link and `%2$s` for the privacy link, so a
translation can order them; a sentence missing either placeholder is refused.

```kotlin
AuthEmailField(value = email, onValueChange = onEmailChange, label = "E-Mail", placeholder = "du@beispiel.de")

SsoGroup(
    providers = providers,
    onProviderClick = onProviderClick,
    dividerText = "oder weiter mit",
    providerLabel = { provider -> "Weiter mit ${provider.name}" },
)

LegalConsent(
    links = legalLinks,
    onLinkClick = ::openDocument,
    sentence = "Mit dem Fortfahren stimmst du unseren %1\$s und der %2\$s zu.",
    termsLabel = "Nutzungsbedingungen",
    privacyLabel = "Datenschutzerklärung",
)
```

See [flow/auth](../flow/auth/README.md).

## flow-profile

| Part | Parameters with an English default |
| --- | --- |
| `BirthdayRow` | `label` "Birthday", `placeholder` "Add birthday", `sheetTitle` "Choose your birthday", `applyLabel` "Save", `cancelLabel` "Cancel", `removeLabel` "Remove", `formatDate` (English dates), `calendarLabels` (`CalendarLabels.English`) |
| `SignOutRow`, `DeleteAccountRow`, `AddAccountRow` | `title` "Sign out", "Delete account", "Add account" |
| `EmailRow` | `label` "Email", `placeholder` "Add email", `unverifiedBadge` "Unverified", `verifyLabel` "Verify" |
| `LinkedAccountRow` | `linkLabel` "Link", `unlinkLabel` "Unlink" |
| `AccountRow` | `currentDescription` "Current account" |
| `DataExportRow` | `title` "Download your data", `requestLabel` "Request", `downloadLabel` "Download", `retryLabel` "Try again" |
| `UsernameCheckFooter` | `checkingText` "Checking username…", `availableText` "@name is available.", `takenText` "@name is already taken." |
| `StaleDetailsNotice` | `title` "Showing saved details" |
| `ProfileLoadFailure` | `title` "Couldn't load your account", `retryLabel` "Try again" |
| `ProfileSummaryCardPlaceholder`, `ProfileGreetingPlaceholder` | `contentDescription` "Loading account" |
| `AvatarUploadStatus` | `uploadingTitle` "Uploading photo", `failedTitle` "Photo not updated", `retryLabel` "Try again", `dismissLabel` "Dismiss", `formatProgress` "40%" |
| `EditableAvatar` | `changePhotoLabel` "Change photo" |
| `ProfileHeaderCard`, `ProfileCoverCard` | `verifiedDescription` "Verified" |
| `SignOutSheet` | `title` "Sign out?", `confirmText` "Sign out", `cancelText` "Cancel" |
| `SignOutOthersSheet` | `title` "Sign out all other devices?", `confirmText` "Sign out other devices", `cancelText` "Cancel" |
| `RevokeSessionSheet` | `title` "Sign out this device?", `confirmText` "Sign out device", `cancelText` "Cancel", `description` (a function of the session) |
| `DeleteAccountSheet` | `title` "Delete this account?", `confirmText` "Delete account", `cancelText` "Cancel", `confirmationPrompt` "Type DELETE to confirm." |
| `ActiveSessionsSections` | `thisDeviceHeader` "This device", `activeSessionsHeader` "Active sessions", `signOutOthersLabel` "Sign out all other devices", `emptyTitle` "No other devices", `failedTitle` "Couldn't load your devices", `retryLabel` "Try again", `loadingDescription` "Loading devices" |
| `AvatarSourceSheet` | `title` "Change photo", `closeLabel` "Close"; its choices `AvatarSourceOptions.camera` "Take a photo", `library` "Choose from library", `remove` "Remove current photo" |

The functions that name a value — `availableText`, `takenText`, `confirmationPrompt`,
`RevokeSessionSheet`'s `description`, `formatProgress` — are composable, so they can call
`stringResource` with the value. `formatDate` is a plain `(LocalDate) -> String`.

```kotlin
SignOutSheet(
    visible = confirmingSignOut,
    onConfirm = onSignOut,
    onDismiss = { confirmingSignOut = false },
    title = "Abmelden?",
    confirmText = "Abmelden",
    cancelText = "Abbrechen",
)
```

See [flow/profile](../flow/profile/README.md).

## flow-attachments

| Part | Parameters with an English default |
| --- | --- |
| `AttachmentStrip` | `addLabel` "Add attachment", `removeLabel` "Remove <label>", `caption` (the file's label), `description` ("Receipt, IMG_2041.jpg"), `stateDescription` ("Uploading, 40%", "Not downloaded", "Waiting to download", "Unavailable", …) |
| `AttachmentTile` | `removeLabel` "Remove <label>" |
| `AttachmentAddTile` | `label` "Add attachment" |
| `AttachmentIndicator` | `contentDescription` "Has attachments" |
| `AttachmentSourceSheet` | `title` "Add attachment", `closeLabel` "Close"; its choices `AttachmentSourceOptions.camera` "Take a photo", `library` "Choose from library", `files` "Choose a file" |
| `AttachmentOptionsSheet` | `closeLabel` "Close"; its choices `AttachmentOptions.rename` "Rename", `share` "Share", `save` "Save to device", `openElsewhere` "Open in another app", `remove` "Remove attachment" |
| `AttachmentRenameSheet` | `title` "Rename", `label` "Name", `saveText` "Save", `cancelText` "Cancel" |
| `PendingAttachmentSheet` | `title` "Add this file?", `problemTitle` "Couldn’t add this file", `attachText` "Attach", `cancelText` "Cancel", `closeLabel` "Close", `editText` "Edit" |
| `AttachmentViewer` | `closeLabel` "Close", `shareLabel` "Share", `moreLabel` "More options", `position` "2 of 5", `filmstripLabel` |
| `AttachmentViewerPage` | `waitingTitle` "Not on this device yet", `retryText` "Try again", `unavailableTitle` "This file is no longer available", `openElsewhereText` "Open in another app", `saveText` "Save to device", `zoomInLabel` "Zoom in", `zoomOutLabel` "Zoom out", `resetZoomLabel` "Reset zoom", `pageLabel` and `pageDescription` "Page 2 of 9", `downloadingText` |

```kotlin
AttachmentSourceSheet(
    visible = choosingSource,
    options = listOf(
        AttachmentSourceOptions.camera(label = "Foto aufnehmen", onClick = onCamera),
        AttachmentSourceOptions.files(label = "Datei wählen", onClick = onFiles),
    ),
    onDismiss = { choosingSource = false },
    title = "Anhang hinzufügen",
    closeLabel = "Schließen",
)
```

A file's size and anything else that names or formats a value arrive already worded in its
`AttachmentItem`. See [flow/attachments](../flow/attachments/README.md).

## icons, viewmodel

Neither has words of its own. An icon's meaning is the `contentDescription` of the component that
draws it.
