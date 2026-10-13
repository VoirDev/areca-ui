package dev.voir.areca.flow.auth.part

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.unit.Dp
import dev.voir.areca.flow.auth.AuthBranding
import dev.voir.areca.flow.auth.AuthLayout
import dev.voir.areca.flow.auth.heroFraction
import dev.voir.areca.foundation.layout.BaseLayout
import dev.voir.areca.foundation.topbar.LocalTopBarSize
import dev.voir.areca.foundation.topbar.LocalTopBarStyle
import dev.voir.areca.foundation.topbar.TopBarSize
import dev.voir.areca.foundation.topbar.TopBarStyle
import dev.voir.areca.theme.ArecaTheme

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
 * The bottom edge is held by [Arrangement.SpaceBetween] against a column whose minimum height is
 * the viewport, rather than by a weighted spacer. A weight measures against the parent's height,
 * and inside a vertically scrolling column that height is infinite, so a weighted spacer collapses
 * to nothing. The column has to scroll, because a registration form plus a raised keyboard does not
 * fit on a short phone.
 *
 * The artwork of a split layout is sized against the viewport before the keyboard inset is taken
 * off it, so raising the keyboard scrolls the artwork away rather than shrinking it.
 *
 * The same arrangement is what bottom-aligns [AuthLayout.FullBleed]: that layout emits an empty
 * leading child and puts everything else in the trailing one, so the group lands against the bottom
 * edge instead of the top.
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
    content: @Composable ColumnScope.() -> Unit,
) {
    val layout = branding.layout
    val hero = branding.hero
    // FullBleed without artwork is Plain: light foregrounds on the page background would vanish.
    val onHero = hero != null && layout == AuthLayout.FullBleed

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
        // Consumed as well as applied, so the keyboard inset below is measured past the navigation
        // bar this padding already clears rather than adding its height a second time.
        Column(modifier = Modifier.fillMaxSize().padding(insets).consumeWindowInsets(insets)) {
            topBar?.let { bar -> AuthTopBarDefaults(gutter = gutter, onHero = onHero, content = bar) }

            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                // Measured outside the keyboard inset, so the artwork keeps its height when the
                // keyboard opens instead of shrinking with what is left of the viewport.
                val heroHeight = layout.heroFraction?.let { maxHeight * it }

                val body: @Composable ColumnScope.() -> Unit = {
                    CompositionLocalProvider(LocalAuthOnHero provides onHero) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = gutter),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            // A bar above already holds the top edge away from the status bar.
                            Spacer(Modifier.height(if (topBar == null) ArecaTheme.spacing.xl4 else ArecaTheme.spacing.xl))

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

                val footerBlock: @Composable ColumnScope.() -> Unit = {
                    if (footer != null) {
                        CompositionLocalProvider(LocalAuthOnHero provides onHero) {
                            Column(modifier = Modifier.padding(horizontal = gutter)) {
                                Spacer(Modifier.height(ArecaTheme.spacing.xl4))
                                footer()
                                Spacer(Modifier.height(ArecaTheme.spacing.xl3))
                            }
                        }
                    } else {
                        Spacer(Modifier.height(ArecaTheme.spacing.xl3))
                    }
                }

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
                        verticalArrangement = Arrangement.SpaceBetween,
                    ) {
                        // Leading child. Empty when the content is bottom-aligned, which is what
                        // pushes the trailing child — everything else — against the bottom edge.
                        Column(modifier = Modifier.fillMaxWidth()) {
                            if (hero != null && heroHeight != null && layout is AuthLayout.HeroTop) {
                                HeroBox(height = heroHeight, content = hero)
                            }
                            if (!onHero) body()
                        }

                        Column(modifier = Modifier.fillMaxWidth()) {
                            if (onHero) body()
                            footerBlock()
                            if (hero != null && heroHeight != null && layout is AuthLayout.HeroBottom) {
                                HeroBox(height = heroHeight, content = hero)
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Hands a bar placed in the scaffold the defaults that fit it there.
 *
 * Provided rather than applied, so a bar that sets its own size or style still wins.
 *
 * @param gutter Horizontal inset of the form, which the bar's controls line up with.
 * @param onHero Whether the bar sits over full-bleed artwork.
 * @param content The bar.
 */
@Composable
private fun AuthTopBarDefaults(
    gutter: Dp,
    onHero: Boolean,
    content: @Composable () -> Unit,
) {
    val size = LocalTopBarSize.current ?: TopBarSize.Default
    val style = LocalTopBarStyle.current ?: TopBarStyle.Default

    CompositionLocalProvider(
        LocalTopBarSize provides size.copy(
            padding = PaddingValues(horizontal = gutter, vertical = ArecaTheme.spacing.md),
        ),
        LocalTopBarStyle provides if (onHero) TopBarStyle.Scrim else style,
        content = content,
    )
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
