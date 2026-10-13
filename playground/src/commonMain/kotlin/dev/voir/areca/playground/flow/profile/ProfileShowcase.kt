package dev.voir.areca.playground.flow.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import dev.voir.areca.flow.profile.ProfileSummary
import dev.voir.areca.flow.profile.part.ProfileCardAction
import dev.voir.areca.flow.profile.part.ProfileCoverCard
import dev.voir.areca.flow.profile.part.ProfileHeaderCard
import dev.voir.areca.flow.profile.part.ProfileLoadFailure
import dev.voir.areca.flow.profile.part.StaleDetailsNotice
import dev.voir.areca.flow.profile.row.EmailRow
import dev.voir.areca.flow.profile.row.ProfileInfoRow
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.list.ListItemChevron
import dev.voir.areca.foundation.settings.Section
import dev.voir.areca.foundation.settings.Settings
import dev.voir.areca.foundation.settings.SettingsRow
import dev.voir.areca.foundation.settings.SettingsRowIcon
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.AtSign
import dev.voir.areca.icons.Edit
import dev.voir.areca.icons.Gift
import dev.voir.areca.icons.Laptop
import dev.voir.areca.icons.Phone
import dev.voir.areca.icons.QrCode
import dev.voir.areca.icons.Share
import dev.voir.areca.playground.showcase.FlowShowcase
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.canvas.DeviceFrame
import dev.voir.areca.playground.showcase.canvas.DeviceSize
import dev.voir.areca.playground.showcase.canvas.windowInsets
import dev.voir.areca.playground.showcase.chrome.ChromeText
import dev.voir.areca.playground.showcase.chrome.ChromeTextRole
import dev.voir.areca.playground.showcase.chrome.ChromeTone
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.horizontalContentInsets

/** Which card opens the profile. */
private enum class ProfileCardOption(
    /** Label of the option in the control panel. */
    val label: String,
) {
    /** The centred header card with action tiles. */
    Header("header card"),

    /** The cover card with a bio. */
    Cover("cover card"),
}

/** How the profile itself loaded. */
private enum class ProfileLoad(
    /** Label of the option in the control panel. */
    val label: String,
) {
    /** Loaded and current. */
    Fresh("fresh"),

    /** Loaded from memory; the refresh failed. */
    Stale("stale"),

    /** Not loaded at all. */
    Failed("failed"),
}

/**
 * A profile screen — one's own, as others would see it — assembled from the module's parts on the
 * foundation settings list.
 */
@Composable
fun ProfileShowcase() = FlowShowcase(
    title = "Profile",
    summary = "Who the account is: a profile card, the facts about it, and a way to its devices.",
    module = "flow-profile",
    status = ShowcaseStatus.Beta,
    notes = {
        ChromeText(text = "The recipe", role = ChromeTextRole.Label)
        ChromeText(
            text = "ProfileRecipe in this file is the whole assembly: a card from the module, then a " +
                "foundation Settings list of ProfileInfoRow facts. Values lead and labels follow.",
            role = ChromeTextRole.SectionDescription,
            tone = ChromeTone.Muted,
        )
        ChromeText(
            text = "Only a profile that could not be loaded at all replaces the screen; one loaded " +
                "from memory keeps it and says so above the card.",
            role = ChromeTextRole.SectionDescription,
            tone = ChromeTone.Muted,
        )
    },
) {
    val card by enumControl(
        key = "card",
        default = ProfileCardOption.Header,
        options = ProfileCardOption.entries,
        optionLabel = { it.label },
    )
    val avatar by enumControl(
        key = "avatar",
        default = SampleAvatar.Picture,
        options = SampleAvatar.entries,
        optionLabel = { it.label },
    )
    val verified by booleanControl("email verified", default = true)
    val profile by enumControl(
        key = "profile",
        default = ProfileLoad.Fresh,
        options = ProfileLoad.entries,
        optionLabel = { it.label },
    )

    preview {
        DeviceFrame(size = DeviceSize.Regular) {
            if (profile == ProfileLoad.Failed) {
                ProfileLoadFailure(
                    message = "Check your connection and try again.",
                    onRetry = {},
                    modifier = Modifier.background(ArecaTheme.colors.bgCanvas),
                )
            } else {
                ProfileRecipe(
                    summary = ProfileSamples.summary(avatar = avatar, isVerified = verified),
                    useCover = card == ProfileCardOption.Cover,
                    staleReason = if (profile == ProfileLoad.Stale) {
                        "We couldn't reach the server, so these are the details saved on this device."
                    } else {
                        null
                    },
                    windowInsets = DeviceSize.Regular.windowInsets,
                )
            }
        }
    }
}

/**
 * The recipe: a profile screen built from `flow-profile` parts on the foundation settings list.
 *
 * @param summary Who the account is.
 * @param useCover Whether to open with the cover card rather than the header card.
 * @param staleReason Why the details could not be refreshed, or `null` when they are fresh.
 * @param windowInsets Regions the system occupies.
 */
@Composable
private fun ProfileRecipe(
    summary: ProfileSummary,
    useCover: Boolean,
    staleReason: String?,
    windowInsets: WindowInsets,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ArecaTheme.colors.bgCanvas)
            .verticalScroll(rememberScrollState())
            .windowInsetsPadding(windowInsets),
    ) {
        Spacer(Modifier.height(ArecaTheme.spacing.lg))

        // Above the card it is talking about: it describes everything below it.
        if (staleReason != null) {
            StaleDetailsNotice(message = staleReason, modifier = Modifier.horizontalContentInsets())
            Spacer(Modifier.height(ArecaTheme.spacing.lg))
        }

        if (useCover) {
            ProfileCoverCard(
                summary = summary,
                bio = ProfileSamples.Bio,
                cover = { ProfileSamples.Cover() },
                modifier = Modifier.horizontalContentInsets(),
                actions = {
                    Button(
                        style = ButtonStyle.soft(ActionColor.Neutral),
                        size = ButtonSize.Small,
                        onClick = {},
                    ) {
                        Text("Edit profile")
                    }
                },
            )
        } else {
            ProfileHeaderCard(
                summary = summary,
                modifier = Modifier.horizontalContentInsets(),
                actions = {
                    ProfileCardAction(ArecaIcons.Edit, "Edit", onClick = {}, modifier = Modifier.weight(1f))
                    ProfileCardAction(ArecaIcons.Share, "Share", onClick = {}, modifier = Modifier.weight(1f))
                    ProfileCardAction(ArecaIcons.QrCode, "QR code", onClick = {}, modifier = Modifier.weight(1f))
                },
            )
        }

        Spacer(Modifier.height(ArecaTheme.spacing.lg))

        Settings {
            // Every row carries a tile, and the tiles already separate the rows.
            Section(header = { Text("Your info") }, separated = false) {
                ProfileInfoRow(
                    value = "+381 64 123 45 67",
                    label = "Mobile",
                    leading = { SettingsRowIcon(ArecaIcons.Phone) },
                )
                summary.handle?.let { handle ->
                    ProfileInfoRow(
                        value = handle,
                        label = "Username",
                        leading = { SettingsRowIcon(ArecaIcons.AtSign) },
                    )
                }
                EmailRow(email = summary.email, isVerified = summary.isVerified, unverifiedBadge = "Unverified")
                ProfileInfoRow(
                    value = "10 December 1815",
                    label = "Birthday",
                    leading = { SettingsRowIcon(ArecaIcons.Gift) },
                )
            }

            Section {
                SettingsRow(
                    title = { Text("Devices") },
                    value = { Text("3") },
                    leading = { SettingsRowIcon(ArecaIcons.Laptop) },
                    trailing = { ListItemChevron() },
                    onClick = {},
                )
            }
        }

        Spacer(Modifier.height(ArecaTheme.spacing.xl3))
    }
}
