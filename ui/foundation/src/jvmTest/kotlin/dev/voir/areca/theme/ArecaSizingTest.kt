package dev.voir.areca.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.window.InputMode
import dev.voir.areca.window.ProvideWindowClass
import dev.voir.areca.window.WindowClass
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** Which sizing, type and gutters a subtree gets for its input and window. */
@OptIn(ExperimentalTestApi::class)
class ArecaSizingTest {
    @Test
    fun `the desktop is sized for a pointer`() = runComposeUiTest {
        lateinit var sizing: ArecaSizing
        lateinit var medium: ButtonSize
        var body = 0f
        setContent {
            ArecaThemeProvider {
                sizing = ArecaTheme.sizing
                medium = ButtonSize.Medium
                body = ArecaTheme.typography.bodyMedium.fontSize.value
            }
        }
        waitForIdle()

        assertTrue(sizing.dense)
        assertEquals(36.dp, medium.minHeight)
        assertEquals(0.dp, medium.touchTarget)
        assertEquals(14f, body)
    }

    @Test
    fun `touch keeps the sizes Areca always had`() = runComposeUiTest {
        lateinit var medium: ButtonSize
        lateinit var large: ButtonSize
        var body = 0f
        var label = 0f
        setContent {
            ArecaThemeProvider(inputMode = InputMode.Touch) {
                medium = ButtonSize.Medium
                large = ButtonSize.Large
                body = ArecaTheme.typography.bodyMedium.fontSize.value
                label = ArecaTheme.typography.labelLarge.fontSize.value
            }
        }
        waitForIdle()

        assertEquals(48.dp, medium.minHeight)
        assertEquals(48.dp, medium.touchTarget)
        assertEquals(56.dp, large.minHeight)
        assertEquals(16.sp.value, body)
        assertEquals(16.sp.value, label)
    }

    @Test
    fun `an explicit sizing wins over the input`() = runComposeUiTest {
        lateinit var sizing: ArecaSizing
        setContent {
            ArecaThemeProvider(inputMode = InputMode.Touch, sizing = ArecaSizing.pointer()) {
                sizing = ArecaTheme.sizing
            }
        }
        waitForIdle()

        assertEquals(ArecaSizing.pointer(), sizing)
    }

    @Test
    fun `a dark region keeps the sizing above it`() = runComposeUiTest {
        lateinit var sizing: ArecaSizing
        setContent {
            ArecaThemeProvider(inputMode = InputMode.Touch) {
                ArecaDarkTheme { sizing = ArecaTheme.sizing }
            }
        }
        waitForIdle()

        assertFalse(sizing.dense)
    }

    @Test
    fun `a nested provider keeps the outer input`() = runComposeUiTest {
        lateinit var sizing: ArecaSizing
        setContent {
            ArecaThemeProvider(inputMode = InputMode.Touch) {
                ArecaThemeProvider(darkTheme = true) { sizing = ArecaTheme.sizing }
            }
        }
        waitForIdle()

        assertEquals(ArecaSizing.touch(), sizing)
    }

    @Test
    fun `select picks by density`() {
        assertEquals("finger", ArecaSizing.touch().select(touch = "finger", pointer = "mouse"))
        assertEquals("mouse", ArecaSizing.pointer().select(touch = "finger", pointer = "mouse"))
        assertEquals(ArecaSizing.pointer(), ArecaSizing.forInput(InputMode.Pointer))
    }

    @Test
    fun `the screen gutter widens with the window`() = runComposeUiTest {
        val gutters = mutableMapOf<Int, Dp>()
        setContent {
            ArecaThemeProvider(inputMode = InputMode.Touch) {
                for (width in listOf(393, 700, 1180)) {
                    ProvideWindowClass(WindowClass(DpSize(width.dp, 800.dp), InputMode.Touch)) {
                        Box(Modifier.fillMaxSize().horizontalContentInsets()) {
                            Box(
                                Modifier.onGloballyPositioned { coordinates ->
                                    gutters[width] = with(density) { coordinates.positionInRoot().x.toDp() }
                                },
                            )
                        }
                    }
                }
            }
        }
        waitForIdle()

        assertEquals(16.dp, gutters.getValue(393))
        assertEquals(24.dp, gutters.getValue(700))
        assertEquals(32.dp, gutters.getValue(1180))
    }
}
