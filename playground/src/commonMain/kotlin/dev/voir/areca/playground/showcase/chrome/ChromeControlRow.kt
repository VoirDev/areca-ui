package dev.voir.areca.playground.showcase.chrome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.theme.ArecaTheme

/**
 * One labelled row in the control panel: name and optional hint on the left, the control itself on
 * the right.
 *
 * Every control type renders through this so the panel stays a readable column of aligned rows
 * rather than a pile of differently shaped widgets.
 *
 * @param label Control name.
 * @param modifier Modifier applied to the row.
 * @param description Optional one-line hint under the label.
 * @param control The control widget.
 */
@Composable
fun ChromeControlRow(
    label: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    control: @Composable () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xs2),
        ) {
            ChromeText(text = label, role = ChromeTextRole.Label, maxLines = 1)
            if (description != null) {
                ChromeText(text = description, role = ChromeTextRole.Detail, maxLines = 2)
            }
        }

        Row(
            modifier = Modifier.widthIn(min = 0.dp, max = 320.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            control()
        }
    }
}

/**
 * A control stacked under its label rather than placed beside it.
 *
 * [ChromeControlRow] gives the control whatever width the label leaves, which is right for a
 * switch and wrong for a segmented picker of four: the picker does not shrink, so it clips, and a
 * clipped picker hides the option a reader was looking for. This gives the control the full width
 * of the panel and puts the label above it.
 *
 * @param label Name of the prop.
 * @param modifier Modifier applied to the container.
 * @param description Optional one-line hint under the label.
 * @param control The control, which is given the panel's full width.
 */
@Composable
fun ChromeControlColumn(
    label: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    control: @Composable () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.sm),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xs2)) {
            ChromeText(text = label, role = ChromeTextRole.Label, maxLines = 1)
            if (description != null) {
                ChromeText(text = description, role = ChromeTextRole.Detail, maxLines = 2)
            }
        }

        control()
    }
}
