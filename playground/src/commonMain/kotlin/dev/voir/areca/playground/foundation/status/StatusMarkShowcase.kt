package dev.voir.areca.playground.foundation.status

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.icon.IconSize
import dev.voir.areca.foundation.status.StatusMark
import dev.voir.areca.foundation.status.StatusMarkSize
import dev.voir.areca.foundation.status.StatusMarkState
import dev.voir.areca.icons.AlertTriangle
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.FileCheck
import dev.voir.areca.icons.UploadCloud
import dev.voir.areca.icons.WifiOff
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.playground.showcase.section.States
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.theme.ArecaTheme
import kotlinx.coroutines.delay

/** How long the recipe's pretend import runs before it succeeds, in milliseconds. */
private const val ImportMillis = 2500L

/** The three [StatusMarkState]s, as a flat choice the playground can offer. */
private enum class MarkState {
    /** [StatusMarkState.Working]. */
    Working,

    /** [StatusMarkState.Success]. */
    Success,

    /** [StatusMarkState.Stopped], with a warning glyph. */
    Stopped,
}

/**
 * Showcase for [StatusMark].
 *
 * The playground passes each new state to the same mark rather than drawing a new one, because the
 * transitions between states are the component's point: the disc stays put while its colour, its
 * contents and its halo change.
 */
@Composable
fun StatusMarkShowcase() = ShowcaseScaffold(
    title = "Status mark",
    summary = "Disc that says work is running, then lands as a tick or holds still when it stopped.",
    module = "foundation",
    status = ShowcaseStatus.Beta,
) {
    Playground(
        description = "Switch the state to watch one mark move between them; replay to draw it afresh.",
        contentAlignment = Alignment.Center,
    ) {
        val state by enumControl(
            key = "state",
            default = MarkState.Working,
            options = MarkState.entries,
            description = "Passed to the same mark, so this shows the transition, not a new mark.",
        )
        val withIcon by booleanControl(
            key = "icon",
            default = true,
            label = "working icon",
            description = "Working(icon): a glyph naming the work inside the arc; off for the arc alone.",
        )
        var replays by remember { mutableIntStateOf(0) }
        actionControl(
            key = "replay",
            buttonLabel = "Replay",
            description = "Draws a new mark in the current state, from its entrance.",
            onInvoke = { replays++ },
        )

        preview {
            key(replays) {
                StatusMark(
                    state = state.toState(workingIcon = withIcon),
                    contentDescription = state.name,
                )
            }
        }
    }

    States(layout = VariantLayout.Horizontal) {
        variant("Working", detail = "arc alone") {
            StatusMark(state = StatusMarkState.Working(), contentDescription = "Importing")
        }
        variant("Working(icon)", detail = "a glyph names the work") {
            StatusMark(
                state = StatusMarkState.Working(ArecaIcons.UploadCloud),
                contentDescription = "Uploading",
            )
        }
        variant("Success", detail = "filled disc, tick draws itself") {
            StatusMark(state = StatusMarkState.Success, contentDescription = "Import complete")
        }
        variant("Stopped(icon)", detail = "grey disc, still halo") {
            StatusMark(
                state = StatusMarkState.Stopped(ArecaIcons.AlertTriangle),
                contentDescription = "Import stopped",
            )
        }
    }

    Recipes {
        recipe(
            name = "Work that ends",
            description = "Start an import: the mark works for a few seconds, then lands as a success.",
        ) {
            var running by remember { mutableStateOf(false) }
            var finished by remember { mutableStateOf(false) }

            LaunchedEffect(running) {
                if (running) {
                    delay(ImportMillis)
                    running = false
                    finished = true
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xl3),
            ) {
                StatusMark(
                    state = if (finished) {
                        StatusMarkState.Success
                    } else {
                        StatusMarkState.Working(ArecaIcons.FileCheck)
                    },
                    contentDescription = if (finished) "Import complete" else "Importing",
                )
                Button(
                    text = if (finished) "Import again" else "Start import",
                    onClick = {
                        finished = false
                        running = true
                    },
                    loading = running,
                    enabled = !running,
                    style = ButtonStyle.outlined(ActionColor.Neutral),
                    size = ButtonSize.Small,
                )
            }
        }
    }

    EdgeCases {
        case(
            name = "Stopped with a different glyph",
            expectation = "The glyph says why; the disc and the still halo are the same as any stop.",
        ) {
            StatusMark(
                state = StatusMarkState.Stopped(ArecaIcons.WifiOff),
                contentDescription = "Sync stopped: offline",
            )
        }
        case(
            name = "Smaller disc",
            expectation = "StatusMarkSize.default(disc, spinner, icon) scales the halo with the disc.",
        ) {
            StatusMark(
                state = StatusMarkState.Working(ArecaIcons.UploadCloud),
                contentDescription = "Uploading",
                size = StatusMarkSize.default(
                    disc = ArecaTheme.spacing.xl6,
                    spinner = ArecaTheme.spacing.xl5,
                    icon = IconSize.Medium,
                ),
            )
        }
    }
}

/**
 * The mark state this choice stands for.
 *
 * @param workingIcon Whether a working mark carries a glyph inside its arc.
 * @return The state to pass to [StatusMark].
 */
private fun MarkState.toState(workingIcon: Boolean): StatusMarkState = when (this) {
    MarkState.Working -> StatusMarkState.Working(if (workingIcon) ArecaIcons.FileCheck else null)
    MarkState.Success -> StatusMarkState.Success
    MarkState.Stopped -> StatusMarkState.Stopped(ArecaIcons.AlertTriangle)
}
