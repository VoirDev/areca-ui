# Tabbed screen

An application's main screen: a tab bar of destinations along the bottom, a top bar titled with the
current destination, and, inside one destination, a strip of tabs switching between two feeds.

Needs `dev.voir.areca:foundation`, and `dev.voir.areca:icons` for the destination glyphs.

## Components

| Component | Role here |
| --- | --- |
| `BaseLayout` | The screen's frame, with the top bar and the tab bar |
| `TopBar` | The current destination's title |
| `TabBar` and `TabBarItem` | The application's primary destinations, docked to the bottom edge. It applies the bottom inset itself |
| `Tabs`, `TabItem` and `rememberTabsState` | Views within one destination, underlined |
| `Icon`, `Text` with `TextVariant.LabelSmall` | Each destination's glyph and name |

Every component is in the [catalogue](../components.md).

## Code

```kotlin
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.layout.BaseLayout
import dev.voir.areca.foundation.tabbar.TabBar
import dev.voir.areca.foundation.tabbar.TabBarItem
import dev.voir.areca.foundation.tabs.TabItem
import dev.voir.areca.foundation.tabs.Tabs
import dev.voir.areca.foundation.tabs.rememberTabsState
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.foundation.topbar.TopBar
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.Home
import dev.voir.areca.icons.Search
import dev.voir.areca.icons.User
import dev.voir.areca.theme.ArecaTheme
```

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

- **Two kinds of tab.** `TabBar` switches destinations; `Tabs` switches views inside one. Do not use
  `Tabs` as an application's navigation.
- **Selection.** Hold it yourself (`selectedValue` and `onValueSelected`) when something else needs
  it — the title here — or let `rememberTabsState` hold it when only the strip does.
- **Content.** A tab's content is plain `Text` and `Icon`; the bar provides their colour for the
  selected and unselected states. The icon's `contentDescription` is `null` because the name beside
  it says the same.

## Variations

**A setting, not a view.** `TabsVariant.Segmented` sets the tabs in a capsule track with a sliding
thumb — a period switch. Pair it with `TabsSize.Segmented`; `fillWidth = true` shares the width
equally:

```kotlin
Tabs(
    listOf(
        TabItem(value = Period.Month) { Text("Month") },
        TabItem(value = Period.Year) { Text("Year") },
    ),
    selectedValue = period,
    onValueSelected = onPeriodChange,
    size = TabsSize.Segmented,
    variant = TabsVariant.Segmented,
    fillWidth = true,
)
```

`TabsVariant.Button` gives the selected tab a fill of its own.

**A floating bar.** `FloatingTabBar` floats above the content instead of docking. Like `TabBar`, it
takes `selectedValue` and `onValueSelected`, or a state that holds the selection:

```kotlin
val items = Destination.entries.map { entry ->
    TabBarItem(value = entry) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(entry.icon, contentDescription = null)
            Text(entry.label, variant = TextVariant.LabelSmall, maxLines = 1)
        }
    }
}
val state = rememberTabBarState(items, initialSelectedValue = Destination.Home)

FloatingTabBar(items = items, state = state, modifier = Modifier.fillMaxWidth())
```

**A sidebar.** On a wide window, `NavList` lists the destinations down the side, with expandable
sections.

**Tabs over artwork.** `CollapsingHeaderLayout` scrolls a header away above a strip that stays — a
profile's photo over its tabs.

**Swiped pages.** An onboarding tour is a `SwipePager` with `PageIndicator` and `PagerControls`, not
tabs.
