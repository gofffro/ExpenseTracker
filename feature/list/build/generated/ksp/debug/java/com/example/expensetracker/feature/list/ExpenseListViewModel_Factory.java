package com.example.expensetracker.feature.list;

import com.example.expensetracker.core.analytics.AnalyticsService;
import com.example.expensetracker.domain.usecase.DeleteExpenseUseCase;
import com.example.expensetracker.domain.usecase.GetExpensesUseCase;
import com.example.expensetracker.domain.usecase.GetTotalAmountUseCase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class ExpenseListViewModel_Factory implements Factory<ExpenseListViewModel> {
  private final Provider<GetExpensesUseCase> getExpensesUseCaseProvider;

  private final Provider<GetTotalAmountUseCase> getTotalAmountUseCaseProvider;

  private final Provider<DeleteExpenseUseCase> deleteExpenseUseCaseProvider;

  private final Provider<AnalyticsService> analyticsServiceProvider;

  public ExpenseListViewModel_Factory(Provider<GetExpensesUseCase> getExpensesUseCaseProvider,
      Provider<GetTotalAmountUseCase> getTotalAmountUseCaseProvider,
      Provider<DeleteExpenseUseCase> deleteExpenseUseCaseProvider,
      Provider<AnalyticsService> analyticsServiceProvider) {
    this.getExpensesUseCaseProvider = getExpensesUseCaseProvider;
    this.getTotalAmountUseCaseProvider = getTotalAmountUseCaseProvider;
    this.deleteExpenseUseCaseProvider = deleteExpenseUseCaseProvider;
    this.analyticsServiceProvider = analyticsServiceProvider;
  }

  @Override
  public ExpenseListViewModel get() {
    return newInstance(getExpensesUseCaseProvider.get(), getTotalAmountUseCaseProvider.get(), deleteExpenseUseCaseProvider.get(), analyticsServiceProvider.get());
  }

  public static ExpenseListViewModel_Factory create(
      Provider<GetExpensesUseCase> getExpensesUseCaseProvider,
      Provider<GetTotalAmountUseCase> getTotalAmountUseCaseProvider,
      Provider<DeleteExpenseUseCase> deleteExpenseUseCaseProvider,
      Provider<AnalyticsService> analyticsServiceProvider) {
    return new ExpenseListViewModel_Factory(getExpensesUseCaseProvider, getTotalAmountUseCaseProvider, deleteExpenseUseCaseProvider, analyticsServiceProvider);
  }

  public static ExpenseListViewModel newInstance(GetExpensesUseCase getExpensesUseCase,
      GetTotalAmountUseCase getTotalAmountUseCase, DeleteExpenseUseCase deleteExpenseUseCase,
      AnalyticsService analyticsService) {
    return new ExpenseListViewModel(getExpensesUseCase, getTotalAmountUseCase, deleteExpenseUseCase, analyticsService);
  }
}
