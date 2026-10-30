package dev.voir.areca.playground.flow.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.input.ImeAction
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
import dev.voir.areca.flow.auth.sso.SsoGroup
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.topbar.BackAction
import dev.voir.areca.foundation.topbar.TopBar
import dev.voir.areca.playground.showcase.FlowShowcase
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.canvas.WindowFrame
import dev.voir.areca.playground.showcase.canvas.WindowPreset
import dev.voir.areca.playground.showcase.canvas.windowInsets
import dev.voir.areca.playground.showcase.chrome.ChromeText
import dev.voir.areca.playground.showcase.chrome.ChromeTextRole
import dev.voir.areca.playground.showcase.chrome.ChromeTone
import dev.voir.areca.theme.ArecaTheme

/**
 * What a split sign-in draws where the split does not fit, as something an `enumControl` can offer.
 *
 * Only the layouts that take the same crop-tolerant artwork as the panel: an application passes one
 * `hero` for every window, so an inline illustration is not a fallback it can pick here.
 *
 * @property label Name shown in the control.
 * @property layout The fallback this option selects.
 */
private enum class SplitFallbackOption(val label: String, val layout: AuthLayout) {
    /** A band across the top: the default. */
    HeroTop("Hero top", AuthLayout.HeroTop()),

    /** A band across the bottom. */
    HeroBottom("Hero bottom", AuthLayout.HeroBottom()),

    /** The artwork behind everything. */
    FullBleed("Full bleed", AuthLayout.FullBleed),

    /** No artwork at all on the smaller windows. */
    Plain("Plain", AuthLayout.Plain),
}

/**
 * A sign-in screen for every window an application runs in, from a phone to a desktop monitor.
 *
 * The same assembly as [SignInShowcase]'s, with [AuthLayout.Split]: from an expanded width up the
 * artwork is a panel on the start side and the form sits centred beside it; anywhere smaller it is
 * the fallback the layout names. The screen is drawn in a [WindowFrame] rather than a phone, because
 * the window is the thing being changed.
 *
 * What is drawn here is [SplitSignInRecipe], immediately below. It is the README's "On a tablet or
 * desktop" recipe.
 */
@Composable
fun SignInSplitShowcase() = FlowShowcase(
    title = "Sign in on tablet and desktop",
    summary = "A sign-in screen with the artwork beside the form on a large window and above it on a phone.",
    module = "flow-auth",
    status = ShowcaseStatus.Beta,
    notes = {
        ChromeText(
            text = "The recipe",
            role = ChromeTextRole.Label,
        )
        ChromeText(
            text = "SplitSignInRecipe in this file is the sign-in recipe with one change: " +
                "layout = AuthLayout.Split(). One value covers every window.",
            role = ChromeTextRole.SectionDescription,
            tone = ChromeTone.Muted,
        )
        ChromeText(
            text = "The split draws from an expanded width (840dp) up while the height is not " +
                "compact — tablet landscape, laptop, desktop. Phone, the foldable and the tablet " +
                "held upright draw the fallback; a phone on its side is wide but too short.",
            role = ChromeTextRole.SectionDescription,
            tone = ChromeTone.Muted,
        )
        ChromeText(
            text = "On every window wider than a phone the column stops at 480dp and sits centred; " +
                "turn the back bar on to see its chevron line up with the form.",
            role = ChromeTextRole.SectionDescription,
            tone = ChromeTone.Muted,
        )
    },
) {
    val preset by enumControl(
        key = "window",
        default = WindowPreset.Laptop,
        options = WindowPreset.entries,
        optionLabel = { it.label },
        description = "The split needs 840dp of width and more than 480dp of height.",
    )
    val fallback by enumControl(
        key = "fallback",
        default = SplitFallbackOption.HeroTop,
        options = SplitFallbackOption.entries,
        optionLabel = { it.label },
        description = "What the smaller windows draw.",
    )
    val fraction by numberControl(
        key = "fraction",
        default = AuthLayout.DefaultHeroFraction,
        range = 0f..1f,
        step = 0.05f,
        format = { "${(it * 100).toInt()}%" },
        description = "Share of the width the panel takes. Clamped to 10–80%.",
    )
    val hero by booleanControl(
        key = "hero",
        default = true,
        description = "Off: no panel; the form is centred in the whole window.",
    )
    val back by booleanControl(
        key = "back",
        default = false,
        label = "back bar",
        description = "A TopBar with a back control, pinned above the form pane.",
    )
    val submitting by booleanControl("submitting", default = false)

    preview {
        var state by remember { mutableStateOf(SignInScreenState()) }

        // The preview area's height, so the window is scaled to what the page actually has.
        BoxWithConstraints(contentAlignment = Alignment.Center) {
            WindowFrame(preset = preset, maxHeight = maxHeight) {
                SplitSignInRecipe(
                    state = state.copy(submitting = submitting),
                    onStateChange = { state = it },
                    branding = AuthBranding(
                        wordmark = { AuthSamples.Wordmark() },
                        hero = if (hero) ({ AuthSamples.Hero() }) else null,
                        layout = AuthLayout.Split(fraction = fraction, fallback = fallback.layout),
                    ),
                    topBar = if (back) {
                        { TopBar(modifier = Modifier.fillMaxWidth(), leading = { BackAction(onClick = {}) }) {} }
                    } else {
                        null
                    },
                    windowInsets = preset.windowInsets,
                )
            }
        }
    }
}

/**
 * The recipe: a sign-in screen that lays itself out for the window it is in.
 *
 * Nothing in it branches on the window. [AuthLayout.Split] and the scaffold decide between the
 * panel and the fallback, and cap the column, from `ArecaTheme.window`.
 *
 * @param state Everything to draw.
 * @param onStateChange Called with the next state.
 * @param branding Wordmark, artwork and the split layout.
 * @param topBar Bar above the form, or `null` on the first screen of a flow.
 * @param windowInsets Regions the system occupies.
 */
@Composable
private fun SplitSignInRecipe(
    state: SignInScreenState,
    onStateChange: (SignInScreenState) -> Unit,
    branding: AuthBranding,
    topBar: (@Composable () -> Unit)?,
    windowInsets: WindowInsets,
) {
    val passwordFocus = remember { FocusRequester() }
    val submit = { onStateChange(state.checked()) }

    AuthScaffold(
        branding = branding,
        windowInsets = windowInsets,
        topBar = topBar,
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

        AuthEmailField(
            value = state.email,
            onValueChange = { onStateChange(state.copy(email = it, emailError = null)) },
            error = state.emailError,
            enabled = !state.busy,
            imeAction = ImeAction.Next,
            onImeAction = { passwordFocus.requestFocus() },
        )

        Spacer(Modifier.height(ArecaTheme.spacing.xl))

        AuthPasswordField(
            value = state.password,
            onValueChange = { onStateChange(state.copy(password = it, passwordError = null)) },
            error = state.passwordError,
            enabled = !state.busy,
            focusRequester = passwordFocus,
            imeAction = ImeAction.Done,
            onImeAction = submit,
        )

        Spacer(Modifier.height(ArecaTheme.spacing.lg))

        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
            AuthTextLink(text = "Forgot password?", onClick = {}, enabled = !state.busy)
        }

        Spacer(Modifier.height(ArecaTheme.spacing.xl2))

        AuthBanner(message = state.formError, contentDescription = AuthSamples::errorDescription)

        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.busy,
            loading = state.submitting,
            onClick = submit,
        ) {
            Text("Sign in")
        }

        Spacer(Modifier.height(ArecaTheme.spacing.xl2))

        SsoGroup(
            providers = listOf(AuthProvider.Apple, AuthProvider.Google),
            onProviderClick = {},
            enabled = !state.busy,
            busyProvider = state.busyProvider,
            dividerText = "or continue with",
        )
    }
}
