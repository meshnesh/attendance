package com.mw.churchattendance.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mw.churchattendance.data.converters.Converters
import com.mw.churchattendance.data.local.dao.attendance.AttendanceDao
import com.mw.churchattendance.data.local.dao.child.ChildDao
import com.mw.churchattendance.data.local.dao.parent.ParentDao
import com.mw.churchattendance.data.local.dao.tags.NfcTagDao
import com.mw.churchattendance.data.local.entity.attendance.Attendance
import com.mw.churchattendance.data.local.entity.child.Child
import com.mw.churchattendance.data.local.entity.parent.Parent
import com.mw.churchattendance.data.local.entity.tags.NfcTag

@Database(
    entities = [Child::class, Parent::class, NfcTag::class, Attendance::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun childDao(): ChildDao
    abstract fun parentDao(): ParentDao
    abstract fun nfcTagDao(): NfcTagDao
    abstract fun attendanceDao(): AttendanceDao
}