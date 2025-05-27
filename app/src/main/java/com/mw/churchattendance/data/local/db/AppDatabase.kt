package com.mw.churchattendance.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mw.churchattendance.data.converters.Converters
import com.mw.churchattendance.data.local.dao.NfcTagDao
import com.mw.churchattendance.data.local.entity.NfcTag


@Database(
    entities = [NfcTag::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun nfcTagDao(): NfcTagDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "church_attendance.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}