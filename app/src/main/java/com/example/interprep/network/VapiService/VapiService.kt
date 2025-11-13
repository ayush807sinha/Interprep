package com.example.interprep.network.VapiService


import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface VapiService {
    @Headers("Content-Type: application/json")
    @POST("v1/calls")
    suspend fun synthesizeSpeech(@Body request: VapiTtsRequest): VapiTtsResponse
}
