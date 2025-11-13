package com.example.interprep.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.interprep.state.ResultState
import com.example.interprep.viewmodel.AuthViewModel

@Composable
fun AuthScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    onAuthSuccess: () -> Unit
) {
    val signInState by authViewModel.signInState.collectAsState()
    val signUpState by authViewModel.signUpState.collectAsState()
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isSignUpMode by remember { mutableStateOf(false) }

    LaunchedEffect(signInState, signUpState) {
        val success = when {
            !isSignUpMode && signInState is ResultState.Success -> true
            isSignUpMode && signUpState is ResultState.Success -> true
            else -> false
        }
        if (success) onAuthSuccess()

        val errorMessage = when {
            !isSignUpMode && signInState is ResultState.Error -> (signInState as ResultState.Error).message
            isSignUpMode && signUpState is ResultState.Error -> (signUpState as ResultState.Error).message
            else -> null
        }
        errorMessage?.let { Toast.makeText(context, it, Toast.LENGTH_LONG).show() }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF1f1147), Color(0xFF171729))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {

            Text(
                text = "InterPrep",
                fontSize = 44.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 10.dp)
            )


            Text(
                text = "Master Every Interview — Powered by AI, Designed for You",
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFD4C1F0),
                modifier = Modifier.padding(bottom = 40.dp),
                letterSpacing = 0.6.sp,
                lineHeight = 22.sp,
                textAlign = TextAlign.Center
            )


            Surface(
                modifier = Modifier
                    .widthIn(max = 360.dp)
                    .wrapContentHeight()
                    .shadow(20.dp, RoundedCornerShape(36.dp)),
                shape = RoundedCornerShape(36.dp),
                color = Color(0xFF16151d).copy(alpha = 0.95f)
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 30.dp, vertical = 36.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (isSignUpMode) "Create Account" else "Sign In",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE3E1E6),
                        modifier = Modifier.padding(bottom = 32.dp)
                    )


                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email", color = Color(0xFFE3E1E6)) },
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF6a11cb),
                            unfocusedBorderColor = Color(0xFF28263C),
                            cursorColor = Color(0xFF6a11cb),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))


                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password", color = Color(0xFFE3E1E6)) },
                        visualTransformation = PasswordVisualTransformation(),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF6a11cb),
                            unfocusedBorderColor = Color(0xFF28263C),
                            cursorColor = Color(0xFF6a11cb),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    )

                    Spacer(modifier = Modifier.height(36.dp))

                    val isLoading =
                        if (isSignUpMode) (signUpState is ResultState.Loading)
                        else (signInState is ResultState.Loading)


                    Button(
                        onClick = {
                            if (email.isNotBlank() && password.isNotBlank()) {
                                if (isSignUpMode)
                                    authViewModel.signUp(email.trim(), password.trim())
                                else
                                    authViewModel.signIn(email.trim(), password.trim())
                            } else {
                                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        enabled = !isLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6a11cb),
                            contentColor = Color.White
                        )
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(28.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = if (isSignUpMode) "Sign Up" else "Sign In",
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(28.dp))


                    TextButton(
                        onClick = { isSignUpMode = !isSignUpMode },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (isSignUpMode)
                                "Already have an account? Sign In"
                            else
                                "New here? Create Account",
                            color = Color(0xFFCAC5CE),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
