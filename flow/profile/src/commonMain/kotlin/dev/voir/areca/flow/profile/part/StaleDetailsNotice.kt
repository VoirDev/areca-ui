package dev.voir.areca.flow.profile.part

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.voir.areca.flow.profile.icon.BundledIcons
import dev.voir.areca.flow.profile.icon.CloudOff
import dev.voir.areca.foundation.icon.FeaturedIconSize
import dev.voir.areca.foundation.notice.Notice
import dev.voir.areca.foundation.notice.NoticeIconPlacement
import dev.voir.areca.foundation.notice.NoticeSize
import dev.voir.areca.foundation.notice.NoticeStyle
import dev.voir.areca.foundation.notice.NoticeText
import dev.voir.areca.foundation.notice.NoticeTone
import dev.voir.areca.foundation.settings.SettingsTheme
import dev.voir.areca.theme.ArecaTheme

/**
 * The band that says the details below were not just fetched.
 *
 * It heads the page rather than appearing over it as a toast, because it is not an event: it
 * describes everything below it for as long as it is there, and a toast that has faded leaves
 * somebody reading month-old details with nothing to tell them so.
 *
 * All of it is neutral — the fill and corner of a settings section, and a neutral glyph. The
 * screen's actual news is "this still works, just from memory", which should not be the loudest
 * thing on it.
 *
 * @param message Why the details could not be refreshed, already worded.
 * @param modifier Modifier applied to the band.
 * @param title Heading of the band.
 */
@Composable
public fun StaleDetailsNotice(
    message: String,
    modifier: Modifier = Modifier,
    title: String = "Showing saved details",
) {
    Notice(
        title = title,
        modifier = modifier,
        icon = BundledIcons.CloudOff,
        tone = NoticeTone.Neutral,
        // The ground and corner of the settings sections it heads, and its glyph beside the copy
        // rather than above it, so it reads as one more card in the list.
        style = NoticeStyle.of(
            tone = NoticeTone.Neutral,
            bg = SettingsTheme.styles.section.bg,
            shape = SettingsTheme.styles.section.shape,
            chipShape = ArecaTheme.shapes.tile,
        ),
        size = NoticeSize.default(
            chip = FeaturedIconSize.ExtraSmall,
            iconPlacement = NoticeIconPlacement.Start,
        ),
    ) {
        NoticeText(message)
    }
}
