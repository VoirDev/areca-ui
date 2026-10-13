package dev.voir.areca.flow.auth.sso

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.voir.areca.flow.auth.AuthProvider
import dev.voir.areca.flow.auth.part.AuthOnHero
import dev.voir.areca.flow.auth.part.LocalAuthOnHero
import dev.voir.areca.foundation.layout.HorizontalDivider
import dev.voir.areca.theme.ArecaTheme

/**
 * How a block of provider buttons is arranged.
 *
 * The choice is about what single sign-on is on this screen, not about taste. [Buttons] makes each
 * provider a considered option and costs a full row of height each; [Icons] makes them one
 * alternative among several and costs one row in total.
 */
@Immutable
public enum class SsoLayout {
    /**
     * Full-width labelled pills, stacked.
     *
     * Reach for this when single sign-on is the primary offer — a landing screen, or a form whose
     * providers matter more than its email box. Past three providers the stack starts to outweigh
     * whatever is above it, and [Icons] is the better answer.
     */
    Buttons,

    /**
     * A centred row of circular marks.
     *
     * Reach for this when single sign-on is the alternative to a form that is the main event. Four
     * providers cost one row here and four rows as [Buttons]. Marks that do not fit the width wrap
     * onto another centred row rather than being squeezed or clipped.
     */
    Icons,
}

/**
 * A block of provider buttons, optionally under a labelled rule.
 *
 * Draws nothing at all when [providers] is empty — not an empty column, not a stray rule. A screen
 * can call this unconditionally and let its configuration decide, which is the difference between
 * one call site and an `if` at every one.
 *
 * Each button reports [onProviderClick] and nothing else. Acquiring an identity token is platform
 * work the module cannot see; see `flow/FLOWS.md` §5.
 *
 * @param providers Providers to offer, in the order they are drawn. Empty draws nothing.
 * @param onProviderClick Called with the provider whose button was pressed.
 * @param modifier Modifier applied to the block.
 * @param layout How the buttons are arranged.
 * @param busyProvider Provider whose exchange is in flight, drawn with a spinner — in place of the
 *   mark as an icon, at the trailing end as a button. `null` when nothing is running.
 * @param dividerText Label in the rule above the buttons — "or continue with" — or `null` for no
 *   rule, for a block that is the only thing on the screen and has nothing to be separated from.
 * @param providerLabel Each provider's label — the text of a labelled button, the accessibility
 *   label of a mark. Defaults to "Continue with" and the provider's name.
 * @param enabled Whether the buttons accept input. Pass `false` while any attempt is in flight, so
 *   a second exchange cannot be started on top of the first.
 */
@Composable
public fun SsoGroup(
    providers: List<AuthProvider>,
    onProviderClick: (AuthProvider) -> Unit,
    modifier: Modifier = Modifier,
    layout: SsoLayout = SsoLayout.Buttons,
    busyProvider: AuthProvider? = null,
    dividerText: String? = null,
    providerLabel: (AuthProvider) -> String = { it.defaultLabel },
    enabled: Boolean = true,
) {
    if (providers.isEmpty()) return

    val onHero = LocalAuthOnHero.current

    Column(modifier = modifier.fillMaxWidth()) {
        if (dividerText != null) {
            HorizontalDivider(
                text = dividerText,
                style = AuthOnHero.dividerStyle(onHero),
            )
            Spacer(Modifier.height(ArecaTheme.spacing.xl2))
        }

        when (layout) {
            SsoLayout.Buttons -> Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
            ) {
                providers.forEach { provider ->
                    SsoButton(
                        provider = provider,
                        onClick = { onProviderClick(provider) },
                        modifier = Modifier.fillMaxWidth(),
                        label = providerLabel(provider),
                        enabled = enabled,
                        loading = busyProvider == provider,
                    )
                }
            }

            SsoLayout.Icons -> FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    space = ArecaTheme.spacing.lg,
                    alignment = Alignment.CenterHorizontally,
                ),
                verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
            ) {
                providers.forEach { provider ->
                    SsoIconButton(
                        provider = provider,
                        onClick = { onProviderClick(provider) },
                        contentDescription = providerLabel(provider),
                        enabled = enabled,
                        loading = busyProvider == provider,
                    )
                }
            }
        }
    }
}
