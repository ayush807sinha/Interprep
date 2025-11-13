package com.example.interprep.network.gemini

import com.example.interprep.network.PerplexityService.Gemeni.GeminiRequest
import com.example.interprep.network.PerplexityService.Gemeni.GeminiResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface GeminiApiService {

    // ✅ Content type header is correct
    @Headers("Content-Type: application/json")
    @POST("models/gemini-2.5-flash:generateContent")
    suspend fun generateContent(
        @Body request: GeminiRequest
    ): GeminiResponse
}
