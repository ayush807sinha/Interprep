package com.example.interprep.network.perplexity.model

data class PerplexityMessage(
    val role: String,
    val content: String
)

data class PerplexityChoice(
    val index: Int?,
    val message: PerplexityMessage?,
    val finish_reason: String?
)

data class PerplexityResponse(
    val id: String?,
    val choices: List<PerplexityChoice>?,
    val created: Long?,
    val model: String?
)

data class PerplexityRequest(
    val model: String = "sonar-pro", // Best for interviews and reasoning
    val messages: List<PerplexityMessage>,
    val temperature: Double = 0.7
)
