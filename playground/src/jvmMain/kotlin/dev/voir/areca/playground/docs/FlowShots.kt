package dev.voir.areca.playground.docs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.flow.attachments.part.AttachmentStrip
import dev.voir.areca.flow.attachments.sheet.AttachmentSourceOptions
import dev.voir.areca.flow.attachments.sheet.AttachmentSourceSheet
import dev.voir.areca.flow.auth.AuthBranding
import dev.voir.areca.flow.auth.AuthProvider
import dev.voir.areca.flow.auth.part.AuthEmailField
import dev.voir.areca.flow.auth.part.AuthFooterPrompt
import dev.voir.areca.flow.auth.part.AuthHeader
import dev.voir.areca.flow.auth.part.AuthPasswordField
import dev.voir.areca.flow.auth.part.AuthScaffold
import dev.voir.areca.flow.auth.part.AuthTextLink
import dev.voir.areca.flow.auth.sso.SsoGroup
import dev.voir.areca.flow.profile.part.ProfileCardAction
import dev.voir.areca.flow.profile.part.ProfileHeaderCard
import dev.voir.areca.flow.profile.row.EmailRow
import dev.voir.areca.flow.profile.row.ProfileInfoRow
import dev.voir.areca.flow.profile.row.SignOutRow
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.settings.Section
import dev.voir.areca.foundation.settings.Settings
import dev.voir.areca.foundation.settings.SettingsRowIcon
import dev.voir.areca.foundation.settings.SettingsTextFieldRow
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.AtSign
import dev.voir.areca.icons.Edit
import dev.voir.areca.icons.Gift
import dev.voir.areca.icons.Phone
import dev.voir.areca.icons.QrCode
import dev.voir.areca.icons.Share
import dev.voir.areca.playground.flow.attachments.AttachmentSamples
import dev.voir.areca.playground.flow.auth.AuthSamples
import dev.voir.areca.playground.flow.profile.ProfileSamples
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.horizontalContentInsets

/** `flow-auth`: the sign-in screen, as the auth README's recipe assembles it. */
internal val FlowAuthShot = DocShot(name = "flow-auth", width = 390.dp, height = 780.dp, padded = false) {
    AuthScaffold(
        branding = AuthBranding(wordmark = { AuthSamples.Wordmark() }),
        footer = {
            AuthFooterPrompt(prompt = "New here?", linkText = "Create account", onLinkClick = {})
        },
    ) {
        AuthHeader(title = "Welcome back", subtitle = "Sign in to pick up where you left off.")
        Spacer(Modifier.height(ArecaTheme.spacing.xl6))
        AuthEmailField(value = AuthSamples.Account, onValueChange = {}, error = null)
        Spacer(Modifier.height(ArecaTheme.spacing.xl))
        AuthPasswordField(value = AuthSamples.Password, onValueChange = {}, error = null)
        Spacer(Modifier.height(ArecaTheme.spacing.lg))
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
            AuthTextLink(text = "Forgot password?", onClick = {})
        }
        Spacer(Modifier.height(ArecaTheme.spacing.xl2))
        Button(modifier = Modifier.fillMaxWidth(), onClick = {}) { Text("Sign in") }
        Spacer(Modifier.height(ArecaTheme.spacing.xl2))
        SsoGroup(providers = listOf(AuthProvider.Apple, AuthProvider.Google), onProviderClick = {})
    }
}

/** `flow-profile`: the profile screen, as the profile README's recipe assembles it. */
internal val FlowProfileShot = DocShot(name = "flow-profile", width = 390.dp) {
    Column {
        ProfileHeaderCard(
            summary = ProfileSamples.summary(),
            modifier = Modifier.horizontalContentInsets(),
            actions = {
                ProfileCardAction(ArecaIcons.Edit, "Edit", onClick = {}, modifier = Modifier.weight(1f))
                ProfileCardAction(ArecaIcons.Share, "Share", onClick = {}, modifier = Modifier.weight(1f))
                ProfileCardAction(ArecaIcons.QrCode, "QR code", onClick = {}, modifier = Modifier.weight(1f))
            },
        )
        Spacer(Modifier.height(ArecaTheme.spacing.lg))
        Settings {
            Section(header = { Text("Your info") }, separated = false) {
                ProfileInfoRow(
                    value = "+44 20 7946 0958",
                    label = "Mobile",
                    leading = { SettingsRowIcon(ArecaIcons.Phone) },
                )
                ProfileInfoRow(
                    value = "@ada",
                    label = "Username",
                    leading = { SettingsRowIcon(ArecaIcons.AtSign) },
                )
                EmailRow(email = ProfileSamples.summary().email, isVerified = true)
                ProfileInfoRow(
                    value = "10 December 1815",
                    label = "Birthday",
                    leading = { SettingsRowIcon(ArecaIcons.Gift) },
                )
            }
            Section { SignOutRow(onClick = {}) }
        }
    }
}

/** An expense form with its files in an attachment strip under the fields. */
@Composable
private fun ExpenseForm() {
    Column(Modifier.fillMaxSize()) {
        Spacer(Modifier.height(ArecaTheme.spacing.xl3))
        Text("New expense", variant = TextVariant.HeadingSmall, modifier = Modifier.horizontalContentInsets())
        Spacer(Modifier.height(ArecaTheme.spacing.lg))
        Settings {
            Section {
                SettingsTextFieldRow(value = "42.80", onValueChange = {}, placeholder = "Amount")
                SettingsTextFieldRow(value = "Corner Grocery", onValueChange = {}, placeholder = "Merchant")
                SettingsTextFieldRow(value = "", onValueChange = {}, placeholder = "Note", singleLine = false)
            }
        }
        Spacer(Modifier.height(ArecaTheme.spacing.lg))
        AttachmentStrip(
            items = AttachmentSamples.expenseFiles,
            onOpen = {},
            onAdd = {},
            onRemove = {},
            title = "Attachments",
            counter = "4 of 20",
            addCaption = "Add",
        )
        Spacer(Modifier.height(ArecaTheme.spacing.xl3))
        Button(
            text = "Save expense",
            onClick = {},
            modifier = Modifier.fillMaxWidth().horizontalContentInsets(),
        )
    }
}

/** `flow-attachments`: an expense's attachment strip, and the sheet its add square opens. */
internal val FlowAttachmentsShot = DocShot(
    name = "flow-attachments",
    panels = listOf(
        DocPanel(width = 352.dp, padded = false) { ExpenseForm() },
        DocPanel(width = 352.dp, padded = false) {
            ExpenseForm()
            AttachmentSourceSheet(
                visible = true,
                options = listOf(
                    AttachmentSourceOptions.camera {},
                    AttachmentSourceOptions.library {},
                    AttachmentSourceOptions.files {},
                ),
                onDismiss = {},
                subtitle = "Up to 25 MB each. Photos are resized before they're saved.",
            )
        },
    ),
    height = 640.dp,
)
