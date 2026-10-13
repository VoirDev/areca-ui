package dev.voir.areca.foundation.avatar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.foundation.LocalTextStyle
import dev.voir.areca.foundation.feedback.LocalSpinnerSize
import dev.voir.areca.foundation.feedback.Spinner
import dev.voir.areca.foundation.feedback.SpinnerSize
import dev.voir.areca.foundation.icon.FeaturedIconSize
import dev.voir.areca.foundation.icon.LocalIconSize
import dev.voir.areca.foundation.layout.Overlay
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.theme.ArecaTheme

/**
 * Colors for [Avatar].
 *
 * The loading pair is separate from the resting pair because the overlay dims whatever is underneath
 * — an image, initials, a placeholder icon — so the spinner has to read against the overlay rather
 * than against [bg].
 *
 * @property bg Fill behind initials, an icon, or a still-loading image.
 * @property content Color of initials and icons.
 * @property spinnerOverlay Scrim drawn over the avatar while loading.
 * @property spinner Color of the loading spinner, which sits on [spinnerOverlay].
 */
@Immutable
public data class AvatarStyle(
    public val bg: Color,
    public val content: Color,
    public val spinnerOverlay: Color,
    public val spinner: Color,
) {
    /** The theme default and the factory for variations on it. */
    public companion object {
        /** Theme default: a muted fill with secondary-foreground initials. */
        public val Default: AvatarStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme.
         *
         * @param bg Fill behind the avatar's content.
         * @param content Color of initials and icons.
         * @param spinnerOverlay Scrim drawn while loading.
         * @param spinner Color of the loading spinner.
         * @return Style with the given colors.
         */
        @Composable
        public fun default(
            bg: Color = ArecaTheme.colors.bgMuted,
            content: Color = ArecaTheme.colors.fgSubtle,
            spinnerOverlay: Color = ArecaTheme.colors.bgOverlayDark,
            spinner: Color = ArecaTheme.colors.fgOnFill,
        ): AvatarStyle = AvatarStyle(
            bg = bg,
            content = content,
            spinnerOverlay = spinnerOverlay,
            spinner = spinner,
        )
    }
}

/**
 * Container, icon, text, and spinner sizes for one step of [Avatar].
 *
 * Four dimensions move together because an avatar is a container for whichever of the three kinds of
 * content it has. The container ladder matches [FeaturedIconSize], so an avatar and a featured icon
 * in the same row line up.
 *
 * Initials are always semi-bold: at avatar sizes a regular weight disappears against the fill.
 *
 * @property text Style used for initials.
 * @property icon Size provided to icon content through [LocalIconSize].
 * @property container Square avatar size.
 * @property spinner Size of the loading spinner.
 */
@Immutable
public data class AvatarSize(
    public val text: TextStyle,
    public val icon: Dp,
    public val container: Dp,
    public val spinner: SpinnerSize,
) {
    /** The theme default and the steps of the avatar ramp, smallest first. */
    public companion object {
        /** Theme default: [Medium]. */
        public val Default: AvatarSize
            @Composable
            get() = Medium

        /** 20dp. Inline with text, or stacked in a dense participant list. */
        public val Tiny: AvatarSize
            @Composable
            get() = AvatarSize(
                container = 20.dp,
                icon = 12.dp,
                text = ArecaTheme.baseTypography.xs.merge(fontWeight = FontWeight.SemiBold),
                spinner = SpinnerSize(10.dp),
            )

        /** 32dp. Leading slot of a dense list row. */
        public val ExtraSmall: AvatarSize
            @Composable
            get() = AvatarSize(
                container = 32.dp,
                icon = 18.dp,
                text = ArecaTheme.baseTypography.sm.merge(fontWeight = FontWeight.SemiBold),
                spinner = SpinnerSize(14.dp),
            )

        /** 40dp. Leading slot of a standard list row. */
        public val Small: AvatarSize
            @Composable
            get() = AvatarSize(
                container = 40.dp,
                icon = 22.dp,
                text = ArecaTheme.baseTypography.sm.merge(fontWeight = FontWeight.SemiBold),
                spinner = SpinnerSize(16.dp),
            )

        /** 48dp. A profile header or a card's subject; the [Default]. */
        public val Medium: AvatarSize
            @Composable
            get() = AvatarSize(
                container = 48.dp,
                icon = 26.dp,
                text = ArecaTheme.baseTypography.sm.merge(fontWeight = FontWeight.SemiBold),
                spinner = SpinnerSize(16.dp),
            )

        /** 56dp. A prominent profile header. */
        public val Large: AvatarSize
            @Composable
            get() = AvatarSize(
                container = 56.dp,
                icon = 26.dp,
                text = ArecaTheme.baseTypography.md.merge(fontWeight = FontWeight.SemiBold),
                spinner = SpinnerSize(20.dp),
            )

        /** 64dp. A screen whose subject is the person. */
        public val ExtraLarge: AvatarSize
            @Composable
            get() = AvatarSize(
                container = 64.dp,
                icon = 28.dp,
                text = ArecaTheme.baseTypography.lg.merge(fontWeight = FontWeight.SemiBold),
                spinner = SpinnerSize(24.dp),
            )
    }
}

/**
 * A person or entity shown as a small shaped image, with graceful fallbacks.
 *
 * Three kinds of content, in priority order: a [content] slot for a real image, [letters] as
 * initials when there is no image, and nothing at all when there is neither. The slot is a slot
 * rather than a URL so the avatar stays independent of any image-loading library — pass Coil, Kamel,
 * or a plain `Image` into it, and it is clipped and sized for you.
 *
 * [loading] draws a scrim and spinner over whatever is underneath, so an avatar that is refreshing
 * keeps showing its old content rather than collapsing to an empty box.
 *
 * Initials are never read out as text: two letters are not a name. With a [contentDescription] the
 * avatar is one image node carrying it; without one the avatar is decorative and the initials are
 * hidden from assistive technology, which is right when the name is shown beside it.
 *
 * @param modifier Modifier applied to the avatar container.
 * @param style Fill, foreground, and loading colors. Defaults to the theme's.
 * @param size Container, icon, text, and spinner sizes. Defaults to the theme's, or
 *   [AvatarSize.Default].
 * @param shape Shape the avatar and its content are clipped to.
 * @param letters Initials to show when [content] is absent. Reduced to at most two characters — see
 *   [initialsOf].
 * @param contentDescription Who or what the avatar represents, read by assistive technology. `null`
 *   when the name is already shown beside it, which makes the initials decorative.
 * @param loading Whether to draw the loading scrim and spinner over the content.
 * @param content Image or custom content, clipped to [shape] and sized to the container.
 */
@Composable
public fun Avatar(
    modifier: Modifier = Modifier,
    style: AvatarStyle = LocalAvatarStyle.current ?: AvatarStyle.Default,
    size: AvatarSize = LocalAvatarSize.current ?: AvatarSize.Default,
    shape: Shape = ArecaTheme.shapes.full,
    letters: String? = null,
    contentDescription: String? = null,
    loading: Boolean = false,
    content: (@Composable BoxScope.() -> Unit)? = null,
) {
    CompositionLocalProvider(
        LocalContentColor provides style.content,
        LocalTextStyle provides size.text,
        LocalIconSize provides size.icon,
    ) {
        Box(
            modifier = modifier
                .size(size.container)
                .clip(shape)
                .background(style.bg)
                .then(
                    if (contentDescription != null) {
                        // clearAndSet, so initials are not read out as text on top of the label.
                        Modifier.clearAndSetSemantics {
                            role = Role.Image
                            this.contentDescription = contentDescription
                        }
                    } else {
                        Modifier
                    }
                ),
            contentAlignment = Alignment.Center,
        ) {
            if (content != null) {
                content()
            } else if (!letters.isNullOrBlank()) {
                Text(
                    text = initialsOf(letters),
                    // Decorative: the avatar's description, or the name beside it, says who it is.
                    modifier = Modifier.clearAndSetSemantics {},
                    maxLines = 1,
                )
            }

            if (loading) {
                CompositionLocalProvider(
                    LocalSpinnerSize provides size.spinner,
                    LocalContentColor provides style.spinner,
                ) {
                    Overlay(color = style.spinnerOverlay) {
                        Spinner(modifier = Modifier.align(Alignment.Center))
                    }
                }
            }
        }
    }
}

/** Default [AvatarSize] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalAvatarSize: ProvidableCompositionLocal<AvatarSize?> =
    compositionLocalOf { null }

/** Default [AvatarStyle] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalAvatarStyle: ProvidableCompositionLocal<AvatarStyle?> =
    compositionLocalOf { null }

/**
 * Reduces [letters] to at most two uppercase initials.
 *
 * Words are split on whitespace and only their letters and digits count, so punctuation, emoji and
 * other symbols never become an initial. A multi-word value gives the first letter of its first and
 * last word, so "Jean-Luc Picard" reads "JP". A single word gives its first two letters, which is
 * what keeps a username or a product name legible. Hyphens are not split on: a double-barrelled
 * first name is one name, and "JL" for "Jean-Luc Picard" would name the wrong person.
 *
 * Characters outside the Basic Multilingual Plane are skipped along with the symbols, because a
 * `Char` holds only half of one.
 *
 * @param letters Name, username, or already-abbreviated initials.
 * @return One or two uppercase characters, or an empty string when [letters] has no letters or
 *   digits.
 */
internal fun initialsOf(letters: String): String {
    val words = letters
        .split(WHITESPACE)
        .map { word -> word.filter(Char::isLetterOrDigit) }
        .filter { it.isNotEmpty() }
    return when {
        words.isEmpty() -> ""
        words.size == 1 -> words[0].take(2).uppercase().take(2)
        // Each initial is cut to one character after uppercasing, since "ß" uppercases to "SS".
        else -> words.first().take(1).uppercase().take(1) + words.last().take(1).uppercase().take(1)
    }
}

/** Runs of whitespace that separate the words of a name. */
private val WHITESPACE = Regex("\\s+")
