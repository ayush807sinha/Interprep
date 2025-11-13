package com.example.interprep.network.PerplexityService

import retrofit2.http.Body
import retrofit2.http.POST
import com.example.interprep.network.PerplexityService.ResumeResponse
import com.example.interprep.network.PerplexityService.ResumeRequest
interface ResumeApi {

    @POST("chat/completions")
    suspend fun analyzeResume(
        @Body body: ResumeRequest
    ): ResumeResponse
}
