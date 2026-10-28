package dev.voir.areca.media.carousel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import dev.voir.areca.foundation.button.IconButton
import dev.voir.areca.foundation.button.IconButtonSize
import dev.voir.areca.foundation.button.IconButtonStyle
import dev.voir.areca.indication.TouchIndication
import dev.voir.areca.media.icon.BundledIcons
import dev.voir.areca.media.icon.ChevronLeft
import dev.voir.areca.media.icon.ChevronRight
import dev.voir.areca.theme.ArecaTheme

/**
 * Previous and next buttons over a [Carousel]'s edges, for a pointer — a desktop product page.
 *
 * Lay them over the carousel, filling its width. Each is left out where it would go nowhere — the
 * previous one on the first page, the next on the last — unless the carousel loops. Touch screens
 * swipe; leave the arrows out there, where they would cover the picture.
 *
 * @param state The carousel they move.
 * @param modifier Modifier applied to the row holding them.
 * @param previousLabel What the previous button does, read out.
 * @param nextLabel What the next button does, read out.
 * @param style The buttons' look; by default a translucent surface disc that reads on any picture.
 * @param size The buttons' size.
 */
@Composable
public fun CarouselArrows(
    state: CarouselState,
    modifier: Modifier = Modifier,
    previousLabel: String = "Previous picture",
    nextLabel: String = "Next picture",
    style: IconButtonStyle = arrowStyle(),
    size: IconButtonSize = IconButtonSize.Small,
) {
    val page = state.currentPage
    val pages = state.pageCount

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = ArecaTheme.spacing.lg),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (state.loop || page > 0) {
            IconButton(BundledIcons.ChevronLeft, previousLabel, onClick = state::previous, style = style, size = size)
        } else {
            Box(Modifier)
        }
        if (state.loop || page < pages - 1) {
            IconButton(BundledIcons.ChevronRight, nextLabel, onClick = state::next, style = style, size = size)
        }
    }
}

/** @return A disc of the theme's surface, slightly see-through, with the theme's foreground. */
@Composable
private fun arrowStyle(): IconButtonStyle = IconButtonStyle(
    bg = ArecaTheme.colors.bgSurface.copy(alpha = ARROW_ALPHA),
    border = Color.Transparent,
    content = ArecaTheme.colors.fgBase,
    shape = CircleShape,
    indication = TouchIndication(shape = CircleShape),
)

/** How solid an arrow's disc is over a picture. */
private const val ARROW_ALPHA = 0.92f
