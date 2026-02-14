package com.crossline.app.ui.moneytrack.business

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.crossline.app.data.entity.AiProjectTab
import com.crossline.app.data.repository.AiBusinessRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiBusinessScreen(
    viewModel: AiBusinessViewModel,
    onBack: () -> Unit
) {
    val projects by viewModel.projects.collectAsState()
    val selectedProjectId by viewModel.selectedProjectId.collectAsState()
    val tabs by viewModel.tabs.collectAsState()
    var showAddProjectDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI 창업", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로")
                    }
                },
                actions = {
                    IconButton(onClick = { showAddProjectDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "프로젝트 추가")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (projects.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "프로젝트가 없습니다",
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextButton(onClick = { showAddProjectDialog = true }) {
                            Text("+ 새 프로젝트 만들기")
                        }
                    }
                }
            } else {
                // Project selector
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.weight(if (selectedProjectId == null) 1f else 0f, fill = selectedProjectId == null)
                ) {
                    if (selectedProjectId == null) {
                        items(projects, key = { it.id }) { project ->
                            Card(
                                modifier = Modifier.fillMaxWidth().clickable {
                                    viewModel.selectProject(project.id)
                                },
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Folder,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.secondary
                                    )
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            project.name,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        project.description?.let {
                                            Text(
                                                it,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                            )
                                        }
                                    }
                                    IconButton(onClick = { viewModel.deleteProject(project) }) {
                                        Icon(
                                            Icons.Default.Delete, contentDescription = "삭제",
                                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // 7-tab management view
                if (selectedProjectId != null) {
                    val selectedProject = projects.find { it.id == selectedProjectId }
                    if (selectedProject != null) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(onClick = { viewModel.selectProject(-1); }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                                Text(" 목록으로")
                            }
                            Text(
                                selectedProject.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        ProjectTabsView(
                            tabs = tabs,
                            onUpdateTab = { tab, content -> viewModel.updateTabContent(tab, content) }
                        )
                    }
                }
            }
        }
    }

    if (showAddProjectDialog) {
        AddProjectDialog(
            onDismiss = { showAddProjectDialog = false },
            onAdd = { name, desc ->
                viewModel.addProject(name, desc)
                showAddProjectDialog = false
            }
        )
    }
}

@Composable
private fun ProjectTabsView(
    tabs: List<AiProjectTab>,
    onUpdateTab: (AiProjectTab, String) -> Unit
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    if (tabs.isEmpty()) return

    Column(modifier = Modifier.fillMaxSize()) {
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex.coerceIn(0, tabs.size - 1),
            edgePadding = 8.dp
        ) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            AiBusinessRepository.TAB_LABELS[tab.tabType] ?: tab.tabType,
                            maxLines = 1
                        )
                    }
                )
            }
        }

        val currentTab = tabs.getOrNull(selectedTabIndex)
        if (currentTab != null) {
            var editText by remember(currentTab.id) { mutableStateOf(currentTab.content) }

            OutlinedTextField(
                value = editText,
                onValueChange = {
                    editText = it
                    onUpdateTab(currentTab, it)
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                placeholder = {
                    Text("${AiBusinessRepository.TAB_LABELS[currentTab.tabType]} 내용을 입력하세요...")
                },
                maxLines = Int.MAX_VALUE
            )
        }
    }
}

@Composable
private fun AddProjectDialog(
    onDismiss: () -> Unit,
    onAdd: (name: String, description: String?) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("새 프로젝트") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("프로젝트 이름") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("설명 (선택)") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onAdd(name, description.ifBlank { null }) },
                enabled = name.isNotBlank()
            ) { Text("생성") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("취소") } }
    )
}
