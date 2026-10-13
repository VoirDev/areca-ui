package dev.voir.areca.flow.auth

import androidx.compose.runtime.Immutable

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
 */
@Immutable
public sealed interface AuthLayout {
    /**
     * No artwork. Wordmark, heading, form, footer.
     *
     * The shape to reach for when the screen has to hold a long form, because it is the only one
     * that gives the whole viewport to it.
     */
    public data object Plain : AuthLayout

    /**
     * Artwork fills the top of the screen, the content sits below it.
     *
     * The hero scrolls away with the content rather than staying pinned, so that a raised keyboard
     * leaves the form usable on a short device.
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
     * @property fraction Share of the viewport height the artwork takes, clamped to `0.1f..0.8f`.
     */
    public data class HeroBottom(public val fraction: Float = DefaultHeroFraction) : AuthLayout

    /**
     * Artwork sits inside the content column, centred above the heading.
     *
     * For an illustration rather than a photograph — something with its own margins that would look
     * cropped if it were bled to the screen edges.
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
     */
    public data object FullBleed : AuthLayout

    /** Bounds and default of the share of the viewport the split layouts give the artwork. */
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
        AuthLayout.Plain, AuthLayout.HeroInline, AuthLayout.FullBleed -> null
    }?.coerceIn(AuthLayout.MinHeroFraction, AuthLayout.MaxHeroFraction)
