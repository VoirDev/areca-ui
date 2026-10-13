package dev.voir.areca.crop

import androidx.compose.runtime.Immutable

/**
 * Every word a [CropToolbar] shows or reads aloud, and the actions an [ImageCropper] offers assistive
 * technology, with English defaults. Pass the product's own to both.
 *
 * The values the toolbar shows and reads aloud — an angle, a zoom, a rotation — are written by
 * functions here too, so a product can write them in its own language and number format. Pass
 * function references or top-level values: labels that compare unequal recompose the toolbar.
 *
 * @property reset Option that puts every option back to its start.
 * @property angle Option and dial that straighten the photo.
 * @property zoom Option and dial that magnify the photo.
 * @property format Option that chooses the frame's proportions.
 * @property mirror Option that mirrors the photo left to right.
 * @property rotate Option that turns the photo a quarter turn anticlockwise.
 * @property cancel Bar button that leaves without a crop.
 * @property done Bar button that keeps the crop.
 * @property undo Bar button that steps back through the changes, one at a time.
 * @property ok Bar button that keeps an open tool's changes and closes it.
 * @property level Angle tool's action: back to level.
 * @property fit Zoom tool's action: back to the size at which the photo just covers the frame.
 * @property swapOrientation Format tool's action: turn the ratio on its side, 4:3 to 3:4.
 * @property discardTitle Question asked before leaving with changes.
 * @property discardDescription What leaving with changes means, under [discardTitle].
 * @property discard Answer that leaves and throws the changes away.
 * @property keepEditing Answer that stays in the cropper.
 * @property free The [CropAspect.Free] chip.
 * @property original The [CropAspect.Original] chip.
 * @property moveLeft Stage action that moves the photo left.
 * @property moveRight Stage action that moves the photo right.
 * @property moveUp Stage action that moves the photo up.
 * @property moveDown Stage action that moves the photo down.
 * @property zoomIn Stage action that magnifies the photo.
 * @property zoomOut Stage action that shrinks the photo back towards the size that covers the frame.
 * @property widenFrame Stage action that makes a free frame wider for its height, as dragging its grips
 *   would.
 * @property heightenFrame Stage action that makes a free frame taller for its width.
 * @property angleValue Writes a straightening angle in degrees, as Angle's value and the Angle dial's
 *   readout show it and read it aloud: `+3.2°` by default.
 * @property zoomValue Writes a magnification, as Zoom's value and the Zoom dial's readout show it and
 *   read it aloud: `2.5×` by default.
 * @property rotationValue Writes Rotate's quarter-turn rotation in degrees, as read aloud: `90°` by
 *   default.
 */
@Immutable
public data class CropToolbarLabels(
    public val reset: String = "Reset",
    public val angle: String = "Angle",
    public val zoom: String = "Zoom",
    public val format: String = "Format",
    public val mirror: String = "Mirror",
    public val rotate: String = "Rotate",
    public val cancel: String = "Cancel",
    public val done: String = "Done",
    public val undo: String = "Undo",
    public val ok: String = "OK",
    public val level: String = "Level",
    public val fit: String = "Fit",
    public val swapOrientation: String = "Swap",
    public val discardTitle: String = "Discard changes?",
    public val discardDescription: String = "The changes you made to this photo will be lost.",
    public val discard: String = "Discard",
    public val keepEditing: String = "Keep editing",
    public val free: String = "Free",
    public val original: String = "Original",
    public val moveLeft: String = "Move photo left",
    public val moveRight: String = "Move photo right",
    public val moveUp: String = "Move photo up",
    public val moveDown: String = "Move photo down",
    public val zoomIn: String = "Zoom in",
    public val zoomOut: String = "Zoom out",
    public val widenFrame: String = "Make frame wider",
    public val heightenFrame: String = "Make frame taller",
    public val angleValue: (degrees: Float) -> String = ::formatAngle,
    public val zoomValue: (zoom: Float) -> String = ::formatZoom,
    public val rotationValue: (degrees: Int) -> String = ::formatRotation,
)
