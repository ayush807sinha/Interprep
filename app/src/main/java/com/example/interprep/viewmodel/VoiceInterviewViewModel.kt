package com.example.interprep.viewmodel

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.interprep.audio.SpeechToTextHelper
import com.example.interprep.network.PerplexityService.Gemeni.GemeniClient
import com.example.interprep.network.PerplexityService.Gemeni.GeminiContent
import com.example.interprep.network.PerplexityService.Gemeni.GeminiPart
import com.example.interprep.network.PerplexityService.Gemeni.GeminiRequest
import com.example.interprep.network.gemini.GeminiApiService
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.util.Locale

class VoiceInterviewViewModel(
    private val context: Context,
    private val geminiApiKey: String
) : ViewModel() {

   
    var isInterviewStarted = mutableStateOf(false)
        private set
    var aiText = mutableStateOf("Press Start to begin the interview.")
        private set
    var listening = mutableStateOf(false)
        private set
    var processing = mutableStateOf(false)
        private set

  
    private val sttHelper = SpeechToTextHelper(context)
    private var textToSpeech: TextToSpeech? = null
    private var conversationJob: Job? = null

    
    private val conversationHistory = mutableListOf<GeminiContent>()

  
    private val geminiApi = GemeniClient
        .getRetrofit(
            baseUrl = "https://generativelanguage.googleapis.com/v1beta/",
            apiKey = geminiApiKey
        )
        .create(GeminiApiService::class.java)

  
    init {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech?.language = Locale.US
                Log.d("VoiceInterviewVM", "✅ TTS initialized successfully")
            } else {
                Log.e("VoiceInterviewVM", "❌ TTS initialization failed")
            }
        }
    }

    
    fun startInterview(jobRole: String, skills: String, experience: String) {
        if (isInterviewStarted.value) return

        isInterviewStarted.value = true
        aiText.value = "Starting interview for $jobRole..."
        conversationHistory.clear() 

        val prompt = """
You are a strict and professional **AI Interviewer** conducting a mock interview for the role of $jobRole.
The candidate has skills in: $skills.
Experience level: $experience.

Your behavior and rules:
1. Conduct this like a **real technical interview**.
2. Ask **one question at a time**, relevant to the candidate’s skills and experience.
3. When the candidate answers:
   - If the answer is **correct or partially correct**, respond with brief positive feedback like:
     “Yes, that’s correct.” or “Good job, you explained it well.” or “That’s right — well done.”
   - If the answer is **wrong or incomplete**, respond politely and briefly with feedback like:
     “Not exactly,” or “That’s not quite right,” and then move to the next question.
4. Keep your tone **professional but encouraging** — you are strict but motivating.
5. Never reveal the correct answer or detailed explanations.
6. Do **not** repeat the same question unless the user says “repeat”.
7. If the user says “next” or “skip”, move to a new topic.
8. If the user says “stop”, “end interview”, or “thank you”, respond only with:
   “Thank you for your time. The interview session is now complete.”
9. Use **plain text only**, no markdown, no emojis, no special formatting.
10. Keep each question concise, technical, and relevant to **$skills**.

Start with a short greeting like:
“Good day. Let's begin your interview for the $jobRole role.”
Then immediately ask your first question.
""".trimIndent()


        processUserText(prompt, aiStarts = true)
    }

    fun stopInterview() {
        stopAll()
        aiText.value = "Interview ended."
        isInterviewStarted.value = false
        conversationHistory.clear() 
    }

    
    fun startConversationOnce() {
        if (listening.value || processing.value) return

        listening.value = true
        aiText.value = "🎙 Listening..."

        sttHelper.startListening(
            onPartial = { partial ->
                aiText.value = "You: $partial"
            },
            onFinal = { text ->
                listening.value = false
                if (text.isBlank()) {
                    aiText.value = "I didn’t hear anything. Try again."
                } else {
                    processUserText(text)
                }
            }
        )
    }

  
    private fun processUserText(userText: String, aiStarts: Boolean = false) {
        processing.value = true
        if (!aiStarts) aiText.value = "You said: $userText\nThinking..."

        conversationJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                
                conversationHistory.add(
                    GeminiContent(
                        role = "user",
                        parts = listOf(GeminiPart(text = userText))
                    )
                )

                
                val request = GeminiRequest(contents = conversationHistory)

    
                val response = geminiApi.generateContent(request)

                val reply = response.candidates
                    ?.firstOrNull()
                    ?.content
                    ?.parts
                    ?.firstOrNull()
                    ?.text
                    ?: "No response from AI."

                
                conversationHistory.add(
                    GeminiContent(
                        role = "model",
                        parts = listOf(GeminiPart(text = reply))
                    )
                )

              
                aiText.value = "AI: $reply"
                speakText(reply)

            } catch (ce: CancellationException) {
                Log.d("VoiceInterviewVM", "Conversation cancelled.")
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                aiText.value = "Server error: ${e.code()} - ${e.message()}"
                Log.e("VoiceInterviewVM", "HTTP Error: $errorBody", e)
            } catch (e: IOException) {
                aiText.value = "Network error. Please check your connection."
                Log.e("VoiceInterviewVM", "IO Error", e)
            } catch (e: Exception) {
                aiText.value = "Unexpected error: ${e.localizedMessage}"
                Log.e("VoiceInterviewVM", "Error", e)
            } finally {
                processing.value = false
            }
        }
    }

   
    private fun speakText(text: String) {
        val utteranceId = "tts_${System.currentTimeMillis()}"

        textToSpeech?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                Log.d("VoiceInterviewVM", "🔊 Speaking started...")
            }

            override fun onDone(utteranceId: String?) {
                Log.d("VoiceInterviewVM", "✅ Speaking done, restarting listening...")
                viewModelScope.launch(Dispatchers.Main) {
                    delay(800)
                    startConversationOnce()
                }
            }

            override fun onError(utteranceId: String?) {
                Log.e("VoiceInterviewVM", "❌ Error during TTS")
            }
        })

        textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
    }

    
    fun stopAll() {
        sttHelper.stopListening()
        conversationJob?.cancel()
        listening.value = false
        processing.value = false
        textToSpeech?.stop()
    }

    override fun onCleared() {
        super.onCleared()
        stopAll()
        textToSpeech?.shutdown()
    }
}
