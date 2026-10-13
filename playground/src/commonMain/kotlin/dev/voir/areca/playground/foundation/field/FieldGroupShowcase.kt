package dev.voir.areca.playground.foundation.field

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.field.FieldGroup
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.field.FieldLabelPlacement
import dev.voir.areca.foundation.field.FieldMessageBehavior
import dev.voir.areca.foundation.field.FieldMessagePlacement
import dev.voir.areca.foundation.field.FieldStatus
import dev.voir.areca.foundation.field.TextField
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.Anatomy
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

/** Label placements, as a selectable list. [FieldLabelPlacement] is a sealed type. */
private enum class LabelPlacementOption {
    Above,
    Beside,
    Inside,
    InsideLeading,
    None,
}

/** Message behaviours, as a selectable list. */
private enum class MessageBehaviourOption {
    Animated,
    Immediate,
    Reserve,
}

/** Showcase for [FieldGroup]. */
@Composable
fun FieldGroupShowcase() = ShowcaseScaffold(
    title = "Field group",
    summary = "Wraps a control with its label, required marker and validation message.",
    module = "foundation",
) {
    Playground {
        val label by textControl("label", default = "Email address")
        val assistive by textControl("assistive", default = "We only use this for receipts.")
        val error by textControl("error", default = "", placeholder = "Leave empty for no error")
        val required by booleanControl("required", default = false)
        val placement by enumControl(
            key = "labelPlacement",
            default = LabelPlacementOption.Above,
            options = LabelPlacementOption.entries,
            optionLabel = { it.name },
        )
        val messagePlacement by enumControl(
            key = "messagePlacement",
            default = FieldMessagePlacement.Below,
            options = FieldMessagePlacement.entries,
            optionLabel = { it.name },
        )

        preview {
            var value by remember { mutableStateOf("") }

            FieldGroup(
                modifier = Modifier.fillMaxWidth(),
                label = label.takeIf { it.isNotEmpty() },
                required = required,
                assistive = assistive.takeIf { it.isNotEmpty() },
                error = error.takeIf { it.isNotEmpty() },
                labelPlacement = placement.resolve(),
                messagePlacement = messagePlacement,
            ) { field ->
                TextField(
                    value = value,
                    onValueChange = { value = it },
                    field = field,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                )
            }
        }
    }

    Anatomy(description = "Four parts, three of them optional.") {
        slot(name = "label", description = "Names the field. Optional but almost always present.") {
            SampleGroup(label = "Email address")
        }
        slot(
            name = "required",
            description = "Marks the field as mandatory, announced to screen readers.",
        ) {
            SampleGroup(label = "Email address", required = true)
        }
        slot(
            name = "assistive",
            description = "Standing help. Replaced by the error when one appears.",
        ) {
            SampleGroup(label = "Email address", assistive = "We only use this for receipts.")
        }
        slot(
            name = "error",
            description = "Sets the status to Error and takes over the message line.",
        ) {
            SampleGroup(
                label = "Email address",
                assistive = "We only use this for receipts.",
                error = "Enter a valid email address.",
            )
        }
    }

    Variants(
        title = "Label placement",
        description = "Where the label sits relative to the control.",
        layout = VariantLayout.Vertical,
    ) {
        for (option in LabelPlacementOption.entries) {
            variant(name = option.name) {
                SampleGroup(label = "Email address", placement = option.resolve())
            }
        }
    }

    Variants(
        title = "Status",
        description = "Status colours the border and the message together.",
        layout = VariantLayout.Vertical,
    ) {
        for (status in FieldStatus.entries) {
            variant(name = status.name) {
                SampleGroup(
                    label = "Email address",
                    assistive = "Message rendered in the ${status.name} style.",
                    status = status,
                )
            }
        }
    }

    Variants(
        title = "Message behaviour",
        description = "What happens to the layout when a message appears or leaves.",
        layout = VariantLayout.Vertical,
    ) {
        for (option in MessageBehaviourOption.entries) {
            variant(name = option.name, detail = option.detail) {
                var showError by remember { mutableStateOf(false) }
                var value by remember { mutableStateOf("") }

                FieldGroup(
                    modifier = Modifier.fillMaxWidth(),
                    label = "Email address",
                    error = if (showError) "Enter a valid email address." else null,
                    messageBehavior = option.resolve(),
                ) { field ->
                    TextField(
                        value = value,
                        onValueChange = {
                            value = it
                            showError = it.isNotEmpty() && !it.contains('@')
                        },
                        field = field,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        placeholder = { Text("Type without an @") },
                    )
                }
            }
        }
    }

    Recipes {
        recipe(
            name = "Sign-in form",
            description = "Several groups stacked, one required, one with live validation.",
        ) {
            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            val emailError = email.isNotEmpty() && !email.contains('@')

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl),
            ) {
                FieldGroup(
                    modifier = Modifier.fillMaxWidth(),
                    label = "Email address",
                    required = true,
                    error = if (emailError) "Enter a valid email address." else null,
                ) { field ->
                    TextField(
                        value = email,
                        onValueChange = { email = it },
                        field = field,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                    )
                }

                FieldGroup(
                    modifier = Modifier.fillMaxWidth(),
                    label = "Password",
                    required = true,
                    assistive = "At least twelve characters.",
                ) { field ->
                    TextField(
                        value = password,
                        onValueChange = { password = it },
                        field = field,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                    )
                }
            }
        }
    }

    EdgeCases {
        case(
            name = "Error replaces assistive text",
            expectation = "Only one message line shows; the error wins while it is set.",
        ) {
            SampleGroup(
                label = "Email address",
                assistive = "You will not see this while the error is set.",
                error = "Enter a valid email address.",
            )
        }
        case(
            name = "Long message",
            expectation = "Message wraps and the group grows; the control keeps its height.",
        ) {
            SampleGroup(
                label = "Email address",
                error = "This address is already associated with another workspace, so it " +
                    "cannot be used again here. Use a different address or contact support.",
            )
        }
        case(
            name = "No label",
            expectation = "Group collapses to the control and its message, with no reserved gap.",
        ) {
            SampleGroup(label = null, assistive = "Label omitted entirely.")
        }
    }
}

@Composable
private fun SampleGroup(
    label: String?,
    required: Boolean = false,
    assistive: String? = null,
    error: String? = null,
    status: FieldStatus = if (error != null) FieldStatus.Error else FieldStatus.Default,
    placement: FieldLabelPlacement = FieldLabelPlacement.Above,
) {
    var value by remember { mutableStateOf("") }

    FieldGroup(
        modifier = Modifier.fillMaxWidth(),
        label = label,
        required = required,
        assistive = assistive,
        error = error,
        status = status,
        labelPlacement = placement,
    ) { field ->
        TextField(
            value = value,
            onValueChange = { value = it },
            field = field,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
    }
}

private fun LabelPlacementOption.resolve(): FieldLabelPlacement = when (this) {
    LabelPlacementOption.Above -> FieldLabelPlacement.Above
    LabelPlacementOption.Beside -> FieldLabelPlacement.Beside()
    LabelPlacementOption.Inside -> FieldLabelPlacement.Inside
    LabelPlacementOption.InsideLeading -> FieldLabelPlacement.InsideLeading()
    LabelPlacementOption.None -> FieldLabelPlacement.None
}

private val MessageBehaviourOption.detail: String
    get() = when (this) {
        MessageBehaviourOption.Animated -> "message animates in and out"
        MessageBehaviourOption.Immediate -> "message appears instantly, layout jumps"
        MessageBehaviourOption.Reserve -> "space is always reserved, layout never jumps"
    }

private fun MessageBehaviourOption.resolve(): FieldMessageBehavior = when (this) {
    MessageBehaviourOption.Animated -> FieldMessageBehavior.Animated
    MessageBehaviourOption.Immediate -> FieldMessageBehavior.Immediate
    MessageBehaviourOption.Reserve -> FieldMessageBehavior.Reserve(20.dp)
}
