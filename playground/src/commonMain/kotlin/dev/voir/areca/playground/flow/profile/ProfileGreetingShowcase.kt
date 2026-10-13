package dev.voir.areca.playground.flow.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.flow.profile.part.ProfileGreeting
import dev.voir.areca.flow.profile.part.ProfileGreetingPlaceholder
import dev.voir.areca.flow.profile.part.StaleDetailsNotice
import dev.voir.areca.foundation.topbar.NotificationsAction
import dev.voir.areca.foundation.topbar.TopBar
import dev.voir.areca.foundation.topbar.TopBarStyle
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

/** Showcase for [ProfileGreeting] and [ProfileGreetingPlaceholder]. */
@Composable
fun ProfileGreetingShowcase() = ShowcaseScaffold(
    title = "Profile greeting",
    summary = "Who is signed in, across the top of a screen that is not about the account.",
    module = "flow-profile",
    status = ShowcaseStatus.Beta,
) {
    Playground {
        val loading by booleanControl(
            key = "loading",
            default = false,
            description = "Swaps in ProfileGreetingPlaceholder at the greeting's final height.",
        )
        val avatar by enumControl(
            key = "avatar",
            default = SampleAvatar.Picture,
            options = SampleAvatar.entries,
            optionLabel = { it.label },
        )
        val name by textControl("name", default = "Ada Lovelace")
        val greeting by textControl(
            key = "greeting",
            default = "Welcome back",
            description = "Worded by the application; with none, the name stands alone.",
        )

        preview {
            if (loading) {
                ProfileGreetingPlaceholder(modifier = Modifier.fillMaxWidth())
            } else {
                ProfileGreeting(
                    summary = ProfileSamples.summary(avatar = avatar).copy(name = name),
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    greeting = greeting,
                )
            }
        }
    }

    Variants(
        title = "Greeting",
        description = "The line above the name, worded by the application for the moment.",
        layout = VariantLayout.Vertical,
    ) {
        variant(name = "Name alone", detail = "greeting = null") {
            ProfileGreeting(summary = ProfileSamples.summary(), onClick = {})
        }
        variant(name = "Time of day", detail = "greeting = \"Good morning\"") {
            ProfileGreeting(
                summary = ProfileSamples.summary(),
                onClick = {},
                greeting = "Good morning",
            )
        }
    }

    States(layout = VariantLayout.Vertical) {
        variant(name = "Loaded") {
            ProfileGreeting(summary = ProfileSamples.summary(), onClick = {})
        }
        variant(name = "Loading", detail = "ProfileGreetingPlaceholder") {
            ProfileGreetingPlaceholder(modifier = Modifier.fillMaxWidth())
        }
        variant(name = "Picture loading", detail = "Initials under the spinner") {
            ProfileGreeting(
                summary = ProfileSamples.summary(avatar = SampleAvatar.Loading),
                onClick = {},
            )
        }
        variant(name = "No picture", detail = "Initials") {
            ProfileGreeting(
                summary = ProfileSamples.summary(avatar = SampleAvatar.None),
                onClick = {},
            )
        }
    }

    EdgeCases {
        case(
            name = "Very long name",
            expectation = "One line, ending in an ellipsis.",
        ) {
            ProfileGreeting(
                summary = ProfileSamples.summary().copy(
                    name = "Augusta Ada King, Countess of Lovelace and Baroness Wentworth",
                ),
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
            )
        }
        case(
            name = "Very long greeting",
            expectation = "One line, ending in an ellipsis; the name stays under it.",
        ) {
            ProfileGreeting(
                summary = ProfileSamples.summary(),
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                greeting = "Good evening — three new replies since you last looked",
            )
        }
        case(
            name = "Narrow container",
            expectation = "Constrained to 240dp: both lines ellipsise, the picture keeps its size.",
        ) {
            ProfileGreeting(
                summary = ProfileSamples.summary().copy(name = "Augusta Ada King"),
                onClick = {},
                modifier = Modifier.width(240.dp),
            )
        }
    }

    Recipes {
        recipe(
            name = "Home screen header",
            description = "The greeting as a tonal top bar's title, beside the notifications " +
                "control, with no card around it.",
        ) {
            TopBar(
                modifier = Modifier.fillMaxWidth(),
                style = TopBarStyle.Tonal,
                trailing = { NotificationsAction(onClick = {}, badge = "3") },
            ) {
                ProfileGreeting(
                    summary = ProfileSamples.summary(),
                    onClick = {},
                    greeting = "Good morning",
                )
            }
        }
        recipe(
            name = "Home screen from memory",
            description = "The greeting above the notice that says the details are not fresh.",
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl),
            ) {
                ProfileGreeting(summary = ProfileSamples.summary(), onClick = {})
                StaleDetailsNotice(
                    message = "You're offline. These are the details saved on this device.",
                )
            }
        }
    }
}
