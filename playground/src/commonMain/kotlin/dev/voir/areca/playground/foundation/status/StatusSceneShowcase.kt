package dev.voir.areca.playground.foundation.status

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.feedback.ProgressBar
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.status.StatusMark
import dev.voir.areca.foundation.status.StatusMarkState
import dev.voir.areca.foundation.status.StatusScene
import dev.voir.areca.foundation.status.StatusSceneActions
import dev.voir.areca.foundation.status.StatusSceneActionsStyle
import dev.voir.areca.icons.AlertTriangle
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.FileText
import dev.voir.areca.icons.Share
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.canvas.DeviceFrame
import dev.voir.areca.playground.showcase.canvas.DeviceSize
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.Recipes
import dev.voir.areca.theme.ArecaTheme

/** Factor the playground's phone is drawn at, so the whole screen fits the preview without scrolling. */
private const val PlaygroundScale = 0.7f

/** Factor each recipe's phone is drawn at. */
private const val CellScale = 0.55f

/** How far the pretend import has got, as a fraction, while it is running. */
private const val ImportProgress = 0.4f

/** The three moments of one piece of work, as the playground offers them. */
private enum class SceneState {
    /** The import is running: no actions, a progress bar and a note. */
    Working,

    /** The import finished: the brand button to leave. */
    Success,

    /** The import stopped: the neutral button to try again. */
    Stopped,
}

/**
 * Showcase for [StatusScene] and [StatusSceneActions].
 *
 * A scene is a whole screen, so every cell draws it inside a [DeviceFrame] — in a variant cell's own
 * width, a scene centred in whatever it is given would say nothing about the screen it is for.
 */
@Composable
fun StatusSceneShowcase() = ShowcaseScaffold(
    title = "Status scene",
    summary = "Screen given over to one piece of work: that it is running, or how it ended.",
    module = "foundation",
    status = ShowcaseStatus.Beta,
) {
    Playground(
        description = "Move the import through its states on one screen; replay to watch it arrive.",
        contentAlignment = Alignment.Center,
    ) {
        val state by enumControl(
            key = "state",
            default = SceneState.Success,
            options = SceneState.entries,
            description = "The mark changes in place; the copy is swapped without a second entrance.",
        )
        val withMark by booleanControl("mark", default = true, description = "Off for a scene with only words.")
        val withFootnote by booleanControl("footnote", default = true)
        val withSecondary by booleanControl(
            key = "secondary",
            default = true,
            description = "StatusSceneActions' plain button under the main one.",
        )
        val loading by booleanControl(
            key = "loading",
            default = false,
            description = "The main button spins and refuses a second press.",
        )
        val stagedEntrance by booleanControl(
            key = "stagedEntrance",
            default = true,
            description = "The copy follows the mark in, a beat later. Off for a restored scene.",
        )
        var replays by remember { mutableIntStateOf(0) }
        actionControl(
            key = "replay",
            buttonLabel = "Replay",
            description = "Draws the scene afresh, entrance and all.",
            onInvoke = { replays++ },
        )

        preview {
            DeviceFrame(size = DeviceSize.Compact, scale = PlaygroundScale) {
                key(replays) {
                    StatusScene(
                        title = when (state) {
                            SceneState.Working -> "Importing transactions"
                            SceneState.Success -> "Import complete"
                            SceneState.Stopped -> "Import stopped"
                        },
                        modifier = Modifier.padding(horizontal = ArecaTheme.spacing.xl2),
                        mark = if (withMark) {
                            {
                                StatusMark(
                                    state = when (state) {
                                        SceneState.Working -> StatusMarkState.Working(ArecaIcons.FileText)
                                        SceneState.Success -> StatusMarkState.Success
                                        SceneState.Stopped -> StatusMarkState.Stopped(ArecaIcons.AlertTriangle)
                                    },
                                    contentDescription = null,
                                )
                            }
                        } else {
                            null
                        },
                        message = when (state) {
                            SceneState.Working -> "1,240 of 3,112 transactions from statement-2026-08.csv."
                            SceneState.Success -> "3,112 transactions were added to 4 wallets."
                            SceneState.Stopped -> "The file ended partway through a row. Nothing was added."
                        },
                        footnote = if (withFootnote && state == SceneState.Success) {
                            "18 rows matched transactions you already had and were skipped."
                        } else {
                            null
                        },
                        actions = when (state) {
                            SceneState.Working -> {
                                {
                                    ProgressBar(
                                        progress = ImportProgress,
                                        modifier = Modifier.fillMaxWidth(),
                                    )
                                }
                            }

                            SceneState.Success -> {
                                {
                                    StatusSceneActions(
                                        primary = "Done",
                                        onPrimary = {},
                                        style = StatusSceneActionsStyle.default(primaryColor = ActionColor.Brand),
                                        loading = loading,
                                        secondary = if (withSecondary) "View wallets" else null,
                                        onSecondary = {},
                                    )
                                }
                            }

                            SceneState.Stopped -> {
                                {
                                    StatusSceneActions(
                                        primary = "Try again",
                                        onPrimary = {},
                                        loading = loading,
                                        secondary = if (withSecondary) "Close" else null,
                                        onSecondary = {},
                                    )
                                }
                            }
                        },
                        note = if (state == SceneState.Working) "Keep the app open until this finishes." else null,
                        stagedEntrance = stagedEntrance,
                    )
                }
            }
        }
    }

    Recipes(description = "The four scenes an import and a backup show, as a product words them.") {
        recipe(
            name = "Importing",
            description = "Working mark with a glyph, the counts as the message, a bar in the actions " +
                "slot and a note to keep the app open.",
        ) {
            DeviceFrame(size = DeviceSize.Compact, scale = CellScale) {
                StatusScene(
                    title = "Importing transactions",
                    modifier = Modifier.padding(horizontal = ArecaTheme.spacing.xl2),
                    mark = {
                        StatusMark(state = StatusMarkState.Working(ArecaIcons.FileText), contentDescription = null)
                    },
                    message = "1,240 of 3,112 transactions from statement-2026-08.csv.",
                    actions = {
                        ProgressBar(progress = ImportProgress, modifier = Modifier.fillMaxWidth())
                    },
                    note = "Keep the app open until this finishes.",
                )
            }
        }
        recipe(
            name = "Import complete",
            description = "Success mark; a brand \"Done\" and a plain way on to the result.",
        ) {
            DeviceFrame(size = DeviceSize.Compact, scale = CellScale) {
                StatusScene(
                    title = "Import complete",
                    modifier = Modifier.padding(horizontal = ArecaTheme.spacing.xl2),
                    mark = { StatusMark(state = StatusMarkState.Success, contentDescription = null) },
                    message = "3,112 transactions were added to 4 wallets.",
                    footnote = "18 rows matched transactions you already had and were skipped.",
                    actions = {
                        StatusSceneActions(
                            primary = "Done",
                            onPrimary = {},
                            style = StatusSceneActionsStyle.default(primaryColor = ActionColor.Brand),
                            secondary = "View wallets",
                            onSecondary = {},
                        )
                    },
                )
            }
        }
        recipe(
            name = "Import stopped",
            description = "Stopped mark; the dark neutral button is the way back from the problem.",
        ) {
            DeviceFrame(size = DeviceSize.Compact, scale = CellScale) {
                StatusScene(
                    title = "Import stopped",
                    modifier = Modifier.padding(horizontal = ArecaTheme.spacing.xl2),
                    mark = {
                        StatusMark(
                            state = StatusMarkState.Stopped(ArecaIcons.AlertTriangle),
                            contentDescription = null,
                        )
                    },
                    message = "The file ended partway through a row. Nothing was added.",
                    actions = {
                        StatusSceneActions(
                            primary = "Try again",
                            onPrimary = {},
                            style = StatusSceneActionsStyle.default(primaryColor = ActionColor.NeutralDark),
                            secondary = "Close",
                            onSecondary = {},
                        )
                    },
                )
            }
        }
        recipe(
            name = "Backup saved",
            description = "Success mark; the plain button carries a leading glyph for what it does.",
        ) {
            DeviceFrame(size = DeviceSize.Compact, scale = CellScale) {
                StatusScene(
                    title = "Backup saved",
                    modifier = Modifier.padding(horizontal = ArecaTheme.spacing.xl2),
                    mark = { StatusMark(state = StatusMarkState.Success, contentDescription = null) },
                    message = "ledger-2026-09-25.csv holds all 1,284 transactions.",
                    footnote = "Restore it any time from Import.",
                    actions = {
                        StatusSceneActions(
                            primary = "Done",
                            onPrimary = {},
                            style = StatusSceneActionsStyle.default(primaryColor = ActionColor.Brand),
                            secondary = "Share file",
                            onSecondary = {},
                            secondaryLeading = { Icon(ArecaIcons.Share, contentDescription = null) },
                        )
                    },
                )
            }
        }
    }

    EdgeCases {
        case(
            name = "Words only",
            expectation = "mark = null: the title is centred on the screen, with nothing reserved above it.",
        ) {
            DeviceFrame(size = DeviceSize.Compact, scale = CellScale) {
                StatusScene(
                    title = "Checking your file",
                    modifier = Modifier.padding(horizontal = ArecaTheme.spacing.xl2),
                    message = "This takes a moment for a large statement.",
                    stagedEntrance = false,
                )
            }
        }
        case(
            name = "Long copy",
            expectation = "Title and message wrap, centred inside textInset; the buttons keep their width.",
        ) {
            DeviceFrame(size = DeviceSize.Compact, scale = CellScale) {
                StatusScene(
                    title = "Some of your transactions couldn't be imported from this statement",
                    modifier = Modifier.padding(horizontal = ArecaTheme.spacing.xl2),
                    mark = {
                        StatusMark(
                            state = StatusMarkState.Stopped(ArecaIcons.AlertTriangle),
                            contentDescription = null,
                        )
                    },
                    message = "2,870 transactions were added. 242 rows used a date format we don't read, " +
                        "so they were left out; the rest of your ledger is unchanged.",
                    actions = {
                        StatusSceneActions(
                            primary = "Review skipped rows",
                            onPrimary = {},
                            secondary = "Close",
                            onSecondary = {},
                        )
                    },
                )
            }
        }
        case(
            name = "Main button in flight",
            expectation = "loading = true: the button spins at its full width and ignores a second press.",
        ) {
            DeviceFrame(size = DeviceSize.Compact, scale = CellScale) {
                StatusScene(
                    title = "Import stopped",
                    modifier = Modifier.padding(horizontal = ArecaTheme.spacing.xl2),
                    mark = {
                        StatusMark(
                            state = StatusMarkState.Stopped(ArecaIcons.AlertTriangle),
                            contentDescription = null,
                        )
                    },
                    message = "The connection dropped. Nothing was added.",
                    actions = {
                        StatusSceneActions(
                            primary = "Try again",
                            onPrimary = {},
                            loading = true,
                            secondary = "Close",
                            onSecondary = {},
                        )
                    },
                    stagedEntrance = false,
                )
            }
        }
    }
}
