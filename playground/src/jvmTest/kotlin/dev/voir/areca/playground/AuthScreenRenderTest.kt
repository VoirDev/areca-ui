package dev.voir.areca.playground

import dev.voir.areca.flow.auth.AuthLegalLinks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.Density
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Alignment
import dev.voir.areca.flow.auth.AuthBranding
import dev.voir.areca.flow.auth.AuthLayout
import dev.voir.areca.flow.auth.AuthProvider
import dev.voir.areca.flow.auth.part.AuthBanner
import dev.voir.areca.flow.auth.part.AuthEmailField
import dev.voir.areca.flow.auth.part.AuthFooterPrompt
import dev.voir.areca.flow.auth.part.AuthHeader
import dev.voir.areca.flow.auth.part.AuthPasswordField
import dev.voir.areca.flow.auth.part.AuthScaffold
import dev.voir.areca.flow.auth.part.AuthTextLink
import dev.voir.areca.flow.auth.part.LegalConsent
import dev.voir.areca.flow.auth.sso.SsoGroup
import dev.voir.areca.flow.auth.sso.SsoLayout
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.ArecaThemeProvider
import dev.voir.areca.playground.flow.auth.AuthSamples
import dev.voir.areca.playground.flow.auth.SignInScreenState
import org.jetbrains.skia.EncodedImageFormat
import java.io.File
import kotlin.test.Test

/** Logical width of the device the screens are rendered at. */
private const val WIDTH = 390

/** Logical height of the device the screens are rendered at. */
private const val HEIGHT = 844

/** Pixel density the scenes are rasterised at, so the PNGs are legible when opened. */
private const val DENSITY = 2f

/** Where the rendered screens are written. */
private val OUTPUT = File(System.getProperty("render.out") ?: "build/screens")

/**
 * Renders one screen to a PNG.
 *
 * @param name File name, without the extension.
 * @param dark Whether to render in the dark theme.
 * @param content Screen to render.
 */

@OptIn(ExperimentalComposeUiApi::class)
private fun render(name: String, dark: Boolean = false, content: @Composable () -> Unit) {
    ImageComposeScene(
        width = (WIDTH * DENSITY).toInt(),
        height = (HEIGHT * DENSITY).toInt(),
        density = Density(DENSITY),
    ) {
        ArecaThemeProvider(darkTheme = dark) {
            Box(Modifier.fillMaxSize().background(ArecaTheme.colors.bgCanvas)) { content() }
        }
    }.let { scene ->
        try {
            val data = scene.render().encodeToData(EncodedImageFormat.PNG)!!
            OUTPUT.mkdirs()
            File(OUTPUT, "$name.png").writeBytes(data.bytes)
        } finally {
            scene.close()
        }
    }
}

/** Stand-in brand mark, so the wordmark slot is exercised. */
@Composable
private fun Wordmark() = Text("ARECA", variant = TextVariant.HeadingSmall, color = TextColor.Brand)

/** Stand-in artwork for the layouts that place a hero. */
@Composable
private fun Hero() = Box(
    Modifier.fillMaxSize().background(
        Brush.linearGradient(
            listOf(
                ArecaTheme.baseColors.brand60,
                ArecaTheme.baseColors.brand70,
                ArecaTheme.baseColors.neutral100,
            ),
            start = Offset.Zero,
            end = Offset.Infinite,
        ),
    ),
)

/** Stand-in legal addresses, never opened. */
private val legal = AuthLegalLinks("https://example.com/terms", "https://example.com/privacy")

/**
 * Renders every notable configuration of the authentication screens to `build/screens`.
 *
 * Not an assertion, and deliberately so: there is no golden image to compare against, and one that
 * existed would fail on every intentional change. What it is worth is two things a unit test cannot
 * give. It fails when any of these configurations throws during composition or layout — a real
 * regression, caught across eight of them at once. And it is the only way to look at these screens
 * on a machine with no display, which is how the full-bleed layout's illegible field labels were
 * found.
 *
 * Run it and open the directory:
 *
 * ```bash
 * ./gradlew :playground:jvmTest --tests '*AuthScreenRenderTest*'
 * ```
 */
/**
 * Renders the assemblies from the showcase recipes to `build/screens`.
 *
 * Not an assertion, and deliberately so: there is no golden image to compare against, and one that
 * existed would fail on every intentional change. What it is worth is two things a unit test cannot
 * give. It fails when any of these assemblies throws during composition or layout — a real
 * regression, caught across all of them at once. And it is the only way to look at these screens on
 * a machine with no display, which is how the full-bleed layout's illegible field labels were
 * found.
 *
 * Run it and open the directory:
 *
 * ```bash
 * ./gradlew :playground:jvmTest --tests '*AuthScreenRenderTest*'
 * ```
 */
class AuthScreenRenderTest {

    @Test
    fun `every notable assembly renders`() {
        render("01-signin-plain") {
            SignIn(
                state = SignInScreenState(email = "ada@example.com"),
                branding = AuthBranding(wordmark = { Wordmark() }),
            )
        }
        render("02-signin-sso-buttons") {
            SignIn(
                state = SignInScreenState(),
                branding = AuthBranding(wordmark = { Wordmark() }),
                providers = listOf(AuthProvider.Apple, AuthProvider.Google, AuthProvider.Facebook),
            )
        }
        render("03-signin-sso-icons") {
            SignIn(
                state = SignInScreenState(),
                branding = AuthBranding(wordmark = { Wordmark() }),
                providers = listOf(
                    AuthProvider.Apple,
                    AuthProvider.Google,
                    AuthProvider.Facebook,
                ),
                ssoLayout = SsoLayout.Icons,
            )
        }
        render("04-signin-errors") {
            SignIn(
                state = SignInScreenState(
                    email = "ada@",
                    emailError = "That does not look like an email address.",
                    passwordError = "Enter your password.",
                    formError = "That email and password do not match an account.",
                ),
            )
        }
        render("05-signin-herotop") {
            SignIn(
                state = SignInScreenState(),
                branding = AuthBranding(hero = { Hero() }, layout = AuthLayout.HeroTop()),
                providers = listOf(AuthProvider.Google),
            )
        }
        render("06-sso-only-fullbleed") {
            SsoOnly(
                providers = listOf(
                    AuthProvider.Apple,
                    AuthProvider.Google,
                    AuthProvider.Facebook,
                    AuthProvider.Email,
                ),
                layout = SsoLayout.Buttons,
            )
        }
        render("07-sso-only-icons") {
            SsoOnly(
                providers = listOf(
                    AuthProvider.Apple,
                    AuthProvider.Google,
                    AuthProvider.Facebook,
                ),
                layout = SsoLayout.Icons,
            )
        }
        render("08-signin-dark", dark = true) {
            SignIn(
                state = SignInScreenState(email = "ada@example.com"),
                branding = AuthBranding(wordmark = { Wordmark() }),
                providers = listOf(AuthProvider.Apple, AuthProvider.Google),
            )
        }
        println("Rendered to ${OUTPUT.absolutePath}")
    }
}

/**
 * The sign-in assembly, as the showcase recipe builds it.
 *
 * @param state Everything to draw.
 * @param branding Wordmark, hero and layout.
 * @param providers Providers to offer.
 * @param ssoLayout How the provider buttons are arranged.
 */
@Composable
private fun SignIn(
    state: SignInScreenState,
    branding: AuthBranding = AuthBranding(),
    providers: List<AuthProvider> = emptyList(),
    ssoLayout: SsoLayout = SsoLayout.Buttons,
) {

    AuthScaffold(
        branding = branding,
        footer = {
            AuthFooterPrompt(
                prompt = "New here?",
                linkText = "Create account",
                onLinkClick = {},
            )
        },
    ) {
        AuthHeader(title = "Welcome back", subtitle = "Sign in to pick up where you left off.")
        Spacer(Modifier.height(ArecaTheme.spacing.xl6))

        AuthEmailField(value = state.email, onValueChange = {}, error = state.emailError)
        Spacer(Modifier.height(ArecaTheme.spacing.xl))
        AuthPasswordField(value = state.password, onValueChange = {}, error = state.passwordError)

        Spacer(Modifier.height(ArecaTheme.spacing.lg))
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
            AuthTextLink(text = "Forgot password?", onClick = {})
        }

        Spacer(Modifier.height(ArecaTheme.spacing.xl2))
        AuthBanner(message = state.formError, contentDescription = AuthSamples::errorDescription)
        Button(modifier = Modifier.fillMaxWidth(), onClick = {}) { Text("Sign in") }

        if (providers.isNotEmpty()) {
            Spacer(Modifier.height(ArecaTheme.spacing.xl2))
            SsoGroup(providers = providers, onProviderClick = {}, layout = ssoLayout)
        }
    }
}

/**
 * The provider-led assembly: no form anywhere.
 *
 * @param providers Providers to offer.
 * @param layout How the provider buttons are arranged.
 */
@Composable
private fun SsoOnly(providers: List<AuthProvider>, layout: SsoLayout) {

    AuthScaffold(
        branding = AuthBranding(hero = { Hero() }, layout = AuthLayout.FullBleed),
        footer = { LegalConsent(links = legal, onLinkClick = {}) },
    ) {
        AuthHeader(title = "Welcome back", subtitle = "Sign in to pick up where you left off.")
        Spacer(Modifier.height(ArecaTheme.spacing.xl6))
        SsoGroup(
            providers = providers,
            onProviderClick = {},
            layout = layout,
            dividerText = null,
        )
    }
}
