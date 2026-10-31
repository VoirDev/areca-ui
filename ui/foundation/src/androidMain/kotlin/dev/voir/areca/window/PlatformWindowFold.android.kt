package dev.voir.areca.window

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpRect
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker
import androidx.window.layout.WindowLayoutInfo

/** The first folding feature of the activity's window, from Jetpack WindowManager. */
@Composable
internal actual fun platformWindowFold(): WindowFold? {
    val context = LocalContext.current
    val activity = remember(context) { context.findActivity() } ?: return null
    val layouts = remember(activity) { WindowInfoTracker.getOrCreate(activity).windowLayoutInfo(activity) }
    val layout: WindowLayoutInfo? by layouts.collectAsState(initial = null)
    val feature = layout?.displayFeatures?.filterIsInstance<FoldingFeature>()?.firstOrNull() ?: return null
    val density = LocalDensity.current
    val bounds = with(density) {
        DpRect(
            left = feature.bounds.left.toDp(),
            top = feature.bounds.top.toDp(),
            right = feature.bounds.right.toDp(),
            bottom = feature.bounds.bottom.toDp(),
        )
    }
    return WindowFold(
        orientation = if (feature.orientation == FoldingFeature.Orientation.VERTICAL) FoldOrientation.Vertical else FoldOrientation.Horizontal,
        bounds = bounds,
        state = if (feature.state == FoldingFeature.State.HALF_OPENED) FoldState.HalfOpened else FoldState.Flat,
        isSeparating = feature.isSeparating,
    )
}

/** The activity this context belongs to, unwrapping themed and other wrapper contexts. */
private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
