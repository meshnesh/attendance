package com.mw.churchattendance.ui.taglist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mw.churchattendance.data.repository.TagRepository

class TagListViewModelFactory(
    private val repository: TagRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TagViewModel::class.java)) {
            return TagViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}