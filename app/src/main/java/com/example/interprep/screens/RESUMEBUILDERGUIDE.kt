package com.example.interprep.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.interprep.network.PerplexityService.ResumeFileHelper
import com.example.interprep.viewmodel.ResumeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RESUMEBUILDERGUIDE() {

    val context = LocalContext.current
    val viewModel: ResumeViewModel = viewModel()
    val scope = rememberCoroutineScope()

    val resumeText by viewModel.resumeText.collectAsState()
    val feedback by viewModel.feedback.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState(initial = null)

    var fileName by remember { mutableStateOf<String?>(null) }
    var role by remember { mutableStateOf("") }
    var extracting by remember { mutableStateOf(false) }


    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri: Uri? ->
            if (uri != null) {
                extracting = true
                scope.launch {
                    try {
                        // Run text extraction on background thread
                        val text = ResumeFileHelper.extractTextFromPdf(context, uri)
                        viewModel.setResumeText(text)
                        fileName = ResumeFileHelper.getFileName(context, uri)
                    } finally {
                        extracting = false
                    }
                }
            }
        }
    )

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
                text = "AI Resume Builder Guide",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(top = 40.dp, bottom = 10.dp)
            )

            Text(
                text = "Get instant AI feedback and improvement suggestions for your resume.",
                fontSize = 15.sp,
                color = Color(0xFFD4C1F0),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 30.dp)
            )


            Button(
                onClick = { filePickerLauncher.launch(arrayOf("application/pdf")) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6a11cb),
                    contentColor = Color.White
                )
            ) {
                Text("📄 Upload Resume (PDF)", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }

            if (fileName != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Selected File: $fileName",
                    color = Color(0xFFD4C1F0),
                    fontSize = 14.sp
                )
            }

            if (extracting) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Extracting text from resume...",
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(10.dp)
                )
                CircularProgressIndicator(color = Color(0xFF6a11cb))
            }

            Spacer(modifier = Modifier.height(20.dp))


            OutlinedTextField(
                value = role,
                onValueChange = { role = it },
                label = { Text("Enter target job role", color = Color.White) },
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

            Spacer(modifier = Modifier.height(25.dp))


            Button(
                onClick = { viewModel.analyzeResume(role.ifBlank { "Android Developer" }) },
                enabled = resumeText.isNotBlank() && !extracting,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6a11cb),
                    disabledContainerColor = Color(0xFF4A3A6D),
                    contentColor = Color.White
                )
            ) {
                Text("🧠 Analyze Resume", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(20.dp))


            when {
                isLoading -> {
                    CircularProgressIndicator(color = Color(0xFF6a11cb))
                    Spacer(modifier = Modifier.height(20.dp))
                }

                error != null -> {
                    Text(
                        text = error ?: "",
                        color = Color.Red,
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(10.dp)
                    )
                }

                feedback.isNotEmpty() -> {
                    Text(
                        text = "AI Feedback",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
                    )


                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(350.dp)
                            .background(Color(0xFF2A2550), RoundedCornerShape(12.dp))
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = feedback,
                            color = Color(0xFFD4C1F0),
                            fontSize = 16.sp,
                            textAlign = TextAlign.Start,
                            lineHeight = 22.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}
