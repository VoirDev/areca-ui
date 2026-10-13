package dev.voir.areca.flow.auth.part

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import dev.voir.areca.flow.auth.icon.AlertCircle
import dev.voir.areca.flow.auth.icon.BundledIcons
import dev.voir.areca.foundation.AnnounceOnChange
import dev.voir.areca.foundation.notice.AnimatedNotice
import dev.voir.areca.foundation.notice.Notice
import dev.voir.areca.foundation.notice.NoticeEmphasis
import dev.voir.areca.foundation.notice.NoticeIconPlacement
import dev.voir.areca.foundation.notice.NoticeSize
import dev.voir.areca.foundation.notice.NoticeText
import dev.voir.areca.foundation.notice.NoticeTone
import dev.voir.areca.theme.ArecaTheme

/**
 * Why the last attempt failed, shown just above the control that will be pressed again.
 *
 * A banner rather than a toast, because this describes a state the form is in and not an event that
 * passed. It has to still be there when somebody looks up from the keyboard, and it has to sit next
 * to the button it is about. A toast satisfies neither.
 *
 * The failure is announced to assistive technology when it arrives and whenever it changes,
 * through the platform's announcement API on Android and iOS (see [AnnounceOnChange]); a live
 * region would stay silent, because the banner's node is created with its message. Sighted users
 * get the same information from the colour and the icon; this is what gives everybody else the
 * equivalent. Once shown, the banner is one node to a screen reader, read as [contentDescription]
 * words it — the same text that was announced.
 *
 * Most failures are a sentence: the password was wrong, the service could not be reached. A
 * failure that stops the form rather than the attempt — too many tries, an account that is locked —
 * earns a [title] over the sentence and a [NoticeTone.Warning] tone, because the reader is being
 * told to wait rather than to fix something.
 *
 * @param message Failure to explain, or `null` when the last attempt did not fail.
 * @param modifier Modifier applied to the banner.
 * @param title Headline over the message — "Too many attempts" — or `null` for the message alone.
 * @param tone What the banner is saying. [NoticeTone.Danger] for a failed attempt,
 *   [NoticeTone.Warning] for a form that has to wait.
 * @param icon Glyph beside the message. Pass a clock for a wait, say, rather than an alert.
 * @param contentDescription What assistive technology reads for the banner, and announces when it
 *   appears, given [title] and the message. A translation decides how the two join and whether a
 *   word such as "Error" leads them. Defaults to the title and the message as two English
 *   sentences.
 */
@Composable
public fun AuthBanner(
    message: String?,
    modifier: Modifier = Modifier,
    title: String? = null,
    tone: NoticeTone = NoticeTone.Danger,
    icon: ImageVector = BundledIcons.AlertCircle,
    contentDescription: (title: String?, message: String) -> String = ::defaultBannerDescription,
) {
    AnnounceOnChange(message?.let { contentDescription(title, it) })

    AnimatedNotice(value = message, modifier = modifier) { shown ->
        val description = contentDescription(title, shown)
        Column(modifier = Modifier.fillMaxWidth()) {
            // Filled whatever the tone: the banner is a state the form is in, and a subtle notice
            // reads as an aside. The glyph stays beside the copy even under a title, so a titled
            // banner is one line taller rather than a different shape.
            Notice(
                title = title,
                icon = icon,
                tone = tone,
                emphasis = NoticeEmphasis.Filled,
                size = NoticeSize.default(
                    padding = PaddingValues(ArecaTheme.spacing.lg),
                    iconGap = ArecaTheme.spacing.md,
                    iconPlacement = NoticeIconPlacement.Start,
                ),
                // Cleared and replaced, so the title and the text beneath are not read a second
                // time after the description that already holds them.
                modifier = Modifier.clearAndSetSemantics {
                    this.contentDescription = description
                },
            ) {
                NoticeText(shown)
            }

            Spacer(Modifier.height(ArecaTheme.spacing.xl))
        }
    }
}

/**
 * What a screen reader says for a banner the application has not worded: the title and the
 * message as two English sentences.
 *
 * @param title Headline, or `null` for none.
 * @param message Failure being explained.
 * @return The title and the message, each closed as a sentence when there is a title.
 */
internal fun defaultBannerDescription(title: String?, message: String): String =
    if (title == null) message else "$title. $message"
