package dev.voir.areca.modifier

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.voir.areca.theme.ArecaTheme

/**
 * Appends [modifier] only when [condition] holds.
 *
 * Keeps a chain readable when one link is optional, instead of splitting it around an `if`:
 *
 * ```kotlin
 * Modifier
 *     .fillMaxWidth()
 *     .thenIf(selected) { border(1.dp, ArecaTheme.colors.accentBase, shape) }
 *     .padding(ArecaTheme.spacing.md)
 * ```
 *
 * [modifier] is built on a fresh [Modifier], not on the receiver, so it contributes only its own
 * links and the chain's order is exactly what the call site reads like. It is not invoked at all
 * when [condition] is `false`, so it may safely reference state that is only valid in that branch.
 *
 * @param condition Whether to append [modifier].
 * @param modifier Links to append. Composable, so it may read the theme.
 * @return The receiver, with [modifier] appended when [condition] holds.
 */
@Composable
public fun Modifier.thenIf(
    condition: Boolean,
    modifier: @Composable Modifier.() -> Modifier,
): Modifier = if (condition) then(modifier(Modifier)) else this
