package com.example.interprep.network.PerplexityService

data class Question(
    val questionText: String,
    val options: List<String>? = null,
    val correctAnswer: String? = null,
    val answerText: String? = null,
    val category: String,
    val difficulty: String? = null
)

