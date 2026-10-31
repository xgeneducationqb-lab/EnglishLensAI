package com.englishlens.ai.ui.camera

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Zap
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.englishlens.ai.mlkit.MlKitTextRecognizer
import com.englishlens.ai.ui.det.DetBlank
import kotlinx.coroutines.launch

@Composable
fun PhotoReviewScreen(
    bitmap: Bitmap,
    onRetake: () -> Unit,
    onCropConfirmed: (Bitmap) -> Unit,
    onAnalyze: (List<DetBlank>) -> Unit
) {
    val scope = rememberCoroutineScope()
    var isAnalyzing by remember { mutableStateOf(false) }
    var statusText by remember { mutableStateOf("Xem lại ảnh trước khi gửi AI") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF020617))
    ) {
        // High-resolution Captured Photo Preview
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Captured exercise",
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 100.dp, top = 40.dp),
            contentScale = ContentScale.Fit
        )

        // Status / Banner
        Surface(
            color = Color(0xCC0F172A),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(16.dp)
        ) {
            Text(
                text = statusText,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        // Bottom Action Bar: Chụp lại, Cắt ảnh, Phân tích
        Surface(
            color = Color(0xF00F172A),
            shadowElevation = 12.dp,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // 1. Chụp lại (Retake)
                OutlinedButton(
                    onClick = onRetake,
                    enabled = !isAnalyzing,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Chụp lại")
                }

                // 2. Cắt ảnh (Crop / Bounds)
                Button(
                    onClick = {
                        // Crops center 75% region of exercise text
                        val startX = (bitmap.width * 0.1).toInt().coerceAtLeast(0)
                        val startY = (bitmap.height * 0.2).toInt().coerceAtLeast(0)
                        val width = (bitmap.width * 0.8).toInt().coerceAtMost(bitmap.width - startX)
                        val height = (bitmap.height * 0.6).toInt().coerceAtMost(bitmap.height - startY)
                        if (width > 50 && height > 50) {
                            val cropped = Bitmap.createBitmap(bitmap, startX, startY, width, height)
                            onCropConfirmed(cropped)
                            statusText = "Đã cắt tập trung vào vùng chữ đề thi"
                        }
                    },
                    enabled = !isAnalyzing,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF334155))
                ) {
                    Icon(Icons.Default.Crop, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Cắt ảnh")
                }

                // 3. Phân tích (Analyze via ML Kit + Gemini)
                Button(
                    onClick = {
                        isAnalyzing = true
                        statusText = "Đang chạy ML Kit OCR và phân tích ô trống..."
                        scope.launch {
                            try {
                                val ocrText = MlKitTextRecognizer.recognizeText(bitmap)
                                statusText = "OCR hoàn tất! Đang giải câu hỏi DET..."
                                // Extract blanks and target word
                                val result = MlKitTextRecognizer.extractDetBlanks(ocrText)
                                onAnalyze(result)
                            } catch (e: Exception) {
                                e.printStackTrace()
                                statusText = "Lỗi xử lý: ${e.message}"
                            } finally {
                                isAnalyzing = false
                            }
                        }
                    },
                    enabled = !isAnalyzing,
                    modifier = Modifier.weight(1.3f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF59E0B))
                ) {
                    if (isAnalyzing) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = Color(0xFF0F172A),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(Icons.Default.Zap, contentDescription = null, tint = Color(0xFF0F172A), modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Phân tích", color = Color(0xFF0F172A), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
