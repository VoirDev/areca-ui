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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.adaptive.AdaptiveSheet
import dev.voir.areca.foundation.badge.Badge
import dev.voir.areca.foundation.badge.BadgeStyle
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.field.AffixTextField
import dev.voir.areca.foundation.field.FieldGroup
import dev.voir.areca.foundation.field.TextField
import dev.voir.areca.foundation.layout.VerticalDivider
import dev.voir.areca.foundation.overlay.FlowModalActionBar
import dev.voir.areca.foundation.overlay.FlowModalHeaderInsets
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.foundation.topbar.AddAction
import dev.voir.areca.foundation.topbar.CloseAction
import dev.voir.areca.foundation.topbar.TopBar
import dev.voir.areca.foundation.topbar.TopBarSize
import dev.voir.areca.foundation.topbar.TopBarStyle
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
import dev.voir.areca.window.WindowClassBox

/**
 * Showcase for [AdaptiveSheet].
 *
 * The sample is a finance app's "New transaction" form: a sheet from the bottom edge on a phone, a
 * centred dialog from an expanded window up.
 */
@Composable
fun AdaptiveSheetShowcase() = ShowcaseScaffold(
    title = "Adaptive sheet",
    summary = "A form or a short task over the screen, as a bottom sheet on a phone and a dialog on a tablet or desktop.",
    module = "foundation",
    status = ShowcaseStatus.Beta,
) {
    Playground(
        description = "Pick a window, then open the sheet. It opens over the whole playground window, not inside " +
            "the frame, and takes its form — sheet or dialog — from the chosen window.",
    ) {
        val preset by enumControl(
            key = "window",
            default = WindowPreset.Phone,
            options = WindowPreset.entries,
            optionLabel = { it.label },
        )
        val header by booleanControl(
            key = "header",
            default = true,
            description = "Pinned to the top: a modal TopBar clear of the drag handle.",
        )
        val footer by booleanControl(
            key = "footer",
            default = true,
            description = "Pinned to the bottom: a FlowModalActionBar with the commit.",
        )

        var visible by remember { mutableStateOf(false) }
        actionControl(key = "visible", buttonLabel = "Open", onInvoke = { visible = true })

        preview {
            WindowFrame(preset = preset) {
                NewTransactionSample(
                    preset = preset,
                    visible = visible,
                    onVisibleChange = { visible = it },
                    withHeader = header,
                    withFooter = footer,
                )
            }
        }
    }

    Variants(
        title = "Form by window",
        description = "Open each: a sheet below an expanded window or on a short one, a dialog from 840dp wide and 480dp tall.",
        layout = VariantLayout.Grid,
    ) {
        for ((preset, form) in SheetForms) {
            variant(preset.label, detail = form) {
                WindowFrame(preset = preset, maxHeight = 240.dp) { NewTransactionSample(preset = preset) }
            }
        }
    }

    EdgeCases(description = "Openers whose own size is not the window's, and a sheet with only a body.") {
        case(
            name = "Opened from a narrow pane on a laptop",
            expectation = "The 380dp pane reads Compact, but the form follows the window: a dialog, not a sheet.",
        ) {
            WindowFrame(preset = WindowPreset.Laptop, maxHeight = 300.dp) {
                Row(modifier = Modifier.fillMaxSize()) {
                    WindowClassBox(modifier = Modifier.width(380.dp).fillMaxHeight()) {
                        NewTransactionSample(preset = WindowPreset.Laptop)
                    }
                    VerticalDivider()
                    Box(modifier = Modifier.weight(1f).fillMaxHeight().padding(32.dp)) {
                        Text("Lidl · −€42.80", variant = TextVariant.HeadingSmall)
                    }
                }
            }
        }
        case(
            name = "Tablet held upright",
            expectation = "820dp is Medium: a sheet, capped at 640dp and centred along the bottom edge.",
        ) {
            WindowFrame(preset = WindowPreset.Tablet, maxHeight = 300.dp) {
                NewTransactionSample(preset = WindowPreset.Tablet)
            }
        }
        case(
            name = "No header or footer",
            expectation = "The body alone; it is dismissed with the scrim, back or a drag, and nothing else.",
        ) {
            WindowFrame(preset = WindowPreset.Phone, maxHeight = 280.dp) {
                NewTransactionSample(preset = WindowPreset.Phone, withHeader = false, withFooter = false)
            }
        }
    }
}

/** Windows on either side of the sheet–dialog line, with the form each one gets. */
private val SheetForms = listOf(
    WindowPreset.Phone to "sheet",
    WindowPreset.PhoneLandscape to "sheet: too short for a dialog",
    WindowPreset.FoldUnfolded to "sheet, capped at 640dp",
    WindowPreset.TabletLandscape to "dialog",
    WindowPreset.Laptop to "dialog",
)

/**
 * An activity screen with a "New transaction" action, and the sheet it opens.
 *
 * @param preset Window the screen is drawn in, for its insets.
 * @param visible Whether the sheet is open, when the caller holds it; `null` to hold it here.
 * @param onVisibleChange Called when the sheet opens or is dismissed, when the caller holds it.
 * @param withHeader Whether the sheet has a title bar.
 * @param withFooter Whether the sheet has an action bar.
 */
@Composable
private fun NewTransactionSample(
    preset: WindowPreset,
    visible: Boolean? = null,
    onVisibleChange: (Boolean) -> Unit = {},
    withHeader: Boolean = true,
    withFooter: Boolean = true,
) {
    var ownVisible by remember { mutableStateOf(false) }
    val open = visible ?: ownVisible
    val setOpen: (Boolean) -> Unit = { if (visible == null) ownVisible = it else onVisibleChange(it) }
    val window = ArecaTheme.window

    Column(modifier = Modifier.fillMaxSize().windowInsetsPadding(preset.windowInsets)) {
        TopBar(
            title = "Activity",
            trailing = { AddAction(onClick = { setOpen(true) }, contentDescription = "New transaction") },
        )
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(text = "New transaction", onClick = { setOpen(true) })
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    "Opener · ${window.size.width.value.toInt()}dp",
                    variant = TextVariant.BodySmall,
                    color = TextColor.Muted,
                )
                Badge(style = BadgeStyle.Neutral) { Text(window.width.name) }
                Badge(style = BadgeStyle.Neutral) { Text("${window.height.name} height") }
            }
        }
    }

    AdaptiveSheet(
        visible = open,
        onDismissRequest = { setOpen(false) },
        header = if (withHeader) {
            {
                TopBar(
                    title = "New transaction",
                    modifier = Modifier.fillMaxWidth(),
                    style = TopBarStyle.Modal,
                    size = TopBarSize.Modal,
                    windowInsets = FlowModalHeaderInsets,
                    trailing = { CloseAction(onClick = { setOpen(false) }) },
                )
            }
        } else {
            null
        },
        footer = if (withFooter) {
            { FlowModalActionBar(confirmText = "Save", onConfirm = { setOpen(false) }) }
        } else {
            null
        },
    ) {
        TransactionForm()
    }
}

/** The sheet's body: amount, payee and a note. */
@Composable
private fun TransactionForm() {
    var amount by remember { mutableStateOf("") }
    var payee by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = ArecaTheme.spacing.xl, vertical = ArecaTheme.spacing.lg),
        verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
    ) {
        FieldGroup(modifier = Modifier.fillMaxWidth(), label = "Amount") { field ->
            AffixTextField(
                value = amount,
                onValueChange = { amount = it },
                modifier = Modifier.fillMaxWidth(),
                prefix = { Text("€") },
                field = field,
                placeholder = { Text("0.00") },
            )
        }
        FieldGroup(modifier = Modifier.fillMaxWidth(), label = "Payee") { field ->
            TextField(
                value = payee,
                onValueChange = { payee = it },
                modifier = Modifier.fillMaxWidth(),
                field = field,
                singleLine = true,
                placeholder = { Text("Lidl") },
            )
        }
        FieldGroup(modifier = Modifier.fillMaxWidth(), label = "Note", assistive = "Only you can see this.") { field ->
            TextField(
                value = note,
                onValueChange = { note = it },
                modifier = Modifier.fillMaxWidth(),
                field = field,
                minLines = 2,
            )
        }
    }
}
