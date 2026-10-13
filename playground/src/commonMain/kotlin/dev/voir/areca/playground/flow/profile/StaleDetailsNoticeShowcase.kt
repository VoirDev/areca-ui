package dev.voir.areca.playground.flow.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.flow.profile.part.ProfileHeaderCard
import dev.voir.areca.flow.profile.part.StaleDetailsNotice
import dev.voir.areca.flow.profile.row.EmailRow
import dev.voir.areca.flow.profile.row.ProfileInfoRow
import dev.voir.areca.foundation.settings.Section
import dev.voir.areca.foundation.settings.Settings
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.theme.ArecaTheme

/** Why the sample details could not be refreshed. */
private const val OfflineMessage: String = "You're offline. These are the details saved on this " +
    "device, last updated yesterday at 18:40."

/** Showcase for [StaleDetailsNotice]. */
@Composable
fun StaleDetailsNoticeShowcase() = ShowcaseScaffold(
    title = "Stale details notice",
    summary = "The band heading a profile that loaded from memory, saying the details below were " +
        "not just fetched.",
    module = "flow-profile",
    status = ShowcaseStatus.Beta,
) {
    Playground {
        val message by textControl("message", default = OfflineMessage)
        val title by textControl(
            key = "title",
            default = "Showing saved details",
            description = "Defaults to \"Showing saved details\"; pass the application's translation.",
        )

        preview {
            StaleDetailsNotice(
                message = message,
                modifier = Modifier.fillMaxWidth(),
                title = title,
            )
        }
    }

    States(layout = VariantLayout.Vertical) {
        variant(name = "Offline", detail = "Default title") {
            StaleDetailsNotice(message = OfflineMessage)
        }
        variant(name = "Service unavailable", detail = "Custom title") {
            StaleDetailsNotice(
                message = "We couldn't reach the server. Try again in a few minutes.",
                title = "Couldn't refresh",
            )
        }
    }

    EdgeCases {
        case(
            name = "Very long message",
            expectation = "Wraps to as many lines as it needs; the icon stays at the top.",
        ) {
            StaleDetailsNotice(
                message = "We couldn't reach the server, so these are the details saved on this " +
                    "device when you last opened your profile. Changes you made on another device " +
                    "since then will appear once the connection is back.",
            )
        }
        case(
            name = "Very long title",
            expectation = "Wraps rather than ellipsising; the title is the whole point of the band.",
        ) {
            StaleDetailsNotice(
                message = OfflineMessage,
                title = "Showing the details saved on this device while the connection is down",
            )
        }
        case(
            name = "Narrow container",
            expectation = "Constrained to 240dp: text wraps beside the icon.",
        ) {
            StaleDetailsNotice(message = OfflineMessage, modifier = Modifier.width(240.dp))
        }
    }

    Recipes {
        recipe(
            name = "Profile from memory",
            description = "Heading the page, above the card and rows it describes.",
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl),
            ) {
                StaleDetailsNotice(message = OfflineMessage)
                ProfileHeaderCard(summary = ProfileSamples.summary(), modifier = Modifier.fillMaxWidth())
                Settings(modifier = Modifier.fillMaxWidth()) {
                    Section {
                        ProfileInfoRow(value = "@ada", label = "Username")
                        EmailRow(email = "ada@analytical.engine", isVerified = true)
                    }
                }
            }
        }
    }
}
