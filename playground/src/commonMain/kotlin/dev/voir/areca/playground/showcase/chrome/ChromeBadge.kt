package dev.voir.areca.playground.showcase.chrome

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.voir.areca.theme.ArecaTheme

/**
 * Meanings a shell badge carries.
 */
enum class ChromeBadgeTone {
    /** Owning module, or another purely factual label. */
    Neutral,

    /** Stable API. */
    Positive,

    /** Beta or in-progress API. */
    Caution,

    /** Deprecated API. */
    Critical,
}

/**
 * A small status pill in the showcase shell — a page's module and API status.
 *
 * @param text Badge text.
 * @param modifier Modifier applied to the badge.
 * @param tone Meaning conveyed by the badge.
 */
@Composable
fun ChromeBadge(
    text: String,
    modifier: Modifier = Modifier,
    tone: ChromeBadgeTone = ChromeBadgeTone.Neutral,
) {
    val shape = RoundedCornerShape(percent = 50)

    Box(
        modifier = modifier
            .clip(shape)
            .background(tone.background, shape)
            .padding(horizontal = ArecaTheme.spacing.md, vertical = 3.dp),
    ) {
        ChromeText(
            text = text,
            role = ChromeTextRole.Detail,
            tone = tone.contentTone,
            maxLines = 1,
        )
    }
}

private val ChromeBadgeTone.background: Color
    @Composable
    @ReadOnlyComposable
    get() = when (this) {
        ChromeBadgeTone.Neutral -> ArecaTheme.colors.bgNeutralMuted
        ChromeBadgeTone.Positive -> ArecaTheme.colors.bgSuccessMuted
        ChromeBadgeTone.Caution -> ArecaTheme.colors.bgWarnMuted
        ChromeBadgeTone.Critical -> ArecaTheme.colors.bgDangerMuted
    }

private val ChromeBadgeTone.contentTone: ChromeTone
    get() = when (this) {
        ChromeBadgeTone.Neutral -> ChromeTone.Secondary
        else -> ChromeTone.Primary
    }
