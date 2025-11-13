package com.example.interprep.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.interprep.data.repository.ResumeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class ResumeViewModel : ViewModel() {

    private val repository = ResumeRepository()


    private val _resumeText = MutableStateFlow("")
    val resumeText: StateFlow<String> = _resumeText

    private val _feedback = MutableStateFlow("")
    val feedback: StateFlow<String> = _feedback

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error



    fun setResumeText(text: String) {
        _resumeText.value = text
    }


    fun analyzeResume(role: String = "Android Developer") {
        viewModelScope.launch {
            if (_resumeText.value.isBlank()) {
                _feedback.value = "⚠ Please upload a resume first."
                return@launch
            }

            try {
                _isLoading.value = true
                _error.value = null

                val result = repository.analyzeResume(_resumeText.value, role)
                _feedback.value = result

            } catch (e: Exception) {
                _error.value = "❌ Error analyzing resume: ${e.localizedMessage ?: "Unknown error"}"
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun clear() {
        _resumeText.value = ""
        _feedback.value = ""
        _error.value = null
    }
}
