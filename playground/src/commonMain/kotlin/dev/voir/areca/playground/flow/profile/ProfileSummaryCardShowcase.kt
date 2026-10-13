package dev.voir.areca.playground.flow.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.flow.profile.ProfileSummary
import dev.voir.areca.flow.profile.part.ProfileSummaryCaption
import dev.voir.areca.flow.profile.part.ProfileSummaryCard
import dev.voir.areca.flow.profile.part.ProfileSummaryCardAction
import dev.voir.areca.flow.profile.part.ProfileSummaryCardPlaceholder
import dev.voir.areca.flow.profile.row.AccountRow
import dev.voir.areca.flow.profile.row.AddAccountRow
import dev.voir.areca.flow.profile.row.SignOutRow
import dev.voir.areca.foundation.list.ListItemChevron
import dev.voir.areca.foundation.settings.Section
import dev.voir.areca.foundation.settings.Settings
import dev.voir.areca.foundation.settings.SettingsRowSkeleton
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.Anatomy
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

/** What the playground card draws under the name. */
private enum class CaptionOption(
    /** Label of the option in the control panel. */
    val label: String,
) {
    /** The default: the summary's caption. */
    Caption("caption"),

    /** The default with no caption: the handle. */
    Handle("handle"),

    /** The default with no caption or handle: the address. */
    Address("address"),

    /** A [ProfileSummaryCaption] of the application's own, in the muted default colour. */
    Custom("custom"),

    /** A [ProfileSummaryCaption] reporting something to act on, in the danger text colour. */
    Attention("attention"),
}

/** Showcase for [ProfileSummaryCard], [ProfileSummaryCardPlaceholder] and [ProfileSummaryCaption]. */
@Composable
fun ProfileSummaryCardShowcase() = ShowcaseScaffold(
    title = "Profile summary card",
    summary = "The compact card that leads an account menu or a settings screen into the account.",
    module = "flow-profile",
    status = ShowcaseStatus.Beta,
) {
    Playground {
        val loading by booleanControl(
            key = "loading",
            default = false,
            description = "Swaps in ProfileSummaryCardPlaceholder at the card's final height.",
        )
        val avatar by enumControl(
            key = "avatar",
            default = SampleAvatar.Picture,
            options = SampleAvatar.entries,
            optionLabel = { it.label },
        )
        val name by textControl("name", default = "Ada Lovelace")
        val caption by enumControl(
            key = "caption",
            default = CaptionOption.Caption,
            options = CaptionOption.entries,
            optionLabel = { it.label },
        )
        val customCaption by textControl(
            key = "custom caption",
            default = "Synced 4 min ago",
            description = "Used by the custom and attention captions.",
        )

        preview {
            if (loading) {
                ProfileSummaryCardPlaceholder(modifier = Modifier.fillMaxWidth())
            } else {
                val sample = ProfileSamples.summary(avatar = avatar).copy(name = name)
                val summary = when (caption) {
                    CaptionOption.Handle -> sample.copy(caption = null)
                    CaptionOption.Address -> sample.copy(caption = null, handle = null)
                    else -> sample
                }
                when (caption) {
                    CaptionOption.Custom -> ProfileSummaryCard(
                        summary = summary,
                        onClick = {},
                        trailing = { ProfileSummaryCardAction() },
                        modifier = Modifier.fillMaxWidth(),
                        caption = { ProfileSummaryCaption(text = customCaption) },
                    )

                    CaptionOption.Attention -> ProfileSummaryCard(
                        summary = summary,
                        onClick = {},
                        trailing = { ProfileSummaryCardAction() },
                        modifier = Modifier.fillMaxWidth(),
                        caption = {
                            ProfileSummaryCaption(
                                text = customCaption,
                                color = ArecaTheme.colors.fgDangerText,
                            )
                        },
                    )

                    else -> ProfileSummaryCard(
                        summary = summary,
                        onClick = {},
                        trailing = { ProfileSummaryCardAction() },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }

    Anatomy(description = "A picture and two lines, with \"Edit\" and a chevron at the end.") {
        slot(
            name = "caption",
            description = "The quiet line under the name. Defaults to the summary's caption, else " +
                "its handle, else its address; pass ProfileSummaryCaption to replace it.",
        ) {
            ProfileSummaryCard(
                summary = ProfileSamples.summary(),
                onClick = {},
                trailing = { ProfileSummaryCardAction() },
                modifier = Modifier.fillMaxWidth(),
                caption = { ProfileSummaryCaption(text = "Synced 4 min ago") },
            )
        }
    }

    Variants(
        title = "What the caption says",
        description = "The summary's own lines in order of preference, then a caption of the " +
            "application's own.",
        layout = VariantLayout.Vertical,
    ) {
        variant(name = "Caption", detail = "summary.caption") {
            ProfileSummaryCard(
                summary = ProfileSamples.summary(),
                onClick = {},
                trailing = { ProfileSummaryCardAction() },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        variant(name = "Handle", detail = "caption = null") {
            ProfileSummaryCard(
                summary = ProfileSamples.summary().copy(caption = null),
                onClick = {},
                trailing = { ProfileSummaryCardAction() },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        variant(name = "Address", detail = "caption and handle null") {
            ProfileSummaryCard(
                summary = ProfileSamples.summary().copy(caption = null, handle = null),
                onClick = {},
                trailing = { ProfileSummaryCardAction() },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        variant(name = "ProfileSummaryCaption", detail = "caption = { … }") {
            ProfileSummaryCard(
                summary = ProfileSamples.summary(),
                onClick = {},
                trailing = { ProfileSummaryCardAction() },
                modifier = Modifier.fillMaxWidth(),
                caption = { ProfileSummaryCaption(text = "Synced 4 min ago") },
            )
        }
        variant(name = "ProfileSummaryCaption / fgDangerText", detail = "Something to act on") {
            ProfileSummaryCard(
                summary = ProfileSamples.summary(),
                onClick = {},
                trailing = { ProfileSummaryCardAction() },
                modifier = Modifier.fillMaxWidth(),
                caption = {
                    ProfileSummaryCaption(
                        text = "Payment failed — update your card",
                        color = ArecaTheme.colors.fgDangerText,
                    )
                },
            )
        }
    }

    States(layout = VariantLayout.Vertical) {
        variant(name = "Loaded") {
            ProfileSummaryCard(
                summary = ProfileSamples.summary(),
                onClick = {},
                trailing = { ProfileSummaryCardAction() },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        variant(name = "Loading", detail = "ProfileSummaryCardPlaceholder") {
            ProfileSummaryCardPlaceholder(modifier = Modifier.fillMaxWidth())
        }
        variant(name = "Picture loading", detail = "Initials under the spinner") {
            ProfileSummaryCard(
                summary = ProfileSamples.summary(avatar = SampleAvatar.Loading),
                onClick = {},
                trailing = { ProfileSummaryCardAction() },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        variant(name = "Picture failed", detail = "Initials") {
            ProfileSummaryCard(
                summary = ProfileSamples.summary(avatar = SampleAvatar.Broken),
                onClick = {},
                trailing = { ProfileSummaryCardAction() },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }

    EdgeCases {
        case(
            name = "Very long name",
            expectation = "One line, ending in an ellipsis; \"Edit\" and the chevron keep their place.",
        ) {
            ProfileSummaryCard(
                summary = ProfileSamples.summary().copy(
                    name = "Augusta Ada King, Countess of Lovelace and Baroness Wentworth",
                ),
                onClick = {},
                trailing = { ProfileSummaryCardAction() },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        case(
            name = "Very long caption",
            expectation = "One line, ending in an ellipsis.",
        ) {
            ProfileSummaryCard(
                summary = ProfileSamples.summary(),
                onClick = {},
                trailing = { ProfileSummaryCardAction() },
                modifier = Modifier.fillMaxWidth(),
                caption = {
                    ProfileSummaryCaption(
                        text = "Synced 4 min ago on Pixel 9 Pro, MacBook Pro and Firefox on Windows",
                    )
                },
            )
        }
        case(
            name = "Name only",
            expectation = "No caption, handle or address: the name alone beside the picture.",
        ) {
            ProfileSummaryCard(
                summary = ProfileSummary(name = "Ada Lovelace"),
                onClick = {},
                trailing = { ProfileSummaryCardAction() },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        case(
            name = "Narrow container",
            expectation = "Constrained to 240dp: name and caption ellipsise, the end stays whole.",
        ) {
            ProfileSummaryCard(
                summary = ProfileSamples.summary(),
                onClick = {},
                trailing = { ProfileSummaryCardAction() },
                modifier = Modifier.width(240.dp),
            )
        }
        case(
            name = "Placeholder in a narrow container",
            expectation = "Constrained to 240dp: the bars keep their proportions.",
        ) {
            ProfileSummaryCardPlaceholder(modifier = Modifier.width(240.dp))
        }
    }

    Recipes {
        recipe(
            name = "Account menu",
            description = "The card leading the list of accounts and the way out, as one surface.",
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl),
            ) {
                ProfileSummaryCard(
                    summary = ProfileSamples.summary(),
                    onClick = {},
                    trailing = { ProfileSummaryCardAction() },
                    modifier = Modifier.fillMaxWidth(),
                )
                Settings(modifier = Modifier.fillMaxWidth()) {
                    Section {
                        ProfileSamples.OtherAccounts.forEach { account ->
                            AccountRow(
                                account = account,
                                onClick = {},
                                trailing = { ListItemChevron() },
                            )
                        }
                        AddAccountRow(onClick = {})
                    }
                    Section {
                        SignOutRow(onClick = {})
                    }
                }
            }
        }
        recipe(
            name = "Account menu, loading",
            description = "The placeholder over row skeletons, each at its final height, so nothing " +
                "jumps when the account arrives.",
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl),
            ) {
                ProfileSummaryCardPlaceholder(modifier = Modifier.fillMaxWidth())
                Settings(modifier = Modifier.fillMaxWidth()) {
                    Section {
                        SettingsRowSkeleton(showLeading = true, showSubtitle = true)
                        SettingsRowSkeleton(showLeading = true, showSubtitle = true)
                    }
                }
            }
        }
    }
}
