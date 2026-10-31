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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.adaptive.AdaptiveMenu
import dev.voir.areca.foundation.adaptive.AdaptiveMenuItem
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
import dev.voir.areca.icons.DotsVertical
import dev.voir.areca.icons.Edit
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
 * [contextMenu] for a right-click or a long press: a dropdown under a pointer or on a wide window, a
 * sheet from the bottom edge on a touched phone or portrait tablet.
 */
@Composable
fun AdaptiveMenuShowcase() = ShowcaseScaffold(
    title = "Adaptive menu",
    summary = "The actions for one row or card, as a dropdown under a pointer and a bottom sheet under a finger.",
    module = "foundation",
    status = ShowcaseStatus.Beta,
) {
    Playground(
        description = "Press a row's overflow button, right-click the row, or long-press it under touch. The menu " +
            "opens over the whole playground window, and takes its form — dropdown or sheet — from the chosen " +
            "window and input.",
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
            description = "Window uses the preset's own. Any pointer gets the dropdown, whatever the width.",
        )
        val title by booleanControl(
            key = "title",
            default = true,
            description = "What the actions act on, at the head of the sheet. The dropdown shows none.",
        )
        val icons by booleanControl(key = "icon", default = true, description = "A glyph before each label.")
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
                ActivitySample(preset = preset, withTitle = title, withIcons = icons, placement = placement)
            }
        }
    }

    Variants(
        title = "Form by window",
        description = "Open each: a dropdown under a pointer or from 840dp wide, a sheet on a narrower touched window.",
        layout = VariantLayout.Grid,
    ) {
        for ((preset, form) in MenuForms) {
            variant(preset.label, detail = form) {
                WindowFrame(preset = preset, maxHeight = 240.dp) { ActivitySample(preset = preset) }
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
            name = "Phone-sized window under a pointer",
            expectation = "393dp is Compact, but a pointer needs no reach: the dropdown beside the button, not a sheet.",
        ) {
            WindowFrame(preset = WindowPreset.Phone, input = InputMode.Pointer, maxHeight = 320.dp) {
                ActivitySample(preset = WindowPreset.Phone)
            }
        }
    }
}

/** Windows and inputs on either side of the dropdown–sheet line, with the form each one gets. */
private val MenuForms = listOf(
    WindowPreset.Phone to "sheet: touch, compact",
    WindowPreset.Tablet to "sheet: touch, medium",
    WindowPreset.TabletLandscape to "dropdown: expanded",
    WindowPreset.NarrowWindow to "dropdown: pointer",
    WindowPreset.Laptop to "dropdown",
)

/**
 * An activity list whose rows each open an [AdaptiveMenu], and a line saying what was last chosen.
 *
 * @param preset Window the list is drawn in, for its insets.
 * @param withTitle Whether the sheet names the transaction at its head.
 * @param withIcons Whether each action has a glyph.
 * @param placement Where the dropdown hangs off its trigger.
 * @param moveEnabled Whether "Move to wallet" can be chosen.
 * @param longLabel Whether "Move to wallet" names the wallet in full.
 */
@Composable
private fun ActivitySample(
    preset: WindowPreset,
    withTitle: Boolean = true,
    withIcons: Boolean = true,
    placement: AnchoredPlacement = AnchoredPlacement.BelowEnd,
    moveEnabled: Boolean = true,
    longLabel: Boolean = false,
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
                AdaptiveMenu(
                    state = menu,
                    title = if (withTitle) "${transaction.merchant} · ${transaction.amount}" else null,
                    actions = listOf(
                        AdaptiveMenuItem(
                            label = "Edit",
                            icon = if (withIcons) ArecaIcons.Edit else null,
                            onClick = { onChosen("Edit") },
                        ),
                        AdaptiveMenuItem(
                            label = "Duplicate",
                            icon = if (withIcons) ArecaIcons.Copy else null,
                            onClick = { onChosen("Duplicate") },
                        ),
                        AdaptiveMenuItem(
                            label = moveLabel,
                            icon = if (withIcons) ArecaIcons.Wallet else null,
                            enabled = moveEnabled,
                            onClick = { onChosen("Move to wallet") },
                        ),
                        AdaptiveMenuItem(
                            label = "Delete",
                            icon = if (withIcons) ArecaIcons.Trash else null,
                            destructive = true,
                            onClick = { onChosen("Delete") },
                        ),
                    ),
                )
            }
        },
    )
}
