package dev.voir.areca.flow.auth.sso

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import dev.voir.areca.flow.auth.AuthProvider
import dev.voir.areca.theme.ArecaColors
import kotlin.math.max
import kotlin.math.min
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Tests for the colours each provider's button is drawn in, checked against the providers' own
 * button guidelines and against WCAG AA for the label.
 */
class SsoBrandTest {
    private val light = ArecaColors.light()
    private val dark = ArecaColors.dark()

    @Test
    fun `Apple is black on a light theme and white on a dark one`() {
        val onLight = AuthProvider.Apple.brand(isDark = false, colors = light)
        val onDark = AuthProvider.Apple.brand(isDark = true, colors = dark)

        assertEquals(Color.Black, onLight.bg)
        assertEquals(Color.White, onLight.content)
        assertEquals(Color.White, onDark.bg)
        assertEquals(Color.Black, onDark.content)
    }

    @Test
    fun `Google takes its neutral theme on light and its dark theme on dark`() {
        val onLight = AuthProvider.Google.brand(isDark = false, colors = light)
        val onDark = AuthProvider.Google.brand(isDark = true, colors = dark)

        assertEquals(Color(0xFFF2F2F2), onLight.bg)
        assertEquals(Color(0xFF1F1F1F), onLight.content)
        assertEquals(Color.Transparent, onLight.border)
        assertEquals(Color(0xFF131314), onDark.bg)
        assertEquals(Color(0xFFE3E3E3), onDark.content)
        assertEquals(Color(0xFF8E918F), onDark.border)
    }

    @Test
    fun `the Google mark is never tinted`() {
        assertFalse(AuthProvider.Google.brand(isDark = false, colors = light).tintMark)
        assertFalse(AuthProvider.Google.brand(isDark = true, colors = dark).tintMark)
    }

    @Test
    fun `Facebook is its brand blue in either theme`() {
        assertEquals(Color(0xFF0866FF), AuthProvider.Facebook.brand(isDark = false, colors = light).bg)
        assertEquals(Color(0xFF0866FF), AuthProvider.Facebook.brand(isDark = true, colors = dark).bg)
    }

    @Test
    fun `every label meets AA contrast on its fill in both themes`() {
        listOf(false to light, true to dark).forEach { (isDark, colors) ->
            AuthProvider.entries.forEach { provider ->
                val brand = provider.brand(isDark = isDark, colors = colors)
                val ratio = contrast(brand.content, brand.bg)
                assertTrue(ratio >= 4.5, "$provider (dark = $isDark) label contrast is $ratio")
            }
        }
    }

    /**
     * WCAG contrast ratio between two opaque colours.
     *
     * @param a One colour.
     * @param b The other colour.
     * @return Ratio from 1 to 21.
     */
    private fun contrast(a: Color, b: Color): Double {
        val la = a.luminance().toDouble()
        val lb = b.luminance().toDouble()
        return (max(la, lb) + 0.05) / (min(la, lb) + 0.05)
    }
}
