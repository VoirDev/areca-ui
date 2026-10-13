package dev.voir.areca.foundation.text

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.snap
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.CacheDrawScope
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.voir.areca.foundation.button.Button
import dev.voir.areca.foundation.feedback.SkeletonStyle
import dev.voir.areca.indication.TouchIndication
import dev.voir.areca.modifier.clickCursor
import dev.voir.areca.modifier.thenIf
import dev.voir.areca.platform.isBlurSupported
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.millis
import dev.voir.areca.theme.tween
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import androidx.compose.animation.core.tween as composeTween
import androidx.compose.runtime.ProvidableCompositionLocal

/**
 * Colors of a [SensitiveText]'s veil.
 *
 * Two fills rather than one, because the veil carries the whole of the concealment on a platform
 * that cannot blur. [veil] sits over blurred glyphs and may be translucent — that is what makes it
 * read as frosted glass. [opaqueVeil] stands alone, so it must hide on its own.
 *
 * @property veil Fill drawn over the blurred text. Translucent by design; the blur beneath is what
 *   the reader sees through it.
 * @property opaqueVeil Fill used where the platform cannot blur. Must be fully opaque — a
 *   translucent color here would leave the value legible through it.
 * @property shimmer Color the band travelling across a concealed veil lightens it towards. Read
 *   against the canvas rather than as "lighter", the way [SkeletonStyle] is, so it stays coherent
 *   when the theme flips.
 * @property sparkle Color of the sparks thrown off by a reveal.
 */
@Immutable
public data class SensitiveTextStyle(
    public val veil: Color,
    public val opaqueVeil: Color,
    public val shimmer: Color,
    public val sparkle: Color,
) {
    /** The theme's default style, and a builder for overriding it. */
    public companion object {
        /** Theme default: a muted pane over the text, sparking in the accent color. */
        public val Default: SensitiveTextStyle
            @Composable
            get() = default()

        /**
         * Builds a style from the current theme.
         *
         * @param veil Fill drawn over the blurred text.
         * @param opaqueVeil Opaque fill used where the platform cannot blur.
         * @param shimmer Color the travelling band lightens the veil towards.
         * @param sparkle Color of the sparks thrown off by a reveal.
         * @return A style with the given colors and the theme's by default.
         */
        @Composable
        public fun default(
            veil: Color = ArecaTheme.colors.bgMuted.copy(alpha = FrostedVeilAlpha),
            opaqueVeil: Color = ArecaTheme.colors.bgMuted,
            shimmer: Color = ArecaTheme.colors.bgSubtle,
            sparkle: Color = ArecaTheme.colors.accentBase,
        ): SensitiveTextStyle = SensitiveTextStyle(
            veil = veil,
            opaqueVeil = opaqueVeil,
            shimmer = shimmer,
            sparkle = sparkle,
        )
    }
}

/**
 * Shape, spread, and timing of a [SensitiveText]'s veil.
 *
 * The two paddings are how far the veil reaches past the glyphs it covers. They are small on
 * purpose: a veil that follows each line's own width is what makes a concealed paragraph still look
 * like text, and generous padding would flatten every line into one block.
 *
 * @property shape Corner shape of the veil over each line.
 * @property horizontalPadding How far the veil extends past the start and end of each line.
 * @property verticalPadding How far the veil extends above and below the glyphs of each line.
 * @property blurRadius Smallest blur applied to the glyphs under the veil, where the platform
 *   supports it. Larger type is blurred proportionally more, because how far a glyph has to be
 *   smeared before it stops being readable follows its size.
 * @property sparkRadius Radius of a spark at its brightest.
 * @property revealDuration Time the veil takes to clear, and to come back.
 * @property shimmerPeriod Time the shimmer takes to cross a concealed text once. Slow on purpose:
 *   the shimmer is there to say the value can be revealed, not to demand attention.
 */
@Immutable
public data class SensitiveTextSize(
    public val shape: Shape,
    public val horizontalPadding: Dp,
    public val verticalPadding: Dp,
    public val blurRadius: Dp,
    public val sparkRadius: Dp,
    public val revealDuration: Duration,
    public val shimmerPeriod: Duration,
) {
    /** The theme's default size, and a builder for overriding it. */
    public companion object {
        /** Theme default. */
        public val Default: SensitiveTextSize
            @Composable
            get() = default()

        /**
         * Builds a size from the current theme.
         *
         * @param shape Corner shape of the veil over each line.
         * @param horizontalPadding How far the veil extends past each end of a line.
         * @param verticalPadding How far the veil extends above and below a line's glyphs.
         * @param blurRadius Smallest blur applied to the glyphs under the veil.
         * @param sparkRadius Radius of a spark at its brightest.
         * @param revealDuration Time the veil takes to clear, and to come back.
         * @param shimmerPeriod Time the shimmer takes to cross a concealed text once.
         * @return A size with the given metrics and the theme's by default.
         */
        @Composable
        public fun default(
            shape: Shape = ArecaTheme.shapes.xs,
            horizontalPadding: Dp = ArecaTheme.spacing.xs,
            verticalPadding: Dp = ArecaTheme.spacing.xs / 2f,
            blurRadius: Dp = 6.dp,
            sparkRadius: Dp = 4.dp,
            revealDuration: Duration = ArecaTheme.motion.slow,
            shimmerPeriod: Duration = 2_600.milliseconds,
        ): SensitiveTextSize = SensitiveTextSize(
            shape = shape,
            horizontalPadding = horizontalPadding,
            verticalPadding = verticalPadding,
            blurRadius = blurRadius,
            sparkRadius = sparkRadius,
            revealDuration = revealDuration,
            shimmerPeriod = shimmerPeriod,
        )
    }
}

/** Default [SensitiveTextSize] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalSensitiveTextSize: ProvidableCompositionLocal<SensitiveTextSize?> = compositionLocalOf { null }

/** Default [SensitiveTextStyle] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalSensitiveTextStyle: ProvidableCompositionLocal<SensitiveTextStyle?> = compositionLocalOf { null }

/**
 * Text whose value is hidden under a veil until it is revealed.
 *
 * For anything a reader should be able to *have* on screen without *showing* it: a balance, a card
 * number, an API key, a diagnosis. Unlike a masked field, nothing is substituted — the real text is
 * laid out at its real size, wrapped over its real number of lines, and then covered. That is the
 * point of the component: revealing changes what is drawn and never what is measured, so a concealed
 * value cannot make the screen jump when it is shown.
 *
 * Where the platform can blur, the veil is a frosted pane over glyphs blurred past reading. Where it
 * cannot — Android before 12 — the glyphs are not drawn at all and the veil is opaque, which looks
 * flatter and hides exactly as well. Concealment never rests on an effect that might be skipped.
 *
 * This is a control over what is *on the screen* — it answers the person reading over a shoulder,
 * not one holding the device. The value is still in the composition, in a screenshot, and in the
 * clipboard if something copies it.
 *
 * State is the caller's. Pass [onRevealedChange] to make the text itself the control:
 *
 * ```kotlin
 * var revealed by remember { mutableStateOf(false) }
 *
 * SensitiveText(
 *     text = account.iban,
 *     variant = TextVariant.BodyMedium,
 *     revealed = revealed,
 *     onRevealedChange = { revealed = it },
 * )
 * ```
 *
 * Leave it `null` where something else does the revealing — an eye button beside the row, or a
 * screen-wide "show balances" toggle — and drive [revealed] from there.
 *
 * While concealed the value is kept from assistive technology too, and [concealedDescription] is
 * announced in its place; a reader using one reveals the text the same way anyone else does, and
 * hears it once it is shown.
 *
 * @param text Value to conceal. Laid out in full whether or not it is currently shown.
 * @param modifier Modifier applied to the container.
 * @param revealed Whether the value is currently shown.
 * @param onRevealedChange Called with the requested state when the text is tapped. `null` makes the
 *   text inert, for a value revealed by a control elsewhere.
 * @param variant Role the text plays, which decides its style.
 * @param color Named foreground color of the revealed text.
 * @param fontWeight Weight override.
 * @param textAlign Alignment within the node's width.
 * @param overflow How text that does not fit is handled.
 * @param softWrap Whether text wraps onto further lines.
 * @param maxLines Largest number of lines to render.
 * @param minLines Smallest number of lines to reserve space for.
 * @param animated Whether the veil shimmers, sparks, and crossfades. Pass `false` for a screenshot
 *   test, or when the user has asked for reduced motion — the reveal then simply switches.
 * @param concealedDescription What assistive technology announces in place of the hidden value.
 * @param style Veil and sparkle colors. Defaults to the theme's.
 * @param size Shape, spread, and timing. Defaults to the theme's.
 */
@Composable
public fun SensitiveText(
    text: String,
    modifier: Modifier = Modifier,
    revealed: Boolean = false,
    onRevealedChange: ((Boolean) -> Unit)? = null,
    variant: TextVariant = TextVariant.BodyMedium,
    color: TextColor = TextColor.Base,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign = TextAlign.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    animated: Boolean = true,
    concealedDescription: String = ConcealedDescription,
    style: SensitiveTextStyle = LocalSensitiveTextStyle.current ?: SensitiveTextStyle.Default,
    size: SensitiveTextSize = LocalSensitiveTextSize.current ?: SensitiveTextSize.Default,
) {
    // Held as State rather than read through a delegate: every frame of the reveal writes to it, and
    // reading it in composition would recompose the text that often instead of only redrawing it.
    val layout = remember { mutableStateOf<TextLayoutResult?>(null) }
    val reveal = animateFloatAsState(
        targetValue = if (revealed) 1f else 0f,
        animationSpec = if (animated) {
            size.revealDuration.tween(ArecaTheme.motion.fade)
        } else {
            snap()
        },
        label = "sensitiveTextReveal",
    )
    // Only while something is actually hidden: an infinite transition keeps the frame clock running
    // for as long as it exists.
    val shimmering = animated && !revealed
    val shimmer = rememberShimmer(shimmering = shimmering, period = size.shimmerPeriod)
    val interactionSource = remember { MutableInteractionSource() }
    // The press tint an indication would draw sits behind the content, which means behind the veil.
    // Brightening the veil itself is the same feedback, drawn where it can actually be seen.
    val pressed by interactionSource.collectIsPressedAsState()
    val glow = animateFloatAsState(
        targetValue = if (pressed) 1f else 0f,
        animationSpec = ArecaTheme.motion.instant.tween(ArecaTheme.motion.fade),
        label = "sensitiveTextPressGlow",
    )

    Box(
        modifier = modifier
            .thenIf(onRevealedChange != null) {
                clickable(
                    interactionSource = interactionSource,
                    indication = TouchIndication(shape = size.shape),
                    role = Role.Button,
                    onClick = { onRevealedChange?.invoke(!revealed) },
                ).clickCursor(true)
            }
            .thenIf(!revealed) {
                semantics { contentDescription = concealedDescription }
            }
    ) {
        Text(
            text = text,
            variant = variant,
            modifier = Modifier
                .veil(
                    layout = layout,
                    reveal = reveal,
                    shimmer = shimmer,
                    glow = glow,
                    shimmering = shimmering,
                    style = style,
                    metrics = size,
                )
                // The value is hidden from a screen reader for as long as it is hidden on screen.
                // The container's description stands in for it, and reveals it the same way.
                .thenIf(!revealed) { clearAndSetSemantics { } },
            color = color,
            fontWeight = fontWeight,
            textAlign = textAlign,
            overflow = overflow,
            softWrap = softWrap,
            maxLines = maxLines,
            minLines = minLines,
            onTextLayout = { layout.value = it },
        )
    }
}

/**
 * Shimmer progress for a concealed text, or a resting `0` when nothing should move.
 *
 * @param shimmering Whether the shimmer runs.
 * @param period Time one crossing takes.
 * @return Progress within the current cycle, in `[0, 1]`.
 */
@Composable
private fun rememberShimmer(shimmering: Boolean, period: Duration): State<Float> = if (shimmering) {
    rememberInfiniteTransition(label = "sensitiveTextShimmer").animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            // Linear and restarting, as in Skeleton: an eased sweep reads as an object moving across
            // the veil rather than as light crossing it.
            animation = composeTween(durationMillis = period.millis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "sensitiveTextShimmerProgress",
    )
} else {
    remember { mutableStateOf(0f) }
}

/**
 * Draws the veil over this text, and blurs the text under it.
 *
 * Both are driven from draw and layer lambdas that read [reveal] directly, so a running reveal costs
 * frames but no recompositions.
 *
 * @param layout Latest text layout, which the veil's line boxes are taken from.
 * @param reveal Reveal progress, `0` fully concealed and `1` fully revealed.
 * @param shimmer Shimmer progress within its current cycle.
 * @param glow How far the veil is brightened by a press, `0` at rest.
 * @param shimmering Whether the shimmer is running and should be drawn.
 * @param style Veil and sparkle colors.
 * @param metrics Shape, spread, and timing.
 * @return The receiver, with the veil applied.
 */
private fun Modifier.veil(
    layout: State<TextLayoutResult?>,
    reveal: State<Float>,
    shimmer: State<Float>,
    glow: State<Float>,
    shimmering: Boolean,
    style: SensitiveTextStyle,
    metrics: SensitiveTextSize,
): Modifier = this
    .drawWithCache {
        // Read here rather than in the draw lambda, so the line boxes and their outlines are rebuilt
        // when the text is laid out again and not on every frame of a reveal.
        val result = layout.value
        val bars = result?.let { veilBars(it, metrics) }.orEmpty()
        val lineBoxes = result?.let { lineBoxPath(it) }
        val spark = sparkPath()
        val sparkRadius = metrics.sparkRadius.toPx()
        val bandWidth = size.width * ShimmerBandFraction
        val fill = if (isBlurSupported) style.veil else style.opaqueVeil

        onDrawWithContent {
            val progress = reveal.value
            if (progress >= 1f || bars.isEmpty() || lineBoxes == null) {
                drawContent()
                return@onDrawWithContent
            }

            // A blur spreads the glyphs past the lines they belong to, which would leave a smear
            // beside a short line where the veil does not reach. Confining it to the line boxes
            // costs nothing at the end of the reveal, because sharp glyphs sit inside them anyway.
            clipPath(lineBoxes) { this@onDrawWithContent.drawContent() }

            val veilAlpha = 1f - progress
            val shimmerCenter = SensitiveTextMath.shimmerCenter(shimmer.value, size.width, bandWidth)

            bars.forEach { bar ->
                translate(bar.rect.left, bar.rect.top) {
                    drawOutline(outline = bar.outline, color = fill, alpha = veilAlpha)
                    if (glow.value > 0f) {
                        drawOutline(
                            outline = bar.outline,
                            color = style.shimmer,
                            alpha = veilAlpha * glow.value * PressGlowAlpha,
                        )
                    }
                    if (shimmering) {
                        drawOutline(
                            outline = bar.outline,
                            // Built per bar, because the gradient is positioned in the text's
                            // coordinates and the translation above has moved the origin.
                            brush = Brush.linearGradient(
                                colors = listOf(Color.Transparent, style.shimmer, Color.Transparent),
                                start = Offset(shimmerCenter - bandWidth - bar.rect.left, 0f),
                                end = Offset(shimmerCenter + bandWidth - bar.rect.left, 0f),
                            ),
                            alpha = veilAlpha * ShimmerAlpha,
                        )
                    }
                }
            }

            drawSparks(
                bars = bars,
                path = spark,
                radius = sparkRadius,
                color = style.sparkle,
                progress = progress,
            )
        }
    }
    .graphicsLayer {
        val progress = reveal.value
        val fontSize = layout.value?.layoutInput?.style?.fontSize
        // A fixed radius that hides body copy leaves a heading's shapes perfectly recognisable, so
        // the type size sets the floor and the token is only the minimum.
        val glyphHeight = if (fontSize != null && fontSize.isSp) {
            fontSize.toPx() * GlyphBandFactor
        } else {
            0f
        }
        val radius = maxOf(metrics.blurRadius.toPx(), glyphHeight * BlurGlyphFactor) * (1f - progress)
        renderEffect = if (isBlurSupported && radius > 0f) {
            BlurEffect(radiusX = radius, radiusY = radius, edgeTreatment = TileMode.Decal)
        } else {
            null
        }
        // Where the blur is real the glyphs stay drawn and it hides them. Where it is not, this is
        // what hides them, and the layer is simply not drawn while the value is concealed.
        alpha = if (isBlurSupported) 1f else SensitiveTextMath.contentAlpha(progress)
        // The draw above already confines the blur to the text's own lines. This is what catches it
        // in the frame before there is a layout to confine it to.
        clip = true
    }

/**
 * The veil over one line of text, and the outline it is drawn with.
 *
 * @property rect Area the veil covers, in the text's coordinates.
 * @property outline [rect]'s shape, resolved once so a running reveal does not rebuild it per frame.
 */
private data class VeilBar(
    val rect: Rect,
    val outline: Outline,
)

/**
 * Line boxes to veil for a laid-out text.
 *
 * Each box follows its own line's width, which is what keeps a concealed paragraph looking like
 * text. Its height comes from the type size rather than from the line box, because a line box
 * includes the leading above and below it: veiling that too would close the gaps between lines and
 * turn a paragraph into a single slab.
 *
 * @param result Text layout to read the lines from.
 * @param metrics Shape and spread of the veil.
 * @return One bar per line that has anything on it, in line order.
 */
private fun CacheDrawScope.veilBars(
    result: TextLayoutResult,
    metrics: SensitiveTextSize,
): List<VeilBar> {
    val horizontalPadding = metrics.horizontalPadding.toPx()
    val verticalPadding = metrics.verticalPadding.toPx()
    val fontSize = result.layoutInput.style.fontSize
    // Unspecified, or given in em rather than sp, leaves nothing absolute to measure the glyph band
    // with; the line box itself is then the only thing to fall back on.
    val glyphHeight = if (fontSize.isSp) fontSize.toPx() * GlyphBandFactor else null

    val bars = ArrayList<VeilBar>(result.lineCount)
    for (line in 0 until result.lineCount) {
        val lineTop = result.getLineTop(line)
        val lineBottom = result.getLineBottom(line)
        val band = glyphHeight ?: ((lineBottom - lineTop) * FallbackBandFraction)
        val centerY = (lineTop + lineBottom) / 2f

        val left = (result.getLineLeft(line) - horizontalPadding).coerceAtLeast(0f)
        val right = (result.getLineRight(line) + horizontalPadding).coerceAtMost(size.width)
        val top = (centerY - band / 2f - verticalPadding).coerceAtLeast(0f)
        val bottom = (centerY + band / 2f + verticalPadding).coerceAtMost(size.height)
        // An empty line has no glyphs to hide, and a veil over it would only advertise its position.
        if (right - left < MinimumBarPx || bottom - top < MinimumBarPx) continue

        val rect = Rect(left = left, top = top, right = right, bottom = bottom)
        bars += VeilBar(
            rect = rect,
            outline = metrics.shape.createOutline(rect.size, layoutDirection, this),
        )
    }
    return bars
}

/**
 * The lines of a laid-out text as one path, for clipping the blur to the text's own shape.
 *
 * Line boxes rather than the veil's bars: a bar is trimmed to the glyph band and would cut the tops
 * and tails off the letters, while a line box is what the text was laid out inside and so contains
 * every glyph that belongs to it.
 *
 * @param result Text layout to read the lines from.
 * @return One rectangle per line, unioned into a single path.
 */
private fun CacheDrawScope.lineBoxPath(result: TextLayoutResult): Path {
    val bleed = LineBoxBleed.toPx()
    return Path().apply {
        for (line in 0 until result.lineCount) {
            addRect(
                Rect(
                    left = result.getLineLeft(line) - bleed,
                    top = result.getLineTop(line) - bleed,
                    right = result.getLineRight(line) + bleed,
                    bottom = result.getLineBottom(line) + bleed,
                )
            )
        }
    }
}

/**
 * Draws the sparks of a reveal in progress.
 *
 * Their positions come from [SensitiveTextMath.noise], so a given text sparkles the same way every
 * time instead of crawling, and each spark peaks when the reveal passes its own position, which
 * turns the burst into a wave crossing the text.
 *
 * @param bars Veil the sparks are scattered over.
 * @param path Spark shape, in unit coordinates about its own centre.
 * @param radius Radius of a spark at full brightness, in pixels.
 * @param color Color to draw the sparks in.
 * @param progress Reveal progress. Nothing is drawn at rest.
 */
private fun DrawScope.drawSparks(
    bars: List<VeilBar>,
    path: Path,
    radius: Float,
    color: Color,
    progress: Float,
) {
    bars.forEachIndexed { line, bar ->
        repeat(SparksPerLine) { index ->
            val seed = line * SparkSeedStride + index
            val along = SensitiveTextMath.noise(seed)
            val across = SensitiveTextMath.noise(seed + SparkSeedOffset)
            val alpha = SensitiveTextMath.sparkAlpha(progress = progress, phase = along)
            if (alpha <= 0f) return@repeat

            val scale = radius * (SparkMinimumScale + (1f - SparkMinimumScale) * alpha)
            withTransform({
                translate(
                    left = bar.rect.left + bar.rect.width * along,
                    top = bar.rect.top + bar.rect.height * across,
                )
                scale(scaleX = scale, scaleY = scale, pivot = Offset.Zero)
            }) {
                drawPath(path = path, color = color, alpha = alpha)
            }
        }
    }
}

/**
 * The four-pointed star a spark is drawn as, in unit coordinates about its own centre.
 *
 * Pinched sides rather than a plain diamond: the concave curve is what reads as a glint at this
 * size, where a straight-edged diamond reads as a dot.
 *
 * @return A closed path spanning `-1` to `1` on both axes.
 */
private fun sparkPath(): Path = Path().apply {
    moveTo(0f, -1f)
    quadraticTo(SparkWaist, -SparkWaist, 1f, 0f)
    quadraticTo(SparkWaist, SparkWaist, 0f, 1f)
    quadraticTo(-SparkWaist, SparkWaist, -1f, 0f)
    quadraticTo(-SparkWaist, -SparkWaist, 0f, -1f)
    close()
}

/** Opacity of the frosted veil drawn over blurred glyphs. */
private const val FrostedVeilAlpha = 0.55f

/** Share of a text's width the shimmer band covers. */
private const val ShimmerBandFraction = 0.45f

/** Peak opacity of the shimmer band, before the veil's own fade is applied. */
private const val ShimmerAlpha = 0.35f

/** How far the blur is allowed past a line box, so an overhanging glyph is not cut off. */
private val LineBoxBleed = 1.dp

/** Blur radius as a share of a line's glyph band, which the veil's own token is a floor for. */
private const val BlurGlyphFactor = 0.42f

/** Peak opacity of the tint a press adds to the veil. */
private const val PressGlowAlpha = 0.18f

/** Height of the veil over a line, as a multiple of the type size. */
private const val GlyphBandFactor = 1.2f

/** Height of the veil over a line, as a share of the line box, when the type size is not in sp. */
private const val FallbackBandFraction = 0.72f

/** Smallest bar worth drawing, in pixels. Anything under it is an empty line. */
private const val MinimumBarPx = 1f

/** Sparks scattered over each veiled line. */
private const val SparksPerLine = 7

/** Stride between the seeds of neighbouring lines, so no two lines sparkle alike. */
private const val SparkSeedStride = 31

/** Offset between a spark's two seeds, so its position along a line does not decide its height. */
private const val SparkSeedOffset = 977

/** Size of a spark at the edge of its window, as a share of its full size. */
private const val SparkMinimumScale = 0.35f

/** Control point of a spark's pinched side, as a share of its radius. */
private const val SparkWaist = 0.16f

/**
 * What assistive technology announces in place of a concealed value.
 *
 * Plain English rather than a localized string, because the foundation carries no resources; an
 * application that localizes should pass its own `concealedDescription`.
 */
private const val ConcealedDescription = "Hidden"
