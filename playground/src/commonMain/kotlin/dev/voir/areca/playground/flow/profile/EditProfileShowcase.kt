package dev.voir.areca.playground.flow.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import dev.voir.areca.flow.profile.ProfileSummary
import dev.voir.areca.flow.profile.part.AvatarUploadState
import dev.voir.areca.flow.profile.part.AvatarUploadStatus
import dev.voir.areca.flow.profile.part.EditableAvatar
import dev.voir.areca.flow.profile.row.BirthdayRow
import dev.voir.areca.flow.profile.row.DeleteAccountRow
import dev.voir.areca.flow.profile.row.ProfileInfoRow
import dev.voir.areca.flow.profile.sheet.AvatarSourceOption
import dev.voir.areca.flow.profile.sheet.AvatarSourceOptions
import dev.voir.areca.flow.profile.sheet.AvatarSourceSheet
import dev.voir.areca.flow.profile.sheet.DeleteAccountSheet
import dev.voir.areca.foundation.adaptive.AdaptiveDefaults
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.list.ListItemChevron
import dev.voir.areca.foundation.settings.Section
import dev.voir.areca.foundation.settings.Settings
import dev.voir.areca.foundation.settings.SettingsRowIcon
import dev.voir.areca.foundation.settings.SettingsTextFieldRow
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.AtSign
import dev.voir.areca.icons.Phone
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
import kotlinx.datetime.LocalDate

/** States of a picture change, as a control. */
private enum class PictureChangeOption(
    /** Label of the option in the control panel. */
    val label: String,
    /** The state it stands for. */
    val state: AvatarUploadState,
) {
    /** Nothing happening. */
    Idle("idle", AvatarUploadState.Idle),

    /** Part-way through, with a known fraction. */
    Uploading("uploading", AvatarUploadState.Uploading(progress = 0.42f)),

    /** Stopped. */
    Failed("failed", AvatarUploadState.Failed("The picture is larger than 10 MB.")),
}

/**
 * The edit-profile screen: the picture, the name, a bio, the facts and the way out — the same shape
 * as a messenger's account screen, kept neutral.
 */
@Composable
fun EditProfileShowcase() = FlowShowcase(
    title = "Edit profile",
    summary = "The screen that edits a profile: picture, name, bio, facts and account deletion.",
    module = "flow-profile",
    status = ShowcaseStatus.Beta,
    notes = {
        ChromeText(text = "The recipe", role = ChromeTextRole.Label)
        ChromeText(
            text = "EditProfileRecipe in this file is the whole assembly. The names and the bio are " +
                "foundation SettingsTextFieldRows — the card is the field and the hairline its " +
                "underline. The module validates nothing: clear the first name and press Save to see " +
                "the recipe's own check put its complaint in the section footer.",
            role = ChromeTextRole.SectionDescription,
            tone = ChromeTone.Muted,
        )
        ChromeText(
            text = "The picture is its own transaction: its upload band sits under the avatar and never " +
                "touches the form. Tap the avatar for the source sheet, Birthday for the calendar and " +
                "Delete account for the typed confirmation.",
            role = ChromeTextRole.SectionDescription,
            tone = ChromeTone.Muted,
        )
    },
) {
    val avatar by enumControl(
        key = "avatar",
        default = SampleAvatar.Picture,
        options = SampleAvatar.entries,
        optionLabel = { it.label },
    )
    val upload by enumControl(
        key = "upload",
        default = PictureChangeOption.Idle,
        options = PictureChangeOption.entries,
        optionLabel = { it.label },
    )
    val camera by booleanControl("camera available", default = true)
    val saving by booleanControl("saving", default = false)

    preview {
        var fields by remember {
            mutableStateOf(
                ProfileFields(
                    firstName = "Ada",
                    lastName = "Lovelace",
                    bio = ProfileSamples.Bio,
                    birthday = LocalDate(1815, 12, 10),
                ),
            )
        }
        var picking by remember { mutableStateOf(false) }
        var deleting by remember { mutableStateOf(false) }
        val summary = ProfileSamples.summary(avatar = avatar)

        DeviceFrame(size = DeviceSize.Regular) {
            EditProfileRecipe(
                summary = summary,
                fields = fields,
                onFieldsChange = { fields = it },
                saving = saving,
                upload = upload.state,
                avatarOptions = buildList {
                    if (camera) add(AvatarSourceOptions.camera(onClick = { picking = false }))
                    add(AvatarSourceOptions.library(onClick = { picking = false }))
                    if (summary.avatar != null) add(AvatarSourceOptions.remove(onClick = { picking = false }))
                },
                pickingAvatar = picking,
                confirmingDelete = deleting,
                onChangePhoto = { picking = true },
                onAvatarSheetDismissed = { picking = false },
                // The application's own rule, not the module's: this product wants a first name.
                onSave = {
                    fields = fields.copy(
                        firstNameError = if (fields.firstName.isBlank()) "Enter your first name." else null,
                    )
                },
                onDelete = { deleting = true },
                onDeleteConfirmed = { deleting = false },
                onDeleteDismissed = { deleting = false },
                windowInsets = DeviceSize.Regular.windowInsets,
            )
        }
    }
}

/**
 * The recipe: an edit-profile screen built from `flow-profile` parts on the foundation settings list.
 *
 * Internal so [AccountSettingsShowcase] can open it in its detail pane, as an application would.
 *
 * @param summary Who is signed in.
 * @param fields The form's values, and the application's complaint about the name.
 * @param onFieldsChange Called with the next values.
 * @param saving Whether a save is in flight.
 * @param upload What a change of picture is doing.
 * @param avatarOptions Picture choices this platform can complete.
 * @param pickingAvatar Whether the source sheet is up.
 * @param confirmingDelete Whether the delete sheet is up.
 * @param onChangePhoto Called when the avatar is tapped.
 * @param onAvatarSheetDismissed Called when the source sheet is dismissed.
 * @param onSave Called when the form is saved.
 * @param onDelete Called when deletion is asked for.
 * @param onDeleteConfirmed Called when deletion is confirmed.
 * @param onDeleteDismissed Called when the delete sheet is dismissed.
 * @param windowInsets Regions the system occupies.
 */
@Composable
internal fun EditProfileRecipe(
    summary: ProfileSummary,
    fields: ProfileFields,
    onFieldsChange: (ProfileFields) -> Unit,
    saving: Boolean,
    upload: AvatarUploadState,
    avatarOptions: List<AvatarSourceOption>,
    pickingAvatar: Boolean,
    confirmingDelete: Boolean,
    onChangePhoto: () -> Unit,
    onAvatarSheetDismissed: () -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit,
    onDeleteConfirmed: () -> Unit,
    onDeleteDismissed: () -> Unit,
    windowInsets: WindowInsets,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ArecaTheme.colors.bgCanvas)
            .verticalScroll(rememberScrollState())
            .windowInsetsPadding(windowInsets)
            // The fields are low on the page; the keyboard has to lift them, not cover them.
            .imePadding()
            // On a tablet or a desktop the column stops at a readable width, centred; a phone is narrower.
            .wrapContentWidth()
            .widthIn(max = AdaptiveDefaults.ReadableWidth)
            .fillMaxWidth(),
    ) {
        Spacer(Modifier.height(ArecaTheme.spacing.xl3))

        EditableAvatar(
            summary = summary,
            busy = upload is AvatarUploadState.Uploading,
            onChangePhoto = onChangePhoto,
            changePhotoLabel = "Change photo",
            modifier = Modifier.horizontalContentInsets(),
        )

        AvatarUploadStatus(
            state = upload,
            onRetry = {},
            onDismiss = {},
            uploadingSubtitle = "Keep the app open until it finishes.",
            modifier = Modifier.horizontalContentInsets(),
        )

        Spacer(Modifier.height(ArecaTheme.spacing.xl2))

        Settings {
            Section(
                header = { Text("Your name") },
                // A complaint about the name belongs under the name, in the danger colour.
                footer = fields.firstNameError?.let { complaint ->
                    { Text(complaint, color = ArecaTheme.colors.fgDangerText) }
                },
            ) {
                SettingsTextFieldRow(
                    value = fields.firstName,
                    onValueChange = { onFieldsChange(fields.copy(firstName = it, firstNameError = null)) },
                    placeholder = "First name (required)",
                    enabled = !saving,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next,
                    ),
                )
                SettingsTextFieldRow(
                    value = fields.lastName,
                    onValueChange = { onFieldsChange(fields.copy(lastName = it)) },
                    placeholder = "Last name (optional)",
                    enabled = !saving,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next,
                    ),
                )
            }

            Section(footer = { Text("A few words about you.") }) {
                SettingsTextFieldRow(
                    value = fields.bio,
                    onValueChange = { onFieldsChange(fields.copy(bio = it)) },
                    placeholder = "Bio",
                    maxLength = BioLimit,
                    singleLine = false,
                    enabled = !saving,
                )
            }

            // Every row carries a tile, and the tiles already separate the rows.
            Section(
                header = { Text("Your info") },
                footer = { Text("Only your contacts can see your birthday.") },
                separated = false,
            ) {
                ProfileInfoRow(
                    value = "+381 64 123 45 67",
                    label = "Tap to change phone number",
                    leading = { SettingsRowIcon(ArecaIcons.Phone) },
                    onClick = {},
                )
                ProfileInfoRow(
                    value = summary.handle ?: "Username",
                    label = "Username",
                    leading = { SettingsRowIcon(ArecaIcons.AtSign) },
                    trailing = { ListItemChevron() },
                    onClick = {},
                )
                BirthdayRow(
                    value = fields.birthday,
                    onValueChange = { onFieldsChange(fields.copy(birthday = it)) },
                    enabled = !saving,
                )
            }
        }

        Spacer(Modifier.height(ArecaTheme.spacing.xl2))

        Button(
            modifier = Modifier.fillMaxWidth().horizontalContentInsets(),
            enabled = !saving,
            loading = saving,
            onClick = onSave,
        ) {
            Text("Save")
        }

        Spacer(Modifier.height(ArecaTheme.spacing.xl2))

        Settings {
            // A section of its own with no header: "Danger zone" would name a group of one.
            Section {
                DeleteAccountRow(
                    onClick = onDelete,
                    subtitle = "Permanent, and cannot be undone",
                    enabled = !saving,
                )
            }
        }

        Spacer(Modifier.height(ArecaTheme.spacing.xl3))
    }

    AvatarSourceSheet(
        visible = pickingAvatar,
        options = avatarOptions,
        onDismiss = onAvatarSheetDismissed,
        subtitle = "Choose where your new photo comes from.",
    )

    DeleteAccountSheet(
        visible = confirmingDelete,
        confirmationPhrase = "DELETE",
        description = "Your profile and everything in it will be removed from every device you use.",
        note = "This cannot be undone.",
        onConfirm = onDeleteConfirmed,
        onDismiss = onDeleteDismissed,
    )
}

/**
 * What the edit screen holds — the application's own state, not the module's.
 *
 * @property firstName First name as typed.
 * @property lastName Last name as typed.
 * @property bio Bio as typed.
 * @property birthday Chosen birthday, or `null`.
 * @property firstNameError The application's complaint about the first name, already worded.
 */
internal data class ProfileFields(
    val firstName: String,
    val lastName: String,
    val bio: String,
    val birthday: LocalDate?,
    val firstNameError: String? = null,
)

/** Most characters a bio may hold; the field counts down to it. */
private const val BioLimit = 140
