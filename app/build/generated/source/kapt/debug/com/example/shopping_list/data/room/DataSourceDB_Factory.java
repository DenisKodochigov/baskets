package com.example.shopping_list.data.room;

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
public final class DataSourceDB_Factory implements Factory<DataSourceDB> {
  private final Provider<DataDao> dataDaoProvider;

  public DataSourceDB_Factory(Provider<DataDao> dataDaoProvider) {
    this.dataDaoProvider = dataDaoProvider;
  }

  @Override
  public DataSourceDB get() {
    return newInstance(dataDaoProvider.get());
  }

  public static DataSourceDB_Factory create(Provider<DataDao> dataDaoProvider) {
    return new DataSourceDB_Factory(dataDaoProvider);
  }

  public static DataSourceDB newInstance(DataDao dataDao) {
    return new DataSourceDB(dataDao);
  }
}
