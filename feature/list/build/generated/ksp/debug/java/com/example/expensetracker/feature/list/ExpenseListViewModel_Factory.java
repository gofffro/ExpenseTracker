package com.example.expensetracker.feature.list;

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

  public ExpenseListViewModel_Factory(Provider<GetExpensesUseCase> getExpensesUseCaseProvider,
      Provider<GetTotalAmountUseCase> getTotalAmountUseCaseProvider,
      Provider<DeleteExpenseUseCase> deleteExpenseUseCaseProvider) {
    this.getExpensesUseCaseProvider = getExpensesUseCaseProvider;
    this.getTotalAmountUseCaseProvider = getTotalAmountUseCaseProvider;
    this.deleteExpenseUseCaseProvider = deleteExpenseUseCaseProvider;
  }

  @Override
  public ExpenseListViewModel get() {
    return newInstance(getExpensesUseCaseProvider.get(), getTotalAmountUseCaseProvider.get(), deleteExpenseUseCaseProvider.get());
  }

  public static ExpenseListViewModel_Factory create(
      Provider<GetExpensesUseCase> getExpensesUseCaseProvider,
      Provider<GetTotalAmountUseCase> getTotalAmountUseCaseProvider,
      Provider<DeleteExpenseUseCase> deleteExpenseUseCaseProvider) {
    return new ExpenseListViewModel_Factory(getExpensesUseCaseProvider, getTotalAmountUseCaseProvider, deleteExpenseUseCaseProvider);
  }

  public static ExpenseListViewModel newInstance(GetExpensesUseCase getExpensesUseCase,
      GetTotalAmountUseCase getTotalAmountUseCase, DeleteExpenseUseCase deleteExpenseUseCase) {
    return new ExpenseListViewModel(getExpensesUseCase, getTotalAmountUseCase, deleteExpenseUseCase);
  }
}
