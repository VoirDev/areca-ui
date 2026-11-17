package dev.voir.areca.playground

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.layout.Card
import dev.voir.areca.foundation.layout.CardFooterStyle
import dev.voir.areca.foundation.text.Heading
import dev.voir.areca.foundation.text.Text

// The "Subscription card" recipe from skills/areca-ui-v1/reference/recipes.md, compiled so the
// recipe cannot drift from the API. Keep the two in step.

/** Where a subscription stands, which decides its footer band. */
internal enum class SubscriptionState { Active, DueSoon, Overdue, Renewed }

/**
 * What the card shows about one subscription.
 *
 * @property name The plan's name.
 * @property priceLine Its price and period.
 * @property state Where it stands.
 * @property statusLine The band's one line: a date or an amount.
 * @property openLabel What opening the card does, for assistive technology.
 * @property payNowLabel The overdue band's action.
 */
internal data class PlanUi(
    val name: String,
    val priceLine: String,
    val state: SubscriptionState,
    val statusLine: String,
    val openLabel: String,
    val payNowLabel: String,
)

/**
 * One subscription: what it is in the body, its state as a footer band.
 *
 * @param plan What to show.
 * @param onOpen Opens the subscription.
 * @param onPayNow Pays an overdue subscription.
 * @param statusIcon The band's glyph for each state.
 * @param modifier Modifier applied to the card.
 */
@Composable
internal fun SubscriptionCard(
    plan: PlanUi,
    onOpen: () -> Unit,
    onPayNow: () -> Unit,
    statusIcon: (SubscriptionState) -> ImageVector,
    modifier: Modifier = Modifier,
) {
    val footerStyle = when (plan.state) {
        SubscriptionState.Active -> CardFooterStyle.plain()
        SubscriptionState.DueSoon -> CardFooterStyle.soft(ActionColor.Warning)
        SubscriptionState.Overdue -> CardFooterStyle.soft(ActionColor.Danger)
        SubscriptionState.Renewed -> CardFooterStyle.soft(ActionColor.Success)
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = onOpen,
        onClickLabel = plan.openLabel,
        footerStyle = footerStyle,
        footer = {
            Icon(statusIcon(plan.state), contentDescription = null)
            Text(plan.statusLine, modifier = Modifier.weight(1f), maxLines = 1)
            if (plan.state == SubscriptionState.Overdue) {
                Button(text = plan.payNowLabel, onClick = onPayNow)
            }
        },
    ) { padding ->
        Heading(
            title = { Text(plan.name) },
            supportive = { Text(plan.priceLine) },
            modifier = Modifier.padding(padding),
        )
    }
}
