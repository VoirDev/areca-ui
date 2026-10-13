package dev.voir.areca.foundation.field

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.LayoutScopeMarker
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.isSpecified
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.foundation.LocalTextStyle
import dev.voir.areca.foundation.button.LocalIconButtonSize
import dev.voir.areca.foundation.button.LocalIconButtonStyle
import dev.voir.areca.foundation.button.focusRing
import dev.voir.areca.foundation.feedback.LocalSpinnerSize
import dev.voir.areca.foundation.feedback.Spinner
import dev.voir.areca.foundation.feedback.SpinnerStyle
import dev.voir.areca.foundation.icon.LocalIconSize
import dev.voir.areca.modifier.clickCursor
import dev.voir.areca.modifier.thenIf

/**
 * One part of a [FieldRow]: a slot with its own padding, its own click, and its own decoration,
 * inside a container it does not own.
 *
 * The scope is what makes a composite field one field rather than several stuck together. It
 * carries the palette, the metrics and the one [FieldVisualState] the row resolved, so every
 * segment reports the same focus and the same error, and a caller building a segment type of its
 * own has the same material the stock ones do.
 *
 * @property style Palette the row resolved its colors from.
 * @property size Metrics the row is drawn at.
 * @property visualState The one state every segment is drawn in.
 * @property enabled Whether the row as a whole accepts input. A segment can be disabled on its
 *   own; it cannot be enabled while the row is not.
 * @property readOnly Whether the row's value can be changed.
 * @property contentPadding Inset a segment takes when it does not name its own: the row's
 *   [FieldSize.padding], with the top inset narrowed to [FieldLabelMetrics.insideGap] while an
 *   internal label sits above the segments, so the two paddings do not stack.
 *
 * Sealed: only [FieldRow] provides this scope. Build a segment type of your own as an extension on
 * it, the way [TextSegment] and [dev.voir.areca.foundation.field.select.SelectSegment] are.
 */
@LayoutScopeMarker
@Stable
public sealed interface FieldRowScope {
    /** Palette the row resolved its colors from. */
    public val style: FieldStyle

    /** Metrics the row is drawn at. */
    public val size: FieldSize

    /** The one state every segment is drawn in. */
    public val visualState: FieldVisualState

    /** Whether the row as a whole accepts input. */
    public val enabled: Boolean

    /** Whether the row's value can be changed. */
    public val readOnly: Boolean

    /** Inset a segment takes when it does not name its own. */
    public val contentPadding: PaddingValues

    /**
     * A part of the row, holding whatever a field normally holds.
     *
     * Give exactly one segment the width — with [weight] — and let the rest wrap, or weight
     * several to split the row in fixed proportions. A segment with no weight is as wide as its
     * content, which is what a currency selector or a unit picker wants.
     *
     * [onClick] makes this segment alone the trigger: the date half of a date-and-time field opens
     * a calendar while the time half opens a wheel, and neither steals the other's tap. A segment
     * holding an editor leaves it null and lets the editor take its own clicks.
     *
     * @param modifier Modifier applied to the segment.
     * @param weight Share of the leftover width, or 0 to size to the content.
     * @param enabled Whether this segment accepts input. Ignored while the row is disabled.
     * @param onClick Makes the segment a trigger, which answers a press with a tint across the
     *   segment as a clickable [Field] does across its container. Null leaves it inert.
     * @param padding Inset between the segment's edges and its content. Defaults to
     *   [contentPadding], which is what keeps the outer edges of a composite aligned with a
     *   single-part field and leaves no gap under an internal label.
     * @param loading Replaces the trailing slot with a spinner. Does not disable the segment.
     * @param empty Whether this segment has no value, and so should show its [placeholder].
     * @param leading Slot pinned to the segment's start edge, in the style's decoration color.
     * @param trailing Slot pinned to the segment's end edge. Hidden while [loading].
     * @param placeholder Hint drawn over the value while [empty].
     * @param content The segment's value.
     */
    @Composable
    public fun Segment(
        modifier: Modifier = Modifier,
        weight: Float = 0f,
        enabled: Boolean = true,
        onClick: (() -> Unit)? = null,
        padding: PaddingValues = contentPadding,
        loading: Boolean = false,
        empty: Boolean = false,
        leading: (@Composable RowScope.() -> Unit)? = null,
        trailing: (@Composable RowScope.() -> Unit)? = null,
        placeholder: (@Composable RowScope.() -> Unit)? = null,
        content: @Composable RowScope.() -> Unit,
    )

    /**
     * The rule between two segments, drawn edge to edge.
     *
     * Emitted rather than inserted automatically: a row that wants no rule between two halves of
     * one value — a range, an amount and its currency — should be able to say so by leaving
     * this out, and a DSL that guesses cannot be overruled.
     *
     * @param modifier Modifier applied to the rule.
     * @param color Rule color. Defaults to the style's.
     * @param width Rule thickness. Defaults to the style's border thickness, so a divider and a
     *   border read as the same line.
     */
    @Composable
    public fun SegmentDivider(
        modifier: Modifier = Modifier,
        color: Color = style.divider,
        width: Dp = size.borderWidth,
    )
}

/**
 * Several controls sharing one field: one border, one background, one focus, one status.
 *
 * The container is a [Field] in everything but its middle — same palette, same metrics, same
 * resolution of status against focus — so a composite sits in a form beside single-part fields
 * without giving itself away. What changes is that the inside is divided: each
 * [FieldRowScope.Segment] pads and clicks for itself, and [FieldRowScope.SegmentDivider] draws
 * the rule between them.
 *
 * ```kotlin
 * FieldRow {
 *     TextSegment(
 *         value = amount,
 *         onValueChange = { amount = it },
 *         weight = 1f,
 *         placeholder = { Text("0.00") },
 *     )
 *     SegmentDivider()
 *     Segment(onClick = { currencyMenu.show() }, trailing = { Chevron(ChevronDirection.Down) }) {
 *         Text(currency)
 *     }
 * }
 * ```
 *
 * Focus is the row's, not any segment's: the container watches whether focus is anywhere inside it,
 * so tabbing from the amount into the currency button leaves the border lit rather than flickering
 * between two fields. Status works the same way — one error colors the whole row, because a
 * composite holds one value however many parts it is entered in.
 *
 * The row measures itself at [IntrinsicSize.Min] so a divider can run its full height. Segment
 * content therefore has to answer an intrinsic measurement, which rules out a bare
 * [androidx.compose.ui.layout.SubcomposeLayout] inside one — put such a thing in its own field
 * rather than in a segment.
 *
 * The row needs a bounded width, as [Field] does: segments divide the width they are given, and
 * there is nothing to divide inside a horizontally scrolling parent.
 *
 * A [FieldLabelPlacement.Inside] label is drawn above the segments and spans the whole row, since
 * it names the value rather than any one part of it. [FieldLabelPlacement.InsideLeading] is drawn
 * the same way, for the same reason: a composite has no single line to share with a label.
 *
 * @param modifier Modifier applied to the container.
 * @param style Color palette and shape. Defaults to the ambient [LocalFieldStyle].
 * @param size Typography, paddings and vertical metrics. Defaults to the ambient [LocalFieldSize].
 * @param field Scope handed down by an enclosing [FieldGroup]: the status to report, and a label
 *   to draw inside the container when the group placed it there.
 * @param status Validation outcome to report, for the whole row. Comes from [field] unless it is
 *   given explicitly.
 * @param enabled When false, every segment is non-interactive and the row is drawn as disabled.
 *   Comes from [field] unless it is given explicitly.
 * @param readOnly The value can be read but not changed. Segments still take focus; their clicks
 *   do not fire.
 * @param interactionSource Interaction source the container watches for hover. Focus is watched
 *   directly, because the thing that holds it is a segment rather than the row. Null keeps one
 *   private to the row.
 * @param content The segments.
 */
@Composable
public fun FieldRow(
    modifier: Modifier = Modifier,
    style: FieldStyle = FieldDefaults.style,
    size: FieldSize = FieldDefaults.size,
    field: FieldScope? = null,
    status: FieldStatus = field?.status ?: FieldStatus.Default,
    enabled: Boolean = field?.enabled ?: true,
    readOnly: Boolean = false,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable FieldRowScope.() -> Unit,
) {
    val interactions = interactionSource ?: remember { MutableInteractionSource() }
    // Focus belongs to whichever segment holds it, so the row asks the focus system rather than an
    // interaction source: `hasFocus` is true for the whole subtree, which is what stops the border
    // from blinking as focus moves from one segment to the next.
    var focusedWithin by remember { mutableStateOf(false) }
    val hovered by interactions.collectIsHoveredAsState()

    val visualState = fieldVisualState(status, enabled, readOnly, focusedWithin, hovered)

    val colorSpec = fieldColorSpec()
    val backgroundColor by animateColorAsState(
        targetValue = resolveBackgroundColor(style, visualState),
        animationSpec = colorSpec,
        label = "fieldRowBg",
    )
    val borderColor by animateColorAsState(
        targetValue = resolveBorderColor(style, visualState),
        animationSpec = colorSpec,
        label = "fieldRowBorder",
    )
    val contentColor by animateColorAsState(
        targetValue = resolveContentColor(style, visualState),
        animationSpec = colorSpec,
        label = "fieldRowContent",
    )
    val placeholderColor by animateColorAsState(
        targetValue = resolvePlaceholderColor(style, visualState),
        animationSpec = colorSpec,
        label = "fieldRowPlaceholder",
    )
    val focusRingColor by animateColorAsState(
        targetValue = resolveFocusRingColor(style, visualState, focusedWithin),
        animationSpec = colorSpec,
        label = "fieldRowFocusRing",
    )
    val labelColor by animateColorAsState(
        targetValue = style.label.colorFor(visualState),
        animationSpec = colorSpec,
        label = "fieldRowLabel",
    )

    val internalLabel = field.internalLabel()

    val layoutDirection = LocalLayoutDirection.current
    val startPadding = size.padding.calculateStartPadding(layoutDirection)
    val endPadding = size.padding.calculateEndPadding(layoutDirection)
    val topPadding = size.padding.calculateTopPadding()
    val bottomPadding = size.padding.calculateBottomPadding()

    // With a label above them the segments give up their own top inset, or the two paddings stack
    // and the row grows a band of empty space between the label and the values.
    val segmentPadding = if (internalLabel != null) {
        PaddingValues(
            start = startPadding,
            end = endPadding,
            top = size.label.insideGap,
            bottom = bottomPadding,
        )
    } else {
        size.padding
    }

    CompositionLocalProvider(
        LocalContentColor provides contentColor,
        LocalTextStyle provides size.textStyle,
        LocalIconSize provides size.icon,
        LocalSpinnerSize provides size.spinner,
        LocalIconButtonSize provides size.iconButton,
        LocalIconButtonStyle provides style.iconButton,
    ) {
        Column(
            modifier = modifier
                .heightIn(min = size.minHeight)
                // The requester lands on the group rather than on a control, so a tap on the
                // group's label moves focus to whichever segment can take it first.
                .focusGroup()
                .fieldControl(field)
                .onFocusChanged { focusedWithin = it.hasFocus }
                .fieldContainer(
                    style = style,
                    metrics = size,
                    bg = backgroundColor,
                    border = borderColor,
                    focusRing = focusRingColor,
                )
                .hoverable(interactions, enabled = enabled),
        ) {
            if (internalLabel != null) {
                FieldGroupDefaults.Label(
                    text = internalLabel.first.text,
                    modifier = Modifier.padding(
                        start = startPadding,
                        end = endPadding,
                        top = topPadding,
                    ),
                    required = internalLabel.first.required,
                    requiredText = internalLabel.first.requiredText,
                    color = labelColor,
                    requiredColor = style.label.required,
                    textStyle = size.label.insideTextStyle,
                    requiredGap = size.label.requiredGap,
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    // What lets a divider fill the row: every segment is measured against the
                    // tallest one rather than against its own content.
                    .height(IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val scope = FieldRowScopeImpl(
                    rowScope = this,
                    style = style,
                    size = size,
                    visualState = visualState,
                    enabled = enabled,
                    readOnly = readOnly,
                    placeholderColor = placeholderColor,
                    contentPadding = segmentPadding,
                )
                scope.content()
            }
        }
    }
}

/**
 * The row's implementation of [FieldRowScope], holding what a segment needs and cannot see.
 *
 * @property rowScope The enclosing [Row], so a segment can claim a weight in it.
 * @property style Palette the row resolved its colors from.
 * @property size Metrics the row is drawn at.
 * @property visualState The one state every segment is drawn in.
 * @property enabled Whether the row accepts input.
 * @property readOnly Whether the row's value can be changed.
 * @property placeholderColor Resolved placeholder foreground, shared by every segment.
 * @property contentPadding Inset a segment takes when it does not name its own.
 */
@Stable
private class FieldRowScopeImpl(
    private val rowScope: RowScope,
    override val style: FieldStyle,
    override val size: FieldSize,
    override val visualState: FieldVisualState,
    override val enabled: Boolean,
    override val readOnly: Boolean,
    private val placeholderColor: Color,
    override val contentPadding: PaddingValues,
) : FieldRowScope {

    @Composable
    override fun Segment(
        modifier: Modifier,
        weight: Float,
        enabled: Boolean,
        onClick: (() -> Unit)?,
        padding: PaddingValues,
        loading: Boolean,
        empty: Boolean,
        leading: (@Composable RowScope.() -> Unit)?,
        trailing: (@Composable RowScope.() -> Unit)?,
        placeholder: (@Composable RowScope.() -> Unit)?,
        content: @Composable RowScope.() -> Unit,
    ) {
        val active = this.enabled && enabled && !readOnly
        val weighted = weight > 0f
        // `Row` and `FieldRowScope` are both DSL-marked, so inside the row the scope's own members
        // are out of reach by an implicit receiver. Bind them here rather than qualifying each use.
        val rowStyle = style
        val rowSize = size
        val hintColor = placeholderColor
        // Rectangular: the row's container clip rounds the outer segments' corners, and an inner
        // segment's edges are the dividers.
        val pressIndication = remember(rowStyle.content) {
            fieldPressIndication(shape = RectangleShape, ink = rowStyle.content)
        }

        Row(
            modifier = with(rowScope) {
                modifier.thenIf(weighted) { weight(weight) }
            }
                // Fills the height the row was measured at, so the whole segment is the tap target
                // rather than just the strip its text happens to occupy.
                .fillMaxHeight()
                .heightIn(min = rowSize.minHeight)
                .then(
                    if (onClick != null) {
                        Modifier
                            .clickable(
                                enabled = active,
                                onClick = onClick,
                                interactionSource = null,
                                indication = pressIndication,
                            )
                            .clickCursor(active)
                    } else {
                        Modifier
                    },
                )
                .padding(padding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (leading != null) {
                CompositionLocalProvider(LocalContentColor provides rowStyle.extra) {
                    leading()
                }
                Spacer(Modifier.width(rowSize.gap))
            }

            FieldRowValue(
                // Weighted either way, so the row measures the leading and trailing slots first
                // and gives the value what is left. Without this the value is measured against the
                // whole segment and a trailing chevron is squeezed to nothing by a long value.
                // `fill` is what separates the two cases: a weighted segment's value takes its
                // whole share, an unweighted one's stops at its content so the segment wraps.
                modifier = Modifier.weight(1f, fill = weighted),
                empty = empty,
                placeholderColor = hintColor,
                placeholder = placeholder,
                content = content,
            )

            when {
                loading -> {
                    Spacer(Modifier.width(rowSize.gap))
                    Spinner(style = SpinnerStyle(color = rowStyle.spinner))
                }

                trailing != null -> {
                    Spacer(Modifier.width(rowSize.gap))
                    CompositionLocalProvider(LocalContentColor provides rowStyle.extra) {
                        trailing()
                    }
                }
            }
        }
    }

    @Composable
    override fun SegmentDivider(
        modifier: Modifier,
        color: Color,
        width: Dp,
    ) {
        Box(
            modifier = modifier
                .fillMaxHeight()
                .width(width)
                .background(color),
        )
    }
}

/**
 * A segment's value, with the placeholder layered behind it.
 *
 * The same arrangement [Field] uses for its single value, and for the same reason: the value is
 * always composed, so an editor that is empty keeps the caret the user is about to type into.
 *
 * @param modifier Modifier applied to the slot, carrying the weight where one applies.
 * @param empty Whether the segment has no value, and so should show its placeholder.
 * @param placeholderColor Resolved placeholder foreground.
 * @param placeholder Hint drawn over the value while [empty], or null for a segment with none.
 * @param content The value.
 */
@Composable
private fun FieldRowValue(
    modifier: Modifier,
    empty: Boolean,
    placeholderColor: Color,
    placeholder: (@Composable RowScope.() -> Unit)?,
    content: @Composable RowScope.() -> Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterStart,
    ) {
        if (placeholder != null) {
            val placeholderAlpha by animateFloatAsState(
                targetValue = if (empty) 1f else 0f,
                animationSpec = fieldFadeSpec(),
                label = "fieldRowPlaceholderAlpha",
            )

            if (placeholderAlpha > 0f) {
                CompositionLocalProvider(LocalContentColor provides placeholderColor) {
                    Row(
                        modifier = Modifier.alpha(placeholderAlpha),
                        verticalAlignment = Alignment.CenterVertically,
                        content = placeholder,
                    )
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            content = content,
        )
    }
}

/**
 * Width for a segment that should be exactly as wide as a fixed label — a currency code, a unit,
 * a country dialling code — rather than as wide as whichever value happens to be in it.
 *
 * Without it a segment showing `USD` and one showing `€` are different widths, and a column of
 * composite fields comes out ragged.
 *
 * @param width Fixed segment width, or [Dp.Unspecified] to size to the content.
 * @return The modifier, unchanged when no width was named.
 */
public fun Modifier.segmentWidth(width: Dp): Modifier =
    if (width.isSpecified) this.width(width) else this
