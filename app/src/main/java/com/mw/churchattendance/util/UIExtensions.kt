package com.mw.churchattendance.util

import android.app.Activity
import android.app.Dialog
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import com.mw.churchattendance.ui.dialog.AppDialogHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@BindingAdapter("isScrollable")
fun setScrollable(appBarLayout: AppBarLayout, scrollable: Boolean) {
    val layoutParams = appBarLayout.layoutParams as CoordinatorLayout.LayoutParams
    val behavior = (layoutParams.behavior as? AppBarLayout.Behavior) ?: AppBarLayout.Behavior()
    behavior.setDragCallback(object : AppBarLayout.Behavior.DragCallback() {
        override fun canDrag(appBarLayout: AppBarLayout): Boolean = scrollable
    })
    layoutParams.behavior = behavior
}

/*
 * Message Dialog
 */
fun Activity.messageDialog(func: AppDialogHelper.() -> Unit): AlertDialog =
    AppDialogHelper(this).apply {
        func()
    }.create()

fun Fragment.messageDialog(func: AppDialogHelper.() -> Unit): AlertDialog =
    AppDialogHelper(requireActivity()).apply {
        func()
    }.create()

/**
 * Toast extension
 * */
fun Activity.showToast(message: String) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Fragment.showToast(message: String) =
    Toast.makeText(this.requireContext(), message, Toast.LENGTH_SHORT).show()

fun Fragment.showToast(message: String, duration: Int) =
    Toast.makeText(this.requireContext(), message, duration).show()

/**
 * Snackbar extension
 * */
inline fun View.snack(
    @StringRes messageRes: Int,
    length: Int = Snackbar.LENGTH_LONG,
    f: Snackbar.() -> Unit
) {
    snack(resources.getString(messageRes), length, f)
}

inline fun View.snack(message: String, length: Int = Snackbar.LENGTH_LONG, f: Snackbar.() -> Unit) {
    val snack = Snackbar.make(this, message, length)
    snack.f()
    snack.show()
}

fun Snackbar.action(@StringRes actionRes: Int, color: Int? = null, listener: (View) -> Unit) {
    action(view.resources.getString(actionRes), color, listener)
}

fun Snackbar.action(action: String, color: Int? = null, listener: (View) -> Unit) {
    setAction(action, listener)
    color?.let { setActionTextColor(ContextCompat.getColor(context, color)) }
}

/**
 * Auto dismiss dialog after X seconds
 * @param timeMillis [Time] in Milliseconds
 * */
fun Dialog?.autoDismiss(owner: LifecycleOwner, timeMillis: Long) {
    owner.lifecycleScope.launch {
        delay(timeMillis)
        if (this@autoDismiss?.isShowing == true) {
            this@autoDismiss.dismiss()
        }
    }
}


fun Fragment.getColorFromAttr(@AttrRes attrRes: Int): Int {
    val typedValue = TypedValue()
    requireContext().theme.resolveAttribute(attrRes, typedValue, true)
    return typedValue.data
}

@BindingAdapter("formattedTimestamp")
fun TextView.setFormattedTimestamp(timestamp: Long?) {
    if (timestamp != null && timestamp > 0) {
        val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        text = "Last Checked In: ${sdf.format(Date(timestamp))}"
    } else {
        text = "Last Checked In: Never"
    }
}
