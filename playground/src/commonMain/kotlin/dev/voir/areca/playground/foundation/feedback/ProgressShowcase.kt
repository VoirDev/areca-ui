package dev.voir.areca.playground.foundation.feedback

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.badge.IndicatorDot
import dev.voir.areca.foundation.badge.IndicatorDotSize
import dev.voir.areca.foundation.feedback.ProgressBarSize
import dev.voir.areca.foundation.feedback.ProgressBar
import dev.voir.areca.foundation.feedback.SpinnerStyle
import dev.voir.areca.foundation.layout.RelativePosition
import dev.voir.areca.foundation.feedback.Spinner
import dev.voir.areca.foundation.feedback.SpinnerSize
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

/**
 * Showcase for [Spinner], [ProgressBar] and [IndicatorDot].
 *
 * One page: all three answer "something is happening, or something is in a state" without taking a
 * layout of their own.
 */
@Composable
fun ProgressShowcase() = ShowcaseScaffold(
    title = "Progress",
    summary = "Indeterminate spinner, determinate bar and the small state dot.",
    module = "foundation",
) {
    Playground {
        val progress by numberControl(
            key = "progress",
            default = 0.4f,
            range = 0f..1f,
            step = 0.05f,
            format = { "${(it * 100).toInt()}%" },
        )
        val label by textControl("label", default = "Uploading", placeholder = "Leave empty for none")
        val position by enumControl(
            key = "labelPosition",
            default = RelativePosition.BottomCenter,
            options = RelativePosition.entries,
            optionLabel = { it.name },
        )

        preview {
            ProgressBar(
                progress = progress,
                label = label.takeIf { it.isNotEmpty() },
                labelPosition = position,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }

    Variants(
        title = "Spinner sizes",
        description = "Size and stroke are one token, so the ring stays proportional.",
        layout = VariantLayout.Grid,
    ) {
        variant("Small", detail = "16dp / 2dp stroke") {
            Spinner(size = SpinnerSize(container = 16.dp, stroke = 2.dp))
        }
        variant("Default", detail = "22dp / 3dp stroke") {
            Spinner()
        }
        variant("Large", detail = "40dp / 4dp stroke") {
            Spinner(size = SpinnerSize(container = 40.dp, stroke = 4.dp))
        }
        variant("Tinted") {
            Spinner(style = SpinnerStyle(color = ArecaTheme.colors.fgDanger))
        }
    }

    Variants(
        title = "Progress values",
        description = "The bar is determinate: it shows exactly the fraction it is given.",
        layout = VariantLayout.Vertical,
    ) {
        for (value in listOf(0f, 0.25f, 0.5f, 0.75f, 1f)) {
            variant(name = "${(value * 100).toInt()}%") {
                ProgressBar(progress = value, modifier = Modifier.fillMaxWidth())
            }
        }
    }

    Variants(
        title = "Label positions",
        description = "Where the label sits relative to the bar.",
        layout = VariantLayout.Vertical,
    ) {
        for (position in RelativePosition.entries) {
            variant(name = position.name) {
                ProgressBar(
                    progress = 0.6f,
                    label = "60%",
                    labelPosition = position,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }

    Variants(
        title = "Indicator dot",
        description = "A single coloured dot. Meaning comes entirely from the colour it is given.",
        layout = VariantLayout.Grid,
    ) {
        variant("Success") { IndicatorDot(color = ArecaTheme.colors.bgSuccessBase) }
        variant("Warning") { IndicatorDot(color = ArecaTheme.colors.bgWarnBase) }
        variant("Danger") { IndicatorDot(color = ArecaTheme.colors.bgDangerBase) }
        variant("Neutral") { IndicatorDot(color = ArecaTheme.colors.bgNeutralIntense) }
        variant("Large", detail = "12dp") {
            IndicatorDot(color = ArecaTheme.colors.bgSuccessBase, size = IndicatorDotSize.Large)
        }
    }

    Recipes {
        recipe(
            name = "Inline loading row",
            description = "Spinner beside the copy explaining what is loading.",
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spinner(size = SpinnerSize(container = 16.dp, stroke = 2.dp))
                Text(text = "Checking availability", variant = TextVariant.BodySmall, color = TextColor.Subtle)
            }
        }
        recipe(
            name = "Upload progress",
            description = "Determinate bar with the file name above and the percentage below.",
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
            ) {
                Text(text = "quarterly-report.pdf", variant = TextVariant.BodyMedium)
                ProgressBar(
                    progress = 0.72f,
                    label = "72% — 2.1 MB of 2.9 MB",
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
        recipe(
            name = "Status row",
            description = "Indicator dot carrying service state next to its name.",
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg)) {
                StatusRow("API", ArecaTheme.colors.bgSuccessBase, "Operational")
                StatusRow("Webhooks", ArecaTheme.colors.bgWarnBase, "Degraded")
                StatusRow("Exports", ArecaTheme.colors.bgDangerBase, "Outage")
            }
        }
    }

    EdgeCases {
        case(
            name = "Zero and full",
            expectation = "Zero shows only the track; full fills it exactly, with no overshoot.",
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
            ) {
                ProgressBar(progress = 0f, modifier = Modifier.fillMaxWidth())
                ProgressBar(progress = 1f, modifier = Modifier.fillMaxWidth())
            }
        }
        case(
            name = "Very tall bar",
            expectation = "Thickness comes from the size token; the corner shape scales with it.",
        ) {
            ProgressBar(
                progress = 0.5f,
                size = ProgressBarSize.Large,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun StatusRow(name: String, color: Color, state: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IndicatorDot(color = color)
        Text(text = name, variant = TextVariant.BodyMedium)
        Text(text = state, variant = TextVariant.BodySmall, color = TextColor.Muted)
    }
}
