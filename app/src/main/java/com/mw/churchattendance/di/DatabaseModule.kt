package com.mw.churchattendance.di

import android.content.Context
import androidx.room.Room
import com.mw.churchattendance.data.local.dao.NfcTagDao
import com.mw.churchattendance.data.local.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "church_attendance.db"
        ).build()
    }

    @Provides
    fun provideNfcTagDao(db: AppDatabase): NfcTagDao = db.nfcTagDao()
}