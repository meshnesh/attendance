package com.mw.churchattendance.data.local.entity.attendance

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mw.churchattendance.data.local.entity.child.Child

@Entity(
    tableName = "attendance",
    foreignKeys = [
        ForeignKey(
            entity = Child::class,
            parentColumns = ["wristbandId"],
            childColumns = ["childTagId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["childTagId"])]
)
data class Attendance(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val childTagId: String,
    val childName: String,
    val className: String,
    val checkInTime: Long
)
