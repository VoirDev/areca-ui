package dev.voir.areca.playground.flow.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.flow.profile.ProfileSummary
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
import dev.voir.areca.playground.showcase.section.Variants

/** What the playground block draws under the picture. */
private enum class IdentityLine(
    /** Label of the option in the control panel. */
    val label: String,
) {
    /** The handle, which wins over the address. */
    Handle("handle"),

    /** No handle, so the address. */
    Address("address"),

    /** Neither, so nothing under the picture. */
    None("none"),
}

/** Showcase for [EditableAvatar]. */
@Composable
fun EditableAvatarShowcase() = ShowcaseScaffold(
    title = "Editable avatar",
    summary = "The picture at the top of an edit screen, with the camera mark that says it can be " +
        "changed and the handle under it.",
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
        val line by enumControl(
            key = "identity line",
            default = IdentityLine.Handle,
            options = IdentityLine.entries,
            optionLabel = { it.label },
        )
        val busy by booleanControl(
            key = "busy",
            default = false,
            description = "A change is in flight: spinner, grey badge, no taps.",
        )

        preview {
            val sample = ProfileSamples.summary(avatar = avatar)
            EditableAvatar(
                summary = sample.copy(
                    handle = if (line == IdentityLine.Handle) sample.handle else null,
                    email = if (line == IdentityLine.None) null else sample.email,
                ),
                onChangePhoto = {},
                busy = busy,
            )
        }
    }

    Variants(
        title = "What sits under the picture",
        description = "The handle when there is one, the address when there is not, nothing otherwise.",
        layout = VariantLayout.Horizontal,
    ) {
        variant(name = "Handle", detail = "handle = \"@ada\"") {
            EditableAvatar(summary = ProfileSamples.summary(), onChangePhoto = {})
        }
        variant(name = "Address", detail = "handle = null") {
            EditableAvatar(
                summary = ProfileSamples.summary().copy(handle = null),
                onChangePhoto = {},
            )
        }
        variant(name = "Name only", detail = "handle and email null") {
            EditableAvatar(summary = ProfileSummary(name = "Ada Lovelace"), onChangePhoto = {})
        }
    }

    States(layout = VariantLayout.Horizontal) {
        variant(name = "Picture") {
            EditableAvatar(summary = ProfileSamples.summary(), onChangePhoto = {})
        }
        variant(name = "Picture loading", detail = "Badge stays live") {
            EditableAvatar(
                summary = ProfileSamples.summary(avatar = SampleAvatar.Loading),
                onChangePhoto = {},
            )
        }
        variant(name = "No picture", detail = "Initials") {
            EditableAvatar(
                summary = ProfileSamples.summary(avatar = SampleAvatar.None),
                onChangePhoto = {},
            )
        }
        variant(name = "Busy", detail = "busy = true, taps ignored") {
            EditableAvatar(summary = ProfileSamples.summary(), onChangePhoto = {}, busy = true)
        }
    }

    EdgeCases {
        case(
            name = "Very long handle",
            expectation = "One line under the picture, ending in an ellipsis.",
        ) {
            EditableAvatar(
                summary = ProfileSamples.summary().copy(
                    handle = "@augusta_ada_king_countess_of_lovelace_and_baroness_wentworth",
                ),
                onChangePhoto = {},
            )
        }
        case(
            name = "Narrow container",
            expectation = "Constrained to 240dp: the picture stays centred and the address " +
                "ellipsises.",
        ) {
            EditableAvatar(
                summary = ProfileSamples.summary().copy(
                    handle = null,
                    email = "augusta.ada.king@analytical-engine.example.org",
                ),
                onChangePhoto = {},
                modifier = Modifier.width(240.dp),
            )
        }
    }

    Recipes {
        recipe(
            name = "Edit screen header, mid-upload",
            description = "The avatar over the upload band that reports the change it started.",
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
        recipe(
            name = "Edit screen header, upload failed",
            description = "The old picture is back and the band offers to try again.",
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                EditableAvatar(summary = ProfileSamples.summary(), onChangePhoto = {})
                AvatarUploadStatus(
                    state = AvatarUploadState.Failed(
                        message = "The connection dropped before the photo finished uploading.",
                    ),
                    onRetry = {},
                    onDismiss = {},
                )
            }
        }
    }
}
