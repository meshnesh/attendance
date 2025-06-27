package com.mw.churchattendance.ui.taglist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mw.churchattendance.data.local.entity.ChildWithParent
import com.mw.churchattendance.data.local.entity.child.Child
import com.mw.churchattendance.data.local.entity.parent.Parent
import com.mw.churchattendance.data.local.entity.tags.NfcTag
import com.mw.churchattendance.data.repository.TagRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TagViewModel @Inject constructor(
    private val repository: TagRepository
) : ViewModel() {

    private val _allTags = MutableStateFlow<List<NfcTag>>(emptyList())
    val allTags = _allTags.asStateFlow()

    private val _scannedChild = MutableStateFlow<ChildWithParent?>(null)
    val scannedChild = _scannedChild.asStateFlow()

    init {
        viewModelScope.launch {
            repository.allTags.collect {
                _allTags.value = it
            }
        }
    }

    fun insertTag(tag: NfcTag) = viewModelScope.launch {
        repository.insert(tag)
    }

    fun updateTag(tag: NfcTag) = viewModelScope.launch {
        repository.update(tag)
    }

    fun getTagById(tagId: String, onResult: (NfcTag?) -> Unit) {
        viewModelScope.launch {
            val tag = repository.getByTagId(tagId)
            onResult(tag)
        }
    }

    fun saveProfile(child: Child, parent: Parent) {
        viewModelScope.launch {
            repository.insertParent(parent)
            repository.insertChild(child)
        }
    }

    fun getChildWithParent(tagId: String, callback: (ChildWithParent?) -> Unit) {
        viewModelScope.launch {
            val result = repository.getChildWithParent(tagId)
            callback(result)
        }
    }
}