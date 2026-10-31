# App shell on tablets and foldables

Open this when building an app's top-level shell with `AdaptiveNavigation`, or a screen inside it,
at the latest `dev.voir.areca:foundation` release: where the destinations, the toolbar, search and
status go on each window, and how a screen hands its toolbar to the edge column. Signatures are in
[signatures.md](signatures.md).

## Where the chrome goes

| Window | Destinations | A screen's title, back and actions | Status |
| --- | --- | --- | --- |
| Compact width: a phone, a folded foldable, a narrow split view | `TabBar` along the bottom, or the app's `bottomBar` | `AdaptiveToolbar` draws a `TopBar` | In the screen |
| Compact height: a phone on its side | `NavRail` without labels | `AdaptiveToolbar` draws a `TopBar` | Foot of the rail |
| Medium width and up, `ActionReach.Header`: a tablet on its side, a desktop | `VerticalTabBar` at the foot of the `EdgeColumn`; every destination labelled in the `FloatingSidebar` | Title as a heading over the screen with the actions beside it as labelled buttons; back at the top of the column | An `EdgeStatus` at the head of the column; the full text in the sidebar's `footer` |
| Medium width and up, `ActionReach.Thumb`: a tablet held upright, an unfolded foldable either way round | As above | Title as a heading over the screen; back at the top of the column; the actions as round buttons at its foot, between the tabs and search | As above |
| Medium width and up, `style = AdaptiveNavigationStyle.Rail` | `NavRail`, then a sidebar of rows from 1200dp | `AdaptiveToolbar` draws a `TopBar` | Foot of the rail or the sidebar |

The edge column floats over the content along `edgeSide` — `EdgeColumnSide.Auto` by default: the
camera cutout's side, and the end edge when the cutout is along the top or there is none — in two
clusters:

1. **Top**, under the window's top edge: `status`, then the sidebar toggle (or the screen's back),
   then `header` under `ActionReach.Header`.
2. **Bottom**, within a thumb's reach: the `VerticalTabBar` (five tabs at most, the rest in More),
   then under `ActionReach.Thumb` `header` and the screen's actions (three buttons at most, More
   counted), then the search button from `onSearch`.

`ActionReach` comes from `ArecaTheme.hostWindow.actionReach` and answers how the window is held,
not how wide it is: a pointer, or a touch window at least `WindowClass.WideAspectRatio` (1.25) times
as wide as it is tall, is `Header`; anything squarer under touch — a tablet held upright, an unfolded
foldable on its side at about 1.2 — is held from below and is `Thumb`. Under `Header` the screen's
actions are labelled buttons beside its title, three places at most, More counted.

The middle stays empty. On a foldable propped up along a horizontal fold (`FoldState.HalfOpened`),
the top cluster stays above the fold and the bottom cluster below it. The column is 56dp wide with a 12dp inset under touch (48dp under a
pointer), and `contentPadding` carries its clearance — 80dp under touch — on its side.

## The shell

```kotlin
@Composable
fun AppShell(state: ShellState, onAction: (ShellAction) -> Unit) {
    var sidebarOpen by rememberSaveable { mutableStateOf(false) }

    AdaptiveNavigation(
        destinations = Tab.entries.map { NavDestination(it, it.label, it.icon) },
        selectedValue = state.tab,
        onValueSelected = { onAction(ShellAction.Select(it)) },
        sidebarVisible = sidebarOpen,
        onSidebarVisibleChange = { sidebarOpen = it },
        onSearch = { onAction(ShellAction.Search) },
        status = {
            if (state.offline) {
                EdgeStatus(ArecaIcons.CloudOff, "Offline, ${state.pending} changes waiting", attention = true)
            } else {
                EdgeStatus(ArecaIcons.Check, "Synced")
            }
        },
        header = {
            if (layout == AdaptiveNavigationLayout.EdgeColumn) {
                EdgeButton(
                    imageVector = ArecaIcons.Plus,
                    contentDescription = "New transaction",
                    onClick = { onAction(ShellAction.Add) },
                    emphasis = EdgeButtonEmphasis.Primary,
                )
            }
        },
        sidebarSections = { AccountRows(state.accounts) },
        footer = { SyncFooter(state) },
    ) {
        when (state.tab) {
            Tab.Activity -> ActivityScreen(contentPadding)
            Tab.Home -> HomeScreen(contentPadding)
            // …
        }
    }
}
```

- `status` takes an `EdgeStatus` in the column. On the rail it sits at the foot; draw what suits
  the rail when `layout` is `Rail`.
- `header` is the app's primary action. In the bottom-bar form there is no room for it: add it to
  the screen's `AdaptiveToolbar` actions with `primary = true` when `layout` is `BottomBar`.
- `sidebarSections` and `footer` show only in the sidebar (and `footer` at the foot of the rail).
  The tab capsule hides while the sidebar is open.
- The shell does not handle the platform's back gesture: while `sidebarOpen`, close it from the
  app's back handler.

## A screen inside the shell

Declare the toolbar once with `AdaptiveToolbar`, never a `TopBar`, and keep text and controls inside
`contentPadding`; backgrounds, images and maps may run under the column.

```kotlin
@Composable
fun ActivityScreen(contentPadding: PaddingValues) {
    var selected by rememberSaveable { mutableStateOf<String?>(null) }

    ListDetail(
        showDetail = selected != null,
        modifier = Modifier.fillMaxSize().padding(contentPadding),
        list = {
            Column {
                AdaptiveToolbar(title = "Activity")
                TransactionList(onOpen = { selected = it })
            }
        },
        detail = {
            Column {
                AdaptiveToolbar(
                    title = "Lidl",
                    onBack = if (isTwoPane) null else { { selected = null } },
                    actions = listOf(
                        AdaptiveToolbarAction(ArecaIcons.Edit, "Edit", onClick = edit, primary = true),
                        AdaptiveToolbarAction(ArecaIcons.Share, "Share", onClick = share),
                        AdaptiveToolbarAction(ArecaIcons.Trash, "Delete", onClick = delete, destructive = true),
                    ),
                )
                TransactionDetail(selected!!)
            }
        },
    )
}
```

How the toolbar lands in each case:

| Screen state | Column (medium and up) | Bar (compact) |
| --- | --- | --- |
| Root screen, `ActionReach.Header` | Toggle and `header` at the top; the actions as labelled buttons beside the title | Title and actions |
| Root screen, `ActionReach.Thumb` | Toggle at the top; `header` and the actions at the foot, between the tabs and search | Title and actions |
| Pushed screen (`onBack` set) | Back in the toggle's place; the actions where the reach puts them | Back, title, actions |
| Two panes | The detail's toolbar takes the column; the list's stays a bar over its pane. The reach is the window's, so a narrow detail pane on a tablet on its side still draws its actions beside its title | — |
| More actions than room | The first fit, then More with the rest, in order: three places beside the title or at the foot, More counted | The same, in the bar |
| Sidebar open | Toggle drawn as on; the tabs give way to the sidebar's rows | — |
| Foldable opened flat, a vertical fold | `ListDetail` puts the list on one half and the detail on the other | — |
| Foldable propped up, a horizontal fold | Status and back above the fold, the tabs, the actions and search below | — |

The column shows the toolbar of the screen drawn most recently, so a screen pushed over another
takes it over and hands it back when it leaves; nothing has to be cleared by hand.

## A shell of your own

The parts are public. Put full-bleed content in a `Box`, draw the `FloatingSidebar` over it and the
`EdgeColumn` last, aligned to its side:

```kotlin
Box(Modifier.fillMaxSize()) {
    Screen(Modifier.padding(end = EdgeColumnSize.Default.clearance))
    FloatingSidebar(
        visible = sidebarOpen,
        onDismissRequest = { sidebarOpen = false },
        edgeOffset = EdgeColumnSize.Default.width,
    ) { Destinations() }
    EdgeColumn(
        modifier = Modifier.align(Alignment.CenterEnd),
        top = {
            EdgeStatus(ArecaIcons.Check, "Synced")
            EdgeButton(ArecaIcons.Menu01, "Show navigation", onClick = { sidebarOpen = !sidebarOpen },
                emphasis = if (sidebarOpen) EdgeButtonEmphasis.Toggled else EdgeButtonEmphasis.Plain)
        },
        bottom = {
            if (!sidebarOpen) VerticalTabBar(destinations, tab, onValueSelected = { tab = it })
            EdgeButton(ArecaIcons.Search, "Search", onClick = openSearch)
        },
    )
}
```

## Don't

| Don't | Do |
| --- | --- |
| Draw a `TopBar` with a back arrow on a screen inside the shell | `AdaptiveToolbar(title, onBack = …)`; it is a bar on a phone and moves to the column on a tablet |
| Place a button, a banner or a FAB along the column's edge | Give the action to `AdaptiveToolbar` or `header`; keep content inside `contentPadding` |
| Pad the whole screen, background included, by `contentPadding` when it shows a photo or a map | Let the media run under the column and pad only the text and controls |
| Put sync or offline text across the top of the screen | `status` with an `EdgeStatus` (`attention = true` when it needs the person), the full text in `footer` |
| Give the column more than three toolbar buttons by drawing your own | Pass them all as actions; the rest fold into More in order |
| Put a control, a row of text or a chart's axis across `ArecaTheme.window.fold.bounds` | Let `ListDetail` split at the fold, or lay each half out for itself while the fold is half opened |
| Branch on a device or a platform to choose the column | `AdaptiveNavigation` decides by the window; `style = AdaptiveNavigationStyle.Rail` opts out |
| Decide where a screen's own actions go by width, or by `isLandscape` | Branch on `ArecaTheme.hostWindow.actionReach`: an unfolded foldable on its side is landscape and expanded, and still held from below |
