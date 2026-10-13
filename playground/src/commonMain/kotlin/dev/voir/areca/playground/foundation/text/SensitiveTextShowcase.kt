package dev.voir.areca.playground.foundation.text

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.IconButton
import dev.voir.areca.foundation.button.IconButtonSize
import dev.voir.areca.foundation.button.IconButtonStyle
import dev.voir.areca.foundation.text.SensitiveText
import dev.voir.areca.foundation.text.SensitiveTextSize
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.EyeOff
import dev.voir.areca.icons.EyeShow
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

/** Showcase for [SensitiveText]. */
@Composable
fun SensitiveTextShowcase() = ShowcaseScaffold(
    title = "Sensitive text",
    summary = "Value laid out at its real size and hidden under a veil until it is revealed.",
    module = "foundation",
) {
    Playground {
        val value by textControl(
            key = "text",
            default = "NL91 ABNA 0417 1643 00",
            description = "The value being protected.",
        )
        val revealed = booleanControl(
            key = "revealed",
            default = false,
            description = "Tap the text itself to toggle it too.",
        )
        val variant by enumControl(
            key = "variant",
            default = TextVariant.BodyMedium,
            options = listOf(
                TextVariant.Caption,
                TextVariant.BodyMedium,
                TextVariant.HeadingMedium,
                TextVariant.Display,
            ),
            optionLabel = { it.name },
        )
        val animated by booleanControl("enableAnimation", default = true)
        val blur by numberControl(
            key = "blurRadius",
            default = 8f,
            range = 2f..24f,
            step = 2f,
            format = { "${it.toInt()}dp" },
            description = "Ignored where the platform cannot blur; the veil turns opaque instead.",
        )

        preview {
            SensitiveText(
                text = value,
                variant = variant,
                revealed = revealed.value,
                onRevealedChange = { revealed.value = it },
                animated = animated,
                size = SensitiveTextSize.default(blurRadius = blur.dp),
            )
        }
    }

    Variants(
        title = "Type scale",
        description = "The veil is measured from the text it covers, so it fits any role.",
        layout = VariantLayout.Vertical,
    ) {
        variant("Caption", detail = "concealed") {
            SensitiveText(text = "Ends 4242 · exp 09/29", variant = TextVariant.Caption)
        }
        variant("Body", detail = "concealed") {
            SensitiveText(text = "NL91 ABNA 0417 1643 00", variant = TextVariant.BodyMedium)
        }
        variant("Heading", detail = "concealed") {
            SensitiveText(text = "€ 12 480.55", variant = TextVariant.HeadingMedium)
        }
        variant("Display", detail = "revealed") {
            SensitiveText(text = "€ 12 480.55", variant = TextVariant.Display, revealed = true)
        }
    }

    Variants(
        title = "States",
        description = "Revealing changes what is drawn and never what is measured.",
        layout = VariantLayout.Grid,
    ) {
        variant("Concealed") {
            SensitiveText(text = "sk-live-8f2b91c4", variant = TextVariant.BodyMedium)
        }
        variant("Revealed") {
            SensitiveText(
                text = "sk-live-8f2b91c4",
                variant = TextVariant.BodyMedium,
                revealed = true,
            )
        }
        variant("Tap to reveal", detail = "onRevealedChange") {
            var revealed by remember { mutableStateOf(false) }

            SensitiveText(
                text = "sk-live-8f2b91c4",
                variant = TextVariant.BodyMedium,
                revealed = revealed,
                onRevealedChange = { revealed = it },
            )
        }
    }

    Recipes {
        recipe(
            name = "Account row",
            description = "Revealed by a control beside the value rather than by the value itself.",
        ) {
            var revealed by remember { mutableStateOf(false) }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xs),
                ) {
                    Text(
                        text = "Current account",
                        variant = TextVariant.LabelMedium,
                        color = TextColor.Subtle,
                    )
                    SensitiveText(
                        text = "€ 12 480.55",
                        variant = TextVariant.HeadingSmall,
                        revealed = revealed,
                    )
                }

                IconButton(
                    imageVector = if (revealed) ArecaIcons.EyeOff else ArecaIcons.EyeShow,
                    contentDescription = if (revealed) "Hide balance" else "Show balance",
                    onClick = { revealed = !revealed },
                    style = IconButtonStyle.soft(ActionColor.Neutral),
                    size = IconButtonSize.Small,
                )
            }
        }

        recipe(
            name = "Recovery phrase",
            description = "Several lines under one veil, each following its own width.",
        ) {
            var revealed by remember { mutableStateOf(false) }

            SensitiveText(
                text = "ridge patrol lumber cabin spring " +
                    "orbit candle harvest mellow tunnel garden onion",
                variant = TextVariant.BodyMedium,
                revealed = revealed,
                onRevealedChange = { revealed = it },
                modifier = Modifier.widthIn(max = 260.dp),
            )
        }
    }

    EdgeCases {
        case(
            name = "Wrapped paragraph",
            expectation = "One veil per line, the last one short — the block still reads as text.",
        ) {
            SensitiveText(
                text = "Patient reports intermittent symptoms over the past three weeks " +
                    "with no change after the dose was adjusted.",
                variant = TextVariant.BodySmall,
                modifier = Modifier.widthIn(max = 280.dp),
            )
        }
        case(
            name = "Truncated value",
            expectation = "Covers exactly what is laid out, ellipsis included.",
        ) {
            SensitiveText(
                text = "sk-live-8f2b91c4e7d3a05b66f1c284d9e7b3a512",
                variant = TextVariant.BodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.widthIn(max = 160.dp),
            )
        }
        case(
            name = "Animation disabled",
            expectation = "No shimmer and no sparks; revealing switches rather than crossfades.",
        ) {
            var revealed by remember { mutableStateOf(false) }

            SensitiveText(
                text = "NL91 ABNA 0417 1643 00",
                variant = TextVariant.BodyMedium,
                revealed = revealed,
                onRevealedChange = { revealed = it },
                animated = false,
            )
        }
        case(
            name = "Empty value",
            expectation = "Nothing to veil, and nothing drawn that would suggest otherwise.",
        ) {
            SensitiveText(text = "", variant = TextVariant.BodyMedium)
        }
    }
}
