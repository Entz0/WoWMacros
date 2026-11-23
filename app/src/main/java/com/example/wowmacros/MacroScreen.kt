package com.example.wowmacros

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MacroScreen(viewModel: MacroViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MacroForge") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = uiState.newMacroName,
                onValueChange = { viewModel.updateNewMacroName(it) },
                label = { Text("Makronamn (t.ex. Focus Fear)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.newMacroCategory,
                onValueChange = { viewModel.updateNewMacroCategory(it) },
                label = { Text("Kategori (t.ex. PvE eller PvP)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.newMacroCode,
                onValueChange = { viewModel.updateNewMacroCode(it) },
                label = { Text("Makrokod (t.ex. /cast Fear)") },
                modifier = Modifier.fillMaxWidth(),
                supportingText = {
                    val count = uiState.newMacroCode.length
                    Text(
                        text = "$count / 255",
                        // Blir röd om man går över WoWs maxgräns på 255 tecken
                        color = if (count > 255) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                isError = uiState.newMacroCode.length > 255
            )

            // Knappar för att snabbt lägga till logik
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = {
                    viewModel.updateNewMacroCode(uiState.newMacroCode + "[@mouseover, exists] ")
                }) {
                    Text("+ Lägg till @mouseover")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { viewModel.addMacro() },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState.newMacroName.isNotBlank() && uiState.newMacroCode.isNotBlank() && uiState.newMacroCode.length <= 255
            ) {
                Text("Spara Makro i listan")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Listan med sparade makron kommer här...", style = MaterialTheme.typography.bodyLarge)
        }
    }
}