package com.englishlens.ai.ui.det

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class DetBlank(
    val id: Int,
    val prefix: String,
    val missingBoxesCount: Int,
    val targetWord: String,
    val missingLetters: String,
    val alternative: String?,
    val options: List<String>,
    val contextSentence: String,
    var selectedOption: String? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetReadCompleteScreen(
    blanks: List<DetBlank>,
    onScanNext: () -> Unit
) {
    var showAnswers by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("DET Read & Complete", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2563EB),
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            Surface(shadowElevation = 8.dp, color = Color(0xFF0F172A)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { showAnswers = !showAnswers },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF334155))
                    ) {
                        Text(if (showAnswers) "Ẩn đáp án" else "Xem đáp án chuẩn")
                    }
                    Button(
                        onClick = onScanNext,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
                    ) {
                        Text("Chụp tiếp")
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(blanks) { blank ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = blank.contextSentence,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color(0xFFE2E8F0)
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        // Visual Missing-Letter Boxes
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = blank.prefix,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                color = Color.White
                            )
                            repeat(blank.missingBoxesCount) { idx ->
                                Box(
                                    modifier = Modifier
                                        .size(32.dp, 40.dp)
                                        .border(2.dp, Color(0xFF38BDF8), RoundedCornerShape(6.dp))
                                        .background(Color(0xFF0F172A)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (showAnswers) {
                                        val char = blank.missingLetters.getOrNull(idx)?.toString() ?: ""
                                        Text(char, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF34D399))
                                    } else {
                                        Text("_", color = Color(0xFF64748B), fontSize = 18.sp)
                                    }
                                }
                            }
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "(${blank.missingBoxesCount} ô)",
                                color = Color(0xFF94A3B8),
                                fontSize = 12.sp
                            )
                        }

                        // Randomized Selective Options
                        if (blank.options.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(14.dp))
                            Text("Lựa chọn phương án:", color = Color(0xFF94A3B8), fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                blank.options.forEach { opt ->
                                    val isSelected = blank.selectedOption == opt
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (isSelected) Color(0xFF2563EB) else Color(0xFF334155),
                                        modifier = Modifier.clickable {
                                            blank.selectedOption = opt
                                        }
                                    ) {
                                        Text(
                                            text = "${blank.prefix}$opt",
                                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                            color = Color.White,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
