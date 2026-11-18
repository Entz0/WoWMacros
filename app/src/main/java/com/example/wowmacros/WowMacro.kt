package com.example.wowmacros

import java.util.UUID

data class WowMacro(
    val id: String = UUID.randomUUID().toString(), // Unikt ID (viktigt för att kunna redigera/ta bort rätt rad)
    val name: String,                              // Namn på makrot (t.ex. "Focus Kick" eller "Mouseover Throw")
    val category: String,                          // Val/Kategori (t.ex. "PvE", "PvP" eller "Utility")
    val macroCode: String,                         // Själva macrokoden som byggs ihop
    val isTested: Boolean = false                  // Klar/Oklar (Testat i spelet?)
)