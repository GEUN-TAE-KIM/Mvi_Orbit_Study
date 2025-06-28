package com.example.jetpack_mvi_orbit_study.di

import android.content.Context
import androidx.room.Room
import com.example.jetpack_mvi_orbit_study.data.local.AppDatabase
import com.example.jetpack_mvi_orbit_study.data.local.MessageDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDb(@ApplicationContext ctx: Context): AppDatabase =
        Room.databaseBuilder(ctx, AppDatabase::class.java, "app.db").build()

    @Provides
    fun provideDao(db: AppDatabase): MessageDao = db.messageDao()
}