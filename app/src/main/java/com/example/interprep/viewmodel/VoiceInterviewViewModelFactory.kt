package com.example.interprep.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class VoiceInterviewViewModelFactory(
    private val context: Context,
    private val geminiApiKey: String   // ✅ rename to match Gemini usage
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VoiceInterviewViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VoiceInterviewViewModel(context, geminiApiKey) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
