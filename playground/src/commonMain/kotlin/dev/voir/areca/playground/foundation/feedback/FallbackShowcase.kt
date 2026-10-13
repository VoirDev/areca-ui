package dev.voir.areca.playground.foundation.feedback

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.feedback.Fallback
import dev.voir.areca.foundation.icon.FeaturedIcon
import dev.voir.areca.foundation.icon.FeaturedIconStyle
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.icons.AlertTriangle
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.Inbox
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.Anatomy
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.theme.ArecaTheme

/** Showcase for [Fallback]. */
@Composable
fun FallbackShowcase() = ShowcaseScaffold(
    title = "Fallback",
    summary = "Centred block for the moments a screen has nothing to show — empty, error or offline.",
    module = "foundation",
) {
    Playground {
        val heading by textControl("heading", default = "Could not load invoices")
        val body by textControl(
            key = "body",
            default = "Check your connection and try again.",
        )
        val withTop by booleanControl("top", default = true, label = "top slot")
        val withAction by booleanControl("content", default = true, label = "content slot")

        preview {
            Fallback(
                modifier = Modifier.fillMaxWidth(),
                top = if (withTop) {
                    {
                        FeaturedIcon(
                            imageVector = ArecaIcons.AlertTriangle,
                            contentDescription = null,
                            style = FeaturedIconStyle.soft(ActionColor.Danger),
                        )
                    }
                } else {
                    null
                },
                heading = { Text(heading) },
                body = { Text(body) },
                content = if (withAction) {
                    {
                        Button(
                            style = ButtonStyle.solid(ActionColor.Brand),
                            size = ButtonSize.Small,
                            onClick = {},
                        ) {
                            Text("Try again")
                        }
                    }
                } else {
                    null
                },
            )
        }
    }

    Anatomy(description = "Four stacked slots, all optional. Order is fixed.") {
        slot(name = "top", description = "A visual anchor — usually a featured icon.") {
            Fallback(
                modifier = Modifier.fillMaxWidth(),
                top = {
                    FeaturedIcon(
                        imageVector = ArecaIcons.Inbox,
                        contentDescription = null,
                        style = FeaturedIconStyle.soft(ActionColor.Neutral),
                    )
                },
            )
        }
        slot(name = "heading", description = "One line naming what happened.") {
            Fallback(modifier = Modifier.fillMaxWidth(), heading = { Text("Nothing here yet") })
        }
        slot(name = "body", description = "A sentence saying what to do about it.") {
            Fallback(
                modifier = Modifier.fillMaxWidth(),
                heading = { Text("Nothing here yet") },
                body = { Text("Invoices you create will appear in this list.") },
            )
        }
        slot(name = "content", description = "The recovery action, if there is one.") {
            Fallback(
                modifier = Modifier.fillMaxWidth(),
                heading = { Text("Nothing here yet") },
                content = {
                    Button(
                        style = ButtonStyle.solid(ActionColor.Brand),
                        size = ButtonSize.Small,
                        onClick = {},
                    ) {
                        Text("Create invoice")
                    }
                },
            )
        }
    }

    Recipes {
        recipe(
            name = "Empty state",
            description = "Neutral icon, an invitation rather than an apology.",
        ) {
            Fallback(
                modifier = Modifier.fillMaxWidth(),
                top = {
                    FeaturedIcon(
                        imageVector = ArecaIcons.Inbox,
                        contentDescription = null,
                        style = FeaturedIconStyle.soft(ActionColor.Neutral),
                    )
                },
                heading = { Text("No invoices yet") },
                body = { Text("Invoices you create will appear here.") },
                content = {
                    Button(
                        style = ButtonStyle.solid(ActionColor.Brand),
                        size = ButtonSize.Small,
                        onClick = {},
                    ) {
                        Text("Create invoice")
                    }
                },
            )
        }
        recipe(
            name = "Load failure",
            description = "Danger icon and a retry — the action is the point.",
        ) {
            Fallback(
                modifier = Modifier.fillMaxWidth(),
                top = {
                    FeaturedIcon(
                        imageVector = ArecaIcons.AlertTriangle,
                        contentDescription = null,
                        style = FeaturedIconStyle.soft(ActionColor.Danger),
                    )
                },
                heading = { Text("Could not load invoices") },
                body = { Text("Check your connection and try again.") },
                content = {
                    Row(horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
                        Button(
                            style = ButtonStyle.outlined(ActionColor.Neutral),
                            size = ButtonSize.Small,
                            onClick = {},
                        ) {
                            Text("Go back")
                        }
                        Button(
                            style = ButtonStyle.solid(ActionColor.Brand),
                            size = ButtonSize.Small,
                            onClick = {},
                        ) {
                            Text("Try again")
                        }
                    }
                },
            )
        }
    }

    EdgeCases {
        case(
            name = "Heading only",
            expectation = "Collapses to one line; omitted slots take no vertical space.",
        ) {
            Fallback(modifier = Modifier.fillMaxWidth(), heading = { Text("Nothing to show") })
        }
        case(
            name = "Long body",
            expectation = "Body wraps and stays centred rather than left-aligning.",
        ) {
            Fallback(
                modifier = Modifier.fillMaxWidth(),
                heading = { Text("Sync paused") },
                body = {
                    Text(
                        "We stopped syncing because the workspace exceeded its storage quota. " +
                            "Remove some files or upgrade the plan, then syncing resumes on its own.",
                    )
                },
            )
        }
        case(
            name = "Every slot empty",
            expectation = "Renders an empty centred box rather than failing.",
        ) {
            Fallback(modifier = Modifier.fillMaxWidth())
        }
    }
}
