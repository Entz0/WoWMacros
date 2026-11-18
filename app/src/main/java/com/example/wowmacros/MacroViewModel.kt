package com.example.wowmacros

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MacroViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MacroUiState())

    val uiState: StateFlow<MacroUiState> = _uiState.asStateFlow()

    fun updateNewMacroName(name: String) {
        _uiState.update { it.copy(newMacroName = name) }
    }

    fun updateNewMacroCode(code: String) {
        _uiState.update { it.copy(newMacroCode = code) }
    }

    fun updateNewMacroCategory(category: String) {
        _uiState.update { it.copy(newMacroCategory = category) }
    }

    fun addMacro() {
        val currentState = _uiState.value

        // Validering
        if (currentState.newMacroName.isNotBlank() && currentState.newMacroCode.isNotBlank()) {
            val newMacro = WowMacro(
                name = currentState.newMacroName,
                macroCode = currentState.newMacroCode,
                category = currentState.newMacroCategory
            )

            _uiState.update { state ->
                state.copy(
                    macros = state.macros + newMacro,
                    newMacroName = "",
                    newMacroCode = "",
                    newMacroCategory = "PvE"
                )
            }
        }
    }

    // Markera en rad som klar/oklar
    fun toggleIsTested(macroId: String) {
        _uiState.update { state ->
            val updatedMacros = state.macros.map { macro ->
                if (macro.id == macroId) macro.copy(isTested = !macro.isTested) else macro
            }
            state.copy(macros = updatedMacros)
        }
    }

    // Ta bort vald rad
    fun deleteMacro(macroId: String) {
        _uiState.update { state ->
            state.copy(macros = state.macros.filter { it.id != macroId })
        }
    }
}