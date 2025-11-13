package com.example.interprep.data.repository

import com.example.interprep.network.PerplexityService.ResumeApiClient
import com.example.interprep.network.PerplexityService.ResumeMessage
import com.example.interprep.network.PerplexityService.ResumeRequest

class ResumeRepository {

    private val api = ResumeApiClient.getInstance()

    suspend fun analyzeResume(resumeText: String, role: String): String {
        val prompt = """
            You are an expert resume reviewer and career advisor.

Analyze the following resume for the role of **$role** and provide feedback in a simple, human-readable way.

Please format your response clearly with headings and bullet points, using emojis to make it engaging and easy to scan.

✨ **Headings to include:**
1. 📝 Grammar & Language
2. 🎨 Formatting & Structure
3. ❌ Missing or Weak Sections
4. 🔑 Keyword Suggestions (important skills or terms to add)
5. 🏆 Overall Score (out of 100)

Each section should be concise and professional, written in plain text with line breaks for readability.

Here is the resume text:
            $resumeText
        """.trimIndent()

        val request = ResumeRequest(
            messages = listOf(ResumeMessage(role = "user", content = prompt))
        )

        val response = api.analyzeResume(request)
        return response.choices.firstOrNull()?.message?.content ?: "⚠ No feedback received."
    }
}
