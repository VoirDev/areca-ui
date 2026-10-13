package dev.voir.areca.foundation

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.theme.ArecaBaseTypography
import dev.voir.areca.theme.ArecaColors

/*
 * The two values every text-bearing component inherits from whatever contains it.
 *
 * They travel together: a container that changes one almost always changes the other — a heading
 * sets a larger style and a stronger color, a toast sets a smaller style and an on-contrast color —
 * so they are declared as a pair rather than each beside the component that happens to read it.
 *
 * ArecaThemeProvider seeds both from the theme. A container overrides them for its own subtree with
 * CompositionLocalProvider; Heading is the reference for that pattern.
 *
 * Both are non-static on purpose. A container may hand down an animating color, and a static local
 * would invalidate its whole subtree on every frame of that animation rather than only the text that
 * reads it.
 */

/**
 * Foreground color for text and icons that do not specify their own.
 *
 * Defaults to the light theme's primary foreground, so a component composes without a theme
 * provider — in a preview or a test — instead of throwing.
 */
public val LocalContentColor: ProvidableCompositionLocal<Color> = compositionLocalOf { ArecaColors.light().fgBase }

/**
 * Text style for [Text] and anything built on it that does not specify its own.
 *
 * Defaults to the raw scale's body size, matching what
 * [dev.voir.areca.theme.ArecaThemeProvider] provides, so text renders at a sane size without one.
 */
public val LocalTextStyle: ProvidableCompositionLocal<TextStyle> = compositionLocalOf { ArecaBaseTypography.from().md }
