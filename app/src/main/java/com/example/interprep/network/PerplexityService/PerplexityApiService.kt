package com.example.interprep.network.perplexity

import com.example.interprep.network.perplexity.model.PerplexityRequest
import com.example.interprep.network.perplexity.model.PerplexityResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface PerplexityApiService {
    @Headers("Content-Type: application/json")
    @POST("chat/completions")
    suspend fun getChatCompletion(
        @Body request: PerplexityRequest
    ): PerplexityResponse

}
