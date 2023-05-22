package com.example.shopping_list.entity;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class ErrorApp_Factory implements Factory<ErrorApp> {
  private final Provider<Context> contextProvider;

  public ErrorApp_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public ErrorApp get() {
    return newInstance(contextProvider.get());
  }

  public static ErrorApp_Factory create(Provider<Context> contextProvider) {
    return new ErrorApp_Factory(contextProvider);
  }

  public static ErrorApp newInstance(Context context) {
    return new ErrorApp(context);
  }
}
