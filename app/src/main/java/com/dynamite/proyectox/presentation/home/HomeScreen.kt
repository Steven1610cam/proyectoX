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
import androidx.compose.material.icons.filled.Visibility
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
import androidx.navigation.NavController // IMPORT AÑADIDO
import com.dynamite.proyectox.Screen // IMPORT AÑADIDO
import com.dynamite.proyectox.common.Resource
import com.dynamite.proyectox.domain.model.Table
import com.dynamite.proyectox.domain.model.TableStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController, // PARÁMETRO AÑADIDO
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
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
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
                        Text(
                            text = "No hay mesas. ¡Añade una!",
                            modifier = Modifier.align(Alignment.Center),
                            textAlign = TextAlign.Center
                        )
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(tables) { table ->
                                TableCardItem(
                                    table = table,
                                    onClick = {
                                        // Navegar a OrderScreen con el número de mesa
                                        navController.navigate(Screen.OrderScreen.withArgs(table.number))
                                    }
                                )
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
                Text(
                    "CharlyHot",
                    style = MaterialTheme.typography.headlineSmall,
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
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}

@Composable
fun HomeBottomBar() {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
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
        TableStatus.FREE -> Color(0xFFFFE082)
        TableStatus.OCCUPIED -> Color(0xFFE0E0E0)
        TableStatus.BILL_REQUESTED -> Color(0xFF81D4FA)
    }

    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = onClick // Se llama al lambda proporcionado
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Mesa ${table.number}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
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
