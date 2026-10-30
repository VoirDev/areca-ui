package dev.voir.areca.playground.foundation.adaptive

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.adaptive.ListDetail
import dev.voir.areca.foundation.feedback.Fallback
import dev.voir.areca.foundation.layout.VerticalDivider
import dev.voir.areca.foundation.list.ListItem
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.foundation.topbar.BackAction
import dev.voir.areca.foundation.topbar.TopBar
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.canvas.WindowFrame
import dev.voir.areca.playground.showcase.canvas.WindowPreset
import dev.voir.areca.playground.showcase.canvas.windowInsets
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.horizontalContentInsets

/** Width of the list pane as the playground offers it. */
private enum class ListWidthOption(val label: String, val width: Dp) {
    Default("listPaneWidth", Dp.Unspecified),
    Narrow("280dp", 280.dp),
    Wide("400dp", 400.dp),
}

/**
 * Showcase for [ListDetail].
 *
 * The sample is a transactions screen: the list and the open transaction side by side from 600dp,
 * one at a time below that, with the detail's back action shown only when the list is hidden.
 */
@Composable
fun ListDetailShowcase() = ShowcaseScaffold(
    title = "List detail",
    summary = "A list and the item that is open, side by side when there is room and one at a time when there is not.",
    module = "foundation",
    status = ShowcaseStatus.Beta,
) {
    Playground(
        description = "Open a transaction, then change the window: the selection and the list's scroll stay put. " +
            "Under a pointer, drag the divider to resize the list; double-click it to put it back.",
    ) {
        val preset by enumControl(
            key = "window",
            default = WindowPreset.Tablet,
            options = WindowPreset.entries,
            optionLabel = { it.label },
        )
        val listWidth by enumControl(
            key = "listWidth",
            default = ListWidthOption.Default,
            options = ListWidthOption.entries,
            optionLabel = { it.label },
            description = "Unspecified takes AdaptiveDefaults.listPaneWidth: 280, 320 or 360dp by the container.",
        )
        val placeholder by booleanControl(
            key = "detailPlaceholder",
            default = true,
            description = "Two panes only: what the detail pane shows while nothing is open.",
        )

        preview {
            WindowFrame(preset = preset) {
                TransactionsScreen(
                    preset = preset,
                    listWidth = listWidth.width,
                    withPlaceholder = placeholder,
                )
            }
        }
    }

    Variants(
        title = "Every window",
        description = "Lidl is open in each: beside the list from 600dp, in place of it with a back action below.",
        layout = VariantLayout.Grid,
    ) {
        for (preset in WindowPreset.entries) {
            variant(preset.label, detail = "${preset.width.value.toInt()} × ${preset.height.value.toInt()}dp") {
                WindowFrame(preset = preset, maxHeight = 280.dp) {
                    TransactionsScreen(preset = preset, initialSelection = Transactions.first().id)
                }
            }
        }
    }

    States(description = "What each pane shows before and after something is opened.") {
        variant("Nothing open, two panes", detail = "detailPlaceholder shown") {
            WindowFrame(preset = WindowPreset.Tablet, maxHeight = 280.dp) {
                TransactionsScreen(preset = WindowPreset.Tablet)
            }
        }
        variant("Nothing open, no placeholder", detail = "detailPlaceholder = null") {
            WindowFrame(preset = WindowPreset.Tablet, maxHeight = 280.dp) {
                TransactionsScreen(preset = WindowPreset.Tablet, withPlaceholder = false)
            }
        }
        variant("Nothing open, one pane", detail = "the list alone") {
            WindowFrame(preset = WindowPreset.Phone, maxHeight = 280.dp) {
                TransactionsScreen(preset = WindowPreset.Phone)
            }
        }
        variant("Open, one pane", detail = "the detail, with BackAction") {
            WindowFrame(preset = WindowPreset.Phone, maxHeight = 280.dp) {
                TransactionsScreen(preset = WindowPreset.Phone, initialSelection = Transactions[2].id)
            }
        }
    }

    EdgeCases(description = "Containers near the two-pane threshold, and one that is not the window.") {
        case(
            name = "Fold, unfolded",
            expectation = "690dp is past 600dp: two panes, a 280dp list and a detail that lays itself out as Compact.",
        ) {
            WindowFrame(preset = WindowPreset.FoldUnfolded, maxHeight = 320.dp) {
                TransactionsScreen(preset = WindowPreset.FoldUnfolded, initialSelection = Transactions.first().id)
            }
        }
        case(
            name = "Phone on its side",
            expectation = "852dp wide is room for two panes; the list scrolls in the short window rather than clipping.",
        ) {
            WindowFrame(preset = WindowPreset.PhoneLandscape, maxHeight = 240.dp) {
                TransactionsScreen(preset = WindowPreset.PhoneLandscape, initialSelection = Transactions[3].id)
            }
        }
        case(
            name = "560dp column on a laptop",
            expectation = "The window is Large but the layout is given 560dp: one pane, because the threshold reads the container.",
        ) {
            WindowFrame(preset = WindowPreset.Laptop, maxHeight = 300.dp) {
                Row(modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier.width(560.dp).fillMaxHeight()) {
                        TransactionsScreen(preset = WindowPreset.Laptop, initialSelection = Transactions.first().id)
                    }
                    VerticalDivider()
                    Box(modifier = Modifier.weight(1f).fillMaxHeight().padding(24.dp)) {
                        Text("Spending by category", variant = TextVariant.HeadingSmall)
                    }
                }
            }
        }
    }
}

/**
 * The transactions screen, written as a consumer would: the open transaction is the screen's state,
 * and [ListDetail] decides how much of it shows.
 *
 * @param preset Window the screen is drawn in, for its insets.
 * @param listWidth Width of the list beside the detail, or [Dp.Unspecified] for the default.
 * @param withPlaceholder Whether the empty detail pane says what to do.
 * @param initialSelection Transaction open when the screen first draws, or `null` for none.
 */
@Composable
private fun TransactionsScreen(
    preset: WindowPreset,
    listWidth: Dp = Dp.Unspecified,
    withPlaceholder: Boolean = true,
    initialSelection: Int? = null,
) {
    var selected by remember { mutableStateOf(initialSelection) }

    ListDetail(
        showDetail = selected != null,
        modifier = Modifier.fillMaxSize().windowInsetsPadding(preset.windowInsets),
        listWidth = listWidth,
        detailPlaceholder = if (withPlaceholder) ({ PickTransaction() }) else null,
        list = {
            TransactionList(
                // Only a list beside the detail marks the open row; alone, it has nothing to point at.
                selected = selected.takeIf { isTwoPane },
                onOpen = { selected = it },
            )
        },
        detail = {
            val transaction = Transactions.first { it.id == selected }
            TransactionDetail(
                transaction = transaction,
                onBack = if (isTwoPane) null else ({ selected = null }),
            )
        },
    )
}

/**
 * The list pane.
 *
 * @param selected Transaction marked as open, or `null`.
 * @param onOpen Called with a transaction's id when it is chosen.
 */
@Composable
private fun TransactionList(selected: Int?, onOpen: (Int) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopBar(title = "Activity", subtitle = paneLabel("List pane"))
        Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
            for (transaction in Transactions) {
                ListItem(
                    title = { Text(transaction.merchant, maxLines = 1) },
                    subtitle = { Text("${transaction.category} · ${transaction.date}", maxLines = 1) },
                    value = {
                        Text(
                            transaction.amount,
                            variant = TextVariant.BodyMedium,
                            color = if (transaction.isIncome) TextColor.Success else TextColor.Base,
                        )
                    },
                    selected = transaction.id == selected,
                    onClick = { onOpen(transaction.id) },
                )
            }
        }
    }
}

/**
 * The detail pane: the open transaction, with a way back to the list when the list is hidden.
 *
 * @param transaction The open transaction.
 * @param onBack Returns to the list, or `null` when the list is on screen beside it.
 */
@Composable
private fun TransactionDetail(transaction: Transaction, onBack: (() -> Unit)?) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopBar(
            title = transaction.merchant,
            subtitle = paneLabel("Detail pane"),
            leading = if (onBack != null) ({ BackAction(onClick = onBack) }) else null,
        )
        Column(
            modifier = Modifier.fillMaxWidth().horizontalContentInsets(),
            verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xs),
        ) {
            Text(
                transaction.amount,
                variant = TextVariant.HeadingLarge,
                color = if (transaction.isIncome) TextColor.Success else TextColor.Base,
            )
            Text(transaction.date, variant = TextVariant.BodySmall, color = TextColor.Muted)
        }
        Column(modifier = Modifier.padding(top = ArecaTheme.spacing.lg)) {
            ListItem(title = { Text("Category") }, value = { Text(transaction.category) })
            ListItem(title = { Text("Wallet") }, value = { Text(transaction.wallet) })
            ListItem(title = { Text("Booked") }, value = { Text(transaction.date) })
        }
    }
}

/** The detail pane while nothing is open. */
@Composable
private fun PickTransaction() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Fallback(
            heading = { Text("Pick a transaction") },
            body = { Text("Its category, wallet and booking date open here.") },
        )
    }
}

/**
 * Names a pane with the width and class it reports, so the reader can see each pane lay itself out
 * for its own width.
 *
 * @param name The pane.
 * @return "List pane · 320dp · Compact", for example.
 */
@Composable
private fun paneLabel(name: String): String {
    val pane = ArecaTheme.window
    return "$name · ${pane.size.width.value.toInt()}dp · ${pane.width.name}"
}
