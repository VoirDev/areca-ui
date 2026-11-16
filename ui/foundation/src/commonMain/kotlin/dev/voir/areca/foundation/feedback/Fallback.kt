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
import androidx.compose.ui.graphics.vector.ImageVector
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.icon.FeaturedIconStyle
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.icon.AlertCircle
import dev.voir.areca.icon.BundledIcons
import dev.voir.areca.icon.Tray
import androidx.compose.ui.semantics.heading as headingRole
import androidx.compose.ui.semantics.semantics
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height

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
 * @property actionGap Space between the text and the way out; defaults to [gap].
 * @property captionTextStyle Style given to the caption.
 */
@Immutable
public data class FallbackSize(
    public val padding: PaddingValues,
    public val gap: Dp,
    public val textGap: Dp,
    public val titleTextStyle: TextStyle,
    public val captionTextStyle: TextStyle,
    public val actionGap: Dp = gap,
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
         * @param actionGap Space between the text and the way out.
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
            actionGap: Dp = gap,
        ): FallbackSize = FallbackSize(
            padding = padding,
            gap = gap,
            textGap = textGap,
            titleTextStyle = titleTextStyle,
            captionTextStyle = captionTextStyle,
            actionGap = actionGap,
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
 * A block shown in place of content that is not there — explaining why, and offering a way out.
 * Centred by default; [horizontalAlignment] sets it against the start edge for a message that
 * belongs to a section rather than to the whole region.
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
 * @param horizontalAlignment Where the mark, the text and the way out sit across the block. The
 *   text is aligned to match: start, centre or end.
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
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    top: (@Composable () -> Unit)? = null,
    heading: (@Composable () -> Unit)? = null,
    body: (@Composable () -> Unit)? = null,
    content: (@Composable () -> Unit)? = null,
) {
    val textAlign = when (horizontalAlignment) {
        Alignment.Start -> TextAlign.Start
        Alignment.End -> TextAlign.End
        else -> TextAlign.Center
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(style.bg)
            .padding(size.padding)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(size.gap),
            horizontalAlignment = horizontalAlignment,
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
                    horizontalAlignment = horizontalAlignment,
                    verticalArrangement = Arrangement.spacedBy(size.textGap),
                ) {
                    if (heading != null) {
                        CompositionLocalProvider(
                            LocalContentColor provides style.title,
                            LocalTextStyle provides size.titleTextStyle.merge(textAlign = textAlign),
                        ) {
                            // Announced as a heading, so a screen reader can jump to what happened.
                            Box(Modifier.semantics(mergeDescendants = true) { headingRole() }) { heading() }
                        }
                    }
                    if (body != null) {
                        CompositionLocalProvider(
                            LocalContentColor provides style.caption,
                            LocalTextStyle provides size.captionTextStyle.merge(textAlign = textAlign),
                        ) {
                            body()
                        }
                    }
                }
            }

            if (content != null) {
                // The column already spaced this slot by gap; add what actionGap asks beyond it.
                if (size.actionGap > size.gap) Spacer(Modifier.height(size.actionGap - size.gap))
                content()
            }
        }
    }
}


/** Which of the two things a [Fallback] says: that nothing is there, or that reading it failed. */
public enum class FallbackTone {
    /**
     * Nothing is there. A neutral mark and a neutral way back — show all, clear the filter — with
     * generous room around it, so the block reads as an empty state rather than a list that failed
     * to load.
     */
    Empty,

    /** Reading it failed. A danger mark and a soft brand retry, at the block's default room. */
    Failure,
}

/**
 * An empty state or a failure in words: a mark, a title, a line of explanation and one way out.
 *
 * The stock form of the slot [Fallback] above, for the two cases nearly every screen has. Use the
 * slot form for anything else in the place of missing content.
 *
 * @param title What is empty, or what went wrong, in a few words.
 * @param modifier Modifier applied to the block.
 * @param body What to do about it, or why it happened; `null` for none.
 * @param tone Whether this is an empty state or a failure; decides the mark's colour, the action's
 *   style and the room around the block.
 * @param icon The mark above the title, `null` for none. Defaults to the tone's own: a tray for an
 *   empty state, an alert for a failure.
 * @param actionText Label of the way out — "Clear filters", "Try again"; `null` for none.
 * @param onAction Called when the way out is pressed; `null` for none.
 * @param actionLoading Whether the way out is running — a retry in flight; the button shows its
 *   spinner and refuses presses.
 * @param actionStyle Look of the way out. `null` takes the tone's: a soft neutral button for an
 *   empty state, a soft brand one for a failure.
 * @param horizontalAlignment Where the block sits across its width; see the slot [Fallback].
 * @param style Fill and text colors. Defaults to the theme's.
 * @param size Padding, gaps and type. Defaults to the tone's: more room for an empty state.
 */
@Composable
public fun Fallback(
    title: String,
    modifier: Modifier = Modifier,
    body: String? = null,
    tone: FallbackTone = FallbackTone.Empty,
    icon: ImageVector? = when (tone) {
        FallbackTone.Empty -> BundledIcons.Tray
        FallbackTone.Failure -> BundledIcons.AlertCircle
    },
    actionText: String? = null,
    onAction: (() -> Unit)? = null,
    actionLoading: Boolean = false,
    actionStyle: ButtonStyle? = null,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    style: FallbackStyle = LocalFallbackStyle.current ?: FallbackStyle.Default,
    size: FallbackSize = LocalFallbackSize.current ?: when (tone) {
        FallbackTone.Empty -> FallbackSize.default(
            padding = PaddingValues(
                horizontal = ArecaTheme.spacing.xl,
                vertical = ArecaTheme.spacing.xl6,
            ),
        )
        FallbackTone.Failure -> FallbackSize.Default
    },
) {
    val failure = tone == FallbackTone.Failure
    Fallback(
        modifier = modifier,
        style = style,
        size = size,
        horizontalAlignment = horizontalAlignment,
        top = icon?.let { glyph ->
            {
                FeaturedIcon(
                    imageVector = glyph,
                    contentDescription = null,
                    style = FeaturedIconStyle.soft(if (failure) ActionColor.Danger else ActionColor.Neutral),
                    size = FeaturedIconSize.Medium,
                )
            }
        },
        heading = { Text(title) },
        body = body?.let { line -> { Text(line) } },
        content = if (actionText != null && onAction != null) {
            {
                Button(
                    text = actionText,
                    onClick = onAction,
                    // The way out of an empty state is a way back, never the screen's call to action.
                    style = actionStyle ?: ButtonStyle.soft(if (failure) ActionColor.Brand else ActionColor.Neutral),
                    loading = actionLoading,
                )
            }
        } else {
            null
        },
    )
}
