package dev.voir.areca.flow.auth.part

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.voir.areca.foundation.feedback.Requirement
import dev.voir.areca.foundation.feedback.RequirementList
import dev.voir.areca.foundation.feedback.RequirementListLabels
import dev.voir.areca.foundation.feedback.RequirementState

/**
 * The rules a new password has to meet, each ticked off as it is typed.
 *
 * Placed under the password box of a screen that chooses one — registration, a reset, an
 * invitation — so the rules are met one at a time rather than learned from one refusal after
 * another. A foundation `RequirementList`, with the foregrounds flipped over full-bleed artwork.
 *
 * The module decides none of it. Which rules exist, how they are worded, and whether each is met are
 * the application's — they have to be the service's own rules, or the form contradicts the server.
 * Report every unmet rule as [RequirementState.Pending] until the first submit and
 * [RequirementState.Unmet] after, and the list turns from guidance into the reason the form was
 * refused at the moment it becomes one.
 *
 * @param requirements The rules, worded, with where the typed password stands against each.
 * @param modifier Modifier applied to the list.
 * @param columns How many rules share a row. Two suits the usual four short rules at phone width;
 *   pass one when the rules are sentences.
 * @param contentDescription What the list is read as before its rules — "Password requirements" —
 *   or `null` to read the rules alone.
 * @param labels How each state is read aloud after a rule.
 */
@Composable
public fun AuthPasswordRequirements(
    requirements: List<Requirement>,
    modifier: Modifier = Modifier,
    columns: Int = 2,
    contentDescription: String? = null,
    labels: RequirementListLabels = RequirementListLabels.English,
) {
    RequirementList(
        requirements = requirements,
        modifier = modifier.fillMaxWidth(),
        columns = columns,
        labels = labels,
        contentDescription = contentDescription,
        style = AuthOnHero.requirementStyle(LocalAuthOnHero.current),
    )
}
