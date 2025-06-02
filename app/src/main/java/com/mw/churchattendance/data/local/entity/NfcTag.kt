package com.mw.churchattendance.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "nfc_tags", indices = [Index(value = ["tagId"], unique = true)])
data class NfcTag(
    @PrimaryKey val tagId: String,
    val childName: String? = null,
    val tagType: TagType,
    val isCheckedIn: Boolean = false,
    val isCheckedOut: Boolean = false,
    val lastActionTimestamp: Long = System.currentTimeMillis()
)
enum class TagType {
    WRISTBAND, // For check-in
    FOB        // For check-out
}
