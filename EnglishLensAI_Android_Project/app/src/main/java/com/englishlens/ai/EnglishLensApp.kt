package com.englishlens.ai

import android.app.Application
import androidx.room.Room
import com.englishlens.ai.data.local.AppDatabase

class EnglishLensApp : Application() {
    lateinit var database: AppDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "english_lens_ai.db"
        ).fallbackToDestructiveMigration().build()
    }
}
