package dev.voir.areca.crop

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextOverflow
import dev.voir.areca.crop.icon.Angle
import dev.voir.areca.crop.icon.BundledIcons
import dev.voir.areca.crop.icon.Check
import dev.voir.areca.crop.icon.Crop
import dev.voir.areca.crop.icon.FlipBackward
import dev.voir.areca.crop.icon.Reflect
import dev.voir.areca.crop.icon.RefreshCcw
import dev.voir.areca.crop.icon.RotateCcwSquare
import dev.voir.areca.crop.icon.XClose
import dev.voir.areca.crop.icon.ZoomIn
import dev.voir.areca.foundation.button.ActionColor
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.button.ButtonSize
import dev.voir.areca.foundation.button.ButtonStyle
import dev.voir.areca.foundation.button.IconButton
import dev.voir.areca.foundation.button.IconButtonStyle
import dev.voir.areca.foundation.icon.Icon
import dev.voir.areca.foundation.overlay.ConfirmSheet
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.indication.FadeIndication
import dev.voir.areca.modifier.clickCursor
import dev.voir.areca.theme.ArecaDarkTheme
import dev.voir.areca.theme.ArecaTheme
import kotlin.math.abs

/**
 * The controls under an [ImageCropper]: a row of options above a bar that holds the screen's Cancel
 * and Done.
 *
 * The options, in order:
 * - **Reset** puts every option back to its start;
 * - **Angle**, **Zoom** and **Format** are tools. Each opens its control — a dial, or the aspect
 *   chips — in place of the row, and the bar turns into Undo, one action of the tool's own (Level,
 *   Fit, Swap) and OK. Undo steps back through every change of the session, one at a time and
 *   across tools ([ImageCropState.undo]); OK keeps the changes and brings the row back. Format is
 *   left out when [aspects] is empty or the aspect is locked;
 * - **Mirror** is a toggle, filled while on;
 * - **Rotate** turns the photo a quarter turn anticlockwise on every tap.
 *
 * The toolbar is dark whatever the application's theme, like the [ImageCropper] above it: it paints
 * [CropToolbarStyle.bg] and draws its buttons in a dark scope of the application's theme.
 *
 * A dot marks a tool or Rotate whose value has left its start. The photo stays live throughout, and
 * its drags and pinches are steps Undo walks back too.
 *
 * Cancel leaves at once on a fresh crop. Once anything has changed it asks first, in a sheet, whether
 * to discard the changes. Inside a [CropDialog] the back gesture closes an open tool, then asks the
 * same question, and only then leaves.
 *
 * The open tool and the discard question are saved instance state, like the crop
 * [rememberImageCropState] keeps, so both survive a configuration change and process death.
 *
 * @param state Crop the toolbar reads and drives.
 * @param onCancel Called to leave without a crop: by Cancel on a fresh crop, or once the person confirms
 *   discarding their changes.
 * @param onDone Called by the bar's Done, while no tool is open. Enabled once [ImageCropState.isReady].
 * @param modifier Modifier applied to the toolbar. It fills the width it is given.
 * @param title Screen title in the middle of the bar while no tool is open, or `null` for none. Read
 *   as a heading.
 * @param aspects Aspect choices the Format tool offers, in order. Defaults to the state's
 *   [CropSpec.aspects].
 * @param style Colors. Defaults to [LocalCropToolbarStyle], or [CropToolbarStyle.Default].
 * @param size Metrics. Defaults to [LocalCropToolbarSize], or [CropToolbarSize.Default].
 * @param labels Every word the toolbar shows or reads aloud, and how it writes its values.
 */
@Composable
public fun CropToolbar(
    state: ImageCropState,
    onCancel: () -> Unit,
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    aspects: List<CropAspect> = state.spec.aspects,
    style: CropToolbarStyle = LocalCropToolbarStyle.current ?: CropToolbarStyle.Default,
    size: CropToolbarSize = LocalCropToolbarSize.current ?: CropToolbarSize.Default,
    labels: CropToolbarLabels = CropToolbarLabels(),
) {
    var tool by rememberSaveable(state, stateSaver = CropToolSaver) { mutableStateOf<CropTool?>(null) }
    var isConfirmingDiscard by rememberSaveable(state) { mutableStateOf(false) }
    val open: (CropTool) -> Unit = { next ->
        if (state.isReady) {
            // Opening a tool is a step of its own, so Undo always has the tool's starting point.
            state.checkpoint()
            tool = next
        }
    }
    val close: () -> Unit = { tool = null }
    val cancel: () -> Unit = { if (state.canReset) isConfirmingDiscard = true else onCancel() }
    val back: () -> Boolean = {
        when {
            tool != null -> {
                close()
                true
            }
            state.canReset -> {
                isConfirmingDiscard = true
                true
            }
            else -> false
        }
    }
    RegisterBack(back)

    ArecaDarkTheme {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(style.bg),
        ) {
            AnimatedContent(
                targetState = tool,
                modifier = Modifier.fillMaxWidth().height(size.optionsHeight),
                transitionSpec = {
                    (fadeIn(tween(EnterMillis)) + slideInVertically(tween(EnterMillis)) { it / SlideFraction }) togetherWith
                        fadeOut(tween(ExitMillis))
                },
                label = "cropToolbarOptions",
            ) { current ->
                when (current) {
                    null -> OptionRow(
                        state = state,
                        hasFormat = aspects.isNotEmpty() && !state.spec.isAspectLocked,
                        style = style,
                        size = size,
                        labels = labels,
                        onOpen = open,
                    )
                    CropTool.Angle, CropTool.Zoom -> DialPanel(state, current, style, size, labels)
                    CropTool.Format -> FormatPanel(state, aspects, style, size, labels)
                }
            }
            Bar(
                state = state,
                tool = tool,
                title = title,
                style = style,
                labels = labels,
                onCancel = cancel,
                onDone = onDone,
                onOk = close,
            )
        }
        ConfirmSheet(
            visible = isConfirmingDiscard,
            icon = null,
            title = labels.discardTitle,
            description = labels.discardDescription,
            confirmText = labels.discard,
            onConfirm = {
                isConfirmingDiscard = false
                onCancel()
            },
            onDismiss = { isConfirmingDiscard = false },
            cancelText = labels.keepEditing,
        )
    }
}

/**
 * Answers the back gesture of the enclosing [CropDialog] with [onBack] while this toolbar is shown.
 * Outside a dialog there is no gesture to answer, and nothing is registered.
 *
 * @param onBack Handles back; returns `true` when it did, so the dialog stays.
 */
@Composable
private fun RegisterBack(onBack: () -> Boolean) {
    val handler = LocalCropBackHandler.current ?: return
    val latest by rememberUpdatedState(onBack)
    DisposableEffect(handler) {
        val registered: () -> Boolean = { latest() }
        handler.onBack = registered
        onDispose { if (handler.onBack === registered) handler.onBack = null }
    }
}

/**
 * The options: Reset, a rule, then Angle, Zoom, Format, Mirror and Rotate. Centred, and scrolling
 * sideways when they outgrow the width.
 *
 * Reads the crop through one [OptionReadout], so a drag or pinch on the stage recomposes the row only
 * when something it shows changes, not on every frame.
 *
 * @param state Crop the options read and drive.
 * @param hasFormat Whether to offer Format.
 * @param style Colors.
 * @param size Metrics.
 * @param labels Words.
 * @param onOpen Opens a tool.
 */
@Composable
private fun OptionRow(
    state: ImageCropState,
    hasFormat: Boolean,
    style: CropToolbarStyle,
    size: CropToolbarSize,
    labels: CropToolbarLabels,
    onOpen: (CropTool) -> Unit,
) {
    val readout by remember(state, labels) { derivedStateOf { OptionReadout.of(state, labels) } }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = ArecaTheme.spacing.md),
            horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xs2),
        ) {
            CropOption(
                icon = BundledIcons.RefreshCcw,
                label = labels.reset,
                onClick = state::reset,
                style = style,
                size = size,
                enabled = readout.canReset,
            )
            Box(
                modifier = Modifier
                    .padding(horizontal = ArecaTheme.spacing.xs)
                    .padding(top = ArecaTheme.spacing.xs + (size.optionCircle - size.separatorHeight) / 2)
                    .width(size.separatorWidth)
                    .height(size.separatorHeight)
                    .background(style.separator),
            )
            CropOption(
                icon = BundledIcons.Angle,
                label = labels.angle,
                onClick = { onOpen(CropTool.Angle) },
                style = style,
                size = size,
                enabled = readout.isReady,
                isChanged = readout.isAngleChanged,
                stateDescription = readout.angle,
            )
            CropOption(
                icon = BundledIcons.ZoomIn,
                label = labels.zoom,
                onClick = { onOpen(CropTool.Zoom) },
                style = style,
                size = size,
                enabled = readout.isReady,
                isChanged = readout.isZoomChanged,
                stateDescription = readout.zoom,
            )
            if (hasFormat) {
                CropOption(
                    icon = BundledIcons.Crop,
                    label = labels.format,
                    onClick = { onOpen(CropTool.Format) },
                    style = style,
                    size = size,
                    enabled = readout.isReady,
                    isChanged = readout.isFormatChanged,
                    stateDescription = readout.format,
                )
            }
            CropOption(
                icon = BundledIcons.Reflect,
                label = labels.mirror,
                onClick = state::flipHorizontally,
                style = style,
                size = size,
                enabled = readout.isReady,
                checked = readout.isMirrored,
            )
            CropOption(
                icon = BundledIcons.RotateCcwSquare,
                label = labels.rotate,
                onClick = state::rotateLeft,
                style = style,
                size = size,
                enabled = readout.isReady,
                isChanged = readout.isRotated,
                stateDescription = readout.rotation,
            )
        }
    }
}

/**
 * What the option row shows of a crop, compared as a whole so the row recomposes only when it
 * changes.
 *
 * @property isReady Whether the crop can be read and changed.
 * @property canReset Whether Reset would change anything.
 * @property isAngleChanged Whether the straightening has left level.
 * @property angle The straightening as Angle reads it aloud.
 * @property isZoomChanged Whether the zoom has left the cover size.
 * @property zoom The zoom as Zoom reads it aloud.
 * @property isFormatChanged Whether the aspect or its orientation has left the spec's.
 * @property format The aspect as Format reads it aloud.
 * @property isMirrored Whether the photo is mirrored.
 * @property isRotated Whether the photo is turned by a quarter turn or more.
 * @property rotation The quarter-turn rotation as Rotate reads it aloud.
 */
@Immutable
private data class OptionReadout(
    val isReady: Boolean,
    val canReset: Boolean,
    val isAngleChanged: Boolean,
    val angle: String,
    val isZoomChanged: Boolean,
    val zoom: String,
    val isFormatChanged: Boolean,
    val format: String,
    val isMirrored: Boolean,
    val isRotated: Boolean,
    val rotation: String,
) {
    /** Reading a crop. */
    companion object {
        /**
         * Reads [state] as the option row shows it.
         *
         * @param state Crop to read.
         * @param labels How the values are written.
         * @return The readout.
         */
        fun of(state: ImageCropState, labels: CropToolbarLabels): OptionReadout = OptionReadout(
            isReady = state.isReady,
            canReset = state.canReset,
            isAngleChanged = abs(state.straighten) > ChangedTolerance,
            angle = labels.angleValue(state.straighten),
            isZoomChanged = state.zoom - 1f > ChangedTolerance,
            zoom = labels.zoomValue(state.zoom),
            isFormatChanged = state.aspect != state.spec.aspect || state.isOrientationSwapped,
            format = aspectLabel(state, state.aspect, labels),
            isMirrored = state.isMirrored,
            isRotated = state.rotation != 0,
            rotation = labels.rotationValue(state.rotation),
        )
    }
}

/**
 * One option: an icon in a circle over its name.
 *
 * Answers a press with foundation's tint indication over the whole option, whose focus ring follows
 * the option's rounded shape; the option is not clipped, so the ring is drawn whole.
 *
 * @param icon Icon of the option.
 * @param label Name of the option, shown and read aloud.
 * @param onClick Called when the option is pressed, or toggled when it is a toggle.
 * @param style Colors.
 * @param size Metrics.
 * @param enabled Whether the option can be pressed.
 * @param isChanged Whether the option's value has left its start, marked with a dot.
 * @param checked Whether a toggle option is on, or `null` for an option that is pressed rather than
 *   toggled.
 * @param stateDescription The option's value as it should be read aloud, or `null` for none.
 */
@Composable
private fun CropOption(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    style: CropToolbarStyle,
    size: CropToolbarSize,
    enabled: Boolean = true,
    isChanged: Boolean = false,
    checked: Boolean? = null,
    stateDescription: String? = null,
) {
    val isOn = checked == true
    val container by animateColorAsState(
        targetValue = when {
            !enabled -> Color.Transparent
            isOn -> style.optionSelectedBg
            else -> style.optionBg
        },
        label = "cropOptionBg",
    )
    val content by animateColorAsState(
        targetValue = when {
            !enabled -> style.optionDisabledContent
            isOn -> style.optionSelectedContent
            else -> style.optionContent
        },
        label = "cropOptionContent",
    )
    val interactionSource = remember { MutableInteractionSource() }
    val indication = FadeIndication.tint(shape = ArecaTheme.shapes.md, ink = ArecaTheme.colors.fgBase)
    val press = if (checked == null) {
        Modifier.clickable(
            interactionSource = interactionSource,
            indication = indication,
            enabled = enabled,
            role = Role.Button,
            onClick = onClick,
        )
    } else {
        Modifier.toggleable(
            value = checked,
            interactionSource = interactionSource,
            indication = indication,
            enabled = enabled,
            role = Role.Switch,
            onValueChange = { onClick() },
        )
    }
    Column(
        modifier = Modifier
            .width(size.optionWidth)
            .then(press)
            .clickCursor(enabled)
            .then(if (stateDescription == null) Modifier else Modifier.semantics { this.stateDescription = stateDescription })
            .padding(vertical = ArecaTheme.spacing.xs),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.sm),
    ) {
        Box(modifier = Modifier.size(size.optionCircle)) {
            Box(
                modifier = Modifier.fillMaxSize().background(container, ArecaTheme.shapes.full),
                contentAlignment = Alignment.Center,
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = content, size = size.optionIcon)
            }
            if (isChanged && enabled) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(size.changedDotInset)
                        .size(size.changedDot)
                        .background(style.optionChangedIndicator, ArecaTheme.shapes.full),
                )
            }
        }
        Text(
            text = label,
            style = ArecaTheme.typography.labelSmall,
            color = when {
                !enabled -> style.optionDisabledContent
                isOn -> style.optionSelectedLabel
                else -> style.optionLabel
            },
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

/**
 * The Angle or Zoom tool: the value over a dial.
 *
 * The dial and its readout follow the value as drawn, so they move with an animation such as Level's
 * or Fit's; the panel is the one part of the toolbar that recomposes every frame of a drag.
 *
 * @param state Crop the dial drives.
 * @param tool [CropTool.Angle] or [CropTool.Zoom].
 * @param style Colors.
 * @param size Metrics.
 * @param labels Words.
 */
@Composable
private fun DialPanel(
    state: ImageCropState,
    tool: CropTool,
    style: CropToolbarStyle,
    size: CropToolbarSize,
    labels: CropToolbarLabels,
) {
    val isAngle = tool == CropTool.Angle
    val value = if (isAngle) state.liveStraighten else state.liveZoom
    val origin = if (isAngle) 0f else 1f
    val valueText = if (isAngle) labels.angleValue(value) else labels.zoomValue(value)

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = ArecaTheme.spacing.xl),
        verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xs, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = valueText,
            style = ArecaTheme.typography.labelMedium.copy(fontFeatureSettings = "tnum"),
            color = if (abs(value - origin) > ChangedTolerance) style.valueChanged else style.value,
        )
        CropDial(
            value = value,
            onValueChange = { if (isAngle) state.setStraighten(it) else state.setZoom(it) },
            valueRange = if (isAngle) -MaxAngle..MaxAngle else 1f..state.spec.maxZoom,
            contentDescription = if (isAngle) labels.angle else labels.zoom,
            tickStep = if (isAngle) 1f else ZoomTickStep,
            majorTickEvery = if (isAngle) AngleMajorEvery else ZoomMajorEvery,
            origin = origin,
            stateDescription = valueText,
            style = style.dial,
            size = size.dial,
            onAdjustStart = { state.startAdjusting(straightening = isAngle) },
            onAdjustEnd = { state.stopAdjusting() },
        )
    }
}

/**
 * The Format tool: one chip per aspect, scrolling sideways when they outgrow the width. The row is a
 * selectable group, so assistive technology reads the chips as one set of choices.
 *
 * @param state Crop whose aspect the chips choose.
 * @param aspects Aspect choices, in order.
 * @param style Colors.
 * @param size Metrics.
 * @param labels Words.
 */
@Composable
private fun FormatPanel(
    state: ImageCropState,
    aspects: List<CropAspect>,
    style: CropToolbarStyle,
    size: CropToolbarSize,
    labels: CropToolbarLabels,
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .selectableGroup()
                .padding(horizontal = ArecaTheme.spacing.xl),
            horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            aspects.forEach { aspect ->
                AspectChip(
                    label = aspectLabel(state, aspect, labels),
                    glyphRatio = when (aspect) {
                        CropAspect.Free -> null
                        CropAspect.Original -> state.originalRatio()
                        is CropAspect.Ratio -> state.oriented(aspect).let { (width, height) -> width.toFloat() / height }
                    },
                    selected = state.aspect == aspect,
                    onClick = { state.selectAspect(aspect) },
                    style = style,
                    size = size,
                )
            }
        }
    }
}

/**
 * The bar: Cancel, the title and Done while no tool is open; Undo, the tool's action and OK while one
 * is.
 *
 * @param state Crop the bar reads.
 * @param tool The open tool, or `null`.
 * @param title Screen title, or `null` for none.
 * @param style Colors.
 * @param labels Words.
 * @param onCancel Leaves without a crop, asking first when something has changed.
 * @param onDone Keeps the crop.
 * @param onOk Closes the open tool, keeping its changes.
 */
@Composable
private fun Bar(
    state: ImageCropState,
    tool: CropTool?,
    title: String?,
    style: CropToolbarStyle,
    labels: CropToolbarLabels,
    onCancel: () -> Unit,
    onDone: () -> Unit,
    onOk: () -> Unit,
) {
    val isToolOpen = tool != null
    val isReady by remember(state) { derivedStateOf { state.isReady } }
    val canUndo by remember(state) { derivedStateOf { state.canUndo } }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = ArecaTheme.spacing.lg, vertical = ArecaTheme.spacing.md),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
    ) {
        IconButton(
            imageVector = if (isToolOpen) BundledIcons.FlipBackward else BundledIcons.XClose,
            contentDescription = if (isToolOpen) labels.undo else labels.cancel,
            onClick = if (isToolOpen) state::undo else onCancel,
            style = IconButtonStyle.plain(ActionColor.Neutral, shape = ArecaTheme.shapes.full),
            enabled = !isToolOpen || canUndo,
        )
        AnimatedContent(
            targetState = tool,
            modifier = Modifier.weight(1f),
            transitionSpec = { fadeIn(tween(EnterMillis)) togetherWith fadeOut(tween(ExitMillis)) },
            contentAlignment = Alignment.Center,
            label = "cropToolbarBarMiddle",
        ) { current ->
            // The slot is as wide as the weight gives it; centre what sits in it rather than stretch it.
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                when (current) {
                    null -> if (title != null) {
                        Text(
                            text = title,
                            modifier = Modifier.semantics { heading() },
                            style = ArecaTheme.typography.labelLarge,
                            color = style.title,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    else -> ToolAction(state, current, labels)
                }
            }
        }
        IconButton(
            imageVector = BundledIcons.Check,
            contentDescription = if (isToolOpen) labels.ok else labels.done,
            onClick = if (isToolOpen) onOk else onDone,
            // Plain like Cancel, in the brightest ink: the way forward, without a fill.
            style = IconButtonStyle.plain(ActionColor.Neutral, shape = ArecaTheme.shapes.full)
                .copy(content = ArecaTheme.colors.fgBase),
            enabled = isToolOpen || isReady,
        )
    }
}

/**
 * The open tool's one action, in the middle of the bar: Level for Angle, Fit for Zoom, Swap for
 * Format. Disabled while it would change nothing.
 *
 * @param state Crop the action drives.
 * @param tool The open tool.
 * @param labels Words.
 */
@Composable
private fun ToolAction(state: ImageCropState, tool: CropTool, labels: CropToolbarLabels) {
    val enabled by remember(state, tool) {
        derivedStateOf {
            when (tool) {
                CropTool.Angle -> abs(state.straighten) > ChangedTolerance
                CropTool.Zoom -> state.zoom - 1f > ChangedTolerance
                CropTool.Format -> state.canSwapOrientation
            }
        }
    }
    val (text, onClick) = when (tool) {
        CropTool.Angle -> labels.level to state::resetStraighten
        CropTool.Zoom -> labels.fit to state::resetZoom
        CropTool.Format -> labels.swapOrientation to state::swapOrientation
    }
    Button(
        text = text,
        onClick = onClick,
        style = ButtonStyle.outlined(ActionColor.Neutral, shape = ArecaTheme.shapes.full),
        size = ButtonSize.Small,
        enabled = enabled,
    )
}

/**
 * Label of an aspect as the chips and Format's state show it.
 *
 * @param state Crop whose orientation a ratio follows.
 * @param aspect Aspect to name.
 * @param labels Words for the free and original aspects.
 * @return The label, such as "Free" or "3:4".
 */
private fun aspectLabel(state: ImageCropState, aspect: CropAspect, labels: CropToolbarLabels): String = when (aspect) {
    CropAspect.Free -> labels.free
    CropAspect.Original -> labels.original
    is CropAspect.Ratio -> state.oriented(aspect).let { (width, height) -> "$width:$height" }
}

/**
 * A ratio's sides as the frame currently applies them, swapped when the orientation is.
 *
 * @param ratio Ratio to orient.
 * @return Width and height parts.
 */
private fun ImageCropState.oriented(ratio: CropAspect.Ratio): Pair<Int, Int> =
    if (isOrientationSwapped && !ratio.isSquare) ratio.height to ratio.width else ratio.width to ratio.height

/**
 * The photo's width over height as currently turned, or square until the photo is known.
 *
 * @return Width over height of the turned photo, or `1` before [ImageCropState.isReady].
 */
private fun ImageCropState.originalRatio(): Float {
    if (!isReady) return 1f
    val ratio = imageSize.width / imageSize.height
    return if (rotation % 180 == 90) 1f / ratio else ratio
}

/** An option that opens its own control in place of the option row. */
private enum class CropTool {
    /** The straightening dial. */
    Angle,

    /** The zoom dial. */
    Zoom,

    /** The aspect chips. */
    Format,
}

/** Saves the open [CropTool], or none, as its position among the tools; `-1` for none. */
private val CropToolSaver: Saver<CropTool?, Int> = Saver(
    save = { tool -> tool?.ordinal ?: -1 },
    restore = { ordinal -> CropTool.entries.getOrNull(ordinal) },
)

/** Largest fine rotation either way on the dial, in degrees. */
private const val MaxAngle = 45f

/** Zoom between neighbouring ticks. */
private const val ZoomTickStep = 0.1f

/** Angle ticks between major ticks: every 5°. */
private const val AngleMajorEvery = 5

/** Zoom ticks between major ticks: every whole step. */
private const val ZoomMajorEvery = 10

/** How far a value must leave its origin before it reads as changed and can be reset. */
private const val ChangedTolerance = 0.04f

/** Length of the incoming side of a swap between the row and a tool. */
private const val EnterMillis = 220

/** Length of the outgoing side of a swap between the row and a tool. */
private const val ExitMillis = 160

/** The incoming side rises by its height over this. */
private const val SlideFraction = 10
