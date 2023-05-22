package com.example.shopping_list.ui;

import com.example.shopping_list.data.DataRepository;
import com.example.shopping_list.entity.ErrorApp;
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
    "KotlinInternalInJava"
})
public final class AppViewModel_Factory implements Factory<AppViewModel> {
  private final Provider<ErrorApp> errorAppProvider;

  private final Provider<DataRepository> dataRepositoryProvider;

  public AppViewModel_Factory(Provider<ErrorApp> errorAppProvider,
      Provider<DataRepository> dataRepositoryProvider) {
    this.errorAppProvider = errorAppProvider;
    this.dataRepositoryProvider = dataRepositoryProvider;
  }

  @Override
  public AppViewModel get() {
    return newInstance(errorAppProvider.get(), dataRepositoryProvider.get());
  }

  public static AppViewModel_Factory create(Provider<ErrorApp> errorAppProvider,
      Provider<DataRepository> dataRepositoryProvider) {
    return new AppViewModel_Factory(errorAppProvider, dataRepositoryProvider);
  }

  public static AppViewModel newInstance(ErrorApp errorApp, DataRepository dataRepository) {
    return new AppViewModel(errorApp, dataRepository);
  }
}
