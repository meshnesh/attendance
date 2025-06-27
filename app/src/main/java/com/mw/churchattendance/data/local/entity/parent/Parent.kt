package com.mw.churchattendance.data.local.entity.parent

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "parents")
data class Parent(
    @PrimaryKey val fobId: String,
    val name: String,
    val phone: String
)