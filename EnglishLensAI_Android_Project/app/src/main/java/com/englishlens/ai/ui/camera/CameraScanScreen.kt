package com.englishlens.ai.ui.camera

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import java.nio.ByteBuffer

@Composable
fun CameraScanScreen(
    hasPermission: Boolean,
    onRequestPermission: () -> Unit,
    onImageCaptured: (Bitmap) -> Unit,
    onOpenSessions: () -> Unit,
    onOpenTemplates: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var isFlashEnabled by remember { mutableStateOf(false) }
    var cameraControl by remember { mutableStateOf<CameraControl?>(null) }
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    var isCapturing by remember { mutableStateOf(false) }

    if (!hasPermission) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0F172A))
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = null,
                    tint = Color(0xFF38BDF8),
                    modifier = Modifier.size(64.dp)
                )
                Text(
                    text = "Cần quyền truy cập Camera",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
                Text(
                    text = "Để quét và giải trực tiếp đề bài DET và bài tập tiếng Anh, vui lòng cấp quyền Camera.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF94A3B8)
                )
                Button(
                    onClick = onRequestPermission,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
                ) {
                    Text("Cấp quyền Camera ngay")
                }
            }
        }
        return
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        // CameraX Viewfinder Preview
        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx).apply {
                    scaleType = PreviewView.ScaleType.FIT_CENTER
                }
                val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                    val capture = ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .setTargetRotation(previewView.display?.rotation ?: 0)
                        .build()
                    imageCapture = capture

                    // Prioritize standard Rear Camera (1.0x wide-angle)
                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                    try {
                        cameraProvider.unbindAll()
                        val camera = cameraProvider.bindToLifecycle(
                            lifecycleOwner, cameraSelector, preview, capture
                        )
                        cameraControl = camera.cameraControl

                        // Set zoom to 1.0x
                        cameraControl?.setLinearZoom(0f)
                    } catch (exc: Exception) {
                        exc.printStackTrace()
                    }
                }, ContextCompat.getMainExecutor(ctx))
                previewView
            },
            modifier = Modifier.fillMaxSize()
        )

        // Viewfinder Framing Guides
        Box(
            modifier = Modifier
                .fillMaxWidth(0.88f)
                .fillMaxHeight(0.65f)
                .align(Alignment.Center)
                .border(2.dp, Color(0xFF38BDF8), RoundedCornerShape(16.dp))
        )

        // Top bar: App info & Flash
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = Color(0x990F172A),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = "ENGLISH LENS AI • 1x Chuẩn",
                    color = Color.White,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }

            IconButton(
                onClick = {
                    isFlashEnabled = !isFlashEnabled
                    cameraControl?.enableTorch(isFlashEnabled)
                },
                modifier = Modifier.background(Color(0x990F172A), CircleShape)
            ) {
                Icon(
                    imageVector = if (isFlashEnabled) Icons.Default.FlashOn else Icons.Default.FlashOff,
                    contentDescription = "Flash",
                    tint = if (isFlashEnabled) Color(0xFFFBBF24) else Color.White
                )
            }
        }

        // Bottom Controls: Templates, Capture, Sessions
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 32.dp, start = 24.dp, end = 24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onOpenTemplates,
                modifier = Modifier.size(52.dp).background(Color(0x990F172A), CircleShape)
            ) {
                Icon(Icons.Default.Book, "Templates", tint = Color.White)
            }

            // Real Shutter Button with ImageCapture execution
            Surface(
                onClick = {
                    if (isCapturing || imageCapture == null) return@Surface
                    isCapturing = true

                    val executor = ContextCompat.getMainExecutor(context)
                    imageCapture?.takePicture(
                        executor,
                        object : ImageCapture.OnImageCapturedCallback() {
                            override fun onCaptureSuccess(imageProxy: ImageProxy) {
                                try {
                                    val bitmap = imageProxyToBitmap(imageProxy)
                                    onImageCaptured(bitmap)
                                } finally {
                                    imageProxy.close()
                                    isCapturing = false
                                }
                            }

                            override fun onError(exception: ImageCaptureException) {
                                exception.printStackTrace()
                                isCapturing = false
                            }
                        }
                    )
                },
                modifier = Modifier.size(80.dp),
                shape = CircleShape,
                color = if (isCapturing) Color.LightGray else Color.White,
                shadowElevation = 8.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .border(3.dp, Color(0xFF2563EB), CircleShape)
                    )
                    if (isCapturing) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(36.dp),
                            color = Color(0xFF2563EB),
                            strokeWidth = 3.dp
                        )
                    }
                }
            }

            IconButton(
                onClick = onOpenSessions,
                modifier = Modifier.size(52.dp).background(Color(0x990F172A), CircleShape)
            ) {
                Icon(Icons.Default.Layers, "Sessions", tint = Color.White)
            }
        }
    }
}

// Convert ImageProxy to Bitmap with correct rotation
private fun imageProxyToBitmap(imageProxy: ImageProxy): Bitmap {
    val buffer: ByteBuffer = imageProxy.planes[0].buffer
    val bytes = ByteArray(buffer.remaining())
    buffer.get(bytes)
    val original = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

    val rotationDegrees = imageProxy.imageInfo.rotationDegrees
    return if (rotationDegrees != 0 && original != null) {
        val matrix = Matrix().apply { postRotate(rotationDegrees.toFloat()) }
        Bitmap.createBitmap(original, 0, 0, original.width, original.height, matrix, true)
    } else {
        original ?: Bitmap.createBitmap(640, 480, Bitmap.Config.ARGB_8888)
    }
}
