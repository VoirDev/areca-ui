package dev.voir.areca.foundation.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.button.LocalButtonStyle
import dev.voir.areca.theme.ArecaTheme

/**
 * The foot of a [SwipePager]: page marks over one full-width button.
 *
 * The button moves to the next page by default. Pass [onAction] to change what it does — on the
 * last page it usually finishes instead — and change [actionText] with the page, reading
 * [PagerController.isLastPage] or [PagerController.currentPage]. The marks follow the pages
 * whatever moved them.
 *
 * @param controller The pages' controller, shared with the [SwipePager].
 * @param actionText Caption of the button, or `null` for marks alone.
 * @param modifier Modifier applied to the column.
 * @param onAction Called when the button is pressed. Moves to the next page by default.
 * @param indicatorDescription What the marks say to assistive technology, such as "Page 2 of 4".
 * @param buttonStyle Appearance of the button.
 * @param indicatorStyle Colors of the page marks.
 * @param indicatorSize Metrics of the page marks.
 * @param spacing Gap between the marks and the button.
 * @param contentPadding Padding around the marks and the button.
 * @param windowInsets Platform insets to clear, normally the navigation bar at the screen's foot.
 */
@Composable
public fun PagerControls(
    controller: PagerController,
    actionText: String?,
    modifier: Modifier = Modifier,
    onAction: () -> Unit = controller::next,
    indicatorDescription: String? = null,
    buttonStyle: ButtonStyle = LocalButtonStyle.current ?: ButtonStyle.solid(),
    indicatorStyle: PageIndicatorStyle = LocalPageIndicatorStyle.current ?: PageIndicatorStyle.Default,
    indicatorSize: PageIndicatorSize = LocalPageIndicatorSize.current ?: PageIndicatorSize.Default,
    spacing: Dp = ArecaTheme.spacing.xl2,
    contentPadding: PaddingValues = PaddingValues(
        start = ArecaTheme.spacing.xl3,
        end = ArecaTheme.spacing.xl3,
        top = ArecaTheme.spacing.xl3,
        bottom = ArecaTheme.spacing.xl,
    ),
    windowInsets: WindowInsets = WindowInsets(0, 0, 0, 0),
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(windowInsets)
            .padding(contentPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(spacing),
    ) {
        PageIndicator(
            pagerState = controller.pagerState,
            contentDescription = indicatorDescription,
            style = indicatorStyle,
            size = indicatorSize,
        )

        if (actionText != null) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                text = actionText,
                style = buttonStyle,
                onClick = onAction,
            )
        }
    }
}
