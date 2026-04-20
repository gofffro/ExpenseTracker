package com.example.expensetracker.feature.add;

import com.example.expensetracker.domain.usecase.AddExpenseUseCase;
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
public final class AddExpenseViewModel_Factory implements Factory<AddExpenseViewModel> {
  private final Provider<AddExpenseUseCase> addExpenseUseCaseProvider;

  public AddExpenseViewModel_Factory(Provider<AddExpenseUseCase> addExpenseUseCaseProvider) {
    this.addExpenseUseCaseProvider = addExpenseUseCaseProvider;
  }

  @Override
  public AddExpenseViewModel get() {
    return newInstance(addExpenseUseCaseProvider.get());
  }

  public static AddExpenseViewModel_Factory create(
      Provider<AddExpenseUseCase> addExpenseUseCaseProvider) {
    return new AddExpenseViewModel_Factory(addExpenseUseCaseProvider);
  }

  public static AddExpenseViewModel newInstance(AddExpenseUseCase addExpenseUseCase) {
    return new AddExpenseViewModel(addExpenseUseCase);
  }
}
