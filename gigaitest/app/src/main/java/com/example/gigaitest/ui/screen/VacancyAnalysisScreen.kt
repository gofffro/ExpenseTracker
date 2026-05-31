package com.example.gigaitest.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VacancyAnalysisScreen(
    viewModel: VacancyViewModel = hiltViewModel()
) {
    var vacancyText by remember { mutableStateOf("") }
    var skillsText by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Vacancy Analysis") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = vacancyText,
                onValueChange = { vacancyText = it },
                label = { Text("Vacancy Text") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            OutlinedTextField(
                value = skillsText,
                onValueChange = { skillsText = it },
                label = { Text("My Skills") },
                placeholder = { Text("Kotlin, Android SDK, Retrofit, 1 year experience...") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { viewModel.analyzeVacancy(vacancyText, skillsText) },
                    modifier = Modifier.weight(1f),
                    enabled = uiState !is VacancyUiState.Loading && vacancyText.isNotBlank() && skillsText.isNotBlank()
                ) {
                    Text("Analyze")
                }

                OutlinedButton(
                    onClick = {
                        vacancyText = ""
                        skillsText = ""
                        viewModel.clear()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Clear")
                }
            }

            when (val state = uiState) {
                is VacancyUiState.Loading -> {
                    CircularProgressIndicator()
                }
                is VacancyUiState.Success -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Text(
                            text = state.analysis,
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                is VacancyUiState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                else -> {}
            }
        }
    }
}
