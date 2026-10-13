package dev.voir.areca.foundation.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * A tinted layer filling its parent, with content centred on top of it.
 *
 * Sized with `matchParentSize` rather than `fillMaxSize`, which is the whole point: it takes the
 * parent's measured size *without* contributing to it, so covering a box does not change how big
 * that box is. `fillMaxSize` would make the overlay participate in measurement and grow its parent
 * to the available space.
 *
 * Draws over whatever came before it in the parent, so declare it last. For a scrim behind a
 * separate window — a dialog or a sheet — use that component's own overlay instead; this is for
 * covering something already on screen, such as an avatar that is loading.
 *
 * @param color Tint laid over the parent. Normally a translucent scrim from the theme.
 * @param modifier Modifier applied to the overlay.
 * @param contentAlignment Where [content] sits within the covered area.
 * @param content Content drawn on top of the tint, such as a spinner.
 */
@Composable
public fun BoxScope.Overlay(
    color: Color,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .matchParentSize()
            .background(color),
        contentAlignment = contentAlignment,
        content = content,
    )
}
