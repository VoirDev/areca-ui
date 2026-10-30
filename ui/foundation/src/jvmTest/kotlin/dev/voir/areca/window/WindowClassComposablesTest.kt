package dev.voir.areca.window

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import dev.voir.areca.theme.ArecaDarkTheme
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.ArecaThemeProvider
import kotlin.test.Test
import kotlin.test.assertEquals

/** Which window class a subtree reads, through providers, boxes and nested themes. */
@OptIn(ExperimentalTestApi::class)
class WindowClassComposablesTest {
    private val laptop = WindowClass(DpSize(1440.dp, 900.dp), InputMode.Pointer)
    private val tablet = WindowClass(DpSize(820.dp, 1180.dp), InputMode.Touch)

    @Test
    fun `the root provider reports the desktop as pointer driven`() = runComposeUiTest {
        lateinit var read: WindowClass
        setContent {
            ArecaThemeProvider { read = ArecaTheme.window }
        }
        waitForIdle()

        assertEquals(InputMode.Pointer, read.input)
    }

    @Test
    fun `a provided class wins over the window`() = runComposeUiTest {
        lateinit var read: WindowClass
        setContent {
            ArecaThemeProvider {
                ProvideWindowClass(tablet) { read = ArecaTheme.window }
            }
        }
        waitForIdle()

        assertEquals(tablet, read)
        assertEquals(WindowProfile.TabletPortrait, read.profile)
    }

    @Test
    fun `a nested theme keeps the class above it`() = runComposeUiTest {
        lateinit var read: WindowClass
        setContent {
            ArecaThemeProvider {
                ProvideWindowClass(tablet) {
                    ArecaDarkTheme { read = ArecaTheme.window }
                }
            }
        }
        waitForIdle()

        assertEquals(tablet, read)
    }

    @Test
    fun `a nested theme given an input mode keeps the size`() = runComposeUiTest {
        lateinit var read: WindowClass
        setContent {
            ArecaThemeProvider {
                ProvideWindowClass(tablet) {
                    ArecaThemeProvider(inputMode = InputMode.Pointer) { read = ArecaTheme.window }
                }
            }
        }
        waitForIdle()

        assertEquals(WindowClass(tablet.size, InputMode.Pointer), read)
    }

    @Test
    fun `a box reports its own size as the class`() = runComposeUiTest {
        lateinit var pane: WindowClass
        setContent {
            ArecaThemeProvider {
                ProvideWindowClass(laptop) {
                    WindowClassBox(Modifier.size(width = 480.dp, height = 700.dp)) { pane = ArecaTheme.window }
                }
            }
        }
        waitForIdle()

        assertEquals(DpSize(480.dp, 700.dp), pane.size)
        assertEquals(WidthClass.Compact, pane.width)
        assertEquals(InputMode.Pointer, pane.input)
    }

    @Test
    fun `a box that is unbounded vertically keeps the outer height`() = runComposeUiTest {
        lateinit var item: WindowClass
        setContent {
            ArecaThemeProvider {
                ProvideWindowClass(laptop) {
                    Column(Modifier.width(900.dp).height(400.dp).verticalScroll(rememberScrollState())) {
                        WindowClassBox { item = ArecaTheme.window }
                    }
                }
            }
        }
        waitForIdle()

        assertEquals(DpSize(900.dp, 900.dp), item.size)
        assertEquals(WidthClass.Expanded, item.width)
    }

    @Test
    fun `a dark region inside a box still reports the box`() = runComposeUiTest {
        lateinit var read: WindowClass
        setContent {
            ArecaThemeProvider {
                ProvideWindowClass(laptop) {
                    WindowClassBox(Modifier.width(360.dp).fillMaxHeight()) {
                        ArecaDarkTheme { read = ArecaTheme.window }
                    }
                }
            }
        }
        waitForIdle()

        assertEquals(WidthClass.Compact, read.width)
    }

    @Test
    fun `adaptive values and content follow the width class in scope`() = runComposeUiTest {
        var columns = 0
        var drawn = ""
        setContent {
            ArecaThemeProvider {
                ProvideWindowClass(laptop) {
                    Box(Modifier.fillMaxSize()) {
                        columns = adaptive(compact = 2, medium = 3, expanded = 4)
                        WindowClassBox(Modifier.width(700.dp)) {
                            AdaptiveContent(
                                compact = { drawn = "list" },
                                medium = { drawn = "list and detail" },
                            )
                        }
                    }
                }
            }
        }
        waitForIdle()

        assertEquals(4, columns)
        assertEquals("list and detail", drawn)
    }
}
