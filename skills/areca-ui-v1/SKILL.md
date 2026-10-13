---
name: areca-ui-v1
description: Areca UI 1.x conventions for building Compose Multiplatform screens with dev.voir.areca:foundation and its charts, crop, datetime, keyboard, icons and flow modules — theme, tokens, component choice, customisation, accessibility and localisation. Use when writing, reviewing or prototyping Compose Multiplatform UI .kt code in an app that depends on dev.voir.areca:foundation or another Areca UI artifact.
---

# Areca UI Guidelines

> **Audience:** Compose Multiplatform UI developers and coding agents
> **Validated library version:** `dev.voir.areca:foundation` 1.0.0, with `charts`, `crop`,
> `datetime`, `keyboard`, `icons`, `flow-auth`, `flow-profile` and `flow-attachments` 1.0.0

Areca is Voir's Compose Multiplatform design system for Android, iOS and the JVM: theme tokens and
the components built on them (`foundation`), plus feature modules for charts, cropping, dates,
an on-screen keyboard, icons, and authentication, profile and attachment screens. It draws and
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
    implementation("dev.voir.areca:foundation:1.0.0")
    implementation("dev.voir.areca:icons:1.0.0") // only if the app draws ArecaIcons.*
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

- Mount exactly one `ArecaThemeProvider`, at the root. Every component and every Areca module reads
  tokens and component defaults from it; without it they fall back to the light library defaults.
- Pass `darkTheme` from `isSystemInDarkTheme()` or the user's setting; nothing follows the system on
  its own. The provider paints nothing, so give the root its `bgCanvas` ground.
- Re-theme only through the provider's parameters, at the highest level that says what you mean:
  `baseColors` (re-themes both directions), `radius`, `spacing`, `elevation`, `motion`, `fonts`
  (`ArecaFonts(primary = brandSans)`); `colors`, `shapes` and `typography` derive from them.
- Change an app-wide component default by providing its local just inside the provider
  (`LocalButtonStyle provides …`). A nested `ArecaThemeProvider` or `ArecaDarkTheme` reseeds every
  local, so overrides above it do not reach inside.
- Previews and tests compose without a provider (light defaults); wrap them when the dark theme or
  an app token matters.

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

| Accessor | Members |
| --- | --- |
| `ArecaTheme.colors` | Grounds `bgCanvas` (page), `bgSurface` (cards), `bgSubtle`, `bgMuted`, `bgContrast`, `bgDisabled`, overlays; per tone `bg{Brand,Danger,Success,Warn,Info,Neutral}{Muted,Base,Intense}`. Foregrounds `fgBase`, `fgSubtle`, `fgMuted`, `fgDisabled`, `fgOnContrast`, `fgOnFill`, marks `fg{Brand,Danger,Success,Info,Warn}` and text `fg{…}Text`. Borders `borderBase`, `borderStrong`, `borderSubtle`, `border{Danger,Success,Info,Warn}`. Accent `accentBase` … `accentMuted` |
| `ArecaTheme.spacing` | `none` 0, `xs2` 2, `xs` 4, `sm` 6, `md` 8, `lg` 12, `xl` 16, `xl2` 20, `xl3` 24, `xl4` 32, `xl5` 40, `xl6` 48, `xl7` 64, `xl8` 80, `xl9` 96; `horizontalContentInsets` 16, `verticalContentInsets` 24 |
| `ArecaTheme.shapes` / `radius` | `none`, `xs`, `sm`, `md`, `lg`, `xl`, `xl2`, plus shapes `full`, `tile`, `button`; prefer `shapes` |
| `ArecaTheme.typography` | `display`, `heading{ExtraSmall…ExtraLarge}`, `body{ExtraSmall…ExtraLarge}`, `label{Small,Medium,Large}`, `caption` |
| `ArecaTheme.elevation` | `none`, `xs`, `sm`, `md`, `lg`, `xl` |
| `ArecaTheme.motion` | Durations `instant`, `fast`, `medium`, `slow`, `deliberate`; easings `standard`, `enter`, `exit`, `pressIn`, `fade`; `motion.medium.tween(motion.standard)` |
| `ArecaTheme.isDark` | Only for a decision no token carries |

- Set type with `Text(text, variant = TextVariant.X, color = TextColor.Y)`. `TextColor` is `Base`,
  `Subtle`, `Muted`, `Disabled`, `OnContrast`, `OnFill`, `Brand`, `Danger`, `Success`, `Info`,
  `Warning`. Inside a component's slot, plain `Text("…")` inherits the slot's style and colour.
- Icon sizes are `IconSize.ExtraSmall` 12 to `ExtraLarge` 32.
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
  (`ButtonSize.ExtraSmall…ExtraLarge`, `IconButtonSize.Tiny…ExtraLarge`) or, where the companion
  offers one, `XSize.default(…)` (`CardSize.default(padding = …)`).
- Change one property with `.copy(…)`. `ButtonStyle.copy(shape = …)` moves the press and focus
  feedback with the shape.
- Restyle a subtree with `LocalXStyle` / `LocalXSize`. Fields read `LocalFieldStyle` /
  `LocalFieldSize` through `FieldDefaults`; provide one look for a whole form. Settings lists take
  `styles = SettingsDefaults.styles(…)` and `sizes = SettingsDefaults.sizes(…)`.
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
  `visible` flag. Never wrap either in `if`, or it cannot animate out.

### Frame screens with `BaseLayout`

`BaseLayout(topBar, bottomBar, overlay) { padding -> … }` lays content out at full size under the
bars; the content must apply `padding` (a lazy list's `contentPadding`, or `Modifier.padding`). Give
the status-bar inset to the top bar's `windowInsets` and the rest to `contentWindowInsets`; the two
add up. `TabBar` applies the bottom inset itself. Put `ToastHost` in `overlay`.

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

| Intent | Use |
| --- | --- |
| Primary action; secondary; cancel or skip | `Button(text, onClick)`; `ButtonStyle.soft()` / `outlined()`; `ButtonStyle.plain(ActionColor.Neutral)` |
| Destructive action | `ButtonStyle.soft(ActionColor.Danger)` confirmed with `ConfirmSheet` (phone) or `ConfirmationDialog` (centred, suspend handlers) |
| Icon-only action | `IconButton(imageVector, contentDescription, onClick)` |
| Link; prompt plus link | `TextLink`; `TextLinkPrompt` ("No account? Sign up") |
| Text; title with supporting line; masked value | `Text(…, variant, color)`; `Heading(title, supportive)`; `SensitiveText` |
| Icon; icon on a tinted tile; disclosure arrow | `Icon`; `FeaturedIcon`; `Chevron` |
| Labelled input with hint or error | `FieldGroup(label, assistive, error) { field -> TextField(…, field = field) }` |
| Password and its rules; search; amount with unit | `PasswordField` + `RequirementList`; `SearchField`; `AffixTextField` |
| One-time code; compound input | `OtpField`; `FieldRow { SelectSegment(…); TextSegment(…) }` |
| Pick from a short list; a long or searchable one | `SelectField`; `SelectFlowModal` |
| Filter chips; colour; small integer | `BadgeSelect` / `FlowBadgeSelect` / `LazyBadgeSelect`; `SwatchPicker`; `NumberStepper` |
| On/off setting; tick in a form; one of many | `Toggle`; `Checkbox`; `Radio`, each inside its list or settings row |
| Settings screen | `Settings { Section { SettingsToggleRow / SettingsSelectRow / SettingsRow / SettingsTextFieldRow / SettingsOptionRow } }` |
| Grouped rows on one surface | `List { item(…) }` (eager), `LazyList` (lazy), `ListItemColumn` (hand-composed) |
| Row that navigates; row that expands | `NavigationListItem`; `ExpandableListItem` |
| Remote list with loading, empty and error faces | `ContentList` (one load), `PagedContentList` (pages, pull-to-refresh), `Grouped…` variants |
| Grid with group headers; tabular data | `LazyVerticalGroupedGrid` / `LazyHorizontalGroupedGrid`; `Table` / `LazyTable` |
| Grouping surface; show and hide a region; separator | `Card` (expandable overload); `Expandable`; `HorizontalDivider` / `VerticalDivider` |
| Screen frame; title bar; large collapsing title | `BaseLayout`; `TopBar` with `BackAction` and the named `…Action` controls; `LargeTopBar` + `rememberTopBarScrollState(TopBarScrollBehavior.ExitUntilCollapsed)` |
| Progress under the bar; collapsing hero | `TopBarProgress` in the bar's `bottom`; `CollapsingHeaderLayout` |
| App destinations; views within a screen; sidebar | `TabBar` / `FloatingTabBar`; `Tabs` (`TabsVariant.Underline` or `Button`); `NavList` |
| Onboarding pages; steps of a flow | `SwipePager` + `PagerControls` + `PageIndicator`; `StepIndicator` |
| Bottom sheet; any other dialog; menu | `FlowModal` + `rememberFlowModal()` + `FlowModalActionBar`; `Dialog` + `ModalDefaults.Actions`; `DropdownPopup` + `rememberDropdownPopup()` + `Modifier.anchored(state.anchorState)` |
| Transient message; persistent inline message | `ToastHost` + `rememberToastState().show(label = …)`; `Notice` + `NoticeText` (`AnimatedNotice` to appear and leave) |
| Empty or failed region; whole-screen progress or result | `Fallback(top, heading, body, content)`; `StatusScene` + `StatusMark` + `StatusSceneActions` |
| Loading placeholder; waiting; known progress | `Skeleton`, `ListItemSkeleton`, `SettingsRowSkeleton`; `Spinner`; `ProgressBar` |
| Status label; count; dot; person | `Badge(style = BadgeStyle.Success)`; `NotificationBadge`; `IndicatorDot`; `Avatar(letters, contentDescription)` |
| Custom start-centre-end row; content beside content | `SlotRow`; `RelativePositionLayout`, `BoxScope.Overlay` |
| Scroll wheel; dates and times | `WheelPicker { WheelPickerColumn(…) }`; `dev.voir.areca:datetime` |

Exact signatures, overloads and packages: [reference/signatures.md](reference/signatures.md).

## Add a module for

| Need | Artifact |
| --- | --- |
| Bar or stacked-bar charts | `dev.voir.areca:charts` |
| Cropping a photo before saving or uploading it | `dev.voir.areca:crop` (add `foundation` too) |
| Calendar, date range, time picker, date-time field | `dev.voir.areca:datetime` (brings `kotlinx-datetime` 0.8.x) |
| On-screen numeric or custom keyboard | `dev.voir.areca:keyboard` |
| The shared icon set | `dev.voir.areca:icons` |
| Sign-in, sign-up, code verification, SSO buttons | `dev.voir.areca:flow-auth` |
| Profile, account menu, devices, account deletion | `dev.voir.areca:flow-profile` |
| Attaching, listing and viewing files | `dev.voir.areca:flow-attachments` |

Flow modules ship parts and README recipes, not screens: copy the recipe, pass the app's words and
complaints, and keep validation, networking and navigation in the app. Usage rules for each module:
[reference/modules.md](reference/modules.md).

## Feed screens from presentation state

Split a screen into a route that obtains the ViewModel and collects its state, and a stateless
screen composable that takes state and callbacks. Only the stateless screen uses Areca components,
so it previews and tests without a ViewModel. Collect state and effects as
`areca:areca-viewmodel-v1` prescribes, with one lifecycle-aware effect collector in the route: it
performs navigation requests, shows transient messages with `ToastState.show(…)` and raises
permission prompts. Render an outcome the screen must keep showing from state.

Map Areca ViewModel list state to the foundation list state in the UI module:

```kotlin
fun <T> UiState<ListState<T, *, *>>.toPagedContentListState(
    loadMoreErrorText: String,
): PagedContentListState<T> = when (this) {
    UiState.Initial, UiState.Loading ->
        PagedContentListState(items = emptyList(), hasNextPage = false, loadingInitial = true)
    is UiState.Error ->
        PagedContentListState(items = emptyList(), hasNextPage = false, initialError = message)
    is UiState.Display -> PagedContentListState(
        items = data.items,
        hasNextPage = data.pagingMeta.hasNextPage,
        loadingNextPage = data.isLoadingNextPage,
        loadMoreError = loadMoreErrorText.takeIf { data.nextPageError != null },
        refreshing = data.isRefreshing,
    )
}
```

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
  page per component family with live controls, variants and edge cases, and every flow recipe as
  a compiled page. Run it from a checkout with `./gradlew :playground:run`; the pages are under
  `playground/src/commonMain/kotlin/dev/voir/areca/playground/`.
- **Guides.** In the same repository, `ui/foundation/AGENTS.md` is the agent guide to foundation,
  `ui/foundation/README.md` the concepts, and each module has its own `README.md`.
- **Compile.** Build the app's UI module for every target it ships; check both themes and a
  right-to-left locale in previews.

## References

- Read [reference/signatures.md](reference/signatures.md) when calling a foundation component whose
  parameters, overloads, defaults or package you are not sure of.
- Read [reference/recipes.md](reference/recipes.md) when building a settings screen, a validated
  form, a remote list, a confirmation, a tabbed shell, a bottom sheet, a menu, a dark region or a
  custom pressable surface, or when reviewing UI against the anti-pattern table.
- Read [reference/modules.md](reference/modules.md) when a screen needs charts, cropping, dates and
  times, the on-screen keyboard, the icon set, or a flow module.

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
- [ ] Experimental pull-to-refresh opted in explicitly; module-specific rules followed.
