package dev.voir.areca.playground.foundation.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.voir.areca.foundation.layout.Card
import dev.voir.areca.foundation.layout.CardStyle
import dev.voir.areca.foundation.icon.Chevron
import dev.voir.areca.foundation.icon.ChevronDirection
import dev.voir.areca.foundation.layout.Expandable
import dev.voir.areca.foundation.layout.HorizontalDivider
import dev.voir.areca.foundation.list.ListItem
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.foundation.layout.rememberExpandableState
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.Anatomy
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.theme.ArecaTheme

/** Showcase for [Expandable]. */
@Composable
fun ExpandableShowcase() = ShowcaseScaffold(
    title = "Expandable",
    summary = "Reveals and hides content under a trigger the caller draws.",
    module = "foundation",
) {
    Playground(description = "The trigger is a slot: the component owns the animation, not the look.") {
        val startExpanded by booleanControl("initiallyExpanded", default = false)

        preview {
            val state = rememberExpandableState(initialExpanded = startExpanded)

            Column(modifier = Modifier.fillMaxWidth()) {
                Expandable(
                    state = state,
                    trigger = { expanded, onToggle ->
                        ListItem(
                            title = { Text("Shipping details") },
                            onClick = onToggle,
                            trailing = {
                                Chevron(
                                    direction = if (expanded) {
                                        ChevronDirection.Up
                                    } else {
                                        ChevronDirection.Down
                                    },
                                )
                            },
                        )
                    },
                ) {
                    DetailBlock()
                }
            }
        }
    }

    Anatomy(description = "Two slots: the trigger you draw, and the content it reveals.") {
        slot(
            name = "trigger",
            description = "Given the current state and a toggle. Draw whatever affordance suits.",
        ) {
            val state = rememberExpandableState()
            Expandable(
                state = state,
                trigger = { expanded, onToggle ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Chevron(
                            direction = if (expanded) ChevronDirection.Down else ChevronDirection.Right,
                        )
                        Text(
                            text = if (expanded) "Hide details" else "Show details",
                            variant = TextVariant.LabelMedium,
                            color = TextColor.Brand,
                            modifier = Modifier.padding(vertical = ArecaTheme.spacing.md),
                        )
                    }
                },
            ) {
                DetailBlock()
            }
        }
        slot(
            name = "content",
            description = "Animates in and out. Height is measured, so it can be any size.",
        ) {
            val state = rememberExpandableState(initialExpanded = true)
            Expandable(
                state = state,
                trigger = { _, onToggle ->
                    Text(
                        text = "Toggle",
                        variant = TextVariant.LabelMedium,
                        color = TextColor.Brand,
                        modifier = Modifier.padding(vertical = ArecaTheme.spacing.md),
                    )
                    HorizontalDivider()
                },
            ) {
                DetailBlock()
            }
        }
    }

    Recipes {
        recipe(
            name = "FAQ list",
            description = "Several independent expandables, each owning its own state.",
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                listOf(
                    "How do refunds work?" to "Refunds return to the original payment method within five working days.",
                    "Can I change my plan?" to "Yes, at any time. Changes are prorated to the day.",
                    "Where is my data stored?" to "In the region you chose when the workspace was created.",
                ).forEach { (question, answer) ->
                    val state = rememberExpandableState()
                    Expandable(
                        state = state,
                        trigger = { expanded, onToggle ->
                            ListItem(
                                title = { Text(question) },
                                onClick = onToggle,
                                trailing = {
                                    Chevron(
                                        direction = if (expanded) {
                                            ChevronDirection.Up
                                        } else {
                                            ChevronDirection.Down
                                        },
                                    )
                                },
                            )
                        },
                    ) {
                        Text(
                            text = answer,
                            variant = TextVariant.BodySmall,
                            color = TextColor.Subtle,
                            modifier = Modifier.fillMaxWidth().padding(ArecaTheme.spacing.lg),
                        )
                    }
                    HorizontalDivider()
                }
            }
        }
        recipe(
            name = "Inside a card",
            description = "Card owns the surface; Expandable owns only the reveal.",
        ) {
            val state = rememberExpandableState()
            Card(style = CardStyle.Outlined, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Expandable(
                        state = state,
                        trigger = { expanded, onToggle ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(text = "Advanced options", variant = TextVariant.LabelLarge)
                                Chevron(
                                    direction = if (expanded) {
                                        ChevronDirection.Up
                                    } else {
                                        ChevronDirection.Down
                                    },
                                )
                            }
                        },
                    ) {
                        DetailBlock()
                    }
                }
            }
        }
    }

    EdgeCases {
        case(
            name = "Tall content",
            expectation = "Expands to the full content height; it does not scroll internally.",
        ) {
            val state = rememberExpandableState()
            Expandable(
                state = state,
                trigger = { expanded, onToggle ->
                    ListItem(
                        title = {
                            Text(if (expanded) "Collapse twelve rows" else "Expand twelve rows")
                        },
                        onClick = onToggle,
                    )
                },
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(ArecaTheme.spacing.lg),
                    verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
                ) {
                    repeat(12) { Text(text = "Row ${it + 1}", variant = TextVariant.BodySmall) }
                }
            }
        }
        case(
            name = "Starts expanded",
            expectation = "No opening animation on first composition.",
        ) {
            val state = rememberExpandableState(initialExpanded = true)
            Expandable(
                state = state,
                trigger = { _, onToggle ->
                    ListItem(title = { Text("Already open") }, onClick = onToggle)
                },
            ) {
                DetailBlock()
            }
        }
    }
}

@Composable
private fun DetailBlock() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(ArecaTheme.spacing.lg),
        verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
    ) {
        Text(text = "Carrier: Areca Express", variant = TextVariant.BodySmall, color = TextColor.Subtle)
        Text(text = "Estimated: 2–4 working days", variant = TextVariant.BodySmall, color = TextColor.Subtle)
        Text(text = "Tracking: AE-4821-QX", variant = TextVariant.BodySmall, color = TextColor.Subtle)
    }
}
