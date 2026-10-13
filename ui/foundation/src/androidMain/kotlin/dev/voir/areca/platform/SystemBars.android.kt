package dev.voir.areca.platform

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowInsetsController
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.DialogWindowProvider

/**
 * Applies the bars to the dialog window this is composed in, or else to the activity's, and puts
 * back what was there when it leaves the composition.
 *
 * Before Android 11 the icon appearance is the decor view's `systemUiVisibility` flags, the only
 * switch those versions have; the navigation bar's icons follow from Android 8.
 */
@Composable
public actual fun SystemBars(color: Color, lightIcons: Boolean) {
    val view = LocalView.current
    val dialogWindow = (view.parent as? DialogWindowProvider)?.window
    val window = dialogWindow ?: view.context.findActivity()?.window ?: return
    DisposableEffect(window, color, lightIcons) {
        val previous = BarsState.of(window)
        BarsState(
            statusBarColor = color.toArgb(),
            navigationBarColor = color.toArgb(),
            isStatusBarContrastEnforced = false,
            isNavigationBarContrastEnforced = false,
            hasLightIcons = lightIcons,
            dimAmount = if (dialogWindow != null) 0f else previous.dimAmount,
        ).applyTo(window)
        onDispose { previous.applyTo(window) }
    }
}

/**
 * What [SystemBars] changes on a window, read so it can be put back.
 *
 * @property statusBarColor Status bar color, as ARGB.
 * @property navigationBarColor Navigation bar color, as ARGB.
 * @property isStatusBarContrastEnforced Whether Android lays its contrast scrim over the status bar.
 * @property isNavigationBarContrastEnforced Whether Android lays its contrast scrim over the
 *   navigation bar.
 * @property hasLightIcons Whether both bars' icons are light.
 * @property dimAmount How far the window dims what is behind it, `0..1`.
 */
private data class BarsState(
    val statusBarColor: Int,
    val navigationBarColor: Int,
    val isStatusBarContrastEnforced: Boolean,
    val isNavigationBarContrastEnforced: Boolean,
    val hasLightIcons: Boolean,
    val dimAmount: Float,
) {
    /**
     * Sets this state on [window].
     *
     * @param window Window to change.
     */
    @Suppress("DEPRECATION")
    fun applyTo(window: Window) {
        window.statusBarColor = statusBarColor
        window.navigationBarColor = navigationBarColor
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isStatusBarContrastEnforced = isStatusBarContrastEnforced
            window.isNavigationBarContrastEnforced = isNavigationBarContrastEnforced
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.setSystemBarsAppearance(
                if (hasLightIcons) 0 else LightBarsAppearance,
                LightBarsAppearance,
            )
        } else {
            val flags = window.decorView.systemUiVisibility
            window.decorView.systemUiVisibility = if (hasLightIcons) flags and LightBarsFlags.inv() else flags or LightBarsFlags
        }
        window.setDimAmount(dimAmount)
    }

    /** Reading the state a window has now. */
    companion object {
        /**
         * Reads the state [window] has now.
         *
         * @param window Window to read.
         * @return Its state.
         */
        @Suppress("DEPRECATION")
        fun of(window: Window): BarsState = BarsState(
            statusBarColor = window.statusBarColor,
            navigationBarColor = window.navigationBarColor,
            isStatusBarContrastEnforced = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
                window.isStatusBarContrastEnforced,
            isNavigationBarContrastEnforced = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
                window.isNavigationBarContrastEnforced,
            hasLightIcons = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                (window.insetsController?.systemBarsAppearance ?: 0) and LightBarsAppearance == 0
            } else {
                window.decorView.systemUiVisibility and LightBarsFlags == 0
            },
            dimAmount = window.attributes.dimAmount,
        )
    }
}

/** Android 11's appearance bits for dark icons on both bars. */
private const val LightBarsAppearance =
    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS or WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS

/** The `systemUiVisibility` flags for dark icons on both bars, before Android 11. */
@Suppress("DEPRECATION")
private val LightBarsFlags: Int =
    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR else 0

/**
 * The activity this context belongs to, unwrapping the wrappers a Compose view's context is usually
 * in.
 *
 * @return The activity, or `null` for a context outside one.
 */
private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
