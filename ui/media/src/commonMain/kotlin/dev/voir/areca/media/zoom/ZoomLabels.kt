package dev.voir.areca.media.zoom

import androidx.compose.runtime.Immutable

/**
 * Names of the zoom actions a screen reader offers on zoomable content, where a double tap means
 * "activate" rather than "zoom".
 *
 * @property zoomIn Brings the content closer.
 * @property zoomOut Takes it back a step.
 * @property reset Returns it to its fitted size.
 */
@Immutable
public data class ZoomLabels(
    public val zoomIn: String = "Zoom in",
    public val zoomOut: String = "Zoom out",
    public val reset: String = "Reset zoom",
)
