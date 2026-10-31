---
name: areca-ui-v1
description: Areca UI 1.x conventions for building Compose Multiplatform screens with dev.voir.areca:foundation and its charts, crop, media, datetime, keyboard, icons and flow modules — theme, tokens, component choice, customisation, accessibility and localisation. Use when writing, reviewing or prototyping Compose Multiplatform UI .kt code in an app that depends on dev.voir.areca:foundation or another Areca UI artifact.
---

# Areca UI Guidelines

> **Audience:** Compose Multiplatform UI developers and coding agents
> **Library version:** the latest `dev.voir.areca` release of `foundation`, `charts`, `crop`,
> `media`, `datetime`, `keyboard`, `icons`, `flow-auth`, `flow-profile`, `flow-attachments` and `flow-gallery`, every module
> at the same version

Areca is Voir's Compose Multiplatform design system for Android, iOS and the JVM: theme tokens and
the components built on them (`foundation`), plus feature modules for charts, cropping, pictures
and carousels, dates, an on-screen keyboard, icons, and authentication, profile, attachment and
gallery screens. It draws and
reports intents; apps own state, validation, wording, networking, navigation and image loading.

Apply `areca:areca-viewmodel-v1` for the presentation state a screen renders. This skill covers
what is drawn and how.

## Set up an app once

Areca is published to GitHub Packages, not Maven Central. Add its repository to
`settings.gradle.kts`, limited to the Areca group:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        exclusiveContent {
            forRepository {
                maven("https://maven.pkg.github.com/VoirDev/areca-ui") {
                    name = "Areca"
                    credentials(PasswordCredentials::class)
                }
            }
            filter { includeGroup("dev.voir.areca") }
        }
    }
}
```

GitHub Packages asks for credentials even for public packages: the Gradle properties
`ArecaUsername` (a GitHub user name) and `ArecaPassword` (a classic personal access token with
`read:packages`), set in `~/.gradle/gradle.properties` or, on CI, as
`ORG_GRADLE_PROJECT_ArecaUsername` and `ORG_GRADLE_PROJECT_ArecaPassword`. Never commit them.

```kotlin
commonMain.dependencies {
    implementation("dev.voir.areca:foundation:<version>")
    implementation("dev.voir.areca:icons:<version>") // only if the app draws ArecaIcons.*
}
```

```kotlin
@Composable
fun App() {
    ArecaThemeProvider(darkTheme = isSystemInDarkTheme()) {
        Box(Modifier.fillMaxSize().background(ArecaTheme.colors.bgCanvas)) {
            AppNavigation()
        }
    }
}
```

- Mount exactly one `ArecaThemeProvider`, at the root; every component and module reads tokens and
  component defaults from it (without it, the light library defaults — fine for previews). Pass
  `darkTheme` from `isSystemInDarkTheme()` or the user's setting; give the root `bgCanvas`.
- Re-theme only through the provider's parameters, at the highest level that says what you mean:
  `baseColors` (both directions), `radius`, `spacing`, `elevation`, `motion`, `fonts`; `colors`,
  `shapes` and `typography` derive from them ([reference/theme.md](reference/theme.md)).
- Change an app-wide component default by providing its local just inside the provider
  (`LocalButtonStyle provides …`). A nested `ArecaThemeProvider` or `ArecaDarkTheme` reseeds every
  local, so overrides above it do not reach inside.

## Rules

### Tokens, never literals

Read every colour, spacing, radius, shape, type style, elevation and duration from `ArecaTheme`
inside composition. Screen gutters are `Modifier.horizontalContentInsets()`,
`verticalContentInsets()` and `contentInsets()`.

```kotlin
// Wrong: literals and Material theme
Column(Modifier.padding(16.dp).background(Color(0xFFF5F5F7), RoundedCornerShape(12.dp))) {
    Text("Storage", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.SemiBold))
}

// Right: tokens and text roles
Column(
    modifier = Modifier
        .horizontalContentInsets()
        .background(ArecaTheme.colors.bgSurface, ArecaTheme.shapes.lg)
        .padding(ArecaTheme.spacing.xl),
    verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
) {
    Text("Storage", variant = TextVariant.HeadingSmall)
    Text("12 GB of 50 GB used", variant = TextVariant.BodySmall, color = TextColor.Subtle)
}
```

Every token, what it is for and how to change it: [reference/theme.md](reference/theme.md).

| Accessor | Members |
| --- | --- |
| `ArecaTheme.colors` | Grounds `bgCanvas` (page), `bgSoft` (softest ground: an expanded group, a note), `bgSurface` (cards), `bgSubtle` (settings sections, grouped regions), `bgMuted` (tracks and wells), `bgContrast`, `bgContrastMuted`, `bgOverlayDark`, `bgOverlayLight`, `bgDisabled`; per tone `bg{Brand,Danger,Success,Warn,Info,Neutral}{Muted,Base,Intense}`. Foregrounds `fgBase`, `fgSubtle`, `fgMuted`, `fgDisabled`, `fgOnContrast`, `fgOnFill`, `fgOnWarn`, `fgNeutral`, marks `fg{Brand,Danger,Success,Info,Warn}` and text `fg{…}Text`. Borders `borderBase`, `borderStrong`, `borderIntense`, `borderSubtle`, `borderOnSubtle` (hairlines on `bgSubtle`), `borderDisabled`, `border{Danger,Success,Info,Warn}`. Accent `accentBase`, `accentStrong`, `accentIntense`, `accentSubtle`, `accentMuted`; `accentScale`, eight shades for data, index `0` faintest in both themes |
| `ArecaTheme.spacing` | `none` 0, `xs2` 2, `xs` 4, `sm` 6, `md` 8, `lg` 12, `xl` 16, `xl2` 20, `xl3` 24, `xl4` 32, `xl5` 40, `xl6` 48, `xl7` 64, `xl8` 80, `xl9` 96; `horizontalContentInsets` 16, `verticalContentInsets` 24 |
| `ArecaTheme.shapes` / `radius` | `none`, `xs`, `sm`, `md`, `lg`, `xl`, `xl2`, plus shapes `full`, `tile`, `button`; prefer `shapes` |
| `ArecaTheme.typography` | `display`, `heading{ExtraSmall…ExtraLarge}`, `body{ExtraSmall…ExtraLarge}`, `label{Small,Medium,Large}`, `caption` |
| `ArecaTheme.elevation` | `none`, `xs`, `sm`, `md`, `lg`, `xl` |
| `ArecaTheme.motion` | Durations `instant`, `fast`, `medium`, `slow`, `deliberate`; easings `standard`, `enter`, `exit`, `pressIn`, `fade`; `motion.medium.tween(motion.standard)` |
| `ArecaTheme.isDark`, `darkColors` | Direction, only for a decision no token carries; the theme's dark colours before an `ArecaDarkTheme` scope |

- Set type with `Text(text, variant = TextVariant.X, color = TextColor.Y)` (`TextColor` `Base`,
  `Subtle`, `Muted`, `Disabled`, `OnContrast`, `OnFill`, `Brand`, `Danger`, `Success`, `Info`,
  `Warning`). In a component's slot, plain `Text("…")` inherits the slot's style and colour. Icon
  sizes are `IconSize.ExtraSmall` 12 to `ExtraLarge` 32.
- Exception: a component dimension no token names (a chart height, a fixed table column width) may
  be a literal `Dp`. Padding, gaps, colours, type and motion never are.

### Foundation before raw Compose; never Material

Use `Text` not `BasicText`, `Icon` not `Image` for glyphs, `Button` / `IconButton` not a clickable
`Box`, `Card` not a bordered `Box`, `ListItem` not a `Row`, `TextField` not `BasicTextField`,
`HorizontalDivider` not a 1 dp `Box`, `BaseLayout` not `Scaffold`. Never import
`androidx.compose.material3` or `material` components or `MaterialTheme`.

### Customise through the component's own types

Every component takes `style: XStyle` (colours, shape, indication) and `size: XSize` (padding, type,
metrics), defaulting to `LocalXStyle.current ?: …` and `LocalXSize.current ?: …`.

```kotlin
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
Badge(style = BadgeStyle.Success.copy(content = ArecaTheme.colors.fgSuccessText)) { Text("Paid") }

CompositionLocalProvider(
    LocalButtonStyle provides ButtonStyle.plain(ActionColor.Neutral),
    LocalButtonSize provides ButtonSize.Small,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
        Button(text = "Cancel", onClick = onCancel)
        Button(text = "Skip", onClick = onSkip)
    }
}
```

- Meaning is an `ActionColor` (`Brand`, `Danger`, `Success`, `Warning`, `Neutral`,
  `NeutralDark`); loudness is the factory: `ButtonStyle` / `IconButtonStyle` `.solid`, `.soft`,
  `.outlined`, `.plain`, and `FeaturedIconStyle` `.solid`, `.soft`, `.outlined`.
- Other styles have named presets (`CardStyle.Outlined`, `BadgeStyle.Danger`, `TopBarStyle.Tonal`)
  and a `default(…)` factory whose parameters are the tokens it reads. Sizes are named steps
  (`ButtonSize.ExtraSmall…ExtraLarge`, `IconButtonSize.Tiny…ExtraLarge`, `AvatarSize`,
  `HeadingSize`, `FieldSize`) or, where the companion offers one, `XSize.default(…)`
  (`CardSize.default(padding = …)`).
- Change one property with `.copy(…)`. `ButtonStyle.copy(shape = …)` moves the press and focus
  feedback with the shape.
- Restyle a subtree with `LocalXStyle` / `LocalXSize` (nullable, seeded by the provider). Fields
  read `LocalFieldStyle` / `LocalFieldSize` through `FieldDefaults`; provide one look for a whole
  form. Settings lists take `styles = SettingsDefaults.styles(…)` and
  `sizes = SettingsDefaults.sizes(…)`. `LocalNoticeStyle`, `LocalConfirmSheetStyle` and
  `LocalConfirmSheetSize` stay `null` unless provided: a notice follows its tone, and a confirm
  sheet builds on the `LocalFlowModalStyle` / `LocalFlowModalSize` in scope.
- A sheet opens in its own window, so capping the width of the screens does not reach it. An app
  that keeps a phone layout on a wide window caps every sheet with `LocalFlowModalSize provides
  FlowModalSize.default(maxWidth = …)` — `ConfirmSheet` and `SelectFlowModal` follow it too.
- Never write a wrapper composable whose only job is to restyle a component.
- Build presets and `default(…)` inside composition: they read the theme where called, so a style
  built inside `ArecaDarkTheme` is dark.

### One primary action per screen

`ButtonStyle.solid()` is the default and marks the primary action. Give every other button an
explicit `soft`, `outlined` or `plain` style; cancel and skip are
`ButtonStyle.plain(ActionColor.Neutral)`. A destructive action is
`ButtonStyle.soft(ActionColor.Danger)` (or `outlined`) and is confirmed with `ConfirmSheet` or
`ConfirmationDialog`. Two solid buttons on one screen means no primary action.

### Never clip an interactive element

Indications draw the keyboard focus ring outside the element's bounds and clip their own press
effect; an outer `Modifier.clip` crops the ring. Fill with `Modifier.background(color, shape)` and
pass the same shape to the indication. For a custom pressable surface use
`Modifier.clickable(indication = TouchIndication(shape = shape), role = Role.Button, …)` with
`Modifier.clickCursor()` and `Modifier.minimumTouchTarget()` (48 dp) for small targets.

```kotlin
// Wrong
Box(Modifier.clip(shape).clickable(onClick = onOpen))

// Right
Box(
    Modifier
        .background(ArecaTheme.colors.bgSurface, shape)
        .clickCursor()
        .clickable(interactionSource = null, indication = TouchIndication(shape = shape),
            role = Role.Button, onClick = onOpen),
)
```

Prefer `Card(onClick = …)` or `ListItem(onClick = …)`, which already do this.

### Label everything a screen reader cannot infer

- Pass a localised `contentDescription` to every `Icon`, `IconButton`, `Avatar`, `Spinner`,
  `ProgressBar`, `Skeleton`, `StatusMark`, `NotificationBadge` ("3 unread messages", not "3") and
  `IndicatorDot` that carries meaning. Pass `null` only when adjacent text says the same.
- Give pressable rows and cards an `onClickLabel` ("Open invoice").
- Put `Checkbox`, `Radio` and `Toggle` in their list or settings rows (`CheckboxListItem`,
  `RadioListItem`, `ToggleListItem`, `SettingsToggleRow`) so the title labels them; a bare group of
  `Radio`s needs `Modifier.selectableGroup()`.
- Forward the `FieldScope` from `FieldGroup` to its field (`field = field`) so the label, required
  marker and error are announced and the label focuses the field.
- Announce a message composed into existence (a failed sign-in) with `AnnounceOnChange(text)`;
  `FieldGroup` already announces its error.
- Use start and end slots, never left/right positioning, so layouts mirror in right-to-left.

### Localise every word

Components ship English defaults (`"Busy"`, `"Back"`, `"Cancel"`, `"Dismiss"`, `"Show password"`,
`ContentListLabels.English`, `SelectFieldLabels.English`, `RequirementListLabels.English`, …). A
production app passes its own string resource for every one: `loadingStateDescription`,
`contentDescription` on bar controls, `cancelText`, `overlayContentDescription`, `labels = …`.
Presentation-safe messages (errors, statuses) come from the presentation layer; static UI words come
from the app's resources. Never show exception text.

### Icons are opt-in

`ArecaIcons.*` lives in `dev.voir.areca:icons`, which no Areca module depends on. Use it only when
the app declares the dependency; otherwise pass the app's own `ImageVector` or `Painter`. Each icon
is an extension property needing two imports: `dev.voir.areca.icons.ArecaIcons` and the icon
(`dev.voir.areca.icons.Trash`).

### Hoist state; keep overlays composed

- Components are controlled: `value` + `onValueChange`, `selectedValue` + `onValueSelected`,
  `visible` + `onDismiss`. Use the `rememberXState` helpers only for state that belongs to the
  screen and nothing else; state a ViewModel owns is passed in.
- Call `FlowModal` unconditionally and drive it with `show()` / `hide()`; pass `ConfirmSheet` its
  `visible` flag. Never wrap either in `if`, or it cannot animate out. A `ConfirmSheet` takes its
  sheet from the `LocalFlowModalStyle` in scope, so restyle sheets there, not per confirmation.
- A `FlowModal` header starts at the sheet's top edge under the floating drag handle; pass its
  `TopBar` `windowInsets = FlowModalHeaderInsets` so the fill runs under the handle and the title
  clears it.

### Frame screens with `BaseLayout`

`BaseLayout(topBar, bottomBar, overlay) { padding -> … }` lays content out at full size under the
bars; the content must apply `padding` (a lazy list's `contentPadding`, or `Modifier.padding`). Give
the status-bar inset to the top bar's `windowInsets` and the rest to `contentWindowInsets`; the two
add up. `TabBar` applies the bottom inset itself. Put `ToastHost` in `overlay`.

### Lay out by window class, never by device

Branch a screen on `ArecaTheme.window` (`dev.voir.areca.window`), never on a platform, a screen
size in pixels or an `isTablet` flag: a tablet in a narrow split view is a compact window. Width
decides layout (`WidthClass.Compact` under 600dp, `Medium` from 600, `Expanded` from 840, `Large`
from 1200, `ExtraLarge` from 1600); `HeightClass.Compact` (under 480dp) is a phone on its side;
`InputMode` (`Touch` or `Pointer`) decides only what depends on how controls are pressed.
`ArecaTheme.window.fold` is a foldable's fold (Android only, else `null`): keep text and controls
off its `bounds`, and while its `state` is `FoldState.HalfOpened` lay each half out for itself.
`ListDetail`, `SupportingPane` and the edge column follow it already.

- One value: `adaptive(compact = 2, medium = 3, expanded = 4)`. Name only `compact` and the steps
  where the value changes; each wider step falls back to the one below.
- One of several layouts: `AdaptiveContent(compact = { … }, medium = { … })`.
- Panes: wrap each pane of a multi-pane layout in `WindowClassBox(modifier) { … }` so what is inside
  reads the pane's class, not the window's.
- Previews and tests at a chosen window: `ProvideWindowClass(WindowClass(DpSize(820.dp, 1180.dp),
  InputMode.Touch)) { … }`; a foldable adds `fold = WindowFold.vertical(at = 380.dp, height = 720.dp)`. It changes the class only; to draw at another input's sizes too, nest
  `ArecaThemeProvider(inputMode = …)`.
- Use the adaptive layouts (`dev.voir.areca.foundation.adaptive`) before branching by hand:
  `AdaptiveNavigation` for the app shell (bottom bar on a phone, the edge column from medium up),
  `ListDetail`,
  `SupportingPane`, `ContentColumn` for forms and auth, `AdaptiveGrid`, `AdaptiveSheet` instead of a
  bare `FlowModal` for forms (`ConfirmSheet` is already a dialog on wide windows), `AdaptiveMenu`
  for a row's actions. Their slots say which form they are in (`layout`, `isTwoPane`,
  `isSupportingInline`): draw a back action only when `!isTwoPane`, a "Filters" button only when
  `!isSupportingInline`. Signatures: reference/signatures.md.
- Inside `AdaptiveNavigation`, every screen declares its title, back and actions with
  `AdaptiveToolbar(title, onBack, actions)`, never a `TopBar`: it is a bar on a phone, and from a
  medium window up it hands back to the edge column and places the actions by
  `ArecaTheme.hostWindow.actionReach` — beside the title as labelled buttons on a tablet on its side
  or under a pointer (`ActionReach.Header`), at the foot of the column between the tabs and search on
  a tablet held upright or an unfolded foldable (`ActionReach.Thumb`). A screen that draws its own
  actions branches on the same `actionReach`, never on width or on the device. Keep text and
  controls inside the scope's `contentPadding` (the column's clearance) and let backgrounds and
  media run under the column. The app's primary action goes in `header` (an `EdgeButton` with
  `EdgeButtonEmphasis.Primary` in the column; it follows the reach too), its sync or offline state in
  `status` as an `EdgeStatus` with the full text in `footer`, search in `onSearch`. Never draw
  your own buttons along the column's edge or across `ArecaTheme.window`'s top as a toolbar.
  Placement, the screen states and a shell of your own: reference/adaptive-shell.md.
- Desktop affordances come with the components; do not build them by hand. Put
  `Modifier.contextMenu(menu)` on a row that has an `AdaptiveMenu`, ahead of its `clickable`: it
  opens the menu on a right-click, a long press, the Menu key and the accessibility long-click, and
  a long press then does not also click. `IconButton` shows its `contentDescription` as a tooltip
  under a pointer; wrap anything else a pointer user cannot read off — an icon alone, a truncated
  title — in `TooltipBox(text)`, and still give it a description for assistive technology. Areca's
  lists, grids and tables draw a scrollbar under a pointer; beside a `verticalScroll` or a
  `LazyColumn` of your own add `VerticalScrollbar(state, Modifier.align(Alignment.CenterEnd)
  .fillMaxHeight())` in a `Box`. `ListDetail`'s divider drags under a pointer
  (`resizable = false` pins it). None of these draws or reacts under touch.
- Size presets already follow the input: `ButtonSize.Medium` is 48dp under touch and 36dp under a
  pointer, read from `ArecaTheme.sizing`, and screen gutters widen with the window. Never hard-code a
  control or row height; in a size of your own use `ArecaTheme.sizing.controlMd` and
  `sizing.select(touch = …, pointer = …)`.

### Regions that are always dark

Wrap a photo viewer, camera or editor in `ArecaDarkTheme { … }`, which keeps the app's palette,
shapes and type. A nested `ArecaThemeProvider(darkTheme = true)` resets them to library defaults.
Paint the system bars with `SystemBars(color)` while it shows.

### Experimental API

Only the pull-to-refresh family is `@ExperimentalArecaApi` (`PullToRefreshLayout`,
`PullToRefreshIndicator`, `PullToRefreshState`, `rememberPullToRefreshState`,
`PullToRefreshStyle`, `PullToRefreshSize` and their locals). Opt in at the call site with
`@OptIn(ExperimentalArecaApi::class)`. `PagedContentList(onRefresh = …)` gives pull-to-refresh
without opting in.

## Choose a component

Every foundation family, by intent. Signatures, overloads and packages:
[reference/signatures.md](reference/signatures.md).

| Intent | Use |
| --- | --- |
| Primary action; secondary; cancel or skip | `Button(text, onClick)`; `ButtonStyle.soft()` / `outlined()`; `ButtonStyle.plain(ActionColor.Neutral)` |
| Destructive action | `ButtonStyle.soft(ActionColor.Danger)` confirmed with `ConfirmSheet` (phone) or `ConfirmationDialog` (centred, suspend handlers) |
| Icon-only action | `IconButton(imageVector, contentDescription, onClick)` |
| Link; prompt plus link | `TextLink`; `TextLinkPrompt` ("No account? Sign up") |
| Text; title with supporting line; masked value | `Text(…, variant, color)`; `Heading(title, supportive)`; `SensitiveText` |
| Icon; icon on a tinted tile; disclosure arrow | `Icon`; `FeaturedIcon`; `Chevron` (`ListItemChevron` in a row) |
| Labelled input with hint or error | `FieldGroup(label, assistive, error) { field -> TextField(…, field = field) }` |
| Password and its rules; search; amount with unit | `PasswordField` + `RequirementList`; `SearchField`; `AffixTextField` |
| One-time code; compound input | `OtpField`; `FieldRow { SelectSegment(…); TextSegment(…) }` |
| A custom control drawn as a field; label and message around it without wiring | `Field(onClick = …) { … }`; `FieldGroupLayout` (prefer `FieldGroup`) |
| Pick from a short list; a long or searchable one | `SelectField`; `SelectFlowModal` |
| Filter chips; colour; small integer | `BadgeSelect` / `FlowBadgeSelect` / `LazyBadgeSelect` with `BadgeStyle.Chip` (`FilledChip` to fill the unchosen ones) + `BadgeSize.Chip`; `SwatchPicker` (one chip `Swatch`); `NumberStepper` |
| On/off setting; tick in a form; one of many | `Toggle`; `Checkbox`; `Radio`, each inside its row: `ToggleListItem`, `CheckboxListItem`, `RadioListItem`, `CheckmarkListItem` (a tick, `ListItemCheckmark` by hand), or the settings rows |
| Settings screen; a short, bounded group of rows | `Settings { Section { SettingsToggleRow / SettingsSelectRow / SettingsRow / SettingsTextFieldRow / SettingsOptionRow } }`; leading `SettingsRowIcon` / `SettingsRowTile`; loading row `SettingsRowSkeleton`; free content `SettingsBlock` |
| Second action in a pressable row; line of detail under a subtitle | `trailing = { SettingsRowLink("Verify", onVerify) }`; the row's `caption` slot |
| Grouped rows on one surface | `List { item(…) }` (eager), `LazyList` (lazy), `ListItemColumn` (hand-composed `ListItem`s) |
| Row that navigates; row that expands | `NavigationListItem`; `ExpandableListItem` (`container = ExpandableListItemContainer.Soft` for one soft ground with what it reveals) |
| Remote list with loading, empty and error faces | `ContentList` (one load), `PagedContentList` (pages, pull-to-refresh); with group headers `GroupedContentList`, `GroupedPagedContentList` |
| Grid with group headers; tabular data | `LazyVerticalGroupedGrid` / `LazyHorizontalGroupedGrid`; `Table` / `LazyTable` |
| Grouping surface; with status and actions along its bottom | `Card` (expandable overload); `Card(footer = { … }, footerStyle = CardFooterStyle.soft(ActionColor.Brand))` |
| Show and hide a region; separator | `Expandable`; `HorizontalDivider` / `VerticalDivider` |
| Screen frame; title bar; large collapsing title | `BaseLayout`; `TopBar` with `BackAction` and the named controls (`CloseAction`, `OverflowAction`, `DoneAction`, `IconAction`, `TextAction`, `AvatarAction`, … or `Action` for custom content); `LargeTopBar` + `rememberTopBarScrollState(TopBarScrollBehavior.ExitUntilCollapsed)` |
| Progress under the bar; collapsing hero | `TopBarProgress` in the bar's `bottom`; `CollapsingHeaderLayout` (`collapsed = { … }` for a compact band that stays) |
| App destinations; views within a screen; sidebar | `TabBar` / `FloatingTabBar` (items drawn with `FloatingTabBarItemContent`); `Tabs` (`TabsVariant.Underline`); `NavList` (one custom row `NavItem`) |
| Segmented choice of a setting; words with a moving pill; three-state toggle (has / avoid / either) | `SlidingSelector(items = listOf(SelectorItem(value) { Text(…) }), selected, onSelect)` — `style = SlidingSelectorStyle.Segmented` (default) / `Pill` / `Underline` / `Button`, `layout = SelectorLayout.Fill` (default) / `Hug` / `Scroll`; three-state: `mode = SelectorMode.Optional` (tapping the chosen option calls `onSelect(null)`) with a `thumb` colour per `SelectorItem`. Not `Tabs` for a setting: `Tabs` switch views |
| Onboarding pages; steps of a flow | `SwipePager` + `PagerControls` + `PageIndicator` with `rememberPagerController`; `StepIndicator` |
| A product's pictures, a gallery, a picture to zoom | `media`: `Carousel` of `MediaFrame`s opening a `MediaViewer`; whole screens are `flow-gallery`'s recipes |
| Which picture or page is on screen | `PageIndicator(pagerState, variant = PageIndicatorVariant.Dots / Shrinking() / Line / Segments)` — `Elongated` is the default; over a picture `style = PageIndicatorStyle.onImage()`; as a count `PageCounter(pagerState)` (`PageCounterStyle.onImage()`) |
| Pull to refresh | `PagedContentList(onRefresh = …)`; around any scroller `PullToRefreshLayout` + `PullToRefreshIndicator` (experimental) |
| Bottom sheet; any other dialog; menu | `FlowModal` + `rememberFlowModal()` + `FlowModalActionBar` (`layout = FlowModalActionBarLayout.Split()` for a full-width pair, `FlowModalSecondaryAction` beside it); `Dialog` + `ModalDefaults.Actions`; `DropdownPopup` + `rememberDropdownPopup()` + `Modifier.anchored(state.anchorState)` |
| Transient message; persistent inline message | `ToastHost` + `rememberToastState().show(label = …)` (draws `Toast`); `Notice` + `NoticeText` (`AnimatedNotice` to appear and leave) |
| Empty or failed region; whole-screen progress or result | `Fallback(top, heading, body, content)`; `StatusScene` + `StatusMark` + `StatusSceneActions` |
| Loading placeholder; waiting; known progress | `Skeleton`, `ListItemSkeleton`, `SettingsRowSkeleton`; `Spinner`; `ProgressBar` |
| Status label; count; dot; person | `Badge(style = BadgeStyle.Success)`; `NotificationBadge`; `IndicatorDot`; `Avatar(letters, contentDescription)` |
| A figure; a grid of figures | `Stat(label, value, note)` (`StatSize.Large` for the figure a section is about); `StatGrid(columns) { Stat(…) }` |
| Custom start-centre-end row; label beside content; tint over a box | `SlotRow`; `RelativePositionLayout`; `BoxScope.Overlay` |
| Scroll wheel | `WheelPicker { WheelPickerColumn(…) }`, rows `WheelPickerItem`, decorations `WheelPickerHighlight` / `WheelPickerEdgeFade` |
| Tell a screen reader something changed | `AnnounceOnChange(text)` |
| Region that is always dark; system bar colour | `ArecaDarkTheme`; `SystemBars` |
| Custom pressable surface | `Modifier.clickable(indication = TouchIndication(shape))` + `clickCursor()` (see the clipping rule) |
| Period chart that picks a period; running total through a period | `ColumnChart(columns, selectedKey, onSelect)` (`dev.voir.areca:charts`); `LineChart(series)` |
| Amount against a usual or a target; key to a chart's colours | `MeterBar(fraction, marker = …)`; `ChartLegend(items)` with a `ChartSwatch` per series |
| Dates and times | `dev.voir.areca:datetime`: calendars, ranges, pickers, date-time field |
| Heat-map month; pick a month from tiles; date as a row's leading tile | `CalendarHeatDay` in a `Calendar`'s `day` slot + `calendarHeatLevel` (`steps` = the ramp's size, `CalendarHeatStyle.ramp.size`) + `CalendarHeatLegend`; `MonthGrid`; `DateTile` (all `dev.voir.areca:datetime`; the grid and the tile take `labels: CalendarLabels`) |

## Rows, sections and cards

### Pick the surface by what the rows are

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

### Text roles

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

### Alignment: pick by scenario

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

### Leading visuals

Within one section or list, every leading visual has **one size and one shape**, so the visuals
read as a column and the text beside them starts at one edge:

- Glyphs go on a `SettingsRowIcon` tile.
- Anything else — a flag, a photo, a brand or provider mark — goes in `SettingsRowTile { … }`,
  which crops it to the same tile.
- People are round avatars; give `SettingsRowTile` `shape = ArecaTheme.shapes.full` around a picture
  at the tile size rather than mixing a larger avatar into a column of tiles. A list that is only
  people may use a larger avatar throughout.
- Do not mix a bare glyph, a tile and a flag in one column.

### Card corners

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

### Section bands

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

### Card footers

A card's status and actions go in its `footer` (`Card(footer = { … }, footerStyle = …)`), not in a
divider and a row drawn by hand; the subscription-card recipe shows one. `plain()` for supporting
detail, `soft(color)` for a state worth noticing (`Warning` for a trial ending, `Danger` for a
failed payment), `solid(color)` for the card's call to action — one per screen — and `of(bg,
content)` for a colour outside the families. The footer hands its ink and a matching borderless
button style to what is inside it, so do not style the copy or the buttons.

### Surfaces

The page is `bgCanvas`. Settings sections and other grouped regions are `bgSubtle` — the section
card's default — with `borderOnSubtle` hairlines; tracks and wells inside them are `bgMuted`;
raised cards are `bgSurface` or a `CardStyle` preset. Do not put a `bgSubtle` region on another. A
block that should barely separate from the page, or that holds `bgSubtle` controls — an expanded
group, a note — is `bgSoft`.

## Add a module for

| Need | Artifact |
| --- | --- |
| Bar, stacked-bar, column and line charts, meter bars and chart legends | `dev.voir.areca:charts` |
| Cropping a photo before saving or uploading it | `dev.voir.areca:crop` (add `foundation` too) |
| Pictures from data: framed with loading and failed faces, zoom, carousels, thumbnails, a full-screen viewer, hotspots, before/after | `dev.voir.areca:media` |
| Calendar, heat-map calendar, date range, month grid, date tile, time picker, date-time field | `dev.voir.areca:datetime` (brings `kotlinx-datetime` 0.8.x) |
| On-screen numeric or custom keyboard | `dev.voir.areca:keyboard` |
| The shared icon set | `dev.voir.areca:icons` |
| Sign-in, sign-up, code verification, SSO buttons | `dev.voir.areca:flow-auth` |
| Profile, account menu, devices, account deletion | `dev.voir.areca:flow-profile` |
| A product gallery: captions, author lines, the primary action, grid pages and screen recipes | `dev.voir.areca:flow-gallery` (brings `media`) |
| Attaching, listing and viewing files | `dev.voir.areca:flow-attachments` |

Flow modules ship parts and recipes, not screens: copy the recipe, pass the app's words and
complaints, and keep validation, networking and navigation in the app. Which module, its package
and its reference: [reference/modules.md](reference/modules.md).

## Feed screens from presentation state

Split UI into stateful composables that obtain a ViewModel and collect its state, and stateless
composables that take state and callbacks. Only stateless composables use Areca components, so
they preview and test without a ViewModel.

- A screen is a layout of stateful composables, one per feature it shows — `HomeScreen` lays out
  `GreetingSection`, `LibraryPreviewSection` and `StatsSection`, each bound to its own feature
  ViewModel. A screen showing one feature is one stateful composable and its stateless content.
- Each stateful composable renders its own loading, empty and error state — `Skeleton`,
  `EmptyState`, and `ErrorState` or an inline `Notice`. One failing section never replaces the screen; a full-screen
  error is only for the screen's precondition.
- Each stateful composable has one lifecycle-aware effect collector for the ViewModel it owns, as
  `areca:areca-viewmodel-v1` prescribes: it shows transient messages with `ToastState.show(…)`,
  raises permission prompts, and turns navigation requests into the callbacks its parent passed
  in. Sections never see routes.
- Pull-to-refresh on a screen of sections belongs to the screen: it asks every section to refresh
  and shows the indicator while any of them is still refreshing.
- Render an outcome the screen must keep showing from state.

```kotlin
@Composable
fun LibraryPreviewSection(
    onOpenItem: (LibraryItemId) -> Unit,
    onSeeAll: () -> Unit,
    viewModel: LibraryViewModel = koinViewModel(key = "library-preview") {
        parametersOf(LibraryWindow.Latest(4))
    },
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LibraryEffects(viewModel.effects, onOpenItem = onOpenItem)
    LibraryPreviewContent(state = state, onOpen = viewModel::open, onRetry = viewModel::refresh, onSeeAll = onSeeAll)
}
```

Map Areca ViewModel list state (`UiState<ListState<…>>`) to `PagedContentListState` in the UI
module; the mapping is in the list-screen recipe.

- Wire `onRefresh`, `onLoadNextPage`, `onRetryInitial` and `onRetryLoadMore` to the ViewModel's
  public methods.
- Render `ListState.refreshError` with the retained items (a `Notice` in the list's `header`), and
  an inline reload (`isReloading`) as `TopBarProgress(progress = null)` in the top bar's `bottom`.
- Map `SettingsRowStatus.Loading` / `Error(message)` and `Button(loading = …)` from per-item flags
  in state; keep content visible while they run.

## Verify

- **KDoc.** Every public declaration documents each parameter, its default and its contract, and
  the artifacts publish sources, so the IDE shows it. Read the KDoc of the component, its `XStyle`
  and its `XSize` before overriding anything. Never guess a parameter: if the KDoc or
  [reference/signatures.md](reference/signatures.md) does not list it, it does not exist.
- **Playground.** The playground in `VoirDev/areca-ui`, the repository this skill ships from, has a
  page per component family with live controls, variants and edge cases
  (`playground/src/commonMain/kotlin/dev/voir/areca/playground/foundation/<family>/`, such as
  `ButtonShowcase.kt`), and every flow recipe as a compiled page. Run it from a checkout with
  `./gradlew :playground:run`.
- **Guides.** In the same repository, `docs/components.md` catalogues every foundation component,
  `docs/theme.md` the tokens, and each module has its own `README.md` (`ui/foundation/README.md`,
  `ui/charts/README.md`, …). This skill does not depend on them.
- **Compile.** Build the app's UI module for every target it ships; check both themes and a
  right-to-left locale in previews.

## References

Foundation:

- [reference/theme.md](reference/theme.md) — setting up `ArecaThemeProvider`, picking a colour,
  type role, gap, corner, elevation or duration, re-theming for a brand, or building a dark region.
- [reference/signatures.md](reference/signatures.md) — calling a foundation component whose
  parameters, overloads, defaults or package you are not sure of, or fixing an import.
- [reference/adaptive-shell.md](reference/adaptive-shell.md) — an app shell for phones, tablets and
  foldables: where destinations, a screen's toolbar, search and status go on each window, and
  `AdaptiveToolbar` inside `AdaptiveNavigation`.
- [reference/recipes.md](reference/recipes.md) — building a settings screen, a validated form, a
  remote list, a confirmation, a tabbed shell, pickers (segmented, pill, three-state), a bottom
  sheet, a menu, a dark region, a card with a footer, a custom pressable surface or an analytics
  overview; reviewing UI against the
  anti-pattern table; finding the flow recipes.

Other modules — start at [reference/modules.md](reference/modules.md), the index of artifacts,
packages and dependencies, then open the module's file:

- [reference/charts.md](reference/charts.md) — bar, stacked-bar, column and line charts, meter bars,
  chart legends.
- [reference/datetime.md](reference/datetime.md) — calendars, heat-map months, date ranges, month
  grids, date tiles, time and date-time pickers and fields.
- [reference/crop.md](reference/crop.md) — cropping a photo before saving or uploading it.
- [reference/media.md](reference/media.md) — framed pictures with loading and failed faces,
  hotspots, zoom, carousels, thumbnail strips, the full-screen viewer and comparisons.
- [reference/keyboard.md](reference/keyboard.md) — an on-screen numeric or custom keyboard.
- [reference/icons.md](reference/icons.md) — drawing `ArecaIcons.*` and adding the `icons`
  dependency.
- [reference/flow-gallery.md](reference/flow-gallery.md) — product gallery captions, author
  lines, the primary action and grid pages, on media.
- [reference/flow-auth.md](reference/flow-auth.md) — sign-in, sign-up, email-first, code
  verification and SSO screens.
- [reference/flow-profile.md](reference/flow-profile.md) — profile, account menu and switcher,
  devices, photo and account deletion.
- [reference/flow-attachments.md](reference/flow-attachments.md) — attaching, listing and viewing
  files.

## Checklist

- [ ] One `ArecaThemeProvider` at the root with `darkTheme` wired; root painted `bgCanvas`.
- [ ] No Material imports; foundation components instead of raw Compose equivalents.
- [ ] Colours, spacing, shapes, type and motion from `ArecaTheme`, `TextVariant` and `TextColor`.
- [ ] Restyling through `style`, `size`, `.copy(…)` or `LocalX…`; no restyling wrappers.
- [ ] At most one solid button per screen; destructive actions confirmed.
- [ ] No `Modifier.clip` on interactive elements; custom surfaces use a shaped indication.
- [ ] Every meaningful icon, control and indicator labelled; rows and cards have `onClickLabel`.
- [ ] Every English default replaced by the app's strings; no exception text shown.
- [ ] `ArecaIcons` used only with the `icons` dependency declared.
- [ ] `BaseLayout` padding applied; insets split between the top bar and `contentWindowInsets`.
- [ ] Overlays composed unconditionally; state hoisted to the screen or ViewModel.
- [ ] Every independently loaded region is a stateful composable with its own loading, empty and
      error state and one effect collector; stateless composables take no ViewModel.
- [ ] Experimental pull-to-refresh opted in explicitly; module-specific rules followed.
- [ ] Settings sections hold short, bounded groups; open-ended collections are lists, and the user
      was asked when the size was unclear.
- [ ] Row text is plain `Text` in the role's slot — no style, colour or weight; a third line is
      the `caption`; no glyph nudged by padding or a custom `layout`.
- [ ] A card's status and actions sit in its `footer`, inked by its `CardFooterStyle`.
- [ ] The same kind of thing looks the same on every screen.
- [ ] Row alignment comes from the scenario (`Auto` unless a table row says otherwise); no padding
      nudges a glyph or control into line.
- [ ] One size and shape of leading visual per section; non-glyph visuals in `SettingsRowTile`.
- [ ] One card corner per nesting level, set once; the same card has the same corner everywhere.
