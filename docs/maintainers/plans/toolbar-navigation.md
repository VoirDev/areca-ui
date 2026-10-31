# Plan: the edge column for tablets and foldables

Status: phases 1 to 4 implemented: the edge column and `AdaptiveToolbar`; folds in `dev.voir.areca.window`; the column's side from the camera cutout; actions placed by `ActionReach`. Prototypes:
[Areca Tablet & Foldable Chrome](https://claude.ai/artifact/9ksxnnahjLnfViAQucEy9C).
Reference: the foldable layout that stacks every piece of chrome along the camera's edge (camera
cutout, status, toolbar, tab bar and search).

## Goal

On medium and wider windows, the chrome leaves the top and bottom edges and stacks in one floating
**edge column** on the side where the camera sits (the end side by default). The column floats over
full-bleed content in two clusters:

- **Top cluster**, under the cutout: status, then the screen's toolbar as round buttons.
- **Bottom cluster**, within reach of the thumb: the destinations as a vertical tab capsule, with
  search as its own button under it.

The middle of the column stays empty. The sidebar floats in from the same edge. Phones keep the
bottom bar and a top bar.

`dev.voir.areca.foundation.adaptive` is not in any release yet (v1.0.4 predates it), so its API can
change shape now without a deprecation path.

## What the prototypes settle

| Window or posture | Navigation | Toolbar and status |
| --- | --- | --- |
| Compact width | Bottom `TabBar` (as today) | The screen's own `TopBar` |
| Compact height | `NavRail` without labels (as today; open question 3) | The screen's own `TopBar` |
| Medium and wider | Vertical tab capsule at the foot of the edge column | Top of the column |
| Sidebar open | A floating panel beside the column, with labelled destinations, extra sections and full status; the capsule hides | The toggle shows as on |
| Vertical fold | The column on the end edge never crosses the fold; panes split at it | Unchanged |
| Horizontal fold (tabletop) | The bottom cluster sits below the fold | The top cluster stays above it |
| Over five destinations | The last item becomes More, a menu that opens toward the content | — |

Measurements:

- Under touch the column is 56dp wide (48dp buttons in a 4dp capsule padding) with a 12dp inset;
  under a pointer it is 48dp wide with 40dp buttons. Parts are 8dp apart.
- Text and controls in the content end `EdgeColumnSize.clearance` from the edge: 80dp under touch,
  72dp under a pointer. Backgrounds and media run under the column.
- The toolbar holds at most three buttons, top down: back (or the sidebar toggle), the screen's
  actions, then More.
- Tabs are icons only. A tab's name shows as a label toward the content on long press and on
  pointer hover.
- App status (synced, offline, changes waiting) is one icon under the system status. It gains a
  badge when it needs attention. Its full text lives in the sidebar footer.

## API changes

### 1. `AdaptiveNavigation` gains the edge column

```kotlin
public enum class AdaptiveNavigationLayout {
    BottomBar, Rail, Sidebar,
    /** The edge column: a medium window and wider, unless the app keeps the rail. */
    EdgeColumn,
}

public enum class EdgeColumnSide {
    /** The side with the display cutout, else End. */
    Auto,
    Start,
    End,
}

@Composable
public fun <T : Any> AdaptiveNavigation(
    destinations: List<NavDestination<T>>,
    selectedValue: T?,
    onValueSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    style: AdaptiveNavigationStyle = AdaptiveNavigationStyle.EdgeColumn, // open question 1
    edgeSide: EdgeColumnSide = EdgeColumnSide.Auto,
    sidebarVisible: Boolean = false,
    onSidebarVisibleChange: ((Boolean) -> Unit)? = null,
    onSearch: (() -> Unit)? = null,                                // search button under the tabs
    status: (@Composable EdgeStatusScope.() -> Unit)? = null,      // icon in the column, text in the sidebar
    sidebarSections: (@Composable ColumnScope.() -> Unit)? = null, // Accounts, saved filters
    footer: (@Composable AdaptiveNavigationScope.() -> Unit)? = null,
    bottomBar: (@Composable AdaptiveNavigationScope.() -> Unit)? = null,
    content: @Composable AdaptiveNavigationScope.() -> Unit,
)
```

- `AdaptiveNavigationStyle` has two values. `EdgeColumn` is the new form. `Rail` is today's rail
  and sidebar for medium windows and up, kept for apps that want it.
- `sidebarCollapsed`/`onSidebarCollapsedChange` become `sidebarVisible`/`onSidebarVisibleChange`.
  The sidebar is a floating panel from the column's side, never a column that pushes the content.
  It opens with a light scrim, and Escape, back and an outside tap close it. From Large up it may
  stay open. In that case the content's padding also clears the panel.
- `header` (the primary action) moves into the column's toolbar under `EdgeColumn`, as the primary
  button.
- `AdaptiveNavigationScope.contentPadding` carries the column's clearance on its side (76dp) under
  `EdgeColumn`, the way it carries the bottom bar's height today. A full-bleed background ignores
  it; text and controls apply it.
- **System status.** On a device whose system draws status in the column, the column starts under
  it. On Android the status bar stays at the top: the column starts below the top inset, and the
  app's status icon is the first item in the column.

### 2. `AdaptiveToolbar`: a screen hands its actions to whichever bar there is

Each screen used to draw a `TopBar`. Now each screen declares its toolbar once:

```kotlin
@Composable
public fun AdaptiveToolbar(
    title: String,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    scrollState: TopBarScrollState? = null,
    actions: (@Composable EdgeToolbarScope.() -> Unit)? = null, // IconAction, PrimaryAction, More
)
```

- In `BottomBar` and `Rail` forms it draws a `TopBar`, or a `LargeTopBar` when it is handed a
  scroll state, inline as today.
- In `EdgeColumn` form it draws only the large title inline. It registers `onBack` and `actions`
  with the shell's `EdgeColumnHost` (a composition local), and the column draws them as round
  buttons. Registration is a `DisposableEffect` that adds an entry and removes it on dispose, so
  the column always shows the screen that is on screen.
- With `onBack` present, back replaces the sidebar toggle. Overflowing actions fold into More,
  which opens an `AdaptiveMenu` toward the content.
- Pane roles decide which toolbar the column carries. `ListDetail` and `SupportingPane` provide a
  `LocalToolbarRole`. In two-pane form, the detail pane's toolbar owns the column, and the list
  pane's actions draw inline in its header. In one-pane form, whichever pane shows owns it.

### 3. New public parts (`dev.voir.areca.foundation.adaptive`)

These are public so an app with its own shell can build the same column:

- `EdgeColumn(side, top, bottom)`: the floating column. It keeps clear of the display cutout and,
  at a horizontal fold, keeps `top` above the fold and `bottom` below it.
- `EdgeButton(onClick, contentDescription, style = Plain | Primary | Toggled)`: a 44dp circle with
  a tooltip.
- `VerticalTabBar(items, selectedValue, onValueSelected, overflowLabel = "More")`: the vertical
  capsule. It has tab semantics with vertical orientation, a badge per item, and a label on long
  press or hover.
- `EdgeStatus(icon, description, attention: Boolean)`: the status icon, with a badge when
  `attention` is true.
- `FloatingSidebar(visible, onDismissRequest, side) { … }`: the panel. `AdaptiveNavigation` uses
  it, and an app can use it on its own.

### 4. Folds in `dev.voir.areca.window`

```kotlin
public enum class FoldOrientation { Vertical, Horizontal }
public enum class FoldState { Flat, HalfOpened }

@Immutable
public class WindowFold(
    public val orientation: FoldOrientation,
    public val bounds: DpRect,
    public val state: FoldState,
    public val isSeparating: Boolean,
)
// WindowClass gains `val fold: WindowFold?`.
```

- On Android it is read from `androidx.window`: `WindowInfoTracker` gives a `FoldingFeature`.
  This adds `androidx.window:window` to `androidMain`. On iOS and the JVM it is `null`.
- `WindowClassBox` rebases the fold into its own bounds, or drops it when the fold does not cross
  the box.
- `ListDetail` and `SupportingPane` put their split on a vertical separating fold when both panes
  keep their minimum widths.
- `EdgeColumn` splits its clusters at a horizontal fold.
- Previews and tests use `ProvideWindowClass(WindowClass(size, input, fold = …))`.

## Phases

Each phase changes, in the same commit:

- the code and tests;
- the API dumps (`./gradlew updateLegacyAbi`);
- `ui/foundation/README.md`;
- `docs/components.md`;
- the playground showcase;
- the skill files listed below.

Each phase passes `./ci/verify`.

1. **Edge column.** This phase adds:
   - `EdgeColumn`, `EdgeButton`, `VerticalTabBar`, `EdgeStatus` and `FloatingSidebar`;
   - `AdaptiveNavigationStyle.EdgeColumn` and `AdaptiveNavigationLayout.EdgeColumn`;
   - the `status`, `onSearch` and `sidebarSections` slots;
   - `contentPadding` on the column's side;
   - `AdaptiveToolbar` with `EdgeColumnHost` registration;
   - `LocalToolbarRole` in `ListDetail` and `SupportingPane`.

   Tests:
   - each row of the table above;
   - a pushed screen's back replaces the toggle;
   - the column follows the screen as screens come and go;
   - a sixth destination goes to More;
   - the sidebar closes on scrim, Escape and back.
2. **Folds.** This phase adds `WindowFold`, the `androidx.window` reader and rebasing in
   `WindowClassBox`, and makes `ListDetail`, `SupportingPane` and `EdgeColumn` respect folds.
3. **Side from the cutout.** `EdgeColumnSide.Auto` reads `WindowInsets.displayCutout`. RTL mirrors
   `Start` and `End`.
4. **Actions by reach.** `WindowClass.actionReach` (`ActionReach.Header` or `Thumb`) and
   `WindowClass.WideAspectRatio`; `WindowProfile` decides a touch tablet's portrait or landscape by
   shape; `AdaptiveToolbar` draws its actions beside the title under `Header` and hands them to the
   foot of the column under `Thumb`, with `header` following them.

## Skills

The `areca-ui-v1` skill is the agent-facing source of truth. It ships to other repositories, so it
must stand alone.

- **`skills/areca-ui-v1/SKILL.md`**: a new subsection, "App shell on tablets and foldables", under
  "Lay out by window class", with these rules:
  - Use `AdaptiveNavigation`. Every screen declares `AdaptiveToolbar` rather than a `TopBar`.
  - Never place a control in the column's area yourself; apply `contentPadding` to text and
    controls, and let backgrounds and media run under the column.
  - At most three toolbar actions, primary last, the rest in More.
  - Status goes in `status` (an icon) and `footer` (full text), never as a banner across the top.
  - Do not place controls across `ArecaTheme.window.fold`.
- **`skills/areca-ui-v1/reference/adaptive-shell.md`** (new): the anatomy, the measurements, the
  column by screen state (root, pushed, sidebar open, status needing attention, tabletop) with code
  for each, and the decision table.
- **`skills/areca-ui-v1/reference/signatures.md`**: the new signatures and the fold types.
- **`skills/areca-ui-v1/reference/recipes.md`**: one recipe, "Tablet and foldable app shell", built
  from `AdaptiveNavigation`, `ListDetail` and `AdaptiveToolbar`.
- **`playground/SHOWCASES.md`**: if needed, a note on previewing shells through `WindowFrame`
  presets, including fold presets.

`.claude-plugin/plugin.json` is left alone. Only the release workflows raise it.

## Phase 1 decisions

Taken where the open questions below were not answered yet; each is one default to change.

- The edge column is the default style; `AdaptiveNavigationStyle.Rail` keeps the rail and sidebar.
- The column follows the camera cutout (`EdgeColumnSide.Auto`, the default since phase 3), and the
  end edge without one; `Start` and `End` pin it.
- A phone on its side keeps the rail.
- Tabs are icons only, with their labels announced and shown as tooltips under a pointer.
- The sidebar floats over a scrim at every size; it is never pinned.
- `AdaptiveToolbar` takes actions as `AdaptiveToolbarAction` data rather than composable content,
  so the column and the bar draw them alike and fold the overflow into More.
- Tooltips open below their anchor: `AnchoredPlacement` has no side placement yet, so labels do
  not open toward the content as the prototype shows.
- The shell does not catch the platform's back gesture while the sidebar is open; the app routes it
  to `onSidebarVisibleChange(false)`.

## Phases 2 and 3 decisions

- `ListDetail` and `SupportingPane` meet at any vertical fold, flat or half open, that leaves both
  parts their minimum widths; a hinge's strip is left empty instead of divided.
- A fold given to `ProvideWindowClass` is measured from that provider's top-left corner, so a scaled
  preview frame anywhere on a page puts it in the right place; the platform's fold is measured
  from the window.
- The playground's `Fold, unfolded` preset now carries a flat vertical fold, and a new
  `Fold, tabletop` preset (830 × 690dp) a half-opened horizontal one.
- `androidx.window:window` 1.5.1 is an Android-only dependency of foundation.

## Phase 4 decisions

Raised by Expenda's Home on tablets and foldables: actions in the header where the window is wide,
by the thumbs where it is held from below.

- **The question is the hold, not the width.** Width classes put a 13-inch tablet held upright
  (1032 × 1376) and an unfolded foldable on its side (841 × 701) beside a tablet on its side; both
  are held from below. `ActionReach` is decided by input first, then shape:
  - a pointer is `Header` at any size, since it reaches everywhere;
  - a phone on its side is `Header`, keeping its rail and top bar;
  - a compact width is `Thumb`;
  - touch at least `WideAspectRatio` (1.25) times as wide as tall is `Header`, anything squarer
    `Thumb`.
- **1.25 sits in a gap no device fills.** Tablets on their side start at 4:3 (1.33) and run to
  16:10 (1.6); unfolded foldables on their side reach about 1.2. No hysteresis: under touch the
  ratio changes only on rotation or a snapped split view, and a pointer never reads it.
- **`WindowProfile` follows the same line under touch,** so `TabletPortrait` and `TabletLandscape`
  mean held upright and on its side. A large touch window on its side is `TabletLandscape`, no
  longer `Desktop`; `Desktop` is expanded and wider under a pointer.
- **Under `Header` the actions are labelled buttons beside the title,** where a wide window has the
  room for the words — three places, More counted: the primary solid in the brand, a destructive one
  outlined in danger, the rest outlined. The column's top keeps status, the toggle or back, and
  `header`.
- **Under `Thumb` they are round buttons at the foot of the column,** between the tabs and search,
  `header` over them, three places with More. Back stays at the top: platforms put it there and the
  system gesture covers the hand that cannot reach it.
- **The reach is the whole window's** (`ArecaTheme.hostWindow`): a narrow detail pane on a tablet on
  its side is still held by its sides.

## Open questions

1. **Default style.** Should the edge column be the default for medium windows and up, with `Rail`
   kept only for apps that opt into it? Or the other way round?
2. **Column side.** End always, or follow the camera cutout (`Auto`)? A user setting for handedness
   could come later.
3. **Phone on its side.** It is too short for both clusters (about 400dp against about 600dp). Keep
   the rail, or show one merged column (status, toolbar, then tabs) with search moving into More?
4. **Tab labels.** Icons only plus a tooltip, as prototyped, or a short label under each icon? A
   label would widen the column to about 72dp.
5. **Large windows.** Should the floating sidebar be pinned open by default from Large up (the
   content clears it), or stay an overlay at every size?
