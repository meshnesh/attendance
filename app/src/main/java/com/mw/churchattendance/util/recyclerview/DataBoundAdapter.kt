package com.mw.churchattendance.util.recyclerview

import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.mw.churchattendance.base.BaseDataBoundAdapter

/**
 * An Adapter implementation that works with a [DataBoundViewHolder].
 *
 * Although this version enforces a single item type, it can easily be extended to support multiple
 * view types.
 *
 * @param <T> The type of the binding class
</T> */
abstract class DataBoundAdapter<T : ViewDataBinding>
/**
 * Creates a DataBoundAdapter with the given item layout
 *
 * @param mLayoutId The layout to be used for items. It must use body binding.
 */
    (@param:LayoutRes @field:LayoutRes private val mLayoutId: Int) : BaseDataBoundAdapter<T>() {

    override fun getItemLayoutId(position: Int): Int {
        return mLayoutId
    }
}
