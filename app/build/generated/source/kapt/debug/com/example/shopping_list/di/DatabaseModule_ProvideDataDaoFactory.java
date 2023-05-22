package com.example.shopping_list.di;

import com.example.shopping_list.data.room.AppDatabase;
import com.example.shopping_list.data.room.DataDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideDataDaoFactory implements Factory<DataDao> {
  private final Provider<AppDatabase> databaseProvider;

  public DatabaseModule_ProvideDataDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public DataDao get() {
    return provideDataDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideDataDaoFactory create(
      Provider<AppDatabase> databaseProvider) {
    return new DatabaseModule_ProvideDataDaoFactory(databaseProvider);
  }

  public static DataDao provideDataDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideDataDao(database));
  }
}
