package dev.voir.areca.playground.foundation.window

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.badge.Badge
import dev.voir.areca.foundation.badge.BadgeStyle
import dev.voir.areca.foundation.layout.VerticalDivider
import dev.voir.areca.foundation.list.ListItem
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
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
import dev.voir.areca.window.AdaptiveContent
import dev.voir.areca.window.InputMode
import dev.voir.areca.window.WindowClass
import dev.voir.areca.window.WindowClassBox
import dev.voir.areca.window.adaptive

/** Input mode as the playground offers it: the window's own, or one forced over it. */
private enum class InputOption { Window, Touch, Pointer }

/**
 * Showcase for `ArecaTheme.window` and the composables that read and scope it: [AdaptiveContent],
 * [WindowClassBox] and [adaptive].
 *
 * The sample is one small mailbox drawn by the code a consumer would write: a list on a compact
 * window, a list beside the open message from medium up, with each pane in a [WindowClassBox] so
 * it reports its own class.
 */
@Composable
fun WindowClassShowcase() = ShowcaseScaffold(
    title = "Window class",
    summary = "The width and height classes and input mode a screen is laid out for, read per window or per pane.",
    module = "foundation",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "Pick a window and see which classes it falls in and what the sample draws.") {
        val preset by enumControl(
            key = "window",
            default = WindowPreset.FoldUnfolded,
            options = WindowPreset.entries,
            optionLabel = { it.label },
        )
        val input by enumControl(
            key = "input",
            default = InputOption.Window,
            options = InputOption.entries,
            optionLabel = { it.name },
            description = "Window uses the preset's own: touch on phones and tablets, a pointer on desktops.",
        )

        preview {
            WindowFrame(
                preset = preset,
                input = when (input) {
                    InputOption.Window -> preset.input
                    InputOption.Touch -> InputMode.Touch
                    InputOption.Pointer -> InputMode.Pointer
                },
            ) {
                MailboxSample(preset)
            }
        }
    }

    Variants(
        title = "Every window",
        description = "The same code at each named window: one pane below 600dp, two from there up.",
        layout = VariantLayout.Grid,
    ) {
        for (preset in WindowPreset.entries) {
            variant(preset.label, detail = "${preset.width.value.toInt()} × ${preset.height.value.toInt()}dp") {
                WindowFrame(preset = preset, maxHeight = 280.dp) { MailboxSample(preset) }
            }
        }
    }

    EdgeCases(description = "Windows whose class is not what the device suggests.") {
        case(
            name = "Phone on its side",
            expectation = "852dp wide is Expanded, but 393dp tall is Compact height: the profile is PhoneLandscape.",
        ) {
            WindowFrame(preset = WindowPreset.PhoneLandscape, maxHeight = 240.dp) {
                MailboxSample(WindowPreset.PhoneLandscape)
            }
        }
        case(
            name = "Desktop window dragged narrow",
            expectation = "420dp under a pointer is Compact: the phone layout, still pointer-driven.",
        ) {
            WindowFrame(preset = WindowPreset.NarrowWindow, maxHeight = 320.dp) {
                MailboxSample(WindowPreset.NarrowWindow)
            }
        }
        case(
            name = "Pane on a large window",
            expectation = "The window is Large, while the 320dp list pane reads Compact and the detail Medium.",
        ) {
            WindowFrame(preset = WindowPreset.Laptop, maxHeight = 300.dp) {
                MailboxSample(WindowPreset.Laptop)
            }
        }
    }
}

/** Messages the sample mailbox lists: sender and subject. */
private val Messages = listOf(
    "Northwind Travel" to "Your booking for 14 Nov is confirmed",
    "Ana Ribeiro" to "Notes from Thursday's planning call",
    "Payroll" to "October payslip is ready",
    "Studio Kaze" to "Revised quote for the kitchen",
    "City Library" to "Two books are due on Monday",
)

/**
 * A mailbox laid out the way a consumer would write it: one [AdaptiveContent] choosing between a
 * list and a list beside the open message, each pane a [WindowClassBox].
 *
 * @param preset Window the sample is drawn in, for its insets.
 */
@Composable
private fun MailboxSample(preset: WindowPreset) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(preset.windowInsets),
    ) {
        ClassReadout(
            label = "Window",
            windowClass = ArecaTheme.window,
            modifier = Modifier.padding(
                horizontal = adaptive(compact = 16.dp, medium = 24.dp, expanded = 32.dp),
                vertical = 12.dp,
            ),
        )
        AdaptiveContent(
            compact = { MessageList(selected = null) },
            medium = {
                Row(modifier = Modifier.fillMaxSize()) {
                    WindowClassBox(
                        modifier = Modifier
                            .width(adaptive(compact = 280.dp, expanded = 320.dp, large = 360.dp))
                            .fillMaxHeight(),
                    ) {
                        Column {
                            ClassReadout(label = "List pane", windowClass = ArecaTheme.window, modifier = PaneReadoutPadding, detailed = false)
                            MessageList(selected = 0)
                        }
                    }
                    VerticalDivider()
                    WindowClassBox(modifier = Modifier.weight(1f).fillMaxHeight()) {
                        Column {
                            ClassReadout(label = "Detail pane", windowClass = ArecaTheme.window, modifier = PaneReadoutPadding, detailed = false)
                            MessageDetail()
                        }
                    }
                }
            },
        )
    }
}

private val PaneReadoutPadding = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)

/**
 * One line naming a window class: what it describes, its size, and its classes as badges.
 *
 * @param label What the class describes — the window or a pane.
 * @param windowClass The class to name.
 * @param modifier Modifier applied to the line.
 * @param detailed Whether to name the height class and input too. A pane shares both with its window,
 *   and has too little width to repeat them.
 */
@Composable
private fun ClassReadout(
    label: String,
    windowClass: WindowClass,
    modifier: Modifier = Modifier,
    detailed: Boolean = true,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "$label · ${windowClass.size.width.value.toInt()}dp",
            variant = TextVariant.BodySmall,
            color = TextColor.Muted,
        )
        Badge(style = BadgeStyle.Brand) { Text(windowClass.width.name) }
        if (detailed) {
            Badge(style = BadgeStyle.Neutral) { Text("${windowClass.height.name} height") }
            Badge(style = BadgeStyle.Neutral) { Text(windowClass.input.name) }
        }
    }
}

/**
 * The message list.
 *
 * @param selected Index of the message open beside the list, or `null` when none is.
 */
@Composable
private fun MessageList(selected: Int?) {
    Column {
        Messages.forEachIndexed { index, (sender, subject) ->
            ListItem(
                title = { Text(sender) },
                subtitle = { Text(subject, maxLines = 1) },
                selected = index == selected,
                onClick = {},
            )
        }
    }
}

/** The open message. */
@Composable
private fun MessageDetail() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ArecaTheme.colors.bgCanvas)
            .padding(horizontal = adaptive(compact = 16.dp, medium = 24.dp, expanded = 32.dp), vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(Messages[0].second, variant = TextVariant.HeadingSmall)
        Text("From ${Messages[0].first}", variant = TextVariant.BodySmall, color = TextColor.Muted)
        Text(
            "Your flight to Lisbon leaves at 09:40 on Saturday 14 November. Check-in opens 24 hours " +
                "before departure, and your seat is 14C.",
            variant = TextVariant.BodyMedium,
        )
    }
}
