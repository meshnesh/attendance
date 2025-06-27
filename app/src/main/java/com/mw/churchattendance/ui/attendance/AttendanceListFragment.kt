package com.mw.churchattendance.ui.attendance

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.mw.churchattendance.R.dimen
import com.mw.churchattendance.R.layout
import com.mw.churchattendance.base.BaseFragment
import com.mw.churchattendance.databinding.FragmentAttendanceListBinding
import com.mw.churchattendance.util.recyclerview.GridSpacingItemDecoration
import com.mw.churchattendance.util.recyclerview.RecyclerViewClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AttendanceListFragment : BaseFragment(), RecyclerViewClickListener {
    private val viewModel: AttendanceViewModel by viewModels()
    private lateinit var binding: FragmentAttendanceListBinding
    private val attendanceAdapter by lazy { AttendanceAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, layout.fragment_attendance_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.attendanceList.apply {
            adapter = attendanceAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
            setHasFixedSize(true)
            addItemDecoration(
                GridSpacingItemDecoration(
                    requireContext(),
                    spanCount = 2,
                    spacing = dimen.keyline_4,
                    includeEdge = true,
                    headerNum = 0
                )
            )
        }

        lifecycleScope.launch {
            viewModel.todaysAttendance.collect { list ->
                Log.d("AttendanceFragment", "Collected list: ${list.size}")
                attendanceAdapter.updateAttendance(list)
            }
        }
    }

    override fun onClick(data: Any?) {
        TODO("Not yet implemented")
    }
}