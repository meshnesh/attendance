package com.mw.churchattendance.data.local.dao.parent

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.mw.churchattendance.data.local.entity.parent.Parent

@Dao
interface ParentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParent(parent: Parent)
}