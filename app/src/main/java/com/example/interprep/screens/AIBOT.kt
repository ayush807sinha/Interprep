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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.interprep.utils.geminiApiKey
import com.example.interprep.viewmodel.VoiceInterviewViewModel
import com.example.interprep.viewmodel.VoiceInterviewViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIBOT() {
    val context = LocalContext.current

   
    val viewModel: VoiceInterviewViewModel = viewModel(
        factory = VoiceInterviewViewModelFactory(context, geminiApiKey)
    )

    var jobRole by remember { mutableStateOf(TextFieldValue("")) }
    var skills by remember { mutableStateOf(TextFieldValue("")) }
    var selectedExperience by remember { mutableStateOf("Fresher") }
    var expanded by remember { mutableStateOf(false) }
    var started by remember { mutableStateOf(false) }

    val experienceOptions = listOf("Intern", "Fresher", "2 Years", "5 Years", "8 Years")

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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Text(
                text = "AI Interview Bot",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(top = 40.dp, bottom = 10.dp)
            )

            Text(
                text = "Get real-time mock interviews tailored to your skills.",
                fontSize = 15.sp,
                color = Color(0xFFD4C1F0),
                modifier = Modifier.padding(bottom = 30.dp)
            )

            if (!started) {

                OutlinedTextField(
                    value = jobRole,
                    onValueChange = { jobRole = it },
                    label = { Text("Enter your job role", color = Color.White) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF6a11cb),
                        unfocusedBorderColor = Color(0xFF28263C),
                        cursorColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = skills,
                    onValueChange = { skills = it },
                    label = { Text("Enter your key skills", color = Color.White) },
                    singleLine = false,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF6a11cb),
                        unfocusedBorderColor = Color(0xFF28263C),
                        cursorColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedExperience,
                        onValueChange = {},
                        label = { Text("Select Experience", color = Color.White) },
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
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
                        experienceOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option, color = Color.White) },
                                onClick = {
                                    selectedExperience = option
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                Button(
                    onClick = {
                        started = true
                        viewModel.startInterview(
                            jobRole.text,
                            skills.text,
                            selectedExperience
                        )
                    },
                    enabled = jobRole.text.isNotBlank() && skills.text.isNotBlank(),
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
                    Text("Start Interview", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                }

            } else {
                Text(
                    text = "🎤 Interview in progress...\nSpeak to the AI interviewer.",
                    color = Color.White,
                    fontSize = 18.sp,
                    lineHeight = 24.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 50.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                if (viewModel.processing.value) {
                    CircularProgressIndicator(color = Color(0xFF6a11cb))
                    Spacer(modifier = Modifier.height(20.dp))
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(Color(0xFF2A2550), RoundedCornerShape(12.dp))
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = viewModel.aiText.value,
                        color = Color(0xFFD4C1F0),
                        fontSize = 16.sp,
                        textAlign = TextAlign.Start,
                        lineHeight = 22.sp
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Button(
                        onClick = { viewModel.startConversationOnce() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6a11cb),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("🎙️ Speak Again")
                    }

                    Button(
                        onClick = {
                            viewModel.stopInterview()
                            started = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Stop Interview")
                    }
                }

                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    }
}
