package com.example.data.di

import com.example.data.datasource.MessageLocalDataSource
import com.example.data.datasource.MessageLocalDataSourceImpl
import com.example.data.datasource.MessageRemoteDataSource
import com.example.data.datasource.MessageRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindMessageLocalDataSource(
        impl: MessageLocalDataSourceImpl
    ): MessageLocalDataSource

    @Binds
    @Singleton
    abstract fun bindMessageRemoteDataSource(
        impl: MessageRemoteDataSourceImpl
    ): MessageRemoteDataSource
}