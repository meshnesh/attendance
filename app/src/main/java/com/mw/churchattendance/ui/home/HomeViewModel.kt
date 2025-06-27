package com.mw.churchattendance.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mw.churchattendance.data.local.entity.ChildWithParent
import com.mw.churchattendance.data.local.entity.attendance.Attendance
import com.mw.churchattendance.data.local.entity.child.Child
import com.mw.churchattendance.data.repository.TagRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: TagRepository
) : ViewModel() {

    private val _scannedChild = MutableStateFlow<ChildWithParent?>(null)
    val scannedChild = _scannedChild.asStateFlow()

    fun fetchChildWithParentByWristbandId(wristbandId: String) {
        viewModelScope.launch {
            val result = repository.getChildWithParent(wristbandId)
            _scannedChild.value = result
        }
    }

    fun checkInChild(child: Child) {
        viewModelScope.launch {
            val attendance = Attendance(
                childTagId = child.wristbandId,
                childName = child.name,
                className = child.childClass,
                checkInTime = System.currentTimeMillis()
            )
            repository.saveAttendance(attendance)

            // Update the current scanned child with new check-in info
            val current = _scannedChild.value
            if (current != null) {
                val updatedChild = child.copy(
                    isCheckedIn = true,
                    lastCheckIn = attendance.checkInTime
                )

                repository.updateChild(updatedChild) // <-- Add this to persist to DB

                _scannedChild.value = current.copy(child = updatedChild)
            }
        }
    }

    val lastCheckInText = scannedChild.map { childWithParent ->
        childWithParent?.child?.lastCheckIn?.let {
            "Last Checked In: " + SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault()).format(Date(it))
        } ?: "Last Checked In: Never"
    }.stateIn(viewModelScope, SharingStarted.Eagerly, "")

//    fun checkOutByParentFob(fobId: String, onResult: (Boolean) -> Unit) {
//        viewModelScope.launch {
//            val childWithParent = repository.getChildByParentFob(fobId)
//            if (childWithParent != null && childWithParent.child.isCheckedIn) {
//                val updatedChild = childWithParent.child.copy(
//                    isCheckedIn = false,
//                    lastCheckIn = System.currentTimeMillis() // optional to track check-out time
//                )
//                repository.updateChild(updatedChild)
//                _scannedChild.value = childWithParent.copy(child = updatedChild)
//                onResult(true)
//            } else {
//                onResult(false)
//            }
//        }
//    }

    fun checkOutByParentFob(fobId: String, onResult: (ChildWithParent?) -> Unit) {
        viewModelScope.launch {
            val result = repository.getChildByParentFob(fobId)
            onResult(result)
        }
    }

    fun performCheckout(child: Child) {
        viewModelScope.launch {
            val updated = child.copy(isCheckedIn = false)
            repository.updateChild(updated)
            _scannedChild.value = null
        }
    }

    fun fetchChildWithParentByWristbandId(wristbandId: String, callback: (ChildWithParent?) -> Unit) {
        viewModelScope.launch {
            val result = repository.getChildWithParent(wristbandId)
            callback(result)
        }
    }

    fun setScannedChild(childWithParent: ChildWithParent) {
        _scannedChild.value = childWithParent
    }
}