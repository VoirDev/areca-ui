package dev.voir.areca.flow.profile.row

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.voir.areca.foundation.AnnounceOnChange
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.theme.ArecaTheme

/** What the service has said about a username, as the footer under its field shows it. */
public enum class UsernameCheck {
    /** Nothing asked yet: the footer shows its hint. */
    Idle,

    /** Asked, and not answered yet. */
    Checking,

    /** Free to take: the footer says so in the success colour. */
    Available,

    /**
     * Not to be had — held by another account, or reserved or refused by the service: the footer
     * says so in the danger colour.
     */
    Taken,
}

/**
 * The line under a username field that says whether the name can be had.
 *
 * For the `footer` of the `Section` holding the username's `SettingsTextFieldRow`: the verdict sits
 * under the field it is about. With a [hint] the line is there from the start, so a verdict replaces
 * it rather than adding one; with none, nothing is drawn until there is something to say, and the
 * first verdict adds the line. A verdict or complaint longer than the hint wraps onto more lines.
 *
 * Neutral while nothing is known — the [hint], or [checkingText] — and coloured only for the two
 * answers: success when the name is free, danger when it is taken. A [complaint] outranks the
 * verdict: whatever the application has to say against the name — its own rules, a failed save —
 * is the more useful line.
 *
 * Every answer — a verdict or a complaint — is announced to assistive technology as it arrives and
 * whenever it changes. The hint and the checking line are drawn but not announced, so typing does
 * not set off a stream of speech.
 *
 * The footer decides nothing. What a valid username is, and when to ask the service, are the
 * application's; this only draws the answer.
 *
 * @param username The username being checked, without the `@`.
 * @param check The service's verdict on [username].
 * @param modifier Modifier applied to the line.
 * @param complaint Something the application has against [username], already worded, or `null`
 *   for none. Drawn in the danger colour, in place of the verdict.
 * @param hint Line shown before anything is known — where a product states its rules for usernames
 *   — or `null` to show nothing until there is an answer.
 * @param checkingText Line while the service is asked.
 * @param availableText Line when the name is free. Receives the name; composable, so it can read the
 *   application's string resources.
 * @param takenText Line when the name is [UsernameCheck.Taken]. Receives the name; composable, like
 *   [availableText].
 */
@Composable
public fun UsernameCheckFooter(
    username: String,
    check: UsernameCheck,
    modifier: Modifier = Modifier,
    complaint: String? = null,
    hint: String? = null,
    checkingText: String = "Checking username…",
    availableText: @Composable (username: String) -> String = { "@$it is available." },
    takenText: @Composable (username: String) -> String = { "@$it is already taken." },
) {
    val colors = ArecaTheme.colors
    val line = usernameFooterLine(check = check, complaint = complaint, hint = hint)

    val (text, color) = when (line) {
        UsernameFooterLine.Complaint -> complaint to colors.fgDangerText
        UsernameFooterLine.Checking -> checkingText to LocalContentColor.current
        UsernameFooterLine.Available -> availableText(username) to colors.fgSuccessText
        UsernameFooterLine.Taken -> takenText(username) to colors.fgDangerText
        UsernameFooterLine.Hint -> hint to LocalContentColor.current
        UsernameFooterLine.None -> null to LocalContentColor.current
    }

    AnnounceOnChange(text.takeIf { line.isAnswer })

    if (text != null) {
        Text(text = text, modifier = modifier, color = color)
    }
}

/** Which line a [UsernameCheckFooter] draws. */
internal enum class UsernameFooterLine {
    /** The application's complaint, in the danger colour. */
    Complaint,

    /** The line saying the service is being asked. */
    Checking,

    /** The verdict that the name is free, in the success colour. */
    Available,

    /** The verdict that the name is taken, in the danger colour. */
    Taken,

    /** The product's hint, before anything is known. */
    Hint,

    /** Nothing: no verdict, no complaint and no hint. */
    None,
}

/**
 * Whether this line answers the question — a verdict or a complaint — and so is announced when it
 * arrives rather than only drawn.
 */
internal val UsernameFooterLine.isAnswer: Boolean
    get() = when (this) {
        UsernameFooterLine.Complaint, UsernameFooterLine.Available, UsernameFooterLine.Taken -> true
        UsernameFooterLine.Checking, UsernameFooterLine.Hint, UsernameFooterLine.None -> false
    }

/**
 * Picks the line a [UsernameCheckFooter] draws.
 *
 * A complaint outranks everything; then the service's state — checking, available, taken; then the
 * hint; then nothing.
 *
 * @param check The service's verdict.
 * @param complaint The application's complaint, or `null` for none.
 * @param hint The product's hint, or `null` for none.
 * @return The line to draw.
 */
internal fun usernameFooterLine(check: UsernameCheck, complaint: String?, hint: String?): UsernameFooterLine =
    when {
        complaint != null -> UsernameFooterLine.Complaint
        check == UsernameCheck.Checking -> UsernameFooterLine.Checking
        check == UsernameCheck.Available -> UsernameFooterLine.Available
        check == UsernameCheck.Taken -> UsernameFooterLine.Taken
        hint != null -> UsernameFooterLine.Hint
        else -> UsernameFooterLine.None
    }
