package dev.voir.areca.window

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified

/**
 * How wide the space an interface is laid out in is, in five ordered steps.
 *
 * The breakpoints are Material 3's window size classes, which also map onto Apple's regular and
 * compact size classes, so a design file and the platforms' own guidance agree with them. A class
 * describes the window — or, under a [WindowClassBox], the container — and never the device: a large
 * tablet in a narrow split view is [Compact] and should get the phone layout.
 *
 * The steps are ordered, so `width >= WidthClass.Medium` reads as "at least medium".
 *
 * @property minWidth Narrowest width in this class. The class runs up to the next one's.
 */
public enum class WidthClass(public val minWidth: Dp) {
    /** Under 600dp: a phone in portrait, a folded foldable, a narrow split view. */
    Compact(0.dp),

    /** 600dp to 839dp: an unfolded foldable, a tablet in portrait, half a desktop screen. */
    Medium(600.dp),

    /** 840dp to 1199dp: a tablet in landscape, a phone in landscape, a small desktop window. */
    Expanded(840.dp),

    /** 1200dp to 1599dp: a large tablet in landscape, a laptop screen. */
    Large(1200.dp),

    /** 1600dp and wider: a desktop monitor. */
    ExtraLarge(1600.dp),
    ;

    /**
     * Picks the value for this class, mobile first.
     *
     * Only [compact] is required. Each larger step defaults to the one below it, so a caller names
     * the steps where the value changes and the rest follow: `select(compact = 16.dp, medium =
     * 24.dp)` is 16dp on a phone and 24dp on every wider window.
     *
     * @param compact Value for [Compact].
     * @param medium Value for [Medium]. Defaults to [compact].
     * @param expanded Value for [Expanded]. Defaults to [medium].
     * @param large Value for [Large]. Defaults to [expanded].
     * @param extraLarge Value for [ExtraLarge]. Defaults to [large].
     * @return The value for this class.
     */
    public fun <T> select(
        compact: T,
        medium: T = compact,
        expanded: T = medium,
        large: T = expanded,
        extraLarge: T = large,
    ): T = when (this) {
        Compact -> compact
        Medium -> medium
        Expanded -> expanded
        Large -> large
        ExtraLarge -> extraLarge
    }

    /** Lookup by width. */
    public companion object {
        /**
         * The class [width] falls in.
         *
         * An unspecified width — a window not measured yet — is [Compact], so an interface that
         * cannot tell draws the layout every Areca screen already supports.
         *
         * @param width Width of the window or container.
         * @return The class [width] falls in.
         */
        public fun of(width: Dp): WidthClass {
            if (!width.isSpecified) return Compact
            return entries.lastOrNull { width >= it.minWidth } ?: Compact
        }
    }
}

/**
 * How tall the space an interface is laid out in is, in three ordered steps.
 *
 * Height is the rarer question. It is what tells a phone in landscape — wide, but too short for a
 * bottom bar and a top bar both — from a tablet, and it decides whether a form fits without
 * scrolling.
 *
 * @property minHeight Shortest height in this class. The class runs up to the next one's.
 */
public enum class HeightClass(public val minHeight: Dp) {
    /** Under 480dp: a phone in landscape, a short desktop window. */
    Compact(0.dp),

    /** 480dp to 899dp: a phone in portrait, a tablet in landscape, a laptop. */
    Medium(480.dp),

    /** 900dp and taller: a tablet in portrait, a tall desktop window. */
    Expanded(900.dp),
    ;

    /**
     * Picks the value for this class, shortest first. Each taller step defaults to the one below it.
     *
     * @param compact Value for [Compact].
     * @param medium Value for [Medium]. Defaults to [compact].
     * @param expanded Value for [Expanded]. Defaults to [medium].
     * @return The value for this class.
     */
    public fun <T> select(
        compact: T,
        medium: T = compact,
        expanded: T = medium,
    ): T = when (this) {
        Compact -> compact
        Medium -> medium
        Expanded -> expanded
    }

    /** Lookup by height. */
    public companion object {
        /**
         * The class [height] falls in.
         *
         * An unspecified height is [Medium], the height of an ordinary phone held upright, so an
         * unmeasured window is not mistaken for a phone in landscape.
         *
         * @param height Height of the window or container.
         * @return The class [height] falls in.
         */
        public fun of(height: Dp): HeightClass {
            if (!height.isSpecified) return Medium
            return entries.lastOrNull { height >= it.minHeight } ?: Compact
        }
    }
}

/**
 * What presses the controls: a finger, or a mouse or trackpad.
 *
 * It answers a different question from the size classes. Width decides how a screen is laid out;
 * input decides how large a control has to be and whether an action may wait for a hover. A tablet in
 * portrait is wide and still touched, and a laptop is pointer-driven at any width.
 */
public enum class InputMode {
    /** A finger: controls keep a 48dp touch target and nothing depends on hovering. */
    Touch,

    /** A mouse or trackpad: precise, able to hover and to right-click. */
    Pointer,
}

/**
 * A familiar name for a combination of size classes, shape and input.
 *
 * Derived from the window, never from the device: a foldable unfolded is a [TabletPortrait], and a
 * desktop window dragged narrow is a [PhonePortrait]. Use it where a design is specified per
 * profile; branch on [WindowClass.width] where it is specified per breakpoint.
 *
 * Under touch a tablet's profile is decided by its shape, not its width: a 13-inch tablet held
 * upright is as wide as a small one on its side, and an unfolded foldable on its side is wider than
 * a tablet held upright while still being held like one.
 */
public enum class WindowProfile {
    /** Width [WidthClass.Compact]: a phone held upright, a folded foldable, a narrow window. */
    PhonePortrait,

    /** Height [HeightClass.Compact] at any width: a phone on its side. */
    PhoneLandscape,

    /**
     * Touch at [WidthClass.Medium] and wider, less than [WindowClass.WideAspectRatio] times as wide as
     * it is tall: a tablet held upright, an unfolded foldable either way round. Also a medium window
     * under a pointer.
     */
    TabletPortrait,

    /** Touch at [WidthClass.Medium] and wider, at least [WindowClass.WideAspectRatio] times as wide as it is tall: a tablet on its side. */
    TabletLandscape,

    /** Width [WidthClass.Expanded] and wider under a pointer. */
    Desktop,
}

/**
 * Where a screen's actions are within reach: beside its title, or at the foot of the window under
 * the thumbs.
 *
 * It answers how the window is held rather than how wide it is. A tablet on its side stands on a
 * desk or is held by its short edges, and has room beside a title for labelled buttons; a phone, a
 * tablet held upright and an unfolded foldable either way round are held from below, where only the
 * foot of the window is in reach. A mouse or trackpad reaches everywhere, so a pointer is always
 * [Header].
 *
 * Read it from the whole window, [dev.voir.areca.theme.ArecaTheme.hostWindow]: the hands hold the
 * device, not the pane a screen is drawn in.
 */
public enum class ActionReach {
    /**
     * Beside the title: a pointer at any size, a phone on its side, and a touch window at least
     * [WindowClass.WideAspectRatio] times as wide as it is tall.
     */
    Header,

    /** At the foot of the window: a phone, a tablet held upright, an unfolded foldable. */
    Thumb,
}

/**
 * The space an interface is laid out in, classified.
 *
 * Read the one in scope through [dev.voir.areca.theme.ArecaTheme.window]. It describes the window
 * [dev.voir.areca.theme.ArecaThemeProvider] runs in, or the container a [WindowClassBox] measured,
 * so a component inside a 360dp pane of a desktop window sees a compact width.
 *
 * Construct one directly to preview or test a screen at a given window ([ProvideWindowClass]).
 *
 * @property size Width and height of the window or container. Unspecified until it is measured.
 * @property input What presses the controls.
 * @property fold The fold or hinge across the window or container, in its own coordinates, or `null`
 *   for none — always `null` on iOS and the desktop, and under a [WindowClassBox] the fold doesn't cross.
 */
@Immutable
public class WindowClass(
    public val size: DpSize,
    public val input: InputMode,
    public val fold: WindowFold? = null,
) {
    /** Width class of [size]. */
    public val width: WidthClass = WidthClass.of(size.widthOrUnspecified)

    /** Height class of [size]. */
    public val height: HeightClass = HeightClass.of(size.heightOrUnspecified)

    /** Whether [size] is wider than it is tall. `false` while the size is unspecified. */
    public val isLandscape: Boolean =
        size.widthOrUnspecified.isSpecified &&
            size.heightOrUnspecified.isSpecified &&
            size.width > size.height

    /**
     * Whether [size] is at least [WideAspectRatio] times as wide as it is tall: a tablet on its side,
     * and not an unfolded foldable on its side, which is nearly square.
     */
    private val isWide: Boolean = isLandscape && size.width.value >= size.height.value * WideAspectRatio

    /** Where the screen's actions are within reach, for how the window is held. */
    public val actionReach: ActionReach = when {
        input == InputMode.Pointer -> ActionReach.Header
        height == HeightClass.Compact -> ActionReach.Header
        width == WidthClass.Compact -> ActionReach.Thumb
        isWide -> ActionReach.Header
        else -> ActionReach.Thumb
    }

    /** The familiar name for [width], [height], the shape and [input] together. */
    public val profile: WindowProfile = when {
        height == HeightClass.Compact -> WindowProfile.PhoneLandscape
        width == WidthClass.Compact -> WindowProfile.PhonePortrait
        input == InputMode.Pointer ->
            if (width == WidthClass.Medium) WindowProfile.TabletPortrait else WindowProfile.Desktop
        isWide -> WindowProfile.TabletLandscape
        else -> WindowProfile.TabletPortrait
    }

    override fun equals(other: Any?): Boolean =
        this === other ||
            (other is WindowClass && size == other.size && input == other.input && fold == other.fold)

    override fun hashCode(): Int = 31 * (31 * size.hashCode() + input.hashCode()) + fold.hashCode()

    override fun toString(): String =
        "WindowClass(size=$size, input=$input, width=$width, height=$height, fold=$fold)"

    /** The line between a wide window and a nearly square one. */
    public companion object {
        /**
         * How many times as wide as it is tall a touch window has to be to count as a tablet on its
         * side. Tablets on their side start at 4:3, about 1.33; unfolded foldables on their side reach
         * about 1.2. The line sits between, so no device in use lands near it.
         */
        public const val WideAspectRatio: Float = 1.25f
    }
}

// `DpSize.width` throws on a fully unspecified size; one unspecified side reads as `Dp.Unspecified`.
private val DpSize.widthOrUnspecified: Dp get() = if (isSpecified) width else Dp.Unspecified
private val DpSize.heightOrUnspecified: Dp get() = if (isSpecified) height else Dp.Unspecified
