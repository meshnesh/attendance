package com.mw.churchattendance.ui.taglist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mw.churchattendance.R
import com.mw.churchattendance.data.local.db.AppDatabase
import com.mw.churchattendance.data.repository.TagRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TagListFragment : Fragment(R.layout.fragment_tag_list) {

    private lateinit var viewModel: TagViewModel
    private lateinit var adapter: TagListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up DAO, Repository, and ViewModel Factory
        val dao = AppDatabase.getDatabase(requireContext()).nfcTagDao()
        val repository = TagRepository(dao)
        val factory = TagListViewModelFactory(repository)

        viewModel = ViewModelProvider(this, factory)[TagViewModel::class.java]

        // Set up RecyclerView
        adapter = TagListAdapter()
        view.findViewById<RecyclerView>(R.id.recyclerTags).apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = this@TagListFragment.adapter
        }

        // Observe data with lifecycle-aware coroutine
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.allTags.collectLatest { tags ->
                    adapter.submitList(tags)
                }
            }
        }
    }
}