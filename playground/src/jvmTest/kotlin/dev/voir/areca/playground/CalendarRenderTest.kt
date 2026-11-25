package dev.voir.areca.playground

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import dev.voir.areca.datetime.LocalDateTimeLocale
import dev.voir.areca.datetime.DateTimeLocale
import dev.voir.areca.datetime.calendar.Calendar
import dev.voir.areca.datetime.calendar.CalendarDateRange
import dev.voir.areca.datetime.calendar.CalendarDateRule
import dev.voir.areca.datetime.calendar.CalendarDayShape
import dev.voir.areca.datetime.calendar.CalendarMarker
import dev.voir.areca.datetime.calendar.CalendarSelection
import dev.voir.areca.datetime.calendar.CalendarSize
import dev.voir.areca.datetime.calendar.CalendarState
import dev.voir.areca.datetime.calendar.CalendarStyle
import dev.voir.areca.datetime.calendar.range.DateRangeCalendar
import dev.voir.areca.datetime.calendar.range.DateRangeSelection
import dev.voir.areca.datetime.calendar.rememberCalendarState
import dev.voir.areca.icons.ArecaIcons
import dev.voir.areca.icons.Plane
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.ArecaThemeProvider
import dev.voir.areca.window.InputMode
import androidx.compose.runtime.CompositionLocalProvider
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import org.jetbrains.skia.EncodedImageFormat
import java.io.File
import kotlin.test.Test
import kotlin.time.Clock
import kotlin.time.Instant

/** Logical width the calendars are rendered at. */
private const val WIDTH = 390

/** Logical height of one render. */
private const val HEIGHT = 540

/** Pixel density the scenes are rasterised at. */
private const val DENSITY = 2f

/** Time between the frames stepped through before the final one, in nanoseconds. */
private const val FRAME_NANOS = 50_000_000L

/** How long the clock runs before the kept frame: past every colour and level animation. */
private const val SETTLE_NANOS = 1_500_000_000L

/** Where the rendered images are written. */
private val OUTPUT = File(System.getProperty("render.out") ?: "build/screens")

/** A clock pinned to 25 November 2026, so every render draws the same today. */
private val CalendarRenderClock = object : Clock {
    override fun now(): Instant = Instant.parse("2026-11-25T12:00:00Z")
}

/** The pinned today. */
private val RenderToday = LocalDate(2026, 11, 25)

/**
 * Renders one composition to a PNG once it has settled.
 *
 * @param name File name, without the extension.
 * @param dark Whether to render in the dark theme.
 * @param content Composition to render.
 */
@OptIn(ExperimentalComposeUiApi::class)
private fun render(name: String, dark: Boolean = false, content: @Composable () -> Unit) {
    val scene = ImageComposeScene(
        width = (WIDTH * DENSITY).toInt(),
        height = (HEIGHT * DENSITY).toInt(),
        density = Density(DENSITY),
    ) {
        ArecaThemeProvider(darkTheme = dark, inputMode = InputMode.Touch) {
            CompositionLocalProvider(LocalDateTimeLocale provides DateTimeLocale.Iso) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(ArecaTheme.surface.ground)
                        .padding(16.dp),
                ) { content() }
            }
        }
    }
    try {
        var time = 0L
        while (time < SETTLE_NANOS) {
            scene.render(time)
            time += FRAME_NANOS
        }
        val data = requireNotNull(scene.render(SETTLE_NANOS).encodeToData(EncodedImageFormat.PNG))
        OUTPUT.mkdirs()
        File(OUTPUT, "$name.png").writeBytes(data.bytes)
    } finally {
        scene.close()
    }
}

/**
 * Renders the calendar's levels and markers to `build/screens`.
 *
 * Like the other render tests it has no golden image; it fails when a configuration throws, and it
 * is how the calendar is looked at on a machine with no display.
 *
 * ```bash
 * ./gradlew :playground:jvmTest --tests '*CalendarRenderTest*'
 * ```
 */
class CalendarRenderTest {

    /** Event dots under a circle selection, light and dark, and in a compact calendar. */
    @Test
    fun `dot markers render`() {
        render("calendar-dots") { EventCalendar() }
        render("calendar-dots-dark", dark = true) { EventCalendar() }
        render("calendar-dots-compact") { EventCalendar(size = CalendarSize.Compact) }
    }

    /** Prices inside tiles, counts, icons and load bars. */
    @Test
    fun `other markers render`() {
        render("calendar-prices-tile") {
            MarkedCalendar(shape = CalendarDayShape.Tile) { date ->
                val fare = fareOn(date)
                CalendarMarker.Label("€$fare", emphasis = fare <= 85)
            }
        }
        render("calendar-count-tile") {
            MarkedCalendar(shape = CalendarDayShape.Tile) { date ->
                eventsOn(date).size.takeIf { it > 0 }?.let { CalendarMarker.Count(it) }
            }
        }
        render("calendar-icons") {
            MarkedCalendar { date ->
                if (eventsOn(date).size == 2) CalendarMarker.Icon(ArecaIcons.Plane, ArecaColorsWarn) else null
            }
        }
        render("calendar-load") {
            MarkedCalendar { date ->
                eventsOn(date).size.takeIf { it > 0 }?.let { CalendarMarker.Load(it / 5f) }
            }
        }
    }

    /** The months level with month markers, and the years level. */
    @Test
    fun `month and year levels render`() {
        render("calendar-months") {
            val state = pinnedState()
            LaunchedEffect(state) { state.zoomOut() }
            EventCalendar(state = state)
        }
        render("calendar-years") {
            val state = pinnedState()
            LaunchedEffect(state) {
                state.zoomOut()
                state.zoomOut()
            }
            EventCalendar(state = state)
        }
    }

    /** A range with markers below its band. */
    @Test
    fun `range markers render`() {
        render("calendar-range-dots") {
            DateRangeCalendar(
                selection = DateRangeSelection.Complete(
                    CalendarDateRange(LocalDate(2026, 11, 10), LocalDate(2026, 11, 14)),
                ),
                onSelectionChange = {},
                state = pinnedState(),
                markers = { date -> dotsOn(date) },
            )
        }
    }
}

/** A calendar state pinned to [RenderToday]. */
@Composable
private fun pinnedState(): CalendarState =
    rememberCalendarState(clock = CalendarRenderClock, timeZone = TimeZone.UTC)

/**
 * A calendar with event dots on its days and its month tiles, today chosen.
 *
 * @param state State to draw.
 * @param size Calendar size.
 */
@Composable
private fun EventCalendar(state: CalendarState = pinnedState(), size: CalendarSize = CalendarSize.Default) {
    Calendar(
        modifier = Modifier.fillMaxWidth(),
        state = state,
        selection = CalendarSelection.Single(RenderToday),
        onDateClick = {},
        size = size,
        markers = { date -> dotsOn(date) },
        monthMarkers = { month -> if (month.month.ordinal % 3 != 1) CalendarMarker.Dots(listOf(Color(0xFF7257FF))) else null },
    )
}

/**
 * A calendar with today chosen and the given markers, refusing past days.
 *
 * @param shape Day shape.
 * @param markers Markers to draw.
 */
@Composable
private fun MarkedCalendar(
    shape: CalendarDayShape = CalendarDayShape.Circle,
    markers: (LocalDate) -> CalendarMarker?,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Calendar(
            modifier = Modifier.fillMaxWidth(),
            state = pinnedState(),
            selection = CalendarSelection.Single(RenderToday),
            onDateClick = {},
            dateRule = CalendarDateRule { it >= LocalDate(2026, 11, 3) },
            style = CalendarStyle.default(dayShape = shape),
            markers = markers,
        )
    }
}

/** Amber, for the plane icons. */
private val ArecaColorsWarn = Color(0xFFED9B16)

/** Made-up categories per day, the same every run; 25 November has three. */
private fun eventsOn(date: LocalDate): List<Color> = when ((date.day * 7 + 3) % 11) {
    4, 5 -> listOf(Color(0xFF7257FF))
    6 -> listOf(Color(0xFF008557))
    7 -> listOf(Color(0xFF7257FF), Color(0xFFED9B16))
    8, 1, 2 -> listOf(Color(0xFF7257FF), Color(0xFF008557), Color(0xFFDB340B))
    9 -> List(5) { Color(0xFF7257FF) }
    else -> emptyList()
}

/** Dots for [eventsOn], or `null` on a free day. */
private fun dotsOn(date: LocalDate): CalendarMarker? =
    eventsOn(date).takeIf { it.isNotEmpty() }?.let { colors ->
        if (colors.size > 3) {
            CalendarMarker.Dots(colors.distinct() + listOf(Color(0xFF008557), Color(0xFFDB340B), Color(0xFFED9B16)))
        } else {
            CalendarMarker.Dots(colors)
        }
    }

/** A made-up fare per day. */
private fun fareOn(date: LocalDate): Int = 70 + (date.day * 37 + 11) % 110

