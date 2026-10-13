package dev.voir.areca.foundation.avatar

import kotlin.test.Test
import kotlin.test.assertEquals

/** How a name, a username or a pre-made abbreviation is reduced to an avatar's initials. */
class InitialsOfTest {

    @Test
    fun `a full name gives the first letters of its first and last words`() {
        assertEquals("AL", initialsOf("Ada Lovelace"))
    }

    @Test
    fun `middle names are skipped`() {
        assertEquals("AL", initialsOf("Augusta Ada King Lovelace"))
    }

    @Test
    fun `a hyphenated first name counts as one name`() {
        assertEquals("JP", initialsOf("Jean-Luc Picard"))
    }

    @Test
    fun `a single word gives its first two letters`() {
        assertEquals("AD", initialsOf("ada"))
    }

    @Test
    fun `a single letter stays one letter`() {
        assertEquals("A", initialsOf("a"))
    }

    @Test
    fun `surrounding and repeated whitespace is ignored`() {
        assertEquals("AL", initialsOf("  Ada \t\n  Lovelace  "))
    }

    @Test
    fun `punctuation never becomes an initial`() {
        assertEquals("AL", initialsOf("(Ada) \"Lovelace\""))
    }

    @Test
    fun `a word made only of symbols is skipped`() {
        assertEquals("AL", initialsOf("Ada & Lovelace"))
        assertEquals("AD", initialsOf("— Ada"))
    }

    @Test
    fun `digits count as initials`() {
        assertEquals("R2", initialsOf("r2d2"))
    }

    @Test
    fun `a value without letters or digits gives no initials`() {
        assertEquals("", initialsOf("!!! ???"))
        assertEquals("", initialsOf("   "))
        assertEquals("", initialsOf(""))
    }

    @Test
    fun `an initial that uppercases to two letters is cut to one`() {
        assertEquals("SL", initialsOf("ßa Lovelace"))
    }

    @Test
    fun `a single word that uppercases longer is cut to two characters`() {
        assertEquals("SS", initialsOf("ßa"))
    }
}
