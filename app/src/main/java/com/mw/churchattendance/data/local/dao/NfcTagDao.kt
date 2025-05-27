package com.mw.churchattendance.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mw.churchattendance.data.local.entity.NfcTag
import kotlinx.coroutines.flow.Flow

@Dao
interface NfcTagDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTag(tag: NfcTag)

    @Update
    suspend fun updateTag(tag: NfcTag)

    @Delete
    suspend fun deleteTag(tag: NfcTag)

    @Query("SELECT * FROM nfc_tags WHERE tagId = :tagId LIMIT 1")
    suspend fun getTagById(tagId: String): NfcTag?

    @Query("SELECT * FROM nfc_tags")
    fun getAllTags(): Flow<List<NfcTag>>

    @Query("UPDATE nfc_tags SET isCheckedIn = :checkedIn, lastActionTimestamp = :timestamp WHERE tagId = :tagId")
    suspend fun updateCheckInStatus(tagId: String, checkedIn: Boolean, timestamp: Long)

    @Query("UPDATE nfc_tags SET isCheckedOut = 1 WHERE tagId = :tagId")
    suspend fun markAsCheckedOut(tagId: String)
}