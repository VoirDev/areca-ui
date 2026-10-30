# Profile flow — `dev.voir.areca:flow-profile`

Add it for a profile screen, editing a profile, choosing a username, the account menu and account
switcher, sign-in methods, data export, the devices signed in, changing the photo, signing out and
deleting the account.

- Artifact `dev.voir.areca:flow-profile`. Depends on `foundation` as `api` and on
  `dev.voir.areca:datetime` (so `kotlinx-datetime` 0.8.x is on the API surface: `BirthdayRow` takes
  `LocalDate`). Needs `ArecaThemeProvider`.
- It ships **parts and recipes, not screens**: copy the recipe and edit it; never write a configurable
  `ProfileScreen(…)` wrapper.
- Rows are foundation `SettingsRow`s and go in foundation `Settings { Section { … } }`, beside the
  app's own rows. Cards (header, cover, summary) and status bands sit on the canvas above the list.
- The parts draw and report presses. Form state, validation, rules (what may be unlinked, minimum age),
  networking, image loading, capture, cropping and uploading are the app's. No dependency on
  `flow-auth`, `crop`, `icons`, a navigation library or `viewmodel`.

| Package | Holds |
| --- | --- |
| `dev.voir.areca.flow.profile` | `ProfileSummary`, `AvatarImageLoader`, `AvatarImageState`, `LocalAvatarImageLoader` |
| `dev.voir.areca.flow.profile.part` | `ProfileAvatar`, `EditableAvatar`, `ProfileHeaderCard`, `ProfileCardAction`, `ProfileCoverCard`, `ProfileSummaryCard`, `ProfileSummaryCardAction`, `ProfileSummaryCardPlaceholder`, `ProfileGreeting`, `ProfileGreetingPlaceholder`, `ProfileSummaryCaption`, `AvatarUploadStatus`, `AvatarUploadState`, `StaleDetailsNotice`, `ProfileLoadFailure` |
| `dev.voir.areca.flow.profile.row` | `ProfileInfoRow`, `EmailRow`, `LinkedAccountRow`, `BirthdayRow`, `DataExportRow`, `DataExportState`, `AccountRow`, `AddAccountRow`, `SignOutRow`, `DeleteAccountRow`, `UsernameCheckFooter`, `UsernameCheck` |
| `dev.voir.areca.flow.profile.session` | `ActiveSessionsSections`, `SessionRow`, `ActiveSessionsState`, `SessionItem`, `SessionDevice` |
| `dev.voir.areca.flow.profile.sheet` | `AvatarSourceSheet`, `AvatarSourceOption`, `AvatarSourceOptions`, `SignOutSheet`, `SignOutOthersSheet`, `RevokeSessionSheet`, `DeleteAccountSheet` |

## Choose a part

| Need | Use | Types |
| --- | --- | --- |
| The picture or initials (spinner while loading) | `ProfileAvatar(name, image = …)` | `AvatarImageState`, foundation `AvatarSize` |
| The picture with a camera badge, one target, to change it | `EditableAvatar(summary, onChangePhoto)` | `ProfileSummary` |
| The card a profile opens with: picture, name, handle, caption, action tiles | `ProfileHeaderCard(summary, actions = { ProfileCardAction(…) })` | `ProfileSummary` |
| A tile in the header card's action row | `ProfileCardAction(icon, label, onClick)` | — |
| A cover band with the picture overlapping, name, handle, bio | `ProfileCoverCard(summary, bio, cover = …, actions = …)` | — |
| A compact pressable card leading into the account (top of a menu) | `ProfileSummaryCard(summary, onClick, trailing = { ProfileSummaryCardAction(…) })` | — |
| The same row with no card, for a home screen | `ProfileGreeting(summary, onClick, greeting = …)` | — |
| A caption of your own in those (sync state, plan) | `ProfileSummaryCaption(text)` in `caption` | — |
| Loading stand-ins at final height | `ProfileSummaryCardPlaceholder()`, `ProfileGreetingPlaceholder()` | — |
| One fact: value as title, what it is under it ("@ada" over "Username") | `ProfileInfoRow(value, label)` | — |
| Email with an "Unverified" badge and a "Verify" link | `EmailRow(email, isVerified, onVerify = …)` | — |
| One sign-in provider: link, unlink, or why not | `LinkedAccountRow(name, isLinked, onLink, onUnlink, icon = …)` | — |
| A birthday picked in a calendar sheet (a dialog on wide windows), removable | `BirthdayRow(value, onValueChange)` | `LocalDate`, `CalendarLabels` |
| "Download your data": idle → preparing → ready / failed | `DataExportRow(state, onRequest, onDownload)` | `DataExportState` |
| Account switcher rows | `AccountRow(account, isCurrent = …, onClick = …)`, `AddAccountRow(onClick)` | `ProfileSummary` |
| The two ways out, danger tone | `SignOutRow(onClick)`, `DeleteAccountRow(onClick)` | — |
| The verdict line under a username field | `UsernameCheckFooter(username, check, complaint, hint)` in a `Section`'s `footer` | `UsernameCheck` |
| "This device" + "Sign out all other devices" + every other session, with loading/failure/empty | `ActiveSessionsSections(state, onSessionClick, onSignOutOthers, onRetry)` (inside `Settings`) | `ActiveSessionsState`, `SessionItem`, `SessionDevice` |
| One device row | `SessionRow(session, onClick = …)` | `SessionItem` |
| A photo change in progress or failed | `AvatarUploadStatus(state, onRetry, onDismiss)` | `AvatarUploadState` |
| The details below were not just fetched | `StaleDetailsNotice(message)` | — |
| The whole screen when the profile could not load (capped at `ReadableWidth`, centred) | `ProfileLoadFailure(message, onRetry)` | — |
| Where a new picture comes from | `AvatarSourceSheet(visible, options, onDismiss)` | `AvatarSourceOption`, `AvatarSourceOptions` |
| Confirm sign out / sign out others / end one session / delete | `SignOutSheet`, `SignOutOthersSheet`, `RevokeSessionSheet`, `DeleteAccountSheet` | `SessionItem` |

Names and bio are plain foundation `SettingsTextFieldRow`s; the module adds nothing there.

## Signatures

```kotlin
// dev.voir.areca.flow.profile
ProfileSummary(
    name: String, handle: String? = null, email: String? = null,
    isVerified: Boolean = false,
    avatar: Any? = null,                   // whatever the app's AvatarImageLoader understands
    caption: String? = null,               // "Member since 2021"
)
AvatarImageState.Loading | Ready(painter: Painter) | Failed
interface AvatarImageLoader { @Composable fun load(model: Any): AvatarImageState }   // AvatarImageLoader.None answers Failed
LocalAvatarImageLoader: ProvidableCompositionLocal<AvatarImageLoader>

// .part
@Composable fun ProfileAvatar(
    name: String, modifier: Modifier = Modifier, image: Any? = null,
    state: AvatarImageState? = image?.let { LocalAvatarImageLoader.current.load(it) },
    size: AvatarSize = AvatarSize.ExtraLarge, busy: Boolean = false,
)
@Composable fun EditableAvatar(
    summary: ProfileSummary, onChangePhoto: () -> Unit, modifier: Modifier = Modifier,
    busy: Boolean = false, changePhotoLabel: String = "Change photo",
)
@Composable fun ProfileHeaderCard(
    summary: ProfileSummary, modifier: Modifier = Modifier,
    verifiedDescription: String = "Verified",
    actions: (@Composable RowScope.() -> Unit)? = null,
)
@Composable fun ProfileCardAction(icon: ImageVector, label: String, onClick: () -> Unit, modifier: Modifier = Modifier, enabled: Boolean = true)
@Composable fun ProfileCoverCard(
    summary: ProfileSummary, modifier: Modifier = Modifier, bio: String? = null,
    verifiedDescription: String = "Verified",
    cover: (@Composable BoxScope.() -> Unit)? = null,     // the app's artwork, its own loading
    actions: (@Composable RowScope.() -> Unit)? = null,
)
@Composable fun ProfileSummaryCard(
    summary: ProfileSummary, onClick: () -> Unit, modifier: Modifier = Modifier,
    onClickLabel: String? = null,
    caption: @Composable () -> Unit = { /* summary.caption or email */ },
    trailing: (@Composable BoxScope.() -> Unit)? = null,
)
@Composable fun ProfileSummaryCardAction(modifier: Modifier = Modifier, label: String? = null)   // "Edit" + chevron
@Composable fun ProfileSummaryCardPlaceholder(modifier: Modifier = Modifier, contentDescription: String = "Loading account")
@Composable fun ProfileGreeting(summary: ProfileSummary, onClick: () -> Unit, modifier: Modifier = Modifier, greeting: String? = null, onClickLabel: String? = null)
@Composable fun ProfileGreetingPlaceholder(modifier: Modifier = Modifier, contentDescription: String = "Loading account")
@Composable fun ProfileSummaryCaption(text: String, modifier: Modifier = Modifier, color: Color = ArecaTheme.colors.fgMuted)
AvatarUploadState.Idle | Uploading(progress: Float? = null) | Failed(message: String)
@Composable fun AvatarUploadStatus(
    state: AvatarUploadState, onRetry: () -> Unit, onDismiss: () -> Unit, modifier: Modifier = Modifier,
    uploadingTitle: String = "Uploading photo", uploadingSubtitle: String? = null,
    formatProgress: (@Composable (percent: Int) -> String)? = { "$it%" },
    failedTitle: String = "Photo not updated", retryLabel: String = "Try again", dismissLabel: String = "Dismiss",
)
@Composable fun StaleDetailsNotice(message: String, modifier: Modifier = Modifier, title: String = "Showing saved details")
@Composable fun ProfileLoadFailure(
    message: String, onRetry: () -> Unit, modifier: Modifier = Modifier,
    title: String = "Couldn't load your account", retrying: Boolean = false, retryLabel: String = "Try again",
)

// .row
@Composable fun ProfileInfoRow(
    value: String, label: String, modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null, onClickLabel: String? = null,
    status: SettingsRowStatus = SettingsRowStatus.Idle, enabled: Boolean = true,
    leading: (@Composable BoxScope.() -> Unit)? = null,     // SettingsRowIcon(…)
    trailing: (@Composable BoxScope.() -> Unit)? = null,    // ListItemChevron() when it navigates
)
@Composable fun EmailRow(
    email: String?, isVerified: Boolean, modifier: Modifier = Modifier,
    onVerify: (() -> Unit)? = null,                        // shows a "Verify" SettingsRowLink while unverified
    onClick: (() -> Unit)? = null, onClickLabel: String? = null,
    status: SettingsRowStatus = SettingsRowStatus.Idle,
    label: String = "Email", placeholder: String = "Add email",
    unverifiedBadge: String? = "Unverified", verifyLabel: String = "Verify",
    enabled: Boolean = true,
    trailing: (@Composable BoxScope.() -> Unit)? = /* the Verify link */,
)
@Composable fun LinkedAccountRow(
    name: String, isLinked: Boolean, onLink: () -> Unit, onUnlink: () -> Unit, modifier: Modifier = Modifier,
    account: String? = null,                               // the linked address
    canUnlink: Boolean = true,                             // false when it is the last way to sign in
    lockedReason: String? = null,
    status: SettingsRowStatus = SettingsRowStatus.Idle,
    notLinkedLabel: String? = null, linkLabel: String = "Link", unlinkLabel: String = "Unlink",
    onClickLabel: String? = null, enabled: Boolean = true,
    icon: @Composable BoxScope.() -> Unit,                 // the provider's mark, supplied by the app
)
@Composable fun BirthdayRow(
    value: LocalDate?, onValueChange: (LocalDate?) -> Unit, modifier: Modifier = Modifier,
    status: SettingsRowStatus = SettingsRowStatus.Idle,
    minDate: LocalDate? = null,
    maxDate: LocalDate? = Clock.System.todayIn(TimeZone.currentSystemDefault()),
    removable: Boolean = true,
    label: String = "Birthday", placeholder: String = "Add birthday",
    formatDate: (LocalDate) -> String = /* English "15 September 1990" */,
    sheetTitle: String = "Choose your birthday",
    applyLabel: String = "Save", cancelLabel: String = "Cancel", removeLabel: String = "Remove",
    calendarLabels: CalendarLabels = CalendarLabels.English,
    startDayOfWeek: DayOfWeek = DayOfWeek.MONDAY,
    enabled: Boolean = true,
)
DataExportState.Idle | Preparing | Ready | Failed(message: String)
@Composable fun DataExportRow(
    state: DataExportState, onRequest: () -> Unit, onDownload: () -> Unit, modifier: Modifier = Modifier,
    title: String = "Download your data",
    idleSubtitle: String? = null, preparingSubtitle: String? = null, readySubtitle: String? = null,
    requestLabel: String = "Request", downloadLabel: String = "Download", retryLabel: String = "Try again",
    onClickLabel: String? = null, enabled: Boolean = true,
)
@Composable fun AccountRow(
    account: ProfileSummary, modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null, onClickLabel: String? = null,
    isCurrent: Boolean = false, currentDescription: String = "Current account",
    status: SettingsRowStatus = SettingsRowStatus.Idle, enabled: Boolean = true,
    trailing: (@Composable BoxScope.() -> Unit)? = /* tick when isCurrent */,
)
@Composable fun AddAccountRow(onClick: () -> Unit, modifier: Modifier = Modifier, title: String = "Add account",
    onClickLabel: String? = null, status: SettingsRowStatus = SettingsRowStatus.Idle, enabled: Boolean = true)
@Composable fun SignOutRow(onClick: () -> Unit, modifier: Modifier = Modifier, title: String = "Sign out",
    onClickLabel: String? = null, status: SettingsRowStatus = SettingsRowStatus.Idle, enabled: Boolean = true)
@Composable fun DeleteAccountRow(onClick: () -> Unit, modifier: Modifier = Modifier, title: String = "Delete account",
    subtitle: String? = null, onClickLabel: String? = null, status: SettingsRowStatus = SettingsRowStatus.Idle, enabled: Boolean = true)
UsernameCheck.Idle | Checking | Available | Taken
@Composable fun UsernameCheckFooter(
    username: String, check: UsernameCheck, modifier: Modifier = Modifier,
    complaint: String? = null,                             // the app's objection; outranks the verdict
    hint: String? = null,                                  // the rules, shown until a verdict replaces it
    checkingText: String = "Checking username…",
    availableText: @Composable (username: String) -> String = { "@$it is available." },
    takenText: @Composable (username: String) -> String = { "@$it is already taken." },
)

// .session
SessionDevice.Phone | Tablet | Desktop | Browser | Unknown
SessionItem(
    id: String, device: SessionDevice, name: String,
    client: String? = null,                                // "Safari on iOS 18"
    seen: String? = null,                                  // "Belgrade, Serbia · 10:18", worded by the app
    isCurrent: Boolean = false, isRevoking: Boolean = false,
)
ActiveSessionsState.Loading | Failed(message: String) | Loaded(sessions: List<SessionItem>, isSigningOutOthers: Boolean = false)
@Composable fun SettingsScope.ActiveSessionsSections(  // call inside Settings { }
    state: ActiveSessionsState,
    onSessionClick: ((SessionItem) -> Unit)?,              // null = sessions not pressable
    onSignOutOthers: (() -> Unit)?,                        // null = no band
    onRetry: () -> Unit,
    footer: String? = null,
    thisDeviceHeader: String = "This device", activeSessionsHeader: String = "Active sessions",
    signOutOthersLabel: String = "Sign out all other devices", signOutOthersFooter: String? = null,
    signOutOthersClickLabel: String? = null, sessionClickLabel: String? = null,
    emptyTitle: String = "No other devices", emptySubtitle: String? = null,
    failedTitle: String = "Couldn't load your devices", retryLabel: String = "Try again",
    loadingDescription: String = "Loading devices",
)
@Composable fun SessionRow(
    session: SessionItem, modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null, onClickLabel: String? = null, enabled: Boolean = true,
    trailing: (@Composable BoxScope.() -> Unit)? = null,
)

// .sheet
class AvatarSourceOption(label: String, icon: ImageVector, onClick: () -> Unit, isDestructive: Boolean = false)
AvatarSourceOptions.camera(label: String = "Take a photo", onClick: () -> Unit)
AvatarSourceOptions.library(label: String = "Choose from library", onClick: () -> Unit)
AvatarSourceOptions.remove(label: String = "Remove current photo", onClick: () -> Unit)
@Composable fun AvatarSourceSheet(
    visible: Boolean, options: List<AvatarSourceOption>, onDismiss: () -> Unit,
    title: String = "Change photo", subtitle: String? = null, closeLabel: String = "Close",
)
@Composable fun SignOutSheet(
    visible: Boolean, onConfirm: () -> Unit, onDismiss: () -> Unit, confirming: Boolean = false,
    note: String? = null, title: String = "Sign out?", description: String? = null,
    confirmText: String = "Sign out", cancelText: String = "Cancel",
)
@Composable fun SignOutOthersSheet(
    visible: Boolean, onConfirm: () -> Unit, onDismiss: () -> Unit, confirming: Boolean = false,
    title: String = "Sign out all other devices?", description: String? = null, note: String? = null,
    confirmText: String = "Sign out other devices", cancelText: String = "Cancel",
)
@Composable fun RevokeSessionSheet(
    session: SessionItem?,                                 // non-null = shown
    onConfirm: (SessionItem) -> Unit, onDismiss: () -> Unit, confirming: Boolean = false,
    title: String = "Sign out this device?", note: String? = null,
    confirmText: String = "Sign out device", cancelText: String = "Cancel",
    description: @Composable (session: SessionItem) -> String? = { listOfNotNull(it.name, it.client, it.seen).joinToString(" · ") },
)
@Composable fun DeleteAccountSheet(
    visible: Boolean, onConfirm: () -> Unit, onDismiss: () -> Unit, confirming: Boolean = false,
    confirmationPhrase: String? = null,                    // "DELETE": a box that must hold it (case and spaces ignored)
    description: String? = null,                           // say whether deletion is instant, scheduled or reversible
    title: String = "Delete this account?", note: String? = null,
    confirmText: String = "Delete account", cancelText: String = "Cancel",
    confirmationPrompt: @Composable (phrase: String) -> String = { "Type $it to confirm." },
)
```

## Rules

- **Pictures:** implement `AvatarImageLoader` once over the app's image library and provide
  `LocalAvatarImageLoader` above the app. Without it, `AvatarImageLoader.None` answers `Failed` →
  initials. `Failed` always shows initials, never an empty circle. Hand decoded pictures directly with
  `ProfileAvatar(name, state = AvatarImageState.Ready(painter))`.
- **Cards on the canvas:** pad with `Modifier.horizontalContentInsets()` and space from the list with
  `ArecaTheme.spacing.lg`. When the app restyles sections via `Settings(styles = …)`, compose the cards
  inside that `Settings` as its first children so they take its fill and corner.
- `separated = false` on a `Section` whose every row has a leading tile. Leading visuals:
  `SettingsRowIcon(glyph)`; a provider mark in `LinkedAccountRow`'s `icon` is cropped to the tile.
- Rows take plain `Text` in their slots; never style row text.
- **Sheets are state-driven:** call them unconditionally with `visible` (or `session` for
  `RevokeSessionSheet`); scrim, back and Cancel all arrive as `onDismiss`. Answer `onConfirm` by
  closing (`visible = false`) or by setting `confirming = true` until the work ends — a sheet left up
  with neither takes no further press. `AvatarSourceSheet` does not close itself: every option's
  `onClick` clears the flag.
- Offer only sources the platform can complete in `AvatarSourceSheet` (no camera option without a
  camera); `remove` only when `summary.avatar != null`.
- **Upload is separate from the form:** keep `AvatarUploadState` apart from the edit form's state; a
  new photo takes effect when its upload finishes, without Save.
- **Cropping** is the app's crop screen (`dev.voir.areca:crop`), opened from `EditableAvatar`'s
  `onChangePhoto` → `AvatarSourceSheet` → pick → crop → upload. Render the crop unmasked
  (`applyMask = false`): avatars clip to their circle wherever shown.
- `canUnlink = false` keeps `LinkedAccountRow` from removing the last sign-in method — the app counts.
- `ActiveSessionsSections` leaves this device inert; signing out of **this** device is `SignOutRow` in
  the account menu, not a session action. Each sheet closes when answered and the row's spinner
  (`SessionItem.isRevoking`, `Loaded.isSigningOutOthers`) shows the work.
- **Larger windows.** Phones are unchanged. From an expanded window up
  (`AdaptiveDefaults.usesDialog(ArecaTheme.hostWindow)`: a tablet on its side, a laptop, a desktop —
  not a phone on its side) every sheet here opens as a centred dialog with the same content and
  answers: the confirmation sheets via foundation `ConfirmSheet`, `AvatarSourceSheet` and
  `BirthdayRow`'s calendar by the same rule. The whole window decides, not the pane; nothing to pass.
- **Cap the screen's column, not a part:** a card capped alone would sit narrower than the settings
  list under it. After the insets add `.wrapContentWidth().widthIn(max = AdaptiveDefaults.ReadableWidth)
  .fillMaxWidth()` — a no-op on a phone. Do not wrap the recipes in `ContentColumn`: it draws the
  gutters the cards and sections already draw. `ProfileLoadFailure` is a whole screen and caps itself.
- **Settings on a tablet or desktop** is a foundation `ListDetail`: summary card and section rows in
  the list pane, the open page in the detail pane, back action only when `!isTwoPane` (recipe below).
- `BirthdayRow` is bounded to today by default; move `maxDate` back for an age requirement. Save stays
  disabled until a day is tapped.
- Put `SignOutRow` and `DeleteAccountRow` each in a `Section` of its own with no header.
- Key `AccountRow`s by account id inside `forEach`; pass `onClick = null` for the current account.
- Neutral by default: nothing is brand-coloured; pass brand accents through `SettingsDefaults.styles`.

## Words and accessibility

- Required words default to English (titles, button labels, sheet questions, `changePhotoLabel`,
  `verifiedDescription`, `currentDescription`, placeholders of rows); pass the app's strings for every
  one. Optional words (`subtitle`, `footer`, `description`, `note`, `hint`, click labels) default to
  `null` and draw nothing.
- Anything that embeds a value is a function: `availableText`, `takenText`, `confirmationPrompt`,
  `RevokeSessionSheet.description`, `formatProgress` (all `@Composable`, so they can call
  `stringResource(…, value)`), and `formatDate: (LocalDate) -> String` (plain). Pass the birthday
  calendar's `calendarLabels` and `startDayOfWeek`.
- Device names, session details, captions and failure messages arrive worded from the app's data.
- Built in: one-node fact rows ("@ada, Username"), one target for avatar and badge, announced ticks,
  48 dp "Verify" link, announced upload heading and username verdict (not the percentage or hint),
  heading roles, labelled placeholders. Pass `onClickLabel` / `sessionClickLabel` /
  `signOutOthersClickLabel` where the row's words do not say the action.

## Recipes

### Profile

```kotlin
@Composable
fun ProfileContent(summary: ProfileSummary, phone: String, staleReason: String?, deviceCount: Int,
    onEdit: () -> Unit, onShare: () -> Unit, onQr: () -> Unit, onDevices: () -> Unit) {
    Column(
        Modifier.fillMaxSize().background(ArecaTheme.colors.bgCanvas)
            .verticalScroll(rememberScrollState()).windowInsetsPadding(WindowInsets.systemBars)
            // Readable width on a tablet or desktop, centred; a no-op on a phone.
            .wrapContentWidth().widthIn(max = AdaptiveDefaults.ReadableWidth).fillMaxWidth(),
    ) {
        if (staleReason != null) {
            StaleDetailsNotice(message = staleReason, title = staleTitle, modifier = Modifier.horizontalContentInsets())
            Spacer(Modifier.height(ArecaTheme.spacing.lg))
        }
        ProfileHeaderCard(
            summary = summary,
            verifiedDescription = verifiedWord,
            modifier = Modifier.horizontalContentInsets(),
            actions = {
                ProfileCardAction(ArecaIcons.Edit, editLabel, onClick = onEdit, modifier = Modifier.weight(1f))
                ProfileCardAction(ArecaIcons.Share, shareLabel, onClick = onShare, modifier = Modifier.weight(1f))
                ProfileCardAction(ArecaIcons.QrCode, qrLabel, onClick = onQr, modifier = Modifier.weight(1f))
            },
        )
        Spacer(Modifier.height(ArecaTheme.spacing.lg))
        Settings {
            Section(header = { Text(yourInfo) }, separated = false) {
                ProfileInfoRow(value = phone, label = mobileLabel, leading = { SettingsRowIcon(ArecaIcons.Phone) })
                summary.handle?.let { ProfileInfoRow(value = it, label = usernameLabel, leading = { SettingsRowIcon(ArecaIcons.AtSign) }) }
                EmailRow(email = summary.email, isVerified = summary.isVerified, label = emailLabel, unverifiedBadge = unverifiedWord)
            }
            Section {
                SettingsRow(
                    title = { Text(devicesLabel) },
                    value = { Text(deviceCount.toString()) },
                    leading = { SettingsRowIcon(ArecaIcons.Laptop) },
                    trailing = { ListItemChevron() },
                    onClick = onDevices,
                )
            }
        }
    }
}

// Cover variant
ProfileCoverCard(
    summary = summary,
    bio = bio,
    cover = { AsyncImage(model = coverUrl, contentDescription = null, contentScale = ContentScale.Crop) },
    actions = { Button(text = editProfile, onClick = onEdit, style = ButtonStyle.soft(ActionColor.Neutral), size = ButtonSize.Small) },
    modifier = Modifier.horizontalContentInsets(),
)

// Nothing loaded at all
when (val s = uiState) {
    is Loaded -> ProfileContent(…)
    is Failed -> ProfileLoadFailure(message = s.message, onRetry = onRetry, title = loadFailedTitle, retryLabel = tryAgain)
}
```

### Edit profile

```kotlin
Column(
    Modifier.fillMaxSize().background(ArecaTheme.colors.bgCanvas).verticalScroll(rememberScrollState())
        .windowInsetsPadding(WindowInsets.systemBars).imePadding()
        .wrapContentWidth().widthIn(max = AdaptiveDefaults.ReadableWidth).fillMaxWidth(),
) {
    EditableAvatar(summary = summary, busy = upload is AvatarUploadState.Uploading, onChangePhoto = onChangePhoto,
        changePhotoLabel = changePhoto, modifier = Modifier.horizontalContentInsets())
    AvatarUploadStatus(state = upload, onRetry = onRetryUpload, onDismiss = onDismissUpload, modifier = Modifier.horizontalContentInsets())
    Spacer(Modifier.height(ArecaTheme.spacing.xl2))
    Settings {
        Section(
            header = { Text(yourName) },
            footer = state.nameError?.let { error -> { Text(error, color = ArecaTheme.colors.fgDangerText) } },
        ) {
            SettingsTextFieldRow(value = state.firstName, onValueChange = onFirstName, placeholder = firstNamePlaceholder,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words, imeAction = ImeAction.Next))
            SettingsTextFieldRow(value = state.lastName, onValueChange = onLastName, placeholder = lastNamePlaceholder)
        }
        Section(footer = { Text(bioFooter) }) {
            SettingsTextFieldRow(value = state.bio, onValueChange = onBio, placeholder = bioPlaceholder, maxLength = 140, singleLine = false)
        }
        Section(header = { Text(yourInfo) }, separated = false) {
            ProfileInfoRow(value = phone, label = changePhoneHint, onClick = onChangePhone, leading = { SettingsRowIcon(ArecaIcons.Phone) })
            ProfileInfoRow(value = summary.handle ?: usernameLabel, label = usernameLabel, onClick = onUsername,
                leading = { SettingsRowIcon(ArecaIcons.AtSign) }, trailing = { ListItemChevron() })
            BirthdayRow(value = state.birthday, onValueChange = onBirthday, formatDate = appDateFormat::format,
                calendarLabels = appCalendarLabels, startDayOfWeek = firstDayOfWeek, label = birthdayLabel)
        }
    }
    Button(text = saveLabel, onClick = onSave, loading = state.saving, modifier = Modifier.fillMaxWidth().horizontalContentInsets())
    Settings { Section { DeleteAccountRow(onClick = onDelete, enabled = !state.saving, title = deleteAccount) } }
}
AvatarSourceSheet(visible = pickingAvatar, options = avatarOptions, onDismiss = onAvatarSheetDismissed, title = changePhoto)
DeleteAccountSheet(visible = confirmingDelete, confirmationPhrase = deletePhrase, description = deletionTerms,
    onConfirm = onDeleteConfirmed, onDismiss = onDeleteDismissed)
```

### Username

```kotlin
Settings {
    Section(
        header = { Text(usernameLabel) },
        footer = {
            UsernameCheckFooter(
                username = state.username,
                check = state.check,                       // the service's verdict
                complaint = state.complaint,               // the app's own rule or a failed save
                hint = usernameRules,
                checkingText = checkingText,
                availableText = { stringResource(Res.string.username_available, it) },
                takenText = { stringResource(Res.string.username_taken, it) },
            )
        },
    ) {
        SettingsTextFieldRow(
            value = state.username,
            onValueChange = onUsername,
            placeholder = usernameLabel,
            leading = { Text("@") },
            keyboardOptions = KeyboardOptions(autoCorrectEnabled = false, imeAction = ImeAction.Done),
        )
    }
}
```

### Account menu and switcher

```kotlin
ProfileSummaryCard(
    summary = summary,
    onClick = onOpenProfile,
    modifier = Modifier.horizontalContentInsets(),
    trailing = { ProfileSummaryCardAction(label = editLabel) },
)   // while loading: ProfileSummaryCardPlaceholder(contentDescription = loadingAccount)
Spacer(Modifier.height(ArecaTheme.spacing.lg))
Settings {
    Section(header = { Text(signInMethods) }) {
        EmailRow(email = summary.email, isVerified = summary.isVerified, onVerify = onVerify, onClick = onChangeEmail)
        LinkedAccountRow(
            name = "Google",
            icon = { Image(painterResource(Res.drawable.google), contentDescription = null) },
            isLinked = google != null,
            account = google?.email,
            canUnlink = signInMethodCount > 1,
            status = googleStatus,
            onLink = { onLink(Provider.Google) },
            onUnlink = { onUnlink(Provider.Google) },
        )
    }
    Section(header = { Text(yourData) }) {
        DataExportRow(state = export, onRequest = onRequestExport, onDownload = onDownloadExport)
    }
    Section(header = { Text(accountsHeader) }) {
        AccountRow(account = summary, isCurrent = true, onClick = null, currentDescription = currentAccount)
        otherAccounts.forEach { other ->
            key(other.id) { AccountRow(account = other.summary, onClick = { onSwitch(other) }, trailing = { ListItemChevron() }) }
        }
        AddAccountRow(onClick = onAddAccount, title = addAccount)
    }
    Section { SignOutRow(onClick = onSignOut, title = signOut) }
    Section { DeleteAccountRow(onClick = onDelete, title = deleteAccount) }
}
SignOutSheet(visible = confirmingSignOut, confirming = signingOut, note = unsyncedNote,
    onConfirm = onSignOutConfirmed, onDismiss = onSignOutDismissed)
```

Home-screen variant: `ProfileGreeting(summary, onClick = onOpenProfile, greeting = goodMorning)` /
`ProfileGreetingPlaceholder()`.

### Devices

```kotlin
Settings {
    ActiveSessionsSections(
        state = sessions,
        onSessionClick = { revoking = it },
        onSignOutOthers = { confirmingSignOutOthers = true },
        onRetry = onReloadSessions,
        footer = devicesFooter,
        thisDeviceHeader = thisDevice,
        activeSessionsHeader = activeSessions,
        signOutOthersLabel = signOutOthers,
    )
    Section(header = { Text(autoSignOut) }) {
        SettingsSelectRow(title = { Text(ifInactiveFor) }, options = inactivityOptions, selected = inactivity,
            onSelect = onInactivity, optionLabel = { it.label })
    }
}
RevokeSessionSheet(session = revoking, onConfirm = { s -> revoking = null; onRevoke(s) }, onDismiss = { revoking = null })
SignOutOthersSheet(
    visible = confirmingSignOutOthers,
    onConfirm = { confirmingSignOutOthers = false; onSignOutOthers() },
    onDismiss = { confirmingSignOutOthers = false },
)

// A device row of your own, outside the sections
val phone = SessionItem(id = "s1", device = SessionDevice.Phone, name = "iPhone 15", client = "App 4.2", seen = seenLine)
SessionRow(session = phone, onClick = { revoking = phone })
```

### Change photo, with cropping

```kotlin
val AvatarCrop = CropSpec.Avatar                       // app-wide, once
var choosingSource by remember { mutableStateOf(false) }
var picked by remember { mutableStateOf<Painter?>(null) }

EditableAvatar(summary = summary, busy = upload is AvatarUploadState.Uploading, onChangePhoto = { choosingSource = true })
AvatarUploadStatus(state = upload, onRetry = onRetryUpload, onDismiss = onDismissUploadFailure)
AvatarSourceSheet(
    visible = choosingSource,
    options = buildList {
        if (camera.isAvailable) add(AvatarSourceOptions.camera(takePhoto) { choosingSource = false; scope.launch { picked = camera.capture()?.decode() } })
        add(AvatarSourceOptions.library(chooseFromLibrary) { choosingSource = false; scope.launch { picked = picker.pick()?.decode() } })
        add(AvatarSourceOption(label = avatarSet, icon = ArecaIcons.FaceSmile, onClick = { choosingSource = false; onAvatarSet() }))
        if (summary.avatar != null) add(AvatarSourceOptions.remove(removePhoto) { choosingSource = false; onRemoveAvatar() })
    },
    onDismiss = { choosingSource = false },
    title = changePhoto,
)
picked?.let { photo ->
    CropDialog(onDismissRequest = { picked = null }) {
        CropScreen(photo = photo, spec = AvatarCrop, title = moveAndScale,
            photoDescription = photoDescription, labels = appCropLabels,
            onCancel = { picked = null },
            onDone = { bitmap -> picked = null; onUploadAvatar(bitmap) })   // the app's crop screen renders with applyMask = false
    }
}
```

### Delete account

```kotlin
Settings { Section { DeleteAccountRow(onClick = { confirmingDelete = true }, subtitle = deleteCost) } }
DeleteAccountSheet(
    visible = confirmingDelete,
    confirming = deleting,
    confirmationPhrase = "DELETE",
    confirmationPrompt = { stringResource(Res.string.type_to_confirm, it) },
    description = deletionIsScheduledIn30Days,
    note = whatYouLose,
    onConfirm = onDeleteConfirmed,                       // sets deleting = true, clears visible when done
    onDismiss = { confirmingDelete = false },
)
```

### Settings on a tablet or desktop (list–detail)

```kotlin
enum class AccountPage(val title: StringResource) { EditProfile(Res.string.edit_profile), Devices(Res.string.devices) }

@Composable
fun AccountSettings(summary: ProfileSummary, deviceCount: Int, confirmingSignOut: Boolean,
    onSignOut: () -> Unit, onSignOutDismissed: () -> Unit,
    editProfile: @Composable () -> Unit, devices: @Composable () -> Unit) {   // the recipes above, unchanged
    var open by rememberSaveable { mutableStateOf<AccountPage?>(null) }       // the app's state
    ListDetail(
        showDetail = open != null,
        modifier = Modifier.fillMaxSize().background(ArecaTheme.colors.bgCanvas).windowInsetsPadding(WindowInsets.systemBars),
        detailPlaceholder = {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Fallback(heading = { Text(chooseASetting) }) }
        },
        list = {
            val marked = open.takeIf { isTwoPane }                              // mark only beside the page
            val chevron: (@Composable BoxScope.() -> Unit)? = if (isTwoPane) null else ({ ListItemChevron() })
            Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                Spacer(Modifier.height(ArecaTheme.spacing.lg))
                ProfileSummaryCard(summary = summary, onClick = { open = AccountPage.EditProfile },
                    modifier = Modifier.horizontalContentInsets(), trailing = { ProfileSummaryCardAction(label = editLabel) })
                Spacer(Modifier.height(ArecaTheme.spacing.lg))
                Settings {
                    Section(header = { Text(accountLabel) }, separated = false) {
                        SettingsRow(title = { Text(stringResource(AccountPage.EditProfile.title)) },
                            leading = { SettingsRowIcon(ArecaIcons.UserEdit) }, trailing = chevron,
                            selected = marked == AccountPage.EditProfile, onClick = { open = AccountPage.EditProfile })
                        SettingsRow(title = { Text(stringResource(AccountPage.Devices.title)) }, value = { Text(deviceCount.toString()) },
                            leading = { SettingsRowIcon(ArecaIcons.Laptop) }, trailing = chevron,
                            selected = marked == AccountPage.Devices, onClick = { open = AccountPage.Devices })
                    }
                    Section { SignOutRow(onClick = onSignOut) }
                }
            }
        },
        detail = {
            open?.let { page ->
                Column(Modifier.fillMaxSize()) {
                    TopBar(title = stringResource(page.title),
                        leading = if (isTwoPane) null else ({ BackAction(onClick = { open = null }) }))
                    Box(Modifier.weight(1f)) {
                        when (page) { AccountPage.EditProfile -> editProfile(); AccountPage.Devices -> devices() }
                    }
                }
            }
        },
    )
    SignOutSheet(visible = confirmingSignOut, description = signOutDescription,
        onConfirm = onSignOutDismissed, onDismiss = onSignOutDismissed)
}
```

Two panes from a 600dp container (unfolded foldable, phone on its side, tablet, desktop); one at a
time below, with the system back wired to `open = null`. Each pane is a `WindowClassBox`, so the
pages lay themselves out for the pane; their own `systemBars` padding adds nothing once the layout
took the bars.

### Image loader (once, at the root)

```kotlin
object CoilAvatarImageLoader : AvatarImageLoader {
    @Composable
    override fun load(model: Any): AvatarImageState {
        val painter = rememberAsyncImagePainter(model)
        val state by painter.state.collectAsState()
        return when (state) {
            is AsyncImagePainter.State.Success -> AvatarImageState.Ready(painter)
            is AsyncImagePainter.State.Error -> AvatarImageState.Failed
            else -> AvatarImageState.Loading
        }
    }
}
CompositionLocalProvider(LocalAvatarImageLoader provides CoilAvatarImageLoader) { App() }
```
