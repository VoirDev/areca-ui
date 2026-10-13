package dev.voir.areca.foundation.text

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.foundation.LocalTextStyle
import dev.voir.areca.theme.ArecaTheme

/**
 * Type scale and spacing for one step of [Heading].
 *
 * The five steps are a hierarchy within a screen, not absolute sizes: [Medium] is the default a
 * screen's own heading uses, [Small] and [ExtraSmall] title a section and a group inside it, and
 * [Large] and [ExtraLarge] are for screens whose title carries the page.
 *
 * The title styles come from [dev.voir.areca.theme.ArecaTypography] rather than being rebuilt from
 * the raw scale, so retuning a heading role in the theme moves every [Heading] with it.
 */
@Immutable
public data class HeadingSize(
    /** Style provided to the title slot. */
    public val titleTextStyle: TextStyle,
    /** Style provided to the supportive slot. */
    public val supportiveTextStyle: TextStyle,
    /** Space between the title and the supportive text. */
    public val textGap: Dp,
    /** Space between the heading text and the trailing content. */
    public val contentGap: Dp,
) {
    /** The five steps of the heading hierarchy, smallest first. */
    public companion object {
        /**
         * Smallest step, for titling a group inside a card or a list.
         *
         * This is the one step whose title is built from the raw scale: no typography role sits
         * below `headingExtraSmall`, and a group title has to be smaller than a section's.
         */
        public val ExtraSmall: HeadingSize
            @Composable
            get() = HeadingSize(
                titleTextStyle = ArecaTheme.baseTypography.md.merge(
                    fontWeight = FontWeight.SemiBold,
                ),
                supportiveTextStyle = ArecaTheme.typography.bodyExtraSmall,
                textGap = ArecaTheme.spacing.xs2,
                contentGap = ArecaTheme.spacing.md,
            )

        /** Section title within a screen. */
        public val Small: HeadingSize
            @Composable
            get() = HeadingSize(
                titleTextStyle = ArecaTheme.typography.headingExtraSmall,
                supportiveTextStyle = ArecaTheme.typography.bodySmall,
                textGap = ArecaTheme.spacing.xs2,
                contentGap = ArecaTheme.spacing.md,
            )

        /** The default. A screen's own heading. */
        public val Medium: HeadingSize
            @Composable
            get() = HeadingSize(
                titleTextStyle = ArecaTheme.typography.headingSmall,
                supportiveTextStyle = ArecaTheme.typography.bodySmall,
                textGap = ArecaTheme.spacing.sm,
                contentGap = ArecaTheme.spacing.md,
            )

        /** Heading for a screen whose title is its main content. */
        public val Large: HeadingSize
            @Composable
            get() = HeadingSize(
                titleTextStyle = ArecaTheme.typography.headingMedium,
                supportiveTextStyle = ArecaTheme.typography.bodyMedium,
                textGap = ArecaTheme.spacing.md,
                contentGap = ArecaTheme.spacing.lg,
            )

        /**
         * Largest step, for an onboarding or landing screen.
         *
         * The title is `headingLarge` held at `SemiBold` rather than the `Bold` that role carries.
         * A heading block pairs its title with supportive copy, and `Bold` at this size overwhelms
         * that pairing. Drop the weight override to adopt the role as-is.
         */
        public val ExtraLarge: HeadingSize
            @Composable
            get() = HeadingSize(
                titleTextStyle = ArecaTheme.typography.headingLarge.merge(
                    fontWeight = FontWeight.SemiBold,
                ),
                supportiveTextStyle = ArecaTheme.typography.bodyMedium,
                textGap = ArecaTheme.spacing.md,
                contentGap = ArecaTheme.spacing.lg,
            )
    }
}

/**
 * Colors for a [Heading]'s title and supportive text.
 *
 * The two are deliberately different weights of foreground rather than one color: the contrast
 * between them is what makes the block read as a title with a subtitle instead of two lines of text.
 */
@Immutable
public data class HeadingStyle(
    /** Content color provided to the title slot. */
    public val title: Color,
    /** Content color provided to the supportive slot. */
    public val supportive: Color,
) {
    /** The theme's default style, and a builder for overriding it. */
    public companion object {
        /** Theme default: primary foreground titled over secondary supportive text. */
        public val Default: HeadingStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme, overriding either color.
         *
         * @param title Color of the title slot.
         * @param supportive Color of the supportive slot.
         * @return A style with the given colors and the theme's by default.
         */
        @Composable
        public fun default(
            title: Color = ArecaTheme.colors.fgBase,
            supportive: Color = ArecaTheme.colors.fgSubtle,
        ): HeadingStyle = HeadingStyle(
            title = title,
            supportive = supportive,
        )
    }
}

/** Default [HeadingSize] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalHeadingSize: ProvidableCompositionLocal<HeadingSize?> = compositionLocalOf { null }

/** Default [HeadingStyle] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalHeadingStyle: ProvidableCompositionLocal<HeadingStyle?> = compositionLocalOf { null }

/**
 * A title, optional supportive line, and optional trailing content, as one block.
 *
 * The slots are composable rather than strings so a title can carry a badge or an inline link, and
 * so [content] can be anything a section needs under its heading — a filter row, a description, a
 * chart. The block provides [LocalContentColor] and [LocalTextStyle] to each text slot, so a plain
 * [Text] inside picks up the right treatment without being told.
 *
 * The title is announced as a heading, so a screen reader can jump between the sections of a
 * screen; the supportive line and the content below are read as ordinary content.
 *
 * @param title Title slot. Required, because a heading without one is a layout, not a heading.
 * @param modifier Modifier applied to the heading column.
 * @param style Title and supportive colors. Defaults to the theme's, or [HeadingStyle.Default].
 * @param size Type scale and spacing. Defaults to the theme's, or [HeadingSize.Medium].
 * @param supportive Supportive line below the title.
 * @param content Content below the heading text.
 */
@Composable
public fun Heading(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    style: HeadingStyle = LocalHeadingStyle.current ?: HeadingStyle.Default,
    size: HeadingSize = LocalHeadingSize.current ?: HeadingSize.Medium,
    supportive: (@Composable () -> Unit)? = null,
    content: (@Composable () -> Unit)? = null,
) {
    Column(modifier = modifier) {
        CompositionLocalProvider(
            LocalContentColor provides style.title,
            LocalTextStyle provides size.titleTextStyle,
        ) {
            // One merged node, so a title made of several pieces is one heading rather than many.
            Box(modifier = Modifier.semantics(mergeDescendants = true) { heading() }) {
                title()
            }
        }

        if (supportive != null) {
            Spacer(Modifier.height(size.textGap))
            CompositionLocalProvider(
                LocalContentColor provides style.supportive,
                LocalTextStyle provides size.supportiveTextStyle,
            ) {
                supportive()
            }
        }

        if (content != null) {
            Spacer(Modifier.height(size.contentGap))
            content()
        }
    }
}
