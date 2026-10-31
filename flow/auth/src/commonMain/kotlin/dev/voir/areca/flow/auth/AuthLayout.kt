package dev.voir.areca.flow.auth

import androidx.compose.runtime.Immutable
import dev.voir.areca.window.HeightClass
import dev.voir.areca.window.WidthClass
import dev.voir.areca.window.WindowClass

/**
 * Where the hero artwork sits relative to the form.
 *
 * These are the arrangements the design system's onboarding set actually uses, rather than an
 * open-ended layout system: an authentication screen is a hero, a heading, a form and a footer, and
 * the only question is the order and the split. Keeping the set closed is what lets a product
 * switch between them with one value.
 *
 * A layout that names a hero draws nothing when [AuthBranding.hero] is absent, and the screen falls
 * back to the same shape as [Plain]. That is deliberate: a product trying layouts out should not
 * have to change two properties to see each one.
 *
 * **On a window wider than compact** — a tablet, an unfolded foldable, a desktop — the content
 * column stops growing at the scaffold's `maxWidth` and sits centred, so a form keeps the shape it
 * has on a phone instead of stretching across the window. Each layout says below what else changes
 * there. A compact window — a phone held upright — draws every layout exactly as it always has.
 * [Split] is the layout made for those windows; the others carry over to them.
 */
@Immutable
public sealed interface AuthLayout {
    /**
     * No artwork. Wordmark, heading, form, footer.
     *
     * The shape to reach for when the screen has to hold a long form, because it is the only one
     * that gives the whole viewport to it.
     *
     * On a window wider than compact the column is centred both ways: across the window, and down
     * the space above the footer, so a short form does not sit at the top of an empty tablet. A
     * form taller than the window starts at the top and scrolls, as on a phone.
     */
    public data object Plain : AuthLayout

    /**
     * Artwork fills the top of the screen, the content sits below it.
     *
     * The hero scrolls away with the content rather than staying pinned, so that a raised keyboard
     * leaves the form usable on a short device.
     *
     * On a window wider than compact the artwork is still a band across the whole window, and the
     * column under it stops at the readable width. A wide, short band crops a photograph hard; from
     * an expanded window up, [Split] usually suits the same artwork better.
     *
     * @property fraction Share of the viewport height the artwork takes, clamped to `0.1f..0.8f`.
     */
    public data class HeroTop(public val fraction: Float = DefaultHeroFraction) : AuthLayout

    /**
     * Content sits at the top, artwork fills the bottom of the screen.
     *
     * Puts the buttons within thumb reach on a tall device, at the cost of the heading no longer
     * being the first thing under the status bar.
     *
     * On a window wider than compact the artwork is still a band across the whole window, and the
     * column above it stops at the readable width.
     *
     * @property fraction Share of the viewport height the artwork takes, clamped to `0.1f..0.8f`.
     */
    public data class HeroBottom(public val fraction: Float = DefaultHeroFraction) : AuthLayout

    /**
     * Artwork sits inside the content column, centred above the heading.
     *
     * For an illustration rather than a photograph — something with its own margins that would look
     * cropped if it were bled to the screen edges.
     *
     * On a window wider than compact it behaves as [Plain] does: the column, illustration included,
     * is centred both ways.
     */
    public data object HeroInline : AuthLayout

    /**
     * Artwork fills the whole screen and the content is laid over it, pushed to the bottom.
     *
     * Every foreground the screens draw straight onto the background — headings, field labels and
     * messages, links, the rule above the provider buttons, the legal line — flips to
     * [dev.voir.areca.theme.ArecaColors.fgOnContrast]. Controls that bring a ground of their own,
     * such as the field boxes and the buttons, keep their ordinary colours, because they are not
     * sitting on the artwork. The rules are resolved in one place; see
     * `dev.voir.areca.flow.auth.part.AuthOnHero`.
     *
     * That flip assumes the artwork is dark enough behind the content to carry a light foreground.
     * Artwork that is not should not use this layout — a scrim inside the hero slot is the fix,
     * not a lighter foreground.
     *
     * On a window wider than compact the artwork still fills the window, and the column stops at the
     * readable width, centred across it and still against the bottom edge.
     */
    public data object FullBleed : AuthLayout

    /**
     * Artwork as a panel down the start side of the window, the form centred in the rest.
     *
     * The layout for a tablet on its side, a laptop or a desktop monitor, where a band of artwork
     * across the top would be a letterbox and a column of form alone would leave most of the window
     * empty. The panel runs the full height of the window, under the status bar, and stays put while
     * the form beside it scrolls or the keyboard rises. The form side keeps everything the other
     * layouts have — the wordmark, the bar, the pinned footer — capped at the readable width and
     * centred both ways in its pane. The start side is the right in a right-to-left layout.
     *
     * The split is drawn from an expanded width (840dp) up, and only when the height is not compact,
     * read from `ArecaTheme.window`. Anywhere narrower or shorter — a phone either way up, an
     * unfolded foldable, a tablet held upright — the screen draws [fallback] instead, so one value
     * covers every window an application runs in.
     *
     * @property fraction Share of the window width the panel takes, clamped to `0.1f..0.8f`. The
     *   default leaves an 840dp window a form pane just over the readable width.
     * @property fallback Layout drawn where the split is not. A [Split] here falls back to its own
     *   [fallback] in turn.
     */
    public data class Split(
        public val fraction: Float = DefaultHeroFraction,
        public val fallback: AuthLayout = HeroTop(),
    ) : AuthLayout

    /**
     * Bounds and default of the share of the viewport the split layouts give the artwork — its
     * height for [HeroTop] and [HeroBottom], its width for [Split].
     */
    public companion object {
        /** Share of the viewport the split layouts give the artwork when nothing else is said. */
        public const val DefaultHeroFraction: Float = 0.42f

        /** Narrowest share of the viewport a split layout will give the artwork. */
        public const val MinHeroFraction: Float = 0.1f

        /** Widest share of the viewport a split layout will give the artwork. */
        public const val MaxHeroFraction: Float = 0.8f
    }
}

/**
 * The share of the viewport this layout gives its artwork, or `null` when it does not split one.
 *
 * Clamped here rather than in the constructor so that a value arriving from a slider or a
 * deserialized preference cannot throw; a layout that is slightly wrong is better than a screen
 * that does not draw.
 *
 * @return Clamped fraction for the split layouts, `null` for every other layout.
 */
internal val AuthLayout.heroFraction: Float?
    get() = when (this) {
        is AuthLayout.HeroTop -> fraction
        is AuthLayout.HeroBottom -> fraction
        is AuthLayout.Split -> fraction
        AuthLayout.Plain, AuthLayout.HeroInline, AuthLayout.FullBleed -> null
    }?.coerceIn(AuthLayout.MinHeroFraction, AuthLayout.MaxHeroFraction)

/**
 * The layout actually drawn in [window]: this one, or for a [AuthLayout.Split] that does not fit,
 * the fallback it names.
 *
 * The split fits from an expanded width up while the height is not compact — the same windows a
 * sheet opens as a dialog in, because both need room on either side and above and below.
 *
 * @param window Window or container the scaffold is laid out in, normally `ArecaTheme.window`.
 * @return A layout other than [AuthLayout.Split], or a [AuthLayout.Split] that fits [window].
 */
internal fun AuthLayout.resolve(window: WindowClass): AuthLayout = when (this) {
    is AuthLayout.Split -> if (window.fitsSplit) this else fallback.resolve(window)
    else -> this
}

/** Whether a window is wide and tall enough for [AuthLayout.Split]. */
private val WindowClass.fitsSplit: Boolean
    get() = width >= WidthClass.Expanded && height > HeightClass.Compact
