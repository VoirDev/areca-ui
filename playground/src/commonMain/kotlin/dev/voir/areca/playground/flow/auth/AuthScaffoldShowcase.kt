package dev.voir.areca.playground.flow.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.flow.auth.AuthBranding
import dev.voir.areca.flow.auth.AuthLayout
import dev.voir.areca.flow.auth.AuthProvider
import dev.voir.areca.flow.auth.part.AuthFooterPrompt
import dev.voir.areca.flow.auth.part.AuthHeader
import dev.voir.areca.flow.auth.part.AuthScaffold
import dev.voir.areca.flow.auth.part.LegalConsent
import dev.voir.areca.flow.auth.sso.SsoGroup
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.feedback.Spinner
import dev.voir.areca.foundation.feedback.SpinnerStyle
import dev.voir.areca.foundation.topbar.BackAction
import dev.voir.areca.foundation.topbar.LocalTopBarSize
import dev.voir.areca.foundation.topbar.TextAction
import dev.voir.areca.foundation.topbar.TopBar
import dev.voir.areca.foundation.topbar.TopBarProgress
import dev.voir.areca.foundation.topbar.TopBarSize
import dev.voir.areca.foundation.topbar.TopBarTitleAlignment
import dev.voir.areca.playground.showcase.ShowcaseScaffold
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.canvas.DeviceFrame
import dev.voir.areca.playground.showcase.canvas.DeviceSize
import dev.voir.areca.playground.showcase.canvas.windowInsets
import dev.voir.areca.playground.showcase.section.Anatomy
import dev.voir.areca.playground.showcase.section.EdgeCases
import dev.voir.areca.playground.showcase.section.Playground
import dev.voir.areca.playground.showcase.section.VariantLayout
import dev.voir.areca.playground.showcase.section.Variants
import dev.voir.areca.theme.ArecaTheme

/**
 * Factor the phones in variant, anatomy and edge-case cells are drawn at.
 *
 * Small enough that three [DeviceSize.Compact] phones share a row of the page, large enough that
 * the heading still reads. Layout is unaffected: each phone still lays out at its real size.
 */
private const val CellScale: Float = 0.6f

/** Which window insets the playground scaffold keeps its content clear of. */
private enum class InsetsOption(
    /** Label of the option in the control panel. */
    val label: String,
) {
    /** The insets a phone of the chosen size would report. */
    Device("Device"),

    /** None, as for a host that has already consumed them. */
    Consumed("Consumed"),
}

/** Which bar the playground scaffold draws above its form. */
private enum class TopBarOption(
    /** Label of the option in the control panel. */
    val label: String,
) {
    /** None, as on the first screen of a flow. */
    None("None"),

    /** A back control alone, as on a screen reached from another. */
    Back("Back"),

    /** A back control, the step, a skip and a progress rule, as on a multi-step sign-up. */
    Step("Step"),
}

/** Showcase for [AuthScaffold], with the [AuthBranding] and [AuthLayout] it is configured by. */
@Composable
fun AuthScaffoldShowcase() = ShowcaseScaffold(
    title = "Auth scaffold",
    summary = "The frame every authentication screen is built in: insets, scrolling, hero " +
        "placement and a footer pinned to the bottom edge.",
    module = "flow-auth",
    status = ShowcaseStatus.Beta,
) {
    Playground(
        description = "Change the branding and the frame; the body is the application's own sample " +
            "content.",
        contentAlignment = Alignment.Center,
    ) {
        val device by enumControl(
            key = "device",
            default = DeviceSize.Regular,
            options = DeviceSize.entries,
            optionLabel = { it.label },
        )
        val layout by enumControl(
            key = "layout",
            default = AuthLayoutOption.HeroTop,
            options = AuthLayoutOption.entries,
            optionLabel = { it.label },
            description = "Where the hero artwork sits.",
        )
        val fraction by numberControl(
            key = "hero fraction",
            default = AuthLayout.DefaultHeroFraction,
            range = 0f..1f,
            step = 0.05f,
            format = { "${(it * 100).toInt()}%" },
            description = "Hero top and hero bottom only. Clamped to 10–80%.",
        )
        val hero by booleanControl(
            key = "hero",
            default = true,
            description = "Off: a layout that names a hero falls back to Plain's shape.",
        )
        val wordmark by booleanControl("wordmark", default = true)
        val footer by enumControl(
            key = "footer",
            default = FooterOption.Prompt,
            options = FooterOption.entries,
            optionLabel = { it.label },
        )
        val topBar by enumControl(
            key = "top bar",
            default = TopBarOption.None,
            options = TopBarOption.entries,
            optionLabel = { it.label },
            description = "A foundation TopBar; the scaffold lines it up with the gutter.",
        )
        val overlay by booleanControl(
            key = "overlay",
            default = false,
            description = "A scrim with a spinner, drawn above everything.",
        )
        val insets by enumControl(
            key = "window insets",
            default = InsetsOption.Device,
            options = InsetsOption.entries,
            optionLabel = { it.label },
        )
        val gutter by numberControl(
            key = "gutter",
            default = 24f,
            range = 0f..48f,
            step = 4f,
            format = { "${it.toInt()}dp" },
            description = "Defaults to spacing.xl3.",
        )

        preview {
            DeviceFrame(size = device) {
                AuthScaffold(
                    branding = AuthBranding(
                        wordmark = if (wordmark) ({ AuthSamples.Wordmark() }) else null,
                        hero = if (hero) heroFor(layout) else null,
                        layout = layout.toLayout(heroFraction = fraction),
                    ),
                    gutter = gutter.dp,
                    windowInsets = when (insets) {
                        InsetsOption.Device -> device.windowInsets
                        InsetsOption.Consumed -> WindowInsets(0, 0, 0, 0)
                    },
                    topBar = topBar.slot(),
                    overlay = { if (overlay) BusyScrim() },
                    footer = footer.slot(),
                ) {
                    SampleBody()
                }
            }
        }
    }

    Anatomy(description = "The branding slots, the body, the pinned footer and the overlay above them.") {
        slot(
            name = "branding.wordmark",
            description = "Optional. Above the heading, given the column's width.",
        ) {
            ScaffoldSample(
                branding = AuthBranding(wordmark = { AuthSamples.Wordmark() }),
                footer = FooterOption.None,
            )
        }
        slot(
            name = "branding.hero",
            description = "Optional. Placed by branding.layout; a split layout hands it a fixed, " +
                "clipped box to fill.",
        ) {
            ScaffoldSample(
                branding = AuthBranding(hero = { AuthSamples.Hero() }, layout = AuthLayout.HeroTop()),
                footer = FooterOption.None,
            )
        }
        slot(
            name = "topBar",
            description = "Optional. A foundation TopBar pinned above the form. Its controls land on " +
                "the gutter, and over full-bleed artwork it draws as a scrim bar.",
        ) {
            ScaffoldSample(branding = AuthBranding(), footer = FooterOption.None, topBar = TopBarOption.Step)
        }
        slot(
            name = "content",
            description = "Required. The heading, form and actions, in a centred column inside the " +
                "gutter.",
        ) {
            ScaffoldSample(branding = AuthBranding(), footer = FooterOption.None)
        }
        slot(
            name = "footer",
            description = "Optional. Pinned to the bottom edge while the content is short, following " +
                "it once it is long.",
        ) {
            ScaffoldSample(branding = AuthBranding(), footer = FooterOption.Prompt)
        }
        slot(
            name = "overlay",
            description = "Optional. Above everything, for a toast host or a scrim.",
        ) {
            ScaffoldSample(overlay = { BusyScrim() })
        }
    }

    Variants(
        title = "Layout",
        description = "Where the hero sits relative to the form; inline takes an illustration, the " +
            "others a crop-tolerant fill.",
    ) {
        AuthLayoutOption.entries.forEach { option ->
            variant(name = option.label, detail = option.toLayout().code) {
                ScaffoldSample(
                    branding = AuthBranding(
                        wordmark = { AuthSamples.Wordmark() },
                        hero = heroFor(option),
                        layout = option.toLayout(),
                    ),
                    footer = FooterOption.Legal,
                )
            }
        }
    }

    Variants(
        title = "Hero fraction",
        description = "The share of the viewport a split layout gives its artwork, at the bottom, " +
            "default and top of its range.",
    ) {
        val fractions = listOf(
            AuthLayout.MinHeroFraction,
            AuthLayout.DefaultHeroFraction,
            AuthLayout.MaxHeroFraction,
        )
        fractions.forEach { fraction ->
            variant(name = "HeroTop / $fraction", detail = "AuthLayout.HeroTop(${fraction}f)") {
                ScaffoldSample(
                    branding = AuthBranding(
                        hero = { AuthSamples.Hero() },
                        layout = AuthLayout.HeroTop(fraction),
                    ),
                    footer = FooterOption.None,
                )
            }
        }
        fractions.forEach { fraction ->
            variant(name = "HeroBottom / $fraction", detail = "AuthLayout.HeroBottom(${fraction}f)") {
                ScaffoldSample(
                    branding = AuthBranding(
                        hero = { AuthSamples.Hero() },
                        layout = AuthLayout.HeroBottom(fraction),
                    ),
                    footer = FooterOption.None,
                )
            }
        }
    }

    Variants(
        title = "Branding",
        description = "Which of the two visual slots a product fills; each one left out takes its " +
            "spacing with it.",
    ) {
        variant(name = "Wordmark + hero", detail = "HeroTop") {
            ScaffoldSample(
                branding = AuthBranding(
                    wordmark = { AuthSamples.Wordmark() },
                    hero = { AuthSamples.Hero() },
                    layout = AuthLayout.HeroTop(),
                ),
            )
        }
        variant(name = "Wordmark + illustration", detail = "HeroInline") {
            ScaffoldSample(
                branding = AuthBranding(
                    wordmark = { AuthSamples.Wordmark() },
                    hero = { AuthSamples.Illustration() },
                    layout = AuthLayout.HeroInline,
                ),
            )
        }
        variant(name = "Wordmark only", detail = "hero = null") {
            ScaffoldSample(branding = AuthBranding(wordmark = { AuthSamples.Wordmark() }))
        }
        variant(name = "Hero only", detail = "wordmark = null") {
            ScaffoldSample(
                branding = AuthBranding(hero = { AuthSamples.Hero() }, layout = AuthLayout.HeroTop()),
            )
        }
        variant(name = "Neither", detail = "AuthBranding()") {
            ScaffoldSample(branding = AuthBranding())
        }
    }

    Variants(
        title = "Footer",
        description = "What the bottom edge says; with no footer the content keeps a bottom margin " +
            "and nothing else.",
    ) {
        FooterOption.entries.forEach { option ->
            variant(
                name = option.label,
                detail = if (option == FooterOption.None) "footer = null" else null,
            ) {
                ScaffoldSample(footer = option)
            }
        }
    }

    Variants(
        title = "Window insets",
        description = "The content stays clear of the status bar and home indicator it is told " +
            "about, and of nothing else.",
    ) {
        variant(name = "Device", detail = "DeviceSize.windowInsets") {
            ScaffoldSample()
        }
        variant(name = "Consumed", detail = "WindowInsets(0, 0, 0, 0)") {
            ScaffoldSample(windowInsets = WindowInsets(0, 0, 0, 0))
        }
        variant(name = "Device / FullBleed", detail = "Artwork runs under the bars") {
            ScaffoldSample(
                branding = AuthBranding(
                    wordmark = { AuthSamples.Wordmark() },
                    hero = { AuthSamples.Hero() },
                    layout = AuthLayout.FullBleed,
                ),
                footer = FooterOption.Legal,
            )
        }
    }

    Variants(
        title = "Gutter",
        description = "The horizontal inset of the body and the footer from the screen edges.",
    ) {
        variant(name = "0dp", detail = "Flush with the screen edges") {
            ScaffoldSample(gutter = 0.dp)
        }
        variant(name = "24dp", detail = "Default, spacing.xl3") {
            ScaffoldSample()
        }
        variant(name = "48dp") {
            ScaffoldSample(gutter = 48.dp)
        }
    }

    EdgeCases(layout = VariantLayout.Grid) {
        case(
            name = "HeroTop without artwork",
            expectation = "Falls back to Plain's shape: no empty band at the top.",
        ) {
            ScaffoldSample(
                branding = AuthBranding(
                    wordmark = { AuthSamples.Wordmark() },
                    layout = AuthLayout.HeroTop(),
                ),
            )
        }
        case(
            name = "FullBleed without artwork",
            expectation = "Falls back to Plain's shape, with the ordinary foregrounds.",
        ) {
            ScaffoldSample(
                branding = AuthBranding(
                    wordmark = { AuthSamples.Wordmark() },
                    layout = AuthLayout.FullBleed,
                ),
            )
        }
        case(
            name = "Fraction above the range",
            expectation = "HeroTop(0.95f) is clamped to 0.8; the form keeps a fifth of the viewport.",
        ) {
            ScaffoldSample(
                branding = AuthBranding(
                    hero = { AuthSamples.Hero() },
                    layout = AuthLayout.HeroTop(0.95f),
                ),
                footer = FooterOption.None,
            )
        }
        case(
            name = "Fraction below the range",
            expectation = "HeroBottom(0.02f) is clamped to 0.1: a strip of artwork, never none.",
        ) {
            ScaffoldSample(
                branding = AuthBranding(
                    hero = { AuthSamples.Hero() },
                    layout = AuthLayout.HeroBottom(0.02f),
                ),
                footer = FooterOption.None,
            )
        }
        case(
            name = "Content taller than the viewport",
            expectation = "Runs past the bottom edge and scrolls, hero included; the footer follows " +
                "the content rather than covering it.",
        ) {
            ScaffoldSample(
                branding = AuthBranding(
                    wordmark = { AuthSamples.Wordmark() },
                    hero = { AuthSamples.Hero() },
                    layout = AuthLayout.HeroTop(),
                ),
                body = { LongSampleBody() },
            )
        }
        case(
            name = "Tall device",
            expectation = "The footer still reaches the bottom edge, clear of the home indicator.",
        ) {
            ScaffoldSample(device = DeviceSize.Tall)
        }
    }
}

/**
 * One scaled-down phone showing [AuthScaffold] around the sample body.
 *
 * Every cell on the page draws through this so the cells differ only in the argument they are
 * demonstrating.
 *
 * @param branding Wordmark, hero and layout.
 * @param device Phone shape to lay the screen out at.
 * @param footer What the bottom edge says.
 * @param windowInsets Regions kept clear of the content. Defaults to [device]'s.
 * @param gutter Horizontal inset of the content column.
 * @param overlay Content drawn above everything.
 * @param body Screen body.
 */
@Composable
private fun ScaffoldSample(
    branding: AuthBranding = AuthBranding(wordmark = { AuthSamples.Wordmark() }),
    device: DeviceSize = DeviceSize.Compact,
    footer: FooterOption = FooterOption.Prompt,
    windowInsets: WindowInsets = device.windowInsets,
    gutter: Dp = ArecaTheme.spacing.xl3,
    topBar: TopBarOption = TopBarOption.None,
    overlay: @Composable BoxScope.() -> Unit = {},
    body: @Composable () -> Unit = { SampleBody() },
) {
    DeviceFrame(size = device, scale = CellScale) {
        AuthScaffold(
            branding = branding,
            gutter = gutter,
            windowInsets = windowInsets,
            topBar = topBar.slot(),
            overlay = overlay,
            footer = footer.slot(),
        ) {
            body()
        }
    }
}

/**
 * The bar this option draws, or `null` for none.
 *
 * @return Top bar to pass to [AuthScaffold].
 */
private fun TopBarOption.slot(): (@Composable () -> Unit)? = when (this) {
    TopBarOption.None -> null
    TopBarOption.Back -> ({
        TopBar(modifier = Modifier.fillMaxWidth(), leading = { BackAction(onClick = {}) }) {}
    })

    TopBarOption.Step -> ({
        TopBar(
            title = "Step 2 of 3",
            modifier = Modifier.fillMaxWidth(),
            size = (LocalTopBarSize.current ?: TopBarSize.Default).copy(bottomPadding = PaddingValues()),
            titleAlignment = TopBarTitleAlignment.Center,
            leading = { BackAction(onClick = {}) },
            trailing = { TextAction(text = "Skip", onClick = {}, color = ActionColor.Neutral) },
            bottom = { TopBarProgress(progress = 2f / 3f) },
        )
    })
}

/**
 * The slot this footer option fills, or `null` for none.
 *
 * @return Footer to pass to [AuthScaffold].
 */
private fun FooterOption.slot(): (@Composable () -> Unit)? = when (this) {
    FooterOption.Prompt -> ({
        AuthFooterPrompt(prompt = "New here?", linkText = "Create account", onLinkClick = {})
    })

    FooterOption.Legal -> ({ LegalConsent(links = AuthSamples.Legal, onLinkClick = {}) })
    FooterOption.None -> null
}

/**
 * The application's own body, kept to a heading and one action: the scaffold is what is on show.
 */
@Composable
private fun SampleBody() {
    AuthHeader(title = "Welcome back", subtitle = "Sign in to pick up where you left off.")
    Spacer(Modifier.height(ArecaTheme.spacing.xl6))
    Button(text = "Continue with email", onClick = {}, modifier = Modifier.fillMaxWidth())
}

/** A body longer than a small phone, to show the scaffold scrolling instead of clipping. */
@Composable
private fun LongSampleBody() {
    AuthHeader(title = "Create your account", subtitle = "Choose how you want to sign in.")
    Spacer(Modifier.height(ArecaTheme.spacing.xl6))
    SsoGroup(
        providers = AuthProvider.entries,
        onProviderClick = {},
    )
    Spacer(Modifier.height(ArecaTheme.spacing.xl2))
    Button(text = "Continue with email", onClick = {}, modifier = Modifier.fillMaxWidth())
}

/** A scrim with a spinner, as an application might lay over a screen while an attempt runs. */
@Composable
private fun BoxScope.BusyScrim() {
    Box(
        modifier = Modifier
            .matchParentSize()
            .background(ArecaTheme.colors.bgOverlayDark),
        contentAlignment = Alignment.Center,
    ) {
        Spinner(
            style = SpinnerStyle(color = ArecaTheme.colors.fgOnContrast),
            contentDescription = "Signing in",
        )
    }
}

/** How this layout is written in source, for a variant's detail line. */
private val AuthLayout.code: String
    get() = when (this) {
        AuthLayout.Plain -> "AuthLayout.Plain"
        is AuthLayout.HeroTop -> "AuthLayout.HeroTop()"
        is AuthLayout.HeroBottom -> "AuthLayout.HeroBottom()"
        AuthLayout.HeroInline -> "AuthLayout.HeroInline"
        AuthLayout.FullBleed -> "AuthLayout.FullBleed"
    }
