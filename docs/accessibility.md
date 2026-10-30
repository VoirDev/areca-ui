# Accessibility

Areca components set their own roles, states and focus handling, reserve a full touch target, and
mirror in right-to-left layouts. What they cannot know is what your content *means*: the name of an
icon-only control, what a number counts, what pressing a row does. This page lists what you get and
what you supply.

## What components do for you

Every component sets its own role and state, so a screen reader meets the right thing without extra
modifiers:

- **Buttons.** `Button`, `IconButton`, pressable badges, cards and stats are buttons. A loading
  button blocks presses and says its `loadingStateDescription`.
- **Selection controls.** `Checkbox`, `Radio` and `Toggle` report checked, selected or on in the
  platform's own words; a read-only control is announced as offering no action. List and settings
  rows that hold a control report its state on the row.
- **Headings.** `Heading`, a `TopBar` title, a labelled divider and a settings section header are
  headings, so a screen reader can jump between them.
- **Groups.** `Tabs`, `TabBar` and `FloatingTabBar` are tab groups; a single-choice badge group and a
  `SwatchPicker` are selectable groups. A `SlidingSelector` is a selectable group of radio buttons,
  or of switches in `SelectorMode.Optional`; the arrow keys move focus between its enabled options
  and stop at the ends, and a `SelectorItem`'s `contentDescription` names an option whose content
  does not (an icon, or "Has iris").
- **Progress and values.** `ProgressBar`, `StepIndicator` and `Spinner` are progress indicators;
  `NumberStepper` is one adjustable value that the platform's adjust gestures move.
- **Tables.** A `Table` is a collection of rows and columns whose header cells are headings.
- **Rows.** A list or settings row is one node: its slots merge into it and are read in order.
- **Fields.** A field given its `FieldGroup`'s scope announces the group's label, its required
  marker and its error, and takes focus when the label is tapped.
- **Sensitive values.** `SensitiveText` keeps its value from assistive technology while concealed.
- **Toasts.** A toast's duration is lengthened by the platform's accessibility timeout.
- **Colour.** The foreground tokens clear fixed contrast ratios on their grounds: `fgBase` 7:1,
  `fgSubtle` and the `fg…Text` tokens 4.5:1, the tone icon tokens 3:1. `fgMuted` clears only 3:1, so
  never let it carry meaning alone. See [Theme](theme.md#foregrounds).

## What you supply

### Names for what has no words

Pass a `contentDescription` wherever the component shows no text that says the same thing:

| Component | Supply | Example |
| --- | --- | --- |
| `IconButton` | Always. It has no visible label | `contentDescription = "Delete draft"` |
| `Icon`, `FeaturedIcon` | When the icon carries meaning; `null` when the text beside it says the same | `Icon(ArecaIcons.Bell, contentDescription = null)` beside "Notifications" |
| `NotificationBadge` | What the number counts | `"3 unread messages"`, not `"3"` |
| `IndicatorDot` | When the dot is the only statement of a status | `"Online"` |
| `Avatar` | The person's name, unless it is printed beside the avatar | `"Ada Lovelace"` |
| `ProgressBar`, `Spinner`, `Skeleton`, `StatusMark` | What is loading or what finished | `"Uploading report.pdf"` |
| `Swatch` | The colour's name | `"Ocean blue"` |
| `WheelPickerColumn` | The column's name, and `valueDescription` for how an item is read | `contentDescription = "Hour"` |
| `RequirementList` | What the rules are for | `"Password requirements"` |
| Top bar controls (`BackAction`, `CloseAction`, `AddAction`, …) | An English default is built in; pass your translation | `AddAction(onClick = onCreate, contentDescription = "New invoice")` |

```kotlin
IconButton(
    ArecaIcons.Trash,
    contentDescription = "Delete draft",
    onClick = onDelete,
    style = IconButtonStyle.plain(ActionColor.Danger),
)

NotificationBadge("$unread", contentDescription = "$unread unread messages")

ProgressBar(0.64f, label = "64%", contentDescription = "Uploading report.pdf")
```

### What pressing does

Pressable rows, cards and stats take an `onClickLabel`, read after the row's own words, saying what
pressing does when the words alone do not:

```kotlin
ListItem(title = { Text("Invoice 1042") }, onClick = onOpen, onClickLabel = "Open invoice")
```

`ListItem`, `NavigationListItem`, `ExpandableListItem`, `CheckmarkListItem`, the `List` DSL's
`item`, every settings row, `SettingsRowLink`, `Card` and `Stat` take one.

### Labels for controls

A bare `Checkbox`, `Radio` or `Toggle` has no label of its own. Put it in its row —
`CheckboxListItem`, `RadioListItem`, `ToggleListItem`, or a settings row — so the row's title labels
it and the whole row is the target:

```kotlin
Column(Modifier.selectableGroup()) {
    options.forEach { option ->
        RadioListItem(
            selected = option == chosen,
            onSelect = { onChoose(option) },
            title = { Text(option) },
        )
    }
}
```

A group of radios is one selectable group once you add `Modifier.selectableGroup()` to its
container, as above.

A field reads its label from its `FieldGroup`. Forward the group's scope to the field —
`FieldGroup(label = "Email") { field -> TextField(…, field = field) }` — or the label is drawn but
not announced.

## Announcements

A message composed into existence — a validation error, a failed attempt, a "saved" line — is not
spoken by a live region on every platform. `AnnounceOnChange` (in `dev.voir.areca.foundation`)
speaks a sentence when it is first composed and whenever it changes, without moving focus:

```kotlin
AnnounceOnChange(signInError)
```

`null` or blank text says nothing. `FieldGroup` already announces its error, and the flow modules
announce their own banners and verdicts. It speaks through the screen reader on Android and iOS; the
desktop has no announcement API, so there the reader finds the message when it reaches it.

## Touch targets

Under touch, every control that draws smaller than 48dp — a 32dp icon button, a 20dp checkbox, a
small badge — reserves the rest in layout around itself, so a neighbour never sits inside its band.
Only controls with an action reserve it; a display-only control keeps its drawn size. Under a pointer
the target is the control itself (`ArecaTheme.sizing.touchTarget` is zero), as on every desktop
system; a tablet with a trackpad stays touch unless the app passes `inputMode`.

Give your own small controls the same guarantee with `Modifier.minimumTouchTarget()` (in
`dev.voir.areca.modifier`), applied outside the element's size and before its `clickable`. See
[Customising](customising.md#your-own-pressable-surface).

## Keyboard focus

Every interactive component draws keyboard focus as a ring outside its bounds, following its shape,
without changing anything inside it — so hover, press and focus can show at
once. Never `Modifier.clip` an interactive element: the clip crops the ring. Use
`Modifier.background(color, shape)` for a shaped fill.

## Pointer affordances

A tooltip is for the eye: `TooltipBox` and the tooltip an `IconButton` shows under a pointer are
hidden from assistive technology, so the element still needs its own description — for an
`IconButton`, the `contentDescription` the tooltip repeats. A tooltip also shows when keyboard focus
arrives, not only on hover. `Modifier.contextMenu` opens a row's actions from the keyboard (the Menu
key or Shift+F10) and gives the row a long-click action, named by its `label`, that a screen reader
offers; a touch long press alone would be out of reach for both. Scrollbars and `ListDetail`'s
draggable divider are pointer conveniences: the content they move scrolls and the panes lay
themselves out without them.

## Right to left

Slots are start and end, not left and right, so every row, bar and field mirrors in a right-to-left
layout. `ChevronDirection.Left` and `Right` mirror with it, so `Right` always points towards the end
of the line; the bundled chevron icon mirrors itself. A tab indicator, a dropdown's offset, a
skeleton's shimmer and `LiquidSweepIndication` run in reading order, and `BaseLayout` hands its
content the side insets as start and end padding. Charts and the calendar's arrow keys follow the
layout direction too; an on-screen keypad keeps its rows in left-to-right order, as keypads
conventionally do. Each module README says how.

Directional icons from `dev.voir.areca:icons` — arrows, chevrons, `LogIn`, `LogOut`, `Send`,
`Share` — mirror themselves; media, clock and rotation icons do not. See
[standalone/icons](../standalone/icons/README.md).

## Localise what is spoken

Much of what assistive technology says is a string with an English default — `"Busy"`, `"Back"`,
`"Show password"`, the content-list faces, the calendar's day descriptions. A localised application
passes its own for every one. [Localisation](localisation.md) lists them module by module.

## Module notes

| Module | What it handles, and what to pass |
| --- | --- |
| [charts](../ui/charts/README.md) | Each chart is one node with a description generated from its data, in English punctuation; pass `contentDescription` when that does not suit, and on every `ColumnChartColumn` ("October: income €4,250, spent €2,340"). Colour is the only cue in a stacked bar, so show a `ChartLegend`. `MeterBar` reads only its `contentDescription` |
| [datetime](../ui/datetime/README.md) | The calendar grid is one tab stop navigated with the arrow, Home, End and Page keys; each day reads `CalendarLabels.dayDescription`, and range position is added by `DateRangeLabels` |
| [crop](../ui/crop/README.md) | Every drag has a keyboard and an assistive-technology action; their names are in `CropToolbarLabels` |
| [keyboard](../ui/keyboard/README.md) | `KeyboardHost` announces `paneTitle` when the keyboard appears; utility keys read `KeyboardGridLabels` |
| [flow-auth](../flow/auth/README.md) | Autofill types on every credential box, matched keyboards and IME actions, an announced failure banner, labelled icon-only provider buttons |
| [flow-profile](../flow/profile/README.md) | Rows read as one item, the verified and current-account ticks are announced, upload and username verdicts are announced |
| [flow-attachments](../flow/attachments/README.md) | Each file reads as one button with its state; remove marks have full targets; the viewer offers zoom actions and keys |
