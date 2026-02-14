package com.crossline.app.ui.diet

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

/**
 * Pivot Camera screen for body-check photos.
 * Features:
 * - User-defined reference lines (horizontal/vertical grid)
 * - Ghost overlay of previous photo for alignment comparison
 * - CameraX preview placeholder (actual camera binding requires Activity context)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PivotCameraScreen(
    onBack: () -> Unit
) {
    var showGrid by remember { mutableStateOf(true) }
    var showGhostOverlay by remember { mutableStateOf(false) }
    var horizontalLines by remember { mutableIntStateOf(3) }
    var verticalLines by remember { mutableIntStateOf(3) }
    var ghostAlpha by remember { mutableFloatStateOf(0.3f) }
    var viewSize by remember { mutableStateOf(IntSize.Zero) }

    // Draggable reference line offset (user can drag to reposition center)
    var centerOffsetX by remember { mutableFloatStateOf(0.5f) }
    var centerOffsetY by remember { mutableFloatStateOf(0.5f) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("눈바디 카메라", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로")
                    }
                },
                actions = {
                    IconToggleButton(checked = showGrid, onCheckedChange = { showGrid = it }) {
                        Icon(
                            Icons.Default.GridOn,
                            contentDescription = "기준선",
                            tint = if (showGrid) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                        )
                    }
                    IconToggleButton(
                        checked = showGhostOverlay,
                        onCheckedChange = { showGhostOverlay = it }
                    ) {
                        Icon(
                            Icons.Default.Layers,
                            contentDescription = "고스트 오버레이",
                            tint = if (showGhostOverlay) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Camera preview area with grid overlay
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Color.Black)
                    .onSizeChanged { viewSize = it }
                    .pointerInput(Unit) {
                        detectDragGestures { change, _ ->
                            change.consume()
                            if (viewSize.width > 0 && viewSize.height > 0) {
                                centerOffsetX = (change.position.x / viewSize.width).coerceIn(0f, 1f)
                                centerOffsetY = (change.position.y / viewSize.height).coerceIn(0f, 1f)
                            }
                        }
                    }
            ) {
                // Camera preview placeholder
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.CameraAlt,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.3f),
                            modifier = Modifier.size(64.dp)
                        )
                        Text(
                            "CameraX 프리뷰",
                            color = Color.White.copy(alpha = 0.3f),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                // Ghost overlay (previous photo)
                if (showGhostOverlay) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White.copy(alpha = ghostAlpha * 0.1f))
                    ) {
                        // Actual ghost image will be loaded from latest BodyCheck photoUri
                        Text(
                            "이전 사진 오버레이",
                            color = Color.White.copy(alpha = ghostAlpha),
                            modifier = Modifier.align(Alignment.Center),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                // Reference grid lines
                if (showGrid) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawReferenceGrid(
                            horizontalCount = horizontalLines,
                            verticalCount = verticalLines,
                            centerX = centerOffsetX,
                            centerY = centerOffsetY
                        )
                    }
                }
            }

            // Controls panel
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Grid line count controls
                if (showGrid) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("가로선: $horizontalLines", style = MaterialTheme.typography.bodySmall)
                        Row {
                            IconButton(
                                onClick = { if (horizontalLines > 1) horizontalLines-- },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(Icons.Default.RemoveCircleOutline, contentDescription = "감소",
                                    modifier = Modifier.size(18.dp))
                            }
                            IconButton(
                                onClick = { if (horizontalLines < 10) horizontalLines++ },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(Icons.Default.AddCircleOutline, contentDescription = "증가",
                                    modifier = Modifier.size(18.dp))
                            }
                        }
                        Text("세로선: $verticalLines", style = MaterialTheme.typography.bodySmall)
                        Row {
                            IconButton(
                                onClick = { if (verticalLines > 1) verticalLines-- },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(Icons.Default.RemoveCircleOutline, contentDescription = "감소",
                                    modifier = Modifier.size(18.dp))
                            }
                            IconButton(
                                onClick = { if (verticalLines < 10) verticalLines++ },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(Icons.Default.AddCircleOutline, contentDescription = "증가",
                                    modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }

                // Ghost overlay alpha slider
                if (showGhostOverlay) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("투명도", style = MaterialTheme.typography.bodySmall)
                        Slider(
                            value = ghostAlpha,
                            onValueChange = { ghostAlpha = it },
                            valueRange = 0.1f..0.8f,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Shutter button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(
                        onClick = { /* CameraX capture - to be wired with actual camera */ },
                        modifier = Modifier.size(64.dp)
                    ) {
                        Icon(
                            Icons.Default.CameraAlt,
                            contentDescription = "촬영",
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

private fun DrawScope.drawReferenceGrid(
    horizontalCount: Int,
    verticalCount: Int,
    centerX: Float,
    centerY: Float
) {
    val lineColor = Color.Cyan.copy(alpha = 0.6f)
    val centerLineColor = Color.Red.copy(alpha = 0.7f)
    val dashEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))

    val w = size.width
    val h = size.height

    // Center crosshair
    drawLine(
        color = centerLineColor,
        start = Offset(w * centerX, 0f),
        end = Offset(w * centerX, h),
        strokeWidth = 2f
    )
    drawLine(
        color = centerLineColor,
        start = Offset(0f, h * centerY),
        end = Offset(w, h * centerY),
        strokeWidth = 2f
    )

    // Horizontal reference lines (evenly spaced)
    for (i in 1..horizontalCount) {
        val y = h * i / (horizontalCount + 1)
        drawLine(
            color = lineColor,
            start = Offset(0f, y),
            end = Offset(w, y),
            strokeWidth = 1f,
            pathEffect = dashEffect
        )
    }

    // Vertical reference lines (evenly spaced)
    for (i in 1..verticalCount) {
        val x = w * i / (verticalCount + 1)
        drawLine(
            color = lineColor,
            start = Offset(x, 0f),
            end = Offset(x, h),
            strokeWidth = 1f,
            pathEffect = dashEffect
        )
    }
}
