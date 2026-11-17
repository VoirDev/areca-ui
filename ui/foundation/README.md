# Foundation

The Areca theme and every component built on it — buttons, text, fields, selection controls, lists
and settings, overlays, navigation and feedback — for Compose Multiplatform on Android, iOS and the
JVM. Every other `ui` and `flow` module draws with it.

The whole set, one row per component and with pictures, is the
[component catalogue](../../docs/components.md).

## Install

```kotlin
implementation("dev.voir.areca:foundation:<version>")
```

It brings `compose-runtime`, `compose-foundation` and `compose-ui` as API dependencies, and nothing
else. The snippets below use `ArecaIcons.*` from the separate, optional icon set,
`dev.voir.areca:icons` ([README](../../standalone/icons/README.md)). Areca is published to GitHub
Packages, so the build needs its repository first; see
[Getting started](../../docs/getting-started.md).

## Quick start

Wrap the application once in `ArecaThemeProvider`, then build screens from the components:

```kotlin
@Composable
fun App(onSignOut: () -> Unit) {
    ArecaThemeProvider(darkTheme = isSystemInDarkTheme()) {
        var push by remember { mutableStateOf(true) }

        BaseLayout(
            contentWindowInsets = WindowInsets.safeDrawing,
            topBar = { TopBar("Account") },
        ) { padding ->
            Column(
                modifier = Modifier.padding(padding).horizontalContentInsets(),
                verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl),
            ) {
                Settings {
                    Section(header = { Text("Notifications") }) {
                        SettingsToggleRow(
                            checked = push,
                            onCheckedChange = { push = it },
                            title = { Text("Push notifications") },
                        )
                    }
                }
                Button("Sign out", onClick = onSignOut, style = ButtonStyle.soft(ActionColor.Danger))
            }
        }
    }
}
```

`ArecaThemeProvider`, `ArecaTheme` and `horizontalContentInsets` are in `dev.voir.areca.theme`; each
component is in `dev.voir.areca.foundation.<family>` — here `layout`, `topbar`, `settings`, `text`
and `button`.

## Theme and customising

- **[Theme](../../docs/theme.md)** — the tokens (colour, typography, spacing, radius, elevation,
  motion), changing the theme through `ArecaThemeProvider`, the surfaces, app-wide component
  defaults through `componentDefaults`, regions that are always dark with `ArecaDarkTheme` and
  `SystemBars` (in `dev.voir.areca.platform`) to colour the system bars around them, and reading
  tokens through `ArecaTheme`.
- **[Customising components](../../docs/customising.md)** — every component's `style: XStyle` and
  `size: XSize`, the `Default` values, presets and `default(...)`, defaults provided for a subtree,
  `ActionColor`, parameter order, interaction and focus, and touch targets.

## Surfaces

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="../../docs/images/surfaces-dark.png">
  <img alt="The same budget screen twice: in SurfaceLayout.Grouped, a grey page with white cards, and in SurfaceLayout.Plain, a white page with grey cards" src="../../docs/images/surfaces-light.png" width="720">
</picture>

Every fill sits on a surface, and components read the role of the surface they are drawn on —
`ArecaTheme.surface.ground`, `raised`, `well`, `onWell`, `separator`, `field` — rather than a fixed
ground token. `ArecaThemeProvider(surfaceLayout = …)` picks the grounds: `SurfaceLayout.Grouped`,
the default, is a grey page with white cards; `SurfaceLayout.Plain` a white page with grey cards. A
sheet, a dialog and a menu are white in both, and their content lies flat on them: a `Section`
there draws no card, and a `Card` there is for emphasis only. `BaseLayout` puts its content
on the page; `Card`, `Section`, `Notice` and `ContentList` inset groups put theirs one level up; so
a field or a chip anywhere takes the right fill without being told.

A container of your own, in `dev.voir.areca.theme`:

| Composable | Use it for |
| --- | --- |
| `RaisedSurface` | Content drawn on a fill of your own in `ArecaTheme.surface.raised`: puts it one level up, as a card does |
| `ProvideSurface(level, root)` | Content at a chosen `SurfaceLevel` (`Page`, `Card`, `Inset`) of the current ladder, or of a `SurfaceRoot` (`Screen`, `Sheet`) — a white panel across a grey page is `SurfaceLevel.Card` |

```kotlin
Box(Modifier.background(ArecaTheme.surface.raised, ArecaTheme.shapes.surface)) {
    RaisedSurface { UsageSummary() }
}
```

The roles, levels and the full ladder are in [Theme](../../docs/theme.md#surfaces).

## Window classes

`ArecaTheme.window` is the space a composable is laid out in, classified, in
`dev.voir.areca.window`. Lay a screen out by it, never by a device check: a tablet in a narrow split
view is a compact window and gets the phone layout.

| Part | Holds |
| --- | --- |
| `WidthClass` | `Compact` under 600dp, `Medium` from 600, `Expanded` from 840, `Large` from 1200, `ExtraLarge` from 1600. Ordered, so `width >= WidthClass.Medium` reads as "at least medium" |
| `HeightClass` | `Compact` under 480dp (a phone on its side), `Medium` from 480, `Expanded` from 900 |
| `InputMode` | `Touch` or `Pointer`: what presses the controls. Touch on Android and iOS, a pointer on the desktop, unless `ArecaThemeProvider(inputMode = …)` says otherwise |
| `WindowProfile` | A familiar name for the combination: `PhonePortrait`, `PhoneLandscape`, `TabletPortrait`, `TabletLandscape`, `Desktop`. Under touch a tablet's is decided by its shape: at least `WindowClass.WideAspectRatio` (1.25) times as wide as tall is `TabletLandscape`, anything squarer — a tablet held upright, an unfolded foldable either way round — `TabletPortrait` |
| `ActionReach` | Where a screen's actions are within reach: `Header` beside the title (a pointer, a phone on its side, a touch window at least 1.25 times as wide as tall) or `Thumb` at the foot of the window (a phone, a tablet held upright, an unfolded foldable). Read it from `ArecaTheme.hostWindow`: the hands hold the device, not the pane |
| `WindowFold` | A foldable's fold or hinge: `orientation` (`FoldOrientation.Vertical` or `Horizontal`), `bounds`, `state` (`FoldState.Flat` or `HalfOpened`) and `isSeparating`. Reported on Android; `null` on iOS and the desktop |
| `WindowClass` | `size`, `width`, `height`, `input`, `profile`, `actionReach`, `isLandscape` and `fold` together |

| Composable | Use it for |
| --- | --- |
| `adaptive(compact, medium, expanded, large, extraLarge)` | A value for the width class in scope — a column count, a gutter. Only `compact` is required; each wider step defaults to the one below |
| `AdaptiveContent(compact, medium, expanded, large, extraLarge)` | One of several layouts for the width class in scope, with the same fallback |
| `WindowClassBox` | A box that reports its own size as the class of its content, so a pane answers for the pane |
| `ProvideWindowClass` | A given class for a subtree: a preview or a test at a chosen window |

```kotlin
AdaptiveContent(
    compact = { MessageList(onOpen = navigateToMessage) },
    medium = {
        Row(Modifier.fillMaxSize()) {
            WindowClassBox(Modifier.width(adaptive(compact = 280.dp, expanded = 320.dp)).fillMaxHeight()) {
                MessageList(onOpen = select)
            }
            VerticalDivider()
            WindowClassBox(Modifier.weight(1f).fillMaxHeight()) { MessageDetail(selected) }
        }
    },
)
```

`ArecaTheme.hostWindow` is the whole window's class whatever `WindowClassBox` is in between — what
opens over the window (a sheet, a dialog, a menu) chooses its form by it.
`AdaptiveDefaults.usesDialog(ArecaTheme.hostWindow)` is the rule `AdaptiveSheet` and `ConfirmSheet`
follow: a dialog from an expanded width up, unless the window is too short; a sheet of your own can
follow it too.

The outermost provider reads the window's size from `LocalWindowInfo`; a nested provider, such as
`ArecaDarkTheme`, keeps the class above it. Until the window is measured the size is unspecified,
which reads as `Compact` width and `Medium` height. `WidthClass.select` and `HeightClass.select` pick
a value outside composition.

On Android the outermost provider also reads the window's fold from Jetpack WindowManager, and
follows the device as it is opened, closed and propped up. A `WindowClassBox` sees the fold in its
own coordinates once it is placed, and none when the fold does not cross it, so a pane beside the
fold has `fold == null`. Keep text and controls off `fold.bounds`; while `fold.state` is
`HalfOpened`, lay each half out for itself. `ListDetail` and `SupportingPane` meet at a vertical
fold and the `EdgeColumn` keeps its clusters either side of a horizontal one by themselves. To
preview a foldable, give `ProvideWindowClass` a class with a fold — `WindowFold.vertical(at, height)`
or `WindowFold.horizontal(at, width)` — measured from the provider's own top-left corner.

## Components

One section per family. Each component's KDoc is the reference for every parameter; this is the
map and the options that matter.

| Family | Packages under `dev.voir.areca.foundation` |
| --- | --- |
| [Buttons and actions](#buttons-and-actions) | `button`, `topbar` |
| [Text](#text) | `text` |
| [Icons](#icons) | `icon` |
| [Badges and avatars](#badges-and-avatars) | `badge`, `avatar` |
| [Stats](#stats) | `stat` |
| [Selection controls](#selection-controls) | `selection` |
| [Fields](#fields) | `field`, `field.select` |
| [Feedback](#feedback) | `feedback` |
| [Notices and status](#notices-and-status) | `notice`, `status` |
| [Overlays](#overlays) | `overlay` |
| [Layout and surfaces](#layout-and-surfaces) | `layout` |
| [Slot rows](#slot-rows) | `slot` |
| [Lists and settings](#lists-and-settings) | `list`, `settings` |
| [Collections](#collections) | `collection` |
| [Tables](#tables) | `table` |
| [Navigation, tabs, tab bar and top bar](#navigation-tabs-tab-bar-and-top-bar) | `navigation`, `tabs`, `tabbar`, `topbar` |
| [Wheel picker](#wheel-picker) | `wheel` |
| [Adaptive layouts](#adaptive-layouts) | `adaptive` |

### Buttons and actions

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="../../docs/images/foundation-buttons-dark.png">
  <img alt="Buttons in each style and size, and icon buttons" src="../../docs/images/foundation-buttons-light.png" width="600">
</picture>

| Component | Use it for |
| --- | --- |
| `Button` | An action with a label, and optionally an icon either side |
| `IconButton` | An action shown by an icon alone. Always give it a `contentDescription`; under a pointer it shows as a tooltip |
| `IconToggleButton` | An icon that is on or off — a favourite, a pin, a mute — announced as a toggle with its state |

```kotlin
Button("Continue", onClick = onContinue, modifier = Modifier.fillMaxWidth(), loading = saving)

IconButton(
    ArecaIcons.Trash,
    contentDescription = "Delete draft",
    onClick = onDelete,
    style = IconButtonStyle.plain(ActionColor.Danger),
)

IconToggleButton(
    checked = favourite,
    onCheckedChange = { favourite = it },
    imageVector = ArecaIcons.Heart,
    checkedImageVector = ArecaIcons.HeartFill,
    contentDescription = "Favourite",
)
```

`IconToggleButton` draws `checkedImageVector` in `checkedStyle` (`IconButtonStyle.plain(ActionColor.Brand)`)
while `checked`, and `imageVector` in `style` (`plain(ActionColor.Neutral)`) otherwise.
`contentDescription` names the thing toggled; assistive technology adds whether it is on, so do not
switch the description with the state. In a top bar, use `FavoriteAction`.

`enabled = false` says the action is unavailable. `loading = true` says it is under way: the press
is blocked, a spinner replaces the trailing slot, and assistive technology hears
`loadingStateDescription`. Button sizes run `ExtraSmall` (32dp) to `ExtraLarge` (64dp), icon button
sizes `Tiny` (20dp) to `ExtraLarge`; `Medium`, 48dp, is the default for both. Those are the touch
heights; under a pointer every step is smaller — `Medium` is 36dp — and reserves no touch target (see
[Customising](../../docs/customising.md#touch-and-pointer-sizes)). The style factories
`solid`, `soft`, `outlined` and `plain` take an `ActionColor`; see
[Customising](../../docs/customising.md).

**Top bar controls.** Inside a `TopBar`'s or `LargeTopBar`'s `leading` and `trailing` slots, use the
bar's own controls rather than icon buttons. They share one size, one hit target and one distance
from the edge, and take their look from the bar's `TopBarActionStyle`.

| Component | Use it for |
| --- | --- |
| `BackAction` | Go back. A chevron, with an optional `label` |
| `CloseAction` | Dismiss a sheet or a full-screen task |
| `DoneAction` / `CancelAction` | Finish or abandon a task, as a word: "Done", "Save"; "Cancel", "Discard" |
| `TextAction` | Any other control written as a word |
| `IconAction` | Any other control shown as one icon |
| `SearchAction`, `ShareAction`, `EditAction`, `DeleteAction`, `AddAction`, `SettingsAction`, `DownloadAction` | The common verbs, each with its icon and an English `contentDescription` |
| `NotificationsAction` / `FilterAction` | Open notifications or filters, with a count in `badge` |
| `FavoriteAction` | Mark and unmark a favourite; reports its `selected` state as a toggle |
| `MenuAction` / `OverflowAction` | Open a navigation drawer; open everything the bar had no room for |
| `AvatarAction` | Who is signed in, normally opening an account menu |
| `Action` | A control holding any mark, sized and aligned like the rest |

```kotlin
TopBar(
    "Photo",
    leading = { BackAction(onClick = onBack) },
    trailing = {
        FavoriteAction(selected = favourite, onClick = { favourite = !favourite })
        ShareAction(onClick = onShare)
        OverflowAction(onClick = onMore)
    },
)
```

### Text

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="../../docs/images/foundation-amounts-dark.png">
  <img alt="Amounts drawn with AmountText at several sizes, with the symbol before and after, masked, and in income and expense colours, beside stats in cards" src="../../docs/images/foundation-amounts-light.png" width="720">
</picture>

| Component | Use it for |
| --- | --- |
| `Text` with a `TextVariant` | All text. The variant names a role — `HeadingSmall`, `BodyMedium`, `Caption`, `LabelLarge`, `Overline` — and `TextColor` a foreground: `Base`, `Subtle`, `Muted`, `Danger`, … |
| `Text` with a `TextStyle` | Text that inherits `LocalTextStyle` and `LocalContentColor` — inside a component's slot |
| `Heading` | A section's title with an optional supporting line and trailing content, announced as a heading |
| `TextLink` | A standalone pressable label — "Forgot password?" |
| `TextLinkPrompt` | A sentence with one pressable part — "No account? Sign up" |
| `SensitiveText` | A value hidden under a veil until revealed — a balance, a card number |
| `AmountText` | A sum of money or a measured quantity the app has formatted: tabular figures, the minor units and the symbol a step smaller, never reordered by bidi |

```kotlin
Text("Recent activity", variant = TextVariant.HeadingSmall)
Text("Updated 2 minutes ago", variant = TextVariant.Caption, color = TextColor.Muted)

Heading(
    title = { Text("Billing") },
    supportive = { Text("Invoices go to the account owner.") },
)

SensitiveText("€4,210.50", revealed = revealed, onRevealedChange = { revealed = it })
```

A plain `Text("…")` inside a component's slot takes the slot's type and colour; pass no style.

Both `Text` overloads take `autoSize: TextAutoSize?` (`androidx.compose.foundation.text`), last,
`null` by default. It shrinks the text to fit its box. For a figure that must stay on one line, pair
it with `maxLines = 1`:

```kotlin
Text(
    total,
    variant = TextVariant.HeadingLarge,
    maxLines = 1,
    autoSize = TextAutoSize.StepBased(minFontSize = 12.sp, maxFontSize = 32.sp),
)
```

**Amounts.** The app formats the figures for the user's locale — grouping, decimal mark, minus sign
— and passes the parts as an `Amount`. `AmountText` only draws them.

| `Amount` property | Holds |
| --- | --- |
| `whole` | The whole part with its grouping: `"1,284"`, `"1 284"` |
| `fraction` | The minor units without the mark: `"50"`; `null` for none |
| `decimalMark` | `"."` (default) or `","` |
| `symbol` | A currency symbol or a unit: `"€"`, `"د.إ"`, `"kg"`; `null` for none |
| `symbolPlacement` | `AmountSymbolPlacement.Before` (default) or `After`. A currency sign before the figures sits against them (`€12`); a symbol with letters (`CHF 12`) or after the figures (`12 kg`) stands a small gap apart |
| `sign` | A sign before everything: `"−"`, `"+"`; `null` for none |
| `spokenText` | What a screen reader says — "minus 1,284 euros 50 cents"; `null` reads the parts as drawn |

```kotlin
AmountText(
    Amount(whole = "1,284", fraction = "50", symbol = "€", sign = "−"),
    style = ArecaTheme.typography.headingLarge,
    color = TextColor.Danger.color,
)

AmountText(Amount(whole = "1 284", fraction = "50", decimalMark = ",", symbol = "د.إ"))

AmountText(Amount(whole = "12", symbol = "kg", symbolPlacement = AmountSymbolPlacement.After))
```

Every digit has the same width (`tnum`), so a column of amounts lines up and a changing total does
not jitter. `minorScale` (`0.7f` by default) sizes the fraction and the symbol against the figures;
`1f` keeps them level. The symbol and the figures are separate texts laid out left to right, so
`د.إ 1 284,50` keeps its order and no control characters are added; the amount is read as one
phrase. With `autoSize` the figures shrink and the symbol keeps its size. `masked = true` draws `••••` in place of the figures and
drops the fraction. An amount is one line (`maxLines = 1`); pass `autoSize` for a hero figure that
shrinks rather than ellipsizes. `style` and `color` default to the ones in scope, so in a row's
`value` or `trailing` slot the amount takes the slot's type.

### Icons

| Component | Use it for |
| --- | --- |
| `Icon` | A glyph inline with text, tinted with `LocalContentColor` and sized by `LocalIconSize` |
| `FeaturedIcon` | A glyph in a shaped container that gives a block its subject — an empty state, a dialog header |
| `Chevron` | A chevron that turns when its direction changes — a disclosure, a dropdown |

```kotlin
Icon(ArecaIcons.Bell, contentDescription = null)

FeaturedIcon(
    ArecaIcons.Inbox,
    contentDescription = null,
    style = FeaturedIconStyle.soft(ActionColor.Brand),
    size = FeaturedIconSize.Large,
)

Chevron(if (expanded) ChevronDirection.Up else ChevronDirection.Down)
```

`FeaturedIconStyle.solidHue(hue)` and `FeaturedIconStyle.softHue(hue)` draw a tile in one of the
categorical colours of `ArecaTheme.hues` — see [Hues](../../docs/theme.md#hues). Solid fills the hue
under a white glyph; soft mixes the hue into the surface under a deeper glyph. Use a hue when the
colour only tells unrelated things apart, and an `ActionColor` when it carries a meaning.

Pass `contentDescription = null` when the icon repeats something already said beside it, and
`tint = Color.Unspecified` for a multicolour mark. `IconSize` steps run `ExtraSmall` (12dp) to
`ExtraLarge` (32dp).

### Badges and avatars

| Component | Use it for |
| --- | --- |
| `Badge` | A short label — a status, a tag, a filter. Pressable with `onClick` |
| `NotificationBadge` | A count, usually overlaid on something else |
| `IndicatorDot` | A status carried by colour alone: presence, unread |
| `BadgeSelect` / `LazyBadgeSelect` / `FlowBadgeSelect` | A group of chips that select something: one line, one scrolling line, or wrapping lines |
| `Chip` | One chip on its own: a toggle announced with its state, a button (`onClick`) — a suggestion — or an applied filter that a press removes, with an optional mark before the label. `ChipStyle` / `ChipSize` (and `LocalChipStyle` / `LocalChipSize`) restyle it |
| `Avatar` | A person or entity: an image, initials from `letters`, or an empty shape |

```kotlin
Badge(style = BadgeStyle.Success, soft = true) { Text("Paid") }

NotificationBadge("3", contentDescription = "3 unread messages")

LazyBadgeSelect(
    filters,
    onSelect = { selected = listOf(it.value) },
    selected = selected,
    style = BadgeStyle.Chip,
    size = BadgeSize.Chip,
)
```

`BadgeStyle.Chip` leaves unchosen chips as bare words; `BadgeStyle.FilledChip` puts them on the
subtle fill, for a row that must read as tappable before anything is chosen. A badge group does not
hold its selection: `onSelect` reports the chip pressed and the caller updates `selected`. With
`BadgeSelectionMode.Single` the chips are radio buttons, with `Multiple` checkboxes.

```kotlin
Chip(label = "Saved only", selected = savedOnly, onSelectedChange = { savedOnly = it })

Chip(
    label = "Food",
    onRemove = { remove(food) },
    removeContentDescription = "Remove Food",
    leading = { Box(Modifier.size(10.dp).background(ArecaTheme.hues.orange, ArecaTheme.shapes.full)) },
)
```

`Chip` sits in the surface's `well` unchosen and fills with `bgContrast` when `selected`. With
`onSelectedChange` it is a toggle; with only `onRemove` it is a label with a cross — the filters
applied above a list. `removeContentDescription` defaults to the English "Remove"; pass a localized
one naming the chip. A set of chips that select stays `BadgeSelect`, `FlowBadgeSelect` or
`LazyBadgeSelect`.

```kotlin
Avatar(letters = "Ada Lovelace", contentDescription = "Ada Lovelace")

Avatar(contentDescription = "Ada Lovelace", size = AvatarSize.Large) {
    Image(
        photo,
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop,
    )
}
```

The avatar's slot takes any image loader's output and clips it. `letters` is reduced to at most two
initials. Without a `contentDescription` the avatar is decorative — right when the name is printed
beside it.

**Logos and brand tiles.** A merchant's logo, a bank, a currency or a service is an `Avatar` in the
tile shape. The initials show only when `content` is `null`, so pass the slot only when there is an
image to draw; without one, `letters` gives the monogram on the surface's `well`:

```kotlin
val logo = merchant.logo
Avatar(
    shape = ArecaTheme.shapes.tile,
    letters = merchant.name,
    content = if (logo != null) {
        { Image(logo, contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop) }
    } else {
        null
    },
)
```

In a settings row's leading slot, `SettingsRowTile { … }` crops any visual to the row's icon tile.

### Stats

| Component | Use it for |
| --- | --- |
| `Stat` | One figure with what it is and, optionally, what it means: "Spent · €2,140 · a month" |
| `StatGrid` | Stats in equal columns, read as a set |

```kotlin
Stat(
    label = "October so far",
    value = "€2,340",
    note = "€218 under your usual pace",
    size = StatSize.Large,
    trailing = { Badge(style = BadgeStyle.Success, soft = true) { Text("−4%") } },
)

StatGrid(columns = 2) {
    Stat(label = "Earned", value = "€4,180", note = "a month")
    Stat(label = "Spent", value = "€2,140", note = "a month")
    Stat(label = "Kept", value = "41%")
    Stat(label = "A day", value = "€71", note = "weekends 1.8× weekdays")
}
```

`label`, `value` and `note` are plain strings; the style and size decide type and ink, and the value
uses tabular digits. `StatSize.Default` is a figure in a grid, `StatSize.Large` the figure a section
is about, with its change in `trailing`. Give a stat `onClick` to open what the figure is about; it
keeps no visible container and its hit area is at least the minimum touch target. `StatGrid` gives
every cell an equal width and every cell of a row the row's height; pass the stats in reading order.

### Selection controls

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="../../docs/images/foundation-selection-dark.png">
  <img alt="Checkboxes, radios, toggles, sliding selectors, swatches, a number stepper and a badge select" src="../../docs/images/foundation-selection-light.png" width="720">
</picture>

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="../../docs/images/foundation-choice-dark.png">
  <img alt="A plan picker of choice cards with one chosen and a badge, toggle chips and removable filter chips, and favourite icon toggle buttons" src="../../docs/images/foundation-choice-light.png" width="720">
</picture>

| Component | Use it for |
| --- | --- |
| `Checkbox` | A value that is part of a form and saved later. `indeterminate` shows a parent of mixed children, whatever `checked` is |
| `Radio` | One option of several. Put `Modifier.selectableGroup()` on the group |
| `Toggle` | A setting that applies the moment it is flipped. `ToggleState` with `BinaryToggleCycle` or `TriStateToggleCycle` |
| `NumberStepper` | A whole number nudged by one between bounds |
| `SlidingSelector` | A row of options with one thumb sliding to the chosen one: a segmented choice, words with a moving pill, a three-state toggle |
| `Swatch` / `SwatchPicker` | One colour sample; a scrolling set of them with a single selection |

These are the bare controls. With a label and a row-wide tap target, use the list rows
(`CheckboxListItem`, `RadioListItem`, `ToggleListItem`) or the settings rows.

```kotlin
Checkbox(accepted, onCheckedChange = { accepted = it })

Toggle(sync, onToggle = { sync = it })

NumberStepper(
    guests,
    onValueChange = { guests = it },
    minValue = 1,
    maxValue = 12,
    labels = NumberStepperLabels(decrease = "Fewer guests", increase = "More guests"),
)
```

`enabled = false` greys a control out as unavailable; `readOnly = true` keeps its colours and
refuses changes, for a value that is real but not the user's to change. `Checkbox` and `Radio` share
`SelectionStyle`; `Toggle` draws from the same `SelectionColors` through its `ToggleStyle`.

`SlidingSelector` takes `SelectorItem`s, each any content, and moves one thumb between their
measured bounds, so options may differ in width. The look is a `SlidingSelectorStyle` preset —
`Segmented` (the default: a track and a raised capsule), `Pill` (no track, a capsule under the chosen
words), `Underline` or `Button` — and the metrics a `SlidingSelectorSize` (`Small`, `Default`,
`Large`). `layout` is `SelectorLayout.Fill` (equal shares, the default), `Hug` or `Scroll` (keeps the
chosen option in view); `orientation` is `Horizontal` or `Vertical`.

```kotlin
SlidingSelector(
    items = listOf("Any level", "Top", "Heart", "Base").map { SelectorItem(it) { Text(it) } },
    selected = level,
    onSelectedChange = { level = it },
)
```

Where one option is always chosen, `onSelectedChange` takes the value itself. The form with
`onSelect: (T?) -> Unit` and `mode = SelectorMode.Optional` allows nothing chosen: tapping the chosen option calls `onSelect(null)`,
and the options are announced as switches rather than radio buttons. A `SelectorItem` can give its
own `thumb` and `contentSelected` colours, which the thumb cross-fades between as it moves, and a
`contentDescription` for content that does not name it alone:

```kotlin
SlidingSelector(
    items = listOf(
        SelectorItem(Note.Has, thumb = ArecaTheme.colors.fgBase, contentDescription = "Has iris") {
            Text("Has")
        },
        SelectorItem(Note.Avoid, thumb = ArecaTheme.colors.bgDangerBase, contentDescription = "Avoid iris") {
            Text("Avoid")
        },
    ),
    selected = iris,
    onSelect = { iris = it },
    mode = SelectorMode.Optional,
    style = SlidingSelectorStyle.Pill,
    layout = SelectorLayout.Hug,
)
```

The thumb opens in place, slides on the theme's `motion.slide()` spring (`SlidingSelectorStyle.motion`
changes it), grows in at a first choice and shrinks away when cleared. With
`ArecaMotion.reduceMotion` it fades between places instead. `Tabs` and `FloatingTabBar` are built on
it, so every one of them moves the same way. See the [pickers recipe](../../docs/recipes/pickers.md).

### Fields

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="../../docs/images/foundation-fields-dark.png">
  <img alt="Text, password, search and select fields, one showing an error" src="../../docs/images/foundation-fields-light.png" width="720">
</picture>

Every field is a `Field` container with an editor or a value inside. Put it in a `FieldGroup` for
its label and its message.

| Component | Use it for |
| --- | --- |
| `TextField` | Free text. `format` changes the display without touching your state |
| `PasswordField` | A secret, with a reveal control |
| `SearchField` | A query, with a magnifier, a clear control and the keyboard's search action |
| `AffixTextField` | A value with a fixed prefix or suffix — a currency sign, a unit |
| `OtpField` | A one-time code, one character to a cell, with `onComplete` when the last one lands |
| `SelectField` | A value chosen from a dropdown anchored to the field |
| `SelectFlowModal` | A choice in a sheet, as tall as its options: a few at their rows' height, many up to the cap, a search over the whole sheet |
| `FieldRow` | Several parts sharing one border and one status — a dialling code and a number |
| `TextSegment` / `SelectSegment` | The parts of a `FieldRow`: one typed into, one chosen from a dropdown |
| `FieldGroup` | The label above, beside or inside the control, and one assistive or error line |
| `FieldGroupLayout` | `FieldGroup`'s arrangement with the label and message as slots, for renderers of your own |
| `Field` | The bare container, for a value no stock field holds |

```kotlin
FieldGroup(label = "Email", required = true, error = emailError) { field ->
    TextField(
        email,
        onValueChange = { email = it },
        modifier = Modifier.fillMaxWidth(),
        field = field,
        singleLine = true,
        placeholder = { FieldDefaults.Placeholder("you@example.com") },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next,
        ),
    )
}
```

**Forward the scope.** The group hands its control a `FieldScope` holding the status, the label and
the focus handle. A field given it draws the error state, announces the label and the error, and
takes focus when the label is tapped. `labelPlacement = FieldLabelPlacement.Inside` draws the label
inside the box, and works only through the scope. The group decides the status from `error`.

**Looks.** Fields default to `FieldDefaults.style` and `FieldDefaults.size`, which read
`LocalFieldStyle` and `LocalFieldSize`. The looks are factories: `outlined()` (the theme's default),
`filled()`, `soft()`, `solidNeutral()`, `underlined()` and `inline()`, for a field inside a surface
that already draws the boundary. Their backgrounds are left unspecified and take a role of the
surface the field is drawn on: `FieldStyle.bgRole` is `SurfaceRole.Field` for `outlined()` (white
in light) and `SurfaceRole.Well` for `filled()`, `soft()` and `solidNeutral()`; a filled field turns
to `Field` while focused (`bgFocusedRole`). Provide one look for a whole form:

```kotlin
CompositionLocalProvider(LocalFieldStyle provides FieldStyle.filled()) {
    SignUpForm()
}
```

**Select fields.** `SelectField`, `SelectFlowModal` and `SelectSegment` are in
`dev.voir.areca.foundation.field.select` and take their options as `SelectOption(key, value, label)`.
Selection is controlled: `onChange` reports the values that should be selected next, and nothing
moves until your state comes back.

A `SelectFlowModal` given `sections` instead of `options` groups them under titles —
`SelectOptionSection(key, title, options, indexed)`, where `indexed = true` breaks a section such as
"All" under letter headers (pass its options already sorted). With `onCreateOption` and a search
field, a last row offers to add the typed query as the user's own — "Add “Gorenje” as your own" —
and stays when nothing matched. `SelectFlowModalLabels` holds the sheet's copy — in either
overload, so a picker is localized the same way with or without sections. The stock body is
`SelectFlowModalDefaults.SectionedList`, and `CreateOptionRow` is there for a custom one.
The search field above the options, `SelectFlowModalDefaults.Search`, takes the app's field style
and size in scope (`FieldDefaults.style`, `FieldDefaults.size`), so it looks like every other field.

Like every `FlowModal`, the sheet is as tall as what it holds: a theme or a language opens at the
height of its rows, a long list stops at the cap and scrolls, and a picker with search takes the
whole sheet so it does not jump while the search narrows the list. The options lie straight on the sheet as rows with hairlines, like a settings section's, each with its
`SelectOption(description, leading)` line and mark and a brand-coloured tick; `title` adds the stock
`FlowModalTopBar` with a close cross. A single choice with no `footer` closes the sheet once picked
(`closeOnSelect`), so `onChange` only stores the value; pressing the chosen option keeps it rather
than leaving the choice empty (`clearable = true` lets it clear). A closed sheet forgets its search
and calls `onQueryChange("")`, and takes the keyboard with it. `SelectOption(detail = "$")` puts a
short value at the row's end. `headerBottom = { … }` puts content under the stock title — a pill
naming the region a list of banks is drawn from — and keeps the stock close, which clears focus and
hides the keyboard; pass `header` only to replace the header outright. Provide the app's
`SelectFlowModalLabels` once through
`LocalSelectFlowModalLabels` in `componentDefaults`. The stock bodies are
`SelectFlowModalDefaults.List` and `SectionedList`; `SelectField` and `SelectSegment` draw their
dropdown with `SelectFlowModalDefaults.MenuList`, plain rows on the menu.

```kotlin
SelectFlowModal(
    controller = themePicker,
    title = "Appearance",
    values = setOf(theme),
    options = listOf(
        SelectOption(System, System, "System", description = "Follows the device",
            leading = { SettingsRowIcon(ArecaIcons.Monitor, hue = ArecaTheme.hues.gray) }),
        SelectOption(Light, Light, "Light", leading = { SettingsRowIcon(ArecaIcons.Sun, hue = ArecaTheme.hues.orange) }),
        SelectOption(Dark, Dark, "Dark", leading = { SettingsRowIcon(ArecaIcons.MoonStar, hue = ArecaTheme.hues.indigo) }),
    ),
    onChange = { picked -> picked.firstOrNull()?.let(onTheme) },
)
```

```kotlin
SelectFlowModal(
    controller = brandPicker,
    values = setOf(brand),
    sections = listOf(
        SelectOptionSection("yours", "Yours", yours),
        SelectOptionSection("popular", "Popular", popular),
        SelectOptionSection("all", "All", allSorted, indexed = true),
    ),
    title = "Brand",
    onChange = { picked -> picked.firstOrNull()?.let(onBrand) },
    state = search,
    onQueryChange = onSearch,
    onCreateOption = { query -> onAddBrand(query); brandPicker.hide() },
)
```

**Compound fields.** A `FieldRow` holds segments that share its border, status and focus ring:

```kotlin
FieldGroup(label = "Phone number") { field ->
    FieldRow(modifier = Modifier.fillMaxWidth(), field = field) {
        SelectSegment(
            values = setOf(dialCode),
            onChange = { codes -> codes.firstOrNull()?.let { dialCode = it } },
            options = dialCodes,
        )
        TextSegment(
            phone,
            onValueChange = { phone = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        )
    }
}
```

A `TextSegment` takes the leftover width (`weight = 1f`); a `SelectSegment` sizes to its selection
unless given a `width` or a `weight`.

**Your own label or message.** `FieldGroupLayout` arranges a label slot, the control and a message
slot the way `FieldGroup` does, but leaves the status to you — for a label with a counter opposite
it, or a message with an icon:

```kotlin
FieldGroupLayout(
    label = { Text("Bio", variant = TextVariant.LabelMedium) },
    message = { Text("${bio.length} / 160", variant = TextVariant.Caption, color = TextColor.Muted) },
) {
    TextField(bio, onValueChange = { bio = it }, modifier = Modifier.fillMaxWidth())
}
```

### Feedback

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="../../docs/images/foundation-feedback-dark.png">
  <img alt="A notice, a progress bar, skeleton rows, a toast and a status scene" src="../../docs/images/foundation-feedback-light.png" width="720">
</picture>

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="../../docs/images/foundation-states-dark.png">
  <img alt="Empty and failed fallbacks in cards, and skeleton placeholders shaped like the content they stand in for" src="../../docs/images/foundation-states-light.png" width="720">
</picture>

| Component | Use it for |
| --- | --- |
| `Spinner` | Work of unknown length |
| `ProgressBar` | Work with a known total; `progress = null` for indeterminate |
| `StepIndicator` | Which of a handful of steps the user is on — the bar atop a setup flow |
| `Skeleton` | Placeholder bars where content is still loading |
| `SkeletonBlock` | One placeholder shape sized by its modifier — an avatar, a thumbnail, a chip, a card |
| `SkeletonGroup` | A region of placeholders announced once, "Loading transactions" |
| `RequirementList` | The rules a value must meet, each ticked off as it is met |
| `Fallback` | Part of a screen standing in for missing content: empty, failed, offline. The `Fallback(title, …, tone)` overload is the stock empty state and failure in words |
| `ToastHost` / `Toast` | A brief message about something that just happened. `Toast` is one toast's surface, drawn by the host |
| `AnnounceOnChange` | A sentence spoken by the screen reader when it appears or changes, without moving focus |

```kotlin
ProgressBar(0.64f, label = "64%", contentDescription = "Uploading report.pdf")

Fallback(
    top = { FeaturedIcon(ArecaIcons.Inbox, contentDescription = null) },
    heading = { Text("No messages yet") },
    body = { Text("Messages from your team appear here.") },
    content = { Button("Invite a teammate", onClick = onInvite) },
)
```

A `Fallback` is centred; `horizontalAlignment = Alignment.Start` sets it, text included, against
the gutter, for a message that belongs to one section of a page.

Most screens need one of two fallbacks: nothing is there, or reading it failed. The
`Fallback(title, …)` overload draws both from words. `tone` picks the mark, the button and the room
around the block:

| `tone` | Mark (`icon` default) | Button | Room |
| --- | --- | --- | --- |
| `FallbackTone.Empty` (default) | A tray, neutral | Soft neutral: a way back — "Show all", "Clear filters" | Generous: `spacing.xl` across, `spacing.xl6` above and below |
| `FallbackTone.Failure` | An alert circle, danger | Soft brand: "Try again" | `FallbackSize.Default` |

```kotlin
Fallback(
    title = "No transactions yet",
    body = "Transactions you add appear here.",
    actionText = "Show all wallets",
    onAction = onShowAll,
)

Fallback(
    title = "Couldn't load transactions",
    body = "Check your connection and try again.",
    tone = FallbackTone.Failure,
    actionText = "Try again",
    onAction = onRetry,
)
```

Both glyphs ship in foundation, so the overload needs no icons module. Pass `icon` to change the
mark, `icon = null` for none. The button shows only when both `actionText` and `onAction` are set.
The empty tone's button is never the screen's call to action; for "New invoice" use the slot form
with a solid `Button`, or put the action in the top bar.

`Skeleton` sizes its bars from a line of text. `SkeletonBlock` is one shape at exactly the size its
modifier gives it, so it can match what replaces it. Placeholders are silent to assistive
technology; wrap a region of them in `SkeletonGroup`, which reads its `contentDescription` once:

```kotlin
SkeletonGroup(contentDescription = "Loading wallets") {
    Row(horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg)) {
        SkeletonBlock(Modifier.size(48.dp), shape = ArecaTheme.shapes.full)
        SkeletonBlock(Modifier.weight(1f).height(48.dp))
    }
    SkeletonBlock(Modifier.fillMaxWidth().aspectRatio(2f), shape = ArecaTheme.shapes.surface)
}
```

`shape` defaults to the skeleton size's shape; `animated = false` stops the shimmer and `reversed`
runs it against the reading direction. `rememberSettledLoading(loading)` takes the flicker out of a
loading face: a load that ends within `delay` (150ms) never shows one, and a face that does show
stays at least `minimumDuration` (400ms):

```kotlin
val showSkeleton = rememberSettledLoading(state.loading)
when {
    showSkeleton -> SkeletonGroup(contentDescription = "Loading wallets") { repeat(3) { ListItemSkeleton() } }
    state.loading -> Unit
    else -> WalletRows(state.wallets)
}
```

While `loading` is `true` but the face has not shown yet, draw nothing, not the empty state.

Toasts queue in a `ToastState` and show one at a time in a `ToastHost`, which fills its parent and
keeps clear of the window insets. On a screen framed by `BaseLayout`, pass the state as
`BaseLayout(toastState = toasts)` instead: the frame draws the host above its bottom bar and
floating action. A host of your own:

```kotlin
val toasts = rememberToastState()

Box(Modifier.fillMaxSize()) {
    Button("Archive", onClick = { toasts.show("Conversation archived") })
    ToastHost(toasts)
}
```

`show` returns the toast's id for `dismiss(id)`. A toast's duration is lengthened by the platform's
accessibility timeout; `duration = null` keeps it until dismissed, which a toast carrying an action
needs. `AnnounceOnChange` is in `dev.voir.areca.foundation`; see [Accessibility](#accessibility).

### Notices and status

| Component | Use it for |
| --- | --- |
| `Notice` | A block set off from the page to say one thing — a banner with a title, or a one-line note |
| `NoticeText` | A line of a notice's body; `quiet = true` for a secondary line |
| `AnimatedNotice` | A notice that is present only while there is something to say |
| `StatusScene` | A whole screen given over to one piece of work: running, done, or stopped |
| `StatusMark` | The disc a status scene shows — working, then success or stopped |
| `StatusSceneActions` | A status scene's primary and secondary buttons |

```kotlin
Notice("Payment failed", icon = ArecaIcons.AlertTriangle, tone = NoticeTone.Danger) {
    NoticeText("Your card was declined.")
    NoticeText("Try another card or contact your bank.", quiet = true)
}

StatusScene(
    "Import complete",
    mark = { StatusMark(StatusMarkState.Success, contentDescription = null) },
    message = "248 contacts were added.",
    actions = { StatusSceneActions("Done", onPrimary = onDone) },
)
```

A notice takes its colours from `tone` and `emphasis` through `NoticeStyle.of(...)`; a danger notice
is filled by default, every other tone subtle. A subtle notice is on the `raised` fill of the
surface it is drawn on, and its content one level up, so a neutral soft button inside it still
reads. A style provided through `LocalNoticeStyle` replaces
the tone's for every notice in the subtree. A status scene *is* the screen and stages its entrance;
a `Fallback` stands in for part of one and appears at once. Their slots line up one for one.

### Overlays

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="../../docs/images/foundation-overlays-dark.png">
  <img alt="A dialog and a confirm sheet" src="../../docs/images/foundation-overlays-light.png" width="720">
</picture>

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="../../docs/images/foundation-sheets-dark.png">
  <img alt="A form sheet with flat settings rows and a commit button, and a category picker sheet with search, icon tiles, descriptions and one option ticked" src="../../docs/images/foundation-sheets-light.png" width="720">
</picture>

| Component | Use it for |
| --- | --- |
| `ConfirmSheet` | A yes-or-no question answered with a thumb: sign out, delete, discard. A centred dialog on a window from expanded up |
| `ConfirmationDialog` | The same question as a centred dialog, with `suspend` handlers that show progress |
| `Dialog` | A centred window for a short task — rename, pick one thing. End it with `ModalDefaults.Actions` |
| `FlowModal` | A bottom sheet the user can drag between heights and away — a form, a picker, a filter. Shown by a `visible` flag, by an `item` it is about, or by a `FlowModalController` |
| `FlowModalTopBar` | A sheet's header: its title centred, a close cross at the end, a search field pinned in `bottom` |
| `FlowModalActionBar` | A sheet's footer: the commit button, and an optional secondary action in `leading` |
| `FlowModalSecondaryAction` | The secondary button in a `FlowModalActionBar` — "Cancel", "Reset" |
| `TooltipBox` | A short label naming its content when the pointer rests on it or keyboard focus reaches it. Never under touch |
| `DropdownPopup` | A surface hanging off the control that opened it — a menu |

```kotlin
ConfirmSheet(
    visible = confirmingSignOut,
    icon = null,
    title = "Sign out?",
    description = "You'll need your password to sign back in.",
    confirmText = "Sign out",
    onConfirm = {
        confirmingSignOut = false
        onSignOut()
    },
    onDismiss = { confirmingSignOut = false },
)
```

A `ConfirmSheet` is a `FlowModal` underneath, so the sheet defaults in scope — `LocalFlowModalStyle`,
`LocalFlowModalSize`, an `ArecaDarkTheme` — reach it. A question whose yes destroys something takes
`style = ConfirmSheetStyle.Danger`: the tile and the confirming button in the danger family.
`extra` sits inside the sheet's side padding, as the buttons do; wrap choice rows in
`ConfirmSheetRows` so they run edge to edge, as rows do on any sheet.

A sheet spans the window unless `FlowModalSize.maxWidth` caps it; a capped sheet is centred along
the bottom edge and its scrim still covers the window. The sheet opens in its own window, so an app
that draws a phone-wide column on a tablet caps its sheets the same way, once, where it provides its
theme:

```kotlin
CompositionLocalProvider(
    LocalFlowModalSize provides FlowModalSize.default(maxWidth = 480.dp),
) {
    AppContent()
}
```

A `FlowModal` has three forms. Each keeps the sheet composed, so it slides out rather than
vanishing; never wrap one in `if`.

| Form | Use it when |
| --- | --- |
| `FlowModal(visible, onDismissRequest)` | Screen or view model state says whether the sheet is open |
| `FlowModal(item, onDismissRequest) { item -> }` | The sheet is about one thing — the transaction being edited, the file being renamed. It is open while `item` is not `null` |
| `FlowModal(controller)` | Code opens and closes the sheet: `show()`, `hide()`, or `hideAndWait()` before navigating |

`onDismissRequest` is called on back, a press on the scrim or a drag down; clear the state in it.
The `visible` and `item` forms take the controller's heights and drag behaviour from a `controller`
argument, `rememberFlowModal()` by default, and `partial` says whether the sheet opens at its partial
height. The `item` form keeps drawing the last item while the sheet slides out, so the sheet leaves
the way it came even when the item is gone at once — deleted from the list it came from.
`rememberLastNonNull(value)` does the same for an overlay of your own.

**Height.** Every sheet is as tall as its content, up to `maxHeightFraction` of the window (never
past the status bar): a short form or three choices open at their own height, a long list stops at
the cap and scrolls while the header and the footer stay. Content that should be tall whatever it
holds fills its body — a lazy list, or `Modifier.fillMaxHeight()`. A sheet opens at its full height:
a partial height — `partialMultiplier` of the cap — is opt-in, `rememberFlowModal(allowPartial =
true)` for a long list that may peek first, and exists only when the content is taller than it;
`show(partial = true)` on a shorter sheet opens it fully, and `state` reports `Expanded`. Partially
open, the footer stays at the bottom and the body is cut off above it. A change of content moves the
sheet to its new height — content that arrives while the sheet opens (data replacing a placeholder)
bends the opening toward the new height in one motion — and the keyboard counts against the cap. `autoHeight` is deprecated and
ignored.

Give a sheet a `FlowModalTopBar` as `header` and a `FlowModalActionBar` as `footer`. The drag handle
floats over the top of the sheet and the header starts at the sheet's top edge; `FlowModalTopBar`
takes the modal top-bar style and size, centres the title and clears the handle with
`FlowModalHeaderInsets`. Without a header, the body starts below the handle:

```kotlin
var filtering by rememberSaveable { mutableStateOf(false) }

Button("Filters", onClick = { filtering = true })

FlowModal(
    visible = filtering,
    onDismissRequest = { filtering = false },
    header = { FlowModalTopBar("Filters", onClose = { filtering = false }) },
    footer = { FlowModalActionBar("Apply", onConfirm = { filtering = false }) },
) {
    FilterOptions()
}

FlowModal(
    item = renaming,
    onDismissRequest = { renaming = null },
    header = { file -> FlowModalTopBar("Rename file", onClose = { renaming = null }) },
) { file ->
    RenameForm(file)
}
```

`FlowModalTopBar` draws the cross only when `onClose` is set; a sheet whose footer has a Cancel
passes none. `leading` holds a way back in a sheet that is a step of a flow, `trailing` replaces
the cross, and `bottom` holds content under the title inside the header — a `SearchField` pinned
above a list. Name the cross with `closeContentDescription`.

To navigate once a sheet has left, not while it is still on screen, hide it from a coroutine with
`hideAndWait()`, which returns when the sheet is hidden:

```kotlin
val picker = rememberFlowModal()
val scope = rememberCoroutineScope()

FlowModal(picker, header = { FlowModalTopBar("Choose a wallet", onClose = { picker.hide() }) }) {
    WalletRows(onPick = { wallet ->
        scope.launch {
            picker.hideAndWait()
            onOpenWallet(wallet)
        }
    })
}
```

The action bar's `layout` decides how its two ends share the width:
`FlowModalActionBarLayout.Trailing`, the default, keeps the commit at its own width against the end
edge; `Split(secondaryWeight, confirmWeight)` — `1f` and `2f` by default — stretches both across the
bar, with a `FlowModalSecondaryAction` in `leading` taking its share. `showDivider = false` drops
the hairline above the buttons.

A `DropdownPopup` positions itself against the control marked with `Modifier.anchored`, and flips to
the other side when it would run off screen:

```kotlin
val menu = rememberDropdownPopup()

Box {
    Button("Sort", onClick = { menu.toggle() }, modifier = Modifier.anchored(menu.anchorState))
    DropdownPopup(menu, onDismissRequest = { menu.dismiss() }) {
        SortOptions()
    }
}
```

`Dialog` and `FlowModal` open in their own platform window, above everything, with one scrim.

### Layout and surfaces

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="../../docs/images/foundation-layouts-dark.png">
  <img alt="A panel page with its total over a breakdown and a floating action above the tab bar, and a form whose bottom action bar has a caption and a soft, not-ready button" src="../../docs/images/foundation-layouts-light.png" width="720">
</picture>

| Component | Use it for |
| --- | --- |
| `BaseLayout` | A screen's frame: top and bottom bars drawn over full-height content that is handed their height as padding, with an optional floating action and toasts |
| `BottomActionBar` | A screen's bottom bar with its primary action: alone, beside a count ("3 selected · Move"), under a caption, softened until a form is complete, or disabled with a hint saying what is missing |
| `PanelLayout` | A page in two parts: a header on the page's ground over a rounded-top panel that runs to the bottom of the screen — a total over its breakdown |
| `CollapsingHeaderLayout` | A header that scrolls away above a strip that stays — artwork over tabs — optionally into a compact band that stays |
| `PullToRefreshLayout` / `PullToRefreshIndicator` | Pull-to-refresh around scrolling content, and its default indicator. [Experimental](#experimental-api) |
| `Card` | A surface that groups content, optionally pressable or expandable, with an optional footer for its status and actions |
| `ChoiceCard` | A card that is one answer of a choice — a plan, a delivery speed — with an accent ring when chosen, a radio or checkbox mark, a badge on its top edge, and radio or checkbox semantics |
| `Expandable` | A section that opens and closes under a trigger you supply |
| `HorizontalDivider` / `VerticalDivider` | A rule, optionally labelled — "or" between two choices |
| `RelativePositionLayout` | A companion on one of eight sides of its content — a label above a bar |
| `Overlay` | A tint over a `Box`'s content without changing its size — a loading veil |
| `VerticalScrollbar` / `HorizontalScrollbar` | A draggable scrollbar beside a scrolling element of your own, drawn under a pointer and never under touch. Areca's lists, grids and tables draw their own |

```kotlin
BaseLayout(
    contentWindowInsets = WindowInsets.safeDrawing,
    topBar = { TopBar("Inbox") },
) { padding ->
    LazyColumn(contentPadding = padding) {
        items(messages) { message -> MessageRow(message) }
    }
}
```

The content is laid out under the bars and must apply the padding it is handed — normally as a lazy
list's `contentPadding` — or the bars hide its first and last items. The frame paints the page — the
page ground of the theme's surface layout, grey for `Grouped` and white for `Plain` — and puts its
content on the page surface, so a screen stays opaque while navigation slides screens over each
other. `bg` defaults to `Color.Unspecified`, which is that ground; give it a colour for a screen
whose page differs, and `Color.Transparent` only to let something show through.

A screen's floating primary action — a `Button` with a leading glyph — goes in `floatingAction`.
The frame places it at the end edge above the bottom bar, or above the navigation bar when there is
none, and adds its height to the bottom padding, so the last item scrolls clear of it. A
`toastState` shows that state's toasts above the bottom bar and the floating action. Keep `overlay`
for what floats over everything; it is composed outside the measuring pass and cannot see the bars.

**Choice cards.** A plan picker or a paywall is a column of `ChoiceCard`s. Put the set in a column
with `Modifier.selectableGroup()`:

```kotlin
Column(
    modifier = Modifier.selectableGroup(),
    verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
) {
    ChoiceCard(
        selected = plan == Plan.Yearly,
        onClick = { plan = Plan.Yearly },
        badge = { Badge(style = BadgeStyle.Brand) { Text("Best value") } },
        trailing = { AmountText(Amount(whole = "39", fraction = "99", symbol = "€")) },
    ) {
        Text("Yearly", variant = TextVariant.LabelLarge)
        Text("€3.33 a month", variant = TextVariant.BodySmall, color = TextColor.Subtle)
    }
    ChoiceCard(selected = plan == Plan.Monthly, onClick = { plan = Plan.Monthly }) {
        Text("Monthly", variant = TextVariant.LabelLarge)
    }
}
```

The card takes the surface's `raised` fill and `shapes.surface`; the chosen one wears an
`accentBase` ring, the others a hairline in `borderBase`. `mode = ChoiceCardMode.Single` (default)
marks it with a radio and gives it the radio button role; `ChoiceCardMode.Multiple` a checkbox and
the checkbox role. `showMark = false` drops the mark and keeps the ring and the semantics. The whole
card is the target and reports its state once.

```kotlin
val toasts = rememberToastState()

BaseLayout(
    topBar = { TopBar("Wallets") },
    floatingAction = {
        Button("Add", onClick = onAdd, leading = { Icon(ArecaIcons.Plus, contentDescription = null) })
    },
    toastState = toasts,
) { padding ->
    WalletList(contentPadding = padding)
}
```

A screen's primary action goes in a `BottomActionBar` passed as `bottomBar`. It is opaque, draws a
hairline above itself and, like `TabBar`, clears the navigation bar and the keyboard by default — so
keep the bottom insets out of `BaseLayout`'s `contentWindowInsets`, or they are counted twice. A
`leading` slot puts a count beside the button; `enabled = false` with a `hint` says why the action
is not available yet. `caption` is a line above the button, something worth knowing before
committing. `ready = false` softens the button to `BottomActionBarStyle.buttonNotReady` but keeps it
pressable, so a press can run the form's validation and put each complaint under its field; keep
`enabled = false` for an action that must not happen at all. `BottomActionBarStyle.caption` colours
the caption. Keep `FlowModalActionBar` for sheets: it is transparent, adds no insets and has
no hint line.

```kotlin
BaseLayout(
    topBar = { TopBar("Move items") },
    bottomBar = {
        BottomActionBar(
            actionText = "Move",
            onAction = onMove,
            enabled = selected.isNotEmpty(),
            hint = if (selected.isEmpty()) "Select items to move" else null,
            leading = { Text("${selected.size} selected") },
        )
    },
) { padding -> ItemsList(contentPadding = padding) }

BaseLayout(
    topBar = { TopBar("New wallet") },
    bottomBar = {
        BottomActionBar(
            actionText = "Create wallet",
            onAction = { attempted = true; if (complete) onCreate() },
            caption = "You can rename it later.",
            ready = complete,
        )
    },
) { padding -> WalletForm(attempted, contentPadding = padding) }
```

A `PanelLayout` puts a header block on the page's ground and the content proper on a panel with
rounded top corners, filled with the page's `raised` role, that runs from under the header to the
bottom of the screen even when the page is short. The whole page scrolls as one column, so the
header and the panel hold no lazy list. Inside a `BaseLayout`, pass the padding it hands its
content as `contentPadding`: the top goes above the header, the bottom at the foot of the panel.
Both slots bring their own horizontal insets. The page scrolls back to its top when its destination
is selected again:

```kotlin
BaseLayout(topBar = { TopBar("Analytics") }) { padding ->
    PanelLayout(
        contentPadding = padding,
        header = { Stat("Spent in November", "€1,284.50", Modifier.contentInsets(), size = StatSize.Large) },
        panel = { CategoryBreakdown(Modifier.contentInsets()) },
    )
}
```

`panelShape` defaults to the `xl` radius on the top corners; hoist `scrollState` to read or drive
the scroll.

A `Card`'s content is handed the card's padding the same way as `BaseLayout`'s, so a full-bleed
image can ignore it.

A card's default style, `CardStyle.Default` (the theme seeds `LocalCardStyle` with it), fills the
card with the `raised` role of the surface it sits on — white on a grouped page, grey on a plain one
or in a sheet — with no border, in `shapes.surface`. `CardStyle.Outlined` keeps the ground it sits
on and separates by its outline alone; `CardStyle.SoftNeutral` is set into the surface, in its
`well`. What the card holds is drawn on the card's surface.

A card's status and actions go in its `footer`, a strip along the bottom filled by a
`CardFooterStyle`: `plain()` with a hairline, `soft(ActionColor)` tinted (`Neutral` takes the card's
`well`), `solid(ActionColor)` filled, or `of(bg, content)` in colours of your own. Text and buttons in the footer take its ink and
a matching borderless button style.

```kotlin
Card(
    modifier = Modifier.fillMaxWidth(),
    style = CardStyle.Outlined,
    footerStyle = CardFooterStyle.soft(ActionColor.Danger),
    footer = {
        Text("Payment failed on 3 November", modifier = Modifier.weight(1f))
        Button(onClick = onUpdateCard) { Text("Update card") }
    },
) { padding ->
    Heading(
        title = { Text("Team plan") },
        supportive = { Text("Visa ending 4242") },
        modifier = Modifier.padding(padding),
    )
}
```

A `CollapsingHeaderLayout` given a `collapsed` band collapses into it rather than out of sight: the
header stops at the band's height and the band fades in over it. Give the band an opaque ground.
Hoist `rememberCollapsingHeaderState()` into its `state` to read `collapsedFraction`, `0f` fully
shown to `1f` collapsed.

`Overlay` is a `BoxScope` extension; declare it last so it draws over what came before:

```kotlin
Box {
    ReportPreview()
    if (refreshing) {
        Overlay(color = ArecaTheme.colors.bgOverlayDark) {
            Spinner(contentDescription = "Refreshing")
        }
    }
}
```

### Slot rows

| Component | Use it for |
| --- | --- |
| `SlotRow` | A row of a start slot, a required centre and an end slot — the row buttons, badges, list rows and bars are built on. Build a row of your own with it so it lines up with theirs |

```kotlin
SlotRow(
    layout = SlotRowLayout.Auto.copy(gap = ArecaTheme.spacing.lg),
    start = { Avatar(letters = person.name, size = AvatarSize.Small) },
    center = {
        Column {
            Text(
                text = person.name,
                modifier = Modifier.firstLineCenter(ArecaTheme.typography.labelLarge),
                variant = TextVariant.LabelLarge,
            )
            Text(text = person.handle, variant = TextVariant.BodySmall, color = TextColor.Muted)
        }
    },
    end = { Chevron(ChevronDirection.Right) },
)
```

Pick the `SlotRowLayout` by the text in the centre: `Center` for one or two lines, `Top` for three
or more, `FirstLine` for a marker no taller than a line, and `Auto`, which picks `Center` or `Top`
from the lines `Modifier.firstLineCenter(textStyle)` reports. Every list and settings row uses
`Auto`. `SlotSize.Fixed` keeps a start slot's width when it is empty, so a column of rows lines up
with or without icons; `SlotRowLayout.Centered` keeps the centre on the row's centre line, for a
title between two controls.

### Lists and settings

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="../../docs/images/foundation-lists-dark.png">
  <img alt="A grouped list and a settings list" src="../../docs/images/foundation-lists-light.png" width="720">
</picture>

| Component | Use it for |
| --- | --- |
| `List` / `LazyList` | A grouped list declared with `item(...)` and `divider(...)`: eager for a short list, lazy for a long one |
| `ListItemColumn` | A group of row composables, with the separators drawn for you |
| `ListItem` | A row: overline, title, subtitle, caption, value, leading and trailing slots |
| `NavigationListItem` | A row that opens something, with a chevron |
| `CheckboxListItem` / `RadioListItem` / `ToggleListItem` / `CheckmarkListItem` | A row that is a selection control; the row reports the state |
| `ExpandableListItem` | A row that discloses content under it, optionally on a soft ground shared with what it reveals |
| `ListItemSkeleton` | A row-shaped placeholder |
| `ListItemChevron` / `ListItemCheckmark` | The trailing chevron and check mark of a row you compose, taking colour, size and timing from the row |
| `Settings` / `Section` | A settings screen: sections of rows, each section on its own card |
| `SettingsRow` | A settings row: a value, a navigation, or an action |
| `SettingsToggleRow` / `SettingsSelectRow` / `SettingsOptionRow` / `SettingsTextFieldRow` | A row that is a switch, a choice from a dropdown, one option of a set, or a field typed in place |
| `SettingsRowIcon` / `SettingsRowTile` | A row's leading glyph on a tile — in an `ActionColor`, a categorical hue (`SettingsRowIcon(imageVector, hue = …)`) or a style of your own; any other leading visual cropped to the same tile |
| `SettingsRowLink` | A second action at the end of a pressable settings row — "Verify" beside an address |
| `SettingsBlock` | Free content on a section card, inset like a row — a preview, a swatch picker |
| `SettingsRowSkeleton` | A settings-row-shaped placeholder |

```kotlin
List {
    item(
        key = "profile",
        title = { Text("Profile") },
        onClick = onProfile,
        trailing = { ListItemChevron() },
    )
    item(key = "devices", title = { Text("Devices") }, value = { Text("3") }, onClick = onDevices)
    divider(label = "Danger zone")
    item(key = "delete", title = { Text("Delete account") }, onClick = onDelete)
}
```

```kotlin
Settings {
    Section(header = { Text("Notifications") }, footer = { Text("Applies to this device.") }) {
        SettingsToggleRow(
            checked = push,
            onCheckedChange = { push = it },
            title = { Text("Push") },
            leading = { SettingsRowIcon(ArecaIcons.Bell) },
        )
        SettingsRow(title = { Text("Sound") }, value = { Text("Chime") }, onClick = onSound)
    }
    Section(header = { Text("Email") }) {
        if (loading) {
            SettingsRowSkeleton(showSubtitle = true)
        } else {
            SettingsRow(
                title = { Text(email) },
                subtitle = { Text("Unverified") },
                trailing = { SettingsRowLink("Verify", onClick = onVerify) },
                onClick = onEditEmail,
            )
        }
    }
    Section(header = { Text("Appearance") }) {
        SettingsBlock { ThemePreview() }
    }
}
```

A list owns the group's surface and the hairlines between its rows; never draw a divider between two
rows by hand. Each hairline starts where the text of the `ListItem` under it starts — past its
leading icon — and runs to the far edge, as in a settings section; a row that is not a `ListItem`
gets `ListSize.separatorInset`. A row is one accessibility node. `Settings` hands its rows the styles and sizes in
`LocalSettingsStyles` and `LocalSettingsSizes` — provide `SettingsDefaults.styles(…)` and
`SettingsDefaults.sizes(…)` there once for the whole app — or, unprovided, the defaults; it does not
scroll, so put it in a scrolling container.

**Re-inking a row.** A row's inks come from its `ListItemStyle`, each with a state per row state.
`style.withValueColor(ink)` re-inks the value in every state (a bare `copy(value = …)` leaves the
hovered and selected value in the old ink, taken when the style was built), and
`style.withChevronColor(rest, active = …)` gives `ListItemChevron` an ink of its own, quieter than
the leading slot that shares the trailing slot's `extra`. `ListItemStyle.flood(fill = hue)` is a
row that fills with a solid colour when selected — the chosen subcategory in its category's colour
— with its text, slots and chevron reversed out in `content` and no rail. A selected row keeps its
selected inks while hovered or pressed.

```kotlin
val categoryRow = ListItemStyle.plain()
    .withChevronColor(ArecaTheme.colors.fgDisabled, active = ArecaTheme.colors.fgMuted)
val chosenChild = ListItemStyle.flood(fill = category.color)
```

A row's slots are roles with one size and one ink each, so pass plain `Text`: the `title`, the
`subtitle` under it, a `caption` under that for detail such as when a device was last seen, and the
`value` at the end. Leading visuals in a section share one size and shape — glyphs on
`SettingsRowIcon` tiles, anything else in `SettingsRowTile`. In a menu where every row is a
different thing — appearance, backup, export — give each tile a hue from `ArecaTheme.hues`:
`SettingsRowIcon(ArecaIcons.Palette, hue = ArecaTheme.hues.purple)`, or `soft = true` for the quiet
form. Keep one hue per concept. A section's closing action goes on its
`band`, a strip at the bottom of the card in `bandColor`, by default the card's `well`. A section's
card, hairlines and band follow the surface the list is drawn on, so provide its look once through
`LocalSettingsStyles` rather than restyling sections per screen.

A `SettingsTextFieldRow` given a `title` is a labelled form row — the label at the start, the input
against the end — and a `suffix` names its unit. Make the suffix a `SettingsTextFieldSuffix`, with an
`onClick` when the unit can be changed:

```kotlin
SettingsTextFieldRow(
    value = width,
    onValueChange = { width = it },
    title = "Width",
    placeholder = "0",
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
    suffix = {
        SettingsTextFieldSuffix(
            text = unit.symbol,
            modifier = Modifier.anchored(units.anchorState),
            onClick = { units.show() },
            onClickLabel = "Change unit",
        )
    },
)
```

An `ExpandableListItem` with `container = ExpandableListItemContainer.Soft` sits on a block in the
surface's `raised` fill, in `shapes.surface`, while expanded, behind the row and what it reveals. `showChevron = false` drops the chevron when the
row already says it opens. Its `bottom` slot holds content that belongs to the row itself — a meter
under a category's name.

A settings section is for a short, bounded group — options, a form typed in place, a few actions.
A collection that can grow without limit belongs in a `LazyList` or a `PagedContentList` of
`ListItem` rows. See the [settings recipe](../../docs/recipes/settings.md).

### Collections

| Component | Use it for |
| --- | --- |
| `ContentList` / `GroupedContentList` | A set loaded in one go, inside a scrolling parent, with loading, empty and error faces |
| `PagedContentList` / `GroupedPagedContentList` | A set loaded a page at a time, composed lazily, with load-more, end-of-list and pull-to-refresh |
| `LazyVerticalGroupedGrid` / `LazyHorizontalGroupedGrid` | A grid broken into labelled groups |

```kotlin
PagedContentList(
    state,
    onLoadNextPage = onLoadMore,
    onRefresh = onRefresh,
    itemKey = { it.id },
) { message ->
    ListItem(title = { Text(message.subject) }, onClick = { onOpen(message) })
}
```

The state is plain data — `LimitedContentListState` or `PagedContentListState` — that your view
model produces; the list picks the face to show from it. `ContentListLabels` holds the default
faces' copy. A paged list's `contentPadding` — the `padding` a `BaseLayout` hands its content —
insets the items, which scroll under the bars, and the loading, empty and error faces. See the
[list recipe](../../docs/recipes/list.md).

`groupLayout = ContentListGroupLayout.Inset` on a grouped list puts each group on a card of its own —
the surface's `raised` fill in `shapes.surface`, the header inside it, hairlines between items, the header and the
hairlines in line with a row's text. The card is the same `ListStyle` / `ListSize` surface a
`ListItemColumn` takes, given as `ContentListStyle.group` and `ContentListSize.group`; the header's
colour, type and padding and the cards' spacing are the other `group…` properties. A paged list
draws the cards as lazy segments and its headers stop sticking.

```kotlin
GroupedPagedContentList(
    state = byWarrantyEnd,
    groupLayout = ContentListGroupLayout.Inset,
    contentPadding = padding,
    itemKey = { it.id },
    groupHeaderContent = { period -> Text(period.title) },
) { item ->
    ListItem(title = { Text(item.name) }, subtitle = { Text(item.place) }, onClick = { onOpen(item) })
}
```

### Tables

| Component | Use it for |
| --- | --- |
| `Table` | Rows and columns of data, every row composed in one pass |
| `LazyTable` | The same for a long set, composing rows on demand |

```kotlin
Table(
    invoices,
    columns = listOf(
        TableColumn(id = "number", title = "Invoice", value = { it.number }),
        TableColumn(id = "amount", title = "Amount", width = 120.dp, value = { it.amount }),
    ),
)
```

A `TableColumn` is fixed when given a `width`, otherwise as wide as its widest content, sharing the
leftover space by `expandWeight`. Both tables scroll sideways when the columns are wider than the
table.

### Navigation, tabs, tab bar and top bar

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="../../docs/images/foundation-navigation-dark.png">
  <img alt="A top bar, a strip of tabs and a tab bar" src="../../docs/images/foundation-navigation-light.png" width="400">
</picture>

| Component | Use it for |
| --- | --- |
| `TopBar` | A screen's heading bar: a title between controls, with an optional band underneath |
| `LargeTopBar` | A large title that collapses into a `TopBar` as the content scrolls |
| `TopBarProgress` | A hairline of progress along the bar's bottom edge, in its `bottom` band |
| `Tabs` | A strip of tabs switching the content below it: underlined, as buttons, or segmented in a track |
| `TabBar` / `FloatingTabBar` | An application's primary destinations, docked to the bottom edge or floating above it |
| `FloatingTabBarItemContent` | A floating tab bar item's icon and optional label |
| `NavList` | A sidebar of destinations, with expandable sections |
| `NavItem` | One destination row, for a sidebar you compose yourself |
| `SwipePager` / `PagerControls` / `PageIndicator` | Pages swiped sideways with their dots and a next button — an onboarding tour |
| `PageCounter` | Where the page on screen sits among the rest as a count ("3 / 9") — a picture's corner, a viewer's bar |
| `ScrollToTopOnTabReselect` | Back to the top of a column, list or grid when its destination's tab is tapped again |
| `OnTabReselect` | Anything else on that tap — a refresh |

The controls in a bar's `leading` and `trailing` slots are under
[Buttons and actions](#buttons-and-actions).

```kotlin
TopBar(
    "Settings",
    scrollState = topBarScroll,
    leading = { BackAction(onClick = onBack) },
    trailing = { SearchAction(onClick = onSearch) },
)

TopBar(
    "Importing",
    size = TopBarSize.Default.copy(bottomPadding = PaddingValues()),
    leading = { CloseAction(onClick = onClose) },
    bottom = { TopBarProgress(progress, contentDescription = "Importing contacts") },
)
```

A bar handed a `rememberTopBarScrollState()` follows the content through the state's
`nestedScrollConnection`, attached to the scrolling content with `Modifier.nestedScroll`. It then
shows its rule once content is under it, and with `TopBarScrollBehavior.EnterAlways` or
`ExitUntilCollapsed` it moves out of the way. `TopBarStyle` sets the bar's look and
`TopBarActionStyle` (`Plain`, `Tonal`, `Floating`, `Scrim`) its controls'. `TopBarProgress` with
`progress = null` runs indeterminately.

```kotlin
Tabs(
    listOf(
        TabItem(value = "inbox") { Text("Inbox") },
        TabItem(value = "archive") { Text("Archive") },
    ),
    selectedValue = tab,
    onValueSelected = { tab = it },
)
```

`TabsVariant.Underline`, the default, switches views within a screen. `Button` and `Segmented`
switch a setting: `Segmented` sets the tabs in a track (`TabsStyle.track`) and slides a raised thumb
(`TabsStyle.thumb`) to the selected one, at `TabsSize.Segmented` unless given a `size`; `Button`
slides a flat fill (`TabsStyle.bgSelected`). Tabs are built on `SlidingSelector`: the indicator
moves on the theme's `motion.slide()` spring unless `TabsSize.indicatorDuration` is changed from the
theme's `motion.medium`, which makes it a tween of that length. For a setting that is not a view —
or one that can be left unset — reach for `SlidingSelector` itself.
`fillWidth = true` shares the width equally between the tabs; the strip then does not scroll, so
every tab must fit:

```kotlin
Tabs(
    listOf(
        TabItem(value = Period.Month) { Text("Month") },
        TabItem(value = Period.Year) { Text("Year") },
    ),
    selectedValue = period,
    onValueSelected = { period = it },
    variant = TabsVariant.Segmented,
    fillWidth = true,
)
```

```kotlin
FloatingTabBar(
    listOf(
        TabBarItem(value = Destination.Home) {
            FloatingTabBarItemContent(label = "Home") {
                Icon(ArecaIcons.Home, contentDescription = null)
            }
        },
        TabBarItem(value = Destination.Search) {
            FloatingTabBarItemContent(label = "Search") {
                Icon(ArecaIcons.Search, contentDescription = null)
            }
        },
    ),
    selectedValue = destination,
    onValueSelected = { destination = it },
)
```

`PageIndicator` draws a pager's position as marks; `variant` picks them. `Elongated` (the default)
stretches the current dot, `Dots` only colours it, `Shrinking()` slides a window of five dots over any
number of pages, `Line` moves a thumb along a track, and `Segments` draws story bars across the width
it is given, the current one filled to `progress`. Over a picture, pass `PageIndicatorStyle.onImage()`
— white marks on a capsule — and `PageCounterStyle.onImage()` for the count. `onPageClick` lets a
pointer jump to a page; the marks stay one element to a screen reader, named by `contentDescription`.

```kotlin
Box {
    HorizontalPager(state = pagerState) { page -> ProductPicture(pictures[page]) }
    PageIndicator(
        pagerState = pagerState,
        variant = PageIndicatorVariant.Shrinking(),
        style = PageIndicatorStyle.onImage(),
        contentDescription = "Picture ${pagerState.currentPage + 1} of ${pictures.size}",
        modifier = Modifier.align(Alignment.BottomCenter).padding(ArecaTheme.spacing.lg),
    )
    PageCounter(
        pagerState = pagerState,
        style = PageCounterStyle.onImage(),
        modifier = Modifier.align(Alignment.TopEnd).padding(ArecaTheme.spacing.md),
    )
}
```

Selection is the caller's in `Tabs`, `TabBar`, `FloatingTabBar` and `NavList`. The first three also
have overloads that hold it in a remembered state — `rememberTabsState`, `rememberTabBarState`. See
the [tabs recipe](../../docs/recipes/tabs.md).

A tap on the destination already selected is the platforms' way back to the top of it. In
`dev.voir.areca.foundation.navigation`, `LocalTabReselect` is a `Flow<Unit>` of those taps for the
selected destination's screen; `AdaptiveNavigation` provides it. A screen reacts with
`ScrollToTopOnTabReselect()` on its scroll state — `ScrollState`, `LazyListState` or
`LazyGridState` — or with `OnTabReselect { … }` for anything else:

```kotlin
val listState = rememberLazyListState()
listState.ScrollToTopOnTabReselect()
OnTabReselect { onRefresh() }

LazyColumn(state = listState, contentPadding = padding) { … }
```

Switching destinations never emits, so a screen left scrolled is found scrolled on the way back.
Outside a navigation shell the flow never emits. An app that draws its own `TabBar` or
`FloatingTabBar` provides a flow of its own, emitting when `onValueSelected` reports the value
already selected:

```kotlin
val reselects = remember { MutableSharedFlow<Unit>(extraBufferCapacity = 1) }

CompositionLocalProvider(LocalTabReselect provides reselects) {
    BaseLayout(
        bottomBar = {
            TabBar(items, selectedValue = tab, onValueSelected = { value ->
                if (value == tab) reselects.tryEmit(Unit)
                tab = value
            })
        },
    ) { padding -> CurrentScreen(tab, padding) }
}
```

### Wheel picker

| Component | Use it for |
| --- | --- |
| `WheelPicker` | One or more wheels sharing a centre row — hours and minutes, a quantity and a unit |
| `WheelPickerColumn` | One wheel of items, scrolled to a selection |
| `WheelPickerItem` | A wheel row's default text, which shrinks and fades away from the centre; call it from your own `itemContent` |
| `WheelPickerHighlight` / `WheelPickerEdgeFade` | The picker's default decorations — the centre band behind the wheels and the fade over them; pass them, or `{}`, as `backgroundDecoration` and `foregroundDecoration` |

```kotlin
val hour = rememberWheelPickerColumnState(initialIndex = 9)
val minute = rememberWheelPickerColumnState(infinite = true)

WheelPicker(horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
    WheelPickerColumn(
        hours,
        state = hour,
        modifier = Modifier.weight(1f),
        contentDescription = "Hour",
    )
    WheelPickerColumn(
        minutes,
        state = minute,
        modifier = Modifier.weight(1f),
        contentDescription = "Minute",
        onSettledIndexChange = { selectedMinute = minutes[it] },
    ) { item -> WheelPickerItem(item.toString().padStart(2, '0')) }
}
```

Each column needs a width of its own — a `weight` or a fixed `width`. `valueDescription` turns an
item into what a screen reader says for it; the default is `toString()`. Dates and times have their
own pickers in [datetime](../datetime/README.md).

### Adaptive layouts

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="../../docs/images/foundation-adaptive-dark.png">
  <img alt="A tablet screen whose navigation is the edge column of AdaptiveNavigation: status, primary action, destinations and search along the end edge" src="../../docs/images/foundation-adaptive-light.png" width="720">
</picture>

Layouts that take the form the window calls for, in `dev.voir.areca.foundation.adaptive`. Each
falls back to the phone pattern Areca screens already use, and decides by the space it is given — a
layout inside a narrow pane takes its compact form. See [Window classes](#window-classes).

| Component | Compact | Wider |
| --- | --- | --- |
| `AdaptiveNavigation` | `TabBar` along the bottom; a `NavRail` without labels on a phone on its side | An `EdgeColumn` from a medium window up (the default `AdaptiveNavigationStyle.EdgeColumn`); under `AdaptiveNavigationStyle.Rail`, a `NavRail`, then a sidebar of `NavItem` rows from 1200dp the person can hide to the rail |
| `AdaptiveToolbar` | A `TopBar`: back, the title, the actions | The title over the screen and back at the head of the `EdgeColumn`; the actions as labelled buttons beside the title under `ActionReach.Header`, as round buttons at the foot of the column under `ActionReach.Thumb` |
| `EdgeColumn` | — | A floating column along one edge: status and back at its top, the destinations and search at its foot, a held-from-below window's actions between them |
| `EdgeButton`, `VerticalTabBar`, `EdgeStatus` | — | The column's round actions, its vertical capsule of destinations, and the app's own status icon |
| `FloatingSidebar` | — | A sidebar floating in beside the column over a scrim, every destination labelled |
| `NavRail` | — | A column of icon-and-label destinations along the start edge, with a `header` slot for the primary action |
| `ListDetail` | The list, then the detail in its place while `showDetail` | List (280–360dp) and detail side by side from a 600dp container, `detailPlaceholder` while nothing is open. A pointer drags the divider. On a foldable the panes meet at a vertical fold |
| `SupportingPane` | Supporting content in an `AdaptiveSheet` while `supportingVisible` | A permanent 340–360dp pane at the end from a 920dp container |
| `ContentColumn` | Full width less the gutters | Centred, no wider than `AdaptiveDefaults.ReadableWidth` (640dp) or a `maxWidth` you pass |
| `AdaptiveGrid` | Two columns of 160dp cells | As many columns as fit, cells capped at `AdaptiveDefaults.ContentWidth` (1200dp) and centred |
| `AdaptiveSheet` | A `FlowModal` from the bottom edge, capped at 640dp | A centred `Dialog` from an expanded window up |
| `AdaptiveMenu` | A sheet of `AdaptiveMenuItem` rows | A `DropdownPopup` at the trigger, from an expanded window up or under a pointer |
| `Modifier.contextMenu` | Opens an `AdaptiveMenu` on a long press | Opens it on a right-click, at the pointer |

```kotlin
AdaptiveNavigation(
    destinations = listOf(
        NavDestination(Tab.Home, "Home", ArecaIcons.Home),
        NavDestination(Tab.Activity, "Activity", ArecaIcons.Receipt),
    ),
    selectedValue = tab,
    onValueSelected = { tab = it },
    sidebarVisible = sidebarOpen,
    onSidebarVisibleChange = { sidebarOpen = it },
    onSearch = openSearch,
    status = { EdgeStatus(ArecaIcons.Check, contentDescription = "Synced") },
) {
    ListDetail(
        showDetail = selected != null,
        modifier = Modifier.padding(contentPadding),
        list = {
            AdaptiveToolbar(title = "Activity")
            TransactionList(onOpen = { selected = it })
        },
        detail = {
            AdaptiveToolbar(
                title = selected!!.merchant,
                onBack = if (isTwoPane) null else { { selected = null } },
                actions = listOf(AdaptiveToolbarAction(ArecaIcons.Share, "Share", onClick = share)),
            )
            TransactionDetail(selected!!)
        },
    )
}
```

From a medium window up the chrome stacks in an `EdgeColumn` along `edgeSide` (the end edge by
default), floating over the content: `status` and back at its top, a `VerticalTabBar` and the
search button at its foot, the middle left empty. Screens declare their title, back and actions with
`AdaptiveToolbar` in place of a `TopBar`: beside the column it draws the title and hands back to the
column, which shows the toolbar of the screen drawn most recently, and everywhere else it is a
`TopBar`. Back takes the sidebar toggle's place. The actions follow the window's `ActionReach`: a
tablet on its side or a pointer draws them beside the title as labelled buttons, with room for the
words; a tablet held upright or an unfolded foldable, held from below, puts them at the foot of the
column between the tabs and search, with `header` over them. Past three places, More counted, they
fold into More. In two panes the detail's toolbar takes the column and the list's stays a bar over
its pane. `contentPadding` carries the column's clearance on its side (80dp under touch):
keep text and controls inside it and let backgrounds run under the column. `edgeSide = EdgeColumnSide.Auto`, the
default, puts the column on the camera cutout's side, and on the end side when the cutout is along
the top or there is none. On a foldable propped up along a horizontal fold, status and the toolbar
stay above the fold and the tabs and search below it. The toggle opens a
`FloatingSidebar` beside the column — every destination labelled, then `sidebarSections` and
`footer` — and the tabs give way to it while it is open. `AdaptiveNavigationStyle.Rail` keeps a
`NavRail` and a sidebar of rows instead; there `sidebarVisible` chooses the sidebar or the rail on a
large window. `EdgeColumn`, `EdgeButton`, `VerticalTabBar`, `EdgeStatus` and `FloatingSidebar` are
public for a shell of your own, and take `EdgeColumnStyle` and `EdgeColumnSize`.

An app whose phone bar is its own — a floating bar, a bar with an action in its middle — passes it as
`bottomBar`. It replaces the `TabBar` on a compact window and is drawn over the bottom of the screen,
which keeps its full height and reads what the bar covers from `contentPadding`, to end its scroll
with. `footer` sits at the foot of the rail and the sidebars, above the rail's toggle — an account,
a sync state in full.

```kotlin
AdaptiveNavigation(
    destinations = destinations,
    selectedValue = tab,
    onValueSelected = { tab = it },
    footer = { AccountAvatar() },
    bottomBar = { FloatingTabBar(items = destinations.map { it.toItem() }, selectedValue = tab, onValueSelected = { tab = it }) },
) {
    Screen(bottomPadding = contentPadding.calculateBottomPadding())
}
```

A tap on the destination already selected reaches its screen through `LocalTabReselect`, in every
form: `listState.ScrollToTopOnTabReselect()` on the screen takes it back to the top. See
[Navigation, tabs, tab bar and top bar](#navigation-tabs-tab-bar-and-top-bar).

The slots of `AdaptiveNavigation`, `ListDetail` and `SupportingPane` know which form they are in —
`layout`, `isTwoPane`, `isSupportingInline` — so a screen draws a back action or a "Filters" button
only when the layout needs it. `ListDetail` and `SupportingPane` keep each part's saved state, such as
a scroll position, when a resize switches the form. `AdaptiveSheet` and `AdaptiveMenu` choose by the
whole window, not by the pane that opened them. `NavRail` takes `NavRailStyle` and `NavRailSize`,
seeded by the provider like every other component.

Under a pointer the layouts take what a desktop expects. `ListDetail`'s divider is a handle: a drag
moves the list's edge between `AdaptiveDefaults.ListPaneMinWidth` (240dp) and what leaves the detail
`AdaptiveDefaults.DetailPaneMinWidth` (320dp), and a double-click puts it back; pass
`resizable = false` to pin it. `Modifier.contextMenu(state)` on a row opens its `AdaptiveMenu` on a
right-click under a pointer and a long press under touch, from the Menu key or Shift+F10, and from
assistive technology's long-click action; the gesture that opens the menu does not also click the
row.

```kotlin
val menu = rememberDropdownPopup()
ListItem(
    title = { Text(transaction.merchant) },
    onClick = { open(transaction) },
    modifier = Modifier.contextMenu(menu),
)
AdaptiveMenu(state = menu, title = transaction.merchant, actions = actions)
```

## Accessibility

Every component sets its own role and state — buttons, headings, checked and selected states, tab
groups, progress — and every row, bar and field mirrors in a right-to-left layout. You supply what a
component cannot know: a `contentDescription` on an icon that carries meaning, an icon button, a
`NotificationBadge` ("3 unread messages", not "3"), an `IndicatorDot` that is the only statement of
a status, and a progress indicator. `AnnounceOnChange(text)` speaks a message that appears, such as
a validation error; `FieldGroup` already does this for its error.

Every string a component shows or speaks on its own is a parameter with an English default, and
groups of them are labels objects (`SelectFieldLabels`, `ContentListLabels`,
`RequirementListLabels`, `NumberStepperLabels`). See [Accessibility](../../docs/accessibility.md)
and [Localisation](../../docs/localisation.md).

## Experimental API

`@ExperimentalArecaApi` marks public API that may change or go in any release without a deprecation
cycle. It marks `PullToRefreshLayout` and what belongs to it: `PullToRefreshIndicator`,
`PullToRefreshState`, `rememberPullToRefreshState`, `PullToRefreshStyle`, `PullToRefreshSize` and
their locals. Opt in at the call site:

```kotlin
@OptIn(ExperimentalArecaApi::class)
@Composable
fun Feed(posts: List<Post>, refreshing: Boolean, onRefresh: () -> Unit) {
    PullToRefreshLayout(isRefreshing = refreshing, onRefresh = onRefresh) {
        LazyColumn(Modifier.fillMaxSize()) {
            items(posts) { post -> PostRow(post) }
        }
    }
}
```

Or once for a module, with the compiler argument `-opt-in=dev.voir.areca.ExperimentalArecaApi`.
`PagedContentList(onRefresh = …)` gives pull-to-refresh without opting in.

## Where to look

- **[Component catalogue](../../docs/components.md)** — every component, by family, with its package
  and playground page.
- **The playground** — a page per component with its variants, states and sizes, and controls for
  its parameters. Run `./gradlew :playground:run` from the repository root; the foundation pages are
  in `playground/src/commonMain/kotlin/dev/voir/areca/playground/foundation/`.
- **KDoc** — every public declaration documents its contract and each parameter; most components
  carry a usage example.
- **[Recipes](../../docs/recipes/README.md)** — whole screens built from these components.
- **The `areca-ui-v1` skill** — the same API for coding agents:
  [skills/areca-ui-v1/SKILL.md](../../skills/areca-ui-v1/SKILL.md).
