package com.mw.churchattendance.ui.taglist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mw.churchattendance.data.local.entity.NfcTag
import com.mw.churchattendance.data.repository.TagRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


class TagViewModel(private val repository: TagRepository) : ViewModel() {

    val allTags: StateFlow<List<NfcTag>> = repository.allTags
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun insertTag(tag: NfcTag) {
        viewModelScope.launch {
            repository.insert(tag)
        }
    }

    fun updateTag(tag: NfcTag) {
        viewModelScope.launch {
            repository.update(tag)
        }
    }

    fun checkOutChild(tagId: String) = viewModelScope.launch {
        repository.checkOutChild(tagId)
    }


    fun getTagById(tagId: String, onResult: (NfcTag?) -> Unit) = viewModelScope.launch {
        val tag = repository.getByTagId(tagId)
        onResult(tag)
    }

    fun deleteTag(tag: NfcTag) {
        viewModelScope.launch {
            repository.delete(tag)
        }
    }
}