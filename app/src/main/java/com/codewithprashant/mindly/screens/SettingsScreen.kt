package com.codewithprashant.mindly.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.codewithprashant.mindly.viewmodel.MainViewModel
import com.codewithprashant.mindly.ui.icons.CustomIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val quizResults by viewModel.quizResults.collectAsState()
    val allQuestions by viewModel.allQuestions.collectAsState()

    var showDeleteDialog by remember { mutableStateOf(false) }
    var deleteType by remember { mutableStateOf("") }

    var statistics by remember { mutableStateOf<com.codewithprashant.mindly.viewmodel.QuizStatistics?>(null) }

    LaunchedEffect(Unit) {
        statistics = viewModel.getStatistics()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        item {
            // Statistics Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Quiz Statistics",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    statistics?.let { stats ->
                        StatisticItem(
                            iconRes = CustomIcons.QuestionMark,
                            title = "Total Questions",
                            value = stats.totalQuestions.toString()
                        )

                        StatisticItem(
                            icon = Icons.Default.CheckCircle,
                            title = "Quizzes Completed",
                            value = stats.totalQuizzes.toString()
                        )

                        StatisticItem(
                            iconRes = CustomIcons.TrendingUp,
                            title = "Average Score",
                            value = if (stats.averageScore.isNaN()) "0%" else "${stats.averageScore.toInt()}%"
                        )

                        StatisticItem(
                            iconRes = CustomIcons.Timeline,
                            title = "Total Results",
                            value = quizResults.size.toString()
                        )
                    }
                }
            }
        }

        item {
            // App Settings Section
            Text(
                text = "App Settings",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }

        item {
            SettingsItem(
                iconRes = CustomIcons.ColorLens,
                title = "Theme",
                subtitle = "Dark/Light theme",
                onClick = { /* TODO: Implement theme switching */ }
            )
        }

        item {
            SettingsItem(
                icon = Icons.Default.Notifications,
                title = "Notifications",
                subtitle = "Quiz reminders and updates",
                onClick = { /* TODO: Implement notifications */ }
            )
        }

        item {
            // Data Management Section
            Text(
                text = "Data Management",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }

        item {
            SettingsItem(
                iconRes = CustomIcons.GetApp,
                title = "Import Questions",
                subtitle = "Add new questions from file",
                onClick = { /* TODO: Implement import */ }
            )
        }

        item {
            SettingsItem(
                iconRes = CustomIcons.Upload,
                title = "Export Results",
                subtitle = "Save your quiz results",
                onClick = { /* TODO: Implement export */ }
            )
        }

        item {
            SettingsItem(
                icon = Icons.Default.Delete,
                title = "Clear Results",
                subtitle = "Delete all quiz results",
                onClick = {
                    deleteType = "results"
                    showDeleteDialog = true
                },
                isDestructive = true
            )
        }

        item {
            SettingsItem(
                icon = Icons.Default.Refresh,
                title = "Reset App Data",
                subtitle = "Clear all questions and results",
                onClick = {
                    deleteType = "all"
                    showDeleteDialog = true
                },
                isDestructive = true
            )
        }

        item {
            // About Section
            Text(
                text = "About",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }

        item {
            SettingsItem(
                icon = Icons.Default.Info,
                title = "App Version",
                subtitle = "Mindly Quiz v1.0.0",
                onClick = { }
            )
        }

        item {
            SettingsItem(
                icon = Icons.Default.Person,
                title = "Developer",
                subtitle = "Made by Prashant",
                onClick = { }
            )
        }
    }

    // Delete Confirmation Dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(
                    text = when (deleteType) {
                        "results" -> "Clear Results"
                        "all" -> "Reset App Data"
                        else -> "Confirm"
                    }
                )
            },
            text = {
                Text(
                    text = when (deleteType) {
                        "results" -> "This will delete all your quiz results. This action cannot be undone."
                        "all" -> "This will delete all questions and results. This action cannot be undone."
                        else -> "Are you sure?"
                    }
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        // TODO: Implement actual deletion
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

// Updated StatisticItem with support for both ImageVector and drawable resource
@Composable
fun StatisticItem(
    icon: ImageVector? = null,
    iconRes: Int? = null,
    title: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            when {
                icon != null -> {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                iconRes != null -> {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

// Updated SettingsItem with support for both ImageVector and drawable resource
@Composable
fun SettingsItem(
    icon: ImageVector? = null,
    iconRes: Int? = null,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    isDestructive: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            when {
                icon != null -> {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (isDestructive) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                iconRes != null -> {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = null,
                        tint = if (isDestructive) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isDestructive) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}