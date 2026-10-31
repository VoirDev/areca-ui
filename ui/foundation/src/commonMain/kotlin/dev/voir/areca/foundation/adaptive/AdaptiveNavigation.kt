package dev.voir.areca.foundation.adaptive

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.window.HeightClass
import dev.voir.areca.window.WidthClass
import dev.voir.areca.window.WindowClassBox

/** Which navigation an [AdaptiveNavigation] draws for the window it is in. */
public enum class AdaptiveNavigationLayout {
    /**
     * A [TabBar] along the bottom edge, or the app's own `bottomBar`: a compact window that is not
     * too short for it.
     */
    BottomBar,

    /** A [NavRail] along the start edge: a medium or expanded window, a phone on its side, or a sidebar the person collapsed. */
    Rail,

    /** A sidebar of labelled rows along the start edge: a large window and wider. */
    Sidebar,
}

/** What the slots of an [AdaptiveNavigation] know about the navigation around them. */
@Stable
public interface AdaptiveNavigationScope {
    /** The navigation drawn, so the screen can place what the bar or rail does not hold. */
    public val layout: AdaptiveNavigationLayout

    /**
     * What the navigation covers of the content: the height of an app's own `bottomBar`, which is
     * drawn over the screen, and zero in every other form. Scrolling content ends with it, so its
     * last row can be scrolled clear of the bar.
     */
    public val contentPadding: PaddingValues
}

/**
 * An app's top-level navigation, in the form the window calls for.
 *
 * - A compact window gets a [TabBar] along the bottom edge.
 * - A medium or expanded window, and any window too short for two bars, gets a [NavRail].
 * - A large window and wider gets a sidebar of labelled rows, [AdaptiveDefaults.SidebarWidth] wide,
 *   which the person can collapse to the rail when [onSidebarCollapsedChange] is given.
 *
 * The destinations and the selection are the same in every form, so a window that is resized or a
 * foldable that is opened keeps its place. [content] is drawn in a [WindowClassBox] beside or above
 * the navigation, so a screen lays itself out for the space it has left.
 *
 * [bottomBar] replaces the [TabBar] on a compact window, for an app whose bar is its own — a floating
 * bar, a bar with an action in its middle. It is drawn over the bottom of the content rather than
 * under it, so the screen keeps the full height and paints beneath a bar that floats; its height
 * reaches the screen as [AdaptiveNavigationScope.contentPadding]. Build it from the same
 * destinations, so the bar and the rail never disagree about where the app can go.
 *
 * [footer] sits at the foot of the rail and the sidebar — an account, a sync state — above the
 * collapse toggle. Like [header], the bottom bar has no room for it.
 *
 * [header] sits above the destinations of the rail and the sidebar — the screen's primary action,
 * as a compact icon button on the rail and a labelled button in the sidebar. The bottom bar has no
 * room for one: when [AdaptiveNavigationScope.layout] is [AdaptiveNavigationLayout.BottomBar], draw
 * the action in the screen.
 *
 * ```kotlin
 * AdaptiveNavigation(
 *     destinations = destinations,
 *     selectedValue = tab,
 *     onValueSelected = { tab = it },
 *     header = {
 *         if (layout == AdaptiveNavigationLayout.Sidebar) {
 *             Button(onClick = add, modifier = Modifier.fillMaxWidth()) { Text("New transaction") }
 *         } else {
 *             IconButton(imageVector = ArecaIcons.Plus, contentDescription = "New transaction", onClick = add)
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
 * @param sidebarCollapsed Whether the sidebar is collapsed to the rail on a large window.
 * @param onSidebarCollapsedChange Called with the new state when the person collapses or expands the
 *   sidebar. `null` draws no toggle and keeps [sidebarCollapsed] as given.
 * @param sidebarToggleDescription What the toggle is announced as.
 * @param header Content above the destinations of the rail and the sidebar.
 * @param footer Content at the foot of the rail and the sidebar.
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
    sidebarCollapsed: Boolean = false,
    onSidebarCollapsedChange: ((Boolean) -> Unit)? = null,
    sidebarToggleDescription: String = if (sidebarCollapsed) "Expand navigation" else "Collapse navigation",
    header: (@Composable AdaptiveNavigationScope.() -> Unit)? = null,
    footer: (@Composable AdaptiveNavigationScope.() -> Unit)? = null,
    bottomBar: (@Composable AdaptiveNavigationScope.() -> Unit)? = null,
    content: @Composable AdaptiveNavigationScope.() -> Unit,
) {
    val window = ArecaTheme.window
    val layout = when {
        window.width == WidthClass.Compact && window.height > HeightClass.Compact -> AdaptiveNavigationLayout.BottomBar
        window.width >= WidthClass.Large && !sidebarCollapsed -> AdaptiveNavigationLayout.Sidebar
        else -> AdaptiveNavigationLayout.Rail
    }
    val scope = remember(layout) { AdaptiveNavigationScopeImpl(layout, NoPadding) }
    val toggle: (@Composable () -> Unit)? = if (onSidebarCollapsedChange != null && window.width >= WidthClass.Large) {
        {
            IconButton(
                imageVector = BundledIcons.Menu01,
                contentDescription = sidebarToggleDescription,
                onClick = { onSidebarCollapsedChange(!sidebarCollapsed) },
                style = IconButtonStyle.plain(ActionColor.Neutral),
            )
        }
    } else {
        null
    }

    // The foot of the rail and the sidebar: the app's own footer, then the toggle under it.
    val foot: (@Composable () -> Unit)? = if (footer != null || toggle != null) {
        {
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
                footer = foot,
            )
            WindowClassBox(modifier = Modifier.weight(1f).fillMaxHeight()) { scope.content() }
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
 * @param footer What sits at the foot, or `null`.
 */
@Composable
private fun <T : Any> Sidebar(
    destinations: List<NavDestination<T>>,
    selectedValue: T?,
    onValueSelected: (T) -> Unit,
    header: (@Composable () -> Unit)?,
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
            Column(
                modifier = Modifier.selectableGroup(),
                verticalArrangement = Arrangement.spacedBy(spacing.xs),
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
            if (footer != null) {
                Spacer(Modifier.weight(1f))
                footer()
            }
        }
        VerticalDivider()
    }
}

private class AdaptiveNavigationScopeImpl(
    override val layout: AdaptiveNavigationLayout,
    override val contentPadding: PaddingValues,
) : AdaptiveNavigationScope

/** What a navigation that does not cover the content hands it. */
private val NoPadding = PaddingValues()
