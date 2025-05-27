package com.mw.churchattendance.data.repository

import com.mw.churchattendance.data.local.dao.NfcTagDao
import com.mw.churchattendance.data.local.entity.NfcTag
import kotlinx.coroutines.flow.Flow

class TagRepository(private val dao: NfcTagDao) {

    val allTags: Flow<List<NfcTag>> = dao.getAllTags()

    suspend fun insert(tag: NfcTag) {
        dao.insertTag(tag)
    }

    suspend fun update(tag: NfcTag) {
        dao.updateTag(tag)
    }

    suspend fun delete(tag: NfcTag) {
        dao.deleteTag(tag)
    }

    suspend fun getByTagId(tagId: String): NfcTag? {
        return dao.getTagById(tagId)
    }

    suspend fun checkOutChild(tagId: String) {
        dao.markAsCheckedOut(tagId)
    }

    suspend fun setCheckInStatus(tagId: String, checkedIn: Boolean) {
        val timestamp = System.currentTimeMillis()
        dao.updateCheckInStatus(tagId, checkedIn, timestamp)
    }
}