package com.example.interprep.network.PerplexityService.Gemeni


data class GeminiRequest(
    val contents: List<GeminiContent>
)

data class GeminiContent(
    val role: String = "user",
    val parts: List<GeminiPart>
)

data class GeminiPart(
    val text: String
)
