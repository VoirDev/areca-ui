package dev.voir.areca.foundation.adaptive

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import dev.voir.areca.theme.ArecaTheme

/**
 * A sidebar that floats in from one edge of the window over a scrim, for an [EdgeColumn]'s shell:
 * every destination with its label, sections of the app's own and its status in full.
 *
 * Draw it last but one in a full-size `Box` — under the [EdgeColumn], so the toggle that opened it
 * stays pressable. It slides in from [side], [edgeOffset] in from that edge so it opens beside the
 * column. A tap on the scrim and the Escape key ask to close it through [onDismissRequest]; route
 * the platform's back gesture there too while it is open.
 *
 * @param visible Whether the sidebar is open.
 * @param onDismissRequest Called when the person closes it; set [visible] to `false` there.
 * @param modifier Modifier applied to the panel.
 * @param side The edge it opens from; [EdgeColumnSide.Auto] follows the camera cutout, as the column does.
 * @param width Width of the panel.
 * @param edgeOffset Space kept between the panel and its edge, beyond the column's inset — the
 *   column's width, when it opens beside one.
 * @param title What assistive technology announces the panel as.
 * @param dismissLabel What assistive technology announces a tap on the scrim as.
 * @param style Colours. Defaults to [LocalEdgeColumnStyle], then [EdgeColumnStyle.Default].
 * @param size Dimensions. Defaults to [LocalEdgeColumnSize], then [EdgeColumnSize.Default].
 * @param windowInsets Insets the panel keeps clear of.
 * @param content The panel's content, top down.
 */
@Composable
public fun FloatingSidebar(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    side: EdgeColumnSide = EdgeColumnSide.Auto,
    width: Dp = AdaptiveDefaults.FloatingSidebarWidth,
    edgeOffset: Dp = 0.dp,
    title: String = "Navigation",
    dismissLabel: String = "Close navigation",
    style: EdgeColumnStyle = LocalEdgeColumnStyle.current ?: EdgeColumnStyle.Default,
    size: EdgeColumnSize = LocalEdgeColumnSize.current ?: EdgeColumnSize.Default,
    windowInsets: WindowInsets = edgeColumnInsets(side),
    content: @Composable ColumnScope.() -> Unit,
) {
    val edge = side.resolve()
    val rtl = LocalLayoutDirection.current == LayoutDirection.Rtl
    // Slide from the panel's own edge: the end edge is on the right left to right.
    val fromRight = (edge == EdgeColumnSide.End) != rtl

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(visible = visible, enter = fadeIn(), exit = fadeOut()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(style.scrim)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onDismissRequest,
                    )
                    .semantics { onClick(label = dismissLabel) { onDismissRequest(); true } },
            )
        }
        AnimatedVisibility(
            visible = visible,
            modifier = Modifier.align(if (edge == EdgeColumnSide.End) Alignment.CenterEnd else Alignment.CenterStart),
            enter = fadeIn() + slideInHorizontally { full -> if (fromRight) full / 4 else -full / 4 },
            exit = fadeOut() + slideOutHorizontally { full -> if (fromRight) full / 4 else -full / 4 },
        ) {
            val focus = remember { FocusRequester() }
            LaunchedEffect(Unit) { focus.requestFocus() }
            Column(
                modifier = modifier
                    .fillMaxHeight()
                    .windowInsetsPadding(windowInsets)
                    .padding(size.inset)
                    .padding(
                        start = if (edge == EdgeColumnSide.Start) edgeOffset else 0.dp,
                        end = if (edge == EdgeColumnSide.End) edgeOffset else 0.dp,
                    )
                    .width(width)
                    .shadow(style.shadowElevation, style.panelShape)
                    .background(style.surface, style.panelShape)
                    .border(1.dp, style.border, style.panelShape)
                    .semantics { paneTitle = title }
                    .onPreviewKeyEvent { event ->
                        if (event.key == Key.Escape && event.type == KeyEventType.KeyDown) {
                            onDismissRequest()
                            true
                        } else {
                            false
                        }
                    }
                    .focusRequester(focus)
                    .focusable()
                    .padding(ArecaTheme.spacing.lg),
                content = content,
            )
        }
    }
}
