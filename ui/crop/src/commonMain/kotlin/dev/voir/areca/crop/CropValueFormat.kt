package dev.voir.areca.crop

import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * Formats a straightening angle to one decimal, signed: `0.0°`, `+3.2°`, `−3.2°`.
 *
 * A typographic minus keeps the sign the same width as the plus, so the value does not jitter as it
 * crosses level.
 *
 * @param degrees Angle in degrees.
 * @return The label.
 */
internal fun formatAngle(degrees: Float): String {
    val tenths = (abs(degrees) * 10f).roundToInt()
    val sign = when {
        tenths == 0 -> ""
        degrees > 0f -> "+"
        else -> "−"
    }
    return "$sign${tenths / 10}.${tenths % 10}°"
}

/**
 * Formats a zoom to one decimal with a multiplication sign: `1.0×`, `2.5×`.
 *
 * @param zoom Magnification.
 * @return The label.
 */
internal fun formatZoom(zoom: Float): String {
    val tenths = (zoom * 10f).roundToInt()
    return "${tenths / 10}.${tenths % 10}×"
}

/**
 * Formats a quarter-turn rotation in whole degrees: `0°`, `90°`.
 *
 * @param degrees Rotation in degrees.
 * @return The label.
 */
internal fun formatRotation(degrees: Int): String = "$degrees°"
