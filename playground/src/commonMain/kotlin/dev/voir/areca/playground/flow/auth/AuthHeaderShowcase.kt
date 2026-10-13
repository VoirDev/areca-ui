package dev.voir.areca.playground.flow.auth

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.flow.auth.AuthBranding
import dev.voir.areca.flow.auth.AuthLayout
import dev.voir.areca.flow.auth.part.AuthHeader
import dev.voir.areca.flow.auth.part.AuthScaffold
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.Check
import dev.voir.areca.icons.Clock
import dev.voir.areca.icons.Mail
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.Anatomy
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants

/** The alignments the header's `textAlign` is offered at. */
private enum class HeaderAlignment(
    /** Label of the option in the control panel. */
    val label: String,
    /** Alignment handed to the header. */
    val align: TextAlign,
) {
    /** Both lines against the leading edge. */
    Start("Start", TextAlign.Start),

    /** Both lines centred, the default. */
    Center("Center", TextAlign.Center),

    /** Both lines against the trailing edge. */
    End("End", TextAlign.End),
}

/** The sample sign-in screen's title. */
private const val SAMPLE_TITLE = "Welcome back"

/** The sample sign-in screen's subtitle. */
private const val SAMPLE_SUBTITLE = "Sign in to pick up where you left off."

/** Height of the artwork a cell draws the header over; enough for both lines and the scaffold's gaps. */
private val ArtworkHeight: Dp = 200.dp

/** Showcase for [AuthHeader]. */
@Composable
fun AuthHeaderShowcase() = ShowcaseScaffold(
    title = "Auth header",
    summary = "The title and one-line subtitle that open an authentication screen, with the title " +
        "marked as the screen's heading and an optional mark above it.",
    module = "flow-auth",
    status = ShowcaseStatus.Beta,
) {
    Playground(description = "Change the words and their alignment; clear the subtitle to pass null.") {
        val title by textControl("title", default = SAMPLE_TITLE)
        val subtitle by textControl(
            key = "subtitle",
            default = SAMPLE_SUBTITLE,
            description = "Leave empty to pass null: the title stands alone, with no gap under it.",
        )
        val alignment by enumControl(
            key = "textAlign",
            default = HeaderAlignment.Center,
            options = HeaderAlignment.entries,
            optionLabel = { it.label },
        )
        val mark by booleanControl(
            key = "icon",
            default = false,
            description = "A FeaturedIcon in the icon slot; it follows textAlign.",
        )

        preview {
            AuthHeader(
                title = title,
                subtitle = subtitle.ifEmpty { null },
                textAlign = alignment.align,
                icon = if (mark) ({ AuthSamples.Mark(ArecaIcons.Mail, ActionColor.Brand) }) else null,
            )
        }
    }

    Anatomy(description = "A heading and an optional line under it, both spanning the width.") {
        slot(
            name = "title",
            description = "Carries the heading role, so a screen reader can jump to it on arrival.",
        ) {
            AuthHeader(title = SAMPLE_TITLE)
        }
        slot(
            name = "subtitle",
            description = "One quieter line saying what the screen is for; drawn only when given.",
        ) {
            AuthHeader(title = SAMPLE_TITLE, subtitle = SAMPLE_SUBTITLE)
        }
        slot(
            name = "icon",
            description = "A mark above the title saying what kind of screen this is; decorative, drawn only when given.",
        ) {
            AuthHeader(
                title = "Check your email",
                subtitle = "Enter the 6-digit code we sent to ada@example.com.",
                icon = { AuthSamples.Mark(ArecaIcons.Mail, ActionColor.Brand) },
            )
        }
    }

    Variants(
        title = "Mark",
        description = "The icon slot takes any mark; a soft FeaturedIcon in the screen's tone is the usual one.",
        layout = VariantLayout.Vertical,
    ) {
        variant(name = "Brand", detail = "a step that asks for something") {
            AuthHeader(
                title = "Check your email",
                subtitle = "Enter the 6-digit code we sent to ada@example.com.",
                icon = { AuthSamples.Mark(ArecaIcons.Mail, ActionColor.Brand) },
            )
        }
        variant(name = "Success", detail = "an outcome") {
            AuthHeader(
                title = "You're signed in",
                subtitle = "Welcome back.",
                icon = { AuthSamples.Mark(ArecaIcons.Check, ActionColor.Success) },
            )
        }
        variant(name = "Warning, start-aligned", detail = "the mark follows textAlign") {
            AuthHeader(
                title = "Too many attempts",
                subtitle = "For your security, wait a moment before trying again.",
                textAlign = TextAlign.Start,
                icon = { AuthSamples.Mark(ArecaIcons.Clock, ActionColor.Warning) },
            )
        }
    }

    Variants(
        title = "Subtitle",
        description = "A subtitle adds a line and the gap above it; null removes both.",
        layout = VariantLayout.Vertical,
    ) {
        variant(name = "With subtitle", detail = "subtitle = \"$SAMPLE_SUBTITLE\"") {
            AuthHeader(title = SAMPLE_TITLE, subtitle = SAMPLE_SUBTITLE)
        }
        variant(name = "Title only", detail = "subtitle = null") {
            AuthHeader(title = SAMPLE_TITLE)
        }
    }

    Variants(
        title = "Alignment",
        description = "textAlign moves both lines together; the header always spans the width.",
        layout = VariantLayout.Vertical,
    ) {
        for (alignment in HeaderAlignment.entries) {
            variant(name = alignment.label, detail = "textAlign = TextAlign.${alignment.label}") {
                AuthHeader(
                    title = SAMPLE_TITLE,
                    subtitle = SAMPLE_SUBTITLE,
                    textAlign = alignment.align,
                )
            }
        }
    }

    Variants(
        title = "Ground",
        description = "Inside a full-bleed AuthScaffold both lines turn light, with no parameter passed.",
        layout = VariantLayout.Vertical,
    ) {
        variant(name = "Page background", detail = "Base title, subtle subtitle") {
            AuthHeader(title = SAMPLE_TITLE, subtitle = SAMPLE_SUBTITLE)
        }
        variant(name = "Over artwork", detail = "AuthLayout.FullBleed: both lines OnContrast") {
            AuthScaffold(
                modifier = Modifier.fillMaxWidth().height(ArtworkHeight),
                branding = AuthBranding(
                    hero = { AuthSamples.Hero() },
                    layout = AuthLayout.FullBleed,
                ),
                windowInsets = WindowInsets(0, 0, 0, 0),
            ) {
                AuthHeader(title = SAMPLE_TITLE, subtitle = SAMPLE_SUBTITLE)
            }
        }
    }

    EdgeCases {
        case(
            name = "Very long title",
            expectation = "Wraps onto as many lines as it needs, still centred; nothing is cut.",
        ) {
            AuthHeader(
                title = "Welcome back to the Analytical Engine Society members' area",
                subtitle = SAMPLE_SUBTITLE,
            )
        }
        case(
            name = "Very long subtitle",
            expectation = "The subtitle wraps under the title rather than ending in an ellipsis.",
        ) {
            AuthHeader(
                title = "Create your account",
                subtitle = "Your workspace, your projects and every note you have shared with your " +
                    "team, kept in one place and in sync on every device you sign in on.",
            )
        }
        case(
            name = "Narrow container",
            expectation = "Constrained to 200dp: both lines wrap within it and stay centred.",
        ) {
            AuthHeader(
                title = SAMPLE_TITLE,
                subtitle = SAMPLE_SUBTITLE,
                modifier = Modifier.width(200.dp),
            )
        }
    }
}
