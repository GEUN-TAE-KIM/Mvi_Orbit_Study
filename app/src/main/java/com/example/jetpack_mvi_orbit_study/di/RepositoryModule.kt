package com.example.jetpack_mvi_orbit_study.di

import com.example.jetpack_mvi_orbit_study.data.MessageRepository
import com.example.jetpack_mvi_orbit_study.data.local.MessageDao
import com.example.jetpack_mvi_orbit_study.data.remote.MessageApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideRepo(api: MessageApi, dao: MessageDao) = MessageRepository(api, dao)
}