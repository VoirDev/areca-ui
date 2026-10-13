package dev.voir.areca.playground.flow.profile

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.flow.profile.ProfileSummary
import dev.voir.areca.flow.profile.part.ProfileCoverCard
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.Anatomy
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants

/** Which controls the playground card puts at the end of the picture's row. */
private enum class CoverActions(
    /** Label of the option in the control panel. */
    val label: String,
) {
    /** No actions. */
    None("None"),

    /** "Edit profile", for a profile of one's own. */
    Own("Edit profile"),

    /** "Follow" and "Message", for somebody else's profile. */
    Other("Follow + Message"),
}

/** Showcase for [ProfileCoverCard]. */
@Composable
fun ProfileCoverCardShowcase() = ShowcaseScaffold(
    title = "Profile cover card",
    summary = "The card for a profile with more to say than a name: a cover band, the picture " +
        "overlapping it, then name, handle, bio and caption.",
    module = "flow-profile",
    status = ShowcaseStatus.Beta,
) {
    Playground {
        val avatar by enumControl(
            key = "avatar",
            default = SampleAvatar.Picture,
            options = SampleAvatar.entries,
            optionLabel = { it.label },
        )
        val verified by booleanControl("email verified", default = true)
        val handle by booleanControl(
            key = "handle",
            default = true,
            description = "Without a handle the card falls back to the address.",
        )
        val bio by booleanControl("bio", default = true)
        val caption by booleanControl("caption", default = true)
        val cover by booleanControl(
            key = "cover",
            default = true,
            description = "Off draws the default quiet neutral band.",
        )
        val actions by enumControl(
            key = "actions",
            default = CoverActions.Own,
            options = CoverActions.entries,
            optionLabel = { it.label },
        )
        val actionsEnabled by booleanControl("actions enabled", default = true)

        preview {
            val sample = ProfileSamples.summary(avatar = avatar, isVerified = verified)
            ProfileCoverCard(
                summary = sample.copy(
                    handle = if (handle) sample.handle else null,
                    caption = if (caption) sample.caption else null,
                ),
                modifier = Modifier.fillMaxWidth(),
                bio = if (bio) ProfileSamples.Bio else null,
                cover = if (cover) {
                    { ProfileSamples.Cover() }
                } else {
                    null
                },
                actions = when (actions) {
                    CoverActions.None -> null
                    CoverActions.Own -> {
                        {
                            Button(
                                text = "Edit profile",
                                onClick = {},
                                style = ButtonStyle.soft(ActionColor.Neutral),
                                size = ButtonSize.Small,
                                enabled = actionsEnabled,
                            )
                        }
                    }

                    CoverActions.Other -> {
                        {
                            Button(
                                text = "Message",
                                onClick = {},
                                style = ButtonStyle.soft(ActionColor.Neutral),
                                size = ButtonSize.Small,
                                enabled = actionsEnabled,
                            )
                            Button(
                                text = "Follow",
                                onClick = {},
                                style = ButtonStyle.solid(ActionColor.Brand),
                                size = ButtonSize.Small,
                                enabled = actionsEnabled,
                            )
                        }
                    }
                },
            )
        }
    }

    Anatomy(description = "A cover band, the picture straddling its edge, and text under both.") {
        slot(
            name = "cover",
            description = "Artwork cropped to the band — an image, a gradient. Optional; null draws " +
                "a quiet neutral band.",
        ) {
            ProfileCoverCard(
                summary = ProfileSamples.summary(),
                modifier = Modifier.fillMaxWidth(),
                cover = { ProfileSamples.Cover() },
            )
        }
        slot(
            name = "actions",
            description = "Small buttons at the end of the picture's row, just under the band.",
        ) {
            ProfileCoverCard(
                summary = ProfileSamples.summary(),
                modifier = Modifier.fillMaxWidth(),
                actions = {
                    Button(
                        text = "Edit profile",
                        onClick = {},
                        style = ButtonStyle.soft(ActionColor.Neutral),
                        size = ButtonSize.Small,
                    )
                },
            )
        }
        slot(
            name = "bio",
            description = "A few words the person wrote. Wraps; nothing is cut.",
        ) {
            ProfileCoverCard(
                summary = ProfileSamples.summary(),
                modifier = Modifier.fillMaxWidth(),
                bio = ProfileSamples.Bio,
            )
        }
    }

    Variants(
        title = "Cover",
        description = "Artwork of the product's choosing, or the default neutral band.",
        layout = VariantLayout.Vertical,
    ) {
        variant(name = "Artwork", detail = "cover = { … }") {
            ProfileCoverCard(
                summary = ProfileSamples.summary(),
                modifier = Modifier.fillMaxWidth(),
                bio = ProfileSamples.Bio,
                cover = { ProfileSamples.Cover() },
            )
        }
        variant(name = "Default band", detail = "cover = null") {
            ProfileCoverCard(
                summary = ProfileSamples.summary(),
                modifier = Modifier.fillMaxWidth(),
                bio = ProfileSamples.Bio,
            )
        }
    }

    States(layout = VariantLayout.Vertical) {
        variant(name = "Picture") {
            ProfileCoverCard(
                summary = ProfileSamples.summary(),
                modifier = Modifier.fillMaxWidth(),
                cover = { ProfileSamples.Cover() },
            )
        }
        variant(name = "Picture loading", detail = "Initials under the spinner") {
            ProfileCoverCard(
                summary = ProfileSamples.summary(avatar = SampleAvatar.Loading),
                modifier = Modifier.fillMaxWidth(),
                cover = { ProfileSamples.Cover() },
            )
        }
        variant(name = "Picture failed, address unverified", detail = "Initials, no tick") {
            ProfileCoverCard(
                summary = ProfileSamples.summary(avatar = SampleAvatar.Broken, isVerified = false),
                modifier = Modifier.fillMaxWidth(),
                cover = { ProfileSamples.Cover() },
            )
        }
        variant(name = "Actions disabled") {
            ProfileCoverCard(
                summary = ProfileSamples.summary(),
                modifier = Modifier.fillMaxWidth(),
                cover = { ProfileSamples.Cover() },
                actions = {
                    Button(
                        text = "Edit profile",
                        onClick = {},
                        style = ButtonStyle.soft(ActionColor.Neutral),
                        size = ButtonSize.Small,
                        enabled = false,
                    )
                },
            )
        }
    }

    EdgeCases {
        case(
            name = "Very long name",
            expectation = "Wraps to two lines, then ends in an ellipsis; the picture never moves.",
        ) {
            ProfileCoverCard(
                summary = ProfileSamples.summary().copy(
                    name = "Augusta Ada King, Countess of Lovelace and Baroness Wentworth",
                ),
                modifier = Modifier.fillMaxWidth(),
                cover = { ProfileSamples.Cover() },
            )
        }
        case(
            name = "Long bio",
            expectation = "Wraps to as many lines as it needs; nothing is cut.",
        ) {
            ProfileCoverCard(
                summary = ProfileSamples.summary(),
                modifier = Modifier.fillMaxWidth(),
                bio = ProfileSamples.Bio + " Corresponding with Mr Babbage about the Analytical " +
                    "Engine, translating Menabrea, and adding rather longer notes than the paper " +
                    "they are attached to.",
            )
        }
        case(
            name = "Name only",
            expectation = "No handle, address, bio or caption: the name alone under the picture.",
        ) {
            ProfileCoverCard(
                summary = ProfileSummary(name = "Ada Lovelace"),
                modifier = Modifier.fillMaxWidth(),
            )
        }
        case(
            name = "Narrow container",
            expectation = "Constrained to 240dp: the action stays on the picture's row, under the " +
                "band, and the bio wraps.",
        ) {
            ProfileCoverCard(
                summary = ProfileSamples.summary(),
                modifier = Modifier.width(240.dp),
                bio = ProfileSamples.Bio,
                cover = { ProfileSamples.Cover() },
                actions = {
                    Button(
                        text = "Edit profile",
                        onClick = {},
                        style = ButtonStyle.soft(ActionColor.Neutral),
                        size = ButtonSize.Small,
                    )
                },
            )
        }
    }

    Recipes {
        recipe(
            name = "Your own profile",
            description = "\"Edit profile\" where the person expects it, beside their own picture.",
        ) {
            ProfileCoverCard(
                summary = ProfileSamples.summary(),
                modifier = Modifier.fillMaxWidth(),
                bio = ProfileSamples.Bio,
                cover = { ProfileSamples.Cover() },
                actions = {
                    Button(
                        text = "Edit profile",
                        onClick = {},
                        style = ButtonStyle.soft(ActionColor.Neutral),
                        size = ButtonSize.Small,
                    )
                },
            )
        }
        recipe(
            name = "Somebody else's profile",
            description = "A quiet \"Message\" beside the one brand action, \"Follow\".",
        ) {
            ProfileCoverCard(
                summary = ProfileSummary(
                    name = "Charles Babbage",
                    handle = "@babbage",
                    avatar = SampleAvatar.Picture,
                    caption = "Member since January 2023",
                ),
                modifier = Modifier.fillMaxWidth(),
                bio = "Building a difference engine, one column at a time. Marylebone.",
                cover = { ProfileSamples.Cover() },
                actions = {
                    Button(
                        text = "Message",
                        onClick = {},
                        style = ButtonStyle.soft(ActionColor.Neutral),
                        size = ButtonSize.Small,
                    )
                    Button(
                        text = "Follow",
                        onClick = {},
                        style = ButtonStyle.solid(ActionColor.Brand),
                        size = ButtonSize.Small,
                    )
                },
            )
        }
    }
}
