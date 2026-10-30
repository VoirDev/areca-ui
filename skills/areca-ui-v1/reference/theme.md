# Theme and tokens

Open this when setting up `ArecaThemeProvider`, choosing a token (a colour, a type role, a gap, a
corner, an elevation, a duration), re-theming an app for a brand, or building a region that is
always dark, at the latest `dev.voir.areca:foundation` release. Everything here lives in
`dev.voir.areca.theme` unless a row names another package.

## Set up the provider

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

```kotlin
fun ArecaThemeProvider(darkTheme: Boolean = false, baseColors: ArecaBaseColors = ArecaBaseColors(),
    colors: ArecaColors =
        if (darkTheme) { ArecaColors.dark(baseColors) } else { ArecaColors.light(baseColors) },
    radius: ArecaRadius = ArecaRadius(),
    shapes: ArecaShapes = ArecaShapes.from(radius), spacing: ArecaSpacing = ArecaSpacing(),
    elevation: ArecaElevation = ArecaElevation(), motion: ArecaMotion = ArecaMotion(),
    fonts: ArecaFonts = ArecaFonts(),
    baseTypography: ArecaBaseTypography = ArecaBaseTypography.from(fonts),
    inputMode: InputMode? = null,
    sizing: ArecaSizing = ArecaSizing.forInput(inputMode ?: /* outer or platform input */),
    typography: ArecaTypography = ArecaTypography.from(baseTypography, dense = sizing.dense),
    content: @Composable () -> Unit)
```

- Mount one provider at the root. It puts every token and every component default
  (`LocalButtonStyle`, `LocalCardStyle`, `LocalFieldStyle`, …) in scope; every Areca module reads
  them from there.
- Each parameter defaults to a value derived from the one above it — `baseColors` → `colors`,
  `radius` → `shapes`, `fonts` → `baseTypography` → `typography` — so override at the highest level
  that says what you mean, and everything below follows.
- `darkTheme` is only an input: pass `isSystemInDarkTheme()` or the user's setting. Nothing follows
  the system on its own. An explicit `colors` wins over `darkTheme` for the colours, but
  `ArecaTheme.isDark` still reports `darkTheme`.
- The provider paints nothing: give the root `bgCanvas`.
- It also seeds `LocalContentColor` (`fgBase`), `LocalTextStyle` (`baseTypography.md`, normal
  weight) and `LocalIconSize` (`IconSize.Small`), which slot content and plain `Text` / `Icon`
  inherit.
- Without a provider — previews, tests — every accessor returns the light defaults. Wrap a preview
  in the provider when the dark theme or an app token matters.
- `inputMode` (`InputMode.Touch` or `Pointer`) is reported through `ArecaTheme.window`. Leave it
  `null`: the root then takes the platform's (touch on Android and iOS, a pointer on the desktop)
  and a nested provider keeps the outer one. Pass it only for a device that breaks the rule, such as
  a tablet with a trackpad. The window's size is always read, never passed.
- `sizing` follows the input: `ArecaSizing.touch()` (controls 32/40/48/56/64dp, rows 32/44/56dp,
  48dp touch target) or `ArecaSizing.pointer()` (28/32/36/44/52dp, rows 28/36/44dp, no touch target,
  `dense = true`). Under `dense`, `typography.bodyMedium` and `labelLarge` are 14sp instead of 16sp.
  Every `XSize` preset reads its height from `sizing`, so `ButtonSize.Medium` is 48dp on a phone and
  36dp on a desktop — never hard-code a control height. Pass `sizing = ArecaSizing.pointer()` to
  force a dense screen in a touch app; in a size of your own, pick values with
  `sizing.select(touch = …, pointer = …)`.

## Reading tokens

Read through `ArecaTheme` inside composition; every accessor is `@Composable`.

| Accessor | Type | Holds |
| --- | --- | --- |
| `ArecaTheme.colors` | `ArecaColors` | Semantic colours for the direction in scope — what components and screens read |
| `ArecaTheme.typography` | `ArecaTypography` | Named text styles (roles) |
| `ArecaTheme.spacing` | `ArecaSpacing` | Spacing ramp and screen gutters |
| `ArecaTheme.sizing` | `ArecaSizing` | Control heights and touch target for the input in scope — what every size preset reads |
| `ArecaTheme.shapes` | `ArecaShapes` | Corner shapes; prefer these to `radius` |
| `ArecaTheme.radius` | `ArecaRadius` | Raw corner lengths, for a hand-drawn corner in a `Canvas` |
| `ArecaTheme.elevation` | `ArecaElevation` | Shadow elevations, a stacking order |
| `ArecaTheme.motion` | `ArecaMotion` | Durations and easings |
| `ArecaTheme.isDark` | `Boolean` | Theme direction; only for a decision no token carries (a light or dark scrim over arbitrary imagery) |
| `ArecaTheme.darkColors` | `ArecaColors` | This theme's colours in the dark: `colors` when already dark, else `baseColors` mapped by `ArecaColors.dark` — for naming a dark colour before an `ArecaDarkTheme` scope starts |
| `ArecaTheme.baseColors` | `ArecaBaseColors` | Raw palette ramps; only when no semantic colour fits (a chart series, an illustration) |
| `ArecaTheme.baseTypography` | `ArecaBaseTypography` | Raw type sizes; only when no role fits |
| `ArecaTheme.fonts` | `ArecaFonts` | Font families |
| `ArecaTheme.hostWindow` | `WindowClass` | The whole window's class, ignoring any `WindowClassBox` — choose an overlay's form by it (`AdaptiveDefaults.usesDialog(hostWindow)`) |
| `ArecaTheme.window` | `WindowClass` | Not a token: width and height classes, input mode and profile of the window or `WindowClassBox` in scope — branch layout on it (SKILL.md, "Lay out by window class") |

Never write `Color(0xFF…)`, `16.dp` padding, `14.sp`, `RoundedCornerShape(12.dp)` or `tween(300)` in
screen code. A component dimension no token names (a chart's height, a fixed table column) may be a
literal `Dp`; padding, gaps, colours, type and motion never are.

## Colours

`ArecaColors` is the meaning given to the palette. Light and dark share one palette and differ only
in which ramp step each token takes. The step columns below are the defaults, for knowing which part
of a brand ramp drives what.

### Grounds

| Token | For | Light · Dark |
| --- | --- | --- |
| `bgCanvas` | The page; nothing sits behind it | `baseWhite` · `neutral100` |
| `bgSoft` | Softest ground off the canvas: an expanded group, a highlighted row, a note block; `bgSubtle` controls still read on it | `neutral10` · between `neutral100` and `neutral90` |
| `bgSubtle` | Settings sections and grouped regions (the section card's default) | `neutral15` · `neutral90` |
| `bgMuted` | Tracks and inset wells inside a `bgSubtle` region; a disabled track | `neutral30` · `neutral80` |
| `bgSurface` | Raised containers: cards, sheets, popovers | `baseWhite` · `neutral90` |
| `bgContrast` | The inverted surface (dark in light, light in dark); pair with `fgOnContrast` | `neutral100` · `neutral10` |
| `bgContrastMuted` | Content layered on `bgContrast` | `neutral60` · `neutral40` |
| `bgOverlayDark` | Scrim darkening what is behind a modal | `neutral100` at 40% · 60% |
| `bgOverlayLight` | Scrim lightening dark imagery | `baseWhite` at 40% · 20% |
| `bgDisabled` | Fill of a control that cannot be used | `neutral20` · `neutral80` |

Do not put a `bgSubtle` region on another `bgSubtle` region.

### Tone fills

Six families — `Brand`, `Danger`, `Success`, `Warn`, `Info`, `Neutral` — each with three fills:

| Token | For |
| --- | --- |
| `bg{Family}Muted` | Tinted background: a chip, a banner, a selected row, an inline error or confirmation region (`bgDangerMuted` under a danger row) |
| `bg{Family}Base` | Solid fill: a primary or destructive button, a solid badge; pair with `fgOnFill` (`bgWarnBase` with `fgOnWarn`) |
| `bg{Family}Intense` | The solid fill under press or hover |

`bgWarnBase` is light in both themes, so its content is `fgOnWarn`, not `fgOnFill`.
`bgNeutral*` is for a status with no sentiment.

### Foregrounds

| Token | For |
| --- | --- |
| `fgBase` | Primary text and icons; 7:1 on every resting ground |
| `fgSubtle` | Secondary text: supporting lines, inactive labels; 4.5:1 |
| `fgMuted` | Tertiary text: placeholders, timestamps, decorative icons; only 3:1, so never the only carrier of meaning |
| `fgDisabled` | Text and icons of a disabled control |
| `fgOnContrast` | Content on `bgContrast` |
| `fgOnFill` | Content on any solid tone fill except `bgWarnBase`; white in both themes |
| `fgOnWarn` | Content on `bgWarnBase` |
| `fgBrand`, `fgDanger`, `fgSuccess`, `fgInfo`, `fgWarn` | Icons, graphics and rules in the family's colour; 3:1, not enough for type |
| `fgBrandText`, `fgDangerText`, `fgSuccessText`, `fgInfoText`, `fgWarnText` | Text in the family's colour (a field error is `fgDangerText`); 4.5:1 on the canvas and on the family's muted ground |
| `fgNeutral` | Neutral status text and icons (one token serves both) |

### Borders

| Token | For |
| --- | --- |
| `borderBase` | Default border: a card or input at rest |
| `borderStrong` | A border that must separate more, such as a hovered control |
| `borderIntense` | Strongest neutral border: a focused or pressed control |
| `borderSubtle` | Barely visible divider between rows of one group |
| `borderOnSubtle` | Hairline drawn on a `bgSubtle` region (between settings rows) |
| `borderDisabled` | Border of a disabled control |
| `borderDanger`, `borderSuccess`, `borderInfo`, `borderWarn` | Border of a control in that state |

### Accent

| Token | For |
| --- | --- |
| `accentBase` | Selection and focus: a checked control, a focus ring, an active tab, the text cursor |
| `accentStrong` | Accent under press or hover |
| `accentIntense` | Held or dragged state |
| `accentSubtle` | A selection indicator that should not compete with content |
| `accentMuted` | Tint behind selected content |
| `accentScale` | `List<Color>`: eight sequential shades for data (a heat-map day, a density cell); index `0` is faintest against the canvas in both themes, so "more" reads the same way in light and dark |

### Text roles

`Text(text, variant = TextVariant.X, color = TextColor.Y)` sets type by role. `TextColor` maps to
foregrounds: `Base` `fgBase`, `Subtle` `fgSubtle`, `Muted` `fgMuted`, `Disabled` `fgDisabled`,
`OnContrast` `fgOnContrast`, `OnFill` `fgOnFill`, and `Brand`, `Danger`, `Success`, `Info`,
`Warning` the family's `…Text` token. `TextVariant.X.style` and `TextColor.Y.color` resolve a role
where a raw `TextStyle` or `Color` is needed.

### Base palette

`ArecaBaseColors` is the raw palette: `baseWhite`, `baseBlack`, and ramps `brand`, `neutral`,
`green`, `blue`, `yellow`, `red`, each with steps `10`, `20` … `100` (lightest to darkest), plus
`neutral15`, the light theme's grouped-region fill. Ramps carry no direction: `ArecaColors.light`
reads them from the light end and `ArecaColors.dark` from the dark end, so replacing the palette
re-themes both directions. Read a step directly only when no semantic colour expresses the intent.

## Typography

| Role (`ArecaTheme.typography` / `TextVariant`) | Base step · size / line height · weight | For |
| --- | --- | --- |
| `display` / `Display` | `xl5` · 50/60 · Bold | Standalone display text; once per screen at most |
| `headingExtraLarge` / `HeadingExtraLarge` | `xl4` · 40/48 · Bold | A screen whose title is its main content |
| `headingLarge` / `HeadingLarge` | `xl3` · 32/38 · Bold | Page title |
| `headingMedium` / `HeadingMedium` | `xl2` · 26/30 · SemiBold | Screen heading |
| `headingSmall` / `HeadingSmall` | `xl` · 22/26 · SemiBold | Section heading |
| `headingExtraSmall` / `HeadingExtraSmall` | `lg` · 18/24 · SemiBold | Subsection title inside a card or list group |
| `bodyExtraLarge` / `BodyExtraLarge` | `xl` · 22/26 · Medium | Emphasised body copy that is not a heading: a summary line |
| `bodyLarge` / `BodyLarge` | `lg` · 18/24 | Lead paragraph; body on a spacious screen |
| `bodyMedium` / `BodyMedium` | `md` · 16/20 | Default body copy; a row's title |
| `bodySmall` / `BodySmall` | `sm` · 14/17 | Compact body inside dense components; a row's subtitle |
| `bodyExtraSmall` / `BodyExtraSmall` | `xs` · 12/14 | Supporting copy; a row's caption |
| `labelLarge` / `LabelLarge` | `md` · 16/20 · Medium | Label on a large control |
| `labelMedium` / `LabelMedium` | `sm` · 14/17 · Medium | Default control label: buttons, fields; a section header |
| `labelSmall` / `LabelSmall` | `xs` · 12/14 · Medium | Label on a dense control: a badge, a chip |
| `caption` / `Caption` | `sm` · 14/17 · Medium, 0.15sp tracking | Short explanation under a field or an image |

`ArecaBaseTypography` is the raw scale, size and line height only: `xxs` 10/12, `xs` 12/14, `sm`
14/17, `md` 16/20, `lg` 18/24, `xl` 22/26, `xl2` 26/30, `xl3` 32/38, `xl4` 40/48, `xl5` 50/60,
`xl6` 64/72, `xl7` 72/90 (sp).

`ArecaFonts(primary, secondary)`: `primary` sets every style; `secondary` is used by nothing by
default, for opting single styles into a second voice. Both default to `FontFamily.Default`.

## Spacing

| Token | Dp | For |
| --- | --- | --- |
| `none` | 0 | A parameter that must take a value but should add no space |
| `xs2` | 2 | Hairline separation, a badge's inner gap |
| `xs` | 4 | Tightest gap between two elements |
| `sm` | 6 | Gap inside a dense control: icon and label |
| `md` | 8 | Default gap between related elements |
| `lg` | 12 | Padding inside a small container: a badge, a chip |
| `xl` | 16 | Padding inside a standard container |
| `xl2` | 20 | Padding inside a roomy container |
| `xl3` | 24 | Gap between grouped blocks within a section |
| `xl4` | 32 | Gap between sections |
| `xl5` | 40 | Before a major section break |
| `xl6` | 48 | Space above a page heading |
| `xl7` | 64 | Rhythm of sparse, full-height layouts |
| `xl8` | 80 | Empty-state and onboarding breathing room |
| `xl9` | 96 | Hero and splash layouts |
| `horizontalContentInsets` | 16 | Screen gutter between content and the window's sides, compact window |
| `mediumHorizontalContentInsets` | 24 | Side gutter on a medium window (600–839dp) |
| `expandedHorizontalContentInsets` | 32 | Side gutter from an expanded window (840dp) up |
| `verticalContentInsets` | 24 | Screen gutter at the top and bottom |

Apply the gutters with `Modifier.horizontalContentInsets()`, `verticalContentInsets()` or
`contentInsets()` (both), not by reading the tokens, so the gutter stays one decision. The modifiers
pick the side gutter for the window class in scope (inside a `WindowClassBox`, the box's).

## Corners

| `radius` (Dp) | `shapes` | For |
| --- | --- | --- |
| `none` 0 | `none` | Square corners |
| `xs` 2 | `xs` | Hairline elements: a progress track |
| `sm` 4 | `sm` | Small controls: badges, chips, swatches |
| `md` 8 | `md` | Interactive surfaces: inputs, list rows |
| `lg` 16 | `lg` | Containers: cards, settings sections, notices, popovers |
| `xl` 24 | `xl` | Sheets and modals |
| `xl2` 32 | `xl2` | Full-bleed surfaces that still show a corner |
| — | `full` | Fully rounded: a circle on a square, a pill on a wide element (people, capsules) |
| — | `tile` | A squircle (`SquircleShape(exponent = 2.8f)`) for a small glyph tile: settings icons, a row's leading tile |
| — | `button` | Every `ButtonStyle` factory's default and a plain bar control's press shape; `md` by default |

`ArecaShapes.from(radius)` builds the set; `SquircleShape(exponent = 4f, steps = 120)` is in
`dev.voir.areca.shape`. Pick one corner per role for the whole app (see the card-corner rules in
SKILL.md); never pass `shape =` to one card on a screen.

## Elevation

| Token | Dp | For |
| --- | --- | --- |
| `none` | 0 | Flush, no shadow |
| `xs` | 1 | A surface separating from an adjacent one: a sticky bar |
| `sm` | 2 | Lifted by interaction: a dragged row, a toggle thumb |
| `md` | 4 | Anchored transient surfaces: dropdowns, popovers, tooltips |
| `lg` | 8 | Floating surfaces that own focus: a toast, a floating tab bar |
| `xl` | 16 | Modal surfaces over a scrim |

Elevation is the `elevation` of `Modifier.shadow`; it does not tint. A surface sits one step above
what it overlaps.

## Motion

| Token | Value | For |
| --- | --- | --- |
| `instant` | 100 ms | A state change under the finger: press tint, focus ring, checkbox mark |
| `fast` | 150 ms | A small element changing in place: a chevron turning, a colour swapping |
| `medium` | 200 ms | The default: a container resizing, a popover appearing, a crossfade |
| `slow` | 300 ms | A surface travelling far: a sheet, a drawer, a full-screen transition |
| `deliberate` | 450 ms | A one-off moment meant to be watched: a success confirmation |
| `standard` | `FastOutSlowInEasing` | Default easing, for changes in place |
| `enter` | `LinearOutSlowInEasing` | Content arriving |
| `exit` | `FastOutLinearInEasing` | Content leaving |
| `pressIn` | `CubicBezierEasing(0.2, 0.7, 0.4, 1)` | Something expanding from the touch point |
| `fade` | `CubicBezierEasing(0.32, 0.72, 0, 1)` | Fades and crossfades: hover tints, focus rings, colour swaps |
| `springStiffness` | `Spring.StiffnessMediumLow` | Stiffness of `slide()`, the spring a mark travels between places on |
| `reduceMotion` | `false` | The user asked for less motion: sliding marks fade between places instead. Not read from the platform; pass it in |

Pick a duration by how far things move, not by component. Build a spec with
`fun <T> Duration.tween(easing: Easing = FastOutSlowInEasing): TweenSpec<T>`:
`ArecaTheme.motion.medium.tween(ArecaTheme.motion.standard)`.
A thumb or indicator travelling between places uses `ArecaTheme.motion.slide()` instead: a
critically damped spring, interruptible without a jump in speed. `SlidingSelector`, `Tabs` and
`FloatingTabBar` already do.

## Icon sizes

`IconSize` (`dev.voir.areca.foundation.icon`): `ExtraSmall` 12, `Small` 16 (the default
`LocalIconSize`), `Medium` 20, `Large` 24, `ExtraLarge` 32 (dp). Pass one as `Icon(size = …)` or
provide `LocalIconSize`.

## Change the theme

```kotlin
val brand = ArecaBaseColors(
    brand10 = Color(0xFFF2FBF8), brand20 = Color(0xFFDDF4EC), /* … */ brand100 = Color(0xFF04261C),
)
val radius = ArecaRadius(md = 10.dp, lg = 20.dp)
val shapes = ArecaShapes.from(radius).copy(button = ArecaShapes.from(radius).full)

ArecaThemeProvider(
    darkTheme = isSystemInDarkTheme(),
    baseColors = brand,                              // re-themes light and dark at once
    radius = radius,
    shapes = shapes,                                 // capsule buttons everywhere
    spacing = ArecaSpacing(horizontalContentInsets = 20.dp),
    fonts = ArecaFonts(primary = brandSans),         // re-types every style
) { App() }
```

- **Brand colour.** Pass `baseColors` with the brand ramp (and any other ramp) replaced; both
  directions follow. Keep the ramp's lightness order, `10` lightest to `100` darkest, or the
  contrast guarantees above no longer hold.
- **One colour token.** `colors = ArecaColors.light(brand).copy(accentBase = …)` replaces the
  mapping for one direction only, so pass a matching `ArecaColors.dark(brand).copy(…)` when
  `darkTheme` is true. Prefer changing the palette.
- **Corners.** `radius` re-derives `shapes`; `shapes = ArecaShapes.from(radius).copy(…)` changes
  one role (`md = SquircleShape()`, `button = …full`).
- **Type.** `fonts` re-types everything; `baseTypography = ArecaBaseTypography.from(fonts).copy(md =
  …)` retunes one size; `typography = ArecaTypography.from(baseTypography).copy(display = …)` one
  role.
- **Spacing, elevation, motion.** `ArecaSpacing(...)`, `ArecaElevation(...)` and `ArecaMotion(...)`
  have defaults for every field; pass the ones to change.
- **Component defaults app-wide.** Provide the component's local just inside the provider:
  `CompositionLocalProvider(LocalButtonStyle provides ButtonStyle.solid(shape = …),
  LocalCardStyle provides CardStyle.SoftNeutral) { … }`. A nested `ArecaThemeProvider` or
  `ArecaDarkTheme` reseeds every local, so overrides above it do not reach inside.

## Dark regions and the system bars

```kotlin
@Composable
fun PhotoViewer(photo: Painter, onClose: () -> Unit) {
    ArecaDarkTheme {
        SystemBars(color = ArecaTheme.colors.bgCanvas)
        BaseLayout(
            bg = ArecaTheme.colors.bgCanvas,
            topBar = { TopBar("Photo", leading = { CloseAction(onClick = onClose) }) },
        ) { padding -> Image(photo, null, Modifier.padding(padding).fillMaxSize()) }
    }
}
```

- `ArecaDarkTheme(content)` puts content in the dark theme whatever the theme around it, keeping the
  app's palette, shapes, spacing, elevation, motion and type, and re-resolving component defaults
  for dark. Inside an already-dark theme it changes nothing. A nested
  `ArecaThemeProvider(darkTheme = true)` instead resets everything to the library defaults.
- `ArecaTheme.darkColors` names a dark colour before the `ArecaDarkTheme` scope begins, such as a
  style parameter's default resolved at the call site.
- `SystemBars(color: Color, lightIcons: Boolean = color.luminance() < 0.5f)`
  (`dev.voir.areca.platform`) paints the system bars while composed and restores them when it
  leaves. It acts on Android (inside a dialog, on the dialog's window, which also loses its dim);
  on iOS and the desktop it does nothing. From Android 15 the window is edge to edge and the screen's
  own ground shows through, so draw it behind the bars. A dialog that draws behind them takes
  `edgeToEdgeDialogProperties()` (`dev.voir.areca.foundation.overlay`).
- Build styles (`CardStyle.Outlined`, `ButtonStyle.soft()`) inside the dark scope: presets and
  factories read the theme where they are called.
