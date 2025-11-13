package com.example.interprep.nav

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.interprep.screens.AIBOT
import com.example.interprep.screens.AICAREER
import com.example.interprep.screens.ATSCHECKER
import com.example.interprep.screens.AuthScreen
import com.example.interprep.screens.ChitChatScreen
import com.example.interprep.screens.QUESTIONGENERATOR
import com.example.interprep.screens.RESUMEBUILDERGUIDE

@Composable
fun navapp() {
    val navController = rememberNavController()

    var currentUser = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
    NavHost(navController=navController, startDestination = if (currentUser != null) {
        Routes.Home
    }else{
        Routes.Auth
    }){

        composable<Routes.Auth>{
            AuthScreen(hiltViewModel(), onAuthSuccess = {
                navController.navigate(Routes.Home){
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            })
        }

        composable<Routes.Home>{
            ChitChatScreen(navController)
        }

        composable<Routes.AiBot>{
            AIBOT()
        }

        composable<Routes.Qestiongenerator>{
            QUESTIONGENERATOR()
        }

        composable<Routes.resumebuilder>{
            RESUMEBUILDERGUIDE()
        }
        composable<Routes.ATSchecker>{
            ATSCHECKER()
        }
        composable<Routes.CareerCoach>{
            AICAREER()
        }





    }


}