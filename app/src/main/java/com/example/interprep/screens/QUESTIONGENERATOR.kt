package com.example.interprep.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.interprep.viewmodel.QuestionGeneratorViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QUESTIONGENERATOR(
    viewModel: QuestionGeneratorViewModel = hiltViewModel()
) {
    val question by viewModel.question.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val showAnswer by viewModel.showAnswer.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var selectedCategory by remember { mutableStateOf("Aptitude") }
    var expanded by remember { mutableStateOf(false) }

    val categories = listOf("Aptitude", "Technical", "Coding")


    var timeLeft by remember { mutableStateOf(75) } //
    var timerRunning by remember { mutableStateOf(false) }


    LaunchedEffect(question) {
        if (question != null) {
            timeLeft = 75
            timerRunning = true
        }
    }


    LaunchedEffect(timerRunning) {
        if (timerRunning) {
            while (isActive && timeLeft > 0) {
                delay(1000)
                timeLeft--
            }
            timerRunning = false
        }
    }


    fun formatTime(seconds: Int): String {
        val minutes = seconds / 60
        val sec = seconds % 60
        return String.format("%02d:%02d", minutes, sec)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF1f1147), Color(0xFF171729))
                )
            ),
        contentAlignment = Alignment.TopCenter
    ) {


        if (loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.25f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(64.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                    strokeCap = StrokeCap.Round,
                    strokeWidth = 6.dp
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Spacer(modifier = Modifier.height(80.dp))
                    Text(
                        text = "Generating question...",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
                .alpha(if (loading) 0.5f else 1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "AI Question Generator",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(top = 40.dp, bottom = 10.dp)
            )

            Text(
                text = "Generate technical or aptitude interview questions using AI.",
                fontSize = 15.sp,
                color = Color(0xFFD4C1F0),
                modifier = Modifier.padding(bottom = 30.dp),
                textAlign = TextAlign.Center
            )


            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedCategory,
                    onValueChange = {},
                    label = { Text("Select Category", color = Color.White) },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF6a11cb),
                        unfocusedBorderColor = Color(0xFF28263C),
                        cursorColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category, color = Color.White) },
                            onClick = {
                                selectedCategory = category
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(25.dp))


            Button(
                onClick = { viewModel.generateQuestion(selectedCategory) },
                enabled = !loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6a11cb),
                    contentColor = Color.White,
                    disabledContainerColor = Color(0xFF4A3A6D)
                )
            ) {
                Text(
                    text = if (loading) "Generating..." else "Generate Question",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))


            if (question != null) {
                Text(
                    text = "⏳ Time Left: ${formatTime(timeLeft)}",
                    color = if (timeLeft > 10) Color(0xFFB3E5FC) else Color.Red,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))


            if (!errorMessage.isNullOrEmpty()) {
                Text(
                    text = "⚠️ $errorMessage",
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }


            if (question != null && !loading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF2A2550), RoundedCornerShape(12.dp))
                        .padding(16.dp)
                ) {
                    Column(horizontalAlignment = Alignment.Start) {
                        Text(
                            text = "Question:\n${question!!.questionText}",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        question!!.options?.forEachIndexed { index, option ->
                            Text(
                                text = "${'A' + index}) $option",
                                color = Color(0xFFD4C1F0),
                                fontSize = 16.sp,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { viewModel.toggleAnswerVisibility() },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF6a11cb),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(if (showAnswer) "Hide Answer" else "Show Answer")
                        }

                        if (showAnswer) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "💡 Answer:\n${question!!.correctAnswer ?: question!!.answerText ?: "Not available"}",
                                color = Color(0xFFB3E5FC),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}
