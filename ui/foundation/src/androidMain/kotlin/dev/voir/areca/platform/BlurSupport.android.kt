package dev.voir.areca.platform

import android.os.Build

/**
 * `true` from Android 12 (API 31) onwards, which is where `RenderEffect` arrived.
 *
 * Below it the platform has no hardware-accelerated blur at all: Compose keeps the call rather than
 * failing, and draws the content unblurred.
 */
internal actual val isBlurSupported: Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
