package com.example.wowmacros

data class MacroUiState(
    // Listan med alla sparade makron
    val macros: List<WowMacro> = emptyList(),

    // Tillstånd för textfälten när användaren skapar ett nytt makro
    val newMacroName: String = "",
    val newMacroCode: String = "",
    val newMacroCategory: String = "PvE", // Standardval för kategori
    val editingMacro: WowMacro? = null
)