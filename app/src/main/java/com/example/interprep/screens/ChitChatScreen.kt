package com.example.interprep.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.interprep.nav.Routes
import com.google.firebase.auth.FirebaseAuth


@Composable
fun ChitChatScreen(
    navController: NavController,
) {
    val firebaseAuth = FirebaseAuth.getInstance()

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
                .padding(horizontal = 20.dp, vertical = 30.dp)
        ) {

            Text(
                text = "InterPrep",
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )


            Text(
                text = "Master Every Interview — Powered by AI, Designed for You",
                fontSize = 15.sp,
                color = Color(0xFFD4C1F0),
                textAlign = TextAlign.Center,
                lineHeight = 20.sp,
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.height(25.dp))


            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = true)
            ) {
                FeatureCard(
                    title = "AI Interview Bot",
                    description = "Get real-time mock interviews based on your job role and skills.",
                    onClick = { navController.navigate(Routes.AiBot) }
                )

                FeatureCard(
                    title = "Question Generator",
                    description = "Generate custom interview questions based on your resume and preferences.",
                    onClick = { navController.navigate(Routes.Qestiongenerator) }
                )

                FeatureCard(
                    title = "Resume Builder Guidance",
                    description = "Get personalized suggestions to enhance your resume’s impact.",
                    onClick = { navController.navigate(Routes.resumebuilder) }
                )

                FeatureCard(
                    title = "ATS Score Checker",
                    description = "Analyze your resume to see how it ranks against job descriptions.",
                    onClick = { navController.navigate(Routes.ATSchecker) }
                )

                FeatureCard(
                    title = "AI Career Coach",
                    description = "Grow your career smarter with AI insights.",
                    onClick = { navController.navigate(Routes.CareerCoach) }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))


            Button(
                onClick = {
                    firebaseAuth.signOut()
                    navController.navigate(Routes.Auth) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6a11cb),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(bottom = 10.dp)
            ) {
                Text(
                    text = "Log Out",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun FeatureCard(
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFF16151d).copy(alpha = 0.95f),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(10.dp, RoundedCornerShape(24.dp))
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE3E1E6)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                fontSize = 14.sp,
                color = Color(0xFFB8B2C8),
                lineHeight = 18.sp
            )
        }
    }
}
