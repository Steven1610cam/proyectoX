package com.dynamite.proyectox.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dynamite.proyectox.common.Resource
import com.dynamite.proyectox.domain.model.Table

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel() // Inyecta el HomeViewModel
) {
    val tablesState = viewModel.tablesState.value

    Box(modifier = Modifier.fillMaxSize()) {
        when (tablesState) {
            is Resource.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is Resource.Error -> {
                Text(
                    text = tablesState.message ?: "Error desconocido",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            is Resource.Success -> {
                val tables = tablesState.data
                if (tables.isNullOrEmpty()) {
                    Text(
                        text = "No hay mesas disponibles.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item { // Para el título
                            Text(
                                "Mesas Disponibles",
                                style = MaterialTheme.typography.headlineMedium,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                        items(tables) { table ->
                            TableItem(table = table)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TableItem(table: Table) {
    // Diseño simple para cada mesa, puedes expandirlo luego
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(text = "Mesa ${table.number}", style = MaterialTheme.typography.bodyLarge) // CORREGIDO
        Spacer(modifier = Modifier.weight(1f))
        Text(text = table.status.name, style = MaterialTheme.typography.bodyMedium)
    }
}
