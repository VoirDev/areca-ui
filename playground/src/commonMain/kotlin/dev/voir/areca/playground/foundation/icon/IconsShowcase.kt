package dev.voir.areca.playground.foundation.icon

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.icon.LocalIconSize
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.icons.*
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

/** One icon with the name it is referenced by. */
data class IconItem(
    val name: String,
    val imageVector: ImageVector,
)

/** A curated group of related icons. */
data class IconGroup(
    val title: String,
    val description: String? = null,
    val icons: List<IconItem>,
)

/**
 * Showcase for the Areca icon set.
 *
 * Search covers the whole set, read from the generated [arecaIconCatalogue]. The hand-picked and
 * subject groups come first because a grouping reads better than an alphabetical dump; every icon
 * none of them lists follows under "Other icons", so the page shows the whole set.
 */
@Composable
fun IconsShowcase() = ShowcaseScaffold(
    title = "Icons",
    summary = "The whole Areca icon set, grouped by purpose and filterable by name.",
    module = "icons",
) {
    val groups = rememberArecaIconGroups()

    Playground(
        title = "Find an icon",
        description = "Filter by name, and check how the set reads at each size.",
    ) {
        val query by textControl("search", default = "", placeholder = "arrow, check, user…")
        val size by numberControl(
            key = "size",
            default = 24f,
            range = 12f..48f,
            step = 4f,
            format = { "${it.toInt()}dp" },
        )

        val matches = arecaIconCatalogue
            .filter { query.isBlank() || it.name.contains(query.trim(), ignoreCase = true) }

        preview {
            if (matches.isEmpty()) {
                Text(
                    text = "No icon matches \"$query\"",
                    variant = TextVariant.BodySmall,
                    color = TextColor.Muted,
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
                    Text(
                        text = if (matches.size > PREVIEW_LIMIT) {
                            "${matches.size} icons match; showing the first $PREVIEW_LIMIT"
                        } else {
                            "${matches.size} icons match"
                        },
                        variant = TextVariant.BodySmall,
                        color = TextColor.Muted,
                    )
                    IconGrid(icons = matches.take(PREVIEW_LIMIT), iconSize = size.toInt().dp)
                }
            }
        }
    }

    groups.forEach { group ->
        Variants(
            title = group.title,
            description = group.description,
            layout = VariantLayout.Vertical,
        ) {
            variant(name = "${group.icons.size} icons") {
                IconGrid(icons = group.icons, iconSize = 24.dp)
            }
        }
    }
}

/** Most icons the search preview renders at once. */
private const val PREVIEW_LIMIT = 60

@Composable
private fun IconGrid(icons: List<IconItem>, iconSize: Dp) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
        verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
    ) {
        icons.forEach { item ->
            IconTile(item = item, iconSize = iconSize)
        }
    }
}

@Composable
private fun IconTile(item: IconItem, iconSize: Dp) {
    Column(
        modifier = Modifier
            .width(96.dp)
            .padding(vertical = ArecaTheme.spacing.md),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.sm),
    ) {
        Box(contentAlignment = Alignment.Center) {
            CompositionLocalProvider(LocalIconSize provides iconSize) {
                Icon(imageVector = item.imageVector, contentDescription = item.name)
            }
        }
        Text(
            text = item.name,
            variant = TextVariant.BodyExtraSmall,
            color = TextColor.Muted,
            maxLines = 1,
        )
    }
}

@Composable
private fun rememberArecaIconGroups(): List<IconGroup> = remember { withOtherIcons(curatedIconGroups()) }

/**
 * Appends the icons no group lists, so the groups together cover the whole set.
 *
 * @param groups Hand-picked and subject groups.
 * @return [groups] followed by an "Other icons" group, when any icon is left over.
 */
private fun withOtherIcons(groups: List<IconGroup>): List<IconGroup> {
    val grouped = groups.flatMap { group -> group.icons.map(IconItem::name) }.toSet()
    val others = arecaIconCatalogue.filter { it.name !in grouped }
    if (others.isEmpty()) return groups
    return groups + IconGroup(
        title = "Other icons",
        description = "Every icon the groups above do not list, by name.",
        icons = others,
    )
}

private fun curatedIconGroups(): List<IconGroup> {
    // Hand-picked groups first, then the subject groups converted from Lucide and Tabler Icons.
    return listOf(
        IconGroup(
            title = "Arrows",
            description = "Navigation and direction icons (arrows, chevrons).",
            icons = listOf(
                IconItem("ArrowDown", ArecaIcons.ArrowDown),
                IconItem("ArrowDownUp", ArecaIcons.ArrowDownUp),
                IconItem("ArrowLeft", ArecaIcons.ArrowLeft),
                IconItem("ArrowLeftRight", ArecaIcons.ArrowLeftRight),
                IconItem("ArrowRight", ArecaIcons.ArrowRight),
                IconItem("ArrowUp", ArecaIcons.ArrowUp),
                IconItem("ChevronDown", ArecaIcons.ChevronDown),
                IconItem("ChevronLeft", ArecaIcons.ChevronLeft),
                IconItem("ChevronRight", ArecaIcons.ChevronRight),
                IconItem("ChevronUp", ArecaIcons.ChevronUp),
            )
        ),

        IconGroup(
            title = "Status",
            description = "Status and feedback icons (success, warning, info, forbidden).",
            icons = listOf(
                IconItem("Check", ArecaIcons.Check),
                IconItem("CheckCircle", ArecaIcons.CheckCircle),
                IconItem("CheckSquare", ArecaIcons.CheckSquare),
                IconItem("Info", ArecaIcons.Info),
                IconItem("InfoCircle", ArecaIcons.InfoCircle),
                IconItem("WarningSeal", ArecaIcons.WarningSeal),
                IconItem("Forbidden", ArecaIcons.Forbidden),
                IconItem("Cross", ArecaIcons.Cross),
                IconItem("CrossCircle", ArecaIcons.CrossCircle),
            )
        ),

        IconGroup(
            title = "Actions",
            description = "Common actions (copy, save, share, download, delete, search).",
            icons = listOf(
                IconItem("Copy", ArecaIcons.Copy),
                IconItem("Save", ArecaIcons.Save),
                IconItem("Share", ArecaIcons.Share),
                IconItem("ShareNodes", ArecaIcons.ShareNodes),
                IconItem("Download", ArecaIcons.Download),
                IconItem("Delete", ArecaIcons.Delete),
                IconItem("Search", ArecaIcons.Search),
            )
        ),

        IconGroup(
            title = "Finance",
            description = "Finance & banking domain icons.",
            icons = listOf(
                IconItem("Atm", ArecaIcons.Atm),
                IconItem("Bank", ArecaIcons.Bank),
                IconItem("Cash", ArecaIcons.Cash),
                IconItem("Coin", ArecaIcons.Coin),
                IconItem("CreditCard", ArecaIcons.CreditCard),
                IconItem("Wallet", ArecaIcons.Wallet),
                IconItem("MoneyBag", ArecaIcons.MoneyBag),
                IconItem("PiggyBank", ArecaIcons.PiggyBank),
                IconItem("OnlineBanking", ArecaIcons.OnlineBanking),
                IconItem("Exchange", ArecaIcons.Exchange),
                IconItem("Loan", ArecaIcons.Loan),
                IconItem("Insurance", ArecaIcons.Insurance),
                IconItem("Invoice", ArecaIcons.Invoice),
                IconItem("Cheque", ArecaIcons.Cheque),
                IconItem("SafetyBox", ArecaIcons.SafetyBox),
                IconItem("PigMoney", ArecaIcons.PigMoney),
                IconItem("Gold", ArecaIcons.Gold),
                IconItem("Stockmarket", ArecaIcons.Stockmarket),
                IconItem("Scales", ArecaIcons.Scales),
            )
        ),

        IconGroup(
            title = "Charts",
            description = "Charts and analytics.",
            icons = listOf(
                IconItem("ChartUp", ArecaIcons.ChartUp),
            )
        ),

        IconGroup(
            title = "Security",
            description = "User, security and privacy.",
            icons = listOf(
                IconItem("Fingerprint", ArecaIcons.Fingerprint),
                IconItem("Key", ArecaIcons.Key),
                IconItem("Lock", ArecaIcons.Lock),
                IconItem("Unlock", ArecaIcons.Unlock),
                IconItem("EyeShow", ArecaIcons.EyeShow),
                IconItem("EyeHide", ArecaIcons.EyeHide),
            )
        ),

        IconGroup(
            title = "System",
            description = "UI controls and system (settings, more, plus/minus).",
            icons = listOf(
                IconItem("Settings", ArecaIcons.Settings),
                IconItem("SettingsControl", ArecaIcons.SettingsControl),
                IconItem("MoreHorizontal", ArecaIcons.MoreHorizontal),
                IconItem("DotsVertical", ArecaIcons.DotsVertical),
                IconItem("Plus", ArecaIcons.Plus),
                IconItem("PlusCircle", ArecaIcons.PlusCircle),
                IconItem("Minus", ArecaIcons.Minus),
                IconItem("Trash", ArecaIcons.Trash),
                IconItem("TrashAlt", ArecaIcons.TrashAlt),
                IconItem("Tray", ArecaIcons.Tray),
                IconItem("Wrench", ArecaIcons.Wrench),
            )
        ),

        IconGroup(
            title = "Help",
            description = "Communication/help.",
            icons = listOf(
                IconItem("HelpBubble", ArecaIcons.HelpBubble),
                IconItem("HelpCircle", ArecaIcons.HelpCircle),
                IconItem("Link", ArecaIcons.Link),
                IconItem("Info", ArecaIcons.Info),
            )
        ),

        IconGroup(
            title = "Objects",
            description = "Places and objects.",
            icons = listOf(
                IconItem("Home", ArecaIcons.Home),
                IconItem("Calendar", ArecaIcons.Calendar),
                IconItem("Clock", ArecaIcons.Clock),
                IconItem("Camera", ArecaIcons.Camera),
                IconItem("CameraOff", ArecaIcons.CameraOff),
                IconItem("Bookmark", ArecaIcons.Bookmark),
                IconItem("Briefcase", ArecaIcons.Briefcase),
                IconItem("Calculator", ArecaIcons.Calculator),
                IconItem("Pin", ArecaIcons.Pin),
                IconItem("User", ArecaIcons.User),
                IconItem("Heart", ArecaIcons.Heart),
                IconItem("AtSign", ArecaIcons.AtSign),
                IconItem("Discount", ArecaIcons.Discount),
            )
        ),

        IconGroup(
            title = "Social",
            description = "Social providers/brands.",
            icons = listOf(
                IconItem("SocialApple", ArecaIcons.SocialApple),
                IconItem("SocialFacebook", ArecaIcons.SocialFacebook),
                IconItem("SocialGoogle", ArecaIcons.SocialGoogle),
            )
        ),
    ) + subjectIconGroups()
}
