package dev.voir.areca.foundation.adaptive

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.window.HeightClass
import dev.voir.areca.window.WidthClass
import dev.voir.areca.window.WindowClass

/**
 * Widths and thresholds the adaptive layouts share.
 *
 * The thresholds compare against the space a layout is given — its container, after the navigation
 * has taken its width — not against the window, so a layout inside a narrow pane falls back to its
 * compact form.
 */
public object AdaptiveDefaults {
    /** Widest a column of reading or form content grows: sign-in, settings, a form, a sheet. */
    public val ReadableWidth: Dp = 640.dp

    /** Widest a page of browsing content grows: a feed, a grid, a dashboard. */
    public val ContentWidth: Dp = 1200.dp

    /** Smallest container [ListDetail] shows its two panes side by side in. */
    public val TwoPaneMinWidth: Dp = 600.dp

    /** Smallest container [SupportingPane] keeps its supporting pane on screen in. */
    public val SupportingPaneMinWidth: Dp = 920.dp

    /** Narrowest a person can drag a [ListDetail]'s list pane. */
    public val ListPaneMinWidth: Dp = 240.dp

    /** Narrowest a drag of a [ListDetail]'s divider leaves the detail pane. */
    public val DetailPaneMinWidth: Dp = 320.dp

    /** Width of the sidebar [AdaptiveNavigation] draws from a large window up. */
    public val SidebarWidth: Dp = 256.dp

    /** Width of the [FloatingSidebar] an [AdaptiveNavigation] opens beside its [EdgeColumn]. */
    public val FloatingSidebarWidth: Dp = 300.dp

    /** Smallest cell [AdaptiveGrid] lays out. */
    public val GridMinCellWidth: Dp = 160.dp

    /**
     * Width of a list pane beside its detail: 280dp in a container under 760dp, 320dp under
     * 1100dp, 360dp from there.
     *
     * @param containerWidth Width of the space both panes share.
     * @return Width of the list pane.
     */
    public fun listPaneWidth(containerWidth: Dp): Dp = when {
        containerWidth < 760.dp -> 280.dp
        containerWidth < 1100.dp -> 320.dp
        else -> 360.dp
    }

    /**
     * Width of a supporting pane on screen: 340dp in a container under 1200dp, 360dp from there.
     *
     * @param containerWidth Width of the space the main content and the pane share.
     * @return Width of the supporting pane.
     */
    public fun supportingPaneWidth(containerWidth: Dp): Dp = if (containerWidth < 1200.dp) 340.dp else 360.dp

    /**
     * Whether something that rises as a sheet on a phone opens as a centred dialog in [window]: from
     * an expanded width up, unless the window is too short for a dialog — a phone on its side.
     *
     * [AdaptiveSheet] and `ConfirmSheet` decide by it; a sheet of your own can too, with
     * `ArecaTheme.hostWindow`.
     *
     * @param window The window the sheet opens over, normally `ArecaTheme.hostWindow`.
     * @return Whether to open a dialog rather than a sheet.
     */
    public fun usesDialog(window: WindowClass): Boolean =
        window.width >= WidthClass.Expanded && window.height > HeightClass.Compact
}
