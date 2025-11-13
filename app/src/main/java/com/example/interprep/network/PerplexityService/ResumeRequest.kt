package com.example.interprep.network.PerplexityService


data class ResumeRequest(
    val model: String = "sonar-pro",
    val messages: List<ResumeMessage>
)

data class ResumeMessage(
    val role: String,
    val content: String
)