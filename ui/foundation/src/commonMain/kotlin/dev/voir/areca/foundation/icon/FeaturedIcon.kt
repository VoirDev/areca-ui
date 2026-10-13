package dev.voir.areca.foundation.icon

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.IconButton
import dev.voir.areca.foundation.button.filledBackground
import dev.voir.areca.foundation.button.mutedBackground
import dev.voir.areca.foundation.button.onFilled
import dev.voir.areca.foundation.button.outlineBorder
import dev.voir.areca.modifier.gradientBorder
import dev.voir.areca.theme.ArecaColors
import dev.voir.areca.theme.ArecaTheme

/**
 * Container and content colors for [FeaturedIcon].
 *
 * Each of the three surfaces — fill, border, icon — can be given either a flat color or a
 * [Brush]. **The brush wins wherever both are set.** The pairs exist because the common case is a
 * flat theme color and the interesting case is a gradient, and requiring `SolidColor(...)` at every
 * call site would make the common case the noisy one.
 *
 * A `null` [bg] draws nothing at all, which is different from a transparent one: it leaves
 * the surface behind fully visible with no layer composited over it.
 *
 * The factories are the same emphasis ladder as [dev.voir.areca.foundation.button.ButtonStyle],
 * resolved from the same [ActionColor]: [solid], [soft] and [outlined], with [Default] as the
 * containerless step. A featured icon beside a button of the same family takes the same colors.
 *
 * @property bg Flat container fill, or `null` for none.
 * @property bgBrush Container fill brush. Overrides [bg].
 * @property border Flat border color. [Color.Transparent] draws no border.
 * @property borderBrush Border brush. Overrides [border].
 * @property content Flat icon tint.
 * @property contentBrush Icon tint brush. Overrides [content].
 */
@Immutable
public data class FeaturedIconStyle(
    public val bg: Color? = null,
    public val bgBrush: Brush? = null,
    public val border: Color = Color.Transparent,
    public val borderBrush: Brush? = null,
    public val content: Color,
    public val contentBrush: Brush? = null,
) {
    /** The containerless default, and the factories of the emphasis ladder. */
    public companion object {
        /** Theme default: no container, icon in the primary foreground. */
        public val Default: FeaturedIconStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme, overriding any surface.
         *
         * @param bg Flat container fill, or `null` for none.
         * @param bgBrush Container fill brush. Overrides [bg].
         * @param border Flat border color.
         * @param borderBrush Border brush. Overrides [border].
         * @param content Flat icon tint.
         * @param contentBrush Icon tint brush. Overrides [content].
         * @return A style with the given surfaces and the theme's primary foreground by default.
         */
        @Composable
        public fun default(
            bg: Color? = null,
            bgBrush: Brush? = null,
            border: Color = Color.Transparent,
            borderBrush: Brush? = null,
            content: Color = ArecaTheme.colors.fgBase,
            contentBrush: Brush? = null,
        ): FeaturedIconStyle = FeaturedIconStyle(
            bg = bg,
            bgBrush = bgBrush,
            border = border,
            borderBrush = borderBrush,
            content = content,
            contentBrush = contentBrush,
        )

        /**
         * Solid fill in [color]'s base step, with content reversed out of it.
         *
         * `Warning`, `Neutral` and `NeutralDark` take their own content colors: the warning fill is
         * light in both themes, the neutral fill is too weak to reverse white out of, and the
         * inverted surface turns light in the dark theme.
         *
         * @param color Color family.
         * @return The solid style for [color].
         */
        @Composable
        public fun solid(color: ActionColor = ActionColor.Brand): FeaturedIconStyle =
            solid(color, ArecaTheme.colors)

        /**
         * [solid] resolved against an explicit palette rather than the theme in scope.
         *
         * @param color Color family.
         * @param colors Palette to take the fill and content colors from.
         * @return The solid style for [color] in [colors].
         */
        internal fun solid(color: ActionColor, colors: ArecaColors): FeaturedIconStyle =
            FeaturedIconStyle(
                bg = color.filledBackground(colors),
                content = color.onFilled(colors),
            )

        /**
         * Tinted fill in [color]'s muted step, with the glyph in the family's icon foreground.
         *
         * @param color Color family.
         * @return The soft style for [color].
         */
        @Composable
        public fun soft(color: ActionColor = ActionColor.Brand): FeaturedIconStyle =
            soft(color, ArecaTheme.colors)

        /**
         * [soft] resolved against an explicit palette rather than the theme in scope.
         *
         * @param color Color family.
         * @param colors Palette to take the fill and content colors from.
         * @return The soft style for [color] in [colors].
         */
        internal fun soft(color: ActionColor, colors: ArecaColors): FeaturedIconStyle =
            FeaturedIconStyle(
                bg = color.mutedBackground(colors),
                content = when (color) {
                    ActionColor.NeutralDark -> colors.fgOnContrast
                    ActionColor.Brand,
                    ActionColor.Danger,
                    ActionColor.Success,
                    ActionColor.Warning,
                    ActionColor.Neutral -> color.glyphOnSurface(colors)
                },
            )

        /**
         * Transparent container bordered in [color], with the glyph in the family's icon foreground.
         *
         * @param color Color family.
         * @return The outlined style for [color].
         */
        @Composable
        public fun outlined(color: ActionColor = ActionColor.Brand): FeaturedIconStyle {
            val colors = ArecaTheme.colors
            return FeaturedIconStyle(
                bg = Color.Transparent,
                border = color.outlineBorder(colors),
                content = color.glyphOnSurface(colors),
            )
        }
    }
}

/**
 * Glyph color of a featured icon whose ground is the page or the family's muted tint.
 *
 * A featured icon's glyph is a mark, not type, so each family takes its icon foreground — the step
 * that clears the 3:1 non-text floor — rather than the darker text step a button's label needs.
 *
 * @param colors Palette to resolve against.
 * @return The family's icon foreground.
 */
private fun ActionColor.glyphOnSurface(colors: ArecaColors): Color = when (this) {
    ActionColor.Brand -> colors.fgBrand
    ActionColor.Danger -> colors.fgDanger
    ActionColor.Success -> colors.fgSuccess
    ActionColor.Warning -> colors.fgWarn
    ActionColor.Neutral -> colors.fgMuted
    ActionColor.NeutralDark -> colors.fgBase
}

/**
 * Container and icon sizes for [FeaturedIcon].
 *
 * Up to [Medium] the icon fills a little over half the container (12 of 20dp at [Tiny], 26 of 48dp
 * at [Medium]), the same ratio as [dev.voir.areca.foundation.button.IconButtonSize], so a featured
 * icon and an icon button of one step line up glyph for glyph. [Large] and [ExtraLarge] step back to
 * a little under half (24 of 56dp, 28 of 64dp): at those sizes the icon is the block's subject, and
 * the extra ground is what lets the container read as a deliberate shape rather than a tight box.
 *
 * @property container Square container size.
 * @property icon Size the icon is drawn at inside the container.
 * @property borderWidth Border width, drawn only by styles with a border, such as
 *   [FeaturedIconStyle.outlined].
 */
@Immutable
public data class FeaturedIconSize(
    public val container: Dp,
    public val icon: Dp,
    public val borderWidth: Dp,
) {
    /** The named container steps, smallest first. */
    public companion object {
        /** 20dp. Inline with text, where a full-size featured icon would break the line. */
        public val Tiny: FeaturedIconSize =
            FeaturedIconSize(container = 20.dp, icon = 12.dp, borderWidth = 1.dp)

        /** 32dp. Leading slot of a dense list row. */
        public val ExtraSmall: FeaturedIconSize =
            FeaturedIconSize(container = 32.dp, icon = 18.dp, borderWidth = 1.dp)

        /** 40dp. Leading slot of a standard list row. */
        public val Small: FeaturedIconSize =
            FeaturedIconSize(container = 40.dp, icon = 22.dp, borderWidth = 1.dp)

        /** 48dp. The default: a card or dialog header. */
        public val Medium: FeaturedIconSize =
            FeaturedIconSize(container = 48.dp, icon = 26.dp, borderWidth = 1.dp)

        /** 56dp. An empty state or a prominent dialog. */
        public val Large: FeaturedIconSize =
            FeaturedIconSize(container = 56.dp, icon = 24.dp, borderWidth = 1.dp)

        /** 64dp. A screen whose icon is its main content. */
        public val ExtraLarge: FeaturedIconSize =
            FeaturedIconSize(container = 64.dp, icon = 28.dp, borderWidth = 1.dp)
    }
}

/** Default [FeaturedIconSize] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalFeaturedIconSize: ProvidableCompositionLocal<FeaturedIconSize?> = compositionLocalOf { null }

/** Default [FeaturedIconStyle] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalFeaturedIconStyle: ProvidableCompositionLocal<FeaturedIconStyle?> = compositionLocalOf { null }

/**
 * An icon inside a shaped container, used to give a block a subject.
 *
 * Where [Icon] sits inline with text at text size, this is a standalone mark: the header of a
 * dialog, the illustration of an empty state, the leading slot of a row that needs more presence
 * than a bare glyph. It is not interactive — wrap it, or use [IconButton], if it should be pressed.
 *
 * @param painter Icon to draw inside the container.
 * @param contentDescription Label read by assistive technology, or `null` when decorative.
 * @param modifier Modifier applied to the container.
 * @param style Container and content colors. Defaults to the theme's.
 * @param size Container and icon sizes. Defaults to the theme's, or [FeaturedIconSize.Medium].
 * @param shape Container shape, used for the fill and the border alike.
 */
@Composable
public fun FeaturedIcon(
    painter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    style: FeaturedIconStyle = LocalFeaturedIconStyle.current ?: FeaturedIconStyle.Default,
    size: FeaturedIconSize = LocalFeaturedIconSize.current ?: FeaturedIconSize.Medium,
    shape: Shape = ArecaTheme.shapes.md,
) {
    val semantics = if (contentDescription != null) {
        Modifier.semantics {
            this.contentDescription = contentDescription
            this.role = Role.Image
        }
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .featuredIconContainer(style = style, size = size, shape = shape)
            .then(semantics),
        contentAlignment = Alignment.Center,
    ) {
        FeaturedIconContent(
            painter = painter,
            // The container already carries the label; labelling the icon too would read it twice.
            contentDescription = null,
            tint = style.content,
            tintBrush = style.contentBrush,
            size = size.icon,
        )
    }
}

/**
 * An icon inside a shaped container, from a vector.
 *
 * The overload to reach for with `ArecaIcons` from `dev.voir.areca:icons`.
 *
 * @param imageVector Vector to draw inside the container.
 * @param contentDescription Label read by assistive technology, or `null` when decorative.
 * @param modifier Modifier applied to the container.
 * @param style Container and content colors. Defaults to the theme's.
 * @param size Container and icon sizes. Defaults to the theme's, or [FeaturedIconSize.Medium].
 * @param shape Container shape, used for the fill and the border alike.
 */
@Composable
public fun FeaturedIcon(
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    style: FeaturedIconStyle = LocalFeaturedIconStyle.current ?: FeaturedIconStyle.Default,
    size: FeaturedIconSize = LocalFeaturedIconSize.current ?: FeaturedIconSize.Medium,
    shape: Shape = ArecaTheme.shapes.md,
) {
    FeaturedIcon(
        painter = rememberVectorPainter(imageVector),
        contentDescription = contentDescription,
        modifier = modifier,
        style = style,
        size = size,
        shape = shape,
    )
}

/**
 * Sizes, fills, and borders the container.
 *
 * Brushes take precedence over flat colors on both the fill and the border, which is the precedence
 * [FeaturedIconStyle] documents. Nothing is clipped: the fill and the border carry the shape
 * themselves and the glyph sits well inside the container, so a clip would only crop whatever a
 * caller draws around the icon, such as a focus ring.
 *
 * @param style Colors and brushes to draw with.
 * @param size Container size and border width.
 * @param shape Shape of the fill and the border.
 * @return The receiver, with the container's size, fill and border appended.
 */
private fun Modifier.featuredIconContainer(
    style: FeaturedIconStyle,
    size: FeaturedIconSize,
    shape: Shape,
): Modifier = this
    .size(size.container)
    .then(
        when {
            style.bgBrush != null ->
                Modifier.background(brush = style.bgBrush, shape = shape)

            style.bg != null ->
                Modifier.background(color = style.bg, shape = shape)

            else -> Modifier
        }
    )
    .then(
        when {
            style.borderBrush != null -> Modifier.gradientBorder(
                width = size.borderWidth,
                brush = style.borderBrush,
                shape = shape,
            )

            style.border != Color.Transparent -> Modifier.border(
                width = size.borderWidth,
                color = style.border,
                shape = shape,
            )

            else -> Modifier
        }
    )

/**
 * Draws the icon itself, flat-tinted or brush-tinted.
 *
 * A brush cannot go through `ColorFilter`, so the brush path draws the painter and then paints the
 * brush over it in `SrcIn`, which keeps the brush only where the icon has ink. It does so in an
 * offscreen layer, so the blend sees the icon alone rather than the container behind it.
 *
 * @param painter Icon to draw.
 * @param contentDescription Label read by assistive technology, or `null` when decorative.
 * @param tint Flat tint, used when [tintBrush] is `null`.
 * @param tintBrush Brush tint. Overrides [tint].
 * @param size Square size to draw at.
 */
@Composable
private fun FeaturedIconContent(
    painter: Painter,
    contentDescription: String?,
    tint: Color,
    tintBrush: Brush?,
    size: Dp,
) {
    if (tintBrush == null) {
        Icon(
            painter = painter,
            contentDescription = contentDescription,
            tint = tint,
            size = size,
        )
    } else {
        Image(
            painter = painter,
            contentDescription = contentDescription,
            modifier = Modifier
                .size(size)
                // SrcIn must blend against the icon alone, not whatever is already drawn behind it.
                .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
                .drawWithCache {
                    onDrawWithContent {
                        drawContent()
                        drawRect(brush = tintBrush, blendMode = BlendMode.SrcIn)
                    }
                },
            colorFilter = null,
            contentScale = ContentScale.Fit,
        )
    }
}
