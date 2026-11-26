package dev.voir.areca.foundation.overlay

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.getBoundsInRoot
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.ArecaThemeProvider
import dev.voir.areca.window.HeightClass
import dev.voir.areca.window.WidthClass
import dev.voir.areca.window.WindowClass
import kotlin.test.Test
import kotlin.test.assertTrue

/** A [ConfirmSheet] is a sheet on every window: the design system never turns it into a dialog. */
@OptIn(ExperimentalTestApi::class)
class ConfirmSheetWindowTest {

    @Test
    fun `a confirm sheet on an expanded window rises from the bottom edge`() = runComposeUiTest {
        var host: WindowClass? = null
        setContent {
            ArecaThemeProvider {
                host = ArecaTheme.hostWindow
                ConfirmSheet(
                    visible = true,
                    icon = null,
                    title = "Sign out?",
                    description = null,
                    confirmText = "Sign out",
                    onConfirm = {},
                    onDismiss = {},
                )
            }
        }
        waitForIdle()

        val window = checkNotNull(host)
        assertTrue(window.width >= WidthClass.Expanded && window.height > HeightClass.Compact, "an expanded window: $window")
        val scrim = onNodeWithContentDescription("Dismiss").getBoundsInRoot()
        val confirm = onNodeWithText("Sign out").getBoundsInRoot()
        val windowHeight = scrim.bottom - scrim.top
        assertTrue(confirm.bottom > scrim.top + windowHeight * 0.75f, "the answer sits at the bottom edge, at ${confirm.bottom} of $windowHeight")
    }
}
