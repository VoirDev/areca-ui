# `flow-attachments`

Parts for letting people attach files to something — a strip of files on a form, the files on a
record's details, the sheets that add one, and a viewer that swipes through them — and recipes for
putting them together.

```kotlin
implementation("dev.voir.areca:flow-attachments:<version>")
```

Areca is published to GitHub Packages, so the build needs its repository first; see
[Installing](../../README.md#installing).

Every recipe below is also a compiled page in the playground, under **Attachments flow → Screens**,
driven by a sample application that picks, copies, uploads and downloads offline.

The module passes `foundation` on, so the recipes' `ConfirmSheet`, `Settings` rows and `Button` need
nothing more. The `ArecaIcons.*` icons in them (`Trash`, `Paperclip`, `Plus`) come from
`dev.voir.areca:icons`, which nothing here depends on; add it only to use them.

## What this is not

It is not an `AttachmentsScreen`, and it is not a file manager.

Every application that attaches files decides the same things differently: what a file is called
under its square — its name, or a word its owner chose — how many there may be and how large, whether
a removal asks first, whether uploads show, where a file can come from on this device. The module
ships the pieces, each carrying what applications reliably get wrong, and this file ships the
assemblies. **Copy a recipe and edit it.**

Picking, capturing, copying, uploading, downloading, sharing and saving are all the application's.
A part reports an intent — `onAdd`, `onOpen(item)`, `onRemove(item)` — and draws the state it is
given. It has no connection to `flow-auth` or `flow-profile`. See [`../FLOWS.md`](../FLOWS.md).

## One file, as the module sees it

Every part draws an `AttachmentItem`: what the application already knows about a file, worded.

```kotlin
AttachmentItem(
    key = attachment.id,
    fileName = "IMG_2041.jpg",
    label = stringResource(Res.string.kind_receipt), // defaults to the file name
    size = formatFileSize(attachment.size),           // worded by the application, or null
    format = AttachmentFormat.of(attachment.contentType, attachment.fileName),
    state = AttachmentState.Ready,
    model = attachment.localUri,                      // null while the bytes are not here
)
```

`AttachmentFormat` decides how a file is drawn: a picture zoomable, a PDF page by page, anything
else named and handed to another application. `AttachmentFormat.of` reads it from the media type,
and from the extension when the type is missing or the generic one a picker falls back to.

`AttachmentState` says where the bytes are:

| | |
| --- | --- |
| `Adding` | Being saved. A veil and a spinner over the square; it cannot be opened yet |
| `Uploading(progress)` | Here, on its way to the server. Opens like `Ready` |
| `Ready` | Here |
| `Remote` | On the server only. Opening it is asking for it |
| `Downloading(progress)` | Arriving |
| `Waiting` | Wanted, but the server cannot be reached. Ordinary, not an error |
| `Unavailable` | Gone for good |

Report only the states your application has. One whose files never leave the device uses `Ready`
alone; one that does not want uploads on screen never reports `Uploading`.

## The pieces

### Squares and the strip

| | |
| --- | --- |
| `AttachmentThumbnail` | The square a file is recognised by: the picture where there is one, its glyph and extension where not, and a mark for where its bytes are |
| `AttachmentTile` | One file in a strip: its square, a caption, an optional second line and an optional remove mark |
| `AttachmentAddTile` | The square that adds a file, shaped like the files beside it |
| `AttachmentStrip` | A record's files as a row of squares that scrolls sideways, from nothing to many |
| `AttachmentTileStyle` | Colours and shape of every square, neutral by default |

### Rows and marks

| | |
| --- | --- |
| `AttachmentRow` | One file as a foundation `SettingsRow`, for lists read by name |
| `AttachmentIndicator` | A paperclip, and optionally how many, on something with files |

### Sheets

| | |
| --- | --- |
| `AttachmentSourceSheet` | Where a new file comes from, with `AttachmentSourceOptions.camera`, `library` and `files` |
| `PendingAttachmentSheet` | Asked once a file is picked: the file, a question slot, and why it cannot be attached when it cannot |
| `AttachmentTagChips` | What a file is, chosen in one tap from the product's own tags |
| `AttachmentOptionsSheet` | What can be done to one file, with `AttachmentOptions.rename`, `share`, `save`, `openElsewhere` and `remove` |
| `AttachmentRenameSheet` | A new name, editing only the part before the extension |
| `AttachmentChoice` | One choice on the source or options sheet, for a choice the product adds — scan a document, import from a drive |

Removing a file where removing is final asks first through foundation's `ConfirmSheet`; the module
does not ship a second one.

### The viewer

Always dark, edge to edge; see [The viewer](#the-viewer-1).

| | |
| --- | --- |
| `AttachmentViewer` | A record's files one at a time, full screen: a pager, a heading, a filmstrip, and a line over the file |
| `AttachmentViewerPage` | One file: a zoomable picture, a column of zoomable PDF pages, or the file stated when it cannot be drawn |
| `AttachmentViewerStyle` | The viewer's ground and the surfaces laid on it; dark whatever the theme |

## Styling

Each style has a `Default`, which reads the theme in scope, and a `default(…)` factory that replaces
any colour of it. A part given no style reads the style in scope from a nullable composition local
and falls back to `Default`:

| Style | Local | Read by |
| --- | --- | --- |
| `AttachmentTileStyle` | `LocalAttachmentTileStyle` | `AttachmentThumbnail`, `AttachmentTile`, `AttachmentAddTile`, `AttachmentStrip`, `AttachmentRow` and the square of `PendingAttachmentSheet` |
| `AttachmentViewerStyle` | `LocalAttachmentViewerStyle` | `AttachmentViewer` and `AttachmentViewerPage` |

These locals are not seeded by foundation's `ArecaThemeProvider`: that provider lives in
`:foundation`, which does not depend on this module. Provide them yourself around the part of the
application that wants a different default:

```kotlin
CompositionLocalProvider(
    LocalAttachmentTileStyle provides AttachmentTileStyle.default(bg = ArecaTheme.colors.bgSurface),
) {
    // every square in here sits on white
}
```

`AttachmentRow` falls back to its own square — the settings list's tile shape on the inset fill —
rather than to `Default`, so a row reads against a section's card; a provided style replaces that
too.

## Recipe: the sheets and the viewer

Every screen that shows files draws this once, beside whatever holds them — a strip, a list, a
button. The adding sheets come first, then the viewer with the sheets that act on a file inside it.
One owner, usually the screen's ViewModel, answers everything.

```kotlin
@Composable
fun AttachmentSheets(state: AttachmentsState, viewModel: AttachmentsViewModel, confirmRemoval: Boolean) {
    AttachmentSourceSheet(
        visible = state.choosingSource,
        options = picker.sources.map { source ->
            when (source) {
                Source.Camera -> AttachmentSourceOptions.camera { picker.launch(source) }
                Source.Library -> AttachmentSourceOptions.library { picker.launch(source) }
                Source.Files -> AttachmentSourceOptions.files { picker.launch(source) }
            }
        },
        onDismiss = viewModel::dismissSources,
        subtitle = stringResource(Res.string.attachment_limits, state.maxSize),
    )

    // The tag is asked, never guessed: nothing is chosen when the sheet opens, and Attach waits
    // for one — and for the copy.
    var tag by remember(state.pending?.fileName) { mutableStateOf<Kind?>(null) }
    PendingAttachmentSheet(
        file = state.pending,
        onAttach = { tag?.let(viewModel::attach) },
        onDismiss = viewModel::cancelPending,
        title = stringResource(Res.string.what_is_this),
        attachEnabled = tag != null,
        copyingText = stringResource(Res.string.adding),
        question = { AttachmentTagChips(Kind.entries, tag, { tag = it }, { it.label() }) },
    )

    if (state.viewing != null) {
        AttachmentViewer(
            items = state.files,
            initialKey = state.viewing,
            onDismiss = viewModel::closeViewer,
            onRequest = viewModel::download,
            onShare = handoff::share,
            onMore = viewModel::showOptions,
            notice = state.notice,
            onNoticeDismiss = viewModel::dismissNotice,
            page = { item ->
                AttachmentViewerPage(
                    item = item,
                    onRetry = { viewModel.download(item) },
                    onOpenElsewhere = { handoff.open(item) },
                    onSave = { saver.save(item) },
                    waitingText = stringResource(Res.string.downloads_when_online),
                )
            },
            sheets = {
                val item = state.optionsFor
                AttachmentOptionsSheet(
                    item = item,
                    choices = buildList {
                        add(AttachmentOptions.rename(onClick = viewModel::startRename))
                        if (item?.state == AttachmentState.Ready) add(AttachmentOptions.save { saver.save(item) })
                        add(AttachmentOptions.remove { viewModel.startRemove(confirm = confirmRemoval) })
                    },
                    onDismiss = viewModel::dismissOptions,
                    tagsTitle = stringResource(Res.string.type),
                    tags = { AttachmentTagChips(Kind.entries, kindOf(item), { viewModel.retag(item, it) }, { it.label() }) },
                )
                AttachmentRenameSheet(
                    item = state.renaming,
                    onRename = viewModel::rename,
                    onDismiss = viewModel::dismissRename,
                    canSave = { it.isNotBlank() },
                )
                ConfirmSheet(
                    visible = state.removing != null,
                    icon = ArecaIcons.Trash,
                    title = stringResource(Res.string.remove_attachment_title),
                    description = stringResource(Res.string.remove_attachment_description),
                    confirmText = stringResource(Res.string.remove),
                    onConfirm = viewModel::confirmRemove,
                    onDismiss = viewModel::dismissRemove,
                )
            },
        )
    }
}
```

`confirmRemoval` is the one thing that differs between screens: a saved record removes a file at
once and so asks first; a form saves nothing until it is saved, so taking a file off is as
reversible as typing and asks nothing.

Where the device offers only one source — a desktop picks files and nothing else — start that pick
from the add square directly instead of raising a sheet with one choice.

## Recipe: a form

The files of a record being written, in a strip under its fields, saved with the form.

```kotlin
Column(
    modifier = Modifier
        .fillMaxSize()
        .background(ArecaTheme.colors.bgCanvas)
        .verticalScroll(rememberScrollState())
        .windowInsetsPadding(WindowInsets.systemBars)
        .imePadding(),
) {
    Settings {
        Section {
            SettingsTextFieldRow(value = state.amount, onValueChange = viewModel::setAmount, placeholder = stringResource(Res.string.amount))
            SettingsTextFieldRow(value = state.merchant, onValueChange = viewModel::setMerchant, placeholder = stringResource(Res.string.merchant))
            SettingsTextFieldRow(value = state.note, onValueChange = viewModel::setNote, placeholder = stringResource(Res.string.note), singleLine = false)
        }
    }

    Spacer(Modifier.height(ArecaTheme.spacing.lg))

    // Full width, not inset: the strip scrolls under both screen edges while its first square
    // keeps to the gutter.
    AttachmentStrip(
        items = files.items,
        onOpen = viewModel::open,
        onAdd = if (files.items.size < files.limit) viewModel::add else null,
        onRemove = viewModel::remove,
        title = stringResource(Res.string.attachments),
        counter = if (files.items.isEmpty()) null else stringResource(Res.string.count_of_limit, files.items.size, files.limit),
        emptyTitle = stringResource(Res.string.attachments_empty_title),
        emptyDescription = stringResource(Res.string.attachments_empty_description, files.maxSize),
        failure = files.failure,
        caption = { item -> if (item.state == AttachmentState.Adding) stringResource(Res.string.adding) else item.label },
        addCaption = stringResource(Res.string.add),
    )

    Button(modifier = Modifier.fillMaxWidth().horizontalContentInsets(), loading = state.saving, onClick = viewModel::save) {
        Text(stringResource(Res.string.save))
    }
}

AttachmentSheets(files, viewModel, confirmRemoval = false)
```

Nothing a form does to its files is final until the form is saved, so the ViewModel holds them as a
draft and saves them with the record. A file refused on the way in — too large, unreadable — says
why on the pending sheet; a file that failed to save says why under the strip, in `failure`.

## Recipe: a record's details

The files of a saved record, larger and stating their sizes, with every change saved at once.

```kotlin
AttachmentStrip(
    items = files.items,
    onOpen = viewModel::open,
    title = stringResource(Res.string.attachments),
    failure = files.failure,
    tileSize = ArecaTheme.spacing.xl9,
    detail = { item -> item.size },
)

Settings {
    Section {
        SettingsRow(
            title = { Text(stringResource(Res.string.attach_file)) },
            leading = { SettingsRowIcon(ArecaIcons.Paperclip) },
            enabled = files.items.size < files.limit,
            onClick = viewModel::add,
        )
    }
}

AttachmentSheets(files, viewModel, confirmRemoval = true)
```

The strip adds and removes nothing here, and draws nothing at all while there are no files: adding
is a row of the screen's own, removing is in the viewer's options, behind a question. The sizes are
stated because a file not yet on this device costs its size to open — the viewer fetches it when it
lands on it. Inside a sheet already padded by the gutter, pass `bleed = true`.

## Recipe: documents

A record that carries many files, read by name and grouped by what they are.

```kotlin
Settings {
    Section(footer = { Text(stringResource(Res.string.files_count, files.items.size, files.limit)) }) {
        SettingsRow(
            title = { Text(stringResource(Res.string.add_document)) },
            leading = { SettingsRowIcon(ArecaIcons.Plus) },
            onClick = viewModel::add,
        )
    }

    Kind.entries.forEach { kind ->
        val ofKind = files.items.filter { kindOf(it) == kind }
        if (ofKind.isNotEmpty()) {
            Section(header = { Text(kind.label()) }) {
                ofKind.forEach { item ->
                    AttachmentRow(item = item, onClick = { viewModel.open(item) }, title = item.fileName, subtitle = item.size)
                }
            }
        }
    }
}

AttachmentSheets(files, viewModel, confirmRemoval = true)
```

Retagging a file in the viewer's options moves it to its new section. Where its bytes are is said at
each row's end.

## Recipe: cropping a photo before it's attached

The pending sheet offers Edit for the files the application crops; the application's own crop
screen from `:ui:crop` (see its README) does the cropping, and the crop comes back as the file. The
module never depends on the cropper — a product that crops nothing never passes `onEdit`.

```kotlin
// The application's, once: what cropping an attachment means in this product.
val ReceiptCrop = CropSpec.Photo.copy(maxOutputDimension = 2048)

PendingAttachmentSheet(
    // Down while the crop screen has the file; back up with the crop.
    file = state.pending.takeIf { state.editing == null },
    onAttach = { tag?.let(viewModel::attach) },
    onDismiss = viewModel::cancelPending,
    title = stringResource(Res.string.what_is_this),
    attachEnabled = tag != null,
    copyingText = stringResource(Res.string.adding),
    // The application's rule, per file: photos can be cropped, PDFs and documents cannot.
    onEdit = if (state.pending?.format == AttachmentFormat.Image) viewModel::editPending else null,
    question = { AttachmentTagChips(Kind.entries, tag, { tag = it }, { it.label() }) },
)

// Shown however the application navigates: a destination, or over the screen in a CropDialog as here.
state.editing?.let { file ->
    CropDialog(onDismissRequest = viewModel::cancelEdit) {
        CropScreen(
            photo = decoder.decode(file.model),
            spec = ReceiptCrop,
            title = stringResource(Res.string.crop_photo),
            onCancel = viewModel::cancelEdit,
            // The crop replaces the copy: written to the application's storage, it becomes the
            // pending file's model, and the sheet comes back showing it.
            onDone = viewModel::replacePending,
        )
    }
}
```

The rule for what may be edited is as narrow as the product wants: by format, by the chosen tag, by
where the screen is. A product with several kinds of picture opens the same crop screen with a
different `CropSpec` for each.

## The strip

One part covers a record's files wherever they appear. What it offers is decided by what you pass:
`onAdd` draws the add square, `onRemove` draws the remove marks, and leaving either out takes it
away — at a limit, or where the files are only shown. Every word is yours.

On the form that writes the record, where nothing is saved until the form is:

```kotlin
AttachmentStrip(
    items = files,
    onOpen = { item -> viewer.open(item.key) },
    onAdd = if (files.size < limit) viewModel::chooseSource else null,
    onRemove = { item -> viewModel.remove(item.key) },
    title = stringResource(Res.string.attachments),
    counter = if (files.isEmpty()) null else stringResource(Res.string.count_of_limit, files.size, limit),
    emptyTitle = stringResource(Res.string.attachments_empty_title),
    emptyDescription = stringResource(Res.string.attachments_empty_description, maxSize),
    failure = state.failure?.let { stringResource(it) },
    addCaption = stringResource(Res.string.add),
)
```

On the record's details, where the files are what was opened to be looked at — larger squares with
their sizes, nothing to add or remove here, and `bleed` because the sheet is already padded:

```kotlin
AttachmentStrip(
    items = files,
    onOpen = { item -> viewer.open(item.key) },
    title = stringResource(Res.string.attachments),
    tileSize = ArecaTheme.spacing.xl9,
    detail = { item -> item.size },
    bleed = true,
)
```

With no files, nothing to add and no failure, the strip draws nothing at all, title included. A
failure is stated under the squares and announced; it is also drawn with no files, because that is
where the file that failed would have been.

The squares are the subtle page grey, which reads on white. On a grey sheet, pass
`style = AttachmentTileStyle.default(bg = ArecaTheme.colors.bgSurface)`, or provide that style once
through `LocalAttachmentTileStyle` — see [Styling](#styling).

**Squares are captioned with `label`**, the word to scan a strip for, and read out with the file's
name — or with what `description` words. Pass `caption = { item -> if (item.state == AttachmentState.Adding) adding else item.label }`
to say a file is still being added.

## Rows

Where files are read by name — a dozen documents on a record, the files on a ticket — a strip asks
someone to open each one to find the one they want. `AttachmentRow` is a `SettingsRow`, so it goes
in a `Section` beside the application's own rows. That suits a record's files, which a limit keeps
to a handful; a library that grows without one belongs in a `LazyList` or a `PagedContentList` of
`ListItem` rows instead, since a section composes every row at once and never pages:

```kotlin
Settings {
    Section(header = { Text(stringResource(Res.string.documents)) }) {
        files.forEach { item ->
            AttachmentRow(
                item = item,
                onClick = { viewer.open(item.key) },
                title = item.fileName,
                // A file whose size is not known yet states its label alone.
                subtitle = listOfNotNull(item.label, item.size).joinToString(" · "),
            )
        }
    }
}
```

The row's square carries no marks. Where the bytes are is said at the row's end instead, and a file
still being added shows the list's spinner there and cannot be pressed.

`AttachmentIndicator` marks something that has files — an activity row, a message — with a
paperclip and an optional count. It takes the colour and text style of the line it sits in; put it
on the line the list is not read by.

## Adding a file

Three steps, each the application's to move on from: where the file comes from, the pick itself,
and what the file is.

```kotlin
AttachmentSourceSheet(
    visible = choosingSource,
    options = buildList {
        if (picker.canCapture) add(AttachmentSourceOptions.camera { pick(Source.Camera) })
        if (picker.canPickPhotos) add(AttachmentSourceOptions.library { pick(Source.Library) })
        add(AttachmentSourceOptions.files { pick(Source.Files) })
    },
    onDismiss = { choosingSource = false },
    subtitle = stringResource(Res.string.attachment_limits, maxSize),
)
```

List only the sources the platform can complete. Where only one is left — a desktop with no camera
and no library — skip the sheet and open that one from the add square: a sheet with one choice is a
tap for nothing.

The pick copies the file in and hands back a `PendingAttachment`. It is up while the copy runs, so
its owner can answer before it finishes; Attach shows its spinner and waits.

```kotlin
var tag by remember(pending?.fileName) { mutableStateOf<ReceiptKind?>(null) }

PendingAttachmentSheet(
    file = pending,
    onAttach = { tag?.let(viewModel::attach) },
    onDismiss = viewModel::cancelPending,
    title = stringResource(Res.string.what_is_this),
    attachEnabled = tag != null,
    copyingText = stringResource(Res.string.adding),
    question = {
        AttachmentTagChips(
            tags = ReceiptKind.entries,
            selected = tag,
            onSelect = { tag = it },
            label = { it.label() },
        )
    },
)
```

**The kind is asked, never guessed.** Nothing about where a file came from says what it is, and a
wrong guess filed silently is worse than one tap — so nothing is chosen when the sheet opens. A
product that tags nothing leaves `question` out, and the sheet only confirms.

**Editing is optional, and per file.** Pass `onEdit` and the file's row offers Edit once the copy
exists; pass `null` — the default — and it does not. The module edits nothing: what Edit opens, and
for which files, is the application's. See [Recipe: cropping a photo before it's
attached](#recipe-cropping-a-photo-before-its-attached).

A file that cannot be attached — too large, unreadable, not allowed — carries the reason, worded
by the application, in `PendingAttachment.problem`. The sheet states it in place of the question,
announces it, and offers only Close. The module never decides a file is too large.

## Acting on a file

`AttachmentOptionsSheet` is headed by the file and lists only what applies to it — there is nothing
to share or save while a file's bytes are elsewhere. Where the product tags files, the tag leads the
sheet and changes in one tap, with the sheet staying up:

```kotlin
AttachmentOptionsSheet(
    item = optionsFor,
    choices = buildList {
        add(AttachmentOptions.rename { renaming = optionsFor; optionsFor = null })
        if (optionsFor?.state == AttachmentState.Ready) {
            add(AttachmentOptions.share { share(optionsFor) })
            add(AttachmentOptions.save { save(optionsFor) })
        }
        add(AttachmentOptions.remove { removing = optionsFor; optionsFor = null })
    },
    onDismiss = { optionsFor = null },
    tagsTitle = stringResource(Res.string.type),
    tags = { AttachmentTagChips(ReceiptKind.entries, kindOf(optionsFor), viewModel::changeKind, { it.label() }) },
)
```

`AttachmentRenameSheet` edits the name without its extension — a rename that dropped `.pdf` would
leave a file nothing can open — and puts it back on save. It opens focused, with the name selected.
Whether a name is acceptable is yours: `canSave = { it.isNotBlank() }` refuses an empty one, and
`error` states what was wrong with the last attempt.

Every sheet is driven by state — a `visible` flag, or the file it is about — reports `onDismiss`,
and does not close itself when a choice is made. What it drew stays on screen while it slides away.

Every sheet is headed by the foundation's `TopBar` in its modal style, so its controls sit where
every other sheet and screen in the application puts them. A list of choices — where a file comes
from, what to do with one — closes from the × at the end of its heading. The pending and rename
sheets are small forms: Cancel and Attach, or Cancel and Save, sit in the sheet's foot — the
foundation's `FlowModalActionBar`, as on every other sheet that commits a draft — and their heading
only names the question. The sheet keeps clear of the keyboard, so the foot rides above the one the
rename box opens with.

## The viewer

**Always dark, edge to edge.** Like the application's crop screen, the viewer is dark whatever the
application's theme: the file sits on black, and its bar, its pages, its notices and the sheets
opened in `sheets` are drawn in foundation's `ArecaDarkTheme`, which keeps the application's
palette, type and shapes. Its window runs behind the status and navigation bars and paints them
black with foundation's `SystemBars`; on Android that also asks for light bar icons, turns off the
contrast scrim and dims nothing, so the bars never read as lighter bands. On iOS the status bar's
text keeps the style the application's view controller gives it. `AttachmentViewerPage` is dark on
its own too, for a viewer of the product's own.

Its heading is the foundation's plain top bar on the viewer's ground: close, the file's name and
its position centred on the screen, then share and more — bare glyphs, the same size, spacing and
edge as any screen's own bar. It sits above the pager rather than over the file, so it is not a
floating bar and has nothing to raise its controls off. `AttachmentViewerStyle.topBar()` is that bar
in the viewer's colours.

The viewer opens on the pressed file and swipes to the rest; a receipt and its invoice are read
together. A picture pinches and double-taps closer around the fingers — one finger swipes to the
next file until the picture is zoomed, and panning stops at the picture's edges — and a PDF scrolls
down its pages while files go across. A PDF's pages pinch and double-tap closer the same way; once
zoomed, one finger moves around the page and, past what is in view, scrolls on through the pages.
Zooming does not need gestures: a screen reader offers zoom in, zoom out and reset as actions on a
picture or a PDF (`zoomInLabel`, `zoomOutLabel`, `resetZoomLabel` on `AttachmentViewerPage`), the
keyboard's `+`, `−` and `0` do the same, and Ctrl with the mouse wheel zooms around the pointer.

```kotlin
AttachmentViewer(
    items = files,
    initialKey = viewing,
    onDismiss = { viewing = null },
    onRequest = { item -> viewModel.download(item.key) },
    onShare = { item -> handoff.share(item) },
    onMore = { item -> optionsFor = item },
    notice = notice,
    onNoticeDismiss = { notice = null },
    page = { item ->
        AttachmentViewerPage(
            item = item,
            onRetry = { viewModel.download(item.key) },
            onOpenElsewhere = { handoff.open(item) },
            onSave = { saver.save(item) },
            waitingTitle = stringResource(Res.string.not_here_yet),
            waitingText = stringResource(Res.string.downloads_when_online),
        )
    },
    sheets = {
        AttachmentOptionsSheet(item = optionsFor, choices = …, onDismiss = { optionsFor = null })
        AttachmentRenameSheet(item = renaming, onRename = …, onDismiss = { renaming = null })
        ConfirmSheet(visible = removing != null, …)
    },
)
```

**Opening a file is asking for it.** When a file whose bytes are only on the server
(`AttachmentState.Remote`) settles on screen, the viewer calls `onRequest` — once per landing, and
never for a file merely swiped past. Report `Downloading(progress)` while it arrives, `Waiting` when
the server cannot be reached, and `Ready` with a `model` once it is here.

**The sheets go inside the viewer**, in `sheets`, so they open above the file rather than under the
viewer's window. Sharing is offered only for a file whose bytes are here. What came of handing a file
on — no application opens it, the save failed — is `notice`: a line over the file, announced, and
cleared through `onNoticeDismiss` after three seconds, or after longer where the platform's
accessibility settings ask for more time. When the last file is removed, the viewer closes itself.

A file the page cannot draw — a document, a picture that will not decode, a PDF with no renderer —
is stated with its glyph, name and size, and offers `onOpenElsewhere` and `onSave` when you pass
them. Opening, sharing and saving are the platform's work and stay in the application.

## Pictures

The module has no image loader and never will: an application that uses Coil should not have to
take a second one. Every part that draws a picture reads one from `LocalAttachmentImageLoader`, and
the application provides it once, above everything:

```kotlin
object CoilAttachmentImageLoader : AttachmentImageLoader {
    @Composable
    override fun load(model: Any, maxSize: Int): AttachmentImageState = coil(model, maxSize, Scale.FIT)

    @Composable
    override fun loadSquare(model: Any, side: Int): AttachmentImageState = coil(model, side, Scale.FILL)

    @Composable
    private fun coil(model: Any, size: Int, scale: Scale): AttachmentImageState {
        val context = LocalPlatformContext.current
        val request = remember(model, size, scale) {
            ImageRequest.Builder(context).data(model).size(size).scale(scale).precision(Precision.INEXACT).build()
        }
        val painter = rememberAsyncImagePainter(request)
        val state by painter.state.collectAsState()
        return when (state) {
            is AsyncImagePainter.State.Success -> AttachmentImageState.Ready(painter)
            is AsyncImagePainter.State.Error -> AttachmentImageState.Failed
            else -> AttachmentImageState.Loading
        }
    }
}
```

A picture is asked for in one of two ways, by how it is drawn. `load(model, maxSize)` is for a
picture drawn whole — the viewer's, at the size of the page it fills — and `maxSize` is the longest
edge it is drawn at. `loadSquare(model, side)` is for a picture
cropped into a square, and the picture must **cover** the square: its shorter edge reaches `side`,
so a 4:3 photograph comes back a third longer than `side`. Decode no larger than either asks: a
strip holds many photographs, and each is thousands of pixels on its long edge. Left out,
`loadSquare` asks `load` for twice the side, which covers any picture up to 2:1.

A picture that is loading or fails draws the file's kind — a glyph and its extension — never an
empty square. With no loader provided, `AttachmentImageLoader.None` fails everything, so every file
draws as its kind.

## PDFs

The module draws no PDF itself; the viewer reads one from `LocalAttachmentPdfRenderer`. Each
platform has a renderer, and each needs work a flow module does not do — Android's reads a seekable
file, so the bytes go to storage first. With no renderer provided, or on a platform without one, a
PDF is offered to another application like any other document.

`AttachmentPdfDocument.render` is called per page as it scrolls into view, at the column's width —
twice that while the pages are zoomed, so small print stays sharp — so a long statement is never
drawn whole. Draw on white — a page is paper — and return `null` for a
page that cannot be drawn.

Two things arrive at awkward moments, and a renderer has to be ready for both. The viewer closes a
document as its page leaves the screen, on the main thread, even while a page of it is still being
drawn; and it cancels an open that has not finished when the page is swiped past, after which
nothing else will ever hold what that open made.

On Android, `PdfRenderer` reads a seekable file, opens one page at a time, and throws if it is
closed while a page is open. Drawing and closing share one lock, so closing waits for the page being
drawn; a cancelled open closes what it opened; and a PDF locked with a password, which `PdfRenderer`
refuses with a `SecurityException`, is reported as one that cannot be drawn here:

```kotlin
class AndroidAttachmentPdfRenderer(
    private val read: suspend (model: Any) -> ByteArray?,
) : AttachmentPdfRenderer {
    override suspend fun open(model: Any): AttachmentPdfDocument? {
        val bytes = read(model) ?: return null
        var opened: AttachmentPdfDocument? = null
        return try {
            withContext(Dispatchers.IO) { openPdf(bytes).also { opened = it } }
        } catch (cancellation: CancellationException) {
            // The page left before the document reached it: nobody else will close it.
            opened?.close()
            throw cancellation
        }
    }
}

private fun openPdf(bytes: ByteArray): AttachmentPdfDocument? {
    // `java.io.tmpdir` is the application's cache directory. The copy is deleted as soon as it is
    // open — the descriptor keeps it readable — so none is left behind however the document ends.
    val file = File.createTempFile("attachment", ".pdf")
    val descriptor = try {
        file.writeBytes(bytes)
        ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
    } catch (failure: IOException) {
        return null
    } finally {
        file.delete()
    }

    return try {
        AndroidPdfDocument(descriptor, PdfRenderer(descriptor))
    } catch (notAPdf: IOException) {
        descriptor.close()
        null
    } catch (locked: SecurityException) {
        descriptor.close()
        null
    }
}

private class AndroidPdfDocument(
    private val descriptor: ParcelFileDescriptor,
    private val renderer: PdfRenderer,
) : AttachmentPdfDocument {
    private val lock = Any()
    private var closed = false

    override val pageCount: Int = renderer.pageCount

    override suspend fun render(index: Int, width: Int): ImageBitmap? = withContext(Dispatchers.Default) {
        synchronized(lock) {
            if (closed || width <= 0) return@withContext null
            renderer.openPage(index).use { page ->
                val height = (width.toLong() * page.height / page.width).toInt().coerceAtLeast(1)
                val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                bitmap.eraseColor(Color.WHITE)
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                bitmap.asImageBitmap()
            }
        }
    }

    // Waits for a page being drawn, which is one page at the column's width.
    override fun close() {
        synchronized(lock) {
            if (closed) return
            closed = true
            renderer.close()
            descriptor.close()
        }
    }
}
```

On iOS, PDFKit reads the bytes from memory and draws a page as an image:

```kotlin
@OptIn(ExperimentalForeignApi::class)
class IosAttachmentPdfRenderer(
    private val read: suspend (model: Any) -> ByteArray?,
) : AttachmentPdfRenderer {
    override suspend fun open(model: Any): AttachmentPdfDocument? = withContext(Dispatchers.IO) {
        val bytes = read(model) ?: return@withContext null
        val data = bytes.usePinned { NSData.create(bytes = it.addressOf(0), length = bytes.size.toULong()) }
        PDFDocument(data = data).takeIf { it.pageCount > 0u }?.let(::IosPdfDocument)
    }
}

@OptIn(ExperimentalForeignApi::class)
private class IosPdfDocument(private val document: PDFDocument) : AttachmentPdfDocument {
    override val pageCount: Int = document.pageCount.toInt()

    override suspend fun render(index: Int, width: Int): ImageBitmap? = withContext(Dispatchers.Default) {
        val page = document.pageAtIndex(index.toULong()) ?: return@withContext null
        val (pageWidth, pageHeight) = page.boundsForBox(kPDFDisplayBoxMediaBox).useContents { size.width to size.height }
        if (pageWidth <= 0.0 || width <= 0) return@withContext null
        // PDFKit sizes in points and draws at the screen's scale.
        val points = width / UIScreen.mainScreen.scale
        val image = page.thumbnailOfSize(CGSizeMake(points, points * pageHeight / pageWidth), kPDFDisplayBoxMediaBox)
        val png = UIImagePNGRepresentation(image) ?: return@withContext null
        Image.makeFromEncoded(png.toByteArray()).toComposeImageBitmap()
    }

    override fun close() = Unit
}

@OptIn(ExperimentalForeignApi::class)
private fun NSData.toByteArray(): ByteArray =
    ByteArray(length.toInt()).also { copy -> if (copy.isNotEmpty()) copy.usePinned { memcpy(it.addressOf(0), bytes, length) } }
```

Provide the platform's renderer once, beside the image loader:

```kotlin
CompositionLocalProvider(
    LocalAttachmentImageLoader provides CoilAttachmentImageLoader,
    LocalAttachmentPdfRenderer provides platformPdfRenderer(read = attachments::read),
) {
    App()
}
```

On the desktop, provide none: a PDF is then offered elsewhere, like any document.

## Words

Every word a part draws is a parameter of that part, and the application passes its own. A word
the part cannot do without — a sheet's title, a button's caption, the accessibility label of an
icon-only control — has a plain English default. An optional word — a subtitle, a hint, a counter —
is `null` by default and draws nothing. Anything that names or formats a value, such as a file's
size, arrives already worded.

## Accessibility

The parts carry what applications usually miss:

- A file's square and the lines under it read as one button, named by its label and its file name —
  "Receipt, IMG_2041.jpg" — then its second line, so a strip captioned with tags still says which
  file is which. Pass `description` to word it yourself. The add square and its caption are one
  button too.
- Where a file's bytes are — adding, uploading and how far, not downloaded, downloading, waiting,
  unavailable — is the square's and the row's state for assistive technology, not only a drawn
  mark, and is said once: the ring and the spinner that draw it are silent. Pass `stateDescription`
  to word it; the English default says nothing for a file that is simply here. A download in the
  viewer is read once too, by its progress bar.
- The remove mark is a small disc with the whole corner around it as its target, at the platform's
  48-point minimum, and a label of its own (`removeLabel`); the add square and every icon-only
  control in the viewer take one too. The filmstrip's squares and the sheets' choices are at least
  48 points as well, however small a square is drawn.
- Focus rings are drawn whole, around the shape of what is focused, never cut off by a clip. A
  zoomable picture or PDF takes keyboard focus and draws its ring inside its frame.
- A zoomable picture or PDF offers zoom in, zoom out and reset as actions to a screen reader, and to
  the keyboard as `+`, `−` and `0`.
- A failure under the strip, a refusal on the pending sheet, a notice over the viewer and a download
  that stopped are announced when they appear, not only drawn. The viewer's notice stays longer
  where the platform's accessibility settings ask for more time.
- The sheets' and the viewer's headings carry the heading role; the filmstrip is a group of tabs,
  the one on screen selected. A share control with nothing to share is out of the tree, not merely
  invisible.
- The rename box opens focused with the editable part selected, does not capitalise or correct a
  file name, and Done on the keyboard saves.
- A picture in the viewer is described by `pictureDescription` — the file's label by default — and
  each PDF page by `pageDescription`.
- A square that is not pressable takes no `onClick` rather than a press that does nothing, and a
  file still being added cannot be opened until there is something to show.

## Where to look

The playground renders all of this live. Under **Attachments flow → Screens**, the recipe screens —
[`AttachmentFormShowcase`](../../playground/src/commonMain/kotlin/dev/voir/areca/playground/flow/attachments/AttachmentFormShowcase.kt),
[`AttachmentCroppingShowcase`](../../playground/src/commonMain/kotlin/dev/voir/areca/playground/flow/attachments/AttachmentCroppingShowcase.kt),
[`AttachmentDetailsShowcase`](../../playground/src/commonMain/kotlin/dev/voir/areca/playground/flow/attachments/AttachmentDetailsShowcase.kt)
and
[`AttachmentDocumentsShowcase`](../../playground/src/commonMain/kotlin/dev/voir/areca/playground/flow/attachments/AttachmentDocumentsShowcase.kt),
with the sheets and the viewer they share in
[`AttachmentSheetsRecipe`](../../playground/src/commonMain/kotlin/dev/voir/areca/playground/flow/attachments/AttachmentSheetsRecipe.kt)
— hold the same assemblies as compiled code, driven by a sample application in
[`SampleAttachments`](../../playground/src/commonMain/kotlin/dev/voir/areca/playground/flow/attachments/SampleAttachments.kt)
that picks, copies, uploads and downloads offline. The group's other sections give every public
part a page of its own, in
[`playground/flow/attachments`](../../playground/src/commonMain/kotlin/dev/voir/areca/playground/flow/attachments).
