package dev.voir.areca.foundation.status

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.millis

/**
 * A whole screen given over to one piece of work: that it is running, or how it ended.
 *
 * A mark — normally a [StatusMark] — then a title, the message under it, and, when the scene is
 * waiting on the reader rather than on the work, what to do next. Everything is centred on the
 * space the scene is given.
 *
 * **The copy arrives a beat after the mark** and rises slightly as it fades in, so the eye is taken
 * to the mark first and then let down to the words. Everything appearing at once is legible and
 * reads as a dialog; staged, it reads as something completing. Pass `stagedEntrance = false` where
 * the scene is not the arrival of anything — a screenshot, or a scene restored after a rotation.
 *
 * The scene does not inset itself from the screen's edges; pad it the way the screen pads its
 * content. [StatusSceneSize.textInset] then narrows the copy further.
 *
 * **StatusScene or [dev.voir.areca.foundation.feedback.Fallback].** A status scene *is* the screen:
 * it fills the space it is given, takes plain strings, stages its entrance, and is for one piece of
 * work running or just ended — an import in progress, a payment that went through, a profile that
 * would not load. A fallback stands *in for part of* a screen whose content is missing — an empty
 * list, a failed section, an offline panel — sizes to its content, takes composable slots, and
 * appears at once. The two line up slot for slot: the scene's [mark], [title], [message] and
 * [actions] are the fallback's `top`, `heading`, `body` and `content`.
 *
 * @param title The headline — what happened, or what is happening.
 * @param modifier Modifier applied to the scene, which fills the space it is given.
 * @param mark The mark above the copy, or `null` for a scene with only words.
 * @param message The line under the title, or `null` for none.
 * @param footnote A quieter line under the message, for something true but secondary.
 * @param actions What to do next — normally [StatusSceneActions] — or `null` for a scene waiting on
 * the work.
 * @param note The last, quietest line, under the actions.
 * @param stagedEntrance Whether the copy follows the mark in rather than appearing with it.
 * @param style Colors of the copy. Defaults to the theme's.
 * @param size Metrics and typography. Defaults to the theme's.
 */
@Composable
public fun StatusScene(
    title: String,
    modifier: Modifier = Modifier,
    mark: (@Composable () -> Unit)? = null,
    message: String? = null,
    footnote: String? = null,
    actions: (@Composable () -> Unit)? = null,
    note: String? = null,
    stagedEntrance: Boolean = true,
    style: StatusSceneStyle = LocalStatusSceneStyle.current ?: StatusSceneStyle.Default,
    size: StatusSceneSize = LocalStatusSceneSize.current ?: StatusSceneSize.Default,
) {
    val motion = ArecaTheme.motion

    // Flipped one frame after the scene first composes, so the entrance runs from its start.
    // A target set during the first composition is a target already reached.
    var shown by remember { mutableStateOf(!stagedEntrance) }
    LaunchedEffect(Unit) { shown = true }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        if (mark != null) {
            mark()
            Spacer(Modifier.height(size.markGap))
        }

        AnimatedVisibility(
            visible = shown,
            enter = fadeIn(
                animationSpec = tween(durationMillis = motion.slow.millis, delayMillis = motion.medium.millis),
            ) + slideInVertically(
                animationSpec = tween(durationMillis = motion.slow.millis, delayMillis = motion.medium.millis),
                initialOffsetY = { height -> height / 2 },
            ),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = size.textInset),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(size.lineGap),
            ) {
                Text(
                    text = title,
                    modifier = Modifier.semantics { heading() },
                    style = size.titleTextStyle,
                    color = style.title,
                    textAlign = TextAlign.Center,
                )

                if (message != null) {
                    Text(
                        text = message,
                        style = size.messageTextStyle,
                        color = style.message,
                        textAlign = TextAlign.Center,
                    )
                }

                if (footnote != null) {
                    Spacer(Modifier.height(size.footnoteGap))
                    Text(
                        text = footnote,
                        style = size.footnoteTextStyle,
                        color = style.message,
                        textAlign = TextAlign.Center,
                    )
                }

                if (actions != null) {
                    Spacer(Modifier.height(size.actionsGap))
                    actions()
                }

                if (note != null) {
                    Spacer(Modifier.height(size.noteGap))
                    Text(
                        text = note,
                        style = size.noteTextStyle,
                        color = style.note,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

/**
 * A [StatusScene]'s ways on: the one it exists for, and a quieter one under it.
 *
 * The main button is at least [StatusSceneActionsSize.primaryMinWidth] wide, so it reads as the
 * screen's one action rather than as a chip — a short label at its natural width is a control
 * somebody has to aim at — and stops short of the gutters, because a button run edge to edge is a
 * docked bar floated up the page.
 *
 * @param primary Caption of the main button.
 * @param onPrimary Called when the main button is pressed.
 * @param modifier Modifier applied to the pair.
 * @param style Styles of the two buttons. Defaults to the theme's; pass
 *   `StatusSceneActionsStyle.default(primaryColor = ActionColor.Brand)` for a "done" scene.
 * @param size Sizes of the two buttons and the gap between them. Defaults to the theme's.
 * @param loading Whether the main button's work is in flight: it spins and refuses a second press.
 * @param secondary Caption of the plain button under it, or `null` for none.
 * @param onSecondary Called when the plain button is pressed.
 * @param secondaryLeading Glyph before the plain button's caption, or `null` for none.
 */
@Composable
public fun StatusSceneActions(
    primary: String,
    onPrimary: () -> Unit,
    modifier: Modifier = Modifier,
    style: StatusSceneActionsStyle = LocalStatusSceneActionsStyle.current ?: StatusSceneActionsStyle.Default,
    size: StatusSceneActionsSize = LocalStatusSceneActionsSize.current ?: StatusSceneActionsSize.Default,
    loading: Boolean = false,
    secondary: String? = null,
    onSecondary: (() -> Unit)? = null,
    secondaryLeading: (@Composable () -> Unit)? = null,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(size.gap),
    ) {
        Button(
            text = primary,
            onClick = onPrimary,
            style = style.primary,
            size = size.primary,
            loading = loading,
            enabled = !loading,
            modifier = Modifier.widthIn(min = size.primaryMinWidth),
        )

        if (secondary != null && onSecondary != null) {
            Button(
                text = secondary,
                onClick = onSecondary,
                style = style.secondary,
                size = size.secondary,
                leading = secondaryLeading,
            )
        }
    }
}
