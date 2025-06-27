package com.mw.churchattendance.ui.taglist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mw.churchattendance.R
import com.mw.churchattendance.data.local.entity.tags.NfcTag

class TagListAdapter : ListAdapter<NfcTag, TagListAdapter.TagViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tag, parent, false)
        return TagViewHolder(view)
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TagViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(tag: NfcTag) {
            itemView.findViewById<TextView>(R.id.tvTagId).text = tag.tagId
            itemView.findViewById<TextView>(R.id.tvStatus).text =
                if (tag.isCheckedIn) "Checked In" else "Checked Out"
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<NfcTag>() {
        override fun areItemsTheSame(old: NfcTag, new: NfcTag) = old.tagId == new.tagId
        override fun areContentsTheSame(old: NfcTag, new: NfcTag) = old == new
    }
}