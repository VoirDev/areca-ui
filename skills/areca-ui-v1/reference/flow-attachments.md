# Attachments flow — `dev.voir.areca:flow-attachments`

Add it when people attach files to something — a strip of files on a form, the files on a record's
details, a list of documents — with the sheets that add a file and act on one, and a full-screen
viewer that swipes through them.

- Artifact `dev.voir.areca:flow-attachments`. Depends on `foundation` as `api` (`ConfirmSheet`,
  `Settings`, `Button` need nothing more). Needs `ArecaThemeProvider`. Recipes that draw
  `ArecaIcons.*` need `dev.voir.areca:icons`; the module bundles its own glyphs.
- It ships **parts and recipes, not screens**, and is not a file manager. Picking, capturing, copying,
  uploading, downloading, sharing, saving, size limits and PDF rendering are the app's. Parts report
  intents (`onAdd`, `onOpen(item)`, `onRemove(item)`) and draw the state given. No dependency on other
  flows, `crop`, a navigation library or `viewmodel`.

| Package | Holds |
| --- | --- |
| `dev.voir.areca.flow.attachments` | `AttachmentItem`, `AttachmentFormat`, `AttachmentState`, `AttachmentImageLoader` (deprecated), `AttachmentImageState`, `LocalAttachmentImageLoader`, `AttachmentPdfRenderer`, `AttachmentPdfDocument`, `LocalAttachmentPdfRenderer` |
| `dev.voir.areca.flow.attachments.part` | `AttachmentThumbnail`, `AttachmentTile`, `AttachmentAddTile`, `AttachmentStrip`, `AttachmentIndicator`, `AttachmentTileStyle`, `LocalAttachmentTileStyle` |
| `dev.voir.areca.flow.attachments.row` | `AttachmentRow` |
| `dev.voir.areca.flow.attachments.sheet` | `AttachmentSourceSheet`, `AttachmentSourceOptions`, `PendingAttachmentSheet`, `PendingAttachment`, `AttachmentTagChips`, `AttachmentOptionsSheet`, `AttachmentOptions`, `AttachmentRenameSheet`, `AttachmentChoice` |
| `dev.voir.areca.flow.attachments.viewer` | `AttachmentViewer`, `AttachmentViewerPage`, `AttachmentViewerStyle`, `LocalAttachmentViewerStyle` |

## Choose a part

| Need | Use | Types |
| --- | --- | --- |
| A record's files as a sideways-scrolling row of squares, with add, remove, empty and failure | `AttachmentStrip(items, onOpen, onAdd = …, onRemove = …)` | `AttachmentItem` |
| One file's square: picture, or glyph and extension, plus where its bytes are | `AttachmentThumbnail(item)` | `AttachmentItem` |
| One file in a strip of your own: square, caption, second line, remove mark | `AttachmentTile(item, onClick)` | — |
| The square that adds a file | `AttachmentAddTile(onClick, caption = …)` | — |
| One file as a settings row, for files read by name | `AttachmentRow(item, onClick)` in a `Section` | — |
| A paperclip (and count) on something that has files | `AttachmentIndicator(count = …)` | — |
| Where a new file comes from | `AttachmentSourceSheet(visible, options, onDismiss)` | `AttachmentSourceOptions`, `AttachmentChoice` |
| After a pick: the file, a question, Attach; or why it cannot be attached | `PendingAttachmentSheet(file, onAttach, onDismiss)` | `PendingAttachment` |
| What a file is, one tap from the product's tags | `AttachmentTagChips(tags, selected, onSelect, label)` | — |
| What can be done to one file | `AttachmentOptionsSheet(item, choices, onDismiss)` | `AttachmentOptions`, `AttachmentChoice` |
| A new name, editing only the part before the extension | `AttachmentRenameSheet(item, onRename, onDismiss)` | — |
| Confirm a final removal | foundation `ConfirmSheet` (the module ships none) | — |
| A record's files full screen, one at a time, always dark | `AttachmentViewer(items, onDismiss, initialKey = …)` | `AttachmentViewerStyle` |
| One file in the viewer (zoomable picture, PDF pages, or the file stated) | `AttachmentViewerPage(item)` | — |
| A record's files beside it on a tablet or desktop, in a sheet below 920dp | foundation `SupportingPane` — record as `main`, `AttachmentTile`s in a `FlowRow` as `supporting` (recipe below) | — |

A library that grows without limit is a foundation `LazyList` / `PagedContentList` of `ListItem`s, not
`AttachmentRow`s in a `Section`.

## Data

```kotlin
AttachmentItem(
    key: Any,                                   // stable identity
    fileName: String,
    label: String = fileName,                   // the caption: a name, or a tag the owner chose
    size: String? = null,                       // worded by the app: "2.4 MB"
    format: AttachmentFormat = AttachmentFormat.Document,
    state: AttachmentState = AttachmentState.Ready,
    model: Any? = null,                         // what the loader / renderer understands; null while bytes are not here
)   // item.extension: String?
AttachmentFormat.Image | Pdf | Document
AttachmentFormat.of(contentType: String?, fileName: String): AttachmentFormat   // falls back to the extension

AttachmentState.Adding                          // being saved: veil + spinner, cannot open
    | Uploading(progress: Float? = null)        // here, going up; opens like Ready
    | Ready                                     // here
    | Remote                                    // server only; opening it asks for it (onRequest)
    | Downloading(progress: Float? = null)
    | Waiting                                   // wanted, server unreachable; not an error
    | Unavailable                               // gone for good

PendingAttachment(
    fileName: String?, format: AttachmentFormat = AttachmentFormat.Document, size: String? = null,
    model: Any? = null,
    isCopying: Boolean = false,                 // Attach shows a spinner and waits
    problem: String? = null,                    // worded refusal: shown instead of the question, only Close
)
class AttachmentChoice(label: String, icon: ImageVector, onClick: () -> Unit, isDestructive: Boolean = false)
AttachmentSourceOptions.camera(label = "Take a photo") { } · library(label = "Choose from library") { } · files(label = "Choose a file") { }
AttachmentOptions.rename(label = "Rename") { } · share(label = "Share") { } · save(label = "Save to device") { }
    · openElsewhere(label = "Open in another app") { } · remove(label = "Remove attachment") { }

// Pictures load through media's MediaImageLoader / LocalMediaImageLoader (see media.md):
// load(model, maxSize) drawn whole; loadSquare(model, side) must COVER the square. Unprovided → glyphs.
// AttachmentImageLoader, AttachmentImageState, LocalAttachmentImageLoader: deprecated; still read
// while LocalMediaImageLoader is unprovided.
interface AttachmentPdfRenderer { suspend fun open(model: Any): AttachmentPdfDocument? }   // AttachmentPdfRenderer.None
interface AttachmentPdfDocument : AutoCloseable {
    val pageCount: Int
    suspend fun render(index: Int, width: Int): ImageBitmap?   // on white; null if it cannot be drawn
}
```

## Signatures

```kotlin
// .part
@Composable fun AttachmentStrip(
    items: List<AttachmentItem>,
    onOpen: ((AttachmentItem) -> Unit)?,
    modifier: Modifier = Modifier,
    onAdd: (() -> Unit)? = null,                // null = no add square (at the limit, or read-only)
    onRemove: ((AttachmentItem) -> Unit)? = null,   // null = no remove marks
    title: String? = null,
    counter: String? = null,                    // "2 of 5"
    emptyTitle: String? = null,
    emptyDescription: String? = null,
    failure: String? = null,                    // under the squares, announced
    caption: (AttachmentItem) -> String? = { it.label },
    detail: (AttachmentItem) -> String? = { null },        // second line, e.g. { it.size }
    addCaption: String? = null,
    addLabel: String = "Add attachment",
    removeLabel: (AttachmentItem) -> String = { "Remove ${it.label}" },
    description: (AttachmentItem) -> String = { /* "label, fileName" */ },
    stateDescription: (AttachmentState) -> String? = { /* English; null for Ready */ },
    gutter: Dp = ArecaTheme.spacing.horizontalContentInsets,
    bleed: Boolean = false,                     // true inside a container already padded by the gutter
    style: AttachmentTileStyle = LocalAttachmentTileStyle.current ?: AttachmentTileStyle.Default,
    tileSize: Dp = ArecaTheme.spacing.xl8,      // xl9 for a record's details
)
@Composable fun AttachmentThumbnail(
    item: AttachmentItem, modifier: Modifier = Modifier,
    style: AttachmentTileStyle = LocalAttachmentTileStyle.current ?: AttachmentTileStyle.Default,
    size: Dp = ArecaTheme.spacing.xl8,
    marks: Boolean = true,                      // the where-are-the-bytes mark
)
@Composable fun AttachmentTile(
    item: AttachmentItem, onClick: (() -> Unit)?, modifier: Modifier = Modifier,
    caption: String? = item.label, detail: String? = null,
    onRemove: (() -> Unit)? = null, removeLabel: String = "Remove ${item.label}",
    description: String = /* "label, fileName" */, stateDescription: String? = /* English */,
    style: AttachmentTileStyle = LocalAttachmentTileStyle.current ?: AttachmentTileStyle.Default,
    size: Dp = ArecaTheme.spacing.xl8,
)
@Composable fun AttachmentAddTile(
    onClick: () -> Unit, modifier: Modifier = Modifier,
    caption: String? = null, label: String = "Add attachment",
    style: AttachmentTileStyle = LocalAttachmentTileStyle.current ?: AttachmentTileStyle.Default,
    size: Dp = ArecaTheme.spacing.xl8, enabled: Boolean = true,
)
@Composable fun AttachmentIndicator(
    modifier: Modifier = Modifier, count: String? = null,
    contentDescription: String = "Has attachments",
    color: Color = LocalContentColor.current, size: Dp = IconSize.ExtraSmall,
)

// .row
@Composable fun AttachmentRow(
    item: AttachmentItem, onClick: (() -> Unit)?, modifier: Modifier = Modifier,
    title: String = item.label, subtitle: String? = null, onClickLabel: String? = null,
    stateDescription: String? = /* English */,
    style: AttachmentTileStyle = LocalAttachmentTileStyle.current ?: AttachmentTileStyle.default(bg = ArecaTheme.colors.bgMuted, shape = ArecaTheme.shapes.tile),
    thumbnailSize: Dp = ArecaTheme.spacing.xl5,
    enabled: Boolean = true,
    trailing: (@Composable BoxScope.() -> Unit)? = null,
)

// .sheet
@Composable fun AttachmentSourceSheet(
    visible: Boolean, options: List<AttachmentChoice>, onDismiss: () -> Unit,
    title: String = "Add attachment", subtitle: String? = null, closeLabel: String = "Close",
)
@Composable fun PendingAttachmentSheet(
    file: PendingAttachment?,                   // non-null = shown
    onAttach: () -> Unit, onDismiss: () -> Unit,
    title: String = "Add this file?", problemTitle: String = "Couldn’t add this file",
    attachText: String = "Attach", cancelText: String = "Cancel", closeLabel: String = "Close",
    copyingText: String? = null,
    onEdit: (() -> Unit)? = null, editText: String = "Edit",   // offer Edit (crop) for this file
    thumbnailStyle: AttachmentTileStyle = LocalAttachmentTileStyle.current ?: AttachmentTileStyle.Default,
    attachEnabled: Boolean = true,
    question: (@Composable ColumnScope.() -> Unit)? = null,     // e.g. AttachmentTagChips
)
@Composable fun <T> AttachmentTagChips(
    tags: List<T>, selected: T?, onSelect: (T) -> Unit, label: (T) -> String,
    modifier: Modifier = Modifier, bg: Color = ArecaTheme.colors.bgSubtle, enabled: Boolean = true,
)
@Composable fun AttachmentOptionsSheet(
    item: AttachmentItem?,                      // non-null = shown
    choices: List<AttachmentChoice>, onDismiss: () -> Unit,
    title: (AttachmentItem) -> String = { it.fileName },
    subtitle: (AttachmentItem) -> String? = { it.size },
    tagsTitle: String? = null, closeLabel: String = "Close",
    tags: (@Composable ColumnScope.() -> Unit)? = null,
)
@Composable fun AttachmentRenameSheet(
    item: AttachmentItem?,                      // non-null = shown
    onRename: (String) -> Unit,                 // receives the full name, extension restored
    onDismiss: () -> Unit,
    title: String = "Rename", label: String = "Name", saveText: String = "Save", cancelText: String = "Cancel",
    canSave: (String) -> Boolean = { true }, error: String? = null, saving: Boolean = false,
)

// .viewer
@Composable fun AttachmentViewer(
    items: List<AttachmentItem>,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    initialKey: Any? = null,
    onRequest: (AttachmentItem) -> Unit = {},   // a Remote file settled on screen: fetch it
    onShare: ((AttachmentItem) -> Unit)? = null,
    onMore: ((AttachmentItem) -> Unit)? = null,
    notice: String? = null,                     // outcome of a hand-off, announced
    onNoticeDismiss: (() -> Unit)? = null,
    noticeDuration: Duration = 3.seconds,
    title: (AttachmentItem) -> String = { it.label },
    position: ((index: Int, count: Int) -> String)? = { i, n -> "${i + 1} of $n" },
    caption: (AttachmentItem) -> String? = { /* default line over the file */ },
    closeLabel: String = "Close", shareLabel: String = "Share", moreLabel: String = "More options",
    filmstripLabel: (item: AttachmentItem, index: Int, count: Int) -> String = { item, i, n -> "${item.label}, ${i + 1} of $n" },
    style: AttachmentViewerStyle = LocalAttachmentViewerStyle.current ?: AttachmentViewerStyle.Default,
    page: @Composable (AttachmentItem) -> Unit = { AttachmentViewerPage(item = it, onRetry = { onRequest(it) }, style = style) },
    sheets: @Composable () -> Unit = {},        // options, rename and confirm sheets go HERE
)
@Composable fun AttachmentViewerPage(
    item: AttachmentItem, modifier: Modifier = Modifier,
    onRetry: (() -> Unit)? = null, onOpenElsewhere: (() -> Unit)? = null, onSave: (() -> Unit)? = null,
    style: AttachmentViewerStyle = LocalAttachmentViewerStyle.current ?: AttachmentViewerStyle.Default,
    pictureDescription: String? = item.label,
    detail: String? = /* format and size */,
    downloadingText: (progress: Float?) -> String = { /* "Downloading · 40%" */ },
    waitingTitle: String = "Not on this device yet", waitingText: String? = null, retryText: String = "Try again",
    unavailableTitle: String = "This file is no longer available", unavailableText: String? = null,
    elsewhereText: String? = null, openElsewhereText: String = "Open in another app", saveText: String = "Save to device",
    pageLabel: (page: Int, count: Int) -> String = { p, n -> "Page $p of $n" },
    pageDescription: (page: Int, count: Int) -> String = { p, n -> "Page $p of $n" },
    zoomInLabel: String = "Zoom in", zoomOutLabel: String = "Zoom out", resetZoomLabel: String = "Reset zoom",
)
```

## Rules

- **Report only the states the app has.** Files that never leave the device are always `Ready`; an app
  that hides uploads never reports `Uploading`.
- **Strip options come from what you pass:** `onAdd` draws the add square, `onRemove` the remove marks;
  leave them out at a limit or where files are only shown. With no files, no `onAdd` and no `failure`
  the strip draws nothing, title included. Place it full width (not inset): it scrolls under both
  edges while its first square keeps to `gutter`; pass `bleed = true` inside an already-padded sheet.
- On a grey ground pass `style = AttachmentTileStyle.default(bg = ArecaTheme.colors.bgSurface)` or
  provide `LocalAttachmentTileStyle` once. `LocalAttachmentTileStyle` and `LocalAttachmentViewerStyle`
  are **not seeded** by `ArecaThemeProvider`.
- **Sheets are state-driven:** `visible` or the item they are about (`file`, `item`); they report
  `onDismiss` and **do not close themselves** on a choice — every choice clears the state that shows it.
- Offer only sources the platform can complete. With one source left (desktop: files only) skip the
  sheet and start that pick from the add square.
- **The tag is asked, never guessed:** start `PendingAttachmentSheet` with nothing selected
  (`remember(pending?.fileName) { mutableStateOf<Tag?>(null) }`) and `attachEnabled = tag != null`.
  The module never decides a file is too large: put the worded reason in `PendingAttachment.problem`.
- `onEdit` is per file and optional: pass it only for files the app crops (e.g.
  `format == AttachmentFormat.Image`), open the app's crop screen, and replace the pending file's
  `model` with the crop. Hide the pending sheet while cropping (`file = pending.takeIf { editing == null }`).
- `AttachmentOptionsSheet` lists only what applies: no share or save unless `state == Ready`.
- **Wide windows:** every module sheet (source, pending, options, rename) is a `FlowModal` on a phone
  and a centred foundation `Dialog` where `AdaptiveDefaults.usesDialog(ArecaTheme.hostWindow)` —
  expanded width and up, not a phone on its side. Same heading, content and foot; heading and foot
  pinned, content scrolls; scrim / Escape / back arrive as `onDismiss`. Nothing to pass, and it reads
  the whole window, not the pane it was opened from. `ConfirmSheet` does the same.
- **Removal:** a saved record removes at once and confirms first with foundation `ConfirmSheet`; a form
  saves nothing until it is saved, so removing there asks nothing.
- **Viewer:** compose it while a file is being viewed (`if (viewing != null) AttachmentViewer(…)`); it
  is its own always-dark, edge-to-edge window and paints the system bars. Put the options, rename and
  confirm sheets in its `sheets` slot so they open above the file. It calls `onRequest` once per
  landing on a `Remote` file; report `Downloading`, `Waiting`, then `Ready` with a `model`. It closes
  itself when the last file is removed. Clear `notice` through `onNoticeDismiss`.
  On a tablet or desktop it still takes the whole window (no details pane inside it): a picture fills
  the page, a PDF's pages and a stated file stay centred at most `AdaptiveDefaults.ReadableWidth`
  wide, and its `sheets` open as dialogs. Put a file's details on the record, beside the files.
- **Pictures and PDFs:** implement media's `MediaImageLoader` once (decode no larger than asked;
  `loadSquare` must cover the square) and provide `LocalMediaImageLoader` — never the deprecated
  `LocalAttachmentImageLoader`. Provide a platform
  `AttachmentPdfRenderer` via `LocalAttachmentPdfRenderer` (Android `PdfRenderer` needs a seekable
  file; iOS PDFKit; none on desktop — PDFs are then offered elsewhere). The viewer may close a
  document on the main thread while a page renders and cancels unfinished opens: lock drawing and
  closing together, close what a cancelled open opened.

## Words and accessibility

- Required words default to English (sheet titles, button texts, `addLabel`, `removeLabel`,
  `closeLabel`, viewer labels, `AttachmentIndicator.contentDescription`, choice labels); pass the app's
  strings. Optional words (`subtitle`, `counter`, `copyingText`, `waitingText`, empty texts) default to
  `null`. Sizes arrive worded.
- Pass `stateDescription` to word where a file's bytes are, and `description` to word a square ("Receipt,
  IMG_2041.jpg"); `pictureDescription` / `pageDescription` in the viewer.
- Built in: one button per square with its caption, 48 dp remove target, unclipped focus rings, zoom
  actions (`+` / `−` / `0`), announced failures, refusals and notices, heading roles, filmstrip as tabs,
  rename opens focused with the name selected.

## Recipes

### The sheets and the viewer (draw once per screen that shows files)

```kotlin
@Composable
fun AttachmentSheets(state: AttachmentsState, actions: AttachmentActions, confirmRemoval: Boolean) {
    AttachmentSourceSheet(
        visible = state.choosingSource,
        options = platformSources.map { source ->
            when (source) {
                Source.Camera -> AttachmentSourceOptions.camera(takePhoto) { actions.pick(source) }
                Source.Library -> AttachmentSourceOptions.library(chooseFromLibrary) { actions.pick(source) }
                Source.Files -> AttachmentSourceOptions.files(chooseFile) { actions.pick(source) }
            }
        },
        onDismiss = actions::dismissSources,
        title = addAttachment,
        subtitle = limitsLine,
    )

    var tag by remember(state.pending?.fileName) { mutableStateOf<Kind?>(null) }
    PendingAttachmentSheet(
        file = state.pending,
        onAttach = { tag?.let(actions::attach) },
        onDismiss = actions::cancelPending,
        title = whatIsThis,
        attachEnabled = tag != null,
        copyingText = addingWord,
        question = { AttachmentTagChips(Kind.entries, tag, { tag = it }, { it.label() }) },
    )

    if (state.viewing != null) {
        AttachmentViewer(
            items = state.files,
            initialKey = state.viewing,
            onDismiss = actions::closeViewer,
            onRequest = actions::download,
            onShare = actions::share,
            onMore = actions::showOptions,
            notice = state.notice,
            onNoticeDismiss = actions::dismissNotice,
            page = { item ->
                AttachmentViewerPage(
                    item = item,
                    onRetry = { actions.download(item) },
                    onOpenElsewhere = { actions.openElsewhere(item) },
                    onSave = { actions.save(item) },
                    waitingText = downloadsWhenOnline,
                )
            },
            sheets = {
                val item = state.optionsFor
                AttachmentOptionsSheet(
                    item = item,
                    choices = buildList {
                        add(AttachmentOptions.rename(renameLabel, actions::startRename))
                        if (item?.state == AttachmentState.Ready) add(AttachmentOptions.save(saveLabel) { actions.save(item) })
                        add(AttachmentOptions.remove(removeLabel) { actions.startRemove(confirm = confirmRemoval) })
                    },
                    onDismiss = actions::dismissOptions,
                    tagsTitle = typeLabel,
                    tags = { AttachmentTagChips(Kind.entries, kindOf(item), { actions.retag(item, it) }, { it.label() }) },
                )
                AttachmentRenameSheet(item = state.renaming, onRename = actions::rename, onDismiss = actions::dismissRename,
                    canSave = { it.isNotBlank() })
                ConfirmSheet(
                    visible = state.removing != null,
                    icon = ArecaIcons.Trash,
                    title = removeTitle,
                    description = removeDescription,
                    confirmText = removeWord,
                    onConfirm = actions::confirmRemove,
                    onDismiss = actions::dismissRemove,
                )
            },
        )
    }
}
```

### A form: strip under the fields, saved with the form

```kotlin
Column(
    Modifier.fillMaxSize().background(ArecaTheme.colors.bgCanvas).verticalScroll(rememberScrollState())
        .windowInsetsPadding(WindowInsets.systemBars).imePadding(),
) {
    Settings {
        Section {
            SettingsTextFieldRow(value = state.amount, onValueChange = onAmount, placeholder = amountLabel)
            SettingsTextFieldRow(value = state.note, onValueChange = onNote, placeholder = noteLabel, singleLine = false)
        }
    }
    Spacer(Modifier.height(ArecaTheme.spacing.lg))
    AttachmentStrip(
        items = files.items,
        onOpen = actions::open,
        onAdd = if (files.items.size < files.limit) actions::add else null,
        onRemove = actions::remove,
        title = attachmentsTitle,
        counter = if (files.items.isEmpty()) null else countOfLimit(files.items.size, files.limit),
        emptyTitle = emptyTitle,
        emptyDescription = emptyDescription,
        failure = files.failure,
        caption = { if (it.state == AttachmentState.Adding) addingWord else it.label },
        addCaption = addWord,
    )
    Button(text = saveLabel, onClick = onSave, loading = state.saving, modifier = Modifier.fillMaxWidth().horizontalContentInsets())
}
AttachmentSheets(files, actions, confirmRemoval = false)
```

### A record's details: larger squares with sizes, changes saved at once

```kotlin
AttachmentStrip(
    items = files.items,
    onOpen = actions::open,
    title = attachmentsTitle,
    failure = files.failure,
    tileSize = ArecaTheme.spacing.xl9,
    detail = { it.size },
)
Settings {
    Section {
        SettingsRow(
            title = { Text(attachFile) },
            leading = { SettingsRowIcon(ArecaIcons.Paperclip) },
            enabled = files.items.size < files.limit,
            onClick = actions::add,
        )
    }
}
AttachmentSheets(files, actions, confirmRemoval = true)
```

### On a tablet or desktop: the record beside its files

```kotlin
var filesOpen by remember { mutableStateOf(false) }   // read only below 920dp
SupportingPane(
    supportingVisible = filesOpen,
    onSupportingDismissRequest = { filesOpen = false },
    modifier = Modifier.fillMaxSize().background(ArecaTheme.colors.bgCanvas).windowInsetsPadding(WindowInsets.systemBars),
    supportingHeader = {
        TopBar(title = attachmentsTitle, subtitle = countOfLimit, size = TopBarSize.Compact, windowInsets = FlowModalHeaderInsets)
    },
    main = {
        Column(   // the record at a readable width, centred
            Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                .wrapContentWidth().widthIn(max = AdaptiveDefaults.ReadableWidth).fillMaxWidth(),
        ) {
            RecordHeading(expense)                       // amount and summary, on the sections' 16dp gutter
            Settings {
                Section {
                    SettingsRow(title = { Text(categoryLabel) }, value = { Text(expense.category) })
                    SettingsRow(title = { Text(accountLabel) }, value = { Text(expense.account) })
                }
                if (!isSupportingInline) {               // no pane: this row opens the files in a sheet
                    Section {
                        SettingsRow(title = { Text(attachmentsTitle) }, leading = { SettingsRowIcon(ArecaIcons.Paperclip) },
                            value = { Text(files.items.size.toString()) }, onClick = { filesOpen = true })
                    }
                }
            }
        }
    },
    supporting = {
        FlowRow(   // squares that wrap: the pane is tall and narrow
            Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).horizontalContentInsets()
                .padding(top = ArecaTheme.spacing.sm, bottom = ArecaTheme.spacing.xl3),
            horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
            verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
        ) {
            files.items.forEach { item -> key(item.key) { AttachmentTile(item, onClick = { actions.open(item) }, detail = item.size) } }
            if (files.items.size < files.limit) AttachmentAddTile(onClick = actions::add, caption = addWord)
        }
    },
)
AttachmentSheets(files, actions, confirmRemoval = true)   // unchanged: its sheets become dialogs here
```

From a 920dp container the files are a 340/360dp pane at the end and the record has no row for them;
below it they open in a sheet (a dialog on an expanded window) from the row. To keep a phone's files on
the page, draw the details strip in `main` while `!isSupportingInline` instead of the row.

### Documents: rows grouped by tag

```kotlin
Settings {
    Section(footer = { Text(filesCount(files.items.size, files.limit)) }) {
        SettingsRow(title = { Text(addDocument) }, leading = { SettingsRowIcon(ArecaIcons.Plus) }, onClick = actions::add)
    }
    Kind.entries.forEach { kind ->
        val ofKind = files.items.filter { kindOf(it) == kind }
        if (ofKind.isNotEmpty()) {
            Section(header = { Text(kind.label()) }) {
                ofKind.forEach { item ->
                    AttachmentRow(item = item, onClick = { actions.open(item) }, title = item.fileName,
                        subtitle = listOfNotNull(item.label, item.size).joinToString(" · "))
                }
            }
        }
    }
}
AttachmentSheets(files, actions, confirmRemoval = true)
```

### Cropping a photo before it is attached

```kotlin
val ReceiptCrop = CropSpec.Photo.copy(maxOutputDimension = 2048)   // the app's, once
PendingAttachmentSheet(
    file = state.pending.takeIf { state.editing == null },          // down while the crop screen has it
    onAttach = { tag?.let(actions::attach) },
    onDismiss = actions::cancelPending,
    title = whatIsThis,
    attachEnabled = tag != null,
    copyingText = addingWord,
    onEdit = if (state.pending?.format == AttachmentFormat.Image) actions::editPending else null,
    question = { AttachmentTagChips(Kind.entries, tag, { tag = it }, { it.label() }) },
)
state.editing?.let { file ->
    CropDialog(onDismissRequest = actions::cancelEdit) {
        CropScreen(photo = decode(file.model), spec = ReceiptCrop, title = cropPhoto,
            photoDescription = photoDescription, labels = appCropLabels,
            onCancel = actions::cancelEdit, onDone = actions::replacePending)   // the crop becomes the pending model
    }
}
```

### A strip of your own, and a marker

```kotlin
LazyRow(horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md), contentPadding = PaddingValues(horizontal = ArecaTheme.spacing.xl)) {
    items(files, key = { it.key }) { item ->
        AttachmentTile(item = item, onClick = { onOpen(item) }, onRemove = { onRemove(item) }, removeLabel = removeLabelFor(item))
    }
    item { AttachmentAddTile(onClick = onAdd, caption = addWord, label = addAttachment) }
}
AttachmentThumbnail(item = file, size = ArecaTheme.spacing.xl5, marks = false)   // e.g. a message preview
ListItem(title = { Text(activity.title) }, caption = { Row { AttachmentIndicator(count = "3", contentDescription = threeAttachments) } })
```

### Loaders (once, at the root)

```kotlin
// CoilMediaImageLoader: the MediaImageLoader in media.md / ui/media/README.md.
CompositionLocalProvider(
    LocalMediaImageLoader provides CoilMediaImageLoader,
    LocalAttachmentPdfRenderer provides platformPdfRenderer(read = attachments::read),   // omit on desktop
) { App() }
```

### PDF renderers

```kotlin
// androidMain: PdfRenderer reads a seekable file, one page at a time, and throws if closed mid-page
class AndroidAttachmentPdfRenderer(private val read: suspend (model: Any) -> ByteArray?) : AttachmentPdfRenderer {
    override suspend fun open(model: Any): AttachmentPdfDocument? {
        val bytes = read(model) ?: return null
        var opened: AttachmentPdfDocument? = null
        return try {
            withContext(Dispatchers.IO) { openPdf(bytes).also { opened = it } }
        } catch (cancellation: CancellationException) {
            opened?.close()                     // swiped past before it arrived: nobody else will close it
            throw cancellation
        }
    }
}

private fun openPdf(bytes: ByteArray): AttachmentPdfDocument? {
    val file = File.createTempFile("attachment", ".pdf")
    val descriptor = try {
        file.writeBytes(bytes)
        ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
    } catch (failure: IOException) {
        return null
    } finally {
        file.delete()                           // the open descriptor keeps it readable
    }
    return try {
        AndroidPdfDocument(descriptor, PdfRenderer(descriptor))
    } catch (notAPdf: IOException) {
        descriptor.close(); null
    } catch (locked: SecurityException) {       // password-protected
        descriptor.close(); null
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
                bitmap.eraseColor(android.graphics.Color.WHITE)
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                bitmap.asImageBitmap()
            }
        }
    }

    override fun close() {                      // waits for a page being drawn
        synchronized(lock) {
            if (closed) return
            closed = true
            renderer.close()
            descriptor.close()
        }
    }
}

// iosMain: PDFKit draws a page as an image
@OptIn(ExperimentalForeignApi::class)
class IosAttachmentPdfRenderer(private val read: suspend (model: Any) -> ByteArray?) : AttachmentPdfRenderer {
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
        val points = width / UIScreen.mainScreen.scale  // PDFKit sizes in points
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
