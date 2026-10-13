package dev.voir.areca.foundation.feedback

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.Dp
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.icon.BundledIcons
import dev.voir.areca.icon.Check
import dev.voir.areca.icon.Cross

/**
 * Where a value stands against one [Requirement].
 *
 * Three states rather than two, because "not met" means two different things over the life of a
 * form. Before anybody has tried to submit, an unsatisfied rule is guidance and should stay quiet;
 * after, it is the reason the form was refused and should say so. The caller decides when one
 * becomes the other — normally the first submit — and the list draws whichever it is told.
 */
public enum class RequirementState {
    /** Not satisfied, and not yet held against the value. Drawn quietly. */
    Pending,

    /** Satisfied by the current value. */
    Met,

    /** Not satisfied, and the value was checked against it. Drawn as a problem. */
    Unmet,
}

/**
 * One rule a value has to meet — "12 or more characters", "a number".
 *
 * The list only draws the rule. Deciding whether it is met is the caller's, and so is the wording.
 *
 * @property text The rule, worded for the reader.
 * @property state Where the current value stands against it.
 */
@Immutable
public data class Requirement(
    public val text: String,
    public val state: RequirementState,
)

/**
 * The rules a value has to meet, each marked with whether it does.
 *
 * For the constraints a person can satisfy while typing — a new password's length and character
 * classes, a username's alphabet, a file's size and type — shown under the field so they are
 * met one at a time rather than discovered one error at a time.
 *
 * A pending rule carries a hollow ring, a met one a tick and an unmet one a cross, so the state is
 * never carried by color alone. Each rule is one node for assistive technology, read as its text
 * followed by its state from [labels] — "A number, met".
 *
 * ```kotlin
 * // Quiet until the first submit, then every unmet rule turns red.
 * val unmet = if (triedToSubmit) RequirementState.Unmet else RequirementState.Pending
 *
 * RequirementList(
 *     requirements = listOf(
 *         Requirement("12 or more characters", if (password.length >= 12) RequirementState.Met else unmet),
 *         Requirement("A number", if (password.any(Char::isDigit)) RequirementState.Met else unmet),
 *     ),
 *     columns = 2,
 * )
 * ```
 *
 * @param requirements Rules to draw, in reading order.
 * @param modifier Modifier applied to the list.
 * @param columns How many rules share a row, at least one. Rules fill each row from its start edge
 *   before starting the next; two suits short rules under a phone-width field.
 * @param labels How each state is read aloud after the rule's text.
 * @param contentDescription What the list is, read before its rules — "Password requirements" — or
 *   `null` to read the rules alone.
 * @param style Mark and text colors for each state. Defaults to the theme's.
 * @param size Type and spacing. Defaults to the theme's.
 * @throws IllegalArgumentException When [columns] is less than one.
 */
@Composable
public fun RequirementList(
    requirements: List<Requirement>,
    modifier: Modifier = Modifier,
    columns: Int = 1,
    labels: RequirementListLabels = RequirementListLabels.English,
    contentDescription: String? = null,
    style: RequirementListStyle = LocalRequirementListStyle.current ?: RequirementListStyle.Default,
    size: RequirementListSize = LocalRequirementListSize.current ?: RequirementListSize.Default,
) {
    require(columns > 0) { "A requirement list needs at least one column, not $columns." }

    Column(
        modifier = modifier.then(
            if (contentDescription != null) {
                Modifier.semantics { this.contentDescription = contentDescription }
            } else {
                Modifier
            },
        ),
        verticalArrangement = Arrangement.spacedBy(size.rowGap),
    ) {
        for (row in requirements.chunked(columns)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(size.columnGap),
            ) {
                for (requirement in row) {
                    RequirementRow(
                        requirement = requirement,
                        stateDescription = labels.describe(requirement.state),
                        style = style,
                        size = size,
                        modifier = Modifier.weight(1f),
                    )
                }
                // A short last row keeps its rules in the columns above rather than spreading them.
                repeat(columns - row.size) { Spacer(Modifier.weight(1f)) }
            }
        }
    }
}

/**
 * One rule and its mark.
 *
 * @param requirement Rule to draw.
 * @param stateDescription How its state is read aloud.
 * @param style Colors.
 * @param size Metrics.
 * @param modifier Modifier carrying the rule's share of the row.
 */
@Composable
private fun RequirementRow(
    requirement: Requirement,
    stateDescription: String,
    style: RequirementListStyle,
    size: RequirementListSize,
    modifier: Modifier = Modifier,
) {
    val markColor by animateColorAsState(style.markFor(requirement.state), label = "requirementMark")
    val textColor by animateColorAsState(style.textFor(requirement.state), label = "requirementText")

    Row(
        modifier = modifier.semantics(mergeDescendants = true) {
            this.stateDescription = stateDescription
        },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Crossfade(targetState = requirement.state, label = "requirementMarkShape") { state ->
            Box(modifier = Modifier.size(size.mark), contentAlignment = Alignment.Center) {
                when (state) {
                    RequirementState.Pending -> PendingMark(color = markColor, size = size.mark)
                    RequirementState.Met -> Icon(
                        imageVector = BundledIcons.Check,
                        contentDescription = null,
                        tint = markColor,
                        size = size.mark,
                    )
                    RequirementState.Unmet -> Icon(
                        imageVector = BundledIcons.Cross,
                        contentDescription = null,
                        tint = markColor,
                        size = size.mark,
                    )
                }
            }
        }

        Spacer(Modifier.width(size.markGap))

        Text(text = requirement.text, style = size.textStyle, color = textColor)
    }
}

/**
 * The hollow ring beside a rule that is not yet held against anything.
 *
 * Drawn rather than taken from the icon set: it has to sit at exactly the weight of the tick and
 * cross it is replaced by, and read as "not yet" rather than as a radio button.
 *
 * @param color Stroke color.
 * @param size Box the ring is centred in.
 */
@Composable
private fun PendingMark(color: Color, size: Dp) {
    Canvas(modifier = Modifier.size(size)) {
        val stroke = size.toPx() * PendingStrokeFraction
        drawCircle(
            color = color,
            radius = this.size.minDimension * PendingRadiusFraction - stroke / 2f,
            style = Stroke(width = stroke),
        )
    }
}

/** Stroke of the pending ring, as a share of the mark's size. Matches the icon set's weight. */
private const val PendingStrokeFraction = 1.5f / 14f

/** Outer radius of the pending ring, as a share of the mark's size — a third of the box. */
private const val PendingRadiusFraction = 1f / 3f
