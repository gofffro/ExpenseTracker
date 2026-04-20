package com.example.expensetracker.feature.detail;

import com.example.expensetracker.domain.usecase.DeleteExpenseUseCase;
import com.example.expensetracker.domain.usecase.GetExpensesUseCase;
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
public final class ExpenseDetailViewModel_Factory implements Factory<ExpenseDetailViewModel> {
  private final Provider<GetExpensesUseCase> getExpensesUseCaseProvider;

  private final Provider<DeleteExpenseUseCase> deleteExpenseUseCaseProvider;

  public ExpenseDetailViewModel_Factory(Provider<GetExpensesUseCase> getExpensesUseCaseProvider,
      Provider<DeleteExpenseUseCase> deleteExpenseUseCaseProvider) {
    this.getExpensesUseCaseProvider = getExpensesUseCaseProvider;
    this.deleteExpenseUseCaseProvider = deleteExpenseUseCaseProvider;
  }

  @Override
  public ExpenseDetailViewModel get() {
    return newInstance(getExpensesUseCaseProvider.get(), deleteExpenseUseCaseProvider.get());
  }

  public static ExpenseDetailViewModel_Factory create(
      Provider<GetExpensesUseCase> getExpensesUseCaseProvider,
      Provider<DeleteExpenseUseCase> deleteExpenseUseCaseProvider) {
    return new ExpenseDetailViewModel_Factory(getExpensesUseCaseProvider, deleteExpenseUseCaseProvider);
  }

  public static ExpenseDetailViewModel newInstance(GetExpensesUseCase getExpensesUseCase,
      DeleteExpenseUseCase deleteExpenseUseCase) {
    return new ExpenseDetailViewModel(getExpensesUseCase, deleteExpenseUseCase);
  }
}
