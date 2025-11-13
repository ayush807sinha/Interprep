package com.example.interprep.di

import com.example.interprep.data.repo.AuthRepository
import com.example.interprep.network.perplexity.PerplexityApiService
import com.example.interprep.network.perplexity.RetrofitInstance
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DiModules {

    // 🔹 Firebase Authentication Provider
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepository(firebaseAuth: FirebaseAuth): AuthRepository {
        return AuthRepository(firebaseAuth)
    }

    // 🔹 Perplexity API Provider
    @Provides
    @Singleton
    fun providePerplexityApiService(): PerplexityApiService {
        return RetrofitInstance.api
    }
}
