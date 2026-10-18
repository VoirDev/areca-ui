# Foundation for agents

How to build screens with `dev.voir.areca:foundation` without reading its source: the rules, which
component answers which intent, the exact signatures of the ones used most, and recipes to copy.
The guide for people — what the module is, setup, theming — is [README.md](README.md); the
repository layout is the root [README.md](../../README.md); flow modules follow
[FLOWS.md](../../flow/FLOWS.md).

```kotlin
implementation("dev.voir.areca:foundation:1.0.0")
implementation("dev.voir.areca:icons:1.0.0") // optional: ArecaIcons.*
```

Areca is published to GitHub Packages, so the build needs its repository first; see
[Installing](../../README.md#installing).

---

## 1. Rules

1. **One `ArecaThemeProvider` at the root.** Every component, and every `ui/*` and `flow/*` module,
   reads tokens and component defaults from it. Without it, components fall back to the light
   library defaults and ignore the app's theme.
2. **No hard-coded design values.** Colours, spacing, radii, shapes, type, elevation and durations
   come from `ArecaTheme.*` (§2). No `Color(0xFF…)`, `16.dp`, `14.sp`, `300.milliseconds` in screen
   code. Screen gutters are `Modifier.horizontalContentInsets()` / `contentInsets()`.
3. **Foundation before raw Compose.** `Text` not `BasicText`, `Icon` not `Image`, `Button` /
   `IconButton` not a clickable `Box`, `Card` not a `Box` with a border, `ListItem` not a `Row`,
   `TextField` not `BasicTextField`, `HorizontalDivider` not a 1dp `Box`. Never Material components.
4. **Customise through the component's own types.** Each component takes a `style: XStyle` (colours,
   shape, indication) and a `size: XSize` (padding, type, metrics). Build them with the companion
   factories and named steps (`ButtonStyle.soft(ActionColor.Danger)`, `ButtonSize.Large`,
   `CardStyle.softNeutral()`), adjust with `.copy(…)`, or provide them for a subtree with
   `CompositionLocalProvider(LocalButtonStyle provides …)`. Do not wrap a component to restyle it.
5. **One solid button per screen.** `ButtonStyle.solid()` (the default) is the primary action.
   Everything else is `soft`, `outlined` or `plain`. Two solid buttons means no primary action.
6. **Never `Modifier.clip` an interactive element.** Indications draw the focus ring outside the
   bounds and clip their own press effect; an outer `clip` crops the ring. Use
   `Modifier.background(color, shape)` and pass the same shape to the indication.
7. **Label everything interactive.** Pass `contentDescription` to every `Icon`, `IconButton`,
   `Avatar`, `Spinner`, `ProgressBar`, `Skeleton` and `StatusMark` that carries meaning, and `null`
   only when adjacent text says the same. Give rows and cards an `onClickLabel`. Controls that sit
   in rows (`Checkbox`, `Radio`, `Toggle`) go in their list items so the title labels them.
8. **Localise.** Components ship English defaults (`"Busy"`, `"Back"`, `"Cancel"`, `"Dismiss"`,
   `"Show password"`, `ContentListLabels.English`, `SelectFieldLabels.English`, …). Production apps
   pass their own strings for every one of these parameters. The literals in this file stand in for
   the app's string resources.
9. **Icons are opt-in.** `ArecaIcons.*` lives in `dev.voir.areca:icons`, which foundation does not
   depend on. Use it only if the app depends on it; otherwise pass the app's own `ImageVector` or
   `Painter`. Each icon is an extension property: `import dev.voir.areca.icons.ArecaIcons` plus
   `import dev.voir.areca.icons.Trash`.
10. **Dark regions use `ArecaDarkTheme { … }`**, which keeps the app's palette, shapes and type. A
    nested `ArecaThemeProvider(darkTheme = true)` resets them to the library defaults.
11. **State is hoisted.** Components are controlled (`value` + `onValueChange`, `visible` +
    `onDismiss`, `selectedValue` + `onValueSelected`). The `rememberXState` helpers exist for state
    that belongs to the screen and nothing else.
12. **Experimental API needs opt-in.** Only the pull-to-refresh family is `@ExperimentalArecaApi`:
    `PullToRefreshLayout`, `PullToRefreshIndicator`, `PullToRefreshState`,
    `rememberPullToRefreshState`, `PullToRefreshStyle`, `PullToRefreshSize` and their locals.
    Annotate the caller with `@OptIn(ExperimentalArecaApi::class)`.

---

## 2. Tokens

Read through `ArecaTheme` inside composition. Every accessor is `@Composable`.

| Accessor | Type | Members |
| --- | --- | --- |
| `ArecaTheme.colors` | `ArecaColors` | Backgrounds `bgCanvas` (page), `bgSurface` (cards), `bgSubtle` (settings sections, grouped regions), `bgMuted` (tracks and wells), `bgContrast`, `bgContrastMuted`, `bgOverlayDark`, `bgOverlayLight`, `bgDisabled`; per tone `bg{Brand,Danger,Success,Warn,Info,Neutral}{Muted,Base,Intense}`. Foregrounds `fgBase`, `fgSubtle`, `fgMuted`, `fgDisabled`, `fgOnContrast`, `fgOnFill`, `fgOnWarn`, `fgNeutral`, `fg{Brand,Danger,Success,Info,Warn}` (marks) and `fg{…}Text` (type). Borders `borderBase`, `borderStrong`, `borderIntense`, `borderSubtle`, `borderOnSubtle` (hairlines on `bgSubtle`), `borderDisabled`, `border{Danger,Success,Info,Warn}`. Accent `accentBase`, `accentStrong`, `accentIntense`, `accentSubtle`, `accentMuted` |
| `ArecaTheme.spacing` | `ArecaSpacing` | `none` 0, `xs2` 2, `xs` 4, `sm` 6, `md` 8, `lg` 12, `xl` 16, `xl2` 20, `xl3` 24, `xl4` 32, `xl5` 40, `xl6` 48, `xl7` 64, `xl8` 80, `xl9` 96; `horizontalContentInsets` 16, `verticalContentInsets` 24 |
| `ArecaTheme.shapes` | `ArecaShapes` | `none`, `xs`, `sm`, `md`, `lg`, `xl`, `xl2`, `full`, `tile`, `button` |
| `ArecaTheme.radius` | `ArecaRadius` | `none`, `xs`, `sm`, `md`, `lg`, `xl`, `xl2` (raw `Dp`; prefer `shapes`) |
| `ArecaTheme.typography` | `ArecaTypography` | `display`, `heading{ExtraSmall,Small,Medium,Large,ExtraLarge}`, `body{ExtraSmall,Small,Medium,Large,ExtraLarge}`, `label{Small,Medium,Large}`, `caption` |
| `ArecaTheme.elevation` | `ArecaElevation` | `none`, `xs`, `sm`, `md`, `lg`, `xl` |
| `ArecaTheme.motion` | `ArecaMotion` | Durations `instant`, `fast`, `medium`, `slow`, `deliberate`; easings `standard`, `enter`, `exit`, `pressIn`, `fade`. `motion.medium.tween(motion.standard)` builds a `TweenSpec` |
| `ArecaTheme.isDark` | `Boolean` | Only for decisions tokens cannot carry |

`Text(text, variant = TextVariant.X, color = TextColor.Y)` is the token-safe way to set type:
`TextVariant` mirrors the typography names (`HeadingSmall`, `BodyMedium`, `LabelSmall`, `Caption`,
`Display`, …) and `TextColor` is `Base`, `Subtle`, `Muted`, `Disabled`, `OnContrast`, `OnFill`,
`Brand`, `Danger`, `Success`, `Info`, `Warning`. `IconSize` is `ExtraSmall` 12, `Small` 16,
`Medium` 20, `Large` 24, `ExtraLarge` 32.

---

## 3. Which component

| Intent | Use |
| --- | --- |
| Primary action | `Button(text, onClick)` — default style `ButtonStyle.solid()`, one per screen |
| Secondary, tertiary action | `ButtonStyle.soft()`, `ButtonStyle.outlined()`; cancel or skip `ButtonStyle.plain(ActionColor.Neutral)` |
| Destructive action | `ButtonStyle.soft(ActionColor.Danger)`, confirmed with `ConfirmSheet` |
| Icon-only action | `IconButton(imageVector, contentDescription, onClick)` |
| Link in text | `TextLink`; prompt plus link ("No account? Sign up") `TextLinkPrompt` |
| Text | `Text(text, variant = TextVariant.BodyMedium, color = TextColor.Subtle)` |
| Title with supporting line | `Heading(title = { … }, supportive = { … })` |
| Masked value (balance, card number) | `SensitiveText` |
| Icon; icon on a tinted tile; disclosure arrow | `Icon`; `FeaturedIcon`; `Chevron` |
| Labelled text input with hint or error | `FieldGroup(label, assistive, error) { field -> TextField(…, field = field) }` |
| Password; its rules | `PasswordField`; `RequirementList` |
| Search box | `SearchField` |
| Amount with currency, unit suffix | `AffixTextField` |
| One-time code | `OtpField` |
| Compound input (code + number) | `FieldRow { SelectSegment(…); TextSegment(…) }` |
| Pick from a short list | `SelectField` (dropdown) |
| Pick from a long or searchable list | `SelectFlowModal` (sheet) |
| Scroll-wheel picker | `WheelPicker { WheelPickerColumn(…) }`; dates and times are `dev.voir.areca:datetime` |
| Filter chips | `BadgeSelect`, `FlowBadgeSelect` (wraps), `LazyBadgeSelect` (scrolls) |
| Colour choice | `SwatchPicker`; one swatch `Swatch` |
| Small integer | `NumberStepper` |
| On/off setting; tick in a form; one of many | `Toggle`; `Checkbox`; `Radio` |
| Those controls in a row with a title | `ToggleListItem`, `CheckboxListItem`, `RadioListItem`, `CheckmarkListItem` |
| Settings screen; a short, bounded group of rows | `Settings { Section { SettingsToggleRow / SettingsSelectRow / SettingsRow / SettingsTextFieldRow / SettingsOptionRow } }`; loading row `SettingsRowSkeleton`; free content `SettingsBlock` (see "Rows, sections and cards" below) |
| Second action in a pressable row; line of detail under a subtitle | `trailing = { SettingsRowLink("Verify", onVerify) }`; the row's `caption` slot |
| Grouped rows on one surface | `List { item(…) }` (eager), `LazyList { … }` (lazy), `ListItemColumn` (hand-composed rows) |
| Row that navigates; row that expands | `NavigationListItem`; `ExpandableListItem` |
| Remote list with loading, empty and error | `ContentList` (eager), `PagedContentList` (lazy, paging, pull-to-refresh), `Grouped…` variants |
| Grid with group headers | `LazyVerticalGroupedGrid`, `LazyHorizontalGroupedGrid` |
| Tabular data | `Table`, `LazyTable` |
| Surface grouping content, optionally pressable | `Card`; with a disclosed part `Card(expanded, …, expandedContent)` |
| Card with its status and actions along the bottom | `Card(footer = { … }, footerStyle = CardFooterStyle.soft(ActionColor.Brand))` |
| Show and hide a region | `Expandable` |
| Separator, optionally labelled | `HorizontalDivider`, `VerticalDivider` |
| Screen frame with bars | `BaseLayout(topBar, bottomBar, overlay) { padding -> … }` |
| Screen title bar | `TopBar(title, leading = { BackAction(…) }, trailing = { … })` |
| Large title that collapses | `LargeTopBar` + `rememberTopBarScrollState(TopBarScrollBehavior.ExitUntilCollapsed)` |
| Progress under the bar | `TopBarProgress` in the bar's `bottom` slot |
| Hero band that collapses above pinned content | `CollapsingHeaderLayout` |
| App destinations at the bottom | `TabBar` (docked), `FloatingTabBar` (floating) |
| Switch views within a screen; segmented setting | `Tabs` (`TabsVariant.Underline`); `Tabs(variant = TabsVariant.Button)` |
| Sidebar or tree navigation | `NavList`, `NavItem` |
| Paged onboarding | `SwipePager` + `PagerControls` + `PageIndicator`, `rememberPagerController` |
| Steps of a flow | `StepIndicator` |
| Pull to refresh | `PagedContentList(onRefresh = …)`, or `PullToRefreshLayout` (experimental) around any scroller |
| Bottom sheet | `FlowModal` + `rememberFlowModal()`; its buttons `FlowModalActionBar` |
| Confirm before something hard to undo | `ConfirmSheet` (phone); `ConfirmationDialog` (centred, suspend handlers) |
| Any other dialog | `Dialog` with `ModalDefaults.Actions` |
| Menu hanging off a control | `DropdownPopup` + `rememberDropdownPopup()` + `Modifier.anchored(state.anchorState)` |
| Transient message | `ToastHost` + `rememberToastState().show(label = …)` |
| Persistent inline message | `Notice` with `NoticeText`; appearing and leaving `AnimatedNotice` |
| Empty or failed region | `Fallback(top, heading, body, content)` |
| Whole-screen progress or result | `StatusScene(title, mark = { StatusMark(…) }, actions = { StatusSceneActions(…) })` |
| Loading placeholder | `Skeleton`, `ListItemSkeleton`, `SettingsRowSkeleton` |
| Waiting; known progress | `Spinner`; `ProgressBar` |
| Status label; count; dot | `Badge(style = BadgeStyle.Success)`; `NotificationBadge`; `IndicatorDot` |
| Person | `Avatar(letters, contentDescription)` |
| Custom start, centre, end row | `SlotRow`; aligned like a list row, `SlotRow(layout = SlotRowLayout.Auto)` + `Modifier.firstLineCenter(textStyle)` |
| Content placed beside other content; scrim over a box | `RelativePositionLayout`; `BoxScope.Overlay` |
| Tell a screen reader something changed | `AnnounceOnChange(text)` |
| Region that is always dark; system bar colour | `ArecaDarkTheme`; `SystemBars` |
| Custom pressable surface | `Modifier.clickable(indication = TouchIndication(shape))` + `clickCursor()` (§7) |

### Rows, sections and cards

#### Pick the surface by what the rows are

- A `Settings` section holds a short, fixed set: options, switches, a form typed in place, a few
  actions, or a bounded group of things — the sign-in methods of one account, the devices it is
  signed in on, the files on one record. About one to ten rows a section; split a longer one into
  named sections.
- Anything that can grow without limit — transactions, messages, search results, a file library —
  is a `List` / `LazyList` of `ListItem` or `NavigationListItem` rows, or `ContentList` /
  `PagedContentList` when it loads. A settings section composes every row at once, does not page
  and has no empty or error face.
- **When the request does not say how many items a list holds and the answer decides between the
  two, ask before building it.** Offer both: grouped settings rows read calmer for a few items; a
  list scales. Do not guess.
- Keep one vocabulary across the app. Once a kind of thing is shown one way — devices as settings
  rows, an edit screen as `SettingsTextFieldRow`s — show it that way on every screen, or ask before
  departing from it.

#### Text roles

A row's slots carry fixed roles; plain `Text("…")` in a slot takes the role's size and ink.

| Role | Slot | Style · ink | Holds |
| --- | --- | --- | --- |
| Title | `title` | `bodyMedium` · `fgBase` | What the row is, or the fact itself |
| Subtitle | `subtitle` | `bodySmall` · `fgMuted` | What the fact is, or a short explanation |
| Caption | `caption` | `bodyExtraSmall` · subtitle's ink | Detail: last seen, size, date |
| Value | `value` | `bodyMedium` · `fgSubtle` | The current answer: "English", "2.4 GB" |
| Badge | `badge` | extra-small neutral `Badge` | State of the title: "New", "Unverified" |
| Second action | `trailing = { SettingsRowLink(…) }` | value size, row ink, underlined | "Verify", "Resend" |
| Section header · footer | `Section(header, footer)` | `labelMedium` · `fgSubtle`; `bodySmall` · `fgMuted` | Group name; what the group means |

- Never pass `style` or `fontWeight` to text in a row slot; `maxLines` and `overflow` are fine.
  Colour comes from `tone`, `status` and `badge`; the one exception is a line that reports a
  problem to act on — a file that failed to upload — which takes its family's text ink
  (`fgWarnText`, `fgDangerText`), never a grey picked to look quieter or louder.
- Never stack lines in `subtitle` with a `Column`: a third line is the `caption`.
- A short answer is the `value` ("Language" — "English"). Long data — an address, a phone number —
  is the `title`, with what it is as the `subtitle` ("ada@…" over "Email").
- A row with one action is pressable and names it in `value` ("Link", "Download"); only a second
  action takes a `SettingsRowLink`.
- A row built by hand next to rows — a summary at the top of a card — reads its type and ink from
  `SettingsTheme.sizes.row` and `SettingsTheme.styles.row` instead of picking its own.

#### Alignment: pick by scenario

A row's leading visual and trailing content line up with its text according to a
`SlotRowAlignment`. List and settings rows use `Auto`, which picks `Center` or `Top` from how much
text the row holds — so most rows need nothing. Choose by the scenario, never by nudging:

| Scenario | Alignment | What lines up |
| --- | --- | --- |
| One line — a switch, a menu entry, "Language — English" | `Center` (via `Auto`) | Everything on the row's centre |
| Two lines — title over subtitle, a value over its label, a person with a handle, an account with its balance, a card's name over its detail | `Center` (via `Auto`) | Visual, text block and trailing all centred; an amount or chevron sits level with the icon |
| Three lines or more — a wrapped description, title + subtitle + caption, a growing field | `Top` (via `Auto`) | Visual and text start at the top and the text runs down; the trailing switch, checkbox or amount stays level with the visual |
| Everything pinned to the top whatever the length — a feed entry, a comment | `Top` | As above, without switching to centred for short text |
| A marker no taller than a line — a legend dot, a bullet, a tiny status glyph | `FirstLine` | The marker and the trailing value centre on the first line of text |

- Rows: leave `layout` alone for `Auto`; pass `layout = SlotRowLayout.Top`, `.Center` or
  `.FirstLine` only when the scenario says so.
- A row of your own: `SlotRow(layout = SlotRowLayout.Auto)` (or the scenario's preset) and
  `Modifier.firstLineCenter(textStyle)` on the text that is its first line — `Auto` measures the
  text in lines from it.
- A trailing control never aligns to the top of the text on its own; it follows the visual.
- Never pad a glyph, a dot or a control to line it up. If it looks wrong, the scenario is wrong.

#### Leading visuals

Within one section or list, every leading visual has **one size and one shape**, so the visuals
read as a column and the text beside them starts at one edge:

- Glyphs go on a `SettingsRowIcon` tile.
- Anything else — a flag, a photo, a brand or provider mark — goes in `SettingsRowTile { … }`,
  which crops it to the same tile.
- People are round avatars; give `SettingsRowTile` `shape = ArecaTheme.shapes.full` around a picture
  at the tile size rather than mixing a larger avatar into a column of tiles. A list that is only
  people may use a larger avatar throughout.
- Do not mix a bare glyph, a tile and a flag in one column.

#### Card corners

Corners are part of the layout, so they are decided once per app, not per card:

- **One radius per role.** Every full-width card — `Card`, settings sections, notices, banners —
  shares one corner (Areca's defaults are all `shapes.lg`), whether it sits on the page or on a
  full-width panel or sheet: a surface that spans the screen counts as the page. Compact cards (a
  half-width tile in a grid) and cards nested inside another card step down one radius. Icon tiles
  are `shapes.tile`; people are `shapes.full`.
- **Set it once.** Provide the app's card corner through `LocalCardStyle` / the `CardStyle` preset
  and the settings section style, and reuse those; never pass `shape =` to one card on a screen.
- **The same kind of card has the same corner everywhere.** Two plan cards, two wallet cards, a
  card in a list and the same card in a picker are the same object and must not differ by radius.
- **Concentric nesting.** A nested surface's radius is the outer radius minus the padding between
  them, rounded to the scale — never larger than the outer one.

#### Section bands

A section's closing action — "Terminate all other sessions", "Add device" — sits on the section's
`band`, a strip at the bottom of its card separated by colour, not by one more row behind a
hairline:

```kotlin
Section(
    header = { Text(thisDeviceHeader) },
    bandColor = ArecaTheme.colors.bgDangerMuted,
    band = {
        SettingsRow(
            title = { Text(signOutOthersLabel) },
            tone = SettingsRowTone.Danger,
            onClick = onSignOutOthers,
        )
    },
) { SessionRow(session = current) }
```

The band's default colour is the neutral step past the card; use the danger tint under a danger
row. One band per section, holding one row.

#### Card footers

A card's status and actions go in its `footer`, not in a divider and a row drawn by hand:

```kotlin
Card(
    modifier = Modifier.fillMaxWidth(),
    style = CardStyle.Outlined,
    footerStyle = CardFooterStyle.soft(ActionColor.Brand),
    footer = {
        Text(renewalLine, modifier = Modifier.weight(1f))
        Button(onClick = onManage) { Text(manageLabel) }
    },
) { padding -> PlanSummary(plan, Modifier.padding(padding)) }
```

`plain()` for supporting detail, `soft(color)` for a state worth noticing (`Warning` for a trial
ending, `Danger` for a failed payment), `solid(color)` for the card's call to action — one per
screen — and `of(bg, content)` for a colour outside the families. The footer hands its ink and a
matching borderless button style to what is inside it, so do not style the copy or the buttons.

#### Surfaces

The page is `bgCanvas`. Settings sections and other grouped regions are `bgSubtle` — the section
card's default — with `borderOnSubtle` hairlines; tracks and wells inside them are `bgMuted`;
raised cards are `bgSurface` or a `CardStyle` preset. Do not put a `bgSubtle` region on another.

### Packages

Every foundation symbol is imported from its family's package. Extension composables (`Section`,
`BackAction`, `TextSegment`, …) are imported by name like any top-level function.

| Package | Holds |
| --- | --- |
| `dev.voir.areca.theme` | `ArecaThemeProvider`, `ArecaDarkTheme`, `ArecaTheme`, `contentInsets`, `horizontalContentInsets`, `verticalContentInsets`, `tween` |
| `dev.voir.areca.foundation` | `LocalContentColor`, `LocalTextStyle`, `AnnounceOnChange` |
| `….foundation.button` | `Button`, `IconButton`, `ButtonStyle`, `ButtonSize`, `IconButtonStyle`, `IconButtonSize`, `ActionColor`, `LocalButtonStyle`, `LocalButtonSize` |
| `….foundation.text` | `Text`, `TextVariant`, `TextColor`, `Heading`, `TextLink`, `TextLinkPrompt`, `SensitiveText` |
| `….foundation.icon` | `Icon`, `IconSize`, `LocalIconSize`, `FeaturedIcon`, `Chevron` |
| `….foundation.field` | `TextField`, `PasswordField`, `SearchField`, `AffixTextField`, `OtpField`, `FieldGroup`, `FieldRow`, `TextSegment`, `FieldStatus`, `FieldStyle`, `FieldSize`, `FieldLabelPlacement` |
| `….foundation.field.select` | `SelectField`, `SelectOption`, `SelectSelectionMode`, `SelectFieldLabels`, `SelectFlowModal`, `SelectSegment` |
| `….foundation.selection` | `Checkbox`, `Radio`, `Toggle`, `ToggleState`, `NumberStepper`, `Swatch`, `SwatchPicker` |
| `….foundation.list` | `List`, `LazyList`, `ListItem`, `NavigationListItem`, `CheckboxListItem`, `RadioListItem`, `ToggleListItem`, `CheckmarkListItem`, `ExpandableListItem`, `ListItemColumn`, `ListItemSkeleton`, `ListItemStyle`, `ListItemSize` |
| `….foundation.settings` | `Settings`, `Section`, `SettingsRow`, `SettingsToggleRow`, `SettingsSelectRow`, `SettingsTextFieldRow`, `SettingsOptionRow`, `SettingsBlock`, `SettingsRowIcon`, `SettingsRowTile`, `SettingsRowLink`, `SettingsRowStatus`, `SettingsRowTone`, `SettingsRowSkeleton` |
| `….foundation.layout` | `BaseLayout`, `Card`, `CardStyle`, `CardSize`, `CardFooterStyle`, `HorizontalDivider`, `VerticalDivider`, `Expandable`, `PullToRefreshLayout`, `CollapsingHeaderLayout`, `Overlay`, `RelativePositionLayout` |
| `….foundation.topbar` | `TopBar`, `LargeTopBar`, `rememberTopBarScrollState`, `TopBarScrollBehavior`, `TopBarStyle`, `TopBarSize`, the `…Action` controls, `TopBarProgress` |
| `….foundation.tabbar`, `….foundation.tabs` | `TabBar`, `FloatingTabBar`, `TabBarItem`; `Tabs`, `TabItem`, `TabsVariant`, `rememberTabsState` |
| `….foundation.overlay` | `Dialog`, `ModalDefaults`, `ConfirmationDialog`, `ConfirmSheet`, `FlowModal`, `rememberFlowModal`, `FlowModalController`, `FlowModalActionBar`, `DropdownPopup`, `rememberDropdownPopup`, `anchored` |
| `….foundation.feedback` | `ToastHost`, `rememberToastState`, `Toast`, `Fallback`, `Skeleton`, `Spinner`, `ProgressBar`, `StepIndicator`, `RequirementList` |
| `….foundation.notice`, `….foundation.status` | `Notice`, `NoticeText`, `NoticeTone`, `NoticeEmphasis`, `AnimatedNotice`; `StatusScene`, `StatusSceneActions`, `StatusMark`, `StatusMarkState` |
| `….foundation.badge`, `….foundation.avatar` | `Badge`, `BadgeStyle`, `NotificationBadge`, `IndicatorDot`, `BadgeSelect`; `Avatar` |
| `….foundation.collection`, `….foundation.table` | `ContentList`, `PagedContentList`, `LimitedContentListState`, `PagedContentListState`, `ContentListLabels`, `ContentListDefaults`, `LazyVerticalGroupedGrid`; `Table`, `LazyTable`, `TableColumn` |
| `….foundation.navigation`, `….foundation.wheel`, `….foundation.slot` | `NavList`, `SwipePager`, `PagerControls`, `PageIndicator`, `rememberPagerController`; `WheelPicker`, `WheelPickerColumn`, `rememberWheelPickerColumnState`; `SlotRow`, `SlotRowLayout` (`Default`, `Auto`, `Center`, `Top`, `FirstLine`, `Centered`, `Fill`, `wrap(…)`), `SlotRowAlignment`, `Modifier.firstLineCenter(textStyle)`, `FirstLineCenter` |
| `dev.voir.areca.indication`, `.modifier`, `.platform`, `.shape` | `TouchIndication`, `FadeIndication`, `StrokeIndication`, `FocusOutline`; `clickCursor`, `minimumTouchTarget`, `thenIf`; `SystemBars`; `SquircleShape` |

---

## 4. Customising

```kotlin
@Composable
fun CustomisingExamples(onSave: () -> Unit, onCancel: () -> Unit, onDelete: () -> Unit) {
    Button(text = "Delete", onClick = onDelete, style = ButtonStyle.outlined(ActionColor.Danger))
    Button(
        text = "Save",
        onClick = onSave,
        style = ButtonStyle.solid(shape = ArecaTheme.shapes.full),
        size = ButtonSize.Large,
    )
    Card(
        style = CardStyle.softNeutral(),
        size = CardSize.default(padding = PaddingValues(ArecaTheme.spacing.xl3)),
    ) { padding ->
        Text("Card body", modifier = Modifier.padding(padding))
    }
    Badge(style = BadgeStyle.Success.copy(content = ArecaTheme.colors.fgSuccessText)) {
        Text("Paid")
    }
    CompositionLocalProvider(
        LocalButtonStyle provides ButtonStyle.plain(ActionColor.Neutral),
        LocalButtonSize provides ButtonSize.Small,
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
            Button(text = "Cancel", onClick = onCancel)
            Button(text = "Skip", onClick = onCancel)
        }
    }
}
```

- **Factories** take the semantic family (`ActionColor.Brand`, `Danger`, `Success`, `Warning`,
  `Neutral`, `NeutralDark`) and a shape: `ButtonStyle.solid / soft / outlined / plain`,
  `IconButtonStyle.solid / soft / outlined / plain`, `FeaturedIconStyle.solid / soft / outlined`.
  Other styles have named presets (`CardStyle.Outlined`, `BadgeStyle.Danger`, `TopBarStyle.Tonal`)
  and a `default(…)` factory whose parameters are the tokens it reads.
- **Sizes** are named steps (`ButtonSize.ExtraSmall…ExtraLarge`, `IconButtonSize.Tiny…ExtraLarge`,
  `AvatarSize`, `HeadingSize`, `FieldSize`) or `XSize.default(…)`.
- **`.copy(…)`** changes one property of a built style. `ButtonStyle` and `IconButtonStyle` draw
  their indication in the style's `shape`, so `.copy(shape = …)` moves the feedback with it.
- **`LocalXStyle` / `LocalXSize`** (nullable, seeded by `ArecaThemeProvider`) restyle every instance
  in a subtree. `LocalNoticeStyle`, `LocalConfirmSheetStyle` and `LocalConfirmSheetSize` stay `null`
  unless provided: a notice derives its style from its tone, and a confirm sheet builds its default
  on the `LocalFlowModalStyle` / `LocalFlowModalSize` in scope where it is drawn. Settings lists take `styles = SettingsDefaults.styles(…)` and
  `sizes = SettingsDefaults.sizes(…)` instead.
- **App-wide** retuning is `ArecaThemeProvider(baseColors = …, fonts = …, radius = …, …)`; see
  [README.md](README.md).

---

## 5. Signatures

All are `@Composable public fun` in the packages of §3, with parameters in source order. Call with
named arguments after the leading required ones. Comments name the other overloads.

### Buttons, text, icons

```kotlin
fun Button(text: String, onClick: (() -> Unit)?, modifier: Modifier = Modifier,
    style: ButtonStyle = LocalButtonStyle.current ?: ButtonStyle.solid(),
    size: ButtonSize = LocalButtonSize.current ?: ButtonSize.Medium,
    textStyle: TextStyle = size.textStyle, textAlign: TextAlign = TextAlign.Center,
    layout: SlotRowLayout = SlotRowLayout.wrap(startGap = size.gap, endGap = size.gap),
    enabled: Boolean = true, loading: Boolean = false, loadingStateDescription: String = "Busy",
    interactionSource: MutableInteractionSource? = null, leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null)
// Overload: `content: @Composable () -> Unit` (last) in place of `text`.

fun IconButton(imageVector: ImageVector, contentDescription: String?, onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    style: IconButtonStyle = LocalIconButtonStyle.current ?: IconButtonStyle.solid(),
    size: IconButtonSize = LocalIconButtonSize.current ?: IconButtonSize.Medium,
    enabled: Boolean = true, loading: Boolean = false, loadingStateDescription: String = "Busy",
    interactionSource: MutableInteractionSource? = null)
// Overloads: `imageBitmap: ImageBitmap` or `painter: Painter` in place of `imageVector`; or no
// image and no `contentDescription`, with a trailing `content: @Composable BoxScope.() -> Unit`.

fun Text(text: CharSequence, variant: TextVariant, modifier: Modifier = Modifier,
    color: TextColor = TextColor.Base, fontSize: TextUnit = TextUnit.Unspecified,
    lineHeight: TextUnit = TextUnit.Unspecified, fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null, fontStyle: FontStyle? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified, textDecoration: TextDecoration? = null,
    textAlign: TextAlign = TextAlign.Unspecified, overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true, maxLines: Int = Int.MAX_VALUE, minLines: Int = 1,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null)

fun Text(text: CharSequence, modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current, color: Color = LocalContentColor.current,
    fontSize: TextUnit = TextUnit.Unspecified, lineHeight: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight? = null, fontFamily: FontFamily? = null, fontStyle: FontStyle? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified, textDecoration: TextDecoration? = null,
    textAlign: TextAlign = TextAlign.Unspecified, overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true, maxLines: Int = Int.MAX_VALUE, minLines: Int = 1,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null)

fun Heading(title: @Composable () -> Unit, modifier: Modifier = Modifier,
    style: HeadingStyle = LocalHeadingStyle.current ?: HeadingStyle.Default,
    size: HeadingSize = LocalHeadingSize.current ?: HeadingSize.Medium,
    supportive: (@Composable () -> Unit)? = null, content: (@Composable () -> Unit)? = null)

fun Icon(imageVector: ImageVector, contentDescription: String?, modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current, size: Dp = LocalIconSize.current)
// Overloads: `painter: Painter` or `bitmap: ImageBitmap` in place of `imageVector`.
```

### Fields

```kotlin
fun FieldGroup(modifier: Modifier = Modifier, label: CharSequence? = null,
    required: Boolean = false, requiredText: String = "*", requiredDescription: String = "required",
    assistive: CharSequence? = null, error: CharSequence? = null,
    status: FieldStatus = if (error != null) FieldStatus.Error else FieldStatus.Default,
    style: FieldStyle = FieldDefaults.style, size: FieldSize = FieldDefaults.size,
    labelPlacement: FieldLabelPlacement = FieldLabelPlacement.Above,
    messagePlacement: FieldMessagePlacement = FieldMessagePlacement.Below,
    messageBehavior: FieldMessageBehavior = FieldMessageBehavior.Animated,
    labelSpace: Dp = size.label.gap, messageSpace: Dp = size.message.gap, enabled: Boolean = true,
    content: @Composable (field: FieldScope) -> Unit)

fun TextField(value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier,
    format: ((String) -> String)? = null, style: FieldStyle = FieldDefaults.style,
    size: FieldSize = FieldDefaults.size, field: FieldScope? = null,
    status: FieldStatus = field?.status ?: FieldStatus.Default,
    enabled: Boolean = field?.enabled ?: true, readOnly: Boolean = false, loading: Boolean = false,
    leading: (@Composable RowScope.() -> Unit)? = null,
    trailing: (@Composable RowScope.() -> Unit)? = null,
    placeholder: (@Composable RowScope.() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = false, maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1, cursorBrush: Brush = SolidColor(ArecaTheme.colors.accentBase),
    interactionSource: MutableInteractionSource? = null)
// Overload: `value: TextFieldValue`, `onValueChange: (TextFieldValue) -> Unit`, no `format`.

fun PasswordField(value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier,
    format: ((String) -> String)? = null, style: FieldStyle = FieldDefaults.style,
    size: FieldSize = FieldDefaults.size, field: FieldScope? = null,
    status: FieldStatus = field?.status ?: FieldStatus.Default,
    enabled: Boolean = field?.enabled ?: true, readOnly: Boolean = false, loading: Boolean = false,
    placeholder: (@Composable RowScope.() -> Unit)? = null,
    leading: (@Composable RowScope.() -> Unit)? = null,
    trailing: (@Composable RowScope.() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    revealContentDescription: String = "Show password",
    hideContentDescription: String = "Hide password",
    cursorBrush: Brush = SolidColor(ArecaTheme.colors.accentBase),
    interactionSource: MutableInteractionSource? = null)

fun SearchField(value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier,
    style: FieldStyle = FieldDefaults.style, size: FieldSize = FieldDefaults.size,
    field: FieldScope? = null, status: FieldStatus = field?.status ?: FieldStatus.Default,
    enabled: Boolean = field?.enabled ?: true, readOnly: Boolean = false, loading: Boolean = false,
    placeholder: (@Composable RowScope.() -> Unit)? = null,
    onSearch: (KeyboardActionScope.() -> Unit)? = null,
    keyboardActions: KeyboardActions = KeyboardActions(onSearch = onSearch),
    keyboardOptions: KeyboardOptions =
        KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Search),
    searchContentDescription: String = "Search", clearContentDescription: String = "Clear search")

fun <Value> SelectField(values: Set<Value>, onChange: (Set<Value>) -> Unit,
    options: List<SelectOption<Value>>, modifier: Modifier = Modifier,
    selectionMode: SelectSelectionMode = SelectSelectionMode.Single,
    style: FieldStyle = FieldDefaults.style, size: FieldSize = FieldDefaults.size,
    field: FieldScope? = null, status: FieldStatus = field?.status ?: FieldStatus.Default,
    enabled: Boolean = field?.enabled ?: true, readOnly: Boolean = false, loading: Boolean = false,
    empty: Boolean = values.isEmpty(), placement: AnchoredPlacement = AnchoredPlacement.BelowStart,
    dropdownStyle: DropdownPopupStyle =
        LocalDropdownPopupStyle.current ?: DropdownPopupStyle.Default,
    dropdownSize: DropdownPopupSize = LocalDropdownPopupSize.current ?: DropdownPopupSize.Default,
    labels: SelectFieldLabels = SelectFieldLabels.English,
    placeholder: (@Composable RowScope.() -> Unit)? = null,
    leading: (@Composable RowScope.() -> Unit)? = null,
    trailing: (@Composable RowScope.(expanded: Boolean) -> Unit)? = null,
    valueContent: (@Composable RowScope.(selectedEntries: List<SelectOption<Value>>) -> Unit)? =
        null,
    content: @Composable (
        options: List<SelectOption<Value>>,
        values: Set<Value>,
        onSelect: (SelectOption<Value>) -> Unit,
    ) -> Unit = { dropdownOptions, selectedValues, onSelect ->
        SelectFlowModalDefaults.List(options = dropdownOptions, values = selectedValues,
            onSelect = onSelect)
    })
// SelectOption(key: Any, value: T, label: String, enabled: Boolean = true)
// SelectSelectionMode.Single | Multiple. In Single, choosing the selected option clears it.
```

### Selection controls

```kotlin
fun Checkbox(checked: Boolean, onCheckedChange: ((Boolean) -> Unit)?, modifier: Modifier = Modifier,
    style: CheckboxStyle = LocalCheckboxStyle.current ?: CheckboxStyle.Default,
    size: CheckboxSize = LocalCheckboxSize.current ?: CheckboxSize.Default,
    indeterminate: Boolean = false, enabled: Boolean = true, readOnly: Boolean = false,
    interactionSource: MutableInteractionSource? = null)

fun Radio(checked: Boolean, onClick: (() -> Unit)?, modifier: Modifier = Modifier,
    style: RadioStyle = LocalRadioStyle.current ?: RadioStyle.Default,
    size: RadioSize = LocalRadioSize.current ?: RadioSize.Default, enabled: Boolean = true,
    readOnly: Boolean = false, interactionSource: MutableInteractionSource? = null)

fun Toggle(state: ToggleState, onToggle: ((ToggleState) -> Unit)?, modifier: Modifier = Modifier,
    style: ToggleStyle = LocalToggleStyle.current ?: ToggleStyle.Default,
    size: ToggleSize = LocalToggleSize.current ?: ToggleSize.Default,
    cycleOrder: List<ToggleState> = BinaryToggleCycle, enabled: Boolean = true,
    readOnly: Boolean = false, interactionSource: MutableInteractionSource? = null)
// ToggleState.Off | Indeterminate | On
```

### Lists

```kotlin
fun ListItem(modifier: Modifier = Modifier, title: (@Composable () -> Unit)? = null,
    subtitle: (@Composable () -> Unit)? = null, overline: (@Composable () -> Unit)? = null,
    caption: (@Composable () -> Unit)? = null, value: (@Composable () -> Unit)? = null,
    enabled: Boolean = true, selected: Boolean = false,
    highlightSelected: Boolean = true, dragging: Boolean = false, nestingLevel: Int = 0,
    style: ListItemStyle = LocalListItemStyle.current ?: ListItemStyle.Default,
    size: ListItemSize = LocalListItemSize.current ?: ListItemSize.Default,
    layout: SlotRowLayout = LocalListItemLayout.current,
    interactionSource: MutableInteractionSource? = null, onClick: (() -> Unit)? = null,
    onClickLabel: String? = null, role: Role? = Role.Button,
    leading: (@Composable BoxScope.() -> Unit)? = null,
    trailing: (@Composable BoxScope.() -> Unit)? = null,
    bottom: (@Composable ColumnScope.() -> Unit)? = null,
    content: (@Composable BoxScope.() -> Unit)? = null)

fun NavigationListItem(onClick: () -> Unit, title: @Composable () -> Unit,
    modifier: Modifier = Modifier, subtitle: (@Composable () -> Unit)? = null,
    overline: (@Composable () -> Unit)? = null, value: (@Composable () -> Unit)? = null,
    enabled: Boolean = true, selected: Boolean = false, nestingLevel: Int = 0,
    direction: ChevronDirection = ChevronDirection.Right,
    style: ListItemStyle = LocalListItemStyle.current ?: ListItemStyle.Default,
    size: ListItemSize = LocalListItemSize.current ?: ListItemSize.Default,
    layout: SlotRowLayout = LocalListItemLayout.current,
    interactionSource: MutableInteractionSource? = null, onClickLabel: String? = null,
    leading: (@Composable BoxScope.() -> Unit)? = null,
    bottom: (@Composable ColumnScope.() -> Unit)? = null)

fun CheckboxListItem(checked: Boolean, onCheckedChange: (Boolean) -> Unit,
    title: @Composable () -> Unit, modifier: Modifier = Modifier,
    subtitle: (@Composable () -> Unit)? = null, overline: (@Composable () -> Unit)? = null,
    value: (@Composable () -> Unit)? = null, enabled: Boolean = true,
    indeterminate: Boolean = false, highlightChecked: Boolean = false, nestingLevel: Int = 0,
    placement: ListItemControlPlacement = ListItemControlPlacement.Leading,
    style: ListItemStyle = LocalListItemStyle.current ?: ListItemStyle.Default,
    size: ListItemSize = LocalListItemSize.current ?: ListItemSize.Default,
    layout: SlotRowLayout = LocalListItemLayout.current,
    checkboxStyle: CheckboxStyle = LocalCheckboxStyle.current ?: CheckboxStyle.Default,
    checkboxSize: CheckboxSize = LocalCheckboxSize.current ?: CheckboxSize.Default,
    interactionSource: MutableInteractionSource? = null,
    leading: (@Composable BoxScope.() -> Unit)? = null,
    trailing: (@Composable BoxScope.() -> Unit)? = null,
    bottom: (@Composable ColumnScope.() -> Unit)? = null)

fun RadioListItem(selected: Boolean, onSelect: () -> Unit, title: @Composable () -> Unit,
    modifier: Modifier = Modifier, subtitle: (@Composable () -> Unit)? = null,
    overline: (@Composable () -> Unit)? = null, value: (@Composable () -> Unit)? = null,
    enabled: Boolean = true, highlightSelected: Boolean = false, nestingLevel: Int = 0,
    placement: ListItemControlPlacement = ListItemControlPlacement.Leading,
    style: ListItemStyle = LocalListItemStyle.current ?: ListItemStyle.Default,
    size: ListItemSize = LocalListItemSize.current ?: ListItemSize.Default,
    layout: SlotRowLayout = LocalListItemLayout.current,
    radioStyle: RadioStyle = LocalRadioStyle.current ?: RadioStyle.Default,
    radioSize: RadioSize = LocalRadioSize.current ?: RadioSize.Default,
    interactionSource: MutableInteractionSource? = null,
    leading: (@Composable BoxScope.() -> Unit)? = null,
    trailing: (@Composable BoxScope.() -> Unit)? = null,
    bottom: (@Composable ColumnScope.() -> Unit)? = null)

fun ToggleListItem(state: ToggleState, onToggle: (ToggleState) -> Unit,
    title: @Composable () -> Unit, modifier: Modifier = Modifier,
    subtitle: (@Composable () -> Unit)? = null, overline: (@Composable () -> Unit)? = null,
    enabled: Boolean = true, nestingLevel: Int = 0,
    cycleOrder: List<ToggleState> = BinaryToggleCycle,
    style: ListItemStyle = LocalListItemStyle.current ?: ListItemStyle.Default,
    size: ListItemSize = LocalListItemSize.current ?: ListItemSize.Default,
    layout: SlotRowLayout = LocalListItemLayout.current,
    toggleStyle: ToggleStyle = LocalToggleStyle.current ?: ToggleStyle.Default,
    toggleSize: ToggleSize = LocalToggleSize.current ?: ToggleSize.Default,
    interactionSource: MutableInteractionSource? = null,
    leading: (@Composable BoxScope.() -> Unit)? = null,
    bottom: (@Composable ColumnScope.() -> Unit)? = null)

fun CheckmarkListItem(selected: Boolean, onClick: () -> Unit, title: @Composable () -> Unit,
    modifier: Modifier = Modifier, subtitle: (@Composable () -> Unit)? = null,
    overline: (@Composable () -> Unit)? = null, value: (@Composable () -> Unit)? = null,
    enabled: Boolean = true, highlightSelected: Boolean = false,
    checkmarkColor: Color = Color.Unspecified, nestingLevel: Int = 0,
    style: ListItemStyle = LocalListItemStyle.current ?: ListItemStyle.Default,
    size: ListItemSize = LocalListItemSize.current ?: ListItemSize.Default,
    layout: SlotRowLayout = LocalListItemLayout.current,
    interactionSource: MutableInteractionSource? = null, onClickLabel: String? = null,
    leading: (@Composable BoxScope.() -> Unit)? = null,
    bottom: (@Composable ColumnScope.() -> Unit)? = null)

fun ExpandableListItem(expanded: Boolean, onExpandedChange: (Boolean) -> Unit,
    title: @Composable () -> Unit, modifier: Modifier = Modifier,
    subtitle: (@Composable () -> Unit)? = null, overline: (@Composable () -> Unit)? = null,
    value: (@Composable () -> Unit)? = null, enabled: Boolean = true, selected: Boolean = false,
    nestingLevel: Int = 0,
    style: ListItemStyle = LocalListItemStyle.current ?: ListItemStyle.Default,
    size: ListItemSize = LocalListItemSize.current ?: ListItemSize.Default,
    layout: SlotRowLayout = LocalListItemLayout.current,
    interactionSource: MutableInteractionSource? = null, onClickLabel: String? = null,
    transition: ExpandableTransition = ExpandableTransition.Default,
    leading: (@Composable BoxScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit)
// `content` is the disclosed region; the row has no trailing or bottom slot.

fun List(modifier: Modifier = Modifier,
    itemStyle: ListItemStyle = LocalListItemStyle.current ?: ListItemStyle.Default,
    itemSize: ListItemSize = LocalListItemSize.current ?: ListItemSize.Default,
    dividerStyle: DividerStyle = LocalDividerStyle.current ?: DividerStyle.Default,
    dividerSize: DividerSize = LocalDividerSize.current ?: DividerSize.Default,
    listStyle: ListStyle = LocalListStyle.current ?: ListStyle.Default,
    listSize: ListSize = LocalListSize.current ?: ListSize.Default, separated: Boolean = true,
    enabled: Boolean = true, contentPadding: PaddingValues = PaddingValues(0.dp),
    content: ListScope.() -> Unit)

fun LazyList(modifier: Modifier = Modifier, state: LazyListState = rememberLazyListState(),
    itemStyle: ListItemStyle = LocalListItemStyle.current ?: ListItemStyle.Default,
    itemSize: ListItemSize = LocalListItemSize.current ?: ListItemSize.Default,
    dividerStyle: DividerStyle = LocalDividerStyle.current ?: DividerStyle.Default,
    dividerSize: DividerSize = LocalDividerSize.current ?: DividerSize.Default,
    listStyle: ListStyle = LocalListStyle.current ?: ListStyle.Default,
    listSize: ListSize = LocalListSize.current ?: ListSize.Default, separated: Boolean = true,
    enabled: Boolean = true, contentPadding: PaddingValues = PaddingValues(0.dp),
    content: ListScope.() -> Unit)
// Both: overload with a leading `entries: List<ListEntry>` and no `content`.

// ListScope, the receiver of `content`:
fun item(key: Any, title: (@Composable () -> Unit)? = null,
    subtitle: (@Composable () -> Unit)? = null, overline: (@Composable () -> Unit)? = null,
    value: (@Composable () -> Unit)? = null, enabled: Boolean = true, selected: Boolean = false,
    highlightSelected: Boolean = true, nestingLevel: Int = 0, style: ListItemStyle? = null,
    onClick: (() -> Unit)? = null, onClickLabel: String? = null,
    leading: (@Composable BoxScope.() -> Unit)? = null,
    trailing: (@Composable BoxScope.() -> Unit)? = null,
    bottom: (@Composable ColumnScope.() -> Unit)? = null,
    content: (@Composable BoxScope.() -> Unit)? = null)
fun divider(key: Any = "divider:${entries.size}", label: String? = null,
    style: DividerStyle? = null, size: DividerSize? = null)
```

### Settings

```kotlin
fun Settings(modifier: Modifier = Modifier, styles: SettingsStyles = SettingsDefaults.styles(),
    sizes: SettingsSizes = SettingsDefaults.sizes(),
    headerPlacement: SettingsHeaderPlacement = SettingsHeaderPlacement.Inside,
    content: @Composable SettingsScope.() -> Unit)

fun SettingsScope.Section(modifier: Modifier = Modifier, header: (@Composable () -> Unit)? = null,
    footer: (@Composable () -> Unit)? = null, enabled: Boolean = true, separated: Boolean = true,
    headerPlacement: SettingsHeaderPlacement = LocalSettingsHeaderPlacement.current,
    bandColor: Color = SettingsTheme.styles.section.band, band: (@Composable () -> Unit)? = null,
    content: @Composable SettingsSectionScope.() -> Unit)
// `band`: a strip at the bottom of the card for the section's closing action — one row, usually a
// SettingsRow in the matching tone; `bandColor = ArecaTheme.colors.bgDangerMuted` under a danger row.

fun SettingsRow(title: @Composable () -> Unit, modifier: Modifier = Modifier,
    subtitle: (@Composable () -> Unit)? = null, caption: (@Composable () -> Unit)? = null,
    value: (@Composable () -> Unit)? = null,
    badge: (@Composable () -> Unit)? = null, leading: (@Composable BoxScope.() -> Unit)? = null,
    trailing: (@Composable BoxScope.() -> Unit)? = null,
    bottom: (@Composable ColumnScope.() -> Unit)? = null,
    tone: SettingsRowTone = SettingsRowTone.Default,
    status: SettingsRowStatus = SettingsRowStatus.Idle, enabled: Boolean = true,
    selected: Boolean = false, nestingLevel: Int = 0, onClick: (() -> Unit)? = null,
    onClickLabel: String? = null, interactionSource: MutableInteractionSource? = null,
    style: ListItemStyle = SettingsTheme.styles.rowStyle(tone, interactive = onClick != null),
    size: ListItemSize = SettingsTheme.sizes.row,
    layout: SlotRowLayout = LocalListItemLayout.current)

fun SettingsToggleRow(checked: Boolean, onCheckedChange: (Boolean) -> Unit,
    title: @Composable () -> Unit, modifier: Modifier = Modifier,
    subtitle: (@Composable () -> Unit)? = null, badge: (@Composable () -> Unit)? = null,
    leading: (@Composable BoxScope.() -> Unit)? = null,
    bottom: (@Composable ColumnScope.() -> Unit)? = null,
    status: SettingsRowStatus = SettingsRowStatus.Idle, enabled: Boolean = true,
    nestingLevel: Int = 0, onClick: (() -> Unit)? = null, onClickLabel: String? = null,
    interactionSource: MutableInteractionSource? = null,
    style: ListItemStyle = SettingsTheme.styles.row, size: ListItemSize = SettingsTheme.sizes.row,
    layout: SlotRowLayout = LocalListItemLayout.current)
// Overload: `state: ToggleState`, `onToggle: (ToggleState) -> Unit` in place of `checked` /
// `onCheckedChange`, and `cycleOrder: List<ToggleState> = BinaryToggleCycle` after `onClickLabel`.

fun <T> SettingsSelectRow(title: @Composable () -> Unit, options: List<T>, selected: T,
    onSelect: (T) -> Unit, optionLabel: (T) -> String, modifier: Modifier = Modifier,
    subtitle: (@Composable () -> Unit)? = null, badge: (@Composable () -> Unit)? = null,
    leading: (@Composable BoxScope.() -> Unit)? = null,
    status: SettingsRowStatus = SettingsRowStatus.Idle, enabled: Boolean = true,
    nestingLevel: Int = 0, onClickLabel: String? = null,
    interactionSource: MutableInteractionSource? = null,
    style: ListItemStyle =
        SettingsTheme.styles.rowStyle(SettingsRowTone.Default, interactive = true),
    size: ListItemSize = SettingsTheme.sizes.row,
    layout: SlotRowLayout = LocalListItemLayout.current)
// SettingsRowStatus.Idle | Loading | Error(message); SettingsRowTone.Default | Brand | Danger.
// SettingsTextFieldRow(value, onValueChange, placeholder, …) and
// SettingsOptionRow(selected, onClick, title, …) complete the row set.

fun SettingsRowTile(modifier: Modifier = Modifier, shape: Shape = ArecaTheme.shapes.tile,
    size: Dp = SettingsTheme.sizes.iconTile.container, content: @Composable BoxScope.() -> Unit)
// Any visual — a flag, a photo, a brand mark — cropped to the icon tile, so a section's leading
// visuals share one size and shape.

fun SettingsRowLink(text: String, onClick: () -> Unit, modifier: Modifier = Modifier,
    enabled: Boolean = true, onClickLabel: String? = null)
// A second action in a pressable row's `trailing` slot ("Verify"): row ink, underlined, a 48dp
// target that overhangs the line instead of making the row taller.
```

### Layout

```kotlin
fun BaseLayout(modifier: Modifier = Modifier, bg: Color = Color.Transparent,
    contentWindowInsets: WindowInsets = WindowInsets(0, 0, 0, 0),
    topBar: @Composable () -> Unit = {}, bottomBar: @Composable () -> Unit = {},
    background: @Composable BoxScope.() -> Unit = {}, overlay: @Composable BoxScope.() -> Unit = {},
    content: @Composable (PaddingValues) -> Unit)

fun Card(modifier: Modifier = Modifier,
    style: CardStyle = LocalCardStyle.current ?: CardStyle.Default,
    size: CardSize = LocalCardSize.current ?: CardSize.Default, enabled: Boolean = true,
    active: Boolean = false, onClick: (() -> Unit)? = null, onClickLabel: String? = null,
    interactionSource: MutableInteractionSource? = null,
    footerStyle: CardFooterStyle = CardFooterStyle.Default,
    footer: (@Composable RowScope.() -> Unit)? = null,
    content: @Composable BoxScope.(padding: PaddingValues) -> Unit)
// Overload: leading `expanded: Boolean`, then `transition`, `footerStyle`, `footer`,
// `expandedContent` and `content`, both `@Composable ColumnScope.(padding: PaddingValues) -> Unit`.
// CardFooterStyle presets: Default (= plain()), plain(divider), soft(ActionColor = Neutral),
// solid(ActionColor = Brand), of(bg, content, divider, button) for a color of your own. The footer
// hands its `content` color and its `button` style to what is inside it.

fun HorizontalDivider(modifier: Modifier = Modifier,
    style: DividerStyle = LocalDividerStyle.current ?: DividerStyle.Default,
    size: DividerSize = LocalDividerSize.current ?: DividerSize.Default, text: String? = null,
    textPosition: DividerTextPosition = DividerTextPosition.Center)
// VerticalDivider takes the same parameters.
```

`BaseLayout` lays `content` out at full size under the bars; `content` must apply the padding it is
given (a `LazyColumn`'s `contentPadding`, or `Modifier.padding`). The top bar's height and
`contentWindowInsets` add up, so give the status-bar inset to the top bar and the rest to
`contentWindowInsets` (§6). A `Card`'s content is handed its padding rather than padded for it.

### Bars and tabs

```kotlin
fun TopBar(title: String, modifier: Modifier = Modifier, subtitle: String? = null,
    style: TopBarStyle = LocalTopBarStyle.current ?: TopBarStyle.Default,
    size: TopBarSize = LocalTopBarSize.current ?: TopBarSize.Default,
    titleAlignment: TopBarTitleAlignment = TopBarTitleAlignment.Start,
    layout: SlotRowLayout = titleAlignment.slotRowLayout(size.gap),
    windowInsets: WindowInsets = WindowInsets(0, 0, 0, 0), scrollState: TopBarScrollState? = null,
    leading: (@Composable TopBarActionScope.() -> Unit)? = null,
    trailing: (@Composable TopBarActionScope.() -> Unit)? = null,
    bottom: (@Composable () -> Unit)? = null)
// Overload: no `title` / `subtitle`; a trailing `content: @Composable () -> Unit` instead.

fun LargeTopBar(title: String, scrollState: TopBarScrollState, modifier: Modifier = Modifier,
    subtitle: String? = null, style: TopBarStyle = LocalTopBarStyle.current ?: TopBarStyle.Default,
    size: TopBarSize = LocalTopBarSize.current ?: TopBarSize.Default,
    windowInsets: WindowInsets = WindowInsets(0, 0, 0, 0),
    leading: (@Composable TopBarActionScope.() -> Unit)? = null,
    trailing: (@Composable TopBarActionScope.() -> Unit)? = null,
    bottom: (@Composable () -> Unit)? = null)

fun rememberTopBarScrollState(
    behavior: TopBarScrollBehavior = TopBarScrollBehavior.Pinned): TopBarScrollState
// TopBarScrollBehavior.Pinned | EnterAlways | ExitUntilCollapsed. Attach
// `scrollState.nestedScrollConnection` to the scroller with `Modifier.nestedScroll`.

fun <T : Any> TabBar(items: List<TabBarItem<T>>, selectedValue: T?, onValueSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    style: TabBarStyle = LocalTabBarStyle.current ?: TabBarStyle.Default,
    size: TabBarSize = LocalTabBarSize.current ?: TabBarSize.Default,
    windowInsets: WindowInsets =
        WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom))

fun <T : Any> FloatingTabBar(items: List<TabBarItem<T>>, selectedValue: T?,
    onValueSelected: (T) -> Unit, modifier: Modifier = Modifier,
    style: FloatingTabBarStyle = LocalFloatingTabBarStyle.current ?: FloatingTabBarStyle.Default,
    size: FloatingTabBarSize = LocalFloatingTabBarSize.current ?: FloatingTabBarSize.Default,
    indicatorStyle: FloatingTabBarIndicatorStyle = FloatingTabBarIndicatorStyle.underline(),
    windowInsets: WindowInsets =
        WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom),
    animationSpec: FiniteAnimationSpec<Dp> =
        ArecaTheme.motion.medium.tween(ArecaTheme.motion.standard))
// Both: overload with `state: TabBarState<T>` in place of `selectedValue` / `onValueSelected`.
// TabBarItem(value: T, enabled: Boolean = true, content: @Composable TabBarItemScope.() -> Unit)

fun <T> Tabs(tabs: List<TabItem<T>>, selectedValue: T, onValueSelected: (T) -> Unit,
    modifier: Modifier = Modifier, style: TabsStyle = LocalTabsStyle.current ?: TabsStyle.Default,
    size: TabsSize = LocalTabsSize.current ?: TabsSize.Default,
    variant: TabsVariant = TabsVariant.Underline,
    orientation: TabsOrientation = TabsOrientation.Horizontal,
    horizontalPosition: TabsHorizontalPosition = TabsHorizontalPosition.Bottom,
    verticalPosition: TabsVerticalPosition = TabsVerticalPosition.Start)
// Overload: `state: TabsState<T>` (from `rememberTabsState(initialValue)`) in place of
// `selectedValue` / `onValueSelected`.
// TabItem(value: T, enabled: Boolean = true, content: @Composable TabItemScope.() -> Unit)
```

Bar controls are `TopBarActionScope` extensions for `leading` and `trailing`. Each takes `onClick`
first (`FavoriteAction` takes `selected` before it; `IconAction` and `TextAction` take their icon or
text first), then `modifier`, a localisable `contentDescription` or `text`, `enabled` and
`interactionSource`. The other parameters differ per control:

| Control | Other parameters and English default |
| --- | --- |
| `BackAction` | `label: String? = null`, `contentDescription = "Back"`, `color = ActionColor.Neutral` |
| `CloseAction`, `OverflowAction`, `AddAction` | `contentDescription = "Close"` / `"More"` / `"Add"`, `color = ActionColor.Neutral` |
| `MenuAction` | `contentDescription = "Menu"`, `color = ActionColor.Neutral`, `badge: String? = null` |
| `NotificationsAction`, `FilterAction` | `badge: String? = null`, `contentDescription = "Notifications"` / `"Filters"` |
| `SearchAction`, `EditAction`, `SettingsAction` | `contentDescription = "Search"` / `"Edit"` / `"Settings"` |
| `ShareAction`, `DownloadAction` | `contentDescription = "Share"` / `"Download"`, `loading = false` |
| `DeleteAction` | `contentDescription = "Delete"`, `color = ActionColor.Danger`, `loading = false` |
| `FavoriteAction(selected, onClick, …)` | `contentDescription = "Favorite"`, `selectedColor = ActionColor.Danger` |
| `DoneAction`, `CancelAction` | `text = "Done"` / `"Cancel"`; `DoneAction` also `loading = false` |
| `IconAction(icon, contentDescription, onClick, …)` | `color = ActionColor.Neutral`, `loading = false`, `badge: String? = null` |
| `TextAction(text, onClick, …)` | `color = ActionColor.Brand`, `loading = false`, `leading`, `trailing` |
| `AvatarAction` | `letters: String? = null`, `contentDescription = "Account"`, `loading = false`, `content` |

Controls without a `color` parameter draw in `ActionColor.Neutral`, except `DoneAction`
(`ActionColor.Brand`) and a selected `FavoriteAction` (`selectedColor`).

### Overlays

```kotlin
fun Dialog(onDismissRequest: () -> Unit, modifier: Modifier = Modifier,
    style: ModalStyle = LocalModalStyle.current ?: ModalStyle.Default,
    size: ModalSize = LocalModalSize.current ?: ModalSize.Default,
    dismissOnOverlayClick: Boolean = true, dismissOnBackPress: Boolean = true,
    dismissOnClickOutside: Boolean = true, overlayContentDescription: String = "Dismiss",
    header: (@Composable () -> Unit)? = null, footer: (@Composable () -> Unit)? = null,
    overlay: @Composable BoxScope.() -> Unit = {}, content: @Composable () -> Unit)

fun ConfirmationDialog(confirmText: @Composable () -> Unit, onConfirm: suspend () -> Unit,
    modifier: Modifier = Modifier, onDecline: (suspend () -> Unit)? = null,
    onDismissRequest: () -> Unit = {}, onFailure: (Exception) -> Unit = {},
    style: ConfirmationDialogStyle =
        LocalConfirmationDialogStyle.current ?: ConfirmationDialogStyle.Default,
    size: ConfirmationDialogSize =
        LocalConfirmationDialogSize.current ?: ConfirmationDialogSize.Default,
    leading: (@Composable ColumnScope.() -> Unit)? = null, title: (@Composable () -> Unit)? = null,
    text: (@Composable () -> Unit)? = null,
    extraContent: (@Composable ColumnScope.() -> Unit)? = null,
    declineText: (@Composable () -> Unit)? = null,
    footer: (@Composable ColumnScope.() -> Unit)? = null)

fun rememberFlowModal(allowPartial: Boolean = true, allowExpand: Boolean = true,
    allowDrag: Boolean = true, partialMultiplier: Float = 0.5f,
    @FloatRange(from = 0.0, to = 1.0) maxHeightFraction: Float = 0.9f, autoHeight: Boolean = false,
    dialogProperties: DialogProperties = flowModalDialogProperties(),
    animationSpec: AnimationSpec<Float>? = null): FlowModalController

fun FlowModal(controller: FlowModalController, modifier: Modifier = Modifier,
    style: FlowModalStyle = LocalFlowModalStyle.current ?: FlowModalStyle.Default,
    size: FlowModalSize = LocalFlowModalSize.current ?: FlowModalSize.Default,
    onDismissRequest: () -> Unit = { controller.hide() },
    overlayContentDescription: String = "Dismiss", header: (@Composable () -> Unit)? = null,
    footer: (@Composable () -> Unit)? = null, content: @Composable () -> Unit)
// FlowModalController: show(partial: Boolean = true), expand(), collapse(), hide(), isVisible.
// FlowModal composes nothing while hidden, so call it unconditionally.

fun FlowModalActionBar(confirmText: String, onConfirm: () -> Unit, modifier: Modifier = Modifier,
    enabled: Boolean = true, loading: Boolean = false,
    style: FlowModalActionBarStyle =
        LocalFlowModalActionBarStyle.current ?: FlowModalActionBarStyle.Default,
    size: FlowModalActionBarSize =
        LocalFlowModalActionBarSize.current ?: FlowModalActionBarSize.Default,
    leading: (@Composable () -> Unit)? = null)

fun ConfirmSheet(visible: Boolean, icon: ImageVector?, title: String, description: String?,
    confirmText: String, onConfirm: () -> Unit, onDismiss: () -> Unit,
    modifier: Modifier = Modifier, note: String? = null,
    noteColor: Color = ArecaTheme.colors.fgSubtle, confirming: Boolean = false,
    confirmEnabled: Boolean = true, cancelText: String = "Cancel",
    style: ConfirmSheetStyle = LocalConfirmSheetStyle.current ?: ConfirmSheetStyle.Default,
    size: ConfirmSheetSize = LocalConfirmSheetSize.current ?: ConfirmSheetSize.Default,
    extra: (@Composable ColumnScope.() -> Unit)? = null)

fun rememberDropdownPopup(initialOpen: Boolean = false,
    placement: AnchoredPlacement = AnchoredPlacement.BelowStart,
    anchorState: AnchoredState = rememberAnchoredState()): DropdownPopupState

fun DropdownPopup(state: DropdownPopupState, onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    style: DropdownPopupStyle = LocalDropdownPopupStyle.current ?: DropdownPopupStyle.Default,
    size: DropdownPopupSize = LocalDropdownPopupSize.current ?: DropdownPopupSize.Default,
    dismissOnBackPress: Boolean = true, dismissOnClickOutside: Boolean = true,
    clippingEnabled: Boolean = true, content: @Composable BoxScope.() -> Unit)
// DropdownPopupState: isOpen, show(), dismiss(), toggle(), anchorState.
// Put `Modifier.anchored(state.anchorState)` on the trigger.
```

### Feedback

```kotlin
fun ToastHost(state: ToastState, modifier: Modifier = Modifier,
    animation: ToastAnimation = ToastAnimation.Slide,
    style: ToastStyle = LocalToastStyle.current ?: ToastStyle.Default,
    windowInsets: WindowInsets = WindowInsets.safeDrawing)
// rememberToastState(): ToastState. ToastHost fills its parent and keeps clear of the insets:
// put it in BaseLayout(overlay = { … }).
// ToastState: show(...): Long, dismiss(id: Long), dismiss(), dismissAll().
fun show(label: CharSequence, caption: CharSequence? = null, color: ToastColor? = null,
    shape: Shape? = null, duration: Duration? = 2.seconds,
    alignment: Alignment.Vertical = Alignment.Top,
    leading: (@Composable RowScope.(id: Long) -> Unit)? = null,
    trailing: (@Composable RowScope.(id: Long) -> Unit)? = null,
    onClick: ((id: Long) -> Unit)? = null): Long

fun Notice(title: String?, modifier: Modifier = Modifier, icon: ImageVector? = null,
    tone: NoticeTone = NoticeTone.Neutral,
    emphasis: NoticeEmphasis =
        if (tone == NoticeTone.Danger) { NoticeEmphasis.Filled } else { NoticeEmphasis.Subtle },
    busy: Boolean = false,
    style: NoticeStyle = LocalNoticeStyle.current ?: NoticeStyle.of(tone, emphasis),
    size: NoticeSize = LocalNoticeSize.current ?: NoticeSize.Default,
    content: @Composable ColumnScope.() -> Unit = {})
// NoticeTone.Neutral | Info | Success | Warning | Danger. Lines: NoticeText(text).
// LocalNoticeStyle is null by default, so each notice follows its tone; providing a style gives
// every notice in the subtree that one look.

fun Fallback(modifier: Modifier = Modifier,
    style: FallbackStyle = LocalFallbackStyle.current ?: FallbackStyle.Default,
    size: FallbackSize = LocalFallbackSize.current ?: FallbackSize.Default,
    top: (@Composable () -> Unit)? = null, heading: (@Composable () -> Unit)? = null,
    body: (@Composable () -> Unit)? = null, content: (@Composable () -> Unit)? = null)

fun StatusScene(title: String, modifier: Modifier = Modifier,
    mark: (@Composable () -> Unit)? = null, message: String? = null, footnote: String? = null,
    actions: (@Composable () -> Unit)? = null, note: String? = null, stagedEntrance: Boolean = true,
    style: StatusSceneStyle = LocalStatusSceneStyle.current ?: StatusSceneStyle.Default,
    size: StatusSceneSize = LocalStatusSceneSize.current ?: StatusSceneSize.Default)
// mark: StatusMark(state = StatusMarkState.Working() | Success | Stopped(icon), contentDescription)
// actions: StatusSceneActions(primary, onPrimary, …)

fun Skeleton(modifier: Modifier = Modifier, count: Int = 1,
    arrangement: SkeletonArrangement = SkeletonArrangement.Lines, width: Dp? = null,
    height: Dp? = null, textStyle: TextStyle? = null, contentDescription: String? = null,
    animated: Boolean = true, reversed: Boolean = false,
    style: SkeletonStyle = LocalSkeletonStyle.current ?: SkeletonStyle.Default,
    size: SkeletonSize = LocalSkeletonSize.current ?: SkeletonSize.Default)

fun Spinner(modifier: Modifier = Modifier,
    style: SpinnerStyle = LocalSpinnerStyle.current ?: SpinnerStyle.Default,
    size: SpinnerSize = LocalSpinnerSize.current ?: SpinnerSize.Default,
    contentDescription: String? = null, period: Duration = 900.milliseconds)

fun ProgressBar(progress: Float?, modifier: Modifier = Modifier, label: CharSequence? = null,
    labelPosition: RelativePosition = RelativePosition.BottomCenter,
    contentDescription: String? = null,
    style: ProgressBarStyle = LocalProgressBarStyle.current ?: ProgressBarStyle.Default,
    size: ProgressBarSize = LocalProgressBarSize.current ?: ProgressBarSize.Default)
// progress = null is indeterminate.

fun Badge(modifier: Modifier = Modifier,
    style: BadgeStyle = LocalBadgeStyle.current ?: BadgeStyle.Default,
    size: BadgeSize = LocalBadgeSize.current ?: BadgeSize.Default,
    shape: Shape = ArecaTheme.shapes.full, soft: Boolean = false, onClick: (() -> Unit)? = null,
    enabled: Boolean = true, interactionSource: MutableInteractionSource? = null,
    leading: (@Composable () -> Unit)? = null, trailing: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit)

fun Avatar(modifier: Modifier = Modifier,
    style: AvatarStyle = LocalAvatarStyle.current ?: AvatarStyle.Default,
    size: AvatarSize = LocalAvatarSize.current ?: AvatarSize.Default,
    shape: Shape = ArecaTheme.shapes.full, letters: String? = null,
    contentDescription: String? = null, loading: Boolean = false,
    content: (@Composable BoxScope.() -> Unit)? = null)
```

### Collections and tables

```kotlin
fun <T> ContentList(state: LimitedContentListState<T>, modifier: Modifier = Modifier,
    style: ContentListStyle = LocalContentListStyle.current ?: ContentListStyle.Default,
    size: ContentListSize = LocalContentListSize.current ?: ContentListSize.Default,
    labels: ContentListLabels = ContentListLabels.English, fillAvailableHeight: Boolean = false,
    onRetryInitial: () -> Unit = {},
    loadingContent: @Composable () -> Unit = { ContentListDefaults.LoadingState() },
    emptyContent: @Composable () -> Unit =
        { ContentListDefaults.EmptyState(title = labels.emptyTitle) },
    initialErrorContent: @Composable (String?, () -> Unit) -> Unit = { message, retry ->
        ContentListDefaults.ErrorState(message = message, onRetry = retry,
            title = labels.errorTitle, actionText = labels.retry) },
    header: (@Composable ColumnScope.() -> Unit)? = null, itemContent: @Composable (T) -> Unit)

fun <T> PagedContentList(state: PagedContentListState<T>, modifier: Modifier = Modifier,
    style: ContentListStyle = LocalContentListStyle.current ?: ContentListStyle.Default,
    size: ContentListSize = LocalContentListSize.current ?: ContentListSize.Default,
    labels: ContentListLabels = ContentListLabels.English,
    contentPadding: PaddingValues = PaddingValues(ArecaTheme.spacing.none),
    fillAvailableHeight: Boolean = true, listState: LazyListState = rememberLazyListState(),
    onRefresh: (() -> Unit)? = null,
    onLoadNextPage: () -> Unit = {}, onRetryInitial: () -> Unit = onRefresh ?: {},
    onRetryLoadMore: () -> Unit = onLoadNextPage, itemKey: ((T) -> Any)? = null,
    loadingContent: @Composable () -> Unit = { ContentListDefaults.LoadingState() },
    emptyContent: @Composable () -> Unit =
        { ContentListDefaults.EmptyState(title = labels.emptyTitle) },
    initialErrorContent: @Composable (String?, () -> Unit) -> Unit = { message, retry ->
        ContentListDefaults.ErrorState(message = message, onRetry = retry,
            title = labels.errorTitle, actionText = labels.retry) },
    loadMoreContent: @Composable () -> Unit = { ContentListDefaults.LoadMoreState() },
    loadMoreErrorContent: @Composable (String, () -> Unit) -> Unit = { message, retry ->
        ContentListDefaults.LoadMoreErrorState(message = message, onRetry = retry,
            title = labels.loadMoreErrorTitle, actionText = labels.retry) },
    endReachedContent: @Composable () -> Unit =
        { ContentListDefaults.EndReachedState(text = labels.endReached) },
    header: (@Composable ColumnScope.() -> Unit)? = null, itemContent: @Composable (T) -> Unit)
// LimitedContentListState(items, loadingInitial = false, refreshing = false, initialError = null)
// PagedContentListState(items, hasNextPage, loadingNextPage = false, loadMoreError = null,
//     loadingInitial = false, refreshing = false, initialError = null)

fun <T> Table(items: List<T>, columns: List<TableColumn<T>>, modifier: Modifier = Modifier,
    style: TableStyle = LocalTableStyle.current ?: TableStyle.Default,
    size: TableSize = LocalTableSize.current ?: TableSize.Default, separated: Boolean = true)
// LazyTable adds `key: ((index: Int, item: T) -> Any)? = null`.
// TableColumn(id: String, title: String, width: Dp? = null, minWidth: Dp = 80.dp,
//     maxWidth: Dp? = null, expandWeight: Float = 1f, value: (T) -> Any? = { "" },
//     cell: (@Composable (T) -> Unit)? = null, header: (@Composable () -> Unit)? = null)
```

### Theme

```kotlin
fun ArecaThemeProvider(darkTheme: Boolean = false, baseColors: ArecaBaseColors = ArecaBaseColors(),
    colors: ArecaColors =
        if (darkTheme) { ArecaColors.dark(baseColors) } else { ArecaColors.light(baseColors) },
    radius: ArecaRadius = ArecaRadius(),
    shapes: ArecaShapes = ArecaShapes.from(radius), spacing: ArecaSpacing = ArecaSpacing(),
    elevation: ArecaElevation = ArecaElevation(), motion: ArecaMotion = ArecaMotion(),
    fonts: ArecaFonts = ArecaFonts(),
    baseTypography: ArecaBaseTypography = ArecaBaseTypography.from(fonts),
    typography: ArecaTypography = ArecaTypography.from(baseTypography),
    content: @Composable () -> Unit)

fun ArecaDarkTheme(content: @Composable () -> Unit)
```

---

## 6. Recipes

Each recipe compiles against foundation and icons. The first lists its imports in full; the others
take theirs from the packages in §3 plus the usual Compose ones. Literal strings stand in for the
app's string resources, and screen state would normally come from a view model.

### Settings screen

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

### Form with validation

`FieldGroup` owns the status: an `error` turns the field red, shows the message and announces it.

```kotlin
@Composable
fun SignUpForm(
    submitting: Boolean,
    serverError: String?,
    onSubmit: (email: String, password: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var attempted by rememberSaveable { mutableStateOf(false) }

    val emailError = "Enter an email address like name@example.com."
        .takeIf { attempted && "@" !in email }
    val passwordError = "Use at least 8 characters.".takeIf { attempted && password.length < 8 }
    val submit = {
        attempted = true
        if ("@" in email && password.length >= 8) onSubmit(email, password)
    }

    Column(
        modifier = modifier.contentInsets(),
        verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl),
    ) {
        Heading(
            title = { Text("Create your account") },
            supportive = { Text("You can change these later in Settings.") },
        )
        if (serverError != null) {
            Notice(title = null, tone = NoticeTone.Danger) { NoticeText(serverError) }
        }
        FieldGroup(label = "Email", required = true, error = emailError) { field ->
            TextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                field = field,
                singleLine = true,
                placeholder = { Text("name@example.com") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                ),
            )
        }
        FieldGroup(
            label = "Password",
            required = true,
            assistive = "At least 8 characters.",
            error = passwordError,
        ) { field ->
            PasswordField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                field = field,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(onDone = { submit() }),
                revealContentDescription = "Show password",
                hideContentDescription = "Hide password",
            )
        }
        Button(
            text = "Create account",
            onClick = submit,
            modifier = Modifier.fillMaxWidth(),
            loading = submitting,
            loadingStateDescription = "Creating account",
        )
    }
}
```

### List screen with refresh, empty and error states

`PagedContentList` shows the loading face, the error face with retry, the empty face or the items,
requests pages near the end, and adds pull-to-refresh when `onRefresh` is set. `contentPadding`
takes the layout's padding, so the rows scroll under the bars and rest clear of them.

```kotlin
data class Invoice(val id: String, val customer: String, val due: String, val amount: String)

@Composable
fun InvoicesScreen(
    state: PagedContentListState<Invoice>,
    onRefresh: () -> Unit,
    onLoadNextPage: () -> Unit,
    onOpen: (Invoice) -> Unit,
    onCreate: () -> Unit,
) {
    BaseLayout(
        bg = ArecaTheme.colors.bgCanvas,
        contentWindowInsets = WindowInsets.safeDrawing.only(
            WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal,
        ),
        topBar = {
            TopBar(
                title = "Invoices",
                windowInsets = WindowInsets.safeDrawing.only(
                    WindowInsetsSides.Top + WindowInsetsSides.Horizontal,
                ),
                trailing = { AddAction(onClick = onCreate, contentDescription = "New invoice") },
            )
        },
    ) { padding ->
        PagedContentList(
            state = state,
            contentPadding = padding,
            labels = ContentListLabels(
                emptyTitle = "No invoices yet",
                errorTitle = "Couldn’t load invoices",
                retry = "Try again",
                loadMoreErrorTitle = "Couldn’t load more invoices",
                endReached = "That’s every invoice",
            ),
            onRefresh = onRefresh,
            onLoadNextPage = onLoadNextPage,
            itemKey = { it.id },
            emptyContent = {
                Fallback(
                    top = { FeaturedIcon(ArecaIcons.Inbox, contentDescription = null) },
                    heading = { Text("No invoices yet") },
                    body = { Text("Invoices you send appear here.") },
                    content = { Button(text = "New invoice", onClick = onCreate) },
                )
            },
        ) { invoice ->
            NavigationListItem(
                title = { Text(invoice.customer) },
                onClick = { onOpen(invoice) },
                subtitle = { Text(invoice.due) },
                value = { Text(invoice.amount) },
                onClickLabel = "Open invoice",
            )
        }
    }
}
```

For a list outside `ContentList`, wrap the scroller in
`PullToRefreshLayout(isRefreshing, onRefresh) { … }` (opt in to `ExperimentalArecaApi`) and show
`Fallback` yourself when the screen's state says the content is missing or failed.

### Confirmation flow

The sheet's visibility is the caller's state. Answer `onConfirm` by setting `confirming`, then clear
`visible` on success or `confirming` on failure; the sheet takes no further press until one of them
changes. The toast host sits in `BaseLayout`'s overlay.

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

### Tabbed screen

`TabBar` switches destinations and applies the bottom inset itself; `Tabs` switches views inside
one.

```kotlin
enum class Destination(val label: String, val icon: ImageVector) {
    Home("Home", ArecaIcons.Home),
    Search("Search", ArecaIcons.Search),
    Account("Account", ArecaIcons.User),
}

enum class Feed(val label: String) { Following("Following"), Popular("Popular") }

@Composable
fun MainScreen() {
    var destination by rememberSaveable { mutableStateOf(Destination.Home) }

    BaseLayout(
        bg = ArecaTheme.colors.bgCanvas,
        topBar = {
            TopBar(
                title = destination.label,
                windowInsets = WindowInsets.safeDrawing.only(
                    WindowInsetsSides.Top + WindowInsetsSides.Horizontal,
                ),
            )
        },
        bottomBar = {
            TabBar(
                items = Destination.entries.map { entry ->
                    TabBarItem(value = entry) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(entry.icon, contentDescription = null)
                            Text(entry.label, variant = TextVariant.LabelSmall, maxLines = 1)
                        }
                    }
                },
                selectedValue = destination,
                onValueSelected = { destination = it },
                modifier = Modifier.fillMaxWidth(),
            )
        },
    ) { padding ->
        when (destination) {
            Destination.Home -> HomeTab(Modifier.padding(padding))
            Destination.Search -> Text("Search", modifier = Modifier.padding(padding))
            Destination.Account -> Text("Account", modifier = Modifier.padding(padding))
        }
    }
}

@Composable
private fun HomeTab(modifier: Modifier = Modifier) {
    val feed = rememberTabsState(Feed.Following)

    Column(modifier) {
        Tabs(
            tabs = Feed.entries.map { entry -> TabItem(value = entry) { Text(entry.label) } },
            state = feed,
            modifier = Modifier.fillMaxWidth(),
        )
        when (feed.selectedValue) {
            Feed.Following -> Text("Posts from people you follow")
            Feed.Popular -> Text("Popular posts")
        }
    }
}
```

### Smaller pieces

A sheet with its own action bar, and a menu on an icon button:

```kotlin
@Composable
fun FilterSheet(onApply: () -> Unit) {
    val modal = rememberFlowModal(allowPartial = false, autoHeight = true)
    var unpaidOnly by remember { mutableStateOf(false) }

    Button(text = "Filters", onClick = { modal.show() })

    FlowModal(
        controller = modal,
        footer = {
            FlowModalActionBar(
                confirmText = "Apply",
                onConfirm = {
                    onApply()
                    modal.hide()
                },
            )
        },
    ) {
        CheckboxListItem(
            checked = unpaidOnly,
            onCheckedChange = { unpaidOnly = it },
            title = { Text("Unpaid only") },
        )
    }
}
```

```kotlin
@Composable
fun MoreMenu(onRename: () -> Unit, onArchive: () -> Unit) {
    val menu = rememberDropdownPopup()

    IconButton(
        imageVector = ArecaIcons.DotsVertical,
        contentDescription = "More actions",
        onClick = { menu.toggle() },
        modifier = Modifier.anchored(menu.anchorState),
        style = IconButtonStyle.plain(ActionColor.Neutral),
    )
    DropdownPopup(state = menu, onDismissRequest = { menu.dismiss() }) {
        Column {
            ListItem(
                title = { Text("Rename") },
                onClick = {
                    menu.dismiss()
                    onRename()
                },
            )
            ListItem(
                title = { Text("Archive") },
                onClick = {
                    menu.dismiss()
                    onArchive()
                },
            )
        }
    }
}
```

---

## 7. Anti-patterns

| Don't | Do |
| --- | --- |
| `androidx.compose.material3.*` (`Button`, `Scaffold`, `TopAppBar`, `ModalBottomSheet`, `Snackbar`, `Switch`) | `Button`, `BaseLayout`, `TopBar`, `FlowModal` / `ConfirmSheet`, `ToastHost`, `Toggle` |
| `BasicText` / `Text(style = TextStyle(fontSize = 14.sp))` | `Text(text, variant = TextVariant.BodySmall)` |
| `Color(0xFF…)`, `MaterialTheme.colorScheme.*` | `ArecaTheme.colors.*`, `TextColor.*`, `ActionColor.*` |
| `Modifier.padding(16.dp)` around a screen | `Modifier.horizontalContentInsets()`, `contentInsets()`, `ArecaTheme.spacing.*` |
| `tween(300)` | `ArecaTheme.motion.slow.tween(ArecaTheme.motion.standard)` |
| `Modifier.clickable { }` on a `Box` | `Card(onClick = …)`, `ListItem(onClick = …)`, or the custom surface below |
| `Modifier.clip(shape).clickable(…)` | `Modifier.background(color, shape).clickable(indication = TouchIndication(shape = shape), …)` |
| Two `ButtonStyle.solid()` buttons on one screen | One solid; the rest `soft`, `outlined` or `plain` |
| `Checkbox` next to a bare `Text` | `CheckboxListItem(checked, onCheckedChange, title = { Text(…) })` |
| `IconButton(…, contentDescription = null)` | A localised description |
| Relying on `"Back"`, `"Cancel"`, `ContentListLabels.English` in a localised app | Pass the app's strings |
| `ArecaThemeProvider(darkTheme = true)` nested for a dark region | `ArecaDarkTheme { … }` |
| `if (open) FlowModal(…)` | Call `FlowModal` always; drive it with `show()` / `hide()` |
| `if (asking) ConfirmSheet(…)` | `ConfirmSheet(visible = asking, …)`, so it animates out |
| A wrapper composable that only changes a component's colours | `style = XStyle.….copy(…)` or `LocalXStyle provides …` |
| Ignoring `BaseLayout`'s `padding` | Apply it as `contentPadding` or `Modifier.padding(padding)` |
| A `Row` of `Text` and `Icon` pretending to be a list row | `ListItem(title, leading, trailing, onClick)` |
| `subtitle = { Column { Text(a, color = …); Text(b) } }` | `subtitle = { Text(a) }, caption = { Text(b) }` |
| `title = { Text(name, fontWeight = FontWeight.SemiBold) }` in a row | `title = { Text(name) }`; the row owns its type |
| `Modifier.padding(top = 2.dp)` on a row's icon to line it up | Nothing: rows align by scenario (`SlotRowLayout.Auto`); pick `.Top`, `.Center` or `.FirstLine` if the scenario differs |
| A flag or a photo beside icon tiles at its own size | `SettingsRowTile { … }`, the tile's size and shape |
| `shape =` passed to one card on a screen | One corner per nesting level, set once through the card style |
| A danger row behind a hairline at the bottom of a section | `Section(band = { SettingsRow(tone = Danger, …) }, bandColor = bgDangerMuted)` |
| A `Card` with a `HorizontalDivider` and a `Row` of buttons at the bottom | `Card(footer = { … }, footerStyle = …)` |
| `forEach` over an open-ended collection inside a `Section` | `LazyList` / `PagedContentList` of `ListItem` rows, or ask which the product wants |
| `Box(Modifier.height(1.dp).background(…))` | `HorizontalDivider()` |

A custom pressable surface, when no component fits:

```kotlin
@Composable
fun Tile(label: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val shape = ArecaTheme.shapes.lg
    Box(
        modifier = modifier
            .background(ArecaTheme.colors.bgSurface, shape)
            .clickCursor()
            .clickable(
                interactionSource = null,
                indication = TouchIndication(shape = shape),
                role = Role.Button,
                onClick = onClick,
            )
            .padding(ArecaTheme.spacing.xl),
    ) {
        Text(label)
    }
}
```

---

## 8. Where to verify

- **KDoc.** Every public declaration in `ui/foundation/src/commonMain` is documented (explicit API
  mode), including every parameter, its default and the contract behind it. Read the KDoc of the
  component, its `XStyle` and its `XSize` before overriding anything.
- **Playground.** Each family has showcases in
  `playground/src/commonMain/kotlin/dev/voir/areca/playground/foundation/<family>/`
  (`ButtonShowcase.kt`, `SettingsShowcase.kt`, `ConfirmSheetShowcase.kt`, …): a playground with live
  controls, the variants and the edge cases. Run it with `./gradlew :playground:run`. The authoring
  contract is [playground/SHOWCASES.md](../../playground/SHOWCASES.md).
- **Compile.** `./gradlew :playground:compileKotlinJvm` type-checks a screen quickly when it lives
  in the playground; an app compiles its own module.
- **Flows.** `flow/auth`, `flow/profile` and `flow/attachments` are complete screens built from
  these rules; their READMEs show larger compositions.
