package com.englishlens.ai.ui.sessions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionHistoryScreen(onBack: () -> Unit) {
    val sampleSessions = listOf(
        "DET Read & Complete: Perishable Goods (qui____)",
        "DET Read & Complete: History of Coffee (pop____)",
        "Interactive Reading: Vertical Farming (Stage A)"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lịch sử phiên học DET", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0F172A),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(sampleSessions) { sessionTitle ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(Icons.Default.Layers, contentDescription = null, tint = Color(0xFF38BDF8))
                        Column {
                            Text(sessionTitle, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("Phiên học đã lưu trong Room Database", color = Color(0xFF94A3B8), style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}
