package com.mw.churchattendance.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AlertDialog

abstract class BaseDialogHelper() {

    abstract val dialogView: View
    abstract val builder: AlertDialog.Builder

    //  required bools
    open var cancelable: Boolean = true
    open var isBackGroundTransparent: Boolean = true

    open var autoDismiss = false
    open var autoDismissAfter = 7000L // time in milliseconds

    //  dialog
    open var dialog: AlertDialog? = null

    //  dialog create
    open fun create(): AlertDialog {
        if (dialog != null) {
            dialog = null
        }

        dialog = builder.setCancelable(cancelable).create()

        //  very much needed for customised dialogs
        if (isBackGroundTransparent) {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        if (autoDismiss) autoDismiss()
        return dialog!!
    }

    /**
     * auto dismiss dialog
     * */
    private fun autoDismiss() {
        val handler = Handler(Looper.getMainLooper())
        val runnable = Runnable {
            if (dialog != null && dialog?.isShowing == true) {
                dialog?.dismiss()
            }
        }
        onDismissListener { handler.removeCallbacks(runnable) }
        handler.postDelayed(runnable, autoDismissAfter)
    }

    //  cancel listener
    open fun onCancelListener(func: () -> Unit): AlertDialog.Builder? =
        builder.setOnCancelListener {
            func()
        }

    // dismiss listener
    open fun onDismissListener(func: () -> Unit): AlertDialog.Builder? =
        builder.setOnDismissListener {
            func()
        }

}