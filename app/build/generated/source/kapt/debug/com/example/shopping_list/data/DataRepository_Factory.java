package com.example.shopping_list.data;

import com.example.shopping_list.data.room.DataSourceDB;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class DataRepository_Factory implements Factory<DataRepository> {
  private final Provider<DataSourceDB> dataSourceDBProvider;

  public DataRepository_Factory(Provider<DataSourceDB> dataSourceDBProvider) {
    this.dataSourceDBProvider = dataSourceDBProvider;
  }

  @Override
  public DataRepository get() {
    return newInstance(dataSourceDBProvider.get());
  }

  public static DataRepository_Factory create(Provider<DataSourceDB> dataSourceDBProvider) {
    return new DataRepository_Factory(dataSourceDBProvider);
  }

  public static DataRepository newInstance(DataSourceDB dataSourceDB) {
    return new DataRepository(dataSourceDB);
  }
}
