package com.mw.churchattendance.ui.attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mw.churchattendance.data.local.entity.attendance.Attendance
import com.mw.churchattendance.data.repository.TagRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(
    private val repository: TagRepository
) : ViewModel() {

    val todaysAttendance: StateFlow<List<Attendance>> =
        repository.getTodaysAttendance()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}