package dev.voir.areca.playground.foundation.adaptive

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.adaptive.AdaptiveMenu
import dev.voir.areca.foundation.adaptive.AdaptiveMenuIconPosition
import dev.voir.areca.foundation.adaptive.AdaptiveMenuItem
import dev.voir.areca.foundation.adaptive.AdaptiveMenuPresentation
import dev.voir.areca.foundation.adaptive.LocalAdaptiveMenuPresentation
import dev.voir.areca.foundation.adaptive.AdaptiveMenuSize
import dev.voir.areca.foundation.adaptive.contextMenu
import dev.voir.areca.foundation.badge.Badge
import dev.voir.areca.foundation.badge.BadgeStyle
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.IconButton
import dev.voir.areca.foundation.button.IconButtonStyle
import dev.voir.areca.foundation.list.ListItem
import dev.voir.areca.foundation.overlay.AnchoredPlacement
import dev.voir.areca.foundation.overlay.anchored
import dev.voir.areca.foundation.overlay.rememberDropdownPopup
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.foundation.topbar.TopBar
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.Copy
import dev.voir.areca.icons.DotsHorizontal
import dev.voir.areca.icons.DotsVertical
import dev.voir.areca.icons.Edit
import dev.voir.areca.icons.StopCircle
import dev.voir.areca.icons.Trash
import dev.voir.areca.icons.Wallet
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.canvas.WindowFrame
import dev.voir.areca.playground.showcase.canvas.WindowPreset
import dev.voir.areca.playground.showcase.canvas.windowInsets
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.window.InputMode

/** Input mode as the playground offers it: the window's own, or one forced over it. */
private enum class MenuInputOption { Window, Touch, Pointer }

/**
 * Showcase for [AdaptiveMenu] and the [AdaptiveMenuItem]s it lists.
 *
 * The sample is a finance app's activity list, each row with an overflow button and
 * [contextMenu] for a right-click or a long press, in the form the app chose: a dropdown at the
 * trigger or a sheet from the bottom edge, the same on every window.
 */
@Composable
fun AdaptiveMenuShowcase() = ShowcaseScaffold(
    title = "Adaptive menu",
    summary = "The actions for one row, card or details sheet, as a card at the trigger or a bottom sheet.",
    module = "foundation",
    status = ShowcaseStatus.Beta,
) {
    Playground(
        description = "Press a row's overflow button, right-click the row, or long-press it under touch. The menu " +
            "opens over the whole playground window in the chosen presentation, whatever the window and input.",
    ) {
        val preset by enumControl(
            key = "window",
            default = WindowPreset.Phone,
            options = WindowPreset.entries,
            optionLabel = { it.label },
        )
        val input by enumControl(
            key = "input",
            default = MenuInputOption.Window,
            options = MenuInputOption.entries,
            optionLabel = { it.name },
            description = "Window uses the preset's own. A pointer opens the menu with a right-click, touch with a long press.",
        )
        val title by booleanControl(
            key = "title",
            default = true,
            description = "What the actions act on, at the head of the sheet. The dropdown shows none.",
        )
        val presentation by enumControl(
            key = "presentation",
            default = AdaptiveMenuPresentation.Dropdown,
            options = AdaptiveMenuPresentation.entries,
            optionLabel = { it.name },
            description = "Dropdown is a card at the trigger, Sheet a sheet from the bottom edge, on every window.",
        )
        val grouped by booleanControl(
            key = "grouped",
            default = true,
            description = "The groups form: a hairline between groups, Delete last on its own. Off is the flat actions form.",
        )
        val icons by booleanControl(key = "icon", default = true, description = "A glyph beside each label.")
        val iconPosition by enumControl(
            key = "iconPosition",
            default = AdaptiveMenuIconPosition.Trailing,
            options = AdaptiveMenuIconPosition.entries,
            optionLabel = { it.name },
            description = "Which end of the row the glyph sits at, in the groups form.",
        )
        val placement by enumControl(
            key = "placement",
            default = AnchoredPlacement.BelowEnd,
            options = AnchoredPlacement.entries,
            optionLabel = { it.name },
            description = "Where the dropdown hangs off its trigger; the sheet ignores it.",
        )

        preview {
            WindowFrame(
                preset = preset,
                input = when (input) {
                    MenuInputOption.Window -> preset.input
                    MenuInputOption.Touch -> InputMode.Touch
                    MenuInputOption.Pointer -> InputMode.Pointer
                },
            ) {
                ActivitySample(
                    preset = preset,
                    withTitle = title,
                    withIcons = icons,
                    placement = placement,
                    grouped = grouped,
                    presentation = presentation,
                    iconPosition = iconPosition,
                )
            }
        }
    }

    Variants(
        title = "Form",
        description = "Open each: the form is the presentation the app passes, never the window's.",
        layout = VariantLayout.Grid,
    ) {
        for (presentation in AdaptiveMenuPresentation.entries) {
            for (preset in listOf(WindowPreset.Phone, WindowPreset.Laptop)) {
                variant("${presentation.name} / ${preset.label}", detail = presentation.name.lowercase()) {
                    WindowFrame(preset = preset, maxHeight = 240.dp) {
                        ActivitySample(preset = preset, grouped = true, presentation = presentation)
                    }
                }
            }
        }
    }

    Variants(
        title = "Details menu",
        description = "The ⋯ in a details sheet's heading on a touched phone: a card under it, Edit and End together, " +
            "Delete apart in red. Open it to see it grow from the ⋯'s corner.",
        layout = VariantLayout.Grid,
    ) {
        for (position in AdaptiveMenuIconPosition.entries) {
            variant("Dropdown / ${position.name}", detail = "glyph ${position.name.lowercase()}") {
                WindowFrame(preset = WindowPreset.Phone, maxHeight = 320.dp) { DetailsSample(iconPosition = position) }
            }
        }
    }

    EdgeCases(description = "Actions that cannot run, labels that do not fit, and an input the window does not suggest.") {
        case(
            name = "Disabled action",
            expectation = "Move to wallet is listed but cannot be chosen; the others run and close the menu.",
        ) {
            WindowFrame(preset = WindowPreset.Laptop, maxHeight = 260.dp) {
                ActivitySample(preset = WindowPreset.Laptop, moveEnabled = false)
            }
        }
        case(
            name = "Long label",
            expectation = "The dropdown grows to 320dp at most, then the label wraps rather than widening it.",
        ) {
            WindowFrame(preset = WindowPreset.Laptop, maxHeight = 260.dp) {
                ActivitySample(preset = WindowPreset.Laptop, longLabel = true)
            }
        }
        case(
            name = "Destructive action given first",
            expectation = "Delete is listed first in its group but drawn last, below one hairline, in the danger ink.",
        ) {
            WindowFrame(preset = WindowPreset.Phone, maxHeight = 320.dp) { DetailsSample(deleteFirst = true) }
        }
        case(
            name = "Phone-sized window under a pointer",
            expectation = "A right-click opens the menu at the pointer; the form is still the one the app chose.",
        ) {
            WindowFrame(preset = WindowPreset.Phone, input = InputMode.Pointer, maxHeight = 320.dp) {
                ActivitySample(preset = WindowPreset.Phone)
            }
        }
    }
}

/**
 * An activity list whose rows each open an [AdaptiveMenu], and a line saying what was last chosen.
 *
 * @param preset Window the list is drawn in, for its insets.
 * @param withTitle Whether the sheet names the transaction at its head.
 * @param withIcons Whether each action has a glyph.
 * @param placement Where the dropdown hangs off its trigger.
 * @param moveEnabled Whether "Move to wallet" can be chosen.
 * @param longLabel Whether "Move to wallet" names the wallet in full.
 * @param grouped Whether the menu takes the groups form rather than the flat actions form.
 * @param presentation The menu's form.
 * @param iconPosition Which end of a row the glyph sits at in the groups form.
 */
@Composable
private fun ActivitySample(
    preset: WindowPreset,
    withTitle: Boolean = true,
    withIcons: Boolean = true,
    placement: AnchoredPlacement = AnchoredPlacement.BelowEnd,
    moveEnabled: Boolean = true,
    longLabel: Boolean = false,
    grouped: Boolean = false,
    presentation: AdaptiveMenuPresentation = AdaptiveMenuPresentation.Dropdown,
    iconPosition: AdaptiveMenuIconPosition = AdaptiveMenuIconPosition.Trailing,
) {
    var lastChosen by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxSize().windowInsetsPadding(preset.windowInsets)) {
        TopBar(title = "Activity", subtitle = lastChosen ?: "Nothing chosen yet")
        for (transaction in Transactions.take(3)) {
            TransactionRow(
                transaction = transaction,
                withTitle = withTitle,
                withIcons = withIcons,
                placement = placement,
                moveEnabled = moveEnabled,
                longLabel = longLabel,
                grouped = grouped,
                presentation = presentation,
                iconPosition = iconPosition,
                onChosen = { action -> lastChosen = "$action · ${transaction.merchant}" },
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val window = ArecaTheme.window
            Text("Window", variant = TextVariant.BodySmall, color = TextColor.Muted)
            Badge(style = BadgeStyle.Neutral) { Text(window.width.name) }
            Badge(style = BadgeStyle.Neutral) { Text(window.input.name) }
        }
    }
}

/**
 * One transaction row with its overflow button and the menu that button opens.
 *
 * @param transaction The transaction.
 * @param withTitle Whether the sheet names the transaction at its head.
 * @param withIcons Whether each action has a glyph.
 * @param placement Where the dropdown hangs off its trigger.
 * @param moveEnabled Whether "Move to wallet" can be chosen.
 * @param longLabel Whether "Move to wallet" names the wallet in full.
 * @param grouped Whether the menu takes the groups form rather than the flat actions form.
 * @param presentation The menu's form.
 * @param iconPosition Which end of a row the glyph sits at in the groups form.
 * @param onChosen Called with the label of the action chosen.
 */
@Composable
private fun TransactionRow(
    transaction: Transaction,
    withTitle: Boolean,
    withIcons: Boolean,
    placement: AnchoredPlacement,
    moveEnabled: Boolean,
    longLabel: Boolean,
    grouped: Boolean,
    presentation: AdaptiveMenuPresentation,
    iconPosition: AdaptiveMenuIconPosition,
    onChosen: (String) -> Unit,
) {
    val menu = rememberDropdownPopup(placement = placement)
    val moveLabel = if (longLabel) "Move to wallet “Shared household expenses”" else "Move to wallet"

    ListItem(
        title = { Text(transaction.merchant, maxLines = 1) },
        subtitle = { Text("${transaction.category} · ${transaction.amount}", maxLines = 1) },
        modifier = Modifier.contextMenu(menu),
        trailing = {
            Box {
                IconButton(
                    imageVector = ArecaIcons.DotsVertical,
                    contentDescription = "Actions for ${transaction.merchant}",
                    onClick = menu::show,
                    modifier = Modifier.anchored(menu.anchorState),
                    style = IconButtonStyle.plain(ActionColor.Neutral),
                )
                val edit = AdaptiveMenuItem(
                    label = "Edit",
                    icon = if (withIcons) ArecaIcons.Edit else null,
                    onClick = { onChosen("Edit") },
                )
                val duplicate = AdaptiveMenuItem(
                    label = "Duplicate",
                    icon = if (withIcons) ArecaIcons.Copy else null,
                    onClick = { onChosen("Duplicate") },
                )
                val move = AdaptiveMenuItem(
                    label = moveLabel,
                    icon = if (withIcons) ArecaIcons.Wallet else null,
                    enabled = moveEnabled,
                    onClick = { onChosen("Move to wallet") },
                )
                val delete = AdaptiveMenuItem(
                    label = "Delete",
                    icon = if (withIcons) ArecaIcons.Trash else null,
                    destructive = true,
                    onClick = { onChosen("Delete") },
                )
                val title = if (withTitle) "${transaction.merchant} · ${transaction.amount}" else null
                if (grouped) {
                    AdaptiveMenu(
                        state = menu,
                        title = title,
                        presentation = presentation,
                        size = AdaptiveMenuSize.default(iconPosition = iconPosition),
                        groups = listOf(listOf(edit, duplicate), listOf(move), listOf(delete)),
                    )
                } else {
                    CompositionLocalProvider(LocalAdaptiveMenuPresentation provides presentation) {
                        AdaptiveMenu(state = menu, title = title, actions = listOf(edit, duplicate, move, delete))
                    }
                }
            }
        },
    )
}

/**
 * A details sheet's heading with a ⋯ that opens its actions as a card: Edit and End, then Delete.
 *
 * @param iconPosition Which end of a row the glyph sits at.
 * @param deleteFirst Whether Delete is given first in the one group, to show it is drawn last anyway.
 */
@Composable
private fun DetailsSample(
    iconPosition: AdaptiveMenuIconPosition = AdaptiveMenuIconPosition.Trailing,
    deleteFirst: Boolean = false,
) {
    var lastChosen by remember { mutableStateOf<String?>(null) }
    val menu = rememberDropdownPopup(placement = AnchoredPlacement.BelowEnd)
    val edit = AdaptiveMenuItem(label = "Edit", icon = ArecaIcons.Edit, onClick = { lastChosen = "Edit" })
    val end = AdaptiveMenuItem(label = "End subscription", icon = ArecaIcons.StopCircle, onClick = { lastChosen = "End" })
    val delete = AdaptiveMenuItem(
        label = "Delete",
        icon = ArecaIcons.Trash,
        destructive = true,
        onClick = { lastChosen = "Delete" },
    )

    Column(modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowPreset.Phone.windowInsets)) {
        TopBar(
            title = "Streaming plan",
            subtitle = lastChosen ?: "Nothing chosen yet",
            trailing = {
                Box {
                    IconButton(
                        imageVector = ArecaIcons.DotsHorizontal,
                        contentDescription = "Actions for Streaming plan",
                        onClick = menu::show,
                        modifier = Modifier.anchored(menu.anchorState),
                        style = IconButtonStyle.plain(ActionColor.Neutral),
                    )
                    AdaptiveMenu(
                        state = menu,
                        presentation = AdaptiveMenuPresentation.Dropdown,
                        size = AdaptiveMenuSize.default(iconPosition = iconPosition),
                        groups = if (deleteFirst) listOf(listOf(delete, edit, end)) else listOf(listOf(edit, end), listOf(delete)),
                    )
                }
            },
        )
    }
}
