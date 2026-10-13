package dev.voir.areca.playground.flow.auth

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import dev.voir.areca.flow.auth.AuthBranding
import dev.voir.areca.flow.auth.part.AuthCodeField
import dev.voir.areca.flow.auth.part.AuthHeader
import dev.voir.areca.flow.auth.part.AuthIdentityChip
import dev.voir.areca.flow.auth.part.AuthResendCode
import dev.voir.areca.flow.auth.part.AuthScaffold
import dev.voir.areca.flow.auth.part.AuthTextLink
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.topbar.BackAction
import dev.voir.areca.foundation.topbar.TopBar
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.Mail
import dev.voir.areca.icons.MessageSquare
import dev.voir.areca.icons.ShieldTick
import dev.voir.areca.playground.showcase.FlowShowcase
import dev.voir.areca.playground.showcase.ShowcaseStatus
import dev.voir.areca.playground.showcase.canvas.DeviceFrame
import dev.voir.areca.playground.showcase.canvas.DeviceSize
import dev.voir.areca.playground.showcase.canvas.windowInsets
import dev.voir.areca.playground.showcase.chrome.ChromeText
import dev.voir.areca.playground.showcase.chrome.ChromeTextRole
import dev.voir.areca.playground.showcase.chrome.ChromeTone
import dev.voir.areca.theme.ArecaTheme
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

/**
 * A screen that asks for a code — sent by email, by text message, or read off an authenticator.
 *
 * The same recipe for all three, because they are the same screen: what it asks for, where the code
 * went, and what to do when it did not arrive. What changes is words, whether there is a destination
 * to show and change, and whether a new code can be sent at all — an authenticator cannot be asked
 * for one, so it offers a recovery code instead.
 */
@Composable
fun VerifyCodeShowcase() = FlowShowcase(
    title = "Verify code",
    summary = "A code screen for email, text message or an authenticator app, verifying on the last digit.",
    module = "flow-auth",
    status = ShowcaseStatus.Beta,
    notes = {
        ChromeText(text = "The recipe", role = ChromeTextRole.Label)
        ChromeText(
            text = "VerifyCodeRecipe in this file. The code verifies itself when the last cell fills; " +
                "the button is for anyone who wants to press something.",
            role = ChromeTextRole.SectionDescription,
            tone = ChromeTone.Muted,
        )
        ChromeText(text = "Demo data", role = ChromeTextRole.Label)
        ChromeText(
            text = "${AuthSamples.GoodCode} is right and ${AuthSamples.ExpiredCode} has expired; anything " +
                "else is wrong. Sending a new code starts a one-minute cooldown.",
            role = ChromeTextRole.SectionDescription,
            tone = ChromeTone.Muted,
        )
    },
) {
    val channel by enumControl(
        key = "channel",
        default = CodeChannel.Email,
        options = CodeChannel.entries,
        optionLabel = { it.label },
    )
    val start by enumControl(
        key = "state",
        default = CodeStart.Entering,
        options = CodeStart.entries,
        optionLabel = { it.label },
        description = "Where the screen starts. From there it is live.",
    )
    val grouped by booleanControl("grouped", default = false, description = "Two groups of three, as a text message prints it.")

    preview {
        VerifyCodePreview(channel = channel, start = start, grouped = grouped)
    }
}

/**
 * The page's live preview: the code screen for [channel], started at [start].
 *
 * Internal rather than private so the render test can draw every channel and state.
 *
 * @param channel Where the code comes from.
 * @param start Where the screen starts.
 * @param grouped Whether the cells are split into two groups of three.
 */
@Composable
internal fun VerifyCodePreview(channel: CodeChannel, start: CodeStart, grouped: Boolean) {
    var state by remember(start, channel) { mutableStateOf(start.state(channel)) }

    LaunchedEffect(state.request) {
        if (state.request == 0 || !state.busy) return@LaunchedEffect
        delay(900.milliseconds)
        val complaint = channel.complaint(state.code)
        state = state.copy(busy = false, error = complaint, verified = complaint == null)
    }
    LaunchedEffect(state.cooldown) {
        if (state.cooldown <= 0) return@LaunchedEffect
        delay(1.seconds)
        state = state.copy(cooldown = state.cooldown - 1)
    }

    val verify = { code: String ->
        state = if (code.length < 6) {
            state.copy(error = "Enter the 6-digit code.")
        } else {
            state.copy(code = code, busy = true, request = state.request + 1)
        }
    }

    DeviceFrame(size = DeviceSize.Regular) {
        VerifyCodeRecipe(
            mark = channel.mark,
            title = channel.title,
            subtitle = if (state.verified) "Verified. Continuing…" else channel.subtitle,
            destination = channel.destination,
            code = state.code,
            codeError = state.error,
            busy = state.busy,
            sent = if (state.resent) "New code sent." else null,
            cooldown = state.cooldown.takeIf { it > 0 }?.let { "You can ask for another in 0:${it.toString().padStart(2, '0')}." },
            onCodeChange = { state = state.copy(code = it, error = null, verified = false) },
            onVerify = verify,
            onResend = if (channel.canResend) {
                { state = state.copy(code = "", error = null, resent = true, cooldown = 59) }
            } else {
                null
            },
            alternative = channel.alternative,
            onAlternative = {},
            onBack = {},
            groupSize = if (grouped) 3 else null,
            branding = AuthBranding(wordmark = { AuthSamples.Wordmark() }),
            windowInsets = DeviceSize.Regular.windowInsets,
        )
    }
}

/**
 * The recipe: what is being asked for, where the code went, the code, and the ways out.
 *
 * @param mark Glyph in the header's mark.
 * @param title What the screen asks for.
 * @param subtitle Where to find the code.
 * @param destination Where the code went — an address or a number — or `null` when it was not sent
 *   anywhere.
 * @param code Code as typed.
 * @param codeError The service's complaint, or `null`.
 * @param busy Whether the code is being verified.
 * @param sent Confirmation that a new code went out, worded, or `null`.
 * @param cooldown How long until another can be asked for, worded, or `null`.
 * @param onCodeChange Called on every edit.
 * @param onVerify Called with the code — by the last digit, or by the button.
 * @param onResend Called to send a new code, or `null` when one cannot be sent.
 * @param alternative Another way in — "Use a recovery code instead" — or `null`.
 * @param onAlternative Called when the alternative is pressed.
 * @param onBack Called to leave the screen.
 * @param groupSize Cells per group, or `null` for one run.
 * @param branding Wordmark, hero and layout.
 * @param windowInsets Regions the system occupies.
 */
@Composable
private fun VerifyCodeRecipe(
    mark: ImageVector,
    title: String,
    subtitle: String,
    destination: String?,
    code: String,
    codeError: String?,
    busy: Boolean,
    sent: String?,
    cooldown: String?,
    onCodeChange: (String) -> Unit,
    onVerify: (String) -> Unit,
    onResend: (() -> Unit)?,
    alternative: String?,
    onAlternative: () -> Unit,
    onBack: () -> Unit,
    groupSize: Int?,
    branding: AuthBranding,
    windowInsets: WindowInsets,
) {
    val codeFocus = remember { FocusRequester() }
    LaunchedEffect(Unit) { codeFocus.requestFocus() }

    AuthScaffold(
        branding = branding,
        windowInsets = windowInsets,
        topBar = { TopBar(modifier = Modifier.fillMaxWidth(), leading = { BackAction(onClick = onBack) }) {} },
    ) {
        AuthHeader(title = title, subtitle = subtitle, icon = { AuthSamples.Mark(mark) })

        Spacer(Modifier.height(ArecaTheme.spacing.xl6))

        if (destination != null) {
            AuthIdentityChip(identifier = destination, onChange = onBack, enabled = !busy)

            Spacer(Modifier.height(ArecaTheme.spacing.xl))
        }

        AuthCodeField(
            value = code,
            onValueChange = onCodeChange,
            onComplete = onVerify,
            error = codeError,
            enabled = !busy,
            focusRequester = codeFocus,
            groupSize = groupSize,
        )

        if (onResend != null) {
            Spacer(Modifier.height(ArecaTheme.spacing.xl))

            AuthResendCode(
                onResend = onResend,
                prompt = "Didn't get it?",
                sent = sent,
                cooldown = cooldown,
                enabled = !busy,
            )
        }

        Spacer(Modifier.height(ArecaTheme.spacing.xl2))

        Button(
            text = "Verify",
            onClick = { onVerify(code) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !busy,
            loading = busy,
        )

        if (alternative != null) {
            Spacer(Modifier.height(ArecaTheme.spacing.xl))

            AuthTextLink(text = alternative, onClick = onAlternative, enabled = !busy)
        }
    }
}

/**
 * Where a code comes from, and the words and ways out that go with it.
 *
 * @property label Name in the control.
 * @property mark Glyph in the header's mark.
 * @property title What the screen asks for.
 * @property subtitle Where to find the code.
 * @property destination Where the code went, or `null` when it was not sent.
 * @property canResend Whether another code can be asked for.
 * @property alternative Another way in, or `null`.
 * @property wrongCode How the service words a code that does not match.
 * @property expiredCode How the service words a code that has run out.
 */
internal enum class CodeChannel(
    val label: String,
    val mark: ImageVector,
    val title: String,
    val subtitle: String,
    val destination: String?,
    val canResend: Boolean,
    val alternative: String?,
    val wrongCode: String,
    val expiredCode: String,
) {
    /** A code sent to an address. */
    Email(
        label = "Email",
        mark = ArecaIcons.Mail,
        title = "Check your email",
        subtitle = "Enter the 6-digit code we sent to your email. It expires in 10 minutes.",
        destination = AuthSamples.Account,
        canResend = true,
        alternative = null,
        wrongCode = AuthSamples.WrongCodeMessage,
        expiredCode = AuthSamples.ExpiredCodeMessage,
    ),

    /** A code sent to a phone number. */
    TextMessage(
        label = "Text message",
        mark = ArecaIcons.MessageSquare,
        title = "Check your messages",
        subtitle = "Enter the 6-digit code we texted to your phone. It expires in 5 minutes.",
        destination = "+44 7700 900123",
        canResend = true,
        alternative = "Call me with a code instead",
        wrongCode = "That code isn't right. Check the latest text and try again.",
        expiredCode = "This code has expired. Send a new one below.",
    ),

    /** A code read off an authenticator app. */
    Authenticator(
        label = "Authenticator",
        mark = ArecaIcons.ShieldTick,
        title = "Two-step verification",
        subtitle = "Enter the 6-digit code from your authenticator app.",
        destination = null,
        canResend = false,
        alternative = "Use a recovery code instead",
        wrongCode = "That code isn't right. Check the clock on your phone is set automatically.",
        expiredCode = "That code has changed. Enter the one your app shows now.",
    ),
    ;

    /**
     * The service's verdict on a complete code, in this channel's words.
     *
     * @param code Code as typed.
     * @return The complaint, worded, or `null` when the code is right.
     */
    fun complaint(code: String): String? = when (code) {
        AuthSamples.GoodCode -> null
        AuthSamples.ExpiredCode -> expiredCode
        else -> wrongCode
    }
}

/**
 * What the sample application holds for a code screen.
 *
 * @property code Code as typed.
 * @property error The service's complaint, or `null`.
 * @property busy Whether the code is being verified.
 * @property verified Whether the last code was right.
 * @property resent Whether a new code was just sent.
 * @property cooldown Seconds until another code can be asked for.
 * @property request Increments with every verification, so the service answers each once. Zero for
 *   a state the page started in.
 */
internal data class CodeScreenState(
    val code: String = "",
    val error: String? = null,
    val busy: Boolean = false,
    val verified: Boolean = false,
    val resent: Boolean = false,
    val cooldown: Int = 0,
    val request: Int = 0,
)

/**
 * Where the page's code screen can start.
 *
 * @property label Name in the control.
 */
internal enum class CodeStart(val label: String) {
    /** Nothing typed yet. */
    Entering("Entering"),

    /** A wrong code. */
    Wrong("Wrong code"),

    /** An expired code. */
    Expired("Expired"),

    /** A code being checked. */
    Verifying("Verifying"),

    /** A new code just went out. An authenticator cannot send one, so it starts empty. */
    Resent("Just resent"),
    ;

    /**
     * The state the screen starts in for a channel.
     *
     * @param channel Where the code comes from, which words the complaints.
     * @return The starting state.
     */
    fun state(channel: CodeChannel): CodeScreenState = when (this) {
        Entering -> CodeScreenState()
        Wrong -> CodeScreenState(code = "482913", error = channel.complaint("482913"))
        Expired -> CodeScreenState(code = AuthSamples.ExpiredCode, error = channel.complaint(AuthSamples.ExpiredCode))
        Verifying -> CodeScreenState(code = AuthSamples.GoodCode, busy = true)
        Resent -> if (channel.canResend) CodeScreenState(resent = true, cooldown = 59) else CodeScreenState()
    }
}
