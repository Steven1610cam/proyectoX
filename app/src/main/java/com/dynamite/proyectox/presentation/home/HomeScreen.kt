package com.dynamite.proyectox.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Visibility // Icono de ejemplo para el ojo
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dynamite.proyectox.common.Resource
import com.dynamite.proyectox.domain.model.Table
import com.dynamite.proyectox.domain.model.TableStatus

@OptIn(ExperimentalMaterial3Api::class) // Necesario para Scaffold y otros componentes de M3
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val tablesState = viewModel.tablesState.value

    Scaffold(
        topBar = {
            HomeTopAppBar()
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.addNewTable() }) {
                Icon(Icons.Filled.Add, contentDescription = "Añadir Mesa")
            }
        },
        bottomBar = {
            HomeBottomBar()
        }
    ) { innerPadding -> // Padding proporcionado por Scaffold para el contenido
        Box(
            modifier = Modifier
                .padding(innerPadding) // Aplicar el padding para evitar solapamiento
                .fillMaxSize()
        ) {
            when (tablesState) {
                is Resource.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is Resource.Error -> {
                    Text(
                        text = tablesState.message ?: "Error desconocido",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center),
                        textAlign = TextAlign.Center
                    )
                }
                is Resource.Success -> {
                    val tables = tablesState.data
                    if (tables.isNullOrEmpty()) {
                        // Este caso es menos probable ahora que siempre hay al menos una mesa
                        Text(
                            text = "No hay mesas. ¡Añade una!",
                            modifier = Modifier.align(Alignment.Center),
                            textAlign = TextAlign.Center
                        )
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp), // Espacio alrededor de la cuadrícula
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(tables) { table ->
                                TableCardItem(table = table, onClick = {
                                    // TODO: Implementar acción al hacer clic en una mesa
                                    println("Mesa ${table.number} clickeada")
                                })
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar() {
    TopAppBar(
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // TODO: Considerar usar una imagen para el logo "CharlyHot"
                Text(
                    "CharlyHot",
                    style = MaterialTheme.typography.headlineSmall, // Un tamaño más prominente
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Selecciona tu mesa",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        actions = {
            IconButton(onClick = { /* TODO: Implementar acción del ojo */ }) {
                Icon(Icons.Filled.Visibility, contentDescription = "Visualizar")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors( // Colores de ejemplo
            containerColor = MaterialTheme.colorScheme.surfaceVariant, // Un color sutil para el fondo
            titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
        // Puedes añadir un Modifier.height() si necesitas un TopAppBar más alto
    )
}

@Composable
fun HomeBottomBar() {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.surfaceVariant, // Coherente con el TopAppBar
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Los puntos naranjas pueden ser emojis o pequeños Box con fondo naranja y forma de círculo
            Text("🔸 Comida rápida de calidad 🔸", style = MaterialTheme.typography.labelMedium)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TableCardItem(
    table: Table,
    onClick: () -> Unit
) {
    val cardColor = when (table.status) {
        TableStatus.FREE -> Color(0xFFFFE082) // Un amarillo claro, similar al de la imagen
        TableStatus.OCCUPIED -> Color(0xFFE0E0E0) // Un gris para ocupado
        TableStatus.BILL_REQUESTED -> Color(0xFF81D4FA) // Un azul para cuenta pedida
    }

    Card(
        modifier = Modifier
            .aspectRatio(1f) // Para que sea cuadrado
            .clip(RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp), // Padding interno para los elementos
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Mesa ${table.number}", // Ajustado para que sea "Mesa X"
                    fontSize = 20.sp, // Tamaño ajustado para prominencia
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray // Un color que contraste con el amarillo
                )
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .background(Color.White, CircleShape)
                )
            }
        }
    }
}
