package com.dynamite.proyectox.presentation.tables // Ajusta el paquete si es necesario, por ejemplo a com.dynamite.proyectox.ui.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dynamite.proyectox.domain.model.Table // Asegúrate que TableViewModel está accesible o pásalo como parámetro

@Composable
fun TablesScreen(
    tableViewModel: TableViewModel // Asume que TableViewModel está en el mismo paquete o importado
) {
    val tables by tableViewModel.tables.collectAsState()

    if (tables.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Cargando mesas o no hay mesas disponibles...")
        }
    } else {
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(tables) { table ->
                TableItem(table = table)
            }
        }
    }
}

@Composable
fun TableItem(table: Table) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = "Mesa ID: ${table.id}", fontSize = 18.sp)
        Text(text = "Número: ${table.number}", fontSize = 16.sp)
        Text(text = "Estado: ${table.status}", fontSize = 14.sp)
    }
}