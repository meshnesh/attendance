package com.mw.churchattendance.ui.attendance

import android.annotation.SuppressLint
import com.mw.churchattendance.R
import com.mw.churchattendance.data.local.entity.attendance.Attendance
import com.mw.churchattendance.databinding.ItemAttendanceBinding
import com.mw.churchattendance.util.recyclerview.DataBoundAdapter
import com.mw.churchattendance.util.recyclerview.DataBoundViewHolder
import com.mw.churchattendance.util.recyclerview.RecyclerViewClickListener

class AttendanceAdapter(
    private val clickListener: RecyclerViewClickListener
) : DataBoundAdapter<ItemAttendanceBinding>(R.layout.item_attendance) {

    private var items: List<Attendance> = emptyList()

    override fun bindItem(
        holder: DataBoundViewHolder<ItemAttendanceBinding>?,
        position: Int,
        payloads: MutableList<Any>?
    ) {
        val item = items[position]
        holder?.binding?.attendance = item
        holder?.binding?.root?.setOnClickListener {
            clickListener.onClick(item)
        }
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateAttendance(favorites: List<Attendance>) {
        items = favorites
        notifyDataSetChanged()
    }

}