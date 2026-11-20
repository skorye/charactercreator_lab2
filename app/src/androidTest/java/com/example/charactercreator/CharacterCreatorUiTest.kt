package com.example.charactercreator

import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.assert
import org.junit.Rule
import org.junit.Test

class CharacterCreatorUiTest {

    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    private fun scrollToSaveButton() {
        rule.onNodeWithTag("mainList")
            .performScrollToNode(
                androidx.compose.ui.test.hasTestTag("saveButton")
            )
    }

    @Test
    fun saveButtonIsDisabledForInvalidName() {
        scrollToSaveButton()
        rule.onNodeWithTag("saveButton").assertIsNotEnabled()

        rule.onNodeWithTag("nameField").performTextInput("Al")
        rule.onNodeWithTag("saveButton").assertIsNotEnabled()

        rule.onNodeWithTag("nameField").performTextClearance()
        rule.onNodeWithTag("nameField").performTextInput("Ala")
        rule.onNodeWithTag("saveButton").assertIsEnabled()
    }

    @Test
    fun previewUpdatesWhenNameChanges() {
        rule.onNodeWithTag("nameField").performTextInput("Ala")

        rule.onNodeWithTag("previewName")
            .assertTextEquals("Ala")
    }

    @Test
    fun validCharacterCanBeSaved() {
        scrollToSaveButton()
        rule.onNodeWithTag("nameField").performTextInput("Ala")
        rule.onNodeWithTag("saveButton").performClick()

        rule.onNodeWithTag("characterName_1")
            .assertTextEquals("Ala")
    }

    @Test
    fun savedCharacterCanBeEdited() {
        // 1. Wpisz imię pierwszej postaci
        rule.onNodeWithTag("nameField")
            .performTextInput("Ala")

        // 2. Przewiń do przycisku zapisu i zapisz postać
        scrollToSaveButton()
        rule.onNodeWithTag("saveButton")
            .performClick()

        // 3. Potwierdź, że postać została zapisana
        rule.onNodeWithTag("mainList")
            .performScrollToNode(hasTestTag("characterName_1"))

        rule.onNodeWithTag("characterName_1")
            .assertTextEquals("Ala")

        // 4. Kliknij Edytuj
        rule.onNodeWithTag("mainList")
            .performScrollToNode(hasTestTag("edit_1"))

        rule.onNodeWithTag("edit_1")
            .performClick()

        // 5. Przewiń do formularza i zmień imię
        rule.onNodeWithTag("mainList")
            .performScrollToNode(hasTestTag("nameField"))

        rule.onNodeWithTag("nameField")
            .performTextClearance()

        rule.onNodeWithTag("nameField")
            .performTextInput("Ewa")

        // 6. Przewiń do przycisku zapisu zmian
        scrollToSaveButton()

        // Upewnij się, że przycisk jest dostępny
        rule.onNodeWithTag("saveButton")
            .assertIsEnabled()
            .performClick()

        // 7. Sprawdź, czy istniejąca postać została zaktualizowana
        rule.onNodeWithTag("mainList")
            .performScrollToNode(hasTestTag("characterName_1"))

        rule.onNodeWithTag("characterName_1")
            .assertTextEquals("Ewa")

        // 8. Upewnij się, że nie utworzono drugiej postaci
        rule.onNodeWithTag("character_1")
            .assertExists()

        rule.onNodeWithTag("character_2")
            .assertDoesNotExist()
    }

    @Test
    fun savedCharacterCanBeDeleted() {
        scrollToSaveButton()
        // Zapisz postać
        rule.onNodeWithTag("nameField")
            .performTextInput("Ewa")

        scrollToSaveButton()

        rule.onNodeWithTag("saveButton")
            .performClick()

        // Przewiń do przycisku usuwania
        rule.onNodeWithTag("mainList")
            .performScrollToNode(hasTestTag("delete_1"))

        rule.onNodeWithTag("delete_1")
            .performClick()

        // Sprawdź, czy postać została usunięta
        rule.onNodeWithTag("character_1")
            .assertDoesNotExist()
    }

    @Test
    fun formAndCharactersSurviveActivityRecreation() {
        scrollToSaveButton()
        rule.onNodeWithTag("nameField").performTextInput("Ala")
        rule.onNodeWithTag("saveButton").performClick()

        // Rozpocznij tworzenie kolejnej postaci.
        rule.onNodeWithTag("nameField").performTextInput("Ewa")

        rule.activityRule.scenario.recreate()
        rule.waitForIdle()

        // Lista zapisanych postaci została zachowana.
        rule.onNodeWithTag("characterName_1")
            .assertTextEquals("Ala")

        // Niezapisany formularz również został zachowany.
        rule.onNodeWithTag("nameField")
            .assert(hasText("Ewa"))
    }
}