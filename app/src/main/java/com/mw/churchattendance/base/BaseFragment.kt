package com.mw.churchattendance.base

import android.text.Spannable
import android.view.Gravity
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.mw.churchattendance.R
import com.mw.churchattendance.util.messageDialog


abstract class BaseFragment: Fragment() {


    private var messageDialog: AlertDialog? = null
    var dialog: AlertDialog? = null

    private fun showMessageDialog(
        messageText: String,
        positiveText: String,
        iconId: Int?,
        callback: () -> Unit
    ) {
        if (messageDialog != null && messageDialog!!.isShowing) messageDialog?.dismiss()

        messageDialog = messageDialog {

            if (iconId != null) drawableIcon = iconId

            message.text = messageText
            positiveButton.text = positiveText

            positiveButtonClickListener {
                dialog?.dismiss()
                callback()
            }

            cancelable = false
        }
        messageDialog?.show()
    }

    fun showSuccessDialog(
        spannable: Spannable,
        title: String,
        hasIcon: Boolean,
        callback: () -> Unit
    ) {
        messageDialog {
            if (hasIcon)
                lottieIcon = R.raw.success_saf
            updateDialog(title, spannable, resources.getString(R.string.ok), "")
            this.title.gravity = Gravity.CENTER
            positiveButtonClickListener {
                callback()
            }
            cancelable = false
        }.show()
    }
}