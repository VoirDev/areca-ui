# Form with validation

A sign-up form: a heading, an email field and a password field, each with its label and error line,
a banner for a server error, and a submit button that shows progress. Errors appear after the first
attempt to submit, and each is announced when it appears.

Needs `dev.voir.areca:foundation`.

## Components

| Component | Role here |
| --- | --- |
| `Heading` | The form's title and supporting line, announced as a heading |
| `Notice` with `NoticeTone.Danger` | A failure that belongs to the whole form, not to one field |
| `FieldGroup` | Each field's label, required marker and message line. Its `error` turns the field red, shows the message and announces it |
| `TextField` | The email address |
| `PasswordField` | The password, with a reveal control |
| `FieldDefaults.Placeholder` | Placeholder text in the field's placeholder style |
| `Button` with `loading` | The submit action; blocks presses and shows a spinner while submitting |

Every component is in the [catalogue](../components.md).

## Code

```kotlin
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.field.FieldDefaults
import dev.voir.areca.foundation.field.FieldGroup
import dev.voir.areca.foundation.field.PasswordField
import dev.voir.areca.foundation.field.TextField
import dev.voir.areca.foundation.notice.Notice
import dev.voir.areca.foundation.notice.NoticeText
import dev.voir.areca.foundation.notice.NoticeTone
import dev.voir.areca.foundation.text.Heading
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.contentInsets
```

```kotlin
@Composable
fun SignUpForm(
    submitting: Boolean,
    serverError: String?,
    onSubmit: (email: String, password: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var attempted by rememberSaveable { mutableStateOf(false) }

    val emailError = "Enter an email address like name@example.com."
        .takeIf { attempted && "@" !in email }
    val passwordError = "Use at least 8 characters.".takeIf { attempted && password.length < 8 }
    val submit = {
        attempted = true
        if ("@" in email && password.length >= 8) onSubmit(email, password)
    }

    Column(
        modifier = modifier.contentInsets(),
        verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl),
    ) {
        Heading(
            title = { Text("Create your account") },
            supportive = { Text("You can change these later in Settings.") },
        )
        if (serverError != null) {
            Notice(title = null, tone = NoticeTone.Danger) { NoticeText(serverError) }
        }
        FieldGroup(label = "Email", required = true, error = emailError) { field ->
            TextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                field = field,
                singleLine = true,
                placeholder = { FieldDefaults.Placeholder("name@example.com") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                ),
            )
        }
        FieldGroup(
            label = "Password",
            required = true,
            assistive = "At least 8 characters.",
            error = passwordError,
        ) { field ->
            PasswordField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                field = field,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(onDone = { submit() }),
                revealContentDescription = "Show password",
                hideContentDescription = "Hide password",
            )
        }
        Button(
            text = "Create account",
            onClick = submit,
            modifier = Modifier.fillMaxWidth(),
            loading = submitting,
            loadingStateDescription = "Creating account",
        )
    }
}
```

- **Forward the scope.** `FieldGroup` hands its content a `FieldScope`; pass it to the field as
  `field`. That is what draws the error state, announces the label and the error, and focuses the
  field when the label is tapped.
- **One status.** The group decides the status from `error`, so the message and the red border never
  disagree. Pass `null` for no error.
- **Validation is yours.** The component shows what you tell it. Here an error appears only once
  `attempted` is set, so the form does not complain before the user has typed.
- **Keyboard.** `ImeAction.Next` moves to the next field; `ImeAction.Done` with
  `KeyboardActions(onDone = …)` submits.

## Variations

**A choice from a list.** `SelectField` (in `dev.voir.areca.foundation.field.select`) takes the same
scope. Its selection is a set, so a single choice is a set of one:

```kotlin
FieldGroup(label = "Country", required = true, error = countryError) { field ->
    SelectField(
        values = setOfNotNull(country),
        onChange = { onCountryChange(it.firstOrNull()) },
        options = Country.entries.map { SelectOption(key = it, value = it, label = it.label) },
        modifier = Modifier.fillMaxWidth(),
        field = field,
        placeholder = { FieldDefaults.Placeholder("Choose a country") },
    )
}
```

**One look for the whole form.** Fields read their style from `LocalFieldStyle`. Provide it once,
and put the label inside the box with `FieldLabelPlacement.Inside`, which works only through the
scope:

```kotlin
CompositionLocalProvider(LocalFieldStyle provides FieldStyle.filled()) {
    FieldGroup(label = "Name", labelPlacement = FieldLabelPlacement.Inside) { field ->
        TextField(name, onValueChange = onNameChange, modifier = Modifier.fillMaxWidth(), field = field)
    }
}
```

**Other fields.** `SearchField` for a query, `AffixTextField` for a value with a prefix or a suffix
(a currency, a unit), `OtpField` for a one-time code, `FieldRow` for parts that share one border —
an amount and its currency. `RequirementList` ticks off password rules as they are met.

**An error that is not a field's.** A message composed into existence outside a `FieldGroup` — a
failed sign-in shown as text — is not spoken by itself. `AnnounceOnChange(message)` speaks it when
it appears and whenever it changes.

**Settings typed in place.** A short form inside a settings card is `SettingsTextFieldRow`; see the
[settings screen](settings.md).

**Sign-in and sign-up.** `flow-auth` ships the fields, provider buttons and legal line of an
authentication screen, with autofill types set; see [flow/auth](../../flow/auth/README.md).
