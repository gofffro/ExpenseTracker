package com.example.expensetracker.feature.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensetracker.domain.model.Expense
import androidx.hilt.navigation.compose.hiltViewModel

import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import android.view.ViewGroup

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseListScreen(
    viewModel: ExpenseListViewModel = hiltViewModel(),
    onAddExpenseClick: () -> Unit,
    onExpenseClick: (Expense) -> Unit
) {
    val expenses by viewModel.allExpenses.collectAsState()
    val totalAmount by viewModel.totalAmount.collectAsState()
    var selectedCategory by remember { mutableStateOf("All") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Expense Tracker") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddExpenseClick) {
                Icon(Icons.Default.Add, contentDescription = "Добавить расход")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            TotalAmountCard(totalAmount)
            
            if (expenses.isNotEmpty()) {
                Box(modifier = Modifier.height(200.dp).fillMaxWidth()) {
                    ExpensePieChart(expenses)
                }
            }

            val categoryMap = mapOf(
                "All" to "Все",
                "Food" to "Еда",
                "Transport" to "Транспорт",
                "Entertainment" to "Развлечения",
                "Health" to "Здоровье",
                "Other" to "Прочее"
            )

            var selectedCategoryDisplay by remember { mutableStateOf("Все") }

            CategoryFilter(
                selectedCategory = selectedCategoryDisplay,
                onCategorySelected = { display ->
                    selectedCategoryDisplay = display
                    val internal = categoryMap.entries.find { it.value == display }?.key ?: "All"
                    viewModel.filterByCategory(internal)
                }
            )
            
            if (expenses.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Расходы не найдены", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(expenses) { expense ->
                        ExpenseItem(
                            expense = expense,
                            onClick = { onExpenseClick(expense) },
                            onDelete = { viewModel.deleteExpense(expense) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExpensePieChart(expenses: List<Expense>) {
    val categoryTotals = expenses
        .groupBy { it.category }
        .mapValues { entry -> entry.value.sumOf { it.amount }.toFloat() }

    val categoryColorMap = mapOf(
        "Food" to android.graphics.Color.parseColor("#FF9800"),
        "Transport" to android.graphics.Color.parseColor("#2196F3"),
        "Entertainment" to android.graphics.Color.parseColor("#9C27B0"),
        "Health" to android.graphics.Color.parseColor("#F44336"),
        "Other" to android.graphics.Color.parseColor("#607D8B")
    )

    AndroidView(
        factory = { context ->
            PieChart(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                description.isEnabled = false
                isDrawHoleEnabled = true
                holeRadius = 40f
                setHoleColor(android.graphics.Color.TRANSPARENT)
                legend.isEnabled = true
                legend.textSize = 11f
                setEntryLabelColor(android.graphics.Color.WHITE)
                animateY(800)
            }
        },
        modifier = Modifier.fillMaxSize(),
        update = { chart ->
            val categoryDisplayMap = mapOf(
                "Food" to "Еда",
                "Transport" to "Транспорт",
                "Entertainment" to "Развлечения",
                "Health" to "Здоровье",
                "Other" to "Прочее"
            )
            val entries = categoryTotals.map { (category, total) ->
                PieEntry(total, categoryDisplayMap[category] ?: category)
            }

            val colors = categoryTotals.keys.map { category ->
                categoryColorMap[category] ?: android.graphics.Color.parseColor("#607D8B")
            }

            val dataSet = PieDataSet(entries, "").apply {
                this.colors = colors
                valueTextSize = 12f
                valueTextColor = android.graphics.Color.WHITE
            }

            chart.data = PieData(dataSet)
            chart.invalidate()
        }
    )
}

@Composable
fun TotalAmountCard(amount: Double) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Общий баланс", style = MaterialTheme.typography.titleMedium)
            Text(
                text = "${String.format("%.2f", amount)} ₽",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Composable
fun CategoryFilter(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    val categories = listOf("Все", "Еда", "Транспорт", "Развлечения", "Здоровье", "Прочее")
    ScrollableTabRow(
        selectedTabIndex = categories.indexOf(selectedCategory),
        edgePadding = 16.dp,
        divider = {},
        containerColor = Color.Transparent
    ) {
        categories.forEach { category ->
            Tab(
                selected = selectedCategory == category,
                onClick = { onCategorySelected(category) },
                text = { Text(category) }
            )
        }
    }
}

@Composable
fun ExpenseItem(
    expense: Expense,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(expense.title, style = MaterialTheme.typography.titleLarge)
                val categoryMap = mapOf(
                    "Food" to "Еда",
                    "Transport" to "Транспорт",
                    "Entertainment" to "Развлечения",
                    "Health" to "Здоровье",
                    "Other" to "Прочее"
                )
                Text(categoryMap[expense.category] ?: expense.category, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                Text(expense.date, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${String.format("%.2f", expense.amount)} ₽",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Удалить", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
