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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import dev.voir.areca.flow.profile.row.UsernameCheck
import dev.voir.areca.flow.profile.row.UsernameCheckFooter
import dev.voir.areca.foundation.adaptive.AdaptiveDefaults
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.settings.Section
import dev.voir.areca.foundation.settings.Settings
import dev.voir.areca.foundation.settings.SettingsTextFieldRow
import dev.voir.areca.foundation.text.Text
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

/**
 * The username screen: one field, and under it the service's verdict as it arrives.
 */
@Composable
fun UsernameShowcase() = FlowShowcase(
    title = "Username",
    summary = "Choosing a username: a field and the service's verdict on it, under the field.",
    module = "flow-profile",
    status = ShowcaseStatus.Beta,
    notes = {
        ChromeText(text = "The recipe", role = ChromeTextRole.Label)
        ChromeText(
            text = "UsernameRecipe is a foundation Section holding one SettingsTextFieldRow, with a " +
                "UsernameCheckFooter as its footer — the line a section already reserves, so nothing " +
                "moves as the verdict changes.",
            role = ChromeTextRole.SectionDescription,
            tone = ChromeTone.Muted,
        )
        ChromeText(
            text = "The footer decides nothing. The verdict is the service's, so it is a control here; " +
                "the rule against spaces is this recipe's own, passed in as a complaint. Type a space " +
                "to see it outrank the verdict.",
            role = ChromeTextRole.SectionDescription,
            tone = ChromeTone.Muted,
        )
    },
) {
    val check by enumControl(
        key = "service verdict",
        default = UsernameCheck.Available,
        options = UsernameCheck.entries,
        optionLabel = { it.name.lowercase() },
    )
    val saving by booleanControl("saving", default = false)

    preview {
        var username by remember { mutableStateOf("ada_lovelace") }

        DeviceFrame(size = DeviceSize.Regular) {
            UsernameRecipe(
                username = username,
                onUsernameChange = { username = it },
                check = check,
                // The application's own rule, not the module's.
                complaint = if (username.any { it.isWhitespace() }) "A username can't contain spaces." else null,
                saving = saving,
                onSave = {},
                windowInsets = DeviceSize.Regular.windowInsets,
            )
        }
    }
}

/**
 * The recipe: a username screen built from a foundation settings section and `flow-profile`'s
 * verdict footer.
 *
 * @param username The username as typed, without the `@`.
 * @param onUsernameChange Called on every edit, where an application starts its availability check.
 * @param check The service's verdict on [username].
 * @param complaint The application's own objection to [username], or `null`.
 * @param saving Whether a save is in flight.
 * @param onSave Called when the username is saved.
 * @param windowInsets Regions the system occupies.
 */
@Composable
private fun UsernameRecipe(
    username: String,
    onUsernameChange: (String) -> Unit,
    check: UsernameCheck,
    complaint: String?,
    saving: Boolean,
    onSave: () -> Unit,
    windowInsets: WindowInsets,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ArecaTheme.colors.bgCanvas)
            .windowInsetsPadding(windowInsets)
            .imePadding()
            // On a tablet or a desktop the column stops at a readable width, centred; a phone is narrower.
            .wrapContentWidth()
            .widthIn(max = AdaptiveDefaults.ReadableWidth)
            .fillMaxWidth(),
    ) {
        Spacer(Modifier.height(ArecaTheme.spacing.xl3))

        Settings {
            Section(
                header = { Text("Username") },
                footer = {
                    UsernameCheckFooter(
                        username = username,
                        check = check,
                        complaint = complaint,
                        hint = "People can find you by this name.",
                    )
                },
            ) {
                SettingsTextFieldRow(
                    value = username,
                    onValueChange = onUsernameChange,
                    placeholder = "Username",
                    leading = { Text("@") },
                    enabled = !saving,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.None,
                        autoCorrectEnabled = false,
                        imeAction = ImeAction.Done,
                    ),
                )
            }
        }

        Spacer(Modifier.weight(1f))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalContentInsets(),
            enabled = !saving && complaint == null && check != UsernameCheck.Taken,
            loading = saving,
            onClick = onSave,
        ) {
            Text("Save")
        }

        Spacer(Modifier.height(ArecaTheme.spacing.xl3))
    }
}
