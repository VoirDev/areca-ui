package dev.voir.areca.foundation.layout

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.navigation.ScrollToTopOnTabReselect
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.RaisedSurface

/**
 * A page in two parts: a header block on the page's ground, and the content proper on a panel with
 * rounded top corners that runs from under it to the bottom of the screen — a total over its
 * breakdown, a summary over its list.
 *
 * The panel is given the rest of the viewport as its minimum height, so a short page still ends at
 * the bottom edge rather than in a band of the page's ground under a panel that stopped early; a
 * long page scrolls as a whole. The panel takes the raised fill of the page — white on a grey page,
 * grey on a white one — and what is in it is drawn on that raised surface. The column scrolls back
 * to its top when its destination is selected again.
 *
 * Inside a [BaseLayout], pass the padding it hands its content as [contentPadding]: the top padding
 * goes above the header and the bottom padding at the foot of the panel, so the last row clears the
 * bars.
 *
 * @param modifier Modifier applied to the page, which fills the space it is given.
 * @param contentPadding Padding the bars take up; see above.
 * @param scrollState Scroll position of the page.
 * @param panelShape Shape of the panel's top. Defaults to the `xl` radius on the top corners.
 * @param header The header block, on the page's ground. It brings its own horizontal insets.
 * @param panel The content proper, on the panel. It brings its own horizontal insets.
 */
@Composable
public fun PanelLayout(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    scrollState: ScrollState = rememberScrollState(),
    panelShape: Shape = RoundedCornerShape(topStart = ArecaTheme.radius.xl, topEnd = ArecaTheme.radius.xl),
    header: @Composable ColumnScope.() -> Unit,
    panel: @Composable ColumnScope.() -> Unit,
) {
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current
    var headerHeight by remember { mutableStateOf(0.dp) }
    val panelBg = ArecaTheme.surface.raised
    scrollState.ScrollToTopOnTabReselect()

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        // A parent that scrolls hands down no height to fill; the panel then wraps its content.
        val panelMinHeight = if (constraints.hasBoundedHeight) {
            (maxHeight - headerHeight).coerceAtLeast(0.dp)
        } else {
            0.dp
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(
                    start = contentPadding.calculateStartPadding(layoutDirection),
                    end = contentPadding.calculateEndPadding(layoutDirection),
                ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .onSizeChanged { size -> headerHeight = with(density) { size.height.toDp() } }
                    .padding(top = contentPadding.calculateTopPadding()),
                content = header,
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = panelMinHeight)
                    .background(panelBg, panelShape),
            ) {
                RaisedSurface { Column(content = panel) }
                Spacer(Modifier.height(contentPadding.calculateBottomPadding()))
            }
        }
    }
}
