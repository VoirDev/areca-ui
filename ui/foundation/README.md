# Foundation

The Areca design system for Compose Multiplatform: the theme tokens, and every component built on
them — buttons, text, fields, lists, overlays, navigation — for Android, iOS and the JVM.

```kotlin
implementation("dev.voir.areca:foundation:1.0.0")
```

Areca is published to GitHub Packages, so the build needs its repository first; see
[Installing](../../README.md#installing).

Every other `ui` and `flow` module depends on this one, and everything they draw reads the tokens it
provides, so an application has exactly one theme to set up. The module carries `compose-runtime`,
`compose-foundation` and `compose-ui` as API dependencies and nothing else. It does not depend on
the icon set: the snippets below use `ArecaIcons.*` from `dev.voir.areca:icons`, which an
application adds itself — see [Icons are opt-in](../../README.md#icons-are-opt-in).

Where each declaration lives is the root README's [Namespaces](../../README.md#namespaces). For a
terse guide to picking components, written for coding agents, see [AGENTS.md](AGENTS.md).

---

## Setup

### The theme provider

Wrap the application once, near its root, in `ArecaThemeProvider`. It puts the tokens in scope and
seeds the default style and size of every component:

```kotlin
@Composable
fun App() {
    ArecaThemeProvider(darkTheme = isSystemInDarkTheme()) {
        Box(Modifier.fillMaxSize().background(ArecaTheme.colors.bgCanvas)) {
            Home()
        }
    }
}
```

`darkTheme` picks the direction; nothing follows the system on its own, so pass
`isSystemInDarkTheme()` or the user's own setting. The provider paints nothing, so give the screen
its ground — `bgCanvas` — yourself.

Without a provider, every token and component falls back to the light theme's defaults, so a
preview or a test composes without wrapping. An application always wraps: only the provider can make
the theme dark or change a token.

### Changing the theme

Each parameter of `ArecaThemeProvider` defaults to a value derived from the one above it, so
override at the highest level that says what you mean and the rest follows:

| Parameter | What it is | Derived from |
| --- | --- | --- |
| `baseColors` | The raw palette | — |
| `colors` | Semantic colors: `bgCanvas`, `fgBase`, `borderBase`, `accentBase`, … | `baseColors`, for `darkTheme` |
| `radius` / `shapes` | Corner lengths, and the shapes built from them | `shapes` from `radius` |
| `spacing` | The spacing scale and the screen gutters | — |
| `elevation` | The shadow scale | — |
| `motion` | Durations and easings | — |
| `fonts` / `baseTypography` / `typography` | Font families, the raw type scale, the named styles | each from the one before |

```kotlin
ArecaThemeProvider(
    darkTheme = isSystemInDarkTheme(),
    radius = ArecaRadius(md = 12.dp, lg = 20.dp),
    fonts = ArecaFonts(primary = brandSans),
) {
    App()
}
```

Supplying `baseColors` re-themes both directions; supplying `colors` replaces the mapping for the
current direction only.

### Reading tokens

`ArecaTheme` is the only way to read a token. Each accessor resolves the value the nearest provider
put in scope:

| Accessor | Holds |
| --- | --- |
| `ArecaTheme.colors` | Semantic colors — the ones to use |
| `ArecaTheme.typography` | Named text styles: `headingSmall`, `bodyMedium`, `labelLarge`, … |
| `ArecaTheme.spacing` | `xs2` (2dp) to `xl9` (96dp), and `horizontalContentInsets` / `verticalContentInsets` |
| `ArecaTheme.radius` / `ArecaTheme.shapes` | Corner lengths, and shapes from `none` to `full`, plus `tile` and `button` |
| `ArecaTheme.elevation` | Shadow depths from `none` to `xl` |
| `ArecaTheme.motion` | Durations from `instant` to `deliberate`, and the `standard`, `enter`, `exit`, `pressIn` and `fade` easings |
| `ArecaTheme.isDark` | The direction, for a decision no token can carry |
| `ArecaTheme.baseColors` / `baseTypography` / `fonts` | The raw material, for when no semantic token says what you mean |

```kotlin
Column(
    modifier = Modifier
        .background(ArecaTheme.colors.bgSurface, ArecaTheme.shapes.lg)
        .padding(ArecaTheme.spacing.xl),
    verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
) {
    Text("Storage", style = ArecaTheme.typography.headingSmall)
    Text("12 GB of 50 GB used", variant = TextVariant.BodySmall, color = TextColor.Subtle)
}
```

`Modifier.horizontalContentInsets()`, `verticalContentInsets()` and `contentInsets()` pad by the
screen gutters.

### Regions that are always dark

A photo editor, a media viewer or a camera is dark whatever the theme around it. Wrap it in
`ArecaDarkTheme`, which keeps everything the application set — palette, shapes, spacing, motion,
type — and changes only the direction:

```kotlin
@Composable
fun PhotoViewer(photo: Painter) {
    ArecaDarkTheme {
        SystemBars(color = ArecaTheme.colors.bgCanvas)
        Box(Modifier.fillMaxSize().background(ArecaTheme.colors.bgCanvas)) {
            Image(photo, contentDescription = null, modifier = Modifier.fillMaxSize())
        }
    }
}
```

A nested `ArecaThemeProvider(darkTheme = true)` would start from the defaults instead, losing the
application's palette. To name a dark color before the dark scope begins — a style parameter's
default, which is resolved at the call site — read `ArecaTheme.darkColors`, the dark counterpart of
the theme in scope.

`SystemBars` paints the window's bars for as long as it is composed and restores them when it
leaves. It acts on Android and does nothing on iOS and the desktop. In a dialog, pair it with
`edgeToEdgeDialogProperties()` so the window draws behind the bars.

---

## How every component works

The families differ in what they draw, not in how they are configured. Learn this section once.

### Style and size

A component's look is two values:

- **`XStyle`** — colors, and the shape and indication that go with them.
- **`XSize`** — paddings, gaps, type, minimum bounds and touch target.

Each is a data class whose companion holds the ready-made values:

| On the companion | What it is |
| --- | --- |
| `Default` | The theme's value. What the component uses when given nothing |
| Named presets | A ladder of sizes (`Small`, `Medium`, `Large`, …) or a set of looks (`BadgeStyle.Success`, `CardStyle.Outlined`) |
| `default(...)` | `Default` with any field replaced, every parameter defaulting to the theme |
| Style factories | Where the look is a choice: `ButtonStyle.solid(ActionColor.Danger)`, `FieldStyle.filled()` |

```kotlin
Button(
    "Delete",
    onClick = onDelete,
    style = ButtonStyle.soft(ActionColor.Danger),
    size = ButtonSize.Small,
)

ProgressBar(
    0.4f,
    style = ProgressBarStyle.default(fill = ArecaTheme.colors.fgSuccess),
    size = ProgressBarSize.Small,
)
```

Most presets and every `default(...)` are `@Composable`: they read the theme where they are called,
so a style built inside an `ArecaDarkTheme` is a dark one.

### Defaults in scope

Every component reads its style and size from a composition local when it is not given one.
`Button`'s `style` defaults to `LocalButtonStyle.current ?: ButtonStyle.solid()`, and its `size` to
`LocalButtonSize.current ?: ButtonSize.Medium`; every other component follows the same pattern with
its own `LocalXStyle` and `LocalXSize`.

The locals are nullable. `ArecaThemeProvider` seeds them, and the fallback after `?:` is what a
component uses with no provider at all. To change a default for a subtree — a toolbar of small
outlined buttons, a dense form — provide your own:

```kotlin
CompositionLocalProvider(
    LocalButtonSize provides ButtonSize.Small,
    LocalButtonStyle provides ButtonStyle.outlined(ActionColor.Neutral),
) {
    Row(horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
        Button("Archive", onClick = onArchive)
        Button("Move", onClick = onMove)
    }
}
```

Provided just inside the application's `ArecaThemeProvider`, the same thing changes the default for
the whole application. A nested `ArecaThemeProvider` or `ArecaDarkTheme` seeds every local again, so
an override made above one does not reach inside it.

Three locals carry content rather than a component's look, and every family reads them:
`LocalContentColor` and `LocalTextStyle` (in `dev.voir.areca.foundation`) and `LocalIconSize`. A
container sets them for its slots — a button provides its label color, a field its type — which is
why a plain `Text` or `Icon` inside a slot comes out right without arguments.

A few families resolve their defaults differently, and their sections say how: fields read
`FieldDefaults.style` and `FieldDefaults.size`, a `Notice` builds its style from its tone unless
`LocalNoticeStyle` is provided, and a `Settings` list hands its rows the styles from
`SettingsDefaults`.

### `ActionColor`

What an action *means* is an `ActionColor`: `Brand`, `Danger`, `Success`, `Warning`, `Neutral` or
`NeutralDark`. How *loud* it is, is the style factory. `Button`, `IconButton` and `FeaturedIcon`
resolve the same family the same way, along the same ladder:

| Factory | Use it for |
| --- | --- |
| `solid` | The screen's one primary action. Two solid buttons on a screen means no primary action |
| `soft` | A secondary action that still reads as its family |
| `outlined` | A secondary action on a busy surface |
| `plain` | An action that must not pull the eye — Cancel, a toolbar control (`Button` and `IconButton` only) |

`Danger` is destructive whether it is a filled button or a plain icon. The theme's default button is
`solid(ActionColor.Brand)`.

### Parameter order

Signatures share one order: the required values first — the value and its change callback, or the
action and its label — then `modifier` as the first optional parameter, then the rest, with the
main content slot last so it can be a trailing lambda. A few place `modifier` later. Pass everything
after the leading value by name, and the order never matters at a call site.

Callbacks are nullable where *no* callback means something. `Button(onClick = null)` draws a button
with no action, for one inside a row that is itself clickable; a `Checkbox` given a `null`
`onCheckedChange` draws a display-only box whose state the enclosing row reports.

### Interaction and focus

Every interactive component takes `interactionSource: MutableInteractionSource? = null`. `null`
creates one internally; pass your own to observe press, hover and focus, or to drive one
component's state from another:

```kotlin
val interactions = remember { MutableInteractionSource() }
val pressed by interactions.collectIsPressedAsState()

Button(
    if (pressed) "Release to send" else "Hold to send",
    onClick = onSend,
    interactionSource = interactions,
)
```

A style carries its indication, which draws hover, press and keyboard focus. The four live in
`dev.voir.areca.indication`:

| Indication | Feedback | Used by |
| --- | --- | --- |
| `TouchIndication` | A hover tint and a press that spreads from the finger | Solid and soft buttons, swatches — the default for anything tapped |
| `FadeIndication` | The background crossfades | Plain buttons, ghost cards |
| `StrokeIndication` | The outline draws itself and thickens on press | Outlined buttons and cards |
| `LiquidSweepIndication` | A fill sweeps across in reading order | Nothing by default; for a deliberate, commit-style action of your own |

`ButtonStyle` and `IconButtonStyle` keep their shape and their indication in step: the button draws
an Areca indication in the style's `shape` whatever shape it was built with, so
`style.copy(shape = ArecaTheme.shapes.full)` gives a pill that also reacts as a pill.

Every indication draws keyboard focus the same way: a `FocusOutline` ring outside the element's
bounds, following its shape. Nothing inside the element changes on focus, so hover, press and focus
can all show at once. Because the ring is outside the bounds, never `Modifier.clip` an element that
draws one — use `Modifier.background(color, shape)` for a shaped fill.

### Touch targets

`MinimumTouchTarget` is 48dp. A control that draws smaller — a 32dp icon button, a 20dp checkbox —
reserves the rest in layout around itself, centred, so a neighbour can never sit inside its band.
The size classes of `Button`, `IconButton`, `Badge`, `Checkbox`, `Radio`, `Toggle`, `Swatch` and
`SwatchPicker` carry this as `touchTarget`. It is reserved only for a control that has an action:
one given a `null` callback is display-only and keeps its drawn size.

Give your own small controls the same guarantee with `Modifier.minimumTouchTarget()`, applied
outside the element's size and before its `clickable`:

```kotlin
Box(
    modifier = Modifier
        .minimumTouchTarget()
        .size(32.dp)
        .background(ArecaTheme.colors.bgMuted, ArecaTheme.shapes.full)
        .clickable(
            interactionSource = interactions,
            indication = TouchIndication(shape = ArecaTheme.shapes.full),
            role = Role.Button,
            onClick = onPlay,
        ),
)
```

---

## Components

One section per package under `dev.voir.areca.foundation`. Each component's KDoc is the reference
for every parameter; this is the map.

### `button`

| Component | Use it for |
| --- | --- |
| `Button` | An action with a label, and optionally an icon either side |
| `IconButton` | An action shown by an icon alone. Always give it a `contentDescription` |

```kotlin
Button("Continue", onClick = onContinue, modifier = Modifier.fillMaxWidth(), loading = saving)

IconButton(
    ArecaIcons.Trash,
    contentDescription = "Delete draft",
    onClick = onDelete,
    style = IconButtonStyle.plain(ActionColor.Danger),
)
```

`enabled = false` says the action is unavailable. `loading = true` says it is under way: the press
is blocked, a spinner replaces the trailing slot, and assistive technology hears
`loadingStateDescription`. Sizes run `ExtraSmall` (32dp) to `ExtraLarge` (64dp) for buttons, and
`Tiny` (20dp) to `ExtraLarge` for icon buttons; `Medium`, 48dp, is the default for both.

### `text`

| Component | Use it for |
| --- | --- |
| `Text` with a `TextVariant` | All text. The variant names a role — `HeadingSmall`, `BodyMedium`, `Caption`, `LabelLarge` — and `TextColor` a foreground: `Base`, `Subtle`, `Muted`, `Danger`, … |
| `Text` with a `TextStyle` | Text that inherits `LocalTextStyle` and `LocalContentColor` — inside a component's slot |
| `Heading` | A section's title with an optional supporting line and trailing content, announced as a heading |
| `TextLink` | A standalone pressable label — "Forgot password?" |
| `TextLinkPrompt` | A sentence with one pressable part — "No account? Sign up" |
| `SensitiveText` | A value hidden under a veil until revealed — a balance, a card number |

```kotlin
Text("Recent activity", variant = TextVariant.HeadingSmall)
Text("Updated 2 minutes ago", variant = TextVariant.Caption, color = TextColor.Muted)

Heading(
    title = { Text("Billing") },
    supportive = { Text("Invoices go to the account owner.") },
)

SensitiveText("€4,210.50", revealed = revealed, onRevealedChange = { revealed = it })
```

### `icon`

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

Pass `contentDescription = null` when the icon repeats something already said beside it, and
`tint = Color.Unspecified` for a multicolour mark. `IconSize` is plain `Dp` steps, `ExtraSmall`
(12dp) to `ExtraLarge` (32dp).

### `badge`

| Component | Use it for |
| --- | --- |
| `Badge` | A short label — a status, a tag, a filter. Pressable with `onClick` |
| `NotificationBadge` | A count, usually overlaid on something else |
| `IndicatorDot` | A status carried by color alone: presence, unread |
| `BadgeSelect` / `LazyBadgeSelect` / `FlowBadgeSelect` | A group of chips that select something: one line, one scrolling line, or wrapping lines |

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

A badge group does not hold its selection: `onSelect` reports the chip pressed, and the caller
updates `selected`. With `BadgeSelectionMode.Single` the chips are radio buttons, with `Multiple`
checkboxes.

### `avatar`

`Avatar` shows a person or entity: an image in its content slot, or initials from `letters`, or an
empty shape. The slot takes any image loader's output and clips it.

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

`letters` is reduced to at most two initials. Without a `contentDescription` the avatar is
decorative, which is right when the name is printed beside it.

### `selection`

| Component | Use it for |
| --- | --- |
| `Checkbox` | A value that is part of a form and saved later. `indeterminate` shows a checked parent of mixed children |
| `Radio` | One option of several. Put `Modifier.selectableGroup()` on the group |
| `Toggle` | A setting that applies the moment it is flipped. `ToggleState` with `BinaryToggleCycle` or `TriStateToggleCycle` |
| `NumberStepper` | A whole number nudged by one between bounds |
| `Swatch` / `SwatchPicker` | One color sample; a scrolling set of them with a single selection |

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

`enabled = false` greys a control out as unavailable; `readOnly = true` keeps its colors and refuses
changes, for a value that is real but not the user's to change. `Checkbox` and `Radio` share
`SelectionStyle`, which maps checked, disabled and read-only to `SelectionColors`; `Toggle` draws
from the same `SelectionColors` through its `ToggleStyle`.

### `field`

Every field is a `Field` container with an editor or a value inside. Put it in a `FieldGroup` for
its label and its message.

| Component | Use it for |
| --- | --- |
| `TextField` | Free text. `format` beautifies the display without touching your state |
| `PasswordField` | A secret, with a reveal control |
| `SearchField` | A query, with a magnifier, a clear control and the keyboard's search action |
| `AffixTextField` | A value with a fixed prefix or suffix — a currency sign, a unit |
| `OtpField` | A one-time code, one character to a cell, with `onComplete` when the last one lands |
| `select.SelectField` | A value chosen from a dropdown anchored to the field |
| `select.SelectFlowModal` | A long list chosen from in a sheet, with optional search and an apply button |
| `FieldRow` | Several parts sharing one border and one status — an amount and its currency |
| `FieldGroup` | The label above, beside or inside the control, and one assistive or error line |
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
takes focus when the label is tapped; with `labelPlacement = FieldLabelPlacement.Inside` the label
is drawn inside the box, which works only through the scope. The group decides the status from
`error`, so the message and the border cannot disagree.

**Theming fields.** Fields default to `FieldDefaults.style` and `FieldDefaults.size`, which read
`LocalFieldStyle` and `LocalFieldSize`. The looks are factories: `outlined()` (the theme's default),
`filled()`, `soft()`, `solidNeutral()`, `underlined()` and `inline()`, for a field inside a surface
that already draws the boundary. A form is one look — provide it once for the screen:

```kotlin
CompositionLocalProvider(LocalFieldStyle provides FieldStyle.filled()) {
    SignUpForm()
}
```

`SelectField` and `SelectFlowModal` live in `dev.voir.areca.foundation.field.select`, and take their
options as `SelectOption(key, value, label)`. Selection is controlled: `onChange` reports the values
that should be selected next, and nothing moves until your state comes back.

### `feedback`

| Component | Use it for |
| --- | --- |
| `Spinner` | Work of unknown length |
| `ProgressBar` | Work with a known total; `progress = null` for indeterminate |
| `StepIndicator` | Which of a handful of steps the user is on — the bar atop a setup flow |
| `Skeleton` | Placeholder bars where content is still loading |
| `RequirementList` | The rules a value must meet, each ticked off as it is met |
| `Fallback` | Part of a screen standing in for missing content: empty, failed, offline |
| `ToastHost` / `Toast` | A brief message about something that just happened |

```kotlin
ProgressBar(0.64f, label = "64%", contentDescription = "Uploading report.pdf")

Fallback(
    top = { FeaturedIcon(ArecaIcons.Inbox, contentDescription = null) },
    heading = { Text("No messages yet") },
    body = { Text("Messages from your team appear here.") },
    content = { Button("Invite a teammate", onClick = onInvite) },
)
```

Toasts queue in a `ToastState` and show one at a time in a `ToastHost`, which fills its parent and
keeps clear of the window insets:

```kotlin
val toasts = rememberToastState()

Box(Modifier.fillMaxSize()) {
    Button("Archive", onClick = { toasts.show("Conversation archived") })
    ToastHost(toasts)
}
```

`show` returns the toast's id for `dismiss(id)`. A toast's duration is lengthened by the platform's
accessibility timeout; `duration = null` keeps it until dismissed, which a toast carrying an action
needs.

### `notice` and `status`

| Component | Use it for |
| --- | --- |
| `Notice` | A block set off from the page to say one thing — a banner with a title, or a one-line note |
| `AnimatedNotice` | A notice that is present only while there is something to say |
| `StatusScene` | A whole screen given over to one piece of work: running, done, or stopped |
| `StatusMark` | The disc a status scene shows — working, then success or stopped |

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

A notice takes its colors from `tone` and `emphasis` through `NoticeStyle.of(...)`; a danger notice
is filled by default, every other tone subtle. `LocalNoticeStyle` is `null` unless provided; a
style provided there replaces the tone's for every notice in the subtree, so provide one only where
every notice should look the same. **StatusScene or Fallback:** a status scene *is* the
screen and stages its entrance; a fallback stands in for part of one and appears at once. Their
slots line up one for one.

### `overlay`

| Component | Use it for |
| --- | --- |
| `ConfirmSheet` | A yes-or-no question answered with a thumb: sign out, delete, discard |
| `ConfirmationDialog` | The same question as a centred dialog, with `suspend` handlers that show progress |
| `Dialog` | A centred window for a short task — rename, pick one thing |
| `FlowModal` | A bottom sheet the user can drag between heights and away — a form, a picker, a filter |
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

A `FlowModal` is driven by a `FlowModalController` from `rememberFlowModal`; give it a `TopBar` as
`header` and a `FlowModalActionBar` as `footer`, so every sheet commits from the same corner:

```kotlin
val filters = rememberFlowModal(autoHeight = true)

Button("Filters", onClick = { filters.show() })

FlowModal(
    filters,
    header = { TopBar("Filters", trailing = { CloseAction(onClick = { filters.hide() }) }) },
    footer = { FlowModalActionBar("Apply", onConfirm = { filters.hide() }) },
) {
    FilterOptions()
}
```

A `DropdownPopup` positions itself against the control marked with `Modifier.anchored` and flips to
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

`Dialog` and `FlowModal` open in their own platform window, above everything, and draw one scrim.
`ModalDefaults.Actions` is the stacked pair of buttons a dialog usually ends with.

### `layout`

| Component | Use it for |
| --- | --- |
| `BaseLayout` | A screen's frame: top and bottom bars drawn over full-height content that is handed their height as padding |
| `CollapsingHeaderLayout` | A header that scrolls away above a strip that stays — artwork over tabs |
| `PullToRefreshLayout` | Pull-to-refresh around scrolling content. Experimental — see below |
| `Card` | A surface that groups content, optionally pressable or expandable, with an optional footer for its status and actions |
| `Expandable` | A section that opens and closes under a trigger you supply |
| `HorizontalDivider` / `VerticalDivider` | A rule, optionally labelled — "or" between two choices |
| `RelativePositionLayout` | A companion on one of eight sides of its content — a label above a bar |
| `BoxScope.Overlay` | A tint over a box without changing its size — a loading veil |

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

The content is laid out under the bars, so it must apply the padding it is handed — normally as a
lazy list's `contentPadding` — or the bars hide its first and last items. A `Card`'s content is
handed the card's padding the same way, so a full-bleed image can ignore it.

A card's status and actions go in its `footer`: a strip along the bottom, filled and inked by a
`CardFooterStyle` — `plain()` with a hairline, `soft(ActionColor)` tinted, `solid(ActionColor)`
filled, or `of(bg, content)` in colours of your own. Copy in the footer takes the style's ink and
buttons take its borderless button style, so a brand-filled footer reads white without restyling.

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

### `slot`

`SlotRow` is the row most components are built on: a start slot, a required centre and an end slot,
divided by a `SlotRowLayout`. A button, a badge, a list row and a top bar are the same three boxes
with different width rules, which is why an icon sits the same distance from a label in all of
them. Reach for it to build a row of your own that lines up with theirs.

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

A `SlotRowLayout.alignment` lines the sides up with a middle slot of text by scenario:
`SlotRowLayout.Center` for one or two lines, `.Top` for three or more — the visual and the text
start at the top and the trailing slot stays level with the visual — `.FirstLine` for a marker no
taller than a line, and `.Auto`, which picks `Center` or `Top` by measuring the middle slot in the
lines its `Modifier.firstLineCenter(textStyle)` reports. Every list and settings row uses `Auto`.

`SlotSize.Fixed` keeps a start slot's width when it is empty, so a column of rows lines up whether
or not each has an icon. `SlotRowLayout.Centered` keeps the middle on the row's centre line, for a
title between two controls.

### `list` and `settings`

| Component | Use it for |
| --- | --- |
| `List` / `LazyList` | A grouped list declared with `item(...)` and `divider(...)`: eager for a short list, lazy for a long one |
| `ListItemColumn` | A group of row composables, with the separators drawn for you |
| `ListItem` | A row: overline, title, subtitle, caption, value, leading and trailing slots |
| `NavigationListItem` | A row that opens something, with a chevron |
| `CheckboxListItem` / `RadioListItem` / `ToggleListItem` / `CheckmarkListItem` | A row that is a selection control; the row reports the state |
| `ExpandableListItem` | A row that discloses content under it |
| `ListItemSkeleton` | A row-shaped placeholder |
| `Settings` | A settings screen: sections of rows, each section on its own card |
| `SettingsRowLink` | A second action at the end of a pressable settings row — "Verify" beside an address |

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
        SettingsToggleRow(checked = push, onCheckedChange = { push = it }, title = { Text("Push") })
        SettingsRow(title = { Text("Sound") }, value = { Text("Chime") }, onClick = onSound)
    }
}
```

A list owns the group's surface and the hairlines between its rows, so never put a divider between
two rows by hand. A row is one accessibility node: its slots merge into it. `Settings` takes its
styles and sizes from `SettingsDefaults.styles()` and `SettingsDefaults.sizes()` and hands them to
the `SettingsRow`, `SettingsToggleRow`, `SettingsSelectRow`, `SettingsOptionRow` and
`SettingsTextFieldRow` inside it; the list does not scroll, so put it in a scrolling container.

A row's slots are roles with one size and one ink each, so pass plain `Text` and let the slot style
it: the title (`bodyMedium`, base ink), the subtitle under it (`bodySmall`, muted), a caption under
that for detail such as when a device was last seen (`bodyExtraSmall`, the subtitle's ink), and the
value at the end (`bodyMedium` in a settings row). A row of one or two lines centres its visual,
its text and its trailing content; a longer one starts the visual and the text at the top and keeps
the trailing content level with the visual (`SlotRowLayout.Auto`). Leading visuals in a section
share one size and shape — glyphs on `SettingsRowIcon` tiles, anything else in `SettingsRowTile`.
A section's closing action goes on its `band`, a strip at the bottom of the card in a colour of
its own (`bandColor`), rather than behind a hairline. Settings rows are padded 16dp across and 12dp down, sit on `bgSubtle` cards on the
`bgCanvas` page, and are divided by `borderOnSubtle` hairlines.

A settings section is for a short, bounded group — options, a form typed in place, a few actions,
the handful of devices an account is signed in on. A collection that can grow without limit belongs
in a `LazyList` or a `PagedContentList` of `ListItem` rows: a section composes every row at once,
does not page and has no empty or error face.

### `collection`

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
model produces; the list decides which face to show from it. `ContentListLabels` holds the default
faces' copy. A paged list's `contentPadding` — the `padding` a `BaseLayout` hands its content —
insets the items, which scroll under the bars, and the loading, empty and error faces; the
pull-to-refresh indicator emerges below its top edge.

### `table`

`Table` composes every row in one pass; `LazyTable` composes rows on demand for a long set. Columns
are `TableColumn`s: fixed when given a `width`, otherwise as wide as their widest content and
sharing leftover space by `expandWeight`. Both scroll sideways when the columns are wider than the
table.

```kotlin
Table(
    invoices,
    columns = listOf(
        TableColumn(id = "number", title = "Invoice", value = { it.number }),
        TableColumn(id = "amount", title = "Amount", width = 120.dp, value = { it.amount }),
    ),
)
```

### `navigation`, `tabs`, `tabbar` and `topbar`

| Component | Use it for |
| --- | --- |
| `TopBar` | A screen's heading bar: a title between controls, with an optional band underneath |
| `LargeTopBar` | A large title that collapses into a `TopBar` as the content scrolls |
| `Tabs` | A strip of tabs switching the content below it, underlined or as buttons |
| `TabBar` / `FloatingTabBar` | An application's primary destinations, docked to the bottom edge or floating above it |
| `NavList` | A sidebar of destinations, with expandable sections |
| `SwipePager` / `PagerControls` / `PageIndicator` | Pages swiped sideways with their dots and a next button — an onboarding tour |

```kotlin
TopBar(
    "Settings",
    scrollState = topBarScroll,
    leading = { BackAction(onClick = onBack) },
    trailing = { SearchAction(onClick = onSearch) },
)

Tabs(
    listOf(
        TabItem(value = "inbox") { Text("Inbox") },
        TabItem(value = "archive") { Text("Archive") },
    ),
    selectedValue = tab,
    onValueSelected = { tab = it },
)
```

A top bar's `leading` and `trailing` slots are a `TopBarActionScope`, which brings the bar's
controls: `BackAction`, `CloseAction`, `IconAction`, `TextAction`, `DoneAction`, `ShareAction` and
the rest. Use them rather than raw icon buttons — they come out the same size and the same distance
from the edge on every screen. `TopBarActionStyle` (`Plain`, `Tonal`, `Floating`, `Scrim`) sets
their appearance, and `TopBarStyle` the bar's.

A bar handed a `rememberTopBarScrollState()` learns about the content from the state's
`nestedScrollConnection`, attached to the scrolling content with `Modifier.nestedScroll`. It then
shows its rule once content is under it, and with `TopBarScrollBehavior.EnterAlways` or
`ExitUntilCollapsed` it also moves out of the way.

Selection is the caller's in `Tabs`, `TabBar`, `FloatingTabBar` and `NavList`. The first three also
have overloads that hold it in a remembered state — `rememberTabsState`, `rememberTabBarState`.

### `wheel`

`WheelPicker` holds one or more `WheelPickerColumn`s and draws what they share: the highlight on the
centre row and the fade at the edges. Each column needs a width of its own.

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
    )
}
```

`valueDescription` turns an item into what a screen reader says for it; the default is `toString()`.

---

## Accessibility

**What components announce.** Every component sets its own role and state, so a screen reader
meets the right thing without extra modifiers:

- Buttons, icon buttons, pressable badges and cards are buttons. A loading button says its
  `loadingStateDescription`.
- `Checkbox`, `Radio` and `Toggle` report checked, selected or on in the platform's own words; a
  read-only control is announced as offering no action. List and settings rows that hold a control
  report its state on the row.
- `Heading`, a `TopBar` title, a labelled divider and a settings section header are headings.
- `Tabs`, `TabBar` and `FloatingTabBar` are tab groups; a single-choice badge group and a
  `SwatchPicker` are selectable groups. A group of bare `Radio`s is one once you add
  `Modifier.selectableGroup()`.
- `ProgressBar`, `StepIndicator` and `Spinner` are progress indicators; `NumberStepper` is one
  adjustable value the platform's adjust gestures move.
- A `Table` is a collection of rows and columns whose header cells are headings.
- A field given its group's `FieldScope` announces the group's label, required marker and error.
- `SensitiveText` keeps its value from assistive technology while concealed.

**What you supply.** Name what the component cannot know: `contentDescription` on an icon that
carries meaning, an icon button, a `NotificationBadge` ("3 unread messages", not "3"), an
`IndicatorDot` that is the only statement of a status, and a progress indicator. Pass `null` for an
icon that repeats the text beside it.

**Localisation.** Every string a component speaks or shows on its own is a parameter with an English
default — `loadingStateDescription = "Busy"`, `revealContentDescription = "Show password"`,
`overlayContentDescription = "Dismiss"`, `BackAction(contentDescription = "Back")`, `ConfirmSheet`'s
`cancelText = "Cancel"`. Where a component speaks several, they are gathered in a labels object:
`SelectFieldLabels`, `ContentListLabels`, `RequirementListLabels` and `NumberStepperLabels`, each
with an `English` value. A localised application passes its own:

```kotlin
RequirementList(
    requirements,
    labels = RequirementListLabels(met = "erfüllt", notMet = "nicht erfüllt"),
    contentDescription = "Passwortanforderungen",
)
```

**Right to left.** Slots are start and end, not left and right, so every row, bar and field mirrors
in a right-to-left layout. `ChevronDirection.Left` and `Right` mirror with it, so `Right` always
points towards the end of the line; the bundled chevron icon mirrors itself. A tab indicator, a
dropdown's offset, a skeleton's shimmer and a `LiquidSweepIndication` all run in reading order, and
`BaseLayout` hands its content the left and right insets as start and end padding.

**Announcing what appears.** A message composed into existence — a validation error, a failed
attempt — is not spoken by a live region on every platform. `AnnounceOnChange` speaks a sentence
when it is first composed and whenever it changes, without moving focus:

```kotlin
AnnounceOnChange(signInError)
```

`null` or blank text says nothing. `FieldGroup` already does this for its error. It speaks through
the screen reader on Android and iOS; the desktop has no announcement API, so there the reader finds
the message when it reaches it.

---

## Experimental API

`@ExperimentalArecaApi` marks API that is public but not yet stable: it may change or go in any
release, minor and patch included, without a deprecation cycle. Everything without the marker
follows the library's compatibility promise.

It marks `PullToRefreshLayout` and everything that belongs to it: `PullToRefreshIndicator`,
`PullToRefreshState`, `rememberPullToRefreshState`, `PullToRefreshStyle`, `PullToRefreshSize` and
their locals. Using one is a compile error until the call site opts in:

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

A module that uses it in many places can opt in once with the compiler argument
`-opt-in=dev.voir.areca.ExperimentalArecaApi`. `PagedContentList` uses the layout internally for its
own pull-to-refresh, which needs no opt-in.

---

## Where to look

- **The playground.** Most components have a page showing their variants, states and sizes, with
  controls for their parameters. Run it from the repository root with `./gradlew :playground:run`;
  the pages are in `playground/src/commonMain/kotlin/dev/voir/areca/playground/foundation/`, one
  package per component package. See [playground/SHOWCASES.md](../../playground/SHOWCASES.md).
- **KDoc.** Every public declaration documents its contract, each parameter included, and most
  components carry a usage example. It is the reference this file leaves out.
- **The other modules.** [crop](../crop/README.md), [keyboard](../keyboard/README.md) and the flow
  modules ([FLOWS.md](../../flow/FLOWS.md)) are built from these components and show them assembled
  into screens.
