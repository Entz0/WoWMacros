package com.example.wowmacros

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.material.icons.filled.Edit

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

            val testedCount = uiState.macros.count { it.isTested }
            val totalCount = uiState.macros.size
            Text(
                text = "$testedCount av $totalCount makron testade",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (uiState.macros.isEmpty()) {
                EmptyMacroState(modifier = Modifier.weight(1f))
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = uiState.macros,
                        key = { it.id }
                    ) { macro ->
                        MacroItemCard(
                            macro = macro,
                            onToggleTested = { viewModel.toggleIsTested(macro.id) },
                            onEdit = { viewModel.startEditing(macro) },
                            onDelete = { viewModel.deleteMacro(macro.id) }
                        )
                    }
                }
            }
        }
    }
    uiState.editingMacro?.let { macroToEdit ->
        EditMacroDialog(
            macro = macroToEdit,
            onDismiss = { viewModel.cancelEditing() },
            onSave = { name, category, code ->
                viewModel.saveEditedMacro(name, category, code)
            }
        )
    }
}

@Composable
fun MacroItemCard(
    macro: WowMacro,
    onToggleTested: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = macro.isTested,
                onCheckedChange = { onToggleTested() }
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = macro.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        textDecoration = if (macro.isTested) TextDecoration.LineThrough else TextDecoration.None
                    )
                    Text(
                        text = "[${macro.category}]",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = macro.macroCode,
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(onClick = onEdit) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Redigera makro",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Ta bort makro",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun EmptyMacroState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Inga makron sparade ännu.\nFyll i namn och makrokod ovan för att börja!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun EditMacroDialog(
    macro: WowMacro,
    onDismiss: () -> Unit,
    onSave: (name: String, category: String, code: String) -> Unit
) {
    var editedName by remember(macro.id) { mutableStateOf(macro.name) }
    var editedCategory by remember(macro.id) { mutableStateOf(macro.category) }
    var editedCode by remember(macro.id) { mutableStateOf(macro.macroCode) }

    val isValid = editedName.isNotBlank() &&
            editedCode.isNotBlank() &&
            editedCode.length <= 255

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Redigera makro") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = editedName,
                    onValueChange = { editedName = it },
                    label = { Text("Makronamn") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = editedCategory,
                    onValueChange = { editedCategory = it },
                    label = { Text("Kategori") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = editedCode,
                    onValueChange = { editedCode = it },
                    label = { Text("Makrokod") },
                    modifier = Modifier.fillMaxWidth(),
                    supportingText = {
                        val count = editedCode.length
                        Text(
                            text = "$count / 255",
                            color = if (count > 255) {
                                MaterialTheme.colorScheme.error
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )
                    },
                    isError = editedCode.length > 255
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(editedName, editedCategory, editedCode) },
                enabled = isValid
            ) {
                Text("Spara")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Avbryt")
            }
        }
    )
}