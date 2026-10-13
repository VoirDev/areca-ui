package dev.voir.areca.playground.flow.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.flow.profile.part.EditableAvatar
import dev.voir.areca.flow.profile.sheet.AvatarSourceOption
import dev.voir.areca.flow.profile.sheet.AvatarSourceOptions
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.FaceSmile
import dev.voir.areca.flow.profile.sheet.AvatarSourceSheet
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import kotlinx.coroutines.delay

/** Showcase for [AvatarSourceSheet] and the [AvatarSourceOption]s it offers. */
@Composable
fun AvatarSourceSheetShowcase() = ShowcaseScaffold(
    title = "Avatar source sheet",
    summary = "The sheet that asks where a new profile picture comes from, or removes the current one.",
    module = "flow-profile",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "Pick which choices the sheet offers, open it, and choose one.") {
        val camera by booleanControl(
            key = "Camera",
            default = true,
            description = "Offer it only where the platform can take a photograph.",
        )
        val library by booleanControl("Library", default = true)
        val remove by booleanControl(
            key = "Remove",
            default = true,
            description = "Offer it only when there is a picture to remove.",
        )
        val custom by booleanControl(
            key = "Custom choice",
            default = false,
            description = "An option of the product's own, beside the built-in ones.",
        )
        val title by textControl("title", default = "Change photo")
        val subtitle by booleanControl("subtitle", default = true)

        preview {
            var visible by remember { mutableStateOf(false) }
            var chosen by remember { mutableStateOf("Nothing chosen yet") }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    text = "Open sheet",
                    onClick = { visible = true },
                    style = ButtonStyle.soft(ActionColor.Neutral),
                    size = ButtonSize.Small,
                )

                Text(text = chosen, variant = TextVariant.BodySmall, color = TextColor.Muted)
            }

            AvatarSourceSheet(
                visible = visible,
                options = buildList {
                    if (camera) add(AvatarSourceOptions.camera { chosen = "Chose the camera"; visible = false })
                    if (library) add(AvatarSourceOptions.library { chosen = "Chose the library"; visible = false })
                    if (custom) {
                        add(
                            AvatarSourceOption(
                                label = "Choose from avatar set",
                                icon = ArecaIcons.FaceSmile,
                                onClick = { chosen = "Chose the avatar set"; visible = false },
                            ),
                        )
                    }
                    if (remove) add(AvatarSourceOptions.remove { chosen = "Chose to remove"; visible = false })
                },
                onDismiss = {
                    chosen = "Dismissed without a choice"
                    visible = false
                },
                title = title,
                subtitle = if (subtitle) "Choose where your new photo comes from." else null,
            )
        }
    }

    Variants(
        title = "Options",
        description = "Only what the product can complete, in the order given — built-in or its own.",
        layout = VariantLayout.Grid,
    ) {
        variant(name = "Camera / Library / Remove", detail = "A phone, with a picture set") {
            var visible by remember { mutableStateOf(false) }

            Button(
                text = "Open sheet",
                onClick = { visible = true },
                style = ButtonStyle.soft(ActionColor.Neutral),
                size = ButtonSize.Small,
            )

            AvatarSourceSheet(
                visible = visible,
                options = listOf(
                    AvatarSourceOptions.camera { visible = false },
                    AvatarSourceOptions.library { visible = false },
                    AvatarSourceOptions.remove { visible = false },
                ),
                onDismiss = { visible = false },
            )
        }
        variant(name = "Library / Remove", detail = "A desktop, with no camera to open") {
            var visible by remember { mutableStateOf(false) }

            Button(
                text = "Open sheet",
                onClick = { visible = true },
                style = ButtonStyle.soft(ActionColor.Neutral),
                size = ButtonSize.Small,
            )

            AvatarSourceSheet(
                visible = visible,
                options = listOf(
                    AvatarSourceOptions.library { visible = false },
                    AvatarSourceOptions.remove { visible = false },
                ),
                onDismiss = { visible = false },
            )
        }
        variant(name = "Custom choice", detail = "A product's own option beside the built-in ones") {
            var visible by remember { mutableStateOf(false) }

            Button(
                text = "Open sheet",
                onClick = { visible = true },
                style = ButtonStyle.soft(ActionColor.Neutral),
                size = ButtonSize.Small,
            )

            AvatarSourceSheet(
                visible = visible,
                options = listOf(
                    AvatarSourceOptions.library { visible = false },
                    AvatarSourceOption(
                        label = "Choose from avatar set",
                        icon = ArecaIcons.FaceSmile,
                        onClick = { visible = false },
                    ),
                ),
                onDismiss = { visible = false },
                title = "Choose your avatar",
                subtitle = null,
            )
        }
        variant(name = "Library", detail = "A desktop, with no picture yet") {
            var visible by remember { mutableStateOf(false) }

            Button(
                text = "Open sheet",
                onClick = { visible = true },
                style = ButtonStyle.soft(ActionColor.Neutral),
                size = ButtonSize.Small,
            )

            AvatarSourceSheet(
                visible = visible,
                options = listOf(
                    AvatarSourceOptions.library { visible = false },
                ),
                onDismiss = { visible = false },
            )
        }
    }

    States(
        description = "Whether the account has a picture decides whether removing it is offered.",
    ) {
        variant(name = "Picture set", detail = "Remove offered, in the danger colour") {
            var visible by remember { mutableStateOf(false) }

            Button(
                text = "Open sheet",
                onClick = { visible = true },
                style = ButtonStyle.soft(ActionColor.Neutral),
                size = ButtonSize.Small,
            )

            AvatarSourceSheet(
                visible = visible,
                options = listOf(
                    AvatarSourceOptions.camera { visible = false },
                    AvatarSourceOptions.library { visible = false },
                    AvatarSourceOptions.remove { visible = false },
                ),
                onDismiss = { visible = false },
            )
        }
        variant(name = "No picture", detail = "Remove left out") {
            var visible by remember { mutableStateOf(false) }

            Button(
                text = "Open sheet",
                onClick = { visible = true },
                style = ButtonStyle.soft(ActionColor.Neutral),
                size = ButtonSize.Small,
            )

            AvatarSourceSheet(
                visible = visible,
                options = listOf(
                    AvatarSourceOptions.camera { visible = false },
                    AvatarSourceOptions.library { visible = false },
                ),
                onDismiss = { visible = false },
            )
        }
    }

    Recipes {
        recipe(
            name = "Changing the profile picture",
            description = "The editable avatar opens the sheet; a new picture uploads, Remove leaves initials.",
        ) {
            var avatar by remember { mutableStateOf(SampleAvatar.Picture) }
            var visible by remember { mutableStateOf(false) }
            var uploading by remember { mutableStateOf(false) }

            LaunchedEffect(uploading) {
                if (uploading) {
                    delay(UploadDelayMillis)
                    avatar = SampleAvatar.Picture
                    uploading = false
                }
            }

            EditableAvatar(
                summary = ProfileSamples.summary(avatar = avatar),
                onChangePhoto = { visible = true },
                busy = uploading,
            )

            AvatarSourceSheet(
                visible = visible,
                options = buildList {
                    add(AvatarSourceOptions.camera { visible = false; uploading = true })
                    add(AvatarSourceOptions.library { visible = false; uploading = true })
                    if (avatar != SampleAvatar.None) {
                        add(AvatarSourceOptions.remove { visible = false; avatar = SampleAvatar.None })
                    }
                },
                onDismiss = { visible = false },
            )
        }
    }

    EdgeCases {
        case(
            name = "Long translated labels",
            expectation = "Choice labels and the subtitle wrap; the title keeps to one line beside " +
                "the close control.",
        ) {
            var visible by remember { mutableStateOf(false) }

            Button(
                text = "Open sheet",
                onClick = { visible = true },
                style = ButtonStyle.soft(ActionColor.Neutral),
                size = ButtonSize.Small,
            )

            AvatarSourceSheet(
                title = "Change the photo people see next to your name",
                subtitle = "Choose where your new photo comes from. It is shown on " +
                    "your profile, in conversations and on every device you are signed in on.",
                visible = visible,
                options = listOf(
                    AvatarSourceOptions.camera(label = "Take a new photo with this device's camera") {
                        visible = false
                    },
                    AvatarSourceOptions.library(label = "Choose a photo from your library or a connected drive") {
                        visible = false
                    },
                    AvatarSourceOptions.remove(label = "Remove the current photo and show your initials instead") {
                        visible = false
                    },
                ),
                onDismiss = { visible = false },
            )
        }
        case(
            name = "Nothing to offer",
            expectation = "Title and close only, with no empty block under them; an application should not " +
                "open the sheet with nothing to offer.",
        ) {
            var visible by remember { mutableStateOf(false) }

            Button(
                text = "Open sheet",
                onClick = { visible = true },
                style = ButtonStyle.soft(ActionColor.Neutral),
                size = ButtonSize.Small,
            )

            AvatarSourceSheet(
                visible = visible,
                options = emptyList(),
                onDismiss = { visible = false },
            )
        }
        case(
            name = "Opened from a narrow container",
            expectation = "The button sits in 200dp; the sheet still spans the window, not the container.",
        ) {
            var visible by remember { mutableStateOf(false) }

            Button(
                text = "Open sheet",
                onClick = { visible = true },
                modifier = Modifier.width(200.dp),
                style = ButtonStyle.soft(ActionColor.Neutral),
                size = ButtonSize.Small,
            )

            AvatarSourceSheet(
                visible = visible,
                options = listOf(
                    AvatarSourceOptions.camera { visible = false },
                    AvatarSourceOptions.library { visible = false },
                    AvatarSourceOptions.remove { visible = false },
                ),
                onDismiss = { visible = false },
            )
        }
    }
}

/** How long the recipe's pretend upload takes, so the avatar's busy state can be seen. */
private const val UploadDelayMillis: Long = 1_500
