package dev.voir.areca.playground.foundation.feedback

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.feedback.Toast
import dev.voir.areca.foundation.feedback.ToastAnimation
import dev.voir.areca.foundation.feedback.ToastColor
import dev.voir.areca.foundation.feedback.ToastStyle
import dev.voir.areca.foundation.feedback.ToastHost
import dev.voir.areca.foundation.feedback.rememberToastState
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.Check
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import kotlin.time.Duration.Companion.seconds

/** Host height every example is given, since the host fills its parent. */
private val HostHeight = 160.dp

/** Showcase for [Toast] and [ToastHost]. */
@Composable
fun ToastShowcase() = ShowcaseScaffold(
    title = "Toast",
    summary = "Transient message queued through a host and dismissed on a timer.",
    module = "foundation",
) {
    Playground(description = "Push toasts into the host; they queue and show one at a time.") {
        val label by textControl("label", default = "Changes saved")
        val caption by textControl("caption", default = "", placeholder = "Optional second line")
        val color by enumControl(
            key = "color",
            default = ToastColor.Neutral,
            options = ToastColor.entries,
            optionLabel = { it.name },
        )
        val animation by enumControl(
            key = "animation",
            default = ToastAnimation.Slide,
            options = ToastAnimation.entries,
            optionLabel = { it.name },
        )
        val seconds by numberControl(
            key = "duration",
            default = 2f,
            range = 1f..6f,
            step = 1f,
            format = { "${it.toInt()}s" },
        )

        val state = rememberToastState()

        actionControl(
            key = "show",
            buttonLabel = "Show toast",
            onInvoke = {
                state.show(
                    label = label,
                    caption = caption.takeIf { it.isNotEmpty() },
                    color = color,
                    duration = seconds.toInt().seconds,
                )
            },
        )

        preview {
            Box(modifier = Modifier.fillMaxWidth().height(HostHeight)) {
                ToastHost(state = state, animation = animation)
            }
        }
    }

    Variants(
        title = "Colors",
        description = "Two grounds: the muted neutral and the inverted one.",
        layout = VariantLayout.Vertical,
    ) {
        for (color in ToastColor.entries) {
            variant(name = color.name) {
                Toast(
                    id = 0L,
                    label = "Changes saved",
                    caption = "Your workspace is up to date",
                    style = ToastStyle.default(color),
                )
            }
        }
    }

    Variants(
        title = "Content",
        description = "Label is required; caption and the two slots are not.",
        layout = VariantLayout.Vertical,
    ) {
        variant("Label only") {
            Toast(id = 1L, label = "Changes saved")
        }
        variant("Label and caption") {
            Toast(id = 2L, label = "Changes saved", caption = "Your workspace is up to date")
        }
        variant("With a leading icon") {
            Toast(
                id = 3L,
                label = "Changes saved",
                leading = { Icon(imageVector = ArecaIcons.Check, contentDescription = null) },
            )
        }
    }

    EdgeCases {
        case(
            name = "Queued toasts",
            expectation = "Pressing twice queues the second; it shows after the first expires.",
        ) {
            val state = rememberToastState()

            Box(modifier = Modifier.fillMaxWidth().height(HostHeight)) {
                ToastHost(state = state)
            }
            QueueTrigger(
                onShow = {
                    state.show(label = "First toast", duration = 2.seconds)
                    state.show(label = "Second toast", duration = 2.seconds)
                },
            )
        }
        case(
            name = "No automatic dismissal",
            expectation = "A null duration keeps the toast until something else replaces it.",
        ) {
            val state = rememberToastState()

            Box(modifier = Modifier.fillMaxWidth().height(HostHeight)) {
                ToastHost(state = state)
            }
            QueueTrigger(onShow = { state.show(label = "Stays put", duration = null) })
        }
        case(
            name = "Long caption",
            expectation = "Caption wraps and the toast grows rather than truncating.",
        ) {
            Toast(
                id = 4L,
                label = "Upload failed",
                caption = "The connection dropped partway through. We kept your draft, so you " +
                    "can retry without losing anything.",
            )
        }
    }
}

/** A plain trigger for the queueing examples, which have no control panel of their own. */
@Composable
private fun QueueTrigger(onShow: () -> Unit) {
    Button(
        style = ButtonStyle.solid(ActionColor.Brand),
        size = ButtonSize.Small,
        onClick = onShow,
    ) {
        Text("Show")
    }
}
