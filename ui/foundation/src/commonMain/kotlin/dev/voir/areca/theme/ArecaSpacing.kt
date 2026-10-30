package dev.voir.areca.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Spacing scale used for padding, gaps, and layout insets.
 *
 * The scale is a single ordered ramp: [xs2] is the tightest step and [xl9] the widest. Steps are not
 * a fixed multiple of each other — the small end is fine-grained because that is where component
 * internals live, and the large end grows quickly because that is where section rhythm lives.
 *
 * The last four tokens are not part of the ramp. They are the page-level gutters every screen shares,
 * kept here so a consumer can widen a whole app's margins in one place. The horizontal gutter widens
 * with the window: [horizontalContentInsets] on a compact one, [mediumHorizontalContentInsets] from
 * 600dp and [expandedHorizontalContentInsets] from 840dp. Apply them with
 * [dev.voir.areca.theme.contentInsets], which picks the step, rather than reading them directly.
 *
 * Every value has a default, so `ArecaSpacing()` is a complete scale and `copy` overrides one step.
 */
@Immutable
public data class ArecaSpacing(
    /** No space. Use where a caller must pass a value but none should be applied. */
    public val none: Dp = 0.dp,
    /** Hairline separation, such as a badge's internal gap. */
    public val xs2: Dp = 2.dp,
    /** Tightest usable gap between two adjacent elements. */
    public val xs: Dp = 4.dp,
    /** Gap inside a dense control, such as an icon and its label. */
    public val sm: Dp = 6.dp,
    /** Default gap between related elements in a row or column. */
    public val md: Dp = 8.dp,
    /** Padding inside a small container, such as a badge or chip. */
    public val lg: Dp = 12.dp,
    /** Padding inside a standard container, and the default horizontal screen gutter. */
    public val xl: Dp = 16.dp,
    /** Padding inside a roomy container. */
    public val xl2: Dp = 20.dp,
    /** Gap between grouped blocks within a section. */
    public val xl3: Dp = 24.dp,
    /** Gap between sections. */
    public val xl4: Dp = 32.dp,
    /** Gap before a major section break. */
    public val xl5: Dp = 40.dp,
    /** Leading space above a page heading. */
    public val xl6: Dp = 48.dp,
    /** Vertical rhythm for sparse, full-height layouts. */
    public val xl7: Dp = 64.dp,
    /** Empty-state and onboarding breathing room. */
    public val xl8: Dp = 80.dp,
    /** Widest step, for hero and splash layouts. */
    public val xl9: Dp = 96.dp,
    /** Horizontal gutter between screen content and the window edge, on a compact window. */
    public val horizontalContentInsets: Dp = 16.dp,
    /** Vertical gutter between screen content and the top and bottom edges. */
    public val verticalContentInsets: Dp = 24.dp,
    /** Horizontal gutter on a medium window, 600dp to 839dp wide. */
    public val mediumHorizontalContentInsets: Dp = 24.dp,
    /** Horizontal gutter on an expanded window and every wider one, from 840dp. */
    public val expandedHorizontalContentInsets: Dp = 32.dp,
)

/** Spacing scale in scope. Read it through [ArecaTheme.spacing]. */
internal val LocalSpacing = staticCompositionLocalOf { ArecaSpacing() }
