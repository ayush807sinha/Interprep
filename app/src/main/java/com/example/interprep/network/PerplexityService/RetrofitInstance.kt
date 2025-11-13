package com.example.interprep.network.perplexity

import com.example.interprep.utils.perplexityKey
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    // 🔐 Replace with your actual API key securely (ideally store in local.properties or BuildConfig)
     val API_KEY = perplexityKey

    // 🌐 Base URL for Perplexity API
    private const val BASE_URL = "https://api.perplexity.ai/"

    // 🧩 OkHttp client with Authorization header
    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val newRequest: Request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $API_KEY")
                    .addHeader("Content-Type", "application/json")
                    .build()
                chain.proceed(newRequest)
            }
            .build()
    }

    // 🚀 Retrofit instance for Perplexity API
    val api: PerplexityApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PerplexityApiService::class.java)
    }
}
