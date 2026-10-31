package dev.voir.areca.playground.foundation.adaptive

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.adaptive.AdaptiveDefaults
import dev.voir.areca.foundation.adaptive.ContentColumn
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.field.FieldGroup
import dev.voir.areca.foundation.field.PasswordField
import dev.voir.areca.foundation.field.TextField
import dev.voir.areca.foundation.text.Heading
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.canvas.WindowFrame
import dev.voir.areca.playground.showcase.canvas.WindowPreset
import dev.voir.areca.playground.showcase.canvas.windowInsets
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

/** The two widths a content column is usually capped at. */
private enum class MaxWidthOption(val width: Dp) {
    ReadableWidth(AdaptiveDefaults.ReadableWidth),
    ContentWidth(AdaptiveDefaults.ContentWidth),
}

/** Where the column puts a form shorter than the window. */
private enum class ArrangementOption { Top, Center }

/**
 * Showcase for [ContentColumn].
 *
 * The sample is a sign-in form: full width less the gutters on a phone, and stopped at a readable
 * width and centred on anything wider.
 */
@Composable
fun ContentColumnShowcase() = ShowcaseScaffold(
    title = "Content column",
    summary = "Keeps a form or a page of reading at a readable width, centred, on a window of any size.",
    module = "foundation",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "Widen the window: past the cap the column stops growing and the margins take the rest.") {
        val preset by enumControl(
            key = "window",
            default = WindowPreset.Laptop,
            options = WindowPreset.entries,
            optionLabel = { it.label },
        )
        val maxWidth by enumControl(
            key = "maxWidth",
            default = MaxWidthOption.ReadableWidth,
            options = MaxWidthOption.entries,
            optionLabel = { "${it.name} · ${it.width.value.toInt()}dp" },
            description = "ReadableWidth for forms and reading; ContentWidth for browsing.",
        )
        val arrangement by enumControl(
            key = "verticalArrangement",
            default = ArrangementOption.Center,
            options = ArrangementOption.entries,
            optionLabel = { it.name },
            description = "Center needs the column's full height: the sample passes Modifier.fillMaxSize().",
        )
        val centred by booleanControl(
            key = "horizontalAlignment",
            default = false,
            description = "On: children centred in the column. Off: Alignment.Start.",
        )

        preview {
            WindowFrame(preset = preset) {
                SignInForm(
                    preset = preset,
                    maxWidth = maxWidth.width,
                    arrangement = arrangement,
                    centred = centred,
                )
            }
        }
    }

    Variants(
        title = "Every window",
        description = "The same form at each window: the column follows the window up to 640dp, then stops.",
        layout = VariantLayout.Grid,
    ) {
        for (preset in WindowPreset.entries) {
            variant(preset.label, detail = "${preset.width.value.toInt()} × ${preset.height.value.toInt()}dp") {
                WindowFrame(preset = preset, maxHeight = 280.dp) { SignInForm(preset = preset) }
            }
        }
    }

    Variants(
        title = "Max width",
        description = "The two caps AdaptiveDefaults names, on a laptop: one keeps a form's line length, one a page of cards.",
        layout = VariantLayout.Vertical,
    ) {
        for (option in MaxWidthOption.entries) {
            variant("maxWidth = ${option.name}", detail = "${option.width.value.toInt()}dp") {
                WindowFrame(preset = WindowPreset.Laptop, maxHeight = 260.dp) {
                    SignInForm(preset = WindowPreset.Laptop, maxWidth = option.width)
                }
            }
        }
    }

    EdgeCases(description = "Windows narrower and wider than the cap, and a column taller than its window.") {
        case(
            name = "Narrowest phone",
            expectation = "344dp is under the cap: the column is the window less the 16dp gutters, as any phone screen.",
        ) {
            WindowFrame(preset = WindowPreset.FoldFolded, maxHeight = 320.dp) {
                SignInForm(preset = WindowPreset.FoldFolded)
            }
        }
        case(
            name = "ContentWidth on a desktop monitor",
            expectation = "1920dp is wider than 1200dp: the column stops at 1200dp and sits centred.",
        ) {
            WindowFrame(preset = WindowPreset.Desktop, maxHeight = 280.dp) {
                SignInForm(preset = WindowPreset.Desktop, maxWidth = AdaptiveDefaults.ContentWidth)
            }
        }
        case(
            name = "Taller than the window",
            expectation = "On a phone on its side the form scrolls, with the scroll on the full-width area rather than the column.",
        ) {
            WindowFrame(preset = WindowPreset.PhoneLandscape, maxHeight = 240.dp) {
                SignInForm(preset = WindowPreset.PhoneLandscape, arrangement = ArrangementOption.Top, scroll = true)
            }
        }
    }
}

/**
 * A sign-in form in a [ContentColumn], written as a consumer would.
 *
 * @param preset Window the form is drawn in, for its insets.
 * @param maxWidth Widest the column grows.
 * @param arrangement Where the form sits vertically.
 * @param centred Whether the children are centred in the column.
 * @param scroll Whether the column scrolls rather than filling the window's height.
 */
@Composable
private fun SignInForm(
    preset: WindowPreset,
    maxWidth: Dp = AdaptiveDefaults.ReadableWidth,
    arrangement: ArrangementOption = ArrangementOption.Center,
    centred: Boolean = false,
    scroll: Boolean = false,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val spacing = ArecaTheme.spacing.xl

    ContentColumn(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(preset.windowInsets)
            .then(if (scroll) Modifier.verticalScroll(rememberScrollState()) else Modifier),
        maxWidth = maxWidth,
        verticalArrangement = when (arrangement) {
            ArrangementOption.Top -> Arrangement.spacedBy(spacing)
            ArrangementOption.Center -> Arrangement.spacedBy(spacing, Alignment.CenterVertically)
        },
        horizontalAlignment = if (centred) Alignment.CenterHorizontally else Alignment.Start,
    ) {
        Heading(
            title = { Text("Sign in to Ledger") },
            supportive = { Text("Use the email address you registered with.") },
        )
        FieldGroup(modifier = Modifier.fillMaxWidth(), label = "Email address") { field ->
            TextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                field = field,
                singleLine = true,
                placeholder = { Text("name@example.com") },
            )
        }
        FieldGroup(modifier = Modifier.fillMaxWidth(), label = "Password") { field ->
            PasswordField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                field = field,
            )
        }
        Button(text = "Sign in", onClick = {}, modifier = Modifier.fillMaxWidth())
        Text(
            "Column · ${ArecaTheme.window.size.width.value.toInt()}dp · ${ArecaTheme.window.width.name}",
            variant = TextVariant.BodySmall,
            color = TextColor.Muted,
        )
    }
}
