package dev.voir.areca.playground.flow.profile

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.flow.profile.part.ProfileLoadFailure
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import kotlin.math.roundToInt

/** Why the sample profile could not be loaded. */
private const val LoadMessage: String = "Check your connection and try again."

/** Showcase for [ProfileLoadFailure]. */
@Composable
fun ProfileLoadFailureShowcase() = ShowcaseScaffold(
    title = "Profile load failure",
    summary = "The screen when the profile could not be loaded at all and there is nothing to show " +
        "in its place.",
    module = "flow-profile",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "It fills the space it is given, so the preview is given a fixed height.") {
        val message by textControl("message", default = LoadMessage)
        val title by textControl(
            key = "title",
            default = "Couldn't load your account",
            description = "Defaults to \"Couldn't load your account\"; pass the application's translation.",
        )
        val height by numberControl(
            key = "height",
            default = 320f,
            range = 240f..560f,
            step = 40f,
            format = { "${it.roundToInt()}dp" },
            description = "Height of the space the screen fills.",
        )

        preview {
            ProfileLoadFailure(
                message = message,
                onRetry = {},
                modifier = Modifier.fillMaxWidth().height(height.dp),
                title = title,
            )
        }
    }

    States(layout = VariantLayout.Vertical) {
        variant(name = "Default title", detail = "title left at its English default") {
            ProfileLoadFailure(
                message = LoadMessage,
                onRetry = {},
                modifier = Modifier.fillMaxWidth().height(320.dp),
            )
        }
        variant(name = "Custom title", detail = "The service's own wording") {
            ProfileLoadFailure(
                message = "Your account is temporarily unavailable while we carry out maintenance. " +
                    "It should be back within the hour.",
                onRetry = {},
                modifier = Modifier.fillMaxWidth().height(320.dp),
                title = "Down for maintenance",
            )
        }
    }

    EdgeCases {
        case(
            name = "Very long message",
            expectation = "Wraps, centred, with the button still below it.",
        ) {
            ProfileLoadFailure(
                message = "We couldn't reach the server to load your profile, and there is no copy " +
                    "saved on this device yet. Check that you're connected to the internet, or try " +
                    "again in a few minutes if the connection looks fine.",
                onRetry = {},
                modifier = Modifier.fillMaxWidth().height(320.dp),
            )
        }
        case(
            name = "Narrow container",
            expectation = "Constrained to 240dp: title and message wrap and stay centred.",
        ) {
            ProfileLoadFailure(
                message = LoadMessage,
                onRetry = {},
                modifier = Modifier.size(width = 240.dp, height = 320.dp),
            )
        }
        case(
            name = "Tall container",
            expectation = "Constrained to 560dp: the block stays centred vertically, not pinned to " +
                "the top.",
        ) {
            ProfileLoadFailure(
                message = LoadMessage,
                onRetry = {},
                modifier = Modifier.fillMaxWidth().height(560.dp),
            )
        }
    }
}
