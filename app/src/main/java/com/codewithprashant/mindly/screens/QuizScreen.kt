package com.codewithprashant.mindly.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

import com.codewithprashant.mindly.ui.theme.CorrectAnswer
import com.codewithprashant.mindly.ui.theme.SelectedOption
import com.codewithprashant.mindly.ui.theme.UnselectedOption
import com.codewithprashant.mindly.ui.theme.WrongAnswer
import com.codewithprashant.mindly.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    onNavigateBack: () -> Unit,
    onNavigateToResult: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentQuestions by viewModel.currentQuestions.collectAsState()

    var selectedOption by remember { mutableStateOf<String?>(null) }
    var showAnswer by remember { mutableStateOf(false) }

    val currentQuestion = viewModel.getCurrentQuestion()
    val progress = viewModel.getProgress()

    LaunchedEffect(uiState.currentQuestionIndex) {
        selectedOption = null
        showAnswer = false
    }

    LaunchedEffect(uiState.showResult) {
        if (uiState.showResult) {
            onNavigateToResult()
        }
    }

    if (!uiState.isQuizActive || currentQuestion == null) {
        LaunchedEffect(Unit) {
            onNavigateBack()
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top Bar
        TopAppBar(
            title = {
                Text(
                    text = "Question ${uiState.currentQuestionIndex + 1}/${currentQuestions.size}",
                    style = MaterialTheme.typography.titleMedium
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    viewModel.resetQuiz()
                    onNavigateBack()
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Progress Bar
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Question
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = currentQuestion.question,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Answer Options
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AnswerOption(
                option = "A",
                text = currentQuestion.optionA,
                isSelected = selectedOption == "A",
                isCorrect = currentQuestion.correctAnswer == "A",
                showAnswer = showAnswer,
                onClick = {
                    if (!showAnswer) {
                        selectedOption = "A"
                    }
                }
            )

            AnswerOption(
                option = "B",
                text = currentQuestion.optionB,
                isSelected = selectedOption == "B",
                isCorrect = currentQuestion.correctAnswer == "B",
                showAnswer = showAnswer,
                onClick = {
                    if (!showAnswer) {
                        selectedOption = "B"
                    }
                }
            )

            AnswerOption(
                option = "C",
                text = currentQuestion.optionC,
                isSelected = selectedOption == "C",
                isCorrect = currentQuestion.correctAnswer == "C",
                showAnswer = showAnswer,
                onClick = {
                    if (!showAnswer) {
                        selectedOption = "C"
                    }
                }
            )

            AnswerOption(
                option = "D",
                text = currentQuestion.optionD,
                isSelected = selectedOption == "D",
                isCorrect = currentQuestion.correctAnswer == "D",
                showAnswer = showAnswer,
                onClick = {
                    if (!showAnswer) {
                        selectedOption = "D"
                    }
                }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Submit Button
        Button(
            onClick = {
                selectedOption?.let { option ->
                    if (!showAnswer) {
                        showAnswer = true
                    } else {
                        viewModel.submitAnswer(option)
                    }
                }
            },
            enabled = selectedOption != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = if (!showAnswer) "Submit Answer" else "Next Question",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun AnswerOption(
    option: String,
    text: String,
    isSelected: Boolean,
    isCorrect: Boolean,
    showAnswer: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        showAnswer && isCorrect -> CorrectAnswer.copy(alpha = 0.2f)
        showAnswer && isSelected && !isCorrect -> WrongAnswer.copy(alpha = 0.2f)
        isSelected -> SelectedOption.copy(alpha = 0.1f)
        else -> UnselectedOption
    }

    val borderColor = when {
        showAnswer && isCorrect -> CorrectAnswer
        showAnswer && isSelected && !isCorrect -> WrongAnswer
        isSelected -> SelectedOption
        else -> Color.Transparent
    }

    val iconColor = when {
        showAnswer && isCorrect -> CorrectAnswer
        showAnswer && isSelected && !isCorrect -> WrongAnswer
        else -> Color.Transparent
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(
                width = if (borderColor != Color.Transparent) 2.dp else 0.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 6.dp else 2.dp
        ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Option Letter
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = option,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Option Text
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
            }

            // Result Icon
            AnimatedVisibility(
                visible = showAnswer && (isCorrect || (isSelected && !isCorrect)),
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                Icon(
                    imageVector = if (isCorrect) Icons.Default.Check else Icons.Default.Close,
                    contentDescription = if (isCorrect) "Correct" else "Wrong",
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}