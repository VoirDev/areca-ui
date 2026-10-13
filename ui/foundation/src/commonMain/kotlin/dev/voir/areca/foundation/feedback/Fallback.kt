package dev.voir.areca.foundation.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.foundation.LocalTextStyle
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.icon.FeaturedIcon
import dev.voir.areca.foundation.icon.FeaturedIconSize
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.icon.IconSize
import dev.voir.areca.foundation.icon.LocalFeaturedIconSize
import dev.voir.areca.foundation.icon.LocalIconSize
import dev.voir.areca.theme.ArecaTheme

/**
 * Metrics and typography of a [Fallback].
 *
 * Two gaps rather than one: [gap] separates the block's three parts — mark, text, actions — while
 * [textGap] holds the heading and its caption together as a single unit. Deriving the second from
 * the first would tie the two apart, which is exactly what a block like this must not do.
 *
 * @property padding Space between the block's edges and its content.
 * @property gap Space between the mark, the text, and the actions.
 * @property textGap Space between the heading and its caption.
 * @property titleTextStyle Style given to the heading.
 * @property captionTextStyle Style given to the caption.
 */
@Immutable
public data class FallbackSize(
    public val padding: PaddingValues,
    public val gap: Dp,
    public val textGap: Dp,
    public val titleTextStyle: TextStyle,
    public val captionTextStyle: TextStyle,
) {
    /** The theme's default [FallbackSize], and [default] for building a variation on it. */
    public companion object {
        /** Theme default. */
        public val Default: FallbackSize
            @Composable
            get() = default()

        /**
         * Builds a size from the current theme.
         *
         * @param padding Space between the block's edges and its content.
         * @param gap Space between the mark, the text, and the actions.
         * @param textGap Space between the heading and its caption.
         * @param titleTextStyle Style given to the heading.
         * @param captionTextStyle Style given to the caption.
         * @return Metrics for a fallback.
         */
        @Composable
        public fun default(
            padding: PaddingValues = PaddingValues(ArecaTheme.spacing.xl),
            gap: Dp = ArecaTheme.spacing.lg,
            textGap: Dp = ArecaTheme.spacing.sm,
            titleTextStyle: TextStyle = ArecaTheme.typography.labelLarge.merge(
                textAlign = TextAlign.Center,
            ),
            captionTextStyle: TextStyle = ArecaTheme.typography.bodySmall.merge(
                textAlign = TextAlign.Center,
            ),
        ): FallbackSize = FallbackSize(
            padding = padding,
            gap = gap,
            textGap = textGap,
            titleTextStyle = titleTextStyle,
            captionTextStyle = captionTextStyle,
        )
    }
}

/**
 * Colors of a [Fallback].
 *
 * Transparent by default: the block states something about the surface it is on rather than being a
 * surface of its own, so it inherits whatever it is placed in.
 *
 * @property bg Fill behind the block.
 * @property title Color of the heading.
 * @property caption Color of the caption.
 */
@Immutable
public data class FallbackStyle(
    public val bg: Color,
    public val title: Color,
    public val caption: Color,
) {
    /** The theme's default [FallbackStyle], and [default] for building a variation on it. */
    public companion object {
        /** Theme default: no fill, primary heading over a secondary caption. */
        public val Default: FallbackStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme.
         *
         * @param bg Fill behind the block.
         * @param title Color of the heading.
         * @param caption Color of the caption.
         * @return Colors for a fallback.
         */
        @Composable
        public fun default(
            bg: Color = Color.Transparent,
            title: Color = ArecaTheme.colors.fgBase,
            caption: Color = ArecaTheme.colors.fgSubtle,
        ): FallbackStyle = FallbackStyle(
            bg = bg,
            title = title,
            caption = caption,
        )
    }
}

/** Default [FallbackSize] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalFallbackSize: ProvidableCompositionLocal<FallbackSize?> =
    compositionLocalOf { null }

/** Default [FallbackStyle] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalFallbackStyle: ProvidableCompositionLocal<FallbackStyle?> =
    compositionLocalOf { null }

/**
 * A centred block shown in place of content that is not there — explaining why, and offering a
 * way out.
 *
 * This is the fallback a screen renders when its own state says the content is missing: a load
 * failed, a list came back empty, the device is offline. It does not catch anything — Compose has no
 * way to recover from an exception thrown during composition — so the caller decides when to show
 * it, usually from its UI state.
 *
 * One component for both empty states and failures, because they are the same shape — a mark, a
 * sentence, an explanation, and a way out — and differ only in what each slot says. That shared
 * shape is what stops a product's "no results yet" and its "something went wrong" looking like two
 * unrelated screens.
 *
 * Every slot is optional, so the block degrades from all four down to a single line without a
 * branch at the call site. The mark slot is given a larger icon size than the surrounding screen,
 * since it carries the block.
 *
 * **Fallback or [dev.voir.areca.foundation.status.StatusScene].** A fallback stands *in for part of*
 * a screen: it fills the width it is given but only the height its content needs, takes composable
 * slots, and appears at once, because it is the content's resting state rather than an event. A
 * status scene *is* the screen, given over to one piece of work running or just ended — it fills the
 * space, takes plain strings, and stages its entrance. The two line up slot for slot: [top],
 * [heading], [body] and [content] are the scene's `mark`, `title`, `message` and `actions`.
 *
 * @param modifier Modifier applied to the block.
 * @param style Fill and text colors. Defaults to the theme's.
 * @param size Padding, gaps and type. Defaults to the theme's.
 * @param top The mark, normally an [Icon] or a [FeaturedIcon].
 * @param heading What happened, in a few words.
 * @param body Why it happened, or what to do about it.
 * @param content The way out, normally a [Button].
 */
@Composable
public fun Fallback(
    modifier: Modifier = Modifier,
    style: FallbackStyle = LocalFallbackStyle.current ?: FallbackStyle.Default,
    size: FallbackSize = LocalFallbackSize.current ?: FallbackSize.Default,
    top: (@Composable () -> Unit)? = null,
    heading: (@Composable () -> Unit)? = null,
    body: (@Composable () -> Unit)? = null,
    content: (@Composable () -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(style.bg)
            .padding(size.padding)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(size.gap),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (top != null) {
                CompositionLocalProvider(
                    LocalIconSize provides IconSize.Medium,
                    LocalFeaturedIconSize provides FeaturedIconSize.Large
                ) {
                    top()
                }
            }

            if (heading != null || body != null) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(size.textGap),
                ) {
                    if (heading != null) {
                        CompositionLocalProvider(
                            LocalContentColor provides style.title,
                            LocalTextStyle provides size.titleTextStyle
                        ) {
                            heading()
                        }
                    }
                    if (body != null) {
                        CompositionLocalProvider(
                            LocalContentColor provides style.caption,
                            LocalTextStyle provides size.captionTextStyle
                        ) {
                            body()
                        }
                    }
                }
            }

            content?.invoke()
        }
    }
}

