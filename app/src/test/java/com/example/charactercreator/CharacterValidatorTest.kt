package com.example.charactercreator

import org.junit.Assert.*
import org.junit.Test

class CharacterValidatorTest {

    @Test
    fun nameMustContainBetween3And10Letters() {
        assertFalse(CharacterValidator.isValidName("Al"))
        assertTrue(CharacterValidator.isValidName("Ala"))
        assertTrue(CharacterValidator.isValidName("Aleksandra"))
        assertFalse(CharacterValidator.isValidName("AleksandraX"))
    }

    @Test
    fun nameMustNotContainSpecialCharacters() {
        assertFalse(CharacterValidator.isValidName("Ala123"))
        assertFalse(CharacterValidator.isValidName("Al@"))
        assertFalse(CharacterValidator.isValidName("Ala Nowak"))
        assertTrue(CharacterValidator.isValidName("Łukasz"))
    }

    @Test
    fun statsAreSummedCorrectly() {
        assertEquals(
            10,
            CharacterValidator.totalStats(listOf(2, 2, 2, 2, 2))
        )
        assertEquals(
            15,
            CharacterValidator.totalStats(listOf(3, 3, 3, 3, 3))
        )
    }

    @Test
    fun statsMustNotExceedBudget() {
        assertTrue(
            CharacterValidator.isWithinBudget(listOf(2, 2, 2, 2, 2))
        )
        assertTrue(
            CharacterValidator.isWithinBudget(listOf(10, 0, 0, 0, 0))
        )
        assertFalse(
            CharacterValidator.isWithinBudget(listOf(3, 3, 3, 3, 3))
        )
    }

    @Test
    fun statsCannotBeNegativeOrAboveTen() {
        assertFalse(
            CharacterValidator.isWithinBudget(listOf(-1, 2, 2, 2, 2))
        )
        assertFalse(
            CharacterValidator.isWithinBudget(listOf(11, 0, 0, 0, 0))
        )
    }
}