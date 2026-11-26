# Theme and tokens

Open this when setting up `ArecaThemeProvider`, choosing a token (a surface role, a colour, a type
role, a gap, a corner, an elevation, a duration), re-theming an app for a brand, or building a region that is
always dark, at the latest `dev.voir.areca:foundation` release. Everything here lives in
`dev.voir.areca.theme` unless a row names another package.

## Set up the provider

```kotlin
@Composable
fun App() {
    ArecaThemeProvider(darkTheme = isSystemInDarkTheme()) {
        AppNavigation()
    }
}
```

```kotlin
fun ArecaThemeProvider(darkTheme: Boolean = false, baseColors: ArecaBaseColors = ArecaBaseColors(),
    colors: ArecaColors =
        if (darkTheme) { ArecaColors.dark(baseColors) } else { ArecaColors.light(baseColors) },
    surfaceLayout: SurfaceLayout = SurfaceLayout.Grouped,
    surfaces: ArecaSurfaces = ArecaSurfaces.from(baseColors, surfaceLayout, darkTheme),
    hues: ArecaHues = ArecaHues(),
    labels: ArecaLabels = ArecaLabels(),
    radius: ArecaRadius = ArecaRadius(),
    shapes: ArecaShapes = ArecaShapes.from(radius), spacing: ArecaSpacing = ArecaSpacing(),
    elevation: ArecaElevation = ArecaElevation(), motion: ArecaMotion = ArecaMotion(),
    fonts: ArecaFonts = ArecaFonts(),
    baseTypography: ArecaBaseTypography = ArecaBaseTypography.from(fonts),
    inputMode: InputMode? = null,
    sizing: ArecaSizing = ArecaSizing.forInput(inputMode ?: /* outer or platform input */),
    typography: ArecaTypography = ArecaTypography.from(baseTypography, dense = sizing.dense),
    componentDefaults: ComponentDefaults = ComponentDefaults.None,
    content: @Composable () -> Unit)

fun interface ComponentDefaults {
    @Composable fun provide(): Array<ProvidedValue<*>>
    companion object { val None: ComponentDefaults }   // provides nothing
}
```

- Mount one provider at the root. It puts every token and every component default
  (`LocalButtonStyle`, `LocalCardStyle`, `LocalFieldStyle`, …) in scope; every Areca module reads
  them from there.
- Each parameter defaults to a value derived from the one above it — `baseColors` → `colors`,
  `baseColors` + `surfaceLayout` + `darkTheme` → `surfaces`, `radius` → `shapes`, `fonts` → `baseTypography` → `typography` — so override at the highest level
  that says what you mean, and everything below follows.
- `darkTheme` is only an input: pass `isSystemInDarkTheme()` or the user's setting. Nothing follows
  the system on its own. An explicit `colors` wins over `darkTheme` for the colours, but
  `ArecaTheme.isDark` still reports `darkTheme`.
- `surfaceLayout` picks the grounds: `SurfaceLayout.Grouped` (default) is a grey page with white
  cards, `SurfaceLayout.Plain` a white page with grey cards. See [Surfaces](#surfaces).
- The provider paints nothing, and the root needs no background: each screen's `BaseLayout` paints
  its page in the layout's page ground.
- `componentDefaults` holds the app's own component locals:
  `ComponentDefaults { arrayOf(LocalButtonSize provides ButtonSize.Large, …) }`. It runs inside the
  theme, after the theme's own seeds, so its styles read the theme's tokens; `ArecaDarkTheme` runs it
  again in its dark region. Provide app-wide locals here rather than in a `CompositionLocalProvider`
  around the content, which does not reach inside an `ArecaDarkTheme`.
- The theme seeds `LocalAvatarStyle`, `LocalBadgeStyle`, `LocalProgressBarStyle`,
  `LocalSkeletonStyle`, `LocalSlidingSelectorStyle`, `LocalTabsStyle`, `LocalWheelPickerStyle` and
  `LocalPageCounterStyle` with `null`, so each component builds its style where it is drawn, from
  the surface under it. `LocalNoticeStyle` is `null` too (a notice derives its style from its tone
  and emphasis), and so are `LocalConfirmSheetStyle` and `LocalConfirmSheetSize` (a confirm sheet
  builds them from the flow modal defaults in scope). `LocalCardStyle` is seeded with
  `CardStyle.Default`.
- It also seeds `LocalContentColor` (`fgBase`), `LocalTextStyle` (`typography.bodyMedium`, normal
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
| `ArecaTheme.surface` | `ArecaSurface` | Roles of the surface the content in scope is drawn on — `ground`, `raised`, `well`, `onWell`, `separator`, `field`, `band`; what components read for fills |
| `ArecaTheme.surfaceLevel` | `SurfaceLevel` | `Page`, `Card` or `Inset`: where the content in scope sits |
| `ArecaTheme.surfaces` | `ArecaSurfaces` | `layout`, the `screen` and `sheet` ladders, `bar` |
| `ArecaTheme.colors` | `ArecaColors` | Semantic colours for the direction in scope — what components and screens read |
| `ArecaTheme.hues` | `ArecaHues` | Categorical colours, one per kind of thing, for tiles that tell unrelated rows apart — see Hues |
| `ArecaTheme.labels` | `ArecaLabels` | The words components share (back, close, search, …) in the app's language — see Labels |
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
| `ArecaTheme.hostWindow` | `WindowClass` | The whole window's class, ignoring any `WindowClassBox` — what depends on how the device is held reads it (`actionReach`); never switch a sheet to a dialog by it |
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
| `bgCanvas` | The lightest ground in light, the darkest in dark | `baseWhite` · `neutral100` |
| `bgSoft` | A step off `bgCanvas` | `neutral10` · between `neutral100` and `neutral90` |
| `bgSubtle` | The grey of a grouped page or a plain card | `neutral15` · `neutral90` |
| `bgMuted` | A step further back | `neutral30` · `neutral80` |
| `bgSurface` | A raised container in a fixed colour | `baseWhite` · `neutral90` |
| `bgContrast` | The inverted surface (dark in light, light in dark); pair with `fgOnContrast` | `neutral100` · `neutral10` |
| `bgContrastMuted` | Content layered on `bgContrast` | `neutral60` · `neutral40` |
| `bgOverlayDark` | Scrim darkening what is behind a modal | `neutral100` at 40% · 60% |
| `bgOverlayLight` | Scrim lightening dark imagery | `baseWhite` at 40% · 20% |
| `bgDisabled` | Fill of a control that cannot be used | `neutral20` · `neutral80` |

These are fixed palette steps. No component reads them for a page, a card, a well or a hairline;
components read the roles of `ArecaTheme.surface`, and screen code does the same for anything drawn
on a surface. A ground token is right only for a fill that must stay one colour wherever it is.

### Tone fills

Six families — `Brand`, `Danger`, `Success`, `Warn`, `Info`, `Neutral` — each with three fills:

| Token | For |
| --- | --- |
| `bg{Family}Muted` | Tinted background: a chip, a banner, a selected row, an inline error or confirmation region (`bgDangerMuted` under a danger row) |
| `bg{Family}Base` | Solid fill: a primary or destructive button, a solid badge; pair with `fgOnFill` (`bgWarnBase` with `fgOnWarn`) |
| `bg{Family}Intense` | The solid fill under press or hover |

`bgWarnBase` is light in both themes, so its content is `fgOnWarn`, not `fgOnFill`.
In the dark theme every tone's `bg{Family}Muted` (all but `Neutral`) and `accentMuted` is `neutral90`
with 14% of the family's colour mixed in (its `60` step, yellow's `50`); `bgNeutralMuted` and
`bgNeutralBase` are `neutral80`.
`bgNeutral*` is for a status with no sentiment.

### Foregrounds

| Token | For |
| --- | --- |
| `fgBase` | Primary text and icons; 7:1 on every resting ground |
| `fgSubtle` | Secondary text: supporting lines, inactive labels; 4.5:1 |
| `fgMuted` | Tertiary text: placeholders, timestamps, decorative icons, section footers; never the only carrier of meaning. Light: a fifth of the way from `neutral60` to `neutral70`, 4.5:1 on the grey page of a grouped layout |
| `fgDisabled` | Text and icons of a disabled control |
| `fgOnContrast` | Content on `bgContrast` |
| `fgOnFill` | Content on any solid tone fill except `bgWarnBase`; white in both themes |
| `fgOnWarn` | Content on `bgWarnBase` |
| `fgBrand`, `fgDanger`, `fgSuccess`, `fgInfo`, `fgWarn` | Icons, graphics and rules in the family's colour; 3:1, not enough for type |
| `fgBrandText`, `fgDangerText`, `fgSuccessText`, `fgInfoText`, `fgWarnText` | Text in the family's colour (a field error is `fgDangerText`); 4.5:1 on the canvas and on the family's muted ground. Light `fgBrandText` is the first of `brand70`, `brand80`, `brand90` that reads at 4.5:1 on `brand20` (`bgBrandMuted`), so a light brand such as a yellow keeps soft buttons and brand text readable; the default brand takes `brand70` |
| `fgNeutral` | Neutral status text and icons (one token serves both) |

### Borders

| Token | For |
| --- | --- |
| `borderBase` | Default border: a card or input at rest |
| `borderStrong` | A border that must separate more, such as a hovered control |
| `borderIntense` | Strongest neutral border: a focused or pressed control |
| `borderSubtle` | Barely visible divider between rows of one group |
| `borderOnSubtle` | Hairline drawn on `bgSubtle`. Components draw hairlines in `ArecaTheme.surface.separator` |
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

### Hues

`ArecaHues` holds ten categorical colours: `blue`, `indigo`, `purple`, `pink`, `red`, `orange`,
`yellow`, `green`, `teal`, `gray`, and `all`, the ten in that order for assigning one by index. Each
is one colour in both themes, dark enough for a white glyph at 3:1 or more.

```kotlin
@Immutable
data class ArecaHues(
    val blue: Color = Color(0xFF0A69FA), val indigo: Color = Color(0xFF4F5BD5),
    val purple: Color = Color(0xFF7C3AED), val pink: Color = Color(0xFFD6336C),
    val red: Color = Color(0xFFDB340B), val orange: Color = Color(0xFFD9480F),
    val yellow: Color = Color(0xFFB26205), val green: Color = Color(0xFF008557),
    val teal: Color = Color(0xFF0C7C8C), val gray: Color = Color(0xFF727679),
) { val all: List<Color> }
```

| Draw a hue with | Gives |
| --- | --- |
| `FeaturedIconStyle.solidHue(hue)` | The hue filled, the glyph `fgOnFill` |
| `FeaturedIconStyle.softHue(hue)` | The hue mixed into the surface (`field` in light, `ground` in dark), the glyph a deeper step (a lighter one in dark) |
| `SettingsRowIcon(imageVector, hue = …, soft = false)` | A settings row's leading tile, solid or soft |

The rule: a hue means only "a different kind of thing" — the rows of a settings menu, categories of
spending, tags. Pick one per concept and keep it wherever the concept appears. A colour that means
something — brand, danger, success, warning, information — is an `ActionColor` or a tone token, never
a hue. Replace the set with `ArecaThemeProvider(hues = ArecaHues(…))`; `ArecaDarkTheme` keeps it. Do
not keep an app `SettingsTint` / `HueTint` palette, and do not recolour the `blue` ramp so `Info`
reads as an accent.

## Surfaces

Every fill sits on a surface. A surface hands out colours by role, and every component reads the
role of the surface it is drawn on, so one card, field or chip is right on a grey page, in a white
card and in a sheet.

```kotlin
enum class SurfaceLayout { Grouped, Plain }
enum class SurfaceLevel { Page, Card, Inset }
enum class SurfaceRoot { Screen, Sheet }
enum class SurfaceRole { Ground, Raised, Well, OnWell, Separator, Field, Band }

data class ArecaSurface(ground: Color, raised: Color, well: Color, onWell: Color,
    separator: Color, field: Color, band: Color = well) {
    operator fun get(role: SurfaceRole): Color }
data class SurfaceLadder(page: ArecaSurface, card: ArecaSurface, inset: ArecaSurface) {
    fun at(level: SurfaceLevel): ArecaSurface }
data class ArecaSurfaces(layout: SurfaceLayout, screen: SurfaceLadder, sheet: SurfaceLadder,
    bar: Color) {
    fun ladder(root: SurfaceRoot): SurfaceLadder
    companion object {
        fun from(baseColors: ArecaBaseColors, layout: SurfaceLayout, darkTheme: Boolean): ArecaSurfaces
    }
}

@Composable fun RaisedSurface(content: @Composable () -> Unit)
@Composable fun ProvideSurface(level: SurfaceLevel,
    root: SurfaceRoot = /* the current ladder */, content: @Composable () -> Unit)
@Composable fun ProvideSurface(fill: Color, content: @Composable () -> Unit)
// The surface a fill of your own stands for: wrap the content of a bar painted surfaces.bar, or a
// panel, so soft buttons and tracks in it take that surface's well and stay visible in dark.
```

| Role | For | Read by |
| --- | --- | --- |
| `ground` | The surface's own fill | `BaseLayout`'s page, `TopBar`, `Table` rows, `WheelPicker`, a skeleton's highlight, chart marker rings |
| `raised` | A card laid on it | `Card` (default style), a settings `Section`, a subtle `Notice`, a `ContentList` inset group, `ExpandableListItemContainer.Soft` |
| `well` | Something set into it | Filled, soft and solid-neutral fields; soft `Neutral` buttons, icon buttons and featured icons; `CardStyle.SoftNeutral`; `SlidingSelector`, `Tabs` and `ProgressBar` tracks; `Skeleton`; `WheelPicker`'s highlight; empty `Avatar`; `PageCounter`; `BadgeStyle.FilledChip`; picture placeholders |
| `onWell` | Something raised inside a well | `SlidingSelector` and `Tabs` thumbs |
| `separator` | A hairline on it | Settings section, `List`, `ListItemColumn` and inset-group hairlines |
| `field` | An outlined field or a stepper | `FieldStyle.outlined()`, a focused filled field, `NumberStepper`, a soft `Badge`; white anywhere in light |
| `band` | A strip that closes the surface along its edge | A neutral card footer (`CardFooterStyle.soft()`), a settings section's band. Defaults to `well`; in light a white surface's band is `neutral30`, a step past the well, so it does not run into a grey page |

Grounds of each level:

| Level | `Grouped` (light) | `Plain` (light) | Dark, both layouts |
| --- | --- | --- | --- |
| Screen page | `neutral15` | white | `neutral100` |
| Card on it | white | `neutral15` | `neutral90` |
| Inset in the card | `neutral10` | white | between `neutral90` and `neutral80` |
| Sheet page | white | white | a step above the screen page |
| `surfaces.bar` | white | white | `neutral90` |

- Levels run Page → Card → Inset → Card …. `BaseLayout` puts its content on the page;
  `FlowModal`, `Dialog` and `DropdownPopup` on the sheet ladder's page (white in light); `Card`,
  `Section`, `Notice` and inset groups put theirs on the level whose ground is their fill, normally
  one up. A transparent fill (`CardStyle.Outlined`, `GhostNeutral`) keeps the current surface.
- On a sheet's ground a `Section` with no `bg` of its own draws flat — no fill, no corners, rows
  edge to edge, hairlines from the text — because a sheet is already a segment. A `Card` there is
  emphasis only.
- A fill of your own: `Modifier.background(ArecaTheme.surface.raised, ArecaTheme.shapes.surface)`
  and `RaisedSurface { … }` around what it holds. `ProvideSurface(SurfaceLevel.Card) { … }` puts
  content at a chosen level — a white side panel across a grey page; pass
  `root = SurfaceRoot.Sheet` for a sheet-like panel of your own.
- `TabBar`, `BottomActionBar`, `NavRail` and the `AdaptiveNavigation` column take `surfaces.bar`.
- `ArecaSurfaces.from` reads the palette's neutral ramp, so re-theming `baseColors` re-themes the
  surfaces. Pass `surfaces = ArecaSurfaces(…)` to set every ground by hand, one value per direction.

## Typography

| Role (`ArecaTheme.typography` / `TextVariant`) | Base step · size / line height · weight | For |
| --- | --- | --- |
| `display` / `Display` | `xl5` · 50/60 · Bold | Standalone display text; once per screen at most |
| `headingExtraLarge` / `HeadingExtraLarge` | `xl4` · 40/48 · Bold | A screen whose title is its main content |
| `headingLarge` / `HeadingLarge` | `xl3` · 32/40 · Bold | Page title |
| `headingMedium` / `HeadingMedium` | `xl2` · 26/32 · SemiBold | Screen heading |
| `headingSmall` / `HeadingSmall` | `xl` · 22/28 · SemiBold | Section heading |
| `headingExtraSmall` / `HeadingExtraSmall` | `lg` · 18/26 · SemiBold | Subsection title inside a card or list group |
| `bodyExtraLarge` / `BodyExtraLarge` | `xl` · 22/28 · Medium | Emphasised body copy that is not a heading: a summary line |
| `bodyLarge` / `BodyLarge` | `lg` · 18/26 | Lead paragraph; body on a spacious screen |
| `bodyMedium` / `BodyMedium` | `md` · 16/24 | Default body copy; a row's title |
| `bodySmall` / `BodySmall` | `sm` · 14/20 | Compact body inside dense components; a row's subtitle |
| `bodyExtraSmall` / `BodyExtraSmall` | `xs` · 12/16 | Supporting copy; a row's caption |
| `labelLarge` / `LabelLarge` | `md` · 16/24 · Medium | Label on a large control |
| `labelMedium` / `LabelMedium` | `sm` · 14/20 · Medium | Default control label: buttons, fields; a section header |
| `labelSmall` / `LabelSmall` | `xs` · 12/16 · Medium | Label on a dense control: a badge, a chip |
| `overline` / `Overline` | `xs` · 12/16 · Medium, 0.06em tracking | A small label above what it names: a group above its rows, an eyebrow above a title |
| `caption` / `Caption` | `sm` · 14/20 · Medium, 0.15sp tracking | Short explanation under a field or an image |

`ArecaBaseTypography` is the raw scale, size and line height only, line heights on a 4pt grid:
`xxs` 10/12, `xs` 12/16, `sm` 14/20, `md` 16/24, `lg` 18/26, `xl` 22/28, `xl2` 26/32, `xl3` 32/40,
`xl4` 40/48, `xl5` 50/60, `xl6` 64/72, `xl7` 72/80 (sp). Steps leave their text direction unset:
Areca's `Text` lays a paragraph out in the interface's direction (`LocalLayoutDirection`) unless its style sets one, so an amount written `1 234,50 د.إ` keeps its order in an English interface. A symbol in another script *before* the figures (`د.إ 1 234,50`) is reordered by the bidi algorithm whatever the paragraph's direction — draw amounts with `AmountText`, which isolates each part. `ArecaTypography(overline = …)` defaults to `labelSmall` tracked out.

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
| `lg` 16 | `lg` | Containers: popovers |
| `xl` 24 | `xl` | Sheets and modals |
| `xl2` 32 | `xl2` | Full-bleed surfaces that still show a corner |
| — | `full` | Fully rounded: a circle on a square, a pill on a wide element (people, capsules) |
| — | `tile` | A squircle (`SquircleShape(exponent = 2.8f)`) for a small glyph tile: settings icons, a row's leading tile |
| — | `button` | Every `ButtonStyle` factory's default and a plain bar control's press shape; `md` by default |
| — | `surface` | Every surface that groups content: each `CardStyle` preset, a settings section, a `Notice`, a `ContentList` inset group, `ExpandableListItemContainer.Soft`; `lg` by default. `ArecaShapes(surface: Shape = lg)` |

`ArecaShapes.from(radius)` builds the set; `SquircleShape(exponent = 4f, steps = 120)` is in
`dev.voir.areca.shape`. Pick one corner per role for the whole app (see the card-corner rules in
SKILL.md) — `shapes = ArecaShapes.from(radius).copy(surface = …)` changes every card corner; never
pass `shape =` to one card on a screen.

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
- **Grounds.** `surfaceLayout = SurfaceLayout.Plain` for a white page with grey cards;
  `surfaces = ArecaSurfaces(…)` to set every ground.
- **Category colours.** `hues = ArecaHues(purple = …)` replaces one or all of the tile hues.
- **Component defaults app-wide.** Pass them as `componentDefaults = ComponentDefaults {
  arrayOf(LocalButtonStyle provides ButtonStyle.solid(shape = …), LocalCardStyle provides
  CardStyle.Outlined) }`. `ArecaDarkTheme` applies them again inside; a local provided in a
  `CompositionLocalProvider` above an `ArecaDarkTheme` does not reach inside, and a nested
  `ArecaThemeProvider` reseeds every local.

## Dark regions and the system bars

```kotlin
@Composable
fun PhotoViewer(photo: Painter, onClose: () -> Unit) {
    ArecaDarkTheme {
        SystemBars(color = ArecaTheme.colors.bgCanvas)
        BaseLayout(
            topBar = { TopBar("Photo", leading = { CloseAction(onClick = onClose) }) },
        ) { padding -> Image(photo, null, Modifier.padding(padding).fillMaxSize()) }
    }
}
```

- `ArecaDarkTheme(content)` puts content in the dark theme whatever the theme around it, keeping the
  app's palette, surface layout, hues, labels, shapes, spacing, sizing, elevation, motion, type and
  `componentDefaults`,
  and re-resolving component defaults and surfaces for dark. Inside an already-dark theme it changes nothing. A nested
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

## Labels

`ArecaThemeProvider(labels = ArecaLabels(…))` gives the words components share in the app's
language: `back`, `close`, `dismiss`, `busy`, `cancel`, `more`, `remove`, `search`, `clearSearch`,
`showPassword`, `hidePassword`. Read them with `ArecaTheme.labels`. Every component and flow whose
default is one of these words reads it from here; a value passed at the call site still wins, and
`ArecaDarkTheme` keeps them. Component-specific copy stays in each component's `…Labels`.
