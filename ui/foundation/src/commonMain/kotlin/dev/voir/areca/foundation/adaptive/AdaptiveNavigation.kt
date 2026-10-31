package dev.voir.areca.foundation.adaptive

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.IconButton
import dev.voir.areca.foundation.button.IconButtonStyle
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.layout.BaseLayout
import dev.voir.areca.foundation.layout.VerticalDivider
import dev.voir.areca.foundation.navigation.NavItem
import dev.voir.areca.foundation.tabbar.TabBar
import dev.voir.areca.foundation.tabbar.TabBarItem
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.icon.BundledIcons
import dev.voir.areca.icon.Menu01
import dev.voir.areca.icon.Search
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.window.ActionReach
import dev.voir.areca.window.HeightClass
import dev.voir.areca.window.WidthClass
import dev.voir.areca.window.WindowClassBox

/** Which family of forms an [AdaptiveNavigation] takes on a medium window and wider. */
public enum class AdaptiveNavigationStyle {
    /**
     * An [EdgeColumn] along one edge: status and the screen's toolbar at its top, the destinations
     * and search at its foot, the sidebar floating in beside it. A tablet or an unfolded foldable
     * then keeps its chrome along the camera's edge.
     */
    EdgeColumn,

    /** A [NavRail] along the start edge, widening to a sidebar of labelled rows from a large window. */
    Rail,
}

/** Which navigation an [AdaptiveNavigation] draws for the window it is in. */
public enum class AdaptiveNavigationLayout {
    /**
     * A [TabBar] along the bottom edge, or the app's own `bottomBar`: a compact window that is not
     * too short for it.
     */
    BottomBar,

    /**
     * A [NavRail] along the start edge: a phone on its side, and under [AdaptiveNavigationStyle.Rail]
     * a medium or expanded window, or a large one whose sidebar the person hid.
     */
    Rail,

    /** A sidebar of labelled rows along the start edge: a large window and wider, under [AdaptiveNavigationStyle.Rail]. */
    Sidebar,

    /** An [EdgeColumn] over the content: a medium window and wider, under [AdaptiveNavigationStyle.EdgeColumn]. */
    EdgeColumn,
}

/** What the slots of an [AdaptiveNavigation] know about the navigation around them. */
@Stable
public interface AdaptiveNavigationScope {
    /** The navigation drawn, so the screen can place what the bar or rail does not hold. */
    public val layout: AdaptiveNavigationLayout

    /**
     * What the navigation covers of the content: the height of an app's own `bottomBar`, drawn over
     * the screen; the [EdgeColumnSize.clearance] on the column's side beside an [EdgeColumn], which
     * floats over the screen; zero in every other form. Keep text and controls inside it, and let
     * backgrounds and media run under it; scrolling content ends with it, so its last row can be
     * scrolled clear of the bar.
     */
    public val contentPadding: PaddingValues
}

/**
 * An app's top-level navigation, in the form the window calls for.
 *
 * - A compact window gets a [TabBar] along the bottom edge, and a phone on its side a [NavRail]
 *   without labels.
 * - Under [AdaptiveNavigationStyle.EdgeColumn], the default, a medium window and wider gets an
 *   [EdgeColumn] along [edgeSide] — the camera's side by default — floating over the content: [status]
 *   and back at its top, a [VerticalTabBar] and search at its foot. Where [header] and the screen's
 *   actions go follows the window's [ActionReach]: under [ActionReach.Header] — a tablet on its side,
 *   a pointer — [header] stays at the top and the screen draws its actions beside its title; under
 *   [ActionReach.Thumb] — a tablet held upright, an unfolded foldable — both go to the foot, between
 *   the tabs and search. The sidebar toggle at the head of the column opens a [FloatingSidebar]
 *   beside it, with every destination labelled, [sidebarSections] and [footer]; the tabs give way to
 *   it while it is open.
 * - Under [AdaptiveNavigationStyle.Rail] a medium or expanded window gets a [NavRail], and a large
 *   window and wider a sidebar of labelled rows, [AdaptiveDefaults.SidebarWidth] wide, which the
 *   person can hide to the rail.
 *
 * The destinations and the selection are the same in every form, so a window that is resized or a
 * foldable that is opened keeps its place. [content] is drawn in a [WindowClassBox] beside, above or
 * under the navigation, so a screen lays itself out for the space it has.
 *
 * Screens declare their title, back and actions with [AdaptiveToolbar] rather than a
 * [dev.voir.areca.foundation.topbar.TopBar]: it is a bar over the screen in every form but the
 * column, which takes back as a round button and the actions as the reach decides.
 *
 * [bottomBar] replaces the [TabBar] on a compact window, for an app whose bar is its own — a floating
 * bar, a bar with an action in its middle. It is drawn over the bottom of the content rather than
 * under it, so the screen keeps the full height and paints beneath a bar that floats; its height
 * reaches the screen as [AdaptiveNavigationScope.contentPadding]. Build it from the same
 * destinations, so the bar and the rail never disagree about where the app can go.
 *
 * [header] is the app's primary action: above the destinations of the rail and the sidebar, after
 * the toggle in the column, or over the screen's actions at its foot under [ActionReach.Thumb]. Draw
 * it for the form in [AdaptiveNavigationScope.layout] — a
 * labelled button in the sidebar, an [EdgeButton] in the column, an icon button on the rail. The
 * bottom bar has no room for one: draw the action in the screen there.
 *
 * ```kotlin
 * AdaptiveNavigation(
 *     destinations = destinations,
 *     selectedValue = tab,
 *     onValueSelected = { tab = it },
 *     sidebarVisible = sidebarOpen,
 *     onSidebarVisibleChange = { sidebarOpen = it },
 *     onSearch = openSearch,
 *     status = { EdgeStatus(ArecaIcons.Check, "Synced") },
 *     header = {
 *         when (layout) {
 *             AdaptiveNavigationLayout.EdgeColumn ->
 *                 EdgeButton(ArecaIcons.Plus, "New transaction", onClick = add, emphasis = EdgeButtonEmphasis.Primary)
 *             AdaptiveNavigationLayout.Sidebar ->
 *                 Button(onClick = add, modifier = Modifier.fillMaxWidth()) { Text("New transaction") }
 *             else -> IconButton(imageVector = ArecaIcons.Plus, contentDescription = "New transaction", onClick = add)
 *         }
 *     },
 * ) {
 *     when (tab) { … }
 * }
 * ```
 *
 * @param destinations The top-level destinations, in order.
 * @param selectedValue Value of the selected destination, or `null` for none.
 * @param onValueSelected Called with a destination's value when it is chosen.
 * @param modifier Modifier applied to the whole shell.
 * @param style Which forms a medium window and wider takes.
 * @param edgeSide The edge the [EdgeColumn] runs along, and the sidebar floats in from:
 *   [EdgeColumnSide.Auto] puts it on the camera cutout's side, the end side without one.
 * @param sidebarVisible Whether the sidebar shows: floating beside the column, or in place of the
 *   rail on a large window. Defaults to shown under [AdaptiveNavigationStyle.Rail] and hidden under
 *   [AdaptiveNavigationStyle.EdgeColumn].
 * @param onSidebarVisibleChange Called with the new state when the person shows or hides the
 *   sidebar. `null` draws no toggle and keeps [sidebarVisible] as given.
 * @param sidebarToggleDescription What the toggle is announced as.
 * @param onSearch Called when the person presses search, the button under the tabs in the column.
 *   `null` draws none; the other forms leave search to the screen.
 * @param searchDescription What the search button is announced as.
 * @param status The app's own status — synced, offline — as an [EdgeStatus] at the head of the
 *   column, and at the foot of the rail and the sidebar. Put its full text in [footer].
 * @param header The app's primary action, above the destinations of the rail and the sidebar, and
 *   after the toggle in the column — at its foot, over the screen's actions, under
 *   [ActionReach.Thumb].
 * @param sidebarSections More of the sidebar, under the destinations: accounts, saved filters.
 * @param footer Content at the foot of the rail and the sidebars.
 * @param bottomBar The bar a compact window draws in place of the [TabBar], over the bottom of the
 *   content. `null` draws the [TabBar].
 * @param content The selected destination's screen.
 */
@Composable
public fun <T : Any> AdaptiveNavigation(
    destinations: List<NavDestination<T>>,
    selectedValue: T?,
    onValueSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    style: AdaptiveNavigationStyle = AdaptiveNavigationStyle.EdgeColumn,
    edgeSide: EdgeColumnSide = EdgeColumnSide.Auto,
    sidebarVisible: Boolean = style == AdaptiveNavigationStyle.Rail,
    onSidebarVisibleChange: ((Boolean) -> Unit)? = null,
    sidebarToggleDescription: String = if (sidebarVisible) "Hide navigation" else "Show navigation",
    onSearch: (() -> Unit)? = null,
    searchDescription: String = "Search",
    status: (@Composable AdaptiveNavigationScope.() -> Unit)? = null,
    header: (@Composable AdaptiveNavigationScope.() -> Unit)? = null,
    sidebarSections: (@Composable ColumnScope.() -> Unit)? = null,
    footer: (@Composable AdaptiveNavigationScope.() -> Unit)? = null,
    bottomBar: (@Composable AdaptiveNavigationScope.() -> Unit)? = null,
    content: @Composable AdaptiveNavigationScope.() -> Unit,
) {
    val window = ArecaTheme.window
    val layout = when {
        window.width == WidthClass.Compact && window.height > HeightClass.Compact -> AdaptiveNavigationLayout.BottomBar
        window.height == HeightClass.Compact -> AdaptiveNavigationLayout.Rail
        style == AdaptiveNavigationStyle.EdgeColumn -> AdaptiveNavigationLayout.EdgeColumn
        window.width >= WidthClass.Large && sidebarVisible -> AdaptiveNavigationLayout.Sidebar
        else -> AdaptiveNavigationLayout.Rail
    }
    val scope = remember(layout) { AdaptiveNavigationScopeImpl(layout, NoPadding) }

    if (layout == AdaptiveNavigationLayout.EdgeColumn) {
        EdgeColumnShell(
            destinations = destinations,
            selectedValue = selectedValue,
            onValueSelected = onValueSelected,
            modifier = modifier,
            side = edgeSide.resolve(),
            sidebarVisible = sidebarVisible,
            onSidebarVisibleChange = onSidebarVisibleChange,
            sidebarToggleDescription = sidebarToggleDescription,
            onSearch = onSearch,
            searchDescription = searchDescription,
            status = status,
            header = header,
            sidebarSections = sidebarSections,
            footer = footer,
            content = content,
        )
        return
    }

    val toggle: (@Composable () -> Unit)? =
        if (onSidebarVisibleChange != null && style == AdaptiveNavigationStyle.Rail && window.width >= WidthClass.Large) {
            {
                IconButton(
                    imageVector = BundledIcons.Menu01,
                    contentDescription = sidebarToggleDescription,
                    onClick = { onSidebarVisibleChange(!sidebarVisible) },
                    style = IconButtonStyle.plain(ActionColor.Neutral),
                )
            }
        } else {
            null
        }

    // The foot of the rail and the sidebar: the status, the app's own footer, then the toggle under them.
    val foot: (@Composable () -> Unit)? = if (status != null || footer != null || toggle != null) {
        {
            if (status != null) scope.status()
            if (footer != null) scope.footer()
            if (toggle != null) toggle()
        }
    } else {
        null
    }

    when (layout) {
        AdaptiveNavigationLayout.BottomBar -> if (bottomBar != null) {
            // Over the content rather than under it, measured first, so a floating bar keeps the
            // screen's full height and the screen learns what the bar covers.
            BaseLayout(
                modifier = modifier,
                bottomBar = { scope.bottomBar() },
            ) { padding ->
                val padded = remember(padding) { AdaptiveNavigationScopeImpl(layout, padding) }
                WindowClassBox(modifier = Modifier.fillMaxSize()) { padded.content() }
            }
        } else Column(modifier = modifier.fillMaxSize()) {
            WindowClassBox(modifier = Modifier.weight(1f).fillMaxWidth()) { scope.content() }
            TabBar(
                items = destinations.map { destination ->
                    TabBarItem(value = destination.value, enabled = destination.enabled) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(imageVector = destination.icon, contentDescription = null)
                            Text(
                                text = destination.label,
                                variant = TextVariant.LabelSmall,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                },
                selectedValue = selectedValue,
                onValueSelected = onValueSelected,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        AdaptiveNavigationLayout.Rail -> Row(modifier = modifier.fillMaxSize()) {
            NavRail(
                destinations = destinations,
                selectedValue = selectedValue,
                onValueSelected = onValueSelected,
                // A phone on its side has no height for labels under five icons.
                showLabels = window.height > HeightClass.Compact,
                header = if (header != null) { { scope.header() } } else null,
                footer = if (foot != null) { { foot() } } else null,
            )
            WindowClassBox(modifier = Modifier.weight(1f).fillMaxHeight()) { scope.content() }
        }

        AdaptiveNavigationLayout.Sidebar -> Row(modifier = modifier.fillMaxSize()) {
            Sidebar(
                destinations = destinations,
                selectedValue = selectedValue,
                onValueSelected = onValueSelected,
                header = if (header != null) { { scope.header() } } else null,
                sections = sidebarSections,
                footer = foot,
            )
            WindowClassBox(modifier = Modifier.weight(1f).fillMaxHeight()) { scope.content() }
        }

        AdaptiveNavigationLayout.EdgeColumn -> Unit
    }
}

/**
 * The edge-column form: the screen at the window's full size, the [FloatingSidebar] over it, and the
 * [EdgeColumn] over both, so the toggle that opened the sidebar stays pressable. Screens' toolbars
 * reach the column through [LocalEdgeColumnHost].
 */
@Composable
private fun <T : Any> EdgeColumnShell(
    destinations: List<NavDestination<T>>,
    selectedValue: T?,
    onValueSelected: (T) -> Unit,
    modifier: Modifier,
    side: EdgeColumnSide,
    sidebarVisible: Boolean,
    onSidebarVisibleChange: ((Boolean) -> Unit)?,
    sidebarToggleDescription: String,
    onSearch: (() -> Unit)?,
    searchDescription: String,
    status: (@Composable AdaptiveNavigationScope.() -> Unit)?,
    header: (@Composable AdaptiveNavigationScope.() -> Unit)?,
    sidebarSections: (@Composable ColumnScope.() -> Unit)?,
    footer: (@Composable AdaptiveNavigationScope.() -> Unit)?,
    content: @Composable AdaptiveNavigationScope.() -> Unit,
) {
    val size = LocalEdgeColumnSize.current ?: EdgeColumnSize.Default
    val host = remember { EdgeColumnHost() }
    val clearance = size.clearance
    val scope = remember(side, clearance) {
        AdaptiveNavigationScopeImpl(
            AdaptiveNavigationLayout.EdgeColumn,
            if (side == EdgeColumnSide.End) PaddingValues(end = clearance) else PaddingValues(start = clearance),
        )
    }
    // Held from below, the app's action and the screen's go down to the thumbs; held by its sides or
    // driven by a pointer, the screen draws its actions beside its title and the app's stays on top.
    val thumb = ArecaTheme.hostWindow.actionReach == ActionReach.Thumb
    val sidebarShown = sidebarVisible && onSidebarVisibleChange != null
    val toggle: (@Composable () -> Unit)? = if (onSidebarVisibleChange != null) {
        {
            EdgeButton(
                imageVector = BundledIcons.Menu01,
                contentDescription = sidebarToggleDescription,
                onClick = { onSidebarVisibleChange(!sidebarVisible) },
                emphasis = if (sidebarVisible) EdgeButtonEmphasis.Toggled else EdgeButtonEmphasis.Plain,
            )
        }
    } else {
        null
    }

    CompositionLocalProvider(LocalEdgeColumnHost provides host) {
        Box(modifier = modifier.fillMaxSize()) {
            WindowClassBox(modifier = Modifier.fillMaxSize()) { scope.content() }
            FloatingSidebar(
                visible = sidebarShown,
                onDismissRequest = { onSidebarVisibleChange?.invoke(false) },
                side = side,
                edgeOffset = size.width,
            ) {
                SidebarRows(
                    destinations = destinations,
                    selectedValue = selectedValue,
                    onValueSelected = onValueSelected,
                )
                if (sidebarSections != null) {
                    Spacer(Modifier.height(ArecaTheme.spacing.xl))
                    sidebarSections()
                }
                if (footer != null) {
                    Spacer(Modifier.weight(1f))
                    scope.footer()
                }
            }
            EdgeColumn(
                modifier = Modifier.align(if (side == EdgeColumnSide.End) Alignment.CenterEnd else Alignment.CenterStart),
                side = side,
                size = size,
                top = {
                    if (status != null) scope.status()
                    EdgeToolbarBack(host = host, leading = toggle)
                    if (!thumb && header != null) scope.header()
                },
                bottom = {
                    if (!sidebarShown) {
                        VerticalTabBar(
                            destinations = destinations,
                            selectedValue = selectedValue,
                            onValueSelected = onValueSelected,
                            size = size,
                        )
                    }
                    if (thumb) {
                        if (header != null) scope.header()
                        EdgeToolbarActions(host = host, size = size)
                    }
                    if (onSearch != null) {
                        EdgeButton(imageVector = BundledIcons.Search, contentDescription = searchDescription, onClick = onSearch)
                    }
                },
            )
        }
    }
}

/**
 * The large-window form: labelled rows down the start edge, the header above them and the footer —
 * the app's own and the toggle — at the foot.
 *
 * @param destinations The destinations.
 * @param selectedValue Value of the selected destination.
 * @param onValueSelected Called when a destination is chosen.
 * @param header Content above the rows.
 * @param sections More of the sidebar under the rows, or `null`.
 * @param footer What sits at the foot, or `null`.
 */
@Composable
private fun <T : Any> Sidebar(
    destinations: List<NavDestination<T>>,
    selectedValue: T?,
    onValueSelected: (T) -> Unit,
    header: (@Composable () -> Unit)?,
    sections: (@Composable ColumnScope.() -> Unit)?,
    footer: (@Composable () -> Unit)?,
) {
    val spacing = ArecaTheme.spacing
    Row(modifier = Modifier.fillMaxHeight().background(ArecaTheme.colors.bgCanvas)) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(AdaptiveDefaults.SidebarWidth)
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Vertical + WindowInsetsSides.Start))
                .padding(horizontal = spacing.lg, vertical = spacing.xl),
        ) {
            if (header != null) {
                header()
                Spacer(Modifier.height(spacing.xl))
            }
            SidebarRows(destinations, selectedValue, onValueSelected)
            if (sections != null) {
                Spacer(Modifier.height(spacing.xl))
                sections()
            }
            if (footer != null) {
                Spacer(Modifier.weight(1f))
                footer()
            }
        }
        VerticalDivider()
    }
}

/**
 * The destinations as labelled rows, the selected one marked: the sidebar's, and the floating
 * sidebar's.
 *
 * @param destinations The destinations.
 * @param selectedValue Value of the selected destination.
 * @param onValueSelected Called when a destination is chosen.
 */
@Composable
private fun <T : Any> SidebarRows(
    destinations: List<NavDestination<T>>,
    selectedValue: T?,
    onValueSelected: (T) -> Unit,
) {
    Column(
        modifier = Modifier.selectableGroup(),
        verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xs),
    ) {
        for (destination in destinations) {
            NavItem(
                modifier = Modifier.fillMaxWidth(),
                selected = destination.value == selectedValue,
                enabled = destination.enabled,
                onClick = { onValueSelected(destination.value) },
                leading = { Icon(imageVector = destination.icon, contentDescription = null) },
            ) {
                Text(destination.label, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

private class AdaptiveNavigationScopeImpl(
    override val layout: AdaptiveNavigationLayout,
    override val contentPadding: PaddingValues,
) : AdaptiveNavigationScope

/** What a navigation that does not cover the content hands it. */
private val NoPadding = PaddingValues()
