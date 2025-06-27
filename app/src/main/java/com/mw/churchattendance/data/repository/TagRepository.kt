package com.mw.churchattendance.data.repository

import com.mw.churchattendance.data.local.dao.attendance.AttendanceDao
import com.mw.churchattendance.data.local.dao.child.ChildDao
import com.mw.churchattendance.data.local.dao.parent.ParentDao
import com.mw.churchattendance.data.local.dao.tags.NfcTagDao
import com.mw.churchattendance.data.local.entity.ChildWithParent
import com.mw.churchattendance.data.local.entity.attendance.Attendance
import com.mw.churchattendance.data.local.entity.child.Child
import com.mw.churchattendance.data.local.entity.parent.Parent
import com.mw.churchattendance.data.local.entity.tags.NfcTag
import kotlinx.coroutines.flow.Flow
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TagRepository @Inject constructor(
    private val dao: NfcTagDao,
    private val childDao: ChildDao,
    private val parentDao: ParentDao,
    private val attendanceDao: AttendanceDao
) {
    val allTags: Flow<List<NfcTag>> = dao.getAllTags()

    suspend fun insert(tag: NfcTag) = dao.insertTag(tag)

    suspend fun update(tag: NfcTag) = dao.updateTag(tag)

    suspend fun delete(tag: NfcTag) = dao.deleteTag(tag)

    suspend fun getByTagId(tagId: String): NfcTag? = dao.getTagById(tagId)

    suspend fun checkOutChild(tagId: String) = dao.markAsCheckedOut(tagId)

    suspend fun setCheckInStatus(tagId: String, checkedIn: Boolean) {
        val timestamp = System.currentTimeMillis()
        dao.updateCheckInStatus(tagId, checkedIn, timestamp)
    }

    suspend fun insertChild(child: Child) = childDao.insertChild(child)

    suspend fun updateChild(child: Child) {
        childDao.update(child)
    }

    suspend fun insertParent(parent: Parent) = parentDao.insertParent(parent)

    suspend fun getChildWithParent(wristbandId: String): ChildWithParent? {
        return childDao.getChildWithParent(wristbandId)
    }

    suspend fun saveAttendance(attendance: Attendance) {
        attendanceDao.insertAttendance(attendance)
    }

    fun getAttendanceByClass(className: String): Flow<List<Attendance>> {
        return attendanceDao.getAttendanceByClass(className)
    }

    fun getTodaysAttendance(): Flow<List<Attendance>> {
        val startOfDay = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
        return attendanceDao.getTodaysAttendance(startOfDay)
    }

    suspend fun getChildByParentFob(fobId: String): ChildWithParent? {
        return childDao.getChildByParentFob(fobId)
    }
}