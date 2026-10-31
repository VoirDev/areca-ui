package dev.voir.areca.foundation.layout

import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.ScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.voir.areca.theme.ArecaTheme

/** Compose Desktop's scrollbar: a thin thumb in the copy's colour, firmer under the pointer. */
@Composable
internal actual fun PlatformScrollbar(target: ScrollbarTarget, vertical: Boolean, modifier: Modifier) {
    val ink = ArecaTheme.colors.fgBase
    val style = ScrollbarStyle(
        minimalHeight = 32.dp,
        thickness = 6.dp,
        shape = ArecaTheme.shapes.full,
        hoverDurationMillis = ArecaTheme.motion.fast.inWholeMilliseconds.toInt(),
        unhoverColor = ink.copy(alpha = 0.22f),
        hoverColor = ink.copy(alpha = 0.45f),
    )
    val adapter = when (target) {
        is ScrollbarTarget.Scroll -> rememberScrollbarAdapter(target.state)
        is ScrollbarTarget.LazyList -> rememberScrollbarAdapter(target.state)
        is ScrollbarTarget.LazyGrid -> rememberScrollbarAdapter(target.state)
    }
    if (vertical) {
        VerticalScrollbar(adapter = adapter, modifier = modifier, style = style)
    } else {
        HorizontalScrollbar(adapter = adapter, modifier = modifier, style = style)
    }
}
