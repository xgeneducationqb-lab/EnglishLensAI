package com.englishlens.ai.ui.navigation

import android.graphics.Bitmap
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.englishlens.ai.ui.camera.CameraScanScreen
import com.englishlens.ai.ui.camera.PhotoReviewScreen
import com.englishlens.ai.ui.det.DetBlank
import com.englishlens.ai.ui.det.DetReadCompleteScreen
import com.englishlens.ai.ui.sessions.SessionHistoryScreen
import com.englishlens.ai.ui.templates.TemplateScreen

@Composable
fun AppNavigation(
    hasCameraPermission: Boolean,
    onRequestCameraPermission: () -> Unit
) {
    val navController = rememberNavController()
    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var detectedBlanks by remember {
        mutableStateOf(
            listOf(
                DetBlank(
                    id = 1,
                    prefix = "qui",
                    missingBoxesCount = 4,
                    targetWord = "quickly",
                    missingLetters = "ckly",
                    alternative = "etly",
                    options = listOf("ckly", "etly"),
                    contextSentence = "To prevent them from spoiling, perishable goods must be qui____ transported from the production site to retail outlets."
                )
            )
        )
    }

    NavHost(navController = navController, startDestination = "camera") {
        composable("camera") {
            CameraScanScreen(
                hasPermission = hasCameraPermission,
                onRequestPermission = onRequestCameraPermission,
                onImageCaptured = { bitmap ->
                    capturedBitmap = bitmap
                    navController.navigate("review")
                },
                onOpenSessions = { navController.navigate("sessions") },
                onOpenTemplates = { navController.navigate("templates") }
            )
        }

        composable("review") {
            capturedBitmap?.let { bitmap ->
                PhotoReviewScreen(
                    bitmap = bitmap,
                    onRetake = {
                        capturedBitmap = null
                        navController.popBackStack()
                    },
                    onCropConfirmed = { cropped ->
                        capturedBitmap = cropped
                    },
                    onAnalyze = { processedBlanks ->
                        if (processedBlanks.isNotEmpty()) {
                            detectedBlanks = processedBlanks
                        }
                        navController.navigate("det_result")
                    }
                )
            }
        }

        composable("det_result") {
            DetReadCompleteScreen(
                blanks = detectedBlanks,
                onScanNext = {
                    capturedBitmap = null
                    navController.navigate("camera") {
                        popUpTo("camera") { inclusive = true }
                    }
                }
            )
        }

        composable("sessions") {
            SessionHistoryScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable("templates") {
            TemplateScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
