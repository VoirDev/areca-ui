package dev.voir.areca.foundation.list

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Indication
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.triStateToggleable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.foundation.LocalTextStyle
import dev.voir.areca.foundation.icon.LocalChevronSize
import dev.voir.areca.foundation.icon.LocalIconSize
import dev.voir.areca.foundation.slot.SlotRow
import dev.voir.areca.foundation.slot.SlotRowLayout
import dev.voir.areca.foundation.slot.firstLineCenter
import dev.voir.areca.indication.buildShapePath
import dev.voir.areca.modifier.clickCursor
import dev.voir.areca.modifier.thenIf
import dev.voir.areca.theme.tween

/**
 * A row of a list, a menu, or anything else made of stacked lines.
 *
 * The row owns its interaction states instead of delegating them to an [Indication]. A clickable
 * row tints its background on hover, wears that same tint when it takes keyboard focus, deepens it
 * on press, and shows a rail along its leading edge while selected — stepping its content aside to
 * make room for it. Keeping selection and interaction on separate layers is what lets a selected
 * row stay visibly selected while the pointer is over it. Every transition is timed by
 * [ListItemSize.stateAnimation], and slots can follow along by reading [LocalListItemState].
 *
 * Content comes from either the text block — [overline], [title] and [subtitle] — or from
 * [content], never both; one of the two is required. [value] renders inline before [trailing],
 * which is the usual shape of a row that states a current value and then a chevron.
 *
 * The row aligns its slots by [SlotRowLayout.Auto]: a row of one or two lines centres everything,
 * and a longer one starts its visual and its text at the top, with the trailing slot level with the
 * visual — see [dev.voir.areca.foundation.slot.SlotRowAlignment]. Pass [layout] for a scenario that
 * asks otherwise — [SlotRowLayout.FirstLine] beside a legend dot, say. [content] that wants the
 * same reports its first line with [dev.voir.areca.foundation.slot.firstLineCenter].
 *
 * The appearance is chosen rather than assembled: pick a [ListItemStyle] appearance and a
 * [ListItemColor] instead of passing a background per row.
 *
 * The row is one accessibility node: its slots merge into it, so a screen reader reads the whole
 * row and a keyboard reaches it once. A row that toggles or picks something is a
 * [CheckboxListItem], [RadioListItem] or [ToggleListItem], which report the control's state on the
 * row itself.
 *
 * ```kotlin
 * ListItem(
 *     title = { Text("Delete workspace") },
 *     subtitle = { Text("This cannot be undone") },
 *     style = ListItemStyle.plain(ListItemColor.Danger),
 *     onClick = { confirmDeletion() },
 * )
 * ```
 *
 * @param modifier Modifier applied to the row container.
 * @param title Main copy of the row; ignored when [content] is provided.
 * @param subtitle Supporting copy below [title].
 * @param overline Small copy above [title], for a category or a timestamp.
 * @param caption A line of detail below [subtitle] — when a device was last seen, how large a file
 * is — set smaller than the subtitle and in its color.
 * @param value Inline value rendered at the end of the row, before [trailing].
 * @param enabled Whether the row accepts input and uses its enabled colors. A row inside a disabled
 * list is disabled regardless of this flag.
 * @param selected Whether the row is one of the selected rows in its list. Reported to
 * [LocalListItemState] and, while `true`, to accessibility, whether or not it is drawn. An
 * unselected row reports nothing, so a list without a selection does not announce "not selected"
 * on every row.
 * @param highlightSelected Whether a selected row draws its selected background, rail and copy.
 * Turn it off in a list that already states the selection some other way — a checkbox column, a
 * trailing check mark — so a long list does not become a wall of color.
 * @param dragging Whether the row is currently being dragged, in a reorderable list.
 * @param nestingLevel Indentation steps of [ListItemSize.nestedLevelOffset] to add.
 * @param style Colors of the row across its states.
 * @param size Metrics and typography of the row.
 * @param layout Slot layout of the main row line; its gaps come from [size]. The list's, which is
 * [SlotRowLayout.Auto] unless a list provides another.
 * @param interactionSource Source of the row interactions; pass one to observe them from outside.
 * @param onClick Click handler; omit for a row that only displays.
 * @param onClickLabel Accessibility description of what the click does.
 * @param role Accessibility role reported for a clickable row.
 * @param leading Slot before the text block, tinted with the `extra` colors.
 * @param trailing Slot after the text block, tinted with the `extra` colors.
 * @param bottom Content below the main row line, inside the row hit area.
 * @param content Replacement for the whole text block.
 * @throws IllegalArgumentException When neither [title] nor [content] is provided.
 */
@Composable
public fun ListItem(
    modifier: Modifier = Modifier,
    title: (@Composable () -> Unit)? = null,
    subtitle: (@Composable () -> Unit)? = null,
    overline: (@Composable () -> Unit)? = null,
    caption: (@Composable () -> Unit)? = null,
    value: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    selected: Boolean = false,
    highlightSelected: Boolean = true,
    dragging: Boolean = false,
    nestingLevel: Int = 0,
    style: ListItemStyle = LocalListItemStyle.current ?: ListItemStyle.Default,
    size: ListItemSize = LocalListItemSize.current ?: ListItemSize.Default,
    layout: SlotRowLayout = LocalListItemLayout.current,
    interactionSource: MutableInteractionSource? = null,
    onClick: (() -> Unit)? = null,
    onClickLabel: String? = null,
    role: Role? = Role.Button,
    leading: (@Composable BoxScope.() -> Unit)? = null,
    trailing: (@Composable BoxScope.() -> Unit)? = null,
    bottom: (@Composable ColumnScope.() -> Unit)? = null,
    content: (@Composable BoxScope.() -> Unit)? = null,
) {
    val click = onClick
    ListItemLayout(
        modifier = modifier,
        title = title,
        subtitle = subtitle,
        overline = overline,
        caption = caption,
        value = value,
        enabled = enabled,
        selected = selected,
        highlightSelected = highlightSelected,
        dragging = dragging,
        nestingLevel = nestingLevel,
        style = style,
        size = size,
        layout = layout,
        interactionSource = interactionSource,
        action = if (click != null) {
            ListItemAction.Click(onClick = click, onClickLabel = onClickLabel, role = role)
        } else {
            ListItemAction.None
        },
        leading = leading,
        trailing = trailing,
        bottom = bottom,
        content = content,
    )
}

/**
 * How a row answers activation, and so what it reports to accessibility.
 *
 * Internal because each public row names its own vocabulary — `onClick`, `onCheckedChange`,
 * `onSelect`, `onToggle` — and maps it onto one of these.
 */
internal sealed interface ListItemAction {

    /** A row that only displays: not focusable, and without a click action. */
    data object None : ListItemAction

    /**
     * A row that runs an action when pressed.
     *
     * @property onClick Called when the row is activated.
     * @property onClickLabel Accessibility description of what the click does.
     * @property role Accessibility role reported for the row.
     */
    class Click(
        val onClick: () -> Unit,
        val onClickLabel: String?,
        val role: Role?,
    ) : ListItemAction

    /**
     * A row that flips an on/off — or on/off/mixed — state, reported as the row's toggleable state.
     *
     * @property state State reported to accessibility.
     * @property role Accessibility role reported for the row, such as [Role.Checkbox] or
     * [Role.Switch].
     * @property onToggle Called when the row is activated.
     */
    class Toggle(
        val state: ToggleableState,
        val role: Role,
        val onToggle: () -> Unit,
    ) : ListItemAction

    /**
     * A row that is one option of a single choice, reported as selectable.
     *
     * @property selected Whether this option is the chosen one.
     * @property role Accessibility role reported for the row, such as [Role.RadioButton].
     * @property onSelect Called when the row is activated.
     */
    class Select(
        val selected: Boolean,
        val role: Role,
        val onSelect: () -> Unit,
    ) : ListItemAction
}

/**
 * The row behind [ListItem] and every specialised row, parameterised by how it answers activation.
 *
 * @param modifier Modifier applied to the row container.
 * @param title Main copy of the row; ignored when [content] is provided.
 * @param subtitle Supporting copy below [title].
 * @param overline Small copy above [title].
 * @param value Inline value rendered at the end of the row, before [trailing].
 * @param enabled Whether the row accepts input; combined with the enclosing list's state.
 * @param selected Whether the row is one of the selected rows in its list.
 * @param highlightSelected Whether a selected row draws its selected background, rail and copy.
 * @param dragging Whether the row is currently being dragged.
 * @param nestingLevel Indentation steps of [ListItemSize.nestedLevelOffset] to add.
 * @param style Colors of the row across its states.
 * @param size Metrics and typography of the row.
 * @param layout Slot layout of the main row line; its gaps come from [size].
 * @param interactionSource Source of the row interactions, or `null` to create one.
 * @param action How the row answers activation and what it reports to accessibility.
 * @param leading Slot before the text block.
 * @param trailing Slot after the text block.
 * @param bottom Content below the main row line, inside the row hit area.
 * @param content Replacement for the whole text block.
 * @param caption Line of detail below [subtitle], in the subtitle's colors.
 * @throws IllegalArgumentException When neither [title] nor [content] is provided.
 */
@Composable
internal fun ListItemLayout(
    modifier: Modifier,
    title: (@Composable () -> Unit)?,
    subtitle: (@Composable () -> Unit)?,
    overline: (@Composable () -> Unit)?,
    value: (@Composable () -> Unit)?,
    enabled: Boolean,
    selected: Boolean,
    highlightSelected: Boolean,
    dragging: Boolean,
    nestingLevel: Int,
    style: ListItemStyle,
    size: ListItemSize,
    layout: SlotRowLayout,
    interactionSource: MutableInteractionSource?,
    action: ListItemAction,
    leading: (@Composable BoxScope.() -> Unit)?,
    trailing: (@Composable BoxScope.() -> Unit)?,
    bottom: (@Composable ColumnScope.() -> Unit)?,
    content: (@Composable BoxScope.() -> Unit)?,
    caption: (@Composable () -> Unit)? = null,
) {
    require(content != null || title != null) {
        "Either content or title must be provided"
    }

    val layoutDirection = LocalLayoutDirection.current
    // A row in a disabled list is disabled regardless of its own flag.
    val rowEnabled = enabled && LocalListEnabled.current
    val resolvedInteractionSource = interactionSource ?: remember { MutableInteractionSource() }
    val groupClip = LocalListGroupClip.current
    val statesPlacement = remember { PlacedCoordinates() }

    // Only an interactive row reports interactions, so a display row stays visually inert.
    val hovered by resolvedInteractionSource.collectIsHoveredAsState()
    val pressed by resolvedInteractionSource.collectIsPressedAsState()
    val focused by resolvedInteractionSource.collectIsFocusedAsState()

    val state = ListItemState(
        enabled = rowEnabled,
        selected = selected,
        hovered = hovered,
        pressed = pressed,
        focused = focused,
        dragging = dragging,
    )

    // Selection always reaches semantics and the row slots; only its drawing is optional.
    val showSelected = selected && highlightSelected

    val animation = size.stateAnimation
    val selectionSpec = animation.selection.tween<Color>(animation.easing)

    // The background is two layers: what the row is, then what is being done to it. Keeping them
    // apart is what lets a selected row stay selected while the pointer is over it.
    val bgColor by animateColorAsState(
        targetValue = when {
            !rowEnabled -> style.bgDisabled
            dragging -> style.bgDragging
            showSelected && state.isActive -> style.bgSelectedHovered
            showSelected -> style.bgSelected
            else -> style.bg
        },
        animationSpec = selectionSpec,
        label = "listItemBg",
    )
    val interactionColor by animateColorAsState(
        targetValue = when {
            // Fading to a transparent copy of the tint rather than to transparent black keeps the
            // crossfade from drifting through another hue on the way out.
            !rowEnabled -> style.bgHovered.copy(alpha = 0f)
            pressed -> style.bgPressed
            // Keyboard focus wears the hover treatment: one way for a row to say it is the one
            // being reached for, whichever input reached it.
            hovered || focused -> style.bgHovered
            else -> style.bgHovered.copy(alpha = 0f)
        },
        // Press lands fast; everything else settles.
        animationSpec = (if (pressed) animation.pressIn else animation.release)
            .tween(animation.easing),
        label = "listItemInteraction",
    )
    val borderColor by animateColorAsState(
        targetValue = when {
            !rowEnabled -> style.borderDisabled
            showSelected -> style.borderSelected
            else -> style.border
        },
        animationSpec = selectionSpec,
        label = "listItemBorder",
    )
    val selectedRailProgress by animateFloatAsState(
        targetValue = if (showSelected && style.selectedRail != Color.Unspecified) 1f else 0f,
        animationSpec = animation.selection.tween(animation.easing),
        label = "listItemSelectedRail",
    )
    val selectedContentOffset by animateDpAsState(
        targetValue = if (selectedRailProgress > 0f) size.selectedContentOffset else 0.dp,
        animationSpec = animation.selection.tween(animation.easing),
        label = "listItemSelectedOffset",
    )

    val titleColor by animateColorAsState(
        targetValue = when {
            !rowEnabled -> style.titleDisabled
            showSelected -> style.titleSelected
            else -> style.title
        },
        animationSpec = selectionSpec,
        label = "listItemTitle",
    )
    val subtitleColor by animateColorAsState(
        targetValue = when {
            !rowEnabled -> style.subtitleDisabled
            showSelected -> style.subtitleSelected
            else -> style.subtitle
        },
        animationSpec = selectionSpec,
        label = "listItemSubtitle",
    )
    val overlineColor by animateColorAsState(
        targetValue = when {
            !rowEnabled -> style.overlineDisabled
            showSelected -> style.overlineSelected
            else -> style.overline
        },
        animationSpec = selectionSpec,
        label = "listItemOverline",
    )
    val extraColor by animateColorAsState(
        targetValue = when {
            !rowEnabled -> style.extraDisabled
            hovered || pressed -> style.extraActive
            showSelected -> style.extraSelected
            else -> style.extra
        },
        animationSpec = selectionSpec,
        label = "listItemExtra",
    )
    val valueColor by animateColorAsState(
        targetValue = when {
            !rowEnabled -> style.valueDisabled
            hovered || pressed -> style.valueActive
            showSelected -> style.valueSelected
            else -> style.value
        },
        animationSpec = selectionSpec,
        label = "listItemValue",
    )

    val rowModifier = modifier
        .fillMaxWidth()
        .thenIf(groupClip != null) { recordPlacement(statesPlacement) }
        .drawRowStates(
            shape = style.shape,
            groupClip = groupClip,
            placement = statesPlacement,
            bgColor = bgColor,
            interactionColor = interactionColor,
            selectedRailColor = style.selectedRail,
            selectedRailProgress = selectedRailProgress,
            selectedRailWidth = size.selectedRailWidth,
            selectedRailInset = size.selectedRailInset,
            selectedRailRadius = size.selectedRailRadius,
        )
        .thenIf(style.borderWidth > 0.dp && borderColor != Color.Unspecified) {
            border(style.borderWidth, borderColor, style.shape)
        }
        .rowAction(
            action = action,
            enabled = rowEnabled,
            selected = selected,
            indication = style.indication,
            interactionSource = resolvedInteractionSource,
        )
        .padding(
            start = size.contentPadding.calculateStartPadding(layoutDirection) +
                size.nestedLevelOffset * nestingLevel +
                selectedContentOffset,
            top = size.contentPadding.calculateTopPadding(),
            end = size.contentPadding.calculateEndPadding(layoutDirection),
            bottom = size.contentPadding.calculateBottomPadding(),
        )

    CompositionLocalProvider(
        LocalListItemState provides state,
        LocalListItemRowSize provides size,
        LocalIconSize provides size.icon,
        LocalChevronSize provides size.chevron,
    ) {
        Column(modifier = rowModifier) {
            SlotRow(
                modifier = Modifier.defaultMinSize(minHeight = size.minHeight),
                layout = layout.copy(
                    startGap = size.leadingGap,
                    endGap = size.trailingGap,
                ),
                start = leading?.let { slot ->
                    {
                        CompositionLocalProvider(LocalContentColor provides extraColor) {
                            slot()
                        }
                    }
                },
                center = {
                    if (content != null) {
                        CompositionLocalProvider(
                            LocalContentColor provides titleColor,
                            LocalTextStyle provides size.titleTextStyle,
                        ) {
                            content()
                        }
                    } else if (title != null) {
                        ListItemText(
                            title = title,
                            subtitle = subtitle,
                            overline = overline,
                            caption = caption,
                            titleColor = titleColor,
                            subtitleColor = subtitleColor,
                            overlineColor = overlineColor,
                            size = size,
                        )
                    }
                },
                end = trailingSlot(
                    value = value,
                    trailing = trailing,
                    valueColor = valueColor,
                    valueStyle = size.valueTextStyle,
                    extraColor = extraColor,
                    gap = size.valueGap,
                ),
            )

            bottom?.let {
                Spacer(modifier = Modifier.height(size.bottomGap))
                it()
            }
        }
    }
}

/**
 * Makes a row answer [action] and merges its slots into one accessibility node.
 *
 * Toggle and select rows use the foundation `triStateToggleable` and `selectable` modifiers, so
 * their state is reported as structured semantics rather than as text. A click or display row
 * reports `selected` only while it is selected.
 *
 * @param action How the row answers activation.
 * @param enabled Whether the row accepts input.
 * @param selected Whether a click or display row is one of the selected rows.
 * @param indication Indication layered over the row's own state drawing, or `null` for none.
 * @param interactionSource Source the row's hover, press and focus are read from.
 * @return Modifier that handles input and semantics for the row.
 */
private fun Modifier.rowAction(
    action: ListItemAction,
    enabled: Boolean,
    selected: Boolean,
    indication: Indication?,
    interactionSource: MutableInteractionSource,
): Modifier {
    val interactive = when (action) {
        ListItemAction.None -> this

        is ListItemAction.Click -> clickable(
            enabled = enabled,
            onClickLabel = action.onClickLabel,
            role = action.role,
            // States are drawn by the row itself so press, hover and focus share one treatment.
            indication = indication,
            interactionSource = interactionSource,
            onClick = action.onClick,
        ).clickCursor(enabled = enabled)

        is ListItemAction.Toggle -> triStateToggleable(
            state = action.state,
            interactionSource = interactionSource,
            indication = indication,
            enabled = enabled,
            role = action.role,
            onClick = action.onToggle,
        ).clickCursor(enabled = enabled)

        is ListItemAction.Select -> selectable(
            selected = action.selected,
            interactionSource = interactionSource,
            indication = indication,
            enabled = enabled,
            role = action.role,
            onClick = action.onSelect,
        ).clickCursor(enabled = enabled)
    }

    // Toggle and select rows already report their own state; repeating it as `selected` would
    // announce a checked checkbox row as "selected" too.
    val reportsSelected = selected &&
        (action is ListItemAction.None || action is ListItemAction.Click)
    return interactive.semantics(mergeDescendants = true) {
        if (reportsSelected) this.selected = true
    }
}

/**
 * Builds the end slot of a row out of its optional inline value and trailing content.
 *
 * @param value Inline value rendered first.
 * @param trailing Trailing content rendered after [value].
 * @param valueColor Current color of the inline value.
 * @param valueStyle Text style of the inline value.
 * @param extraColor Current color of the trailing content.
 * @param gap Spacing between the inline value and the trailing content.
 * @return End slot content, or `null` when the row has neither a value nor trailing content.
 */
private fun trailingSlot(
    value: (@Composable () -> Unit)?,
    trailing: (@Composable BoxScope.() -> Unit)?,
    valueColor: Color,
    valueStyle: TextStyle,
    extraColor: Color,
    gap: Dp,
): (@Composable BoxScope.() -> Unit)? {
    if (value == null && trailing == null) return null

    return {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(gap),
        ) {
            value?.let {
                CompositionLocalProvider(
                    LocalContentColor provides valueColor,
                    LocalTextStyle provides valueStyle,
                ) {
                    it()
                }
            }

            trailing?.let {
                CompositionLocalProvider(LocalContentColor provides extraColor) {
                    Box(contentAlignment = Alignment.Center, content = it)
                }
            }
        }
    }
}

/**
 * Overline, title, subtitle and caption block used by [ListItem] when it has no replacement content.
 *
 * @param title Main copy.
 * @param subtitle Supporting copy below [title].
 * @param overline Small copy above [title].
 * @param caption Line of detail below [subtitle], drawn in [subtitleColor].
 * @param titleColor Current color of [title].
 * @param subtitleColor Current color of [subtitle].
 * @param overlineColor Current color of [overline].
 * @param size Metrics and typography the block draws its text styles and spacing from.
 */
@Composable
private fun ListItemText(
    title: @Composable () -> Unit,
    subtitle: (@Composable () -> Unit)?,
    overline: (@Composable () -> Unit)?,
    caption: (@Composable () -> Unit)?,
    titleColor: Color,
    subtitleColor: Color,
    overlineColor: Color,
    size: ListItemSize,
) {
    Column(verticalArrangement = Arrangement.spacedBy(size.textGap)) {
        overline?.let {
            CompositionLocalProvider(
                LocalContentColor provides overlineColor,
                LocalTextStyle provides size.overlineTextStyle,
            ) {
                it()
            }
        }

        CompositionLocalProvider(
            LocalContentColor provides titleColor,
            LocalTextStyle provides size.titleTextStyle,
        ) {
            // The title's first line is what the row measures its text in and aligns against.
            Box(modifier = Modifier.firstLineCenter(size.titleTextStyle)) { title() }
        }

        subtitle?.let {
            CompositionLocalProvider(
                LocalContentColor provides subtitleColor,
                LocalTextStyle provides size.subtitleTextStyle,
            ) {
                it()
            }
        }

        // Smaller than the subtitle rather than paler: two muted inks a step apart read as one
        // line that faded, while a size step reads as a line of lesser detail.
        caption?.let {
            CompositionLocalProvider(
                LocalContentColor provides subtitleColor,
                LocalTextStyle provides size.captionTextStyle,
            ) {
                it()
            }
        }
    }
}

/**
 * Draws the background layers and the selected rail of a row behind its content, inside [shape].
 *
 * The decorations are clipped to [shape] while they draw rather than by clipping the row itself,
 * so an indication layered over the row — a focus ring drawn outside its bounds — is not cropped.
 * A row stacked inside a group — a [ListItemColumn] or a settings section — uses a square shape and
 * also clips its decorations to [groupClip], so the first and last row follow the group's corners
 * while their focus rings stay whole.
 *
 * @param shape Shape the decorations are drawn inside.
 * @param groupClip Surface of the group the row sits in, or `null` outside one.
 * @param placement Where the decorations are placed; recorded by the row while [groupClip] is set.
 * @param bgColor Current row background, before any interaction tint.
 * @param interactionColor Hover or press tint laid over [bgColor].
 * @param selectedRailColor Color of the selected rail; [Color.Unspecified] draws none.
 * @param selectedRailProgress Extension of the selected rail from its center, from `0f` to `1f`.
 * @param selectedRailWidth Width of the selected rail; `0.dp` hides it.
 * @param selectedRailInset Vertical inset of the selected rail at full extension.
 * @param selectedRailRadius Corner radius of the selected rail ends.
 * @return Modifier that draws the row state decorations.
 */
private fun Modifier.drawRowStates(
    shape: Shape,
    groupClip: ListGroupClip?,
    placement: PlacedCoordinates,
    bgColor: Color,
    interactionColor: Color,
    selectedRailColor: Color,
    selectedRailProgress: Float,
    selectedRailWidth: Dp,
    selectedRailInset: Dp,
    selectedRailRadius: Dp,
): Modifier = drawWithCache {
    val outline = Path()
    buildShapePath(
        shape = shape,
        size = size,
        layoutDirection = layoutDirection,
        density = this,
        offsetPx = 0f,
        target = outline,
    )
    val groupOutline = Path()
    onDrawBehind {
        val decorations: DrawScope.() -> Unit = {
            clipPath(outline) {
                drawRowDecorations(
                    bgColor = bgColor,
                    interactionColor = interactionColor,
                    selectedRailColor = selectedRailColor,
                    selectedRailProgress = selectedRailProgress,
                    selectedRailWidth = selectedRailWidth,
                    selectedRailInset = selectedRailInset,
                    selectedRailRadius = selectedRailRadius,
                )
            }
        }
        val inGroup = groupClip?.outlineIn(
            row = placement.coordinates,
            layoutDirection = layoutDirection,
            density = this,
            target = groupOutline,
        ) == true
        if (inGroup) clipPath(groupOutline, block = decorations) else decorations()
    }
}

/**
 * Fills a row's background and interaction tint and draws its selected rail, without clipping.
 *
 * @param bgColor Current row background, before any interaction tint.
 * @param interactionColor Hover or press tint laid over [bgColor].
 * @param selectedRailColor Color of the selected rail; [Color.Unspecified] draws none.
 * @param selectedRailProgress Extension of the selected rail from its center, from `0f` to `1f`.
 * @param selectedRailWidth Width of the selected rail; `0.dp` hides it.
 * @param selectedRailInset Vertical inset of the selected rail at full extension.
 * @param selectedRailRadius Corner radius of the selected rail ends.
 */
private fun DrawScope.drawRowDecorations(
    bgColor: Color,
    interactionColor: Color,
    selectedRailColor: Color,
    selectedRailProgress: Float,
    selectedRailWidth: Dp,
    selectedRailInset: Dp,
    selectedRailRadius: Dp,
) {
    if (bgColor.alpha > 0.001f) {
        drawRect(color = bgColor)
    }

    if (interactionColor.alpha > 0.001f) {
        drawRect(color = interactionColor)
    }

    if (
        selectedRailProgress > 0.001f &&
        selectedRailWidth > 0.dp &&
        selectedRailColor != Color.Unspecified
    ) {
        val railWidth = selectedRailWidth.toPx()
        // The rail grows out of the row's vertical center, so selection reads as an arrival.
        val fullHeight = size.height - selectedRailInset.toPx() * 2f
        val railHeight = (fullHeight * selectedRailProgress).coerceAtLeast(0f)
        val left = if (layoutDirection == LayoutDirection.Ltr) 0f else size.width - railWidth
        drawRoundRect(
            color = selectedRailColor,
            topLeft = Offset(left, (size.height - railHeight) / 2f),
            size = Size(railWidth, railHeight),
            cornerRadius = CornerRadius(selectedRailRadius.toPx()),
        )
    }
}
