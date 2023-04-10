package com.example.shopping_list.data.room

import android.content.Context
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

class ProviderDao {
    @InstallIn(SingletonComponent::class)
    @EntryPoint
    interface DataDaoEntryPoint {
        fun dataDao(): DataDao
    }
    fun getDataDao(appContext: Context): DataDao {

        return EntryPointAccessors.fromApplication(
            appContext, DataDaoEntryPoint::class.java).dataDao()
    }
}