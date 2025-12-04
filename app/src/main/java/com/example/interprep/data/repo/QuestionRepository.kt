package com.example.interprep.data.repo

import com.example.interprep.network.perplexity.PerplexityApiService
import com.example.interprep.network.perplexity.model.PerplexityMessage
import com.example.interprep.network.perplexity.model.PerplexityRequest
import com.example.interprep.network.PerplexityService.Question
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuestionRepository @Inject constructor(
    private val api: PerplexityApiService
) {

    suspend fun generateQuestion(category: String): Question {
        val prompt = """
You are an expert question generator trained on real interview and aptitude test patterns from companies like TCS, Accenture, Wipro, Infosys, and Cognizant.

Generate ONE interview or aptitude question based on the selected category: "$category".

Follow these strict rules:

If category = Aptitude then ask any where from verbal, non ,verble ,logical ,aptitude. 

1. The question must come from **real aptitude and placement-related topics**, including:
   - Quantitative Aptitude from percentages, profit & loss, time, speed & distance, permutations & combinations, probability, ratios,ages,number system
   - Logical Reasoning from coding-decoding, blood relations, direction sense, seating arrangement, syllogisms, puzzles
   - Verbal Ability from synonyms, antonyms, sentence correction, paragraph comprehension
   - Technical Aptitude  applicable to category, e.g., basic programming logic, number systems, or pattern recognition

2. Randomly decide if the question will be:
   - **MCQ (Multiple Choice)** → must have exactly 4 options (A, B, C, D) and clearly mention the correct one.
   - **Descriptive** → a short conceptual or reasoning-based question that requires an explanation.

3. Always generate **fresh, unique questions** — no repetition or generic content.

4. The difficulty level should be **moderate**, following real test standards used by TCS NQT or Accenture Cognitive Assessment.

5. Output format must be **exactly as follows** (no extra symbols, markdown, or punctuation):
   
   Question: <question text>
   Options:
   A) ...
   B) ...
   C) ...
   D) ...
   Answer: <correct option or correct answer>
   Detailed Answer: <Short descriptive>

6. If the question is descriptive and does not need options, leave the “Options” section empty but keep the structure.

7. Use **simple English** and ensure questions are copyable as plain text (no *, #, or other symbols).

Now generate one question following the above rules.
""".trimIndent()


        val request = PerplexityRequest(
            model = "sonar-pro",
            messages = listOf(
                PerplexityMessage(
                    role = "system",
                    content = "You are an expert interviewer who generates clean, formatted questions."
                ),
                PerplexityMessage(role = "user", content = prompt)
            ),
            temperature = 0.7
        )

        val response = api.getChatCompletion(request)
        val content = response.choices
            ?.firstOrNull()
            ?.message
            ?.content
            ?.trim()
            ?: "No question found."

      
        val questionText = content.substringAfter("Question:", "").substringBefore("Options:").trim()

        val optionsBlock = content.substringAfter("Options:", "").substringBefore("Answer:").trim()
        val options = if (optionsBlock.isNotEmpty()) {
            optionsBlock.lines()
                .mapNotNull { line ->
                    // Handles "A)", "A.", or "A-" style prefixes
                    line.substringAfter(")").substringAfter(".").substringAfter("-").trim()
                        .takeIf { it.isNotBlank() }
                }
                .filter { it.isNotBlank() }
                .takeIf { it.isNotEmpty() }
        } else null

        val answer = content.substringAfter("Answer:", "").substringBefore("Detailed Answer:").trim()
        val detailedAnswer = content.substringAfter("Detailed Answer:", "").trim()

        val correctAnswer = if (options != null && answer.isNotBlank()) answer else null

        return Question(
            questionText = questionText.ifBlank { "No question found." },
            options = options,
            correctAnswer = correctAnswer,
            answerText = if (options == null) {
                if (detailedAnswer.isNotBlank()) detailedAnswer else if (answer.isNotBlank()) answer else null
            } else null,
            category = category
        )
    }
}
