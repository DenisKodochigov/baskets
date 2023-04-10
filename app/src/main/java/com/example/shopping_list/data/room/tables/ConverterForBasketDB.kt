package com.example.shopping_list.data.room.tables

import androidx.room.TypeConverter
import com.example.shopping_list.entity.Product
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.lang.reflect.ParameterizedType

/** Converter class for adding the FILE class to the database table: FilmDB*/
class ConverterForBasketDB {
    // Converter class List<Country> to string and back
    private val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val typeListProduct: ParameterizedType = Types.newParameterizedType(List::class.java, Product::class.java)
    private val productAdapter: JsonAdapter<List<Product>> = moshi.adapter(typeListProduct)
    @TypeConverter
    fun productsToJSON(source: List<Product>?): String = productAdapter.toJson(source)
    @TypeConverter
    fun productsFromJSON(sourceStr: String) = productAdapter.fromJson(sourceStr)
}