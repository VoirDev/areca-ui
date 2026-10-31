package dev.voir.areca.foundation.adaptive

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.button.IconButton
import dev.voir.areca.foundation.button.IconButtonStyle
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.overlay.AnchoredPlacement
import dev.voir.areca.foundation.overlay.anchored
import dev.voir.areca.foundation.overlay.rememberDropdownPopup
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.foundation.topbar.BackAction
import dev.voir.areca.foundation.topbar.IconAction
import dev.voir.areca.foundation.topbar.OverflowAction
import dev.voir.areca.foundation.topbar.TopBar
import dev.voir.areca.icon.BundledIcons
import dev.voir.areca.icon.ChevronLeft
import dev.voir.areca.icon.DotsVertical
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.horizontalContentInsets
import dev.voir.areca.window.ActionReach

/**
 * One action of an [AdaptiveToolbar]: a labelled button beside the title or a round button at the
 * foot of the [EdgeColumn], an icon in a [TopBar], or a row of the More menu each folds its overflow
 * into.
 *
 * @property icon The action's glyph.
 * @property label What the action is announced as, shown as its tooltip and as its row in More.
 * @property onClick Called when the action is chosen.
 * @property primary Whether it is the screen's primary action, drawn filled with the brand.
 * @property enabled Whether the action is available.
 * @property destructive Whether the action destroys something, drawn in the danger colour in More.
 */
@Immutable
public class AdaptiveToolbarAction(
    public val icon: ImageVector,
    public val label: String,
    public val onClick: () -> Unit,
    public val primary: Boolean = false,
    public val enabled: Boolean = true,
    public val destructive: Boolean = false,
)

/**
 * A screen's toolbar inside an [AdaptiveNavigation], drawn where the navigation keeps its chrome.
 *
 * Declare it once, at the top of the screen, in place of a [TopBar]:
 *
 * - Under a bottom bar, a rail or a sidebar — and outside any [AdaptiveNavigation] — it is a [TopBar]:
 *   back at the start, the actions at the end.
 * - Beside an [EdgeColumn] it draws the screen's title as a heading at the top of the content and
 *   hands back to the column, in place of the sidebar toggle. Where the actions go follows
 *   [dev.voir.areca.window.ActionReach], read from the whole window:
 *   - [dev.voir.areca.window.ActionReach.Header] — a tablet on its side, a pointer — draws them
 *     beside the title as labelled buttons, where there is room for the words.
 *   - [dev.voir.areca.window.ActionReach.Thumb] — a tablet held upright, an unfolded foldable —
 *     hands them to the foot of the column as round buttons, between the tabs and search, where the
 *     thumbs are.
 *
 *   The column shows the toolbar of the screen most recently drawn, so a screen pushed over another
 *   takes the column over and gives it back when it leaves.
 *
 * Actions past the room there is — three labelled buttons beside the title, three round buttons at
 * the foot of the column, three icons in the bar, More counted in each — fold into a More menu, in
 * order. Give back only to a screen pushed over another, and only when [ListDetailScope.isTwoPane] is
 * `false`.
 *
 * In two panes the detail's toolbar takes the column and the list's stays a bar over its pane, as a
 * [SupportingPane]'s supporting pane keeps its own header.
 *
 * ```kotlin
 * AdaptiveToolbar(
 *     title = "Lidl",
 *     onBack = if (isTwoPane) null else { { selected = null } },
 *     actions = listOf(
 *         AdaptiveToolbarAction(ArecaIcons.Edit, "Edit", onClick = edit, primary = true),
 *         AdaptiveToolbarAction(ArecaIcons.Share, "Share", onClick = share),
 *         AdaptiveToolbarAction(ArecaIcons.Trash, "Delete", onClick = delete, destructive = true),
 *     ),
 * )
 * ```
 *
 * @param title The screen's name.
 * @param modifier Modifier applied to the bar, or to the title's row beside a column.
 * @param onBack Called when the person goes back. `null` draws no back control.
 * @param actions The screen's actions, most important first.
 * @param backDescription What the back control is announced as.
 * @param moreDescription What the More control is announced and shown as.
 * @param windowInsets Insets the bar, or the title, keeps clear of: the status bar, when the screen
 *   reaches the top of the window.
 */
@Composable
public fun AdaptiveToolbar(
    title: String,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    actions: List<AdaptiveToolbarAction> = emptyList(),
    backDescription: String = "Back",
    moreDescription: String = "More",
    windowInsets: WindowInsets = WindowInsets(0, 0, 0, 0),
) {
    val host = LocalEdgeColumnHost.current
    if (host == null || LocalToolbarInline.current) {
        InlineToolbar(title, modifier, onBack, actions, backDescription, moreDescription, windowInsets)
        return
    }

    // The hands hold the device, not the pane: a detail pane narrower than it is tall still sits in a
    // window held on its side.
    val beside = ArecaTheme.hostWindow.actionReach == ActionReach.Header
    val entry = remember(host) { EdgeToolbarEntry() }
    SideEffect {
        entry.onBack = onBack
        entry.actions = if (beside) emptyList() else actions
        entry.backDescription = backDescription
        entry.moreDescription = moreDescription
    }
    DisposableEffect(host, entry) {
        host.entries += entry
        onDispose { host.entries -= entry }
    }
    Row(
        modifier = modifier
            .windowInsetsPadding(windowInsets)
            .horizontalContentInsets()
            .padding(top = ArecaTheme.spacing.xl, bottom = ArecaTheme.spacing.md),
        horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            modifier = Modifier.weight(1f).semantics { heading() },
            variant = TextVariant.HeadingLarge,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        if (beside && actions.isNotEmpty()) {
            HeaderActions(title, actions, moreDescription)
        }
    }
}

/**
 * The actions beside the title, as labelled buttons: the primary one filled with the brand, a
 * destructive one outlined in danger, the rest outlined; the overflow in More.
 *
 * @param title The screen's name, for the More menu's heading.
 * @param actions The screen's actions, most important first.
 * @param moreDescription What More is announced as.
 */
@Composable
private fun HeaderActions(
    title: String,
    actions: List<AdaptiveToolbarAction>,
    moreDescription: String,
) {
    val (shown, folded) = splitActions(actions, room = HeaderToolbarRoom)
    for (action in shown) {
        Button(
            text = action.label,
            onClick = action.onClick,
            style = when {
                action.primary -> ButtonStyle.solid(ActionColor.Brand)
                action.destructive -> ButtonStyle.outlined(ActionColor.Danger)
                else -> ButtonStyle.outlined(ActionColor.Neutral)
            },
            enabled = action.enabled,
            leading = { Icon(imageVector = action.icon, contentDescription = null) },
        )
    }
    if (folded.isNotEmpty()) {
        val menu = rememberDropdownPopup(placement = AnchoredPlacement.BelowEnd)
        IconButton(
            imageVector = BundledIcons.DotsVertical,
            contentDescription = moreDescription,
            onClick = menu::show,
            modifier = Modifier.anchored(menu.anchorState),
            style = IconButtonStyle.outlined(ActionColor.Neutral),
        )
        AdaptiveMenu(state = menu, title = title, actions = folded.map { it.toMenuItem() })
    }
}

/**
 * The toolbar as a [TopBar]: back at the start, the actions at the end, the overflow in More.
 *
 * @param title The screen's name.
 * @param modifier Modifier applied to the bar.
 * @param onBack Called when the person goes back, or `null`.
 * @param actions The screen's actions.
 * @param backDescription What back is announced as.
 * @param moreDescription What More is announced as.
 * @param windowInsets Insets the bar pads itself by.
 */
@Composable
private fun InlineToolbar(
    title: String,
    modifier: Modifier,
    onBack: (() -> Unit)?,
    actions: List<AdaptiveToolbarAction>,
    backDescription: String,
    moreDescription: String,
    windowInsets: WindowInsets,
) {
    val (shown, folded) = splitActions(actions, room = InlineToolbarRoom)
    TopBar(
        title = title,
        modifier = modifier,
        windowInsets = windowInsets,
        leading = if (onBack != null) { { BackAction(onClick = onBack, contentDescription = backDescription) } } else null,
        trailing = if (actions.isNotEmpty()) {
            {
                for (action in shown) {
                    IconAction(
                        icon = action.icon,
                        contentDescription = action.label,
                        onClick = action.onClick,
                        color = if (action.primary) ActionColor.Brand else ActionColor.Neutral,
                        enabled = action.enabled,
                    )
                }
                if (folded.isNotEmpty()) {
                    val menu = rememberDropdownPopup(placement = AnchoredPlacement.BelowEnd)
                    OverflowAction(
                        onClick = menu::show,
                        modifier = Modifier.anchored(menu.anchorState),
                        contentDescription = moreDescription,
                    )
                    AdaptiveMenu(state = menu, title = title, actions = folded.map { it.toMenuItem() })
                }
            }
        } else {
            null
        },
    )
}

/**
 * The head of the [EdgeColumn]'s toolbar for the screen on show: back, or [leading] when the screen
 * has none. It stays at the top of the column whatever the reach: going back is where every platform
 * puts it, and the system's own back gesture covers the hand that cannot reach it.
 *
 * @param host Where screens register their toolbars.
 * @param leading What stands at the head of the column when the screen has no back: the sidebar
 *   toggle.
 */
@Composable
internal fun EdgeToolbarBack(
    host: EdgeColumnHost,
    leading: (@Composable () -> Unit)?,
) {
    val entry = host.current
    val onBack = entry?.onBack
    if (onBack != null) {
        EdgeButton(imageVector = BundledIcons.ChevronLeft, contentDescription = entry.backDescription, onClick = onBack)
    } else {
        leading?.invoke()
    }
}

/**
 * The actions of the screen on show, as round buttons at the foot of the [EdgeColumn]: under
 * [ActionReach.Thumb] only, since under [ActionReach.Header] the toolbar draws them beside its title.
 * The overflow folds into More.
 *
 * @param host Where screens register their toolbars.
 * @param size Dimensions of the column, for how many buttons it takes.
 */
@Composable
internal fun EdgeToolbarActions(
    host: EdgeColumnHost,
    size: EdgeColumnSize,
) {
    val entry = host.current
    val actions = entry?.actions.orEmpty()
    if (actions.isEmpty()) return
    val (shown, folded) = splitActions(actions, room = size.maxToolbarButtons.coerceAtLeast(1))
    for (action in shown) {
        EdgeButton(
            imageVector = action.icon,
            contentDescription = action.label,
            onClick = action.onClick,
            emphasis = if (action.primary) EdgeButtonEmphasis.Primary else EdgeButtonEmphasis.Plain,
            enabled = action.enabled,
        )
    }
    if (folded.isNotEmpty()) {
        val menu = rememberDropdownPopup(placement = AnchoredPlacement.BelowEnd)
        EdgeButton(
            imageVector = BundledIcons.DotsVertical,
            contentDescription = entry?.moreDescription ?: "More",
            onClick = menu::show,
            modifier = Modifier.anchored(menu.anchorState),
        )
        AdaptiveMenu(state = menu, actions = folded.map { it.toMenuItem() })
    }
}

/**
 * Splits actions between the buttons there is room for and the More menu: all of them when they fit,
 * else one fewer than [room], so More itself takes the last place.
 *
 * @param actions The actions, most important first.
 * @param room Buttons there is room for, More included.
 * @return The actions drawn as buttons, then the actions folded into More.
 */
internal fun splitActions(
    actions: List<AdaptiveToolbarAction>,
    room: Int,
): Pair<List<AdaptiveToolbarAction>, List<AdaptiveToolbarAction>> =
    if (actions.size <= room) actions to emptyList() else actions.take(room - 1) to actions.drop(room - 1)

/** The action as a row of the More menu. */
private fun AdaptiveToolbarAction.toMenuItem(): AdaptiveMenuItem =
    AdaptiveMenuItem(label = label, icon = icon, destructive = destructive, enabled = enabled, onClick = onClick)

/** Icons a [TopBar] takes at its end before the rest fold into More. */
private const val InlineToolbarRoom = 3

/** Labelled buttons the title's row takes beside a column before the rest fold into More. */
private const val HeaderToolbarRoom = 3

/** One screen's toolbar, as it registered with the column. */
@Stable
internal class EdgeToolbarEntry {
    var onBack: (() -> Unit)? by mutableStateOf(null)
    var actions: List<AdaptiveToolbarAction> by mutableStateOf(emptyList())
    var backDescription: String by mutableStateOf("Back")
    var moreDescription: String by mutableStateOf("More")
}

/** The toolbars of the screens on show beside an [EdgeColumn], oldest first. */
@Stable
internal class EdgeColumnHost {
    val entries = mutableStateListOf<EdgeToolbarEntry>()

    /** The toolbar the column draws: the screen drawn most recently. */
    val current: EdgeToolbarEntry? get() = entries.lastOrNull()
}

/** The column a screen's [AdaptiveToolbar] hands its controls to, or `null` outside one. */
internal val LocalEdgeColumnHost = staticCompositionLocalOf<EdgeColumnHost?> { null }

/** Whether a toolbar here draws as a bar over its pane rather than in the column. */
internal val LocalToolbarInline = compositionLocalOf { false }
