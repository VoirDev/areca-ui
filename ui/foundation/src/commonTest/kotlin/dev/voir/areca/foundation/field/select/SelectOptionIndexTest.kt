package dev.voir.areca.foundation.field.select

import kotlin.test.Test
import kotlin.test.assertEquals

/** Behavior of [indexRuns], which breaks an indexed [SelectOptionSection] under letter headers. */
class SelectOptionIndexTest {

    private fun option(label: String) = SelectOption(key = label, value = label, label = label)

    private fun letters(vararg labels: String): List<Pair<String, List<String>>> =
        indexRuns(labels.map(::option), other = "#").map { run -> run.letter to run.options.map { it.label } }

    @Test
    fun `consecutive labels sharing a first letter form one upper-cased run`() {
        assertEquals(
            listOf("A" to listOf("Acer", "apple"), "B" to listOf("Bosch")),
            letters("Acer", "apple", "Bosch"),
        )
    }

    @Test
    fun `labels that do not start with a letter share the other header`() {
        assertEquals(
            listOf("#" to listOf("3M", "  ", "_Lab"), "L" to listOf("Lenovo")),
            letters("3M", "  ", "_Lab", "Lenovo"),
        )
    }

    @Test
    fun `an unsorted list keeps its order and repeats the letter`() {
        assertEquals(
            listOf("B" to listOf("Bosch"), "A" to listOf("Acer"), "B" to listOf("Braun")),
            letters("Bosch", "Acer", "Braun"),
        )
    }

    @Test
    fun `cyrillic labels are indexed by their own letters`() {
        assertEquals(
            listOf("Д" to listOf("Дрель"), "Ш" to listOf("шуруповёрт")),
            letters("Дрель", "шуруповёрт"),
        )
    }

    @Test
    fun `no options give no runs`() {
        assertEquals(emptyList(), letters())
    }
}
