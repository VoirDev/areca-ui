package dev.voir.areca.foundation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import dev.voir.areca.foundation.text.Text

/**
 * Where the page on screen sits among the rest, as a count — "3 / 9" in a capsule on a picture's
 * corner, or the position in a full-screen viewer's bar.
 *
 * Says in a fixed width what marks say only up to about ten pages, so prefer it to a
 * [PageIndicator] when the pages can be many. Over a picture, pass [PageCounterStyle.onImage].
 *
 * The count is read out as drawn unless [contentDescription] is given: "3 / 9" is read as "three
 * slash nine", so pass "Picture 3 of 9" where that matters.
 *
 * @param currentPage Index of the page on screen, coerced into `0 until pageCount`.
 * @param pageCount Number of pages, at least one.
 * @param modifier Modifier applied to the capsule.
 * @param contentDescription What the counter says to assistive technology, or `null` to read the
 *   count as drawn.
 * @param text Words the count, given the page's index and the number of pages.
 * @param style Colors of the counter.
 * @param size Metrics of the counter.
 * @throws IllegalArgumentException When [pageCount] is less than one.
 */
@Composable
public fun PageCounter(
    currentPage: Int,
    pageCount: Int,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    text: (index: Int, count: Int) -> String = { index, count -> "${index + 1} / $count" },
    style: PageCounterStyle = LocalPageCounterStyle.current ?: PageCounterStyle.Default,
    size: PageCounterSize = LocalPageCounterSize.current ?: PageCounterSize.Default,
) {
    require(pageCount > 0) { "A page counter needs at least one page, got $pageCount." }

    Box(
        modifier = modifier
            .then(
                if (contentDescription == null) {
                    Modifier
                } else {
                    Modifier.clearAndSetSemantics { this.contentDescription = contentDescription }
                },
            )
            .background(style.bg, size.shape)
            .padding(size.padding),
    ) {
        Text(
            text = text(currentPage.coerceIn(0, pageCount - 1), pageCount),
            style = size.textStyle.copy(fontFeatureSettings = TABULAR_FIGURES),
            color = style.content,
            maxLines = 1,
        )
    }
}

/**
 * A [PageCounter] following a pager: it counts the page the pager is on.
 *
 * @param pagerState State of the pager whose pages are counted.
 * @param modifier Modifier applied to the capsule.
 * @param contentDescription What the counter says to assistive technology, or `null` to read the
 *   count as drawn.
 * @param text Words the count, given the page's index and the number of pages.
 * @param style Colors of the counter.
 * @param size Metrics of the counter.
 */
@Composable
public fun PageCounter(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    text: (index: Int, count: Int) -> String = { index, count -> "${index + 1} / $count" },
    style: PageCounterStyle = LocalPageCounterStyle.current ?: PageCounterStyle.Default,
    size: PageCounterSize = LocalPageCounterSize.current ?: PageCounterSize.Default,
) {
    if (pagerState.pageCount == 0) return

    PageCounter(
        currentPage = pagerState.currentPage,
        pageCount = pagerState.pageCount,
        modifier = modifier,
        contentDescription = contentDescription,
        text = text,
        style = style,
        size = size,
    )
}

/** OpenType feature for figures of one width, so the capsule does not twitch as the count moves. */
private const val TABULAR_FIGURES = "tnum"
