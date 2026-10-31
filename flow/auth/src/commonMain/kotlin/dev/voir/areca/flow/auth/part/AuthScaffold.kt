package dev.voir.areca.flow.auth.part

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import dev.voir.areca.flow.auth.AuthBranding
import dev.voir.areca.flow.auth.AuthLayout
import dev.voir.areca.flow.auth.heroFraction
import dev.voir.areca.flow.auth.resolve
import dev.voir.areca.foundation.layout.BaseLayout
import dev.voir.areca.foundation.topbar.LocalTopBarSize
import dev.voir.areca.foundation.topbar.LocalTopBarStyle
import dev.voir.areca.foundation.topbar.TopBarSize
import dev.voir.areca.foundation.topbar.TopBarStyle
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.window.WidthClass
import dev.voir.areca.window.WindowClassBox

/**
 * Whether the parts below are being drawn over hero artwork rather than over the page background.
 *
 * Read by every part that draws straight onto the background — [AuthHeader], the credential fields,
 * [AuthTextLink], [AuthFooterPrompt] and [LegalConsent] — to pick a foreground that survives on a
 * photograph. It is
 * an implementation detail of [AuthLayout.FullBleed] shared between parts that cannot see each
 * other, which is exactly what a composition local is for — and it is `internal`, so it is not a
 * hook a consumer can reach for and then be surprised by.
 */
internal val LocalAuthOnHero = staticCompositionLocalOf { false }

/**
 * Widths [AuthScaffold] lays its column out by.
 */
public object AuthScaffoldDefaults {
    /**
     * Widest the content column grows on a window wider than compact, gutters included.
     *
     * Narrower than foundation's `AdaptiveDefaults.ReadableWidth` (640dp), which is sized for
     * reading: a sign-in form is a stack of single-line boxes and full-width buttons, and past
     * about 430dp a box only gets emptier and a button stops reading as one. 480dp less the default
     * 24dp gutters leaves a 432dp form — a large phone's, not a page's. It is also under the
     * narrowest window it applies to (600dp), so the cap shows on every one of them, and it is what
     * the form pane of [AuthLayout.Split] at its default fraction has on an 840dp window, so the
     * column is the same width on either side of the split's breakpoint.
     */
    public val MaxWidth: Dp = 480.dp
}

/**
 * The frame every authentication screen is built in.
 *
 * Owns the window insets, the keyboard inset, the scroll container, the horizontal gutter, the
 * placement of the hero and the footer pinned to the bottom edge. A screen passes its form and,
 * when it is not the first screen of the flow, a bar with its way back.
 *
 * **The bar is a foundation [dev.voir.areca.foundation.topbar.TopBar]**, pinned above the
 * scrolling form rather than scrolling with it. The scaffold sets the defaults every bar inside it
 * reads, so a bar written with no size or style of its own lands its back chevron on the form's
 * [gutter] — and, over [AuthLayout.FullBleed] artwork, draws in [TopBarStyle.Scrim] so it reads
 * over the picture instead of covering it with the page colour. Pass a size or style to the bar to
 * override either.
 *
 * The bottom edge is held by an arrangement against a column whose minimum height is the viewport,
 * rather than by a weighted spacer. A weight measures against the parent's height, and inside a
 * vertically scrolling column that height is infinite, so a weighted spacer collapses to nothing.
 * The column has to scroll, because a registration form plus a raised keyboard does not fit on a
 * short phone.
 *
 * The artwork of a split layout is sized against the viewport before the keyboard inset is taken
 * off it, so raising the keyboard scrolls the artwork away rather than shrinking it.
 *
 * The same arrangement is what bottom-aligns [AuthLayout.FullBleed]: the body is placed against
 * the footer, so the group lands against the bottom edge instead of the top.
 *
 * **Larger windows.** The scaffold reads `ArecaTheme.window` — the window, or the container a
 * `WindowClassBox` above it measured. On a compact width, a phone held upright, it draws exactly
 * the phone screen described above. On anything wider, the column — wordmark, body and footer —
 * stops at [maxWidth] and sits centred, the bar's controls move in to line up with it, and, where
 * no band of artwork sits above or below it, the body is centred in the height above the footer.
 * [AuthLayout.Split] puts the artwork in a panel beside the column from an expanded width up.
 * Whatever the window, the body is composed inside a `WindowClassBox`, so a part that adapts to
 * its width sees the column rather than the window.
 *
 * @param modifier Modifier applied to the layout.
 * @param branding Wordmark, hero and the layout that places them.
 * @param gutter Horizontal inset of the content column from the screen edges.
 * @param windowInsets Regions the system occupies, kept clear of the content. Defaults to the real
 *   ones. Worth overriding in two cases: a host that has already consumed them — pass
 *   `WindowInsets(0, 0, 0, 0)` — and a preview, where the real insets are whatever the developer's
 *   window reports rather than whatever a phone would.
 * @param topBar Bar above the form: a back control, a step, a progress rule, a skip. `null` draws
 *   none, which is what the first screen of a flow wants.
 * @param overlay Slot above everything, for a toast host or a scrim. `null` draws none.
 * @param footer Content pinned to the bottom edge, normally the link to the other screen. `null`
 *   leaves the bottom to the content.
 * @param maxWidth Widest the content column grows, [gutter] included, on a window wider than
 *   compact. Ignored on a compact width, where the column is always the screen's. `Dp.Unspecified`
 *   lets it fill the window.
 * @param content The screen body — heading, form and actions.
 */
@Composable
public fun AuthScaffold(
    modifier: Modifier = Modifier,
    branding: AuthBranding = AuthBranding(),
    gutter: Dp = ArecaTheme.spacing.xl3,
    windowInsets: WindowInsets = WindowInsets.systemBars,
    topBar: (@Composable () -> Unit)? = null,
    overlay: (@Composable BoxScope.() -> Unit)? = null,
    footer: (@Composable () -> Unit)? = null,
    maxWidth: Dp = AuthScaffoldDefaults.MaxWidth,
    content: @Composable ColumnScope.() -> Unit,
) {
    val window = ArecaTheme.window
    val wide = window.width > WidthClass.Compact
    // A Split that does not fit this window is already its fallback here.
    val layout = branding.layout.resolve(window)
    val hero = branding.hero
    // FullBleed without artwork is Plain: light foregrounds on the page background would vanish.
    val onHero = hero != null && layout == AuthLayout.FullBleed
    val splitHero = if (layout is AuthLayout.Split) hero else null
    val heroBand = hero != null && (layout is AuthLayout.HeroTop || layout is AuthLayout.HeroBottom)
    val columnMaxWidth = if (wide) maxWidth else Dp.Unspecified
    val arrangement = when {
        onHero -> BodyAgainstFooter
        wide && !heroBand -> BodyCentred
        else -> BodyAtTop
    }

    BaseLayout(
        modifier = modifier,
        bg = ArecaTheme.colors.bgCanvas,
        contentWindowInsets = windowInsets,
        // Full-bleed artwork is the page background, so it is painted below the insets rather than
        // inside them — otherwise it would stop short of the status bar and read as a banner.
        background = {
            if (hero != null && onHero) {
                Box(modifier = Modifier.fillMaxSize()) { hero() }
            }
        },
        overlay = overlay ?: {},
    ) { insets ->
        // The split's panel runs under the bars, edge to edge like full-bleed artwork, so the start
        // inset is the panel's and the pane beside it keeps only the other three.
        val direction = LocalLayoutDirection.current
        val paneInsets = if (splitHero == null) {
            insets
        } else {
            PaddingValues(
                top = insets.calculateTopPadding(),
                end = insets.calculateEndPadding(direction),
                bottom = insets.calculateBottomPadding(),
            )
        }

        // One row whatever the layout, so the form keeps its place — and its scroll position — when
        // a resized window crosses the split's breakpoint.
        Row(modifier = Modifier.fillMaxSize()) {
            if (splitHero != null) {
                HeroPanel(fraction = layout.heroFraction ?: AuthLayout.DefaultHeroFraction, content = splitHero)
            }

            // Consumed as well as applied, so the keyboard inset below is measured past the
            // navigation bar this padding already clears rather than adding its height a second time.
            BoxWithConstraints(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(paneInsets)
                    .consumeWindowInsets(paneInsets),
            ) {
                // The pane's own width: `maxWidth` alone would read the parameter, not the scope.
                val paneWidth = this.maxWidth
                // The bar's controls line up with the column, which sits centred once it stops
                // growing.
                val margin = if (columnMaxWidth.isSpecified) {
                    ((paneWidth - columnMaxWidth) / 2).coerceAtLeast(0.dp)
                } else {
                    0.dp
                }

                Column(modifier = Modifier.fillMaxSize()) {
                    topBar?.let { bar ->
                        AuthTopBarDefaults(inset = gutter + margin, onHero = onHero, content = bar)
                    }

                    AuthColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        branding = branding,
                        layout = layout,
                        onHero = onHero,
                        gutter = gutter,
                        columnMaxWidth = columnMaxWidth,
                        arrangement = arrangement,
                        firstOnScreen = topBar == null,
                        footer = footer,
                        content = content,
                    )
                }
            }
        }
    }
}

/**
 * The scrolling part of the scaffold: the band of artwork a split layout puts above or below, the
 * body and the footer.
 *
 * @param modifier Modifier applied to the area, which is the viewport the column scrolls in.
 * @param branding Wordmark and hero.
 * @param layout Layout already resolved for the window.
 * @param onHero Whether the body and footer are drawn over full-bleed artwork.
 * @param gutter Horizontal inset of the column.
 * @param columnMaxWidth Widest the column grows, or `Dp.Unspecified` for the full width.
 * @param arrangement Where the body sits between the band above and the footer below.
 * @param firstOnScreen Whether nothing sits above the column, so it keeps its own top margin.
 * @param footer Content pinned to the bottom edge, or `null`.
 * @param content The screen body.
 */
@Composable
private fun AuthColumn(
    modifier: Modifier,
    branding: AuthBranding,
    layout: AuthLayout,
    onHero: Boolean,
    gutter: Dp,
    columnMaxWidth: Dp,
    arrangement: Arrangement.Vertical,
    firstOnScreen: Boolean,
    footer: (@Composable () -> Unit)?,
    content: @Composable ColumnScope.() -> Unit,
) {
    val hero = branding.hero

    BoxWithConstraints(modifier = modifier) {
        // Measured outside the keyboard inset, so the artwork keeps its height when the
        // keyboard opens instead of shrinking with what is left of the viewport.
        val heroHeight = layout.heroFraction?.let { maxHeight * it }

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
        ) {
            val visibleHeight = maxHeight
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .heightIn(min = visibleHeight),
                verticalArrangement = arrangement,
            ) {
                // Exactly three children, which the arrangement places: the band above, the body,
                // and the footer with the band below.
                Column(modifier = Modifier.fillMaxWidth()) {
                    if (hero != null && heroHeight != null && layout is AuthLayout.HeroTop) {
                        HeroBox(height = heroHeight, content = hero)
                    }
                }

                CompositionLocalProvider(LocalAuthOnHero provides onHero) {
                    // Reports the column's width to the body, so a part that adapts sees the
                    // column it is in rather than the window around it.
                    WindowClassBox(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .widthIn(max = columnMaxWidth)
                            .fillMaxWidth(),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = gutter),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            // A bar above already holds the top edge away from the status bar.
                            Spacer(Modifier.height(if (firstOnScreen) ArecaTheme.spacing.xl4 else ArecaTheme.spacing.xl))

                            branding.wordmark?.let { wordmark ->
                                wordmark()
                                Spacer(Modifier.height(ArecaTheme.spacing.xl4))
                            }

                            if (hero != null && layout == AuthLayout.HeroInline) {
                                hero()
                                Spacer(Modifier.height(ArecaTheme.spacing.xl3))
                            }

                            content()
                        }
                    }
                }

                Column(modifier = Modifier.fillMaxWidth()) {
                    if (footer != null) {
                        CompositionLocalProvider(LocalAuthOnHero provides onHero) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .widthIn(max = columnMaxWidth)
                                    .fillMaxWidth(),
                            ) {
                                Column(modifier = Modifier.padding(horizontal = gutter)) {
                                    Spacer(Modifier.height(ArecaTheme.spacing.xl4))
                                    footer()
                                    Spacer(Modifier.height(ArecaTheme.spacing.xl3))
                                }
                            }
                        }
                    } else {
                        Spacer(Modifier.height(ArecaTheme.spacing.xl3))
                    }
                    if (hero != null && heroHeight != null && layout is AuthLayout.HeroBottom) {
                        HeroBox(height = heroHeight, content = hero)
                    }
                }
            }
        }
    }
}

/**
 * Places the scrolling column's three children — the band above, the body, and the footer with
 * the band below — with the footer always against the bottom edge and the body where [body] says.
 *
 * Content taller than the viewport makes the column taller, and then all three simply stack.
 *
 * @param body Where the body sits in the space between the band above and the footer.
 */
private class AuthColumnArrangement(private val body: BodyPlacement) : Arrangement.Vertical {
    override fun Density.arrange(totalSize: Int, sizes: IntArray, outPositions: IntArray) {
        if (sizes.size != 3) {
            // Not the scaffold's own column; stack whatever is there.
            var y = 0
            sizes.forEachIndexed { index, size ->
                outPositions[index] = y
                y += size
            }
            return
        }
        val (above, middle, below) = sizes
        val free = (totalSize - above - middle - below).coerceAtLeast(0)
        outPositions[0] = 0
        outPositions[1] = above + when (body) {
            BodyPlacement.Top -> 0
            BodyPlacement.Centre -> free / 2
            BodyPlacement.Bottom -> free
        }
        outPositions[2] = maxOf(totalSize - below, outPositions[1] + middle)
    }

    override fun toString(): String = "AuthColumnArrangement($body)"
}

/** Where the body sits in the space the band above and the footer leave. */
private enum class BodyPlacement {
    /** Straight under the band, or the top edge: the phone screen. */
    Top,

    /** Centred, on a window wider than compact with no band. */
    Centre,

    /** Straight above the footer: [AuthLayout.FullBleed]. */
    Bottom,
}

/** The body under the band, the footer against the bottom edge: every phone layout but one. */
private val BodyAtTop: Arrangement.Vertical = AuthColumnArrangement(BodyPlacement.Top)

/** The body centred above the footer: a wider window with no band of artwork. */
private val BodyCentred: Arrangement.Vertical = AuthColumnArrangement(BodyPlacement.Centre)

/** The body sitting on the footer, the group against the bottom edge: [AuthLayout.FullBleed]. */
private val BodyAgainstFooter: Arrangement.Vertical = AuthColumnArrangement(BodyPlacement.Bottom)

/**
 * Hands a bar placed in the scaffold the defaults that fit it there.
 *
 * Provided rather than applied, so a bar that sets its own size or style still wins. The bar keeps
 * the pane's full width — over full-bleed artwork its scrim spans the window — and only its
 * controls move in.
 *
 * @param inset Distance from the pane's edges to the form's content, which the bar's controls line
 *   up with: the gutter, plus the margin either side of a capped column.
 * @param onHero Whether the bar sits over full-bleed artwork.
 * @param content The bar.
 */
@Composable
private fun AuthTopBarDefaults(
    inset: Dp,
    onHero: Boolean,
    content: @Composable () -> Unit,
) {
    val size = LocalTopBarSize.current ?: TopBarSize.Default
    val style = LocalTopBarStyle.current ?: TopBarStyle.Default

    CompositionLocalProvider(
        LocalTopBarSize provides size.copy(
            padding = PaddingValues(horizontal = inset, vertical = ArecaTheme.spacing.md),
        ),
        LocalTopBarStyle provides if (onHero) TopBarStyle.Scrim else style,
        content = content,
    )
}

/**
 * The panel [AuthLayout.Split] hands its artwork: the full height of the window, on the start side.
 *
 * Clipped, like [HeroBox], so artwork that does not crop to the panel cannot spill over the form.
 *
 * @param fraction Share of the width the panel takes, already clamped.
 * @param content Artwork to fill the panel.
 */
@Composable
private fun HeroPanel(
    fraction: Float,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(fraction)
            .clipToBounds(),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

/**
 * The fixed box a split layout hands its artwork.
 *
 * Clipped, so a consumer passing an image that does not crop to the box cannot spill over the form
 * or push it off the screen.
 *
 * @param height Height taken from the viewport.
 * @param content Artwork to fill the box.
 */
@Composable
private fun HeroBox(
    height: Dp,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clipToBounds(),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}
