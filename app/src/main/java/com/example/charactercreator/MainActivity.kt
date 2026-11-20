package com.example.charactercreator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

/*
 * KREATOR POSTACI — SZABLON DLA STUDENTÓW
 *
 * Zadanie:
 * 1. Uzupełnij TODO w modelu, walidatorze i funkcjach pomocniczych.
 * 2. Zaimplementuj stan formularza oraz zapis/edycję/usuwanie postaci.
 * 3. Zachowaj testTagi opisane komentarzami — korzystają z nich testy UI.
 * 4. Nie zmieniaj nazw publicznych klas/funkcji bez aktualizacji testów.
 *
 * TODO: uzupełnij implementacje. Plik może się kompilować, ale testy nie przejdą,
 * dopóki nie zostaną zaimplementowane brakujące fragmenty.
 */

// TODO: uzupełnij model postaci zgodnie z testami.
// Wymagane pola: id (Long), name (String), stats (List<Int>).
data class Character(
    val id: Long,
    val name: String,
    val stats: List<Int>
)

object CharacterValidator {
    // Testy oczekują imienia o długości 3–10 znaków, zawierającego wyłącznie litery.
    fun isValidName(name: String): Boolean {
        TODO("Sprawdź długość i dozwolone znaki imienia")
    }

    fun totalStats(stats: List<Int>): Int {
        TODO("Zwróć sumę statystyk")
    }

    // Testy oczekują wartości każdej statystyki 0..10 i sumy nie większej niż 10.
    fun isWithinBudget(stats: List<Int>): Boolean {
        TODO("Sprawdź zakres statystyk i łączny budżet")
    }
}

// TODO: zaimplementuj kodowanie listy postaci do Stringa, aby stan dało się
// zachować przez rememberSaveable. Możesz użyć formatu id~name~stat1,stat2,...
private fun encodeCharacters(characters: List<Character>): String {
    TODO("Zakoduj listę postaci")
}

// TODO: zaimplementuj odczyt formatu używanego przez encodeCharacters.
private fun decodeCharacters(encoded: String): List<Character> {
    TODO("Odtwórz listę postaci z tekstu")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                // Główna zawartość aplikacji.
                CharacterCreatorScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterCreatorScreen() {
    // TODO: utwórz stan formularza przez rememberSaveable.
    var name by rememberSaveable { mutableStateOf("") }
    var strength by rememberSaveable { mutableIntStateOf(2) }
    var dexterity by rememberSaveable { mutableIntStateOf(2) }
    var intelligence by rememberSaveable { mutableIntStateOf(2) }
    var vitality by rememberSaveable { mutableIntStateOf(2) }
    var luck by rememberSaveable { mutableIntStateOf(2) }

    // TODO: połącz statystyki w listę, oblicz sumę i walidację.
    val stats = listOf(strength, dexterity, intelligence, vitality, luck)
    val total = CharacterValidator.totalStats(stats)
    val validName = CharacterValidator.isValidName(name)
    val withinBudget = CharacterValidator.isWithinBudget(stats)

    // TODO: przechowuj listę postaci jako tekst i odtwarzaj ją przez decodeCharacters.
    var charactersData by rememberSaveable { mutableStateOf("") }
    val characters = decodeCharacters(charactersData)

    var nextId by rememberSaveable { mutableLongStateOf(1L) }
    var editingId by rememberSaveable { mutableStateOf<Long?>(null) }

    val previewColor by animateColorAsState(
        targetValue = if (!withinBudget) Color(0xFFFFCDD2)
        else MaterialTheme.colorScheme.surfaceVariant,
        label = "previewColor"
    )

    // TODO: uzupełnij funkcję aktualizującą pięć wartości statystyk.
    fun updateStats(values: List<Int>) {
        TODO("Przypisz wartości do strength, dexterity, intelligence, vitality i luck")
    }

    // TODO: wyczyść formularz i zakończ tryb edycji.
    fun resetForm() {
        TODO("Wyczyść formularz i ustaw editingId na null")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Kreator Postaci")
                        Text(
                            "Wykorzystane statystyki: $total/10",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                // TEST TAG: testy przewijają tę listę do elementów formularza i kart.
                .testTag("mainList"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        // TEST TAG: karta podglądu.
                        .testTag("previewCard"),
                    colors = CardDefaults.cardColors(containerColor = previewColor)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Podgląd postaci", style = MaterialTheme.typography.titleLarge)
                        Text(
                            text = name.ifBlank { "Nieznana postać" },
                            modifier = Modifier
                                // TEST TAG: nazwa widoczna w podglądzie.
                                .testTag("previewName"),
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text("Siła: $strength · Zręczność: $dexterity")
                        Text("Inteligencja: $intelligence · Witalność: $vitality")
                        Text("Szczęście: $luck")
                        Text(
                            "Suma statystyk: $total/10",
                            modifier = Modifier
                                // TEST TAG: suma punktów w podglądzie.
                                .testTag("previewTotal")
                        )
                        if (!withinBudget) {
                            Text(
                                "Przekroczono limit punktów!",
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier
                                    // TEST TAG: komunikat o przekroczeniu budżetu.
                                    .testTag("budgetError")
                            )
                        }
                    }
                }
            }

            item {
                Text("Konfiguracja", style = MaterialTheme.typography.titleLarge)
            }

            item {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Imię postaci") },
                    singleLine = true,
                    isError = name.isNotEmpty() && !validName,
                    supportingText = {
                        when {
                            name.isEmpty() -> Text("Od 3 do 10 liter")
                            name.length !in 3..10 -> Text("Imię musi mieć od 3 do 10 znaków")
                            !validName -> Text("Dozwolone są tylko litery")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        // TEST TAG: pole nazwy używane w testach UI.
                        .testTag("nameField")
                )
            }

            item {
                Text("Statystyki (łącznie maks. 10 punktów)")
            }

            val statNames = listOf("Siła", "Zręczność", "Inteligencja", "Witalność", "Szczęście")

            items(statNames.size) { index ->
                Column {
                    Text("${statNames[index]}: ${stats[index]}")
                    Slider(
                        value = stats[index].toFloat(),
                        onValueChange = { value ->
                            // TODO: zaktualizuj wybraną statystykę i zachowaj pozostałe.
                            val updated = stats.toMutableList()
                            updated[index] = value.toInt().coerceIn(0, 10)
                            updateStats(updated)
                        },
                        valueRange = 0f..10f,
                        steps = 9,
                        modifier = Modifier
                            // TEST TAG: statSlider0 ... statSlider4, według indeksu.
                            .testTag("statSlider$index")
                    )
                }
            }

            item {
                Button(
                    onClick = {
                        // TODO: jeśli trwa edycja, zachowaj editingId; w przeciwnym razie
                        // przydziel nextId. Zaktualizuj charactersData i wyczyść formularz.
                        val id = editingId ?: nextId++
                        val character = Character(id, name.trim(), stats)
                        val updated = characters.filterNot { it.id == id } + character
                        charactersData = encodeCharacters(updated)
                        resetForm()
                    },
                    enabled = validName && withinBudget,
                    modifier = Modifier
                        .fillMaxWidth()
                        // TEST TAG: przycisk dodawania/zapisywania zmian.
                        .testTag("saveButton")
                ) {
                    Text(if (editingId == null) "Zapisz postać" else "Zapisz zmiany")
                }
            }

            item {
                Text("Zapisane postacie", style = MaterialTheme.typography.titleLarge)
            }

            if (characters.isEmpty()) {
                item { Text("Brak zapisanych postaci") }
            }

            items(characters, key = { it.id }) { character ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        // TEST TAG: karta postaci, np. character_1.
                        .testTag("character_${character.id}")
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            character.name,
                            modifier = Modifier
                                // TEST TAG: nazwa zapisanej postaci, np. characterName_1.
                                .testTag("characterName_${character.id}"),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text("Suma statystyk: ${character.stats.sum()}")
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            TextButton(
                                onClick = {
                                    // TODO: wczytaj postać do formularza i ustaw editingId.
                                    name = character.name
                                    updateStats(character.stats)
                                    editingId = character.id
                                },
                                modifier = Modifier
                                    // TEST TAG: przycisk edycji, np. edit_1.
                                    .testTag("edit_${character.id}")
                            ) {
                                Text("Edytuj")
                            }
                            TextButton(
                                onClick = {
                                    // TODO: usuń postać o tym ID z charactersData.
                                    charactersData = encodeCharacters(
                                        characters.filterNot { it.id == character.id }
                                    )
                                    if (editingId == character.id) resetForm()
                                },
                                modifier = Modifier
                                    // TEST TAG: przycisk usuwania, np. delete_1.
                                    .testTag("delete_${character.id}")
                            ) {
                                Text("Usuń")
                            }
                        }
                    }
                }
            }
        }
    }
}
