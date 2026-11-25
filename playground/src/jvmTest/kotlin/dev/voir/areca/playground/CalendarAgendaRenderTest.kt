package dev.voir.areca.playground

import androidx.compose.animation.core.snap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import dev.voir.areca.window.ProvideWindowClass
import dev.voir.areca.window.WindowClass
import dev.voir.areca.datetime.DateTimeLocale
import dev.voir.areca.datetime.LocalDateTimeLocale
import dev.voir.areca.datetime.calendar.agenda.CalendarAgendaState
import dev.voir.areca.playground.datetime.CalendarAgendaShowcase
import dev.voir.areca.theme.ArecaTheme
import dev.voir.areca.theme.ArecaThemeProvider
import dev.voir.areca.window.InputMode
import kotlinx.datetime.LocalDate
import org.jetbrains.skia.EncodedImageFormat
import java.io.File
import kotlin.test.Test
import kotlin.time.Clock
import kotlin.time.Instant

/** Where the rendered images are written. */
private val AgendaOutput = File(System.getProperty("render.out") ?: "build/screens")

/** A clock pinned to 25 November 2026. */
private val AgendaClock = object : Clock {
    override fun now(): Instant = Instant.parse("2026-11-25T12:00:00Z")
}

/**
 * Renders one composition at a window size to a PNG once it has settled.
 *
 * @param name File name, without the extension.
 * @param width Logical width.
 * @param height Logical height.
 * @param dark Whether to render in the dark theme.
 * @param content Composition to render.
 */
@OptIn(ExperimentalComposeUiApi::class)
private fun renderAgenda(name: String, width: Int = 390, height: Int = 800, dark: Boolean = false, content: @Composable () -> Unit) {
    val density = 2f
    val scene = ImageComposeScene(width = (width * density).toInt(), height = (height * density).toInt(), density = Density(density)) {
        ArecaThemeProvider(darkTheme = dark, inputMode = InputMode.Touch) {
            CompositionLocalProvider(LocalDateTimeLocale provides DateTimeLocale.Iso) {
                Box(Modifier.fillMaxSize().background(ArecaTheme.surface.ground).padding(top = 8.dp)) { content() }
            }
        }
    }
    try {
        var time = 0L
        while (time < 3_000_000_000L) {
            scene.render(time)
            time += 16_000_000L
        }
        val data = requireNotNull(scene.render(time).encodeToData(EncodedImageFormat.PNG))
        AgendaOutput.mkdirs()
        File(AgendaOutput, "$name.png").writeBytes(data.bytes)
    } finally {
        scene.close()
    }
}

/**
 * A planned-payments agenda with its own selection, optionally moved by [setUp] once composed.
 *
 * @param setUp What to do to the state once it is composed.
 */
@Composable
private fun Agenda(setUp: suspend (CalendarAgendaState, (LocalDate) -> Unit) -> Unit = { _, _ -> }) {
    val state = rememberPlannedPaymentsState(clock = AgendaClock)
    var selected by remember { mutableStateOf(LocalDate(2026, 11, 25)) }
    LaunchedEffect(state) { setUp(state) { selected = it } }
    PlannedPaymentsAgenda(state = state, selected = selected, onSelect = { selected = it })
}

/**
 * Renders the agenda open, folded and with an empty day picked, on a phone and a dark phone, to
 * `build/screens`. No golden image: it fails when a configuration throws.
 *
 * ```bash
 * ./gradlew :playground:jvmTest --tests '*CalendarAgendaRenderTest*'
 * ```
 */
class CalendarAgendaRenderTest {

    /** The month open over the first days. */
    @Test
    fun `open month renders`() {
        renderAgenda("agenda-open") { Agenda() }
        renderAgenda("agenda-open-dark", dark = true) { Agenda() }
    }

    /** Folded to the week of a day picked further on, its heading at the top of the list. */
    @Test
    fun `folded week renders`() {
        renderAgenda("agenda-folded") {
            Agenda { state, select ->
                select(LocalDate(2026, 12, 1))
                state.requestScrollToDay(LocalDate(2026, 12, 1))
                state.calendar.fold(animationSpec = snap())
            }
        }
    }

    /** A tablet: the calendar card beside the list card, a day picked further on. */
    @Test
    fun `wide window renders`() {
        renderAgenda("agenda-tablet", width = 1024, height = 768) {
            ProvideWindowClass(WindowClass(DpSize(1024.dp, 768.dp), InputMode.Touch)) {
                Agenda { state, select ->
                    select(LocalDate(2026, 12, 15))
                    state.requestScrollToDay(LocalDate(2026, 12, 15))
                }
            }
        }
    }

    /** The playground page, top to bottom: it must compose with every section. */
    @Test
    fun `showcase page renders`() {
        renderAgenda("agenda-showcase", width = 1100, height = 1400) { CalendarAgendaShowcase() }
    }

    /** A tap on a day with nothing planned: its placeholder group at the top. */
    @Test
    fun `empty day renders`() {
        renderAgenda("agenda-empty-day") {
            Agenda { state, select ->
                select(LocalDate(2026, 12, 9))
                state.requestScrollToDay(LocalDate(2026, 12, 9))
            }
        }
    }
}
