package com.example.interprep.network.PerplexityService

data class ResumeResponse(
    val choices: List<ResumeChoice>
)

data class ResumeChoice(
    val message: ResumeMessageContent
)

data class ResumeMessageContent(
    val content: String
)