package com.codewithprashant.mindly.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.codewithprashant.mindly.viewmodel.MainViewModel
import com.codewithprashant.mindly.ui.icons.CustomIcons
import kotlin.math.roundToInt

@Composable
fun QuizResultScreen(
    onNavigateHome: () -> Unit,
    onRetakeQuiz: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val result = uiState.lastQuizResult

    var animationPlayed by remember { mutableStateOf(false) }
    val animatedScore by animateFloatAsState(
        targetValue = if (animationPlayed) (result?.score?.toFloat() ?: 0f) else 0f,
        animationSpec = tween(
            durationMillis = 1500,
            easing = EaseOutBounce
        )
    )

    val scaleAnimation by animateFloatAsState(
        targetValue = if (animationPlayed) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    LaunchedEffect(Unit) {
        animationPlayed = true
    }

    if (result == null) {
        LaunchedEffect(Unit) {
            onNavigateHome()
        }
        return
    }

    val scoreColor = when {
        result.score >= 80 -> MaterialTheme.colorScheme.primary
        result.score >= 60 -> MaterialTheme.colorScheme.secondary
        else -> MaterialTheme.colorScheme.error
    }

    val performanceText = when {
        result.score >= 90 -> "Excellent!"
        result.score >= 80 -> "Great Job!"
        result.score >= 70 -> "Good Work!"
        result.score >= 60 -> "Not Bad!"
        else -> "Keep Trying!"
    }

    val performanceIcon = when {
        result.score >= 80 -> Icons.Default.Star
        result.score >= 60 -> Icons.Default.ThumbUp
        else -> CustomIcons.TrendingUp
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Celebration Icon
        AnimatedVisibility(
            visible = animationPlayed,
            enter = scaleIn(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ) + fadeIn()
        ) {
            when (performanceIcon) {
                is ImageVector -> {
                    Icon(
                        imageVector = performanceIcon,
                        contentDescription = null,
                        tint = scoreColor,
                        modifier = Modifier
                            .size(80.dp)
                            .scale(scaleAnimation)
                    )
                }
                else -> {
                    Icon(
                        painter = painterResource(id = performanceIcon as Int),
                        contentDescription = null,
                        tint = scoreColor,
                        modifier = Modifier
                            .size(80.dp)
                            .scale(scaleAnimation)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Performance Text
        AnimatedVisibility(
            visible = animationPlayed,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(durationMillis = 800)
            ) + fadeIn(animationSpec = tween(delayMillis = 0))
        ) {
            Text(
                text = performanceText,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = scoreColor,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Circular Progress Score
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(200.dp)
        ) {
            // Background Circle
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    color = scoreColor.copy(alpha = 0.1f),
                    radius = size.minDimension / 2,
                    style = Stroke(width = 20.dp.toPx(), cap = StrokeCap.Round)
                )
            }

            // Progress Circle
            Canvas(modifier = Modifier.fillMaxSize()) {
                val sweepAngle = (animatedScore / 100) * 360
                drawArc(
                    color = scoreColor,
                    startAngle = -90f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(width = 20.dp.toPx(), cap = StrokeCap.Round)
                )
            }

            // Score Text
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${animatedScore.roundToInt()}%",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = scoreColor
                )
                Text(
                    text = "Score",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Result Details Card
        AnimatedVisibility(
            visible = animationPlayed,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(durationMillis = 1000, delayMillis = 500)
            ) + fadeIn(animationSpec = tween(delayMillis = 500))
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Quiz Details",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ResultDetailRow(
                        label = "Correct Answers",
                        value = "${result.correctAnswers}/${result.totalQuestions}",
                        icon = Icons.Default.CheckCircle
                    )

                    ResultDetailRow(
                        label = "Category",
                        value = result.category,
                        iconRes = CustomIcons.Tag
                    )

                    ResultDetailRow(
                        label = "Difficulty",
                        value = result.difficulty,
                        iconRes = CustomIcons.Speed
                    )

                    ResultDetailRow(
                        label = "Time Taken",
                        value = formatTime(result.timeTaken),
                        iconRes = CustomIcons.Schedule
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Action Buttons
        AnimatedVisibility(
            visible = animationPlayed,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(durationMillis = 800, delayMillis = 800)
            ) + fadeIn(animationSpec = tween(delayMillis = 800))
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onRetakeQuiz,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Try Again",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                OutlinedButton(
                    onClick = onNavigateHome,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Back to Home",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

// Updated ResultDetailRow with support for both ImageVector and drawable resource
@Composable
fun ResultDetailRow(
    label: String,
    value: String,
    icon: ImageVector? = null,
    iconRes: Int? = null
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
                        modifier = Modifier.size(20.dp)
                    )
                }
                iconRes != null -> {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
        }

        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

private fun formatTime(timeMillis: Long): String {
    val seconds = (timeMillis / 1000) % 60
    val minutes = (timeMillis / (1000 * 60)) % 60
    return if (minutes > 0) {
        "${minutes}m ${seconds}s"
    } else {
        "${seconds}s"
    }
}