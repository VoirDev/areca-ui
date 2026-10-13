package dev.voir.areca.playground.flow.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.BrushPainter
import dev.voir.areca.flow.profile.AvatarImageLoader
import dev.voir.areca.flow.profile.AvatarImageState
import dev.voir.areca.flow.profile.ProfileSummary
import dev.voir.areca.flow.profile.session.SessionDevice
import dev.voir.areca.flow.profile.session.SessionItem
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.theme.ArecaTheme

/**
 * Demo content shared by every profile page — the recipe screens under Flows and the component pages
 * under Profile — so they document one account rather than many invented ones.
 */
internal object ProfileSamples {

    /**
     * The account every page shows.
     *
     * @param avatar Which sample picture to give it.
     * @param isVerified Whether its address is confirmed.
     * @return The summary.
     */
    fun summary(
        avatar: SampleAvatar = SampleAvatar.Picture,
        isVerified: Boolean = true,
    ): ProfileSummary = ProfileSummary(
        name = "Ada Lovelace",
        handle = "@ada",
        email = "ada@analytical.engine",
        isVerified = isVerified,
        avatar = avatar.takeUnless { it == SampleAvatar.None },
        caption = "Member since March 2024",
    )

    /** A bio of realistic length for the cover card and the edit form. */
    const val Bio: String = "Writing the first programs for a machine that does not exist yet. " +
        "London, occasionally Somerset."

    /** A second and third account signed in on the same device, for the account switcher. */
    val OtherAccounts: List<ProfileSummary> = listOf(
        ProfileSummary(name = "Analytical Society", handle = "@analytical", avatar = SampleAvatar.Picture),
        ProfileSummary(name = "Augusta King", email = "augusta@lovelace.family"),
    )

    /** Every device the sample account is signed in on, the current one first. */
    val Sessions: List<SessionItem> = listOf(
        SessionItem(
            id = "this",
            device = SessionDevice.Phone,
            name = "Pixel 9 Pro",
            client = "Areca for Android 12.10.3",
            seen = "Belgrade, Serbia · online",
            isCurrent = true,
        ),
        SessionItem(
            id = "laptop",
            device = SessionDevice.Desktop,
            name = "MacBook Pro",
            client = "Areca for macOS 7.2.9",
            seen = "Belgrade, Serbia · 10:18",
        ),
        SessionItem(
            id = "browser",
            device = SessionDevice.Browser,
            name = "Firefox",
            client = "Areca Web on Windows",
            seen = "Novi Sad, Serbia · Thu",
        ),
    )

    /** The one session the component pages show on its own. */
    val OtherSession: SessionItem = Sessions[1]

    /**
     * A provider mark standing in for a real one, which a product supplies under that provider's
     * brand terms.
     */
    @Composable
    fun ProviderMark() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ArecaTheme.colors.bgCanvas, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "G",
                style = ArecaTheme.typography.labelSmall,
                color = ArecaTheme.colors.fgBase,
            )
        }
    }

    /**
     * Cover artwork for the cover card: a quiet neutral gradient, standing in for a photograph a
     * reader would look at instead of the layout.
     */
    @Composable
    fun Cover() {
        Box(
            modifier = Modifier.fillMaxSize().background(
                Brush.linearGradient(
                    colors = listOf(
                        ArecaTheme.baseColors.neutral30,
                        ArecaTheme.baseColors.neutral50,
                    ),
                    start = Offset.Zero,
                    end = Offset.Infinite,
                ),
            ),
        )
    }
}

/**
 * Picture models the sample loader understands, one per appearance an avatar can take.
 *
 * @property label Name shown in the control.
 */
internal enum class SampleAvatar(val label: String) {
    /** No picture: initials. */
    None("none"),

    /** A picture that loads. */
    Picture("picture"),

    /** A picture still on its way: spinner over initials. */
    Loading("loading"),

    /** A picture that could not be drawn: initials. */
    Broken("broken"),
}

/**
 * Stands in for an application's image loader.
 *
 * Draws a gradient for [SampleAvatar.Picture] rather than fetching anything, so the playground runs
 * offline and shows the module's three appearances on demand. A cropped picture's model is the crop
 * itself, an [ImageBitmap], and is drawn as it is.
 */
internal object SampleAvatarLoader : AvatarImageLoader {
    /** @return The state the sample model stands for. */
    @Composable
    override fun load(model: Any): AvatarImageState {
        val start = ArecaTheme.baseColors.neutral40
        val end = ArecaTheme.baseColors.neutral70
        val picture = remember(start, end) {
            AvatarImageState.Ready(
                BrushPainter(
                    Brush.linearGradient(
                        colors = listOf(start, end),
                        start = Offset.Zero,
                        end = Offset.Infinite,
                    ),
                ),
            )
        }

        val cropped = remember(model) { (model as? ImageBitmap)?.let { AvatarImageState.Ready(BitmapPainter(it)) } }

        return when (model) {
            is ImageBitmap -> cropped ?: AvatarImageState.Failed
            SampleAvatar.Picture -> picture
            SampleAvatar.Loading -> AvatarImageState.Loading
            else -> AvatarImageState.Failed
        }
    }
}
