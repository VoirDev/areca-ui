package dev.voir.areca.foundation.feedback

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.AccessibilityManager
import androidx.compose.ui.platform.LocalAccessibilityManager
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import dev.voir.areca.foundation.LocalContentColor
import dev.voir.areca.foundation.LocalTextStyle
import dev.voir.areca.foundation.icon.IconSize
import dev.voir.areca.foundation.icon.LocalIconSize
import dev.voir.areca.foundation.text.Text
import dev.voir.areca.indication.FadeIndication
import dev.voir.areca.modifier.clickCursor
import dev.voir.areca.theme.ArecaMotion
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.millis
import kotlinx.coroutines.delay
import kotlin.concurrent.atomics.AtomicLong
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.concurrent.atomics.incrementAndFetch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

/** How a toast arrives and leaves. */
public enum class ToastAnimation {
    /** Slides up from the bottom edge as it fades in. The default, and the most legible arrival. */
    Slide,

    /** Fades only. For a toast that replaces another in place. */
    Fade,

    /** Grows from its centre as it fades in. Draws more attention than it usually deserves. */
    Scale,
}

/** How much a toast separates itself from the screen behind it. */
public enum class ToastColor {
    /** A muted surface, for a toast on a plain background. */
    Neutral,

    /** The inverted surface, for a toast that must read over arbitrary content. */
    NeutralDark,
}

/**
 * One queued toast.
 *
 * @property id Identity of this toast, assigned on creation and used to dismiss it. Unique within a
 *   process.
 * @property label What happened. Kept to two lines.
 * @property caption Supporting detail. Kept to three lines.
 * @property color How much the toast separates from the screen. `null` uses the [ToastHost]'s
 *   style, which defaults to [LocalToastStyle].
 * @property shape Shape of the toast's surface. `null` uses the theme's.
 * @property duration How long it stays before accessibility settings lengthen it. `null` keeps it
 *   until something dismisses it, which is what a toast carrying an action needs.
 * @property alignment How the toast's rows line up against each other. Top-aligned by default, as
 *   [ToastState.show] and [Toast] are, so a two-line label keeps its icon beside the first line.
 * @property leading Content before the text, normally an icon. Given the toast's [id].
 * @property trailing Content after the text, normally a dismiss or undo control. Given the toast's
 *   [id].
 * @property onClick Called with the toast's [id] when the toast itself is pressed. `null` leaves the
 *   toast unpressable.
 */
@Immutable
public data class ToastData(
    public val id: Long = nextToastId(),
    public val label: CharSequence,
    public val caption: CharSequence? = null,
    public val color: ToastColor? = null,
    public val shape: Shape? = null,
    public val duration: Duration? = 2.seconds,
    public val alignment: Alignment.Vertical = Alignment.Top,
    public val leading: (@Composable RowScope.(id: Long) -> Unit)? = null,
    public val trailing: (@Composable RowScope.(id: Long) -> Unit)? = null,
    public val onClick: ((id: Long) -> Unit)? = null,
)

/**
 * Colors of a toast.
 *
 * Four rather than two, because a toast holds three kinds of content at three weights: what
 * happened, the detail under it, and the controls beside it.
 *
 * @property bg Fill of the toast's surface.
 * @property label Color of the main line.
 * @property caption Color of the supporting line.
 * @property icon Color of the leading and trailing slots.
 */
@Immutable
public data class ToastStyle(
    public val bg: Color,
    public val label: Color,
    public val caption: Color,
    public val icon: Color,
) {
    /** Factories reading the current theme. */
    public companion object {
        /** Theme default: a muted surface. */
        public val Default: ToastStyle
            @Composable
            get() = default(ToastColor.Neutral)

        /**
         * Builds a style from the current theme.
         *
         * @param color How much the toast separates from the screen behind it.
         * @return Colors for that separation.
         */
        @Composable
        public fun default(color: ToastColor = ToastColor.Neutral): ToastStyle = when (color) {
            ToastColor.Neutral -> ToastStyle(
                bg = ArecaTheme.colors.bgMuted,
                label = ArecaTheme.colors.fgBase,
                caption = ArecaTheme.colors.fgSubtle,
                icon = ArecaTheme.colors.fgMuted,
            )

            ToastColor.NeutralDark -> ToastStyle(
                bg = ArecaTheme.colors.bgContrast,
                label = ArecaTheme.colors.fgOnContrast,
                caption = ArecaTheme.colors.fgOnContrast,
                icon = ArecaTheme.colors.fgOnContrast,
            )
        }
    }
}

/** Default [ToastStyle] in scope, seeded by [dev.voir.areca.theme.ArecaThemeProvider]. */
public val LocalToastStyle: ProvidableCompositionLocal<ToastStyle?> = compositionLocalOf { null }

/**
 * Identities handed to toasts, counted rather than clocked.
 *
 * A millisecond timestamp collides whenever two toasts are created in the same millisecond, which is
 * exactly what a burst of them does — and `dismiss(id)` would then close the wrong one. Atomic,
 * because a [ToastData] can be built on any thread, not only the one that shows it.
 */
@OptIn(ExperimentalAtomicApi::class)
private val toastIdCounter = AtomicLong(0L)

/**
 * Next unused toast identity.
 *
 * Safe to call from any thread: concurrent callers never receive the same identity.
 *
 * @return An identity no other toast in this process has been given.
 */
@OptIn(ExperimentalAtomicApi::class)
private fun nextToastId(): Long = toastIdCounter.incrementAndFetch()

/**
 * The queue a [ToastHost] shows, one toast at a time and first in, first out.
 *
 * Get one from [rememberToastState]; it is not for implementing. Every method is safe to call from
 * an event handler on the main thread, and the host follows the change in its next frame.
 */
@Stable
public sealed interface ToastState {
    /** Toast being shown, or `null` when the queue is empty. */
    public val currentToast: State<ToastData?>

    /**
     * Queues a toast, showing it at once when no other toast is showing.
     *
     * @param label What happened. Kept to two lines.
     * @param caption Supporting detail. Kept to three lines.
     * @param color How much the toast separates from the screen. `null` uses the host's style.
     * @param shape Shape of the toast's surface. `null` uses the theme's.
     * @param duration How long it stays before accessibility settings lengthen it. `null` keeps it
     *   until dismissed.
     * @param alignment How the toast's rows line up against each other.
     * @param leading Content before the text, given the toast's id.
     * @param trailing Content after the text, given the toast's id.
     * @param onClick Called with the toast's id when the toast itself is pressed.
     * @return Identity of the queued toast, for [dismiss].
     */
    public fun show(
        label: CharSequence,
        caption: CharSequence? = null,
        color: ToastColor? = null,
        shape: Shape? = null,
        duration: Duration? = 2.seconds,
        alignment: Alignment.Vertical = Alignment.Top,
        leading: (@Composable RowScope.(id: Long) -> Unit)? = null,
        trailing: (@Composable RowScope.(id: Long) -> Unit)? = null,
        onClick: ((id: Long) -> Unit)? = null,
    ): Long

    /**
     * Dismisses the toast with [id], whether it is showing or still waiting. An id already dismissed
     * changes nothing.
     *
     * @param id Identity returned by [show].
     */
    public fun dismiss(id: Long)

    /** Dismisses the toast being shown, and shows the oldest waiting one, if any. */
    public fun dismiss()

    /** Dismisses the toast being shown and drops every waiting one. */
    public fun dismissAll()
}

/**
 * Shows the toasts of [state], one at a time, at the bottom of the space it is given.
 *
 * Fills its parent, keeps clear of [windowInsets], and pads itself by the theme's outer spacing. A
 * toast with a duration is dismissed when it runs out — lengthened by the platform's accessibility
 * settings, which give a reader who asked for more time, or a toast carrying a control, longer. A
 * toast that is dismissed leaves with [animation] before the next queued one arrives, so two toasts
 * never sit on top of each other.
 *
 * @param state Queue of toasts to show, from [rememberToastState].
 * @param modifier Modifier applied to the host.
 * @param animation How each toast arrives and leaves.
 * @param style Colors of a toast that names no [ToastData.color]. Defaults to the theme's.
 * @param windowInsets Platform insets the host keeps the toast clear of. Insets an ancestor already
 *   consumed are not applied twice; pass `WindowInsets(0, 0, 0, 0)` when the parent already insets
 *   its content by other means.
 */
@Composable
public fun ToastHost(
    state: ToastState,
    modifier: Modifier = Modifier,
    animation: ToastAnimation = ToastAnimation.Slide,
    style: ToastStyle = LocalToastStyle.current ?: ToastStyle.Default,
    windowInsets: WindowInsets = WindowInsets.safeDrawing,
) {
    val toast = state.currentToast.value
    val motion = ArecaTheme.motion
    val accessibilityManager = LocalAccessibilityManager.current

    if (toast != null) {
        val timeout = toast.dismissTimeout(accessibilityManager)
        if (timeout != null) {
            LaunchedEffect(toast.id, timeout) {
                delay(timeout)
                // By id, so a timer that outlives its toast cannot close the one that replaced it.
                state.dismiss(toast.id)
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(windowInsets)
            .padding(all = ArecaTheme.spacing.xl),
        contentAlignment = Alignment.BottomCenter,
    ) {
        AnimatedContent(
            targetState = toast,
            contentKey = { it?.id },
            contentAlignment = Alignment.BottomCenter,
            transitionSpec = {
                val isSwap = initialState != null && targetState != null
                val enterDelay = if (isSwap) motion.medium.millis else 0
                (animation.enter(motion, enterDelay) togetherWith animation.exit(motion))
                    .using(null)
            },
        ) { shown ->
            if (shown != null) {
                Toast(
                    id = shown.id,
                    label = shown.label,
                    caption = shown.caption,
                    style = shown.color?.let { ToastStyle.default(it) } ?: style,
                    shape = shown.shape ?: ArecaTheme.shapes.md,
                    alignment = shown.alignment,
                    onClick = shown.onClick,
                    leading = shown.leading,
                    trailing = shown.trailing,
                )
            }
        }
    }
}

/**
 * How long this toast stays, once the platform's accessibility settings have had their say.
 *
 * The platform lengthens a timeout for a reader who asked for more time to act, and lengthens it
 * further for a toast carrying a control — the reader has to reach it, not just read it. A toast
 * with no [ToastData.duration] stays until dismissed whatever the settings say.
 *
 * @param accessibilityManager The platform's accessibility settings, or `null` where the platform
 *   offers none, which keeps the toast's own duration.
 * @return How long the toast stays, or `null` when it stays until dismissed.
 */
internal fun ToastData.dismissTimeout(accessibilityManager: AccessibilityManager?): Duration? {
    val requested = duration ?: return null
    if (accessibilityManager == null) return requested
    val recommendedMillis = accessibilityManager.calculateRecommendedTimeoutMillis(
        originalTimeoutMillis = requested.inWholeMilliseconds,
        containsIcons = leading != null,
        containsText = true,
        containsControls = trailing != null || onClick != null,
    )
    return recommendedMillis.milliseconds.takeIf { it.isFinite() }
}

/**
 * How a toast arrives.
 *
 * @param motion Motion tokens to time the arrival with.
 * @param delayMillis How long to wait before arriving, in milliseconds.
 * @return The enter transition for this animation.
 */
private fun ToastAnimation.enter(motion: ArecaMotion, delayMillis: Int): EnterTransition {
    val durationMillis = motion.medium.millis
    return when (this) {
        ToastAnimation.Slide ->
            slideInVertically(tween(durationMillis, delayMillis, motion.enter)) { it } +
                fadeIn(tween(durationMillis, delayMillis, motion.enter))

        ToastAnimation.Fade -> fadeIn(tween(durationMillis, delayMillis, motion.enter))

        ToastAnimation.Scale ->
            scaleIn(tween(durationMillis, delayMillis, motion.enter)) +
                fadeIn(tween(durationMillis, delayMillis, motion.enter))
    }
}

/**
 * How a toast leaves.
 *
 * @param motion Motion tokens to time the departure with.
 * @return The exit transition for this animation.
 */
private fun ToastAnimation.exit(motion: ArecaMotion): ExitTransition {
    val durationMillis = motion.medium.millis
    return when (this) {
        ToastAnimation.Slide ->
            slideOutVertically(tween(durationMillis, easing = motion.exit)) { it } +
                fadeOut(tween(durationMillis, easing = motion.exit))

        ToastAnimation.Fade -> fadeOut(tween(durationMillis, easing = motion.exit))

        ToastAnimation.Scale ->
            scaleOut(tween(durationMillis, easing = motion.exit)) +
                fadeOut(tween(durationMillis, easing = motion.exit))
    }
}

/**
 * One toast's surface: what happened, the detail under it, and the controls beside it.
 *
 * Normally drawn by a [ToastHost] from a queued [ToastData]; call it directly only to show a toast
 * outside a host, such as in a preview. It fills the available width and is announced politely as
 * soon as it appears, because a toast that leaves after two seconds is never reached by focus
 * traversal.
 *
 * Pass [onClick] to make the whole toast pressable. It then gains hover, press, and a keyboard focus
 * ring, and announces itself as a button.
 *
 * @param id Identity of the toast, handed to [onClick] and to the slots.
 * @param label What happened. Kept to two lines.
 * @param modifier Modifier applied to the toast.
 * @param caption Supporting detail, or `null` for none. Kept to three lines.
 * @param style Colors. Defaults to the theme's.
 * @param shape Shape of the surface, the indication, and its focus ring.
 * @param alignment How the leading slot, the text, and the trailing slot line up against each other.
 * @param onClick Called with [id] when the toast is pressed. `null` leaves it unpressable.
 * @param interactionSource Source of a pressable toast's press, hover, and focus interactions.
 *   `null` creates one internally.
 * @param leading Content before the text, normally an icon. Given [id].
 * @param trailing Content after the text, normally a dismiss or undo control. Given [id].
 */
@Composable
public fun Toast(
    id: Long,
    label: CharSequence,
    modifier: Modifier = Modifier,
    caption: CharSequence? = null,
    style: ToastStyle = LocalToastStyle.current ?: ToastStyle.Default,
    shape: Shape = ArecaTheme.shapes.md,
    alignment: Alignment.Vertical = Alignment.Top,
    onClick: ((id: Long) -> Unit)? = null,
    interactionSource: MutableInteractionSource? = null,
    leading: (@Composable RowScope.(id: Long) -> Unit)? = null,
    trailing: (@Composable RowScope.(id: Long) -> Unit)? = null,
) {
    val labelStyle = ArecaTheme.typography.labelMedium
    val captionStyle = ArecaTheme.typography.bodySmall
    val click = onClick

    CompositionLocalProvider(
        LocalTextStyle provides labelStyle,
        LocalContentColor provides style.label,
        LocalIconSize provides IconSize.Medium,
    ) {
        Box(
            modifier = modifier
                .semantics { liveRegion = LiveRegionMode.Polite }
                .fillMaxWidth()
                .background(style.bg, shape)
                .then(
                    if (click != null) {
                        Modifier
                            .clickable(
                                interactionSource = interactionSource,
                                indication = FadeIndication(shape = shape),
                                role = Role.Button,
                                onClick = { click(id) },
                            )
                            .clickCursor()
                    } else {
                        Modifier
                    },
                ),
        ) {
            Row(
                verticalAlignment = alignment,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = ArecaTheme.spacing.lg),
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = alignment,
                    horizontalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.md),
                ) {
                    if (leading != null) {
                        CompositionLocalProvider(LocalContentColor provides style.icon) {
                            leading(id)
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(ArecaTheme.spacing.xs2)) {
                        Text(
                            text = label,
                            style = labelStyle,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )

                        if (caption != null) {
                            CompositionLocalProvider(LocalContentColor provides style.caption) {
                                Text(
                                    text = caption,
                                    style = captionStyle.merge(color = style.caption),
                                    maxLines = 3,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }
                        }
                    }
                }

                if (trailing != null) {
                    CompositionLocalProvider(LocalContentColor provides style.icon) {
                        trailing(id)
                    }
                }
            }
        }
    }
}

/**
 * Remembers an empty toast queue for a [ToastHost] to show.
 *
 * @return A queue that survives recomposition, but not the host leaving the composition.
 */
@Composable
public fun rememberToastState(): ToastState = remember { ToastStateImpl() }

/**
 * The [ToastState] behind [rememberToastState]: builds each toast and hands it to a [ToastQueue].
 */
private class ToastStateImpl : ToastState {
    /** Toasts shown and waiting, in showing order. */
    private val queue = ToastQueue()

    override val currentToast: State<ToastData?> = queue.current

    override fun show(
        label: CharSequence,
        caption: CharSequence?,
        color: ToastColor?,
        shape: Shape?,
        duration: Duration?,
        alignment: Alignment.Vertical,
        leading: (@Composable RowScope.(id: Long) -> Unit)?,
        trailing: (@Composable RowScope.(id: Long) -> Unit)?,
        onClick: ((id: Long) -> Unit)?,
    ): Long = queue.enqueue(
        ToastData(
            label = label,
            caption = caption,
            color = color,
            shape = shape,
            duration = duration,
            alignment = alignment,
            leading = leading,
            trailing = trailing,
            onClick = onClick,
        ),
    )

    override fun dismiss(id: Long) = queue.dismiss(id)

    override fun dismissAll() = queue.clear()

    override fun dismiss() = queue.dismissCurrent()
}

/**
 * Toasts waiting to be shown, one at a time and first in, first out.
 *
 * At most one toast is [current]. A toast queued while another is current waits behind it, and the
 * oldest waiting toast becomes current in the same snapshot write that dismisses the current one.
 * Backed by snapshot state, so a composition reading [current] follows it.
 */
internal class ToastQueue {
    /** Toasts waiting behind the current one, oldest first. */
    private val waiting = mutableStateListOf<ToastData>()

    /** Writable backing of [current]. */
    private val currentToast = mutableStateOf<ToastData?>(null)

    /** Toast being shown, or `null` when the queue is empty. */
    val current: State<ToastData?> = currentToast

    /**
     * Queues [toast], showing it at once when nothing else is.
     *
     * @param toast Toast to show.
     * @return The toast's [ToastData.id], for [dismiss].
     */
    fun enqueue(toast: ToastData): Long {
        waiting.add(toast)
        if (currentToast.value == null) showNext()
        return toast.id
    }

    /**
     * Dismisses the toast with [id], whether it is current or still waiting.
     *
     * An id that is neither — a toast already dismissed — changes nothing.
     *
     * @param id Identity of the toast to dismiss.
     */
    fun dismiss(id: Long) {
        if (currentToast.value?.id == id) {
            dismissCurrent()
        } else {
            waiting.removeAll { it.id == id }
        }
    }

    /** Dismisses the current toast and shows the oldest waiting one, if any. */
    fun dismissCurrent() = showNext()

    /** Replaces the current toast with the oldest waiting one, or with none when none waits. */
    private fun showNext() {
        currentToast.value = waiting.removeFirstOrNull()
    }

    /** Dismisses the current toast and drops every waiting one. */
    fun clear() {
        currentToast.value = null
        waiting.clear()
    }
}
