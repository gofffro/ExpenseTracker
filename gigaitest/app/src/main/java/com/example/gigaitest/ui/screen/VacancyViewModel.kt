package com.example.gigaitest.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gigaitest.data.repository.VacancyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VacancyViewModel @Inject constructor(
    private val repository: VacancyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<VacancyUiState>(VacancyUiState.Idle)
    val uiState: StateFlow<VacancyUiState> = _uiState.asStateFlow()

    fun analyzeVacancy(vacancyText: String, skillsText: String) {
        if (vacancyText.isBlank() || skillsText.isBlank()) {
            _uiState.value = VacancyUiState.Error("Please fill in both fields")
            return
        }

        viewModelScope.launch {
            _uiState.value = VacancyUiState.Loading
            repository.analyzeVacancy(vacancyText, skillsText)
                .onSuccess { result ->
                    _uiState.value = VacancyUiState.Success(result)
                }
                .onFailure { error ->
                    _uiState.value = VacancyUiState.Error(error.message ?: "Unknown error")
                }
        }
    }

    fun clear() {
        _uiState.value = VacancyUiState.Idle
    }
}

sealed class VacancyUiState {
    object Idle : VacancyUiState()
    object Loading : VacancyUiState()
    data class Success(val analysis: String) : VacancyUiState()
    data class Error(val message: String) : VacancyUiState()
}
