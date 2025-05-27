package com.mw.churchattendance.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nfc_tags")
data class NfcTag(
    @PrimaryKey(autoGenerate = false)
    val tagId: String, // NFC ID in hex or decimal

    val childName: String,

    val tagType: TagType, // WRISTBAND or FOB

    val isCheckedIn: Boolean = false,

    val isCheckedOut: Boolean = false,

    val lastActionTimestamp: Long = System.currentTimeMillis()
)

enum class TagType {
    WRISTBAND, // For check-in
    FOB        // For check-out
}
