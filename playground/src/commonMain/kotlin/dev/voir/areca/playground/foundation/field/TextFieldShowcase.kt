package dev.voir.areca.playground.foundation.field

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.field.AffixTextField
import dev.voir.areca.foundation.field.FieldSize
import dev.voir.areca.foundation.field.FieldStatus
import dev.voir.areca.foundation.field.FieldStyle
import dev.voir.areca.foundation.field.PasswordField
import dev.voir.areca.foundation.field.SearchField
import dev.voir.areca.foundation.field.TextField
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.Mail
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Sizes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

/** Style presets offered by [FieldStyle], as a selectable list. */
private enum class FieldStyleOption {
    Outlined,
    Soft,
    Filled,
    Underlined,
    Inline,
}

/** Sizes offered by [FieldSize], as a selectable list. */
private enum class FieldSizeOption {
    ExtraSmall,
    Small,
    Medium,
    Large,
    ExtraLarge,
}

/**
 * Showcase for the text input family: [TextField], [AffixTextField], [PasswordField] and
 * [SearchField].
 *
 * One page because they share a style, size and status model and differ only in what they add.
 */
@Composable
fun TextFieldShowcase() = ShowcaseScaffold(
    title = "Text field",
    summary = "Single-line and multi-line text input, with affix, password and search variants.",
    module = "foundation",
) {
    Playground {
        val placeholder by textControl("placeholder", default = "you@example.com")
        val style by enumControl(
            key = "style",
            default = FieldStyleOption.Outlined,
            options = FieldStyleOption.entries,
            optionLabel = { it.name },
        )
        val size by enumControl(
            key = "size",
            default = FieldSizeOption.Medium,
            options = FieldSizeOption.entries,
            optionLabel = { it.name },
        )
        val status by enumControl(
            key = "status",
            default = FieldStatus.Default,
            options = FieldStatus.entries,
            optionLabel = { it.name },
        )
        val enabled by booleanControl("enabled", default = true)
        val readOnly by booleanControl("readOnly", default = false)
        val loading by booleanControl("loading", default = false)
        val singleLine by booleanControl("singleLine", default = true)
        val withLeading by booleanControl("leading", default = false, label = "leading slot")

        preview {
            var value by remember { mutableStateOf("") }

            TextField(
                value = value,
                onValueChange = { value = it },
                modifier = Modifier.fillMaxWidth(),
                style = style.resolve(),
                size = size.resolve(),
                status = status,
                enabled = enabled,
                readOnly = readOnly,
                loading = loading,
                singleLine = singleLine,
                placeholder = { Text(placeholder) },
                leading = if (withLeading) {
                    { Icon(imageVector = ArecaIcons.Mail, contentDescription = null) }
                } else {
                    null
                },
            )
        }
    }

    Variants(
        title = "Styles",
        description = "Each preset decides which of the fill, the border and the focus ring " +
            "carries the field's state.",
        layout = VariantLayout.Vertical,
    ) {
        for (option in FieldStyleOption.entries) {
            variant(name = option.name) {
                SampleField(style = option.resolve(), placeholder = option.name)
            }
        }
    }

    Sizes(
        description = "Size drives padding, min height, text style and the icon and spinner sizes.",
        layout = VariantLayout.Vertical,
    ) {
        for (option in FieldSizeOption.entries) {
            variant(name = option.name) {
                SampleField(size = option.resolve(), placeholder = option.name)
            }
        }
    }

    Variants(
        title = "Status",
        description = "Status outranks focus, and reaches whichever roles the style gives it — " +
            "the border on outlined, the fill as well on soft and filled.",
        layout = VariantLayout.Vertical,
    ) {
        for (status in FieldStatus.entries) {
            variant(name = status.name) {
                SampleField(status = status, initial = "jane@example.com")
            }
        }
    }

    Variants(
        title = "Status on a soft style",
        description = "The same statuses on FieldStyle.soft(), where the fill reports them too.",
        layout = VariantLayout.Vertical,
    ) {
        for (status in FieldStatus.entries) {
            variant(name = status.name) {
                SampleField(
                    style = FieldStyle.soft(),
                    status = status,
                    initial = "jane@example.com",
                )
            }
        }
    }

    States(layout = VariantLayout.Vertical) {
        variant("Empty") { SampleField(placeholder = "Placeholder showing") }
        variant("Filled") { SampleField(initial = "jane@example.com") }
        variant("Disabled") { SampleField(initial = "jane@example.com", enabled = false) }
        variant("Read-only", detail = "selectable but not editable") {
            SampleField(initial = "jane@example.com", readOnly = true)
        }
        variant("Loading", detail = "spinner in the trailing slot") {
            SampleField(initial = "Checking availability", loading = true)
        }
    }

    Variants(
        title = "Family",
        description = "Same field, different affordances layered on top.",
        layout = VariantLayout.Vertical,
    ) {
        variant("TextField", detail = "the base") {
            SampleField(placeholder = "Plain text input")
        }
        variant("AffixTextField", detail = "prefix and suffix") {
            var value by remember { mutableStateOf("") }
            AffixTextField(
                value = value,
                onValueChange = { value = it },
                modifier = Modifier.fillMaxWidth(),
                prefix = { Text(text = "https://", variant = TextVariant.BodyMedium, color = TextColor.Muted) },
                suffix = { Text(text = ".voir.dev", variant = TextVariant.BodyMedium, color = TextColor.Muted) },
                placeholder = { Text("workspace") },
            )
        }
        variant("PasswordField", detail = "masked with a reveal toggle") {
            var value by remember { mutableStateOf("correct horse battery") }
            PasswordField(
                value = value,
                onValueChange = { value = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Password") },
            )
        }
        variant("SearchField", detail = "search icon and IME action") {
            var value by remember { mutableStateOf("") }
            SearchField(
                value = value,
                onValueChange = { value = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search components") },
            )
        }
    }

    Variants(
        title = "Affix behaviour",
        description = "Whether affixes show before the field has a value.",
        layout = VariantLayout.Vertical,
    ) {
        variant("On value only", detail = "alwaysShowAffixes = false") {
            var value by remember { mutableStateOf("") }
            AffixTextField(
                value = value,
                onValueChange = { value = it },
                modifier = Modifier.fillMaxWidth(),
                prefix = { Text(text = "$", variant = TextVariant.BodyMedium, color = TextColor.Muted) },
                suffix = { Text(text = "USD", variant = TextVariant.BodyMedium, color = TextColor.Muted) },
                placeholder = { Text("Type to reveal the affixes") },
            )
        }
        variant("Always", detail = "alwaysShowAffixes = true") {
            var value by remember { mutableStateOf("") }
            AffixTextField(
                value = value,
                onValueChange = { value = it },
                modifier = Modifier.fillMaxWidth(),
                alwaysShowAffixes = true,
                prefix = { Text(text = "$", variant = TextVariant.BodyMedium, color = TextColor.Muted) },
                suffix = { Text(text = "USD", variant = TextVariant.BodyMedium, color = TextColor.Muted) },
                placeholder = { Text("Affixes visible while empty") },
            )
        }
    }

    EdgeCases {
        case(
            name = "Input formatting",
            expectation = "format rewrites the value on every keystroke — this one forces uppercase.",
        ) {
            var value by remember { mutableStateOf("") }
            TextField(
                value = value,
                onValueChange = { value = it },
                modifier = Modifier.fillMaxWidth(),
                format = { it.uppercase() },
                singleLine = true,
                placeholder = { Text("Type in lower case") },
            )
        }
        case(
            name = "Multi-line growth",
            expectation = "Grows with the content between minLines and maxLines, then scrolls.",
        ) {
            var value by remember { mutableStateOf("") }
            TextField(
                value = value,
                onValueChange = { value = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                minLines = 2,
                maxLines = 4,
                placeholder = { Text("Press return a few times") },
            )
        }
        case(
            name = "Narrow container",
            expectation = "Value scrolls horizontally; the field never forces its parent wider.",
        ) {
            var value by remember { mutableStateOf("a-very-long-value-that-will-not-fit") }
            TextField(
                value = value,
                onValueChange = { value = it },
                modifier = Modifier.width(160.dp),
                singleLine = true,
            )
        }
        case(
            name = "Long placeholder",
            expectation = "Placeholder truncates rather than wrapping in a single-line field.",
        ) {
            SampleField(
                placeholder = "Enter the email address associated with your workspace account",
            )
        }
    }
}

@Composable
private fun SampleField(
    placeholder: String = "Placeholder",
    initial: String = "",
    style: FieldStyle = FieldStyle.outlined(),
    size: FieldSize = FieldSize.Medium,
    status: FieldStatus = FieldStatus.Default,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    loading: Boolean = false,
) {
    var value by remember { mutableStateOf(initial) }

    Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
        TextField(
            value = value,
            onValueChange = { value = it },
            modifier = Modifier.fillMaxWidth(),
            style = style,
            size = size,
            status = status,
            enabled = enabled,
            readOnly = readOnly,
            loading = loading,
            singleLine = true,
            placeholder = { Text(placeholder) },
        )
    }
}

@Composable
private fun FieldStyleOption.resolve(): FieldStyle = when (this) {
    FieldStyleOption.Outlined -> FieldStyle.outlined()
    FieldStyleOption.Soft -> FieldStyle.soft()
    FieldStyleOption.Filled -> FieldStyle.filled()
    FieldStyleOption.Underlined -> FieldStyle.underlined()
    FieldStyleOption.Inline -> FieldStyle.inline()
}

@Composable
private fun FieldSizeOption.resolve(): FieldSize = when (this) {
    FieldSizeOption.ExtraSmall -> FieldSize.ExtraSmall
    FieldSizeOption.Small -> FieldSize.Small
    FieldSizeOption.Medium -> FieldSize.Medium
    FieldSizeOption.Large -> FieldSize.Large
    FieldSizeOption.ExtraLarge -> FieldSize.ExtraLarge
}
