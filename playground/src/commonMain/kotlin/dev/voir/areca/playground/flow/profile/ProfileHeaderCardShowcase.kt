package dev.voir.areca.playground.flow.profile

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import dev.voir.areca.flow.profile.ProfileSummary
import dev.voir.areca.flow.profile.part.ProfileCardAction
import dev.voir.areca.flow.profile.part.ProfileHeaderCard
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.Edit
import dev.voir.areca.icons.QrCode
import dev.voir.areca.icons.Share
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.Anatomy
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.playground.showcase.section.VariantLayout

/** How many action tiles the playground card carries. */
private enum class ActionCount(
    /** Label of the option in the control panel. */
    val label: String,
    /** Number of tiles. */
    val count: Int,
) {
    /** No actions row at all. */
    None("None", 0),

    /** One tile. */
    One("One", 1),

    /** Three tiles. */
    Three("Three", 3),
}

/** Showcase for [ProfileHeaderCard] and its [ProfileCardAction] tiles. */
@Composable
fun ProfileHeaderCardShowcase() = ShowcaseScaffold(
    title = "Profile header card",
    summary = "The card a profile screen opens with: picture, name, handle, caption and a row of " +
        "action tiles.",
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
        val caption by booleanControl("caption", default = true)
        val actions by enumControl(
            key = "actions",
            default = ActionCount.Three,
            options = ActionCount.entries,
            optionLabel = { it.label },
        )
        val actionsEnabled by booleanControl("actions enabled", default = true)

        preview {
            val sample = ProfileSamples.summary(avatar = avatar, isVerified = verified)
            ProfileHeaderCard(
                summary = sample.copy(
                    handle = if (handle) sample.handle else null,
                    caption = if (caption) sample.caption else null,
                ),
                modifier = Modifier.fillMaxWidth(),
                actions = if (actions == ActionCount.None) {
                    null
                } else {
                    {
                        SampleActions.take(actions.count).forEach { action ->
                            ProfileCardAction(
                                icon = action.icon,
                                label = action.label,
                                onClick = {},
                                enabled = actionsEnabled,
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }
                },
            )
        }
    }

    Anatomy(description = "An identity block over an optional row of action tiles.") {
        slot(
            name = "Identity",
            description = "Picture with the verified tick, name, handle or address, and caption.",
        ) {
            ProfileHeaderCard(summary = ProfileSamples.summary(), modifier = Modifier.fillMaxWidth())
        }
        slot(
            name = "actions",
            description = "ProfileCardAction tiles sharing the width equally.",
        ) {
            ProfileHeaderCard(
                summary = ProfileSamples.summary(),
                modifier = Modifier.fillMaxWidth(),
                actions = {
                    SampleActions.forEach { action ->
                        ProfileCardAction(
                            icon = action.icon,
                            label = action.label,
                            onClick = {},
                            modifier = Modifier.weight(1f),
                        )
                    }
                },
            )
        }
    }

    Variants(
        title = "What the card says under the name",
        description = "The handle when there is one, the address when there is not, nothing otherwise.",
        layout = VariantLayout.Vertical,
    ) {
        variant(name = "Handle", detail = "handle = \"@ada\"") {
            ProfileHeaderCard(summary = ProfileSamples.summary(), modifier = Modifier.fillMaxWidth())
        }
        variant(name = "Address", detail = "handle = null") {
            ProfileHeaderCard(
                summary = ProfileSamples.summary().copy(handle = null),
                modifier = Modifier.fillMaxWidth(),
            )
        }
        variant(name = "Name only", detail = "handle, email and caption null") {
            ProfileHeaderCard(
                summary = ProfileSummary(name = "Ada Lovelace"),
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }

    States(layout = VariantLayout.Vertical) {
        variant(name = "Picture") {
            ProfileHeaderCard(summary = ProfileSamples.summary(), modifier = Modifier.fillMaxWidth())
        }
        variant(name = "Picture loading", detail = "Initials under the spinner") {
            ProfileHeaderCard(
                summary = ProfileSamples.summary(avatar = SampleAvatar.Loading),
                modifier = Modifier.fillMaxWidth(),
            )
        }
        variant(name = "Picture failed", detail = "Initials, never an empty circle") {
            ProfileHeaderCard(
                summary = ProfileSamples.summary(avatar = SampleAvatar.Broken, isVerified = false),
                modifier = Modifier.fillMaxWidth(),
            )
        }
        variant(name = "Actions disabled") {
            ProfileHeaderCard(
                summary = ProfileSamples.summary(),
                modifier = Modifier.fillMaxWidth(),
                actions = {
                    SampleActions.forEach { action ->
                        ProfileCardAction(
                            icon = action.icon,
                            label = action.label,
                            onClick = {},
                            enabled = false,
                            modifier = Modifier.weight(1f),
                        )
                    }
                },
            )
        }
    }

    EdgeCases {
        case(
            name = "Very long name",
            expectation = "Wraps to two lines, then ends in an ellipsis; the picture never moves.",
        ) {
            ProfileHeaderCard(
                summary = ProfileSamples.summary().copy(
                    name = "Augusta Ada King, Countess of Lovelace and Baroness Wentworth",
                ),
                modifier = Modifier.fillMaxWidth(),
            )
        }
        case(
            name = "Narrow container",
            expectation = "Constrained to 240dp: action labels ellipsise rather than wrap.",
        ) {
            ProfileHeaderCard(
                summary = ProfileSamples.summary(),
                modifier = Modifier.width(240.dp),
                actions = {
                    SampleActions.forEach { action ->
                        ProfileCardAction(
                            icon = action.icon,
                            label = action.label,
                            onClick = {},
                            modifier = Modifier.weight(1f),
                        )
                    }
                },
            )
        }
    }
}

/**
 * One demo action tile.
 *
 * @property icon Glyph of the tile.
 * @property label Label of the tile.
 */
private class SampleAction(val icon: ImageVector, val label: String)

/** The three actions a profile of one's own offers. */
private val SampleActions: List<SampleAction> = listOf(
    SampleAction(ArecaIcons.Edit, "Edit"),
    SampleAction(ArecaIcons.Share, "Share"),
    SampleAction(ArecaIcons.QrCode, "QR code"),
)
