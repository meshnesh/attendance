package com.mw.churchattendance.data.local.entity.child

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mw.churchattendance.data.local.entity.parent.Parent

@Entity(
    tableName = "children",
    indices = [Index(value = ["parentFobId"])],
    foreignKeys = [ForeignKey(
        entity = Parent::class,
        parentColumns = ["fobId"],
        childColumns = ["parentFobId"],
        onDelete = ForeignKey.Companion.CASCADE
    )]
)
data class Child(
    @PrimaryKey val wristbandId: String,
    val name: String,
    val dob: Long,
    val childClass: String,
    val parentFobId: String,
    val isCheckedIn: Boolean = false,
    val lastCheckIn: Long? = null
)