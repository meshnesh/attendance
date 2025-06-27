package com.mw.churchattendance.data.local.dao.child

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.mw.churchattendance.data.local.entity.ChildWithParent
import com.mw.churchattendance.data.local.entity.child.Child

@Dao
interface ChildDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChild(child: Child)

    @Transaction
    @Query("SELECT * FROM children WHERE wristbandId = :wristbandId")
    suspend fun getChildWithParent(wristbandId: String): ChildWithParent?

    @Update
    suspend fun update(child: Child)

    @Transaction
    @Query("SELECT * FROM children WHERE parentFobId = :fobId LIMIT 1")
    suspend fun getChildByParentFob(fobId: String): ChildWithParent?
}