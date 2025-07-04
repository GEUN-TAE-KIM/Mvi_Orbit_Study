package com.example.jetpack_mvi_orbit_study.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MessageEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao
}