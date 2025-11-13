package com.example.interprep.network.PerplexityService.Gemeni
import com.example.interprep.utils.geminiApiKey
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GemeniClient {

    fun getRetrofit(baseUrl: String, apiKey: String): Retrofit {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain ->
                val original = chain.request()
                val newUrl = original.url.newBuilder()
                    .addQueryParameter("key", geminiApiKey) // ✅ Gemini requires ?key=
                    .build()

                val newRequest = original.newBuilder()
                    .url(newUrl)
                    .header("Content-Type", "application/json")
                    .build()

                chain.proceed(newRequest)
            }
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
