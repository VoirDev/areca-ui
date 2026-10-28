package dev.voir.areca.foundation.navigation

import androidx.compose.runtime.Immutable

/**
 * How a [PageIndicator] draws the pages: which marks, and how the one on screen stands out.
 *
 * Pick by how many pages there are. A mark per page reads at a glance up to about ten; past that
 * the row is wider than it is useful, and [Shrinking] or [Line] say the same in a fixed width.
 */
@Immutable
public sealed interface PageIndicatorVariant {
    /** A dot per page, the one on screen stretched long and coloured — an onboarding tour. */
    public data object Elongated : PageIndicatorVariant

    /** A dot per page, the one on screen coloured and no larger — a product picture's dots. */
    public data object Dots : PageIndicatorVariant

    /**
     * Dots through a window of [window] pages that slides with the one on screen, with a smaller
     * dot either side while more pages lie beyond — any number of pages in the width of a few.
     *
     * Draws like [Dots] when every page fits in the window and its two edge dots.
     *
     * @property window How many dots are drawn full size, at least three.
     */
    public data class Shrinking(public val window: Int = 5) : PageIndicatorVariant {
        init {
            require(window >= MIN_WINDOW) { "A shrinking window needs at least $MIN_WINDOW dots, got $window." }
        }
    }

    /** A track with a thumb one page long, sliding along it — many pages, said quietly. */
    public data object Line : PageIndicatorVariant

    /**
     * A bar per page across the width it is given, filled up to the page on screen, which fills
     * with the indicator's `progress` — pages that advance on their own, as stories do.
     */
    public data object Segments : PageIndicatorVariant
}

/** The narrowest [PageIndicatorVariant.Shrinking] window: the current dot and one either side. */
private const val MIN_WINDOW = 3
