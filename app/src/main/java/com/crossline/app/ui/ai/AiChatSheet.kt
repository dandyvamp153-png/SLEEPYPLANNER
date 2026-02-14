package com.crossline.app.ui.ai

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Floating AI assistant button - placed in screens where AI is allowed.
 * NEVER used in Pickup screens.
 */
@Composable
fun AiFloatingButton(
    viewModel: AiAssistantViewModel,
    modifier: Modifier = Modifier
) {
    var showChat by remember { mutableStateOf(false) }

    FloatingActionButton(
        onClick = { showChat = true },
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.primary,
        shape = CircleShape
    ) {
        Icon(
            Icons.Default.SmartToy,
            contentDescription = "AI 비서",
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }

    if (showChat) {
        AiChatSheet(
            viewModel = viewModel,
            onDismiss = { showChat = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiChatSheet(
    viewModel: AiAssistantViewModel,
    onDismiss: () -> Unit
) {
    val messages by viewModel.messages.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isConfigured by viewModel.isConfigured.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = Modifier.fillMaxHeight(0.85f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.SmartToy,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "AI 비서",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { viewModel.saveDailySummary() }) {
                    Icon(
                        Icons.Default.SaveAlt,
                        contentDescription = "오늘 요약 저장",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
                IconButton(onClick = { viewModel.clearChat() }) {
                    Icon(
                        Icons.Default.DeleteSweep,
                        contentDescription = "대화 초기화",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "닫기",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }

            if (!isConfigured) {
                // API key setup
                ApiKeySetup(onSubmit = { viewModel.setApiKey(it) })
            } else {
                // Chat messages
                val listState = rememberLazyListState()

                LaunchedEffect(messages.size) {
                    if (messages.isNotEmpty()) {
                        listState.animateScrollToItem(messages.size - 1)
                    }
                }

                LazyColumn(
                    state = listState,
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (messages.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth().padding(top = 48.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        Icons.Default.SmartToy,
                                        contentDescription = null,
                                        modifier = Modifier.size(48.dp),
                                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        "무엇을 도와드릴까요?",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        "다이어트, 학습, 창업, 투자에 대해 물어보세요",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                    )
                                }
                            }
                        }
                    }

                    items(messages, key = { it.timestamp }) { bubble ->
                        ChatBubbleItem(bubble = bubble)
                    }

                    if (isLoading) {
                        item {
                            Row(
                                modifier = Modifier.padding(start = 8.dp, top = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp
                                )
                                Text(
                                    "생각 중...",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                )
                            }
                        }
                    }
                }

                // Input bar
                ChatInputBar(
                    isLoading = isLoading,
                    onSend = { viewModel.sendMessage(it) }
                )
            }
        }
    }
}

@Composable
private fun ChatBubbleItem(bubble: ChatBubble) {
    val isUser = bubble.role == "user"

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isUser) 16.dp else 4.dp,
                        bottomEnd = if (isUser) 4.dp else 16.dp
                    )
                )
                .background(
                    if (isUser) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.surfaceVariant
                )
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Text(
                text = bubble.content,
                color = if (isUser) MaterialTheme.colorScheme.onPrimary
                else MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun ChatInputBar(
    isLoading: Boolean,
    onSend: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.weight(1f),
            placeholder = { Text("메시지 입력...") },
            maxLines = 3,
            shape = RoundedCornerShape(24.dp),
            enabled = !isLoading
        )
        IconButton(
            onClick = {
                if (text.isNotBlank()) {
                    onSend(text)
                    text = ""
                }
            },
            enabled = text.isNotBlank() && !isLoading
        ) {
            Icon(
                Icons.AutoMirrored.Filled.Send,
                contentDescription = "전송",
                tint = if (text.isNotBlank() && !isLoading) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            )
        }
    }
}

@Composable
private fun ApiKeySetup(onSubmit: (String) -> Unit) {
    var key by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.SmartToy,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "OpenAI API 키 설정",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "ChatGPT API를 사용하려면 API 키가 필요합니다",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = key,
            onValueChange = { key = it },
            label = { Text("API Key") },
            placeholder = { Text("sk-...") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(
            onClick = { if (key.isNotBlank()) onSubmit(key) },
            enabled = key.isNotBlank()
        ) {
            Text("설정 완료")
        }
    }
}
