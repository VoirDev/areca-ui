package dev.voir.areca.platform

/** Always `true` on the desktop, which renders through Skia and blurs in software where it must. */
internal actual val isBlurSupported: Boolean = true
