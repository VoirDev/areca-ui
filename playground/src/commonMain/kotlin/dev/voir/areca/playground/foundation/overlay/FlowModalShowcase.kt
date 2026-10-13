package dev.voir.areca.playground.foundation.overlay

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.overlay.FlowModal
import dev.voir.areca.foundation.overlay.FlowModalActionBar
import dev.voir.areca.foundation.overlay.FlowModalController
import dev.voir.areca.foundation.overlay.FlowModalSecondaryAction
import dev.voir.areca.foundation.overlay.rememberFlowModal
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.foundation.topbar.TopBar
import dev.voir.areca.foundation.topbar.TopBarSize
import dev.voir.areca.foundation.topbar.TopBarStyle
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.theme.ArecaTheme

/** Showcase for [FlowModal]. */
@Composable
fun FlowModalShowcase() = ShowcaseScaffold(
    title = "Flow modal",
    summary = "Bottom sheet that moves between hidden, partial and expanded heights.",
    module = "foundation",
) {
    Playground(description = "Reconfigure the modal and reopen it; changes apply to the live controller.") {
        val allowPartial by booleanControl(
            key = "allowPartial",
            default = true,
            description = "Whether a half-height resting position exists.",
        )
        val allowExpand by booleanControl("allowExpand", default = true)
        val allowDrag by booleanControl(
            key = "allowDrag",
            default = true,
            description = "Whether gestures can move the sheet.",
        )
        val autoHeight by booleanControl(
            key = "autoHeight",
            default = false,
            description = "Wrap the content instead of using a fixed fraction.",
        )
        val partialMultiplier by numberControl(
            key = "partialMultiplier",
            default = 0.5f,
            range = 0.2f..0.8f,
            step = 0.05f,
            format = { "${(it * 100).toInt()}%" },
        )
        val maxHeightFraction by numberControl(
            key = "maxHeightFraction",
            default = 0.92f,
            range = 0.4f..1f,
            step = 0.04f,
            format = { "${(it * 100).toInt()}%" },
        )

        val controller = rememberFlowModal(
            allowPartial = allowPartial,
            allowExpand = allowExpand,
            allowDrag = allowDrag,
            partialMultiplier = partialMultiplier,
            maxHeightFraction = maxHeightFraction,
            autoHeight = autoHeight,
        )

        actionControl(
            key = "open",
            buttonLabel = if (allowPartial) "Open partial" else "Open",
            onInvoke = { controller.show(partial = allowPartial) },
        )
        actionControl(key = "expand", buttonLabel = "Expand", onInvoke = controller::expand)
        actionControl(key = "collapse", buttonLabel = "Collapse", onInvoke = controller::collapse)
        actionControl(key = "hide", buttonLabel = "Hide", onInvoke = controller::hide)

        preview {
            ControllerReadout(controller)

            FlowModal(
                controller = controller,
                header = { ModalHeader("Order summary", "Configured from the controls above") },
                footer = {
                    ModalFooter(
                        primaryLabel = "Checkout",
                        secondaryLabel = "Close",
                        onPrimary = controller::expand,
                        onSecondary = controller::hide,
                    )
                },
            ) {
                ModalBody(lines = 14)
            }
        }
    }

    Recipes(description = "The configurations product surfaces actually use.") {
        recipe(
            name = "Default flow",
            description = "Opens partial, expands on drag or button. The general-purpose sheet.",
        ) {
            ModalRecipe(
                openLabel = "Open partial",
                controller = rememberFlowModal(maxHeightFraction = 0.92f),
                title = "Order summary",
                subtitle = "allowPartial and allowExpand",
                partial = true,
            )
        }
        recipe(
            name = "Expanded only",
            description = "Hidden or full, nothing between. Dialogs and full-screen flows.",
        ) {
            ModalRecipe(
                openLabel = "Open",
                controller = rememberFlowModal(allowPartial = false, maxHeightFraction = 0.92f),
                title = "Expanded-only modal",
                subtitle = "allowPartial = false",
                partial = false,
            )
        }
        recipe(
            name = "Partial only",
            description = "Compact pickers and confirmations that must not take the screen.",
        ) {
            ModalRecipe(
                openLabel = "Open",
                controller = rememberFlowModal(
                    allowExpand = false,
                    partialMultiplier = 0.45f,
                ),
                title = "Partial-only modal",
                subtitle = "allowExpand = false",
                partial = true,
            )
        }
        recipe(
            name = "Auto height",
            description = "Wraps its content up to a cap. Action sheets, menus, short forms.",
        ) {
            ModalRecipe(
                openLabel = "Open auto-height",
                controller = rememberFlowModal(
                    allowPartial = false,
                    maxHeightFraction = 0.75f,
                    autoHeight = true,
                ),
                title = "Quick actions",
                subtitle = "autoHeight = true",
                partial = false,
                bodyLines = 4,
            )
        }
        recipe(
            name = "Drag disabled",
            description = "Controller-driven only — for flows that must not be dismissed by gesture.",
        ) {
            ModalRecipe(
                openLabel = "Open partial",
                controller = rememberFlowModal(allowDrag = false, maxHeightFraction = 0.85f),
                title = "No-drag modal",
                subtitle = "allowDrag = false",
                partial = true,
            )
        }
    }
}

@Composable
private fun ModalRecipe(
    openLabel: String,
    controller: FlowModalController,
    title: String,
    subtitle: String,
    partial: Boolean,
    bodyLines: Int = 12,
) {
    Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
        Row(horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md)) {
            Button(
                style = ButtonStyle.solid(ActionColor.Brand),
                size = ButtonSize.Small,
                onClick = { controller.show(partial = partial) },
            ) {
                Text(openLabel)
            }
            Button(
                style = ButtonStyle.outlined(ActionColor.Neutral),
                size = ButtonSize.Small,
                onClick = controller::hide,
            ) {
                Text("Hide")
            }
        }

        ControllerReadout(controller)
    }

    FlowModal(
        controller = controller,
        header = { ModalHeader(title, subtitle) },
        footer = {
            ModalFooter(
                primaryLabel = "Done",
                secondaryLabel = null,
                onPrimary = controller::hide,
                onSecondary = null,
            )
        },
    ) {
        ModalBody(lines = bodyLines)
    }
}

@Composable
private fun ControllerReadout(controller: FlowModalController) {
    Text(
        text = "state: ${controller.state}",
        variant = TextVariant.BodySmall,
        color = TextColor.Muted,
    )
}

/**
 * A sheet's header, as a modal [TopBar].
 *
 * @param title What the sheet is.
 * @param subtitle A line of detail under it.
 */
@Composable
private fun ModalHeader(title: String, subtitle: String) {
    TopBar(
        title = title,
        subtitle = subtitle,
        modifier = Modifier.fillMaxWidth(),
        size = TopBarSize.Modal,
        style = TopBarStyle.Modal,
    )
}

/**
 * A sheet's foot, as the shared [FlowModalActionBar].
 *
 * @param primaryLabel Caption of the commit.
 * @param secondaryLabel Caption of the lighter action, or `null` for none.
 * @param onPrimary Called when the commit is pressed.
 * @param onSecondary Called when the lighter action is pressed, or `null` for none.
 */
@Composable
private fun ModalFooter(
    primaryLabel: String,
    secondaryLabel: String?,
    onPrimary: () -> Unit,
    onSecondary: (() -> Unit)?,
) {
    FlowModalActionBar(
        confirmText = primaryLabel,
        onConfirm = onPrimary,
        leading = if (secondaryLabel != null && onSecondary != null) {
            { FlowModalSecondaryAction(text = secondaryLabel, onClick = onSecondary) }
        } else {
            null
        },
    )
}

@Composable
private fun ModalBody(lines: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = ArecaTheme.spacing.xl),
        verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
    ) {
        repeat(lines) { index ->
            Text(text = "Line item ${index + 1}", variant = TextVariant.BodyMedium)
        }
    }
}
