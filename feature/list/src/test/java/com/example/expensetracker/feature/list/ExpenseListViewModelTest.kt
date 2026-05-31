package com.example.expensetracker.feature.list

import com.example.expensetracker.domain.usecase.DeleteExpenseUseCase
import com.example.expensetracker.domain.usecase.GetExpensesUseCase
import com.example.expensetracker.domain.usecase.GetTotalAmountUseCase
import com.example.expensetracker.fakes.FakeAnalyticsService
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ExpenseListViewModelTest {

    private lateinit var viewModel: ExpenseListViewModel
    private lateinit var analyticsService: FakeAnalyticsService
    private val getExpensesUseCase = mockk<GetExpensesUseCase>(relaxed = true)
    private val getTotalAmountUseCase = mockk<GetTotalAmountUseCase>(relaxed = true)
    private val deleteExpenseUseCase = mockk<DeleteExpenseUseCase>(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        analyticsService = FakeAnalyticsService()
        viewModel = ExpenseListViewModel(
            getExpensesUseCase,
            getTotalAmountUseCase,
            deleteExpenseUseCase,
            analyticsService
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init should track screen_viewed event`() {
        val event = analyticsService.events.find { it.first == "screen_viewed" }
        assert(event != null)
        assertEquals("ExpenseList", event?.second?.get("screen_name"))
    }

    @Test
    fun `deleteExpense should track expense_deleted event`() {
        val expense = com.example.expensetracker.domain.model.Expense(
            id = 1,
            title = "Lunch",
            amount = 100.0,
            category = "Food",
            date = "2023-10-27"
        )
        
        viewModel.deleteExpense(expense)
        testDispatcher.scheduler.runCurrent()
        
        val event = analyticsService.events.find { it.first == "expense_deleted" }
        assert(event != null)
        assertEquals("Food", event?.second?.get("category"))
    }

    @Test
    fun `filterByCategory should track category_filtered event`() {
        viewModel.filterByCategory("Work")
        
        val event = analyticsService.events.find { it.first == "category_filtered" }
        assert(event != null)
        assertEquals("Work", event?.second?.get("category"))
    }
}
