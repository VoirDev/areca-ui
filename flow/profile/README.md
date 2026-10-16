# `flow-profile`

Parts for building account screens — the profile, the screen that edits it, the username, the
account menu and the devices signed in — and recipes for putting them together.

```kotlin
implementation("dev.voir.areca:flow-profile:<version>")
```

Areca is published to GitHub Packages, so the build needs its repository first; see
[Installing](../../README.md#installing).

The module's own glyphs are bundled with it. The recipes below also draw glyphs of the
application's choosing from `ArecaIcons` — a phone, an at sign, a share arrow — which needs
`dev.voir.areca:icons` as well. Their words come from the application's own `composeResources`
(`Res.string.…`), as under [Words](#words).

## What this is not

It is not a `ProfileScreen`.

Every application with accounts has a profile, and no two agree on what is on it. One shows a
birthday and one a phone number; one signs out from the profile and one from a menu two screens
away; one deletes the account at the foot of the edit form and one behind a support conversation. A
composable taking every one of those decisions as a parameter is longer than the screen it replaces,
and still wrong for the next product.

So the module ships the pieces, each carrying the things applications reliably get wrong, and this
file ships the assemblies. **Copy a recipe and edit it.** That is the intended use.

It has no connection to `flow-auth`. The two are separate flows: an application can use either
without the other. See [`../FLOWS.md`](../FLOWS.md).

## Built on the settings list

A profile screen is a settings screen with a person at the top. So the module does not ship a list
of its own: its rows are foundation `SettingsRow`s and they go in a foundation `Settings` list, in
`Section`s, beside any row the application writes itself.

```kotlin
Settings {
    Section(header = { Text(stringResource(Res.string.your_info)) }, separated = false) {
        ProfileInfoRow(value = phone, label = stringResource(Res.string.mobile), leading = { SettingsRowIcon(ArecaIcons.Phone) })
        EmailRow(email = summary.email, isVerified = summary.isVerified)
        BirthdayRow(value = birthday, onValueChange = onBirthdayChange)
    }
}
```

Everything the settings list does, these rows do: the neutral look, the section card and its
header and footer, the hairlines that start at the text, the press, hover and focus answer, the
danger tone, the spinner in place of the value while a change saves, and the message under the row
when it fails (`SettingsRowStatus`). The names and the bio are plain `SettingsTextFieldRow`s — the
card is the field and the hairline its underline — so the module adds nothing there.

Where every row in a section carries a tile, pass `separated = false`: the tiles already separate
the rows.

The rows set their copy in the settings row's roles — title, subtitle, caption, value — and align
their tiles and trailing content as every settings row does: centred for one or two lines, from the
top for more. The summary card and the upload band, drawn by hand, read the same type and ink from
`SettingsTheme` and line up the same way. The way out of every other session sits on the "This
device" card's danger band rather than behind a hairline, and a provider's mark is cropped to the
icon tile by `SettingsRowTile`, so every leading visual in a section has one size and shape. A row of the application's own beside them should pass plain `Text` into the slots
rather than styling it, or it will be the one row on the card that looks different.

The cards — header, cover, summary — and the status bands are drawn on a section's card, read from
`SettingsTheme.styles.section`, so a card above a settings list reads as its first block. Put
them on the canvas, pad them with `Modifier.horizontalContentInsets()`, and space them from the list
with `ArecaTheme.spacing.lg`, the list's own section gap.

An application that restyles its sections — a white card on a grey page, say, through
`Settings(styles = …)` — composes the cards inside that same list, as its first children. They then
take the list's fill and corner instead of the theme default, and the list's section gap spaces them
from the sections below for free:

```kotlin
Settings(styles = SettingsDefaults.styles(section = SettingsDefaults.sectionStyle(bg = surface))) {
    ProfileHeaderCard(summary = summary, modifier = Modifier.horizontalContentInsets())
    Section { … }
}
```

## The pieces

### Who the account is

| | |
| --- | --- |
| `ProfileAvatar` | The picture, or the initials. Initials, spinner and picture told apart on sight |
| `ProfileHeaderCard` | The card a profile opens with: picture, name, handle, caption, and a row of `ProfileCardAction` tiles |
| `ProfileCoverCard` | A cover band, the picture overlapping it, and the name, handle, bio and caption |
| `ProfileSummaryCard` | A compact card that leads into the account, at the top of a menu |
| `ProfileGreeting` | The same row with no card and no press tint, for the top of a home screen |
| `ProfileSummaryCaption` | The quiet line of either, for a caption of your own |
| `ProfileSummaryCardPlaceholder` `ProfileGreetingPlaceholder` | Loading stand-ins at the final height |
| `EditableAvatar` | The picture with a camera badge, one tap target for both |

### Rows, for a `Section`

| | |
| --- | --- |
| `ProfileInfoRow` | One fact: the value as the title, what it is under it — "@ada" over "Username" |
| `BirthdayRow` | A birthday chosen from a calendar — bounded to today unless told otherwise — and removed from it |
| `EmailRow` | The address, an "Unverified" badge until it is confirmed, and an optional "Verify" |
| `LinkedAccountRow` | One sign-in provider; pressing links or unlinks it, or says why it cannot |
| `DataExportRow` | "Download your data", through idle, preparing, ready and failed |
| `AccountRow` `AddAccountRow` | The account switcher: the account in use ticked, the others one press away |
| `SignOutRow` `DeleteAccountRow` | The two ways out, in the danger tone |
| `UsernameCheckFooter` | The line under a username field that says whether the name can be had |

### Devices

| | |
| --- | --- |
| `ActiveSessionsSections` | "This device" with "Sign out all other devices", then every other session — loading, failure and emptiness drawn in the card |
| `SessionRow` | One device: a round tile and up to three lines — the device as the title, what it runs as the subtitle, where and when it was last seen as the caption — pressable when given `onClick` |

### Sheets

| | |
| --- | --- |
| `AvatarSourceSheet` `AvatarSourceOption` `AvatarSourceOptions` | Any choices the product offers for a new picture — camera, library and remove ready-made, and its own |
| `SignOutSheet` | Confirms signing out of this device |
| `SignOutOthersSheet` | Confirms ending every other session |
| `RevokeSessionSheet` | Confirms ending one session, naming the device |
| `DeleteAccountSheet` | Confirms deletion, optionally behind a typed phrase |

### Status

| | |
| --- | --- |
| `AvatarUploadStatus` | What a picture change is doing: progress, or why it stopped |
| `StaleDetailsNotice` | Says the details below were not just fetched |
| `ProfileLoadFailure` | The whole-screen state for a profile that could not be loaded at all |

### State

| | |
| --- | --- |
| `ProfileSummary` | Who the account is, already worded |
| `UsernameCheck` | What `UsernameCheckFooter` shows: nothing asked, checking, available, taken |
| `ActiveSessionsState` `SessionItem` `SessionDevice` | The device list |
| `AvatarUploadState` `DataExportState` | The two long-running things a profile does |

All of it is UI state — what to draw. There is no form state, no validation and no rules: what a
valid name or username is, what may be unlinked, which session may be revoked and how old a person
must be are the application's. The parts take the answers as parameters — a complaint to show, a
`canUnlink`, an `onClick` or none, a `maxDate` — and draw them.

Nothing here reaches the network, stores a picture, knows what a session is or decides what a
valid profile is. It draws, and it reports what was pressed.

## Recipe: the profile

```kotlin
@Composable
fun ProfileScreen(
    summary: ProfileSummary,
    phone: String,
    staleReason: String?,
    onEdit: () -> Unit,
    onShare: () -> Unit,
    onQr: () -> Unit,
    deviceCount: Int,
    onDevices: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ArecaTheme.colors.bgCanvas)
            .verticalScroll(rememberScrollState())
            .windowInsetsPadding(WindowInsets.systemBars),
    ) {
        // Above the card it is talking about: it describes everything below it.
        if (staleReason != null) {
            StaleDetailsNotice(message = staleReason, modifier = Modifier.horizontalContentInsets())
            Spacer(Modifier.height(ArecaTheme.spacing.lg))
        }

        ProfileHeaderCard(
            summary = summary,
            modifier = Modifier.horizontalContentInsets(),
            actions = {
                ProfileCardAction(ArecaIcons.Edit, stringResource(Res.string.edit), onClick = onEdit, modifier = Modifier.weight(1f))
                ProfileCardAction(ArecaIcons.Share, stringResource(Res.string.share), onClick = onShare, modifier = Modifier.weight(1f))
                ProfileCardAction(ArecaIcons.QrCode, stringResource(Res.string.qr_code), onClick = onQr, modifier = Modifier.weight(1f))
            },
        )

        Spacer(Modifier.height(ArecaTheme.spacing.lg))

        Settings {
            // Every row carries a tile, and the tiles already separate the rows.
            Section(header = { Text(stringResource(Res.string.your_info)) }, separated = false) {
                ProfileInfoRow(value = phone, label = stringResource(Res.string.mobile), leading = { SettingsRowIcon(ArecaIcons.Phone) })
                summary.handle?.let { handle ->
                    ProfileInfoRow(value = handle, label = stringResource(Res.string.username), leading = { SettingsRowIcon(ArecaIcons.AtSign) })
                }
                EmailRow(email = summary.email, isVerified = summary.isVerified)
            }

            Section {
                SettingsRow(
                    title = { Text(stringResource(Res.string.devices)) },
                    value = { Text(deviceCount.toString()) },
                    leading = { SettingsRowIcon(ArecaIcons.Laptop) },
                    trailing = { ListItemChevron() },
                    onClick = onDevices,
                )
            }
        }
    }
}
```

A profile with more to say — a bio, a cover — opens with `ProfileCoverCard` instead:

```kotlin
ProfileCoverCard(
    summary = summary,
    bio = bio,
    cover = { AsyncImage(model = coverUrl, contentDescription = null, contentScale = ContentScale.Crop) },
    actions = { Button(style = ButtonStyle.soft(ActionColor.Neutral), size = ButtonSize.Small, onClick = onEdit) { Text(stringResource(Res.string.edit_profile)) } },
    modifier = Modifier.horizontalContentInsets(),
)
```

When the profile cannot be loaded at all there is nothing to draw, so the screen is the failure:

```kotlin
when (val state = uiState) {
    is Loaded -> ProfileScreen(…)
    is Failed -> ProfileLoadFailure(message = state.message, onRetry = viewModel::retry)
}
```

## Recipe: editing the profile

```kotlin
Column(
    modifier = Modifier
        .fillMaxSize()
        .background(ArecaTheme.colors.bgCanvas)
        .verticalScroll(rememberScrollState())
        .windowInsetsPadding(WindowInsets.systemBars)
        // The fields are low on the page; the keyboard has to lift them, not cover them.
        .imePadding(),
) {
    EditableAvatar(
        summary = summary,
        busy = upload is AvatarUploadState.Uploading,
        onChangePhoto = onChangePhoto,
        modifier = Modifier.horizontalContentInsets(),
    )

    AvatarUploadStatus(state = upload, onRetry = onRetryUpload, onDismiss = onDismissUpload, modifier = Modifier.horizontalContentInsets())

    Spacer(Modifier.height(ArecaTheme.spacing.xl2))

    Settings {
        Section(
            header = { Text(stringResource(Res.string.your_name)) },
            // The application's complaint about the name, if it has one, under the name.
            footer = state.nameError?.let { error -> { Text(error, color = ArecaTheme.colors.fgDangerText) } },
        ) {
            SettingsTextFieldRow(
                value = state.firstName,
                onValueChange = viewModel::setFirstName,
                placeholder = stringResource(Res.string.first_name_placeholder),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words, imeAction = ImeAction.Next),
            )
            SettingsTextFieldRow(value = state.lastName, onValueChange = viewModel::setLastName, placeholder = stringResource(Res.string.last_name_placeholder))
        }

        Section(footer = { Text(stringResource(Res.string.bio_footer)) }) {
            SettingsTextFieldRow(
                value = state.bio,
                onValueChange = viewModel::setBio,
                placeholder = stringResource(Res.string.bio_placeholder),
                maxLength = 140,
                singleLine = false,
            )
        }

        Section(header = { Text(stringResource(Res.string.your_info)) }, separated = false) {
            ProfileInfoRow(value = phone, label = stringResource(Res.string.change_phone_hint), onClick = onChangePhone, leading = { SettingsRowIcon(ArecaIcons.Phone) })
            ProfileInfoRow(value = summary.handle ?: stringResource(Res.string.username), label = stringResource(Res.string.username), onClick = onUsername, leading = { SettingsRowIcon(ArecaIcons.AtSign) }, trailing = { ListItemChevron() })
            BirthdayRow(value = state.birthday, onValueChange = viewModel::setBirthday)
        }
    }

    Button(modifier = Modifier.fillMaxWidth().horizontalContentInsets(), loading = state.saving, onClick = viewModel::save) {
        Text(stringResource(Res.string.save))
    }

    Settings {
        // A section of its own with no header: "Danger zone" would name a group of one.
        Section { DeleteAccountRow(onClick = onDelete, enabled = !state.saving) }
    }
}

AvatarSourceSheet(visible = pickingAvatar, options = avatarOptions, onDismiss = onAvatarSheetDismissed)
DeleteAccountSheet(visible = confirmingDelete, confirmationPhrase = stringResource(Res.string.delete_phrase), onConfirm = onDeleteConfirmed, onDismiss = onDeleteDismissed)
```

The fields, their values and whatever is wrong with them are the screen's own state; the module
holds none of it. The picture, though, is a separate transaction from the rest: a new photo takes
effect when its upload finishes, with no Save involved, so keep `AvatarUploadState` apart from the
form's state and a failed upload leaves a half-filled form untouched.

## Recipe: the username

A screen of its own, because choosing a username is a conversation with the service: its verdict
arrives while the name is still being typed, and it belongs under the field.

```kotlin
Settings {
    Section(
        header = { Text(stringResource(Res.string.username)) },
        footer = {
            UsernameCheckFooter(
                username = state.username,
                check = state.check,
                complaint = state.complaint,
                hint = stringResource(Res.string.username_rules),
            )
        },
    ) {
        SettingsTextFieldRow(
            value = state.username,
            onValueChange = viewModel::setUsername,
            placeholder = stringResource(Res.string.username),
            leading = { Text("@") },
            keyboardOptions = KeyboardOptions(autoCorrectEnabled = false, imeAction = ImeAction.Done),
        )
    }
}
```

With a `hint`, the footer's line is there from the start and a verdict replaces it; with none,
nothing is drawn until there is something to say, so the first verdict adds a line — and a verdict
or complaint longer than the hint wraps onto more. The line is neutral until there is an answer,
and announced when there is one. It checks nothing itself: the
service's verdict arrives as `check`, the application's own objections — its rules, a failed save
— as `complaint`, which outranks the verdict, and the product's rules are stated in `hint`.

## Recipe: the account menu

```kotlin
ProfileSummaryCard(
    summary = summary,
    onClick = onOpenProfile,
    modifier = Modifier.horizontalContentInsets(),
    trailing = { ProfileSummaryCardAction(label = stringResource(Res.string.edit)) },
)

Spacer(Modifier.height(ArecaTheme.spacing.lg))

Settings {
    Section(header = { Text(stringResource(Res.string.sign_in_methods)) }) {
        EmailRow(email = summary.email, isVerified = summary.isVerified, onVerify = onVerify, onClick = onChangeEmail)
        LinkedAccountRow(
            name = "Google",
            icon = { Image(painterResource(Res.drawable.google), contentDescription = null) },
            isLinked = google != null,
            account = google?.email,
            canUnlink = signInMethods > 1,
            status = googleStatus,
            onLink = { onLink(Google) },
            onUnlink = { onUnlink(Google) },
        )
    }

    Section(header = { Text(stringResource(Res.string.your_data)) }) {
        DataExportRow(state = export, onRequest = onRequestExport, onDownload = onDownloadExport)
    }

    Section(header = { Text(stringResource(Res.string.accounts)) }) {
        AccountRow(account = summary, isCurrent = true, onClick = null)
        // Keyed, so a row's press and focus stay with its account when the list changes.
        otherAccounts.forEach { other ->
            key(other.id) {
                AccountRow(account = other.summary, onClick = { onSwitch(other) }, trailing = { ListItemChevron() })
            }
        }
        AddAccountRow(onClick = onAddAccount)
    }

    // Each way out in a section of its own, so neither is pressed on the way to something else.
    Section { SignOutRow(onClick = onSignOut) }
    Section { DeleteAccountRow(onClick = onDelete) }
}

SignOutSheet(visible = confirmingSignOut, confirming = signingOut, note = unsyncedNote, onConfirm = onSignOutConfirmed, onDismiss = onSignOutDismissed)
```

`LinkedAccountRow` takes the provider's mark as a slot. Brand marks have fixed colours and usage
terms of their own; the application that agreed to those terms supplies the artwork.

`canUnlink = false` keeps the row from offering to remove the account's last way to sign in. The
module cannot count sign-in methods — only the application knows about passwords and providers it
has not drawn — so the application answers the question.

While the account loads, `ProfileSummaryCardPlaceholder()` stands in at the card's final height. A
caption of your own — synchronisation state, a plan name — goes in the card's `caption` slot as a
`ProfileSummaryCaption`.

## Recipe: devices

```kotlin
Settings {
    ActiveSessionsSections(
        state = sessions,
        onSessionClick = { session -> revoking = session },
        onSignOutOthers = { confirmingSignOutOthers = true },
        onRetry = viewModel::reloadSessions,
        footer = stringResource(Res.string.devices_footer),
    )

    Section(header = { Text(stringResource(Res.string.auto_sign_out)) }) {
        SettingsSelectRow(
            title = { Text(stringResource(Res.string.if_inactive_for)) },
            options = inactivityOptions,
            selected = inactivity,
            onSelect = viewModel::setInactivity,
            optionLabel = { it.label },
        )
    }
}

RevokeSessionSheet(
    session = revoking,
    onConfirm = { session -> revoking = null; viewModel.revoke(session) },
    onDismiss = { revoking = null },
)
SignOutOthersSheet(
    visible = confirmingSignOutOthers,
    onConfirm = { confirmingSignOutOthers = false; viewModel.signOutOthers() },
    onDismiss = { confirmingSignOutOthers = false },
)
```

Each sheet closes as soon as it is answered, and the row's own spinner — `SessionItem.isRevoking`,
`ActiveSessionsState.Loaded.isSigningOutOthers` — shows the work. To keep a sheet up with its
spinner instead, pass `confirming` and clear the session or `visible` when the work ends.

Signing out of *this* device is deliberately not in these sections. It is a different act from
revoking a session — it clears local data too — and it lives in the account menu as `SignOutRow`.

## Recipe: changing the photo, with cropping

Pick, crop, upload. The module draws the first and the last step; the crop in between is the
application's own crop screen from `:ui:crop` (see its README), opened with the spec this product
wants for avatars. `flow-profile` does not depend on the cropper — a product that never crops leaves
the middle step out and uploads the pick, as under [Picture choices](#picture-choices).

```kotlin
// The application's, once: what cropping an avatar means in this product.
val AvatarCrop = CropSpec.Avatar // or CropSpec.Avatar.copy(mask = SquircleShape(0.24f), maxOutputDimension = 1024)

var choosingSource by remember { mutableStateOf(false) }
var picked by remember { mutableStateOf<Painter?>(null) }

EditableAvatar(
    summary = summary,
    busy = upload is AvatarUploadState.Uploading,
    onChangePhoto = { choosingSource = true },
    changePhotoLabel = stringResource(Res.string.change_photo),
)
AvatarUploadStatus(state = upload, onRetry = viewModel::retryUpload, onDismiss = viewModel::dismissUploadFailure)

AvatarSourceSheet(
    visible = choosingSource,
    options = buildList {
        if (camera.isAvailable) {
            add(AvatarSourceOptions.camera { choosingSource = false; scope.launch { picked = camera.capture()?.decode() } })
        }
        add(AvatarSourceOptions.library { choosingSource = false; scope.launch { picked = picker.pick()?.decode() } })
        if (summary.avatar != null) add(AvatarSourceOptions.remove { choosingSource = false; viewModel.removeAvatar() })
    },
    onDismiss = { choosingSource = false },
)

// Shown however the application navigates: a destination, or over the screen in a CropDialog as here.
picked?.let { photo ->
    CropDialog(onDismissRequest = { picked = null }) {
        CropScreen(
            photo = photo,
            spec = AvatarCrop,
            title = stringResource(Res.string.move_and_scale),
            onCancel = { picked = null },
            onDone = { crop -> picked = null; viewModel.uploadAvatar(crop) },
            // Square and unmasked: EditableAvatar clips it to its circle wherever it is shown.
            applyMask = false,
        )
    }
}
```

**Store the square.** `EditableAvatar` and `ProfileAvatar` clip the picture to their circle, so the
crop is rendered rectangular (`applyMask = false`). The stored picture then suits whatever shape the
product draws avatars in later; the spec's mask is only the guide the person frames against.

**A different avatar shape is a different spec**, and nothing else changes: the parts above take the
same arguments for a squircle product as for a circle one.

## Pictures

The module has no image loader and never will: an application that uses Coil should not have to
take a second one, and an application that does not use Coil should not have to take the first.
Every part that draws a picture reads one from `LocalAvatarImageLoader` instead, and the
application provides it once, above everything:

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

CompositionLocalProvider(LocalAvatarImageLoader provides CoilAvatarImageLoader) {
    App()
}
```

`ProfileSummary.avatar` is whatever that loader understands — a URL, bytes, a file, a platform URI.
The module passes it through and never looks at it.

| | |
| --- | --- |
| `Loading` | The initials, under the avatar's own spinner |
| `Ready` | The picture, cropped to the circle |
| `Failed` | The initials. Never an empty circle — that reads as a picture still arriving, forever |

With no loader provided, `AvatarImageLoader.None` answers `Failed` for everything, so an application
with no pictures yet gets initials rather than a spinner that never stops. An application that
already holds decoded pictures can hand a state to one avatar directly:
`ProfileAvatar(name = "Ada Lovelace", state = AvatarImageState.Ready(BitmapPainter(bitmap)))`.

The cover card's band is a slot, not a model: a cover is the product's artwork as much as the
person's, and the product decides how it is fetched and cropped.

## Picture choices

`AvatarSourceSheet` offers exactly the options it is given. `AvatarSourceOptions` builds the common
three with the module's wording and glyphs; anything else — an avatar set, an import from a linked
account — is an `AvatarSourceOption` of the product's own. A camera option that opens nothing is
worse than none, so list only what the current platform can complete:

```kotlin
var picking by remember { mutableStateOf(false) }

AvatarSourceSheet(
    visible = picking,
    options = buildList {
        if (camera.isAvailable) {
            add(AvatarSourceOptions.camera { picking = false; scope.launch { camera.capture()?.let(viewModel::upload) } })
        }
        add(AvatarSourceOptions.library { picking = false; scope.launch { picker.pick()?.let(viewModel::upload) } })
        add(AvatarSourceOption(label = stringResource(Res.string.avatar_set), icon = ArecaIcons.FaceSmile, onClick = { picking = false; onAvatarSet() }))
        if (summary.avatar != null) add(AvatarSourceOptions.remove { picking = false; viewModel.removeAvatar() })
    },
    onDismiss = { picking = false },
    title = stringResource(Res.string.change_photo),
)
```

Capturing, picking, cropping and uploading are all the application's. The sheet reports which
source was chosen and does not close itself, so every choice clears the state that shows it;
`AvatarUploadStatus` shows what happened next. To crop between the pick and the
upload, see [Recipe: changing the photo, with cropping](#recipe-changing-the-photo-with-cropping).

## Sheets are driven by state

Every sheet takes `visible` — or, for `RevokeSessionSheet`, the session itself — and reports
`onDismiss`. Whether it is up belongs to the caller's state, not to the sheet, so a confirmation
survives the screen being rebuilt and cannot get out of step with the work it is confirming.
Dismissing by the scrim, by the back gesture and by Cancel all arrive as `onDismiss`.

A sheet whose work is in flight takes `confirming = true`: the confirming button shows its spinner
and neither button accepts a second press — nor does the scrim, so clear it when the work ends,
however it ends.

A confirmation is answered once. Its buttons, the scrim, a drag and back count only while the
sheet rests open, and only the first of them counts: a press while it rises or slides away is
ignored, so a double tap on Cancel cannot land on the confirming button as the sheet drops. So
answer `onConfirm` by closing the sheet or by setting `confirming` — a sheet left up with neither
takes no further press. `RevokeSessionSheet` confirms only the session it shows, and only while
`session` is set.

Every sheet's words are parameters — `title`, `confirmText` and `cancelText` with English
defaults, `description` and `note` with none — so a product whose deletion is reversible says so
in the sheet itself, and one with nothing at stake simply passes no note. `RevokeSessionSheet`'s
`description` is a function of the session, by default its name, client and last-seen line joined
by " · ".

Each sheet carries at most one coloured thing, the note that says what is at stake, and the
confirming button is the neutral dark one every primary action uses. `DeleteAccountSheet(
confirmationPhrase = "DELETE")` adds a box that must hold that phrase — case and surrounding spaces
ignored — before the button enables. Whether deletion is instant, scheduled or reversible is the
application's to say, in `description`.

## Neutral by default

Nothing the module draws is in the brand colour; only foundation's focus ring, shared by every
Areca control, is. Tiles are neutral, the verified tick is the inverted neutral disc, the current
device's tile is the one dark disc on the devices card, the upload bar fills with that same
inverted neutral, and links and values are the list's own ink. Colour is kept for what it means: the danger tone on the ways out,
danger and success in the username verdict, the warning in a sign-out note that has something to
lose. A product that wants brand accents passes them to the settings list — see
`SettingsDefaults.styles` — and the rows follow.

## Words

There is no string table. Every word a part draws is a parameter of that part, and the application
passes its own — from its `composeResources`, in its language — where it composes the part:

```kotlin
DeleteAccountRow(
    onClick = onDelete,
    title = stringResource(Res.string.delete_account),
    subtitle = stringResource(Res.string.delete_account_cost),
)

UsernameCheckFooter(
    username = state.username,
    check = state.check,
    hint = stringResource(Res.string.username_rules),
    availableText = { name -> stringResource(Res.string.username_available, name) },
)

BirthdayRow(
    value = birthday,
    onValueChange = onBirthday,
    formatDate = dateFormatter::format,
    calendarLabels = calendarLabels,
    startDayOfWeek = firstDayOfWeek,
)
```

Two kinds of words, two rules:

- **Words the part cannot do without** — a row's title, a button's label, a sheet's question, the
  label of an icon-only control such as `EditableAvatar`'s `changePhotoLabel`, and the description
  of a mark with no words beside it such as `verifiedDescription` and `currentDescription` — have a
  plain English default, so a part drops in and works before the application has translated it.
  Override every one of them in a localised application.
- **Words that are optional** — a subtitle, a footer, a sheet's description and note, a hint, an
  accessibility label for something already visible — default to `null`, and `null` draws nothing.
  The part never invents copy the product did not write.

Anything that names or formats something takes a function rather than a string — the username in
the verdict (`availableText`, `takenText`), the phrase in `DeleteAccountSheet`'s
`confirmationPrompt`, the session in `RevokeSessionSheet`'s `description`, the upload percentage
(`formatProgress`), the birthday (`formatDate`) — so a translation decides where the value goes and
a locale decides how a date reads. All but `formatDate` are composable, so they can call
`stringResource` with the value, as above; `formatDate` is a plain `(LocalDate) -> String`.

The birthday calendar's own words — its month and weekday names, each day's description and its
navigation actions, all read out by assistive technology — come from `calendarLabels`, a
`CalendarLabels` from `dev.voir.areca:datetime`, and the first day of its week from
`startDayOfWeek`. Both default to English and Monday.

Device names, session details ("Belgrade, Serbia · 10:18"), the member-since caption and every
failure message arrive already worded. They come from the application's data, and only the
application knows how to phrase them.

## Birthdays and `kotlinx-datetime`

`BirthdayRow` uses `kotlinx.datetime.LocalDate`, so this module puts `kotlinx-datetime` on your API
surface through `dev.voir.areca:datetime`. The calendar opens on the date in force and cannot page
outside `minDate`–`maxDate` — up to today by default, since a birthday is in the past; a product
with an age requirement moves `maxDate` back. A `null` limit falls back to the calendar's own range,
1 January 1900 to 31 December 2100.

The sheet's heading holds only its title. Its actions are in the action bar at its foot, as on
every other sheet: Save (`applyLabel`) on the right, and on the left Remove (`removeLabel`) while a
date is in force, or Cancel (`cancelLabel`) while there is none to remove. With `removable = false`
Cancel is always the left action. While Remove is offered there is no Cancel button; the scrim, a
drag or back closes the sheet without saving.

A tap marks a day and Save commits it, so a mis-tap is never the answer. With no date in force
nothing is marked when the sheet opens — the calendar shows today's month, pulled inside the
limits — and Save stays disabled until a day is tapped, so an untouched calendar never saves today
as somebody's birthday.

## Accessibility

The parts carry what applications usually miss:

- A fact row reads as one item — "@ada, Username" — and a pressable one as one button.
- The avatar and its camera badge are one target, announced once with `changePhotoLabel` —
  "Change photo" unless you pass your own; the badge alone would be under the minimum touch size.
- The verified tick and the current-account tick are always announced, with `verifiedDescription`
  ("Verified") and `currentDescription` ("Current account") — pass your translations.
- Every pressable card and row the module ships takes an `onClickLabel` saying what pressing does;
  `ActiveSessionsSections` takes `sessionClickLabel` for its sessions and
  `signOutOthersClickLabel` for the row that ends them. These are optional, since
  the row's own words are read out anyway, and worth passing where they do not say the action.
- The "Verify" link on an unconfirmed address is foundation's `SettingsRowLink`: underlined, so it
  is not mistaken for a value, and a target of at least 48 dp inside the row.
- `ProfileGreeting` has no press tint, but it is a button to assistive technology and shows the
  focus ring when keyboard focus reaches it.
- The upload band's heading and the username verdict — or the complaint in its place — are
  announced as they appear and whenever they change, not only drawn. The upload percentage is not,
  and neither are the username hint and the checking line, so neither a running upload nor typing
  talks over everything else.
- Loading placeholders are read as one item each — `contentDescription` on
  `ProfileSummaryCardPlaceholder` and `ProfileGreetingPlaceholder`, `loadingDescription` on
  `ActiveSessionsSections` — rather than as nothing at all.
- `DeleteAccountSheet`'s box is labelled with its instruction ("Type DELETE to confirm."), not only
  by its placeholder.
- A row with nothing to do takes no `onClick` and is not pressable, rather than a control that
  silently does nothing — `ActiveSessionsSections` leaves this device inert, and the account in use
  is usually passed `onClick = null`.
- Card and section headings carry the heading role.

## Where to look

The playground renders all of this live. Under **Profile flow → Screens**, the recipe screens —
[`ProfileShowcase`](../../playground/src/commonMain/kotlin/dev/voir/areca/playground/flow/profile/ProfileShowcase.kt),
[`EditProfileShowcase`](../../playground/src/commonMain/kotlin/dev/voir/areca/playground/flow/profile/EditProfileShowcase.kt),
[`ChangePhotoShowcase`](../../playground/src/commonMain/kotlin/dev/voir/areca/playground/flow/profile/ChangePhotoShowcase.kt),
[`UsernameShowcase`](../../playground/src/commonMain/kotlin/dev/voir/areca/playground/flow/profile/UsernameShowcase.kt),
[`AccountShowcase`](../../playground/src/commonMain/kotlin/dev/voir/areca/playground/flow/profile/AccountShowcase.kt)
and
[`DevicesShowcase`](../../playground/src/commonMain/kotlin/dev/voir/areca/playground/flow/profile/DevicesShowcase.kt)
— hold the same assemblies as compiled code, so they cannot drift from something that builds. The
group's other sections give every public part a page of its own, in
[`playground/flow/profile`](../../playground/src/commonMain/kotlin/dev/voir/areca/playground/flow/profile).
