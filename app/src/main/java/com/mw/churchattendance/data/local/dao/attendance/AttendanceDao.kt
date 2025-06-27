package com.mw.churchattendance.data.local.dao.attendance

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mw.churchattendance.data.local.entity.attendance.Attendance
import kotlinx.coroutines.flow.Flow

@Dao
interface AttendanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendance(attendance: Attendance)

    @Query("SELECT * FROM attendance WHERE className = :className ORDER BY checkInTime DESC")
    fun getAttendanceByClass(className: String): Flow<List<Attendance>>

    @Query("SELECT * FROM attendance WHERE checkInTime >= :startOfDay ORDER BY checkInTime DESC")
    fun getTodaysAttendance(startOfDay: Long): Flow<List<Attendance>>
}