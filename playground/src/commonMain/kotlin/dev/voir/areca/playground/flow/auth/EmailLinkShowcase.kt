package dev.voir.areca.playground.flow.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.voir.areca.flow.auth.AuthBranding
import dev.voir.areca.flow.auth.part.AuthBanner
import dev.voir.areca.flow.auth.part.AuthHeader
import dev.voir.areca.flow.auth.part.AuthIdentityChip
import dev.voir.areca.flow.auth.part.AuthPasswordField
import dev.voir.areca.flow.auth.part.AuthPasswordRequirements
import dev.voir.areca.flow.auth.part.AuthProgress
import dev.voir.areca.flow.auth.part.AuthScaffold
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.feedback.RequirementState
import dev.voir.areca.foundation.feedback.Skeleton
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.foundation.text.TextColor
import dev.voir.areca.foundation.text.TextVariant
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.Check
import dev.voir.areca.icons.Clock
import dev.voir.areca.icons.LinkBroken
import dev.voir.areca.icons.Lock
import dev.voir.areca.icons.Mail
import dev.voir.areca.icons.UserCheck
import dev.voir.areca.icons.UserPlus
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

/**
 * The pages an emailed link opens: set a new password, finish registering, accept an invitation,
 * confirm a new address.
 *
 * Four flows, one shape — the link is checked, the page asks for the one thing the link was for, and
 * then says how it went — so four recipes cover every state: [LinkCheckingRecipe] while the link is
 * checked, [LinkPasswordRecipe] and [LinkConfirmEmailRecipe] for the ask, and [LinkOutcomeRecipe]
 * for every ending, good or bad.
 */
@Composable
fun EmailLinkShowcase() = FlowShowcase(
    title = "Email link",
    summary = "The pages an emailed link opens — reset, registration, invitation, email change — " +
        "from checking the link to how it ended.",
    module = "flow-auth",
    status = ShowcaseStatus.Beta,
    notes = {
        ChromeText(text = "The recipes", role = ChromeTextRole.Label)
        ChromeText(
            text = "LinkCheckingRecipe, LinkPasswordRecipe, LinkConfirmEmailRecipe and LinkOutcomeRecipe " +
                "in this file. The words for each flow are the application's, in LinkFlow.",
            role = ChromeTextRole.SectionDescription,
            tone = ChromeTone.Muted,
        )
        ChromeText(
            text = "The rules under the password are quiet until the first submit, then every unmet " +
                "one turns red. A password that meets them all succeeds.",
            role = ChromeTextRole.SectionDescription,
            tone = ChromeTone.Muted,
        )
    },
) {
    val flow by enumControl(
        key = "flow",
        default = LinkFlow.Reset,
        options = LinkFlow.entries,
        optionLabel = { it.label },
    )
    val step by enumControl(
        key = "step",
        default = LinkStep.Form,
        options = LinkStep.entries,
        optionLabel = { it.label },
        description = "Where the page starts. The form is live from there.",
    )
    val autoplay by booleanControl(
        key = "autoplay",
        default = false,
        description = "From Checking, open the form after a moment, as a real link would.",
    )

    preview {
        EmailLinkPreview(flow = flow, step = step, autoplay = autoplay)
    }
}

/**
 * The page's live preview: the page [flow]'s link opens, starting at [step].
 *
 * Internal rather than private so the render test can draw every flow and step.
 *
 * @param flow Which link was opened.
 * @param step Where the page starts.
 * @param autoplay Whether a page that starts by checking the link opens the form by itself.
 */
@Composable
internal fun EmailLinkPreview(flow: LinkFlow, step: LinkStep, autoplay: Boolean) {
    var state by remember(flow, step) { mutableStateOf(step.state) }

    LaunchedEffect(state.step, autoplay) {
        when {
            state.step == LinkStep.Checking && autoplay -> {
                delay(1600.milliseconds)
                state = LinkState()
            }
            state.request > 0 && state.step == LinkStep.Submitting -> {
                delay(1500.milliseconds)
                state = state.copy(step = LinkStep.Success)
            }
        }
    }

    val branding = AuthBranding(wordmark = { AuthSamples.Wordmark() })
    val insets = DeviceSize.Regular.windowInsets
    val submit = {
        val refused = flow.asksForPassword && AuthSamples.passwordRequirements(state.password, checked = true)
            .any { it.state != RequirementState.Met }
        state = if (refused) {
            state.copy(step = LinkStep.Refused)
        } else {
            state.copy(step = LinkStep.Submitting, request = state.request + 1)
        }
    }

    DeviceFrame(size = DeviceSize.Regular) {
        when (state.step) {
            LinkStep.Checking -> LinkCheckingRecipe(branding = branding, windowInsets = insets)

            LinkStep.Form, LinkStep.Refused, LinkStep.Submitting, LinkStep.Failed -> if (flow.asksForPassword) {
                LinkPasswordRecipe(
                    flow = flow,
                    email = AuthSamples.Account,
                    password = state.password,
                    passwordError = if (state.step == LinkStep.Refused) {
                        "Choose a password that meets every rule below."
                    } else {
                        null
                    },
                    requirementsChecked = state.step == LinkStep.Refused,
                    failure = if (state.step == LinkStep.Failed) flow.failure else null,
                    busy = state.step == LinkStep.Submitting,
                    onPasswordChange = {
                        state = state.copy(
                            password = it,
                            step = if (state.step == LinkStep.Refused) LinkStep.Refused else LinkStep.Form,
                        )
                    },
                    onSubmit = submit,
                    branding = branding,
                    windowInsets = insets,
                )
            } else {
                LinkConfirmEmailRecipe(
                    flow = flow,
                    newEmail = NewEmail,
                    failure = if (state.step == LinkStep.Failed) flow.failure else null,
                    busy = state.step == LinkStep.Submitting,
                    onConfirm = submit,
                    branding = branding,
                    windowInsets = insets,
                )
            }

            LinkStep.Success -> LinkOutcomeRecipe(
                mark = ArecaIcons.Check,
                markColor = ActionColor.Success,
                title = flow.doneTitle,
                subtitle = flow.doneSubtitle,
                button = "Open Areca",
                note = "You can close this tab.",
                branding = branding,
                windowInsets = insets,
            )

            LinkStep.Exists -> LinkOutcomeRecipe(
                mark = ArecaIcons.UserCheck,
                markColor = ActionColor.Neutral,
                title = if (flow == LinkFlow.Invite) "Sign in to accept" else "You already have an account",
                subtitle = if (flow == LinkFlow.Invite) {
                    "${AuthSamples.Account} already has an account. Open Areca and sign in to accept the invitation."
                } else {
                    "${AuthSamples.Account} is already registered. Sign in to Areca instead; you can reset " +
                        "your password there."
                },
                button = if (flow == LinkFlow.Invite) "Continue to Areca" else "Sign in to Areca",
                branding = branding,
                windowInsets = insets,
            )

            LinkStep.Expired -> LinkOutcomeRecipe(
                mark = ArecaIcons.LinkBroken,
                markColor = ActionColor.Neutral,
                title = "This link is no longer valid",
                subtitle = "It may have expired or already been used. Go back to Areca and ask for a new one.",
                button = "Open Areca",
                branding = branding,
                windowInsets = insets,
            )

            LinkStep.Incomplete -> LinkOutcomeRecipe(
                mark = ArecaIcons.LinkBroken,
                markColor = ActionColor.Neutral,
                title = "This link is incomplete",
                subtitle = "Part of the address is missing. Open the link from your email again, or copy " +
                    "the whole address into your browser.",
                button = "Open Areca",
                branding = branding,
                windowInsets = insets,
            )

            LinkStep.Limited -> LinkOutcomeRecipe(
                mark = ArecaIcons.Clock,
                markColor = ActionColor.Warning,
                title = "Too many attempts",
                subtitle = "For your security, wait a moment before trying again.",
                button = "Try again in 1:58",
                buttonEnabled = false,
                note = "Nothing was changed.",
                branding = branding,
                windowInsets = insets,
            )
        }
    }
}

/**
 * The recipe while a link is checked: the shape of the page to come, and what it is waiting on.
 *
 * Placeholders in the shape of the form rather than a spinner in the middle of a blank page, so
 * the form arrives where the eye already is.
 *
 * @param branding Wordmark, hero and layout.
 * @param windowInsets Regions the system occupies.
 */
@Composable
private fun LinkCheckingRecipe(
    branding: AuthBranding,
    windowInsets: WindowInsets,
) {
    AuthScaffold(
        branding = branding,
        windowInsets = windowInsets,
        footer = { AuthProgress(text = "Checking your link…") },
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
        ) {
            Skeleton(width = 48.dp, height = 48.dp)
            Spacer(Modifier.height(ArecaTheme.spacing.md))
            Skeleton(width = 220.dp, height = 28.dp)
            Skeleton(count = 2, textStyle = ArecaTheme.typography.bodySmall)
        }

        Spacer(Modifier.height(ArecaTheme.spacing.xl6))

        Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.sm)) {
            Skeleton(width = 64.dp, textStyle = ArecaTheme.typography.labelMedium)
            Skeleton(height = 48.dp)
            Spacer(Modifier.height(ArecaTheme.spacing.md))
            Skeleton(width = 96.dp, textStyle = ArecaTheme.typography.labelMedium)
            Skeleton(height = 48.dp)
            Spacer(Modifier.height(ArecaTheme.spacing.xl))
            Skeleton(height = 48.dp)
        }
    }
}

/**
 * The recipe for a link that sets a password: which account, the password and its rules.
 *
 * @param flow Which link this is, and its words.
 * @param email The account the link belongs to.
 * @param password Password as typed.
 * @param passwordError The application's complaint, or `null`.
 * @param requirementsChecked Whether a submit was refused, which turns unmet rules red.
 * @param failure Why the last submit failed, or `null`.
 * @param busy Whether the submit is in flight.
 * @param onPasswordChange Called on every edit.
 * @param onSubmit Called to submit.
 * @param branding Wordmark, hero and layout.
 * @param windowInsets Regions the system occupies.
 */
@Composable
private fun LinkPasswordRecipe(
    flow: LinkFlow,
    email: String,
    password: String,
    passwordError: String?,
    requirementsChecked: Boolean,
    failure: String?,
    busy: Boolean,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    branding: AuthBranding,
    windowInsets: WindowInsets,
) {
    AuthScaffold(
        branding = branding,
        windowInsets = windowInsets,
        footer = flow.note?.let { note ->
            { Text(text = note, variant = TextVariant.BodySmall, color = TextColor.Subtle, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) }
        },
    ) {
        AuthHeader(title = flow.title, subtitle = flow.subtitle, icon = { AuthSamples.Mark(flow.mark) })

        Spacer(Modifier.height(ArecaTheme.spacing.xl6))

        AuthBanner(message = failure, title = "Something went wrong", contentDescription = AuthSamples::errorDescription)

        // The account comes from the link, so it is shown rather than asked for.
        AuthIdentityChip(identifier = email, label = flow.accountLabel)

        Spacer(Modifier.height(ArecaTheme.spacing.xl))

        AuthPasswordField(
            value = password,
            onValueChange = onPasswordChange,
            error = passwordError,
            enabled = !busy,
            newAccount = true,
            label = flow.passwordLabel,
            imeAction = ImeAction.Done,
            onImeAction = onSubmit,
        )

        Spacer(Modifier.height(ArecaTheme.spacing.md))

        AuthPasswordRequirements(
            requirements = AuthSamples.passwordRequirements(password, checked = requirementsChecked),
            contentDescription = "Password requirements",
        )

        Spacer(Modifier.height(ArecaTheme.spacing.xl2))

        Button(
            text = if (busy) flow.busyAction else flow.action,
            onClick = onSubmit,
            modifier = Modifier.fillMaxWidth(),
            enabled = !busy,
            loading = busy,
        )
    }
}

/**
 * The recipe for a link that confirms a new address: the address, and one explicit press.
 *
 * Confirming takes a press rather than happening when the link opens, because mail scanners open
 * links too, and an address that changed because a scanner looked at the message is a takeover.
 *
 * @param flow Which link this is, and its words.
 * @param newEmail The address being confirmed.
 * @param failure Why the last confirmation failed, or `null`.
 * @param busy Whether the confirmation is in flight.
 * @param onConfirm Called to confirm.
 * @param branding Wordmark, hero and layout.
 * @param windowInsets Regions the system occupies.
 */
@Composable
private fun LinkConfirmEmailRecipe(
    flow: LinkFlow,
    newEmail: String,
    failure: String?,
    busy: Boolean,
    onConfirm: () -> Unit,
    branding: AuthBranding,
    windowInsets: WindowInsets,
) {
    AuthScaffold(
        branding = branding,
        windowInsets = windowInsets,
        footer = flow.note?.let { note ->
            { Text(text = note, variant = TextVariant.BodySmall, color = TextColor.Subtle, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) }
        },
    ) {
        AuthHeader(title = flow.title, subtitle = flow.subtitle, icon = { AuthSamples.Mark(flow.mark) })

        Spacer(Modifier.height(ArecaTheme.spacing.xl6))

        AuthBanner(message = failure, title = "Something went wrong", contentDescription = AuthSamples::errorDescription)

        AuthIdentityChip(identifier = newEmail, label = flow.accountLabel)

        Spacer(Modifier.height(ArecaTheme.spacing.xl2))

        Button(
            text = if (busy) flow.busyAction else flow.action,
            onClick = onConfirm,
            modifier = Modifier.fillMaxWidth(),
            enabled = !busy,
            loading = busy,
        )
    }
}

/**
 * The recipe for how a link ended: done, already done, dead, or refused for a while.
 *
 * @param mark Glyph in the header's mark.
 * @param markColor Tone: success when it worked, warning for a wait, neutral otherwise.
 * @param title What happened.
 * @param subtitle What to do about it.
 * @param button Caption of the way on.
 * @param branding Wordmark, hero and layout.
 * @param windowInsets Regions the system occupies.
 * @param buttonEnabled Whether the way on is open yet. A disabled button carries a clock, and its
 *   caption says how long.
 * @param note A last, quiet line, or `null` for none.
 */
@Composable
private fun LinkOutcomeRecipe(
    mark: ImageVector,
    markColor: ActionColor,
    title: String,
    subtitle: String,
    button: String,
    branding: AuthBranding,
    windowInsets: WindowInsets,
    buttonEnabled: Boolean = true,
    note: String? = null,
) {
    AuthScaffold(
        branding = branding,
        windowInsets = windowInsets,
        footer = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.lg),
            ) {
                Button(
                    text = button,
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    enabled = buttonEnabled,
                    leading = if (buttonEnabled) null else ({ Icon(imageVector = ArecaIcons.Clock, contentDescription = null) }),
                )
                note?.let { Text(text = it, variant = TextVariant.BodySmall, color = TextColor.Subtle) }
            }
        },
    ) {
        AuthHeader(title = title, subtitle = subtitle, icon = { AuthSamples.Mark(mark, markColor) })
    }
}

/**
 * Which link was opened, with every word its pages say.
 *
 * @property label Name in the control.
 * @property mark Glyph in the form's mark.
 * @property title What the form is for.
 * @property subtitle What it will do.
 * @property accountLabel What the account shown on the form is called.
 * @property asksForPassword Whether the form sets a password, rather than confirming an address.
 * @property passwordLabel Label of the password box.
 * @property action Caption of the button.
 * @property busyAction Caption of the button while the submit is in flight.
 * @property failure Why a submit failed, as the banner says it.
 * @property doneTitle Headline once it worked.
 * @property doneSubtitle What it means once it worked.
 * @property note A quiet line along the bottom edge, or `null`.
 */
internal enum class LinkFlow(
    val label: String,
    val mark: ImageVector,
    val title: String,
    val subtitle: String,
    val accountLabel: String,
    val asksForPassword: Boolean,
    val passwordLabel: String,
    val action: String,
    val busyAction: String,
    val failure: String,
    val doneTitle: String,
    val doneSubtitle: String,
    val note: String?,
) {
    /** A password reset. */
    Reset(
        label = "Reset",
        mark = ArecaIcons.Lock,
        title = "Set a new password",
        subtitle = "Choose a new password for your Areca account. You'll be signed out on every device.",
        accountLabel = "Account",
        asksForPassword = true,
        passwordLabel = "New password",
        action = "Update password",
        busyAction = "Updating password…",
        failure = "Your password wasn't changed. Check your connection and try again.",
        doneTitle = "Password updated",
        doneSubtitle = "Sign in to Areca with your new password.",
        note = "This link works once. Didn't ask for a reset? Close this page.",
    ),

    /** The last step of a registration that started with an address. */
    Register(
        label = "Register",
        mark = ArecaIcons.Mail,
        title = "Create your account",
        subtitle = "Your email is confirmed. Choose a password to finish signing up for Areca.",
        accountLabel = "Email",
        asksForPassword = true,
        passwordLabel = "Password",
        action = "Create account",
        busyAction = "Creating account…",
        failure = "Your account wasn't created. Check your connection and try again.",
        doneTitle = "Account created",
        doneSubtitle = "You can now sign in to Areca with ${AuthSamples.Account}.",
        note = null,
    ),

    /** An invitation to a product somebody has no account in yet. */
    Invite(
        label = "Invite",
        mark = ArecaIcons.UserPlus,
        title = "Join Areca",
        subtitle = "You've been invited to Areca. Create a password to accept the invitation.",
        accountLabel = "Email",
        asksForPassword = true,
        passwordLabel = "Password",
        action = "Accept invitation",
        busyAction = "Accepting invitation…",
        failure = "The invitation wasn't accepted. Check your connection and try again.",
        doneTitle = "You're in",
        doneSubtitle = "Your account is ready. Sign in to Areca with ${AuthSamples.Account}.",
        note = "This invitation expires on October 6, 2026.",
    ),

    /** A change of sign-in address. */
    EmailChange(
        label = "Email change",
        mark = ArecaIcons.Mail,
        title = "Confirm your new email",
        subtitle = "Areca will use this address to sign you in and to send account notices.",
        accountLabel = "New email",
        asksForPassword = false,
        passwordLabel = "",
        action = "Confirm email",
        busyAction = "Confirming…",
        failure = "Your email wasn't changed. Check your connection and try again.",
        doneTitle = "Email updated",
        doneSubtitle = "Use $NewEmail to sign in from now on.",
        note = "Didn't ask for this? Close this page. Nothing changes until you confirm.",
    ),
}

/**
 * The states a link page can be in.
 *
 * @property label Name in the control.
 */
internal enum class LinkStep(val label: String) {
    /** The link is being checked. */
    Checking("Checking"),

    /** The form, empty. */
    Form("Form"),

    /** The form, after a submit the rules refused. */
    Refused("Refused"),

    /** The submit is in flight. */
    Submitting("Submitting"),

    /** The submit failed on the way. */
    Failed("Failed"),

    /** It worked. */
    Success("Success"),

    /** The account already exists (409). */
    Exists("Already exists"),

    /** The link expired or was used. */
    Expired("Expired"),

    /** The link lost part of its address. */
    Incomplete("Incomplete"),

    /** Too many attempts (429). */
    Limited("Rate limited"),
    ;

    /** The state the page starts in at this step. */
    val state: LinkState
        get() = when (this) {
            Refused -> LinkState(step = this, password = "areca")
            Submitting, Failed -> LinkState(step = this, password = AuthSamples.Password)
            else -> LinkState(step = this)
        }
}

/**
 * What the sample application holds for a link page.
 *
 * @property step Where the page is.
 * @property password Password as typed.
 * @property request Increments with every submit, so the service answers each once. Zero for a
 *   state the page started in, which the service leaves alone.
 */
internal data class LinkState(
    val step: LinkStep = LinkStep.Form,
    val password: String = "",
    val request: Int = 0,
)

/** The address an email-change link confirms. */
private const val NewEmail = "ada.new@example.com"
