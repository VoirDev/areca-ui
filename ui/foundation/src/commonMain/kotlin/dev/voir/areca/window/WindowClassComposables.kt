package dev.voir.areca.window

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.isSpecified
import dev.voir.areca.theme.ArecaTheme

/**
 * Window class in scope, or `null` above the outermost [dev.voir.areca.theme.ArecaThemeProvider].
 * Read it through [ArecaTheme.window].
 *
 * Dynamic rather than static: a desktop window resizes continuously, and only the composables that
 * read the class should recompose when it does.
 */
internal val LocalWindowClass = compositionLocalOf<WindowClass?> { null }

/**
 * Width class of [LocalWindowClass], provided beside it so that a composable that only branches on
 * width — [adaptive], [AdaptiveContent] — recomposes when the class changes, not on every resize.
 */
internal val LocalWidthClass = compositionLocalOf<WidthClass?> { null }

/**
 * Input mode of [LocalWindowClass], provided beside it so that what depends on input alone — the
 * theme's sizing — never recomposes on a resize.
 */
internal val LocalInputMode = compositionLocalOf<InputMode?> { null }

/**
 * Class of the whole window, which a [WindowClassBox] does not replace.
 *
 * What opens over the window rather than inside a pane — a sheet, a menu — chooses its form by the
 * window, not by the pane that opened it.
 */
internal val LocalHostWindowClass = compositionLocalOf<WindowClass?> { null }

/**
 * Puts [windowClass] and the parts of it read on their own in scope for [content].
 *
 * @param windowClass The class to provide.
 * @param host Whether [windowClass] is the whole window's, as opposed to a container's.
 * @param content Content composed with it in scope.
 */
@Composable
internal fun ProvideWindowLocals(
    windowClass: WindowClass,
    host: Boolean,
    content: @Composable () -> Unit,
) {
    if (host) {
        CompositionLocalProvider(
            LocalWindowClass provides windowClass,
            LocalHostWindowClass provides windowClass,
            LocalWidthClass provides windowClass.width,
            LocalInputMode provides windowClass.input,
            content = content,
        )
    } else {
        CompositionLocalProvider(
            LocalWindowClass provides windowClass,
            LocalWidthClass provides windowClass.width,
            LocalInputMode provides windowClass.input,
            content = content,
        )
    }
}

/**
 * Class of the whole window in scope, ignoring any [WindowClassBox]: for what opens over the window.
 *
 * @return The window's class.
 */
@Composable
@ReadOnlyComposable
internal fun overlayWindowClass(): WindowClass = LocalHostWindowClass.current ?: ArecaTheme.window

/**
 * Input mode in scope without reading the window's size: the outer provider's, or the platform's.
 *
 * @return The input mode in scope.
 */
@Composable
@ReadOnlyComposable
internal fun currentInputMode(): InputMode = LocalInputMode.current ?: PlatformInputMode

/**
 * Puts [windowClass] in scope for [content], in place of the window's own.
 *
 * For previews and tests that draw a screen at a given window — a phone frame inside a desktop
 * window, a test that checks the tablet layout — and for an application that measures its window
 * some other way. A container that should report its own size uses [WindowClassBox] instead.
 *
 * It changes the class, not the theme: sizing and type stay the ones the provider above chose. To
 * draw at another input's sizes as well, nest `ArecaThemeProvider(inputMode = …)` inside.
 *
 * ```kotlin
 * ProvideWindowClass(WindowClass(DpSize(820.dp, 1180.dp), InputMode.Touch)) {
 *     InboxScreen()
 * }
 * ```
 *
 * @param windowClass The class [content] reads from [ArecaTheme.window].
 * @param content Content composed with [windowClass] in scope.
 */
@Composable
public fun ProvideWindowClass(
    windowClass: WindowClass,
    content: @Composable () -> Unit,
) {
    ProvideWindowLocals(windowClass, host = true, content = content)
}

/**
 * A box that reports its own size, rather than the window's, as the window class of its content.
 *
 * Breakpoints are a question about the space a layout has, and inside a pane that is the pane. A
 * detail pane 480dp wide on a 1440dp desktop window should lay its content out as [WidthClass.Compact],
 * which it can only do if it asks the pane. Wrap each pane of a multi-pane layout in one.
 *
 * The class takes the box's largest allowed width and height. Along an axis the box is unbounded in
 * — the height of an item in a scrolling column — it keeps the outer class's length. The input mode
 * is always the outer one.
 *
 * ```kotlin
 * Row(Modifier.fillMaxSize()) {
 *     WindowClassBox(Modifier.width(360.dp).fillMaxHeight()) { MessageList() }
 *     WindowClassBox(Modifier.weight(1f).fillMaxHeight()) { MessageDetail() }
 * }
 * ```
 *
 * @param modifier Modifier applied to the box. Its constraints are what the class is measured from.
 * @param contentAlignment Default alignment of children inside the box.
 * @param propagateMinConstraints Whether the box's minimum size passes to its children, as for a
 *   `Box`: a child then fills at least the space the box was told to fill.
 * @param content Content composed with the box's class in scope.
 */
@Composable
public fun WindowClassBox(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    propagateMinConstraints: Boolean = false,
    content: @Composable BoxScope.() -> Unit,
) {
    val outer = ArecaTheme.window
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = contentAlignment,
        propagateMinConstraints = propagateMinConstraints,
    ) {
        val outerSize = outer.size
        val width = if (constraints.hasBoundedWidth) maxWidth else outerSize.widthOrUnspecified()
        val height = if (constraints.hasBoundedHeight) maxHeight else outerSize.heightOrUnspecified()
        val windowClass = remember(width, height, outer.input) {
            WindowClass(DpSize(width, height), outer.input)
        }
        ProvideWindowLocals(windowClass, host = false) {
            content()
        }
    }
}

/**
 * Picks the value for the width class in scope, mobile first.
 *
 * Only [compact] is required; each wider step defaults to the one below it, so a caller names only
 * the steps where the value changes. Reads [ArecaTheme.window], so under a [WindowClassBox] it answers
 * for the box.
 *
 * ```kotlin
 * val columns = adaptive(compact = 2, medium = 3, expanded = 4)
 * val gutter = adaptive(compact = 16.dp, medium = 24.dp, expanded = 32.dp)
 * ```
 *
 * @param compact Value under 600dp.
 * @param medium Value from 600dp. Defaults to [compact].
 * @param expanded Value from 840dp. Defaults to [medium].
 * @param large Value from 1200dp. Defaults to [expanded].
 * @param extraLarge Value from 1600dp. Defaults to [large].
 * @return The value for the width class in scope.
 */
@Composable
@ReadOnlyComposable
public fun <T> adaptive(
    compact: T,
    medium: T = compact,
    expanded: T = medium,
    large: T = expanded,
    extraLarge: T = large,
): T = (LocalWidthClass.current ?: ArecaTheme.window.width).select(compact, medium, expanded, large, extraLarge)

/**
 * Draws one of several layouts, chosen by the width class in scope.
 *
 * Only [compact] is required; each wider step defaults to the one below it. Steps that share a
 * lambda share its state, so a window dragged from 700dp to 900dp keeps a scroll position when
 * [medium] and [expanded] are the same layout, and starts the new layout fresh when they differ.
 *
 * For a value rather than a layout — a column count, a gutter — use [adaptive].
 *
 * ```kotlin
 * AdaptiveContent(
 *     compact = { SettingsList(onOpen = navigateToSection) },
 *     medium = { SettingsWithDetail() },
 * )
 * ```
 *
 * @param compact Layout under 600dp.
 * @param medium Layout from 600dp. Defaults to [compact].
 * @param expanded Layout from 840dp. Defaults to [medium].
 * @param large Layout from 1200dp. Defaults to [expanded].
 * @param extraLarge Layout from 1600dp. Defaults to [large].
 */
@Composable
public fun AdaptiveContent(
    compact: @Composable () -> Unit,
    medium: @Composable () -> Unit = compact,
    expanded: @Composable () -> Unit = medium,
    large: @Composable () -> Unit = expanded,
    extraLarge: @Composable () -> Unit = large,
) {
    adaptive(compact, medium, expanded, large, extraLarge)()
}

/**
 * Seeds the window class for a theme provider's content.
 *
 * A nested provider keeps the class above it — a dark region inside a [WindowClassBox] still reports
 * the box — and only replaces its input mode when given one. The outermost provider reads the
 * window. Kept apart from the provider's own body so that a resize recomposes only this, and through
 * the dynamic local only the composables that read the class.
 *
 * @param inputMode Input mode to report, or `null` to keep the outer one, or the platform's at the
 *   root.
 * @param content Content composed with the class in scope.
 */
@Composable
internal fun ProvideThemeWindowClass(
    inputMode: InputMode?,
    content: @Composable () -> Unit,
) {
    val outer = LocalWindowClass.current
    val windowClass = when {
        outer == null -> hostWindowClass(inputMode ?: PlatformInputMode)
        inputMode == null || inputMode == outer.input -> outer
        else -> WindowClass(outer.size, inputMode)
    }
    // A nested provider inside a box keeps the box as its class and the window as its host.
    ProvideWindowLocals(windowClass, host = outer == null, content = content)
}

/**
 * The class of the window this composition is hosted in.
 *
 * `containerDpSize` is unspecified on a host that does not report it and zero before the first
 * layout; both read as an unspecified size rather than as a zero-sized window.
 *
 * @param input Input mode to report.
 * @return The window's class.
 */
@Composable
@ReadOnlyComposable
internal fun hostWindowClass(input: InputMode): WindowClass {
    val size = LocalWindowInfo.current.containerDpSize
    val measured = size.isSpecified && size.width.value > 0f && size.height.value > 0f
    return WindowClass(if (measured) size else DpSize.Unspecified, input)
}

private fun DpSize.widthOrUnspecified(): Dp = if (isSpecified) width else Dp.Unspecified

private fun DpSize.heightOrUnspecified(): Dp = if (isSpecified) height else Dp.Unspecified
