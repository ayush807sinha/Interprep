package com.example.interprep.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.interprep.data.repo.QuestionRepository
import com.example.interprep.network.PerplexityService.Question
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


@HiltViewModel
class QuestionGeneratorViewModel @Inject constructor(
    private val repository: QuestionRepository
) : ViewModel() {


    private val _question = MutableStateFlow<Question?>(null)
    val question: StateFlow<Question?> = _question


    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading


    private val _showAnswer = MutableStateFlow(false)
    val showAnswer: StateFlow<Boolean> = _showAnswer


    private val _selectedOption = MutableStateFlow<String?>(null)
    val selectedOption: StateFlow<String?> = _selectedOption


    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage


    fun generateQuestion(category: String) {
        viewModelScope.launch {
            _loading.value = true
            _showAnswer.value = false
            _selectedOption.value = null
            _errorMessage.value = null

            try {
                val newQuestion = repository.generateQuestion(category)
                _question.value = newQuestion
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Something went wrong"
                _question.value = Question(
                    questionText = "Error fetching question.",
                    category = category
                )
            } finally {
                _loading.value = false
            }
        }
    }


    fun selectOption(option: String) {
        _selectedOption.value = option
    }


    fun toggleAnswerVisibility() {
        _showAnswer.value = !_showAnswer.value
    }
}