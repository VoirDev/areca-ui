package dev.voir.areca.playground.flow.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.flow.profile.part.AvatarUploadState
import dev.voir.areca.flow.profile.part.AvatarUploadStatus
import dev.voir.areca.flow.profile.part.EditableAvatar
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import kotlin.math.roundToInt

/** The [AvatarUploadState] the playground band is in. */
private enum class UploadOption(
    /** Label of the option in the control panel. */
    val label: String,
) {
    /** [AvatarUploadState.Idle]: the band collapses. */
    Idle("Idle"),

    /** [AvatarUploadState.Uploading] with a known fraction. */
    Progress("Uploading"),

    /** [AvatarUploadState.Uploading] with no fraction: an indeterminate bar. */
    Indeterminate("Uploading, unknown"),

    /** [AvatarUploadState.Failed]. */
    Failed("Failed"),
}

/** Why the sample upload failed, as a service would word it. */
private const val FailureMessage: String = "The connection dropped before the photo finished uploading."

/** Showcase for [AvatarUploadStatus] across every [AvatarUploadState]. */
@Composable
fun AvatarUploadStatusShowcase() = ShowcaseScaffold(
    title = "Avatar upload status",
    summary = "The band under an edit screen's avatar that reports a change of picture while it " +
        "uploads, and offers to retry when it fails.",
    module = "flow-profile",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "Try again and Dismiss drive the state control, as a screen would.") {
        var state by enumControl(
            key = "state",
            default = UploadOption.Progress,
            options = UploadOption.entries,
            optionLabel = { it.label },
        )
        val progress by numberControl(
            key = "progress",
            default = 0.42f,
            range = 0f..1f,
            step = 0.1f,
            format = { "${(it * 100).roundToInt()}%" },
            description = "Used by the Uploading state.",
        )
        val message by textControl(
            key = "message",
            default = FailureMessage,
            description = "Used by the Failed state.",
        )

        preview {
            AvatarUploadStatus(
                state = when (state) {
                    UploadOption.Idle -> AvatarUploadState.Idle
                    UploadOption.Progress -> AvatarUploadState.Uploading(progress = progress)
                    UploadOption.Indeterminate -> AvatarUploadState.Uploading(progress = null)
                    UploadOption.Failed -> AvatarUploadState.Failed(message = message)
                },
                onRetry = { state = UploadOption.Progress },
                onDismiss = { state = UploadOption.Idle },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }

    States(layout = VariantLayout.Vertical) {
        variant(name = "Idle", detail = "Collapses to nothing, gap included") {
            AvatarUploadStatus(
                state = AvatarUploadState.Idle,
                onRetry = {},
                onDismiss = {},
                modifier = Modifier.fillMaxWidth(),
            )
        }
        variant(name = "Uploading(0.42f)", detail = "Bar and percentage") {
            AvatarUploadStatus(
                state = AvatarUploadState.Uploading(progress = 0.42f),
                onRetry = {},
                onDismiss = {},
                modifier = Modifier.fillMaxWidth(),
            )
        }
        variant(name = "Uploading(null)", detail = "Indeterminate bar, no percentage") {
            AvatarUploadStatus(
                state = AvatarUploadState.Uploading(progress = null),
                onRetry = {},
                onDismiss = {},
                modifier = Modifier.fillMaxWidth(),
            )
        }
        variant(name = "Failed(message)", detail = "Try again and Dismiss") {
            AvatarUploadStatus(
                state = AvatarUploadState.Failed(message = FailureMessage),
                onRetry = {},
                onDismiss = {},
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }

    EdgeCases {
        case(
            name = "Progress past the end",
            expectation = "Uploading(1.3f) reads 100% with a full bar; the fraction is clamped.",
        ) {
            AvatarUploadStatus(
                state = AvatarUploadState.Uploading(progress = 1.3f),
                onRetry = {},
                onDismiss = {},
                modifier = Modifier.fillMaxWidth(),
            )
        }
        case(
            name = "Very long message",
            expectation = "Wraps under the title; nothing is cut and the buttons stay below.",
        ) {
            AvatarUploadStatus(
                state = AvatarUploadState.Failed(
                    message = "Photos must be under 10 MB and at least 200 × 200 pixels. This one is " +
                        "24 MB, so it was not uploaded and your current photo is unchanged.",
                ),
                onRetry = {},
                onDismiss = {},
                modifier = Modifier.fillMaxWidth(),
            )
        }
        case(
            name = "Narrow container",
            expectation = "Constrained to 240dp: text wraps beside the icon and both buttons fit.",
        ) {
            AvatarUploadStatus(
                state = AvatarUploadState.Failed(message = FailureMessage),
                onRetry = {},
                onDismiss = {},
                modifier = Modifier.width(240.dp),
            )
        }
    }

    Recipes {
        recipe(
            name = "Edit screen header",
            description = "Under the avatar whose change it reports, which is busy for as long as " +
                "the upload runs.",
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                EditableAvatar(summary = ProfileSamples.summary(), onChangePhoto = {}, busy = true)
                AvatarUploadStatus(
                    state = AvatarUploadState.Uploading(progress = 0.42f),
                    onRetry = {},
                    onDismiss = {},
                )
            }
        }
    }
}
