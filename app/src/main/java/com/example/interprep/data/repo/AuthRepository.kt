package com.example.interprep.data.repo

import com.example.interprep.state.ResultState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class AuthRepository(private val firebaseAuth: FirebaseAuth) {

    val currentUser: FirebaseUser? = firebaseAuth.currentUser

    suspend fun signUp(email: String, password: String): ResultState<FirebaseUser> {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            ResultState.Success(authResult.user!!)
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Sign up failed")
        }
    }

    suspend fun signIn(email: String, password: String): ResultState<FirebaseUser> {
        return try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            ResultState.Success(authResult.user!!)
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Sign in failed")
        }
    }

    fun signOut() {
        firebaseAuth.signOut()
    }
}