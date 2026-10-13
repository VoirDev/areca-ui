package dev.voir.areca.flow.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable

/**
 * What an authentication screen is made of — the parts only the product can supply.
 *
 * Both visual slots are `@Composable` lambdas rather than image handles. The module has no image
 * loader and should not acquire one: a consumer painting a `painterResource`, a Coil
 * `AsyncImage`, a gradient drawn with `Canvas`, or an animated composition all pass through the
 * same slot, and the module never learns which it was.
 *
 * @property wordmark Brand mark above the heading, or `null` to draw none. Given the width of the
 *   content column and left to size itself vertically.
 * @property hero Artwork placed according to [layout], or `null` to draw none. Given a fixed box
 *   to fill, so it should normally be a `ContentScale.Crop` image or something else that tolerates
 *   any aspect ratio.
 * @property layout Where [hero] sits relative to the form. Has no visible effect when [hero] is
 *   `null`.
 */
@Immutable
public data class AuthBranding(
    public val wordmark: (@Composable () -> Unit)? = null,
    public val hero: (@Composable () -> Unit)? = null,
    public val layout: AuthLayout = AuthLayout.Plain,
)
