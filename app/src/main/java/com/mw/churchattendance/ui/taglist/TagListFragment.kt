package com.mw.churchattendance.ui.taglist

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mw.churchattendance.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TagListFragment : Fragment(R.layout.fragment_tag_list) {

    private val viewModel: TagViewModel by viewModels()
    private lateinit var adapter: TagListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TagListAdapter()
        view.findViewById<RecyclerView>(R.id.recyclerTags).apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = this@TagListFragment.adapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.allTags.collectLatest { tags ->
                    Log.d("TAG_DEBUG", "Collected tags: $tags")
                    adapter.submitList(tags)
                }
            }
        }
    }
}