package dev.voir.areca.playground.foundation.topbar

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.tabs.Tabs
import dev.voir.areca.foundation.tabs.TabItem
import dev.voir.areca.foundation.tabs.TabsVariant
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.foundation.tabs.rememberTabsState
import dev.voir.areca.foundation.topbar.AvatarAction
import dev.voir.areca.foundation.topbar.BackAction
import dev.voir.areca.foundation.topbar.CancelAction
import dev.voir.areca.foundation.topbar.CloseAction
import dev.voir.areca.foundation.topbar.DoneAction
import dev.voir.areca.foundation.topbar.MenuAction
import dev.voir.areca.foundation.topbar.NotificationsAction
import dev.voir.areca.foundation.topbar.OverflowAction
import dev.voir.areca.foundation.topbar.SearchAction
import dev.voir.areca.foundation.topbar.ShareAction
import dev.voir.areca.foundation.topbar.TextAction
import dev.voir.areca.foundation.topbar.TopBar
import dev.voir.areca.foundation.topbar.TopBarProgress
import dev.voir.areca.foundation.topbar.TopBarSize
import dev.voir.areca.foundation.topbar.TopBarStyle
import dev.voir.areca.foundation.topbar.TopBarTitle
import dev.voir.areca.foundation.topbar.TopBarTitleAlignment
import dev.voir.areca.foundation.topbar.bordered
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.Flash
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.Anatomy
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.Sizes
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants

/** Showcase for [TopBar]. */
@Composable
fun TopBarShowcase() = ShowcaseScaffold(
    title = "Top bar",
    summary = "Screen header whose controls, title blocks and bottom band all take their " +
        "measurements from one place.",
    module = "foundation",
) {
    Playground {
        val title by textControl("title", default = "Order #4821")
        val subtitle by textControl("subtitle", default = "")
        val withLeading by booleanControl("leading", default = true, label = "back control")
        val trailingLabel by textControl("trailing", default = "Edit")
        val withIcon by booleanControl("icon", default = true, label = "trailing icon")
        val centered by booleanControl(
            key = "centered",
            default = false,
            description = "The title keeps the bar's centre line while it fits there.",
        )

        preview {
            TopBar(
                title = title,
                subtitle = subtitle.ifBlank { null },
                modifier = Modifier.fillMaxWidth(),
                titleAlignment = if (centered) {
                    TopBarTitleAlignment.Center
                } else {
                    TopBarTitleAlignment.Start
                },
                leading = if (withLeading) ({ BackAction(onClick = {}) }) else null,
                trailing = {
                    if (withIcon) {
                        ShareAction(onClick = {})
                    }
                    if (trailingLabel.isNotBlank()) {
                        TextAction(text = trailingLabel, onClick = {})
                    }
                },
            )
        }
    }

    Anatomy(description = "Four slots. Only the title content is required.") {
        slot(name = "leading", description = "Back, close or menu control.") {
            TopBar(
                title = "Leading only",
                modifier = Modifier.fillMaxWidth(),
                leading = { BackAction(onClick = {}) },
            )
        }
        slot(name = "content", description = "The screen title. Required.") {
            TopBar(title = "Title only", modifier = Modifier.fillMaxWidth())
        }
        slot(
            name = "trailing",
            description = "Screen-level controls. Write them one after another — the slot spaces " +
                "and aligns them itself.",
        ) {
            TopBar(
                title = "Trailing only",
                modifier = Modifier.fillMaxWidth(),
                trailing = {
                    SearchAction(onClick = {})
                    TextAction(text = "Save", onClick = {})
                },
            )
        }
        slot(
            name = "bottom",
            description = "A band inside the bar: a tab strip, a search field, a progress rule.",
        ) {
            TopBar(
                title = "Bottom band",
                modifier = Modifier.fillMaxWidth(),
                bottom = { SampleTabs() },
            )
        }
    }

    Variants(
        title = "Controls",
        description = "Every control takes the bar's glyph size, hit target and gutter distance, " +
            "so a chevron, an icon, a word and a face line up with each other and with the " +
            "content below.",
        layout = VariantLayout.Vertical,
    ) {
        variant("Back", detail = "BackAction()") {
            TopBar(
                title = "Invoice details",
                modifier = Modifier.fillMaxWidth(),
                leading = { BackAction(onClick = {}) },
            )
        }
        variant("Back with a label", detail = "BackAction(label = \"Orders\")") {
            TopBar(
                title = "Invoice details",
                modifier = Modifier.fillMaxWidth(),
                leading = { BackAction(onClick = {}, label = "Orders") },
            )
        }
        variant("Close", detail = "CloseAction()") {
            TopBar(
                title = "New expense",
                modifier = Modifier.fillMaxWidth(),
                leading = { CloseAction(onClick = {}) },
            )
        }
        variant("Menu and account", detail = "MenuAction() and AvatarAction()") {
            TopBar(
                title = "Inbox",
                modifier = Modifier.fillMaxWidth(),
                leading = { MenuAction(onClick = {}) },
                trailing = { AvatarAction(onClick = {}, letters = "GB") },
            )
        }
        variant("Badged and overflowing", detail = "NotificationsAction(badge = \"3\"), then OverflowAction()") {
            TopBar(
                title = "Project",
                modifier = Modifier.fillMaxWidth(),
                leading = { BackAction(onClick = {}) },
                trailing = {
                    NotificationsAction(onClick = {}, badge = "3")
                    OverflowAction(onClick = {})
                },
            )
        }
        variant("Semantic colors", detail = "ActionColor on a worded control") {
            TopBar(
                title = "Membership",
                modifier = Modifier.fillMaxWidth(),
                leading = {
                    TextAction(text = "Cancel", onClick = {}, color = ActionColor.Neutral)
                },
                trailing = {
                    TextAction(text = "Delete", onClick = {}, color = ActionColor.Danger)
                },
            )
        }
        variant("Working", detail = "loading = true keeps the bar's width") {
            TopBar(
                title = "Draft",
                modifier = Modifier.fillMaxWidth(),
                leading = { BackAction(onClick = {}) },
                trailing = { TextAction(text = "Publish", onClick = {}, loading = true) },
            )
        }
    }

    Variants(
        title = "Title blocks",
        description = "A heading is rarely only a name. Each block holds itself to the height of " +
            "a plain title and truncates rather than wrapping.",
        layout = VariantLayout.Vertical,
    ) {
        variant("Title and subtitle", detail = "TopBarTitle.Default") {
            TopBar(
                title = "Northwind Traders",
                subtitle = "Updated 3 minutes ago",
                modifier = Modifier.fillMaxWidth(),
                leading = { BackAction(onClick = {}) },
            )
        }
        variant("Breadcrumb", detail = "TopBarTitle.Breadcrumb") {
            TopBar(
                modifier = Modifier.fillMaxWidth(),
                leading = { BackAction(onClick = {}) },
            ) {
                TopBarTitle.Breadcrumb(
                    parent = "Q3 campaigns",
                    title = "Launch checklist",
                    onParentClick = {},
                )
            }
        }
        variant("Status", detail = "TopBarTitle.Status") {
            TopBar(
                modifier = Modifier.fillMaxWidth(),
                leading = { BackAction(onClick = {}) },
                trailing = { OverflowAction(onClick = {}) },
            ) {
                TopBarTitle.Status(title = "Design standup", status = "Recording")
            }
        }
        variant("Counter", detail = "TopBarTitle.Counter") {
            TopBar(
                modifier = Modifier.fillMaxWidth(),
                leading = { BackAction(onClick = {}) },
            ) {
                TopBarTitle.Counter(title = "Unread", count = "24")
            }
        }
        variant("Menu", detail = "TopBarTitle.Menu — a title that opens a picker") {
            var expanded by remember { mutableStateOf(false) }

            TopBar(
                modifier = Modifier.fillMaxWidth(),
                titleAlignment = TopBarTitleAlignment.Center,
                leading = { MenuAction(onClick = {}) },
                trailing = { AvatarAction(onClick = {}, letters = "GB") },
            ) {
                TopBarTitle.Menu(
                    title = "Acme Inc.",
                    expanded = expanded,
                    onClick = { expanded = !expanded },
                )
            }
        }
        variant("Branded", detail = "TopBarTitle.Branded") {
            TopBar(
                modifier = Modifier.fillMaxWidth(),
                trailing = { AvatarAction(onClick = {}, letters = "GB") },
            ) {
                TopBarTitle.Branded(title = "Areca", subtitle = "Design system") {
                    Icon(imageVector = ArecaIcons.Flash, contentDescription = null)
                }
            }
        }
    }

    Variants(
        title = "Title alignment",
        description = "Start lets the title follow the leading control; Center puts it on the " +
            "bar's centre line without narrowing the controls.",
        layout = VariantLayout.Vertical,
    ) {
        variant("Start", detail = "title follows the leading control") {
            TopBar(
                title = "Order #4821",
                modifier = Modifier.fillMaxWidth(),
                leading = { BackAction(onClick = {}) },
                trailing = { TextAction(text = "Edit", onClick = {}) },
            )
        }
        variant("Center", detail = "title on the bar's centre line") {
            TopBar(
                title = "Order #4821",
                modifier = Modifier.fillMaxWidth(),
                titleAlignment = TopBarTitleAlignment.Center,
                leading = { BackAction(onClick = {}) },
                trailing = { TextAction(text = "Edit", onClick = {}) },
            )
        }
    }

    Sizes(description = "The bar and its glyphs move together, so a compact bar is not a tall " +
        "bar's icons in a short row.") {
        variant("Compact", detail = "48dp, 20dp glyphs") {
            TopBar(
                title = "Compact",
                modifier = Modifier.fillMaxWidth(),
                size = TopBarSize.Compact,
                leading = { BackAction(onClick = {}) },
                trailing = { TextAction(text = "Edit", onClick = {}) },
            )
        }
        variant("Default", detail = "56dp, 24dp glyphs") {
            TopBar(
                title = "Default",
                modifier = Modifier.fillMaxWidth(),
                leading = { BackAction(onClick = {}) },
                trailing = { TextAction(text = "Edit", onClick = {}) },
            )
        }
        variant("Comfortable", detail = "64dp, 24dp glyphs") {
            TopBar(
                title = "Comfortable",
                modifier = Modifier.fillMaxWidth(),
                size = TopBarSize.Comfortable,
                leading = { BackAction(onClick = {}) },
                trailing = { TextAction(text = "Edit", onClick = {}) },
            )
        }
    }

    Variants(
        title = "Separation",
        description = "A bar over a plain canvas needs no rule. A bar with content under it does " +
            "— and a bar given a scroll state decides that for itself rather than being told.",
        layout = VariantLayout.Vertical,
    ) {
        variant("Default", detail = "fill only") {
            TopBar(
                title = "Inbox",
                modifier = Modifier.fillMaxWidth(),
                leading = { BackAction(onClick = {}) },
            )
        }
        variant("Bordered", detail = "TopBarStyle.Default.bordered()") {
            TopBar(
                title = "Inbox",
                modifier = Modifier.fillMaxWidth(),
                style = TopBarStyle.Default.bordered(),
                leading = { BackAction(onClick = {}) },
            )
        }
    }

    Recipes {
        recipe(
            name = "Detail screen",
            description = "Back control, subject, single action.",
        ) {
            TopBar(
                title = "Invoice details",
                modifier = Modifier.fillMaxWidth(),
                leading = { BackAction(onClick = {}) },
                trailing = { TextAction(text = "Edit", onClick = {}) },
            )
        }
        recipe(
            name = "Modal header",
            description = "Cancel and confirm on either side of a centred title, with the modal " +
                "presets so the bar draws no fill of its own.",
        ) {
            TopBar(
                title = "New expense",
                modifier = Modifier.fillMaxWidth(),
                size = TopBarSize.Modal,
                style = TopBarStyle.Modal,
                titleAlignment = TopBarTitleAlignment.Center,
                leading = { CancelAction(onClick = {}) },
                trailing = { DoneAction(onClick = {}) },
            )
        }
        recipe(
            name = "Tabbed screen",
            description = "The tab strip is the bar's bottom band, so it sits inside the bar's " +
                "fill and above its rule instead of under both.",
        ) {
            TopBar(
                title = "Library",
                modifier = Modifier.fillMaxWidth(),
                style = TopBarStyle.Default.bordered(),
                leading = { MenuAction(onClick = {}) },
                trailing = {
                    SearchAction(onClick = {})
                },
                bottom = { SampleTabs() },
            )
        }
        recipe(
            name = "Working screen",
            description = "A progress rule flush to the bar's edge, for a screen that is syncing.",
        ) {
            TopBar(
                title = "Syncing",
                subtitle = "412 of 1,280 files",
                modifier = Modifier.fillMaxWidth(),
                size = TopBarSize.Default.copy(bottomPadding = PaddingValues()),
                leading = { BackAction(onClick = {}) },
                bottom = { TopBarProgress(progress = 0.32f) },
            )
        }
        recipe(
            name = "Home screen",
            description = "A branded title with a menu and an account.",
        ) {
            TopBar(
                modifier = Modifier.fillMaxWidth(),
                leading = { MenuAction(onClick = {}, badge = "9") },
                trailing = { AvatarAction(onClick = {}, letters = "GB") },
            ) {
                TopBarTitle.Branded(title = "Areca") {
                    Icon(imageVector = ArecaIcons.Flash, contentDescription = null)
                }
            }
        }
    }

    EdgeCases {
        case(
            name = "Wide control, centred title",
            expectation = "The control keeps its full label; the title gives up the centre line " +
                "and truncates instead.",
        ) {
            TopBar(
                title = "Quarterly revenue reconciliation",
                modifier = Modifier.fillMaxWidth(),
                titleAlignment = TopBarTitleAlignment.Center,
                leading = {
                    TextAction(text = "Cancel", onClick = {}, color = ActionColor.Neutral)
                },
                trailing = { TextAction(text = "Save changes", onClick = {}) },
            )
        }
        case(
            name = "Long title",
            expectation = "Truncates rather than pushing the trailing control off the edge.",
        ) {
            TopBar(
                title = "Quarterly revenue reconciliation and audit trail",
                modifier = Modifier.fillMaxWidth(),
                leading = { BackAction(onClick = {}) },
                trailing = { TextAction(text = "Edit", onClick = {}) },
            )
        }
        case(
            name = "Title only",
            expectation = "Fills the bar, and stays exactly as tall as a bar holding controls.",
        ) {
            TopBar(title = "Settings", modifier = Modifier.fillMaxWidth())
        }
        case(
            name = "Three trailing controls",
            expectation = "Evenly spaced by the bar, with only the last one meeting the gutter. " +
                "Past three, move the rest behind an overflow control.",
        ) {
            TopBar(
                title = "Document",
                modifier = Modifier.fillMaxWidth(),
                leading = { BackAction(onClick = {}) },
                trailing = {
                    SearchAction(onClick = {})
                    ShareAction(onClick = {})
                    OverflowAction(onClick = {})
                },
            )
        }
        case(
            name = "Custom title content",
            expectation = "The slot overload takes any composable, styled by the bar.",
        ) {
            TopBar(
                modifier = Modifier.fillMaxWidth(),
                leading = { BackAction(onClick = {}) },
            ) {
                Text(text = "Custom", variant = TextVariant.LabelLarge, color = TextColor.Brand)
            }
        }
    }
}

/** A tab strip standing in for whatever a screen puts in the bar's bottom band. */
@Composable
private fun SampleTabs() {
    val state = rememberTabsState(initialValue = "all")

    Tabs(
        tabs = listOf(
            TabItem(value = "all") { Text(text = "All") },
            TabItem(value = "unread") { Text(text = "Unread") },
            TabItem(value = "flagged") { Text(text = "Flagged") },
        ),
        state = state,
        variant = TabsVariant.Underline,
    )
}
